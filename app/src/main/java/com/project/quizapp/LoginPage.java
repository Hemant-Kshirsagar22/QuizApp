package com.project.quizapp;
import static com.project.quizapp.database.Status.MSG_EMPTY_FORM;
import static com.project.quizapp.database.Status.MSG_LOGIN_SUCCESS;

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
import com.project.quizapp.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

public class LoginPage extends AppCompatActivity{
    EditText userNameEditText = null;
    EditText passwordEditText = null;
    Button loginButton = null;
    SignInButton googleLoginButton = null;
    TextView signUpNowTextView = null;
    ProgressBar progressBar=null;

    private GoogleSignInClient googleLoginClient;
    private static final int RC_SIGN_IN = 9001;

    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*EdgeToEdge.enable(this);*/
        setContentView(R.layout.activity_login_page);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // Add your client ID here
                .requestEmail()
                .build();
        googleLoginClient = GoogleSignIn.getClient(this, gso);

        userNameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUpNowTextView = findViewById(R.id.signUpNow);
        googleLoginButton = findViewById(R.id.googleLogin);
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

                //PtogressBar
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

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInWithGoogle();
            }
        });
    }
    private void logInWithGoogle() {
        Intent signInIntent = googleLoginClient.getSignInIntent();
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
                        Toast.makeText(LoginPage.this, MSG_LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
                        IntentManager.toDashboardActivity(LoginPage.this);
                        finish();
                    }

                    @Override
                    public void onFailure(String errMsg) {
                        Toast.makeText(LoginPage.this,"GOOGLE LOGIN " + errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                // Google sign-in failed, handle the error
                Log.w("GOOGLE_LOGIN", "Google sign-in failed : " + e.getMessage(), e);
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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
}
