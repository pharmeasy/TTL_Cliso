package com.example.e5322.thyrosoft.Registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.PincodeModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Registration_simplified_page extends AppCompatActivity {
    EditText reg_name, reg_address, reg_pincode, reg_mobilenumber, reg_emailAdd;
    Button next;
    String reg_name_shared, reg_address1, reg_pincode1, reg_mobilenumber1, reg_emailAdd1;
    Context context;
    String state, city, country;
    private static final String TAG = Registration_simplified_page.class.getSimpleName();
    private Gson gson;
    private PincodeModel pincodeModel;
    private RequestQueue requestQueue;
    Activity mActivity;
    private String email;
    private String emailPattern;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_simplified_page);

        mActivity = Registration_simplified_page.this;
        initViews();
        initListner();
    }

    private void initListner() {
        reg_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    GlobalClass.showTastyToast((Activity) context,
                            ToastFile.ent_pin,
                            2);
                    if (enteredString.length() > 0) {
                        reg_pincode.setText(enteredString.substring(1));
                    } else {
                        reg_pincode.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    getCityStateAPI(s);
                } else {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.ent_pin, 2);
                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reg_name.getText().toString().equalsIgnoreCase("") && reg_name.getText().length() < 5) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.ent_name, 2);

                } else if (reg_address.getText().toString().equalsIgnoreCase("")) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.min20char, 2);

                } else if (reg_address.getText().length() < 20) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.min20char, 2);

                } else if (reg_pincode.getText().toString().equalsIgnoreCase("") && reg_pincode.getText().length() < 6) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.ent_pin, 2);

                } else if (reg_mobilenumber.getText().toString().equalsIgnoreCase("") && reg_mobilenumber.getText().length() < 10) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.crt_mob_num, 2);

                } else if (reg_emailAdd.getText().toString().equalsIgnoreCase("") && reg_emailAdd.getText().length() < 5) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.crt_eml, 2);
                }
                if (!email.matches(emailPattern)) {
                    GlobalClass.showTastyToast((Activity) context, ToastFile.crt_eml, 2);
                } else {

                    reg_name_shared = reg_name.getText().toString();
                    reg_address1 = reg_address.getText().toString();
                    reg_pincode1 = reg_pincode.getText().toString();
                    reg_mobilenumber1 = reg_mobilenumber.getText().toString();
                    reg_emailAdd1 = reg_emailAdd.getText().toString();

                    Intent reg = new Intent((Activity) context, Registration_second_screen.class);
                    startActivity(reg);

                    SharedPreferences.Editor Registration = getSharedPreferences("Registration", 0).edit();
                    Registration.putString("name_user", reg_name_shared);
                    Registration.putString("reg_address", reg_address1);
                    Registration.putString("reg_pincode", reg_pincode1);
                    Registration.putString("reg_mobilenumber", reg_mobilenumber1);
                    Registration.putString("reg_emailAdd", reg_emailAdd1);
                    Registration.apply();
                }
            }
        });
    }

    private void initViews() {
        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_address = (EditText) findViewById(R.id.reg_address);
        reg_pincode = (EditText) findViewById(R.id.reg_pincode);
        reg_mobilenumber = (EditText) findViewById(R.id.reg_mobilenumber);
        reg_emailAdd = (EditText) findViewById(R.id.reg_emailAdd);
        next = (Button) findViewById(R.id.reg_next);
        email = reg_emailAdd.getText().toString().trim();

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";//[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+

        context = this;
        requestQueue = GlobalClass.setVolleyReq(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

    }

    private void getCityStateAPI(Editable s) {
        String url = "https://maps.google.com/maps/api/geocode/json?components=country:IN|postal_code:" + s + "&sensor=false&key=AIzaSyBcm2CJDh69483Z9dZrSNRqjX1l-nuNo-o";
        Log.e(TAG, "PincodeAPI-url:- " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        GlobalClass.volleyRetryPolicy(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            pincodeModel = new PincodeModel();
            pincodeModel = gson.fromJson(response, PincodeModel.class);
            if (!GlobalClass.isNull(pincodeModel.getStatus()) && pincodeModel.getStatus().equalsIgnoreCase("OK")) {
                Log.e(TAG, "Status:- " + pincodeModel.getStatus());
                Log.e(TAG, "Formatted Address:- " + pincodeModel.getResults().get(0).getFormatted_address());
                if (pincodeModel.getResults().get(0).getAddress_components().get(1).getTypes().contains("locality") &&
                        pincodeModel.getResults().get(0).getAddress_components().get(3).getTypes().contains("administrative_area_level_1") &&
                        pincodeModel.getResults().get(0).getAddress_components().get(4).getTypes().contains("country")) {
                    Log.e(TAG, "City name:- " + pincodeModel.getResults().get(0).getAddress_components().get(1).getLong_name());
                    Log.e(TAG, "State name:- " + pincodeModel.getResults().get(0).getAddress_components().get(3).getLong_name());
                    Log.e(TAG, "Country name:- " + pincodeModel.getResults().get(0).getAddress_components().get(4).getLong_name());
                    city = pincodeModel.getResults().get(0).getAddress_components().get(1).getLong_name();
                    state = pincodeModel.getResults().get(0).getAddress_components().get(3).getLong_name();
                    country = pincodeModel.getResults().get(0).getAddress_components().get(4).getLong_name();
                    GlobalClass.showTastyToast((Activity) context, "" + city + state + country, 1);
                }
            } else {
                GlobalClass.showTastyToast((Activity) context, MessageConstants.ENTER_PINCODE, 2);
            }
        }

    };
    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            GlobalClass.showVolleyError(error, mActivity);
        }
    };


    @Override
    public void onBackPressed() {

        Intent i = new Intent(mActivity, Login.class);
        startActivity(i);
        super.onBackPressed();
    }
}
