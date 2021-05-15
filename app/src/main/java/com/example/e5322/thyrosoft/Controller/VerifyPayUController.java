package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.WOEPaymentActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.VerifyPayURespModel;
import com.example.e5322.thyrosoft.Models.VerifyPayUreqModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPayUController {

    private ProgressDialog progress;
    private Activity activity;
    WOEPaymentActivity woePaymentActivity;

    public VerifyPayUController(WOEPaymentActivity woePaymentActivity) {
        this.woePaymentActivity = woePaymentActivity;
        this.activity = woePaymentActivity;
        progress = GlobalClass.progress(activity, false);
    }


    public void CallAPI(VerifyPayUreqModel verifyPayUreqModel) {
        try {
            progress.show();
            APIInteface geTapiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Techso).create(APIInteface.class);
            Call<VerifyPayURespModel> responseCall = geTapiInterface.VerifyPayU(verifyPayUreqModel);
            responseCall.enqueue(new Callback<VerifyPayURespModel>() {
                @Override
                public void onResponse(Call<VerifyPayURespModel> call, Response<VerifyPayURespModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        woePaymentActivity.getVerifyPayUResponse(response.body());
                    } else {
                        Toast.makeText(activity, "" + ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<VerifyPayURespModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
