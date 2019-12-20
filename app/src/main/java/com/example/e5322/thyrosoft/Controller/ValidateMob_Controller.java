package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
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


    public void callvalidatemob(String user, String chechnumber) {

        ProgressDialog progressDialog = null;
        if (flag == 1) {
            progressDialog = GlobalClass.ShowprogressDialog(specialOffer_activity);
        } else if (flag == 2) {
            progressDialog = GlobalClass.ShowprogressDialog((start_new_woe.getActivity()));

        }


        PostValidateRequest postValidateRequest = new PostValidateRequest();
        postValidateRequest.setUserCode(user);
        postValidateRequest.setDomain("Cli-So");

        if (flag == 1)
            postValidateRequest.setPurpose("Offer");
        else
            postValidateRequest.setPurpose("woe");

        postValidateRequest.setMobile(chechnumber);

        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.OTPgenegrate).create(APIInteface.class);

        Call<ValidateOTPmodel> responseCall = apiInterface.ValidateMob(postValidateRequest);
        Log.e("TAG", "Validate Mobile URL --->" + responseCall.request().url());
       // Log.e("TAG", "Validate Mobile Request --->" + new GsonBuilder().create().toJson(responseCall));

        final ProgressDialog finalProgressDialog = progressDialog;

        responseCall.enqueue(new Callback<ValidateOTPmodel>() {
            @Override
            public void onResponse(Call<ValidateOTPmodel> call, Response<ValidateOTPmodel> response) {
                ValidateOTPmodel validateOTPmodel = response.body();
                try {
                    if (validateOTPmodel.getResponseId().equals("RES0001")) {

                        if (flag == 1) {
                            GlobalClass.hideProgress(specialOffer_activity, finalProgressDialog);
                            specialOffer_activity.onvalidatemob(validateOTPmodel, finalProgressDialog);
                        } else {
                            GlobalClass.hideProgress(((start_new_woe.getActivity())), finalProgressDialog);
                            start_new_woe.onvalidatemob(validateOTPmodel, finalProgressDialog);
                        }

                    } else {
                        if (flag == 1) {
                            GlobalClass.hideProgress(specialOffer_activity, finalProgressDialog);
                            globalClass.showcenterCustomToast(specialOffer_activity, validateOTPmodel.getResponse());
                        } else {
                            GlobalClass.hideProgress((start_new_woe.getActivity()), finalProgressDialog);
                            globalClass.showcenterCustomToast((start_new_woe.getActivity()), validateOTPmodel.getResponse());
                        }

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
