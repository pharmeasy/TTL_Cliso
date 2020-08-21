package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Wind_up_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.CommonResponseModel;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

public class Multiplewindup_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Wind_up_fragment wind_up_fragment;
    ConnectionDetector cd;


    public Multiplewindup_Controller(Activity mActivity, Wind_up_fragment wind_up_fragment) {
        this.mActivity = mActivity;
        this.wind_up_fragment = wind_up_fragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void multiplewindupcontroller(JSONObject jsonObject, RequestQueue postQueOtp) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Api.multiple_windup, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: " + response);

                    GlobalClass.hideProgress(mActivity ,progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())){
                            CommonResponseModel responseModel = new Gson().fromJson(String.valueOf(response), CommonResponseModel.class);
                            if (flag==0){
                                wind_up_fragment.getwindupResponse(responseModel);
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    if (error != null) {
                    } else {
                        GlobalClass.showVolleyError(error, mActivity);
                    }
                }
            });
            postQueOtp.add(jsonObjectRequest1);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
            Log.e(TAG, "onClick: json" + jsonObject);
            Log.e(TAG, "onClick: url" + jsonObjectRequest1);
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
