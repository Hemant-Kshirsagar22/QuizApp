package com.project.quizapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.project.quizapp.databinding.ActivityDashboardBinding;
import com.project.quizapp.databinding.PerformanceActivityBinding;

public class Performance extends GlobalDrawerLayoutAndBottomNavigation {

    private static PerformanceActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.performance_activity, findViewById(R.id.content_frame));

        binding = PerformanceActivityBinding.bind(view);

    }
}
