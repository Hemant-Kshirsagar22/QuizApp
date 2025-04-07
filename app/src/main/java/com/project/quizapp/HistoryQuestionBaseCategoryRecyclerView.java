package com.project.quizapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.quizapp.database.entities.QuestionCategory;

import java.util.List;
import java.util.Map;

public class HistoryQuestionBaseCategoryRecyclerView extends RecyclerView.Adapter<HistoryQuestionBaseCategoryRecyclerView.HistoryQuestionBaseCategoryRecyclerViewHolder> {
    private Context context;
    private List<String> baseCategoryList;
    private Map<String, Object> marksMap;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String baseCategory);
    }

    public HistoryQuestionBaseCategoryRecyclerView(Context context, List<String> baseCategoryList, Map<String, Object> marksMap, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.baseCategoryList = baseCategoryList;
        this.marksMap = marksMap;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public HistoryQuestionBaseCategoryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_cardview_for_history, parent, false);
        return new HistoryQuestionBaseCategoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryQuestionBaseCategoryRecyclerViewHolder holder, int position) {

        // Clear
        holder.textTitle.setText("");
        holder.textDescriptor.setText("");
        holder.imageView.setImageDrawable(null);

        String questionCategory = baseCategoryList.get(position);
        holder.textTitle.setText(questionCategory);

        int cnt = 0;
        for(Map.Entry<String, Object> entry : marksMap.entrySet())
        {
            String[] key = entry.getKey().split("/");

            if(key[1].equals(questionCategory))
            {
                cnt++;
            }

        }

        holder.textDescriptor.setText("Number Of Tests Attempted : " + cnt);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.foreground);
        holder.imageView.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(questionCategory));
    }

    @Override
    public int getItemCount() {
        return baseCategoryList.size();
    }

    public static class HistoryQuestionBaseCategoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle,textDescriptor;
        ImageView imageView;
        public HistoryQuestionBaseCategoryRecyclerViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescriptor = itemView.findViewById(R.id.textDesc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}