package com.project.quizapp;

import android.os.Bundle;

import androidx.navigation.ui.AppBarConfiguration;

import com.project.quizapp.databinding.ActivityTopicWiseBinding;

public class TopicWise extends GlobalDrawerLayoutAndBottomNavigation {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTopicWiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_topic_wise, findViewById(R.id.content_frame));
    }
}