package com.project.quizapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.quizapp.databinding.ActivityTopicWiseBinding;

public class TopicWise extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTopicWiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTopicWiseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}