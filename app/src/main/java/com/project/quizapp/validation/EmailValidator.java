package com.project.quizapp.validation;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class EmailValidator implements TextWatcher {
    private View view = null;
    public boolean isValid = false;
    public EmailValidator(View view)
    {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count)
    {
        String email = ((EditText)view).getText().toString();
        if(isValidEmail(email))
        {
            ((EditText)view).setTextColor(Color.rgb(0,0,0));
            isValid = true;
        }
        else
        {
            ((EditText)view).setTextColor(Color.rgb(255,0,0));
            isValid = false;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean isValidEmail(String email) {
        // Use Android's built-in Patterns class for email validation
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
