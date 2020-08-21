package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.EditText;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.AntiBodyEnterFrag;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateOTP_Controller {
    private Activity mActivity;
    private GlobalClass globalClass;
    private AntiBodyEnterFrag antiBodyEnterFrag;
    private Covidenter_Frag covidenter_frag;
    private RATEnterFrag ratEnterFrag;
    int flag;
    ConnectionDetector cd;

    public ValidateOTP_Controller(Activity activity, AntiBodyEnterFrag antiBodyEnterFrag) {
        this.mActivity = activity;
        this.antiBodyEnterFrag = antiBodyEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }

    public ValidateOTP_Controller(Activity activity, Covidenter_Frag covidenter_frag) {
        this.mActivity = activity;
        this.covidenter_frag = covidenter_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 1;
    }

    public ValidateOTP_Controller(Activity activity, RATEnterFrag ratEnterFrag) {
        this.mActivity = activity;
        this.ratEnterFrag = ratEnterFrag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 2;
    }

    public void getvalidateotp_Controller(String apikey, EditText edt_missed_mobile, EditText edt_verifycc, String usercode) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
            Covid_validateotp_req covid_validateotp_req = new Covid_validateotp_req();
            covid_validateotp_req.setApi_key(apikey);
            covid_validateotp_req.setMobile(edt_missed_mobile.getText().toString());
            covid_validateotp_req.setOtp(edt_verifycc.getText().toString());
            covid_validateotp_req.setScode(usercode);

            Call<Covid_validateotp_res> covidotpresponseCall = postAPIInteface.validateotp(covid_validateotp_req);

            covidotpresponseCall.enqueue(new Callback<Covid_validateotp_res>() {
                @Override
                public void onResponse(Call<Covid_validateotp_res> call, Response<Covid_validateotp_res> response) {
                    GlobalClass.hideProgress(mActivity,progressDialog);

                    if (flag==0){
                        antiBodyEnterFrag.getvalidateOTPResponse(response);
                    }else if (flag==1){
                        covidenter_frag.getvalidateOTPResponse(response);
                    }else if (flag==2){
                        ratEnterFrag.getvalidateOTPResponse(response);
                    }

                }

                @Override
                public void onFailure(Call<Covid_validateotp_res> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity,progressDialog);
                }
            });
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }
}
