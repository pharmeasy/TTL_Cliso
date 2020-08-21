package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

public class CheckNumber_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Start_New_Woe start_new_woe;
    ConnectionDetector cd;
    String fromwoe;


    public CheckNumber_Controller(Activity mActivity, Start_New_Woe start_new_woe,String fromwoe) {
        this.mActivity = mActivity;
        this.start_new_woe = start_new_woe;
        cd = new ConnectionDetector(mActivity);
        this.fromwoe=fromwoe;
        flag = 0;
    }


    public void getchecknumbercontroll(final String checkNumber, RequestQueue requestQueue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String strurl = Api.checkNumber + checkNumber;
            Log.e(TAG, "check mobile URL-->"+strurl);
            StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, strurl, new
                    Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GlobalClass.hideProgress(mActivity, progressDialog);

                            if (!GlobalClass.isNull(response)){
                                if (flag==0){
                                    start_new_woe.getchecknumberResp(response,fromwoe,checkNumber);
                                }
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
            Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
