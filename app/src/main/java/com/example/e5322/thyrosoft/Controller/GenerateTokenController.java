package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ScanSummaryActivity;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;

import org.json.JSONObject;

public class GenerateTokenController {
    private static final String TAG = GenerateTokenController.class.getSimpleName();
    private Activity mActivity;
    private RequestQueue requestQueue;
    private GlobalClass globalClass;
    private PETCT_Frag petct_frag;
    private ScanSummaryActivity scanSummaryActivity;
    ProgressDialog progressDialog;
    int flag=0;

    public GenerateTokenController(Activity activity, PETCT_Frag petct_frag) {
        this.mActivity = activity;
        this.petct_frag = petct_frag;
        globalClass = new GlobalClass(mActivity);
        flag=0;
    }

    public GenerateTokenController(Activity activity, ScanSummaryActivity scanSummaryActivity) {
        this.mActivity = activity;
        this.scanSummaryActivity = scanSummaryActivity;
        globalClass = new GlobalClass(mActivity);
        flag=1;
    }

    public void generateToken(boolean b, JSONObject jsonObject) {
        try {
            if (b) {
                progressDialog= GlobalClass.ShowprogressDialog(mActivity);
            }
            if (requestQueue == null)
                requestQueue = GlobalClass.setVolleyReq(mActivity);
            String Url =  Api.gettoken;
            globalClass.printLog("Error", TAG, "generateTokenAPI", Url);
            globalClass.printLog("Error", TAG, "generateToken post data: ", "" + jsonObject);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        globalClass.hideProgressDialog(progressDialog,mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        globalClass.printLog("Error", TAG, "generateTokenAPIResponse", String.valueOf(response));
                        if (response != null) {
                            if (flag==0){
                                petct_frag.getGenerateTokenResponse(response);
                            }else {
                                scanSummaryActivity.getGenerateTokenResponse(response);
                            }

                        } else {
                            GlobalClass.toastyError(mActivity, "Something went wrong",false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog(progressDialog,mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    globalClass.printLog("Error", TAG, "generateTokenAPIResponse", error.getMessage());
                    GlobalClass.showVolleyError(error, mActivity);
                }
            });
            requestQueue.add(jsonObjectRequest);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }

    }
}
