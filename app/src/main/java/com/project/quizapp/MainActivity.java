package com.project.quizapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    //    Button loginButton;
//    Button registerButton;
    ProgressBar progress=null;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        //progress Bar
        progressBar();

        // Delay for 5 seconds (5000ms)
        new Handler().postDelayed(() -> {
            // Redirect to SecondActivity
            if (!isNetworkConnected()) {
                showNoInternetDialog();
//                System.exit(0);
            }else {

                if(FirebaseDBHelper.isUserLoggedIn()) {

                    FirebaseDBHelper.getUser(new FirebaseDBHelper.UserQueryCallback() {
                        @Override
                        public void onSuccess(User user) {
                            if(user.getEmail().equals("admin@gmail.com"))
                            {
                                Log.d("LOGGEDIN",user.toString());
                                IntentManager.toAdminMainActivity(MainActivity.this);
                                finish();

                            }
                            else
                            {
                                IntentManager.toDashboardActivity(MainActivity.this);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(String errMsg) {
                            IntentManager.toDashboardActivity(MainActivity.this);
                            finish();
                        }
                    });
                }
                else
                {
                    IntentManager.toLoginActivity(MainActivity.this);
                    finish(); // Close the current activity
                }
            }
        }, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Method to check if the device is connected to the internet
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Show an alert dialog asking the user to turn on the internet
    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("It looks like you're not connected to the internet. Would you like to turn on Wi-Fi or Mobile Data?")
                .setCancelable(false) // Prevent closing dialog by tapping outside
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open network settings
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivityForResult(intent, 1); // Start settings activity with request code 1
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app
                        finish();
                    }
                })
                .show();
    }

    // When the settings activity finishes, check the network connection again
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If we came back from the settings screen (requestCode 1)
        if (requestCode == 1) {
            if (!isNetworkConnected()) {
                // If still no internet, show the alert again
                showNoInternetDialog();
            }
            else
            {
                if(FirebaseDBHelper.isUserLoggedIn()) {
                    FirebaseDBHelper.getUser(new FirebaseDBHelper.UserQueryCallback() {
                        @Override
                        public void onSuccess(User user) {
                            if(user.getEmail().equals("admin@gmail.com"))
                            {
                                Log.d("LOGGEDIN",user.toString());
                                IntentManager.toAdminMainActivity(MainActivity.this);
                                finish();

                            }
                            else
                            {
                                IntentManager.toDashboardActivity(MainActivity.this);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(String errMsg) {
                            IntentManager.toDashboardActivity(MainActivity.this);
                            finish();
                        }
                    });
                }
                else
                {
                    IntentManager.toLoginActivity(this);
                    finish(); // Close the current activity
                }
            }
        }
    }

    public void progressBar(){
        progress = (ProgressBar)findViewById(R.id.progress_bar);
        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                progress.setProgress(counter);
                if(counter == 100)
                {
                    t.cancel();
                }
            }
        };
        t.schedule(tt,0,20);
    }

}
