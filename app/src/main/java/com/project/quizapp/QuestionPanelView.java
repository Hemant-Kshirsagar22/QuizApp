package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
import java.util.Map;
import java.util.Objects;

public class QuestionPanelView extends AppCompatActivity {

    private ActivityQuestionPanelViewBinding binding;
    private OnSubmitDailogLayoutBinding dialogBinding;
    private List<Question> questions = null;

    private RadioGroup radioGroup = null;
    private final String A = "A";
    private final String B = "B";
    private final String C = "C";
    private final String D = "D";

    private int numberOfAnswerdQuestions = 0;
    private  int numberOfNotAnsweredQuestions = 0;
    private int numberOfReviewLetterQuestions = 0;
    private int numberOfNotVisitedQuestions = 0;
    private Map<Integer,String> answerList = null;
    private Map<Integer,String> answerStatusList = null;
    private Map<Integer,Boolean> questionsVisitedList = null;

    private int currentQuestionPosition = 0;

    // Questions Status
    private static final String QUESTION_MARK_AS_REVIEW = "REVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityQuestionPanelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // submit alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionPanelView.this);
        //Get binding with on_submit_dialog
        dialogBinding = OnSubmitDailogLayoutBinding.inflate(LayoutInflater.from(QuestionPanelView.this));
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        radioGroup = (RadioGroup) binding.radioGroup;

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set the questions status
                setQuestionsStatus();
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
                        float marks = getMarks();
                        IntentManager.toDashboardActivity(getApplicationContext());
                    }
                });

            }
        });
        FirebaseDBHelper.getQuestionByCategory("aptitude/Area", new FirebaseDBHelper.QuestionQueryCallback() {
            @Override
            public void onSuccess(List<Question> question) {
                questions = question;
                answerList = new HashMap<Integer,String>(question.size());
                answerStatusList = new HashMap<Integer,String>(question.size());
                questionsVisitedList = new HashMap<Integer,Boolean>(question.size());
                // set the values to 0
                initializeQuestionsStatus();

                // set fetched question
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
                    answerList.put(currentQuestionPosition, A);
                    selectOption(binding.a, A);
                }
                else if(binding.b.isChecked())
                {
                    answerList.put(currentQuestionPosition, B);
                    selectOption(binding.b, B);
                }
                else if(binding.c.isChecked())
                {
                    answerList.put(currentQuestionPosition, C);
                    selectOption(binding.c, C);
                }
                else if(binding.d.isChecked())
                {
                    answerList.put(currentQuestionPosition, D);
                    selectOption(binding.d, D);
                }

            }
        });

        binding.clearResponse.setOnClickListener(view -> {
            // clear mark as review
            if(answerStatusList.get(currentQuestionPosition) != null) {
                if (answerStatusList.get(currentQuestionPosition).equals(QUESTION_MARK_AS_REVIEW)) {
                    numberOfReviewLetterQuestions--;
                    answerStatusList.put(currentQuestionPosition,null);
                    if (numberOfReviewLetterQuestions < 0) {
                        numberOfReviewLetterQuestions = 0;
                    }
                    Log.d("REVIEW_LETTER_CLEAR", "" + numberOfReviewLetterQuestions);
                }
            }


            // Clear Selected Answer
            answerList.put(currentQuestionPosition,null);
            Log.d("ANS : ", answerList.toString());
            clearOptions();
        });

        binding.next.setOnClickListener(view -> {
            currentQuestionPosition++;
            if(currentQuestionPosition == questions.size())
            {
                currentQuestionPosition = 0;
            }
            changeQuestion();
        });

        binding.Previous.setOnClickListener(view -> {
            currentQuestionPosition--;
            if(currentQuestionPosition < 0)
            {
                currentQuestionPosition = questions.size() - 1;
            }
            changeQuestion();
        });

        // for review Check
        binding.reviewCheck.setOnClickListener(view -> {
            if(((CheckBox)view).isChecked())
            {
                numberOfReviewLetterQuestions++;
                answerStatusList.put(currentQuestionPosition,QUESTION_MARK_AS_REVIEW);
            }
            else
            {
                numberOfReviewLetterQuestions--;
                answerStatusList.put(currentQuestionPosition,null);
                if(numberOfReviewLetterQuestions < 0)
                {
                    numberOfReviewLetterQuestions = 0;
                }
            }

            Log.d("REVIEW_LETTER", numberOfReviewLetterQuestions + answerStatusList.toString());
        });
    }

    private void initializeQuestionsStatus()
    {
        dialogBinding.answered.setText("0");
        dialogBinding.notAns.setText("0");
        dialogBinding.notVisited.setText("" + (questions.size() - 1));
        dialogBinding.reviewLetter.setText("0");

        numberOfNotAnsweredQuestions = 1;
        // marking All Answers As Not Visited
        for(int i = 0; i < questions.size();i++)
        {
            answerStatusList.put(i,null);
            answerList.put(i,null);
            questionsVisitedList.put(i,false);
        }
    }

    private void setQuestionsStatus()
    {
        numberOfAnswerdQuestions = getNumberOfAnsweredQuestions();

        numberOfNotAnsweredQuestions = questions.size() - numberOfAnswerdQuestions;

        numberOfNotVisitedQuestions = getNumberOfNotVisitedQuestions();

        dialogBinding.answered.setText("" + numberOfAnswerdQuestions);
        dialogBinding.notAns.setText("" + numberOfNotAnsweredQuestions);
        dialogBinding.notVisited.setText("" + numberOfNotVisitedQuestions);
        dialogBinding.reviewLetter.setText("" + numberOfReviewLetterQuestions);
    }

    private void changeQuestion()
    {
        if(questions != null)
        {
            binding.tvQuestion.setText(questions.get(currentQuestionPosition).getQuestion());
            binding.tvQuestionNumber.setText("Question : " + (currentQuestionPosition + 1));

            List<String> options = questions.get(currentQuestionPosition).getOptions();

            binding.a.setText(A + " " + options.get(0));
            binding.b.setText(B + " " + options.get(1));
            binding.c.setText(C + " " + options.get(2));
            binding.d.setText(D + " " + options.get(3));

            clearOptions();

            // make questions visited
            questionsVisitedList.put(currentQuestionPosition, true);

            // set the previous Selected Questions if selected
            String ans = null;
            if((ans = answerList.get(currentQuestionPosition)) != null)
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

            // set check if the question is mark as review
            if((ans = answerStatusList.get(currentQuestionPosition)) != null)
            {
                if(ans.equals(QUESTION_MARK_AS_REVIEW))
                {
                    binding.reviewCheck.setChecked(true);
                }
            }
        }
    }
    private void selectOption(RadioButton option, String answer) {
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

        if (answer.equals(questions.get(currentQuestionPosition).getAnswer())) {
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

        // clear the review letter checkbox
        binding.reviewCheck.setChecked(false);
    }

    private int getNumberOfAnsweredQuestions()
    {
        int count = 0;

        for(int i = 0; i < answerList.size(); i++)
        {
            if(answerList.get(i) != null)
            {
                count++;
            }
        }

        return(count);
    }

    private int getNumberOfNotVisitedQuestions()
    {
        int cnt = 0;
        for(int i = 0; i < questionsVisitedList.size(); i++)
        {
            if(questionsVisitedList.get(i) == false)
            {
                cnt++;
            }
        }
        return(cnt);
    }

    private float getMarks()
    {
        float percentage = 0.0f;
        int numberOfCorrectAnswers = 0;

        for(int i = 0; i < questions.size();i++)
        {
            if(questions.get(i).getAnswer().equals(answerList.get(i)))
            {
                numberOfCorrectAnswers++;
            }
        }

        percentage = ((float)numberOfCorrectAnswers / (float)questions.size()) * 100.0f;
        Log.d("TOTAL_MARKS", "correct Questions : " + numberOfCorrectAnswers + " percentage : " + percentage);
        return(percentage);
    }

}
