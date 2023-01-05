package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.InclusionMasterModel;
import com.example.e5322.thyrosoft.Models.PostInclisionMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class POSTBookLeadController {
    private static final String TAG = POSTBookLeadController.class.getSimpleName();
    ProgressDialog progressDialog;
    String header;
    private RequestQueue requestQueue;
    private Activity mActivity;
    private GlobalClass globalClass;
    private ConfirmbookDetail confirmbookDetail;
    private PETCT_Frag mPETCT_Frag;

    public POSTBookLeadController(Activity activity, ConfirmbookDetail confirmbookDetail, String header) {
        this.mActivity = activity;
        this.confirmbookDetail = confirmbookDetail;
        globalClass = new GlobalClass(mActivity);
        this.header = header;
        requestQueue = GlobalClass.setVolleyReq(mActivity);
    }

    public POSTBookLeadController(PETCT_Frag fragment, Activity activity, String header) {
        this.mActivity = activity;
        this.mPETCT_Frag = fragment;
        globalClass = new GlobalClass(mActivity);
        this.header = header;
        requestQueue = GlobalClass.setVolleyReq(mActivity);
    }

    public void postBookLead(boolean b, JSONObject jsonObject) {
        try {
            if (b) {
                progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            }
            System.out.println(jsonObject);
            String url = Api.LEAD_BOOKING;
            System.out.println("postBookLeadAPI" + url);
            System.out.println("postBookLeadAPI post data " + jsonObject);


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                        globalClass.printLog("Error", TAG, "postBookLeadAPIResponse", "" + response);
                        if (response != null) {
                            confirmbookDetail.getPOSTBookLeadResponse(response);
                        } else {
                            GlobalClass.toastyError(mActivity, MessageConstants.SOMETHING_WENT_WRONG, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    GlobalClass.showVolleyError(error, mActivity);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String error_sd = new String(error.networkResponse.data);
                        confirmbookDetail.getPOSTBookLeadErrorResponse(error_sd);
                    }
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    int mStatusCode = response.statusCode;
                    globalClass.printLog("Error", TAG, "StatusCode: ", "" + mStatusCode);
                    return super.parseNetworkResponse(response);
                }

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", header);
                    Log.e(TAG, "Header ----->" + header);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            requestQueue.add(stringRequest);
            GlobalClass.volleyRetryPolicy(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }
    }

    public void CallMASTERInclusionAPI() {
        try {
            progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.MASTERInclusion;
            System.out.println(url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                globalClass.hideProgressDialog(progressDialog, mActivity);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                ArrayList<InclusionMasterModel> res = getMasterInclusionMasterResponseModel(response);
                                mPETCT_Frag.APIMasterInclusionResult(res);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    GlobalClass.showVolleyError(error, mActivity);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String error_sd = new String(error.networkResponse.data);
                        confirmbookDetail.getPOSTBookLeadErrorResponse(error_sd);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", header);
                    Log.e(TAG, "Header ----->" + header);
                    return headers;
                }
            };

            requestQueue.add(stringRequest);
            GlobalClass.volleyRetryPolicy(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }
    }

    private ArrayList<InclusionMasterModel> getMasterInclusionMasterResponseModel(String response) {
        Gson gson = new Gson();
        ArrayList<InclusionMasterModel> resModel = null;
        try {
            TypeToken<ArrayList<InclusionMasterModel>> token = new TypeToken<ArrayList<InclusionMasterModel>>() {
            };
            resModel = gson.fromJson(response, token.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return resModel;
    }

    public void CallSubMitInClusiondataAPI(PostInclisionMaterial ent) {
        try {
            progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String url = Api.POSTADDINCLUSION;

            JSONObject jsonObj = geneRateReqInclusionMaterial(ent);
            JsonObjectRequest jsonreq = new JsonObjectRequest(url, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        globalClass.printLog("Error", TAG, "postBookLeadAPIResponse", "" + response);
                        if (response != null) {

                        } else {
                            GlobalClass.toastyError(mActivity, MessageConstants.SOMETHING_WENT_WRONG, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        globalClass.hideProgressDialog(progressDialog, mActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    GlobalClass.showVolleyError(error, mActivity);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String error_sd = new String(error.networkResponse.data);
                        System.out.println(error_sd);
                    }

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", header);
                    Log.e(TAG, "Header ----->" + header);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            requestQueue.add(jsonreq);
            GlobalClass.volleyRetryPolicy(jsonreq);

        } catch (Exception e) {
            e.printStackTrace();
            requestQueue = null;
        } finally {
            requestQueue = null;
        }
    }

    private JSONObject geneRateReqInclusionMaterial(PostInclisionMaterial ent) throws JSONException {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(ent);
        JSONObject jsonObj = new JSONObject(json);
        return jsonObj;
    }
}