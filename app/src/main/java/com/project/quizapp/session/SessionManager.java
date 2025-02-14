package com.project.quizapp.session;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.project.quizapp.Dashboard;
import com.project.quizapp.LoginPage;
import com.project.quizapp.database.entities.User;

public class SessionManager {
    private SharedPreferences sharedPreference = null;
    private SharedPreferences.Editor editor = null;

    private static final String  SHARED_PREFERENCE_NAME = "session";
    private static final String  USER_LOGIN_STATUS = "userLogin";
    public SessionManager(Context context)
    {
        sharedPreference = context.getSharedPreferences(SHARED_PREFERENCE_NAME,context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public void setUserLogin(User user)
    {
        String userName = user.getEmail();
        editor.putString(USER_LOGIN_STATUS, userName).commit();
    }

    public static boolean isUserLoggedIn(Context context)
    {
        SessionManager sessionManager = new SessionManager(context);
        if(sessionManager.getUserName() != null) {
            return(true);
        }
        else {
            return(false);
        }
    }

    public String getUserName()
    {
        return (sharedPreference.getString(USER_LOGIN_STATUS,null));
    }

    public void setUserLogout(Context context)
    {
        editor.putString(USER_LOGIN_STATUS,null).commit();
        Intent intent = new Intent(context, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
