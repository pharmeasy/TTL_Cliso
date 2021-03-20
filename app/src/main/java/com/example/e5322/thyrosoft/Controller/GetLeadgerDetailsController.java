package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetLeadgerBalnce;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GetLeadgerDetailsController {


    ConfirmbookDetail confirmbookDetail;
    Activity mActivity;
    ConnectionDetector cd;


    public GetLeadgerDetailsController(ConfirmbookDetail confirmbookDetail, Activity mActivity) {
        this.confirmbookDetail = confirmbookDetail;
        this.mActivity = mActivity;
        cd = new ConnectionDetector(mActivity);
    }

    public void getLeadgerBal() {
        try {
            if (cd.isConnectingToInternet()) {
                final ProgressDialog progressDialog= GlobalClass.ShowprogressDialog(mActivity);
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
                String enterby = sharedPreferences.getString("Username", "");

                APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.Cloud_base).create(APIInteface.class);
                Call<GetLeadgerBalnce> templateResponseCall = apiInterface.getLedgerDetails(enterby);

                templateResponseCall.enqueue(new Callback<GetLeadgerBalnce>() {
                    @Override
                    public void onResponse(Call<GetLeadgerBalnce> call, Response<GetLeadgerBalnce> response) {
                        try {
                            GlobalClass.hideProgress(mActivity,progressDialog);
                            if (response.body()!=null){
                                confirmbookDetail.getLeaderDetails(response.body());
                            }else {
                                GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG,2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetLeadgerBalnce> call, Throwable t) {
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
