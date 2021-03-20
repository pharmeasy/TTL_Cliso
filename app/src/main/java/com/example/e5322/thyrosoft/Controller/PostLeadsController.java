package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.LeadGenerationActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadReqModel;
import com.example.e5322.thyrosoft.Models.LeadRespModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLeadsController {

    LeadGenerationActivity leadGenerationActivity;
    private ProgressDialog progress;
    Activity activity;


    public PostLeadsController(LeadGenerationActivity leadGenerationActivity) {
        this.activity = leadGenerationActivity;
        this.leadGenerationActivity = leadGenerationActivity;
        progress = GlobalClass.progress(activity, false);
    }


    public void CallAPI(LeadReqModel leadReqModel) {
        try {
            progress.show();

            APIInteface apiInterface = null;
            try {
                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Velso).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Call<LeadRespModel> responseCall = apiInterface.PostLead(leadReqModel);

            responseCall.enqueue(new Callback<LeadRespModel>() {
                @Override
                public void onResponse(Call<LeadRespModel> call, Response<LeadRespModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        LeadRespModel leadResponseModel = response.body();
                        leadGenerationActivity.getpostresponse(leadResponseModel);
                    } else {
                        GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 1);

                    }
                }

                @Override
                public void onFailure(Call<LeadRespModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
