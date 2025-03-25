package com.anhinla.mobileasm11;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WheelAdapter extends RecyclerView.Adapter<WheelAdapter.ViewHolder> {
    private final List<String> items;
    private final Context context;
    // Use a Set to track selected positions (allows multiple selection)
    private final Set<Integer> selectedPositions = new HashSet<>();

    public WheelAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public WheelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wheel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WheelAdapter.ViewHolder holder, int position) {
        holder.textView.setText(items.get(position));

        CardView card = (CardView) holder.itemView;
        if (selectedPositions.contains(position)) {
            card.setCardBackgroundColor(Color.parseColor("#79ADFF"));
            holder.textView.setTextColor(Color.WHITE);
        } else {
            card.setCardBackgroundColor(Color.WHITE);
            holder.textView.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position);
                Log.d("WheelAdapter", "Item deselected at position: " + position);
            } else {
                selectedPositions.add(position);
                Log.d("WheelAdapter", "Item selected at position: " + position);
            }
            notifyItemChanged(position);
        });

        float centerPosition = items.size() / 2f;
        float distance = Math.abs(position - centerPosition);
        float alpha = 1.0f - (distance / centerPosition);
        holder.itemView.setAlpha(Math.max(alpha, 0.5f)); // Minimum opacity of 50%
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}