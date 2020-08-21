package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Consignment_fragment;
import com.example.e5322.thyrosoft.Fragment.Next_Consignment_Entry;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

public class CongsigmentBarcode_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    String fromcome;
    private Activity mActivity;
    private Consignment_fragment consignment_fragment;
    private Next_Consignment_Entry next_consignment_entry;
    ConnectionDetector cd;



    public CongsigmentBarcode_Controller(Activity mActivity, Consignment_fragment consignment_fragment,String fromcome) {
        this.mActivity = mActivity;
        this.fromcome=fromcome;
        this.consignment_fragment = consignment_fragment;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }


    public CongsigmentBarcode_Controller(Activity mActivity, Next_Consignment_Entry next_consignment_entry,String fromcome) {
        this.mActivity = mActivity;
        this.fromcome=fromcome;
        this.next_consignment_entry = next_consignment_entry;
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void getcon_barcode(String url, RequestQueue barcodeDetails) {
        if (cd.isConnectingToInternet()) {
            Log.e(TAG, " congsigmnet barcode ---" + url);
            StringRequest jsonObjectRequestPop = new StringRequest(Request.Method.GET, url
                    , new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v(TAG, "barcode respponse" + response);

                    try {
                        if (!GlobalClass.isNull(response)) {
                            if (flag == 0) {
                                consignment_fragment.getbarcodeResponse(response,fromcome);
                            }else if (flag==1){
                                next_consignment_entry.getbarcodeResponse(response,fromcome);
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
                            GlobalClass.showVolleyError(error, mActivity);
                        }
                    }
                }
            });

            barcodeDetails.add(jsonObjectRequestPop);
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            Log.e(TAG, "onFocusChange: url" + jsonObjectRequestPop);
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }

}
