package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetBroadcastsListController;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.BroadcastAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBroadcastsResponseModel;
import com.example.e5322.thyrosoft.R;

public class BroadcastActivity extends AppCompatActivity {

    private ImageView back, home;
    private RecyclerView rvBroadcast;
    private String api_key, user;
    private final String TAG = BroadcastActivity.class.getName();
    private Activity mActivity;
    private LinearLayout offline_ll;
    private ConnectionDetector cd;
    private TextView tv_noRecordFound;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        mActivity = BroadcastActivity.this;
        cd = new ConnectionDetector(mActivity);
        GlobalClass.setStatusBarcolor(mActivity);
        initUI();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        rvBroadcast.setLayoutManager(linearLayoutManager);
        initListeners();
        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        api_key = prefs.getString("API_KEY", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        callAPIGetBroadcastsList();
    }

    private void initUI() {
        offline_ll = findViewById(R.id.offline_ll);
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        rvBroadcast = findViewById(R.id.rvBroadcast);
        tv_noRecordFound = findViewById(R.id.tv_noRecordFound);
    }

    private void initListeners() {
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

    private void callAPIGetBroadcastsList() {
        try {
            if (cd.isConnectingToInternet()) {
                offline_ll.setVisibility(View.GONE);
                rvBroadcast.setVisibility(View.VISIBLE);
                if (ControllersGlobalInitialiser.getBroadcastsListController != null) {
                    ControllersGlobalInitialiser.getBroadcastsListController = null;
                }
                ControllersGlobalInitialiser.getBroadcastsListController = new GetBroadcastsListController(BroadcastActivity.this);
                ControllersGlobalInitialiser.getBroadcastsListController.getBroadcasts(api_key);
            } else {
                offline_ll.setVisibility(View.VISIBLE);
                rvBroadcast.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBroadcastListResponse(GetBroadcastsResponseModel responseModel) {
        try {
            if (!GlobalClass.isNull(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase(Constants.RES0000)) {
                if (GlobalClass.isArraylistNotNull(responseModel.getMessages())) {
                    tv_noRecordFound.setVisibility(View.GONE);
                    rvBroadcast.setVisibility(View.VISIBLE);
                    BroadcastAdapter broadcastAdapter = new BroadcastAdapter(mActivity, responseModel.getMessages());
                    rvBroadcast.setAdapter(broadcastAdapter);
                } else {
                    tv_noRecordFound.setVisibility(View.VISIBLE);
                    rvBroadcast.setVisibility(View.GONE);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 2);
                tv_noRecordFound.setVisibility(View.VISIBLE);
                rvBroadcast.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
