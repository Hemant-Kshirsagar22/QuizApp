package com.project.quizapp;

import android.content.Context;
import android.content.Intent;

import com.project.quizapp.admin.AdminMainActivity;
import com.project.quizapp.admin.user.UserMain;

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
        Class<?> currentActivityClass = context.getClass();

        if(!currentActivityClass.equals(Dashboard.class)) {
            Intent intent = new Intent(context, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    public static void toActivityResultView(Context context)
    {
        Intent intent = new Intent(context, ActivityResultView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void toAdminMainActivity(Context context)
    {
        Intent intent = new Intent(context, AdminMainActivity.class);
        context.startActivity(intent);
    }

    public static void toAdminUserSection(Context context)
    {
        Intent intent = new Intent(context, UserMain.class);
        context.startActivity(intent);
    }

    public static void toQuestionPanelView(Context context, String selectedCategory,Boolean resumeTest, boolean solution){
        Intent intent = new Intent(context, QuestionPanelView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra("selectedCategory", selectedCategory);
        intent.putExtra("resumeTest", resumeTest);
        intent.putExtra("solution", solution);

        context.startActivity(intent);
    }

    public static void toPerformanceView(Context context)
    {
        Intent intent = new Intent(context, Performance.class);
        context.startActivity(intent);
    }
    public static void toAboutUs(Context context)
    {
        Intent intent = new Intent(context, AboutUs.class);
        context.startActivity(intent);
    }

    public static void toHistory(Context context)
    {
        Intent intent = new Intent(context, History.class);
        context.startActivity(intent);
    }
}
