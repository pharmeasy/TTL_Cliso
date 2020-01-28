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

public class UpdateStockController {
    private static final String TAG = UpdateStockController.class.getSimpleName();
    Activity mActivity;
    int flag = 0;
    GlobalClass globalClass;
    BMC_UpdateMaterialActivity bmc_updateMaterialActivity;
    private RequestQueue requestQueue;

    public UpdateStockController(BMC_UpdateMaterialActivity updateMaterialActivity, Activity activity) {
        this.bmc_updateMaterialActivity = updateMaterialActivity;
        this.mActivity = activity;
        flag = 0;
        globalClass = new GlobalClass(mActivity);
    }

    public void UpdateAvailableStock(JSONObject jsonObject) {
        try {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            requestQueue = Volley.newRequestQueue(mActivity);
            String url = Api.Materialupdate;
            Log.e(TAG, "UpdateAvailableStockAPI: " + url);
            Log.e(TAG, "UpdateAvailableStockAPI gson: " + jsonObject);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    Log.e(TAG, "UpdateAvailableStockAPI Response: " + response);
                    if (response != null) {
                        if (flag == 0)
                            bmc_updateMaterialActivity.getUpdatedResponse(response);
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
