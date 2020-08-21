package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CampIdRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.WOERequestModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AntibodySubmitWoeDetails_Controller {

    private static final String TAG = AntibodySubmitWoeDetails_Controller.class.getSimpleName();
    ProgressDialog progressDialog;
    private Activity mActivity;
    private GlobalClass globalClass;
    private AntiBodyEnterFrag antiBodyEnterFrag;
    int flag;
    ConnectionDetector cd;

    public AntibodySubmitWoeDetails_Controller(Activity activity, AntiBodyEnterFrag antiBodyEnterFrag) {
        this.mActivity = activity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void Postwoedetail(WOERequestModel campWoeRequestModel, String usercode) {
        if (cd.isConnectingToInternet()){
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(APIInteface.class);
            CampIdRequestModel campIdRequestModel = new CampIdRequestModel();
            campIdRequestModel.setSourceCode(usercode);
            campIdRequestModel.setTestCode("");
            Call<WOEResponseModel> responseCall = apiInterface.GetResponse(campWoeRequestModel);

            responseCall.enqueue(new Callback<WOEResponseModel>() {
                @Override
                public void onResponse(Call<WOEResponseModel> call, Response<WOEResponseModel> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (flag == 0) {
                            antiBodyEnterFrag.getpostwoeResponse(response.body());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<WOEResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });
        }else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN,2);
        }

    }

}
