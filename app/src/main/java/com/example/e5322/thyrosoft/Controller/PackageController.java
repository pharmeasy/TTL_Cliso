package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Models.PackageReqModel;
import com.example.e5322.thyrosoft.Models.PackageResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageController {
    ManagingTabsActivity managingTabsActivity;
    Activity activity;

    public PackageController(ManagingTabsActivity managingTabsActivity,Activity activity) {
        this.managingTabsActivity = managingTabsActivity;
        this.activity = activity;
    }

    public void fetchPackages(PackageReqModel packageReqModel) {

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.PACKAGE_API).create(PostAPIInterface.class);
        Call<PackageResponseModel> call = apiInterface.getPackages(packageReqModel);

        call.enqueue(new Callback<PackageResponseModel>() {
            @Override
            public void onResponse(Call<PackageResponseModel> call, Response<PackageResponseModel> response) {
                if (response.isSuccessful() && response.body() !=null)
                {
                    PackageResponseModel packageResponseModel = response.body();
                    managingTabsActivity.getPackageResponse(response.body());
                }

            }

            @Override
            public void onFailure(Call<PackageResponseModel> call, Throwable t) {
                Toast.makeText(activity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
