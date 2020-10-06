package com.example.e5322.thyrosoft.HHHtest.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.HHHEnterFrag;
import com.example.e5322.thyrosoft.HHHtest.HHHEnteredFrag;
import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.HHHtest.SelectTestsActivity;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTests {

    private ProgressDialog progress;
    private Activity activity;
    HHHEnterFrag hhhEnterFrag;
    SelectTestsActivity selectTestsActivity;
    int flag=0;
    HHHEnteredFrag hhhEnteredFrag;

    public GetTests(HHHEnterFrag hhhEnterFrag, Context context) {
        this.hhhEnterFrag = hhhEnterFrag;
        this.activity = hhhEnterFrag.getActivity();
        progress = GlobalClass.progress(activity, false);
        flag=1;
    }
    public GetTests(SelectTestsActivity selectTestsActivity) {
        this.selectTestsActivity = selectTestsActivity;
        this.activity = selectTestsActivity;
        progress = GlobalClass.progress(activity, false);
        flag=2;
    }

    public GetTests(HHHEnteredFrag hhhEnteredFrag) {
        this.hhhEnteredFrag = hhhEnteredFrag;
        this.activity = hhhEnteredFrag.getActivity();
        progress = GlobalClass.progress(activity, false);
        flag=3;
    }

    public void GetTests() {
        try {
            progress.show();

            APIInteface geTapiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Static_API).create(APIInteface.class);
            Call<GetTestResponseModel> responseCall = geTapiInterface.gettestdata();

            responseCall.enqueue(new Callback<GetTestResponseModel>() {
                @Override
                public void onResponse(Call<GetTestResponseModel> call, Response<GetTestResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        if (flag==1){
                          //  hhhEnterFrag.getresponse(response.body());
                        }else if (flag==2){
                            selectTestsActivity.getresponse(response.body());
                        }

                    } else {
                        Toast.makeText(activity, ""+ ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<GetTestResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
