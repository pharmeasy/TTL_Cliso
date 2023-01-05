package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.LeadGenerationActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.PostLeadDataModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLeadResponseController {

    LeadGenerationActivity leadGenerationActivity;
    private ProgressDialog progress;
    private Context context;
    PostLeadDataModel postLeadDataModel;


    public PostLeadResponseController(LeadGenerationActivity leadGenerationActivity, PostLeadDataModel postLeadDataModel) {

        this.leadGenerationActivity = leadGenerationActivity;
        this.postLeadDataModel = postLeadDataModel;
        this.context = leadGenerationActivity;
        progress = GlobalClass.progress(context, false);

    }


    public void CallAPI() {
        progress.show();

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient((Activity)context, Api.THYROCARE).create(PostAPIInterface.class);
        Call<LeadDataResponseModel> responseCall = apiInterface.PostdataLead(postLeadDataModel);

        responseCall.enqueue(new Callback<LeadDataResponseModel>() {
            @Override
            public void onResponse(Call<LeadDataResponseModel> call, Response<LeadDataResponseModel> response) {
                GlobalClass.hideProgress(context, progress);

                if (response.isSuccessful() && response.body() != null) {
                    leadGenerationActivity.getleaddataresponse(response.body());
                } else {
                    System.out.println("Failed" + response.body());
                }
            }

            @Override
            public void onFailure(Call<LeadDataResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(context, progress);
                GlobalClass.ShowError(leadGenerationActivity, "Server Error", "Unable to connect to server. Please try after sometime", false);
                Log.v("Failed" , t.getMessage());
            }
        });
    }
}
