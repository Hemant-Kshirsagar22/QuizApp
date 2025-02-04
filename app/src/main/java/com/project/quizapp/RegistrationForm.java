package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.Status;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.validation.EmailValidator;
import com.project.quizapp.validation.NameValidator;

import java.util.Timer;
import java.util.TimerTask;

public class RegistrationForm extends AppCompatActivity implements Status {

   private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passEditTextOne;
    private EditText passEditTextTwo;
    private Button registerButton;
    private SignInButton googleSigninButton;

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private TextView loginNow;
    // for validation
    private NameValidator firstNameValidator = null;
    private  NameValidator lastNameValidator = null;
    private EmailValidator emailValidator = null;

    ProgressBar progressBar = null;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // Add your client ID here
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        emailEditText = findViewById(R.id.email);
        passEditTextOne = findViewById(R.id.passwordOne);
        passEditTextTwo = findViewById(R.id.passwordTwo);
        registerButton = findViewById(R.id.registerBtn);
        loginNow = findViewById(R.id.loginNow);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        googleSigninButton = findViewById(R.id.googleLogin);

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
                //ProgressBar
                ProgressBar();

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



        googleSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
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

    public void ProgressBar() {
        counter = 0; // Reset counter before starting
        progressBar.setProgress(counter);
        progressBar.setVisibility(View.VISIBLE);

        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (counter < 100) {
                            counter++;
                            progressBar.setProgress(counter);
                        } else {
                            t.cancel(); // Stop the timer
                            progressBar.setVisibility(View.GONE); // Hide progress bar when complete
                        }
                    }
                });
            }
        };
        t.schedule(tt, 0, 30); // Adjust speed if needed
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                FirebaseDBHelper.insertUserWithGoogle(account, new FirebaseDBHelper.UserQueryCallback() {
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
            } catch (ApiException e) {
                // Google sign-in failed, handle the error
                Log.w("GOOGLE_SIGNIN", "Google sign-in failed" + e.getMessage(), e);
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
