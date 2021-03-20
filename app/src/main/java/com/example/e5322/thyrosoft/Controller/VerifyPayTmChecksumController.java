package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.PaytmVerifyChecksumRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PaytmVerifyChecksumResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;

public class VerifyPayTmChecksumController {

    private Activity mActivity;
    private ProgressDialog progress;
    Payment_Activity payment_activity;

    public VerifyPayTmChecksumController(Payment_Activity payment_activity) {
        this.mActivity = payment_activity;
        this.payment_activity = payment_activity;
        progress = GlobalClass.progress(mActivity, false);
    }

    public void verifyChecksum(final PaytmVerifyChecksumRequestModel requestModel) {
        try {
            progress.show();
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.THYROCARE).create(PostAPIInteface.class);
            Call<PaytmVerifyChecksumResponseModel> responseModelCall = apiInterface.callPaytmVerifyChecksumAPI(requestModel);
            responseModelCall.enqueue(new Callback<PaytmVerifyChecksumResponseModel>() {
                @Override
                public void onResponse(Call<PaytmVerifyChecksumResponseModel> call, retrofit2.Response<PaytmVerifyChecksumResponseModel> response) {
                    PaytmVerifyChecksumResponseModel responseModel = response.body();
                    GlobalClass.hideProgress(mActivity, progress);
                    if (response.isSuccessful() && responseModel != null) {
                        payment_activity.getVerifyPayTmChecksum(responseModel, requestModel.getORDERID());
                    } else {
                        GlobalClass.showCustomToast(mActivity, ToastFile.something_went_wrong, 0);
                    }
                }

                @Override
                public void onFailure(Call<PaytmVerifyChecksumResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progress);
                    GlobalClass.showCustomToast(mActivity, ToastFile.something_went_wrong, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.hideProgress(mActivity, progress);
            GlobalClass.showCustomToast(mActivity, ToastFile.something_went_wrong, 0);
        }
    }
}
