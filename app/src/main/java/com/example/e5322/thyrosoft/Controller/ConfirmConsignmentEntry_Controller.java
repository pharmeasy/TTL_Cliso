package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Wind_up_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class ConfirmConsignmentEntry_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Wind_up_fragment wind_up_fragment;
    ConnectionDetector cd;


    public ConfirmConsignmentEntry_Controller(Activity mActivity, Wind_up_fragment wind_up_fragment) {
        this.mActivity = mActivity;
        this.wind_up_fragment = wind_up_fragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getconfirmcongsgment(String user, RequestQueue requestQueue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.consignmentperday + user + "/ConfirmConsignmentEntry";
            Log.e(TAG, "Consigment API-->" + url);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                wind_up_fragment.getcongsigmentResp(response);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            // Show timeout error message
                            GlobalClass.hideProgress(mActivity, progressDialog);
                            GlobalClass.showVolleyError(error, mActivity);
                        }
                    }
                }
            });

            requestQueue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);
            Log.e(TAG, "onResponse: json" + jsonObjectRequestPop);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}