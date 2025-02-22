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

public class QuestionBaseCategoryRecyclerViewAdapter extends RecyclerView.Adapter<QuestionBaseCategoryRecyclerViewAdapter.QuestionBaseCategoryRecyclerViewHolder> {
    private Context context;
    private List<QuestionCategory> baseCategoryList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(QuestionCategory baseCategory);
    }

    public QuestionBaseCategoryRecyclerViewAdapter(Context context, List<QuestionCategory> baseCategoryList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.baseCategoryList = baseCategoryList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public QuestionBaseCategoryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_cardview_for_testlist, parent, false);
        return new QuestionBaseCategoryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionBaseCategoryRecyclerViewHolder holder, int position) {

        // Clear
        holder.textTitle.setText("");
        holder.textDescriptor.setText("");
        holder.imageView.setImageDrawable(null);

        QuestionCategory questionCategory = baseCategoryList.get(position);
        holder.textTitle.setText(questionCategory.getBaseCategory());
        holder.textDescriptor.setText( "Number Of Tests : "+ questionCategory.getSubCategory().size());
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.foreground);
        holder.imageView.setImageDrawable(drawable);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(questionCategory));
    }

    @Override
    public int getItemCount() {
        return baseCategoryList.size();
    }

    public class QuestionBaseCategoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle,textDescriptor;
        ImageView imageView;
        public QuestionBaseCategoryRecyclerViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescriptor = itemView.findViewById(R.id.textDesc);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}