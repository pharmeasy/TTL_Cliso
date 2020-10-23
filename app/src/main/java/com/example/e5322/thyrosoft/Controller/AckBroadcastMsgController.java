package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.BroadcastDetailsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.AckBroadcastMsgRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.AckBroadcastMsgResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AckBroadcastMsgController {

    private Activity mactivity;
    private BroadcastDetailsActivity broadcastDetailsActivity;

    public AckBroadcastMsgController(BroadcastDetailsActivity broadcastActivity) {
        this.mactivity = broadcastActivity;
        this.broadcastDetailsActivity = broadcastActivity;
    }

    public void ackBroadcastMsg(AckBroadcastMsgRequestModel requestModel) {
        ProgressDialog progressDialog = null;
        try {
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.BROADCAST).create(PostAPIInteface.class);
            Call<AckBroadcastMsgResponseModel> responseCall = apiInterface.PostAckBroadcastMsgAPI(requestModel);

            final ProgressDialog finalProgressDialog = progressDialog;
            responseCall.enqueue(new Callback<AckBroadcastMsgResponseModel>() {
                @Override
                public void onResponse(Call<AckBroadcastMsgResponseModel> call, Response<AckBroadcastMsgResponseModel> response) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    AckBroadcastMsgResponseModel responseModel = response.body();
                    if (response.isSuccessful() && responseModel != null) {
                        broadcastDetailsActivity.postAckBroadcastMsgResponse(responseModel);
                    } else {
                        GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                    }
                }

                @Override
                public void onFailure(Call<AckBroadcastMsgResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.hideProgress(mactivity, progressDialog);
        }
    }
}
