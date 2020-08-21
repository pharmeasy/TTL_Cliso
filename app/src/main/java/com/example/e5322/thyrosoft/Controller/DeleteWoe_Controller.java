package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.SummaryActivity_New;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Summary_Activity_WOE;
import com.example.e5322.thyrosoft.WOE.SummaryActivity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class DeleteWoe_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private PatientDtailsWoe patientDtailsWoe;
    private SummaryActivity_New summaryActivity_new;
    private Summary_Activity_WOE summary_activity_woe;
    private SummaryActivity summaryActivity;
    ConnectionDetector cd;
    String response1;

    public DeleteWoe_Controller(Activity activity, PatientDtailsWoe patientDtailsWoe) {
        this.mActivity = activity;
        this.patientDtailsWoe = patientDtailsWoe;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public DeleteWoe_Controller(Activity activity, SummaryActivity_New summaryActivity_new) {
        this.mActivity = activity;
        this.summaryActivity_new = summaryActivity_new;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public DeleteWoe_Controller(Activity activity, Summary_Activity_WOE summary_activity_woe) {
        this.mActivity = activity;
        this.summary_activity_woe = summary_activity_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public DeleteWoe_Controller(Activity activity, SummaryActivity summaryActivity) {
        this.mActivity = activity;
        this.summaryActivity = summaryActivity;
        cd = new ConnectionDetector(mActivity);
        flag = 3;
    }


    public void deletewoe(JSONObject jsonObjectOtp, RequestQueue deletePatienDetail) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.deleteWOE, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse: " + response);
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    if (!GlobalClass.isNull(response.toString())) {
                        if (flag == 0) {
                            patientDtailsWoe.getdeletewoe(response);
                        } else if (flag == 1) {
                            summaryActivity_new.getdeletewoe(response);
                        } else if (flag == 2) {
                            summary_activity_woe.getdeletewoe(response);
                        } else if (flag == 3) {
                            summaryActivity.getdeletewoe(response);
                        }
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                    } else {
                        GlobalClass.showVolleyError(error, mActivity);
                        GlobalClass.hideProgress(mActivity,progressDialog);
                        Log.v("TAG", error.toString());
                    }
                }
            });
            deletePatienDetail.add(jsonObjectRequest1);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
            Log.e(TAG, "deletePatientDetailsandTest: url" + jsonObjectRequest1);
            Log.e(TAG, "deletePatientDetailsandTest: json" + jsonObjectOtp);
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
