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

public class CompanyWise extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
private ActivityCompanyWiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCompanyWiseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}