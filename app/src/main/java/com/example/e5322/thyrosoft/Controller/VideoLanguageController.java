package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.VideoActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.VideoLangaugesResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.example.e5322.thyrosoft.startscreen.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer2.VideoView;

public class VideoLanguageController {

    private Activity mActivity;
    private GlobalClass globalClass;
    private Login login;
    private VideoActivity videoActivity;
    int flag;
    ConnectionDetector cd;

    public VideoLanguageController(Activity activity, VideoActivity videoActivity) {
        this.mActivity = activity;
        this.videoActivity = videoActivity;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void GetVideoLang_Controller(final TextView tv_noDatafound, final VideoView videoView) {

        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(APIInteface.class);
            Call<VideoLangaugesResponseModel> responseCall = apiInterface.getVideoLanguages();

            responseCall.enqueue(new Callback<VideoLangaugesResponseModel>() {
                @Override
                public void onResponse(Call<VideoLangaugesResponseModel> call, Response<VideoLangaugesResponseModel> response) {
                    try {
                        if (flag == 0) {
                            videoActivity.getVideoLangResponse(response.body());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<VideoLangaugesResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    tv_noDatafound.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    GlobalClass.showTastyToast(mActivity, MessageConstants.unablefetchdata, 2);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }
    }

}
