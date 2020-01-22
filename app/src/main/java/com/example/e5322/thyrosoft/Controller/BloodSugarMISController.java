package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.frags.BS_MISEntryFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONObject;

public class BloodSugarMISController {
    private static final String TAG = BloodSugarMISController.class.getSimpleName();
    private int flag;
    private Activity mactivity;
    private BS_MISEntryFragment bs_misFragment;
    private RequestQueue requestQueue;

    public BloodSugarMISController(BS_MISEntryFragment bsMisFragment, Activity activity) {
        this.mactivity = activity;
        this.bs_misFragment = bsMisFragment;
        flag = 0;
    }

    public void getMISData(JSONObject jsonObject) {
        try {
            ProgressDialog progressDialog = null;
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);

            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(mactivity);
            String url = Api.SUGARSO + Api.ENTERED_MIS;
            Log.e(TAG, "MISAPI " + url);
            Log.e(TAG, "postData " + jsonObject);
            final ProgressDialog finalProgressDialog = progressDialog;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "MISAPIResponse " + String.valueOf(response));
                    try {
                        if (response != null) {
                            bs_misFragment.getMISResponse(response);
                        } else {
                            Global.showCustomToast(mactivity, ToastFile.something_went_wrong);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    GlobalClass.showVolleyError(error, mactivity);
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
