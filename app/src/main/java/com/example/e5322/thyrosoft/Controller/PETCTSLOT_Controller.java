package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.SlotModel;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PETCTSLOT_Controller {

    private static final String TAG = POSTBookLeadController.class.getSimpleName();
    private Activity mActivity;
    private GlobalClass globalClass;
    PETCT_Frag petct_frag;
    int flag;
    ConnectionDetector cd;

    public PETCTSLOT_Controller(Activity activity, PETCT_Frag petct_frag, String header) {
        this.mActivity = activity;
        this.petct_frag = petct_frag;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
        flag = 0;
    }


    public void getSlotsController(TextView txt_appdate, String header, String centerId) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(mActivity);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.SCANSOAPI).create(APIInteface.class);
            Call<List<SlotModel>> responseCall = apiInterface.getslot(header, GlobalClass.formatDate("dd-mm-yyyy", "yyyy-mm-dd", txt_appdate.getText().toString()), centerId);
            Log.e("TAG", "SLOT URL --->" + responseCall.request().url());

            responseCall.enqueue(new Callback<List<SlotModel>>() {
                @Override
                public void onResponse(Call<List<SlotModel>> call, Response<List<SlotModel>> response) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                    try {
                        if (response.body() != null && response.isSuccessful()) {
                            if (flag == 0) {
                                petct_frag.getSlotResponse(response.body());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<SlotModel>> call, Throwable t) {
                    GlobalClass.hideProgress(mActivity, progressDialog);
                }
            });

        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }

    }

}
