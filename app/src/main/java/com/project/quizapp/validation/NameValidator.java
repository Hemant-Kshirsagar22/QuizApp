package com.project.quizapp.validation;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class NameValidator implements TextWatcher {
    View view = null;
    public boolean isValid = false;
    public NameValidator(View view) {
        this.view = view;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
        Log.d("ONCHNG",charSequence + " : " + isValidName(charSequence.toString()));
        if(isValidName(charSequence.toString()))
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

    private boolean isValidName(String name)
    {
        for(int i = 0; i < name.length();i++)
        {
            if((name.charAt(i) < 'A' || name.charAt(i) > 'Z') && (name.charAt(i) < 'a' || name.charAt(i) > 'z'))
            {
                return(false);
            }
        }
        return(true);
    }
}

/*new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                Log.d("CHAR","" + Character.isLetter(keyEvent.getUnicodeChar()));


//                if(Character.isLetter(keyEvent.getUnicodeChar()))
//                {
//                    firstNameEditText.setTextColor(Color.rgb(0,0,0));
//                    return true;
//                }
//                else if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK)
//                {
//                    Log.d("CHAR","" + Character.isLetter(keyEvent.getUnicodeChar()));
//                    return true;
//                }
//                else
//                {
//                    firstNameEditText.setTextColor(Color.rgb(255,0,0));
//                }
//                return false;
                String name = firstNameEditText.getText().toString();

                if(isValidName(name))
                {
                    firstNameEditText.setTextColor(Color.rgb(0,0,0));
                    return true;
                }
                else
                {
                    firstNameEditText.setTextColor(Color.rgb(255,0,0));
                    return true;
                }


            }

            private boolean isValidName(String name)
            {
                for(int i = 0; i < name.length();i++)
                {
                    if((name.charAt(i) < 'A' || name.charAt(i) > 'Z') && (name.charAt(i) < 'a' || name.charAt(i) > 'z'))
                    {
                        return(false);
                    }
                }
                return(true);
            }
        }
* */