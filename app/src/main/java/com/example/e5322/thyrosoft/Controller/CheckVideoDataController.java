package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetVideoResponse_Model;
import com.example.e5322.thyrosoft.Models.GetVideopost_model;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.example.e5322.thyrosoft.startscreen.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckVideoDataController {

    private Activity mActivity;
    private GlobalClass globalClass;
    private Login login;
    private ManagingTabsActivity managingTabsActivity;
    int flag;
    ConnectionDetector cd;

    public CheckVideoDataController(Activity activity, ManagingTabsActivity managingTabsActivity) {
        this.mActivity = activity;
        this.managingTabsActivity = managingTabsActivity;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void checkvideodata_controller(String USER_CODE) {
        if (cd.isConnectingToInternet()) {
            GetVideopost_model getVideopost_model = new GetVideopost_model();
            getVideopost_model.setApp(Constants.APP_ID);
            getVideopost_model.setSourcedata(USER_CODE);

            APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.video_data).create(APIInteface.class);
            Call<GetVideoResponse_Model> getVideoResponse_modelCall = apiInteface.getVideoData(getVideopost_model);

            getVideoResponse_modelCall.enqueue(new Callback<GetVideoResponse_Model>() {
                @Override
                public void onResponse(Call<GetVideoResponse_Model> call, Response<GetVideoResponse_Model> response) {
                    try {
                        if (flag == 0) {
                            managingTabsActivity.getCheckdataResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetVideoResponse_Model> call, Throwable t) {

                }
            });

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }

}
