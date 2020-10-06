package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.LeadGenerationActivity;
import com.example.e5322.thyrosoft.Activity.frags.AnitbodyEnteredFrag;
import com.example.e5322.thyrosoft.Fragment.RATEnteredFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.PostLeadDataModel;
import com.example.e5322.thyrosoft.Models.RATEnteredRequestModel;
import com.example.e5322.thyrosoft.Models.RATEnteredResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RATEnteredController {

    int flag;
    RATEnteredFrag ratEnteredFrag;
    private ProgressDialog progress;
    private Context context;
    RATEnteredRequestModel ratEnteredRequestModel;
    AnitbodyEnteredFrag anitbodyEnteredFrag;

    public RATEnteredController(RATEnteredFrag ratEnteredFrag, RATEnteredRequestModel ratEnteredRequestModel) {
        this.ratEnteredFrag = ratEnteredFrag;
        this.ratEnteredRequestModel = ratEnteredRequestModel;
        this.context = ratEnteredFrag.getContext();
        flag = 1;
        progress = GlobalClass.progress(context, false);
    }

    public RATEnteredController(AnitbodyEnteredFrag anitbodyEnteredFrag, RATEnteredRequestModel ratEnteredRequestModel) {
        this.anitbodyEnteredFrag = anitbodyEnteredFrag;
        this.ratEnteredRequestModel = ratEnteredRequestModel;
        this.context = anitbodyEnteredFrag.getContext();
        flag = 2;
        progress = GlobalClass.progress(context, false);
    }


    public void CallAPI() {
        progress.show();
        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient((Activity) context, Api.IMAGE_UPLOAD).create(PostAPIInteface.class);
        Call<RATEnteredResponseModel> responseCall = apiInterface.GetEnteredResponse(ratEnteredRequestModel);

        responseCall.enqueue(new Callback<RATEnteredResponseModel>() {
            @Override
            public void onResponse(Call<RATEnteredResponseModel> call, Response<RATEnteredResponseModel> response) {
                GlobalClass.hideProgress(context, progress);

                if (response.isSuccessful() && response.body() != null) {
                    if (flag == 1) {
                        ratEnteredFrag.getresponse(response.body());
                    } else {
                        anitbodyEnteredFrag.getresponse(response.body());
                    }

                } else {
                    System.out.println("Failed" + response.body());
                }
            }

            @Override
            public void onFailure(Call<RATEnteredResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(context, progress);
                GlobalClass.ShowError(ratEnteredFrag.getActivity(), "Server Error", "Unable to connect to server. Please try after sometime", false);
                Log.v("Failed", t.getMessage());
            }
        });
    }
}
