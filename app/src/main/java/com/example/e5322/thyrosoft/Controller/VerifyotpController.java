package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.Models.VerifyotpRequest;
import com.example.e5322.thyrosoft.Retrofit.GetAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyotpController {

    SpecialOffer_Activity specialOffer_activity;
    GlobalClass globalClass;
    int flag = 0;

    public VerifyotpController(SpecialOffer_Activity mContext) {
        this.specialOffer_activity = mContext;
        globalClass = new GlobalClass(mContext);
        flag = 1;
    }


    public void verifyotp(String user, String chechnumber, String otp) {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(specialOffer_activity);
        VerifyotpRequest verifyotpRequest = new VerifyotpRequest();
        verifyotpRequest.setUserCode(user);
        verifyotpRequest.setDomain("Cli-So");
        verifyotpRequest.setPurpose("Offer");
        verifyotpRequest.setMobile(chechnumber);
        verifyotpRequest.setOTP(otp);

        GetAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.OTPgenegrate).create(GetAPIInteface.class);
        Call<VerifyotpModel> responseCall = apiInterface.Verifyotp(verifyotpRequest);
        Log.e("TAG", "Verify otp reuest --->" + new GsonBuilder().create().toJson(verifyotpRequest));
        Log.e("TAG", "Verify otp URL --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<VerifyotpModel>() {
            @Override
            public void onResponse(Call<VerifyotpModel> call, Response<VerifyotpModel> response) {
                VerifyotpModel verifyotpModel = response.body();

                try {
                    if (verifyotpModel.getResponseId().equals("RES0001")) {
                        if (flag == 1) {
                            GlobalClass.hideProgress(specialOffer_activity, progressDialog);
                            specialOffer_activity.onVerifyotp(verifyotpModel);
                        }
                    } else {
                        GlobalClass.hideProgress(specialOffer_activity, progressDialog);
                        specialOffer_activity.onVerifyotp(verifyotpModel);
                        globalClass.showcenterCustomToast(specialOffer_activity, verifyotpModel.getResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<VerifyotpModel> call, Throwable t) {

                Log.e("Errror", t.getMessage());

            }
        });
    }
}
