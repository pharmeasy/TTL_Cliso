package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetTermsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PostTermsRequestModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;

public class TermsAndConditionsActivity extends AppCompatActivity {
    WebView wv_content;
    CheckBox cb_agree;
    Button btn_submit;
    TermsAndConditionsActivity mActivity;
    String Url, user, CLIENT_TYPE, logoutflag = "0";
    Boolean toShow;
    ImageView back;
    LinearLayout appbar;
    AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        mActivity = TermsAndConditionsActivity.this;
        appPreferenceManager = new AppPreferenceManager(mActivity);
        toShow = getIntent().getBooleanExtra("toshow", true);
        logoutflag = getIntent().getStringExtra("logoutflag");
        initUI();
        initListener();
    }

    private void initListener() {
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
//                    btn_submit.setEnabled(false);
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_rounded_button_grey));
                } else {
//                    btn_submit.setEnabled(true);
                    btn_submit.setBackground(getResources().getDrawable(R.drawable.bg_rounded_button));
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_agree.isChecked()) {
                    PostTermsRequestModel postTermsRequestModel = new PostTermsRequestModel();
                    postTermsRequestModel.setAppId(Constants.USER_APPID);
                    postTermsRequestModel.setAgreementLink(Url);
                    postTermsRequestModel.setClientCode(user);
                    postTermsRequestModel.setClientType(CLIENT_TYPE);
                    postTermsRequestModel.setDeviceID(GlobalClass.getIMEINo(mActivity));
                    postTermsRequestModel.setIPAddress(Global.getIPAddress(true));
                    postTermsRequestModel.setLatitude(Global.getCurrentLatLong(mActivity).getmLatitude());
                    postTermsRequestModel.setLongitude(Global.getCurrentLatLong(mActivity).getmLongitude());
                    callPostTermsAPI(postTermsRequestModel);

                } else {
                    Global.showCustomToast(mActivity, "Please accept terms and conditions");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callPostTermsAPI(PostTermsRequestModel requestModel) {
        try {
            APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.Cloud_base).create(APIInteface.class);
            Call<GetTermsResponseModel> call = apiInteface.PostTermsDetails(requestModel);
            call.enqueue(new Callback<GetTermsResponseModel>() {
                @Override
                public void onResponse(Call<GetTermsResponseModel> call, retrofit2.Response<GetTermsResponseModel> response) {
                    if (response.body() != null && !GlobalClass.isNull(response.body().getResponseId())
                            && response.body().getResponseId().equalsIgnoreCase(Constants.RES0000)) {
                        if (!response.body().getTermFlag()) {
                            appPreferenceManager.setTermsFlag(response.body().getTermFlag());
                            Global.showCustomToast(mActivity, response.body().getResponse());
                            finish();
                        } else {
                            Global.showCustomToast(mActivity, response.body().getResponse());
                        }
                    } else {
                        Global.showCustomToast(mActivity, "Something Went Wrong");
                    }
                }

                @Override
                public void onFailure(Call<GetTermsResponseModel> call, Throwable t) {
                    Global.showCustomToast(mActivity, "Something Went Wrong");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {

        appbar = findViewById(R.id.appbar);
        back = findViewById(R.id.back);
        wv_content = findViewById(R.id.wv_content);
        cb_agree = findViewById(R.id.cb_agree);
        btn_submit = findViewById(R.id.btn_submit);


        SharedPreferences prefs = mActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");

        Url = getIntent().getStringExtra("Link");
        if (!GlobalClass.isNull(Url)) {
            wv_content.loadUrl(Url);
            wv_content.canGoBack();
        }

        if (!toShow) {
            appbar.setVisibility(View.VISIBLE);
            cb_agree.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (logoutflag.equalsIgnoreCase("0")) {
            finish();
        } else {
            logout();
        }

    }

    private void logout() {
        appPreferenceManager.clearAllPreferences();

        SharedPreferences.Editor getProfileName = getSharedPreferences("profilename", MODE_PRIVATE).edit();
        getProfileName.clear();
        getProfileName.commit();

        SharedPreferences.Editor profile = getSharedPreferences("profile", MODE_PRIVATE).edit();
        profile.clear();
        profile.commit();


        SharedPreferences.Editor getprodutcs = getSharedPreferences("MyObject", MODE_PRIVATE).edit();
        getprodutcs.clear();
        getprodutcs.commit();


        SharedPreferences.Editor editor1 = getSharedPreferences("profilename", 0).edit();
        editor1.clear();
        editor1.commit();

        SharedPreferences.Editor editor = getSharedPreferences("Userdetails", 0).edit();
        editor.clear();
        editor.commit();

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(TermsAndConditionsActivity.this);
        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
        prefsEditor1.clear();
        prefsEditor1.commit();

        ClearScpSRFData();
        clearApplicationData();

        Constants.covidwoe_flag = "0";
        Constants.covidfrag_flag = "0";
        Constants.ratfrag_flag = "0";
        Constants.pushrat_flag = 0;
        Constants.universal = 0;
        Constants.PUSHNOT_FLAG = false;

        Intent f = new Intent(getApplicationContext(), SplashScreen.class);
        f.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(f);
    }

    private void ClearScpSRFData() {
        SharedPreferences.Editor asdas = getSharedPreferences("SCPDATA", 0).edit();
        asdas.clear();
        asdas.commit();
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }
}