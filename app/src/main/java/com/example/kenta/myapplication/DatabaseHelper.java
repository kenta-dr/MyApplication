package com.example.kenta.myapplication;

/**
 * Created by kenta on 2016/12/12.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "aaaa0000.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成SQL
        String sql = "CREATE TABLE TweetList ("
                + " tweet_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ",user_id TEXT"
                + ",user_name TEXT"
                + ",tweet TEXT"
                + ",via TEXT"
                + ",time TEXT"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}