package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.Myprofile_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.ProfileDetailsResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MyProfile_activity extends AppCompatActivity {
    public static RequestQueue PostQue;
    TextView nametxt, dojtxt, source_codetxt, closing_bal, credit_lim;
    ImageView back, home;
    ImageView profimg;
    String prof = "";
    private String user;
    private String passwrd;
    private String api_key;
    private String access;
    private String TAG = MyProfile_activity.class.getSimpleName();
    Activity mactivity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);
        mactivity = MyProfile_activity.this;

        initViews();
        initListner();

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
                GlobalClass.goToHome(MyProfile_activity.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        nametxt = (TextView) findViewById(R.id.name);
        dojtxt = (TextView) findViewById(R.id.doj);
        source_codetxt = (TextView) findViewById(R.id.source_code);
        closing_bal = (TextView) findViewById(R.id.closing_bal);
        credit_lim = (TextView) findViewById(R.id.credit_lim);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        profimg = (ImageView) findViewById(R.id.profimg);

        SharedPreferences getshared = getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        prof = getshared.getString("prof", "");
        if (!GlobalClass.isNull(prof)) {
            String name = getshared.getString("name", "");
            String closing_balance = getshared.getString("closing_balance", "");
            String credit_limit = getshared.getString("credit_limit", "");
            String doj = getshared.getString("doj", "");
            String source_code = getshared.getString("source_code", "");
            String tsp_img = getshared.getString("tsp_image", "");

            if (!GlobalClass.isNull(tsp_img)) {
                checkFileExists(tsp_img);
            } else {
                Glide.with(MyProfile_activity.this)
                        .load("")
                        .placeholder(MyProfile_activity.this.getResources().getDrawable(R.drawable.userprofile))
                        .into(profimg);
            }


            GlobalClass.SetText(closing_bal, closing_balance);
            GlobalClass.SetText(credit_lim, credit_limit);
            GlobalClass.SetText(dojtxt, doj);
            GlobalClass.SetText(nametxt, name);
            GlobalClass.SetText(source_codetxt, source_code);
        } else {
            GetData();
        }
    }

    private void GetData() {

        PostQue = Volley.newRequestQueue(MyProfile_activity.this);

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        RequestQueue queue = Volley.newRequestQueue(MyProfile_activity.this);

        String URL = Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile";

        try {
            if (ControllersGlobalInitialiser.myprofile_controller != null) {
                ControllersGlobalInitialiser.myprofile_controller = null;
            }
            ControllersGlobalInitialiser.myprofile_controller = new Myprofile_Controller(mactivity, MyProfile_activity.this);
            ControllersGlobalInitialiser.myprofile_controller.getmyprofilecontroller(URL, queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkFileExists(String str) {
        String url = str;
        if (!url.equals("")) {
            CheckFileExistTask task = new CheckFileExistTask();
            task.execute(url);
        }
    }

    public void getMyprofileRespponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);


            ProfileDetailsResponseModel responseModel = new Gson().fromJson(String.valueOf(response), ProfileDetailsResponseModel.class);

            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getTsp_image())) {
                    prof = responseModel.getTsp_image();
                    checkFileExists(prof);
                }


                GlobalClass.SetText(closing_bal, responseModel.getClosing_balance());
                GlobalClass.SetText(credit_lim, responseModel.getCredit_limit());
                GlobalClass.SetText(dojtxt, responseModel.getDoj());
                GlobalClass.SetText(nametxt, responseModel.getName());
                GlobalClass.SetText(source_codetxt, responseModel.getSource_code());

            } else {
                GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
            }

        } catch (Exception e) {
            Log.e(TAG, "on ERROR ------>" + e.getLocalizedMessage());
        }
    }


    private class CheckFileExistTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // This connection won't follow redirects returned by the remote server.
                HttpURLConnection.setFollowRedirects(false);
                // Open connection to the remote server
                java.net.URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                // Set request method
                con.setRequestMethod("HEAD");
                // get returned code
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (result) {
                    Glide.with(MyProfile_activity.this)
                            .load(prof)
                            .into(profimg);
                } else {
                    Glide.with(MyProfile_activity.this)
                            .load("")
                            .placeholder(MyProfile_activity.this.getResources().getDrawable(R.drawable.user_profile))
                            .into(profimg);
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
