package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.Company_Adapter;
import com.example.e5322.thyrosoft.Controller.GetCompanyContactController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Company_Contact_Model;
import com.example.e5322.thyrosoft.Models.ConatctsResponseModel;
import com.example.e5322.thyrosoft.Models.ContactsReqModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.ViewModel.Cmpdt_Viewmodel;

public class CompanyContact_activity extends AppCompatActivity {

    ImageView back, home;
    RecyclerView contact_list;
    Spinner contact_type_spinner;
    Company_Contact_Model company_contact_model;
    LinearLayoutManager linearLayoutManager;
    Company_Adapter company_adapter;
    ImageView add;
    Cmpdt_Viewmodel cmpdt_viewmodel;
    private RequestQueue requestQueue_CompanyContact;
    private String passSpinner_value;
    private String TAG = CompanyContact_activity.class.getSimpleName();
    private Global globalClass;
    private SharedPreferences prefs;
    private String api_key = "";
    LinearLayout ll_spinner;
    private String CLIENT_TYPE = "";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_list_fragment);

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        api_key = prefs.getString("API_KEY", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");
        contact_list = (RecyclerView) findViewById(R.id.contact_list);
        contact_type_spinner = (Spinner) findViewById(R.id.contact_type_spinner);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        ll_spinner = (LinearLayout) findViewById(R.id.ll_spinner);


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
//        getCompany_contact_details();

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

        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            ll_spinner.setVisibility(View.GONE);
            getCompany_contact_details();
        } else {
            ll_spinner.setVisibility(View.VISIBLE);
        }
    }

    private void getCompany_contact_details() {
        String getSpinnertype = "" + contact_type_spinner.getSelectedItem().toString();
//        requestQueue_CompanyContact = GlobalClass.setVolleyReq(CompanyContact_activity.this);

        if (getSpinnertype.equalsIgnoreCase("STATE OFFICER")) {
            passSpinner_value = "STATE OFFICER";
        } else {
            passSpinner_value = getSpinnertype;
        }

        ContactsReqModel contactsReqModel = new ContactsReqModel();
        contactsReqModel.setApikey("" + api_key);
        if (ll_spinner.getVisibility() == View.VISIBLE) {
            contactsReqModel.setType(passSpinner_value);
        } else {
            contactsReqModel.setType("");
        }
        GetCompanyContactController getCompanyContactController = new GetCompanyContactController(this);
        getCompanyContactController.CallAPI(contactsReqModel);


       /* Log.e(TAG, "getCompany_contact_details: " + Api.static_pages_link + passSpinner_value + "/" + "Contact_Details");
        JsonObjectRequest jsonObjectRequestFAQ = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + passSpinner_value + "/" + "Contact_Details", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: " + response);
                company_contact_model = null;
                company_contact_model = new Company_Contact_Model();
                Gson gson = new Gson();
                company_contact_model = gson.fromJson(response.toString(), Company_Contact_Model.class);

                if (company_contact_model != null) {
                    if (!GlobalClass.isNull(company_contact_model.getResponse()) && company_contact_model.getResponse().equalsIgnoreCase("Success")) {
                        company_adapter = new Company_Adapter(CompanyContact_activity.this, company_contact_model.getContact_Array_list());
                        contact_list.setAdapter(company_adapter);
                        company_adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CompanyContact_activity.this, company_contact_model.getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CompanyContact_activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
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
        GlobalClass.volleyRetryPolicy(jsonObjectRequestFAQ);
        requestQueue_CompanyContact.add(jsonObjectRequestFAQ);
        Log.e(TAG, "getCompany_contact_details: URL" + jsonObjectRequestFAQ);*/

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

    public void getcontacts(ConatctsResponseModel getLocationRespModel) {
        if (getLocationRespModel != null) {
            if (!GlobalClass.isNull(getLocationRespModel.getResponse()) && getLocationRespModel.getResponse().equalsIgnoreCase("Success")) {
                if (GlobalClass.isArraylistNotNull(getLocationRespModel.getContact_Array_list())) {
                    company_adapter = new Company_Adapter(CompanyContact_activity.this, getLocationRespModel.getContact_Array_list());
                    contact_list.setAdapter(company_adapter);
                    company_adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CompanyContact_activity.this, "" + company_contact_model.getResponse(), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(CompanyContact_activity.this, "" + ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CompanyContact_activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
