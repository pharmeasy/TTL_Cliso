package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.LeadGenerationActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadRequestModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadController {


    LeadGenerationActivity leadGenerationActivity;
    private ProgressDialog progress;
    private Activity mActivity;


    public LeadController(LeadGenerationActivity leadGenerationActivity, Activity pActivity) {
        this.leadGenerationActivity = leadGenerationActivity;
        this.mActivity = pActivity;
        progress = GlobalClass.progress(pActivity, false);
    }


//    public void CallAPI(LeadRequestModel leadRequestModel) {
//        progress.show(); // Create common function
//
//        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.THYROCARE).create(PostAPIInteface.class);
//        Call<LeadResponseModel> responseCall = apiInterface.getLead(leadRequestModel);
//
//        responseCall.enqueue(new Callback<LeadResponseModel>() {
//            @Override
//            public void onResponse(Call<LeadResponseModel> call, Response<LeadResponseModel> response) {
//                GlobalClass.hideProgress(mActivity, progress);
//
//                if (response.isSuccessful()) {
//                    LeadResponseModel body = response.body();
//                    if (body != null) {
//                        if (body.getRES_ID() != null) {
//                            if (body.getRES_ID().equalsIgnoreCase("RES0000")) {
//                                leadGenerationActivity.getleadresponse(body);
//                            } else {
//                                GlobalClass.ShowError(mActivity, "", "" + body.getRESPONSE(), false);
//                            }
//                        } else {
//                            GlobalClass.ShowError(mActivity, "", "Something Went wrong", false);
//                        }
//                    } else {
//                        GlobalClass.ShowError(mActivity, "", "Something Went wrong", false);
//                    }
//                } else {
//                    System.out.println("Failed" + response.body());
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<LeadResponseModel> call, Throwable t) {
//                GlobalClass.hideProgress(mActivity, progress);
//                GlobalClass.ShowError(mActivity, "Server Error", "Unable to connect to server. Please try after sometime", false);
//                System.out.println("Failed" + t.getMessage());
//                leadGenerationActivity.getleadresponse(null);
//            }
//        });
//    }

}
