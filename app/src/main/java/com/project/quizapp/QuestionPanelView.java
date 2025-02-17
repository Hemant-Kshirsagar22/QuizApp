package com.project.quizapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.databinding.ActivityMainBinding;
import com.project.quizapp.databinding.ActivityQuestionPanelViewBinding;

import java.util.ArrayList;
import java.util.List;

public class QuestionPanelView extends AppCompatActivity {

    private ActivityQuestionPanelViewBinding binding;
    private List<Question> questions = null;

    private RadioGroup radioGroup = null;

    private int currentQuetionPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityQuestionPanelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        radioGroup = (RadioGroup) binding.radioGroup;

        FirebaseDBHelper.getQuestionByCategory("Logical-Reasoning/Analogies", new FirebaseDBHelper.QuestionQueryCallback() {
            @Override
            public void onSuccess(List<Question> question) {
                questions = question;
                Toast.makeText(getApplicationContext(), "Fetch Success", Toast.LENGTH_SHORT).show();
                if(questions != null)
                {
                    binding.tvQuestion.setText(questions.get(currentQuetionPosition).getQuestion());
                    binding.tvQuestionNumber.setText(currentQuetionPosition + 1 + "");

                    List options = questions.get(currentQuetionPosition).getOptions();

                    binding.a.append(options.get(0).toString());
                    binding.b.append(options.get(1).toString());
                    binding.c.append(options.get(2).toString());
                    binding.d.append(options.get(3).toString());


                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            if(binding.a.isChecked())
                            {
                                Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();
                            }
                            else if(binding.b.isChecked())
                            {
                                Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_SHORT).show();
                            }
                            else if(binding.c.isChecked())
                            {
                                Toast.makeText(getApplicationContext(), "C", Toast.LENGTH_SHORT).show();
                            }
                            else if(binding.d.isChecked())
                            {
                                Toast.makeText(getApplicationContext(), "D", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });


        // Handle option selection
//        setupOptionSelection(binding.optionA, "430");
//        setupOptionSelection(binding.optionB, "420");
//        setupOptionSelection(binding.optionC, "410");
//        setupOptionSelection(binding.optionD, "435"); // Correct Answer
    }

    private void setupOptionSelection(LinearLayout option, String answer) {
        option.setOnClickListener(v -> {
            option.setBackgroundResource(R.drawable.option_background_selected);
            if (answer.equals("435")) {
                Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}