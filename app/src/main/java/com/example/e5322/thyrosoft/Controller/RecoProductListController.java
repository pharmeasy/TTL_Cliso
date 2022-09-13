package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoProductListController {

    ManagingTabsActivity managingTabsActivity;
    Activity activity;
    SharedPreferences preferences;
    GetProductsRecommendedResModel recommendedResModel;

    public RecoProductListController(ManagingTabsActivity managingTabsActivity, Activity activity) {
        this.managingTabsActivity = managingTabsActivity;
        this.activity = activity;

    }

    public void fetchRecoProductList(int tenant_id) {

        try {
            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
            Call<GetProductsRecommendedResModel> getProductsRecommendedModelCall = apiInterface.getProductRecommended(Global.getAPIKey(activity), tenant_id);

            getProductsRecommendedModelCall.enqueue(new Callback<GetProductsRecommendedResModel>() {
                @Override
                public void onResponse(Call<GetProductsRecommendedResModel> call, Response<GetProductsRecommendedResModel> response) {
                    if (response.isSuccessful() && response != null) {
                        GetProductsRecommendedResModel resModel = response.body();
                        preferences = activity.getSharedPreferences("Product_Recommended", 0);
                        SharedPreferences.Editor editor = activity.getSharedPreferences("Product_Recommended", 0).edit();
                        String str_storeProductRecommendedDate = preferences.getString("Product_Recommended", GlobalClass.getCurrentDate());
                        editor.putString("Product_Recommended", str_storeProductRecommendedDate);
                        editor.apply();

                        recommendedResModel = new GetProductsRecommendedResModel();
                        String str_recommendedAPIResponse = new Gson().toJson(resModel);
                        SharedPreferences appSharedPrefs = activity.getSharedPreferences("MyRecommendedProduct", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                        prefsEditor1.putString("MyRecommendedProduct", str_recommendedAPIResponse);
                        prefsEditor1.apply();

                        managingTabsActivity.getRecoProductResponse(resModel);
                    }
                }

                @Override
                public void onFailure(Call<GetProductsRecommendedResModel> call, Throwable t) {
                    GlobalClass.showTastyToast(activity, MessageConstants.SOMETHING_WENT_WRONG, 0);
                }
            });
        } catch (Exception e) {
            Toast.makeText(managingTabsActivity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
