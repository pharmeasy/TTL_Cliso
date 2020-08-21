package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidMobverification_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private AntiBodyEnterFrag antiBodyEnterFrag;
    private Covidenter_Frag covidenter_frag;
    private RATEnterFrag ratEnterFrag;
    int flag;
    ConnectionDetector cd;

    public CovidMobverification_Controller(Activity activity, AntiBodyEnterFrag antiBodyEnterFrag) {
        this.mActivity = activity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public CovidMobverification_Controller(Activity activity, Covidenter_Frag covidenter_frag) {
        this.mActivity = activity;
        this.covidenter_frag = covidenter_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public CovidMobverification_Controller(Activity activity, RATEnterFrag ratEnterFrag) {
        this.mActivity = activity;
        this.ratEnterFrag = ratEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public void getcovidmobverifycontroller(String apikey, String mobileno, String usercode) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            CoVerifyMobReq coVerifyMobReq = new CoVerifyMobReq();
            coVerifyMobReq.setApi_key(apikey);
            coVerifyMobReq.setMobile(mobileno);
            coVerifyMobReq.setScode(usercode);

            final Call<COVerifyMobileResponse> covidmis_responseCall = postAPIInteface.covmobileVerification(coVerifyMobReq);

            covidmis_responseCall.enqueue(new Callback<COVerifyMobileResponse>() {
                @Override
                public void onResponse(Call<COVerifyMobileResponse> call, Response<COVerifyMobileResponse> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            antiBodyEnterFrag.getcovidmobverifyResponse(response);
                        } else if (flag == 1) {
                            covidenter_frag.getcovidmobverifyResponse(response);
                        } else if (flag == 2) {
                            ratEnterFrag.getcovidmobverifyResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<COVerifyMobileResponse> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
