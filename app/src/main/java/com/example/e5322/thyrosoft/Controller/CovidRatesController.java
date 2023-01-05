package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CovidRateReqModel;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidRatesController {

    RATEnterFrag ratEnterFrag;
    Activity activity;

    public CovidRatesController(RATEnterFrag ratEnterFrag, Activity activity) {
        this.ratEnterFrag = ratEnterFrag;
        this.activity = activity;
    }

    public void fetchRates(CovidRateReqModel covidRateReqModel) {
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Call<Covidratemodel> covidratemodelCall = postAPIInterface.displayrates(covidRateReqModel);
        covidratemodelCall.enqueue(new Callback<Covidratemodel>() {
            @Override
            public void onResponse(Call<Covidratemodel> call, Response<Covidratemodel> response) {
                try {
                    Covidratemodel covidratemodel = response.body();
                    if (response.isSuccessful() && covidratemodel != null) {
                        ratEnterFrag.getRatesResponse(covidratemodel);
                    } else {
                        GlobalClass.showTastyToast(activity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidratemodel> call, Throwable t) {

            }
        });
    }
}
