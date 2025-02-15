package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.project.quizapp.database.FirebaseDBHelper;

import com.project.quizapp.database.entities.Question;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.databinding.ActivityDashboardBinding;
import com.project.quizapp.databinding.ActivityMainBinding;
import com.project.quizapp.databinding.SampleDialogBinding;


import java.util.List;
import java.util.Objects;

public class Dashboard extends GlobalDrawerLayoutAndBottomNevigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         ActivityDashboardBinding binding;
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_dashboard, findViewById(R.id.content_frame));

        binding = ActivityDashboardBinding.bind(view);

        //Get card view
//        CardView getStartCard = findViewById(R.id.get_start);
        binding.getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                //Get binding with sample_dialog
                SampleDialogBinding dialogBinding = SampleDialogBinding.inflate(LayoutInflater.from(Dashboard.this));
                builder.setView(dialogBinding.getRoot());
                AlertDialog dialog = builder.create();
                dialog.show();

                dialogBinding.companyWise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Dashboard.this, "companyWise", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(Dashboard.this, QuestionPanelView.class);
                         startActivity(intent);
                    }
                });

                dialogBinding.topicWise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(Dashboard.this, "TopicWise", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Dashboard.this, TopicWise.class);
                        startActivity(intent);
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }

            }
        });



    }

}