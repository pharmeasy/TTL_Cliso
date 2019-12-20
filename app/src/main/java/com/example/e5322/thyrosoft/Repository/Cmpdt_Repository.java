package com.example.e5322.thyrosoft.Repository;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Models.Cmpdt_Model;
import com.example.e5322.thyrosoft.Retrofit.GetAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cmpdt_Repository {

    GetAPIInteface getAPIInteface;


    public Cmpdt_Repository() {
    }

    public MutableLiveData<Cmpdt_Model.ContactArrayListBean> getEmpdt() {

        final MutableLiveData<Cmpdt_Model.ContactArrayListBean> emplist = new MutableLiveData<>();
        getAPIInteface = RetroFit_APIClient.getInstance().getClient(Api.static_pages_link).create(GetAPIInteface.class);
        Call<Cmpdt_Model.ContactArrayListBean> cmpdtModelCall = getAPIInteface.getemployeedt("SUPPORT", "Contact_Details");
        Log.e("TAG", "getEmpdt: " + cmpdtModelCall.request().url());
        cmpdtModelCall.enqueue(new Callback<Cmpdt_Model.ContactArrayListBean>() {
            @Override
            public void onResponse(Call<Cmpdt_Model.ContactArrayListBean> call, Response<Cmpdt_Model.ContactArrayListBean> response) {
                try {
                    emplist.setValue(response.body());

                } catch (Exception e) {
                    System.out.println("TAG"+e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<Cmpdt_Model.ContactArrayListBean> call, Throwable t) {

            }
        });
        return emplist;
    }
}
