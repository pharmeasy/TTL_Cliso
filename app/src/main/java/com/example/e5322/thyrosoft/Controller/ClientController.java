package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.WOE.Summary_leadId;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class ClientController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Start_New_Woe start_new_woe;
    private Summary_leadId summary_leadId;
    ConnectionDetector cd;


    public ClientController(Activity mActivity, Start_New_Woe start_new_woe) {
        this.mActivity = mActivity;
        this.start_new_woe = start_new_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public ClientController(Activity mActivity, Summary_leadId summary_leadId) {
        this.mActivity = mActivity;
        this.summary_leadId = summary_leadId;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }


    public void getclientController(String url, RequestQueue requestQueue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            Log.e(TAG, "getClient URL--->"+url);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                start_new_woe.getclientResponse(response);
                            }else if (flag==1){
                                summary_leadId.getclientResponse(response);
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
            Log.e(TAG, "getclient onResponse: url" + url);


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
