package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.example.e5322.thyrosoft.startscreen.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginValidateOTP_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Login login;
    ConnectionDetector cd;
    String comeflag;


    public LoginValidateOTP_Controller(Activity mActivity, Login login,String comeflag) {
        this.mActivity = mActivity;
        this.login = login;
        this.comeflag=comeflag;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }



    public void validateloginotp(JSONObject jsonObjectValidateOtp, RequestQueue POstQueValidateOTP) {

        if (cd.isConnectingToInternet()) {

            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
            GlobalClass.hideProgress(mActivity,progressDialog);
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.ValidateOTP, jsonObjectValidateOtp, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e(TAG, "onResponse: " + response);
                        String finalJson = response.toString();

                        if (!GlobalClass.isNull(finalJson)){
                            JSONObject parentObjectOtp = new JSONObject(finalJson);

                            if (flag==0){
                                login.getvalidatotpresponse(parentObjectOtp,comeflag);
                            }
                        }

                    } catch (JSONException e) {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.invalidOTP, 2);
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        GlobalClass.showVolleyError(error, mActivity);
                        GlobalClass.hideProgress(mActivity, progressDialog);
                    }
                }
            });
            POstQueValidateOTP.add(jsonObjectRequest1);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
            Log.e(TAG, "onClick: " + jsonObjectValidateOtp);
            Log.e(TAG, "onClick: " + jsonObjectRequest1);
            Log.e(TAG, "onClick: " + jsonObjectValidateOtp);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
