package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.example.e5322.thyrosoft.startscreen.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginOTP_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mactivity;
    private Login login;
    ConnectionDetector cd;


    public LoginOTP_Controller(Activity activity, Login login) {
        this.mactivity = activity;
        this.login = login;
        cd = new ConnectionDetector(mactivity);
        flag = 0;
    }

    public void getloginotpcontroller(JSONObject jsonObjectOtp,com.android.volley.RequestQueue generateOTP) {

        if (cd.isConnectingToInternet()) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.OTP, jsonObjectOtp, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String finalJson = response.toString();
                    try {
                        JSONObject parentObjectOtp = new JSONObject(finalJson);
                        if (parentObjectOtp!=null){
                            if (flag==0){
                                login.getloginotp(parentObjectOtp);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        GlobalClass.showVolleyError(error, mactivity);
                    }
                }
            });

            generateOTP.add(jsonObjectRequest);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        } else {
            GlobalClass.showTastyToast(mactivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
