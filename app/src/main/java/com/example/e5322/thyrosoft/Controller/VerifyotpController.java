package com.example.e5322.thyrosoft.Controller;

import android.app.ProgressDialog;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.Models.VerifyotpRequest;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyotpController {

    SpecialOffer_Activity specialOffer_activity;
    GlobalClass globalClass;
    Start_New_Woe start_new_woe;
    int flag = 0;

    public VerifyotpController(SpecialOffer_Activity mContext) {
        this.specialOffer_activity = mContext;
        globalClass = new GlobalClass(mContext);
        flag = 1;
    }

    public VerifyotpController(Start_New_Woe start_new_woe) {
        this.start_new_woe = start_new_woe;
        globalClass = new GlobalClass((start_new_woe.getActivity()));
        flag = 2;
    }


    public void verifyotp(String user, String chechnumber, String otp) {
        ProgressDialog progressDialog = null;
        if (flag == 1) {
            progressDialog = GlobalClass.ShowprogressDialog(specialOffer_activity);
        } else {
            progressDialog = GlobalClass.ShowprogressDialog((start_new_woe.getActivity()));
        }

        VerifyotpRequest verifyotpRequest = new VerifyotpRequest();
        verifyotpRequest.setUserCode(user);
        verifyotpRequest.setDomain("Cli-So");
        if (flag==1){
            verifyotpRequest.setPurpose("Offer");
        }else {
            verifyotpRequest.setPurpose("woe");
        }

        verifyotpRequest.setMobile(chechnumber);
        verifyotpRequest.setOTP(otp);
        APIInteface apiInterface;
       if (flag==1){
           apiInterface = RetroFit_APIClient.getInstance().getClient(specialOffer_activity, Api.OTPgenegrate).create(APIInteface.class);
       }else {
           apiInterface = RetroFit_APIClient.getInstance().getClient(start_new_woe.getActivity(), Api.OTPgenegrate).create(APIInteface.class);
       }

        Call<VerifyotpModel> responseCall = apiInterface.Verifyotp(verifyotpRequest);


        final ProgressDialog finalProgressDialog = progressDialog;
        responseCall.enqueue(new Callback<VerifyotpModel>() {
            @Override
            public void onResponse(Call<VerifyotpModel> call, Response<VerifyotpModel> response) {
                VerifyotpModel verifyotpModel = response.body();
                GlobalClass.hideProgress(specialOffer_activity, finalProgressDialog);
                try {
                    if (verifyotpModel!=null && !GlobalClass.isNull(verifyotpModel.getResponseId()) && verifyotpModel.getResponseId().equalsIgnoreCase("RES0001")) {
                        if (flag == 1) {
                            specialOffer_activity.onVerifyotp(verifyotpModel);
                        } else {
                            start_new_woe.onVerifyotp(verifyotpModel);
                        }
                    } else {
                        if (flag == 1) {
                            specialOffer_activity.onVerifyotp(verifyotpModel);
                            GlobalClass.showTastyToast(specialOffer_activity, verifyotpModel.getResponse(),1);
                        } else {
                            start_new_woe.onVerifyotp(verifyotpModel);
                            GlobalClass.showTastyToast(((start_new_woe.getActivity())), verifyotpModel.getResponse(),2);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<VerifyotpModel> call, Throwable t) {

                GlobalClass.hideProgress(specialOffer_activity, finalProgressDialog);

            }
        });
    }
}
