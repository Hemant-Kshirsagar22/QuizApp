package com.project.quizapp;
import static com.project.quizapp.database.Status.MSG_EMPTY_FORM;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.session.SessionManager;

public class LoginPage extends AppCompatActivity{
    EditText userNameEditText = null;
    EditText passwordEditText = null;
    Button loginButton = null;
    TextView signUpNowTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);*/
        setContentView(R.layout.activity_login_page);

        userNameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpNowTextView = findViewById(R.id.signUpNow);


        signUpNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistrationForm.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if((!userName.isEmpty()) && (!password.isEmpty()))
                {
                    FirebaseDBHelper.getUserByUserName(userName, new FirebaseDBHelper.UserQueryCallback() {
                        @Override
                        public void onSuccess(User user) {
                            if (user != null)
                            {
                                if(user.getPassword().equals(password))
                                {
                                    SessionManager sessionManager = new SessionManager(LoginPage.this);
                                    sessionManager.setUserLogin(user);
                                    Intent intent = new Intent(LoginPage.this,Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
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
}
