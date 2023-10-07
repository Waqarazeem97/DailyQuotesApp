package com.waqar.dailyquotesapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RequestManager {
    private final Context context;
    private final Retrofit retrofit;

    public RequestManager(Context context) {
        this.context = context;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://type.fit/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void GetAllQuotes(final QuoteResponseListener listener) {
        CallQuotes callQuotes = retrofit.create(CallQuotes.class);
        Call<List<QuoteResponse>> call = callQuotes.callQuotes();
        call.enqueue(new Callback<List<QuoteResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuoteResponse>> call, @NonNull Response<List<QuoteResponse>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Request not successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<QuoteResponse> quotes = response.body();
                if (quotes != null) {
                    Log.d("RequestManager", "Received " + quotes.size() + " quotes");
                    listener.didFetch(quotes, response.message());
                } else {
                    Log.e("RequestManager", "Response body is null");
                    listener.didError("Response body is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<QuoteResponse>> call, @NonNull Throwable t) {
                Log.e("RequestManager", "Request failed: " + t.getMessage());
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallQuotes {
        @GET("api/quotes")
        Call<List<QuoteResponse>> callQuotes();
    }
}