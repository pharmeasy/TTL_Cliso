package com.example.e5322.thyrosoft.WOE;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.WOEPaymentActivity;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcodeOutlabs;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Adapter.BrandAdapter;
import com.example.e5322.thyrosoft.Adapter.TRFDisplayAdapter;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.AsyncTaskPost_uploadfile;
import com.example.e5322.thyrosoft.Controller.GetLocationController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.UploadPrescController;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.GetLocationReqModel;
import com.example.e5322.thyrosoft.Models.GetLocationRespModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.RequestModels.UploadPrescRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.VerifyBarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.sdsmdg.tastytoast.TastyToast;

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
import java.util.List;
import java.util.Set;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


public class Scan_Barcode_Outlabs extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    public static ArrayList<String> labAlerts;
    private static String stringofconvertedTime;
    private static String cutString;
    Button outlab_barcode;
    RequestQueue barcodeDetailsdata, POstQue;
    String outTestToSend, testsData;
    EditText enterAmt, enter_barcode, reenter;
    String TAG = Scan_Barcode_Outlabs.class.getSimpleName();
    Button next;
    Camera camera;
    int b2b_rate = 0, b2c_rate = 0;
    ArrayList<Outlabdetails_OutLab> Globaly_Outlab_details = new ArrayList<>();
    LinearLayout sample_type_linear;
    ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist;
    TextView show_selected_tests_data, setAmt, title;
    IntentIntegrator scanIntegrator;
    LinearLayout barcodescanninglist;
    BaseModel.Barcodes[] barcodes;
    Spinner sample_type_spinner;
    RecyclerView recycler_barcode;
    TextView lab_alert_spin;
    String barcodes1;
    SharedPreferences preferences, prefe;
    ImageView img_edt, setback;
    ProgressDialog progressDialog;
    String getAmount;
    String getWrittenAmt;
    LinearLayout manualbarcodelayout, scanBarcode, amt_collected_and_total_amt_ll;
    String user, passwrd, access, api_key, typeName, brandName, ERROR, RES_ID, barcode, response1, barcode_patient_id, afterBarcode, storeResponse, barcodeNumber, displayslectedtest, getSampleType;
    ArrayList<String> getProductCode;
    String productName, showtest;
    ArrayList<String> testToPass;
    BarcodelistModel barcodelist;
    ProgressDialog barProgressDialog;
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
    private int PERMISSION_REQUEST_CODE = 200;
    private int PICK_PHOTO_FROM_CAMERA = 201;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private boolean trfCheckFlag = false;
    private MyPojo myPojo;
    private SpinnerDialog spinnerDialog;
    private ArrayList<String> temparraylist;
    private ArrayList<String> getProducts;
    private AdapterBarcodeOutlabs adapterBarcodeOutlabs;
    private boolean flag = true;
    private TextView companycost_test;
    private String currentText;
    private String barcodeDetailsToStore;
    private String getOnlyBrcode = "";
    private String getTypeName;
    private ImageView back;
    private ImageView element1_iv;
    private ImageView home;
    private String setbarcode;
    private int collectedAmt;
    private int totalAmount;
    MainModel mainModel;
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
    private int pass_to_api;
    private String outputDateStr;
    private String message, selectedProduct;
    private String lab_alert_pass_toApi;
    private String passProducts;
    private ArrayList<String> getBarcodeArrList;
    private boolean flagcallonce = false;
    private DatabaseHelper myDb;
    private String barcode_id;
    private Global globalClass;
    File vialimg_file;
    Button btn_choosefile;
    LinearLayout lin_preview;
    private boolean isvial = false;
    TextView txt_fileupload;
    Bitmap vialbitmap;
    List<String> imagelist = new ArrayList<>();

    LinearLayout ll_letterhead, ll_location_note;
    ConnectionDetector cd;
    RecyclerView recy_brand;
    private String getBrand_name;
    private String EMAIL_ID;
    EditText edt_confirm_amt;
    private int PaymentType;

    LinearLayout ll_prescription, lin_preview_pres;
    Button btn_choosefile_presc;
    TextView txt_fileupload_pres;
    private boolean isprescition = false, ispresc = false;
    List<String> imagelist_per = new ArrayList<>();
    private File presc_file;

    private String ADDITIONAL1 = "";
    TextView tv_location, tv_lab;


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

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);
        mActivity = Scan_Barcode_Outlabs.this;
        cd = new ConnectionDetector(mActivity);

        SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefs.getString("WOEbrand", "");
        typeName = prefs.getString("woetype", "");

        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        img_edt = (ImageView) findViewById(R.id.img_edt);
        back = (ImageView) findViewById(R.id.back);
        element1_iv = (ImageView) findViewById(R.id.element1_iv);
        home = (ImageView) findViewById(R.id.home);
        setback = (ImageView) findViewById(R.id.setback);
        setAmt = (TextView) findViewById(R.id.setAmt);
        recy_brand = (RecyclerView) findViewById(R.id.recy_brand);
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

        btn_choosefile = findViewById(R.id.btn_choosefile);
        txt_fileupload = findViewById(R.id.txt_fileupload);
        lin_preview = findViewById(R.id.lin_preview);

        ll_letterhead = findViewById(R.id.ll_letterhead);
        edt_confirm_amt = findViewById(R.id.edt_confirm_amt);

        ll_prescription = findViewById(R.id.ll_prescription);
        btn_choosefile_presc = findViewById(R.id.btn_choosefile_presc);
        lin_preview_pres = findViewById(R.id.lin_preview_pres);
        txt_fileupload_pres = findViewById(R.id.txt_fileupload_pres);
        tv_location = findViewById(R.id.tv_location);
        tv_lab = findViewById(R.id.tv_lab);
        ll_location_note = findViewById(R.id.ll_location_note);

        ll_location_note.setVisibility(cd.isConnectingToInternet() ? View.VISIBLE : View.GONE);

        btn_choosefile_presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Scan_Barcode_Outlabs.this)
                        .compress(Constants.MaxImageSize)
                        .crop()
                        .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight)
                        .start();
                ispresc = true;
            }
        });

        txt_fileupload_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setviewpager(imagelist_per);
            }
        });


        btn_choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openCamera();
                GlobalClass.cropImageActivity(Scan_Barcode_Outlabs.this, 0);

                isvial = true;
            }
        });
        txt_fileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setviewpager(imagelist);
            }
        });

        preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        final String json = appSharedPrefs.getString("MyObject", "");
        mainModel = gson.fromJson(json, MainModel.class);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


//        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
//        PaymentType = getProfileName.getInt("PaymentType", 0);
        PaymentType = 0;

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
        if (intent.hasExtra("FinalBarcodeList"))
            finalspecimenttypewiselist = intent.getExtras().getParcelableArrayList("FinalBarcodeList");

        getTypeName = intent.getStringExtra("getTypeName");

        linearLayoutManager1 = new LinearLayoutManager(Scan_Barcode_Outlabs.this);
        rec_trf.setLayoutManager(linearLayoutManager1);
        Globaly_Outlab_details = Global.Selcted_Outlab_Test_global;
        if (bundle1 != null) {
            testsData = bundle1.getString("selectedTest");
            Log.e(TAG, "onCreate: size " + Globaly_Outlab_details.size());
            ArrayList<String> getProducts = new ArrayList<>();
            getProductCode = new ArrayList<>();
            trflist.clear();

            ArrayList<String> getTestCode = new ArrayList<>();
            if (GlobalClass.isArraylistNotNull(Globaly_Outlab_details)) {
                for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                    if (!GlobalClass.isNull(Globaly_Outlab_details.get(i).getProduct())) {
                        getTestCode.add(Globaly_Outlab_details.get(i).getProduct());
                    }
                }
            }

            if (GlobalClass.isArraylistNotNull(Globaly_Outlab_details)) {
                for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                    if (Globaly_Outlab_details.get(i).isPrescription()) {
                        isprescition = Globaly_Outlab_details.get(i).isPrescription();
                        ll_prescription.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

            try {
                for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                    getProductCode.add(Globaly_Outlab_details.get(i).getProduct());
                    getProducts.add(Globaly_Outlab_details.get(i).getProduct());
                    displayslectedtest = TextUtils.join(",", getProductCode);
                    passProducts = TextUtils.join(",", getProducts);
                    if (Globaly_Outlab_details.size() > 0) {
                        if (Globaly_Outlab_details.get(i).getTrf().equalsIgnoreCase("Yes")) {
                            TRFModel trfModel = new TRFModel();
                            trfModel.setProduct(Globaly_Outlab_details.get(i).getProduct());
                            trflist.add(trfModel);
                            callTRFAdapter(trflist);
                            Log.e(TAG, "TRF list--->" + trflist.size());
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (cd.isConnectingToInternet()) {
                GetLocationReqModel getLocationReqModel = new GetLocationReqModel();
                getLocationReqModel.setTest("" + TextUtils.join(",", getTestCode));
                getLocationReqModel.setTSP("" + user);
                GetLocationController getLocationController = new GetLocationController(this);
                getLocationController.CallAPI(getLocationReqModel);
            }

            show_selected_tests_data.setText(testsData);
        } else {
            Log.e(TAG, "onCreate: null");
        }


        SetBrandLetterValues();
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

        Log.v(TAG, "" + Globaly_Outlab_details.toString());

        title.setText("Scan Barcode(" + typeName + ")");


//        prefs = getSharedPreferences("showSelectedOutlabTest", MODE_PRIVATE);
//        testName = prefs.getString("OutlabtestsSelected", null);
//        show_selected_tests_data.setText(testName);

        scanIntegrator = new IntentIntegrator(Scan_Barcode_Outlabs.this);
        // linearLayoutManager = new LinearLayoutManager(Scan_Barcode_Outlabs.this);
        //  recycler_barcode.setLayoutManager(linearLayoutManager);


        temparraylist = new ArrayList<>();
        temparraylist.add("Select sample type");
        for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
            if (Globaly_Outlab_details.get(i).getSampletype().length > 0) {
                for (int j = 0; j < Globaly_Outlab_details.get(i).getSampletype().length; j++) {
                    sample_type_linear.setVisibility(View.VISIBLE);
                    temparraylist.add(Globaly_Outlab_details.get(i).getSampletype()[j].getOutlabsampletype());
                    productName = Globaly_Outlab_details.get(i).getProduct();

                }
            }
        }

        sample_type_spinner.setAdapter(new ArrayAdapter<String>(Scan_Barcode_Outlabs.this, R.layout.spinnerproperty, temparraylist));

        ArrayList<String> saveLocation = new ArrayList<>();

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
                    b2c_rate = b2c_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2c());
                    if (!GlobalClass.isNull(Globaly_Outlab_details.get(i).getRate().getCplr())) {
                        b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getCplr());
                    } else {
                        b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2b());
                    }
                } else {
                    b2c_rate = b2c_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2c());
                    if (!GlobalClass.isNull(Globaly_Outlab_details.get(i).getRate().getRplr())) {
                        b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getRplr());
                    } else {
                        b2b_rate = b2b_rate + Integer.parseInt(Globaly_Outlab_details.get(i).getRate().getB2b());
                    }
                }
            }
            Log.e(TAG, "b2b_rate:  " + b2b_rate);
            Log.e(TAG, "oAmount to display" + b2c_rate);
        }


        setAmt.setText(String.valueOf(b2c_rate));

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
        Log.v(TAG, "finallist" + GlobalClass.finalspecimenttypewiselist.toString());


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GlobalClass.finalspecimenttypewiselist = new ArrayList<>();
                    getSampleType = sample_type_spinner.getSelectedItem().toString();
                    barcodeDetailsToStore = outlab_barcode.getText().toString();
                    getAmount = setAmt.getText().toString();
                    getWrittenAmt = enterAmt.getText().toString();
                    String getTestSelection = show_selected_tests_data.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (getSampleType.equalsIgnoreCase("Select sample type")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                    alertDialogBuilder
                            .setMessage("Kindly select sample type")
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    if (barcodeDetailsToStore.length() == 7) {
                        Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.scan_brcd, Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            if (!GlobalClass.isNull(barcodeDetailsToStore)) {
                                getOnlyBrcode = barcodeDetailsToStore.substring(8);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

                        try {
                            if (GlobalClass.isNull(getOnlyBrcode)) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.scan_brcd, Toast.LENGTH_SHORT).show();
                            } else if (GlobalClass.isNull(getWrittenAmt)) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.colAmt, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(getWrittenAmt) < b2b_rate) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, getResources().getString(R.string.amtcollval) + " " + b2b_rate, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(getWrittenAmt) > b2c_rate) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, getResources().getString(R.string.lessamtcollval) + " " + b2c_rate, Toast.LENGTH_SHORT).show();
                            } else if (GlobalClass.isNull(edt_confirm_amt.getText().toString())) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, "Confirm Amount Collected", Toast.LENGTH_SHORT).show();
                            } else if (!edt_confirm_amt.getText().toString().equalsIgnoreCase(getWrittenAmt)) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, "Amount Mismatched", Toast.LENGTH_SHORT).show();
                            }/* else if (vialimg_file == null || !vialimg_file.exists()) {
                                Toast.makeText(Scan_Barcode_Outlabs.this, ToastFile.vialimage, Toast.LENGTH_SHORT).show();
                            } */ else if (ll_letterhead.getVisibility() == View.VISIBLE) {
                                if (GlobalClass.isNull(getBrand_name)) {
                                    Toast.makeText(mActivity, "Select Brand", Toast.LENGTH_SHORT).show();
                                }/* else if (vialimg_file == null) {
                                    Toast.makeText(mActivity, "Upload Vial Image", Toast.LENGTH_SHORT).show();
                                }*/ else if (isprescition) {
                                    if (presc_file != null) {
                                        checklistData();
                                    } else {
                                        Toast.makeText(mActivity, "Please upload Prescription", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    checklistData();
                                }
                            } else if (isprescition) {
                                if (presc_file != null) {
                                    checklistData();
                                } else {
                                    Toast.makeText(mActivity, "Please upload Prescription", Toast.LENGTH_SHORT).show();
                                }
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
                }

                if (s.length() == 8) {
                    if (flag) {
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

                            barcodeDetailsdata = GlobalClass.setVolleyReq(Scan_Barcode_Outlabs.this);
                            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.scanBarcodeWithValidation + api_key + "/" + s + "/getcheckbarcode"
                                    , new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.v(TAG, "barcode response" + response);
                                    try {
                                        progressDialog.dismiss();
                                        Gson gson = new Gson();
                                        VerifyBarcodeResponseModel responseModel = gson.fromJson(String.valueOf(response), VerifyBarcodeResponseModel.class);

                                        if (responseModel != null) {
                                            if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                                                enter_barcode.setText(responseModel.getBarcode());
                                            } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                                                GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                                            } else {
                                                enter_barcode.setText("");
                                                Toast.makeText(Scan_Barcode_Outlabs.this, "" + responseModel.getResponse(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            TastyToast.makeText(Scan_Barcode_Outlabs.this, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        }
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    if (error.networkResponse == null) {
                                        if (error.getClass().equals(TimeoutError.class)) {
                                            // Show timeout error message
                                        }
                                    }
                                }
                            });
                            GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
                            barcodeDetailsdata.add(jsonObjectRequestPop);
                            Log.e(TAG, "afterTextChanged: url" + jsonObjectRequestPop);
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
        reenter.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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

                }

            }
        });

    }


    private void SetBrandLetterValues() {

        if (Global.OTPVERIFIED) {
            ll_letterhead.setVisibility(View.GONE);
        } else {
            final ArrayList<BaseModel.BrandDtlsDTO> getBrandList = GenerateBrandList();
            final ArrayList<String> brandName = new ArrayList<>();
            final ArrayList<String> newbrandName = new ArrayList<>();
            for (int i = 0; i < getBrandList.size(); i++) {
                if (!GlobalClass.isNull(getBrandList.get(i).getAlias())) {
                    brandName.add(getBrandList.get(i).getAlias());
                }
            }
            HashSet<String> duplicateremove = new HashSet<>(brandName);
            newbrandName.clear();
            newbrandName.addAll(duplicateremove);

            ArrayList<String> finallist = new ArrayList<>();
            if (GlobalClass.CheckArrayList(newbrandName)) {
                for (int i = 0; i < newbrandName.size(); i++) {
                    int cnt = 0;
                    for (int j = 0; j < Globaly_Outlab_details.size(); j++) {
                        for (int k = 0; k < Globaly_Outlab_details.get(j).getBrandDtls().size(); k++) {
                            if (newbrandName.get(i).equalsIgnoreCase(Globaly_Outlab_details.get(j).getBrandDtls().get(k).getAlias())) {
                                cnt++;
                            }
                        }
                    }

                    if (cnt == Globaly_Outlab_details.size()) {
                        finallist.add(newbrandName.get(i));
                    }
                }
            }

            if (finallist.size() == 0) {
                ll_letterhead.setVisibility(View.GONE);
            } else {
                ll_letterhead.setVisibility(View.VISIBLE);
            }

            SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
            typeName = prefs.getString("woetype", "");
            BrandAdapter brandAdapter = new BrandAdapter(this, finallist, getBrandList, typeName);
            brandAdapter.setOnItemClickListener(new BrandAdapter.OnClickListener() {
                @Override
                public void onchecked(String brand, String rate) {
                    getBrand_name = brand;
                }
            });
            recy_brand.setAdapter(brandAdapter);
            brandAdapter.notifyDataSetChanged();
        }


    }


    private ArrayList<BaseModel.BrandDtlsDTO> GenerateBrandList() {
        ArrayList<BaseModel.BrandDtlsDTO> entity = new ArrayList<>();
        if (GlobalClass.CheckArrayList(Globaly_Outlab_details)) {
            for (int i = 0; i < Globaly_Outlab_details.size(); i++) {
                if (GlobalClass.CheckArrayList(Globaly_Outlab_details.get(i).getBrandDtls())) {
                    for (int j = 0; j < Globaly_Outlab_details.get(i).getBrandDtls().size(); j++) {
                        BaseModel.BrandDtlsDTO brandDtlsDTO = new BaseModel.BrandDtlsDTO();
                        brandDtlsDTO.setAlias("" + Globaly_Outlab_details.get(i).getBrandDtls().get(j).getAlias());
                        brandDtlsDTO.setBrandName("" + Globaly_Outlab_details.get(i).getBrandDtls().get(j).getBrandName());
                        brandDtlsDTO.setBrandRate("" + Globaly_Outlab_details.get(i).getBrandDtls().get(j).getBrandRate());
                        brandDtlsDTO.setILSRate("" + Globaly_Outlab_details.get(i).getBrandDtls().get(j).getILSRate());
                        brandDtlsDTO.setRPTFile("" + Globaly_Outlab_details.get(i).getBrandDtls().get(j).getRPTFile());
                        brandDtlsDTO.setFullName("" + Globaly_Outlab_details.get(i).getBrandDtls().get(j).getFullName());
                        entity.add(brandDtlsDTO);

                    }
                }
            }
        }

        return entity;
    }

    private void callTRFAdapter(ArrayList<TRFModel> trflist) {
        if (trflist.size() > 0) {
            ll_uploadTRF.setVisibility(View.VISIBLE);
            TRFDisplayAdapter trfDisplayAdapter = new TRFDisplayAdapter(Scan_Barcode_Outlabs.this, trflist);
            trfDisplayAdapter.setOnItemClickListener(new TRFDisplayAdapter.OnItemClickListener() {
                @Override
                public void onUploadClick(String product_name) {
                    selectedProduct = product_name;
                    if (checkPermission()) {
                        selectImage();
                    } else {
                        requestPermission();
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
                    Toast.makeText(this, invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                try {
                    if (isvial) {
                        imagelist.clear();
                        vialbitmap = camera.getCameraBitmap();
                        String imageurlvial = camera.getCameraBitmapPath();
                        vialimg_file = new File(imageurlvial);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + vialimg_file;
                        vialimg_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurlvial), vialimg_file);
                        lin_preview.setVisibility(View.VISIBLE);
                        txt_fileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_fileupload.setPaintFlags(txt_fileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        imagelist.add(vialimg_file.toString());
                        isvial = false;
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_img = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_img;
                        trf_img = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_img);
                        Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(trf_img.length())));
                        storeTRFImage(trf_img);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(mActivity, "Failed to load image!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mActivity, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
                try {
                    if (isvial) {
                        imagelist.clear();
//                        vialbitmap = camera.getCameraBitmap();
                        //                    String imageurlvial = camera.getCameraBitmapPath();
                        String imageurlvial = ImagePicker.Companion.getFile(data).toString();
                        vialimg_file = new File(imageurlvial);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + vialimg_file;
                        vialimg_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurlvial), vialimg_file);
                        lin_preview.setVisibility(View.VISIBLE);
                        txt_fileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_fileupload.setPaintFlags(txt_fileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        imagelist.add(vialimg_file.toString());
                        isvial = false;
                    } else if (ispresc) {
                        imagelist_per.clear();
                        presc_file = ImagePicker.Companion.getFile(data);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + presc_file;
                        presc_file = new File(destFile);
                        GlobalClass.copyFile(ImagePicker.Companion.getFile(data), presc_file);
                        lin_preview_pres.setVisibility(View.VISIBLE);
                        txt_fileupload_pres.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_fileupload_pres.setPaintFlags(txt_fileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        imagelist_per.add(presc_file.toString());
                        ispresc = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setviewpager(List<String> imagelist) {
        final Dialog maindialog = new Dialog(mActivity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        //maindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mActivity, imagelist, 0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        final PageIndicatorView pageIndicatorView = maindialog.findViewById(R.id.pageIndicatorView);
        if (imagelist != null && imagelist.size() > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
            pageIndicatorView.setCount(imagelist.size()); // specify total count of indicators
            pageIndicatorView.setSelection(0);
            pageIndicatorView.setSelectedColor(mActivity.getResources().getColor(R.color.maroon));
        } else {
            pageIndicatorView.setVisibility(View.GONE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
                pageIndicatorView.setSelectedColor(getResources().getColor(R.color.maroon));
                pageIndicatorView.setUnselectedColor(getResources().getColor(R.color.grey));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final Button btn_delete = maindialog.findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.GONE);


        ImageView ic_close = (ImageView) maindialog.findViewById(R.id.img_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maindialog.dismiss();
            }
        });


        maindialog.setCancelable(true);
        maindialog.show();
    }

    private void passBarcodeData(final String barcodeDetails) {
        Log.e(TAG, "passBarcodeData: getBarcode details" + barcodeDetails);
        if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
            outlab_barcode.setText("Barcode:" + barcodeDetails);
        } else {
            barcodeDetailsdata = GlobalClass.setVolleyReq(Scan_Barcode_Outlabs.this);
            JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.scanBarcodeWithValidation + api_key + "/" + barcodeDetails + "/getcheckbarcode"
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v(TAG, "barcode respponse" + response);
                    try {
                        Gson gson = new Gson();
                        VerifyBarcodeResponseModel responseModel = gson.fromJson(String.valueOf(response), VerifyBarcodeResponseModel.class);

                        if (responseModel != null) {
                            if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                                outlab_barcode.setText("Barcode:" + barcodeDetails);
                                for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                                    GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(barcodeDetails);
                                }
                            } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                            } else {
                                outlab_barcode.setText("");
                                Toast.makeText(Scan_Barcode_Outlabs.this, "" + responseModel.getResponse(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            TastyToast.makeText(Scan_Barcode_Outlabs.this, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } catch (JsonSyntaxException e) {
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


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mActivity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mActivity, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
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
                .setCompression(Constants.setcompression)
                .setImageHeight(Constants.setheight)// it will try to achieve this height as close as possible maintaining the aspect ratio;
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


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmapimage = null;
        if (data != null) {
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
                e.printStackTrace();
            }
        }
    }


    private void onCaptureImageResult(Intent data) {
        try {
            bitmapimage = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), imageUri);
            String imageurl = getRealPathFromURI(imageUri);

            bitmapimage = GlobalClass.rotate(bitmapimage, imageurl);
            trf_img = new File(imageurl);
            //  trf_img = GlobalClass.getCompressedFile(mActivity, trf_img);
            storeTRFImage(trf_img);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getUploadFileResponse() {
        TastyToast.makeText(Scan_Barcode_Outlabs.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        if (isprescition) {
            UploadPrescRequestModel uploadPrescRequestModel = new UploadPrescRequestModel();
            uploadPrescRequestModel.setENTRYBY(user);
            uploadPrescRequestModel.setDocType("DCPRESCRIPTION");
            uploadPrescRequestModel.setImgUrl(presc_file);
            uploadPrescRequestModel.setPatientid(barcode_patient_id);
            uploadPrescRequestModel.setSourceCode(user);
            UploadPrescController uploadPrescController = new UploadPrescController(this);
            uploadPrescController.UploadPrescAPICall(uploadPrescRequestModel);
        } else {
            Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("payment", getWrittenAmt);
            bundle.putString("TotalAmt", getAmount);
            bundle.putString("selectedTest", testsData);
            bundle.putString("patient_id", barcode_patient_id);
            bundle.putString("Outlbbarcodes", barcodes1);

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
        }


    }

    public void checklistData() {
        if (GlobalClass.allowForOfflineUse(user)) {
            checkAndProceed();
        } else {
            if (cd.isConnectingToInternet()) {
                checkAndProceed();
            } else {
                GlobalClass.showCustomToast(mActivity, ToastFile.intConnection, 1);
            }
        }
    }

    private void checkAndProceed() {
        if (trflist.size() > 0) {
            for (int i = 0; i < trflist.size(); i++) {
                if (trflist.get(i).getTrf_image() == null)
                    trfCheckFlag = true;
            }
            if (trfCheckFlag) {
                trfCheckFlag = false;
                Toast.makeText(mActivity, ToastFile.TRF_UPLOAD_CHECK, Toast.LENGTH_SHORT).show();
            } else {
                doFinalWoe();
            }
        } else {
            doFinalWoe();
        }
    }

    private void doFinalWoe() {
        if (typeName.equalsIgnoreCase("DPS") || typeName.equalsIgnoreCase("HOME")) {
            if (PaymentType == 1) {
                Intent intent = new Intent(Scan_Barcode_Outlabs.this, WOEPaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", preferences.getString("name", ""));
                intent.putExtra("mobile", preferences.getString("kycinfo", ""));
                intent.putExtra("amount", b2b_rate);
                intent.putExtra("email", EMAIL_ID);
                startActivity(intent);
            } else if (PaymentType == 2) {
                final String[] paymentItems = new String[]{"Cash", "Digital"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                builder.setTitle("Choose payment mode")
                        .setItems(paymentItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (paymentItems[which].equals("Cash")) {

                                    androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
                                    builder1.setMessage("Confirm amount received ₹ " + b2b_rate + "")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    WOE();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    Intent intent = new Intent(Scan_Barcode_Outlabs.this, WOEPaymentActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("name", preferences.getString("name", ""));
                                    intent.putExtra("mobile", preferences.getString("kycinfo", ""));
                                    intent.putExtra("amount", b2b_rate);
                                    intent.putExtra("email", EMAIL_ID);
                                    startActivity(intent);

                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                WOE();
            }
        } else {
            WOE();
        }

    }

    private void WOE() {


        patientName = preferences.getString("name", "");
        patientYear = preferences.getString("age", "");
        patientYearType = preferences.getString("ageType", "");
        patientGender = preferences.getString("gender", "");

//        brandName = preferences.getString("WOEbrand", null);

        if (GlobalClass.isNull(getBrand_name)) {
            getBrand_name = preferences.getString("WOEbrand", "");
        } else if (brandName.equalsIgnoreCase("EQNX")) {
            getBrand_name = "WHATERS";
        } /*else {
            brandName = preferences.getString("WOEbrand", "");
        }*/

        typeName = preferences.getString("woetype", "");
        sampleCollectionDate = preferences.getString("date", "");
        sampleCollectionTime = preferences.getString("sct", "");
        sr_number = preferences.getString("SR_NO", "");
        pass_to_api = Integer.parseInt(sr_number);

        referenceBy = preferences.getString("refBy", "");
        ////////////////////////////////////////////////////////////////////////
        sampleCollectionPoint = preferences.getString("labAddress", "");
        sampleGivingClient = preferences.getString("labname", "");

        ////////////////////////////////////////////////////////////////////////

        refeID = preferences.getString("refId", "");
        labAddress = preferences.getString("labAddress", "");
        labID = preferences.getString("labIDaddress", "");
        labName = preferences.getString("labname", "");
        btechID = preferences.getString("btechIDToPass", "");
        campID = preferences.getString("getcampIDtoPass", "");
        homeaddress = preferences.getString("patientAddress", "");
        getFinalPhoneNumberToPost = preferences.getString("kycinfo", "");
        getPincode = preferences.getString("pincode", "");
        EMAIL_ID = preferences.getString("EMAIL_ID", "");
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


        POstQue = GlobalClass.setVolleyReq(Scan_Barcode_Outlabs.this);

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
//        woe.setADDITIONAL1("CPL");
        woe.setADDITIONAL1(ADDITIONAL1);
        woe.setBCT_ID(btechID);
        woe.setBRAND(getBrand_name);
        woe.setCAMP_ID(campID);
        woe.setCONT_PERSON("");
        woe.setCONTACT_NO(getFinalPhoneNumberToPost);
        if (Constants.selectedPatientData != null && !GlobalClass.isNull(Constants.selectedPatientData.getPatientId())) {
            woe.setCUSTOMER_ID(Constants.selectedPatientData.getPatientId());  // TODO If user has selected patient details from Dropdownlist
        } else {
            woe.setCUSTOMER_ID("");
        }

        woe.setEMAIL_ID("" + EMAIL_ID);

       /* if (Constants.selectedPatientData != null && !GlobalClass.isNull(Constants.selectedPatientData.getEmail())) {
            woe.setEMAIL_ID(Constants.selectedPatientData.getEmail());   // TODO If user has selected patient details from Dropdownlist
        } else {
            woe.setEMAIL_ID(getFinalEmailIdToPost);
        }*/
        woe.setDELIVERY_MODE(2);
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
        woe.setWO_MODE("CLISO APP");
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


        if (Global.isoffline || !GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
            Gson trfgson = new GsonBuilder().create();
            String trfjson = trfgson.toJson(trflist);
            myPojoWoe.setTrfjson(trfjson);

            myPojoWoe.setVialimage(vialimg_file);
        }

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

        if (Global.isoffline) {
            StoreWOEOffline(json);
        } else {
            if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs.this)) {
                StoreWOEOffline(json);
            } else {
                if (!flagcallonce) {
                    flagcallonce = true;
                    barProgressDialog = new ProgressDialog(Scan_Barcode_Outlabs.this);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Api.finalWorkOrderEntry, jsonObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e(TAG, "onResponse: RESPONSE" + response);
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                Gson woeGson = new Gson();
                                WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);
                                barcode_patient_id = woeResponseModel.getBarcode_patient_id();
                                Log.e(TAG, "BARCODE PATIENT ID --->" + barcode_patient_id);
                                message = woeResponseModel.getMessage();
                                if (woeResponseModel != null) {
                                    if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                                        if (trflist.size() > 0) {
                                            new LogUserActivityTagging(mActivity, "WOE-NOVID", barcode_patient_id);

                                            Global.OTPVERIFIED = false;
                                            GlobalClass.transID = "";
                                            new AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs.this, mActivity, api_key, user, barcode_patient_id, trflist, vialimg_file).execute();
                                        } else {
                                            getUploadFileResponse();
                                        }
                                    } else if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase(caps_invalidApikey)) {
                                        GlobalClass.redirectToLogin(Scan_Barcode_Outlabs.this);
                                    } else {
                                        flagcallonce = false;
                                        TastyToast.makeText(Scan_Barcode_Outlabs.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                } else {
                                    flagcallonce = false;
                                    TastyToast.makeText(Scan_Barcode_Outlabs.this, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            flagcallonce = false;
                            if (error != null) {
                            } else {
                                //Log.v(TAG,error);
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
                } else {
                    if (trflist.size() > 0) {
                        new AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs.this, mActivity, api_key, user, barcode_patient_id, trflist, vialimg_file).execute();
                    } else {
                        getUploadFileResponse();
                    }
                }
            }
        }
    }

    private void StoreWOEOffline(String json) {
        barcodes1 = TextUtils.join(",", getBarcodeArrList);
        if (!flagcallonce) {
            flagcallonce = true;
            boolean isInserted = myDb.insertData(barcodes1, json);
            if (isInserted) {
                TastyToast.makeText(Scan_Barcode_Outlabs.this, ToastFile.woeSaved, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("payment", getWrittenAmt);
                bundle.putString("TotalAmt", getAmount);
                bundle.putString("selectedTest", testsData);
                bundle.putString("patient_id", barcode_patient_id);
                bundle.putString("Outlbbarcodes", barcodes1);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (!GlobalClass.isNull(GlobalClass.transID)) {
            WOE();
        }
    }

    public void getprescupload() {
        Intent intent = new Intent(Scan_Barcode_Outlabs.this, SummaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("payment", getWrittenAmt);
        bundle.putString("TotalAmt", getAmount);
        bundle.putString("selectedTest", testsData);
        bundle.putString("patient_id", barcode_patient_id);
        bundle.putString("Outlbbarcodes", barcodes1);

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
    }

    public void getLocationResponse(GetLocationRespModel getLocationRespModel) {
        try {
            if (!GlobalClass.isNull(getLocationRespModel.getResID()) && getLocationRespModel.getResID().equalsIgnoreCase(Constants.RES0000)) {
                if (!GlobalClass.isNull(getLocationRespModel.getProcessAt())) {
                    tv_lab.setText("" + getLocationRespModel.getProcessAt());
                    if (getLocationRespModel.getProcessAt().equalsIgnoreCase("CPL")) {
                        tv_lab.setTextColor(mActivity.getResources().getColor(R.color.colorred));
                    } else if (getLocationRespModel.getProcessAt().equalsIgnoreCase("ZPL")) {
                        tv_lab.setTextColor(mActivity.getResources().getColor(R.color.tabindicatorColor));
                    } else if (getLocationRespModel.getProcessAt().equalsIgnoreCase("RPL")) {
                        tv_lab.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                    }
                    tv_location.setText(Html.fromHtml("<b>Note: </b> This sample will be processed at "));
                    ADDITIONAL1 = getLocationRespModel.getProcessAt();
                } else {
                    tv_location.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(mActivity, "" + getLocationRespModel.getResponse(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}