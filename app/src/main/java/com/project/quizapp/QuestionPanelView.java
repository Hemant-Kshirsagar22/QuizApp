package com.project.quizapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.quizapp.databinding.ActivityMainBinding;
import com.project.quizapp.databinding.ActivityQuestionPanelViewBinding;

public class QuestionPanelView extends AppCompatActivity {

    private ActivityQuestionPanelViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityQuestionPanelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle option selection
        setupOptionSelection(binding.optionA, "430");
        setupOptionSelection(binding.optionB, "420");
        setupOptionSelection(binding.optionC, "410");
        setupOptionSelection(binding.optionD, "435"); // Correct Answer
    }

    private void setupOptionSelection(LinearLayout option, String answer) {
        option.setOnClickListener(v -> {
            if (answer.equals("435")) {
                Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}