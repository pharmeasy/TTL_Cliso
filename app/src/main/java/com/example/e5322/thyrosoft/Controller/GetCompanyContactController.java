package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ConatctsResponseModel;
import com.example.e5322.thyrosoft.Models.ContactsReqModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCompanyContactController {

   com.example.e5322.thyrosoft.Activity.CompanyContact_activity CompanyContact_activity;
    private ProgressDialog progress;
    Activity activity;


    public GetCompanyContactController(com.example.e5322.thyrosoft.Activity.CompanyContact_activity CompanyContact_activity) {
        this.CompanyContact_activity = CompanyContact_activity;
        this.activity = CompanyContact_activity;
        progress = GlobalClass.progress(activity, false);

    }


    public void CallAPI(ContactsReqModel getLocationReqModel) {
        try {
            progress.show();
            APIInteface apiInterface = null;
            try {
                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(APIInteface.class);
//                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, "http://www.thyrocare.local/API/B2B/").create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<ConatctsResponseModel> responseCall = apiInterface.GetConatcts(getLocationReqModel);
            responseCall.enqueue(new Callback<ConatctsResponseModel>() {
                @Override
                public void onResponse(Call<ConatctsResponseModel> call, Response<ConatctsResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        ConatctsResponseModel getLocationRespModel = response.body();
                        CompanyContact_activity.getcontacts(getLocationRespModel);
                    } else {
                        GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
                    }
                }

                @Override
                public void onFailure(Call<ConatctsResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
        }
    }
}

