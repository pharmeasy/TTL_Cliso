package com.example.e5322.thyrosoft.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.BroadcastAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class BroadcastActivity extends AppCompatActivity {

    ImageView back, home;
    RecyclerView rvBroadcast;
    LinearLayoutManager linearLayoutManager;
    BroadcastAdapter broadcastAdapter;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    NoticeBoard_Model noticeBoard_model;
    SharedPreferences prefs;
    String api_key, user, passwrd, access, msgCode;
    String TAG = BroadcastActivity.class.getName();
    private LinearLayout offline_ll;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        offline_ll = (LinearLayout) findViewById(R.id.offline_ll);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        rvBroadcast = (RecyclerView) findViewById(R.id.rvBroadcast);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvBroadcast.setLayoutManager(linearLayoutManager);

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

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        if (!GlobalClass.isNetworkAvailable(BroadcastActivity.this)) {
            offline_ll.setVisibility(View.VISIBLE);
            rvBroadcast.setVisibility(View.GONE);
        } else {
            getBroadcastData();
            offline_ll.setVisibility(View.GONE);
            rvBroadcast.setVisibility(View.VISIBLE);
        }
    }

    private void getBroadcastData() {
        progressDialog = new ProgressDialog(BroadcastActivity.this, R.style.ProgressBarColor);
        progressDialog.setTitle("Kindly wait ...");
        progressDialog.setMessage(ToastFile.processing_request);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(20);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        requestQueue = Volley.newRequestQueue(BroadcastActivity.this);

        Log.e(TAG, "Brodcast receiver URL -->" + Api.NoticeBoardData + "" + api_key + "/getNoticeMessages");

        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.NoticeBoardData + "" + api_key + "/getNoticeMessages", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String getReposne = response.optString("response", "");
                if (getReposne.equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(BroadcastActivity.this);
                } else {
                    Gson gson = new Gson();
                    noticeBoard_model = new NoticeBoard_Model();
                    noticeBoard_model = gson.fromJson(response.toString(), NoticeBoard_Model.class);

                    ArrayList<NoticeBoard_Model> array_notice = new ArrayList<>();

                    if (noticeBoard_model.getMessages() != null) {
                        array_notice.add(noticeBoard_model);
                        if (array_notice.get(0).getMessages()[0].getMessageCode() != null) {
                            msgCode = (array_notice.get(0).getMessages()[0].getMessageCode());
                            broadcastAdapter = new BroadcastAdapter(BroadcastActivity.this, array_notice);
                            rvBroadcast.setAdapter(broadcastAdapter);
                        } else {
                            Toast.makeText(BroadcastActivity.this, "" + noticeBoard_model.getResponse().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BroadcastActivity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(BroadcastActivity.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }

                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequestProfile);
        requestQueue.add(jsonObjectRequestProfile);
        Log.e(TAG, "getBroadcastData: url" + jsonObjectRequestProfile);
    }

}
