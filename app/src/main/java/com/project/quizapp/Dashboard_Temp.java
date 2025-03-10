package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.quizapp.databinding.ActivityDashboardBinding;

public class Dashboard_Temp extends GlobalDrawerLayoutAndBottomNavigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         ActivityDashboardBinding binding;
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_dashboard, findViewById(R.id.content_frame));

        binding = ActivityDashboardBinding.bind(view);

        //Get card view
//        CardView getStartCard = findViewById(R.id.get_start);
        binding.CompanyWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard_Temp.this, "companyWise", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard_Temp.this, CompanyWise.class);
                startActivity(intent);

//                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
//                //Get binding with sample_dialog
//                SampleDialogBinding dialogBinding = SampleDialogBinding.inflate(LayoutInflater.from(Dashboard.this));
//                builder.setView(dialogBinding.getRoot());
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//                dialogBinding.companyWise.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(Dashboard.this, "companyWise", Toast.LENGTH_SHORT).show();
//                         Intent intent = new Intent(Dashboard.this, CompanyWise.class);
//                         startActivity(intent);
//                    }
//                });
//
//                dialogBinding.topicWise.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(Dashboard.this, "TopicWise", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(Dashboard.this, TopicWise.class);
//                        startActivity(intent);
//                    }
//                });
//                if (dialog.getWindow() != null) {
//                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                }
//
            }


        });

        binding.QuantitativeAptitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Dashboard_Temp.this, "QuantitativeAptitude", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard_Temp.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

}