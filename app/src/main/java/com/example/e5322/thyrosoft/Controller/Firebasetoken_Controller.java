package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.FirebaseModel;
import com.example.e5322.thyrosoft.Models.Firebasepost;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Firebasetoken_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private Login login;
    private SplashScreen splashScreen;
    int flag;
    ConnectionDetector cd;

    public Firebasetoken_Controller(Activity activity, Login login) {
        this.mActivity = activity;
        this.login = login;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public Firebasetoken_Controller(Activity activity, SplashScreen splashScreen) {
        this.mActivity = activity;
        this.splashScreen = splashScreen;
        globalClass = new GlobalClass(mActivity);
        flag = 1;
    }

    public void Pushfirebasetoken(String USER_CODE11, String newToken) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.PushToken).create(APIInteface.class);
            Firebasepost firebasepost = new Firebasepost();
            firebasepost.setClient_Id(USER_CODE11);
            firebasepost.setAppName(Constants.APPNAME);
            firebasepost.setEnterBy(USER_CODE11);
            firebasepost.setToken(newToken);
            firebasepost.setTopic(Constants.TOPIC);

            Call<FirebaseModel> responseCall = apiInterface.pushtoken(firebasepost);
            responseCall.enqueue(new Callback<FirebaseModel>() {
                @Override
                public void onResponse(Call<FirebaseModel> call, Response<FirebaseModel> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            login.getpushtokenResponse(response.body());
                        } else if (flag == 1) {
                            splashScreen.getpushtokenResponse(response.body());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<FirebaseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }
}
