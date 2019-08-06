package com.example.e5322.thyrosoft.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.Retrofit.GetAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthRepository {

    GetAPIInteface apiInterface;

    public HealthRepository() {
    }

    public MutableLiveData<List<HealthTipsApiResponseModel.HArt>> getHealthrepositoy() {
        apiInterface = RetroFit_APIClient.getInstance().getClient(Api.BASE_URL_TOCHECK).create(GetAPIInteface.class);
        final MutableLiveData<List<HealthTipsApiResponseModel.HArt>> listMutableLiveData = new MutableLiveData<>();
        Call<HealthTipsApiResponseModel> responseCall = apiInterface.getHealth();
        Log.e("TAG", "URL --->" + responseCall.request().url());
        responseCall.enqueue(new Callback<HealthTipsApiResponseModel>() {
            @Override
            public void onResponse(Call<HealthTipsApiResponseModel> call, Response<HealthTipsApiResponseModel> response) {
                listMutableLiveData.setValue(Arrays.asList(response.body().getHArt()));
                Log.e("TGA", "onResponse: " + listMutableLiveData.toString());
            }

            @Override
            public void onFailure(Call<HealthTipsApiResponseModel> call, Throwable t) {
                Log.e("TGA", "on failure: " + t.getLocalizedMessage());
            }
        });
        return listMutableLiveData;
    }
}
