package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PostValidateRequest;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Retrofit.GetAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateMob_Controller {
    SpecialOffer_Activity specialOffer_activity;
    GlobalClass globalClass;
    int flag = 0;

    public ValidateMob_Controller(SpecialOffer_Activity mContext) {
        this.specialOffer_activity = mContext;
        globalClass = new GlobalClass(mContext);
        flag = 1;
    }


    public void callvalidatemob(String user, String chechnumber) {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(specialOffer_activity);

        PostValidateRequest postValidateRequest = new PostValidateRequest();
        postValidateRequest.setUserCode(user);
        postValidateRequest.setDomain("Cli-So");
        postValidateRequest.setPurpose("Offer");
        postValidateRequest.setMobile(chechnumber);

        GetAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.OTPgenegrate).create(GetAPIInteface.class);

        Call<ValidateOTPmodel> responseCall = apiInterface.ValidateMob(postValidateRequest);
        Log.e("TAG", "Validate Mobile URL --->" + responseCall.request().url());
        Log.e("TAG", "Validate Mobile response --->" + new GsonBuilder().create().toJson(postValidateRequest));

        responseCall.enqueue(new Callback<ValidateOTPmodel>() {
            @Override
            public void onResponse(Call<ValidateOTPmodel> call, Response<ValidateOTPmodel> response) {
                ValidateOTPmodel validateOTPmodel = response.body();
                try {
                    if (validateOTPmodel.getResponseId().equals("RES0001")) {
                        if (flag == 1) {
                            GlobalClass.hideProgress(specialOffer_activity, progressDialog);
                            specialOffer_activity.onvalidatemob(validateOTPmodel, progressDialog);
                        }
                    } else {
                        GlobalClass.hideProgress(specialOffer_activity, progressDialog);
                        globalClass.showcenterCustomToast(specialOffer_activity, validateOTPmodel.getResponse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ValidateOTPmodel> call, Throwable t) {
                Log.e("Errror", t.getMessage());
            }
        });




    }

}
