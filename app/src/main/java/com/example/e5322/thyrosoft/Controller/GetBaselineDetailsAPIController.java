package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.RequestModels.GetBaselineDetailsRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBaselineDetailsResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBaselineDetailsAPIController {
    Activity mActivity;
    AppPreferenceManager appPreferenceManager;
    Login loginActivity;
    SplashScreen splashScreenActivity;
    int flag;

    public GetBaselineDetailsAPIController(Login login) {
        this.mActivity = login;
        this.loginActivity = login;
        flag = 0;
    }

    public GetBaselineDetailsAPIController(SplashScreen splashScreen) {
        this.mActivity = splashScreen;
        this.splashScreenActivity = splashScreen;
        flag = 1;
    }

    public void CallAPI(final GetBaselineDetailsRequestModel requestModel) {
        APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.Cloud_base).create(APIInteface.class);
        Call<GetBaselineDetailsResponseModel> call = apiInteface.GetBaselineDetails(requestModel);
        call.enqueue(new Callback<GetBaselineDetailsResponseModel>() {
            @Override
            public void onResponse(Call<GetBaselineDetailsResponseModel> call, Response<GetBaselineDetailsResponseModel> response) {
                try {
                    if (response.body() != null) {
                        gotoActivity(response.body());
                    } else {
                        gotoActivity(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetBaselineDetailsResponseModel> call, Throwable t) {
                GlobalClass.showCustomToast(mActivity, "Unable to fetch data from server.", 2);
                gotoActivity(null);
            }
        });
    }

    private void gotoActivity(GetBaselineDetailsResponseModel responseModel) {
        appPreferenceManager = new AppPreferenceManager(mActivity);
        appPreferenceManager.setBaselineDetailsResponse(responseModel);
        if (flag == 0) {
            loginActivity.getBaselineDetailsResponse();
        }
    }

}
