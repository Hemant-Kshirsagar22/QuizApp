package com.project.quizapp;

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
    EditText passEditText;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(RegistrationForm.this);

                int status = databaseHelper.addUser(
                        firstNameEditText.getText().toString().trim(),
                        lastNameEditText.getText().toString().trim(),
                        emailEditText.getText().toString().trim(),
                        passEditText.getText().toString().trim());

                if(status == INSERT_SUCCESS)
                {
                    Toast.makeText(RegistrationForm.this, "Insert Success", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RegistrationForm.this, "Insert Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}