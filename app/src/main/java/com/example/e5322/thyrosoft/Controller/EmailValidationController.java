package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.HandBillform;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.EmailModel;
import com.example.e5322.thyrosoft.Models.EmailValidationResponse;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailValidationController {
    private int flag;
    private Activity mactivity;
    private BS_EntryFragment bs_entryFragment;
    private Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity;
    HandBillform handBillform;
    private RequestQueue requestQueue;
    String type;

    public EmailValidationController(BS_EntryFragment bs_entryFragment, Activity activity) {
        this.mactivity = activity;
        this.bs_entryFragment = bs_entryFragment;
        flag = 1;
    }

    public EmailValidationController(Sgc_Pgc_Entry_Activity sgc_pgc_entry_activity, Activity activity, String type) {
        this.mactivity = activity;
        this.type = type;
        this.sgc_pgc_entry_activity = sgc_pgc_entry_activity;
        flag = 2;
    }

    public EmailValidationController(HandBillform handBillform, Activity activity) {
        this.mactivity = activity;
        this.handBillform = handBillform;
        flag = 3;
    }

    public void emailvalidationapi(String email) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mactivity);
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(mactivity, Api.THYROCARE).create(PostAPIInterface.class);
        EmailModel emailModel = new EmailModel();
        emailModel.setAppID("5");
        emailModel.setEmailID(email);

        Call<EmailValidationResponse> responseCall = apiInterface.getvalidemail(emailModel);

        responseCall.enqueue(new Callback<EmailValidationResponse>() {
            @Override
            public void onResponse(Call<EmailValidationResponse> call, Response<EmailValidationResponse> response) {
                GlobalClass.hideProgress(mactivity, progressDialog);
                try {
                    if (response.isSuccessful()) {
                        if (!GlobalClass.isNull(response.body().getSucess()) && response.body().getSucess().equalsIgnoreCase("TRUE")) {
                            if (flag == 1) {
                                bs_entryFragment.getemailresponse();
                            } else if (flag == 2) {
                                sgc_pgc_entry_activity.getemailresponse(type);
                            } else if (flag == 3) {
                                handBillform.getemailresponse();
                            }
                        } else {
                            Toast.makeText(mactivity, MessageConstants.Kdly_entee_emaliID, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        GlobalClass.showTastyToast(mactivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EmailValidationResponse> call, Throwable t) {
                GlobalClass.hideProgress(mactivity, progressDialog);
                GlobalClass.showTastyToast(mactivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
            }
        });
    }
}
