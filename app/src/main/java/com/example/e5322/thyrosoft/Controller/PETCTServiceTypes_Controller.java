package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ServiceModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PETCTServiceTypes_Controller {

    private static final String TAG = PETCTServiceTypes_Controller.class.getSimpleName();
    private Activity mActivity;
    private GlobalClass globalClass;
    private PETCT_Frag petct_frag;
    int flag;

    public PETCTServiceTypes_Controller(Activity activity, PETCT_Frag petct_frag, String header) {
        this.mActivity = activity;
        this.petct_frag = petct_frag;
        globalClass = new GlobalClass(mActivity);
        flag = 0;
    }

    public void getPetctService(String header, String centerId, String user) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.SCANSOAPI).create(APIInteface.class);
        Log.e(TAG, "HEADER ----->" + header);
        Call<List<ServiceModel>> responseCall = apiInterface.getservicelist(header, centerId, user);
        Log.e("TAG", "SERVICE URL --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<List<ServiceModel>>() {
            @Override
            public void onResponse(Call<List<ServiceModel>> call, Response<List<ServiceModel>> response) {
                GlobalClass.hideProgress(mActivity, progressDialog);
                try {
                    if (flag == 0) {
                        petct_frag.getserviceResponse(response.body(), progressDialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ServiceModel>> call, Throwable t) {
                GlobalClass.hideProgress(mActivity, progressDialog);
            }
        });
    }
}
