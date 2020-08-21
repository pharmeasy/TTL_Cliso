package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetClientDetail_Controller {
    SpecialOffer_Activity specialOffer_activity;
    GlobalClass globalClass;
    int flag = 0;
    Activity mActivity;


    public GetClientDetail_Controller(Activity mActivity, SpecialOffer_Activity mContext) {
        this.mActivity = mActivity;
        this.specialOffer_activity = mContext;
        flag = 1;
    }



    public void CallgetClient(String user, String api_key) {

        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(specialOffer_activity, Api.SOURCEils).create(APIInteface.class);
        Call<SourceILSMainModel> responseCall = apiInterface.getClient(api_key, user, "/B2BAPP/getclients");

        responseCall.enqueue(new Callback<SourceILSMainModel>() {
            @Override
            public void onResponse(Call<SourceILSMainModel> call, Response<SourceILSMainModel> response) {

                if (response.isSuccessful() && response.body() != null && response != null) {
                    SourceILSMainModel sourceILSMainModel = response.body();
                    if (sourceILSMainModel.getRESPONSE().equalsIgnoreCase("Success")) {
                        if (flag == 1) {
                            specialOffer_activity.onclientData(sourceILSMainModel);
                        }
                    }
                } else {
                    GlobalClass.showTastyToast(specialOffer_activity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                }
            }

            @Override
            public void onFailure(Call<SourceILSMainModel> call, Throwable t) {

                Log.v("TAG", "HealthTips OnFailure");
            }
        });
    }
}
