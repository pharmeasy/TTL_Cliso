package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.NoticeBoard_Adapter;
import com.example.e5322.thyrosoft.Controller.AcknowledgeNoticeBoard_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.NoticeBoardController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.Models.RequestModels.AckNoticeRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.AckNoticeResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Noticeboard_activity extends AppCompatActivity {

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
    Activity mactivity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_noticeboard_);
        initviews();
        initListner();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initviews() {
        noticeboard_list = findViewById(R.id.noticeboard_list);
        linearLayoutManager = new LinearLayoutManager(mactivity);
        noticeboard_list.setLayoutManager(linearLayoutManager);
        back = findViewById(R.id.back);
        offline_img = findViewById(R.id.offline_img);
        lin_cmsoon = findViewById(R.id.lin_cmsoon);
        home = findViewById(R.id.home);
        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");

        if (!GlobalClass.isNetworkAvailable(mactivity)) {
            offline_img.setVisibility(View.VISIBLE);
            noticeboard_list.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "CLIENT_TYPE: " + CLIENT_TYPE);
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                lin_cmsoon.setVisibility(View.VISIBLE);
            } else {
                lin_cmsoon.setVisibility(View.GONE);
            }
            getNoticeBoardData();
            offline_img.setVisibility(View.GONE);
            noticeboard_list.setVisibility(View.VISIBLE);
        }
    }

    private void initListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(mactivity);
            }
        });

    }

    private void getNoticeBoardData() {

        requestQueueNoticeBoard = Volley.newRequestQueue(mactivity);

        try {
            if (ControllersGlobalInitialiser.noticeBoardController != null) {
                ControllersGlobalInitialiser.noticeBoardController = null;
            }
            ControllersGlobalInitialiser.noticeBoardController = new NoticeBoardController(mactivity, Noticeboard_activity.this);
            ControllersGlobalInitialiser.noticeBoardController.noticeboardController(api_key, requestQueueNoticeBoard);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getnoticeboardresponse(JSONObject response) {
        Log.e(TAG, "onResponse: " + response);
        NoticeBoard_Model noticeBoard_model = new Gson().fromJson(String.valueOf(response), NoticeBoard_Model.class);
        if (noticeBoard_model != null) {

            if (!GlobalClass.isNull(noticeBoard_model.getResponse()) && noticeBoard_model.getResponse().equalsIgnoreCase(Constants.caps_invalidApikey)) {
                GlobalClass.redirectToLogin(mactivity);
            } else {
                ArrayList<NoticeBoard_Model> array_notice = new ArrayList<>();
                if (noticeBoard_model != null && GlobalClass.checkArray(noticeBoard_model.getMessages())) {
                    array_notice.add(noticeBoard_model);
                    if (!GlobalClass.isNull(array_notice.get(0).getMessages()[0].getMessageCode())) {
                        msgCode = (array_notice.get(0).getMessages()[0].getMessageCode());
                        NoticeBoard_Adapter noticeBoard_adapter = new NoticeBoard_Adapter(mactivity, array_notice, msgCode);

                        noticeBoard_adapter.clickListerforAckNoticeboard(new RefreshNoticeBoard() {
                            @Override
                            public void onClickAcknowledge(String msgCode) {
                                PostQueOtp = Volley.newRequestQueue(mactivity);
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

                                try {
                                    if (ControllersGlobalInitialiser.acknowledgeNoticeBoard_controller != null) {
                                        ControllersGlobalInitialiser.acknowledgeNoticeBoard_controller = null;
                                    }
                                    ControllersGlobalInitialiser.acknowledgeNoticeBoard_controller = new AcknowledgeNoticeBoard_Controller(mactivity, Noticeboard_activity.this);
                                    ControllersGlobalInitialiser.acknowledgeNoticeBoard_controller.acknowledgeNoticeBoardController(jsonObject, PostQueOtp);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        noticeboard_list.setAdapter(noticeBoard_adapter);
                    } else {
                        GlobalClass.showTastyToast(mactivity, "" + noticeBoard_model.getResponse(), 2);
                    }
                }
            }
        }
    }

    public void getacknowledgeNoticeBoard(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: response" + response);
            AckNoticeResponseModel responseModel = new Gson().fromJson(String.valueOf(response), AckNoticeResponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase(Constants.RES0000)) {

                    GlobalClass.showTastyToast(mactivity, responseModel.getResponse(), 1);
                    getNoticeBoardData();
                } else {
                    GlobalClass.showTastyToast(mactivity, responseModel.getResponse(), 2);
                }
            } else {
                GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}