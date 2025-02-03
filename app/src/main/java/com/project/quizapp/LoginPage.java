package com.project.quizapp;
import static com.project.quizapp.database.Status.MSG_EMPTY_FORM;
import static com.project.quizapp.database.Status.MSG_LOGIN_SUCCESS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.Status;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

public class LoginPage extends AppCompatActivity{
    EditText userNameEditText = null;
    EditText passwordEditText = null;
    Button loginButton = null;
    TextView signUpNowTextView = null;
    ProgressBar progressBar=null;

    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);*/
        setContentView(R.layout.activity_login_page);

        userNameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpNowTextView = findViewById(R.id.signUpNow);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        signUpNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentManager.toRegistrationActivity(LoginPage.this);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                ProgressBar();
                if((!userName.isEmpty()) && (!password.isEmpty()))
                {
                    FirebaseDBHelper.loginUser(userName, password, new FirebaseDBHelper.UserQueryCallback() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.makeText(LoginPage.this, MSG_LOGIN_SUCCESS,Toast.LENGTH_SHORT).show();
                            IntentManager.toDashboardActivity(LoginPage.this);
                        }

                        @Override
                        public void onFailure(String errMsg) {
                            Toast.makeText(LoginPage.this,errMsg,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(LoginPage.this,MSG_EMPTY_FORM,Toast.LENGTH_SHORT).show();
                }
            }
        });


//        for (String user :
//                userNames) {
//            Log.d("USERNAME",user);
//        }
//
//        for (String pwd :
//                pass) {
//            Log.d("PASS",pwd);
//        }



    }
        public void ProgressBar(){
            final Timer t = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    counter++;
                    progressBar.setProgress(counter);
                    if(counter == 100)
                    {
                        t.cancel();
                    }
                }
            };
            t.schedule(tt,0,10);
        }
}
