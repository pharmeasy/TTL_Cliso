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
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcodeOutlabs;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcode_New;
import com.example.e5322.thyrosoft.Adapter.CLISO_ScanBarcodeAdapter;
import com.example.e5322.thyrosoft.Adapter.LeadIdAdapter;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Adapter.AttachBarcodeAdpter;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ScanBarcodeAdapter;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import org.json.JSONObject;

public class Checkbarcode_Controller {
    public String TAG = getClass().getSimpleName();
    int flag;
    private Activity mActivity;
    private Scan_Barcode_Outlabs scan_barcode_outlabs;
    private AntiBodyEnterFrag  antiBodyEnterFrag;
    private AdapterBarcodeOutlabs adapterBarcodeOutlabs;
    private AdapterBarcode_New adapterBarcode_new;
    private CLISO_ScanBarcodeAdapter cliso_scanBarcodeAdapter;
    private LeadIdAdapter leadIdAdapter;
    private RATEnterFrag ratEnterFrag;
    private AttachBarcodeAdpter attachBarcodeAdpter;
    private ScanBarcodeAdapter scanBarcodeAdapter;
    String fromoutlabadapter;
    String fromadapterbarcodenew;
    String fromclisoadapter;
    String fromcscanbarcodeadapter;
    ConnectionDetector cd;
    String barcodeDetails;

    public Checkbarcode_Controller(Activity mActivity, Scan_Barcode_Outlabs scan_barcode_outlabs) {
        this.mActivity = mActivity;
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public Checkbarcode_Controller(Activity mActivity, Scan_Barcode_Outlabs scan_barcode_outlabs, String barcodeDetails) {
        this.mActivity = mActivity;
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        cd = new ConnectionDetector(mActivity);
        this.barcodeDetails = barcodeDetails;
        flag = 1;
    }

    public Checkbarcode_Controller(Activity mActivity, AntiBodyEnterFrag antiBodyEnterFrag, String barcodeDetails) {
        this.mActivity = mActivity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        cd = new ConnectionDetector(mActivity);
        this.barcodeDetails = barcodeDetails;
        flag = 2;
    }

    public Checkbarcode_Controller(Activity mActivity, AdapterBarcodeOutlabs adapterBarcodeOutlabs, String barcodeDetails,String fromoutlabadapter) {
        this.mActivity = mActivity;
        this.adapterBarcodeOutlabs = adapterBarcodeOutlabs;
        cd = new ConnectionDetector(mActivity);
        this.fromoutlabadapter=fromoutlabadapter;
        this.barcodeDetails = barcodeDetails;
        flag = 3;
    }

    public Checkbarcode_Controller(Activity mActivity, AdapterBarcode_New adapterBarcode_news, String barcodeDetails,String fromadapterbarcodenew) {
        this.mActivity = mActivity;
        this.adapterBarcode_new = adapterBarcode_news;
        cd = new ConnectionDetector(mActivity);
        this.fromadapterbarcodenew=fromadapterbarcodenew;
        this.barcodeDetails = barcodeDetails;
        flag = 4;
    }

    public Checkbarcode_Controller(Activity mActivity, CLISO_ScanBarcodeAdapter cliso_scanBarcodeAdapter, String barcodeDetails,String fromclisoadapter) {
        this.mActivity = mActivity;
        this.cliso_scanBarcodeAdapter = cliso_scanBarcodeAdapter;
        cd = new ConnectionDetector(mActivity);
        this.fromclisoadapter=fromclisoadapter;
        this.barcodeDetails = barcodeDetails;
        flag = 5;
    }

    public Checkbarcode_Controller(Activity mActivity, LeadIdAdapter leadIdAdapter, String barcodeDetails) {
        this.mActivity = mActivity;
        this.leadIdAdapter = leadIdAdapter;
        cd = new ConnectionDetector(mActivity);
        this.barcodeDetails = barcodeDetails;
        flag = 6;
    }

    public Checkbarcode_Controller(Activity mActivity, RATEnterFrag ratEnterFrag, String barcodeDetails) {
        this.mActivity = mActivity;
        this.ratEnterFrag = ratEnterFrag;
        cd = new ConnectionDetector(mActivity);
        this.barcodeDetails = barcodeDetails;
        flag = 7;
    }

    public Checkbarcode_Controller(Activity mActivity, AttachBarcodeAdpter attachBarcodeAdpter, String barcodeDetails) {
        this.mActivity = mActivity;
        this.attachBarcodeAdpter = attachBarcodeAdpter;
        cd = new ConnectionDetector(mActivity);
        this.barcodeDetails = barcodeDetails;
        flag = 8;
    }

    public Checkbarcode_Controller(Activity mActivity,  ScanBarcodeAdapter scanBarcodeAdapter, String barcodeDetails, String fromcscanbarcodeadapter) {
        this.mActivity = mActivity;
        this.scanBarcodeAdapter = scanBarcodeAdapter;
        cd = new ConnectionDetector(mActivity);
        this.fromcscanbarcodeadapter=fromcscanbarcodeadapter;
        this.barcodeDetails = barcodeDetails;
        flag = 9;
    }


    public void getCheckbarcodeController(String url, RequestQueue requestQueue) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (!GlobalClass.isNull(response.toString())) {
                            if (flag == 0) {
                                scan_barcode_outlabs.getCheckbarcodeResponse(response);
                            } else if (flag == 1) {
                                scan_barcode_outlabs.getpassbarcode(response, barcodeDetails);
                            }else if (flag == 2) {
                                antiBodyEnterFrag.getpassbarcode(response, barcodeDetails);
                            }else if (flag == 3) {
                                adapterBarcodeOutlabs.getpassbarcode(response, barcodeDetails,fromoutlabadapter);
                            }else if (flag == 4) {
                                adapterBarcode_new.getpassbarcode(response, barcodeDetails,fromadapterbarcodenew);
                            }else if (flag ==5) {
                                cliso_scanBarcodeAdapter.getpassbarcode(response, barcodeDetails,fromclisoadapter);
                            }else if (flag==6){
                                leadIdAdapter.getpassbarcode(response,barcodeDetails);
                            }else if (flag==7){
                                ratEnterFrag.getpassbarcode(response,barcodeDetails);
                            }else if (flag==8){
                                attachBarcodeAdpter.getpassbarcode(response,barcodeDetails);
                            }else if (flag==9){
                                scanBarcodeAdapter.getpassbarcode(response,barcodeDetails,fromcscanbarcodeadapter);
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
