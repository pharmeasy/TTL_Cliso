package com.example.e5322.thyrosoft.Registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.PincodeModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by E5322 on 19-03-2018.
 */

public class Registration_second_screen extends Activity {

    Context context;
    String getData;
    Button reg_next,pincode_ok,pincode_cancel;
    EditText reg_pincode,reg_Addr,reg_city,reg_state,reg_country;
    String pincode,adddress,state,city,country;
    TextView editTextDialogUserInput;
    private static final String TAG = Registration_second_screen.class.getSimpleName();
    boolean flag = true;
    private RequestQueue requestQueue;
    private Gson gson;
    private PincodeModel pincodeModel;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_registration_second);

        context=this;
        requestQueue = GlobalClass.setVolleyReq(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.pincode_prompt_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Registration_second_screen.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        editTextDialogUserInput = (TextView) promptsView.findViewById(R.id.editTextDialogUserInput);
        pincode_ok = (Button) promptsView.findViewById(R.id.pincode_ok);
        pincode_cancel = (Button) promptsView.findViewById(R.id.pincode_cancel);



        reg_pincode = (EditText) findViewById(R.id.reg_pincode);
        reg_Addr = (EditText) findViewById(R.id.reg_Addr);
        reg_city = (EditText) findViewById(R.id.reg_city);
        reg_state = (EditText) findViewById(R.id.reg_state);
        reg_country = (EditText) findViewById(R.id.reg_country);
        reg_next = (Button) findViewById(R.id.reg_next);

        pincode_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        pincode_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Registration_second_screen.this,
                        Registration_first_screen.class);
                startActivity(back);
            }
        });


        reg_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(Registration_second_screen.this,
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
                    TastyToast.makeText(getApplicationContext(),"Enter correct pincode", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                }

            }
        });


        reg_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pincode=reg_pincode.getText().toString();
                adddress=reg_Addr.getText().toString();
                state=reg_state.getText().toString();
                city=reg_city.getText().toString();
                country=reg_country.getText().toString();

                if(reg_pincode.getText().toString().equals("")&& reg_pincode.getText().length()<6){
                    TastyToast.makeText(getApplicationContext(),ToastFile.ent_pin, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                }else if(reg_Addr.getText().toString().equals("")&& reg_Addr.getText().length()<20){
                    TastyToast.makeText(getApplicationContext(),ToastFile.ent_addr, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                }else if(reg_state.getText().toString().equals("") &&reg_state.getText().length()<3){
                    TastyToast.makeText(getApplicationContext(),ToastFile.ent_state, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                }else if(reg_city.getText().toString().equals("") &&reg_Addr.getText().length()<3){
                    TastyToast.makeText(getApplicationContext(),ToastFile.ent_city, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                }else if(reg_country.getText().toString().equals("") && reg_Addr.getText().length()<3){
                    TastyToast.makeText(getApplicationContext(),ToastFile.ent_country, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                }else {
                    Intent sourceScreen = new Intent(Registration_second_screen.this, Registration_third_screen_Activity.class);
                    startActivity(sourceScreen);
                    SharedPreferences.Editor Registration = getSharedPreferences("Registration", 0).edit();
                    Registration.putString("pincode", pincode);
                    Registration.putString("adddress", adddress);
                    Registration.putString("state", state);
                    Registration.putString("city", city);
                    Registration.putString("country", country);
                    Registration.apply();
                }
            }
        });
    }
    private void getCityStateAPI(Editable s) {
        String url = "https://maps.google.com/maps/api/geocode/json?components=country:IN|postal_code:" + s + "&sensor=false&key=AIzaSyAd6fDdsDvtLea4BB9YPsrO69YwhvukW00";
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
                    reg_city.setText(city);
                    reg_state.setText(state);
                    reg_country.setText(country);
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

}