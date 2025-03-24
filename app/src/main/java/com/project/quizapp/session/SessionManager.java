package com.project.quizapp.session;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.project.quizapp.Dashboard;
import com.project.quizapp.LoginPage;
import com.project.quizapp.database.entities.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SessionManager {
    private SharedPreferences sharedPreference = null;
    private SharedPreferences.Editor editor = null;
    private static final String  SHARED_PREFERENCE_NAME = "session";
    private static final String  USER_LOGIN_STATUS = "userLogin";

    private static final String TEST_PAUSE_STATUS = "testStatus";
    private static final String ANSWER_MAP = "answerMap";
    private static final String PAUSED_STATE_QUESTION_CATEGORY = "questionCategory";
    private static final String ANSWER_STATUS_MAP = "answerStatusMap";
    private static final String QUESTION_VISITED_MAP = "questionVisitedMap";
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

    // pause test related methods
    public void setTestPauseStatus(boolean value)
    {
        editor.putBoolean(TEST_PAUSE_STATUS, value).commit();
    }

    public boolean getTestPauseStatus()
    {
        return(sharedPreference.getBoolean(TEST_PAUSE_STATUS,false));
    }

    public void setPausedStateQuestionCategory(String category)
    {
        editor.putString(PAUSED_STATE_QUESTION_CATEGORY,category).commit();
    }

    public String getPausedStateQuestionCategory()
    {
        return(sharedPreference.getString(PAUSED_STATE_QUESTION_CATEGORY,null));
    }


    public void setAnswerMap(Map<Integer, String> answerMap)
    {
        Set<String> answerSet = null;
        if(answerMap != null) {
            answerSet = new HashSet<String>();
            for (Map.Entry<Integer, String> entry : answerMap.entrySet()) {
                answerSet.add(String.format(entry.getKey() + "=" + entry.getValue()));
            }
        }
        editor.putStringSet(ANSWER_MAP, answerSet).commit();
    }

    public Map<Integer, String> getAnswerMap()
    {
        Map<Integer, String> answerMap = new HashMap<>();

        Set<String> answerSet = sharedPreference.getStringSet(ANSWER_MAP, new HashSet<>());

        for(String answer : answerSet)
        {
            String[] ans = answer.split("=");
            answerMap.put(Integer.parseInt(ans[0]), ans[1]);
        }
        Log.d("GET_ANSWER_MAP", "MAP IS : " + answerMap.toString());
        return answerMap;
    }

    public void setAnswerStatusMap(Map<Integer, String> answerStatusMap)
    {
        Set<String> answerStatusSet = null;
        if(answerStatusMap != null) {
            answerStatusSet = new HashSet<String>();

            for (Map.Entry<Integer, String> entry : answerStatusMap.entrySet()) {
                answerStatusSet.add(String.format(entry.getKey() + "=" + entry.getValue()));
            }
        }

        editor.putStringSet(ANSWER_STATUS_MAP, answerStatusSet).commit();
    }

    public Map<Integer, String> getAnswerStatusMap()
    {
        Map<Integer, String> answerStatusMap = new HashMap<>();

        Set<String> answerSet = sharedPreference.getStringSet(ANSWER_STATUS_MAP, new HashSet<>());

        for(String answer : answerSet)
        {
            String[] ans = answer.split("=");
            answerStatusMap.put(Integer.parseInt(ans[0]), ans[1]);
        }
        Log.d("GET_ANSWER_STATUS_MAP", "MAP IS : " + answerStatusMap.toString());
        return answerStatusMap;
    }

    public void setQuestionVisitedMap(Map<Integer, Boolean> questionVisitedMap)
    {
        Set<String> questionVistedSet = null;

        if(questionVisitedMap != null) {
            questionVistedSet = new HashSet<String>();
            for (Map.Entry<Integer, Boolean> entry : questionVisitedMap.entrySet()) {
                questionVistedSet.add(String.format(entry.getKey() + "=" + entry.getValue()));
            }
        }
        editor.putStringSet(QUESTION_VISITED_MAP, questionVistedSet).commit();
    }

    public Map<Integer, Boolean> getQuestionVisitedMap()
    {
        Map<Integer, Boolean> questionVisitedMap = new HashMap<>();

        Set<String> answerSet = sharedPreference.getStringSet(QUESTION_VISITED_MAP, new HashSet<>());

        for(String answer : answerSet)
        {
            String[] ans = answer.split("=");
            questionVisitedMap.put(Integer.parseInt(ans[0]), Boolean.parseBoolean(ans[1]));
        }
        Log.d("GET_QUESTION_VISITED_MAP", "MAP IS : " + questionVisitedMap.toString());
        return questionVisitedMap;
    }

    public void setValue(String varName, int value)
    {
        editor.putInt(varName, value).commit();
    }

    public int getValue(String varName)
    {
        return sharedPreference.getInt(varName,0);
    }


}
