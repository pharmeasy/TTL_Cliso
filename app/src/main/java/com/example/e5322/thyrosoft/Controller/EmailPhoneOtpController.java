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

import org.json.JSONObject;

public class EmailPhoneOtpController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Login login;
    ConnectionDetector cd;


    public EmailPhoneOtpController(Activity mActivity, Login login) {
        this.mActivity = mActivity;
        this.login = login;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void emailphonecotroller(final JSONObject jsonObjectEmailPhoneOtp, RequestQueue queueEmailPhoneOtp) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            final JsonObjectRequest jsonObjectRequestEmailPhone = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.EmailPhoneOtp, jsonObjectEmailPhoneOtp, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response != null) {
                            if (flag == 0) {
                                login.getemailResponse(response);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        GlobalClass.hideProgress(mActivity, progressDialog);
                        GlobalClass.showVolleyError(error, mActivity);
                    }
                }
            });
            GlobalClass.volleyRetryPolicy(jsonObjectRequestEmailPhone);
            queueEmailPhoneOtp.add(jsonObjectRequestEmailPhone);
            Log.e(TAG, "onClick: " + jsonObjectEmailPhoneOtp);
            Log.e(TAG, "onClick: " + jsonObjectRequestEmailPhone);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
