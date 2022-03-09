package com.example.e5322.thyrosoft.CommonItils;

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

    public static Boolean passportValidation(String passport){
        final String Passport_PATTERN  = "^[A-PR-WYa-pr-wy][1-9]\\d" + "\\s?\\d{4}[1-9]$";
        Pattern pattern = Pattern.compile(Passport_PATTERN);
        Matcher matcher = pattern.matcher(passport);
        return matcher.matches();
    }
}