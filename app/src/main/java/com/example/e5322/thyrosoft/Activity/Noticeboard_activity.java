package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
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
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.Models.RequestModels.AckNoticeRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.AckNoticeResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Noticeboard_activity extends AppCompatActivity {
    View view;
    RecyclerView noticeboard_list;
    ImageView back, home;
    String msgCode;
    RequestQueue requestQueueNoticeBoard;
    SharedPreferences prefs;
    LinearLayout offline_img;
    LinearLayoutManager linearLayoutManager;
    String user, passwrd, access, api_key;
    LinearLayout lin_cmsoon;
    private String TAG = getClass().getSimpleName(), CLIENT_TYPE;
    private RequestQueue PostQueOtp;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_noticeboard_);

        noticeboard_list = findViewById(R.id.noticeboard_list);
        linearLayoutManager = new LinearLayoutManager(Noticeboard_activity.this);
        noticeboard_list.setLayoutManager(linearLayoutManager);
        back = findViewById(R.id.back);
        offline_img = findViewById(R.id.offline_img);
        lin_cmsoon = findViewById(R.id.lin_cmsoon);
        home = findViewById(R.id.home);
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

        if (Global.checkForApi21()) {
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
//                getNoticeBoardData();
            }
            getNoticeBoardData();
            offline_img.setVisibility(View.GONE);
            noticeboard_list.setVisibility(View.VISIBLE);
        }
    }

    private void getNoticeBoardData() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(Noticeboard_activity.this);
        requestQueueNoticeBoard = Volley.newRequestQueue(Noticeboard_activity.this);
        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.NoticeBoardData + "" + api_key + "/getNoticeMessages", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
                GlobalClass.hideProgress(Noticeboard_activity.this, progressDialog);

                NoticeBoard_Model noticeBoard_model = new Gson().fromJson(String.valueOf(response), NoticeBoard_Model.class);
                if (noticeBoard_model != null) {
                    if (!GlobalClass.isNull(noticeBoard_model.getResponse()) && noticeBoard_model.getResponse().equalsIgnoreCase(Constants.caps_invalidApikey)) {
                        GlobalClass.redirectToLogin(Noticeboard_activity.this);
                    } else {
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
                                        JSONObject jsonObject = null;
                                        try {
                                            AckNoticeRequestModel requestModel = new AckNoticeRequestModel();
                                            requestModel.setApiKey(api_key);
                                            requestModel.setMessageCode(msgCode);
                                            requestModel.setUserCode(user);

                                            String json = new Gson().toJson(requestModel);
                                            jsonObject = new JSONObject(json);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.acknowledgeNoticeBoard,
                                                jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    Log.e(TAG, "onResponse: response" + response);
                                                    AckNoticeResponseModel responseModel = new Gson().fromJson(String.valueOf(response), AckNoticeResponseModel.class);
                                                    if (responseModel != null) {
                                                        if (!GlobalClass.isNull(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase(Constants.RES0000)) {
                                                            TastyToast.makeText(Noticeboard_activity.this, responseModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                            getNoticeBoardData();
                                                        } else {
                                                            TastyToast.makeText(Noticeboard_activity.this, responseModel.getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                        }
                                                    } else {
                                                        Toast.makeText(Noticeboard_activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
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

                                        GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
                                        PostQueOtp.add(jsonObjectRequest1);
                                        Log.e(TAG, "onClick: URL" + jsonObjectRequest1);
                                        Log.e(TAG, "post data: " + jsonObject);
                                    }
                                });
                                noticeboard_list.setAdapter(noticeBoard_adapter);
                            } else {
                                Toast.makeText(Noticeboard_activity.this, "" + noticeBoard_model.getResponse(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.hideProgress(Noticeboard_activity.this, progressDialog);
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(Noticeboard_activity.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }
                }
            }
        });

        GlobalClass.volleyRetryPolicy(jsonObjectRequestProfile);
        requestQueueNoticeBoard.add(jsonObjectRequestProfile);
        Log.e(TAG, "getNoticeBoardData: url" + jsonObjectRequestProfile);

    }
}