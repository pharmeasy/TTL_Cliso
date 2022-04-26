package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CategoryResModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListController {
    Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity;
    Activity activity;
    ProgressDialog progressDialog;

    public CategoryListController(Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity, Activity activity) {
        this.sgc_pgc_entry_activity = sgc_pgc_entry_activity;
        this.activity = activity;

    }

    public void fetchSGCcategoryList() {

        try {
            progressDialog = GlobalClass.ShowprogressDialog(activity);
            APIInteface getApiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.SGC).create(APIInteface.class);
            Call<CategoryResModel> categoryResModelCall = getApiInterface.getCategoryList();
            categoryResModelCall.enqueue(new Callback<CategoryResModel>() {
                @Override
                public void onResponse(Call<CategoryResModel> call, Response<CategoryResModel> response) {
                    CategoryResModel categoryResModel = response.body();
                    GlobalClass.hideProgress(activity,progressDialog);
                    if (response.isSuccessful() && response.body() != null) {
                        sgc_pgc_entry_activity.getSGCCategoryListResponse(categoryResModel);
                    }
                    else {
                        GlobalClass.showTastyToast(activity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                    }
                }

                @Override
                public void onFailure(Call<CategoryResModel> call, Throwable throwable) {
                    GlobalClass.hideProgress(activity,progressDialog);
                }
            });
        } catch (Exception e) {
            GlobalClass.hideProgress(activity,progressDialog);
            e.printStackTrace();
        }
    }
}
