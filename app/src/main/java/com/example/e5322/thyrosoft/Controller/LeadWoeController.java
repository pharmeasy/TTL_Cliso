package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.Scan_Barcode_Outlabs_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;
import com.example.e5322.thyrosoft.WOE.Summary_leadId;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class LeadWoeController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity;
    private Scan_Barcode_Outlabs scan_barcode_outlabs;
    private Summary_leadId summary_leadId;
    ConnectionDetector cd;


    public LeadWoeController(Activity mActivity, Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity) {
        this.mActivity = mActivity;
        this.scan_barcode_outlabs_activity = scan_barcode_outlabs_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public LeadWoeController(Activity mActivity, Scan_Barcode_Outlabs scan_barcode_outlabs) {
        this.mActivity = mActivity;
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public LeadWoeController(Activity mActivity, Summary_leadId summary_leadId) {
        this.mActivity = mActivity;
        this.summary_leadId = summary_leadId;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }


    public void getleadwoeController(JSONObject jsonObj, RequestQueue requestQueue) {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
        String url = Api.finalWorkOrderEntry;
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GlobalClass.hideProgress(mActivity, progressDialog);
                try {
                    if (!GlobalClass.isNull(response.toString())) {
                        if (flag == 0) {
                            scan_barcode_outlabs_activity.doleadwoe(response);
                        } else if (flag == 1) {
                            scan_barcode_outlabs.doleadwoe(response);
                        } else if (flag == 2) {
                            summary_leadId.doleadwoe(response);
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
                        if (flag == 0) {
                            scan_barcode_outlabs_activity.doleadwoeError();
                        } else if (flag == 1) {
                            scan_barcode_outlabs.doleadwoeError();
                        }

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


    }

}
