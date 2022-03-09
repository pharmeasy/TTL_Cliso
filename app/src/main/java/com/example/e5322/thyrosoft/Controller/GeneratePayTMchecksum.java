package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PayTmChecksumRequestModel;
import com.example.e5322.thyrosoft.Models.PayTmChecksumResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneratePayTMchecksum {


    private ProgressDialog progress;
    private  Activity activity;
    Payment_Activity payment_activity;


    public GeneratePayTMchecksum(Payment_Activity payment_activity) {
        this.payment_activity = payment_activity;
        this.activity = payment_activity;
        progress = GlobalClass.progress(payment_activity, false);
    }


    public void CallAPI(final PayTmChecksumRequestModel payTmChecksumRequestModel) {
        progress.show();
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.THYROCARE).create(PostAPIInterface.class);
        Call<PayTmChecksumResponseModel> responseCall = apiInterface.GetPayTmCheckSum(payTmChecksumRequestModel);

        responseCall.enqueue(new Callback<PayTmChecksumResponseModel>() {
            @Override
            public void onResponse(Call<PayTmChecksumResponseModel> call, Response<PayTmChecksumResponseModel> response) {
                GlobalClass.hideProgress(activity, progress);

                if (response.isSuccessful() && response.body() != null) {
                    payment_activity.getChecksumResponse(response.body(),payTmChecksumRequestModel.getTxtAmount(),payTmChecksumRequestModel.getTxtORDID());
                } else {
                    GlobalClass.ShowError(activity, "Server Error", "Unable to connect to server. Please try after sometime", false);
                }
            }

            @Override
            public void onFailure(Call<PayTmChecksumResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(activity, progress);
                GlobalClass.ShowError(activity, "Server Error", "Unable to connect to server. Please try after sometime", false);
            }
        });
    }
}
