package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.MyProfile_activity;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class Myprofile_Controller {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private ManagingTabsActivity managingTabsActivity;
    private MyProfile_activity myProfile_activity;
    private PETCT_Frag petct_frag;
    private Payment_Activity payment_activity;
    ConnectionDetector cd;


    public Myprofile_Controller(Activity mActivity, ManagingTabsActivity managingTabsActivity) {
        this.mActivity = mActivity;
        this.managingTabsActivity = managingTabsActivity;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public Myprofile_Controller(Activity mActivity, MyProfile_activity myProfile_activity) {
        this.mActivity = mActivity;
        this.myProfile_activity = myProfile_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public Myprofile_Controller(Activity mActivity, PETCT_Frag petct_frag) {
        this.mActivity = mActivity;
        this.petct_frag = petct_frag;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public Myprofile_Controller(Activity mActivity, Payment_Activity payment_activity) {
        this.mActivity = mActivity;
        this.payment_activity = payment_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 3;
    }

    public void getmyprofilecontroller(String url, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

            Log.e(TAG, "Myprofile URL--->" + url);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                managingTabsActivity.getMyprofileRespponse(response);
                            } else if (flag == 1) {
                                myProfile_activity.getMyprofileRespponse(response);
                            } else if (flag == 2) {
                                petct_frag.getMyprofileRespponse(response);
                            }else if (flag==3){
                                payment_activity.getMyprofileRespponse(response);
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
            Log.e(TAG, " Myprofile onResponse: url" + url);


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);

        }
    }
}
