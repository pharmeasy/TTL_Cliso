package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.NetworkFeedbackActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.FeedbackPostQuestionsRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.FeedbackPostQuestionsResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackPostQuestionsController {
    NetworkFeedbackActivity networkFeedbackActivity;
    private ProgressDialog progress;
    Activity activity;

    public FeedBackPostQuestionsController(NetworkFeedbackActivity networkFeedbackActivity) {
        this.networkFeedbackActivity = networkFeedbackActivity;
        this.activity = networkFeedbackActivity;
        progress = GlobalClass.progress(networkFeedbackActivity, false);
    }

    public void CallAPI(FeedbackPostQuestionsRequestModel feedbackPostQuestionsRequestModel) {
        try {
            progress.show();

            APIInteface apiInterface = null;
            try {
                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<FeedbackPostQuestionsResponseModel> call = apiInterface.PostFeedbackQuestions(feedbackPostQuestionsRequestModel);
            call.enqueue(new Callback<FeedbackPostQuestionsResponseModel>() {
                @Override
                public void onResponse(Call<FeedbackPostQuestionsResponseModel> call, Response<FeedbackPostQuestionsResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResid().equalsIgnoreCase(Constants.RES0000)) {
                            networkFeedbackActivity.getAPIResponse();
                        } else {
                            GlobalClass.showCustomToast(activity, response.body().getResponse() + "", 1);
                        }
                    } else {
                        GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 1);
                    }
                }

                @Override
                public void onFailure(Call<FeedbackPostQuestionsResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 1);
                }
            });
        } catch (Exception e) {
            GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 1);
            GlobalClass.hideProgress(activity, progress);
            e.printStackTrace();
        }
    }
}
