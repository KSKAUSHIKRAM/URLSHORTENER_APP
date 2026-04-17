package com.epicgamers.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    TextView tvHistory;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvHistory = findViewById(R.id.tvHistory);
        dbHelper = new DatabaseHelper(this);

        loadHistory();
    }

    private void loadHistory() {
        Cursor cursor = dbHelper.getAllUrls();

        StringBuilder data = new StringBuilder();

        while (cursor.moveToNext()) {
            String longUrl = cursor.getString(1);
            String shortUrl = cursor.getString(2);

            data.append("Long: ").append(longUrl).append("\n");
            data.append("Short: ").append(shortUrl).append("\n\n");
        }

        tvHistory.setText(data.toString());
        tvHistory.setAutoLinkMask(android.text.util.Linkify.WEB_URLS);
        tvHistory.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
}