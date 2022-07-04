package com.example.e5322.thyrosoft.RevisedScreenNewUser;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.WOEPaymentActivity;
import com.example.e5322.thyrosoft.Adapter.AdapterBarcode_New;
import com.example.e5322.thyrosoft.Adapter.BrandAdapter;
import com.example.e5322.thyrosoft.Adapter.SampleTypeBarcodeAdapter;
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
import com.example.e5322.thyrosoft.GpsTracker;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.GetLocationReqModel;
import com.example.e5322.thyrosoft.Models.GetLocationRespModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.RequestModels.GeoLocationRequestModel;
import com.example.e5322.thyrosoft.Models.RequestModels.UploadPrescRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.example.e5322.thyrosoft.WOE.ProductWithBarcode;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class Scan_Barcode_ILS_New extends AppCompatActivity implements RecyclerInterface {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    public static ArrayList<String> labAlerts;
    public static RequestQueue sendGPSDetails;
    public static RequestQueue POstQue;
    public IntentIntegrator scanIntegrator;
    public String specimenttype1;
    public int position1 = 0;
    public String getFinalPhoneNumberToPost;
    SharedPreferences prefs, profile_pref;
    String setLocation = null;
    EditText enterAmt, edt_confirm_amt, edt_bsValue, edt_bpValue;
    TextView title;
    Button next;
    ImageView back;
    LocationManager locationManager;
    GpsTracker gpsTracker;
    TextView lab_alert_spin;
    ImageView home;
    File trf_img = null;
    LinearLayout sample_type_linear, amt_collected_and_total_amt_ll, ll_bs, ll_bp;
    ArrayList<BaseModel> selctedTest;
    ArrayList<String> setSpecimenTypeCodes;
    SharedPreferences preferences, prefe;
    String brandName, typeName;
    TextView show_selected_tests_data, setAmt;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recycler_barcode;
    Uri imageUri;
    String getTestSelection, totalamt;
    String getStateName, getCountryName, getCityName;
    Bitmap bitmapimage;
    SharedPreferences savepatientDetails;
    ArrayList<BarcodelistModel> barcodelists;
    ArrayList<String> getBarcodeArrList;
    ProgressDialog barProgressDialog;
    BarcodelistModel barcodelist;
    boolean flagcallonce = false;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    ArrayList<String> saveLocation;
    //    ArrayList<String> rateWiseLocation;
    ArrayList<TRFModel> trflist = new ArrayList<>();
    RadioGroup location_radio_grp;
    RadioButton cpl_rdo, rpl_rdo;
    LinearLayout ll_uploadTRF, ll_location_note;
    RecyclerView rec_trf;
    LinearLayoutManager linearLayoutManager1;
    Activity mActivity;
    Button btn_choosefile;
    LinearLayout lin_preview;
    TextView txt_fileupload;
    Bitmap vialbitmap;
    List<String> imagelist = new ArrayList<>();
    List<String> imagelist_per = new ArrayList<>();
    MainModel mainModel;
    Spinner spr_leterhead;
    LinearLayout ll_letterhead;
    RadioGroup rg_brand;
    TextView tv_viewsample;
    ConnectionDetector cd;
    RecyclerView recy_brand;
    LinearLayout ll_prescription, lin_preview_pres;
    Button btn_choosefile_presc;
    TextView txt_fileupload_pres;
    TextView tv_location, tv_lab;
    Dialog sampledialog;
    BottomSheetDialog bottomSheetDialog;
    String communication, commModes;
    private int rate_percent, max_amt, tpc_perc;
    private int B2B_RATE, RPL_RATE, HARDCODE_CPL_RATE;
    private MyPojo myPojo;
    private boolean barcodeExistsFlag = false;
    private boolean isBarcodeNumeric = false;
    private boolean trfCheckFlag = false;
    private SpinnerDialog spinnerDialog;
    private String selectedProduct = "";
    private String userChoosenTask;
    private boolean GpsStatus;
    private ArrayList<String> temparraylist;
    private ArrayList<ProductWithBarcode> getproductDetailswithBarcodes;
    private AdapterBarcode_New adapterBarcode;
    private String getAmount, b2b_rate;
    private AlertDialog.Builder alertDialog;
    private String getCollectedAmt;
    private String testsnames;
    private DatabaseHelper myDb;
    private Cursor cursor;
    private int PERMISSION_REQUEST_CODE = 200;
    private int PICK_PHOTO_FROM_CAMERA = 201;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private Camera camera;
    private String amt_collected;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String sampleCollectionDate;
    private String message;
    private String ageType;
    private String getFinalAddressToPost;
    private String getFinalEmailIdToPost;
    private String TAG = Scan_Barcode_ILS_New.this.getClass().getSimpleName();
    private String outputDateStr;
    private String nameString;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    private String getageType;
    private String genderType;
    private String getFinalDate;
    private String version;
    private int versionCode;
    private Global globalClass;
    private String referenceID;
    private String patientAddress;
    private String referrredBy;
    private String labIDaddress;
    private String barcode_patient_id;
    private String btechIDToPass;
    private String btechNameToPass;
    private String getcampIDtoPass;
    private String kycinfo;
    private String typename;
    private String labAddress;
    private String labname;
    private String getBrand_name;
    private String sr_number;
    private int pass_to_api;
    private String lab_alert_pass_toApi;
    private String testsCodesPassingToProduct;
    private String barcode_id;
    private String getRemark;
    private String getPincode;
    private File vialimg_file;
    private File presc_file;
    private boolean isvial = false, isPOCTTest = false, ispresc = false;
    private String LetterheadURL;
    private String brabdurl;
    private int nhl_rate;
    private String EMAIL_ID;
    private int PaymentType;
    private boolean isprescition = false;
    private String ADDITIONAL1 = "";
    private SampleTypeBarcodeAdapter sampleTypeBarcodeAdapter;


    @SuppressLint({"WrongViewCast", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);

        mActivity = Scan_Barcode_ILS_New.this;
        cd = new ConnectionDetector(mActivity);
        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        setAmt = (TextView) findViewById(R.id.setAmt);
        title = (TextView) findViewById(R.id.title);
        sample_type_linear = (LinearLayout) findViewById(R.id.sample_type_linear);
        amt_collected_and_total_amt_ll = (LinearLayout) findViewById(R.id.amt_collected_and_total_amt_ll);
        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
//        companycost_test = (TextView) findViewById(R.id.companycost_test);
        location_radio_grp = (RadioGroup) findViewById(R.id.location_radio_grp);
        cpl_rdo = (RadioButton) findViewById(R.id.cpl_rdo);
        rpl_rdo = (RadioButton) findViewById(R.id.rpl_rdo);
        enterAmt = (EditText) findViewById(R.id.enterAmt);
        edt_confirm_amt = (EditText) findViewById(R.id.edt_confirm_amt);
        next = (Button) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        ll_uploadTRF = (LinearLayout) findViewById(R.id.ll_uploadTRF);
        rec_trf = (RecyclerView) findViewById(R.id.rec_trf);
        ll_location_note = findViewById(R.id.ll_location_note);

        prefe = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefe.getString("WOEbrand", "");
        typeName = prefe.getString("woetype", null);

//        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
//        PaymentType = getProfileName.getInt("PaymentType", 0);
        PaymentType = 0;


        btn_choosefile = findViewById(R.id.btn_choosefile);
        txt_fileupload = findViewById(R.id.txt_fileupload);
        lin_preview = findViewById(R.id.lin_preview);
        rg_brand = findViewById(R.id.rg_brand);
        recy_brand = findViewById(R.id.recy_brand);

        spr_leterhead = findViewById(R.id.spr_leterhead);
        ll_letterhead = findViewById(R.id.ll_letterhead);
        tv_viewsample = findViewById(R.id.tv_viewsample);

        ll_bs = findViewById(R.id.ll_bs);
        ll_bp = findViewById(R.id.ll_bp);
        edt_bsValue = findViewById(R.id.edt_bsValue);
        edt_bpValue = findViewById(R.id.edt_bpValue);
        tv_location = findViewById(R.id.tv_location);
        tv_lab = findViewById(R.id.tv_lab);

        ll_prescription = findViewById(R.id.ll_prescription);
        btn_choosefile_presc = findViewById(R.id.btn_choosefile_presc);
        lin_preview_pres = findViewById(R.id.lin_preview_pres);
        txt_fileupload_pres = findViewById(R.id.txt_fileupload_pres);

        ll_location_note.setVisibility(cd.isConnectingToInternet() ? View.VISIBLE : View.GONE);

        btn_choosefile_presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Scan_Barcode_ILS_New.this)
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

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        final String json = appSharedPrefs.getString("MyObject", "");
        mainModel = gson.fromJson(json, MainModel.class);


        btn_choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openCamera();
                GlobalClass.cropImageActivity(Scan_Barcode_ILS_New.this, 0);

                isvial = true;
            }
        });
        txt_fileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setviewpager(imagelist);
            }
        });


        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        POstQue = GlobalClass.setVolleyReq(Scan_Barcode_ILS_New.this);

        linearLayoutManager1 = new LinearLayoutManager(Scan_Barcode_ILS_New.this);
        rec_trf.setLayoutManager(linearLayoutManager1);
        title.setText("Scan Barcode");
        GlobalClass.finalspecimenttypewiselist = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        try {
            String seltest = bundle.getString("key");
            Type listType = new TypeToken<ArrayList<BaseModel>>() {
            }.getType();
            selctedTest = new Gson().fromJson(seltest, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myDb = new DatabaseHelper(getApplicationContext());
        CheckGpsStatus();

        SetBrandLetterValues();

        if (GpsStatus) {
            gpsTracker = new GpsTracker(Scan_Barcode_ILS_New.this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                latitudePassTOAPI = String.valueOf(latitude);
                longitudePassTOAPI = String.valueOf(longitude);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null) {
                    if (addresses.size() != 0) {
                        getStateName = addresses.get(0).getAdminArea();
                        getCountryName = addresses.get(0).getCountryName();
                        getCityName = addresses.get(0).getLocality();
                        System.out.println(addresses.get(0).getAdminArea());
                        System.out.println(addresses.get(0).getCountryName());
                        System.out.println(addresses.get(0).getLocality());
                    }
                } else {
                    getStateName = "";
                    getCountryName = "";
                    getCityName = "";
                }
            } else {
                gpsTracker.showSettingsAlert();
            }

        } else {
            showSettingsAlert();
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        //todo get rate percent from profile details
        profile_pref = getSharedPreferences("profile", MODE_PRIVATE);
        rate_percent = profile_pref.getInt(Constants.rate_percent, 0);
        max_amt = profile_pref.getInt(Constants.max_amt, 0);
        tpc_perc = profile_pref.getInt(Constants.tpcPercent, 0);

        System.out.println("<< rate percent >>" + rate_percent);
        System.out.println("<< max amount >>" + max_amt);
        System.out.println("<< tcp_perc amount >>" + tpc_perc);

        savepatientDetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        nameString = savepatientDetails.getString("name", "");
        getFinalAge = savepatientDetails.getString("age", "");
        saveGenderId = savepatientDetails.getString("gender", "");
        getFinalTime = savepatientDetails.getString("sct", "");
        getageType = savepatientDetails.getString("ageType", "");
        getFinalDate = savepatientDetails.getString("date", "");

        labname = savepatientDetails.getString("labname", "");
        labAddress = savepatientDetails.getString("labAddress", "");

        patientAddress = savepatientDetails.getString("patientAddress", "");
        communication = savepatientDetails.getString("Communication", "");
        commModes = savepatientDetails.getString("CommModes", "");

        referrredBy = savepatientDetails.getString("refBy", "");
        referenceID = savepatientDetails.getString("refId", "");
        labIDaddress = savepatientDetails.getString("labIDaddress", "");
        btechIDToPass = savepatientDetails.getString("btechIDToPass", "");
        btechNameToPass = savepatientDetails.getString("btechNameToPass", "");
        getcampIDtoPass = savepatientDetails.getString("getcampIDtoPass", "");
        kycinfo = savepatientDetails.getString("kycinfo", "");
        typename = savepatientDetails.getString("woetype", "");
        sr_number = savepatientDetails.getString("SR_NO", "");
        getPincode = savepatientDetails.getString("pincode", "");
        EMAIL_ID = savepatientDetails.getString("EMAIL_ID", "");

        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", "");
        patientYear = preferences.getString("year", "");
        patientYearType = preferences.getString("yearType", "");
        patientGender = preferences.getString("gender", "");
        getFinalAddressToPost = GlobalClass.setHomeAddress;
        getFinalPhoneNumberToPost = GlobalClass.getPhoneNumberTofinalPost;
        getFinalEmailIdToPost = GlobalClass.getFinalEmailAddressToPOst;

        pass_to_api = Integer.parseInt(sr_number);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView

        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.marronFaint));
        }

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(Scan_Barcode_ILS_New.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();

        try {
            if (myPojo.getMASTERS().getLAB_ALERTS() != null && myPojo.getMASTERS() != null) {
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

        spinnerDialog = new SpinnerDialog(Scan_Barcode_ILS_New.this, labAlerts, "Search Lab Alerts", "Close");// With No Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                lab_alert_spin.setText(s);
            }
        });


        ArrayList<String> getTestNameToPAss = new ArrayList<>();
        getAmount = bundle.getString("payment");
        b2b_rate = bundle.getString("b2b_rate");
        testsnames = bundle.getString("writeTestName");


        Log.e(TAG, "b2b_rate--->" + b2b_rate);
        getTestNameToPAss = bundle.getStringArrayList("TestCodesPass");


        for (int i = 0; i < selctedTest.size(); i++) {
            if (getTestNameToPAss.contains("PPBS") && getTestNameToPAss.contains("RBS")) {
                if (selctedTest.get(i).getProduct().equalsIgnoreCase("RBS")) {
                    selctedTest.remove(selctedTest.get(i));
                }
            }
        }


        Log.e(TAG, "TRF list " + trflist.size());

        if (getTestNameToPAss.contains("PPBS") && getTestNameToPAss.contains("RBS")) {
            getTestNameToPAss.remove("RBS");
            testsCodesPassingToProduct = TextUtils.join(",", getTestNameToPAss);
        } else {
            testsCodesPassingToProduct = TextUtils.join(",", getTestNameToPAss);
        }


        setAmt.setText(getAmount);
        show_selected_tests_data.setText(testsnames);
        System.out.println("" + selctedTest.toString());
        prefs = getSharedPreferences("showSelectedTest", MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(Scan_Barcode_ILS_New.this);
        recycler_barcode.setLayoutManager(linearLayoutManager);
        sample_type_linear.setVisibility(View.GONE);
        temparraylist = new ArrayList<>();
        ArrayList<Integer> rateList = new ArrayList<>();
        int totalcount = 0;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Scan_Barcode_ILS_New.this);
            }
        });
        ArrayList<String> getTestCode = new ArrayList<>();

        try {
            if (GlobalClass.isArraylistNotNull(selctedTest)) {
                for (int i = 0; i < selctedTest.size(); i++) {
                    if (!GlobalClass.isNull(selctedTest.get(i).getProduct())) {
                        getTestCode.add(selctedTest.get(i).getProduct());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (GlobalClass.isArraylistNotNull(selctedTest)) {
                for (int i = 0; i < selctedTest.size(); i++) {
                    if (selctedTest.get(i).isPrescription()) {
                        isprescition = selctedTest.get(i).isPrescription();
                        ll_prescription.setVisibility(View.VISIBLE);
                        break;
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


        if (selctedTest != null && selctedTest.size() > 0) {
            for (int i = 0; i < selctedTest.size(); i++) {
                totalcount = totalcount + Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                Log.e(TAG, "TRF list data --->" + selctedTest.get(i).getTrf());
                Log.e(TAG, "Location list data --->" + selctedTest.get(i).getLocation());

                if (selctedTest.get(i).getTrf() != null && !selctedTest.get(i).getTrf().equals("")) {
                    if (selctedTest.get(i).getTrf().equalsIgnoreCase("Yes")) {
                        TRFModel trfModel = new TRFModel();
                        trfModel.setProduct(selctedTest.get(i).getProduct());
                        trflist.add(trfModel);
                        callTRFAdapter(trflist);
                        Log.e(TAG, "TRF list--->" + trflist.size());
                    }
                }
                Log.e(TAG, "onCreate: 11 " + totalcount);
            }
        }

        if (selctedTest != null && selctedTest.size() > 0) {
            for (int i = 0; i < selctedTest.size(); i++) {
                if (selctedTest.get(i).isPOCT()) {
                    isPOCTTest = true;
                    break;
                }
            }
        }

        if (isPOCTTest) {
            ll_bs.setVisibility(View.VISIBLE);
            ll_bp.setVisibility(View.VISIBLE);
        } else {
            ll_bs.setVisibility(View.GONE);
            ll_bp.setVisibility(View.GONE);
        }

        List<String> productlist = new ArrayList<>();
        if (selctedTest != null) {
            getproductDetailswithBarcodes = new ArrayList<>();
            saveLocation = new ArrayList<>();
            for (int i = 0; i < selctedTest.size(); i++) {
                for (int j = 0; j < selctedTest.get(i).getBarcodes().length; j++) {
                    ProductWithBarcode productWithBarcode = new ProductWithBarcode();
                    temparraylist.add(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                    productWithBarcode.setBarcode(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                    productWithBarcode.setProduct(selctedTest.get(i).getBarcodes()[j].getCode());
                    productWithBarcode.setFasting(selctedTest.get(i).getFasting());
                    productWithBarcode.setType(selctedTest.get(i).getType());
                    getproductDetailswithBarcodes.add(productWithBarcode);
                }
                saveLocation.add(selctedTest.get(i).getLocation());
            }

        }

  /*      if (saveLocation != null && !saveLocation.isEmpty()) {
//            for (int i = 0; i < rateWiseLocation.size(); i++) {
            if (saveLocation.contains("CPL")) {
                location_radio_grp.setVisibility(View.GONE);
                setLocation = "CPL";
            } else {
                location_radio_grp.setVisibility(View.VISIBLE);
                rpl_rdo.setChecked(true);
                cpl_rdo.setChecked(false);
                setLocation = "RPL";
            }
//            }
        }
*/
        rpl_rdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation = "RPL";
            }
        });

        cpl_rdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation = "CPL";
            }
        });

        setAmt.setText(String.valueOf(getAmount));


        for (int i = 0; i < temparraylist.size(); i++) {
            if (!temparraylist.get(i).equalsIgnoreCase("FLUORIDE")) {
                Set<String> hs = new HashSet<>();
                hs.addAll(temparraylist);
                temparraylist.clear();
                temparraylist.addAll(hs);
            }
        }

        for (int i = 0; i < temparraylist.size(); i++) {
            if (!temparraylist.get(i).equalsIgnoreCase("FLUORIDE")) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                setSpecimenTypeCodes = new ArrayList<>();
                for (int j = 0; j < getproductDetailswithBarcodes.size(); j++) {
                    if (temparraylist.get(i).equalsIgnoreCase(getproductDetailswithBarcodes.get(j).getBarcode())) {
                        setSpecimenTypeCodes.add(getproductDetailswithBarcodes.get(j).getProduct());
                    }
                }
                scannedBarcodeDetails.setSpecimen_type(temparraylist.get(i));
                for (int k = 0; k < setSpecimenTypeCodes.size(); k++) {
                    HashSet<String> listToSet = new HashSet<String>(setSpecimenTypeCodes);
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    String setProducts = TextUtils.join(",", listWithoutDuplicates);
                    HashSet<String> test = new HashSet<String>(Arrays.asList(setProducts.split(",")));
                    if (test.contains("RBS") && test.contains("PPBS")) {
                        test.remove("RBS");
                    }
                    String setFinalProducts = TextUtils.join(",", test);
                    scannedBarcodeDetails.setProducts(setFinalProducts);
                }
                GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
            }
        }

        for (int j = 0; j < getproductDetailswithBarcodes.size(); j++) {
            if (getproductDetailswithBarcodes.get(j).getBarcode().equalsIgnoreCase("FLUORIDE") && getproductDetailswithBarcodes.get(j).getFasting().equalsIgnoreCase("FASTING")) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                scannedBarcodeDetails.setFasting(getproductDetailswithBarcodes.get(j).getFasting());
                scannedBarcodeDetails.setProducts(getproductDetailswithBarcodes.get(j).getProduct());
                scannedBarcodeDetails.setSpecimen_type("(F)" + getproductDetailswithBarcodes.get(j).getBarcode());
                scannedBarcodeDetails.setType(getproductDetailswithBarcodes.get(j).getType());
                GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
            } else if (getproductDetailswithBarcodes.get(j).getBarcode().equalsIgnoreCase("FLUORIDE") && getproductDetailswithBarcodes.get(j).getFasting().equalsIgnoreCase("NON FASTING")) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                scannedBarcodeDetails.setFasting(getproductDetailswithBarcodes.get(j).getFasting());
                scannedBarcodeDetails.setProducts(getproductDetailswithBarcodes.get(j).getProduct());
                scannedBarcodeDetails.setSpecimen_type(getproductDetailswithBarcodes.get(j).getBarcode());
                scannedBarcodeDetails.setType(getproductDetailswithBarcodes.get(j).getType());
                GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
            }
        }

        //TODO commented to remove CPL RPL as per input 12-10-2020
        /*rateWiseLocation = new ArrayList<>();
        for (int i = 0; i < selctedTest.size(); i++) {
            if (!GlobalClass.isNull(selctedTest.get(i).getIsCPL())) {
                if (selctedTest.get(i).getIsCPL().equalsIgnoreCase("1")) {
                    rateWiseLocation.add("CPL");
                } else {
                    rateWiseLocation.add("RPL");
                }
            } else {
                rateWiseLocation.add("CPL");
            }
        }*/


        if (typeName.equals("ILS")) {
            if (selctedTest.size() != 0) {
                totalcount = 0;
                for (int i = 0; i < selctedTest.size(); i++) {
                    int getAmtBilledRate = 0;
                    /*if (!rateWiseLocation.isEmpty()) {
                        if (rateWiseLocation.contains("CPL")) {
                            //todo comented to hide CPL RPL rates as per the input 09-10-2020
                            *//*if (!GlobalClass.isNull(selctedTest.get(i).getRate().getCplr())) {
                                getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getRate().getCplr());
                            } else {*//*
                            getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                            *//*}*//*
                        } else {
                            //todo comented to hide CPL RPL rates as per the input 09-10-2020
                            *//*if (!GlobalClass.isNull(selctedTest.get(i).getRate().getRplr())) {
                                getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getRate().getRplr());
                            } else {*//*
                            getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                            *//*}*//*
                        }

                    }*/
                    getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                    totalcount = totalcount + getAmtBilledRate;
                    Log.e(TAG, "onCreate: 11 " + totalcount);
                    amt_collected_and_total_amt_ll.setVisibility(View.GONE);
                    setAmt.setText(String.valueOf(totalcount));
                    enterAmt.setText(String.valueOf(totalcount));
                }
            }
        } else {
            amt_collected_and_total_amt_ll.setVisibility(View.VISIBLE);
        }

        adapterBarcode = new AdapterBarcode_New(Scan_Barcode_ILS_New.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
        adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
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

        recycler_barcode.setAdapter(adapterBarcode);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalamt = getAmount;
                getTestSelection = show_selected_tests_data.getText().toString();
                String getLab_alert = lab_alert_spin.getText().toString();

                if (getLab_alert.equals("SELECT LAB ALERTS")) {
                    lab_alert_pass_toApi = "";
                } else {
                    lab_alert_pass_toApi = lab_alert_spin.getText().toString();
                }
                if (!typeName.equalsIgnoreCase("ILS")) {
                    B2B_RATE = 0;//TODO renamed from CPL_RATE to B2B_RATE to remove CPL rates
                    RPL_RATE = 0;
                    HARDCODE_CPL_RATE = 0;

                    for (int i = 0; i < selctedTest.size(); i++) {
                        if (selctedTest.get(i).getIsAB() == 1) {
                            HARDCODE_CPL_RATE = HARDCODE_CPL_RATE + Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                        } else {
                            B2B_RATE = B2B_RATE + Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                        }
                    }

                    getCollectedAmt = enterAmt.getText().toString();

                    if (getCollectedAmt.equals("")) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Please enter collected amount", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(getCollectedAmt) > Integer.parseInt(totalamt)) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Collected amount should not be greater than total amount", Toast.LENGTH_SHORT).show();
                    } else if (GlobalClass.isNull(edt_confirm_amt.getText().toString())) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Confirm Amount Collected", Toast.LENGTH_SHORT).show();
                    } else if (!edt_confirm_amt.getText().toString().equalsIgnoreCase(getCollectedAmt)) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Amount Mismatched", Toast.LENGTH_SHORT).show();
                    } else if (!checkBSBPValidation()) {

                    } else if (!checkRateVal()) {

                    } else if (GlobalClass.isNull(getTestSelection)) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Select test", Toast.LENGTH_SHORT).show();
                    } else if (ll_letterhead.getVisibility() == View.VISIBLE) {
                        if (GlobalClass.isNull(getBrand_name)) {
                            Toast.makeText(mActivity, "Select Brand", Toast.LENGTH_SHORT).show();
                        } /*else if (vialimg_file == null) {
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
                    } else if (location_radio_grp.getVisibility() == View.VISIBLE) {
                        if (setLocation == null) {
                            TastyToast.makeText(Scan_Barcode_ILS_New.this, "Please select location", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                        } /*else if (vialimg_file == null) {
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
                    } /*else if (vialimg_file == null) {
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
                } else {
                    getCollectedAmt = enterAmt.getText().toString();
                    if (getCollectedAmt.equals("")) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Please enter collected amount", Toast.LENGTH_SHORT).show();
                    } else if (GlobalClass.isNull(getTestSelection)) {
                        Toast.makeText(Scan_Barcode_ILS_New.this, "Select test", Toast.LENGTH_SHORT).show();
                    } else if (!checkBSBPValidation()) {

                    } else if (ll_letterhead.getVisibility() == View.VISIBLE) {
                        if (GlobalClass.isNull(getBrand_name)) {
                            Toast.makeText(mActivity, "Select Brand", Toast.LENGTH_SHORT).show();
                        }/* else if (vialimg_file == null) {
                            Toast.makeText(mActivity, "Upload Vial Image", Toast.LENGTH_SHORT).show();
                        }*/ else {
                            checklistData();
                        }
                    } else if (location_radio_grp.getVisibility() == View.VISIBLE) {
                        if (setLocation == null) {
                            TastyToast.makeText(Scan_Barcode_ILS_New.this, "Please select location", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
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
                    } /*else if (vialimg_file == null) {
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
                }

            }
        });
    }

    boolean checkBSBPValidation() {
        if (edt_bsValue.isShown() && edt_bsValue.getText().toString().length() > 0 && Integer.parseInt(edt_bsValue.getText().toString()) < GlobalClass.getBSBPMinMaxValue(mActivity, Constants.BS_MIN)) {
            Toast.makeText(Scan_Barcode_ILS_New.this, GlobalClass.getBSBPValidationMsg(mActivity, Constants.BS_MSG), Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_bsValue.isShown() && edt_bsValue.getText().toString().length() > 0 && Integer.parseInt(edt_bsValue.getText().toString()) > GlobalClass.getBSBPMinMaxValue(mActivity, Constants.BS_MAX)) {
            Toast.makeText(Scan_Barcode_ILS_New.this, GlobalClass.getBSBPValidationMsg(mActivity, Constants.BS_MSG), Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_bpValue.isShown() && edt_bpValue.getText().toString().length() > 0 && Integer.parseInt(edt_bpValue.getText().toString()) < GlobalClass.getBSBPMinMaxValue(mActivity, Constants.BP_MIN)) {
            Toast.makeText(Scan_Barcode_ILS_New.this, GlobalClass.getBSBPValidationMsg(mActivity, Constants.BP_MSG), Toast.LENGTH_SHORT).show();
            return false;
        } else if (edt_bpValue.isShown() && edt_bpValue.getText().toString().length() > 0 && Integer.parseInt(edt_bpValue.getText().toString()) > GlobalClass.getBSBPMinMaxValue(mActivity, Constants.BP_MAX)) {
            Toast.makeText(Scan_Barcode_ILS_New.this, GlobalClass.getBSBPValidationMsg(mActivity, Constants.BP_MSG), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                    for (int j = 0; j < selctedTest.size(); j++) {
                        for (int k = 0; k < selctedTest.get(j).getBrandDtls().size(); k++) {
                            if (newbrandName.get(i).equalsIgnoreCase(selctedTest.get(j).getBrandDtls().get(k).getAlias())) {
                                cnt++;
                            }
                        }
                    }

                    if (cnt == selctedTest.size()) {
                        finallist.add(newbrandName.get(i));
                    }
                }
            }

            if (finallist.size() == 0) {
                ll_letterhead.setVisibility(View.GONE);
            } else {
                ll_letterhead.setVisibility(View.VISIBLE);
            }

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
        if (GlobalClass.CheckArrayList(selctedTest)) {
            for (int i = 0; i < selctedTest.size(); i++) {
                if (GlobalClass.CheckArrayList(selctedTest.get(i).getBrandDtls())) {
                    for (int j = 0; j < selctedTest.get(i).getBrandDtls().size(); j++) {
                        BaseModel.BrandDtlsDTO brandDtlsDTO = new BaseModel.BrandDtlsDTO();
                        brandDtlsDTO.setAlias("" + selctedTest.get(i).getBrandDtls().get(j).getAlias());
                        brandDtlsDTO.setBrandName("" + selctedTest.get(i).getBrandDtls().get(j).getBrandName());
                        brandDtlsDTO.setBrandRate("" + selctedTest.get(i).getBrandDtls().get(j).getBrandRate());
                        brandDtlsDTO.setILSRate("" + selctedTest.get(i).getBrandDtls().get(j).getILSRate());
                        brandDtlsDTO.setRPTFile("" + selctedTest.get(i).getBrandDtls().get(j).getRPTFile());
                        brandDtlsDTO.setFullName("" + selctedTest.get(i).getBrandDtls().get(j).getFullName());
                        entity.add(brandDtlsDTO);

                    }
                }
            }
        }

        return entity;
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

    private boolean checkRateVal() {
        //TODO commented to hide CPL RPL logic as per input 12-10-2020
        /*if (rateWiseLocation != null && rateWiseLocation.size() > 0) {
            if (rateWiseLocation.contains("CPL") && rateWiseLocation.contains("RPL")) {
                RPL_RATE = calcAmount(B2B_RATE, HARDCODE_CPL_RATE, isCOVIDPresent);
                System.out.println("Calculated RPL rate :" + RPL_RATE);
                if (Integer.parseInt(getCollectedAmt) < RPL_RATE) {
                    Toast.makeText(Scan_Barcode_ILS_New.this, getResources().getString(R.string.amtcollval) + " " + RPL_RATE, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (rateWiseLocation.contains("RPL")) {
                RPL_RATE = calcAmount(RPL_RATE, HARDCODE_CPL_RATE, isCOVIDPresent);
                System.out.println("Calculated RPL rate :" + RPL_RATE);
                if (Integer.parseInt(getCollectedAmt) < RPL_RATE) {
                    Toast.makeText(Scan_Barcode_ILS_New.this, getResources().getString(R.string.amtcollval) + " " + RPL_RATE, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (rateWiseLocation.contains("CPL")) {*/
        B2B_RATE = calcAmount(B2B_RATE, HARDCODE_CPL_RATE);
        System.out.println("Calculated CPL rate :" + B2B_RATE);
        if (Integer.parseInt(getCollectedAmt) < B2B_RATE) {
            Toast.makeText(Scan_Barcode_ILS_New.this, getResources().getString(R.string.amtcollval) + " " + B2B_RATE, Toast.LENGTH_SHORT).show();
            return false;
        }
            /*} else {
                Toast.makeText(Scan_Barcode_ILS_New.this, "Location is blank for selected products", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(Scan_Barcode_ILS_New.this, "Location is blank for selected products", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        return true;
    }

    private int calcAmount(int RATE, int HARDCODE_CPL_RATE) {
        try {
            float aFloat = Float.parseFloat(String.valueOf(RATE));
            float per_amt = ((aFloat / 100) * rate_percent);
            System.out.println("Calculated percent rate :" + per_amt);
            int per_amt1 = Math.round(per_amt);
            if (per_amt1 < max_amt) {
                RATE = RATE + per_amt1;
            } else {
                RATE = RATE + max_amt;
            }
            //TODO Added logic for TPC client for rates.
            aFloat = Float.parseFloat(String.valueOf(RATE));
            float per_amt2 = ((aFloat / 100) * tpc_perc);
            int tpcrate = Math.round(per_amt2);
            System.out.println("Calculated TCP Client rate :" + tpcrate);
            RATE = RATE + tpcrate;
            //TODO Added logic for TPC client for rates.
            RATE = RATE + HARDCODE_CPL_RATE;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return RATE;
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

    public void callTRFAdapter(ArrayList<TRFModel> trflist) {
        if (trflist.size() > 0) {
            ll_uploadTRF.setVisibility(View.VISIBLE);
            TRFDisplayAdapter trfDisplayAdapter = new TRFDisplayAdapter(Scan_Barcode_ILS_New.this, trflist);

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

    private void doFinalWoe() {

        if (GlobalClass.finalspecimenttypewiselist != null) {
            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                if (GlobalClass.isNull(GlobalClass.finalspecimenttypewiselist.get(i).getBarcode())) {
                    barcodeExistsFlag = true;
                } else if (GlobalClass.isNumeric(GlobalClass.finalspecimenttypewiselist.get(i).getBarcode())) {
                    isBarcodeNumeric = true;
                }
            }
            if (barcodeExistsFlag) {
                barcodeExistsFlag = false;
                Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.scan_barcode_all, Toast.LENGTH_SHORT).show();
            } else {

                if (isBarcodeNumeric) {
                    isBarcodeNumeric = false;
                    openDialogBox();
                } else {
                    callWOEAPI();
                }
            }
        } else {
            Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.allw_scan, Toast.LENGTH_SHORT).show();
        }
    }

    private void callWOEAPI() {
        if (typeName.equalsIgnoreCase("DPS") || typeName.equalsIgnoreCase("HOME")) {
            if (PaymentType == 1) {
                Intent intent = new Intent(Scan_Barcode_ILS_New.this, WOEPaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name", nameString);
                intent.putExtra("mobile", kycinfo);
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
                                    Intent intent = new Intent(Scan_Barcode_ILS_New.this, WOEPaymentActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("name", nameString);
                                    intent.putExtra("mobile", kycinfo);
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

    public void imgActionClicked(int i) {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
            GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(null);
            adapterBarcode = new AdapterBarcode_New(Scan_Barcode_ILS_New.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
            adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
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
            recycler_barcode.setAdapter(adapterBarcode);
        }

    }

    private void openDialogBox() {

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(this).inflate(R.layout.lay_sample_dialog, this.findViewById(R.id.ll_bottom_sheet));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.setCancelable(false);

        Button btn_ok = bottomSheet.findViewById(R.id.btn_ok);
        RecyclerView recy_sample_type = bottomSheet.findViewById(R.id.recy_sample_type);
        ImageView imgClose = bottomSheet.findViewById(R.id.imgClose);

        sampleTypeBarcodeAdapter = new SampleTypeBarcodeAdapter(Scan_Barcode_ILS_New.this, selctedTest, GlobalClass.finalspecimenttypewiselist);
        recy_sample_type.setAdapter(sampleTypeBarcodeAdapter);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                callWOEAPI();
            }
        });
        bottomSheetDialog.show();
    }

    private void WOE() {
        if (getRemark == null) {
            getRemark = "Manual";
        }

        savepatientDetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        nameString = savepatientDetails.getString("name", "");
        getFinalAge = savepatientDetails.getString("age", "");
        saveGenderId = savepatientDetails.getString("gender", "");
        getFinalTime = savepatientDetails.getString("sct", "");
        getageType = savepatientDetails.getString("ageType", "");
        getFinalDate = savepatientDetails.getString("date", "");


        if (GlobalClass.isNull(getBrand_name)) {
            getBrand_name = savepatientDetails.getString("WOEbrand", "");
        }

        getPincode = savepatientDetails.getString("pincode", "");
        if (!GlobalClass.isNull(getFinalDate)) {
            sampleCollectionDate = getFinalDate;
        }

        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;

        if (!GlobalClass.isNull(sampleCollectionDate)) {
            try {
                date = inputFormat.parse(sampleCollectionDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            outputDateStr = outputFormat.format(date);

            //sampleCollectionTime
            Log.e(TAG, "fetchData: " + outputDateStr);
        }

        if (!GlobalClass.isNull(getageType)) {
            if (getageType.equalsIgnoreCase("Years")) {
                ageType = "Y";
            } else if (getageType.equalsIgnoreCase("Months")) {
                ageType = "M";
            } else if (getageType.equalsIgnoreCase("Days")) {
                ageType = "D";
            }
            //  Log.e(TAG, " getFinalTime---->" + getFinalTime + "  getOnlyTime---> " + getOnlyTime + " ---- cutString ---->" + GlobalClass.cutString);
        }


        String getFulltime = sampleCollectionDate + " " + getFinalTime;
        GlobalClass.Req_Date_Req(getFulltime, "hh:mm a", "HH:mm:ss");


        MyPojoWoe myPojoWoe = new MyPojoWoe();
        Woe woe = new Woe();
        woe.setAADHAR_NO("");
        woe.setADDRESS(patientAddress);
        woe.setAGE(getFinalAge);
        woe.setAGE_TYPE(ageType);
        woe.setALERT_MESSAGE(lab_alert_pass_toApi);
        woe.setAMOUNT_COLLECTED(getCollectedAmt);
        woe.setAMOUNT_DUE(null);
//        woe.setADDITIONAL1(setLocation);
        woe.setADDITIONAL1(ADDITIONAL1);
        woe.setAPP_ID(version);
        woe.setBCT_ID(btechIDToPass);

        woe.setBRAND(getBrand_name);

        woe.setCAMP_ID(getcampIDtoPass);
        woe.setCONT_PERSON("");
        woe.setCONTACT_NO(kycinfo);
        if (Constants.selectedPatientData != null && !GlobalClass.isNull(Constants.selectedPatientData.getPatientId())) {
            woe.setCUSTOMER_ID(Constants.selectedPatientData.getPatientId());  // TODO If user has selected patient details from Dropdownlist
        } else {
            woe.setCUSTOMER_ID("");
        }
               /* if (Constants.selectedPatientData != null && !GlobalClass.isNull(Constants.selectedPatientData.getEmail())) {
                    woe.setEMAIL_ID(Constants.selectedPatientData.getEmail());   // TODO If user has selected patient details from Dropdownlist
                } else {
                    woe.setEMAIL_ID("");
                }*/
        woe.setEMAIL_ID(EMAIL_ID);
        woe.setDELIVERY_MODE(2);
        woe.setENTERED_BY(user);
        woe.setGENDER(saveGenderId);
        woe.setLAB_ADDRESS(labAddress);
        woe.setLAB_ID(labIDaddress);
        woe.setLAB_NAME(labname);
        woe.setLEAD_ID("");
        woe.setMAIN_SOURCE(user);
        woe.setORDER_NO("");
        woe.setOS("unknown");
        woe.setPATIENT_NAME(nameString);
        woe.setPINCODE(getPincode);
        woe.setPRODUCT(testsCodesPassingToProduct);
        woe.setPurpose("mobile application");
        woe.setREF_DR_ID(referenceID);
        woe.setREF_DR_NAME(referrredBy);
        woe.setREMARKS("MOBILE - " + getRemark);
        woe.setSPECIMEN_COLLECTION_TIME(outputDateStr + " " + GlobalClass.cutString + ".000");
        woe.setSPECIMEN_SOURCE("");
        woe.setCommunication1(commModes);
        woe.setCommModes(communication);
        woe.setSR_NO(pass_to_api);
        woe.setSTATUS("N");
        woe.setSUB_SOURCE_CODE(user);
        woe.setTOTAL_AMOUNT(totalamt);
        woe.setTYPE(typename);
        woe.setWATER_SOURCE("");
        woe.setWO_MODE("CLISO APP");
        woe.setWO_STAGE(3);
        woe.setULCcode("");
        woe.setPaymentId("" + GlobalClass.transID);
        woe.setBS_VALUE(edt_bsValue.isShown() ? edt_bsValue.getText().toString().trim() : "");
        woe.setBP_VALUE(edt_bpValue.isShown() ? edt_bpValue.getText().toString().trim() : "");
        myPojoWoe.setWoe(woe);

        barcodelists = new ArrayList<>();
        getBarcodeArrList = new ArrayList<>();

        for (int p = 0; p < GlobalClass.finalspecimenttypewiselist.size(); p++) {
            barcodelist = new BarcodelistModel();
            barcodelist.setSAMPLE_TYPE(GlobalClass.finalspecimenttypewiselist.get(p).getSpecimen_type().equalsIgnoreCase("(F)FLUORIDE") ? "FLUORIDE" : GlobalClass.finalspecimenttypewiselist.get(p).getSpecimen_type());
            barcodelist.setBARCODE(GlobalClass.finalspecimenttypewiselist.get(p).getBarcode());
            getBarcodeArrList.add(GlobalClass.finalspecimenttypewiselist.get(p).getBarcode());
            barcodelist.setTESTS(GlobalClass.finalspecimenttypewiselist.get(p).getProducts());
            barcodelists.add(barcodelist);
        }

        myPojoWoe.setBarcodelistModel(barcodelists);
        myPojoWoe.setWoe_type("WOE");
        myPojoWoe.setApi_key(api_key);//api_key

        if (Global.isoffline || !GlobalClass.isNetworkAvailable(Scan_Barcode_ILS_New.this)) {
            Gson trfgson = new GsonBuilder().create();
            String trfjson = trfgson.toJson(trflist);
            myPojoWoe.setTrfjson(trfjson);

            myPojoWoe.setVialimage(vialimg_file);
        }

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(myPojoWoe);
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* CleverTapHelper cleverTapHelper = new CleverTapHelper(this);
        cleverTapHelper.woeSubmitEvent(user, typeName, testsCodesPassingToProduct, GlobalClass.cutString);*/
        if (Global.isoffline) {
            StoreOfflineWOE(json);
        } else {
            if (!GlobalClass.isNetworkAvailable(Scan_Barcode_ILS_New.this)) {
                StoreOfflineWOE(json);
            } else {
                if (!flagcallonce) {
                    flagcallonce = true;
                    /*barProgressDialog.show();*/

                    barProgressDialog = GlobalClass.ShowprogressDialog(Scan_Barcode_ILS_New.this);
                    //POstQue = GlobalClass.setVolleyReq(Scan_Barcode_ILS_New.this);
                    if (POstQue == null) {
                        POstQue = GlobalClass.setVolleyReq(Scan_Barcode_ILS_New.this);
                    }
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                GlobalClass.hideProgress(Scan_Barcode_ILS_New.this, barProgressDialog);

                                Log.e(TAG, "onResponse: RESPONSE" + response);

                                Gson woeGson = new Gson();
                                WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);

                                if (woeResponseModel != null) {
                                    barcode_patient_id = woeResponseModel.getBarcode_patient_id();

                                    Log.e(TAG, "BARCODE PATIENT ID --->" + barcode_patient_id);

                                    message = woeResponseModel.getMessage();
                                    status = woeResponseModel.getStatus();
                                    barcode_id = woeResponseModel.getBarcode_id();

                                    if (!GlobalClass.isNull(status) && status.equalsIgnoreCase("SUCCESS")) {

                                        new LogUserActivityTagging(mActivity, "WOE-NOVID", barcode_patient_id);

                                        SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                                        editor.remove("testsSElected");
                                        editor.remove("getProductNames");
                                        editor.apply();
                                        Global.OTPVERIFIED = false;
                                        GlobalClass.transID = "";
                                        sendGPSDetails = GlobalClass.setVolleyReq(Scan_Barcode_ILS_New.this);
                                        JSONObject jsonObject = null;
                                        try {
                                            GeoLocationRequestModel requestModel = new GeoLocationRequestModel();
                                            requestModel.setUsername(user);
                                            requestModel.setIMEI(GlobalClass.getIMEINo(mActivity));
                                            requestModel.setCity(getCityName);
                                            requestModel.setState(getStateName);
                                            requestModel.setCountry(getCountryName);
                                            requestModel.setLongitude(longitudePassTOAPI);
                                            requestModel.setLatitude(latitudePassTOAPI);
                                            requestModel.setDeviceName(GlobalClass.getDeviceName());

                                            Gson geoGson = new Gson();
                                            String json = geoGson.toJson(requestModel);
                                            jsonObject = new JSONObject(json);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.sendGeoLocation, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    Log.e(TAG, "onResponse: " + response);
                                                    String finalJson = response.toString();
                                                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                                                    String Response = parentObjectOtp.getString("Response");
                                                    String resId = parentObjectOtp.getString("resId");
                                                    if (resId.equals("RES0000")) {
                                                        //after sending the woe succrssfully
                                                    } else {
                                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, "" + Response, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    }
                                                    if (trflist.size() > 0) {
                                                        new AsyncTaskPost_uploadfile(Scan_Barcode_ILS_New.this, mActivity, api_key, user, barcode_patient_id, trflist, vialimg_file).execute();
                                                    } else {
                                                        getUploadFileResponse();
                                                    }
                                                } catch (JSONException e) {
                                                    TastyToast.makeText(Scan_Barcode_ILS_New.this, "", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new com.android.volley.Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                if (error != null) {
                                                } else {
                                                    System.out.println(error);
                                                }
                                            }
                                        });
                                        sendGPSDetails.add(jsonObjectRequest1);
                                        Log.e(TAG, "onResponse: url" + jsonObjectRequest1);
                                        Log.e(TAG, "onResponse: json" + jsonObject);
                                    } else if (!GlobalClass.isNull(message) && message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {
                                        flagcallonce = false;
                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        final AlertDialog alertDialog = new AlertDialog.Builder(Scan_Barcode_ILS_New.this).create();
                                        alertDialog.setTitle("Update Ledger !");
                                        alertDialog.setMessage(ToastFile.update_ledger);
                                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(Scan_Barcode_ILS_New.this, Payment_Activity.class);
                                                i.putExtra("COMEFROM", "Scan_Barcode_ILS_New");
                                                startActivity(i);
                                            }
                                        });
                                        alertDialog.show();
                                    } else if (!GlobalClass.isNull(status) && status.equalsIgnoreCase(caps_invalidApikey)) {
                                        GlobalClass.redirectToLogin(Scan_Barcode_ILS_New.this);
                                    } else {
                                        flagcallonce = false;
                                        TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                } else {
                                    flagcallonce = false;
                                    TastyToast.makeText(Scan_Barcode_ILS_New.this, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        /*if (Scan_Barcode_ILS_New.this instanceof Activity) {
                            if (!((Activity) Scan_Barcode_ILS_New.this).isFinishing())
                                barProgressDialog.dismiss();
                        }*/
                            GlobalClass.hideProgress(Scan_Barcode_ILS_New.this, barProgressDialog);
                            flagcallonce = false;
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
                    if (trflist.size() > 0) {
                        new AsyncTaskPost_uploadfile(Scan_Barcode_ILS_New.this, mActivity, api_key, user, barcode_patient_id, trflist, vialimg_file).execute();
                    }
                }
            }
        }
    }

    private void StoreOfflineWOE(String json) {
        String barcodes = TextUtils.join(",", getBarcodeArrList);
        if (!flagcallonce) {
            flagcallonce = true;
            boolean isInserted = myDb.insertData(barcodes, json);
            if (isInserted) {
                TastyToast.makeText(Scan_Barcode_ILS_New.this, ToastFile.woeSaved, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                Intent intent = new Intent(Scan_Barcode_ILS_New.this, SummaryActivity_New.class);
                Bundle bundle = new Bundle();
                bundle.putString("tetsts", getTestSelection);
                bundle.putString("brandname", getBrand_name);
                bundle.putString("location", setLocation);
                bundle.putString("passProdcucts", testsCodesPassingToProduct);
                bundle.putString("TotalAmt", totalamt);
                bundle.putString("CollectedAmt", getCollectedAmt);
                bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
                bundle.putString("patientId", "");
                bundle.putString("barcodes", barcodes);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                TastyToast.makeText(Scan_Barcode_ILS_New.this, ToastFile.woenotSaved, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }
    }


    private void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(Scan_Barcode_ILS_New.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Scan_Barcode_ILS_New.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void CheckGpsStatus() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void putDataToscan(Activity context, int position, String specimenttype) {
        specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();
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
                        /**Below is local path for store our image in folder*/
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_img;
                        trf_img = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_img);
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
                    trf_img = GlobalClass.getCompressedFile(mActivity, trf_img);
                    storeTRFImage(trf_img);
                } catch (IOException e) {
                    Toast.makeText(mActivity, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
                try {
                    if (isvial) {
                        imagelist.clear();
                        // vialbitmap = camera.getCameraBitmap();
                        //   String imageurlvial = camera.getCameraBitmapPath();
//                        vialimg_file = new File(imageurlvial);
                        vialimg_file = ImagePicker.Companion.getFile(data);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + vialimg_file;
                        vialimg_file = new File(destFile);
                        GlobalClass.copyFile(ImagePicker.Companion.getFile(data), vialimg_file);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void passBarcodeData(String s) {
        boolean isbacodeduplicate = false;
        for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
            if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() != null && !GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().isEmpty()) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().equalsIgnoreCase(s)) {
                    isbacodeduplicate = true;
                }
            }
        }

        if (isbacodeduplicate) {
            Toast.makeText(Scan_Barcode_ILS_New.this, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                    GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(s);
                    GlobalClass.finalspecimenttypewiselist.get(i).setRemark("Scan");
                    getRemark = "";
                    getRemark = GlobalClass.finalspecimenttypewiselist.get(i).getRemark();
                    Log.e(TAG, "passBarcodeData: show barcode" + s);
                }
            }
        }

        recycler_barcode.removeAllViews();
//        adapterBarcode.notifyDataSetChanged();
        adapterBarcode = new AdapterBarcode_New(Scan_Barcode_ILS_New.this, selctedTest, GlobalClass.finalspecimenttypewiselist, this);
        adapterBarcode.setOnItemClickListener(new AdapterBarcode_New.OnItemClickListener() {
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
        recycler_barcode.setAdapter(adapterBarcode);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
    }

    @Override
    public void onBackPressed() {
        GlobalClass.hideProgress(Scan_Barcode_ILS_New.this, barProgressDialog);
        finish();
        super.onBackPressed();
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

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
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

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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

    public void getUploadFileResponse() {

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
            TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            Intent intent = new Intent(Scan_Barcode_ILS_New.this, SummaryActivity_New.class);
            Bundle bundle = new Bundle();
            bundle.putString("tetsts", getTestSelection);
            bundle.putString("brandname", getBrand_name);
            bundle.putString("location", setLocation);
            bundle.putString("passProdcucts", testsCodesPassingToProduct);
            bundle.putString("TotalAmt", totalamt);
            bundle.putString("CollectedAmt", getCollectedAmt);
            bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
            bundle.putString("patientId", barcode_patient_id);
            bundle.putString("draft", "");
            intent.putExtras(bundle);
            startActivity(intent);
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
            GlobalClass.hideProgress(Scan_Barcode_ILS_New.this, barProgressDialog);
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
        TastyToast.makeText(Scan_Barcode_ILS_New.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        Intent intent = new Intent(Scan_Barcode_ILS_New.this, SummaryActivity_New.class);
        Bundle bundle = new Bundle();
        bundle.putString("tetsts", getTestSelection);
        bundle.putString("brandname", getBrand_name);
        bundle.putString("location", setLocation);
        bundle.putString("passProdcucts", testsCodesPassingToProduct);
        bundle.putString("TotalAmt", totalamt);
        bundle.putString("CollectedAmt", getCollectedAmt);
        bundle.putParcelableArrayList("sendArraylist", GlobalClass.finalspecimenttypewiselist);
        bundle.putString("patientId", barcode_patient_id);
        bundle.putString("draft", "");
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
                    tv_location.setText(Html.fromHtml("This sample will be processed at "));
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
