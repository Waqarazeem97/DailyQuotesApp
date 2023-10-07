package com.waqar.dailyquotesapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class QuoteRecyclerAdapter extends RecyclerView.Adapter<QuoteViewHolder>{
    Context context;
    List<QuoteResponse> list;
    FavoritesManager favoritesManager;
//    CopyListener listener;
//    public QuoteRecyclerAdapter(Context context, List<QuoteResponse> list, CopyListener listener) {
public QuoteRecyclerAdapter(Context context, List<QuoteResponse> list, FavoritesManager favoritesManager) {
    this.context = context;
    this.list = list;
    this.favoritesManager = favoritesManager;
//        this.listener = listener;
    }
    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuoteViewHolder(LayoutInflater.from(context).inflate(R.layout.list_quote, parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        holder.textView_quote.setText(list.get(position).getText());
        holder.textView_author.setText(list.get(position).getAuthor());
//        holder.button_copy.setOnClickListener(view -> listener.onCopyClicked(list.get(holder.getAdapterPosition()).getText()));
        if (isQuoteFavorite(list.get(position))) {
            holder.imageView_favorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.imageView_favorite.setImageResource(R.drawable.ic_favorite_outline);
        }
        holder.imageView_favorite.setOnClickListener(v -> {
            QuoteResponse quote = list.get(position);
            if (isQuoteFavorite(quote)) {
                removeQuoteFromFavorites(quote);
                holder.imageView_favorite.setImageResource(R.drawable.ic_favorite_outline);
            } else {
                addQuoteToFavorites(quote);
                holder.imageView_favorite.setImageResource(R.drawable.ic_favorite_filled);
            }
        });
        holder.button_share.setOnClickListener(v -> {
            String quoteText = list.get(position).getText();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, quoteText);
            v.getContext().startActivity(Intent.createChooser(shareIntent, "Share Quote"));
        });
    }
    private boolean isQuoteFavorite(QuoteResponse quote) {
        return favoritesManager.isFavorite(quote);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void addQuoteToFavorites(QuoteResponse quote) {
        favoritesManager.open(); // Open the database here if it's not open already
        long result = favoritesManager.addFavorite(quote);
        if (result != -1) {
            notifyDataSetChanged(); // Update the RecyclerView
            // Provide UI feedback here if needed (e.g., toast message)
        }
        favoritesManager.close(); // Close the database after adding the favorite
    }
    @SuppressLint("NotifyDataSetChanged")
    private void removeQuoteFromFavorites(QuoteResponse quote) {
        favoritesManager.removeFavorite(quote);
        notifyDataSetChanged(); // Update the RecyclerView
        // Provide UI feedback here if needed (e.g., toast message)
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
class QuoteViewHolder extends RecyclerView.ViewHolder{
    TextView textView_quote, textView_author;
//    Button button_copy;
    Button button_share;
    ImageView imageView_favorite;
    public QuoteViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_quote = itemView.findViewById(R.id.textView_quote);
        textView_author = itemView.findViewById(R.id.textView_author);
//        button_copy = itemView.findViewById(R.id.button_copy);
        button_share = itemView.findViewById(R.id.button_share);
        imageView_favorite = itemView.findViewById(R.id.imageView_favorite);
    }
}