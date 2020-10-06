package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Product;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Activity.ShowTemplateActivity;
import com.example.e5322.thyrosoft.Activity.ViewGenhandbill_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.TemplateResponse;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Handbilltemplate_Controller {
    int flag;
    ShowTemplateActivity showTemplateActivity;
    Activity mActivity;
    String TAG = getClass().getSimpleName();
    ConnectionDetector cd;
    public Handbilltemplate_Controller(ShowTemplateActivity showTemplateActivity, Activity mActivity) {
        this.showTemplateActivity = showTemplateActivity;
        this.mActivity = mActivity;
        flag = 1;
        cd = new ConnectionDetector(mActivity);
    }

    public void gethandteplates(){
        try {
            if (cd.isConnectingToInternet()){
                final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
                APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.SGC).create(APIInteface.class);
                Call<TemplateResponse> templateResponseCall = apiInterface.getTemplate();

                templateResponseCall.enqueue(new Callback<TemplateResponse>() {
                    @Override
                    public void onResponse(Call<TemplateResponse> call, Response<TemplateResponse> response) {
                        try {
                            GlobalClass.hideProgress(mActivity,progressDialog);
                            showTemplateActivity.gethndTemplateResp(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<TemplateResponse> call, Throwable t) {
                        GlobalClass.hideProgress(mActivity,progressDialog);
                    }
                });

            }else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN,2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
