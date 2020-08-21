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
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Summary_Activity_WOE;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class Getwomaster_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private RateCalculatorFragment rateCalculatorFragment;
    private Start_New_Woe start_new_woe;
    private Summary_Activity_WOE summary_activity_woe;
    ConnectionDetector cd;
    int tspflag = 0;

    public Getwomaster_Controller(Activity mActivity, RateCalculatorFragment rateCalculatorFragment) {
        this.mActivity = mActivity;
        this.rateCalculatorFragment = rateCalculatorFragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public Getwomaster_Controller(Activity mActivity, Start_New_Woe start_new_woe) {
        this.mActivity = mActivity;
        this.start_new_woe = start_new_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public Getwomaster_Controller(Activity mActivity, Start_New_Woe start_new_woe, int tspflag) {
        this.mActivity = mActivity;
        this.start_new_woe = start_new_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
        tspflag = 1;
    }

    public Getwomaster_Controller(Activity mActivity, Summary_Activity_WOE summary_activity_woe) {
        this.mActivity = mActivity;
        this.summary_activity_woe = summary_activity_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 3;
    }


    public void getwoeMaster_Controller(String api_key, String user, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

            String url = Api.getData + "" + api_key + "/" + "" + user + "/B2BAPP/getwomaster";

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                rateCalculatorFragment.getwoemasterResponse(response);
                            } else if (flag == 1) {
                                start_new_woe.getwoemasterResponse(response);
                            } else if (flag == 2) {
                                start_new_woe.getTSPResponse(response);
                            } else if (flag == 3) {
                                summary_activity_woe.getTSPResponse(response);
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
            Log.e(TAG, "getwoemaster url  " + url);
            Log.e(TAG, " getwoemaster onResponse: json" + jsonObjectRequestPop);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
