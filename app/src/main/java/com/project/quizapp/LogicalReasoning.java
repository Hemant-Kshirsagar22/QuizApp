package com.project.quizapp;

import android.os.Bundle;
import android.widget.Adapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LogicalReasoning extends AppCompatActivity {


    AdapterForCardViewTestList adapter;
    RecyclerView recyclerView;
    ArrayList<String> item;
    ArrayList<String> Descitem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logical_reasoning);
        recyclerView = findViewById(R.id.recycleView);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterForCardViewTestList(this, item,Descitem);
        recyclerView.setAdapter(adapter);
    }
}