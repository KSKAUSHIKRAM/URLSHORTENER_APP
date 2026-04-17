package com.epicgamers.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etUrl;
    Button btnShorten, btnHistory;
    TextView tvResult;

    DatabaseHelper dbHelper; // 🔥 database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = findViewById(R.id.etUrl);
        btnShorten = findViewById(R.id.btnShorten);
        btnHistory = findViewById(R.id.btnHistory);
        tvResult = findViewById(R.id.tvResult);

        dbHelper = new DatabaseHelper(this); // ✅ init DB

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 🔥 SHORTEN BUTTON
        btnShorten.setOnClickListener(v -> {

            String url = etUrl.getText().toString();

            if (url.isEmpty()) {
                tvResult.setText("Enter a valid URL");
                return;
            }

            UrlRequest request = new UrlRequest(url);

            apiService.shortenUrl(request).enqueue(new Callback<UrlResponse>() {
                @Override
                public void onResponse(Call<UrlResponse> call, Response<UrlResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        String shortCode = response.body().getShortUrl();
                        String fullShortUrl = "http://10.0.2.2:8080/" + shortCode;

                        // ✅ show result
                        tvResult.setText(fullShortUrl);
                        tvResult.setAutoLinkMask(android.text.util.Linkify.WEB_URLS);
                        tvResult.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
                        // 🔥 SAVE TO DATABASE
                        dbHelper.insertUrl(url, fullShortUrl);

                    } else {
                        tvResult.setText("Failed to shorten URL");
                    }
                }

                @Override
                public void onFailure(Call<UrlResponse> call, Throwable t) {
                    tvResult.setText("Error: " + t.getMessage());
                }
            });
        });

        // 🔥 HISTORY BUTTON
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    // 🔥 METHOD TO DISPLAY DATA
    private void showHistory() {
        Cursor cursor = dbHelper.getAllUrls();

        if (cursor.getCount() == 0) {
            tvResult.setText("No history found");
            return;
        }

        StringBuilder data = new StringBuilder();

        while (cursor.moveToNext()) {
            String longUrl = cursor.getString(1);
            String shortUrl = cursor.getString(2);

            data.append("Long: ").append(longUrl).append("\n");
            data.append("Short: ").append(shortUrl).append("\n\n");
        }

        tvResult.setText(data.toString());


    }
}