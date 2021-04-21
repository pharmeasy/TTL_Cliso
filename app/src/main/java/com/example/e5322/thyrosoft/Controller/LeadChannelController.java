package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.LeadGenerationActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadChannelRespModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadChannelController {

    LeadGenerationActivity leadGenerationActivity;
    private ProgressDialog progress;
    private Context context;


    public LeadChannelController(LeadGenerationActivity leadGenerationActivity) {

        this.leadGenerationActivity = leadGenerationActivity;
        this.context = leadGenerationActivity;
        progress = GlobalClass.progress(context, false);

    }

    public void GetLeadChannel() {
        try {
            progress.show();
            APIInteface apiInterface = null;
            try {
                apiInterface = RetroFit_APIClient.getInstance().getClient(leadGenerationActivity,Api.Velso).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Call<LeadChannelRespModel> responseCall = apiInterface.getLeadChannel();

            responseCall.enqueue(new Callback<LeadChannelRespModel>() {
                @Override
                public void onResponse(Call<LeadChannelRespModel> call, Response<LeadChannelRespModel> response) {
                    GlobalClass.hideProgress(context, progress);

                    if (response.isSuccessful() && response.body() != null) {
                        LeadChannelRespModel leadChannelRespModel = response.body();
                        leadGenerationActivity.getLeadChannel(leadChannelRespModel);
                    } else {
                        leadGenerationActivity.getLeadChannel(null);

                    }
                }

                @Override
                public void onFailure(Call<LeadChannelRespModel> call, Throwable t) {
                    GlobalClass.hideProgress(context, progress);
                    leadGenerationActivity.getLeadChannel(null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
