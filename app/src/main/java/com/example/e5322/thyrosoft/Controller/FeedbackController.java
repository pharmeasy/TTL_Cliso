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
import com.example.e5322.thyrosoft.Activity.Feedback_activity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class FeedbackController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Feedback_activity feedback_activity;
    ConnectionDetector cd;


    public FeedbackController(Activity mActivity, Feedback_activity feedback_activity) {
        this.mActivity = mActivity;
        this.feedback_activity = feedback_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getfeedbackcontroller(JSONObject jsonObject, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.feedback;
            Log.e(TAG, "feedback URL--->"+url);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                feedback_activity.getfeedbackResponse(response);
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

            requestQueue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, " feedback onResponse: url" + url);
            Log.e(TAG, " feedback onResponse: json" + jsonObject);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
