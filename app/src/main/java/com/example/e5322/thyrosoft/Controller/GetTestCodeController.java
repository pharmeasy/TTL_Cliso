package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.SRFCovidWOEEnterFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetTestCodeResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTestCodeController {

    private ProgressDialog progress;
    private Activity activity;
    Covidenter_Frag covidenter_frag;
    SRFCovidWOEEnterFragment srfCovidWOEEnterFragment;
    int flag;

    public GetTestCodeController(Covidenter_Frag covidenter_frag) {
        this.covidenter_frag = covidenter_frag;
        this.activity = covidenter_frag.getActivity();
        progress = GlobalClass.progress(activity, false);
        flag = 1;
    }


    public GetTestCodeController(SRFCovidWOEEnterFragment srfCovidWOEEnterFragment) {
        this.srfCovidWOEEnterFragment = srfCovidWOEEnterFragment;
        this.activity = srfCovidWOEEnterFragment.getActivity();
        progress = GlobalClass.progress(activity, false);
        flag = 2;
    }


    public void CallAPI(String user) {
        try {
            progress.show();
            APIInteface geTapiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(APIInteface.class);
            Call<GetTestCodeResponseModel> responseCall = geTapiInterface.getTestCode(user);
            responseCall.enqueue(new Callback<GetTestCodeResponseModel>() {
                @Override
                public void onResponse(Call<GetTestCodeResponseModel> call, Response<GetTestCodeResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        if (flag == 1) {
                            covidenter_frag.getTestCode(response.body());
                        } else {
                            srfCovidWOEEnterFragment.getTestCode(response.body());
                        }

                    } else {
                        Toast.makeText(activity, "" + ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetTestCodeResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
