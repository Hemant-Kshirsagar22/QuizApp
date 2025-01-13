package com.project.quizapp;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.DatabaseHelper;
import com.project.quizapp.database.Status;

public class RegistrationForm extends AppCompatActivity implements Status {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passEditTextOne;
    EditText passEditTextTwo;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        passEditTextOne = findViewById(R.id.passwordOne);
        passEditTextTwo = findViewById(R.id.passwordTwo);
        registerButton = findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(RegistrationForm.this);

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String passOne = passEditTextOne.getText().toString().trim();
                String passTwo = passEditTextTwo.getText().toString().trim();

                if((!firstName.isEmpty()) && (!lastName.isEmpty()) && (!email.isEmpty()) && (!passOne.isEmpty()) && (!passTwo.isEmpty())) {
                    if(passOne.equals(passTwo)) {
                        int status = databaseHelper.addUser(firstName, lastName, email, passOne);
                        if (status == INSERT_SUCCESS) {
                            Toast.makeText(RegistrationForm.this, MSG_INSERT_SUCCESS, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistrationForm.this, MSG_USER_EXISTS, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(RegistrationForm.this,MSG_PASS_MISMATCH,Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegistrationForm.this,MSG_EMPTY_FORM,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
