package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ProductLisitngActivityNew;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.WOE.OutLabTestsActivity;
import com.example.e5322.thyrosoft.WOE.RecheckAllTest;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.AddWOETestsForSerum;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class ProductListController {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private RateCalculatorFragment rateCalculatorFragment;
    private ProductLisitngActivityNew productLisitngActivityNew;
    private SpecialOffer_Activity specialOffer_activity;
    private OutLabTestsActivity outLabTestsActivity;
    private RecheckAllTest recheckAllTest;
    private AddWOETestsForSerum addWOETestsForSerum;
    ConnectionDetector cd;


    public ProductListController(Activity mActivity, RateCalculatorFragment rateCalculatorFragment) {
        this.mActivity = mActivity;
        this.rateCalculatorFragment = rateCalculatorFragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public ProductListController(Activity mActivity, ProductLisitngActivityNew productLisitngActivityNew) {
        this.mActivity = mActivity;
        this.productLisitngActivityNew = productLisitngActivityNew;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public ProductListController(Activity mActivity, SpecialOffer_Activity specialOffer_activity) {
        this.mActivity = mActivity;
        this.specialOffer_activity = specialOffer_activity;
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public ProductListController(Activity mActivity, OutLabTestsActivity outLabTestsActivity) {
        this.mActivity = mActivity;
        this.outLabTestsActivity = outLabTestsActivity;
        cd = new ConnectionDetector(mActivity);
        flag = 3;
    }

    public ProductListController(Activity mActivity, RecheckAllTest recheckAllTest) {
        this.mActivity = mActivity;
        this.recheckAllTest = recheckAllTest;
        cd = new ConnectionDetector(mActivity);
        flag = 4;
    }

    public ProductListController(Activity mActivity, AddWOETestsForSerum addWOETestsForSerum) {
        this.mActivity = mActivity;
        this.addWOETestsForSerum = addWOETestsForSerum;
        cd = new ConnectionDetector(mActivity);
        flag = 5;
    }

    public void productListingController(String url, RequestQueue requestQueuepoptestILS) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                rateCalculatorFragment.getProductListResponse(response);
                            } else if (flag == 1) {
                                productLisitngActivityNew.getProductListResponse(response);
                            } else if (flag == 2) {
                                specialOffer_activity.getProductListResponse(response);
                            } else if (flag == 3) {
                                outLabTestsActivity.getProductListResponse(response);
                            } else if (flag == 4) {
                                recheckAllTest.getProductListResponse(response);
                            } else if (flag == 5) {
                                addWOETestsForSerum.getProductListResponse(response);
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

            requestQueuepoptestILS.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onResponse: url" + url);
            Log.e(TAG, "onResponse: json" + jsonObjectRequestPop);

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
