package com.example.e5322.thyrosoft.Registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.PincodeModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.Nullable;

/**
 * Created by E5322 on 19-03-2018.
 */

public class Registration_second_screen extends Activity {

    Context context;
    Button reg_next,pincode_ok,pincode_cancel;
    EditText reg_pincode,reg_Addr,reg_city,reg_state,reg_country;
    String pincode,adddress,state,city,country;
    TextView editTextDialogUserInput;
    private static final String TAG = Registration_second_screen.class.getSimpleName();
    private RequestQueue requestQueue;
    private Gson gson;
    private PincodeModel pincodeModel;
    AlertDialog alertDialog;
    Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_registration_second);
        context=this;
        mActivity=Registration_second_screen.this;
        requestQueue = GlobalClass.setVolleyReq(context);

        initViews();
        initListner();

    }

    private void initListner() {
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
                    GlobalClass.showTastyToast(Registration_second_screen.this,
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
                //getData = reg_pincode.getText().toString();
                if(s.length()==6){

                    getCityStateAPI(s);



                }else {
                    GlobalClass.showTastyToast((Activity) context,MessageConstants.ENTER_PINCODE,2);
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
                    GlobalClass.showTastyToast((Activity)context,ToastFile.ent_pin, 2);

                }else if(reg_Addr.getText().toString().equals("")&& reg_Addr.getText().length()<20){
                    GlobalClass.showTastyToast((Activity)context,ToastFile.ent_addr, 2);

                }else if(reg_state.getText().toString().equals("") &&reg_state.getText().length()<3){
                    GlobalClass.showTastyToast((Activity)context,ToastFile.ent_state, 2);

                }else if(reg_city.getText().toString().equals("") &&reg_Addr.getText().length()<3){
                    GlobalClass.showTastyToast((Activity)context,ToastFile.ent_city, 2);

                }else if(reg_country.getText().toString().equals("") && reg_Addr.getText().length()<3){
                    GlobalClass.showTastyToast((Activity)context,ToastFile.ent_country,2);

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

    private void initViews() {
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
    }

    private void getCityStateAPI(Editable s) {
        String url = "https://maps.google.com/maps/api/geocode/json?components=country:IN|postal_code:" + s + "&sensor=false&key=AIzaSyAd6fDdsDvtLea4BB9YPsrO69YwhvukW00";
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

            try {
                if (!GlobalClass.isNull(pincodeModel.getStatus()) && pincodeModel.getStatus().equalsIgnoreCase("OK")) {
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
                    GlobalClass.showTastyToast(mActivity, "pincode_not_found", 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof TimeoutError) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Time_error, 2);
            } else if (error instanceof ServerError) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Server_error, 2);
            } else if (error instanceof NetworkError) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Network_error, 2);
            } else if (error instanceof ParseError) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Parse_error, 2);
            } else if (error instanceof NoConnectionError) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.NoConnection_error, 2);
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG,2);
            }
        }
    };

}