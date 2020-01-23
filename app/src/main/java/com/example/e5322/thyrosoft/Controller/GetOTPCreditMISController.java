package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.OTPCreditMISActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.OTPCreditMISRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.OTPCreditResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOTPCreditMISController {

    private String TAG = GetOTPCreditMISController.class.getSimpleName();
    private Activity mActivity;
    private OTPCreditMISActivity otpCreditMISActivity;
    private int flag;

    public GetOTPCreditMISController(OTPCreditMISActivity otpCreditMISActivity, Activity activity) {
        this.otpCreditMISActivity = otpCreditMISActivity;
        this.mActivity = activity;
        flag = 0;
    }

    public void getOTPCreditMISResponse(OTPCreditMISRequestModel requestModel) {
        ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);

        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.OTP_CREDIT_MIS).create(APIInteface.class);
        Call<OTPCreditResponseModel> responseCall = apiInterface.OTPCreditMIS(requestModel);
        Log.e(TAG, "OTPCreditMIS request --->" + new GsonBuilder().create().toJson(requestModel));
        Log.e(TAG, "OTPCreditMIS URL --->" + responseCall.request().url());

        final ProgressDialog finalProgressDialog = progressDialog;
        responseCall.enqueue(new Callback<OTPCreditResponseModel>() {
            @Override
            public void onResponse(Call<OTPCreditResponseModel> call, Response<OTPCreditResponseModel> response) {
                OTPCreditResponseModel responseModel = response.body();
                GlobalClass.hideProgress(mActivity, finalProgressDialog);
                try {
                    if (responseModel != null) {
                        if (flag == 0) {
                            otpCreditMISActivity.getOTPCreditMISResponse(responseModel);
                        }
                    } else {
                        GlobalClass.showShortToast(mActivity, ToastFile.something_went_wrong);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OTPCreditResponseModel> call, Throwable t) {
                Log.e("Errror", t.getMessage());
            }
        });
    }
}
