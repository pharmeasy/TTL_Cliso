package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.AnitbodyEnteredFrag;
import com.example.e5322.thyrosoft.Fragment.RATEnteredFrag;
import com.example.e5322.thyrosoft.GlobalClass;
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
    private Context context;
    RATEnteredRequestModel ratEnteredRequestModel;
    AnitbodyEnteredFrag anitbodyEnteredFrag;
    ConnectionDetector cd;

    public RATEnteredController(RATEnteredFrag ratEnteredFrag, RATEnteredRequestModel ratEnteredRequestModel) {
        this.ratEnteredFrag = ratEnteredFrag;
        this.ratEnteredRequestModel = ratEnteredRequestModel;
        this.context = ratEnteredFrag.getContext();
        flag = 1;
        cd=new ConnectionDetector(context);
    }

    public RATEnteredController(AnitbodyEnteredFrag anitbodyEnteredFrag, RATEnteredRequestModel ratEnteredRequestModel) {
        this.anitbodyEnteredFrag = anitbodyEnteredFrag;
        this.ratEnteredRequestModel = ratEnteredRequestModel;
        this.context = anitbodyEnteredFrag.getContext();
        flag = 2;
        cd=new ConnectionDetector(context);
    }


    public void CallAPI() {
        if (cd.isConnectingToInternet()){
            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog((Activity) context);
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient((Activity) context, Api.IMAGE_UPLOAD).create(PostAPIInteface.class);
            Call<RATEnteredResponseModel> responseCall = apiInterface.GetEnteredResponse(ratEnteredRequestModel);

            responseCall.enqueue(new Callback<RATEnteredResponseModel>() {
                @Override
                public void onResponse(Call<RATEnteredResponseModel> call, Response<RATEnteredResponseModel> response) {
                    GlobalClass.hideProgress(context, progressDialog);

                    if (response.isSuccessful() && response.body() != null) {
                        if (flag == 1) {
                            ratEnteredFrag.getresponse(response.body());
                        } else {
                            anitbodyEnteredFrag.getresponse(response.body());
                        }

                    } else {
                        Log.v("TAG","Failed" + response.body());
                    }
                }

                @Override
                public void onFailure(Call<RATEnteredResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(context, progressDialog);
                    GlobalClass.ShowError(ratEnteredFrag.getActivity(), MessageConstants.Server_error, MessageConstants.ubableserver, false);
                    Log.v("Failed", t.getMessage());
                }
            });
        }else {
            GlobalClass.showTastyToast((Activity)context, MessageConstants.CHECK_INTERNET_CONN,2);
        }

    }
}
