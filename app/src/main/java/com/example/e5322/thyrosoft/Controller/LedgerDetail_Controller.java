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
import com.example.e5322.thyrosoft.Fragment.FirstFragment;
import com.example.e5322.thyrosoft.Fragment.Ledger_details;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class LedgerDetail_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private FirstFragment firstFragment;
    private Ledger_details ledger_details;
    ConnectionDetector cd;


    public LedgerDetail_Controller(Activity mActivity, FirstFragment firstFragment) {
        this.mActivity = mActivity;
        this.firstFragment = firstFragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public LedgerDetail_Controller(Activity mActivity, Ledger_details ledger_details) {
        this.mActivity = mActivity;
        this.ledger_details = ledger_details;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void getledgerdetail(JSONObject jsonObject, RequestQueue queue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url= Api.LedgerDetLive;
            Log.e(TAG, "Ledgerdetail  URL :"+url);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url,jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                firstFragment.getResponse(response);
                            }else  if (flag == 1) {
                                ledger_details.getResponse(response);
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

            queue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);
            Log.e(TAG, "onResponse: json" + jsonObject);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
