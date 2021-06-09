package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.Fragment.SRFCovidWOEEnterFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.RequestModels.getSCPRequestModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class getSCPSRFAPIController {

    private static final String TAG = getSCPSRFAPIController.class.getSimpleName();
    Activity mactivity;
    GlobalClass globalClass;
    SRFCovidWOEEnterFragment mSRFCovidWOEEnterFragment;
    Covidenter_Frag mCovidenter_Frag;
    RATEnterFrag mRATEnterFrag;
    private RequestQueue requestQueue;
    AppPreferenceManager appPreferenceManager;
    Gson gson;
    int flag = 0;

    public getSCPSRFAPIController(SRFCovidWOEEnterFragment dgdgdg, Activity activity) {
        this.mSRFCovidWOEEnterFragment = dgdgdg;
        this.mactivity = activity;
        globalClass = new GlobalClass(mactivity);
        appPreferenceManager = new AppPreferenceManager(mactivity);
        flag = 1;
    }

    public getSCPSRFAPIController(Covidenter_Frag dgdgdg, Activity activity) {
        this.mCovidenter_Frag = dgdgdg;
        this.mactivity = activity;
        globalClass = new GlobalClass(mactivity);
        appPreferenceManager = new AppPreferenceManager(mactivity);
        flag = 2;
    }

    public getSCPSRFAPIController(RATEnterFrag ratEnterFrag, Activity activity) {
        this.mRATEnterFrag = ratEnterFrag;
        this.mactivity = activity;
        globalClass = new GlobalClass(mactivity);
        appPreferenceManager = new AppPreferenceManager(mactivity);
        flag = 3;
    }

    public void getSCPtechnician(final boolean b, String usercode, String apikey) {
        try {
            if (!b) {
                globalClass.showupdate(MessageConstants.PLEASE_WAIT);
            }
            if (requestQueue == null)
                requestQueue = GlobalClass.setVolleyReq(mactivity);
            String url = Api.Cloud_base + "GetTechnician";
            globalClass.printLog("Error", TAG, "GetTechnician", url);

            JSONObject jsonObject = null;
            try {
                getSCPRequestModel requestModel = new getSCPRequestModel();
                requestModel.setAPI_KEY(apikey);
                requestModel.setSourceCode(usercode);

                String json = new Gson().toJson(requestModel);
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        globalClass.hideProgressDialog1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (flag == 1) {
                            mSRFCovidWOEEnterFragment.setSCPresData(response.toString());
                            mSRFCovidWOEEnterFragment.setFinalSCPList(response.toString());
                        } else if (flag == 2) {
                            mCovidenter_Frag.setSCPresData(response.toString());
                            mCovidenter_Frag.setFinalSCPList(response.toString());
                        } else if (flag == 3) {
                            mRATEnterFrag.setSCPresData(response.toString());
                            mRATEnterFrag.setFinalSCPList(response.toString());
                        }

                    } catch (Exception e) {
                        globalClass.hideProgressDialog1();
                        GlobalClass.showShortToast(mactivity, MessageConstants.SOMETHING_WENT_WRONG);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    globalClass.hideProgressDialog1();
                    GlobalClass.showVolleyError(error, mactivity);

                    if (flag == 1) {
                        mSRFCovidWOEEnterFragment.setFinalSCPList("");
                    } else if (flag == 2) {
                        mCovidenter_Frag.setFinalSCPList("");
                    } else if (flag == 3) {
                        mRATEnterFrag.setFinalSCPList("");
                    }
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