package com.project.quizapp;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.databinding.ActivityQuestionPanelViewBinding;
import com.project.quizapp.databinding.ActivityResultViewBinding;
import com.project.quizapp.databinding.OnSubmitDailogLayoutBinding;
import com.project.quizapp.databinding.QuestionPanelDrawerHeaderViewBinding;
import com.project.quizapp.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuestionPanelView extends AppCompatActivity {

    private ActivityQuestionPanelViewBinding binding;
    private QuestionPanelDrawerHeaderViewBinding drawerHeaderViewBinding;
    private OnSubmitDailogLayoutBinding dialogBinding;
    private ActivityResultViewBinding resultViewBinding;

    private List<Question> questions = null;

    private RadioGroup radioGroup = null;
    private final String A = "A";
    private final String B = "B";
    private final String C = "C";
    private final String D = "D";

    private int numberOfAnsweredQuestions = 0;
    private  int numberOfNotAnsweredQuestions = 0;
    private int numberOfReviewLetterQuestions = 0;
    private int numberOfNotVisitedQuestions = 0;
    private Map<Integer,String> answerList = null;
    private Map<Integer,String> answerStatusList = null;
    private Map<Integer,Boolean> questionsVisitedList = null;

    private int currentQuestionPosition = 0;

    private AlertDialog dialog = null;
    // Questions Status
    private static final String QUESTION_MARK_AS_REVIEW = "REVIEW";

    private List<Button> buttonList = null;

    private String selectedCategory = null;

    private Boolean testSubmitStatus = false;

    // session
    private SessionManager sessionManager = null;
    private Boolean resumeTestStatus = false;

    // timer value
    private long totalTestTime = 0L;
    private long currentTime = 0L;

    private boolean solution = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the selected category
        selectedCategory = getIntent().getStringExtra("selectedCategory");
        resumeTestStatus = getIntent().getBooleanExtra("resumeTest", false); // get resumeTest Status
        solution = getIntent().getBooleanExtra("solution", false);

        // session related code
        sessionManager = new SessionManager(QuestionPanelView.this);

        // Initialize View Binding
        binding = ActivityQuestionPanelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize Question Drawer Binding
        drawerHeaderViewBinding = QuestionPanelDrawerHeaderViewBinding.bind(binding.navigationView.getHeaderView(0));

        // initialize Submit Alert Dialog
        initAlertDialog();
        radioGroup = (RadioGroup) binding.radioGroup;

        // Handle Menu Button Click
        binding.menuButton.setOnClickListener(view -> {
            binding.drawerLayout.openDrawer(binding.navigationView);
        });
        // alert box related code
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // set the questions status
                setQuestionsStatus();
                if(solution)
                {
                    IntentManager.toDashboardActivity(QuestionPanelView.this, true);
                }
                else {
                    dialog.show();
                }
            }
        });

        FirebaseDBHelper.getQuestionByCategory(selectedCategory, new FirebaseDBHelper.QuestionQueryCallback() {
            @Override
            public void onSuccess(List<Question> question) {
                questions = question;
                answerList = new HashMap<Integer,String>(question.size());
                answerStatusList = new HashMap<Integer,String>(question.size());
                questionsVisitedList = new HashMap<Integer,Boolean>(question.size());
                buttonList = new ArrayList<Button>(question.size());

                // set test pause status
                sessionManager.setTestPauseStatus(false);

                // set the values to 0
                initializeQuestionsStatus();

                updateDrawerQuestionSelector();

                if(solution == true)
                {
                    binding.tvExplanation.setVisibility(VISIBLE);
                    binding.timeValue.setVisibility(INVISIBLE);
                    binding.pause.setVisibility(INVISIBLE);
                    binding.reviewCheck.setVisibility(INVISIBLE);
                    binding.clearResponse.setVisibility(INVISIBLE);
                    answerList = sessionManager.getAnswerMap();

                    if(answerList == null)
                    {
                        answerList = new HashMap<>();
                    }
                    // set fetched question
                    changeQuestion();
                    return;
                }
                else
                {
                    totalTestTime = question.size() * (1000 * 60); // set timer value to the number of questions * 1 min i.e. 1 min for 1 question

                    if (resumeTestStatus) {
                        answerList = sessionManager.getAnswerMap();
                        answerStatusList = sessionManager.getAnswerStatusMap();
                        questionsVisitedList = sessionManager.getQuestionVisitedMap();
                        numberOfAnsweredQuestions = sessionManager.getValue("numberOfAnsweredQuestions");
                        numberOfNotAnsweredQuestions = sessionManager.getValue("numberOfNotAnsweredQuestions");
                        numberOfReviewLetterQuestions = sessionManager.getValue("numberOfReviewLetterQuestions");
                        numberOfNotVisitedQuestions = sessionManager.getValue("numberOfNotVisitedQuestions");

                        totalTestTime = sessionManager.getLongValue("timerValue");

                        // update question drawer
                        updateDrawerButtonsForResumeTest();
                    }

                    // set fetched question
                    changeQuestion();

                    // timer related code
                    new CountDownTimer(totalTestTime, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            // set current count down value to global variable to store it if test is paused
                            currentTime = millisUntilFinished;

                            binding.timeValue.setText(getCurrentTimeString(millisUntilFinished));
                            // timer for alert box
                            dialogBinding.timeValue.setText(getCurrentTimeString(millisUntilFinished));

                        }

                        @Override
                        public void onFinish() {

                            dialogBinding.btnResume.setVisibility(View.GONE);
                            dialogBinding.dialogMessage.setText("TIME UP !!! SUBMIT THE ANSWERS");

                            // set the questions status
                            setQuestionsStatus();
                            dialog.show();

                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });

        if(!solution) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (binding.a.isChecked()) {
                        answerList.put(currentQuestionPosition, A);
                        selectOption(binding.a, A);
                    } else if (binding.b.isChecked()) {
                        answerList.put(currentQuestionPosition, B);
                        selectOption(binding.b, B);
                    } else if (binding.c.isChecked()) {
                        answerList.put(currentQuestionPosition, C);
                        selectOption(binding.c, C);
                    } else if (binding.d.isChecked()) {
                        answerList.put(currentQuestionPosition, D);
                        selectOption(binding.d, D);
                    }

                }
            });

            binding.clearResponse.setOnClickListener(view -> {
                // clear mark as review
                if (answerStatusList.get(currentQuestionPosition) != null) {
                    if (answerStatusList.get(currentQuestionPosition).equals(QUESTION_MARK_AS_REVIEW)) {
                        numberOfReviewLetterQuestions--;
                        answerStatusList.put(currentQuestionPosition, null);
                        if (numberOfReviewLetterQuestions < 0) {
                            numberOfReviewLetterQuestions = 0;
                        }
                    }
                }

                // Clear Selected Answer
                answerList.put(currentQuestionPosition, null);
                clearOptions();
            });
        }

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

            // change drawer button colors
            changeDrawerButtonColor();
        });

        // pause button handling
        binding.pause.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                pauseAlertDialogue();
            }
        });
        // handling back-press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(solution)
                {
                    IntentManager.toDashboardActivity(QuestionPanelView.this, true);
                    finish();
                    return;
                }
                pauseAlertDialogue();
            }
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
        numberOfAnsweredQuestions = getNumberOfAnsweredQuestions();

        numberOfNotAnsweredQuestions = questions.size() - numberOfAnsweredQuestions;

        numberOfNotVisitedQuestions = getNumberOfNotVisitedQuestions();

        dialogBinding.answered.setText("" + numberOfAnsweredQuestions);
        dialogBinding.notAns.setText("" + numberOfNotAnsweredQuestions);
        dialogBinding.notVisited.setText("" + numberOfNotVisitedQuestions);
        dialogBinding.reviewLetter.setText("" + numberOfReviewLetterQuestions);
    }

    private void changeQuestion()
    {
        if(questions != null)
        {
            if(solution)
            {
                binding.tvExplanation.setText(questions.get(currentQuestionPosition).getExplanation());
            }

            binding.tvQuestion.setText(questions.get(currentQuestionPosition).getQuestion());
            binding.tvQuestionNumber.setText("Question : " + (currentQuestionPosition + 1));

            List<String> options = questions.get(currentQuestionPosition).getOptions();

            binding.a.setText(A + ". " + options.get(0));
            binding.b.setText(B + ". " + options.get(1));
            binding.c.setText(C + ". " + options.get(2));
            binding.d.setText(D + ". " + options.get(3));

            clearOptions();

            // make questions visited
            questionsVisitedList.put(currentQuestionPosition, true);

            // set the previous Selected Questions if selected
            String ans = null;
            if(solution)
            {
                if ((ans = answerList.get(currentQuestionPosition)) != null) {
                    switch (ans) {
                        case A:
                            binding.a.setBackgroundResource(R.drawable.not_answered_background);
                            break;
                        case B:
                            binding.b.setBackgroundResource(R.drawable.not_answered_background);
                            break;
                        case C:
                            binding.c.setBackgroundResource(R.drawable.not_answered_background);
                            break;
                        case D:
                            binding.d.setBackgroundResource(R.drawable.not_answered_background);
                            break;
                    }
                }
            }else {
                if ((ans = answerList.get(currentQuestionPosition)) != null) {
                    switch (ans) {
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
            // set check if the question is mark as review
            if((ans = answerStatusList.get(currentQuestionPosition)) != null)
            {
                if(ans.equals(QUESTION_MARK_AS_REVIEW))
                {
                    binding.reviewCheck.setChecked(true);
                }
            }

            if(solution) {
                switch (questions.get(currentQuestionPosition).getAnswer()) {
                    case "A":
                        binding.a.setBackgroundResource(R.drawable.answered_background);
                        break;

                    case "B":
                        binding.b.setBackgroundResource(R.drawable.answered_background);
                        break;

                    case "C":
                        binding.c.setBackgroundResource(R.drawable.answered_background);
                        break;

                    case "D":
                        binding.d.setBackgroundResource(R.drawable.answered_background);
                        break;

                    default:
                        Toast.makeText(this, "ERR SOLUTION", Toast.LENGTH_SHORT).show();
                        break;
                }
            }else {
                // change drawer button colors
                changeDrawerButtonColor();
            }
        }
    }

    private void selectOption(RadioButton option, String answer) {

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

        if(!solution) {
            // change drawer button colors
            changeDrawerButtonColor();
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

        if(!solution) {
            // change drawer button colors
            changeDrawerButtonColor();
        }
    }

    private int getNumberOfAnsweredQuestions()
    {
        int count = 0;

        for(int i = 0; i < answerList.size(); i++)
        {
            if((answerList.get(i) != null) && (answerList.get(i).equals(A) || answerList.get(i).equals(B) || answerList.get(i).equals(C) || answerList.get(i).equals(D)))
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
        return(percentage);
    }

    private void initAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionPanelView.this);
        //Get binding with on_submit_dialog
        dialogBinding = OnSubmitDailogLayoutBinding.inflate(LayoutInflater.from(QuestionPanelView.this));
        builder.setView(dialogBinding.getRoot());
        dialog = builder.create();
        dialog.setCancelable(false);
        dialogBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float marks = getMarks();

                testSubmitStatus = true; // make true because we are going to submit the test
                // get a previous map of attempted Test Marks
                FirebaseDBHelper.getMarksMap(new FirebaseDBHelper.GetMarksMapCallback() {
                    @Override
                    public void onSuccess(Map<String, Object> marksMap) {
                        if(marksMap == null)
                        {
                            marksMap = new HashMap<>();
                        }

                        marksMap.put( FirebaseDBHelper.USR_MARKS_ROOT + "/" +selectedCategory, String.format("%.2f", marks).toString());

                        FirebaseDBHelper.updateMarksMap(marksMap, new FirebaseDBHelper.UserQueryCallback() {
                            @Override
                            public void onSuccess(User user) {
                                resultAlertDialog();

                            }

                            @Override
                            public void onFailure(String errMsg) {
                                Toast.makeText(QuestionPanelView.this, "UPDATE_MAP_ERR" + errMsg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onFailure(String errMsg) {
                        Toast.makeText(QuestionPanelView.this, "ERR_GET_MARKS_MAP : " + errMsg, Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });

        dialogBinding.btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void pauseAlertDialogue()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pause Test");
        builder.setMessage("Are you sure you want to pause the test?");

        // Positive button (Pause)
        builder.setPositiveButton("Pause", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                IntentManager.toDashboardActivity(QuestionPanelView.this,true);
                finish();
            }
        });

        // Negative button (Cancel)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Close dialog and continue test
            }
        });

        // Show the AlertDialog
        builder.create().show();
    }
    private void resultAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionPanelView.this);
        //Get binding with resultViewBinding
        resultViewBinding = com.project.quizapp.databinding.ActivityResultViewBinding.inflate(LayoutInflater.from(QuestionPanelView.this));
        builder.setView(resultViewBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);

        resultViewBinding.tvScore.setText(String.format("%.2f%%", getMarks()));
        resultViewBinding.timeTaken.setText(getCurrentTimeString(totalTestTime - currentTime)); // set the time required to solve the test
        resultViewBinding.totalQuestions.setText(String.format("%d", questions.size()));

        // set answer status counts
        Integer count[] = getTotalCorrectWrongSkipAnswers();
        resultViewBinding.correctAnsCount.setText(String.format("%d", count[0]));
        resultViewBinding.wrongAnsCount.setText(String.format("%d", count[1]));
        resultViewBinding.skipAnsCount.setText(String.format("%d", count[2]));

        resultViewBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                IntentManager.toDashboardActivity(QuestionPanelView.this,true);
                finish();
            }
        });

        resultViewBinding.reAttempt.setOnClickListener(view -> {
            alertDialog.dismiss();
            IntentManager.toQuestionPanelView(this, selectedCategory, false, false);
        });

        resultViewBinding.solution.setOnClickListener(view -> {
            alertDialog.dismiss();
            sessionManager.setAnswerMap(answerList);
            IntentManager.toQuestionPanelView(this, selectedCategory, false, true);
        });
        alertDialog.show();
    }

    private void updateDrawerQuestionSelector()
    {
        drawerHeaderViewBinding.questionGrid.removeAllViews();

        for(int i = 0; i < questions.size();i++)
        {
            Button button = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            button.setLayoutParams(layoutParams);
            button.setText("" + (i + 1));
            button.setTextColor(getResources().getColor(R.color.textColorPrimary));
            button.setBackground(getDrawable(R.drawable.option_background));

            button.setOnClickListener(view -> {
                currentQuestionPosition = Integer.parseInt(((Button)view).getText().toString().trim()) - 1;
                changeQuestion();

                binding.drawerLayout.close();
            });

            buttonList.add(button);
            drawerHeaderViewBinding.questionGrid.addView(button);
        }
    }

    private void changeDrawerButtonColor()
    {
      if(questionsVisitedList.get(currentQuestionPosition) == true)
        {
            buttonList.get(currentQuestionPosition).setBackground(getDrawable(R.drawable.not_visited_background));
        }
        if((answerList.get(currentQuestionPosition) != null) && (answerList.get(currentQuestionPosition).equals(A) || answerList.get(currentQuestionPosition).equals(B) || answerList.get(currentQuestionPosition).equals(C) || answerList.get(currentQuestionPosition).equals(D)))
        {
            buttonList.get(currentQuestionPosition).setBackground(getDrawable(R.drawable.answered_background));
        }
        if(Objects.equals(answerStatusList.get(currentQuestionPosition), QUESTION_MARK_AS_REVIEW))
        {
            buttonList.get(currentQuestionPosition).setBackground(getDrawable(R.drawable.review_background));
        }
    }

    private String getCurrentTimeString(long millisUntilFinished)
    {
        int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
        int minutes = (int) (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) (millisUntilFinished % (1000 * 60)) / 1000;

        return(String.format("%02d:%02d:%02d",hours,minutes,seconds));
    }

    private Integer[] getTotalCorrectWrongSkipAnswers()
    {
        Integer count[] = new Integer[3];
        count[0] = 0; // for correct Answer
        count[1] = 0; // for wrong Answer
        count[2] = 0; // for skip Answers

        for(int i = 0; i < answerList.size(); i++)
        {
            if(answerList.get(i) == null)
            {
                count[2]++;
            }
            else if(answerList.get(i).equals(questions.get(i).getAnswer()))
            {
                count[0]++;
            }
            else
            {
                count[1]++;
            }
        }

        return(count);
    }

    // for pause test
    @Override
    protected void onStop() {
        super.onStop();
        if(solution)
        {
            return;
        }

        if(testSubmitStatus == false) {
            if (!sessionManager.getTestPauseStatus()) {
                sessionManager.setTestPauseStatus(true);
                Toast.makeText(this, "TEST IS PAUSED", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "TEST IS PAUSED", Toast.LENGTH_SHORT).show();
            }

            sessionManager.setAnswerMap(answerList);
            sessionManager.setAnswerStatusMap(answerStatusList);
            sessionManager.setQuestionVisitedMap(questionsVisitedList);
            sessionManager.setPausedStateQuestionCategory(selectedCategory);

            sessionManager.setValue("numberOfAnsweredQuestions",numberOfAnsweredQuestions);
            sessionManager.setValue("numberOfNotAnsweredQuestions",numberOfNotAnsweredQuestions);
            sessionManager.setValue("numberOfReviewLetterQuestions",numberOfReviewLetterQuestions);
            sessionManager.setValue("numberOfNotVisitedQuestions",numberOfNotVisitedQuestions);

            sessionManager.setLongValue("timerValue", currentTime);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(solution)
        {
            return;
        }

        if(resumeTestStatus) {
            if (sessionManager.getTestPauseStatus()) {
                sessionManager.setTestPauseStatus(false);
                Toast.makeText(this, "TEST IS RESUME", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDrawerButtonsForResumeTest()
    {
        for(int i = 0; i < questions.size(); i++)
        {
            if(questionsVisitedList.get(i) == true)
            {
                buttonList.get(i).setBackground(getDrawable(R.drawable.not_visited_background));
            }
            if((answerList.get(i) != null) && (answerList.get(i).equals(A) || answerList.get(i).equals(B) || answerList.get(i).equals(C) || answerList.get(i).equals(D)))
            {
                buttonList.get(i).setBackground(getDrawable(R.drawable.answered_background));
            }
            if(Objects.equals(answerStatusList.get(i), QUESTION_MARK_AS_REVIEW))
            {
                buttonList.get(i).setBackground(getDrawable(R.drawable.review_background));
            }
        }
    }

}
