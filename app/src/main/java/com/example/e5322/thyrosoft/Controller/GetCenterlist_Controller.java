package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CenterList_Model;
import com.example.e5322.thyrosoft.Models.TokenModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCenterlist_Controller {

    PETCT_Frag petct_frag;
    GlobalClass globalClass;
    int flag = 0;
    Activity mActivity;


    public GetCenterlist_Controller(PETCT_Frag petct_frag, Activity activity) {
        this.petct_frag = petct_frag;
        mActivity = activity;
        globalClass = new GlobalClass(mActivity);
        flag = 1;
    }

    public void getcenterlist(TokenModel tokenResponseModel, String user) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
        final String header = tokenResponseModel.getToken_type() + " " + tokenResponseModel.getAccess_token();
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.SCANSOAPI).create(APIInteface.class);
        Call<List<CenterList_Model>> responseCall = apiInterface.getcenterList(header, user);

        Log.e("TAG", "header --->" + header);
        Log.e("TAG", "S C A N S O URL --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<List<CenterList_Model>>() {
            @Override
            public void onResponse(Call<List<CenterList_Model>> call, Response<List<CenterList_Model>> response) {
                try {

                    if (response.body() != null) {
                        GlobalClass.hideProgress(mActivity, progressDialog);
                        if (flag == 1) {
                            petct_frag.getCenterListResponse(response.body(),header);
                        }
                    } else {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                        GlobalClass.hideProgress(mActivity, progressDialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CenterList_Model>> call, Throwable t) {
                Log.e("TAG", "on failure: " + t.getLocalizedMessage());
                GlobalClass.hideProgress(mActivity, progressDialog);
            }
        });
    }
}
