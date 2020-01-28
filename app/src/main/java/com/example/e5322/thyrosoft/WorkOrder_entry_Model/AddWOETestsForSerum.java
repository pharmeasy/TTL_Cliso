package com.example.e5322.thyrosoft.WorkOrder_entry_Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.ExpandableAdpaterAddWorkOrderEntry;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.EditTestExpandListAdapterCheckboxDelegate;
import com.example.e5322.thyrosoft.Interface.EditTestExpandListAdapterCheckboxDelegateAddWOE;
import com.example.e5322.thyrosoft.MainModelForAllTests.B2B_MASTERSMainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.Product_Rate_MasterModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.SummaryAddWoe;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

public class AddWOETestsForSerum extends AppCompatActivity {
    Activity mActivity = AddWOETestsForSerum.this;
    SearchView sv_testsList1;
    ExpandableListView exp_list;
    Button btn_save;
    // ArrayList<BaseModel> selectedTestsList;
    MainModel mainModel;
    Button go_button;
    ImageView back, home;
    MainModel onlySelected;
    private GlobalClass globalClass;
    RequestQueue requestQueuepoptestILS;
    private ExpandableAdpaterAddWorkOrderEntry expAdapter;
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    private ArrayList<B2B_MASTERSMainModel> finalproductlist;
    ProgressDialog progressDialog;
    TextView show_selected_tests_list_test_ils1;
    LinearLayout before_discount_layout2, lineargetselectedtestforILS;
    private ArrayList<BaseModel> selectedTestsList;
    List<String> showTestNmaes;
    List<String> getTestCodes;
    String user, passwrd, access, api_key, convertDate;
    SharedPreferences prefs, getBarcodeDetails;
    String RES_ID, barcode, ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE, SL_NO, STATUS, SU_CODE1, SU_CODE2, TESTS;
    ;
    public String TAG = AddWOETestsForSerum.class.getSimpleName().toString();
    TextView title;
    private String getTestName;
    private String passTheTests;
    int days = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_woetests_for_serum);


        globalClass = new GlobalClass(this);
        sv_testsList1 = (SearchView) findViewById(R.id.sv_testsList1);
        exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        btn_save = (Button) findViewById(R.id.btn_save);
        go_button = (Button) findViewById(R.id.go_button);
        before_discount_layout2 = (LinearLayout) findViewById(R.id.before_discount_layout2);

        show_selected_tests_list_test_ils1 = (TextView) findViewById(R.id.show_selected_tests_list_test_ils1);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        home = (ImageView) findViewById(R.id.home);
        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
        before_discount_layout2.setVisibility(View.GONE);


        SharedPreferences getAddtionalTest = getSharedPreferences("AddTestType", MODE_PRIVATE);
        getTestName = getAddtionalTest.getString("TESTS", null);

        selectedTestsList = new ArrayList<>();
        title.setText("Test List");
        progressDialog = new ProgressDialog(AddWOETestsForSerum.this);
        progressDialog.setTitle("Kindly wait ...");
        progressDialog.setMessage(ToastFile.processing_request);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(20);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        sv_testsList1.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));
        sv_testsList1.setIconifiedByDefault(false);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(AddWOETestsForSerum.this);
            }
        });

        days = GlobalClass.getStoreSynctime(AddWOETestsForSerum.this);

        if (GlobalClass.Dayscnt(AddWOETestsForSerum.this) >= Constants.DAYS_CNT) {
            getAlltTestData();
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");
            MainModel obj = gson.fromJson(json, MainModel.class);
            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                    callAdapter(obj);
                }
            } else {
                getAlltTestData();
            }
        }

        getBarcodeDetails = getSharedPreferences("AddTestType", MODE_PRIVATE);
        ALERT = getBarcodeDetails.getString("ALERT", null);
        BARCODE = getBarcodeDetails.getString("BARCODE", null);
        BVT_HRS = getBarcodeDetails.getString("BVT_HRS", null);
        LABCODE = getBarcodeDetails.getString("LABCODE", null);
        PATIENT = getBarcodeDetails.getString("PATIENT", null);
        REF_DR = getBarcodeDetails.getString("REF_DR", null);
        REQUESTED_ADDITIONAL_TESTS = getBarcodeDetails.getString("REQUESTED_ADDITIONAL_TESTS", null);
        REQUESTED_ON = getBarcodeDetails.getString("REQUESTED_ON", null);
        RES_ID = getBarcodeDetails.getString("RES_ID", null);
        SDATE = getBarcodeDetails.getString("SDATE", null);
        SL_NO = getBarcodeDetails.getString("SL_NO", null);
        STATUS = getBarcodeDetails.getString("STATUS", null);
        SU_CODE1 = getBarcodeDetails.getString("SU_CODE1", null);
        SU_CODE2 = getBarcodeDetails.getString("SU_CODE2", null);
        TESTS = getBarcodeDetails.getString("TESTS", null);
        Toast.makeText(mActivity, "" + TESTS, Toast.LENGTH_SHORT).show();

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expAdapter != null) {
                    expAdapter.filterData(sv_testsList1.getQuery().toString());
                    if (!sv_testsList1.getQuery().toString().isEmpty()) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.collapseGroup(i);
                    }
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != getCurrentFocus())
                    imm.hideSoftInputFromWindow(getCurrentFocus()
                            .getApplicationWindowToken(), 0);
            }
        });


        sv_testsList1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (expAdapter != null) {
                    expAdapter.filterData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (expAdapter != null) {
                    expAdapter.filterData(query);

                }
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSelcetdTEst = show_selected_tests_list_test_ils1.getText().toString();
                if (getSelcetdTEst.equals("Select Test")) {
                    Toast.makeText(mActivity, ToastFile.slt_tst_nm, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(AddWOETestsForSerum.this, SummaryAddWoe.class);
                    i.putExtra("extraAddedTest", getSelcetdTEst);
                    i.putExtra("passtoAPI", passTheTests);

                    Toast.makeText(mActivity, passTheTests, Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            }
        });

    }

    private void getAlltTestData() {
        globalClass.showProgressDialog(this);
        requestQueuepoptestILS = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + "" + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: RESPONSE" + response);
                String getResponse = response.optString("RESPONSE", "");
                if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(AddWOETestsForSerum.this);
                } else {
                    Gson gson = new Gson();
                    mainModel = new MainModel();
                    mainModel = gson.fromJson(response.toString(), MainModel.class);
                    onlySelected = new MainModel();

                    SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(AddWOETestsForSerum.this);
                    SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                    Gson gson22 = new Gson();
                    String json22 = gson22.toJson(mainModel);
                    String jsonSelected = gson22.toJson(onlySelected);
                    prefsEditor1.putString("MyObject", json22);
                   // GlobalClass.StoreSyncTime(AddWOETestsForSerum.this);
                    prefsEditor1.commit();


                    GlobalClass.storeProductsCachingTime(AddWOETestsForSerum.this);


                    B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();
                    ArrayList<BaseModel> baseModelProfile = new ArrayList<>();
                    ArrayList<BaseModel> baseModelTest = new ArrayList<>();

                    mainModel = gson.fromJson(response.toString(), MainModel.class);
                    if (mainModel != null) {
                        onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                        if (mainModel.getB2B_MASTERS().getPOP().size() != 0 && !mainModel.getB2B_MASTERS().getPOP().isEmpty())
                            if (mainModel.getB2B_MASTERS().getPROFILE().size() != 0 && !mainModel.getB2B_MASTERS().getPROFILE().isEmpty())
                                for (int i = 0; i < mainModel.getB2B_MASTERS().getPROFILE().size(); i++) {
                                    if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length < 2 && mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes() != null) {
                                        if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length != 0) {
                                            for (int j = 0; j < mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length; j++) {
                                                if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length == 1) {
                                                    if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getSpecimen_type().equals("SERUM")) {
                                                        baseModelProfile.add(mainModel.getB2B_MASTERS().getPROFILE().get(i));
                                                        b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                        if (mainModel.getB2B_MASTERS().getTESTS().size() != 0 && !mainModel.getB2B_MASTERS().getTESTS().isEmpty())
                            for (int i = 0; i < mainModel.getB2B_MASTERS().getTESTS().size(); i++) {
                                if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length < 2 && mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes() != null) {
                                    if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length != 0) {
                                        for (int j = 0; j < mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length; j++) {
                                            if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length == 1) {
                                                if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getSpecimen_type().equals("SERUM")) {
                                                    baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(i));
                                                    b2B_mastersMainModel.setTESTS(baseModelTest);

                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        onlySelected.setB2B_MASTERS(b2B_mastersMainModel);

                    }
                    // onlySelected
                    callAdapter(onlySelected);
                    globalClass.dismissProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuepoptestILS.add(jsonObjectRequestPop);
        Log.e(TAG, "getAlltTestData: URL" + jsonObjectRequestPop);
    }

    private void callAdapter(MainModel mainModel) {

        b2bmasterarraylist = new ArrayList<>();
        b2bmasterarraylist.add(mainModel.B2B_MASTERS);

        ArrayList<Product_Rate_MasterModel> finalproductlist = new ArrayList<Product_Rate_MasterModel>();
        ArrayList<BaseModel> profileRateMasterModels = new ArrayList<BaseModel>();
        ArrayList<BaseModel> testRateMasterModels = new ArrayList<BaseModel>();
        B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();
        String testtype = "";
        for (int i = 0; i < b2bmasterarraylist.size(); i++) {
            if (b2bmasterarraylist.get(i).getPROFILE().size() != 0 && !b2bmasterarraylist.get(i).getPROFILE().isEmpty())
                for (int l = 0; l < b2bmasterarraylist.get(i).getPROFILE().size(); l++) {
                    if (b2bmasterarraylist.get(i).getPROFILE().get(l).getBarcodes().length < 2 && b2bmasterarraylist.get(i).getPROFILE().get(l).getBarcodes() != null) {
                        if (b2bmasterarraylist.get(i).getPROFILE().get(l).getBarcodes().length != 0) {
                            for (int j = 0; j < b2bmasterarraylist.get(i).getPROFILE().get(l).getBarcodes().length; j++) {
                                if (b2bmasterarraylist.get(i).getPROFILE().get(l).getBarcodes().length == 1) {
                                    if (b2bmasterarraylist.get(i).getPROFILE().get(l).getBarcodes()[j].getSpecimen_type().equals("SERUM")) {


                                        profileRateMasterModels.add(b2bmasterarraylist.get(i).getPROFILE().get(l));
                                        b2B_mastersMainModel.setPROFILE(profileRateMasterModels);
                                    }
                                }
                            }
                        }
                    }
                }


            if (mainModel.getB2B_MASTERS().getTESTS().size() != 0 && !mainModel.getB2B_MASTERS().getTESTS().isEmpty())
                for (int m = 0; m < mainModel.getB2B_MASTERS().getTESTS().size(); m++) {
                    if (mainModel.getB2B_MASTERS().getTESTS().get(m).getBarcodes().length < 2 && mainModel.getB2B_MASTERS().getTESTS().get(m).getBarcodes() != null) {
                        if (mainModel.getB2B_MASTERS().getTESTS().get(m).getBarcodes().length != 0) {
                            for (int j = 0; j < mainModel.getB2B_MASTERS().getTESTS().get(m).getBarcodes().length; j++) {
                                if (mainModel.getB2B_MASTERS().getTESTS().get(m).getBarcodes().length == 1) {
                                    if (mainModel.getB2B_MASTERS().getTESTS().get(m).getBarcodes()[j].getSpecimen_type().equals("SERUM")) {
                                        testRateMasterModels.add(mainModel.getB2B_MASTERS().getTESTS().get(m));
                                        b2B_mastersMainModel.setTESTS(testRateMasterModels);
                                    }
                                }
                            }
                        }
                    }
                }


            Product_Rate_MasterModel product_rate_masterModel;

            product_rate_masterModel = new Product_Rate_MasterModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_PROFILE);
            product_rate_masterModel.setTestRateMasterModels(profileRateMasterModels);
            finalproductlist.add(product_rate_masterModel);

            product_rate_masterModel = new Product_Rate_MasterModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
            product_rate_masterModel.setTestRateMasterModels(testRateMasterModels);
            finalproductlist.add(product_rate_masterModel);

        }
        String tests = TESTS;
        expAdapter = new ExpandableAdpaterAddWorkOrderEntry(mActivity, finalproductlist, getTestName, selectedTestsList, new EditTestExpandListAdapterCheckboxDelegate() {
            @Override
            public void onCheckChange(ArrayList<BaseModel> selectedTests) {
                System.out.println("check changed");
                selectedTestsList = selectedTests;
                expAdapter.notifyDataSetChanged();
                if (selectedTestsList.size() != 0) {
                    before_discount_layout2.setVisibility(View.VISIBLE);

                    showTestNmaes = new ArrayList<>();
                    getTestCodes = new ArrayList<>();
                    for (int i = 0; i < selectedTestsList.size(); i++) {
                        showTestNmaes.add(selectedTestsList.get(i).getName());
                        for (int j = 0; j < selectedTestsList.get(i).getBarcodes().length; j++) {
                            getTestCodes.add(selectedTestsList.get(i).getBarcodes()[j].getCode());
                        }
                    }
                    String displayslectedtest = TextUtils.join(" , ", showTestNmaes);


                    passTheTests = TextUtils.join(",", getTestCodes);
                    show_selected_tests_list_test_ils1.setText(displayslectedtest);

                    SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                    editor.putString("testsSElected", passTheTests);
                    editor.putString("passtoAPI", passTheTests);
                    editor.commit();
                } else if (selectedTestsList.size() == 0) {
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        }, new EditTestExpandListAdapterCheckboxDelegateAddWOE() {

            @Override
            public void onCheckChangeAddWoe(ArrayList<BaseModel> selectedTests) {
                System.out.println("check changed");
                selectedTestsList = selectedTests;
                expAdapter.notifyDataSetChanged();

            }
        });
        exp_list.setAdapter(expAdapter);
        progressDialog.dismiss();
    }


}
