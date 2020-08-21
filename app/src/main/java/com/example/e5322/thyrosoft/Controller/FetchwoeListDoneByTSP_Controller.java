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
import com.example.e5322.thyrosoft.Activity.Woe_activity;
import com.example.e5322.thyrosoft.Fragment.Wind_up_fragment;
import com.example.e5322.thyrosoft.Fragment.Woe_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class FetchwoeListDoneByTSP_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Wind_up_fragment wind_up_fragment;
    private Woe_activity woe_activity;
    private Woe_fragment woe_fragment;
    ConnectionDetector cd;


    public FetchwoeListDoneByTSP_Controller(Activity mActivity, Wind_up_fragment wind_up_fragment) {
        this.mActivity = mActivity;
        this.wind_up_fragment = wind_up_fragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public FetchwoeListDoneByTSP_Controller(Activity mActivity, Woe_activity woe_activity) {
        this.mActivity = mActivity;
        this.woe_activity = woe_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public FetchwoeListDoneByTSP_Controller(Activity mActivity, Woe_fragment woe_fragment) {
        this.mActivity = mActivity;
        this.woe_fragment = woe_fragment;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public void woelistdone_controller(RequestQueue requestQueue, String url) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                wind_up_fragment.getwoedonelistResponse(response);
                            } else if (flag == 1) {
                                woe_activity.getwoedonelistResponse(response);
                            } else if (flag == 2) {
                                woe_fragment.getwoedonelistResponse(response);
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

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
