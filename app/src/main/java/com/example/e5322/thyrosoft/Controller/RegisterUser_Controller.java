package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Registration.Registration_third_screen_Activity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class RegisterUser_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Registration_third_screen_Activity registration_third_screen_activity;
    ConnectionDetector cd;


    public RegisterUser_Controller(Activity mActivity, Registration_third_screen_Activity registration_third_screen_activity) {
        this.mActivity = mActivity;
        this.registration_third_screen_activity = registration_third_screen_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void registerusercontroller(JSONObject jsonObjectRegister_User, RequestQueue postQueRegistration) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
            Log.e(TAG, "json object --->" + jsonObjectRegister_User.toString());
            JsonObjectRequest jsonObjectRequestEmailPhone = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.Register_User, jsonObjectRegister_User, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    try {
                        Log.e(TAG, "onResponse: " + response);
                        String finalJson = response.toString();
                        if (!GlobalClass.isNull(finalJson)) {
                            registration_third_screen_activity.getregisterUserResponse(finalJson);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        GlobalClass.showTastyToast(mActivity, "" + error.toString(), 2);
                        GlobalClass.hideProgress(mActivity,progressDialog);
                    } else {

                        Log.v("TAG", error.toString());

                    }
                }
            });
            postQueRegistration.add(jsonObjectRequestEmailPhone);
            Log.e(TAG, "registerUser: json" + jsonObjectRegister_User);
            Log.e(TAG, "registerUser: url" + jsonObjectRequestEmailPhone);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestEmailPhone);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
