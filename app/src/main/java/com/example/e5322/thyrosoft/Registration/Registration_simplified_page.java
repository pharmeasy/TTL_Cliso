package com.example.e5322.thyrosoft.Registration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.PincodeModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

public class Registration_simplified_page extends AppCompatActivity {
    EditText reg_name,reg_address,reg_pincode,reg_mobilenumber,reg_emailAdd;
    Button next;
    String reg_name_shared, reg_address1, reg_pincode1, reg_mobilenumber1, reg_emailAdd1;


    Context context;

    String state,city,country;
    private static final String TAG = Registration_simplified_page.class.getSimpleName();

    private Gson gson;
    private PincodeModel pincodeModel;
    AlertDialog alertDialog;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_simplified_page);

        reg_name =(EditText)findViewById(R.id.reg_name);
        reg_address =(EditText)findViewById(R.id.reg_address);
        reg_pincode =(EditText)findViewById(R.id.reg_pincode);
        reg_mobilenumber =(EditText)findViewById(R.id.reg_mobilenumber);
        reg_emailAdd =(EditText)findViewById(R.id.reg_emailAdd);
        next=(Button)findViewById(R.id.reg_next);
        final String email = reg_emailAdd.getText().toString().trim();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";//[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+

        context=this;
        requestQueue = GlobalClass.setVolleyReq(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

//        requestQueue = Volley.newRequestQueue(Registration_simplified_page.this);
        reg_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(Registration_simplified_page.this,
                            ToastFile.ent_pin,
                            Toast.LENGTH_SHORT).show();
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
                //getData = reg_pincode.getText().toString();
                if(s.length()==6){

                    getCityStateAPI(s);



                }else {
                    TastyToast.makeText(getApplicationContext(),ToastFile.ent_pin, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_name.getText().toString().equals("") && reg_name.getText().length()<5){
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.ent_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_address.getText().toString().equals("")){
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.min20char, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_address.getText().length()<20){
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.min20char, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_pincode.getText().toString().equals("")&& reg_pincode.getText().length()<6){
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.ent_pin, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_mobilenumber.getText().toString().equals("")&& reg_mobilenumber.getText().length()<10){
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else if(reg_emailAdd.getText().toString().equals("")&& reg_emailAdd.getText().length()<5){
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.crt_eml, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }  if (!email.matches(emailPattern)) {
                    TastyToast.makeText(Registration_simplified_page.this,ToastFile.crt_eml, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }else{

                    reg_name_shared                  =reg_name.getText().toString();
                    reg_address1                     =reg_address.getText().toString();
                    reg_pincode1            =reg_pincode.getText().toString();
                    reg_mobilenumber1         =reg_mobilenumber.getText().toString();
                    reg_emailAdd1          =reg_emailAdd.getText().toString();

                    Intent reg = new Intent(Registration_simplified_page.this, Registration_second_screen.class);
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

    private void getCityStateAPI(Editable s) {
        String url = "https://maps.google.com/maps/api/geocode/json?components=country:IN|postal_code:" + s + "&sensor=false&key=AIzaSyBcm2CJDh69483Z9dZrSNRqjX1l-nuNo-o";
        Log.e(TAG, "PincodeAPI-url:- " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, onPostsLoaded, onPostsError);
        requestQueue.add(request);
        RetryPolicy policy = new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }
    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
//            Log.e(TAG, "PincodeAPI Response:- " + response);
            pincodeModel = new PincodeModel();
            pincodeModel = gson.fromJson(response, PincodeModel.class);
            if (pincodeModel.getStatus().equals("OK")) {
                Log.e(TAG, "Status:- " + pincodeModel.getStatus());
                Log.e(TAG, "Formatted Address:- " + pincodeModel.getResults().get(0).getFormatted_address());
                if (pincodeModel.getResults().get(0).getAddress_components().get(1).getTypes().contains("locality") &&
                        pincodeModel.getResults().get(0).getAddress_components().get(3).getTypes().contains("administrative_area_level_1")&&
                        pincodeModel.getResults().get(0).getAddress_components().get(4).getTypes().contains("country")) {
                    Log.e(TAG, "City name:- " + pincodeModel.getResults().get(0).getAddress_components().get(1).getLong_name());
                    Log.e(TAG, "State name:- " + pincodeModel.getResults().get(0).getAddress_components().get(3).getLong_name());
                    Log.e(TAG, "Country name:- " + pincodeModel.getResults().get(0).getAddress_components().get(4).getLong_name());
                    city = pincodeModel.getResults().get(0).getAddress_components().get(1).getLong_name();
                    state = pincodeModel.getResults().get(0).getAddress_components().get(3).getLong_name();
                    country=pincodeModel.getResults().get(0).getAddress_components().get(4).getLong_name();
                    Toast.makeText(context, ""+city+state+country, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "pincode_not_found", Toast.LENGTH_SHORT).show();
            }
        }

    };
    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
            if (error instanceof TimeoutError) {
                Log.e(TAG, "onErrorResponse: TimeoutError");
                Toast.makeText(context, "Timeout Error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Log.e(TAG, "onErrorResponse: ServerError");
                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Log.e(TAG, "onErrorResponse: NetworkError");
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Log.e(TAG, "onErrorResponse: ParseError");
                Toast.makeText(context, "Parse Error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Log.e(TAG, "onErrorResponse: NoConnectionError");
                Toast.makeText(context, "NoConnection Error", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onErrorResponse: error  not found " + error);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    public void onBackPressed() {

        Intent i = new Intent(Registration_simplified_page.this, Login.class);
        startActivity(i);
        super.onBackPressed();
    }
}
