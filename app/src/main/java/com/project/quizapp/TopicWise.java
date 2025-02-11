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

public class TopicWise extends GlobalDrawerLayoutAndBottomNevigation {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTopicWiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_topic_wise, findViewById(R.id.content_frame));
    }
}