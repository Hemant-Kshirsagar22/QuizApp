package com.project.quizapp;
import static com.project.quizapp.database.Status.MSG_EMPTY_FORM;
import static com.project.quizapp.database.Status.MSG_LOGIN_PASS_NOT_MATCH;
import static com.project.quizapp.database.Status.MSG_LOGIN_SUCCESS;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.DatabaseHelper;
import com.project.quizapp.database.DatabaseStrings;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity implements DatabaseStrings {
    DatabaseHelper db = null;
    EditText userNameEditText = null;
    EditText passwordEditText = null;
    Button loginButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);*/
        setContentView(R.layout.activity_login_page);
        db = new DatabaseHelper(this);

        userNameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        ArrayList<String> userNames = db.getColumnDataFromTable(db.readDataFromTable(USER_TABLE_NAME),USER_NAME_COLUMN_ID);

        ArrayList<String> pass = db.getColumnDataFromTable(db.readDataFromTable(DatabaseStrings.USER_TABLE_NAME),DatabaseStrings.USER_PASS_COLUMN_ID);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if((!userName.isEmpty()) && (!password.isEmpty()))
                {
                    if(userNames.contains(userName) && (pass.get(userName.indexOf(userName)).equals(password)))
                    {
                        if(userName.equals(ADMIN_USER_NAME)) {
                            Toast.makeText(LoginPage.this, "ADMIN " + MSG_LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginPage.this, MSG_LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginPage.this,MSG_LOGIN_PASS_NOT_MATCH,Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginPage.this,MSG_EMPTY_FORM,Toast.LENGTH_SHORT).show();
                }
            }
        });


        for (String user :
                userNames) {
            Log.d("USERNAME",user);
        }

        for (String pwd :
                pass) {
            Log.d("PASS",pwd);
        }



    }
}
