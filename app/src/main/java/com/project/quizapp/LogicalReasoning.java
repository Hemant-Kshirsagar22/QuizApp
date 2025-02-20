package com.project.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.quizapp.databinding.ActivityDashboardBinding;
import com.project.quizapp.databinding.ActivityLogicalReasoningBinding;

import java.util.ArrayList;

public class LogicalReasoning extends GlobalDrawerLayoutAndBottomNavigation {


    AdapterForCardViewTestList adapter;
    RecyclerView recyclerView;
    ArrayList<String> item;
    ArrayList<String> Descitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    ActivityLogicalReasoningBinding binding;
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_logical_reasoning, findViewById(R.id.content_frame));

        binding = ActivityLogicalReasoningBinding.bind(view);
        //recyclerView = findViewById(R.id.recycleView);

        item = new ArrayList<>();
        item.add("Test1");
        item.add("Test2");
        item.add("Test3");
        item.add("Test4");
        item.add("Test5");
        item.add("Test6");
        item.add("Test7");
        item.add("Test8");

        Descitem = new ArrayList<>();
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        Descitem.add("Duration:20min   Quetion:15    15M");
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterForCardViewTestList(this, item,Descitem);
        binding.recycleView.setAdapter(adapter);
    }
}