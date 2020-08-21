package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.Change_Passowrd;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class ForgotPassController {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Change_Passowrd change_passowrd;
    ConnectionDetector cd;


    public ForgotPassController(Activity activity, Change_Passowrd change_passowrd) {
        this.mActivity = activity;
        this.change_passowrd = change_passowrd;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void change_passowrd(JSONObject jsonObjectOtp, RequestQueue postQueOtp) {

        if (cd.isConnectingToInternet()) {
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.ForgotPass, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e(TAG, "onResponse: " + response);

                    if (response != null) {
                        if (flag == 0) {
                            change_passowrd.getResponse(response);
                        }
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        GlobalClass.showVolleyError(error, mActivity);
                    }
                }
            });
            postQueOtp.add(jsonObjectRequest1);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
            Log.e(TAG, "onClick: url" + jsonObjectRequest1);
            Log.e(TAG, "onClick: json" + jsonObjectOtp);
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
