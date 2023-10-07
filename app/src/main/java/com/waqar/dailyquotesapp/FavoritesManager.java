package com.waqar.dailyquotesapp;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
public class FavoritesManager {
    private SQLiteDatabase database;
    private final FavoriteQuotesDbHelper dbHelper;
    private static FavoritesManager instance;
    public FavoritesManager(Context context) {
        dbHelper = new FavoriteQuotesDbHelper(context);
        open();
    }
    public static FavoritesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context);
        }
        return instance;
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
    public long addFavorite(QuoteResponse quote) {
        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesDbHelper.COLUMN_QUOTE_TEXT, quote.getText());
        values.put(FavoriteQuotesDbHelper.COLUMN_AUTHOR, quote.getAuthor());
        return database.insert(FavoriteQuotesDbHelper.TABLE_FAVORITE_QUOTES, null, values);
    }
    public void removeFavorite(QuoteResponse quote) {
        database.delete(
                FavoriteQuotesDbHelper.TABLE_FAVORITE_QUOTES,
                FavoriteQuotesDbHelper.COLUMN_QUOTE_TEXT + " = ?",
                new String[]{quote.getText()}
        );
    }
    public boolean isFavorite(QuoteResponse quote) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(
                FavoriteQuotesDbHelper.TABLE_FAVORITE_QUOTES,
                new String[]{FavoriteQuotesDbHelper.COLUMN_QUOTE_TEXT},
                FavoriteQuotesDbHelper.COLUMN_QUOTE_TEXT + " = ?",
                new String[]{quote.getText()},
                null, null, null
        )) {
            return cursor != null && cursor.getCount() > 0;
        }
    }
    public List<QuoteResponse> getFavorites() {
        List<QuoteResponse> favoriteQuotes = new ArrayList<>();
        if (!database.isOpen()) {
            open();
        }
        Cursor cursor = database.query(
                FavoriteQuotesDbHelper.TABLE_FAVORITE_QUOTES,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String quoteText = cursor.getString(cursor.getColumnIndex(FavoriteQuotesDbHelper.COLUMN_QUOTE_TEXT));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(FavoriteQuotesDbHelper.COLUMN_AUTHOR));
                QuoteResponse quote = new QuoteResponse();
                quote.setText(quoteText);
                quote.setAuthor(author);
                favoriteQuotes.add(quote);
            }
            cursor.close();
        }
        return favoriteQuotes;
    }
}