package com.example.e5322.thyrosoft.Cliso_BMC;

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
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.BMC_BaseModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.GpsTracker;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.ProductWithBarcode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class BMC_Scan_BarcodeActivity extends AppCompatActivity implements RecyclerInterface {

    public static ArrayList<String> labAlerts;
    public static RequestQueue sendGPSDetails;
    public static RequestQueue POstQue;
    public static WeakReference<Bitmap> rotateBitmap;
    public IntentIntegrator scanIntegrator;
    public String specimenttype1;
    public int position1 = 0;
    SharedPreferences prefs;
    String setLocation = null;
    EditText enterAmt, edt_trfNumber, edt_receiptNumber;
    TextView title;
    Button next, btn_upload_trf, btn_upload_receipt;
    ImageView back;
    LocationManager locationManager;
    GpsTracker gpsTracker;
    TextView lab_alert_spin, tv_cancel_trf, tv_cancel_receipt;
    ImageView home, img_trf, img_receipt;
    LinearLayout amt_collected_and_total_amt_ll;
    ArrayList<BMC_BaseModel> selctedTest;
    ArrayList<String> setSpecimenTypeCodes;
    SharedPreferences preferences, prefe;
    String brandName, typeName;
    TextView show_selected_tests_data, setAmt;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recycler_barcode;
    String getStateName, getCountryName, getCityName;
    SharedPreferences savepatientDetails;
    ArrayList<BarcodelistModel> barcodelists;
    ArrayList<String> getBarcodeArrList;
    ProgressDialog barProgressDialog;
    BarcodelistModel barcodelist;
    boolean flagcallonce = false;
    String getIMEINUMBER;
    String mobileModel;
    String latitudePassTOAPI;
    String longitudePassTOAPI;
    ArrayList<String> saveLocation;
    RequestQueue barcodeDetailsdata;
    ArrayList<ScannedBarcodeDetails> BMC_FinalBarcodeDetailsList;
    File trf_img = null, receipt_img = null;
    FrameLayout frame_layout_trf, frame_layout_receipt;
    private int status_code = 0;
    private int ttl_other = 0;
    private MyPojo myPojo;
    private SpinnerDialog spinnerDialog;
    private boolean GpsStatus;
    private ArrayList<String> temparraylist;
    private ArrayList<ProductWithBarcode> getproductDetailswithBarcodes;
    private BMC_ScanBarcodeAdapter bmc_scanBarcodeAdapter;
    private String getAmount;
    private AlertDialog.Builder alertDialog;
    private int collectedAmt;
    private int totalAmount;
    private String getCollectedAmt, getTRFnumber, getReceiptNumber;
    private String testsnames;
    private DatabaseHelper myDb;
    private String patientName, patientYearType, user, passwrd, access, api_key, status;
    private String patientYear, patientGender;
    private String sampleCollectionDate;
    private String message;
    private String ageType;
    private boolean flag = true;
    private String currentText;
    private String getFinalAddressToPost, ERROR, RES_ID, barcode, response1;
    private String getFinalPhoneNumberToPost;
    private String getFinalEmailIdToPost;
    private String TAG = BMC_Scan_BarcodeActivity.this.getClass().getSimpleName();
    private String outputDateStr;
    private String nameString;
    private String getFinalAge;
    private String saveGenderId;
    private String getFinalTime;
    private String getageType;
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
    private String COME_FROM;
    private int PERMISSION_REQUEST_CODE = 200;
    private Activity mActivity;
    private String userChoosenTask;
    private int PICK_PHOTO_FROM_CAMERA = 201;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private AlertDialog.Builder alertdialog;
    private Dialog dialog;
    private Bitmap trfbitmap, receiptbitmap;
    private String uploadFlag = "";
    private Uri imageUri;
    private String json;
    private boolean barcodeExistsFlag = false;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        rotateBitmap = new WeakReference<Bitmap>(Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true));
        return rotateBitmap.get();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmc_scan_barcode);

        mActivity = BMC_Scan_BarcodeActivity.this;
        alertdialog = new AlertDialog.Builder(this);
        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        setAmt = (TextView) findViewById(R.id.setAmt);
        title = (TextView) findViewById(R.id.title);
        amt_collected_and_total_amt_ll = (LinearLayout) findViewById(R.id.amt_collected_and_total_amt_ll);
        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
        enterAmt = (EditText) findViewById(R.id.enterAmt);
        edt_trfNumber = (EditText) findViewById(R.id.edt_trfNumber);
        edt_receiptNumber = (EditText) findViewById(R.id.edt_receiptNumber);
        next = (Button) findViewById(R.id.next);
        btn_upload_trf = (Button) findViewById(R.id.btn_upload_trf);
        btn_upload_receipt = (Button) findViewById(R.id.btn_upload_receipt);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        img_trf = (ImageView) findViewById(R.id.img_trf);
        img_receipt = (ImageView) findViewById(R.id.img_receipt);
        frame_layout_trf = (FrameLayout) findViewById(R.id.frame_layout_trf);
        frame_layout_receipt = (FrameLayout) findViewById(R.id.frame_layout_receipt);
        tv_cancel_trf = (TextView) findViewById(R.id.tv_cancel_trf);
        tv_cancel_receipt = (TextView) findViewById(R.id.tv_cancel_receipt);

        prefe = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefe.getString("WOEbrand", null);
        typeName = prefe.getString("woetype", null);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
        POstQue = Volley.newRequestQueue(BMC_Scan_BarcodeActivity.this);

        title.setText("Scan Barcode");

        Bundle bundle = getIntent().getExtras();
        selctedTest = bundle.getParcelableArrayList("key");

        Intent intent = getIntent();
        if (intent.hasExtra("FinalBarcodeList"))
            BMC_FinalBarcodeDetailsList = bundle.getParcelableArrayList("FinalBarcodeList");

        myDb = new DatabaseHelper(getApplicationContext());
        CheckGpsStatus();
        if (GpsStatus == true) {
            gpsTracker = new GpsTracker(BMC_Scan_BarcodeActivity.this);
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
        scanIntegrator = new IntentIntegrator(BMC_Scan_BarcodeActivity.this);
        final SharedPreferences getIMIE = getSharedPreferences("MobilemobileIMEINumber", MODE_PRIVATE);
        getIMEINUMBER = getIMIE.getString("mobileIMEINumber", null);
        SharedPreferences getModelNumber = getSharedPreferences("MobileName", MODE_PRIVATE);
        mobileModel = getModelNumber.getString("mobileName", null);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        savepatientDetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        nameString = savepatientDetails.getString("name", null);
        getFinalAge = savepatientDetails.getString("age", null);
        saveGenderId = savepatientDetails.getString("gender", null);
        getFinalTime = savepatientDetails.getString("sct", null);
        getageType = savepatientDetails.getString("ageType", null);
        getFinalDate = savepatientDetails.getString("date", null);

        labname = savepatientDetails.getString("labname", null);
        labAddress = savepatientDetails.getString("labAddress", null);

        patientAddress = savepatientDetails.getString("patientAddress", null);
        referrredBy = savepatientDetails.getString("refBy", null);
        referenceID = savepatientDetails.getString("refId", null);
        labIDaddress = savepatientDetails.getString("labIDaddress", null);
        btechIDToPass = savepatientDetails.getString("btechIDToPass", null);
        btechNameToPass = savepatientDetails.getString("btechNameToPass", null);
        getcampIDtoPass = savepatientDetails.getString("getcampIDtoPass", null);
        kycinfo = savepatientDetails.getString("kycinfo", null);
        typename = savepatientDetails.getString("woetype", null);
        sr_number = savepatientDetails.getString("SR_NO", null);
        getPincode = savepatientDetails.getString("pincode", null);

        preferences = getSharedPreferences("patientDetails", MODE_PRIVATE);
        patientName = preferences.getString("name", null);
        patientYear = preferences.getString("year", null);
        patientYearType = preferences.getString("yearType", null);
        patientGender = preferences.getString("gender", null);
        getFinalAddressToPost = GlobalClass.setHomeAddress;
        getFinalPhoneNumberToPost = GlobalClass.getPhoneNumberTofinalPost;
        getFinalEmailIdToPost = GlobalClass.getFinalEmailAddressToPOst;

        barProgressDialog = new ProgressDialog(BMC_Scan_BarcodeActivity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

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

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(BMC_Scan_BarcodeActivity.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();
        if (myPojo.getMASTERS().getLAB_ALERTS() != null && myPojo.getMASTERS() != null) {
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

        spinnerDialog = new SpinnerDialog(BMC_Scan_BarcodeActivity.this, labAlerts, "Search Lab Alerts", "Close");// With No Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                lab_alert_spin.setText(s);
            }
        });

        ArrayList<String> getTestNameToPAss = new ArrayList<>();
        getAmount = bundle.getString("payment");
        testsnames = bundle.getString("writeTestName");
        COME_FROM = bundle.getString("come_from");
        getTestNameToPAss = bundle.getStringArrayList("TestCodesPass");
        testsCodesPassingToProduct = TextUtils.join(",", getTestNameToPAss);

        setAmt.setText(getAmount);
        enterAmt.setText(getAmount);
        show_selected_tests_data.setText(testsnames);
        System.out.println("" + selctedTest.toString());
        prefs = getSharedPreferences("showSelectedTest", MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(BMC_Scan_BarcodeActivity.this);
        recycler_barcode.setLayoutManager(linearLayoutManager);

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
                Intent i = new Intent(BMC_Scan_BarcodeActivity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });

        if (selctedTest.size() != 0) {
            for (int i = 0; i < selctedTest.size(); i++) {
                totalcount = totalcount + Integer.parseInt(selctedTest.get(i).getRate().getB2b());
                Log.e(TAG, "onCreate: 11 " + totalcount);
            }
        }
        Log.e(TAG, "onCreate: total " + totalcount);


        if (COME_FROM.equalsIgnoreCase("ProductListing")) {
            BMC_FinalBarcodeDetailsList = new ArrayList<>();
            if (selctedTest != null) {
                getproductDetailswithBarcodes = new ArrayList<>();
                saveLocation = new ArrayList<>();
                for (int i = 0; i < selctedTest.size(); i++) {
                    if (!selctedTest.get(i).getLocation().equalsIgnoreCase("TTL-OTHERS")) {
                        for (int j = 0; j < selctedTest.get(i).getBarcodes().length; j++) {
                            ProductWithBarcode productWithBarcode = new ProductWithBarcode();
                            temparraylist.add(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                            productWithBarcode.setBarcode(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                            productWithBarcode.setProduct(selctedTest.get(i).getBarcodes()[j].getCode());
                            getproductDetailswithBarcodes.add(productWithBarcode);
                            saveLocation.add(selctedTest.get(i).getLocation());
                        }
                    }
                }
            }

            if (saveLocation != null && !saveLocation.isEmpty()) {
                for (int i = 0; i < saveLocation.size(); i++) {
                    if (saveLocation.contains("CPL")) {
                        setLocation = "CPL";
                    } else {
                        setLocation = "RPL";
                    }
                }
            }

            Set<String> hs = new HashSet<>();
            hs.addAll(temparraylist);
            temparraylist.clear();
            temparraylist.addAll(hs);

            for (int i = 0; i < temparraylist.size(); i++) {
                ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
                setSpecimenTypeCodes = new ArrayList<>();
                for (int j = 0; j < getproductDetailswithBarcodes.size(); j++) {
                    if (temparraylist.get(i).equalsIgnoreCase(getproductDetailswithBarcodes.get(j).getBarcode())) {
                        setSpecimenTypeCodes.add(getproductDetailswithBarcodes.get(j).getProduct());
                    }
                }
                scannedBarcodeDetails.setSpecimen_type(temparraylist.get(i));
                for (int j = 0; j < setSpecimenTypeCodes.size(); j++) {
                    HashSet<String> listToSet = new HashSet<String>(setSpecimenTypeCodes);
//                Creating Arraylist without duplicate values
                    List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                    String setProducts = TextUtils.join(",", listWithoutDuplicates);
                    HashSet<String> test = new HashSet<String>(Arrays.asList(setProducts.split(",")));
                    String setFinalProducts = TextUtils.join(",", test);
                    scannedBarcodeDetails.setProducts(setFinalProducts);
                    scannedBarcodeDetails.setLocation("TTL");
                }
                BMC_FinalBarcodeDetailsList.add(scannedBarcodeDetails);
            }
        }


        if (typeName.equals("ILS")) {
            if (selctedTest.size() != 0) {
                totalcount = 0;
                for (int i = 0; i < selctedTest.size(); i++) {
                    int getAmtBilledRate = Integer.parseInt(selctedTest.get(i).getBillrate());
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

        bmc_scanBarcodeAdapter = new BMC_ScanBarcodeAdapter(BMC_Scan_BarcodeActivity.this, selctedTest, BMC_FinalBarcodeDetailsList, this);
        bmc_scanBarcodeAdapter.setOnItemClickListener(new BMC_ScanBarcodeAdapter.OnItemClickListener() {
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

        recycler_barcode.setAdapter(bmc_scanBarcodeAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String totalamt = getAmount;
                final String getTestSelection = show_selected_tests_data.getText().toString();
                final String passProdcucts = testsnames;

                String getLab_alert = lab_alert_spin.getText().toString();

                if (getLab_alert.equals("SELECT LAB ALERTS")) {
                    lab_alert_pass_toApi = "";
                } else {
                    lab_alert_pass_toApi = lab_alert_spin.getText().toString();
                }

                getTRFnumber = edt_trfNumber.getText().toString().trim().toUpperCase();
                getReceiptNumber = edt_receiptNumber.getText().toString().trim().toUpperCase();
                getCollectedAmt = enterAmt.getText().toString();

                if (!getCollectedAmt.equals("") && !totalamt.equals("")) {
                    collectedAmt = Integer.parseInt(getCollectedAmt);
                    totalAmount = Integer.parseInt(totalamt);
                } else {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.colAmt, Toast.LENGTH_SHORT).show();
                }

                if (getCollectedAmt.equals("")) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please enter collected amount", Toast.LENGTH_SHORT).show();
                } else if (getTestSelection.equals("") || getTestSelection.equals(null)) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Select test", Toast.LENGTH_SHORT).show();
                } else if (getCollectedAmt.equals("") || getCollectedAmt.equals(null) || getCollectedAmt.isEmpty()) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                } else if (getTRFnumber == null || getTRFnumber.isEmpty()) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please enter TRF number", Toast.LENGTH_SHORT).show();
                } else if (getTRFnumber.length() < 6) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please enter valid TRF number", Toast.LENGTH_SHORT).show();
                } else if (getReceiptNumber == null || getReceiptNumber.isEmpty()) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please enter receipt number", Toast.LENGTH_SHORT).show();
                } else if (getReceiptNumber.length() <= 6) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please enter minimum 7 digit receipt number", Toast.LENGTH_SHORT).show();
                } else if (trf_img == null) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please select TRF image", Toast.LENGTH_SHORT).show();
                } else if (receipt_img == null) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Please select receipt image", Toast.LENGTH_SHORT).show();
                } else {
                    doFinalWoe(totalamt, getTestSelection);
                }
            }
        });

        btn_upload_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    uploadFlag = "TRF";
                    selectImage();
                } else {
                    requestPermission();
                }
            }
        });

        btn_upload_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    uploadFlag = "RECEIPT";
                    selectImage();
                } else {
                    requestPermission();
                }
            }
        });

        tv_cancel_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trf_img != null) {
                    if (trf_img.exists()) {
                        alertdialog.setMessage("Do you want to delete the selected image ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (trf_img != null) {
                                            if (trf_img.exists()) {
                                                trf_img = null;
                                                frame_layout_trf.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alertdialog.create();
                        alert.show();
                    }
                }
            }
        });

        tv_cancel_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receipt_img != null) {
                    if (receipt_img.exists()) {
                        alertdialog.setMessage("Do you want to delete the selected image ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (receipt_img != null) {
                                            if (receipt_img.exists()) {
                                                receipt_img = null;
                                                frame_layout_receipt.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alertdialog.create();
                        alert.show();
                    }
                }
            }
        });

        img_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trfbitmap != null)
                    showImageDialog(trfbitmap);
            }
        });

        img_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receiptbitmap != null)
                    showImageDialog(receiptbitmap);
            }
        });
    }

    private void doFinalWoe(final String totalamt, final String getTestSelection) {
        if (BMC_FinalBarcodeDetailsList != null) {
            for (int i = 0; i < BMC_FinalBarcodeDetailsList.size(); i++) {
                if (BMC_FinalBarcodeDetailsList.get(i).getBarcode() == null) {
                    barcodeExistsFlag = true;
                }
            }

            if (barcodeExistsFlag) {
                barcodeExistsFlag = false;
                Toast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.scan_barcode_all, Toast.LENGTH_SHORT).show();
            } else {
                if (getRemark == null) {
                    getRemark = "Manual";
                }
                savepatientDetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
                nameString = savepatientDetails.getString("name", null);
                getFinalAge = savepatientDetails.getString("age", null);
                saveGenderId = savepatientDetails.getString("gender", null);
                getFinalTime = savepatientDetails.getString("sct", null);
                getageType = savepatientDetails.getString("ageType", null);
                getFinalDate = savepatientDetails.getString("date", null);
                getBrand_name = savepatientDetails.getString("WOEbrand", null);
                getPincode = savepatientDetails.getString("pincode", null);
                sampleCollectionDate = getFinalDate;
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
                if (getageType.equalsIgnoreCase("Years")) {
                    ageType = "Y";
                } else if (getageType.equalsIgnoreCase("Months")) {
                    ageType = "M";
                } else if (getageType.equalsIgnoreCase("Days")) {
                    ageType = "D";
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
                woe.setADDITIONAL1(setLocation);
                woe.setAPP_ID(version);
                woe.setBCT_ID(btechIDToPass);
                woe.setBRAND(getBrand_name);
                woe.setCAMP_ID(getcampIDtoPass);
                woe.setCONT_PERSON("");
                woe.setCONTACT_NO(kycinfo);
                woe.setCUSTOMER_ID("");
                woe.setDELIVERY_MODE(2);
                woe.setEMAIL_ID("");
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
                woe.setSR_NO(pass_to_api);
                woe.setSTATUS("N");
                woe.setSUB_SOURCE_CODE(user);
                woe.setTOTAL_AMOUNT(totalamt);
                woe.setTYPE(typename);
                woe.setWATER_SOURCE("");
                woe.setWO_MODE("THYROSOFTLITE APP");
                woe.setWO_STAGE(3);
                woe.setULCcode("");
                myPojoWoe.setWoe(woe);
                barcodelists = new ArrayList<>();
                getBarcodeArrList = new ArrayList<>();
                for (int p = 0; p < BMC_FinalBarcodeDetailsList.size(); p++) {
                    barcodelist = new BarcodelistModel();
                    barcodelist.setSAMPLE_TYPE(BMC_FinalBarcodeDetailsList.get(p).getSpecimen_type());
                    barcodelist.setBARCODE(BMC_FinalBarcodeDetailsList.get(p).getBarcode());
                    getBarcodeArrList.add(BMC_FinalBarcodeDetailsList.get(p).getBarcode());
                    barcodelist.setTESTS(BMC_FinalBarcodeDetailsList.get(p).getProducts());
                    barcodelists.add(barcodelist);
                }

                myPojoWoe.setBarcodelistModel(barcodelists);
                myPojoWoe.setWoe_type("WOE");
                myPojoWoe.setApi_key(api_key);//api_key

                Gson gson = new GsonBuilder().create();
                json = gson.toJson(myPojoWoe);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!GlobalClass.isNetworkAvailable(BMC_Scan_BarcodeActivity.this)) {
                    TastyToast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    if (!flagcallonce) {
                        flagcallonce = true;
                        barProgressDialog.show();

                        if (POstQue == null) {
                            POstQue = Volley.newRequestQueue(BMC_Scan_BarcodeActivity.this);
                        }

                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Api.finalWorkOrderEntryNew, jsonObj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (BMC_Scan_BarcodeActivity.this instanceof Activity) {
                                        if (!((Activity) BMC_Scan_BarcodeActivity.this).isFinishing())
                                            barProgressDialog.dismiss();
                                    }
                                    Log.e(TAG, "POST WOE: RESPONSE" + response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    RES_ID = parentObjectOtp.getString("RES_ID");
                                    barcode_patient_id = parentObjectOtp.getString("barcode_patient_id");
                                    message = parentObjectOtp.getString("message");
                                    status = parentObjectOtp.getString("status");
                                    barcode_id = parentObjectOtp.getString("barcode_id");

                                    if (status.equalsIgnoreCase("SUCCESS")) {
                                        SharedPreferences.Editor editor = getSharedPreferences("showSelectedTest", 0).edit();
                                        editor.remove("testsSElected");
                                        editor.remove("getProductNames");
                                        editor.commit();
                                        barProgressDialog.show();
                                        sendGPSDetails = Volley.newRequestQueue(BMC_Scan_BarcodeActivity.this);
                                        JSONObject jsonObjectOtp = new JSONObject();
                                        try {
                                            jsonObjectOtp.put("Username", user);
                                            jsonObjectOtp.put("IMEI", getIMEINUMBER);
                                            jsonObjectOtp.put("city", getCityName);
                                            jsonObjectOtp.put("state", getStateName);
                                            jsonObjectOtp.put("country", getCountryName);
                                            jsonObjectOtp.put("longitude", longitudePassTOAPI);
                                            jsonObjectOtp.put("latitude", latitudePassTOAPI);
                                            jsonObjectOtp.put("DeviceName", mobileModel);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Api.sendGeoLocation, jsonObjectOtp, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if (BMC_Scan_BarcodeActivity.this instanceof Activity) {
                                                        if (!((Activity) BMC_Scan_BarcodeActivity.this).isFinishing())
                                                            barProgressDialog.dismiss();
                                                    }
                                                    Log.e(TAG, "Send locationAPI Response: " + response);
                                                    String finalJson = response.toString();
                                                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                                                    String Response = parentObjectOtp.getString("Response");
                                                    String resId = parentObjectOtp.getString("resId");

                                                    if (resId.equals("RES0000")) {

                                                    } else {
                                                        TastyToast.makeText(BMC_Scan_BarcodeActivity.this, "" + Response, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                                    }

                                                    new AsyncTaskPost_uploadfile(api_key, user, barcode_patient_id, trf_img, receipt_img, getTestSelection, totalamt, getTRFnumber, getReceiptNumber).execute();

                                                } catch (JSONException e) {
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
                                                if (BMC_Scan_BarcodeActivity.this instanceof Activity) {
                                                    if (!((Activity) BMC_Scan_BarcodeActivity.this).isFinishing())
                                                        barProgressDialog.dismiss();
                                                }
                                                flagcallonce = false;
                                            }
                                        });
                                        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                150000,
                                                3,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        sendGPSDetails.add(objectRequest);
                                        Log.e(TAG, "onResponse: url" + objectRequest);
                                        Log.e(TAG, "onResponse: json" + jsonObjectOtp);
                                    } else if (message.equals("YOUR CREDIT LIMIT IS NOT SUFFICIENT TO COMPLETE WORK ORDER")) {
                                        flagcallonce = false;
                                        TastyToast.makeText(BMC_Scan_BarcodeActivity.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        final AlertDialog alertDialog = new AlertDialog.Builder(BMC_Scan_BarcodeActivity.this).create();
                                        alertDialog.setTitle("Update Ledger !");
                                        alertDialog.setMessage(ToastFile.update_ledger);
                                        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(BMC_Scan_BarcodeActivity.this, Payment_Activity.class);
                                                startActivity(i);
                                            }
                                        });
                                        alertDialog.show();
                                    } else if (status.equalsIgnoreCase(caps_invalidApikey)) {
                                        GlobalClass.redirectToLogin(BMC_Scan_BarcodeActivity.this);
                                    } else {
                                        flagcallonce = false;
                                        TastyToast.makeText(BMC_Scan_BarcodeActivity.this, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (BMC_Scan_BarcodeActivity.this instanceof Activity) {
                                    if (!((Activity) BMC_Scan_BarcodeActivity.this).isFinishing())
                                        barProgressDialog.dismiss();
                                }
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
                        if (!GlobalClass.isNetworkAvailable(BMC_Scan_BarcodeActivity.this)) {
                            TastyToast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            new AsyncTaskPost_uploadfile(api_key, user, barcode_patient_id, trf_img, receipt_img, getTestSelection, totalamt, getTRFnumber, getReceiptNumber).execute();
                        }
                    }
                }
            }
        } else {
            Toast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.allw_scan, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(BMC_Scan_BarcodeActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        BMC_Scan_BarcodeActivity.this.startActivity(intent);
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
                    if (ttl_other == 0) {
                        passBarcodeData(getBarcodeDetails);
                    }
                } else {
                    Toast.makeText(this, invalid_brcd, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_PHOTO_FROM_CAMERA && resultCode == RESULT_OK) {
                try {
                    if (uploadFlag.equalsIgnoreCase("TRF")) {
                        frame_layout_trf.setVisibility(View.VISIBLE);
                        String imageurl = getRealPathFromURI(imageUri);
                        trfbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        trfbitmap = rotate(trfbitmap, imageurl);
                        img_trf.setImageBitmap(trfbitmap);
                        trf_img = new File(imageurl);
                    } else if (uploadFlag.equalsIgnoreCase("RECEIPT")) {
                        frame_layout_receipt.setVisibility(View.VISIBLE);
                        String imageurl = getRealPathFromURI(imageUri);
                        receiptbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        receiptbitmap = rotate(receiptbitmap, imageurl);
                        img_receipt.setImageBitmap(receiptbitmap);
                        receipt_img = new File(imageurl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.failed_to_open, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (uploadFlag.equalsIgnoreCase("TRF")) {
                        frame_layout_trf.setVisibility(View.VISIBLE);
                        trf_img = FileUtil.from(this, data.getData());
                        Uri uri = data.getData();
                        trfbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        String imageurl = getRealPathFromURI(uri);
//                        trfbitmap = rotate(trfbitmap, imageurl);
                        img_trf.setImageBitmap(trfbitmap);
                    } else if (uploadFlag.equalsIgnoreCase("RECEIPT")) {
                        frame_layout_receipt.setVisibility(View.VISIBLE);
                        receipt_img = FileUtil.from(this, data.getData());
                        Uri uri = data.getData();
                        receiptbitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        String imageurl = getRealPathFromURI(uri);
//                        receiptbitmap = rotate(receiptbitmap, imageurl);
                        img_receipt.setImageBitmap(receiptbitmap);
                    }
                } catch (IOException e) {
                    Toast.makeText(BMC_Scan_BarcodeActivity.this, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
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

    private Bitmap rotate(Bitmap bitmap, String photoPath) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    private void passBarcodeData(String s) {
        boolean isbacodeduplicate = false;
        for (int i = 0; i < BMC_FinalBarcodeDetailsList.size(); i++) {
            if (BMC_FinalBarcodeDetailsList.get(i).getBarcode() != null && !BMC_FinalBarcodeDetailsList.get(i).getBarcode().isEmpty()) {
                if (BMC_FinalBarcodeDetailsList.get(i).getBarcode().equalsIgnoreCase(s)) {
                    isbacodeduplicate = true;
                }
            }
        }

        if (isbacodeduplicate == true) {
            Toast.makeText(BMC_Scan_BarcodeActivity.this, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < BMC_FinalBarcodeDetailsList.size(); i++) {
                if (BMC_FinalBarcodeDetailsList.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                    BMC_FinalBarcodeDetailsList.get(i).setBarcode(s);
                    BMC_FinalBarcodeDetailsList.get(i).setRemark("Scan");
                    getRemark = "";
                    getRemark = BMC_FinalBarcodeDetailsList.get(i).getRemark();
                    Log.e(TAG, "passBarcodeData: show barcode" + s);
                }
            }
        }

        recycler_barcode.removeAllViews();
//        adapterBarcode.notifyDataSetChanged();
        bmc_scanBarcodeAdapter = new BMC_ScanBarcodeAdapter(BMC_Scan_BarcodeActivity.this, selctedTest, BMC_FinalBarcodeDetailsList, this);
        bmc_scanBarcodeAdapter.setOnItemClickListener(new BMC_ScanBarcodeAdapter.OnItemClickListener() {
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
        recycler_barcode.setAdapter(bmc_scanBarcodeAdapter);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
    }

    @Override
    public void onBackPressed() {
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
        CharSequence[] items = new CharSequence[]{"Take Photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (uploadFlag.equalsIgnoreCase("TRF"))
            builder.setTitle("Upload TRF !");
        else
            builder.setTitle("Upload Receipt !");
        final CharSequence[] finalItems = items;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (finalItems[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    openCamera();
                } else if (finalItems[item].equals("Choose from gallery")) {
                    userChoosenTask = "Choose from gallery";
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
                } else if (finalItems[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        Intent pickPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(pickPhoto, PICK_PHOTO_FROM_CAMERA);
    }

    private void showImageDialog(Bitmap bitmap) {
        dialog = new Dialog(mActivity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_dialog);
        dialog.setCancelable(true);

        ImageView imgView = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        imgView.setImageBitmap(bitmap);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        img_trf.setImageBitmap(null);
    }

    class AsyncTaskPost_uploadfile extends AsyncTask<Void, Void, String> {
        String apiKey, sourceCode, patientID;
        File trfImageFile, receiptImageFile;
        String getTestSelection, totalamt, TRFnumber, receiptNumber;

        public AsyncTaskPost_uploadfile(String apiKey, String sourceCode, String patientID, File trfImageFile, File receiptImageFile, String getTestSelection, String totalamt, String getTRFnumber, String getReceiptNumber) {
            this.apiKey = apiKey;
            this.sourceCode = sourceCode;
            this.patientID = patientID;
            this.trfImageFile = trfImageFile;
            this.receiptImageFile = receiptImageFile;
            this.getTestSelection = getTestSelection;
            this.totalamt = totalamt;
            this.TRFnumber = getTRFnumber;
            this.receiptNumber = getReceiptNumber;

            status_code = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String strUrl = Api.UPLOAD_TRF_RECEIPT;
            System.out.println(strUrl);

            InputStream inputStream = null;
            String result = "";

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);

                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                builder.addPart("KEY", new StringBody("" + apiKey));
                builder.addPart("SOURCECODE", new StringBody("" + sourceCode));
                builder.addPart("PATIENTID", new StringBody("" + patientID));
                builder.addPart("TRFNUMBER", new StringBody("" + TRFnumber));
                builder.addPart("RECEIPTNUMBER", new StringBody("" + receiptNumber));
                builder.addPart("TYPE", new StringBody("DATA Reciept"));
                builder.addPart("MODE", new StringBody("BMC App"));

                if (trfImageFile != null) {
                    if (trfImageFile.exists()) {
                        builder.addBinaryBody("DataSheet", trfImageFile);
                    }
                }
                if (receiptImageFile != null) {
                    if (receiptImageFile.exists()) {
                        builder.addBinaryBody("Receipt", receiptImageFile);
                    }
                }

                Log.e(TAG, "Post params:- " + "\nKEY:" + apiKey + "\nSOURCECODE:" + sourceCode + "\nPATIENTID:" + patientID + "\nTRFNUMBER:" + TRFnumber
                        + "\nRECEIPTNUMBER:" + receiptNumber + "\nTYPE:DATA Reciept" + "\nMODE:BMC App" + "\nDataSheet:" + trfImageFile + "\nReceipt:" + receiptImageFile);

                httpPost.setEntity(builder.build());
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                Log.e(TAG, "Status Line: " + httpResponse.getStatusLine());
                status_code = httpResponse.getStatusLine().getStatusCode();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : " + result);
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                result = "Something went wrong";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (BMC_Scan_BarcodeActivity.this instanceof Activity) {
                if (!((Activity) BMC_Scan_BarcodeActivity.this).isFinishing())
                    barProgressDialog.dismiss();
            }
            if (status_code == 200) {
                if (response != null && !response.isEmpty()) {
                    Log.e(TAG, "ON Response: " + response);
                    response = response.replaceAll("^\"|\"$", "");
                    if (response.equalsIgnoreCase("File Uploaded successfully")) {
                        GlobalClass.TRF_BITMAP = trfbitmap;
                        GlobalClass.RECEIPT_BITMAP = receiptbitmap;
                        TastyToast.makeText(BMC_Scan_BarcodeActivity.this, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Intent intent = new Intent(BMC_Scan_BarcodeActivity.this, BMC_SummaryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("tetsts", getTestSelection);
                        bundle.putString("location", setLocation);
                        bundle.putString("passProdcucts", testsCodesPassingToProduct);
                        bundle.putString("TotalAmt", totalamt);
                        bundle.putString("CollectedAmt", getCollectedAmt);
                        bundle.putParcelableArrayList("sendArraylist", BMC_FinalBarcodeDetailsList);
                        bundle.putString("patientId", barcode_patient_id);
                        bundle.putString("draft", "");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mActivity, response, Toast.LENGTH_SHORT).show();
                        Toast.makeText(mActivity, ToastFile.IMAGE_UPLOAD_FAILED, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mActivity, "Result is null", Toast.LENGTH_SHORT).show();
                    Toast.makeText(mActivity, ToastFile.IMAGE_UPLOAD_FAILED, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mActivity, ToastFile.IMAGE_UPLOAD_FAILED, Toast.LENGTH_LONG).show();
            }
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }
    }
}