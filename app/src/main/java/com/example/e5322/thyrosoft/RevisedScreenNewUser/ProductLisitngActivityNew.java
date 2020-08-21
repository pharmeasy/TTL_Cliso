package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Getcheckulc_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ProductListController;
import com.example.e5322.thyrosoft.Controller.WoeController;
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
    ArrayList<BaseModel> Selcted_Test = new ArrayList<>();
    ImageView back, home;
    MainModel mainModel;
    ULCResponseModel ulcResponseModel;
    private Global globalClass;
    SharedPreferences prefe;
    String brandName, typeName;
    String shr_brandname;
    ArrayList<BaseModel> testRateMasterModels = new ArrayList<BaseModel>();
    ArrayList<BaseModel> filteredFiles;
    String TAG = ProductLisitngActivityNew.class.getSimpleName();
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    TextView show_selected_tests_list_test_ils1, test_txt;
    ArrayList<String> showTestNmaes = new ArrayList<>();
    String user, passwrd, access, api_key, convertDate;
    SharedPreferences prefs, pref_brand;
    SharedPreferences.Editor editor_brand;
    LinearLayout before_discount_layout2, ulc_nonulc_ll, ulc_ll, product_list_ll, ulc_code_edt_ll, ulc_woe_ll;
    TextView title;
    RadioButton ulc_radiobtn, nonulc_radiobtn;
    TextView companycost_test;
    boolean testflag = false;
    private ArrayList<BaseModel> tempselectedTests;
    private List<String> tempselectedTests1;
    Button go_button, ulc_woe_btn;
    RecyclerView recycler_ulc_woe;
    ScannedBarcodeDetails scannedBarcodeDetails;
    ArrayList<ScannedBarcodeDetails> setAllTestWithBArcodeList;
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

        initViews();

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
        before_discount_layout2.setVisibility(View.GONE);


        SharedPreferences preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", "");
        patientYear = preferences.getString("age", "");
        patientYearType = preferences.getString("ageType", "");
        patientGender = preferences.getString("gender", "");
        brandName = preferences.getString("WOEbrand", "");
        typeName = preferences.getString("woetype", "");
        sampleCollectionDate = preferences.getString("date", "");
        sampleCollectionTime = preferences.getString("sct", "");
        sr_number = preferences.getString("SR_NO", "");
        pass_to_api = Integer.parseInt(sr_number);
        referenceBy = preferences.getString("refBy", "");

        sampleCollectionPoint = preferences.getString("labAddress", "");
        sampleGivingClient = preferences.getString("labname", "");


        refeID = preferences.getString("refId", "");
        labAddress = preferences.getString("labAddress", "");
        labID = preferences.getString("labIDaddress", "");
        labName = preferences.getString("labname", "");
        btechID = preferences.getString("btechIDToPass", "");
        campID = preferences.getString("getcampIDtoPass", "");
        homeaddress = preferences.getString("patientAddress", "");
        getFinalPhoneNumberToPost = preferences.getString("kycinfo", "");

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
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", "");


        GlobalClass.SetText(title, "Select Test(s)");

        Selcted_Test = new ArrayList<>();

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        if (typeName.equalsIgnoreCase("CAMP")) {
            ulc_nonulc_ll.setVisibility(View.VISIBLE);
            ulc_ll.setVisibility(View.GONE);
            product_list_ll.setVisibility(View.GONE);
        } else {
            ulc_nonulc_ll.setVisibility(View.GONE);
            ulc_ll.setVisibility(View.GONE);
            product_list_ll.setVisibility(View.VISIBLE);
        }

        initListner();

        if (GlobalClass.Dayscnt(ProductLisitngActivityNew.this) >= Constants.DAYS_CNT) {
            getAllproduct();
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            final String json = appSharedPrefs.getString("MyObject", "");
            MainModel obj = gson.fromJson(json, MainModel.class);

            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && !GlobalClass.isNull(obj.getUSER_TYPE())) {
                    if (!GlobalClass.isNull(shr_brandname) && !GlobalClass.isNull(brandName) && shr_brandname.equalsIgnoreCase(brandName)) {
                        callAdapter(obj);
                    } else {
                        getAllproduct();
                    }

                } else {
                    getAllproduct();
                }
            } else {
                getAllproduct();
            }
        }
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
                GlobalClass.goToHome(ProductLisitngActivityNew.this);
            }
        });


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

                if (GlobalClass.CheckArrayList(testRateMasterModels)) {
                    for (int i = 0; i < testRateMasterModels.size(); i++) {
                        final String text = testRateMasterModels.get(i).getName().toLowerCase();

                        if (!GlobalClass.isNull(testRateMasterModels.get(i).getName())) {
                            name = testRateMasterModels.get(i).getName().toLowerCase();
                        }
                        if (!GlobalClass.isNull(testRateMasterModels.get(i).getCode())) {
                            code = testRateMasterModels.get(i).getCode().toLowerCase();
                        }
                        if (!GlobalClass.isNull(testRateMasterModels.get(i).getProduct())) {
                            product = testRateMasterModels.get(i).getProduct().toLowerCase();
                        }

                        if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                (code != null && code.contains(s1)) ||
                                (product != null && product.contains(s1))) {
                            String testname = testRateMasterModels.get(i).getName();
                            filteredFiles.add(testRateMasterModels.get(i));
                        }

                        callAdapter(filteredFiles);
                    }
                } else {
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        });

        verify_ulc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueueULC = GlobalClass.setVolleyReq(ProductLisitngActivityNew.this);
                try {
                    if (ControllersGlobalInitialiser.getcheckulc_controller != null) {
                        ControllersGlobalInitialiser.getcheckulc_controller = null;
                    }
                    ControllersGlobalInitialiser.getcheckulc_controller = new Getcheckulc_Controller(mActivity, ProductLisitngActivityNew.this);
                    ControllersGlobalInitialiser.getcheckulc_controller.getcheckulc_controller(api_key, checkUlcNumber, user, requestQueueULC);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ulc_woe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
                    for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                        if (GlobalClass.isNull(setAllTestWithBArcodeList.get(i).getBarcode())) {
                            GlobalClass.showTastyToast(mActivity, ToastFile.pls_scn_br + setAllTestWithBArcodeList.get(i).getSpecimen_type(), 2);
                        } else {
                            myDb = new DatabaseHelper(getApplicationContext());

                            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = null;
                            try {
                                date = inputFormat.parse(sampleCollectionDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            outputDateStr = outputFormat.format(date);

                            Log.e(TAG, "fetchData: " + outputDateStr);
                            if (!GlobalClass.isNull(patientYearType) && patientYearType.equalsIgnoreCase("Years")) {
                                ageType = "Y";
                            } else if (!GlobalClass.isNull(patientYearType) && patientYearType.equalsIgnoreCase("Months")) {
                                ageType = "M";
                            } else if (!GlobalClass.isNull(patientYearType) && patientYearType.equalsIgnoreCase("Days")) {
                                ageType = "D";
                            }

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

                            if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
                                for (int p = 0; p < setAllTestWithBArcodeList.size(); p++) {
                                    barcodelist = new BarcodelistModel();
                                    barcodelist.setSAMPLE_TYPE(setAllTestWithBArcodeList.get(p).getSpecimen_type());
                                    barcodelist.setBARCODE(setAllTestWithBArcodeList.get(p).getBarcode());
                                    getBarcodeArrList.add(setAllTestWithBArcodeList.get(p).getBarcode());
                                    barcodelist.setTESTS(setAllTestWithBArcodeList.get(p).getProducts());
                                    barcodelists.add(barcodelist);
                                }
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
                                    GlobalClass.showTastyToast(mActivity, ToastFile.woeSaved, 1);

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
                                    GlobalClass.showTastyToast(mActivity, ToastFile.woenotSaved, 2);
                                }


                            } else {
                                if (!flagforOnce) {
                                    flagforOnce = true;

                                    RequestQueue POstQue = GlobalClass.setVolleyReq(ProductLisitngActivityNew.this);

                                    try {
                                        if (ControllersGlobalInitialiser.woeController != null) {
                                            ControllersGlobalInitialiser.woeController = null;
                                        }
                                        ControllersGlobalInitialiser.woeController = new WoeController(mActivity, ProductLisitngActivityNew.this);
                                        ControllersGlobalInitialiser.woeController.woeDoneController(jsonObj, POstQue);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                } else {
                    GlobalClass.showTastyToast(mActivity, ToastFile.allw_scan, 2);
                }

            }
        });

        prefe = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefe.getString("WOEbrand", null);
        typeName = prefe.getString("woetype", null);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTExtdata = show_selected_tests_list_test_ils1.getText().toString();
                if (Selcted_Test.size() == 0) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.slt_test, 2);
                } else if (getTExtdata.equalsIgnoreCase("Select Test")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.slt_test, 2);
                } else {
                    int sum = 0;
                    int b2b = 0;
                    ArrayList<String> getTestNameLits = new ArrayList<>();
                    ArrayList<String> saveLocation = new ArrayList<>();

                    if (GlobalClass.CheckArrayList(Selcted_Test)) {

                        for (int i = 0; i < Selcted_Test.size(); i++) {
                            if (!GlobalClass.isNull(Selcted_Test.get(i).getIsCPL())) {
                                if (Selcted_Test.get(i).getIsCPL().equalsIgnoreCase("1")) {
                                    saveLocation.add("CPL");
                                } else {
                                    saveLocation.add("RPL");
                                }
                            }
                            if (!GlobalClass.isNull(Selcted_Test.get(i).getType()) && Selcted_Test.get(i).getType().equalsIgnoreCase("TEST")) {
                                getTestNameLits.add(Selcted_Test.get(i).getCode());
                            } else {
                                getTestNameLits.add(Selcted_Test.get(i).getProduct());
                            }

                            if (!saveLocation.isEmpty()) {
                                if (!GlobalClass.isNull(typeName) && typeName.equalsIgnoreCase("ILS")) {
                                    if (saveLocation.contains("CPL")) {
                                        if (!GlobalClass.isNull(Selcted_Test.get(i).getRate().getCplr())) {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getCplr());
                                        } else {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getB2b());
                                        }

                                    } else {
                                        if (!GlobalClass.isNull(Selcted_Test.get(i).getRate().getCplr())) {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getRplr());
                                        } else {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getB2b());
                                        }

                                    }
                                } else {
                                    if (saveLocation.contains("CPL")) {
                                        sum = sum + Integer.parseInt(Selcted_Test.get(i).getRate().getB2c());

                                        if (!GlobalClass.isNull(Selcted_Test.get(i).getRate().getCplr())) {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getCplr());
                                        } else {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getB2b());
                                        }

                                    } else {
                                        sum = sum + Integer.parseInt(Selcted_Test.get(i).getRate().getB2c());

                                        if (!GlobalClass.isNull(Selcted_Test.get(i).getRate().getRplr())) {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getRplr());
                                        } else {
                                            b2b = b2b + Integer.parseInt(Selcted_Test.get(i).getRate().getB2b());
                                        }

                                    }
                                }
                            }

                        }
                    }

                    if (GlobalClass.CheckArrayList(getTestNameLits)) {
                        if (getTestNameLits.contains("PPBS") && getTestNameLits.contains("RBS")) {
                            showTestNmaes.remove("RANDOM BLOOD SUGAR");
                        }
                    }


                    String displayslectedtest = TextUtils.join(",", showTestNmaes);

                    GlobalClass.SetText(show_selected_tests_list_test_ils1, displayslectedtest);

                    getTExtdata = displayslectedtest;

                    String payment = String.valueOf(sum);
                    String b2b_rate = String.valueOf(b2b);
                    Log.e(TAG, "b2b_rate--->" + b2b_rate);

                    Intent intent = new Intent(ProductLisitngActivityNew.this, Scan_Barcode_ILS_New.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("key", Selcted_Test);
                    bundle.putString("payment", payment);
                    bundle.putString("writeTestName", getTExtdata);
                    bundle.putString("b2b_rate", b2b_rate);
                    bundle.putStringArrayList("TestCodesPass", getTestNameLits);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    private void initViews() {

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


    }

    private void callAdapter(ArrayList<BaseModel> filteredFiles) {
        ViewAllTestAdapter outLabRecyclerView = new ViewAllTestAdapter(ProductLisitngActivityNew.this, filteredFiles, testRateMasterModels);
        recycler_all_test.setAdapter(outLabRecyclerView);
    }

    private void getAllproduct() {

        RequestQueue requestQueuepoptestILS = GlobalClass.setVolleyReq(this);
        String URL = Api.getAllTests + api_key + "/ALL/getproducts";
        Log.e(TAG, "Product URL --->" + URL);

        try {
            if (ControllersGlobalInitialiser.productListController != null) {
                ControllersGlobalInitialiser.productListController = null;
            }
            ControllersGlobalInitialiser.productListController = new ProductListController(mActivity, ProductLisitngActivityNew.this);
            ControllersGlobalInitialiser.productListController.productListingController(URL, requestQueuepoptestILS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callAdapter(MainModel mainModel) {
        b2bmasterarraylist = new ArrayList<>();
        b2bmasterarraylist.add(mainModel.B2B_MASTERS);

        String testtype = "";
        if (mainModel != null && mainModel.B2B_MASTERS != null) {
            if (!GlobalClass.isNull(brandName) && brandName.equalsIgnoreCase("TTL")) {

                if (GlobalClass.CheckArrayList(b2bmasterarraylist)) {
                    for (int i = 0; i < b2bmasterarraylist.size(); i++) {
                        Product_Rate_MasterModel product_rate_masterModel = new Product_Rate_MasterModel();
                        product_rate_masterModel.setTestType(Constants.PRODUCT_POP);
                        product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPOP());

                        if (GlobalClass.CheckArrayList(product_rate_masterModel.getTestRateMasterModels())) {
                            for (int j = 0; j < product_rate_masterModel.getTestRateMasterModels().size(); j++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(j));
                            }
                        }


                        product_rate_masterModel = new Product_Rate_MasterModel();
                        product_rate_masterModel.setTestType(Constants.PRODUCT_PROFILE);
                        product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPROFILE());

                        if (GlobalClass.CheckArrayList(product_rate_masterModel.getTestRateMasterModels())) {
                            for (int k = 0; k < product_rate_masterModel.getTestRateMasterModels().size(); k++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(k));
                            }
                        }

                        product_rate_masterModel = new Product_Rate_MasterModel();
                        product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
                        product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getTESTS());

                        if (GlobalClass.CheckArrayList(product_rate_masterModel.getTestRateMasterModels())) {
                            for (int l = 0; l < product_rate_masterModel.getTestRateMasterModels().size(); l++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(l));
                            }
                        }


                        callAdapter(testRateMasterModels);

                    }
                }

            } else if (!GlobalClass.isNull(brandName) && brandName.equalsIgnoreCase("SMT")) {

                if (GlobalClass.CheckArrayList(b2bmasterarraylist)) {
                    for (int i = 0; i < b2bmasterarraylist.size(); i++) {
                        Product_Rate_MasterModel product_rate_masterModel = new Product_Rate_MasterModel();
                        product_rate_masterModel.setTestType(Constants.SMT_TEST);
                        product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getSMT());

                        if (GlobalClass.CheckArrayList(product_rate_masterModel.getTestRateMasterModels())) {
                            for (int j = 0; j < product_rate_masterModel.getTestRateMasterModels().size(); j++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(j));
                            }
                        }


                        callAdapter(testRateMasterModels);

                    }
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
                    GlobalClass.showTastyToast(mActivity, invalid_brcd, 2);
                }
            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(String s) {
        boolean isbacodeduplicate = false;
        if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
            for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                if (!GlobalClass.isNull(setAllTestWithBArcodeList.get(i).getBarcode())) {
                    if (!GlobalClass.isNull(s) && setAllTestWithBArcodeList.get(i).getBarcode().equalsIgnoreCase(s)) {
                        isbacodeduplicate = true;
                    }
                }
            }
        }


        if (isbacodeduplicate) {
            GlobalClass.showTastyToast(mActivity, ToastFile.duplicate_barcd, 2);
        } else {
            if (GlobalClass.CheckArrayList(setAllTestWithBArcodeList)) {
                for (int i = 0; i < setAllTestWithBArcodeList.size(); i++) {
                    if (!GlobalClass.isNull(setAllTestWithBArcodeList.get(i).getSpecimen_type()) && !GlobalClass.isNull(GlobalClass.specimenttype) && setAllTestWithBArcodeList.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                        setAllTestWithBArcodeList.get(i).setBarcode("");
                        setAllTestWithBArcodeList.get(i).setBarcode(s);
                        Log.e(TAG, "passBarcodeData: show barcode" + s);
                    }
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

    public void getULCResponse(JSONObject response) {
        Gson gson = new Gson();
        ulcResponseModel = new ULCResponseModel();
        ulcResponseModel = gson.fromJson(response.toString(), ULCResponseModel.class);
        ulc_woe_ll.setVisibility(View.VISIBLE);

        if (ulcResponseModel != null && !GlobalClass.isNull(ulcResponseModel.getULC_STATUS()) && ulcResponseModel.getULC_STATUS().equalsIgnoreCase("VALID")) {
            ulc_woe_ll.setVisibility(View.VISIBLE);
            GlobalClass.SetText(test_txt, ulcResponseModel.getTEST());
            setAllTestWithBArcodeList = new ArrayList<>();

            if (GlobalClass.checkArray(ulcResponseModel.getULC_TEST())) {
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
            GlobalClass.showTastyToast(mActivity, "INVALID ULC", 2);
            ulc_woe_ll.setVisibility(View.GONE);
        }
    }

    public void getProductListResponse(JSONObject response) {

        Log.e(TAG, "onResponse: RESPONSE" + response);
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

    public void getWoeResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            RES_ID = parentObjectOtp.getString("RES_ID");
            barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
            message = parentObjectOtp.getString("message");
            status = parentObjectOtp.getString("status");
            barcode_id = parentObjectOtp.getString("barcode_id");

            if (!GlobalClass.isNull(status) && status.equalsIgnoreCase("SUCCESS")) {

                GlobalClass.showTastyToast(mActivity, MessageConstants.WOE_DONE, 1);
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

            } else if (!GlobalClass.isNull(message) && message.equals(MessageConstants.CRDIT_LIMIT)) {
                GlobalClass.showTastyToast(mActivity, message, 2);

                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                        ProductLisitngActivityNew.this).create();

                alertDialog.setTitle("Update Ledger !");
                alertDialog.setMessage(ToastFile.update_ledger);
                alertDialog.setButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(ProductLisitngActivityNew.this, Payment_Activity.class);
                        i.putExtra("COMEFROM", "ProductLisitngActivityNew");
                        startActivity(i);

                    }
                });
                alertDialog.show();

            } else {
                GlobalClass.showTastyToast(mActivity, message, 2);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    class ViewAllTestAdapter extends RecyclerView.Adapter<ViewAllTestAdapter.ViewHolder> {
        private final ArrayList<BaseModel> AllProductArrayList;
        Context context;
        ArrayList<BaseModel> productList;

        public ViewAllTestAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<BaseModel> testRateMasterModels, ArrayList<BaseModel> totalProducts) {
            this.context = productLisitngActivityNew;
            this.productList = testRateMasterModels;
            this.AllProductArrayList = totalProducts;
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

            GlobalClass.SetText(holder.outlab_test, productList.get(position).getName());

            holder.checked.setVisibility(View.GONE);
            holder.gray_check.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);
            boolean isChecked = false;

            holder.isSelectedDueToParent = false;
            holder.parentTestCode = "";
            final BaseModel testRateMasterModel = productList.get(position);


            try {
                if (GlobalClass.CheckArrayList(Selcted_Test)) {

                    for (int i = 0; !isChecked && i < Selcted_Test.size(); i++) {
                        BaseModel selectedTestModel = Selcted_Test.get(i);

                        if (!GlobalClass.isNull(selectedTestModel.getCode()) && !GlobalClass.isNull(testRateMasterModel.getCode()) && selectedTestModel.getCode().equals(testRateMasterModel.getCode())) {
                            holder.checked.setVisibility(View.VISIBLE);
                            holder.check.setVisibility(View.GONE);
                            holder.isSelectedDueToParent = false;
                            holder.parentTestCode = "";
                            isChecked = true;
                        } else if (GlobalClass.checkArray(selectedTestModel.getChilds()) && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                            holder.checked.setVisibility(View.GONE);
                            holder.check.setVisibility(View.GONE);
                            holder.gray_check.setVisibility(View.VISIBLE);
                            holder.isSelectedDueToParent = true;
                            holder.parentTestCode = selectedTestModel.getCode();
                            holder.parentTestname = selectedTestModel.getName();
                            isChecked = true;
                        } else {
                            try {
                                if (GlobalClass.checkArray(selectedTestModel.getChilds())) {
                                    for (BaseModel.Childs ctm : selectedTestModel.getChilds()) {
                                        if (!GlobalClass.isNull(ctm.getCode()) && !GlobalClass.isNull(testRateMasterModel.getCode()) && ctm.getCode().equals(testRateMasterModel.getCode())) {
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
                        if (!GlobalClass.isNull(productList.get(position).getCode()) && !GlobalClass.isNull(Constants.CATC) && productList.get(position).getCode().equalsIgnoreCase(Constants.CATC)) {
                            boolean isCAGCA = false;

                            if (GlobalClass.CheckArrayList(Selcted_Test)) {
                                for (int i = 0; i < Selcted_Test.size(); i++) {
                                    if (Selcted_Test.get(i).getCode().equalsIgnoreCase(Constants.CAGCA)) {
                                        isCAGCA = true;
                                        break;
                                    }
                                }
                            }

                            if (isCAGCA) {
                                BaseModel ProfileToSelect = null;

                                if (GlobalClass.CheckArrayList(AllProductArrayList)) {
                                    for (int i = 0; i < AllProductArrayList.size(); i++) {
                                        if (AllProductArrayList.get(i).getCode().equalsIgnoreCase(Constants.P690)) {
                                            ProfileToSelect = AllProductArrayList.get(i);
                                            break;
                                        }
                                    }
                                }

                                if (ProfileToSelect != null) {
                                    CallCheckFunction(ProfileToSelect);
                                } else {
                                    CallCheckFunction(productList.get(position));
                                }
                            } else {
                                CallCheckFunction(productList.get(position));
                            }

                        } else if (!GlobalClass.isNull(productList.get(position).getCode()) && !GlobalClass.isNull(Constants.CAGCA) && productList.get(position).getCode().equalsIgnoreCase(Constants.CAGCA)) {
                            boolean isCATC = false;
                            if (GlobalClass.CheckArrayList(Selcted_Test)) {

                                for (int i = 0; i < Selcted_Test.size(); i++) {
                                    if (Selcted_Test.get(i).getCode().equalsIgnoreCase(Constants.CATC)) {
                                        isCATC = true;
                                        break;
                                    }
                                }
                            }

                            if (isCATC) {
                                BaseModel ProfileToSelect = null;
                                for (int i = 0; i < AllProductArrayList.size(); i++) {
                                    if (AllProductArrayList.get(i).getCode().equalsIgnoreCase(Constants.P690)) {
                                        ProfileToSelect = AllProductArrayList.get(i);
                                        break;
                                    }
                                }
                                if (ProfileToSelect != null) {
                                    CallCheckFunction(ProfileToSelect);
                                } else {
                                    CallCheckFunction(productList.get(position));
                                }
                            } else {
                                CallCheckFunction(productList.get(position));
                            }
                        } else {
                            CallCheckFunction(productList.get(position));
                        }
                    } else if (holder.checked.getVisibility() == View.VISIBLE) {//on click of checked box
                        if (!isSelectedDueToParent) {
                            Selcted_Test.remove(testRateMasterModel);
                            notifyDataSetChanged();

                            before_discount_layout2.setVisibility(View.VISIBLE);
                            if (testRateMasterModel.getCode().equalsIgnoreCase(Constants.THYROTEST)) {
                                testflag = false;
                            }

                            showTestNmaes.remove(testRateMasterModel.getName());

                            String displayslectedtest = TextUtils.join(",", showTestNmaes);

                            GlobalClass.SetText(show_selected_tests_list_test_ils1, displayslectedtest);

                            if (displayslectedtest.equals("")) {
                                before_discount_layout2.setVisibility(View.GONE);
                            }
                        } else {
                            alertDialogBuilder = new AlertDialog.Builder(ProductLisitngActivityNew.this);
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

        private void CallCheckFunction(final BaseModel testRateMasterModel) {
            String str = "";

            str = str + testRateMasterModel.getCode() + ",";

            String slectedpackage = "";


            slectedpackage = testRateMasterModel.getName();

            if (testRateMasterModel.getCode().equalsIgnoreCase(Constants.THYROTEST)) {
                Constants.THYRONAME = testRateMasterModel.getName();
            }

            tempselectedTests = new ArrayList<>();
            tempselectedTests1 = new ArrayList<>();
            if (!testflag) {
                if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                        if (GlobalClass.CheckArrayList(Selcted_Test)) {
                            for (int j = 0; j < Selcted_Test.size(); j++) {
                                if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) && !GlobalClass.isNull(Selcted_Test.get(j).getCode()) &&
                                        testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(Selcted_Test.get(j).getCode())) {
                                    Log.v(TAG, "Cart selectedtestlist Description :" + Selcted_Test.get(j).getName() + "Cart selectedtestlist Code :" + Selcted_Test.get(j).getCode());
                                    tempselectedTests1.add(Selcted_Test.get(j).getName());
                                    tempselectedTests.add(Selcted_Test.get(j));
                                }
                            }
                        }

                    }
                }

                if (GlobalClass.CheckArrayList(Selcted_Test)) {
                    for (int j = 0; j < Selcted_Test.size(); j++) {
                        BaseModel selectedTestModel123 = Selcted_Test.get(j);
                        if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds()) && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                            tempselectedTests1.add(Selcted_Test.get(j).getName());
                            tempselectedTests.add(selectedTestModel123);
                        }
                    }
                }


                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                    HashSet<String> hashSet = new HashSet<String>();
                    hashSet.addAll(tempselectedTests1);
                    tempselectedTests1.clear();
                    tempselectedTests1.addAll(hashSet);


                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                    alertDialogBuilder = new AlertDialog.Builder(ProductLisitngActivityNew.this);
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
                if (GlobalClass.CheckArrayList(Selcted_Test)) {
                    alertDialogBuilder = new AlertDialog.Builder(ProductLisitngActivityNew.this);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml(Constants.THYRONAME + MessageConstants.TESTREPLACE + Constants.THYRONAME))
                            .setCancelable(false)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Selcted_Test.remove(testRateMasterModel);
                                    showTestNmaes.remove(testRateMasterModel.getName());
                                    String displayslectedtest = TextUtils.join(",", showTestNmaes);

                                    GlobalClass.SetText(show_selected_tests_list_test_ils1, displayslectedtest);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            })

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Selcted_Test.clear();
                                    Constants.THYRONAME = testRateMasterModel.getName();
                                    testRateMasterModel.setName(testRateMasterModel.getName());
                                    testRateMasterModel.setCode(testRateMasterModel.getCode());
                                    testflag = true;
                                    Selcted_Test.add(testRateMasterModel);
                                    showTestNmaes.clear();
                                    showTestNmaes.add(testRateMasterModel.getName());

                                    if (GlobalClass.CheckArrayList(Selcted_Test)) {
                                        for (int i = 0; i < Selcted_Test.size(); i++) {
                                            if (!GlobalClass.isNull(Selcted_Test.get(i).getCode()) && Selcted_Test.get(i).getCode().contains("PPBS") &&
                                                    Selcted_Test.get(i).getCode().contains("RBS")) {
                                                Selcted_Test.remove(Selcted_Test.get(i));
                                                showTestNmaes.remove(testRateMasterModel.getName());
                                            }
                                        }

                                    }
                                    String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                    GlobalClass.SetText(show_selected_tests_list_test_ils1, displayslectedtest);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Selcted_Test.clear();
                    Constants.THYRONAME = testRateMasterModel.getName();
                    testRateMasterModel.setName(testRateMasterModel.getName());
                    testRateMasterModel.setCode(testRateMasterModel.getCode());
                    testflag = true;
                    Selcted_Test.add(testRateMasterModel);
                    showTestNmaes.clear();
                    showTestNmaes.add(testRateMasterModel.getName());
                    String displayslectedtest = TextUtils.join(",", showTestNmaes);
                    GlobalClass.SetText(show_selected_tests_list_test_ils1, displayslectedtest);
                    notifyDataSetChanged();

                }


            }


            if (GlobalClass.CheckArrayList(tempselectedTests)) {
                for (int i = 0; i < tempselectedTests.size(); i++) {
                    for (int j = 0; j < Selcted_Test.size(); j++) {
                        if (!GlobalClass.isNull(tempselectedTests.get(i).getCode()) && !GlobalClass.isNull(Selcted_Test.get(j).getCode()) && tempselectedTests.get(i).getCode().equalsIgnoreCase(Selcted_Test.get(j).getCode())) {
                            Selcted_Test.remove(j);
                        }
                    }
                }
            }


            if (!testflag) {
                Selcted_Test.add(testRateMasterModel);
                notifyDataSetChanged();
            }


            before_discount_layout2.setVisibility(View.VISIBLE);
            showTestNmaes = new ArrayList<>();

            if (GlobalClass.CheckArrayList(Selcted_Test)) {
                for (int i = 0; i < Selcted_Test.size(); i++) {
                    showTestNmaes.add(Selcted_Test.get(i).getName());
                }
            }

            Set<String> setString = new HashSet<String>(showTestNmaes);
            showTestNmaes.clear();
            showTestNmaes.addAll(setString);
            String displayslectedtest = TextUtils.join(",", showTestNmaes);
            GlobalClass.SetText(show_selected_tests_list_test_ils1, displayslectedtest);
            if (displayslectedtest.equals("")) {
                before_discount_layout2.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView outlab_test;
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
