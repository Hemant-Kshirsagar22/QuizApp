package com.project.quizapp;

import android.content.Context;
import android.content.Intent;

public class IntentManager {
    private IntentManager() {}

    public static void toRegistrationActivity(Context context)
    {
        Intent intent = new Intent(context, RegistrationForm.class);
        context.startActivity(intent);
    }

    public static void toLoginActivity(Context context)
    {
        Intent intent = new Intent(context,LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toDashboardActivity(Context context)
    {
        Intent intent = new Intent(context, Dashboard.class);
        context.startActivity(intent);
    }



}
