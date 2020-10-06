package com.example.e5322.thyrosoft.HHHtest.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.HHHEnteredFrag;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientDetailRequestModel;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPatientDetails {
    private ProgressDialog progress;
    private Activity activity;
    HHHEnteredFrag hhhEnteredFrag;

    public GetPatientDetails(HHHEnteredFrag hhhEnteredFrag) {
        this.hhhEnteredFrag = hhhEnteredFrag;
        this.activity = hhhEnteredFrag.getActivity();
        progress = GlobalClass.progress(activity, false);
    }


    public void CallAPI(PatientDetailRequestModel patientDetailRequestModel) {
        try {
            progress.show();
            APIInteface geTapiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Static_API).create(APIInteface.class);
            Call<PatientResponseModel> responseCall = geTapiInterface.getResponse(patientDetailRequestModel);
            responseCall.enqueue(new Callback<PatientResponseModel>() {
                @Override
                public void onResponse(Call<PatientResponseModel> call, Response<PatientResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        hhhEnteredFrag.getPatientresponse(response.body());
                    } else {
                        Toast.makeText(activity, "" + ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PatientResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
