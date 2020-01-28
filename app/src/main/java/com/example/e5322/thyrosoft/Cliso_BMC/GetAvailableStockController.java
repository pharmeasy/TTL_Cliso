package com.example.e5322.thyrosoft.Cliso_BMC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;

import org.json.JSONObject;

public class GetAvailableStockController {
    private static final String TAG = GetAvailableStockController.class.getSimpleName();
    Activity mActivity;
    int flag = 0;
    BMC_StockAvailabilityActivity bmc_stockAvailabilityActivity;
    GlobalClass globalClass;
    private RequestQueue requestQueue;

    public GetAvailableStockController(BMC_StockAvailabilityActivity stockAvailabilityActivity, Activity activity) {
        this.bmc_stockAvailabilityActivity = stockAvailabilityActivity;
        this.mActivity = activity;
        flag = 0;
        globalClass = new GlobalClass(mActivity);
    }

    public void getAvailableStock(JSONObject jsonObject) {
        try {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            requestQueue = Volley.newRequestQueue(mActivity);
            String url = Api.StockAvailability;
            Log.e(TAG, "getAvailableStockAPI: " + url);
            Log.e(TAG, "getAvailableStockAPI gson: " + jsonObject);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    Log.e(TAG, "getAvailableStockAPI Response: " + response);
                    if (response != null) {
                        if (flag == 0)
                            bmc_stockAvailabilityActivity.getStockResponse(response);
                    } else {
                        Toast.makeText(mActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        GlobalClass.hideProgress(mActivity, progressDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);
            GlobalClass.volleyRetryPolicy(request);
        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }
    }
}
