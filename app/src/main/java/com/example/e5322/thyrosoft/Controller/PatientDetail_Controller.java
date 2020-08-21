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
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class PatientDetail_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private PatientDtailsWoe patientDtailsWoe;
    ConnectionDetector cd;


    public PatientDetail_Controller(Activity mActivity, PatientDtailsWoe patientDtailsWoe) {
        this.mActivity = mActivity;
        this.patientDtailsWoe = patientDtailsWoe;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getpatienrdetail_controller(String api_key, String user, String patientId, RequestQueue requestQueuePatientDetails) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.getPartientDetailsList + "" + api_key + "/" + "" + user + "/" + "" + patientId + "/" + "geteditdata";
            Log.e(TAG, "ptient details URL==>"+url);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                patientDtailsWoe.getpatientdetailsResponse(response);
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

            requestQueuePatientDetails.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "patient detail onResponse: url" +jsonObjectRequestPop);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
