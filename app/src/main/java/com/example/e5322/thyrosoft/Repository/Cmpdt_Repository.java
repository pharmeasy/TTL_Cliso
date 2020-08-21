package com.example.e5322.thyrosoft.Repository;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.CompanyContact_activity;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Models.Cmpdt_Model;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cmpdt_Repository {

    APIInteface getAPIInteface;


    public Cmpdt_Repository() {
    }

    public MutableLiveData<Cmpdt_Model.ContactArrayListBean> getEmpdt(CompanyContact_activity companyContact_activity) {

        final MutableLiveData<Cmpdt_Model.ContactArrayListBean> emplist = new MutableLiveData<>();
        getAPIInteface = RetroFit_APIClient.getInstance().getClient(companyContact_activity, Api.static_pages_link).create(APIInteface.class);
        Call<Cmpdt_Model.ContactArrayListBean> cmpdtModelCall = getAPIInteface.getemployeedt("SUPPORT", "Contact_Details");

        cmpdtModelCall.enqueue(new Callback<Cmpdt_Model.ContactArrayListBean>() {
            @Override
            public void onResponse(Call<Cmpdt_Model.ContactArrayListBean> call, Response<Cmpdt_Model.ContactArrayListBean> response) {
                try {
                    if (response.body() != null)
                        emplist.setValue(response.body());

                } catch (Exception e) {
                    Log.v("TAG", e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<Cmpdt_Model.ContactArrayListBean> call, Throwable t) {

            }
        });
        return emplist;
    }
}
