package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.Consignment_fragment;
import com.example.e5322.thyrosoft.Fragment.Next_Consignment_Entry;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ScanBarcodeResponseModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GetScannedBarcodeList {

    private final int flag;
    Next_Consignment_Entry next_consignment_entry;
    Activity mActivity;
    ConnectionDetector cd;
    Consignment_fragment consignment_fragment;


    public GetScannedBarcodeList(Next_Consignment_Entry next_consignment_entry) {
        this.next_consignment_entry = next_consignment_entry;
        this.mActivity = next_consignment_entry.getActivity();
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public GetScannedBarcodeList(Consignment_fragment consignment_fragment) {
        this.consignment_fragment = consignment_fragment;
        this.mActivity = consignment_fragment.getActivity();
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public void getScanneddetails() {
        try {
            if (cd.isConnectingToInternet()) {
                final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Userdetails", MODE_PRIVATE);
                String enterby = sharedPreferences.getString("Username", "");
                APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(APIInteface.class);
                Call<ScanBarcodeResponseModel> templateResponseCall = apiInterface.getScannedBarcode(enterby);

                templateResponseCall.enqueue(new Callback<ScanBarcodeResponseModel>() {
                    @Override
                    public void onResponse(Call<ScanBarcodeResponseModel> call, Response<ScanBarcodeResponseModel> response) {
                        try {
                            GlobalClass.hideProgress(mActivity, progressDialog);
                            if (response.body() != null) {
                                if (flag==1){
                                    next_consignment_entry.getScanBarcodeDetails(response.body());
                                }else if (flag==2){
                                    consignment_fragment.getScanBarcodeDetails(response.body());
                                }
                            } else {
                                GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ScanBarcodeResponseModel> call, Throwable t) {
                        GlobalClass.hideProgress(mActivity, progressDialog);
                    }
                });
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
