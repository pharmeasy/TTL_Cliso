package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.Scan_Barcode_Outlabs_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetLocationReqModel;
import com.example.e5322.thyrosoft.Models.GetLocationRespModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetLocationController {

    Scan_Barcode_ILS_New scan_barcode_ils_new;
    private ProgressDialog progress;
    Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity;
    Activity activity;
    int flag = 0;
    Scan_Barcode_Outlabs scan_barcode_outlabs;

    public GetLocationController(Scan_Barcode_ILS_New scan_barcode_ils_new) {
        this.scan_barcode_ils_new = scan_barcode_ils_new;
        this.activity = scan_barcode_ils_new;
        flag = 1;
        progress = GlobalClass.progress(activity, false);

    }
    public GetLocationController(Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity) {
        this.scan_barcode_outlabs_activity = scan_barcode_outlabs_activity;
        this.activity = scan_barcode_outlabs_activity;
        flag = 2;
        progress = GlobalClass.progress(activity, false);

    }

    public GetLocationController(Scan_Barcode_Outlabs scan_barcode_outlabs) {
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        this.activity = scan_barcode_outlabs;
        flag = 3;
        progress = GlobalClass.progress(activity, false);

    }

    public void CallAPI(GetLocationReqModel getLocationReqModel) {
        try {
            progress.show();
            APIInteface apiInterface = null;
            try {

                apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(APIInteface.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<GetLocationRespModel> responseCall = apiInterface.GetLocation(getLocationReqModel);
            responseCall.enqueue(new Callback<GetLocationRespModel>() {
                @Override
                public void onResponse(Call<GetLocationRespModel> call, Response<GetLocationRespModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (response.isSuccessful() && response.body() != null) {
                        GetLocationRespModel getLocationRespModel = response.body();
                        if (flag == 1) {
                            scan_barcode_ils_new.getLocationResponse(getLocationRespModel);
                        } else if (flag == 2) {
                            scan_barcode_outlabs_activity.getLocationResponse(getLocationRespModel);
                        } else if (flag == 3) {
                            scan_barcode_outlabs.getLocationResponse(getLocationRespModel);
                        }

                    } else {
                        GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);

                    }
                }

                @Override
                public void onFailure(Call<GetLocationRespModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
        }
    }
}
