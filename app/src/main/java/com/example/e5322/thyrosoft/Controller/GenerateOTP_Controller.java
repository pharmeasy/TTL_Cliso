package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateOTP_Controller {

    private Activity mActivity;
    private GlobalClass globalClass;
    private AntiBodyEnterFrag antiBodyEnterFrag;
    private Covidenter_Frag covidenter_frag;
    private RATEnterFrag ratEnterFrag;
    int flag;
    ConnectionDetector cd;

    public GenerateOTP_Controller(Activity activity, AntiBodyEnterFrag antiBodyEnterFrag) {
        this.mActivity = activity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public GenerateOTP_Controller(Activity activity, Covidenter_Frag covidenter_frag) {
        this.mActivity = activity;
        this.covidenter_frag = covidenter_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public GenerateOTP_Controller(Activity activity, RATEnterFrag ratEnterFrag) {
        this.mActivity = activity;
        this.ratEnterFrag = ratEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }


    public void generteotpController(String apikey, String mobileno, String usercode) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            COVIDgetotp_req coviDgetotp_req = new COVIDgetotp_req();
            coviDgetotp_req.setApi_key(apikey);
            coviDgetotp_req.setMobile(mobileno);
            coviDgetotp_req.setScode(usercode);
            Call<Covidotpresponse> covidotpresponseCall = postAPIInteface.generateotp(coviDgetotp_req);
            covidotpresponseCall.enqueue(new Callback<Covidotpresponse>() {
                @Override
                public void onResponse(Call<Covidotpresponse> call, Response<Covidotpresponse> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            antiBodyEnterFrag.generateOTPResponse(response);
                        } else if (flag == 1) {
                            covidenter_frag.generateOTPResponse(response);
                        } else if (flag == 2) {
                            ratEnterFrag.generateOTPResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Covidotpresponse> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
