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
import com.example.e5322.thyrosoft.Adapter.RateCAlAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class TestDetails_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private RateCAlAdapter rateCAlAdapter;
    ConnectionDetector cd;


    public TestDetails_Controller(Activity mActivity, RateCAlAdapter rateCAlAdapter) {
        this.mActivity= mActivity;
        this.rateCAlAdapter = rateCAlAdapter;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void gettestdetail_controller(JSONObject jsonObject, RequestQueue POstQueSendEstimation){

        if (cd.isConnectingToInternet()){
            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
            String URL= Api.testDetails;
            Log.e(TAG,"TEST DETAILS URL --->"+URL);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, URL,jsonObject ,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())){
                            if (flag==0){
                                rateCAlAdapter.getTestdetailResponse(response);
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
                            GlobalClass.hideProgress(mActivity,progressDialog);
                            GlobalClass.showVolleyError(error,mActivity);
                        }
                    }
                }
            });

            POstQueSendEstimation.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onClick: URl" + URL);
            Log.e(TAG, "onClick: jsonObject " + jsonObject);

        }else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN,2);
        }
    }
}
