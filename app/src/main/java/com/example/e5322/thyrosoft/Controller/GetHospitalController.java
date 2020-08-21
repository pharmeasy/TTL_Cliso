package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Hospital_model;
import com.example.e5322.thyrosoft.Models.Hospital_req;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetHospitalController {
    private Activity mActivity;
    private GlobalClass globalClass;
    private RATEnterFrag ratEnterFrag;
    int flag;
    ConnectionDetector cd;

    public GetHospitalController(Activity activity, RATEnterFrag ratEnterFrag) {
        this.mActivity = activity;
        this.ratEnterFrag = ratEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public void GetHosptitalController(String apikey, String usercode) {
        if (cd.isConnectingToInternet()) {
            Hospital_req hospital_req = new Hospital_req();
            hospital_req.setApiKey(apikey);
            hospital_req.setUserCode(usercode);

            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            Call<Hospital_model> covidratemodelCall = postAPIInteface.GetWOEHospital(hospital_req);

            covidratemodelCall.enqueue(new Callback<Hospital_model>() {
                @Override
                public void onResponse(Call<Hospital_model> call, Response<Hospital_model> response) {

                    try {
                        if (flag == 0) {
                            ratEnterFrag.gethospitalResponse(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Hospital_model> call, Throwable t) {

                }
            });


        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }


    }
}
