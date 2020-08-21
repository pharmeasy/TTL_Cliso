package com.example.e5322.thyrosoft.WOE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Adapter.TRFDisplayAdapter;
import com.example.e5322.thyrosoft.AsyncTaskPost_uploadfile;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.Controller.Checkbarcode_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.LeadWoeController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.ResponseModels.VerifyBarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;


public class Scan_Barcode_Outlabs extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    public static ArrayList<String> labAlerts;
    private static String stringofconvertedTime;
    private static String cutString;
    Button outlab_barcode;
    SharedPreferences prefs;
    RequestQueue barcodeDetailsdata, POstQue;
    String outTestToSend, testsData;
    EditText enterAmt, enter_barcode, reenter;
    String TAG = Scan_Barcode_Outlabs.class.getSimpleName();
    Button next;
    Camera camera;
    int b2b_rate = 0;
    ArrayList<Outlabdetails_OutLab> Globaly_Outlab_details = new ArrayList<>();
    LinearLayout sample_type_linear;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    TextView show_selected_tests_data, setAmt, title;
    IntentIntegrator scanIntegrator;
    LinearLayout barcodescanninglist;
    Spinner sample_type_spinner;
    RecyclerView recycler_barcode;
    TextView lab_alert_spin;
    String barcodes1;
    SharedPreferences preferences;
    ImageView img_edt, setback;
    String getAmount;
    String getWrittenAmt;
    LinearLayout manualbarcodelayout, scanBarcode, amt_collected_and_total_amt_ll;
    String user, passwrd, access, api_key, typeName, brandName, barcode_patient_id,  displayslectedtest, getSampleType;
    ArrayList<String> getProductCode;
    String productName, showtest;
    ArrayList<String> testToPass;
    BarcodelistModel barcodelist;
    ArrayList<com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel> barcodelists;
    LinearLayout ll_uploadTRF;
    RecyclerView rec_trf;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<TRFModel> trflist = new ArrayList<>();
    Activity mActivity;
    Bitmap bitmapimage;
    Uri imageUri;
    String userChoosenTask;
    File trf_img = null;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private boolean trfCheckFlag = false;
    private MyPojo myPojo;
    private SpinnerDialog spinnerDialog;
    private ArrayList<String> temparraylist;
    private boolean flag = true;
    private TextView companycost_test;
    private String currentText;
    private String barcodeDetailsToStore;
    private String getOnlyBrcode = "";
    private String getTypeName;
    private ImageView back;
    private ImageView element1_iv;
    private ImageView home;
    private int collectedAmt;
    private int totalAmount;
    private String patientName, patientYearType;
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
    private int pass_to_api;
    private String outputDateStr;
    private String message, selectedProduct;
    private String lab_alert_pass_toApi;
    private String passProducts;
    private ArrayList<String> getBarcodeArrList;
    private boolean flagcallonce = false;
    private DatabaseHelper myDb;
    private Global globalClass;


    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        stringofconvertedTime = null;
        try {
            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd ");
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

            stringofconvertedTime = null;

            date = new Date();

            try {
                date = inputFormat.parse(time);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            stringofconvertedTime = outputFormat.format(date);
            cutString = stringofconvertedTime.substring(11, stringofconvertedTime.length() - 0);

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringofconvertedTime;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);
        mActivity = Scan_Barcode_Outlabs.this;

        initViews();

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        Log.v(TAG, "" + Globaly_Outlab_details.toString());

        SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefs.getString("WOEbrand", "");
        typeName = prefs.getString("woetype", "");

        GlobalClass.SetText(title, "Scan Barcode(" + typeName + ")");


        scanIntegrator = new IntentIntegrator(Scan_Barcode_Outlabs.this);
        int totalcount = 0;

        temparraylist = new ArrayList<>();
        temparraylist.add("Select sample type");

        if (GlobalClass.CheckArrayList(Globaly_Outlab_details)) {
            for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                if (GlobalClass.checkArray(Globaly_Outlab_details.get(i).getSampletype())) {
                    for (int j = 0; j < Globaly_Outlab_details.get(i).getSampletype().length; j++) {
                        sample_type_linear.setVisibility(View.VISIBLE);
                        temparraylist.add(Globaly_Outlab_details.get(i).getSampletype()[j].getOutlabsampletype());
                        productName = Globaly_Outlab_details.get(i).getProduct();

                    }
                }
            }
        }


        sample_type_spinner.setAdapter(new ArrayAdapter<String>(Scan_Barcode_Outlabs.this, R.layout.spinnerproperty, temparraylist));

        ArrayList<String> saveLocation = new ArrayList<>();


        if (GlobalClass.CheckArrayList(Globaly_Outlab_details)) {
            for (int i = 0; i < Globaly_Outlab_details.size(); i++) {

                if (!GlobalClass.isNull(Globaly_Outlab_details.get(i).getIsCPL())) {
                    if (Globaly_Outlab_details.get(i).getIsCPL().equalsIgnoreCase("1")) {
                        saveLocation.add("CPL");
                    } else {
                        saveLocation.add("RPL");
                    }
                }

                if (!saveLocation.isEmpty()) {
                    if (saveLocation.contains("CPL")) {
                        totalcount = totalcount + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2c());
                        if (!GlobalClass.isNull(Globaly_Outlab_details.get(i).getRate().getCplr())) {
                            b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getCplr());
                        } else {
                            b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2b());
                        }

                    } else {
                        totalcount = totalcount + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2c());

                        if (!GlobalClass.isNull(Globaly_Outlab_details.get(i).getRate().getRplr())) {
                            b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getRplr());
                        } else {
                            b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2b());
                        }

                    }
                }


                Log.e(TAG, "b2b_rate:  " + b2b_rate);
                Log.e(TAG, "onCreate: 11 " + totalcount);
            }
        }


        GlobalClass.SetText(setAmt, String.valueOf(totalcount));

        Set<String> hs = new HashSet<>();
        hs.addAll(temparraylist);
        temparraylist.clear();
        temparraylist.addAll(hs);

        if (GlobalClass.flag == 0) {
            GlobalClass.flag = 1;
        }
        Log.v(TAG, "finallist" + finalspecimenttypewiselist.toString());


        initListner();
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
                GlobalClass.goToHome(Scan_Barcode_Outlabs.this);
            }
        });

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
                GlobalClass.SetText(lab_alert_spin, s);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    finalspecimenttypewiselist = new ArrayList<>();
                    getSampleType = sample_type_spinner.getSelectedItem().toString();
                    barcodeDetailsToStore = outlab_barcode.getText().toString();
                    getAmount = setAmt.getText().toString();
                    getWrittenAmt = enterAmt.getText().toString();
                    String getTestSelection = show_selected_tests_data.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (getSampleType.equalsIgnoreCase("Select sample type")) {
                    GlobalClass.showTastyToast(mActivity, "Kindly select sample type", 2);
                } else {
                    if (barcodeDetailsToStore.length() == 7) {
                        GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.scan_brcd, 2);
                    } else {


                        try {
                            if (!GlobalClass.isNull(barcodeDetailsToStore)) {
                                getOnlyBrcode = barcodeDetailsToStore.substring(8);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!GlobalClass.isNull(barcodeDetailsToStore)) {
                            ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                            scannedBarcodeDetails.setSpecimen_type(getSampleType);
                            scannedBarcodeDetails.setProducts(displayslectedtest);
                            scannedBarcodeDetails.setBarcode(getOnlyBrcode);
                            finalspecimenttypewiselist.add(scannedBarcodeDetails);
                        }


                        if (!getWrittenAmt.equals("") && !getAmount.equals("")) {
                            collectedAmt = Integer.parseInt(getWrittenAmt);
                            totalAmount = Integer.parseInt(getAmount);
                        } else {
                            GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.colAmt, 2);
                        }

                        try {
                            if (getOnlyBrcode.equals(null) || getOnlyBrcode.equals("")) {
                                GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.scan_brcd, 2);
                            } else if (getWrittenAmt.equals("")) {
                                GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.colAmt, 2);
                            } else if (Integer.parseInt(getWrittenAmt) < b2b_rate) {
                                GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, getResources().getString(R.string.amtcollval) + " " + b2b_rate, 2);
                            } else {
                                checklistData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this,
                            ToastFile.crt_brcd,
                            2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetText(enter_barcode, enteredString.substring(1));
                    } else {
                        GlobalClass.SetText(enter_barcode, "");
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
                    GlobalClass.SetText(enter_barcode, enteredString.substring(1));
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.crt_brcd, 2);
                }

                if (s.length() == 8) {
                    if (flag) {
                        flag = false;
                        String url = Api.scanBarcodeWithValidation + api_key + "/" + s + "/getcheckbarcode";

                        barcodeDetailsdata = Volley.newRequestQueue(Scan_Barcode_Outlabs.this);

                        try {
                            if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                                ControllersGlobalInitialiser.checkbarcode_controller = null;
                            }
                            ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller(mActivity, Scan_Barcode_Outlabs.this);
                            ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(url, barcodeDetailsdata);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
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
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this,
                            ToastFile.entr_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetText(enter_barcode, enteredString.substring(1));
                    } else {
                        GlobalClass.SetText(enter_barcode, "");
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
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this,
                            ToastFile.entr_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetText(reenter, enteredString.substring(1));
                    } else {
                        GlobalClass.SetText(reenter, "");
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
                        GlobalClass.showTastyToast(mActivity, ToastFile.mtch_brcd, 2);
                        currentText = reenter.getText().toString();
                        manualbarcodelayout.setVisibility(View.GONE);
                        scanBarcode.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(outlab_barcode, "Barcode:" + currentText);
                    } else {
                        GlobalClass.SetText(reenter, "");
                        GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.crt_brcd, 2);
                    }

                }

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
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

        ll_uploadTRF = (LinearLayout) findViewById(R.id.ll_uploadTRF);
        rec_trf = (RecyclerView) findViewById(R.id.rec_trf);
        enter_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }


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

        myDb = new DatabaseHelper(Scan_Barcode_Outlabs.this);
        Intent intent = getIntent();
        if (intent.hasExtra("FinalBarcodeList"))
            finalspecimenttypewiselist = intent.getExtras().getParcelableArrayList("FinalBarcodeList");

        getTypeName = intent.getStringExtra("getTypeName");

        linearLayoutManager1 = new LinearLayoutManager(Scan_Barcode_Outlabs.this);
        rec_trf.setLayoutManager(linearLayoutManager1);

        if (bundle1 != null) {
            Globaly_Outlab_details = bundle1.getParcelableArrayList("getOutlablist");
            testsData = bundle1.getString("selectedTest");
            Log.e(TAG, "onCreate: size " + Globaly_Outlab_details.size());

            ArrayList<String> getProducts = new ArrayList<>();
            getProductCode = new ArrayList<>();
            trflist.clear();

            try {
                if (GlobalClass.CheckArrayList(Globaly_Outlab_details)) {
                    for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                        getProductCode.add(Globaly_Outlab_details.get(i).getProduct());
                        getProducts.add(Globaly_Outlab_details.get(i).getProduct());
                        displayslectedtest = TextUtils.join(",", getProductCode);
                        passProducts = TextUtils.join(",", getProducts);
                        if (Globaly_Outlab_details.size() > 0) {
                            if (Globaly_Outlab_details.get(i).getTrf().equalsIgnoreCase(MessageConstants.YES)) {
                                TRFModel trfModel = new TRFModel();
                                trfModel.setProduct(Globaly_Outlab_details.get(i).getProduct());
                                trflist.add(trfModel);
                                callTRFAdapter(trflist);
                                Log.e(TAG, "TRF list--->" + trflist.size());
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            GlobalClass.SetText(show_selected_tests_data, testsData);
        } else {
            Log.e(TAG, "onCreate: null");
        }

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(Scan_Barcode_Outlabs.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();

        try {
            if (myPojo.getMASTERS().getLAB_ALERTS() != null) {
                for (int i = 0; i < myPojo.getMASTERS().getLAB_ALERTS().length; i++) {
                    labAlerts.add(myPojo.getMASTERS().getLAB_ALERTS()[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callTRFAdapter(ArrayList<TRFModel> trflist) {
        if (trflist != null && trflist.size() > 0) {
            ll_uploadTRF.setVisibility(View.VISIBLE);
            TRFDisplayAdapter trfDisplayAdapter = new TRFDisplayAdapter(Scan_Barcode_Outlabs.this, trflist);
            trfDisplayAdapter.setOnItemClickListener(new TRFDisplayAdapter.OnItemClickListener() {
                @Override
                public void onUploadClick(String product_name) {
                    selectedProduct = product_name;
                    if (AccessRuntimePermissions.checkcameraPermission(mActivity) && AccessRuntimePermissions.checkExternalPerm(mActivity)) {
                        selectImage();
                    } else {
                        AccessRuntimePermissions.requestCamerapermission(mActivity);
                        AccessRuntimePermissions.requestExternalpermission(mActivity);
                    }
                }
            });
            rec_trf.setAdapter(trfDisplayAdapter);
        } else {
            ll_uploadTRF.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                if (getBarcodeDetails.length() == 8) {
                    passBarcodeData(getBarcodeDetails);
                } else {
                    GlobalClass.showTastyToast(this, invalid_brcd, 2);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                try {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    trf_img = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_img;
                    trf_img = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), trf_img);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(trf_img.length())));
                    storeTRFImage(trf_img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
                if (data == null) {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.Failed_to_load_image, 2);
                    return;
                }
                try {
                    if (data.getData() != null) {
                        trf_img = FileUtil.from(this, data.getData());
                    }

                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(trf_img.length())));
                    trf_img = GlobalClass.getCompressedFile(mActivity, trf_img);
                    Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(trf_img.length())));

                    storeTRFImage(trf_img);
                } catch (IOException e) {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.Failed_to_load_image, 2);
                    e.printStackTrace();
                }
            }
        }
    }

    private void passBarcodeData(final String barcodeDetails) {

        Log.e(TAG, "passBarcodeData: getBarcode details" + barcodeDetails);

        if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
            GlobalClass.SetText(enter_barcode, barcodeDetails);
        } else {
            barcodeDetailsdata = Volley.newRequestQueue(Scan_Barcode_Outlabs.this);
            String url = Api.scanBarcodeWithValidation + api_key + "/" + barcodeDetails + "/getcheckbarcode";

            try {
                if (ControllersGlobalInitialiser.checkbarcode_controller != null) {
                    ControllersGlobalInitialiser.checkbarcode_controller = null;
                }
                ControllersGlobalInitialiser.checkbarcode_controller = new Checkbarcode_Controller(mActivity, Scan_Barcode_Outlabs.this, barcodeDetails);
                ControllersGlobalInitialiser.checkbarcode_controller.getCheckbarcodeController(url, barcodeDetailsdata);
            } catch (Exception e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, url
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            GlobalClass.showVolleyError(error, mActivity);
                        }
                    }
                }
            });
            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
            barcodeDetailsdata.add(jsonObjectRequestPop);
            Log.e(TAG, "passBarcodeData: url" + jsonObjectRequestPop);
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(mActivity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        openCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        chooseFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void buildCamera() {
        camera = new com.mindorks.paracamera.Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void storeTRFImage(File trf_img) {
        for (int i = 0; i < trflist.size(); i++) {
            if (trflist.get(i).getProduct().equalsIgnoreCase(selectedProduct)) {
                trflist.get(i).setTrf_image(trf_img);
            }
        }

        rec_trf.removeAllViews();
        callTRFAdapter(trflist);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }

                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getUploadFileResponse() {
        GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, message, 1);
        Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("payment", getWrittenAmt);
        bundle.putString("TotalAmt", getAmount);
        bundle.putString("selectedTest", testsData);
        bundle.putString("patient_id", barcode_patient_id);
        bundle.putString("Outlbbarcodes", barcodes1);
        bundle.putParcelableArrayList("sendArraylist", finalspecimenttypewiselist);

        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            testToPass = new ArrayList<>();
            testToPass.add(finalspecimenttypewiselist.get(i).getProducts());
            outTestToSend = finalspecimenttypewiselist.get(i).getProducts();
            showtest = TextUtils.join(",", testToPass);
        }

        bundle.putString("tetsts", displayslectedtest);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void checklistData() {

        if (trflist != null && trflist.size() > 0) {
            for (int i = 0; i < trflist.size(); i++) {
                if (trflist.get(i).getTrf_image() == null)
                    trfCheckFlag = true;
            }
            if (trfCheckFlag) {
                trfCheckFlag = false;
                GlobalClass.showTastyToast(mActivity, ToastFile.TRF_UPLOAD_CHECK, 2);
            } else
                doFinalWoe();
        } else
            doFinalWoe();
    }

    private void doFinalWoe() {
        preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);

        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("age", null);
        patientYearType = preferences.getString("ageType", null);
        patientGender = preferences.getString("gender", null);

        brandName = preferences.getString("WOEbrand", null);

        if (brandName.equalsIgnoreCase("EQNX")) {
            brandName = "WHATERS";
        } else {
            brandName = preferences.getString("WOEbrand", null);
        }

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
        getPincode = preferences.getString("pincode", null);

        getFinalEmailIdToPost = "";

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
        woe.setADDITIONAL1("CPL");
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

        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
            barcodelist = new BarcodelistModel();
            barcodelist.setSAMPLE_TYPE(finalspecimenttypewiselist.get(i).getSpecimen_type());
            barcodelist.setBARCODE(finalspecimenttypewiselist.get(i).getBarcode());
            getBarcodeArrList.add(finalspecimenttypewiselist.get(i).getBarcode());
            barcodelist.setTESTS(finalspecimenttypewiselist.get(i).getProducts());
            barcodelists.add(barcodelist);
        }
        myPojoWoe.setBarcodelistModel(barcodelists);
        myPojoWoe.setWoe_type("WOE");
        myPojoWoe.setApi_key(api_key);//api_key

        Gson trfgson1 = new GsonBuilder().create();
        String trfjson1 = trfgson1.toJson(trflist);
        myPojoWoe.setTrfjson(trfjson1);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
            barcodes1 = TextUtils.join(",", getBarcodeArrList);
            if (!flagcallonce) {
                flagcallonce = true;
                boolean isInserted = myDb.insertData(barcodes1, json);
                if (isInserted) {
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.woeSaved, 1);
                    Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("payment", getWrittenAmt);
                    bundle.putString("TotalAmt", getAmount);
                    bundle.putString("selectedTest", testsData);
                    bundle.putString("patient_id", barcode_patient_id);
                    bundle.putString("Outlbbarcodes", barcodes1);
                    bundle.putParcelableArrayList("sendArraylist", finalspecimenttypewiselist);
                    for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                        testToPass = new ArrayList<>();
                        testToPass.add(finalspecimenttypewiselist.get(i).getProducts());
                        outTestToSend = finalspecimenttypewiselist.get(i).getProducts();
                        showtest = TextUtils.join(",", testToPass);
                    }
                    bundle.putString("tetsts", displayslectedtest);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.woenotSaved, 2);
                }
            }
        } else {
            if (!flagcallonce) {
                flagcallonce = true;
                try {
                    if (ControllersGlobalInitialiser.leadWoeController != null) {
                        ControllersGlobalInitialiser.leadWoeController = null;
                    }
                    ControllersGlobalInitialiser.leadWoeController = new LeadWoeController(mActivity, Scan_Barcode_Outlabs.this);
                    ControllersGlobalInitialiser.leadWoeController.getleadwoeController(jsonObj, POstQue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                if (trflist != null && trflist.size() > 0)
                    new AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs.this, mActivity, api_key, user, barcode_patient_id, trflist).execute();
                else {
                    getUploadFileResponse();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (camera != null) {
                camera.deleteImage();
            }
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCheckbarcodeResponse(JSONObject response) {
        Log.v(TAG, "barcode response" + response);
        try {
            Gson gson = new Gson();
            VerifyBarcodeResponseModel responseModel = gson.fromJson(String.valueOf(response), VerifyBarcodeResponseModel.class);

            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                    GlobalClass.SetText(enter_barcode, responseModel.getBarcode());
                } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                } else {
                    GlobalClass.SetText(enter_barcode, "");
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, "" + responseModel.getResponse(), 1);
                }
            } else {
                GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.something_went_wrong, 2);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getpassbarcode(JSONObject response, String barcodeDetails) {

        Log.v(TAG, "barcode respponse" + response);
        try {
            Gson gson = new Gson();
            VerifyBarcodeResponseModel responseModel = gson.fromJson(String.valueOf(response), VerifyBarcodeResponseModel.class);

            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                    GlobalClass.SetText(outlab_barcode, "Barcode:" + barcodeDetails);

                    if (GlobalClass.CheckArrayList(finalspecimenttypewiselist)) {
                        for (int i = 0; i < finalspecimenttypewiselist.size(); i++) {
                            finalspecimenttypewiselist.get(i).setBarcode(barcodeDetails);
                        }
                    }


                } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                } else {
                    GlobalClass.SetText(outlab_barcode, "");
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, "" + responseModel.getResponse(), 2);
                }
            } else {
                GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.something_went_wrong, 2);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void doleadwoe(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);

            Gson woeGson = new Gson();
            WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);
            barcode_patient_id = woeResponseModel.getBarcode_patient_id();
            Log.e(TAG, "BARCODE PATIENT ID --->" + barcode_patient_id);
            message = woeResponseModel.getMessage();
            if (woeResponseModel != null) {
                if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                    Log.e(TAG, "onResponse message --->: " + message);
                    if (trflist != null && trflist.size() > 0)
                        new AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs.this, mActivity, api_key, user, barcode_patient_id, trflist).execute();
                    else {
                        getUploadFileResponse();
                    }
                } else if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                } else {
                    flagcallonce = false;
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, message, 2);
                }
            } else {
                flagcallonce = false;
                GlobalClass.showTastyToast(Scan_Barcode_Outlabs.this, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doleadwoeError() {
        flagcallonce = false;
    }
}