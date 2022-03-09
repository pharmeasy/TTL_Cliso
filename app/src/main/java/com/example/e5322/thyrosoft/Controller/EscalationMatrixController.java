package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.EscalationMatrixActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.EscalationMatrixRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.EscalationMatrixResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EscalationMatrixController {

    EscalationMatrixActivity escalationMatrixActivity;
    private final Activity activity;
    ProgressDialog progressDialog;

    public EscalationMatrixController(EscalationMatrixActivity escalationMatrixActivity) {
        this.escalationMatrixActivity = escalationMatrixActivity;
        this.activity = escalationMatrixActivity;
    }

    public void CallEscalationMatrixAPI(EscalationMatrixRequestModel requestModel) {
        try {
            progressDialog = GlobalClass.ShowprogressDialog(activity);
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
            Call<EscalationMatrixResponseModel> call = apiInterface.getMatrix(requestModel);
            call.enqueue(new Callback<EscalationMatrixResponseModel>() {
                @Override
                public void onResponse(Call<EscalationMatrixResponseModel> call, Response<EscalationMatrixResponseModel> response) {
                    GlobalClass.hideProgress(activity, progressDialog);
                    if (response.isSuccessful() && response.body() != null) {
                        EscalationMatrixResponseModel responseModel = response.body();
                        passResponse(responseModel);
                    } else {
                        onErrorResponse();
                    }
                }

                @Override
                public void onFailure(Call<EscalationMatrixResponseModel> call, Throwable t) {
                    onErrorResponse();
                }
            });
        } catch (Exception e) {
            GlobalClass.hideProgress(activity, progressDialog);
            onErrorResponse();
            e.printStackTrace();
        }
    }

    private void onErrorResponse() {
        passResponse(null);
        GlobalClass.showTastyToast(activity, MessageConstants.SOMETHING_WENT_WRONG, 2);
    }

    private void passResponse(EscalationMatrixResponseModel responseModel) {
        escalationMatrixActivity.getEscalationMatrixResponse(responseModel);
    }
}