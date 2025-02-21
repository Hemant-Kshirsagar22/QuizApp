
package com.project.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForCardViewTestList extends RecyclerView.Adapter<AdapterForCardViewTestList.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<String> Descdata;

    private List<String> Description;
    AdapterForCardViewTestList(Context context,List<String> data,List<String> Descdata)
    {

        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.Descdata = Descdata;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.custom_cardview_for_testlist,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data here to the textview received

        String title = data.get(position);
        holder.textTitle.setText(title);

        //same for Descriptor
        String desc = Descdata.get(position);
        holder.textDescriptor.setText(desc);

    }

    @Override
    public int getItemCount() {
        return data.size(); // Return actual list size
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle,textDescriptor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescriptor = itemView.findViewById(R.id.textDesc);

        }
    }
}
