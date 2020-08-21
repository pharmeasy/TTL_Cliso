package com.example.e5322.thyrosoft.Repository;

import android.app.Activity;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import com.example.e5322.thyrosoft.Controller.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthRepository {

    APIInteface apiInterface;

    public HealthRepository() {
    }

    public MutableLiveData<List<HealthTipsApiResponseModel.HArt>> getHealthrepositoy(Context context) {
        apiInterface = RetroFit_APIClient.getInstance().getClient((Activity)context, Api.BASE_URL_TOCHECK).create(APIInteface.class);
        final MutableLiveData<List<HealthTipsApiResponseModel.HArt>> listMutableLiveData = new MutableLiveData<>();
        Call<HealthTipsApiResponseModel> responseCall = apiInterface.getHealth();
        responseCall.enqueue(new Callback<HealthTipsApiResponseModel>() {
            @Override
            public void onResponse(Call<HealthTipsApiResponseModel> call, Response<HealthTipsApiResponseModel> response) {
                try {
                    if (response.body()!=null && response.body().getHArt()!=null && response.body().getHArt().length != 0) {
                        listMutableLiveData.setValue(Arrays.asList(response.body().getHArt()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<HealthTipsApiResponseModel> call, Throwable t) {
                Log.e("TGA", "on failure: " + t.getLocalizedMessage());
            }
        });
        return listMutableLiveData;
    }
}
