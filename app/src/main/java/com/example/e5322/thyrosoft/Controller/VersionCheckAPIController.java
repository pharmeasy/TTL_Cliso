package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.ResponseModels.VersionResponseModel;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class VersionCheckAPIController {

    private static final String TAG = VersionCheckAPIController.class.getSimpleName();
    int flag = 0;
    Activity mactivity;
    GlobalClass globalClass;
    SplashScreen splashScreenActivity;
    private RequestQueue requestQueue;
    AppPreferenceManager appPreferenceManager;
    Gson gson;

    public VersionCheckAPIController(SplashScreen splashScreenActivity, Activity activity) {
        this.splashScreenActivity = splashScreenActivity;
        this.mactivity = activity;
        flag = 0;
        globalClass = new GlobalClass(mactivity);
        appPreferenceManager=new AppPreferenceManager(mactivity);

    }

    public void checkVersion(final boolean b) {
        try {
            if (!b) {
                globalClass.showupdate(MessageConstants.CHECKING_UPDATE_PLEASE_WAIT);
            }
            if (requestQueue == null)
                requestQueue = GlobalClass.setVolleyReq(mactivity);
            String url = Api.checkVersion;
            globalClass.printLog("Error", TAG, "VersionCheckAPI", url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        globalClass.hideProgressDialog1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        appPreferenceManager.setVersionResponseModel(new Gson().fromJson(String.valueOf(response), VersionResponseModel.class));

                        globalClass.printLog("Error", TAG, "VersionCheckAPIResponse", String.valueOf(response));


                        String apiVersion = response.getString("Version");
                        String ApkUrl = response.getString("url");
                        String RESPONSE = response.getString("response");
                        String resId = response.getString("resId");
                        int syncproduct = response.getInt("syncproduct");
                        Boolean isoff = (response.optBoolean("isoffline", false));
                        Global.isoffline = isoff;

                        if (response != null) {
                            splashScreenActivity.getVersionResponse(apiVersion, ApkUrl, RESPONSE);
                        } else {
                            GlobalClass.showShortToast(mactivity, MessageConstants.SOMETHING_WENT_WRONG);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    GlobalClass.showVolleyError(error, mactivity);
                    mactivity.finish();
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
