package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Spinner;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PaitientDataResponseModel;
import com.example.e5322.thyrosoft.Models.PaitientdataRequestModel;
import com.example.e5322.thyrosoft.MultiSelectSpinner;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anti_Patineretdata_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private AntiBodyEnterFrag antiBodyEnterFrag;
    int flag;
    ConnectionDetector cd;

    public Anti_Patineretdata_Controller(Activity activity, AntiBodyEnterFrag antiBodyEnterFrag) {
        this.mActivity = activity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd=new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void postpatientdetailcontroller(String usercode, Spinner spr_occu, MultiSelectSpinner spn_symptoms, final String barcode_patient_id) {

        if (cd.isConnectingToInternet()){
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(APIInteface.class);
            PaitientdataRequestModel paitientdataRequestModel = new PaitientdataRequestModel();
            paitientdataRequestModel.setPatientid(barcode_patient_id);
            paitientdataRequestModel.setSourceCode(usercode);
            paitientdataRequestModel.setOccupation(spr_occu.getSelectedItem().toString());
            paitientdataRequestModel.setDisease(spn_symptoms.getSelectedItemsAsString());

            Call<PaitientDataResponseModel> responseCall = apiInterface.PostPatientdetails(paitientdataRequestModel);

            responseCall.enqueue(new Callback<PaitientDataResponseModel>() {
                @Override
                public void onResponse(Call<PaitientDataResponseModel> call, Response<PaitientDataResponseModel> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);

                    try {
                        if (flag == 0) {
                            antiBodyEnterFrag.getpatientdetailResponse(response.body(), barcode_patient_id);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PaitientDataResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });
        }else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN,2);
        }

    }

}
