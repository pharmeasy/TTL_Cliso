package com.example.e5322.thyrosoft.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.e5322.thyrosoft.Activity.CompanyContact_activity;
import com.example.e5322.thyrosoft.Adapter.Company_Adapter;
import com.example.e5322.thyrosoft.Models.Cmpdt_Model;
import com.example.e5322.thyrosoft.Repository.Cmpdt_Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cmpdt_Viewmodel extends AndroidViewModel {

    MutableLiveData<Cmpdt_Model.ContactArrayListBean> listBeanMutableLiveData;
    Cmpdt_Repository cmpdt_repository;
    Company_Adapter company_adapter;
    CompanyContact_activity companyContact_activity;

    public Cmpdt_Viewmodel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Cmpdt_Model.ContactArrayListBean> getempdata() {
        listBeanMutableLiveData = new MutableLiveData<>();
        cmpdt_repository = new Cmpdt_Repository();
        listBeanMutableLiveData = cmpdt_repository.getEmpdt();
        return listBeanMutableLiveData;
    }

    public void setdata(Cmpdt_Model.ContactArrayListBean contactArrayListBean, CompanyContact_activity companyContact_activity, Company_Adapter company_adapter, RecyclerView contact_list) {
        this.company_adapter=company_adapter;
        this.companyContact_activity=companyContact_activity;
        ArrayList<Cmpdt_Model.ContactArrayListBean> emplist = new ArrayList<>();
        emplist.add(contactArrayListBean);
        Log.e("TAG", "set data API: " + emplist.size());


     /*   this.company_adapter = new Company_Adapter(companyContact_activity, emplist);
        contact_list.setAdapter(this.company_adapter);
        this.company_adapter.notifyDataSetChanged();*/

    }
}
