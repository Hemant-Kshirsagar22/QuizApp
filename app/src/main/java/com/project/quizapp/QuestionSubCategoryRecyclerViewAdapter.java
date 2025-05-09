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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class QuestionSubCategoryRecyclerViewAdapter extends RecyclerView.Adapter<QuestionSubCategoryRecyclerViewAdapter.QuestionSubCategoryRecyclerViewHolder> {
    private Context context;
    private Map<String,Long> subCategoryMap;

    private QuestionSubCategoryOnClickCallback callback;
    public interface QuestionSubCategoryOnClickCallback
    {
        void OnSubCategorySelected(String subCategory);
    }
    public QuestionSubCategoryRecyclerViewAdapter(Context context, Map<String,Long> subCategoryMap, QuestionSubCategoryOnClickCallback callback) {
        this.context = context;
        this.subCategoryMap = subCategoryMap;
        this.callback = callback;
    }

    @Override
    public QuestionSubCategoryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_cardview_for_testlist, parent, false);
        return new QuestionSubCategoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionSubCategoryRecyclerViewHolder holder, int position) {

        // Clear
        holder.textTitle.setText("");
        holder.textDescriptor.setText("");
        holder.imageView.setImageDrawable(null);

        Object[] keyList = subCategoryMap.keySet().toArray();

        String subCategoryName = (String) keyList[position];
        Long numberOfQuestions = subCategoryMap.get(subCategoryName);

        holder.textTitle.setText(subCategoryName);
        holder.textDescriptor.setText( "Number Of Questions : "+ subCategoryMap.get(subCategoryName) + "\nTime : " + numberOfQuestions+" min");
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.foreground);
        holder.imageView.setImageDrawable(drawable);

        // setting listener to start the test
        holder.itemView.setOnClickListener(view -> {callback.OnSubCategorySelected(subCategoryName);});


    }

    @Override
    public int getItemCount() {
        return subCategoryMap.size();
    }

    public static class QuestionSubCategoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle,textDescriptor;
        ImageView imageView;
        public QuestionSubCategoryRecyclerViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescriptor = itemView.findViewById(R.id.textDesc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
