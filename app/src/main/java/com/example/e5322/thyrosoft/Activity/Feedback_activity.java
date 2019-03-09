package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Feedback_activity extends AppCompatActivity {
    ImageView cry, sad, happy;
    View view;
    ImageView back,home;
    ProgressDialog barProgressDialog;
    //    LinearLayout sad_cry_layout;
//    EditText editremark;
    String RESPONSE, RES_ID, USER_TYPE;
    Button submitcomment;
    private Global globalClass;
    EditText query;
    public static com.android.volley.RequestQueue PostQueOtp;

    private String blockCharacterSet = "#$^*+-/|><";

    String blockCharacterSetdata = "~#^|$%&*!+:`";
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


    String emoji = "";
    SharedPreferences sharedpreferences, prefs;
    String user, passwrd, access, api_key, email_pref, mobile_pref;
    private String feedbackText;
    private String TAG=Feedback_activity.class.getSimpleName().toString();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feedback);

        cry = (ImageView) findViewById(R.id.cry);
        sad = (ImageView) findViewById(R.id.sad);
        happy = (ImageView) findViewById(R.id.happy);
        if (globalClass.checkForApi21()) {    Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));}

//       excited= (ImageView)findViewById(R.id.excited);
//        sad_cry_layout=(LinearLayout)findViewById(R.id.sad_cry_layout);
//        editremark=(EditText) findViewById(R.id.editremark);
        query = (EditText) findViewById(R.id.query);
//        query.setFilters(new InputFilter[] { filter });
        query.setFilters(new InputFilter[]{filter});
        query.setFilters(new InputFilter[]{filter1});
        query.setFilters(new InputFilter[]{EMOJI_FILTER});
        submitcomment = (Button) findViewById(R.id.submitcomment);
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        email_pref = prefs.getString("email", null);
        mobile_pref = prefs.getString("mobile_user", null);

        back=(ImageView)findViewById(R.id.back);
        home=(ImageView)findViewById(R.id.home);



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
                    Toast.makeText(Feedback_activity.this,
                            ToastFile.ent_feedback,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        query.setText(enteredString.substring(1));
                    } else {
                        query.setText("");
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
                if(getFeedback.equals("")){
                    Toast.makeText(Feedback_activity.this, "Kindly give the feedback for app", Toast.LENGTH_SHORT).show();
                }else{
                    if (emoji.equals("")) {
                        Toast.makeText(Feedback_activity.this, "Please select emoji", Toast.LENGTH_SHORT).show();
                    } else {
                        SendFeedbackToAPI();
                    }
                }
            }
        });
    }
    private void SendFeedbackToAPI() {
        barProgressDialog = new ProgressDialog(Feedback_activity.this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        feedbackText = query.getText().toString();

        PostQueOtp = Volley.newRequestQueue(Feedback_activity.this);
        JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("api_key", api_key);
            jsonObjectOtp.put("display_type", "null");
            jsonObjectOtp.put("purpose", "MOBILE APPLICATION");
            jsonObjectOtp.put("name", user);
            jsonObjectOtp.put("email", email_pref);
            jsonObjectOtp.put("mobile", mobile_pref);
            jsonObjectOtp.put("feedback", feedbackText +" "+ emoji);
            jsonObjectOtp.put("emotion_text", emoji);
            jsonObjectOtp.put("rating", "");
            jsonObjectOtp.put("portal_type", "Thyrosoft Lite");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.feedback, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e(TAG, "onResponse: "+response );
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                    RESPONSE = parentObjectOtp.getString("RESPONSE");
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    USER_TYPE = parentObjectOtp.getString("USER_TYPE");

                    if (RES_ID.equals("RES0001")) {
                        TastyToast.makeText(Feedback_activity.this, "" + RESPONSE, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }else if(RESPONSE.equalsIgnoreCase(caps_invalidApikey)){
                        GlobalClass.redirectToLogin(Feedback_activity.this);
                    } else if (RES_ID.equals("RES0000")) {
                        TastyToast.makeText(Feedback_activity.this, "" + RESPONSE + " " + emoji, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent i = new Intent(Feedback_activity.this, Feedback_activity.class);
                        startActivity(i);
                        finish();

                    }

                } catch (JSONException e) {
                    TastyToast.makeText(Feedback_activity.this, "Feedback not sent successfully", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        PostQueOtp.add(jsonObjectRequest1);
        Log.e(TAG, "SendFeedbackToAPI: json"+jsonObjectOtp );
        Log.e(TAG, "SendFeedbackToAPI: url"+jsonObjectRequest1 );

    }

}
