package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import com.example.e5322.thyrosoft.Controller.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.ToastFile;

public class GETValidateBSOTPController {
    private static final String TAG = GETValidateBSOTPController.class.getSimpleName();
    private SharedPreferences pref;
    private int flag;
    private Activity mactivity;
    private RequestQueue requestQueue;
    private BS_EntryFragment bs_entryFragment;

    public GETValidateBSOTPController(Activity activity, BS_EntryFragment bs_entryFragment) {
        this.mactivity = activity;
        this.bs_entryFragment = bs_entryFragment;
        flag = 0;
        pref = mactivity.getSharedPreferences("domain", 0);
    }

    public void validateOTP(String strMobile, String otp) {
        try {
            ProgressDialog progressDialog = null;
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);

            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(mactivity);
            String url = Api.SUGARSO + Api.VERIFY_OTP_BS + strMobile + "/" + otp;
            Log.e(TAG, "validateOTPAPI " + url);

            final ProgressDialog finalProgressDialog = progressDialog;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "validateOTPResponse " + response);
                    if (response != null && !response.isEmpty()) {
                        if (flag == 0)
                            bs_entryFragment.getValidateOTPResponse(response);
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
