package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidRates_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private Covidenter_Frag covidenter_frag;
    int flag;
    ConnectionDetector cd;

    public CovidRates_Controller(Activity activity, Covidenter_Frag covidenter_frag) {
        this.mActivity = activity;
        this.covidenter_frag = covidenter_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getCovidRatescontroller() {

        if (cd.isConnectingToInternet()) {
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);

            Call<Covidratemodel> covidratemodelCall = postAPIInteface.displayrates();

            covidratemodelCall.enqueue(new Callback<Covidratemodel>() {
                @Override
                public void onResponse(Call<Covidratemodel> call, Response<Covidratemodel> response) {
                    try {
                        if (flag == 0) {
                            covidenter_frag.getcovidratesResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Covidratemodel> call, Throwable t) {

                }
            });

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }

}
