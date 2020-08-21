package com.example.e5322.thyrosoft.CommonItils;

import android.app.Activity;
import android.widget.EditText;

import com.example.e5322.thyrosoft.GlobalClass;

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

    public static Boolean doesItHaveSpecialCharacter(String val) {
        Pattern pattern;
        Matcher matcher;
        final String _PATTERN = "[a-zA-Z. ]*";
        pattern = Pattern.compile(_PATTERN);
        matcher = pattern.matcher(val);
        return matcher.matches();
    }

    public static boolean pincodeValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_PINCODE,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 6) {
            GlobalClass.showTastyToast(activity, MessageConstants.VALID_PINCODE,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().trim().startsWith("0")) {
            GlobalClass.showTastyToast(activity, MessageConstants.PINCODE_SHOULD_NOT_START,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().trim().startsWith("9")) {
            GlobalClass.showTastyToast(activity, MessageConstants.PINCODE_SHOULD_NOT_START_9,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean mobileNoValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_MOBILE,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(activity, MessageConstants.MOBILE_10_DIGITS,2);
            editText.requestFocus();
            return false;
        }
        if (!editText.getText().toString().startsWith("9") && !editText.getText().toString().startsWith("8")
                && !editText.getText().toString().startsWith("7") && !editText.getText().toString().startsWith("6")) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_VALID_MOBILE,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean OTPValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_OTP,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 4) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_VALID_OTP,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean firstNameValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_FNAME,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 2) {
            GlobalClass.showTastyToast(activity, MessageConstants.FNAME_MIN_2,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean lastNameValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_LNAME,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 1) {
            GlobalClass.showTastyToast(activity, MessageConstants.LNAME_MIN_1,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean ageValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_AGE,2);
            editText.requestFocus();
            return false;
        }
        if (Integer.parseInt(editText.getText().toString()) < 1 || Integer.parseInt(editText.getText().toString()) > 135) {
            GlobalClass.showTastyToast(activity, MessageConstants.AGE_BETWEEN_1_AND_135,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean emailValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_EMAIL,2);
            editText.requestFocus();
            return false;
        }
        if (!isValidEmail(editText.getText().toString().trim())) {
            GlobalClass.showTastyToast(activity, MessageConstants.VALID_EMAIL,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean addressValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_ADDRESS,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 25) {
            GlobalClass.showTastyToast(activity, MessageConstants.ADDRESS_MIN_25,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean aadharValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_AADHAR_NO,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 12) {
            GlobalClass.showTastyToast(activity, MessageConstants.AADHAR_12_DIGITS,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean refDrNameValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_REFERRING_DR,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 2) {
            GlobalClass.showTastyToast(activity, MessageConstants.REF_DR_NAME_MIN_2,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean refDrMobileNoValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_REFERRING_DR_MOB,2);
            editText.requestFocus();
            return false;
        }
        if (editText.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(activity, MessageConstants.MOBILE_10_DIGITS,2);
            editText.requestFocus();
            return false;
        }
        if (!editText.getText().toString().startsWith("9") && !editText.getText().toString().startsWith("8")
                && !editText.getText().toString().startsWith("7") && !editText.getText().toString().startsWith("6")) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_VALID_MOBILE,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean refDrEmailValidation(Activity activity, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            GlobalClass.showTastyToast(activity, MessageConstants.ENTER_REFERRING_DR_EMAIL,2);
            editText.requestFocus();
            return false;
        }
        if (!isValidEmail(editText.getText().toString().trim())) {
            GlobalClass.showTastyToast(activity, MessageConstants.VALID_EMAIL,2);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean isAlphaNumeric(String s) {
        return s != null && s.matches("^[a-zA-Z0-9]*$");
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("^[0-9]*$");
    }

    public static boolean passportAadhaarVal(Activity activity, EditText editText) {
        /*if (editText.getText().toString().length() == 9) {
            if (!Validator.isAlphaNumeric(editText.getText().toString())) {
                 GlobalClass.showTastyToast(activity, MessageConstants.PASSPORT_ALPHA_NUMERIC);
                return false;
            }
        }*/
        if (editText.getText().toString().length() == 12) {
            if (!Validator.isNumeric(editText.getText().toString())) {
                GlobalClass.showTastyToast(activity, MessageConstants.AADHAR_NUMERIC,2);
                return false;
            }
        }
        return true;
    }

}