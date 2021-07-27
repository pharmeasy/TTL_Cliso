package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.example.e5322.thyrosoft.Models.AppuserResponse;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;

public class LogUserActivityController {

    private static final String TAG = LogUserActivityController.class.getSimpleName();
    private Activity mActivity;

    public LogUserActivityController(Activity Activity) {
        this.mActivity = Activity;
    }

    public void updateUserActivity(AppuserReq requestModel) {
        try {
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.Velso).create(PostAPIInteface.class);
            Call<AppuserResponse> responseModelCall = apiInterface.PostUserLog(requestModel);
            Log.e("TAG", "API REQ--->" + responseModelCall.request().url());
            Log.e("TAG", "API BODY--->" + new GsonBuilder().create().toJson(requestModel));
            responseModelCall.enqueue(new Callback<AppuserResponse>() {
                @Override
                public void onResponse(Call<AppuserResponse> call, retrofit2.Response<AppuserResponse> response) {
                    try{
                        if (response.body()!=null){
                            AppuserResponse responseModel = response.body();
                            Log.e("TAG", "RESPONSE-->" + responseModel.getRESPONSE());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AppuserResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + "Something went wrong");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
