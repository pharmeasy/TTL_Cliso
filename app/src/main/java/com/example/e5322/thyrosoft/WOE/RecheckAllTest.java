package com.example.e5322.thyrosoft.WOE;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.ExpandableTestMasterListDisplayAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ProductListController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.EditTestExpandListAdapterCheckboxDelegate;
import com.example.e5322.thyrosoft.MainModelForAllTests.B2B_MASTERSMainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.Product_Rate_MasterModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

public class RecheckAllTest extends AppCompatActivity {

    String[] elements = new String[100];
    Activity mActivity = RecheckAllTest.this;
    SearchView sv_testsList1;
    ExpandableListView exp_list;
    Button btn_save;
    MainModel mainModel;
    MainModel dummyModel;
    MainModel onlySelected;
    private GlobalClass globalClass;
    RequestQueue requestQueuepoptestILS;
    private ExpandableTestMasterListDisplayAdapter expAdapter;
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    TextView show_selected_tests_list_test_ils;
    LinearLayout selected_test_display, lineargetselectedtestforILS;
    private ArrayList<BaseModel> selectedTestsList;
    List<String> showTestNmaes;
    List<String> getTestCodes;
    List<String> fixedLenghtList;
    ArrayList<String> sample_type_array;
    String TAG = RecheckWoeActivity.class.getSimpleName().toString();
    ImageView back, home;
    String user, passwrd, access, api_key;
    SharedPreferences prefs, getBarcodeDetails;
    String RES_ID, ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE, SL_NO, STATUS, SU_CODE1, SU_CODE2, TESTS;
    TextView title;
    private String passTheTests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recheck_all_test);
        mActivity = RecheckAllTest.this;
        globalClass = new GlobalClass(this);

        initView();

        if (GlobalClass.Dayscnt(RecheckAllTest.this) >= Constants.DAYS_CNT) {
            getAlltTestData();
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");

            MainModel obj = gson.fromJson(json, MainModel.class);

            onlySelected = new MainModel();
            if (!GlobalClass.isNull(TESTS)) elements = TESTS.split(",");
            fixedLenghtList = Arrays.asList(elements);
            sample_type_array = new ArrayList<String>(fixedLenghtList);

            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && !GlobalClass.isNull(obj.getUSER_TYPE())) {
                    B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();
                    onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                    ArrayList<BaseModel> baseModelPop = new ArrayList<>();
                    ArrayList<BaseModel> baseModelProfile = new ArrayList<>();
                    ArrayList<BaseModel> baseModelTest = new ArrayList<>();

                    if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getPOP())) {
                        for (int i = 0; i < obj.getB2B_MASTERS().getPOP().size(); i++) {
                            if (GlobalClass.checkArray(obj.getB2B_MASTERS().getPOP().get(i).getBarcodes())) {
                                for (int j = 0; j < obj.getB2B_MASTERS().getPOP().get(i).getBarcodes().length; j++) {
                                    if (GlobalClass.CheckArrayList(sample_type_array)) {
                                        for (int k = 0; k < sample_type_array.size(); k++) {
                                            if (!GlobalClass.isNull(obj.getB2B_MASTERS().getPOP().get(i).getBarcodes()[j].getCode()) &&
                                                    !GlobalClass.isNull(sample_type_array.get(k)) &&
                                                    obj.getB2B_MASTERS().getPOP().get(i).getBarcodes()[j].getCode().equalsIgnoreCase(sample_type_array.get(k))) {
                                                baseModelPop.add(obj.getB2B_MASTERS().getPOP().get(i));

                                                if (GlobalClass.CheckArrayList(baseModelPop)) {
                                                    for (int m = 0; m < baseModelPop.size(); m++) {
                                                        if (GlobalClass.checkArray(baseModelPop.get(m).getChilds())) {
                                                            for (int n = 0; n < baseModelPop.get(m).getChilds().length; n++) {
                                                                if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getPROFILE())) {
                                                                    for (int l = 0; l < obj.getB2B_MASTERS().getPROFILE().size(); l++) {
                                                                        if (!GlobalClass.isNull(baseModelPop.get(m).getChilds()[n].getCode()) &&
                                                                                !GlobalClass.isNull(obj.getB2B_MASTERS().getPROFILE().get(l).getCode()) &&
                                                                                baseModelPop.get(m).getChilds()[n].getCode().equalsIgnoreCase(obj.getB2B_MASTERS().getPROFILE().get(l).getCode())) {
                                                                            baseModelProfile.add(obj.getB2B_MASTERS().getPROFILE().get(l));
                                                                        }
                                                                    }
                                                                }
                                                                if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getTESTS())) {
                                                                    for (int l = 0; l < obj.getB2B_MASTERS().getTESTS().size(); l++) {
                                                                        if (!GlobalClass.isNull(baseModelPop.get(m).getChilds()[n].getCode()) &&
                                                                                !GlobalClass.isNull(obj.getB2B_MASTERS().getTESTS().get(l).getCode()) &&
                                                                                baseModelPop.get(m).getChilds()[n].getCode().equalsIgnoreCase(obj.getB2B_MASTERS().getTESTS().get(l).getCode())) {
                                                                            baseModelTest.add(obj.getB2B_MASTERS().getTESTS().get(l));
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }


                                                if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                                    b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                    if (GlobalClass.CheckArrayList(baseModelTest)) {
                                                        b2B_mastersMainModel.setTESTS(baseModelTest);
                                                        onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                        callAdapter(onlySelected);
                                                    }
                                                }


                                                if (GlobalClass.CheckArrayList(baseModelPop)) {
                                                    b2B_mastersMainModel.setPOP(baseModelPop);
                                                    if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                                        b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                        if (GlobalClass.CheckArrayList(baseModelTest)) {
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


                        }

                    }


                    if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getPROFILE())) {
                        for (int i = 0; i < obj.getB2B_MASTERS().getPROFILE().size(); i++) {
                            if (GlobalClass.checkArray(obj.getB2B_MASTERS().getPROFILE().get(i).getBarcodes())) {
                                for (int j = 0; j < obj.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length; j++) {
                                    if (GlobalClass.CheckArrayList(sample_type_array)) {
                                        for (int k = 0; k < sample_type_array.size(); k++) {

                                            if (!GlobalClass.isNull(obj.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getCode()) &&
                                                    !GlobalClass.isNull(sample_type_array.get(k)) &&
                                                    obj.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getCode().equalsIgnoreCase(sample_type_array.get(k))) {
                                                baseModelProfile.add(obj.getB2B_MASTERS().getPROFILE().get(i));

                                                if (GlobalClass.CheckArrayList(baseModelProfile)) {

                                                    for (int l = 0; l < baseModelProfile.size(); l++) {
                                                        if (GlobalClass.checkArray(baseModelProfile.get(l).getChilds())) {
                                                            if (baseModelProfile.get(l).getChilds() != null) {
                                                                for (int m = 0; m < baseModelProfile.get(l).getChilds().length; m++) {
                                                                    if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getTESTS())) {
                                                                        for (int r = 0; r < obj.getB2B_MASTERS().getTESTS().size(); r++) {
                                                                            if (!GlobalClass.isNull(baseModelProfile.get(l).getChilds()[m].getCode()) &&
                                                                                    !GlobalClass.isNull(obj.getB2B_MASTERS().getTESTS().get(r).getCode()) &&
                                                                                    baseModelProfile.get(l).getChilds()[m].getCode().equalsIgnoreCase(obj.getB2B_MASTERS().getTESTS().get(r).getCode())) {//
                                                                                baseModelTest.add(obj.getB2B_MASTERS().getTESTS().get(r));
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }

                                                    }
                                                }


                                                if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                                    b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                    if (GlobalClass.CheckArrayList(baseModelTest)) {
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

                    }

                    if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getTESTS())) {
                        for (int i = 0; i < obj.getB2B_MASTERS().getTESTS().size(); i++) {

                            if (GlobalClass.checkArray(obj.getB2B_MASTERS().getTESTS().get(i).getBarcodes())) {

                                for (int j = 0; j < obj.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length; j++) {
                                    if (GlobalClass.CheckArrayList(sample_type_array)) {
                                        for (int k = 0; k < sample_type_array.size(); k++) {

                                            if (!GlobalClass.isNull(obj.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getCode()) &&
                                                    !GlobalClass.isNull(sample_type_array.get(k)) &&
                                                    obj.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getCode().equalsIgnoreCase(sample_type_array.get(k))) {
                                                baseModelTest.add(obj.getB2B_MASTERS().getTESTS().get(i));

                                                if (GlobalClass.CheckArrayList(baseModelTest)) {
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


                }
            } else {
                getAlltTestData();
            }

        }


        initListner();
    }

    private void initListner() {

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
    }

    private void initView() {
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

        getBarcodeDetails = getSharedPreferences("RecheckTestType", MODE_PRIVATE);
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

        GlobalClass.SetText(title, "Test List");

        selectedTestsList = new ArrayList<>();
        sv_testsList1.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));
        sv_testsList1.setIconifiedByDefault(false);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

    }

    private void getAlltTestData() {
        requestQueuepoptestILS = Volley.newRequestQueue(this);

        String url = Api.getAllTests + "" + api_key + "/ALL/getproducts";
        try {
            if (ControllersGlobalInitialiser.productListController != null) {
                ControllersGlobalInitialiser.productListController = null;
            }
            ControllersGlobalInitialiser.productListController = new ProductListController(mActivity, RecheckAllTest.this);
            ControllersGlobalInitialiser.productListController.productListingController(url, requestQueuepoptestILS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callAdapter(MainModel mainModel) {

        b2bmasterarraylist = new ArrayList<>();
        b2bmasterarraylist.add(mainModel.B2B_MASTERS);

        ArrayList<Product_Rate_MasterModel> finalproductlist = new ArrayList<Product_Rate_MasterModel>();

        String testtype = "";

        if (GlobalClass.CheckArrayList(b2bmasterarraylist)) {
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
        }

        if (GlobalClass.CheckArrayList(finalproductlist)) {

            expAdapter = new ExpandableTestMasterListDisplayAdapter(mActivity, finalproductlist, selectedTestsList, new EditTestExpandListAdapterCheckboxDelegate() {
                @Override
                public void onCheckChange(ArrayList<BaseModel> selectedTests) {
                    Log.v(TAG, "check changed");
                    selectedTestsList = selectedTests;
                    expAdapter.notifyDataSetChanged();
                    if (GlobalClass.CheckArrayList(selectedTestsList)) {
                        selected_test_display.setVisibility(View.VISIBLE);
                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);

                        showTestNmaes = new ArrayList<>();
                        getTestCodes = new ArrayList<>();

                        if (GlobalClass.CheckArrayList(selectedTestsList)) {
                            for (int i = 0; i < selectedTestsList.size(); i++) {
                                showTestNmaes.add(selectedTestsList.get(i).getName().toString());
                                if (GlobalClass.checkArray(selectedTestsList.get(i).getBarcodes())) {
                                    for (int j = 0; j < selectedTestsList.get(i).getBarcodes().length; j++) {
                                        getTestCodes.add(selectedTestsList.get(i).getBarcodes()[j].getCode().toString());
                                    }
                                }

                            }
                        }


                        String displayslectedtest = TextUtils.join(",", showTestNmaes);

                        GlobalClass.SetText(show_selected_tests_list_test_ils, displayslectedtest);
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
        }

    }

    public void getProductListResponse(JSONObject response) {

        Log.e(TAG, "onResponse: RESPONSE" + response);
        String getResponse = response.optString("RESPONSE", "");
        if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
            redirectToLogin(RecheckAllTest.this);
        } else {
            Gson gson = new Gson();
            mainModel = new MainModel();

            dummyModel = new MainModel();
            onlySelected = new MainModel();
            B2B_MASTERSMainModel b2B_mastersMainModel = new B2B_MASTERSMainModel();

            ArrayList<BaseModel> baseModelPop = new ArrayList<>();
            ArrayList<BaseModel> baseModelProfile = new ArrayList<>();
            ArrayList<BaseModel> baseModelTest = new ArrayList<>();

            mainModel = gson.fromJson(response.toString(), MainModel.class);
            if (!GlobalClass.isNull(TESTS))
                elements = TESTS.split(",");
            fixedLenghtList = Arrays.asList(elements);
            sample_type_array = new ArrayList<String>(fixedLenghtList);

            if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getPOP())) {
                for (int i = 0; i < mainModel.getB2B_MASTERS().getPOP().size(); i++) {
                    if (GlobalClass.checkArray(mainModel.getB2B_MASTERS().getPOP().get(i).getBarcodes())) {
                        for (int j = 0; j < mainModel.getB2B_MASTERS().getPOP().get(i).getBarcodes().length; j++) {
                            if (GlobalClass.CheckArrayList(sample_type_array)) {
                                for (int k = 0; k < sample_type_array.size(); k++) {

                                    if (!GlobalClass.isNull(mainModel.getB2B_MASTERS().getPOP().get(i).getBarcodes()[j].getCode()) &&
                                            !GlobalClass.isNull(sample_type_array.get(k)) &&
                                    mainModel.getB2B_MASTERS().getPOP().get(i).getBarcodes()[j].getCode().equalsIgnoreCase(sample_type_array.get(k))) {
                                        baseModelPop.add(mainModel.getB2B_MASTERS().getPOP().get(i));
                                        if (GlobalClass.CheckArrayList(baseModelPop)) {
                                            for (int m = 0; m < baseModelPop.size(); m++) {
                                                if (GlobalClass.checkArray(baseModelPop.get(m).getChilds())) {
                                                    for (int n = 0; n < baseModelPop.get(m).getChilds().length; n++) {
                                                        if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getPROFILE())) {
                                                            for (int l = 0; l < mainModel.getB2B_MASTERS().getPROFILE().size(); l++) {
                                                                if (!GlobalClass.isNull(baseModelPop.get(m).getChilds()[n].getCode()) &&
                                                                        !GlobalClass.isNull(mainModel.getB2B_MASTERS().getPROFILE().get(l).getCode()) &&
                                                                        baseModelPop.get(m).getChilds()[n].getCode().equalsIgnoreCase(mainModel.getB2B_MASTERS().getPROFILE().get(l).getCode())) {
                                                                    baseModelProfile.add(mainModel.getB2B_MASTERS().getPROFILE().get(l));
                                                                }
                                                            }
                                                        }
                                                        if (!GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getTESTS())) {
                                                            for (int l = 0; l < mainModel.getB2B_MASTERS().getTESTS().size(); l++) {
                                                                if (!GlobalClass.isNull(baseModelPop.get(m).getChilds()[n].getCode()) &&
                                                                        !GlobalClass.isNull(mainModel.getB2B_MASTERS().getTESTS().get(l).getCode()) &&
                                                                        baseModelPop.get(m).getChilds()[n].getCode().equals(mainModel.getB2B_MASTERS().getTESTS().get(l).getCode())) {
                                                                    baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(l));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }


                                        if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                            b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                            if (GlobalClass.CheckArrayList(baseModelTest)) {
                                                b2B_mastersMainModel.setTESTS(baseModelTest);
                                                onlySelected.setB2B_MASTERS(b2B_mastersMainModel);
                                                callAdapter(onlySelected);
                                            }
                                        }


                                        if (GlobalClass.CheckArrayList(baseModelPop)) {
                                            b2B_mastersMainModel.setPOP(baseModelPop);
                                            if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                                b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                                if (!GlobalClass.CheckArrayList(baseModelTest)) {
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
                }

            }

            if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getPROFILE())) {

                for (int i = 0; i < mainModel.getB2B_MASTERS().getPROFILE().size(); i++) {
                    if (GlobalClass.checkArray(mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes())){
                        for (int j = 0; j < mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes().length; j++) {
                           if (GlobalClass.CheckArrayList(sample_type_array)){
                               for (int k = 0; k < sample_type_array.size(); k++) {
                                   if (!GlobalClass.isNull(mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getCode()) &&
                                           !GlobalClass.isNull(sample_type_array.get(k)) &&
                                           mainModel.getB2B_MASTERS().getPROFILE().get(i).getBarcodes()[j].getCode().equalsIgnoreCase(sample_type_array.get(k))) {
                                       baseModelProfile.add(mainModel.getB2B_MASTERS().getPROFILE().get(i));


                                       if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                           for (int l = 0; l < baseModelProfile.size(); l++) {
                                               if (!GlobalClass.checkArray(baseModelProfile.get(l).getChilds())) {
                                                   for (int m = 0; m < baseModelProfile.get(l).getChilds().length; m++) {
                                                       if (GlobalClass.CheckArrayList( mainModel.getB2B_MASTERS().getTESTS())){
                                                           for (int r = 0; r < mainModel.getB2B_MASTERS().getTESTS().size(); r++) {
                                                               if (!GlobalClass.isNull(baseModelProfile.get(l).getChilds()[m].getCode()) &&
                                                                       !GlobalClass.isNull(mainModel.getB2B_MASTERS().getTESTS().get(r).getCode()) &&
                                                                       baseModelProfile.get(l).getChilds()[m].getCode().equalsIgnoreCase(mainModel.getB2B_MASTERS().getTESTS().get(r).getCode())) {//
                                                                   baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(r));
                                                               }
                                                           }
                                                       }

                                                   }
                                               }
                                           }
                                       }


                                       if (GlobalClass.CheckArrayList(baseModelProfile)) {
                                           b2B_mastersMainModel.setPROFILE(baseModelProfile);
                                           if (GlobalClass.CheckArrayList(baseModelTest)) {
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

            }

            if (GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getTESTS())) {
                for (int i = 0; i < mainModel.getB2B_MASTERS().getTESTS().size(); i++) {

                    if (GlobalClass.checkArray(mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes())){
                        for (int j = 0; j < mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes().length; j++) {
                            if (GlobalClass.CheckArrayList(sample_type_array)){
                                for (int k = 0; k < sample_type_array.size(); k++) {
                                    if (!GlobalClass.isNull(mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getCode()) &&
                                            !GlobalClass.isNull(sample_type_array.get(k)) &&
                                            mainModel.getB2B_MASTERS().getTESTS().get(i).getBarcodes()[j].getCode().equalsIgnoreCase(sample_type_array.get(k))) {
                                        baseModelTest.add(mainModel.getB2B_MASTERS().getTESTS().get(i));
                                        if (GlobalClass.CheckArrayList(baseModelTest)) {
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
        }

        GlobalClass.storeProductsCachingTime(RecheckAllTest.this);

    }
}
