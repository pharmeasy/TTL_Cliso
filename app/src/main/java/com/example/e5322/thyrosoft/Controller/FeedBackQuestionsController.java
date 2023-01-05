package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.FeedbackQuestionsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.FeedbackQuestionRequestModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackQuestionsController {
    ManagingTabsActivity managingTabsActivity;
    private ProgressDialog progress;
    Activity activity;
    FeedbackQuestionsResponseModel feedbackQuestionsResponseModel;

    public FeedBackQuestionsController(ManagingTabsActivity managingTabsActivity) {
        this.managingTabsActivity = managingTabsActivity;
        this.activity = managingTabsActivity;
        progress = GlobalClass.progress(managingTabsActivity, false);
    }

    public void CallAPI(FeedbackQuestionRequestModel feedbackQuestionRequestModel) {
        try {
            progress.show();

            APIInteface apiInterface = null;
            try {
                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<FeedbackQuestionsResponseModel> call = apiInterface.GetFeedbackQuestions(feedbackQuestionRequestModel);
            call.enqueue(new Callback<FeedbackQuestionsResponseModel>() {
                @Override
                public void onResponse(Call<FeedbackQuestionsResponseModel> call, Response<FeedbackQuestionsResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getResid().equalsIgnoreCase(Constants.RES0000)) {
                            feedbackQuestionsResponseModel = response.body();
                            if (feedbackQuestionsResponseModel.getToShow() == 0) {
                                managingTabsActivity.getpostresponse(feedbackQuestionsResponseModel, true);
                            } else {
                                managingTabsActivity.getpostresponse(feedbackQuestionsResponseModel, false);
                            }
                        } else {
                            managingTabsActivity.getpostresponse(feedbackQuestionsResponseModel, false);
                            GlobalClass.showCustomToast(activity, response.body().getResponse() + "", 1);
                        }
                    } else {
                        managingTabsActivity.getpostresponse(feedbackQuestionsResponseModel, false);
                        GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 1);
                    }
                }

                @Override
                public void onFailure(Call<FeedbackQuestionsResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    managingTabsActivity.getpostresponse(feedbackQuestionsResponseModel, false);
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
