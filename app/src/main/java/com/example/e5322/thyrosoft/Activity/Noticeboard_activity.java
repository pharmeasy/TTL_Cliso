package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.NoticeBoard_Adapter;
import com.example.e5322.thyrosoft.Fragment.Noticeboard_Fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class Noticeboard_activity extends AppCompatActivity {
    View viewfab, view, viewresultfrag;
    RecyclerView noticeboard_list;
    ImageView back, home;
    String msgCode;
    private Global globalClass;
    private Noticeboard_Fragment.OnFragmentInteractionListener mListener;
    RequestQueue requestQueueNoticeBoard;
    ProgressDialog barProgressDialog;
    AlertDialog alert;
    NoticeBoard_Model noticeBoard_model;
    SharedPreferences prefs;
    LinearLayout offline_img;
    LinearLayoutManager linearLayoutManager;
    String user, passwrd, access, api_key;
    private String TAG = Noticeboard_Fragment.class.getSimpleName().toString(), CLIENT_TYPE;
    private RequestQueue PostQueOtp;
    LinearLayout lin_cmsoon;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_noticeboard_);


        noticeboard_list = (RecyclerView) findViewById(R.id.noticeboard_list);
        linearLayoutManager = new LinearLayoutManager(Noticeboard_activity.this);
        noticeboard_list.setLayoutManager(linearLayoutManager);
        back = (ImageView) findViewById(R.id.back);
        offline_img = (LinearLayout) findViewById(R.id.offline_img);
        lin_cmsoon = findViewById(R.id.lin_cmsoon);
        home = (ImageView) findViewById(R.id.home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Noticeboard_activity.this);
            }
        });

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);

        if (!GlobalClass.isNetworkAvailable(Noticeboard_activity.this)) {
            offline_img.setVisibility(View.VISIBLE);
            noticeboard_list.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "CLIENT_TYPE: " + CLIENT_TYPE);
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                lin_cmsoon.setVisibility(View.VISIBLE);
            } else {
                lin_cmsoon.setVisibility(View.GONE);
                getNoticeBoardData();
            }
            getNoticeBoardData();
            offline_img.setVisibility(View.GONE);
            noticeboard_list.setVisibility(View.VISIBLE);
        }


    }


    private void getNoticeBoardData() {
 /*       barProgressDialog = new ProgressDialog(Noticeboard_activity.this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();*/

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(Noticeboard_activity.this);

        requestQueueNoticeBoard = Volley.newRequestQueue(Noticeboard_activity.this);
        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.NoticeBoardData + "" + api_key + "/getNoticeMessages", new Response.Listener<JSONObject>() {
            private String resIdFromApi;
            private String response1;
            private String messages;

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
               /* if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }*/

               GlobalClass.hideProgress(Noticeboard_activity.this,progressDialog);
                String getReposne = response.optString("response", "");
                if (getReposne.equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Noticeboard_activity.this);
                } else {
                    Gson gson = new Gson();
                    noticeBoard_model = new NoticeBoard_Model();
                    noticeBoard_model = gson.fromJson(response.toString(), NoticeBoard_Model.class);

                    ArrayList<NoticeBoard_Model> array_notice = new ArrayList<>();


                    if (noticeBoard_model.getMessages() != null) {
                        array_notice.add(noticeBoard_model);
                        if (array_notice.get(0).getMessages()[0].getMessageCode() != null) {
                            msgCode = (array_notice.get(0).getMessages()[0].getMessageCode());


                            NoticeBoard_Adapter noticeBoard_adapter = new NoticeBoard_Adapter(Noticeboard_activity.this, array_notice, msgCode);

                            noticeBoard_adapter.clickListerforAckNoticeboard(new RefreshNoticeBoard() {
                                @Override
                                public void onClickAcknowledge(String msgCode) {
                                    PostQueOtp = Volley.newRequestQueue(Noticeboard_activity.this);
                                    JSONObject jsonObjectOtp = new JSONObject();
                                    try {
                                        jsonObjectOtp.put("apiKey", api_key);
                                        jsonObjectOtp.put("messageCode", msgCode);
                                        jsonObjectOtp.put("userCode", user);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.acknowledgeNoticeBoard,
                                            jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                Log.e(TAG, "onResponse: response" + response);
                                                String finalJson = response.toString();
                                                JSONObject parentObjectOtp = new JSONObject(finalJson);
                                                messages = parentObjectOtp.getString("messages");
                                                resIdFromApi = parentObjectOtp.getString("resId");
                                                response1 = parentObjectOtp.getString("response");

                                                if (resIdFromApi.equals("RES0000")) {
                                                    TastyToast.makeText(Noticeboard_activity.this, response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                    getNoticeBoardData();
                                                } else {
                                                    TastyToast.makeText(Noticeboard_activity.this, response1, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if (error != null) {
                                            } else {
                                                System.out.println(error);
                                            }
                                        }
                                    });
                                    PostQueOtp.add(jsonObjectRequest1);
                                    Log.e(TAG, "onClick: URL" + jsonObjectOtp);
                                    Log.e(TAG, "onClick: URL" + jsonObjectRequest1);
                                }
                            });
                            noticeboard_list.setAdapter(noticeBoard_adapter);
                        } else {
                            Toast.makeText(Noticeboard_activity.this, "" + noticeBoard_model.getResponse().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Toast.makeText(Noticeboard_activity.this, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(Noticeboard_activity.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }

                }
            }
        });
        jsonObjectRequestProfile.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueueNoticeBoard.add(jsonObjectRequestProfile);
        Log.e(TAG, "getNoticeBoardData: url" + jsonObjectRequestProfile);
    }
}
