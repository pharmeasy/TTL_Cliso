package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CovidAccessReq;
import com.example.e5322.thyrosoft.Models.CovidaccessRes;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidAccess_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private Login login;
    private SplashScreen splashScreen;
    int flag;
    ConnectionDetector cd;

    public CovidAccess_Controller(Activity activity, Login login) {
        this.mActivity = activity;
        this.login = login;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public CovidAccess_Controller(Activity activity, SplashScreen splashScreen) {
        this.mActivity = activity;
        this.splashScreen = splashScreen;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public void getcovidaccessController(String User) {
        if (cd.isConnectingToInternet()) {
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);

            CovidAccessReq covidAccessReq = new CovidAccessReq();
            covidAccessReq.setSourceCode(User);

            Call<CovidaccessRes> covidaccessResCall = postAPIInteface.checkcovidaccess(covidAccessReq);

            covidaccessResCall.enqueue(new Callback<CovidaccessRes>() {
                @Override
                public void onResponse(Call<CovidaccessRes> call, Response<CovidaccessRes> response) {

                    try {
                        if (flag == 0) {
                            login.getcovidaccResponse(response);
                        } else if (flag == 1) {
                            splashScreen.getcovidaccResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CovidaccessRes> call, Throwable t) {

                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
