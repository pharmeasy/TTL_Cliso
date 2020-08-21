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
import com.example.e5322.thyrosoft.Activity.BroadcastActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.Noticeboard_activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class NoticeBoardController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private BroadcastActivity broadcastActivity;
    private Noticeboard_activity noticeboard_activity;
    ConnectionDetector cd;


    public NoticeBoardController(Activity mActivity, BroadcastActivity broadcastActivity) {
        this.mActivity = mActivity;
        this.broadcastActivity = broadcastActivity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public NoticeBoardController(Activity mActivity, Noticeboard_activity noticeboard_activity) {
        this.mActivity = mActivity;
        this.noticeboard_activity = noticeboard_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void noticeboardController(String api_key, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

            String url = Api.NoticeBoardData + "" + api_key + "/getNoticeMessages";

            Log.e(TAG, "URL---->" + url);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                broadcastActivity.getnoticeboardresponse(response);
                            }else if (flag==1){
                                noticeboard_activity.getnoticeboardresponse(response);
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
                            GlobalClass.hideProgress(mActivity, progressDialog);
                            GlobalClass.showVolleyError(error, mActivity);
                        }
                    }
                }
            });

            requestQueue.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);
            Log.e(TAG, "onResponse: json" + jsonObjectRequestPop);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
