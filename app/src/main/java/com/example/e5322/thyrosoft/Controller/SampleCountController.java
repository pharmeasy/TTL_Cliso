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
import com.example.e5322.thyrosoft.Fragment.Consignment_fragment;
import com.example.e5322.thyrosoft.Fragment.Next_Consignment_Entry;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class SampleCountController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Consignment_fragment consignment_fragment;
    private Next_Consignment_Entry next_consignment_entry;
    ConnectionDetector cd;


    public SampleCountController(Activity mActivity, Consignment_fragment consignment_fragment) {
        this.mActivity = mActivity;
        this.consignment_fragment = consignment_fragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public SampleCountController(Activity mActivity, Next_Consignment_Entry next_consignment_entry) {
        this.mActivity = mActivity;
        this.next_consignment_entry = next_consignment_entry;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void samplecnt_Controller(String user, String passDate, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.static_pages_link + user + "/" + passDate + "/getsamplecount";
            Log.e(TAG, "URL getsamplecount--->" + url);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                consignment_fragment.getsamplecount(response);
                            }else if (flag==1){
                                next_consignment_entry.getsamplecount(response);
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
                            GlobalClass.hideProgress(mActivity, progressDialog);
                            GlobalClass.showVolleyError(error, mActivity);
                        }
                    }
                }
            });

            requestQueue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }


}
