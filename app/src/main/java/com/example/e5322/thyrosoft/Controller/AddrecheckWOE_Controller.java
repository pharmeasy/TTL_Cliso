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
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.WOE.RecheckWoeActivity;
import com.example.e5322.thyrosoft.WOE.SummaryAddWoe;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class AddrecheckWOE_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private RecheckWoeActivity recheckWoeActivity;
    private SummaryAddWoe summaryAddWoe;
    ConnectionDetector cd;


    public AddrecheckWOE_Controller(Activity mActivity, RecheckWoeActivity recheckWoeActivity) {
        this.mActivity = mActivity;
        this.recheckWoeActivity = recheckWoeActivity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public AddrecheckWOE_Controller(Activity mActivity, SummaryAddWoe summaryAddWoe) {
        this.mActivity = mActivity;
        this.summaryAddWoe = summaryAddWoe;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void recheckwoeController(JSONObject jsonObjectOtp, RequestQueue POstQue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.addrecheckWOE;

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url, jsonObjectOtp, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                recheckWoeActivity.getrecheckWoeResponse(response);
                            } else if (flag == 1) {
                                summaryAddWoe.getrecheckWoeResponse(response);
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
                            GlobalClass.hideProgress(mActivity, progressDialog);
                            GlobalClass.showVolleyError(error, mActivity);
                        }
                    }
                }
            });

            POstQue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);
            Log.e(TAG, "onResponse: json" + jsonObjectRequestPop);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
