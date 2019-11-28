package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class POSTBookLeadController {
    private static final String TAG = POSTBookLeadController.class.getSimpleName();
    private RequestQueue requestQueue;
    private Activity mActivity;
    private GlobalClass globalClass;
    private ConfirmbookDetail confirmbookDetail;
    ProgressDialog progressDialog;
    String header;

    public POSTBookLeadController(Activity activity, ConfirmbookDetail confirmbookDetail, String header) {
        this.mActivity = activity;
        this.confirmbookDetail = confirmbookDetail;
        globalClass = new GlobalClass(mActivity);
        this.header = header;
    }

    public void postBookLead(boolean b, JSONObject jsonObject) {
        try {
            if (b) {
                progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            }
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(mActivity);
            String url = Api.LEAD_BOOKING;
            globalClass.printLog("Error", TAG, "postBookLeadAPI", url);
            globalClass.printLog("Error", TAG, "postBookLeadAPI post data", "" + jsonObject);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                        globalClass.printLog("Error", TAG, "postBookLeadAPIResponse", "" + response);
                        if (response != null) {
                            confirmbookDetail.getPOSTBookLeadResponse(response);
                        } else {
                            GlobalClass.toastyError(mActivity, MessageConstants.SOMETHING_WENT_WRONG, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    GlobalClass.showVolleyError(error, mActivity);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String error_sd = new String(error.networkResponse.data);
                        confirmbookDetail.getPOSTBookLeadErrorResponse(error_sd);
                    }
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    globalClass.printLog("Error", TAG, "StatusCode: ", "" + mStatusCode);
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", header);
                    Log.e(TAG, "Header ----->" + header);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            requestQueue.add(stringRequest);
            GlobalClass.volleyRetryPolicy(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }
    }
}
