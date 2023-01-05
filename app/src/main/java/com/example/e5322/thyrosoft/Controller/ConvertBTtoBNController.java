package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ConvertBTBNRequestModel;
import com.example.e5322.thyrosoft.Models.ConvertBTBNResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Woe_Edt_Activity;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertBTtoBNController {

    Woe_Edt_Activity Woe_Edt_Activity;
    private ProgressDialog progress;
    private Context context;


    public ConvertBTtoBNController(Woe_Edt_Activity Woe_Edt_Activity) {
        this.Woe_Edt_Activity = Woe_Edt_Activity;
        this.context = Woe_Edt_Activity;
        progress = GlobalClass.progress(context, false);

    }

    public void CallAPI(ConvertBTBNRequestModel convertBTBNRequestModel) {
        try {
            progress.show();
            APIInteface apiInterface = null;
            try {

                apiInterface = RetroFit_APIClient.getInstance().getClient(Woe_Edt_Activity, Api.Cloud_base).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<ConvertBTBNResponseModel> responseCall = apiInterface.ConvertBttoBn(convertBTBNRequestModel);
            responseCall.enqueue(new Callback<ConvertBTBNResponseModel>() {
                @Override
                public void onResponse(Call<ConvertBTBNResponseModel> call, Response<ConvertBTBNResponseModel> response) {
                    GlobalClass.hideProgress(context, progress);

                    if (response.isSuccessful() && response.body() != null) {
                        ConvertBTBNResponseModel convertBTBNResponseModel = response.body();
                        Woe_Edt_Activity .getCovertResponse(convertBTBNResponseModel);
                    } else {
                        GlobalClass.showTastyToast(Woe_Edt_Activity, ToastFile.something_went_wrong, 2);

                    }
                }

                @Override
                public void onFailure(Call<ConvertBTBNResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(context, progress);
                    GlobalClass.showTastyToast(Woe_Edt_Activity, ToastFile.something_went_wrong, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
