package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PostValidateRequest;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateMob_Controller {
    SpecialOffer_Activity specialOffer_activity;
    GlobalClass globalClass;
    Start_New_Woe start_new_woe;
    int flag = 0;

    public ValidateMob_Controller(Start_New_Woe start_new_woe) {
        this.start_new_woe = start_new_woe;
        globalClass = new GlobalClass((start_new_woe.getActivity()));
        flag = 2;
    }

    public ValidateMob_Controller(SpecialOffer_Activity mContext) {
        this.specialOffer_activity = mContext;
        globalClass = new GlobalClass(mContext);
        flag = 1;
    }


    public void callvalidatemob(String user, String chechnumber, String token) {

        ProgressDialog progressDialog = null;
        if (flag == 1) {
            progressDialog = GlobalClass.ShowprogressDialog(specialOffer_activity);
        } else if (flag == 2) {
            progressDialog = GlobalClass.ShowprogressDialog((start_new_woe.getActivity()));
        }

        PostValidateRequest postValidateRequest = new PostValidateRequest();
        postValidateRequest.setUserCode(user);
        postValidateRequest.setDomain("Cli-So");
        postValidateRequest.setAccessToken(token);

        if (flag == 1)
            postValidateRequest.setPurpose("Offer");
        else
            postValidateRequest.setPurpose("woe");

        postValidateRequest.setMobile(chechnumber);
        APIInteface apiInterface;
        if (flag == 1) {
            apiInterface = RetroFit_APIClient.getInstance().getClient(specialOffer_activity, Api.OTPgenegrate).create(APIInteface.class);
        } else {
            apiInterface = RetroFit_APIClient.getInstance().getClient(start_new_woe.getActivity(), Api.OTPgenegrate).create(APIInteface.class);
        }


        Call<ValidateOTPmodel> responseCall = apiInterface.ValidateMob(postValidateRequest);

        final ProgressDialog finalProgressDialog = progressDialog;

        responseCall.enqueue(new Callback<ValidateOTPmodel>() {
            @Override
            public void onResponse(Call<ValidateOTPmodel> call, Response<ValidateOTPmodel> response) {
                ValidateOTPmodel validateOTPmodel = response.body();
                GlobalClass.hideProgress(specialOffer_activity, finalProgressDialog);
                try {
                    if (validateOTPmodel != null && !GlobalClass.isNull(validateOTPmodel.getResponseId()) && validateOTPmodel.getResponseId().equalsIgnoreCase(Constants.RES0001)) {
                        if (flag == 1) {
                            specialOffer_activity.onvalidatemob(validateOTPmodel);
                        } else {
                            start_new_woe.onvalidatemob(validateOTPmodel);
                        }
                    } else {
                        if (flag == 1) {
                            GlobalClass.showTastyToast(specialOffer_activity, validateOTPmodel.getResponse(), 1);
                        } else {
                            GlobalClass.showTastyToast((start_new_woe.getActivity()), validateOTPmodel.getResponse(), 2);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ValidateOTPmodel> call, Throwable t) {
                GlobalClass.hideProgress(specialOffer_activity, finalProgressDialog);
            }
        });
    }

}
