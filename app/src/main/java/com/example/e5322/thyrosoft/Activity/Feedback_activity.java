package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.FeedbackController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.FeedbackRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.FeedbackResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Feedback_activity extends AppCompatActivity {

    public static com.android.volley.RequestQueue PostQueOtp;
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };
    ImageView cry, sad, happy;
    ImageView back, home;
    Button submitcomment;
    EditText query;
    String blockCharacterSetdata = "~#^|$%&*!+:`";
    String emoji = "";
    SharedPreferences  prefs;
    String user, passwrd, access, api_key, email_pref, mobile_pref;
    private Global globalClass;
    private String blockCharacterSet = "#$^*+-/|><";
    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSetdata.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private String feedbackText;
    private String TAG = Feedback_activity.class.getSimpleName().toString();
    Activity mactivity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);
        mactivity = Feedback_activity.this;
        initViews();
        initListner();


    }

    private void initListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Feedback_activity.this);
            }
        });


        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mactivity, ToastFile.ent_feedback, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(query, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(query, "");
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

        cry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emoji = Constants.emoji_cry;

                sad.setImageDrawable(getResources().getDrawable(R.drawable.nuetral_red_img));
                happy.setImageDrawable(getResources().getDrawable(R.drawable.happy_red_img));
                cry.setImageDrawable(getResources().getDrawable(R.drawable.neutral_shadow_img));

            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoji = Constants.emoji_sad;

                cry.setImageDrawable(getResources().getDrawable(R.drawable.sad_red_img));
                happy.setImageDrawable(getResources().getDrawable(R.drawable.happy_red_img));
                sad.setImageDrawable(getResources().getDrawable(R.drawable.sad_shadow_img));//sad_shadow_img

            }
        });

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoji = Constants.emoji_happy;

                cry.setImageDrawable(getResources().getDrawable(R.drawable.sad_red_img));
                sad.setImageDrawable(getResources().getDrawable(R.drawable.nuetral_red_img));


                happy.setImageDrawable(getResources().getDrawable(R.drawable.happy_shadow_img));
            }
        });

        submitcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getFeedback = query.getText().toString();
                Log.e(TAG, "Feedback length---->" + getFeedback.length());
                if (GlobalClass.isNull(getFeedback)) {
                    GlobalClass.showTastyToast(Feedback_activity.this, MessageConstants.Kindly_give_feedback, 2);
                } else if (getFeedback.length() > 250) {
                    GlobalClass.showTastyToast(Feedback_activity.this, MessageConstants.Kindly_give_feedback_250, 2);
                } else {
                    if (GlobalClass.isNull(emoji)) {
                        GlobalClass.showTastyToast(Feedback_activity.this, MessageConstants.PLZ_SL_emoji, 2);
                    } else {

                        SendFeedbackToAPI();


                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        cry = (ImageView) findViewById(R.id.cry);
        sad = (ImageView) findViewById(R.id.sad);
        happy = (ImageView) findViewById(R.id.happy);
        query = (EditText) findViewById(R.id.query);
        query.setFilters(new InputFilter[]{filter});
        query.setFilters(new InputFilter[]{filter1});
        query.setFilters(new InputFilter[]{EMOJI_FILTER});
        submitcomment = (Button) findViewById(R.id.submitcomment);
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        email_pref = prefs.getString("email", "");
        mobile_pref = prefs.getString("mobile_user", "");

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);


    }

    private void SendFeedbackToAPI() {


        feedbackText = query.getText().toString();

        PostQueOtp = Volley.newRequestQueue(Feedback_activity.this);
        JSONObject jsonObject = null;
        try {
            FeedbackRequestModel requestModel = new FeedbackRequestModel();
            requestModel.setApi_key(api_key);
            requestModel.setDisplay_type("null");
            requestModel.setPurpose("MOBILE APPLICATION");
            requestModel.setName(user);
            requestModel.setEmail(email_pref);
            requestModel.setMobile(mobile_pref);
            requestModel.setFeedback(feedbackText + " " + emoji);
            requestModel.setEmotion_text(emoji);
            requestModel.setRating("");
            requestModel.setPortal_type("Thyrosoft Lite");

            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            if (ControllersGlobalInitialiser.feedbackController != null) {
                ControllersGlobalInitialiser.feedbackController = null;
            }
            ControllersGlobalInitialiser.feedbackController = new FeedbackController(mactivity, Feedback_activity.this);
            ControllersGlobalInitialiser.feedbackController.getfeedbackcontroller(jsonObject, PostQueOtp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getfeedbackResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);
            FeedbackResponseModel responseModel = new Gson().fromJson(String.valueOf(response), FeedbackResponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase("RES0001")) {
                    GlobalClass.showTastyToast(mactivity, "" + responseModel.getRESPONSE(), 2);
                } else if (!GlobalClass.isNull(responseModel.getRESPONSE()) && responseModel.getRESPONSE().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Feedback_activity.this);
                } else if (!GlobalClass.isNull(responseModel.getRES_ID()) && responseModel.getRES_ID().equals("RES0000")) {
                    GlobalClass.showTastyToast(Feedback_activity.this, "" + responseModel.getRESPONSE() + " " + emoji, 1);
                    Intent i = new Intent(Feedback_activity.this, Feedback_activity.class);
                    startActivity(i);
                    finish();
                }
            } else {
                GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {

            GlobalClass.showTastyToast(mactivity, MessageConstants.Feedback_not_succuss, 2);
            e.printStackTrace();
        }
    }
}
