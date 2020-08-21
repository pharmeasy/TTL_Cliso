package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CampIdRequestModel;
import com.example.e5322.thyrosoft.Models.CampIdResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCampidController {

    private static final String TAG = GetOTPController.class.getSimpleName();
    int flag = 0;
    private Activity mactivity;
    private AntiBodyEnterFrag antiBodyEnterFrag;
    GlobalClass globalClass;

    public GetCampidController(Activity activity, AntiBodyEnterFrag antiBodyEnterFrag) {
        this.mactivity = activity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        flag = 1;
        globalClass = new GlobalClass(mactivity);

    }

    public void getcampID(String usercode) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mactivity);
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.LIVEAPI).create(APIInteface.class);

        CampIdRequestModel campIdRequestModel = new CampIdRequestModel();
        campIdRequestModel.setSourceCode(usercode);
        campIdRequestModel.setTestCode("COVID RAPID ANTIBODIES");

        Call<CampIdResponseModel> responseCall = apiInterface.GetCampID(campIdRequestModel);

        responseCall.enqueue(new Callback<CampIdResponseModel>() {
            @Override
            public void onResponse(Call<CampIdResponseModel> call, Response<CampIdResponseModel> response) {
                try {
                    GlobalClass.hideProgress(mactivity, progressDialog);
                    if (response.body() != null && !GlobalClass.isNull(response.body().getResponseID()) && response.body().getResponseID().equalsIgnoreCase("RES0000")) {
                        if (flag == 1) {
                            antiBodyEnterFrag.GetCampId(response.body());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CampIdResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(mactivity, progressDialog);
            }
        });

    }
}
