package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.Status;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.validation.EmailValidator;
import com.project.quizapp.validation.NameValidator;

public class RegistrationForm extends AppCompatActivity implements Status {

   private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passEditTextOne;
    private EditText passEditTextTwo;
    private Button registerButton;

    private TextView loginNow;
    // for validation
    private NameValidator firstNameValidator = null;
    private  NameValidator lastNameValidator = null;
    private EmailValidator emailValidator = null;

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
        loginNow = findViewById(R.id.loginNow);

        // setting listeners for validation
        firstNameValidator = new NameValidator(firstNameEditText);
        lastNameValidator = new NameValidator(lastNameEditText);
        emailValidator = new EmailValidator(emailEditText);

        firstNameEditText.addTextChangedListener(firstNameValidator);
        lastNameEditText.addTextChangedListener(lastNameValidator);
        emailEditText.addTextChangedListener(emailValidator);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String passOne = passEditTextOne.getText().toString().trim();
                String passTwo = passEditTextTwo.getText().toString().trim();

                // checking is empty
                if((!firstName.isEmpty()) && (!lastName.isEmpty()) && (!email.isEmpty()) && (!passOne.isEmpty()) && (!passTwo.isEmpty())) {

                    // checking is first name valid
                    if(!firstNameValidator.isValid) {
                        Toast.makeText(RegistrationForm.this,MSG_FIRST_NAME_NOT_VALID,Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // checking is last name valid
                    if(!lastNameValidator.isValid) {
                        Toast.makeText(RegistrationForm.this,MSG_LAST_NAME_NOT_VALID,Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!emailValidator.isValid)
                    {
                        Toast.makeText(RegistrationForm.this,MSG_EMAIL_NOT_VALID,Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    if(passOne.length() < 8)
//                    {
//                        Toast.makeText(RegistrationForm.this,MSG_PASS_LENGTH_ERROR,Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    // checking two passwords are same or not
                    if(passOne.equals(passTwo)) {
                        // registration code
                        User user = new User(firstName,lastName,email,passOne);
                        FirebaseDBHelper.insertUser(user, new FirebaseDBHelper.UserQueryCallback() {
                            @Override
                            public void onSuccess(User user) {
                                Toast.makeText(RegistrationForm.this,MSG_INSERT_SUCCESS,Toast.LENGTH_SHORT).show();
                                IntentManager.toLoginActivity(RegistrationForm.this);
                                finish();
                            }

                            @Override
                            public void onFailure(String errMsg) {
                                Toast.makeText(RegistrationForm.this,errMsg,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else // if password not same
                    {
                        Toast.makeText(RegistrationForm.this,MSG_PASS_MISMATCH,Toast.LENGTH_SHORT).show();
                    }
                }
                else // is fields are empty
                {
                    Toast.makeText(RegistrationForm.this,MSG_EMPTY_FORM,Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentManager.toLoginActivity(RegistrationForm.this);
                finish();
            }
        });
    }
}
