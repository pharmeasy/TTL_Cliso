package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Covidentered_frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVfiltermodel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidFilter_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private Covidentered_frag covidentered_frag;
    int flag;
    ConnectionDetector cd;

    public CovidFilter_Controller(Activity activity, Covidentered_frag covidentered_frag) {
        this.mActivity = activity;
        this.covidentered_frag = covidentered_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getcovidfilter_controller() {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            Call<COVfiltermodel> coVfiltermodelCall = postAPIInteface.getfilter();

            coVfiltermodelCall.enqueue(new Callback<COVfiltermodel>() {
                @Override
                public void onResponse(Call<COVfiltermodel> call, Response<COVfiltermodel> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            covidentered_frag.getcovidfilterResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<COVfiltermodel> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
