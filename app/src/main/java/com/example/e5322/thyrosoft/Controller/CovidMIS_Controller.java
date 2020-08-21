package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Covidentered_frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CovidMIS_req;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidMIS_Controller {

    private Activity mActivity;
    private GlobalClass globalClass;
    private Covidentered_frag covidentered_frag;
    int flag;
    ConnectionDetector cd;

    public CovidMIS_Controller(Activity activity, Covidentered_frag covidentered_frag) {
        this.mActivity = activity;
        this.covidentered_frag = covidentered_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getcovidMIScontroller(String from_formateDate) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            SharedPreferences preferences = mActivity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
            String usercode = preferences.getString("USER_CODE", null);
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            CovidMIS_req covidMIS_req = new CovidMIS_req();
            covidMIS_req.setSdate(from_formateDate);
            covidMIS_req.setSourceCode(usercode);
            final Call<Covidmis_response> covidmis_responseCall = postAPIInteface.getcovidmis(covidMIS_req);
            covidmis_responseCall.enqueue(new Callback<Covidmis_response>() {
                @Override
                public void onResponse(Call<Covidmis_response> call, Response<Covidmis_response> response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    try {
                        if (flag == 0) {
                            covidentered_frag.getcovidMISResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Covidmis_response> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
