package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.Company_Adapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Company_Contact_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ViewModel.Cmpdt_Viewmodel;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

public class CompanyContact_activity extends AppCompatActivity {
    ImageView back, home;
    RecyclerView contact_list;
    Spinner contact_type_spinner;
    private RequestQueue requestQueue_CompanyContact;
    Company_Contact_Model company_contact_model;
    LinearLayoutManager linearLayoutManager;
    Company_Adapter company_adapter;
    ImageView add;
    private String passSpinner_value;
    private String TAG = CompanyContact_activity.class.getSimpleName().toString();
    private Global globalClass;
    Cmpdt_Viewmodel cmpdt_viewmodel;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_list_fragment);

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        contact_list = (RecyclerView) findViewById(R.id.contact_list);
        contact_type_spinner = (Spinner) findViewById(R.id.contact_type_spinner);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(CompanyContact_activity.this);
            }
        });

        linearLayoutManager = new LinearLayoutManager(CompanyContact_activity.this);
        contact_list.setLayoutManager(linearLayoutManager);
        ArrayAdapter company_spinner = ArrayAdapter.createFromResource(CompanyContact_activity.this, R.array.company_contact_spinner_values, R.layout.spinnerproperty);
        contact_type_spinner.setAdapter(company_spinner);
        getCompany_contact_details();

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        contact_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCompany_contact_details();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCompany_contact_details() {
        String getSpinnertype = contact_type_spinner.getSelectedItem().toString();
        requestQueue_CompanyContact = Volley.newRequestQueue(CompanyContact_activity.this);

        if (getSpinnertype.equalsIgnoreCase("STATE OFFICER")) {
            passSpinner_value = "STATE%20OFFICER";
        } else {
            passSpinner_value = getSpinnertype;
        }

        Log.e(TAG, "getCompany_contact_details: " + Api.static_pages_link + passSpinner_value + "/" + "Contact_Details");
        JsonObjectRequest jsonObjectRequestFAQ = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + passSpinner_value + "/" + "Contact_Details", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: " + response);
                company_contact_model = null;
                company_contact_model = new Company_Contact_Model();
                Gson gson = new Gson();
                company_contact_model = gson.fromJson(response.toString(), Company_Contact_Model.class);


                if (company_contact_model.getResponse().equals("Success")) {
                    company_adapter = new Company_Adapter(CompanyContact_activity.this, company_contact_model.getContact_Array_list());
                    contact_list.setAdapter(company_adapter);
                    company_adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(CompanyContact_activity.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            }
        });

        jsonObjectRequestFAQ.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_CompanyContact.add(jsonObjectRequestFAQ);
        Log.e(TAG, "getCompany_contact_details: URL" + jsonObjectRequestFAQ);

        /**MVVM Pattern*/

        /*cmpdt_viewmodel = ViewModelProviders.of(CompanyContact_activity.this).get(Cmpdt_Viewmodel.class);
        Observer<Cmpdt_Model.ContactArrayListBean> cmpdt_modelObserver = new Observer<Cmpdt_Model.ContactArrayListBean>() {
            @Override
            public void onChanged(@Nullable Cmpdt_Model.ContactArrayListBean contactArrayListBean) {
                cmpdt_viewmodel.setdata(contactArrayListBean, CompanyContact_activity.this, company_adapter, contact_list);
            }
        };

        cmpdt_viewmodel.getempdata().observe(CompanyContact_activity.this, cmpdt_modelObserver);*/

    }

    @Override
    public void onTrimMemory(int level) {
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Glide.get(this).clearMemory();
        super.onLowMemory();
    }
}
