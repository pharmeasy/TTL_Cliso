package com.example.e5322.thyrosoft.Controller;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetLeadgerBalnce;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetLeadgerDetailsController {

    ConfirmbookDetail confirmbookDetail;
    Activity mActivity;
    ConnectionDetector cd;
    Start_New_Woe start_new_woe;
    int flag;
    ProgressDialog progressDialog;

    public GetLeadgerDetailsController(ConfirmbookDetail confirmbookDetail, Activity mActivity) {
        this.confirmbookDetail = confirmbookDetail;
        this.mActivity = mActivity;
        flag = 1;
    }

    public GetLeadgerDetailsController(Activity mActivity, Start_New_Woe start_new_woe) {
        this.mActivity = mActivity;
        this.start_new_woe = start_new_woe;
        flag = 2;
    }

    public void getLedgerBal() {
        try {
            cd = new ConnectionDetector(mActivity);
            if (cd.isConnectingToInternet()) {
                progressDialog = GlobalClass.ShowprogressDialog(mActivity);
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
                String enterby = sharedPreferences.getString("Username", "");
                APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.Cloud_base).create(APIInteface.class);
                Call<GetLeadgerBalnce> templateResponseCall = apiInterface.getLedgerDetails(enterby);
                templateResponseCall.enqueue(new Callback<GetLeadgerBalnce>() {
                    @Override
                    public void onResponse(Call<GetLeadgerBalnce> call, Response<GetLeadgerBalnce> response) {
                        GlobalClass.hideProgress(mActivity, progressDialog);
                        if (response.isSuccessful() && response.body() != null) {
                            if (flag == 1) {
                                confirmbookDetail.getLeaderDetails(response.body());
                            }
                            if (flag == 2) {
                                start_new_woe.getLeaderDetails(response.body());
                            }
                        } else {
                            GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetLeadgerBalnce> call, Throwable t) {
                        GlobalClass.hideProgress(mActivity, progressDialog);
                    }
                });
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        } catch (Exception e) {
            GlobalClass.hideProgress(mActivity, progressDialog);
            e.printStackTrace();
        }
    }
}