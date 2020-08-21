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
import com.example.e5322.thyrosoft.Fragment.CHNfragment;
import com.example.e5322.thyrosoft.Fragment.DashboardFragment;
import com.example.e5322.thyrosoft.Fragment.FilterReport;
import com.example.e5322.thyrosoft.Fragment.ResultFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class ResultLIVE_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private FilterReport filterReport;
    private ResultFragment resultFragment;
    private CHNfragment chNfragment;
    private DashboardFragment dashboardFragment;
    ConnectionDetector cd;


    public ResultLIVE_Controller(Activity mActivity, FilterReport filterReport) {
        this.mActivity = mActivity;
        this.filterReport = filterReport;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public ResultLIVE_Controller(Activity mActivity, ResultFragment resultFragment) {
        this.mActivity = mActivity;
        this.resultFragment = resultFragment;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public ResultLIVE_Controller(Activity mActivity, CHNfragment chNfragment) {
        this.mActivity = mActivity;
        this.chNfragment = chNfragment;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public ResultLIVE_Controller(Activity mActivity, DashboardFragment dashboardFragment) {
        this.mActivity = mActivity;
        this.dashboardFragment = dashboardFragment;
        cd = new ConnectionDetector(mActivity);
        flag = 3;
    }



    public void getresultcontroller(String api_key, String passToSpinner, String user, String passDateTogetData, RequestQueue requestQueue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String strurl = Api.ResultLIVE + "/" + api_key + "/" + passToSpinner + "/" + user + "/" + passDateTogetData + "/" + "key/value";
            Log.e(TAG, "Result URL-->" + strurl);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, strurl, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                filterReport.getResponse(response);
                            }else if (flag == 1) {
                                resultFragment.getResponse(response);
                            }else if (flag == 2) {
                                chNfragment.getResponse(response);
                            }else if (flag == 3) {
                                dashboardFragment.getResponse(response);
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
            Log.e(TAG, "onResponse: url" + strurl);


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
