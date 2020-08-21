package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.LoginResponseModel;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LoginController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mactivity;
    private Login login;
    ConnectionDetector cd;


    public LoginController(Activity activity, Login login) {
        this.mactivity = activity;
        this.login = login;
        cd = new ConnectionDetector(mactivity);
        flag = 0;
    }

    public void getlogincontroller(JSONObject jsonObject) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog barProgressDialog = GlobalClass.ShowprogressDialog(mactivity);
            com.android.volley.RequestQueue queue = GlobalClass.setVolleyReq(mactivity);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.LOGIN, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mactivity, barProgressDialog);
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        if (response != null) {
                            Gson gson = new Gson();
                            LoginResponseModel loginResponseModel = gson.fromJson(String.valueOf(response), LoginResponseModel.class);

                            if (loginResponseModel != null) {
                                login.getLoginSuccess(loginResponseModel);
                            } else {
                                GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
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
                        GlobalClass.hideProgress(mactivity, barProgressDialog);
                        GlobalClass.showVolleyError(error, mactivity);
                    } else {
                        GlobalClass.showTastyToast(mactivity, ToastFile.invalid_log, 2);
                        Log.v("TAG", error.toString());
                    }
                }
            });
            queue.add(jsonObjectRequest);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest);
            Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest);
            Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObject);
        } else {
            GlobalClass.showTastyToast(mactivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
