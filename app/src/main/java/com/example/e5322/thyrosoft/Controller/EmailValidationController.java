package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import com.example.e5322.thyrosoft.Controller.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.Activity.frags.BS_MISEntryFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.EmailModel;
import com.example.e5322.thyrosoft.Models.EmailValidationResponse;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailValidationController {
    private static final String TAG = BloodSugarMISController.class.getSimpleName();
    private int flag;
    private Activity mactivity;
    private BS_EntryFragment bs_entryFragment;
    private Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity;
    private RequestQueue requestQueue;
    String type;

    public EmailValidationController(BS_EntryFragment bs_entryFragment, Activity activity) {
        this.mactivity = activity;
        this.bs_entryFragment = bs_entryFragment;
        flag = 1;
    }
    public EmailValidationController(Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity, Activity activity,String type) {
        this.mactivity = activity;
        this.type=type;
        this.sgc_pgc_entry_activity = sgc_pgc_entry_activity;
        flag = 2;
    }


    public void emailvalidationapi(String email) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mactivity);
        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.THYROCARE).create(PostAPIInteface.class);
        EmailModel emailModel = new EmailModel();
        emailModel.setAppID("5");
        emailModel.setEmailID(email);

        Call<EmailValidationResponse> responseCall = apiInterface.getvalidemail(emailModel);
       // Log.e(TAG, "EMAIL  LIST BODY ---->" + new GsonBuilder().create().toJson(emailModel));
        //Log.e(TAG, "EMAIL LIST URL ---->" + responseCall.request().url());
        com.example.e5322.thyrosoft.Controller.Log.e(TAG,new GsonBuilder().create().toJson(emailModel));
        com.example.e5322.thyrosoft.Controller.Log.e(TAG,""+responseCall.request().url());

        responseCall.enqueue(new Callback<EmailValidationResponse>() {
            @Override
            public void onResponse(Call<EmailValidationResponse> call, Response<EmailValidationResponse> response) {
                GlobalClass.hideProgress(mactivity, progressDialog);
                try {
                    if (response.body().getSucess().equalsIgnoreCase("TRUE")) {
                        if (flag==1){
                            bs_entryFragment.getemailresponse();
                        }else if (flag==2){
                            sgc_pgc_entry_activity.getemailresponse(type);
                        }
                    } else {
                        Toast.makeText(mactivity, "Kindly enter valid email ID", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EmailValidationResponse> call, Throwable t) {

            }
        });
    }
}