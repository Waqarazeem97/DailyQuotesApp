package com.waqar.dailyquotesapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler_home;
    QuoteRecyclerAdapter adapter;
    ProgressDialog dialog;
    Button button_refresh;
    Button button_favorites;
    List<QuoteResponse> quoteList;
    FavoritesManager favoritesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_home = findViewById(R.id.recycler_home);
        button_refresh = findViewById(R.id.button_refresh);
        button_favorites = findViewById(R.id.button_favorites);

        quoteList = new ArrayList<>();
        favoritesManager = new FavoritesManager(this);
        favoritesManager.open();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        setupRecyclerView();
        fetchQuotes();

        button_refresh.setOnClickListener(v -> fetchQuotes());
        button_favorites.setOnClickListener(v -> showFavoriteQuotes());
    }

    private void setupRecyclerView() {
        recycler_home.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuoteRecyclerAdapter(this, quoteList, favoritesManager);
        recycler_home.setAdapter(adapter);
    }
    private void fetchQuotes() {
        dialog.show();
        RequestManager manager = new RequestManager(this);
        manager.GetAllQuotes(new QuoteResponseListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void didFetch(List<QuoteResponse> responses, String message) {
                dialog.dismiss();
                quoteList.clear();
                quoteList.addAll(responses);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void didError(String message) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void showFavoriteQuotes() {
        List<QuoteResponse> favoriteQuotes = favoritesManager.getFavorites();
        quoteList.clear();
        quoteList.addAll(favoriteQuotes);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritesManager.close();
    }
}
