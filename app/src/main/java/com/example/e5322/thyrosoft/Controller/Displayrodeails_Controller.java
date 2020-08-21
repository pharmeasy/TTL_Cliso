package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.ReportScansummaryFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetScanReq;
import com.example.e5322.thyrosoft.Models.GetScanResponse;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Displayrodeails_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private ReportScansummaryFrag reportScansummaryFrag;

    int flag;
    ConnectionDetector cd;

    public Displayrodeails_Controller(Activity activity, ReportScansummaryFrag reportScansummaryFrag) {
        this.mActivity = activity;
        this.reportScansummaryFrag = reportScansummaryFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getdisplayrodetails(String usercode, TextView to_date) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            String formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, to_date.getText().toString());
            GetScanReq getScanReq = new GetScanReq();
            getScanReq.setSdate(formateDate);
            getScanReq.setSource(usercode);


            APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.insertscandetail).create(APIInteface.class);
            Call<GetScanResponse> getScanResponseCall = apiInteface.getMIS(getScanReq);

            getScanResponseCall.enqueue(new Callback<GetScanResponse>() {
                @Override
                public void onResponse(Call<GetScanResponse> call, Response<GetScanResponse> response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    try {
                        if (flag==0){
                            reportScansummaryFrag.getscansummResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetScanResponse> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
