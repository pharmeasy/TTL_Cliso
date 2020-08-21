package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.ScanSummaryActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PETCTScansummary_Controller {

    private static final String TAG = PETCTScansummary_Controller.class.getSimpleName();
    ProgressDialog progressDialog;
    private Activity mActivity;
    private GlobalClass globalClass;
    private ConfirmbookDetail confirmbookDetail;
    private ScanSummaryActivity scanSummaryActivity;
    int flag;

    public PETCTScansummary_Controller(Activity activity, ScanSummaryActivity scanSummaryActivity, String header) {
        this.mActivity = activity;
        this.scanSummaryActivity = scanSummaryActivity;
        globalClass = new GlobalClass(mActivity);
        flag = 0;
    }

    public void getpetctSummarycontroller(String header, String str_frmdt, String str_todt, String user) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.SCANSOAPI).create(APIInteface.class);
        Call<List<ScansummaryModel>> responseCall = apiInterface.getsummarydetail(header, str_frmdt, str_todt, user);
        responseCall.enqueue(new Callback<List<ScansummaryModel>>() {
            @Override
            public void onResponse(Call<List<ScansummaryModel>> call, Response<List<ScansummaryModel>> response) {
                GlobalClass.hideProgress(mActivity, progressDialog);
                try {
                    if (flag == 0) {
                        scanSummaryActivity.getScansummaryResponse(response.body());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ScansummaryModel>> call, Throwable t) {
                GlobalClass.hideProgress(mActivity, progressDialog);
            }
        });

    }

}
