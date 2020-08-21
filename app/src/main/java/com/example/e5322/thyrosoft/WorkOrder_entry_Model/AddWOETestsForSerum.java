package com.example.e5322.thyrosoft.WorkOrder_entry_Model;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.ExpandableAdpaterAddWorkOrderEntry;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ProductListController;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

public class AddWOETestsForSerum extends AppCompatActivity {
    Activity mActivity = AddWOETestsForSerum.this;
    SearchView sv_testsList1;
    ExpandableListView exp_list;
    Button btn_save;
    MainModel mainModel;
    Button go_button;
    ImageView back, home;
    MainModel onlySelected;
    private GlobalClass globalClass;
    RequestQueue requestQueuepoptestILS;
    private ExpandableAdpaterAddWorkOrderEntry expAdapter;
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    private ArrayList<B2B_MASTERSMainModel> finalproductlist;
    TextView show_selected_tests_list_test_ils1;
    LinearLayout before_discount_layout2;
    private ArrayList<BaseModel> selectedTestsList;
    List<String> showTestNmaes;
    List<String> getTestCodes;
    String user, passwrd, access, api_key;
    SharedPreferences prefs, getBarcodeDetails;
    String RES_ID, ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE, SL_NO, STATUS, SU_CODE1, SU_CODE2, TESTS;
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

        initViews();

        initListner();

        days = GlobalClass.getStoreSynctime(AddWOETestsForSerum.this);

        if (GlobalClass.Dayscnt(AddWOETestsForSerum.this) >= Constants.DAYS_CNT) {
            getAlltTestData();
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");
            MainModel obj = gson.fromJson(json, MainModel.class);
            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && !GlobalClass.isNull(obj.getUSER_TYPE())) {
                    callAdapter(obj);
                }
            } else {
                getAlltTestData();
            }
        }

        getBarcodeDetails = getSharedPreferences("AddTestType", MODE_PRIVATE);
        ALERT = getBarcodeDetails.getString("ALERT", "");
        BARCODE = getBarcodeDetails.getString("BARCODE", "");
        BVT_HRS = getBarcodeDetails.getString("BVT_HRS", "");
        LABCODE = getBarcodeDetails.getString("LABCODE", "");
        PATIENT = getBarcodeDetails.getString("PATIENT", "");
        REF_DR = getBarcodeDetails.getString("REF_DR", "");
        REQUESTED_ADDITIONAL_TESTS = getBarcodeDetails.getString("REQUESTED_ADDITIONAL_TESTS", "");
        REQUESTED_ON = getBarcodeDetails.getString("REQUESTED_ON", "");
        RES_ID = getBarcodeDetails.getString("RES_ID", "");
        SDATE = getBarcodeDetails.getString("SDATE", "");
        SL_NO = getBarcodeDetails.getString("SL_NO", "");
        STATUS = getBarcodeDetails.getString("STATUS", "");
        SU_CODE1 = getBarcodeDetails.getString("SU_CODE1", "");
        SU_CODE2 = getBarcodeDetails.getString("SU_CODE2", "");
        TESTS = getBarcodeDetails.getString("TESTS", "");
        GlobalClass.showTastyToast(mActivity, "" + TESTS, 2);

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
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
                if (getSelcetdTEst.equalsIgnoreCase("Select Test")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.slt_tst_nm, 2);
                } else {
                    Intent i = new Intent(AddWOETestsForSerum.this, SummaryAddWoe.class);
                    i.putExtra("extraAddedTest", getSelcetdTEst);
                    i.putExtra("passtoAPI", passTheTests);

                    GlobalClass.showTastyToast(mActivity, passTheTests, 2);
                    startActivity(i);
                }
            }
        });

    }

    private void initListner() {

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
    }

    private void initViews() {

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

        sv_testsList1.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));
        sv_testsList1.setIconifiedByDefault(false);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

    }

    private void getAlltTestData() {
        requestQueuepoptestILS = GlobalClass.setVolleyReq(this);
        String url = Api.getAllTests + "" + api_key + "/ALL/getproducts";
        try {
            if (ControllersGlobalInitialiser.productListController != null) {
                ControllersGlobalInitialiser.productListController = null;
            }
            ControllersGlobalInitialiser.productListController = new ProductListController(mActivity, AddWOETestsForSerum.this);
            ControllersGlobalInitialiser.productListController.productListingController(url, requestQueuepoptestILS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callAdapter(MainModel mainModel) {

        b2bmasterarraylist = new ArrayList<>();
        b2bmasterarraylist.add(mainModel.B2B_MASTERS);

        ArrayList<Product_Rate_MasterModel> finalproductlist = new ArrayList<Product_Rate_MasterModel>();
        ArrayList<BaseModel> profileRateMasterModels = new ArrayList<BaseModel>();
        ArrayList<BaseModel> testRateMasterModels = new ArrayList<BaseModel>();
        B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();


        if (GlobalClass.CheckArrayList(b2bmasterarraylist)) {
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
        }

        expAdapter = new ExpandableAdpaterAddWorkOrderEntry(mActivity, finalproductlist, getTestName, selectedTestsList, new EditTestExpandListAdapterCheckboxDelegate() {
            @Override
            public void onCheckChange(ArrayList<BaseModel> selectedTests) {
                Log.v("TAG", "check changed");
                selectedTestsList = selectedTests;
                expAdapter.notifyDataSetChanged();

                if (GlobalClass.CheckArrayList(selectedTestsList)) {
                    before_discount_layout2.setVisibility(View.VISIBLE);

                    showTestNmaes = new ArrayList<>();
                    getTestCodes = new ArrayList<>();

                    if (GlobalClass.CheckArrayList(selectedTestsList)) {
                        for (int i = 0; i < selectedTestsList.size(); i++) {
                            showTestNmaes.add(selectedTestsList.get(i).getName());
                            if (GlobalClass.checkArray(selectedTestsList.get(i).getBarcodes())) {
                                for (int j = 0; j < selectedTestsList.get(i).getBarcodes().length; j++) {
                                    getTestCodes.add(selectedTestsList.get(i).getBarcodes()[j].getCode());
                                }
                            }
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
                Log.v("TAG", "check changed");
                selectedTestsList = selectedTests;
                expAdapter.notifyDataSetChanged();

            }
        });
        exp_list.setAdapter(expAdapter);
    }


    public void getProductListResponse(JSONObject response) {
        Log.e(TAG, "onResponse: RESPONSE" + response);
        String getResponse = response.optString("RESPONSE", "");
        if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
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
            prefsEditor1.commit();


            GlobalClass.storeProductsCachingTime(AddWOETestsForSerum.this);


            B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();
            ArrayList<BaseModel> baseModelProfile = new ArrayList<>();
            ArrayList<BaseModel> baseModelTest = new ArrayList<>();

            mainModel = gson.fromJson(response.toString(), MainModel.class);
            if (mainModel != null) {
                onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getPOP()))
                    if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getPROFILE()))
                        for (int i = 0; i < mainModel.getB2B_MASTERS().getPROFILE().size(); i++) {
                            if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length < 2 && mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes() != null) {
                                if (GlobalClass.checkArray(mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes())) {
                                    for (int j = 0; j < mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length; j++) {
                                        if (mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length == 1) {
                                            if (!GlobalClass.isNull(mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getSpecimen_type()) &&
                                                    mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getSpecimen_type().equals("SERUM")) {
                                                baseModelProfile.add(mainModel.getB2B_MASTERS().getPROFILE().get(i));
                                                b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getTESTS()))
                    for (int i = 0; i < mainModel.getB2B_MASTERS().getTESTS().size(); i++) {
                        if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length < 2 && mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes() != null) {
                            if (GlobalClass.checkArray(mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes())) {
                                for (int j = 0; j < mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length; j++) {
                                    if (mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length == 1) {
                                        if (!GlobalClass.isNull(mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getSpecimen_type())
                                        && mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getSpecimen_type().equals("SERUM")) {
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
        }
    }
}
