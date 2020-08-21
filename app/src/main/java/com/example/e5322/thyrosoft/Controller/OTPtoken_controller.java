package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.OTPrequest;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPtoken_controller {
    public static String OTPAPPID = "9";
    private Activity mActivity;
    private GlobalClass globalClass;
    private BS_EntryFragment bs_entryFragment;
    private Start_New_Woe start_new_woe;
    private SpecialOffer_Activity specialOffer_activity;
    int flag;
    ConnectionDetector cd;

    public OTPtoken_controller(Activity activity, BS_EntryFragment bs_entryFragment) {
        this.mActivity = activity;
        this.bs_entryFragment = bs_entryFragment;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public OTPtoken_controller(Activity activity, Start_New_Woe start_new_woe) {
        this.mActivity = activity;
        this.start_new_woe = start_new_woe;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public OTPtoken_controller(Activity activity, SpecialOffer_Activity specialOffer_activity) {
        this.mActivity = activity;
        this.specialOffer_activity = specialOffer_activity;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public void getotptokencontroller() {

        if (cd.isConnectingToInternet()) {
            PackageInfo pInfo = null;
            try {
                pInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            int versionCode = pInfo.versionCode;
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.THYROCARE).create(PostAPIInteface.class);
            OTPrequest otPrequest = new OTPrequest();
            otPrequest.setAppId(OTPAPPID);
            otPrequest.setPurpose("OTP");
            otPrequest.setVersion("" + versionCode);
            Call<Tokenresponse> responseCall = apiInterface.getotptoken(otPrequest);

            responseCall.enqueue(new Callback<Tokenresponse>() {
                @Override
                public void onResponse(Call<Tokenresponse> call, Response<Tokenresponse> response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    if (flag == 0) {
                        bs_entryFragment.getotptokenResponse(response);
                    } else if (flag == 1) {
                        start_new_woe.getotptokenResponse(response);
                    } else if (flag == 2) {
                        specialOffer_activity.getotptokenResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<Tokenresponse> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }


    }
}
