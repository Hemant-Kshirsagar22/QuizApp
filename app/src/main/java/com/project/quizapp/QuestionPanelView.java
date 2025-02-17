package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.databinding.ActivityQuestionPanelViewBinding;
import com.project.quizapp.databinding.OnSubmitDailogLayoutBinding;

import java.util.HashMap;
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

    private HashMap<Integer,String> answerList = null;

    private int currentQuetionPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityQuestionPanelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        radioGroup = (RadioGroup) binding.radioGroup;
        answerList = new HashMap<Integer,String>();

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionPanelView.this);
                //Get binding with on_submit_dialog
                OnSubmitDailogLayoutBinding dialogBinding = OnSubmitDailogLayoutBinding.inflate(LayoutInflater.from(QuestionPanelView.this));
                builder.setView(dialogBinding.getRoot());
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

                dialogBinding.btnResume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialogBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(QuestionPanelView.this, Dashboard.class);
                         startActivity(intent);
                    }
                });

            }
        });
        FirebaseDBHelper.getQuestionByCategory("Logical-Reasoning/Analogies", new FirebaseDBHelper.QuestionQueryCallback() {
            @Override
            public void onSuccess(List<Question> question) {
                questions = question;
                changeQuestion();
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(binding.a.isChecked())
                {
                    answerList.put(currentQuetionPosition, A);
                    setupOptionSelection(binding.a, A);
                }
                else if(binding.b.isChecked())
                {
                    answerList.put(currentQuetionPosition, B);
                    setupOptionSelection(binding.b, B);
                }
                else if(binding.c.isChecked())
                {
                    answerList.put(currentQuetionPosition, C);
                    setupOptionSelection(binding.c, C);
                }
                else if(binding.d.isChecked())
                {
                    answerList.put(currentQuetionPosition, D);
                    setupOptionSelection(binding.d, D);
                }

            }
        });

        binding.clearResponse.setOnClickListener(view -> {
            clearOptions();
            answerList.put(currentQuetionPosition, null);
            Log.d("ANS",answerList.toString());
        });

        binding.next.setOnClickListener(view -> {
            currentQuetionPosition++;
            if(currentQuetionPosition == questions.size())
            {
                currentQuetionPosition = 0;
            }
            changeQuestion();
        });

        binding.Previous.setOnClickListener(view -> {
            currentQuetionPosition--;
            if(currentQuetionPosition < 0)
            {
                currentQuetionPosition = questions.size() - 1;
            }
            changeQuestion();
        });

        // Handle option selection
//        setupOptionSelection(binding.optionA, "430");
//        setupOptionSelection(binding.optionB, "420");
//        setupOptionSelection(binding.optionC, "410");
//        setupOptionSelection(binding.optionD, "435"); // Correct Answer
    }

    private void changeQuestion()
    {
        if(questions != null)
        {
            binding.tvQuestion.setText(questions.get(currentQuetionPosition).getQuestion());
            binding.tvQuestionNumber.setText("Question : " + (currentQuetionPosition + 1));

            List<String> options = questions.get(currentQuetionPosition).getOptions();

            binding.a.setText(A + " " + options.get(0));
            binding.b.setText(B + " " + options.get(1));
            binding.c.setText(C + " " + options.get(2));
            binding.d.setText(D + " " + options.get(3));

            clearOptions();

            String ans = null;
            if((ans = answerList.get(currentQuetionPosition)) != null)
            {
                switch (ans)
                {
                    case A:
                        binding.a.setBackgroundResource(R.drawable.option_background_selected);
                        break;
                    case B:
                        binding.b.setBackgroundResource(R.drawable.option_background_selected);
                        break;
                    case C:
                        binding.c.setBackgroundResource(R.drawable.option_background_selected);
                        break;
                    case D:
                        binding.d.setBackgroundResource(R.drawable.option_background_selected);
                        break;
                }
            }
        }
    }
    private void setupOptionSelection(RadioButton option, String answer) {
        Log.d("ANS",answerList.toString());
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
        }
    }

    private void clearOptions()
    {
        binding.a.setBackgroundResource(R.drawable.option_background);
        binding.b.setBackgroundResource(R.drawable.option_background);
        binding.c.setBackgroundResource(R.drawable.option_background);
        binding.d.setBackgroundResource(R.drawable.option_background);

    }
}
