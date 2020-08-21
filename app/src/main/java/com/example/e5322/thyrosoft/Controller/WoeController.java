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
import com.example.e5322.thyrosoft.Adapter.Offline_Woe_Adapter;
import com.example.e5322.thyrosoft.Fragment.Offline_woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ProductLisitngActivityNew;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Woe_Edt_Activity;
import com.example.e5322.thyrosoft.SpecialOffer.OfferScan_Activity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class WoeController {

    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Offline_Woe_Adapter offline_woe_adapter;
    private Offline_woe offline_woe;
    private ProductLisitngActivityNew productLisitngActivityNew;
    private Scan_Barcode_ILS_New scan_barcode_ils_new;
    private Woe_Edt_Activity woe_edt_activity;
    private OfferScan_Activity offerScan_activity;
    ConnectionDetector cd;


    public WoeController(Activity mActivity, Offline_Woe_Adapter offline_woe_adapter) {
        this.mActivity = mActivity;
        this.offline_woe_adapter = offline_woe_adapter;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public WoeController(Activity mActivity, Offline_woe offline_woe) {
        this.mActivity = mActivity;
        this.offline_woe = offline_woe;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public WoeController(Activity mActivity, ProductLisitngActivityNew productLisitngActivityNew) {
        this.mActivity = mActivity;
        this.productLisitngActivityNew = productLisitngActivityNew;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public WoeController(Activity mActivity, Scan_Barcode_ILS_New scan_barcode_ils_new) {
        this.mActivity = mActivity;
        this.scan_barcode_ils_new = scan_barcode_ils_new;
        cd = new ConnectionDetector(mActivity);
        flag = 3;
    }

    public WoeController(Activity mActivity, Woe_Edt_Activity woe_edt_activity) {
        this.mActivity = mActivity;
        this.woe_edt_activity = woe_edt_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 4;
    }

    public WoeController(Activity mActivity, OfferScan_Activity offerScan_activity) {
        this.mActivity = mActivity;
        this.offerScan_activity = offerScan_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 5;
    }


    public void woeDoneController(JSONObject jsonObj, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.finalWorkOrderEntryNew;
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                offline_woe_adapter.getWoeResponse(response);
                            } else if (flag == 1) {
                                offline_woe.getWoeResponse(response);
                            } else if (flag == 2) {
                                productLisitngActivityNew.getWoeResponse(response);
                            } else if (flag == 3) {
                                scan_barcode_ils_new.getWoeResponse(response);
                            } else if (flag == 4) {
                                woe_edt_activity.getWoeResponse(response);
                            }else if (flag == 5) {
                                offerScan_activity.getWoeResponse(response);
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

                            if (flag == 3) {
                                scan_barcode_ils_new.onWoeError();
                            }
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
