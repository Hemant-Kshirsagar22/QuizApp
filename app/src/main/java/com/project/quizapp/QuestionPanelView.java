package com.project.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import java.util.Objects;

public class QuestionPanelView extends AppCompatActivity {

    private ActivityQuestionPanelViewBinding binding;
    private List<Question> questions = null;

    private RadioGroup radioGroup = null;
    private final String A = "A";
    private final String B = "B";
    private final String C = "C";
    private final String D = "D";


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
                    binding.tvQuestionNumber.append(currentQuetionPosition + 1 + "");

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
//                                Toast.makeText(getApplicationContext(), "A", Toast.LENGTH_SHORT).show();
                                setupOptionSelection(binding.a, A);
                            }
                            else if(binding.b.isChecked())
                            {
//                                Toast.makeText(getApplicationContext(), "B", Toast.LENGTH_SHORT).show();
                                setupOptionSelection(binding.b, B);
                            }
                            else if(binding.c.isChecked())
                            {
//                                Toast.makeText(getApplicationContext(), "C", Toast.LENGTH_SHORT).show();
                                setupOptionSelection(binding.c, C);
                            }
                            else if(binding.d.isChecked())
                            {
//                                Toast.makeText(getApplicationContext(), "D", Toast.LENGTH_SHORT).show();
                                setupOptionSelection(binding.d, D);
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

    private void setupOptionSelection(RadioButton option, String answer) {
        if(Objects.equals(option, binding.a))
        {
            option.setBackgroundResource(R.drawable.option_background_selected);
            binding.b.setBackgroundResource(R.drawable.option_background);
            binding.c.setBackgroundResource(R.drawable.option_background);
            binding.d.setBackgroundResource(R.drawable.option_background);
        }

        if(Objects.equals(option, binding.b))
        {
            option.setBackgroundResource(R.drawable.option_background_selected);
            binding.a.setBackgroundResource(R.drawable.option_background);
            binding.c.setBackgroundResource(R.drawable.option_background);
            binding.d.setBackgroundResource(R.drawable.option_background);
        }

        if(Objects.equals(option, binding.c))
        {
            option.setBackgroundResource(R.drawable.option_background_selected);
            binding.a.setBackgroundResource(R.drawable.option_background);
            binding.b.setBackgroundResource(R.drawable.option_background);
            binding.d.setBackgroundResource(R.drawable.option_background);
        }

        if(Objects.equals(option, binding.d))
        {
            option.setBackgroundResource(R.drawable.option_background_selected);
            binding.a.setBackgroundResource(R.drawable.option_background);
            binding.b.setBackgroundResource(R.drawable.option_background);
            binding.c.setBackgroundResource(R.drawable.option_background);
        }



        if (answer.equals(questions.get(currentQuetionPosition).getAnswer())) {
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
    }
}