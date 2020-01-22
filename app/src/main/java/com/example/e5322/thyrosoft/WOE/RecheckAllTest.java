package com.example.e5322.thyrosoft.WOE;

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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.e5322.thyrosoft.Adapter.ExpandableTestMasterListDisplayAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.EditTestExpandListAdapterCheckboxDelegate;
import com.example.e5322.thyrosoft.MainModelForAllTests.B2B_MASTERSMainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.Product_Rate_MasterModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ProductLisitngActivityNew;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

public class RecheckAllTest extends AppCompatActivity {

    String[] elements = new String[100];
    Activity mActivity = RecheckAllTest.this;
    SearchView sv_testsList1;
    ExpandableListView exp_list;
    Button btn_save;
    // ArrayList<BaseModel> selectedTestsList;
    MainModel mainModel;
    MainModel dummyModel;
    B2B_MASTERSMainModel dummyB2BmasterModel;
    ArrayList<BaseModel> popArrayList;
    ArrayList<BaseModel> profileArrayList;
    ArrayList<BaseModel> testArrayList;
    MainModel onlySelected;
    private GlobalClass globalClass;
    RequestQueue requestQueuepoptestILS;
    private ExpandableTestMasterListDisplayAdapter expAdapter;
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    private ArrayList<B2B_MASTERSMainModel> finalproductlist;
    ProgressDialog progressDialog;
    TextView show_selected_tests_list_test_ils;
    LinearLayout selected_test_display, lineargetselectedtestforILS;
    private ArrayList<BaseModel> selectedTestsList;
    List<String> showTestNmaes;
    List<String> getTestCodes;
    List<String> fixedLenghtList;
    ArrayList<String> sample_type_array;
    String TAG = RecheckWoeActivity.class.getSimpleName().toString();
    ImageView back, home;
    String user, passwrd, access, api_key, convertDate;
    SharedPreferences prefs, getBarcodeDetails;
    String RES_ID, barcode, ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE, SL_NO, STATUS, SU_CODE1, SU_CODE2, TESTS;
    TextView title;
    private String passTheTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recheck_all_test);

        globalClass = new GlobalClass(this);
        sv_testsList1 = (SearchView) findViewById(R.id.sv_testsList1);
        exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        btn_save = (Button) findViewById(R.id.btn_save);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        home = (ImageView) findViewById(R.id.home);
        selected_test_display = (LinearLayout) findViewById(R.id.selected_test_display);
        lineargetselectedtestforILS = (LinearLayout) findViewById(R.id.lineargetselectedtestforILS);
        show_selected_tests_list_test_ils = (TextView) findViewById(R.id.show_selected_tests_list_test_ils);
        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);
        selected_test_display.setVisibility(View.GONE);
        lineargetselectedtestforILS.setVisibility(View.GONE);

        title.setText("Test List");
        selectedTestsList = new ArrayList<>();
        progressDialog = new ProgressDialog(RecheckAllTest.this);
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
                GlobalClass.goToHome(RecheckAllTest.this);
            }
        });

        getBarcodeDetails = getSharedPreferences("RecheckTestType", MODE_PRIVATE);
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


        sv_testsList1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (expAdapter != null) {
                    expAdapter.filterData(query);
                    if (!query.isEmpty()) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.collapseGroup(i);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (expAdapter != null) {
                    expAdapter.filterData(query);
                    if (!query.isEmpty()) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.collapseGroup(i);
                    }

                }
                return false;
            }
        });


        if (GlobalClass.Dayscnt(RecheckAllTest.this) >= Constants.DAYS_CNT) {
            getAlltTestData();
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");

            MainModel obj = gson.fromJson(json, MainModel.class);

            onlySelected = new MainModel();
            if (TESTS != null)
                elements = TESTS.split(",");
            fixedLenghtList = Arrays.asList(elements);
            sample_type_array = new ArrayList<String>(fixedLenghtList);

            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {

                    B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();
                    if (obj != null) {
                        onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                        ArrayList<BaseModel> baseModelPop = new ArrayList<>();
                        ArrayList<BaseModel> baseModelProfile = new ArrayList<>();
                        ArrayList<BaseModel> baseModelTest = new ArrayList<>();

                        if (obj.getB2B_MASTERS().getPOP().size() != 0 && !obj.getB2B_MASTERS().getPOP().isEmpty()) {
                            for (int i = 0; i < obj.getB2B_MASTERS().getPOP().size(); i++) {

                                for (int j = 0; j < obj.getB2B_MASTERS().getPOP().get(i).getBarcodes().length; j++) {
                                    for (int k = 0; k < sample_type_array.size(); k++) {
                                        if (obj.getB2B_MASTERS().getPOP().get(i).getBarcodes()[j].getCode().equals(sample_type_array.get(k))) {
                                            baseModelPop.add(obj.getB2B_MASTERS().getPOP().get(i));

                                            if (baseModelPop != null) {
                                                for (int m = 0; m < baseModelPop.size(); m++) {
                                                    if (baseModelPop.get(m).getChilds() != null) {
                                                        for (int n = 0; n < baseModelPop.get(m).getChilds().length; n++) {
                                                            if (obj.getB2B_MASTERS().getPROFILE() != null) {
                                                                for (int l = 0; l < obj.getB2B_MASTERS().getPROFILE().size(); l++) {
                                                                    if (baseModelPop.get(m).getChilds()[n].getCode().equals(obj.getB2B_MASTERS().getPROFILE().get(l).getCode())) {
                                                                        baseModelProfile.add(obj.getB2B_MASTERS().getPROFILE().get(l));
                                                                    }
                                                                }
                                                            }
                                                            if (obj.getB2B_MASTERS().getTESTS() != null) {
                                                                for (int l = 0; l < obj.getB2B_MASTERS().getTESTS().size(); l++) {
                                                                    if (baseModelPop.get(m).getChilds()[n].getCode().equals(obj.getB2B_MASTERS().getTESTS().get(l).getCode())) {
                                                                        baseModelTest.add(obj.getB2B_MASTERS().getTESTS().get(l));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }


                                            if (baseModelProfile != null) {
                                                b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                if (baseModelTest != null) {
                                                    b2B_mastersMainModel.setTESTS(baseModelTest);
                                                    onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                    callAdapter(onlySelected);
                                                }
                                            }


                                            if (baseModelPop != null) {
                                                b2B_mastersMainModel.setPOP(baseModelPop);
                                                if (baseModelProfile != null) {
                                                    b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                    if (baseModelTest != null) {
                                                        b2B_mastersMainModel.setTESTS(baseModelTest);
                                                        onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                        callAdapter(onlySelected);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }


                        if (obj.getB2B_MASTERS().getPROFILE().size() != 0 && !obj.getB2B_MASTERS().getPROFILE().isEmpty()) {
                            for (int i = 0; i < obj.getB2B_MASTERS().getPROFILE().size(); i++) {
                                for (int j = 0; j < obj.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length; j++) {
                                    for (int k = 0; k < sample_type_array.size(); k++) {
                                        if (obj.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getCode().equals(sample_type_array.get(k))) {
                                            baseModelProfile.add(obj.getB2B_MASTERS().getPROFILE().get(i));


                                            if (baseModelProfile != null) {
                                                for (int l = 0; l < baseModelProfile.size(); l++) {
                                                    if (baseModelProfile.get(l).getChilds() != null) {
                                                        for (int m = 0; m < baseModelProfile.get(l).getChilds().length; m++) {
                                                            for (int r = 0; r < obj.getB2B_MASTERS().getTESTS().size(); r++) {
                                                                if (baseModelProfile.get(l).getChilds()[m].getCode().equals(obj.getB2B_MASTERS().getTESTS().get(r).getCode())) {//
                                                                    baseModelTest.add(obj.getB2B_MASTERS().getTESTS().get(r));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }


                                            if (baseModelProfile != null) {
                                                b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                if (baseModelTest != null) {
                                                    b2B_mastersMainModel.setTESTS(baseModelTest);
                                                    onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                    callAdapter(onlySelected);
                                                }
                                            }

                                        }
                                    }
                                }

                            }

                        }

                        if (obj.getB2B_MASTERS().getTESTS().size() != 0 && !obj.getB2B_MASTERS().getTESTS().isEmpty()) {
                            for (int i = 0; i < obj.getB2B_MASTERS().getTESTS().size(); i++) {

                                for (int j = 0; j < obj.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length; j++) {
                                    for (int k = 0; k < sample_type_array.size(); k++) {

                                        if (obj.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getCode().equals(sample_type_array.get(k))) {
                                            baseModelTest.add(obj.getB2B_MASTERS().getTESTS().get(i));
                                            if (baseModelTest != null) {
                                                b2B_mastersMainModel.setTESTS(baseModelTest);
                                                onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                callAdapter(onlySelected);
                                            }

                                        }
                                    }
                                }
                            }

                        }
                    }

                }
            } else {
                getAlltTestData();
            }

        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecheckAllTest.this, RecheckWoeActivity.class);
                String getSelcetdTEst = show_selected_tests_list_test_ils.getText().toString();
                i.putExtra("extraAddedTest", getSelcetdTEst);
                i.putExtra("passtoAPI", passTheTests);
                startActivity(i);
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
                    redirectToLogin(RecheckAllTest.this);
                } else {
                    Gson gson = new Gson();
                    mainModel = new MainModel();

                    dummyModel = new MainModel();
                    onlySelected = new MainModel();
                    B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();
                    // BaseModel baseModelPop = new BaseModel();
                    ArrayList<BaseModel> baseModelPop = new ArrayList<>();
                    ArrayList<BaseModel> baseModelProfile = new ArrayList<>();
                    ArrayList<BaseModel> baseModelTest = new ArrayList<>();

                    mainModel = gson.fromJson(response.toString(), MainModel.class);
                    if (TESTS != null)
                        elements = TESTS.split(",");
                    fixedLenghtList = Arrays.asList(elements);
                    sample_type_array = new ArrayList<String>(fixedLenghtList);
                    if (mainModel.getB2B_MASTERS().getPOP().size() != 0 && !mainModel.getB2B_MASTERS().getPOP().isEmpty()) {
                        for (int i = 0; i < mainModel.getB2B_MASTERS().getPOP().size(); i++) {
                            for (int j = 0; j < mainModel.getB2B_MASTERS().getPOP().get(i).getBarcodes().length; j++) {
                                for (int k = 0; k < sample_type_array.size(); k++) {
                                    if (mainModel.getB2B_MASTERS().getPOP().get(i).getBarcodes()[j].getCode().equals(sample_type_array.get(k))) {
                                        baseModelPop.add(mainModel.getB2B_MASTERS().getPOP().get(i));
                                        if (baseModelPop != null) {
                                            for (int m = 0; m < baseModelPop.size(); m++) {
                                                if (baseModelPop.get(m).getChilds() != null) {
                                                    for (int n = 0; n < baseModelPop.get(m).getChilds().length; n++) {
                                                        if (mainModel.getB2B_MASTERS().getPROFILE() != null) {
                                                            for (int l = 0; l < mainModel.getB2B_MASTERS().getPROFILE().size(); l++) {
                                                                if (baseModelPop.get(m).getChilds()[n].getCode().equals(mainModel.getB2B_MASTERS().getPROFILE().get(l).getCode())) {
                                                                    baseModelProfile.add(mainModel.getB2B_MASTERS().getPROFILE().get(l));
                                                                }
                                                            }
                                                        }
                                                        if (mainModel.getB2B_MASTERS().getTESTS() != null) {
                                                            for (int l = 0; l < mainModel.getB2B_MASTERS().getTESTS().size(); l++) {
                                                                if (baseModelPop.get(m).getChilds()[n].getCode().equals(mainModel.getB2B_MASTERS().getTESTS().get(l).getCode())) {
                                                                    baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(l));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                        if (baseModelProfile != null) {
                                            b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                            if (baseModelTest != null) {
                                                b2B_mastersMainModel.setTESTS(baseModelTest);
                                                onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                callAdapter(onlySelected);
                                            }
                                        }


                                        if (baseModelPop != null) {
                                            b2B_mastersMainModel.setPOP(baseModelPop);
                                            if (baseModelProfile != null) {
                                                b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                if (baseModelTest != null) {
                                                    b2B_mastersMainModel.setTESTS(baseModelTest);
                                                    onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                    callAdapter(onlySelected);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }

                    if (mainModel.getB2B_MASTERS().getPROFILE().size() != 0 && !mainModel.getB2B_MASTERS().getPROFILE().isEmpty()) {
                        for (int i = 0; i < mainModel.getB2B_MASTERS().getPROFILE().size(); i++) {
                            for (int j = 0; j < mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length; j++) {
                                for (int k = 0; k < sample_type_array.size(); k++) {
                                    if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getCode().equals(sample_type_array.get(k))) {
                                        baseModelProfile.add(mainModel.getB2B_MASTERS().getPROFILE().get(i));


                                        if (baseModelProfile != null) {
                                            for (int l = 0; l < baseModelProfile.size(); l++) {
                                                if (baseModelProfile.get(l).getChilds() != null) {
                                                    for (int m = 0; m < baseModelProfile.get(l).getChilds().length; m++) {
                                                        for (int r = 0; r < mainModel.getB2B_MASTERS().getTESTS().size(); r++) {
                                                            if (baseModelProfile.get(l).getChilds()[m].getCode().equals(mainModel.getB2B_MASTERS().getTESTS().get(r).getCode())) {//
                                                                baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(r));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                        if (baseModelProfile != null) {
                                            b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                            if (baseModelTest != null) {
                                                b2B_mastersMainModel.setTESTS(baseModelTest);
                                                onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                callAdapter(onlySelected);
                                            }
                                        }

                                    }
                                }
                            }

                        }

                    }

                    if (mainModel.getB2B_MASTERS().getTESTS().size() != 0 && !mainModel.getB2B_MASTERS().getTESTS().isEmpty()) {
                        for (int i = 0; i < mainModel.getB2B_MASTERS().getTESTS().size(); i++) {

                            for (int j = 0; j < mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length; j++) {
                                for (int k = 0; k < sample_type_array.size(); k++) {

                                    if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getCode().equals(sample_type_array.get(k))) {
                                        baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(i));
                                        if (baseModelTest != null) {
                                            b2B_mastersMainModel.setTESTS(baseModelTest);
                                            onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                            callAdapter(onlySelected);
                                        }

                                    }
                                }
                            }
                        }

                    }
                }

                // onlySelected
                /*GlobalClass.StoreSyncTime(RecheckAllTest.this);*/
                GlobalClass.storeProductsCachingTime(RecheckAllTest.this);
                globalClass.dismissProgressDialog();

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
        ArrayList<BaseModel> testRateMasterModels = new ArrayList<BaseModel>();

        String testtype = "";
        for (int i = 0; i < b2bmasterarraylist.size(); i++) {

            Product_Rate_MasterModel product_rate_masterModel = new Product_Rate_MasterModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_POP);
            if (b2bmasterarraylist.get(i).getPOP() != null) {
                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPOP());
                finalproductlist.add(product_rate_masterModel);
            }


            product_rate_masterModel = new Product_Rate_MasterModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_PROFILE);
            if (b2bmasterarraylist.get(i).getPROFILE() != null) {
                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPROFILE());
                finalproductlist.add(product_rate_masterModel);
            }
            product_rate_masterModel = new Product_Rate_MasterModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
            if (b2bmasterarraylist.get(i).getTESTS() != null) {
                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getTESTS());
                finalproductlist.add(product_rate_masterModel);
            }
        }
        if (finalproductlist != null) {
            expAdapter = new ExpandableTestMasterListDisplayAdapter(mActivity, finalproductlist, selectedTestsList, new EditTestExpandListAdapterCheckboxDelegate() {
                @Override
                public void onCheckChange(ArrayList<BaseModel> selectedTests) {
                    System.out.println("check changed");
                    selectedTestsList = selectedTests;
                    expAdapter.notifyDataSetChanged();
                    if (selectedTestsList.size() != 0) {
                        selected_test_display.setVisibility(View.VISIBLE);
                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);

                        showTestNmaes = new ArrayList<>();
                        getTestCodes = new ArrayList<>();
                        for (int i = 0; i < selectedTestsList.size(); i++) {
                            showTestNmaes.add(selectedTestsList.get(i).getName().toString());
                            for (int j = 0; j < selectedTestsList.get(i).getBarcodes().length; j++) {
                                getTestCodes.add(selectedTestsList.get(i).getBarcodes()[j].getCode().toString());
                            }
                        }
                        String displayslectedtest = TextUtils.join(",", showTestNmaes);
                        show_selected_tests_list_test_ils.setText(displayslectedtest);
                        passTheTests = TextUtils.join(",", getTestCodes);
                        SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                        editor.putString("testsSElected", displayslectedtest);
                        editor.commit();
                    } else if (selectedTestsList.size() == 0) {
                        selected_test_display.setVisibility(View.GONE);
                    }
                }
            });
            exp_list.setAdapter(expAdapter);
            progressDialog.dismiss();
        }

    }
}
