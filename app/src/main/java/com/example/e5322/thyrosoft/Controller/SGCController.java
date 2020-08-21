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
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class SGCController {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity;
    ConnectionDetector cd;


    public SGCController(Activity mActivity, Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity) {
        this.mActivity = mActivity;
        this.sgc_pgc_entry_activity = sgc_pgc_entry_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void sgccontroller(JSONObject jsonObject, RequestQueue requestQueue){

        if (cd.isConnectingToInternet()){
            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
            String url= Api.SGCRegistration;
            Log.e(TAG,"URL---->"+url);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())){
                            if (flag==0){
                                sgc_pgc_entry_activity.getSGCResponse(response);
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
                            GlobalClass.hideProgress(mActivity,progressDialog);
                            GlobalClass.showVolleyError(error,mActivity);
                        }
                    }
                }
            });

            requestQueue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);
            Log.e(TAG, "onResponse: json" + jsonObject);

        }else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN,2);
        }

    }
}

