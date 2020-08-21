package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.AlertDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Videopoppost;
import com.example.e5322.thyrosoft.Models.Videopoppost_response;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Videopoppost_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private ManagingTabsActivity managingTabsActivity;

    int flag;
    ConnectionDetector cd;

    public Videopoppost_Controller(Activity activity, ManagingTabsActivity managingTabsActivity) {
        this.mActivity = activity;
        this.managingTabsActivity = managingTabsActivity;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }


    public void VideopostController(String id, String USER_CODE, final AlertDialog dialog) {

        if (cd.isConnectingToInternet()) {
            Videopoppost videopoppost = new Videopoppost();
            videopoppost.setClientid(USER_CODE);
            videopoppost.setVideoid(id);

            APIInteface apiInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.video_data).create(APIInteface.class);
            Call<Videopoppost_response> videopoppost_responseCall = apiInteface.Videopost(videopoppost);

            videopoppost_responseCall.enqueue(new Callback<Videopoppost_response>() {
                @Override
                public void onResponse(Call<Videopoppost_response> call, Response<Videopoppost_response> response) {

                    try {
                        if (flag==0){
                            managingTabsActivity.getVideopostResponse(response,dialog);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Videopoppost_response> call, Throwable t) {

                }
            });

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
