package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.Certificate_activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CertificateRequestModel;
import com.example.e5322.thyrosoft.Models.CertificateResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCertificateController {

    private Activity mactivity;
    private Certificate_activity certificate_activity;

    public GetCertificateController(Certificate_activity certificate_activity) {
        this.mactivity = certificate_activity;
        this.certificate_activity = certificate_activity;
    }

    public void CallAPI(CertificateRequestModel certificateRequestModel) {
        ProgressDialog progressDialog = null;
        try {
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.LIVEAPI).create(PostAPIInterface.class);
            Call<CertificateResponseModel> responseCall = apiInterface.getCertificates(certificateRequestModel);


            final ProgressDialog finalProgressDialog = progressDialog;
            responseCall.enqueue(new Callback<CertificateResponseModel>() {
                @Override
                public void onResponse(Call<CertificateResponseModel> call, Response<CertificateResponseModel> response) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    CertificateResponseModel responseModel = response.body();
                    if (response.isSuccessful() && responseModel != null) {
                        certificate_activity.getResponse(responseModel);
                    } else {
                        GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                    }
                }

                @Override
                public void onFailure(Call<CertificateResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.hideProgress(mactivity, progressDialog);
        }
    }
}
