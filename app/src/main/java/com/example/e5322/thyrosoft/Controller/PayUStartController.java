package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.WOEPaymentActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PayUReqModel;
import com.example.e5322.thyrosoft.Models.StartPayURespModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayUStartController {

    private ProgressDialog progress;
    private Activity activity;
    WOEPaymentActivity woePaymentActivity;

    public PayUStartController(WOEPaymentActivity woePaymentActivity) {
        this.woePaymentActivity = woePaymentActivity;
        this.activity = woePaymentActivity;
        progress = GlobalClass.progress(activity, false);
    }


    public void CallAPI(PayUReqModel payUReqModel) {
        try {
            progress.show();
            APIInteface geTapiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Techso).create(APIInteface.class);
            Call<StartPayURespModel> responseCall = geTapiInterface.startPayUTrans(payUReqModel);
            responseCall.enqueue(new Callback<StartPayURespModel>() {
                @Override
                public void onResponse(Call<StartPayURespModel> call, Response<StartPayURespModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        woePaymentActivity.getPayUstartResponse(response.body());
                    } else {
                        Toast.makeText(activity, "" + ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StartPayURespModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
