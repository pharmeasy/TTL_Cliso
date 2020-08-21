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
import com.example.e5322.thyrosoft.Adapter.TrackDetAdapter;
import com.example.e5322.thyrosoft.Fragment.TrackDetails;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class Receiptmail_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private TrackDetAdapter trackDetAdapter;
    private TrackDetails trackDetails;
    ConnectionDetector cd;


    public Receiptmail_Controller(Activity mActivity, TrackDetAdapter trackDetAdapter) {
        this.mActivity = mActivity;
        this.trackDetAdapter = trackDetAdapter;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }
    public Receiptmail_Controller(Activity mActivity, TrackDetails trackDetails) {
        this.mActivity = mActivity;
        this.trackDetails = trackDetails;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void getreceiptmail(JSONObject jsonObject, RequestQueue requestQueue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url= Api.Receipt_mail;
            Log.e(TAG, "Api.Receipt_mail URL-->"+url);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                trackDetAdapter.getResponse(response);
                            }else if (flag==1){
                                trackDetails.getReceiptresponse(response);
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
            Log.e(TAG, "onResponse: body" + jsonObjectRequestPop);


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
