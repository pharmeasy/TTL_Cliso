package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.LeadGenerationActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadPurposeResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeadPurposeController {

    LeadGenerationActivity leadGenerationActivity;
    private ProgressDialog progress;
    private Context context;
    private Activity activity;


    public LeadPurposeController(LeadGenerationActivity leadGenerationActivity) {

        this.leadGenerationActivity = leadGenerationActivity;
        this.activity = leadGenerationActivity;
        this.context = leadGenerationActivity;
        progress = GlobalClass.progress(context, false);

    }

    public void GetLeadPurpose() {
        try {
            progress.show();


            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Velso).create(APIInteface.class);

            Call<LeadPurposeResponseModel> responseCall = apiInterface.getLeadPurpose();

            responseCall.enqueue(new Callback<LeadPurposeResponseModel>() {
                @Override
                public void onResponse(Call<LeadPurposeResponseModel> call, Response<LeadPurposeResponseModel> response) {
                    GlobalClass.hideProgress(context, progress);

                    if (response.isSuccessful() && response.body() != null) {
                        LeadPurposeResponseModel leadPurposeResponseModel = response.body();
                        leadGenerationActivity.getLeadPurpose(leadPurposeResponseModel);
                    } else {
                        leadGenerationActivity.getLeadPurpose(null);
                    }
                }

                @Override
                public void onFailure(Call<LeadPurposeResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(context, progress);
                    leadGenerationActivity.getLeadPurpose(null);
                }
            });
        } catch (Exception e) {
            GlobalClass.hideProgress(context, progress);
            e.printStackTrace();
        }
    }


}
