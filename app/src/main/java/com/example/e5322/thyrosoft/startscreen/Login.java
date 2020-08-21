package com.example.e5322.thyrosoft.startscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.CovidAccess_Controller;
import com.example.e5322.thyrosoft.Controller.EmailPhoneOtpController;
import com.example.e5322.thyrosoft.Controller.Firebasetoken_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.LoginController;
import com.example.e5322.thyrosoft.Controller.LoginOTP_Controller;
import com.example.e5322.thyrosoft.Controller.LoginValidateOTP_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.SmsListener;
import com.example.e5322.thyrosoft.Models.CovidaccessRes;
import com.example.e5322.thyrosoft.Models.FirebaseModel;
import com.example.e5322.thyrosoft.Models.RequestModels.LoginRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.LoginResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Registration.Registration_first_screen;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.multidex.BuildConfig;
import retrofit2.Response;

public class Login extends Activity implements View.OnClickListener {

    public static com.android.volley.RequestQueue PostQue;
    public static com.android.volley.RequestQueue generateOTP, POstQueValidateOTP, POstQueValidateOTPForgotPassword;
    public static com.android.volley.RequestQueue PostQueEmailPhoneOtp;
    EditText username, password, otpmobile, otpemail;
    TextView forgotpassword, registration;
    Button login, generateotp;
    String  getOTPNO, numberforgotpass, regmobile,  RESPONSE, RES_ID, VALID, USER_TYPE, SENDERID, OTPNO, User, version, emailPattern, pass, macAddress, USER_CODE11;
    SharedPreferences.Editor editor;
    String emailIdValidateOTP, isMobileEmailValidValidateOTP, mobileNoValidateOTP, otpNoValidateOTP, resIdValidateOTP, responseValidateOTP;
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
    private String androidOS;
    Activity activity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;

        initViews();
        initListner();
    }

    private void initListner() {
        login.setOnClickListener(Login.this);
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

                    GlobalClass.SetEditText(password, "");
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
                    GlobalClass.showTastyToast(Login.this,
                            ToastFile.crt_pwd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(password, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(password, "");
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
                            GlobalClass.showTastyToast(Login.this,
                                    ToastFile.crt_mob_num,
                                    2);
                            if (enteredString.length() > 0) {
                                GlobalClass.SetEditText(edt_mobile_otp, enteredString.substring(1));
                            } else {
                                GlobalClass.SetEditText(edt_mobile_otp, "");
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
                                        GlobalClass.SetEditText(edt_reg_otp, messageText);
                                        if (!GlobalClass.isNull(numberforgotpass) && !GlobalClass.isNull(messageText) &&!numberforgotpass.equals(messageText)) {
                                            GlobalClass.showTastyToast(activity, ToastFile.crt_otp, 4);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            reg_otp_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ( !GlobalClass.isNull(edt_reg_otp.getText().toString())) {
                                        POstQueValidateOTPForgotPassword = Volley.newRequestQueue(Login.this);

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

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (ControllersGlobalInitialiser.loginValidateOTP_controller != null) {
                                                ControllersGlobalInitialiser.loginValidateOTP_controller = null;
                                            }
                                            ControllersGlobalInitialiser.loginValidateOTP_controller = new LoginValidateOTP_Controller(activity, Login.this,"1");
                                            ControllersGlobalInitialiser.loginValidateOTP_controller.validateloginotp(jsonObjectValidateOtp, POstQueValidateOTP);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        GlobalClass.showTastyToast(activity, ToastFile.crt_mob_num, 4);
                                    }
                                }
                            });


                        } else {
                            GlobalClass.showTastyToast(Login.this, ToastFile.crt_mob_num, 2);
                        }

                    }
                });

            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(Login.this);
                View promptsView = li.inflate(R.layout.activity_registration, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        Login.this);
                alertDialogBuilder.setView(promptsView);

                alertDialog = alertDialogBuilder.create();

                alertDialog.setCanceledOnTouchOutside(false);
                otpmobile = (EditText) promptsView.findViewById(R.id.otpmobile);
                otpemail = (EditText) promptsView.findViewById(R.id.otpemail);
                generateotp = (Button) promptsView.findViewById(R.id.generateotp);
                alertDialog.show();
                otpmobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        String enteredString = s.toString();
                        if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2") || enteredString.startsWith("3")
                                || enteredString.startsWith("4") || enteredString.startsWith("5") || enteredString.startsWith("6")) {
                            GlobalClass.showTastyToast(Login.this,
                                    ToastFile.crt_mob_num,
                                    2);
                            if (enteredString.length() > 0) {
                                GlobalClass.SetEditText(otpmobile, enteredString.substring(1));
                            } else {
                                GlobalClass.SetEditText(otpmobile, "");
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
                        if (GlobalClass.isNull(otpmobile.getText().toString())) {
                            GlobalClass.showTastyToast(Login.this, ToastFile.crt_mob_num_eml, 2);
                        } else if (regmobile.length() < 10) {
                            GlobalClass.showTastyToast(Login.this, ToastFile.crt_mob_num, 2);
                        } else if (!email.matches(emailPattern)) {
                            GlobalClass.showTastyToast(Login.this, ToastFile.crt_eml, 2);
                        } else {
                            PostQueEmailPhoneOtp = Volley.newRequestQueue(Login.this);

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
                            RequestQueue queueEmailPhoneOtp = Volley.newRequestQueue(Login.this);

                            try {
                                if (ControllersGlobalInitialiser.emailPhoneOtpController != null) {
                                    ControllersGlobalInitialiser.emailPhoneOtpController = null;
                                }
                                ControllersGlobalInitialiser.emailPhoneOtpController = new EmailPhoneOtpController(activity, Login.this);
                                ControllersGlobalInitialiser.emailPhoneOtpController.emailphonecotroller(jsonObjectEmailPhoneOtp, queueEmailPhoneOtp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forgotpassword = (TextView) findViewById(R.id.forgotpass);
        registration = (TextView) findViewById(R.id.registration);
        login = (Button) findViewById(R.id.login);

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
    }


    private void sendOtp() {

        generateOTP = GlobalClass.setVolleyReq(Login.this);
        JSONObject jsonObjectOtp = new JSONObject();
        try {

            jsonObjectOtp.put("apikey", "jLmjR1POZBbn@TVVzb0uJvpTMeBbnNwR3lCNnqI)Ivk=");
            jsonObjectOtp.put("type", "SENDOTP");
            jsonObjectOtp.put("mobile", getPhone_num);
            jsonObjectOtp.put("otpno", randomNumber);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            if (ControllersGlobalInitialiser.loginOTP_controller != null) {
                ControllersGlobalInitialiser.loginOTP_controller = null;
            }
            ControllersGlobalInitialiser.loginOTP_controller = new LoginOTP_Controller(activity, Login.this);
            ControllersGlobalInitialiser.loginOTP_controller.getloginotpcontroller(jsonObjectOtp, generateOTP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateRandomString() {
        randomNumber = random.nextInt((endRange - startRange) + startRange) + startRange + "";
    }

    @Override
    public void onClick(View v) {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifi.getConnectionInfo();
        macAddress = wInfo.getMacAddress();


//        barProgressDialog = new ProgressDialog(Login.this);
//        barProgressDialog.setTitle("Kindly wait ...");
//        barProgressDialog.setMessage(ToastFile.processing_request);
//        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
//        barProgressDialog.setProgress(0);
//        barProgressDialog.setMax(20);
//        barProgressDialog.show();
//        barProgressDialog.setCanceledOnTouchOutside(false);
//        barProgressDialog.setCancelable(false);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // Here you should write your time consuming task...
//                    while (barProgressDialog.getProgress() <= barProgressDialog.getMax()) {
//                        Thread.sleep(2000);
//                        updateBarHandler.post(new Runnable() {
//                            public void run() {
//                                barProgressDialog.incrementProgressBy(2);
//                            }
//                        });
//                        if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
//                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
//                                barProgressDialog.dismiss();
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//        }).start();

        User = username.getText().toString();
        pass = password.getText().toString();
        if (User.length() == 0 & pass.length() == 0) {
            GlobalClass.showTastyToast(this, "Please Enter Ecode and Password", 2);
        } else if (User.length() == 0) {
            GlobalClass.showTastyToast(this, "Please enter your Ecode", 2);

        } else if (pass.length() == 0) {
            GlobalClass.showTastyToast(this, "Please enter your Password", 2);

        } else {
            try {
                PostQue = Volley.newRequestQueue(Login.this);
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

                try {
                    if (ControllersGlobalInitialiser.loginController != null) {
                        ControllersGlobalInitialiser.loginController = null;
                    }
                    ControllersGlobalInitialiser.loginController = new LoginController(activity, Login.this);
                    ControllersGlobalInitialiser.loginController.getlogincontroller(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                GlobalClass.showTastyToast(this, MessageConstants.URL_is_notfound, 2);
            }
        }
    }

    private void checkcovidaccess() {
        try {
            if (ControllersGlobalInitialiser.covidAccess_controller != null) {
                ControllersGlobalInitialiser.covidAccess_controller = null;
            }
            ControllersGlobalInitialiser.covidAccess_controller = new CovidAccess_Controller(activity, Login.this);
            ControllersGlobalInitialiser.covidAccess_controller.getcovidaccessController(User);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.F_TOKEN, token);
        editor.putString(Constants.servertoken, "no");
        editor.apply();
    }

    public void pushToken() {

        try {
            if (ControllersGlobalInitialiser.firebasetoken_controller != null) {
                ControllersGlobalInitialiser.firebasetoken_controller = null;
            }
            ControllersGlobalInitialiser.firebasetoken_controller = new Firebasetoken_Controller(activity, Login.this);
            ControllersGlobalInitialiser.firebasetoken_controller.Pushfirebasetoken(USER_CODE11, newToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void getpushtokenResponse(FirebaseModel body) {
        if (body != null && !GlobalClass.isNull(body.getResponseId()) &&
                body.getResponseId().equalsIgnoreCase(Constants.RES0000)) {
            Constants.PUSHNOT_FLAG = false;
        }
    }

    public void getcovidaccResponse(Response<CovidaccessRes> response) {
        SharedPreferences.Editor editor = getSharedPreferences("CovidAccess_sync", 0).edit();
        editor.putLong("PreivousTimeOfSync", System.currentTimeMillis()); // add this line and comment below line for cache
        editor.commit();
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResponse()) && response.body().getResponse().equalsIgnoreCase("True")) {
                editor = getSharedPreferences("COVIDETAIL", 0).edit();
                editor.putBoolean("covidacc", true);
                editor.commit();
            } else {
                editor = getSharedPreferences("COVIDETAIL", 0).edit();
                editor.putBoolean("covidacc", false);
                editor.commit();
            }
            int versionCode = BuildConfig.VERSION_CODE;
            SharedPreferences pref_versioncheck = activity.getSharedPreferences("pref_versioncheck", MODE_PRIVATE);
            SharedPreferences.Editor editorversioncode = pref_versioncheck.edit();
            editorversioncode.putInt("versioncode", versionCode);
            editorversioncode.apply();

            Intent a = new Intent(Login.this, ManagingTabsActivity.class);
            a.putExtra(Constants.COMEFROM, true);
            startActivity(a);
            GlobalClass.showTastyToast(activity, getResources().getString(R.string.Login), 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getLoginSuccess(LoginResponseModel loginResponseModel) {
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
                editor.apply();

                checkcovidaccess();

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

                new LogUserActivityTagging(activity, Constants.SHLOGIN);

            } else {
                GlobalClass.showTastyToast(activity, loginResponseModel.getRESPONSE(), 2);
            }
        } else {
            GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
        }

    }

    public void getloginotp(JSONObject parentObjectOtp) {
        try {
            OTPNO = parentObjectOtp.getString("OTPNO");
            RESPONSE = parentObjectOtp.getString("RESPONSE");
            RES_ID = parentObjectOtp.getString("RES_ID");
            SENDERID = parentObjectOtp.getString("SENDERID");
            USER_TYPE = parentObjectOtp.getString("USER_TYPE");
            VALID = parentObjectOtp.getString("VALID");

            if (!GlobalClass.isNull(RES_ID) && RES_ID.equals("RES0000")) {
                GlobalClass.showTastyToast(Login.this, "" + RESPONSE, 1);

            } else if (!GlobalClass.isNull(RES_ID) && RES_ID.equals("RES0000")) {
                GlobalClass.showTastyToast(Login.this, "" + RESPONSE, 2);
            }

        } catch (JSONException e) {
            GlobalClass.showTastyToast(Login.this, MessageConstants.FEEDBACKMSG, 2);
            e.printStackTrace();
        }
    }

    public void getemailResponse(JSONObject response) {
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
            if (!GlobalClass.isNull(isMobileEmailValid) && isMobileEmailValid.equalsIgnoreCase(MessageConstants.YES)) {
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
                            GlobalClass.SetEditText(edt_reg_otp, messageText);
                            if (!number.equals(messageText)) {
                                GlobalClass.showTastyToast(activity, ToastFile.crt_otp, 4);
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
                            GlobalClass.showTastyToast(Login.this, ToastFile.crt_otp, 2);
                        } else {
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

                            try {
                                if (ControllersGlobalInitialiser.loginValidateOTP_controller != null) {
                                    ControllersGlobalInitialiser.loginValidateOTP_controller = null;
                                }
                                ControllersGlobalInitialiser.loginValidateOTP_controller = new LoginValidateOTP_Controller(activity, Login.this,"2");
                                ControllersGlobalInitialiser.loginValidateOTP_controller.validateloginotp(jsonObjectValidateOtp, POstQueValidateOTP);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                alertDialog.dismiss();

            } else {
                GlobalClass.showTastyToast(activity, "" + response1, 2);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getvalidatotpresponse(JSONObject parentObjectOtp, String comeflag) {
        try {
            emailIdValidateOTP = parentObjectOtp.getString("emailId");
            isMobileEmailValidValidateOTP = parentObjectOtp.getString("isMobileEmailValid");
            mobileNoValidateOTP = parentObjectOtp.getString("mobileNo");
            otpNoValidateOTP = parentObjectOtp.getString("otpNo");
            resIdValidateOTP = parentObjectOtp.getString("resId");
            responseValidateOTP = parentObjectOtp.getString("response");
            if (!GlobalClass.isNull(responseValidateOTP) && responseValidateOTP.equals("OTP Validated Sucessfully")) {
                Intent reg = new Intent(Login.this, Registration_first_screen.class);
                startActivity(reg);

            } else if (!GlobalClass.isNull(responseValidateOTP)) {
                GlobalClass.showTastyToast(Login.this, MessageConstants.invalidOTP, 2);
            } else {
                GlobalClass.showTastyToast(Login.this, MessageConstants.invalidOTP, 2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
