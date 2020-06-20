package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.ExpandableTestMasterListDisplayAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.MainModelForAllTests.B2B_MASTERSMainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.Product_Rate_MasterModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.Models.ULCResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class ProductLisitngActivityNew extends Activity implements RecyclerInterface {

    Activity mActivity = ProductLisitngActivityNew.this;
    EditText sv_testsList1, ulc_code_edt;
    ExpandableListView exp_list;
    RecyclerView recycler_all_test;
    boolean isSelectedDueToParent;
    Button btn_save, btn_clear;
    ArrayList<BaseModel> Selcted_Outlab_Test = new ArrayList<>();
    ImageView back, home;
    // ArrayList<BaseModel> selectedTestsList;
    MainModel mainModel;
    ULCResponseModel ulcResponseModel;
    private Global globalClass;
    RequestQueue requestQueuepoptestILS;
    SharedPreferences preferences, prefe;
    String brandName, typeName;
    String shr_brandname;
    ArrayList<BaseModel> testRateMasterModels = new ArrayList<BaseModel>();
    MainModel obj;
    ArrayList<BaseModel> filteredFiles;
    String TAG = ProductLisitngActivityNew.class.getSimpleName();
    private ExpandableTestMasterListDisplayAdapter expAdapter;
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    private ArrayList<B2B_MASTERSMainModel> finalproductlist;
    ProgressDialog progressDialog;
    TextView show_selected_tests_list_test_ils1, test_txt;
    ProgressDialog barProgressDialog;
    // List<String> showTestNmaes;
    ArrayList<String> showTestNmaes = new ArrayList<>();
    List<String> getOnlyProducts;
    String user, passwrd, access, api_key, convertDate;
    SharedPreferences prefs, pref_brand;
    SharedPreferences.Editor editor_brand;
    LinearLayout before_discount_layout2, ulc_nonulc_ll, ulc_ll, product_list_ll, ulc_code_edt_ll, ulc_woe_ll;
    TextView title;
    RadioButton ulc_radiobtn, nonulc_radiobtn;
    TextView companycost_test;
    private int totalcount;
    boolean testflag = false;
    private ArrayList<BaseModel> tempselectedTests;
    private List<String> tempselectedTests1;
    private MainModel mainModelRate;
    private String displayslectedtest;
    Button go_button, ulc_woe_btn;
    RecyclerView recycler_ulc_woe;
    ScannedBarcodeDetails scannedBarcodeDetails;
    ArrayList<ScannedBarcodeDetails> setAllTestWithBArcodeList;
    ImageView crosstoclose;
    private static Global globalData;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerBarcode;
    private AlertDialog.Builder alertDialogBuilder;
    private String parentTestCode;
    private boolean flag = true;
    private String specimenttype1;
    private int position1;
    private IntentIntegrator scanIntegrator;
    private String patientName;
    private String patientYear;
    private String patientYearType;
    private String patientGender;
    private String sampleCollectionDate;
    private String sampleCollectionTime;
    private String sr_number;
    private int pass_to_api;
    private String referenceBy;
    private String sampleCollectionPoint;
    private String sampleGivingClient;
    private String refeID;
    private String labAddress;
    int days = 0;
    private String labID;
    private String labName;
    private String btechID;
    private String campID;
    private String homeaddress;
    private String getFinalPhoneNumberToPost;
    private String outputDateStr;
    private String ageType;
    ArrayList<BarcodelistModel> barcodelists;
    BarcodelistModel barcodelist;
    private ArrayList<String> getBarcodeArrList;
    boolean flagcallonce = false;
    private DatabaseHelper myDb;
    private String getIMEINUMBER;
    private String RES_ID;
    private String barcode_patient_id;
    private String message;
    private String status;
    private String barcode_id;
    private RequestQueue sendGPSDetails;
    private String checkUlcNumber;
    private boolean flagforOnce = false;
    Button verify_ulc;
    private String version;
    ArrayList<TRFModel> trf_list = new ArrayList<>();


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poduct_lisitng);


        sv_testsList1 = (EditText) findViewById(R.id.sv_testsList1);
        ulc_code_edt = (EditText) findViewById(R.id.ulc_code_edt);
        exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        recycler_all_test = (RecyclerView) findViewById(R.id.recycler_all_test);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        go_button = (Button) findViewById(R.id.go_button);
        ulc_woe_btn = (Button) findViewById(R.id.ulc_woe_btn);
        verify_ulc = (Button) findViewById(R.id.verify_ulc);
        before_discount_layout2 = (LinearLayout) findViewById(R.id.before_discount_layout2);
        ulc_nonulc_ll = (LinearLayout) findViewById(R.id.ulc_nonulc_ll);
        ulc_ll = (LinearLayout) findViewById(R.id.ulc_ll);
        product_list_ll = (LinearLayout) findViewById(R.id.product_list_ll);
        ulc_code_edt_ll = (LinearLayout) findViewById(R.id.ulc_code_edt_ll);
        ulc_woe_ll = (LinearLayout) findViewById(R.id.ulc_woe_ll);
        ulc_radiobtn = (RadioButton) findViewById(R.id.ulc_radiobtn);
        nonulc_radiobtn = (RadioButton) findViewById(R.id.nonulc_radiobtn);
        show_selected_tests_list_test_ils1 = (TextView) findViewById(R.id.show_selected_tests_list_test_ils1);
        companycost_test = (TextView) findViewById(R.id.companycost_test);
        test_txt = (TextView) findViewById(R.id.test_txt);
        recycler_ulc_woe = (RecyclerView) findViewById(R.id.recycler_ulc_woe);

        linearLayoutManager = new LinearLayoutManager(ProductLisitngActivityNew.this);
        linearLayoutManagerBarcode = new LinearLayoutManager(ProductLisitngActivityNew.this);
        recycler_all_test.setLayoutManager(linearLayoutManager);
        recycler_ulc_woe.setLayoutManager(linearLayoutManagerBarcode);
        recycler_all_test.addItemDecoration(new DividerItemDecoration(ProductLisitngActivityNew.this, DividerItemDecoration.VERTICAL));
        recycler_all_test.setItemAnimator(new DefaultItemAnimator());


        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking


        barProgressDialog = new ProgressDialog(ProductLisitngActivityNew.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);

        before_discount_layout2.setVisibility(View.GONE);


        SharedPreferences preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("age", null);
        patientYearType = preferences.getString("ageType", null);
        patientGender = preferences.getString("gender", null);
        brandName = preferences.getString("WOEbrand", null);
        typeName = preferences.getString("woetype", null);
        sampleCollectionDate = preferences.getString("date", null);
        sampleCollectionTime = preferences.getString("sct", null);
        sr_number = preferences.getString("SR_NO", null);
        pass_to_api = Integer.parseInt(sr_number);
        referenceBy = preferences.getString("refBy", null);
        ////////////////////////////////////////////////////////////////////////
        sampleCollectionPoint = preferences.getString("labAddress", null);
        sampleGivingClient = preferences.getString("labname", null);
        ////////////////////////////////////////////////////////////////////////

        refeID = preferences.getString("refId", null);
        labAddress = preferences.getString("labAddress", null);
        labID = preferences.getString("labIDaddress", null);
        labName = preferences.getString("labname", null);
        btechID = preferences.getString("btechIDToPass", null);
        campID = preferences.getString("getcampIDtoPass", null);
        homeaddress = preferences.getString("patientAddress", null);
        getFinalPhoneNumberToPost = preferences.getString("kycinfo", null);

        pref_brand = getSharedPreferences("BRANDPREF", MODE_PRIVATE);
        if (TextUtils.isEmpty(shr_brandname)) {
            editor_brand = pref_brand.edit();
            editor_brand.putString("BRANDNAME", brandName);
            editor_brand.apply();
        } else {
            shr_brandname = pref_brand.getString("BRANDNAME", "");
        }
        shr_brandname = pref_brand.getString("BRANDNAME", "");

        final SharedPreferences getIMIE = getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", null);


        title.setText("Select Test(s)");
        Selcted_Outlab_Test = new ArrayList<>();

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
                GlobalClass.goToHome(ProductLisitngActivityNew.this);
            }
        });

        if (typeName.equalsIgnoreCase("CAMP")) {
            ulc_nonulc_ll.setVisibility(View.VISIBLE);
            ulc_ll.setVisibility(View.GONE);
            product_list_ll.setVisibility(View.GONE);
        } else {
            ulc_nonulc_ll.setVisibility(View.GONE);
            ulc_ll.setVisibility(View.GONE);
            product_list_ll.setVisibility(View.VISIBLE);
        }
        ulc_radiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ulc_ll.setVisibility(View.VISIBLE);
                ulc_woe_ll.setVisibility(View.GONE);
                product_list_ll.setVisibility(View.GONE);
            }
        });
        nonulc_radiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_list_ll.setVisibility(View.VISIBLE);
                ulc_ll.setVisibility(View.GONE);
                ulc_woe_ll.setVisibility(View.GONE);
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductLisitngActivityNew.this, ProductLisitngActivityNew.class);
                startActivity(i);
                finish();
            }
        });

        sv_testsList1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().toLowerCase();
                filteredFiles = new ArrayList<>();
                String name = "";
                String code = "";
                String product = "";

                if (testRateMasterModels.size() != 0) {
                    for (int i = 0; i < testRateMasterModels.size(); i++) {
                        final String text = testRateMasterModels.get(i).getName().toLowerCase();
                        if (testRateMasterModels.get(i).getName() != null || !testRateMasterModels.get(i).getName().equals("")) {
                            name = testRateMasterModels.get(i).getName().toLowerCase();
                        }
                        if (testRateMasterModels.get(i).getCode() != null || !testRateMasterModels.get(i).getCode().equals("")) {
                            code = testRateMasterModels.get(i).getCode().toLowerCase();
                        }
                        if (testRateMasterModels.get(i).getProduct() != null || !testRateMasterModels.get(i).getProduct().equals("")) {
                            product = testRateMasterModels.get(i).getProduct().toLowerCase();
                        }

                        if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                (code != null && code.contains(s1)) ||
                                (product != null && product.contains(s1))) {
                            String testname = testRateMasterModels.get(i).getName();
                            filteredFiles.add(testRateMasterModels.get(i));
                        }

                        ViewAllTestAdapter outLabRecyclerView = new ViewAllTestAdapter(ProductLisitngActivityNew.this, filteredFiles);
                        recycler_all_test.setAdapter(outLabRecyclerView);
                    }
                } else {
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        });

        verify_ulc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!GlobalClass.isNetworkAvailable(ProductLisitngActivityNew.this)) {
                    GlobalClass.showAlertDialog(ProductLisitngActivityNew.this);
                } else {

                }

                barProgressDialog = new ProgressDialog(ProductLisitngActivityNew.this);
                barProgressDialog.setTitle("Kindly wait ...");
                barProgressDialog.setMessage(ToastFile.processing_request);
                barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                barProgressDialog.setProgress(0);
                barProgressDialog.setMax(20);
                barProgressDialog.show();
                barProgressDialog.setCanceledOnTouchOutside(false);
                barProgressDialog.setCancelable(false);
                checkUlcNumber = ulc_code_edt.getText().toString();
                RequestQueue requestQueueULC = GlobalClass.setVolleyReq(ProductLisitngActivityNew.this);
                JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getData + api_key + "/" + checkUlcNumber + "/" + user + "/getcheckulc", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: RESPONSE" + response);


                        /*if (ProductLisitngActivityNew.this instanceof Activity) {
                            if (!((Activity) ProductLisitngActivityNew.this).isFinishing())
                                barProgressDialog.dismiss();
                        }
*/

                        GlobalClass.hideProgress(ProductLisitngActivityNew.this, barProgressDialog);

                        Gson gson = new Gson();
                        ulcResponseModel = new ULCResponseModel();
                        ulcResponseModel = gson.fromJson(response.toString(), ULCResponseModel.class);
                        ulc_woe_ll.setVisibility(View.VISIBLE);
                        if (ulcResponseModel.getULC_STATUS().equalsIgnoreCase("VALID")) {
                            ulc_woe_ll.setVisibility(View.VISIBLE);
                            test_txt.setText(ulcResponseModel.getTEST());
                            setAllTestWithBArcodeList = new ArrayList<>();

                            if (ulcResponseModel.getULC_TEST().length != 0) {
                                for (int i = 0; i < ulcResponseModel.getULC_TEST().length; i++) {

                                    scannedBarcodeDetails = new ScannedBarcodeDetails();
                                    scannedBarcodeDetails.setProducts(ulcResponseModel.getULC_TEST()[i].getTest());
                                    scannedBarcodeDetails.setSpecimen_type(ulcResponseModel.getULC_TEST()[i].getSample_type());
                                    setAllTestWithBArcodeList.add(scannedBarcodeDetails);
                                }
                            }
                            ScanBarcodeAdapter attachBarcodeAdpter = new ScanBarcodeAdapter(ProductLisitngActivityNew.this, setAllTestWithBArcodeList);
                            recycler_ulc_woe.setAdapter(attachBarcodeAdpter);
                            attachBarcodeAdpter.setOnItemClickListener(new ScanBarcodeAdapter.OnItemClickListener() {
                                @Override
                                public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                                    scanIntegrator = new IntentIntegrator(activity);
                                    if (GlobalClass.specimenttype != null) {
                                        GlobalClass.specimenttype = "";
                                    }
                                    GlobalClass.specimenttype = Specimenttype;
                                    scanIntegrator.initiateScan();
                                }
                            });

                        } else {
                            Toast.makeText(mActivity, "INVALID ULC", Toast.LENGTH_SHORT).show();
                            ulc_woe_ll.setVisibility(View.GONE);
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
                requestQueueULC.add(jsonObjectRequestPop);
                Log.e(TAG, "onCreate: URL" + jsonObjectRequestPop);
            }
        });

        ulc_woe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (setAllTestWithBArcodeList != null) {
                    for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                        if (setAllTestWithBArcodeList.get(i).getBarcode() == null) {
                            Toast.makeText(ProductLisitngActivityNew.this, ToastFile.pls_scn_br + setAllTestWithBArcodeList.get(i).getSpecimen_type(), Toast.LENGTH_SHORT).show();
                        } else {
                            myDb = new DatabaseHelper(getApplicationContext());

                            String getOnlyTime = sampleCollectionTime.substring(0, sampleCollectionTime.length() - 3);
                            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = null;
                            try {
                                date = inputFormat.parse(sampleCollectionDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            outputDateStr = outputFormat.format(date);
                            //sampleCollectionTime
                            Log.e(TAG, "fetchData: " + outputDateStr);
                            if (patientYearType.equalsIgnoreCase("Years")) {
                                ageType = "Y";
                            } else if (patientYearType.equalsIgnoreCase("Months")) {
                                ageType = "M";
                            } else if (patientYearType.equalsIgnoreCase("Days")) {
                                ageType = "D";
                            }
                            String getFulltime = sampleCollectionDate + " " + sampleCollectionTime;

                            GlobalClass.Req_Date_Req(sampleCollectionDate + " " + sampleCollectionTime, "hh:mm a", "HH:mm:ss");

                            MyPojoWoe myPojoWoe = new MyPojoWoe();
                            Woe woe = new Woe();
                            woe.setAADHAR_NO("");
                            woe.setADDRESS(labAddress);
                            woe.setAGE(patientYear);
                            woe.setAGE_TYPE(ageType);
                            woe.setALERT_MESSAGE("");
                            woe.setAMOUNT_COLLECTED(ulcResponseModel.getB2C());
                            woe.setAMOUNT_DUE(null);
                            woe.setAPP_ID(version);
                            woe.setADDITIONAL1("CPL");
                            woe.setBCT_ID("");
                            woe.setBRAND(brandName);
                            woe.setCAMP_ID(campID);
                            woe.setCONT_PERSON("");
                            woe.setCONTACT_NO("");
                            woe.setCUSTOMER_ID("");
                            woe.setDELIVERY_MODE(2);
                            woe.setEMAIL_ID("");
                            woe.setENTERED_BY(user);
                            woe.setGENDER(patientGender);
                            woe.setLAB_ADDRESS(labAddress);
                            woe.setLAB_ID("");
                            woe.setLAB_NAME("");
                            woe.setLEAD_ID("");
                            woe.setMAIN_SOURCE(user);
                            woe.setORDER_NO("");
                            woe.setOS("unknown");
                            woe.setPATIENT_NAME(patientName);
                            woe.setPINCODE("");
                            woe.setPRODUCT(ulcResponseModel.getPRODUCT());
                            woe.setPurpose("mobile application");
                            woe.setREF_DR_ID(refeID);
                            woe.setREF_DR_NAME(referenceBy);
                            woe.setREMARKS("MOBILE");
                            woe.setSPECIMEN_COLLECTION_TIME(outputDateStr + " " + GlobalClass.cutString + ".000");
                            woe.setSPECIMEN_SOURCE("");
                            woe.setSR_NO(pass_to_api);
                            woe.setSTATUS("N");
                            woe.setSUB_SOURCE_CODE(user);
                            woe.setTOTAL_AMOUNT(ulcResponseModel.getB2C());
                            woe.setTYPE(typeName);
                            woe.setWATER_SOURCE("");
                            woe.setWO_MODE("THYROSOFTLITE APP");
                            woe.setWO_STAGE(3);
                            woe.setULCcode(checkUlcNumber);
                            myPojoWoe.setWoe(woe);
                            barcodelists = new ArrayList<>();
                            getBarcodeArrList = new ArrayList<>();
                            for (int p = 0; p < setAllTestWithBArcodeList.size(); p++) {
                                barcodelist = new BarcodelistModel();
                                barcodelist.setSAMPLE_TYPE(setAllTestWithBArcodeList.get(p).getSpecimen_type());
                                barcodelist.setBARCODE(setAllTestWithBArcodeList.get(p).getBarcode());
                                getBarcodeArrList.add(setAllTestWithBArcodeList.get(p).getBarcode());
                                barcodelist.setTESTS(setAllTestWithBArcodeList.get(p).getProducts());
                                barcodelists.add(barcodelist);
                            }
                            myPojoWoe.setBarcodelistModel(barcodelists);
                            myPojoWoe.setWoe_type("WOE");
                            myPojoWoe.setApi_key(api_key);//api_key

                            Gson gson = new GsonBuilder().create();
                            String json = gson.toJson(myPojoWoe);
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (!GlobalClass.isNetworkAvailable(ProductLisitngActivityNew.this)) {
                                String barcodes = TextUtils.join(",", getBarcodeArrList);
                                Gson trfgson = new GsonBuilder().create();
                                String trfjson = trfgson.toJson(trf_list);
                                boolean isInserted = myDb.insertData(barcodes, json);
                                if (isInserted) {
                                    TastyToast.makeText(ProductLisitngActivityNew.this, ToastFile.woeSaved, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    Intent intent = new Intent(ProductLisitngActivityNew.this, SummaryActivity_New.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("tetsts", ulcResponseModel.getTEST());
                                    bundle.putString("passProdcucts", ulcResponseModel.getPRODUCT());
                                    bundle.putString("TotalAmt", ulcResponseModel.getB2C());
                                    bundle.putString("CollectedAmt", ulcResponseModel.getB2C());
                                    bundle.putParcelableArrayList("sendArraylist", setAllTestWithBArcodeList);
                                    bundle.putString("patientId", "");
                                    bundle.putString("barcodes", barcodes);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else {
                                    TastyToast.makeText(ProductLisitngActivityNew.this, ToastFile.woenotSaved, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }


                            } else {
                                if (!flagforOnce) {
                                    flagforOnce = true;
                                    barProgressDialog = new ProgressDialog(ProductLisitngActivityNew.this);
                                    barProgressDialog.setTitle("Kindly wait ...");
                                    barProgressDialog.setMessage(ToastFile.processing_request);
                                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                    barProgressDialog.setProgress(0);
                                    barProgressDialog.setMax(20);
                                    barProgressDialog.setCanceledOnTouchOutside(false);
                                    barProgressDialog.setCancelable(false);
                                    barProgressDialog.show();

                                    RequestQueue POstQue = GlobalClass.setVolleyReq(ProductLisitngActivityNew.this);
                                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {

                                                /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                    barProgressDialog.dismiss();
                                                }*/
                                              /*  if (ProductLisitngActivityNew.this instanceof Activity) {
                                                    if (!((Activity) ProductLisitngActivityNew.this).isFinishing())
                                                        barProgressDialog.dismiss();
                                                }*/

                                                GlobalClass.hideProgress(ProductLisitngActivityNew.this, barProgressDialog);
                                                Log.e(TAG, "onResponse: RESPONSE" + response);
                                                String finalJson = response.toString();
                                                JSONObject parentObjectOtp = new JSONObject(finalJson);
                                                RES_ID = parentObjectOtp.getString("RES_ID");
                                                barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                                message = parentObjectOtp.getString("message");
                                                status = parentObjectOtp.getString("status");
                                                barcode_id = parentObjectOtp.getString("barcode_id");

                                                if (status.equalsIgnoreCase("SUCCESS")) {

                                                    //after sending the woe succrssfully
                                                    TastyToast.makeText(ProductLisitngActivityNew.this, "Your WOE has been saved online !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                                    Intent intent = new Intent(ProductLisitngActivityNew.this, SummaryActivity_New.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("tetsts", ulcResponseModel.getTEST());
                                                    bundle.putString("passProdcucts", ulcResponseModel.getPRODUCT());
                                                    bundle.putString("TotalAmt", ulcResponseModel.getB2C());
                                                    bundle.putString("CollectedAmt", ulcResponseModel.getB2C());
                                                    bundle.putParcelableArrayList("sendArraylist", setAllTestWithBArcodeList);
                                                    bundle.putString("patientId", barcode_patient_id);
                                                    bundle.putString("draft", "");
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    flagforOnce = false;

                                                } else if (message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {


                                                    TastyToast.makeText(ProductLisitngActivityNew.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                    final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                                                            ProductLisitngActivityNew.this).create();

                                                    // Setting Dialog Title
                                                    alertDialog.setTitle("Update Ledger !");

                                                    // Setting Dialog Message
                                                    alertDialog.setMessage(ToastFile.update_ledger);
                                                    // Setting OK Button
                                                    alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            Intent i = new Intent(ProductLisitngActivityNew.this, Payment_Activity.class);
                                                            i.putExtra("COMEFROM", "ProductLisitngActivityNew");
                                                            startActivity(i);
                                                            /*Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                                            httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                                            startActivity(httpIntent);*/
                                                            // Write your code here to execute after dialog closed
                                                        }
                                                    });
                                                    alertDialog.show();

                                                } else {

                                                    TastyToast.makeText(ProductLisitngActivityNew.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                }

                                            } catch (JSONException e) {

                                                e.printStackTrace();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                           /* if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                barProgressDialog.dismiss();
                                            }*/
                                          /*  if (ProductLisitngActivityNew.this instanceof Activity) {
                                                if (!((Activity) ProductLisitngActivityNew.this).isFinishing())
                                                    barProgressDialog.dismiss();
                                            }*/
                                            GlobalClass.hideProgress(ProductLisitngActivityNew.this, barProgressDialog);
                                            if (error != null) {
                                            } else {

                                                System.out.println(error);
                                            }
                                        }
                                    });
                                    jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                                            150000,
                                            3,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    POstQue.add(jsonObjectRequest1);
                                    Log.e(TAG, "fetchData: URL" + jsonObjectRequest1);
                                    Log.e(TAG, "fetchData: JSON" + jsonObj);
                                } else {

                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(ProductLisitngActivityNew.this, ToastFile.allw_scan, Toast.LENGTH_SHORT).show();
                }


            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTExtdata = show_selected_tests_list_test_ils1.getText().toString();
                if (Selcted_Outlab_Test.size() == 0) {
                    Toast.makeText(mActivity, ToastFile.slt_test, Toast.LENGTH_SHORT).show();
                } else if (getTExtdata.equalsIgnoreCase("Select Test")) {
                    Toast.makeText(mActivity, ToastFile.slt_test, Toast.LENGTH_SHORT).show();
                } else {
                    int sum = 0;
                    int b2b = 0;
                    ArrayList<String> getTestNameLits = new ArrayList<>();

                    for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
                        sum = sum + Integer.parseInt(Selcted_Outlab_Test.get(i).getRate().getB2c());
                        b2b = b2b + Integer.parseInt(Selcted_Outlab_Test.get(i).getRate().getB2b());
                        if (Selcted_Outlab_Test.get(i).getType().equalsIgnoreCase("TEST")) {
                            getTestNameLits.add(Selcted_Outlab_Test.get(i).getCode());
                        } else {
                            getTestNameLits.add(Selcted_Outlab_Test.get(i).getProduct());
                        }

                    }


                    if (getTestNameLits.contains("PPBS") && getTestNameLits.contains("RBS")) {
                        showTestNmaes.remove("RANDOM BLOOD SUGAR");
                    }
                    String displayslectedtest = TextUtils.join(",", showTestNmaes);
                    show_selected_tests_list_test_ils1.setText(displayslectedtest);
                    getTExtdata = displayslectedtest;

                    String testsCodesPassing = TextUtils.join(",", getTestNameLits);
                    String payment = String.valueOf(sum);
                    String b2b_rate = String.valueOf(b2b);
                    Log.e(TAG, "b2b_rate--->" + b2b_rate);

                    Intent intent = new Intent(ProductLisitngActivityNew.this, Scan_Barcode_ILS_New.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("key", Selcted_Outlab_Test);
                    bundle.putString("payment", payment);
                    bundle.putString("writeTestName", getTExtdata);
                    bundle.putString("b2b_rate", b2b_rate);
                    bundle.putStringArrayList("TestCodesPass", getTestNameLits);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        if (GlobalClass.Dayscnt(ProductLisitngActivityNew.this) >= Constants.DAYS_CNT) {
            if (!GlobalClass.isNetworkAvailable(ProductLisitngActivityNew.this)) {
                TastyToast.makeText(ProductLisitngActivityNew.this, ToastFile.intConnection, Toast.LENGTH_SHORT, TastyToast.ERROR);
            } else {
                getAllproduct();
            }
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            final String json = appSharedPrefs.getString("MyObject", "");
            MainModel obj = gson.fromJson(json, MainModel.class);

            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                    if (shr_brandname.equalsIgnoreCase(brandName)) {
                        callAdapter(obj);
                    } else {
                        getAllproduct();
                    }

                } else {
                    if (!GlobalClass.isNetworkAvailable(ProductLisitngActivityNew.this)) {
                        TastyToast.makeText(ProductLisitngActivityNew.this, ToastFile.intConnection, Toast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        getAllproduct();
                    }
                }
            } else {
                if (!GlobalClass.isNetworkAvailable(ProductLisitngActivityNew.this)) {
                    TastyToast.makeText(ProductLisitngActivityNew.this, ToastFile.intConnection, Toast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    getAllproduct();
                }
            }
        }
    }

    private void getAllproduct() {
        barProgressDialog = new ProgressDialog(ProductLisitngActivityNew.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        RequestQueue requestQueuepoptestILS = GlobalClass.setVolleyReq(this);
        Log.e(TAG, "Product URL --->" + Api.getAllTests + api_key + "/ALL/getproducts");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: RESPONSE" + response);

                GlobalClass.hideProgress(ProductLisitngActivityNew.this, barProgressDialog);

                String getResponse = response.optString("RESPONSE", "");

                if (TextUtils.isEmpty(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(ProductLisitngActivityNew.this);
                } else {
                    try {
                        Gson gson = new Gson();
                        mainModel = new MainModel();
                        mainModel = gson.fromJson(response.toString(), MainModel.class);
                        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(ProductLisitngActivityNew.this);
                        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                        Gson gson22 = new Gson();
                        String json22 = gson22.toJson(mainModel);
                        prefsEditor1.putString("MyObject", json22);
                        prefsEditor1.commit();


                        GlobalClass.storeProductsCachingTime(ProductLisitngActivityNew.this);
                        callAdapter(mainModel);

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
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
        Log.e(TAG, "onCreate: URL" + jsonObjectRequestPop);
    }

    private void callAdapter(MainModel mainModel) {
        b2bmasterarraylist = new ArrayList<>();
        b2bmasterarraylist.add(mainModel.B2B_MASTERS);

        String testtype = "";
        if (mainModel.B2B_MASTERS != null) {
            if (brandName.equalsIgnoreCase("TTL")) {
                for (int i = 0; i < b2bmasterarraylist.size(); i++) {
                    Product_Rate_MasterModel product_rate_masterModel = new Product_Rate_MasterModel();
                    product_rate_masterModel.setTestType(Constants.PRODUCT_POP);
                    product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPOP());
                    for (int j = 0; j < product_rate_masterModel.getTestRateMasterModels().size(); j++) {
                        testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(j));
                    }
                    product_rate_masterModel = new Product_Rate_MasterModel();
                    product_rate_masterModel.setTestType(Constants.PRODUCT_PROFILE);
                    product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPROFILE());

                    for (int k = 0; k < product_rate_masterModel.getTestRateMasterModels().size(); k++) {
                        testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(k));
                    }
                    product_rate_masterModel = new Product_Rate_MasterModel();
                    product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
                    product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getTESTS());
                    for (int l = 0; l < product_rate_masterModel.getTestRateMasterModels().size(); l++) {
                        testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(l));
                    }

                    ViewAllTestAdapter outLabRecyclerView = new ViewAllTestAdapter(ProductLisitngActivityNew.this, testRateMasterModels);
                    recycler_all_test.setAdapter(outLabRecyclerView);

                }
            } else if (brandName.equalsIgnoreCase("SMT")) {
                for (int i = 0; i < b2bmasterarraylist.size(); i++) {
                    Product_Rate_MasterModel product_rate_masterModel = new Product_Rate_MasterModel();
                    product_rate_masterModel.setTestType(Constants.SMT_TEST);
                    product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getSMT());
                    for (int j = 0; j < product_rate_masterModel.getTestRateMasterModels().size(); j++) {
                        testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(j));
                    }

                    ViewAllTestAdapter outLabRecyclerView = new ViewAllTestAdapter(ProductLisitngActivityNew.this, testRateMasterModels);
                    recycler_all_test.setAdapter(outLabRecyclerView);

                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "123: ");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                if (getBarcodeDetails.length() == 8) {
                    passBarcodeData(getBarcodeDetails);
                } else {
                    Toast.makeText(this, invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(String s) {
        boolean isbacodeduplicate = false;
        for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
            if (setAllTestWithBArcodeList.get(i).getBarcode() != null && !setAllTestWithBArcodeList.get(i).getBarcode().isEmpty()) {
                if (setAllTestWithBArcodeList.get(i).getBarcode().equalsIgnoreCase(s)) {
                    isbacodeduplicate = true;
                }
            } else {

            }
        }

        if (isbacodeduplicate == true) {
            Toast.makeText(ProductLisitngActivityNew.this, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                if (setAllTestWithBArcodeList.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                    setAllTestWithBArcodeList.get(i).setBarcode("");
                    setAllTestWithBArcodeList.get(i).setBarcode(s);
                    Log.e(TAG, "passBarcodeData: show barcode" + s);
                }
            }
        }

        recycler_ulc_woe.removeAllViews();

        ScanBarcodeAdapter attachBarcodeAdpter = new ScanBarcodeAdapter(ProductLisitngActivityNew.this, setAllTestWithBArcodeList);
        attachBarcodeAdpter.setOnItemClickListener(new ScanBarcodeAdapter.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (GlobalClass.specimenttype != null) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator.initiateScan();
            }
        });
        recycler_ulc_woe.setAdapter(attachBarcodeAdpter);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

    @Override
    public void putDataToscan(Activity activity, int position, String specimenttype) {
        specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();
    }

    class ViewAllTestAdapter extends RecyclerView.Adapter<ViewAllTestAdapter.ViewHolder> {
        Context context;
        ArrayList<BaseModel> productList;
//        ArrayList<String> showTestNmaes = new ArrayList<>();


        public ViewAllTestAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<BaseModel> testRateMasterModels) {
            this.context = productLisitngActivityNew;
            this.productList = testRateMasterModels;
        }

        @NonNull
        @Override
        public ViewAllTestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.single_ll_test_list, parent, false);
            ViewAllTestAdapter.ViewHolder vh = new ViewAllTestAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewAllTestAdapter.ViewHolder holder, final int position) {
            holder.outlab_test.setText(productList.get(position).getName());
            holder.checked.setVisibility(View.GONE);
            holder.gray_check.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);
            boolean isChecked = false;

            holder.isSelectedDueToParent = false;
            holder.parentTestCode = "";
            final BaseModel testRateMasterModel = productList.get(position);


            try {
                if (Selcted_Outlab_Test != null && Selcted_Outlab_Test.size() > 0) {

                    for (int i = 0; !isChecked && i < Selcted_Outlab_Test.size(); i++) {
                        BaseModel selectedTestModel = Selcted_Outlab_Test.get(i);

                        if (selectedTestModel.getCode().equals(testRateMasterModel.getCode())) {
                            holder.checked.setVisibility(View.VISIBLE);
                            holder.check.setVisibility(View.GONE);
                            holder.isSelectedDueToParent = false;
                            holder.parentTestCode = "";
                            isChecked = true;
                        } else if (selectedTestModel.getChilds() != null && testRateMasterModel.getChilds() != null && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                            holder.checked.setVisibility(View.GONE);
                            holder.check.setVisibility(View.GONE);
                            holder.gray_check.setVisibility(View.VISIBLE);
                            holder.isSelectedDueToParent = true;
                            holder.parentTestCode = selectedTestModel.getCode();
                            holder.parentTestname = selectedTestModel.getName();
                            isChecked = true;
                        } else {
                            try {
                                if (selectedTestModel.getChilds() != null && selectedTestModel.getChilds().length > 0) {
                                    for (BaseModel.Childs ctm : selectedTestModel.getChilds()) {
                                        if (ctm.getCode().equals(testRateMasterModel.getCode())) {
                                            holder.check.setVisibility(View.GONE);
                                            holder.checked.setVisibility(View.GONE);
                                            holder.gray_check.setVisibility(View.VISIBLE);
                                            holder.isSelectedDueToParent = true;
                                            holder.parentTestCode = selectedTestModel.getCode();
                                            holder.parentTestname = selectedTestModel.getName();
                                            isChecked = true;
                                            break;
                                        } else {
                                            holder.checked.setVisibility(View.GONE);
                                            holder.check.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {
                                    holder.checked.setVisibility(View.GONE);
                                    holder.check.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                } else {
                    holder.checked.setVisibility(View.GONE);
                    holder.check.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.parent_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //on click of blank box
                    if (holder.check.getVisibility() == View.VISIBLE) {
                        String str = "";

                        str = str + testRateMasterModel.getCode() + ",";

                        String slectedpackage = "";


                        slectedpackage = testRateMasterModel.getName();

                        if (testRateMasterModel.getCode().equalsIgnoreCase(Constants.THYROTEST)) {
                            Constants.THYRONAME = testRateMasterModel.getName();
                        }

                        tempselectedTests = new ArrayList<>();
                        tempselectedTests1 = new ArrayList<>();
                        if (testflag == false) {
                            if (testRateMasterModel.getChilds() != null) {
                                for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                    //tejas t -----------------------------
                                    for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                                        if (testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(Selcted_Outlab_Test.get(j).getCode())) {
                                            System.out.println("Cart selectedtestlist Description :" + Selcted_Outlab_Test.get(j).getName() + "Cart selectedtestlist Code :" + Selcted_Outlab_Test.get(j).getCode());
                                            tempselectedTests1.add(Selcted_Outlab_Test.get(j).getName());
                                            tempselectedTests.add(Selcted_Outlab_Test.get(j));
                                        }
                                    }
                                }
                            }

                            for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                                BaseModel selectedTestModel123 = Selcted_Outlab_Test.get(j);
                                if (selectedTestModel123.getChilds() != null && testRateMasterModel.getChilds() != null && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                    tempselectedTests1.add(Selcted_Outlab_Test.get(j).getName());
                                    tempselectedTests.add(selectedTestModel123);
                                }
                            }

                            if (tempselectedTests != null && tempselectedTests.size() > 0) {
                                HashSet<String> hashSet = new HashSet<String>();
                                hashSet.addAll(tempselectedTests1);
                                tempselectedTests1.clear();
                                tempselectedTests1.addAll(hashSet);


                                String cartproduct = TextUtils.join(",", tempselectedTests1);
                                alertDialogBuilder =  new AlertDialog.Builder(ProductLisitngActivityNew.this);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                        .setCancelable(true)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                            }
                        } else {
                            alertDialogBuilder = new AlertDialog.Builder(ProductLisitngActivityNew.this);
                            alertDialogBuilder
                                    .setMessage(Html.fromHtml("You cannot select other test if " + Constants.THYRONAME + " is selected"))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }


                        if (testRateMasterModel.getCode().equalsIgnoreCase(Constants.THYROTEST)) {
                            if (Selcted_Outlab_Test.size() > 0) {
                                alertDialogBuilder = new AlertDialog.Builder(ProductLisitngActivityNew.this);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml(Constants.THYRONAME + " can not be selected along with other test,Do you want to replace " + Constants.THYRONAME))
                                        .setCancelable(false)
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Selcted_Outlab_Test.remove(testRateMasterModel);
                                                showTestNmaes.remove(testRateMasterModel.getName());
                                                String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                                show_selected_tests_list_test_ils1.setText(displayslectedtest);
                                                notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        })

                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Selcted_Outlab_Test.clear();
                                                Constants.THYRONAME = testRateMasterModel.getName();
                                                testRateMasterModel.setName(testRateMasterModel.getName());
                                                testRateMasterModel.setCode(testRateMasterModel.getCode());
                                                testflag = true;
                                                Selcted_Outlab_Test.add(testRateMasterModel);
                                                showTestNmaes.clear();
                                                showTestNmaes.add(testRateMasterModel.getName());

                                                if (Selcted_Outlab_Test != null) {
                                                    for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
                                                        if (Selcted_Outlab_Test.get(i).getCode().contains("PPBS") &&
                                                                Selcted_Outlab_Test.get(i).getCode().contains("RBS")) {
                                                            Selcted_Outlab_Test.remove(Selcted_Outlab_Test.get(i));
                                                            showTestNmaes.remove(testRateMasterModel.getName());
                                                        }
                                                    }

                                                }
                                                String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                                show_selected_tests_list_test_ils1.setText(displayslectedtest);
                                                notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else {
                                Selcted_Outlab_Test.clear();
                                Constants.THYRONAME = testRateMasterModel.getName();
                                testRateMasterModel.setName(testRateMasterModel.getName());
                                testRateMasterModel.setCode(testRateMasterModel.getCode());
                                testflag = true;
                                Selcted_Outlab_Test.add(testRateMasterModel);
                                showTestNmaes.clear();
                                showTestNmaes.add(testRateMasterModel.getName());
                                String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                show_selected_tests_list_test_ils1.setText(displayslectedtest);
                                notifyDataSetChanged();

                            }


                        }


                        for (int i = 0; i < tempselectedTests.size(); i++) {
                            for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                                if (tempselectedTests.get(i).getCode().equalsIgnoreCase(Selcted_Outlab_Test.get(j).getCode())) {
                                    Selcted_Outlab_Test.remove(j);
                                }
                            }
                        }

                        //tejas t -----------------------------

                        if (testflag == false) {
                            Selcted_Outlab_Test.add(testRateMasterModel);
                            notifyDataSetChanged();
                        }


                        before_discount_layout2.setVisibility(View.VISIBLE);
                        showTestNmaes = new ArrayList<>();

                        for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
                            showTestNmaes.add(Selcted_Outlab_Test.get(i).getName());
                        }

                        Set<String> setString = new HashSet<String>(showTestNmaes);
                        showTestNmaes.clear();
                        showTestNmaes.addAll(setString);
                        String displayslectedtest = TextUtils.join(",", showTestNmaes);
                        show_selected_tests_list_test_ils1.setText(displayslectedtest);
                        if (displayslectedtest.equals("")) {
                            before_discount_layout2.setVisibility(View.GONE);
                        }

                    }

                    //on click of checked box
                    else if (holder.checked.getVisibility() == View.VISIBLE) {
                        if (!isSelectedDueToParent) {
                            Selcted_Outlab_Test.remove(testRateMasterModel);
                            notifyDataSetChanged();

                            before_discount_layout2.setVisibility(View.VISIBLE);
                            if (testRateMasterModel.getCode().equalsIgnoreCase(Constants.THYROTEST)) {
                                testflag = false;
                            }

                            showTestNmaes.remove(testRateMasterModel.getName());

                            String displayslectedtest = TextUtils.join(",", showTestNmaes);
                            show_selected_tests_list_test_ils1.setText(displayslectedtest);
                            if (displayslectedtest.equals("")) {
                                before_discount_layout2.setVisibility(View.GONE);
                            }
                        } else {
                            alertDialogBuilder =  new AlertDialog.Builder(ProductLisitngActivityNew.this);
                            alertDialogBuilder
                                    .setMessage(Html.fromHtml("This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode))
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView outlab_test;
            // CheckBox check;
            RelativeLayout parent_ll;
            boolean isSelectedDueToParent;
            String parentTestCode, parentTestname;
            ImageView check, checked, gray_check;

            public ViewHolder(View itemView) {
                super(itemView);
                parent_ll = (RelativeLayout) itemView.findViewById(R.id.parent_ll);
                outlab_test = (TextView) itemView.findViewById(R.id.outlab_test);
                check = (ImageView) itemView.findViewById(R.id.check);
                checked = (ImageView) itemView.findViewById(R.id.checked);
                gray_check = (ImageView) itemView.findViewById(R.id.gray_check);
            }
        }
    }

}
