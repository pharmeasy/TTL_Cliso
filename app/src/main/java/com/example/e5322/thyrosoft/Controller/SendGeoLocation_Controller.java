package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Fragment.Offline_woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class SendGeoLocation_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Offline_woe offline_woe;
    private Scan_Barcode_ILS_New scan_barcode_ils_new;
    ConnectionDetector cd;


    public SendGeoLocation_Controller(Activity mActivity, Offline_woe offline_woe) {
        this.mActivity = mActivity;
        this.offline_woe = offline_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public SendGeoLocation_Controller(Activity mActivity, Scan_Barcode_ILS_New scan_barcode_ils_new) {
        this.mActivity = mActivity;
        this.scan_barcode_ils_new = scan_barcode_ils_new;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }


    public void sendgeolocation_controller(JSONObject jsonObject, RequestQueue sendGPSDetails) {

        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.sendGeoLocation,  jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!GlobalClass.isNull(response.toString())) {
                        if (flag == 0) {
                            offline_woe.getgeolocResponse(response);
                        } else if (flag == 1) {
                            scan_barcode_ils_new.getgeolocResponse(response);
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
                        GlobalClass.showVolleyError(error, mActivity);
                    }
                }
            }
        });
        sendGPSDetails.add(jsonObjectRequestPop);
        GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
        Log.e(TAG, "onResponse: url" + jsonObjectRequestPop);
        Log.e(TAG, "onResponse: json" + jsonObject);

    }
}
