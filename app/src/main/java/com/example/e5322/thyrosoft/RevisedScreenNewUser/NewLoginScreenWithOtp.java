package com.example.e5322.thyrosoft.RevisedScreenNewUser;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.SmsListener;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.MySMSBroadCastReceiver;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class NewLoginScreenWithOtp extends Activity {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    EditText username, password, otpmobile,
            otpemail;
    TextView forgotpassword, registration, check;
    Button login, generateotp;
    Context context;
    String RESPONSE1, getOTPNO, numberforgotpass, regmobile, user_type_from_login, version_code_login, user_code_from_login, uil_from_login, res_id, nameFromLoginApi, client_type, RESPONSE, RES_ID, VALID, USER_TYPE, SENDERID, OTPNO, User, Login_Type, Status, version, emailPattern, pass, ACCESS_TYPE11, API_KEY11, CLIENT_TYPE11, EMAIL11, EXISTS11, MOBILE11, NAME11, macAddress, RESPONSE11, RES_ID11, URL11, USER_CODE11, USER_TYPE11, VERSION_NO11;
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;
    String emailIdValidateOTP, user_mobile, user_email, isMobileEmailValidValidateOTP, mobileNoValidateOTP, otpNoValidateOTP, resIdValidateOTP, responseValidateOTP;
    //WaveDrawable mWaveDrawable;
    AlertDialog alertDialog, alertDialogforgotpass, alertDialogforotp, alertDialogverifyOtp;
    String email, getPhone_num, emailId, isMobileEmailValid, mobileNo, otpNo, resId, response1;
    public static com.android.volley.RequestQueue PostQue;
    public static com.android.volley.RequestQueue generateOTP, POstQueValidateOTP, POstQueValidateOTPForgotPassword;
    public static com.android.volley.RequestQueue PostQueEmailPhoneOtp;

    Boolean isInternetPresent = false;  // flag for Internet connection status

    Random random;
    String randomNumber, number, getOtpnumberfromedtText;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    int startRange = 1000, endRange = 9999;
    EditText edt_reg_otp;
    Button reg_otp_ok;
    private String TAG = NewLoginScreenWithOtp.class.getSimpleName().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login_otp);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forgotpassword = (TextView) findViewById(R.id.forgotpass);
        registration = (TextView) findViewById(R.id.registration);
        login = (Button) findViewById(R.id.login);
        check = (TextView) findViewById(R.id.check);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(NewLoginScreenWithOtp.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewLoginScreenWithOtp.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.WRITE_CONTACTS,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.VIBRATE,
                                    Manifest.permission.WAKE_LOCK,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.INTERNET},
                            1001);


                } else {
                    // switchToActivity(activity, LoginScreenActivity.class, new Bundle());
                    // goAhead();
                }
            }
        }, 1000);


        /* checkPermissionREAD_EXTERNAL_STORAGE(context);*/
//        if (checkAndRequestPermissions()) {
//            // carry on the normal flow, as the case of  permissions  granted.
//        }
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                username.setHint("");
                return false;
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (password.getText().toString().contains(" ")) {
                    password.setText("");
                }
                password.setHint("");
                return false;
            }
        });


        username.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(NewLoginScreenWithOtp.this,
                            ToastFile.crt_mob_num,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        username.setText(enteredString.substring(1));
                    } else {
                        username.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                if (number.length() == 10) {
                    getPhone_num = username.getText().toString();
                    generateRandomString();
                }
            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent u = new Intent(NewLoginScreenWithOtp.this, ManagingTabsActivity.class);
                startActivity(u);
            }
        });
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get the app version Name for display
        version = pInfo.versionName;

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    Toast.makeText(NewLoginScreenWithOtp.this,
                            ToastFile.crt_pwd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        password.setText(enteredString.substring(1));
                    } else {
                        password.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(NewLoginScreenWithOtp.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    private void sendOtp() {

        generateOTP = GlobalClass.setVolleyReq(NewLoginScreenWithOtp.this);

        JSONObject jsonObjectOtp = new JSONObject();
        try {

            jsonObjectOtp.put("apikey", "jLmjR1POZBbn@TVVzb0uJvpTMeBbnNwR3lCNnqI)Ivk=");
            jsonObjectOtp.put("type", "SENDOTP");
            jsonObjectOtp.put("mobile", getPhone_num);
            jsonObjectOtp.put("otpno", randomNumber);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.OTP, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
                try {
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                    OTPNO = parentObjectOtp.getString("OTPNO");
                    RESPONSE = parentObjectOtp.getString("RESPONSE");
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    SENDERID = parentObjectOtp.getString("SENDERID");
                    USER_TYPE = parentObjectOtp.getString("USER_TYPE");
                    VALID = parentObjectOtp.getString("VALID");

                    if (RES_ID.equals("RES0000")) {
                        TastyToast.makeText(NewLoginScreenWithOtp.this, "" + RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        MySMSBroadCastReceiver.bindListener(new SmsListener() {
                            @Override
                            public void messageReceived(String messageText) {
                                try {
                                    numberforgotpass = String.valueOf(messageText);
                                    password.setText(messageText);
                                    if (!numberforgotpass.equals(messageText)) {
                                        TastyToast.makeText(getApplicationContext(), ToastFile.crt_otp, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        TastyToast.makeText(NewLoginScreenWithOtp.this, "" + RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    TastyToast.makeText(NewLoginScreenWithOtp.this, "Feedback not sent successfully", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {

                    System.out.println(error);
                }
            }
        });

        jsonObjectRequest1.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        generateOTP.add(jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObjectOtp);

    }


    private void generateRandomString() {
        random = new Random();
        randomNumber = random.nextInt((endRange - startRange) + startRange) + startRange + "";
        sendOtp();
    }


}

