package com.example.e5322.thyrosoft.WOE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcodeOutlabs;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Set;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;


public class Scan_Barcode_Outlabs extends AppCompatActivity {
    Button serum, edta, outlab_barcode;
    private static String stringofconvertedTime;
    SharedPreferences prefs, sharedPrefe;
    private static String cutString;
    RequestQueue barcodeDetailsdata, POstQue;
    String testName, getB2Brate, outTestToSend, getBarcodeDetails, testsData;
    EditText enterAmt, enter_barcode, reenter;
    String TAG = Scan_Barcode_Outlabs.class.getSimpleName();
    Button next;
    ScannedBarcodeDetails scannedBarcodeDetails;
    ArrayList<Outlabdetails_OutLab> Globaly_Outlab_details = new ArrayList<>();
    LinearLayout sample_type_linear;
    ArrayList<String> getUniquespecimenttype;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    TextView show_selected_tests_data, setAmt, title;
    LinearLayoutManager linearLayoutManager;
    IntentIntegrator scanIntegrator;
    private MyPojo myPojo;
    LinearLayout barcodescanninglist;
    BaseModel.Barcodes[] barcodes;
    private SpinnerDialog spinnerDialog;
    Spinner sample_type_spinner;
    public static ArrayList<String> labAlerts;
    RecyclerView recycler_barcode;
    private ArrayList<String> temparraylist;
    private ArrayList<String> getProducts;
    public String specimenttype1;
    TextView lab_alert_spin;
    private AdapterBarcodeOutlabs adapterBarcodeOutlabs;
    public int position1 = 0;
    SharedPreferences preferences, prefe;
    ImageView img_edt, setback;
    ProgressDialog progressDialog;
    LinearLayout manualbarcodelayout, scanBarcode, amt_collected_and_total_amt_ll;
    String user, passwrd, access, api_key, typeName, brandName, ERROR, RES_ID, barcode, response1, barcode_patient_id, afterBarcode, storeResponse, barcodeNumber, displayslectedtest, getSampleType;
    ArrayList<String> getProductCode;
    String productName, showtest;
    ArrayList<String> testToPass;
    private boolean flag = true;
    private TextView companycost_test;
    private String currentText;
    private String barcodeDetailsToStore;
    private String getOnlyBrcode;
    private String getTypeName;
    private ImageView back;
    private ImageView element1_iv;
    private ImageView home;
    private String setbarcode;
    private int collectedAmt;
    private int totalAmount;
    private String patientName, patientYearType, status;
    private String patientYear, patientGender;
    private String sampleCollectionDate, sampleCollectionTime;
    private String referenceBy, sampleCollectionPoint, sampleGivingClient, getFinalSrNO;
    private String refeID;
    private String ageType;
    private String labAddress;
    private String labID;
    private String labName;
    private String amountPaid;
    private String btechID;
    private String campID;
    private String homeaddress;
    private String getFinalAddressToPost;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String getPincode;
    private String sr_number;
    private String versionNameTopass;
    private int versionCode;
    BarcodelistModel barcodelist;
    private int pass_to_api;
    ProgressDialog barProgressDialog;
    private String outputDateStr;
    ArrayList<com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel> barcodelists;
    private String message;
    private String lab_alert_pass_toApi;
    private String passProducts;
    private ArrayList<String> getBarcodeArrList;
    private boolean flagcallonce = false;
    private DatabaseHelper myDb;
    private String barcode_id;
    private Global globalClass;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);

        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        img_edt = (ImageView) findViewById(R.id.img_edt);
        back = (ImageView) findViewById(R.id.back);
        element1_iv = (ImageView) findViewById(R.id.element1_iv);
        home = (ImageView) findViewById(R.id.home);
        setback = (ImageView) findViewById(R.id.setback);
        setAmt = (TextView) findViewById(R.id.setAmt);
        companycost_test = (TextView) findViewById(R.id.companycost_test);
        title = (TextView) findViewById(R.id.title);
        enterAmt = (EditText) findViewById(R.id.enterAmt);
        reenter = (EditText) findViewById(R.id.reenter);
        enter_barcode = (EditText) findViewById(R.id.enter_barcode);
        next = (Button) findViewById(R.id.next);
        outlab_barcode = (Button) findViewById(R.id.outlab_barcode);
        sample_type_linear = (LinearLayout) findViewById(R.id.sample_type_linear);
        manualbarcodelayout = (LinearLayout) findViewById(R.id.manualbarcodelayout);
        barcodescanninglist = (LinearLayout) findViewById(R.id.barcodescanninglist);
        scanBarcode = (LinearLayout) findViewById(R.id.scanBarcode);
        amt_collected_and_total_amt_ll = (LinearLayout) findViewById(R.id.amt_collected_and_total_amt_ll);
        sample_type_spinner = (Spinner) findViewById(R.id.sample_type_spinner);
        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
        outlab_barcode.setVisibility(View.VISIBLE);
        sample_type_linear.setVisibility(View.VISIBLE);
        barcodescanninglist.setVisibility(View.GONE);
        recycler_barcode.setVisibility(View.GONE);

        enter_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        if (globalClass.checkForApi21()) {    Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));}


        Bundle bundle1 = getIntent().getExtras();
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionNameTopass = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Scan_Barcode_Outlabs.this);
            }
        });

        myDb = new DatabaseHelper(Scan_Barcode_Outlabs.this);
        Intent intent = getIntent();
        getTypeName = intent.getStringExtra("getTypeName");

        if (bundle1 != null) {
            Globaly_Outlab_details = bundle1.getParcelableArrayList("getOutlablist");
            testsData = bundle1.getString("selectedTest");
            Log.e(TAG, "onCreate: size " + Globaly_Outlab_details.size());

            ArrayList<String> getProducts = new ArrayList<>();
            getProductCode = new ArrayList<>();
            for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                getProductCode.add(Globaly_Outlab_details.get(i).getProduct());
                getProducts.add(Globaly_Outlab_details.get(i).getProduct());
                displayslectedtest = TextUtils.join(",", getProductCode);
                passProducts = TextUtils.join(",", getProducts);

            }

            show_selected_tests_data.setText(testsData);
        } else {
            Log.e(TAG, "onCreate: null");
        }

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(Scan_Barcode_Outlabs.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();
        if (myPojo.getMASTERS().getLAB_ALERTS() != null) {
            for (int i = 0; i < myPojo.getMASTERS().getLAB_ALERTS().length; i++) {
                labAlerts.add(myPojo.getMASTERS().getLAB_ALERTS()[i]);
            }
        }

        lab_alert_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        spinnerDialog = new SpinnerDialog(Scan_Barcode_Outlabs.this, labAlerts, "Search Lab Alerts", "Close");// With No Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                lab_alert_spin.setText(s);
            }
        });

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        System.out.println("" + Globaly_Outlab_details.toString());

        SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefs.getString("WOEbrand", null);
        typeName = prefs.getString("woetype", null);

        title.setText("Scan Barcode(" + typeName + ")");


//        prefs = getSharedPreferences("showSelectedOutlabTest", MODE_PRIVATE);
//        testName = prefs.getString("OutlabtestsSelected", null);
//        show_selected_tests_data.setText(testName);

        scanIntegrator = new IntentIntegrator(Scan_Barcode_Outlabs.this);
        // linearLayoutManager = new LinearLayoutManager(Scan_Barcode_Outlabs.this);
        //  recycler_barcode.setLayoutManager(linearLayoutManager);
        int totalcount = 0;
        temparraylist = new ArrayList<>();
        for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
            if (Globaly_Outlab_details.get(i).getSampletype() != null) {
                for (int j = 0; j < Globaly_Outlab_details.get(i).getSampletype().length; j++) {
                    sample_type_linear.setVisibility(View.VISIBLE);
                    temparraylist.add(Globaly_Outlab_details.get(i).getSampletype()[j].getOutlabsampletype());
                    productName = Globaly_Outlab_details.get(i).getProduct();
                    sample_type_spinner.setAdapter(new ArrayAdapter<String>(Scan_Barcode_Outlabs.this, R.layout.spinnerproperty, temparraylist));
                }

            } else {
                sample_type_linear.setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < Globaly_Outlab_details.size(); i++) {

            totalcount = totalcount + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2c());
            Log.e(TAG, "onCreate: 11 " + totalcount);
        }

        setAmt.setText(String.valueOf(totalcount));

        Set<String> hs = new HashSet<>();
        hs.addAll(temparraylist);
        temparraylist.clear();
        temparraylist.addAll(hs);

        if (GlobalClass.flag == 0) {
            GlobalClass.flag = 1;
            for (int i = 0; i < temparraylist.size(); i++) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                // scannedBarcodeDetails.setSpecimen_type(temparraylist.get(i));
                // scannedBarcodeDetails.setProducts(productName);
                //scannedBarcodeDetails.setProducts();
                // GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
            }
        }
        System.out.println("finallist" + GlobalClass.finalspecimenttypewiselist.toString());


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.finalspecimenttypewiselist = new ArrayList<>();
                getSampleType = sample_type_spinner.getSelectedItem().toString();
                barcodeDetailsToStore = outlab_barcode.getText().toString();
                final String getAmount = setAmt.getText().toString();
                final String getWrittenAmt = enterAmt.getText().toString();

                String getTestSelection = show_selected_tests_data.getText().toString();
                if (barcodeDetailsToStore.length() == 7) {
                    Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.scan_brcd, Toast.LENGTH_SHORT).show();
                } else {
                    getOnlyBrcode = barcodeDetailsToStore.substring(8);
                    if (barcodeDetailsToStore != null) {
                        ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                        scannedBarcodeDetails.setSpecimen_type(getSampleType);
                        scannedBarcodeDetails.setProducts(displayslectedtest);
                        scannedBarcodeDetails.setBarcode(getOnlyBrcode);
                        GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
                    }

//                    String totalamt = setAmt.getText().toString();
//                    String amountpaid = enterAmt.getText().toString();

                    if (!getWrittenAmt.equals("") && !getAmount.equals("")) {
                        collectedAmt = Integer.parseInt(getWrittenAmt);
                        totalAmount = Integer.parseInt(getAmount);
                    } else {
                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.colAmt, Toast.LENGTH_SHORT).show();
                    }
                    if (getOnlyBrcode.equals("") || getOnlyBrcode.equals(null)) {
                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.scan_brcd, Toast.LENGTH_SHORT).show();
                    } else if (getWrittenAmt.equals("")) {
                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.colAmt, Toast.LENGTH_SHORT).show();
                    }
//                    else if(collectedAmt<totalAmount){
//                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.ent_crt_amt, Toast.LENGTH_SHORT).show();
//                    }
                    else {


                        preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);

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

                        getFinalEmailIdToPost = "";
                        getPincode = "";

                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

                        Date date = null;
                        try {
                            date = inputFormat.parse(sampleCollectionDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        outputDateStr = outputFormat.format(date);

                        String getLab_alert = lab_alert_spin.getText().toString();

                        if (getLab_alert.equals("SELECT LAB ALERTS")) {
                            lab_alert_pass_toApi = "";
                        } else {
                            lab_alert_pass_toApi = lab_alert_spin.getText().toString();
                        }

                        //sampleCollectionTime

                        String getFulltime = sampleCollectionDate + " " + sampleCollectionTime;
                        GlobalClass.Req_Date_Req(getFulltime, "hh:mm a", "HH:mm:ss");

                        Log.e(TAG, "fetchData: " + outputDateStr);

                        if (patientYearType.equalsIgnoreCase("Years")) {
                            ageType = "Y";
                        } else if (patientYearType.equalsIgnoreCase("Months")) {
                            ageType = "M";
                        } else if (patientYearType.equalsIgnoreCase("Days")) {
                            ageType = "D";
                        }
                        barProgressDialog = new ProgressDialog(Scan_Barcode_Outlabs.this);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);

                        POstQue = Volley.newRequestQueue(Scan_Barcode_Outlabs.this);

                        MyPojoWoe myPojoWoe = new MyPojoWoe();

                        Woe woe = new Woe();
                        woe.setAADHAR_NO("");
                        woe.setADDRESS(homeaddress);
                        woe.setAGE(patientYear);
                        woe.setAGE_TYPE(ageType);
                        woe.setALERT_MESSAGE(lab_alert_pass_toApi);
                        woe.setAMOUNT_COLLECTED(getWrittenAmt);
                        woe.setAMOUNT_DUE("");
                        woe.setAPP_ID(versionNameTopass);
                        woe.setBCT_ID(btechID);
                        woe.setBRAND(brandName);
                        woe.setCAMP_ID(campID);
                        woe.setCONT_PERSON("");
                        woe.setCONTACT_NO(getFinalPhoneNumberToPost);
                        woe.setCUSTOMER_ID("");
                        woe.setDELIVERY_MODE(2);
                        woe.setEMAIL_ID(getFinalEmailIdToPost);
                        woe.setENTERED_BY(user);
                        woe.setGENDER(patientGender);
                        woe.setLAB_ADDRESS(labAddress);
                        woe.setLAB_ID(labID);
                        woe.setLAB_NAME(labName);
                        woe.setLEAD_ID("");
                        woe.setMAIN_SOURCE(user);
                        woe.setORDER_NO("");
                        woe.setOS("unknown");
                        woe.setPATIENT_NAME(patientName);
                        woe.setPINCODE(getPincode);
                        woe.setPRODUCT(passProducts);
                        woe.setPurpose("mobile application");
                        woe.setREF_DR_ID(refeID);
                        woe.setREF_DR_NAME(referenceBy);
                        woe.setREMARKS("MOBILE");
                        woe.setSPECIMEN_COLLECTION_TIME(outputDateStr + " " + GlobalClass.cutString + ".000");
                        woe.setSPECIMEN_SOURCE("");
                        woe.setSR_NO(pass_to_api);
                        woe.setSTATUS("N");
                        woe.setSUB_SOURCE_CODE(user);
                        woe.setTOTAL_AMOUNT(getAmount);
                        woe.setTYPE(typeName);
                        woe.setWATER_SOURCE("");
                        woe.setWO_MODE("THYROSOFTLITE APP");
                        woe.setWO_STAGE(3);
                        woe.setULCcode("");

                        myPojoWoe.setWoe(woe);


                        barcodelists = new ArrayList<>();
                        getBarcodeArrList = new ArrayList<>();

                        for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                            barcodelist = new BarcodelistModel();
                            barcodelist.setSAMPLE_TYPE(GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type());
                            barcodelist.setBARCODE(GlobalClass.finalspecimenttypewiselist.get(i).getBarcode());
                            getBarcodeArrList.add(GlobalClass.finalspecimenttypewiselist.get(i).getBarcode());
                            barcodelist.setTESTS(GlobalClass.finalspecimenttypewiselist.get(i).getProducts());

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


                        if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
                            String barcodes = TextUtils.join(",", getBarcodeArrList);
                            if (flagcallonce == false) {
                                flagcallonce = true;
                                boolean isInserted = myDb.insertData(barcodes, json);
                                if (isInserted == true) {
                                    TastyToast.makeText(Scan_Barcode_Outlabs.this, ToastFile.woeSaved, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("payment", getWrittenAmt);
                                    bundle.putString("TotalAmt", getAmount);
                                    bundle.putString("selectedTest", testsData);
                                    bundle.putString("patient_id", barcode_patient_id);
                                    bundle.putString("Outlbbarcodes", barcodes);
                                    bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
                                    for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                                        testToPass = new ArrayList<>();
                                        testToPass.add(GlobalClass.finalspecimenttypewiselist.get(i).getProducts());
                                        outTestToSend = GlobalClass.finalspecimenttypewiselist.get(i).getProducts();
                                        showtest = TextUtils.join(",", testToPass);
                                    }
                                    bundle.putString("tetsts", displayslectedtest);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else {

                                    TastyToast.makeText(Scan_Barcode_Outlabs.this, ToastFile.woenotSaved, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            }

                        } else {
                            if (flagcallonce == false) {
                                flagcallonce = true;
                            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntry, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {

                                        Log.e(TAG, "onResponse: RESPONSE" + response);
                                        String finalJson = response.toString();
                                        JSONObject parentObjectOtp = new JSONObject(finalJson);

                                        RES_ID = parentObjectOtp.getString("RES_ID");
                                        barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                        message = parentObjectOtp.getString("message");
                                        status = parentObjectOtp.getString("status");
                                        barcode_id = parentObjectOtp.getString("barcode_id");
                                        if (status.equalsIgnoreCase("SUCCESS")) {
                                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                barProgressDialog.dismiss();
                                            }
                                            TastyToast.makeText(Scan_Barcode_Outlabs.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                            Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("payment", getWrittenAmt);
                                            bundle.putString("TotalAmt", getAmount);
                                            bundle.putString("selectedTest", testsData);
                                            bundle.putString("patient_id", barcode_patient_id);
                                            bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
                                            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                                                testToPass = new ArrayList<>();
                                                testToPass.add(GlobalClass.finalspecimenttypewiselist.get(i).getProducts());
                                                outTestToSend = GlobalClass.finalspecimenttypewiselist.get(i).getProducts();
                                                showtest = TextUtils.join(",", testToPass);
                                            }
                                            bundle.putString("tetsts", displayslectedtest);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }else if(status.equalsIgnoreCase(caps_invalidApikey)){
                                            GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                                        } else {
                                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                barProgressDialog.dismiss();
                                            }
                                            flagcallonce=false;
                                            TastyToast.makeText(Scan_Barcode_Outlabs.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error != null) {
                                    } else {
                                        flagcallonce = false;
                                        System.out.println(error);
                                    }
                                }
                            });
                            jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                                    150000,
                                    3,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            POstQue.add(jsonObjectRequest1);
                            Log.e(TAG, "saveandClose: URL" + jsonObjectRequest1);
                            Log.e(TAG, "saveandClose: json" + jsonObj);
                        }}
                    }
                }
            }
        });


        element1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanIntegrator.initiateScan();
            }
        });

        scanBarcode.setVisibility(View.VISIBLE);
        outlab_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualbarcodelayout.setVisibility(View.VISIBLE);
                scanBarcode.setVisibility(View.GONE);
            }
        });

        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualbarcodelayout.setVisibility(View.GONE);
                scanBarcode.setVisibility(View.VISIBLE);
            }
        });


        enter_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(Scan_Barcode_Outlabs.this,
                            ToastFile.crt_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcode.setText(enteredString.substring(1));
                    } else {
                        enter_barcode.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();

                if (s.length() < 8) {
                    flag = true;
                }

                if (s.length() > 8) {
                    enter_barcode.setText(enteredString.substring(1));
                    Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                } else {

                }

                if (s.length() == 8) {


                    if (flag == true) {
                        flag = false;

                        if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
                            enter_barcode.setText(s);
                        } else {
                            progressDialog = new ProgressDialog(Scan_Barcode_Outlabs.this);
                            progressDialog.setTitle("Kindly wait ...");
                            progressDialog.setMessage(ToastFile.processing_request);
                            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                            progressDialog.setProgress(0);
                            progressDialog.setMax(20);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setCancelable(false);
                            progressDialog.show();


                            barcodeDetailsdata = Volley.newRequestQueue(Scan_Barcode_Outlabs.this);//2c=/TAM03/TAM03136166236000078/geteditdata
                            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.scanBarcodeWithValidation + api_key + "/" + s + "/getcheckbarcode"
                                    , new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("barcode respponse" + response);

                                    Log.e(TAG, "onResponse: " + response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = null;
                                    try {
                                        parentObjectOtp = new JSONObject(finalJson);
                                        ERROR = parentObjectOtp.getString("ERROR");
                                        RES_ID = parentObjectOtp.getString("RES_ID");
                                        barcode = parentObjectOtp.getString("barcode");
                                        response1 = parentObjectOtp.getString("response");

                                        if (response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                                            progressDialog.dismiss();
                                            enter_barcode.setText(barcode);
                                        }else if(ERROR.equalsIgnoreCase(caps_invalidApikey)){
                                            GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                                        } else {
                                            enter_barcode.setText("");
                                            progressDialog.dismiss();
                                            Toast.makeText(Scan_Barcode_Outlabs.this, "" + response1, Toast.LENGTH_SHORT).show();
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
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
                            barcodeDetailsdata.add(jsonObjectRequestPop);
                            Log.e(TAG, "afterTextChanged: url" + jsonObjectRequestPop);
                        }


                    }


                } else {
                }

            }
        });


        enter_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Scan_Barcode_Outlabs.this,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcode.setText(enteredString.substring(1));
                    } else {
                        enter_barcode.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        enter_barcode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Scan_Barcode_Outlabs.this,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        reenter.setText(enteredString.substring(1));
                    } else {
                        reenter.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() == 8) {
                    String getPreviouseText = enter_barcode.getText().toString();
                    currentText = reenter.getText().toString();
                    if (getPreviouseText.equals(currentText)) {
                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.mtch_brcd, Toast.LENGTH_SHORT).show();
                        currentText = reenter.getText().toString();
                        manualbarcodelayout.setVisibility(View.GONE);
                        scanBarcode.setVisibility(View.VISIBLE);
                        outlab_barcode.setText("Barcode:" + currentText);
                    } else {
                        reenter.setText("");
                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                    }

                } else {

                }

            }
        });

    }

    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        stringofconvertedTime = null;
        try {
            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd ");
            String convertedDate = sdf_format.format(date);
            date = inputFormat.parse(time);
            stringofconvertedTime = outputFormat.format(date);
            cutString = stringofconvertedTime.substring(11, stringofconvertedTime.length() - 0);
        } catch (ParseException e) {
            if (time.contains("AM")) {
                time = time.substring(0, time.length() - 2);
                time = time + "a.m.";
            } else if (time.contains("PM")) {
                time = time.substring(0, time.length() - 2);
                time = time + "p.m.";
            }
            SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            SimpleDateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = null;
            stringofconvertedTime = null;

            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd ");
            String convertedDate = sdf_format.format(date);
            try {
                date = inputFormat.parse(time);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            stringofconvertedTime = outputFormat.format(date);
            cutString = stringofconvertedTime.substring(11, stringofconvertedTime.length() - 0);


            //Format of the date defined in the input String
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringofconvertedTime;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "123: ");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                getBarcodeDetails = result.getContents();
                if (getBarcodeDetails.length() == 8) {
                    passBarcodeData(getBarcodeDetails);
                } else {
                    Toast.makeText(this, ToastFile.invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(final String barcodeDetails) {

        Log.e(TAG, "passBarcodeData: getBarcode details" + barcodeDetails);
        //   checkBarcode(barcodeDetails);

        if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
            enter_barcode.setText(barcodeDetails);
        }else{
            barcodeDetailsdata = Volley.newRequestQueue(Scan_Barcode_Outlabs.this);//2c=/TAM03/TAM03136166236000078/geteditdata
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.scanBarcodeWithValidation + api_key + "/" + barcodeDetails + "/getcheckbarcode"
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("barcode respponse" + response);

                    Log.e(TAG, "onResponse: " + response);
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = null;
                    try {
                        parentObjectOtp = new JSONObject(finalJson);
                        ERROR = parentObjectOtp.getString("ERROR");
                        RES_ID = parentObjectOtp.getString("RES_ID");
                        barcode = parentObjectOtp.getString("barcode");
                        response1 = parentObjectOtp.getString("response");

                        if (response1.equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                            outlab_barcode.setText("Barcode:" + barcodeDetails);
                            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                                GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(barcodeDetails);
                            }
                        } else {
                            outlab_barcode.setText("");
                            Toast.makeText(Scan_Barcode_Outlabs.this, "" + response1, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
            barcodeDetailsdata.add(jsonObjectRequestPop);
            Log.e(TAG, "passBarcodeData: url" + jsonObjectRequestPop);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

