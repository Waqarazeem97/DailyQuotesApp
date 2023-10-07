package com.waqar.dailyquotesapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class FavoriteQuotesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorite_quotes.db";
    private static final int DATABASE_VERSION = 1;
    // Define your table and column names
    public static final String TABLE_FAVORITE_QUOTES = "favorite_quotes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUOTE_TEXT = "quote_text";
    public static final String COLUMN_AUTHOR = "author";
    private static final String DATABASE_CREATE =
            "create table " + TABLE_FAVORITE_QUOTES + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_QUOTE_TEXT + " text not null, " +
                    COLUMN_AUTHOR + " text not null);";

    public FavoriteQuotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}