package com.project.quizapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class HistoryTestMarksRecyclerView extends RecyclerView.Adapter<HistoryTestMarksRecyclerView.HistoryTestMarksRecyclerViewHolder>{
    private OnItemClickListener onItemClickListener;
    private Context context;
    private List<String> subCategory;
    private Map<String, Object> marksMap;

    public interface OnItemClickListener {
        void onItemClick();
    }
    public HistoryTestMarksRecyclerView(Context context, List<String> subCategory, Map<String, Object> marksMap, OnItemClickListener onItemClickListener)
    {
        this.context = context;
        this.subCategory = subCategory;
        this.marksMap = marksMap;
        this.onItemClickListener = onItemClickListener;

        Log.d("ADAPTER_HISTORY", subCategory.toString());
    }

    @NonNull
    @Override
    public HistoryTestMarksRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_cardview_for_history, parent, false);
        return new HistoryTestMarksRecyclerView.HistoryTestMarksRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTestMarksRecyclerViewHolder holder, int position) {
        // Clear
        holder.textTitle.setText("");
        holder.textDescriptor.setText("");
        holder.imageView.setImageDrawable(null);

        holder.textTitle.setText(subCategory.get(position));

        float marks = Float.parseFloat(String.format("%.2f", Float.parseFloat(marksMap.get(subCategory.get(position)).toString())));

        holder.textDescriptor.setText("marks : " + marks);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.foreground);
        holder.imageView.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick());
    }

    @Override
    public int getItemCount() {
        return subCategory.size();
    }

    public static class HistoryTestMarksRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle,textDescriptor;
        ImageView imageView;
        public HistoryTestMarksRecyclerViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescriptor = itemView.findViewById(R.id.textDesc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
