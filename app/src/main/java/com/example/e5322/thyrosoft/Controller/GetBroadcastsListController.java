package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.BroadcastActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBroadcastsResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBroadcastsListController {

    private Activity mactivity;
    private BroadcastActivity broadcastActivity;

    public GetBroadcastsListController(BroadcastActivity broadcastActivity) {
        this.mactivity = broadcastActivity;
        this.broadcastActivity = broadcastActivity;
    }

    public void getBroadcasts(String apiKey) {
        ProgressDialog progressDialog = null;
        try {
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.BROADCAST).create(APIInteface.class);
            Call<GetBroadcastsResponseModel> responseCall = apiInterface.callGetBroadcastAPI(apiKey);

            final ProgressDialog finalProgressDialog = progressDialog;
            responseCall.enqueue(new Callback<GetBroadcastsResponseModel>() {
                @Override
                public void onResponse(Call<GetBroadcastsResponseModel> call, Response<GetBroadcastsResponseModel> response) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    GetBroadcastsResponseModel responseModel = response.body();
                    if (response.isSuccessful() && responseModel != null) {
                        broadcastActivity.getBroadcastListResponse(responseModel);
                    } else {
                        GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                    }
                }

                @Override
                public void onFailure(Call<GetBroadcastsResponseModel> call, Throwable t) {
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
