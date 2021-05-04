package com.example.e5322.thyrosoft.startscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.BuildConfig;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.SmsListener;
import com.example.e5322.thyrosoft.Models.CovidAccessReq;
import com.example.e5322.thyrosoft.Models.CovidAccessResponseModel;
import com.example.e5322.thyrosoft.Models.FirebaseModel;
import com.example.e5322.thyrosoft.Models.Firebasepost;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.RequestModels.LoginRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.LoginResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Registration.Registration_first_screen;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Activity implements View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static com.android.volley.RequestQueue PostQue;
    public static com.android.volley.RequestQueue generateOTP, POstQueValidateOTP, POstQueValidateOTPForgotPassword;
    public static com.android.volley.RequestQueue PostQueEmailPhoneOtp;
    EditText username, password, otpmobile, otpemail;
    TextView forgotpassword, registration;
    Button login, generateotp;
    Context context;
    String RESPONSE1, islogin, getOTPNO, numberforgotpass, regmobile, uil_from_login, res_id, nameFromLoginApi, RESPONSE, RES_ID, VALID, USER_TYPE, SENDERID, OTPNO, User, version, emailPattern, pass, ACCESS_TYPE11, API_KEY11, CLIENT_TYPE11, EMAIL11, EXISTS11, MOBILE11, NAME11, macAddress, RESPONSE11, RES_ID11, URL11, USER_CODE11, USER_TYPE11, VERSION_NO11;
    ProgressDialog barProgressDialog;
    SharedPreferences.Editor editor;
    Handler updateBarHandler;
    String emailIdValidateOTP, isMobileEmailValidValidateOTP, mobileNoValidateOTP, otpNoValidateOTP, resIdValidateOTP, responseValidateOTP;
    //WaveDrawable mWaveDrawable;
    AlertDialog alertDialog, alertDialogforgotpass, alertDialogforotp, alertDialogverifyOtp;
    String email, getPhone_num, emailId, isMobileEmailValid, mobileNo, otpNo, resId, response1;
    Random random;
    String randomNumber, number, getOtpnumberfromedtText;
    int startRange = 1000, endRange = 9999;
    EditText edt_reg_otp;
    Button reg_otp_ok;
    String newToken;
    private String TAG = Login.class.getSimpleName().toString();
    private GlobalClass globalClass;
    /*SharedPreferences.Editor editorUserActivity;
    SharedPreferences shr_user_log;*/
    private String androidOS;
    AppPreferenceManager appPreferenceManager;
    Activity activity;
    SharedPreferences pref_versioncheck;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forgotpassword = (TextView) findViewById(R.id.forgotpass);
        registration = (TextView) findViewById(R.id.registration);
        login = (Button) findViewById(R.id.login);
        appPreferenceManager = new AppPreferenceManager(activity);
        login.setOnClickListener(Login.this);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version = pInfo.versionName;
        androidOS = Build.VERSION.RELEASE;


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


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    Toast.makeText(Login.this,
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


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(Login.this);
                View promptsView = li.inflate(R.layout.activity_forgotpass, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        Login.this);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                alertDialogforgotpass = alertDialogBuilder.create();
                // show it
                alertDialogforgotpass.show();
                alertDialogforgotpass.setCanceledOnTouchOutside(false);
                final EditText edt_mobile_otp = (EditText) promptsView.findViewById(R.id.edt_mobile_otp);
                final Button cancel = (Button) promptsView.findViewById(R.id.cancel);
                final Button ok = (Button) promptsView.findViewById(R.id.ok);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogforgotpass.dismiss();
                    }
                });
                edt_mobile_otp.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        String enteredString = s.toString();
                        if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                                || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                                || enteredString.startsWith("6")) {
                            Toast.makeText(Login.this,
                                    ToastFile.crt_mob_num,
                                    Toast.LENGTH_SHORT).show();
                            if (enteredString.length() > 0) {
                                edt_mobile_otp.setText(enteredString.substring(1));
                            } else {
                                edt_mobile_otp.setText("");
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

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!edt_mobile_otp.getText().toString().equals("")) {
                            random = new Random();
                            generateRandomString();
                            getPhone_num = edt_mobile_otp.getText().toString();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sendOtp();
                                }
                            }, 100);
                            alertDialogforgotpass.dismiss();

                            LayoutInflater liOtpVerify = LayoutInflater.from(Login.this);
                            View promptsViewOtpVerify = liOtpVerify.inflate(R.layout.activity_otp, null);
                            edt_reg_otp = (EditText) promptsViewOtpVerify.findViewById(R.id.edt_reg_otp);
                            reg_otp_ok = (Button) promptsViewOtpVerify.findViewById(R.id.reg_otp_ok);
                            AlertDialog.Builder alertDialogBuilderOtp = new AlertDialog.Builder(
                                    Login.this);
                            alertDialogBuilderOtp.setView(promptsViewOtpVerify);
                            alertDialogverifyOtp = alertDialogBuilderOtp.create();
                            //show it
                            alertDialogverifyOtp.show();
                            alertDialogverifyOtp.setCanceledOnTouchOutside(false);

                            MySMSBroadCastReceiver.bindListener(new SmsListener() {
                                @Override
                                public void messageReceived(String messageText) {
                                    try {
                                        numberforgotpass = String.valueOf(messageText);
                                        edt_reg_otp.setText(messageText);
                                        if (!numberforgotpass.equals(messageText)) {
                                            TastyToast.makeText(getApplicationContext(), ToastFile.crt_otp, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            reg_otp_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (edt_reg_otp.getText().toString() != "") {
                                        POstQueValidateOTPForgotPassword = GlobalClass.setVolleyReq(Login.this);

                                        JSONObject jsonObjectValidateOtp = new JSONObject();
                                        try {

                                            getOTPNO = edt_reg_otp.getText().toString();
                                            jsonObjectValidateOtp.put("apiKey", "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=");
                                            jsonObjectValidateOtp.put("appId", "B2BApp");
                                            jsonObjectValidateOtp.put("mobileNo", getPhone_num);
                                            jsonObjectValidateOtp.put("emailId", "");
                                            jsonObjectValidateOtp.put("otpNo", getOTPNO);

                                            GlobalClass.saveMobileNUmber = getPhone_num;

                                            SharedPreferences.Editor editor = getSharedPreferences("OTPnumber", 0).edit();
                                            editor.putString("PhoneNUmber", getPhone_num);
//                                    editor.putString("", pass);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.ValidateOTP, jsonObjectValidateOtp, new com.android.volley.Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                Log.e(TAG, "onResponse: response" + response);
                                                try {
                                                    String finalJson = response.toString();
                                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                                    emailIdValidateOTP = parentObjectOtp.getString("emailId");
                                                    isMobileEmailValidValidateOTP = parentObjectOtp.getString("isMobileEmailValid");
                                                    mobileNoValidateOTP = parentObjectOtp.getString("mobileNo");
                                                    otpNoValidateOTP = parentObjectOtp.getString("otpNo");
                                                    resIdValidateOTP = parentObjectOtp.getString("resId");
                                                    responseValidateOTP = parentObjectOtp.getString("response");

                                                    if (resIdValidateOTP.equalsIgnoreCase("RES0000")) {
                                                        if (barProgressDialog.isShowing()) {
                                                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                barProgressDialog.dismiss();
                                                            }
                                                        }
                                                        TastyToast.makeText(Login.this, "" + responseValidateOTP, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                        Intent i = new Intent(Login.this, Change_Passowrd.class);
                                                        startActivity(i);
                                                    } else {
                                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                            barProgressDialog.dismiss();
                                                        }
                                                        TastyToast.makeText(Login.this, responseValidateOTP, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    }
                                                } catch (JSONException e) {
                                                    TastyToast.makeText(Login.this, "Invalid OTP", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                        POstQueValidateOTPForgotPassword.add(jsonObjectRequest1);
                                        Log.e(TAG, "onClick: " + jsonObjectRequest1);
                                        Log.e(TAG, "onClick: " + jsonObjectValidateOtp);

                                    } else {
                                        TastyToast.makeText(getApplicationContext(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(Login.this, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent reg= new Intent(Login.this, Registration_first_screen.class);
                startActivity(reg);*/
                LayoutInflater li = LayoutInflater.from(Login.this);
                View promptsView = li.inflate(R.layout.activity_registration, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        Login.this);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                alertDialog = alertDialogBuilder.create();

                // show it

                alertDialog.setCanceledOnTouchOutside(false);
                otpmobile = (EditText) promptsView.findViewById(R.id.otpmobile);
                otpemail = (EditText) promptsView.findViewById(R.id.otpemail);
                generateotp = (Button) promptsView.findViewById(R.id.generateotp);
                // final ImageView cross = (ImageView) promptsView.findViewById(R.id.cross);
                alertDialog.show();
                otpmobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        String enteredString = s.toString();
                        if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2") || enteredString.startsWith("3")
                                || enteredString.startsWith("4") || enteredString.startsWith("5") || enteredString.startsWith("6")) {
                            Toast.makeText(Login.this,
                                    ToastFile.crt_mob_num,
                                    Toast.LENGTH_SHORT).show();
                            if (enteredString.length() > 0) {
                                otpmobile.setText(enteredString.substring(1));
                            } else {
                                otpmobile.setText("");
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
                generateotp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        email = otpemail.getText().toString();
                        regmobile = otpmobile.getText().toString();
                        if (otpmobile.getText().toString().equals("") || otpemail.getText().toString().equals("")) {
                            Toast.makeText(Login.this, ToastFile.crt_mob_num_eml, Toast.LENGTH_SHORT).show();
                        } else if (regmobile.length() < 10) {
                            Toast.makeText(Login.this, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                        } else if (!email.matches(emailPattern)) {
                            Toast.makeText(Login.this, ToastFile.crt_eml, Toast.LENGTH_SHORT).show();
                        } else {
//                            generateEmailPhoneOTP();
                            PostQueEmailPhoneOtp = GlobalClass.setVolleyReq(Login.this);

                            barProgressDialog = new ProgressDialog(Login.this);
                            barProgressDialog.setTitle("Kindly wait ...");
                            barProgressDialog.setMessage(ToastFile.processing_request);
                            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                            barProgressDialog.setProgress(0);
                            barProgressDialog.setMax(20);
                            barProgressDialog.show();
                            barProgressDialog.setCanceledOnTouchOutside(false);
                            barProgressDialog.setCancelable(false);

                            final JSONObject jsonObjectEmailPhoneOtp = new JSONObject();
                            try {
                                jsonObjectEmailPhoneOtp.put("apiKey", "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=");
                                jsonObjectEmailPhoneOtp.put("appId", "B2BApp");
                                jsonObjectEmailPhoneOtp.put("mobileNo", regmobile);
                                jsonObjectEmailPhoneOtp.put("emailId", email);


                                SharedPreferences.Editor editor25 = getApplicationContext().getSharedPreferences("getEmailMobile", 0).edit();
                                editor25.putString("Mobile", regmobile);
                                editor25.putString("Email", email);
                                editor25.commit();
                                /*Global.Username = username.getText().toString();*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RequestQueue queueEmailPhoneOtp = GlobalClass.setVolleyReq(Login.this);
                            final JsonObjectRequest jsonObjectRequestEmailPhone = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.EmailPhoneOtp, jsonObjectEmailPhoneOtp, new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        Log.e(TAG, "onResponse: " + response);
                                        String finalJson = response.toString();
                                        JSONObject parentObjectOtp = new JSONObject(finalJson);

                                        emailId = parentObjectOtp.getString("emailId");
                                        isMobileEmailValid = parentObjectOtp.getString("isMobileEmailValid");
                                        mobileNo = parentObjectOtp.getString("mobileNo");
                                        otpNo = parentObjectOtp.getString("otpNo");
                                        resId = parentObjectOtp.getString("resId");
                                        response1 = parentObjectOtp.getString("response");

                                        GlobalClass.responseVariable = response1;
                                        if (isMobileEmailValid.equalsIgnoreCase("YES")) {
                                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                barProgressDialog.dismiss();
                                            }
                                            LayoutInflater li = LayoutInflater.from(Login.this);
                                            View promptsView = li.inflate(R.layout.activity_otp, null);
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                    Login.this);
                                            // set prompts.xml to alertdialog builder
                                            alertDialogBuilder.setView(promptsView);
                                            alertDialog.dismiss();
                                            alertDialogforotp = alertDialogBuilder.create();
                                            alertDialogforotp.show();
                                            alertDialogforotp.setCanceledOnTouchOutside(false);
                                            edt_reg_otp = (EditText) promptsView.findViewById(R.id.edt_reg_otp);
                                            reg_otp_ok = (Button) promptsView.findViewById(R.id.reg_otp_ok);


                                            MySMSBroadCastReceiver.bindListener(new SmsListener() {
                                                @Override
                                                public void messageReceived(String messageText) {
                                                    try {
                                                        number = String.valueOf(messageText);
                                                        edt_reg_otp.setText(messageText);
                                                        if (!number.equals(messageText)) {
                                                            TastyToast.makeText(getApplicationContext(), ToastFile.crt_otp, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });


                                            reg_otp_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    getOtpnumberfromedtText = edt_reg_otp.getText().toString();
                                                    if (edt_reg_otp.getText().toString().equals("")) {
                                                        Toast.makeText(Login.this, ToastFile.crt_otp, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // POstQueValidateOTP = GlobalClass.setVolleyReq(Login.this);
                                                        POstQueValidateOTP = GlobalClass.setVolleyReq(Login.this);

                                                        JSONObject jsonObjectValidateOtp = new JSONObject();
                                                        try {
                                                            jsonObjectValidateOtp.put("apiKey", "68rbZ40eNeRephUzIDTos9SsQIm4mHlT3lCNnqI)Ivk=");
                                                            jsonObjectValidateOtp.put("appId", "B2BApp");
                                                            jsonObjectValidateOtp.put("mobileNo", regmobile);
                                                            jsonObjectValidateOtp.put("emailId", email);
                                                            jsonObjectValidateOtp.put("otpNo", getOtpnumberfromedtText);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                        barProgressDialog = new ProgressDialog(Login.this);
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);

                                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.ValidateOTP, jsonObjectValidateOtp, new com.android.volley.Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                try {
                                                                    Log.e(TAG, "onResponse: " + response);
                                                                    String finalJson = response.toString();
                                                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                                                    emailIdValidateOTP = parentObjectOtp.getString("emailId");
                                                                    isMobileEmailValidValidateOTP = parentObjectOtp.getString("isMobileEmailValid");
                                                                    mobileNoValidateOTP = parentObjectOtp.getString("mobileNo");
                                                                    otpNoValidateOTP = parentObjectOtp.getString("otpNo");
                                                                    resIdValidateOTP = parentObjectOtp.getString("resId");
                                                                    responseValidateOTP = parentObjectOtp.getString("response");
                                                                    if (responseValidateOTP.equals("OTP Validated Sucessfully")) {
                                                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                            barProgressDialog.dismiss();
                                                                        }
//                                                        TastyToast.makeText(Login.this,""+RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                                        Intent reg = new Intent(Login.this, Registration_first_screen.class);
                                                                        startActivity(reg);

                                                                    } else if (responseValidateOTP == null) {
                                                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                            barProgressDialog.dismiss();
                                                                        }
                                                                        TastyToast.makeText(Login.this, "Invalid OTP", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                                    } else {
                                                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                            barProgressDialog.dismiss();
                                                                        }
                                                                        TastyToast.makeText(Login.this, "Invalid OTP", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                                    }
                                                                } catch (JSONException e) {
                                                                    TastyToast.makeText(Login.this, "Invalid OTP", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                                        POstQueValidateOTP.add(jsonObjectRequest1);
                                                        Log.e(TAG, "onClick: " + jsonObjectEmailPhoneOtp);
                                                        Log.e(TAG, "onClick: " + jsonObjectRequest1);
                                                        Log.e(TAG, "onClick: " + jsonObjectValidateOtp);

                                                    }

                                                }
                                            });
                                            alertDialog.dismiss();

                                        } else {
                                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                barProgressDialog.dismiss();
                                            }
                                            Toast.makeText(getApplicationContext(), "" + response1, Toast.LENGTH_SHORT).show();

                                        }


                                    } catch (JSONException e) {
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
                            RetryPolicy policy = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            jsonObjectRequestEmailPhone.setRetryPolicy(policy);
                            queueEmailPhoneOtp.add(jsonObjectRequestEmailPhone);
                            Log.e(TAG, "onClick: " + jsonObjectEmailPhoneOtp);
                            Log.e(TAG, "onClick: " + jsonObjectRequestEmailPhone);
                            //close previouse dialogue registartion otp email mobile number
                            // show it


                        }

                    }
                });

            }
        });

    }


    private void generateEmailPhoneOTP() {

    }

    private void sendOtp() {

        generateOTP = GlobalClass.setVolleyReq(Login.this);
        ;

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
                        TastyToast.makeText(Login.this, "" + RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                    } else if (RES_ID.equals("RES0000")) {
                        TastyToast.makeText(Login.this, "" + RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    TastyToast.makeText(Login.this, "Feedback not sent successfully", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        generateOTP.add(jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
        Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObjectOtp);

    }

    private void generateRandomString() {
        randomNumber = random.nextInt((endRange - startRange) + startRange) + startRange + "";
    }

    @Override
    public void onClick(View v) {

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifi.getConnectionInfo();
        macAddress = wInfo.getMacAddress();

        barProgressDialog = new ProgressDialog(Login.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
    /*    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    while (barProgressDialog.getProgress() <= barProgressDialog.getMax()) {
                        Thread.sleep(2000);
                        updateBarHandler.post(new Runnable() {
                            public void run() {
                                barProgressDialog.incrementProgressBy(2);
                            }
                        });
                        if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        User = username.getText().toString();
        pass = password.getText().toString();
        if (User.length() == 0 & pass.length() == 0) {
            Toast.makeText(this, "Please Enter Username and Password", Toast.LENGTH_SHORT).show();
        } else if (User.length() == 0) {
            Toast.makeText(this, "Please enter your Username", Toast.LENGTH_SHORT).show();
        } else if (pass.length() == 0) {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
        } else {
            try {
                barProgressDialog.show();
                PostQue = GlobalClass.setVolleyReq(Login.this);
                JSONObject jsonObject = null;
                try {
                    LoginRequestModel requestModel = new LoginRequestModel();
                    requestModel.setPortalType("ThyrosoftLite");
                    requestModel.setUserCode(User);
                    requestModel.setPassWord(pass);

                    Gson gson = new Gson();
                    String postData = gson.toJson(requestModel);
                    jsonObject = new JSONObject(postData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RequestQueue queue = GlobalClass.setVolleyReq(Login.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.LOGIN, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            if (response != null) {
                                Gson gson = new Gson();
                                LoginResponseModel loginResponseModel = gson.fromJson(String.valueOf(response), LoginResponseModel.class);

                                if (loginResponseModel != null) {
                                    if (!GlobalClass.isNull(loginResponseModel.getRES_ID()) && loginResponseModel.getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
                                        editor = getSharedPreferences("Userdetails", 0).edit();
                                        editor.putString("Username", User);
                                        editor.putString("password", pass);
                                        editor.putString("ACCESS_TYPE", loginResponseModel.getACCESS_TYPE());
                                        editor.putString("API_KEY", loginResponseModel.getAPI_KEY());
                                        editor.putString("email", loginResponseModel.getEMAIL());
                                        editor.putString("mobile_user", loginResponseModel.getMOBILE());
                                        editor.putString("CLIENT_TYPE", loginResponseModel.getCLIENT_TYPE());
                                        editor.putString("NAME", loginResponseModel.getNAME());
                                        editor.putString("RES_ID", loginResponseModel.getRES_ID());
                                        editor.putString("URL", loginResponseModel.getURL());
                                        editor.putString("USER_CODE", loginResponseModel.getUSER_CODE());
                                        editor.putString("USER_TYPE", loginResponseModel.getUSER_TYPE());
                                        editor.putString("VERSION_NO", loginResponseModel.getVERSION_NO());
                                        editor.putString("SOURCE_TYPE", loginResponseModel.getSOURCE_TYPE());
                                        editor.apply();

                                        SharedPreferences pref_versioncheck = activity.getSharedPreferences("pref_versioncheck", MODE_PRIVATE);
                                        SharedPreferences.Editor editorversioncode = pref_versioncheck.edit();
                                        editorversioncode.putInt("versioncode", BuildConfig.VERSION_CODE);
                                        editorversioncode.apply();


                                        checkcovidaccess();


                                        new LogUserActivityTagging(activity, Constants.SHLOGIN);


                                        USER_CODE11 = loginResponseModel.getUSER_CODE();

                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.TOPIC);
                                        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC);
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, new OnSuccessListener<InstanceIdResult>() {
                                            @Override
                                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                                newToken = instanceIdResult.getToken();
                                                Log.e("NewToken----->", newToken);
                                                storeRegIdInPref(newToken);
                                                pushToken();
                                            }
                                        });
                                    } else {
                                        TastyToast.makeText(getApplicationContext(), loginResponseModel.getRESPONSE(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                } else {
                                    TastyToast.makeText(getApplicationContext(), ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }

                            Toast.makeText(Login.this, ToastFile.invalid_log, Toast.LENGTH_SHORT).show();
                            System.out.println(error);

                    }
                });
                Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest);
                Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObject);
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(300000, 3,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjectRequest);
            } catch (Exception ex) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
            }
        }
    }

    private void checkcovidaccess() {
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);

        CovidAccessReq covidAccessReq = new CovidAccessReq();
        covidAccessReq.setSourceCode(User);

        Call<CovidAccessResponseModel> covidaccessResCall = postAPIInteface.checkcovidaccess(covidAccessReq);
        covidaccessResCall.enqueue(new Callback<CovidAccessResponseModel>() {
            @Override
            public void onResponse(Call<CovidAccessResponseModel> call, retrofit2.Response<CovidAccessResponseModel> response) {

                try {
                    SharedPreferences.Editor editor = getSharedPreferences("CovidAccess_sync", 0).edit();
                    editor.clear();
                    editor.putLong("PreivousTimeOfSync", System.currentTimeMillis()); // add this line and comment below line for cache
                    editor.commit();

                    if (response != null && response.body() != null) {
                        appPreferenceManager.setCovidAccessResponseModel(response.body());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                gotoHome();
            }

            @Override
            public void onFailure(Call<CovidAccessResponseModel> call, Throwable t) {
                gotoHome();
            }
        });
    }

    private void gotoHome() {
        Intent a = new Intent(Login.this, ManagingTabsActivity.class);
        a.putExtra(Constants.COMEFROM, true);
        startActivity(a);
        TastyToast.makeText(getApplicationContext(), getResources().getString(R.string.Login), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.F_TOKEN, token);
        editor.putString(Constants.servertoken, "no");
        editor.apply();
    }

    public void pushToken() {
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.PushToken).create(APIInteface.class);
        Firebasepost firebasepost = new Firebasepost();
        firebasepost.setClient_Id(USER_CODE11);
        firebasepost.setAppName(Constants.APPNAME);
        firebasepost.setEnterBy(USER_CODE11);
        firebasepost.setToken(newToken);
        firebasepost.setTopic(Constants.TOPIC);


        Call<FirebaseModel> responseCall = apiInterface.pushtoken(firebasepost);
        // Log.e("TAG", "PUSH TOKEN --->" + new GsonBuilder().create().toJson(firebasepost));
        //Log.e("TAG", "PUSH URL" + responseCall.request().url());
        responseCall.enqueue(new Callback<FirebaseModel>() {
            @Override
            public void onResponse(Call<FirebaseModel> call, Response<FirebaseModel> response) {
                try {
                    if (response.body().getResponseId().equalsIgnoreCase(Constants.RES0000)) {
                        // Log.e(TAG, "o n R e s p o n s e : " + response.body().getResponse());
                        Constants.PUSHNOT_FLAG = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<FirebaseModel> call, Throwable t) {
                Log.e(TAG, "o n E r r o r ---->" + t.getLocalizedMessage());

            }
        });
    }

/*    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return false;
            }
            deviceUniqueIdentifier = tm.getDeviceId();
        }

        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return deviceUniqueIdentifier;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
