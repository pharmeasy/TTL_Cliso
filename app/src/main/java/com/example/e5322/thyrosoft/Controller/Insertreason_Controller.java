package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.EditText;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.ReportScanFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.InsertReasonsReq;
import com.example.e5322.thyrosoft.Models.InsertreasonResponse;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Insertreason_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private ReportScanFrag reportScanFrag;
    int flag;
    ConnectionDetector cd;

    public Insertreason_Controller(Activity activity, ReportScanFrag reportScanFrag) {
        this.mActivity = activity;
        this.reportScanFrag = reportScanFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getinsertreasoncontroller(String usercode, EditText edt_reason, EditText edt_wastecnt) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            InsertReasonsReq insertScandetailReq = new InsertReasonsReq();
            insertScandetailReq.setSource(usercode);
            insertScandetailReq.setRemarks(edt_reason.getText().toString());
            insertScandetailReq.setRowaste(edt_wastecnt.getText().toString());

            APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.insertscandetail).create(APIInteface.class);
            Call<InsertreasonResponse> insertScandetailResCall = apiInteface.insertreason(insertScandetailReq);

            insertScandetailResCall.enqueue(new Callback<InsertreasonResponse>() {
                @Override
                public void onResponse(Call<InsertreasonResponse> call, Response<InsertreasonResponse> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    if (flag == 0) {
                        reportScanFrag.getinsertreasonResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<InsertreasonResponse> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);

                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
