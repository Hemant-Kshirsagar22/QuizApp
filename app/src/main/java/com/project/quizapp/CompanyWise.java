package com.project.quizapp;

import android.os.Bundle;

import androidx.navigation.ui.AppBarConfiguration;

import com.project.quizapp.databinding.ActivityCompanyWiseBinding;

public class CompanyWise extends GlobalDrawerLayoutAndBottomNavigation {

    private AppBarConfiguration appBarConfiguration;
private ActivityCompanyWiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_company_wise, findViewById(R.id.content_frame));



    }
}