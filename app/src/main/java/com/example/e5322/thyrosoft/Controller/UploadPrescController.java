package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.Scan_Barcode_Outlabs_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.UploadPrescRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.UploadPrescResponseModel;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPrescController {
    ProgressDialog progress;
    Scan_Barcode_ILS_New scan_barcode_ils_new;
    Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity;
    Activity activity;
    Scan_Barcode_Outlabs scan_barcode_outlabs;
    int flag = 0;

    public UploadPrescController(Scan_Barcode_ILS_New scan_barcode_ils_new) {
        this.scan_barcode_ils_new = scan_barcode_ils_new;
        activity = scan_barcode_ils_new;
        progress = GlobalClass.progress(activity, false);
        flag=1;
    }

    public UploadPrescController(Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity) {
        this.scan_barcode_outlabs_activity = scan_barcode_outlabs_activity;
        activity = scan_barcode_outlabs_activity;
        progress = GlobalClass.progress(activity, false);
        flag=2;
    }

    public UploadPrescController(Scan_Barcode_Outlabs scan_barcode_outlabs) {
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        activity = scan_barcode_outlabs;
        progress = GlobalClass.progress(activity, false);
        flag=3;
    }


    public void UploadPrescAPICall(UploadPrescRequestModel uploadPrescRequestModel) {
        try {
            progress.show();

            RequestBody Image = RequestBody.create(MediaType.parse("multipart/form-data"), uploadPrescRequestModel.getImgUrl());
            MultipartBody.Part ImgUrl = MultipartBody.Part.createFormData(uploadPrescRequestModel.getSourceCode(), uploadPrescRequestModel.getImgUrl().getName(), Image);

            RequestBody SourceCode = RequestBody.create(MediaType.parse("multipart/form-data"), uploadPrescRequestModel.getSourceCode());
            RequestBody Patientid = RequestBody.create(MediaType.parse("multipart/form-data"), uploadPrescRequestModel.getPatientid());
            RequestBody DocType = RequestBody.create(MediaType.parse("multipart/form-data"), uploadPrescRequestModel.getDocType());
            RequestBody ENTRYBY = RequestBody.create(MediaType.parse("multipart/form-data"), uploadPrescRequestModel.getENTRYBY());

            PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
            Call<UploadPrescResponseModel> call = postAPIInterface.UploadPrescriptionAPI(ImgUrl, SourceCode, Patientid, DocType, ENTRYBY);
            call.enqueue(new Callback<UploadPrescResponseModel>() {
                @Override
                public void onResponse(Call<UploadPrescResponseModel> call, Response<UploadPrescResponseModel> response) {
                    GlobalClass.hideProgress(activity, progress);
                    if (flag==1){
                        scan_barcode_ils_new.getprescupload();
                    }else if (flag==2){
                        scan_barcode_outlabs_activity.getprescupload();
                    }else if (flag==3){
                        scan_barcode_outlabs.getprescupload();
                    }

                }

                @Override
                public void onFailure(Call<UploadPrescResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(activity, progress);
                    if (flag==1){
                        scan_barcode_ils_new.getprescupload();
                    }else if (flag==2){
                        scan_barcode_outlabs_activity.getprescupload();
                    }else if (flag==3){
                        scan_barcode_outlabs.getprescupload();
                    }
                }
            });
        } catch (Exception e) {
            GlobalClass.hideProgress(activity, progress);
            e.printStackTrace();
        }
    }

}
