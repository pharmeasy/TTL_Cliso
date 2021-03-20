package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.DyanamicPaymentReqModel;
import com.example.e5322.thyrosoft.Models.DynamicPaymentResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DynamicPaymentController {

    Payment_Activity payment_activity;
    private ProgressDialog progress;
    Activity activity;

    public DynamicPaymentController(Payment_Activity payment_activity) {
        this.activity = payment_activity;
        this.payment_activity = payment_activity;
        progress = GlobalClass.progress(activity, false);
    }


    public void CallAPI(DyanamicPaymentReqModel dyanamicPaymentReqModel) {
        try {
            progress.show();

            APIInteface apiInterface = null;
            try {
                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.THYROCARE).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Call<DynamicPaymentResponseModel> responseCall = apiInterface.GetDynamicPayment(dyanamicPaymentReqModel);
            responseCall.enqueue(new Callback<DynamicPaymentResponseModel>() {
                @Override
                public void onResponse(Call<DynamicPaymentResponseModel> call, Response<DynamicPaymentResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        DynamicPaymentResponseModel dynamicPaymentResponseModel = response.body();
                        payment_activity.getpostresponse(dynamicPaymentResponseModel);
                    } else {
                        GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 1);
                        payment_activity.getpostresponse(null);
                    }
                }

                @Override
                public void onFailure(Call<DynamicPaymentResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    payment_activity.getpostresponse(null);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
