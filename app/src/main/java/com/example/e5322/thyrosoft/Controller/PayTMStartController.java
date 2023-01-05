package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.WOEPaymentActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.StartPayTmRespModel;
import com.example.e5322.thyrosoft.Models.StartPaytmReqModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayTMStartController {

    private ProgressDialog progress;
    private Activity activity;
    WOEPaymentActivity woePaymentActivity;

    public PayTMStartController(WOEPaymentActivity woePaymentActivity) {
        this.woePaymentActivity = woePaymentActivity;
        this.activity = woePaymentActivity;
        progress = GlobalClass.progress(activity, false);
    }


    public void CallAPI(StartPaytmReqModel startPaytmReqModel) {
        try {
            progress.show();
            APIInteface geTapiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Techso).create(APIInteface.class);
            Call<StartPayTmRespModel> responseCall = geTapiInterface.startPaytmTrans(startPaytmReqModel);
            responseCall.enqueue(new Callback<StartPayTmRespModel>() {
                @Override
                public void onResponse(Call<StartPayTmRespModel> call, Response<StartPayTmRespModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        woePaymentActivity.getPaytmStartresponse(response.body());
                    } else {
                        Toast.makeText(activity, "" + ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StartPayTmRespModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
