package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import java.io.UnsupportedEncodingException;

public class ValidateEmail_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity;
    ConnectionDetector cd;


    public ValidateEmail_Controller(Activity mActivity, Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity) {
        this.mActivity = mActivity;
        this.sgc_pgc_entry_activity = sgc_pgc_entry_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void checkemailvalidate(final String requestBody, RequestQueue requestQueue) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String URL = Api.checkValidEmail;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: " + response);
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            sgc_pgc_entry_activity.getvalidateemailResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    GlobalClass.showVolleyError(error, mActivity);
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

            };

            requestQueue.add(stringRequest);
            GlobalClass.volleyRetryPolicy(stringRequest);
            Log.e(TAG, "afterTextChanged: url" + stringRequest);
            //  Log.e(TAG, "afterTextChanged: url" + jsonBody);
            //Log.e(TAG, "afterTextChanged: url" + finalJson);
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);

        }
    }
}
