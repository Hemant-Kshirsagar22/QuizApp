package com.project.quizapp;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.project.quizapp.databinding.ActivityCompanyWiseBinding;

public class CompanyWise extends GlobalDrawerLayoutAndBottomNevigation {

    private AppBarConfiguration appBarConfiguration;
private ActivityCompanyWiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_company_wise, findViewById(R.id.content_frame));



    }
}