package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import com.example.e5322.thyrosoft.Controller.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONObject;

public class GetOTPController {
    private static final String TAG = GetOTPController.class.getSimpleName();
    private final SharedPreferences pref;
    int flag = 0;
    private Activity mactivity;
    private RequestQueue requestQueue;
    private BS_EntryFragment bs_entryFragment;
    Start_New_Woe start_new_woe;
    SpecialOffer_Activity specialOffer_activity;
    public GetOTPController(Activity activity, BS_EntryFragment bs_entryFragment) {
        this.mactivity = activity;
        this.bs_entryFragment = bs_entryFragment;
        flag = 1;
        pref = mactivity.getSharedPreferences("domain", 0);
    }

    public GetOTPController(Activity activity, Start_New_Woe start_new_woe) {
        this.mactivity = activity;
        this.start_new_woe = start_new_woe;
        flag = 2;
        pref = mactivity.getSharedPreferences("domain", 0);
    }

    public GetOTPController(Activity activity, SpecialOffer_Activity specialOffer_activity) {
        this.specialOffer_activity = specialOffer_activity;
        this.start_new_woe = start_new_woe;
        flag = 3;
        pref = mactivity.getSharedPreferences("domain", 0);
    }

    public void GetOTP(JSONObject jsonObject) {
        try {
            ProgressDialog progressDialog = null;
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);

            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(mactivity);
            String url = Api.THYROCARE + Api.POST_GENERATE_OTP;

            Log.e(TAG, "viewSendOTPAPI" + url);
            Log.e(TAG, "viewSendOTP params" + jsonObject);

            final ProgressDialog finalProgressDialog = progressDialog;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "viewSendOTPAPI Response " + response);
                    if (response != null) {

                        if (flag == 1){
                            bs_entryFragment.getSendOTPResponse(response);
                        }else if (flag==2){
                         //   start_new_woe.onvalidatemob(response);
                        }else if (flag==3){
                         //   specialOffer_activity.onvalidatemob(response);
                        }


                    } else {
                        Global.showCustomToast(mactivity, ToastFile.something_went_wrong);
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