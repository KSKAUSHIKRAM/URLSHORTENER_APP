package com.epicgamers.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "url_db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "urls";
    public static final String COL_ID = "id";
    public static final String COL_LONG = "long_url";
    public static final String COL_SHORT = "short_url";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LONG + " TEXT, " +
                COL_SHORT + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertUrl(String longUrl, String shortUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_LONG, longUrl);
        values.put(COL_SHORT, shortUrl);

        db.insert(TABLE_NAME, null, values);
    }
    public Cursor getAllUrls() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
    }
}