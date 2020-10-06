package com.example.e5322.thyrosoft.CommonItils;

import android.app.Activity;
import android.widget.EditText;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static Boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}