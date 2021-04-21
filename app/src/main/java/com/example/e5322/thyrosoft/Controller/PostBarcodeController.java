package com.example.e5322.thyrosoft.Controller;


import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Fragment.Consignment_fragment;
import com.example.e5322.thyrosoft.Fragment.Next_Consignment_Entry;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PostBarcodeModel;
import com.example.e5322.thyrosoft.Models.PostBarcodeResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostBarcodeController {

    private Activity mactivity;
    private Next_Consignment_Entry next_consignment_entry;
    int flag;
    Consignment_fragment consignmentFragment;

    public PostBarcodeController(Next_Consignment_Entry next_consignment_entry) {
        this.mactivity = next_consignment_entry.getActivity();
        this.next_consignment_entry = next_consignment_entry;
        flag = 1;
    }

    public PostBarcodeController(Consignment_fragment consignmentFragment) {
        this.mactivity = consignmentFragment.getActivity();
        this.consignmentFragment = consignmentFragment;
        flag = 2;
    }


    public void CallAPI(PostBarcodeModel postBarcodeModel) {
        ProgressDialog progressDialog = null;
        try {
            progressDialog = GlobalClass.ShowprogressDialog(mactivity);
            PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.LIVEAPI).create(PostAPIInteface.class);
            Call<PostBarcodeResponseModel> responseCall = apiInterface.postBarcodes(postBarcodeModel);

            final ProgressDialog finalProgressDialog = progressDialog;
            responseCall.enqueue(new Callback<PostBarcodeResponseModel>() {
                @Override
                public void onResponse(Call<PostBarcodeResponseModel> call, Response<PostBarcodeResponseModel> response) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    PostBarcodeResponseModel responseModel = response.body();
                    if (response.isSuccessful() && responseModel != null) {
                        if (flag == 1) {
                            next_consignment_entry.getBarcodeResponse(responseModel);

                        } else if (flag == 2) {
                            consignmentFragment.getBarcodeResponse(responseModel);
                        }
                    } else {
                        GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                    }
                }

                @Override
                public void onFailure(Call<PostBarcodeResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(mactivity, finalProgressDialog);
                    GlobalClass.showTastyToast(mactivity, ToastFile.something_went_wrong, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.hideProgress(mactivity, progressDialog);
        }
    }
}
