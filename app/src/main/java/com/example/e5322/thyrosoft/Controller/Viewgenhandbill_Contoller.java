package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Activity.ViewGenhandbill_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.HandbillsResponse;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Viewgenhandbill_Contoller {
    int flag;
    ViewGenhandbill_Activity viewGenhandbill_activity;
    Activity mActivity;
    String TAG = getClass().getSimpleName();
    ConnectionDetector cd;


    public Viewgenhandbill_Contoller(ViewGenhandbill_Activity viewGenhandbill_activity, Activity mActivity) {
        this.viewGenhandbill_activity = viewGenhandbill_activity;
        this.mActivity = mActivity;
        flag = 1;
        cd = new ConnectionDetector(mActivity);
    }

    public void getgenhandbill() {
        try {
            if (cd.isConnectingToInternet()) {
                final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
                String enterby = sharedPreferences.getString("mobile_user", "");

                APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.SGC).create(APIInteface.class);
                Call<HandbillsResponse> templateResponseCall = apiInterface.gethandbills(enterby);

                templateResponseCall.enqueue(new Callback<HandbillsResponse>() {
                    @Override
                    public void onResponse(Call<HandbillsResponse> call, Response<HandbillsResponse> response) {
                        try {
                            GlobalClass.hideProgress(mActivity,progressDialog);
                            if (response.body()!=null){
                                viewGenhandbill_activity.getgeneratehandbillResp(response.body());
                            }else {
                                GlobalClass.showTastyToast(mActivity,MessageConstants.SOMETHING_WENT_WRONG,2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<HandbillsResponse> call, Throwable t) {
                        GlobalClass.hideProgress(mActivity,progressDialog);
                    }
                });
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
