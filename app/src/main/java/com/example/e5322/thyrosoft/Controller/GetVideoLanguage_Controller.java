package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.VideoActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetVideoLanguageWiseRequestModel;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer2.VideoView;

public class GetVideoLanguage_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private VideoActivity  videoActivity;
    int flag;
    ConnectionDetector cd;

    public GetVideoLanguage_Controller(Activity activity, VideoActivity videoActivity) {
        this.mActivity = activity;
        this.videoActivity = videoActivity;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void getvideolangvideo(String LanguageID, final TextView tv_noDatafound, final VideoView videoView) {

        if (cd.isConnectingToInternet()) {

            final ProgressDialog progressDialog=GlobalClass.ShowprogressDialog(mActivity);
            GetVideoLanguageWiseRequestModel model = new GetVideoLanguageWiseRequestModel();
            model.setApp(Constants.APP_ID);
            model.setLanguage(LanguageID);
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            Call<VideosResponseModel> responseCall = apiInterface.getVideobasedOnLanguage(model);

            responseCall.enqueue(new Callback<VideosResponseModel>() {
                @Override
                public void onResponse(Call<VideosResponseModel> call, Response<VideosResponseModel> response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                    try {
                        if (flag==0){
                            videoActivity.getvideoResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<VideosResponseModel> call, Throwable t) {
                    tv_noDatafound.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    GlobalClass.hideProgress(mActivity,progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }


    }

}
