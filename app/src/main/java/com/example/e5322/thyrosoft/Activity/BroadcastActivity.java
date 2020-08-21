package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.BroadcastAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.NoticeBoardController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class BroadcastActivity extends AppCompatActivity {

    ImageView back, home;
    RecyclerView rvBroadcast;
    LinearLayoutManager linearLayoutManager;
    BroadcastAdapter broadcastAdapter;
    RequestQueue requestQueue;
    NoticeBoard_Model noticeBoard_model;
    SharedPreferences prefs;
    String api_key, user, passwrd, access, msgCode;
    String TAG = BroadcastActivity.class.getName();
    private LinearLayout offline_ll;
    Activity mactivity;
    ConnectionDetector cd;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        mactivity = BroadcastActivity.this;
        cd = new ConnectionDetector(mactivity);

        initViews();
        initListner();

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        if (!GlobalClass.isNetworkAvailable(BroadcastActivity.this)) {
            offline_ll.setVisibility(View.VISIBLE);
            rvBroadcast.setVisibility(View.GONE);
        } else {
            offline_ll.setVisibility(View.GONE);
            rvBroadcast.setVisibility(View.VISIBLE);
        }

        getBroadcastData();

    }


    private void initViews() {

        offline_ll = (LinearLayout) findViewById(R.id.offline_ll);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        rvBroadcast = (RecyclerView) findViewById(R.id.rvBroadcast);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvBroadcast.setLayoutManager(linearLayoutManager);
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
                GlobalClass.goToHome(BroadcastActivity.this);
            }
        });
    }


    private void getBroadcastData() {

        requestQueue = Volley.newRequestQueue(BroadcastActivity.this);
        try {
            if (ControllersGlobalInitialiser.noticeBoardController != null) {
                ControllersGlobalInitialiser.noticeBoardController = null;
            }
            ControllersGlobalInitialiser.noticeBoardController = new NoticeBoardController(mactivity, BroadcastActivity.this);
            ControllersGlobalInitialiser.noticeBoardController.noticeboardController(api_key,requestQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getnoticeboardresponse(JSONObject response) {
        Log.e(TAG, "onResponse: " + response);

        String getReposne = response.optString("response", "");

        if (!GlobalClass.isNull(getReposne) && getReposne.equalsIgnoreCase(caps_invalidApikey)) {
            GlobalClass.redirectToLogin(BroadcastActivity.this);
        } else {
            Gson gson = new Gson();
            noticeBoard_model = new NoticeBoard_Model();
            noticeBoard_model = gson.fromJson(response.toString(), NoticeBoard_Model.class);

            ArrayList<NoticeBoard_Model> array_notice = new ArrayList<>();

            if (noticeBoard_model != null && GlobalClass.checkArray(noticeBoard_model.getMessages())) {
                array_notice.add(noticeBoard_model);
                if (!GlobalClass.isNull(array_notice.get(0).getMessages()[0].getMessageCode()) &&
                        array_notice.get(0).getMessages()[0].getMessageCode() != null) {
                    msgCode = (array_notice.get(0).getMessages()[0].getMessageCode());
                    broadcastAdapter = new BroadcastAdapter(BroadcastActivity.this, array_notice);
                    rvBroadcast.setAdapter(broadcastAdapter);
                } else {
                    GlobalClass.showTastyToast(mactivity, "" + noticeBoard_model.getResponse().toString(), 2);

                }
            } else {
                GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
            }
        }
    }
}
