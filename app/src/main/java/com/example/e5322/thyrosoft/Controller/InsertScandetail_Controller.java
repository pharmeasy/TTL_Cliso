package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.ReportScanFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.InsertScandetailReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailRes;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertScandetail_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private ReportScanFrag reportScanFrag;
    int flag;
    ConnectionDetector cd;

    public InsertScandetail_Controller(Activity activity, ReportScanFrag reportScanFrag) {
        this.mActivity = activity;
        this.reportScanFrag = reportScanFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void insertScandetail(String usercode, TextView txt_barcode) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            InsertScandetailReq insertScandetailReq = new InsertScandetailReq();
            insertScandetailReq.setSource(usercode);
            insertScandetailReq.setBarcode(txt_barcode.getText().toString());

            APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.insertscandetail).create(APIInteface.class);
            Call<InsertScandetailRes> insertScandetailResCall = apiInteface.insertScandetail(insertScandetailReq);

            insertScandetailResCall.enqueue(new Callback<InsertScandetailRes>() {
                @Override
                public void onResponse(Call<InsertScandetailRes> call, Response<InsertScandetailRes> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            reportScanFrag.getscanResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<InsertScandetailRes> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
