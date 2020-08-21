package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.CLISO_ScanBarcodeAdapter;
import com.example.e5322.thyrosoft.Adapter.TRFDisplayAdapter;
import com.example.e5322.thyrosoft.AsyncTaskPost_uploadfile;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.LeadWoeController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.example.e5322.thyrosoft.WOE.SummaryActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

public class Scan_Barcode_Outlabs_Activity extends AppCompatActivity implements RecyclerInterface {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    public static ArrayList<String> labAlerts;
    public String specimenttype1;
    public int position1 = 0;
    SharedPreferences prefs;
    RequestQueue POstQue;
    String outTestToSend, testsData;
    EditText enterAmt;
    String TAG = Scan_Barcode_Outlabs_Activity.class.getSimpleName();
    Button next;
    Camera camera;
    ArrayList<Outlabdetails_OutLab> selectedOutlabTests = new ArrayList<>();
    ArrayList<ScannedBarcodeDetails> FinalBarcodeDetailsList;
    TextView show_selected_tests_data, setAmt, title;
    LinearLayoutManager linearLayoutManager;
    IntentIntegrator scanIntegrator;
    LinearLayout barcodescanninglist;
    RecyclerView recycler_barcode;
    TextView lab_alert_spin;
    String barcodes1;
    SharedPreferences preferences;
    String getAmount;
    String getCollectedAmt;
    LinearLayout amt_collected_and_total_amt_ll;
    String user, passwrd, access, api_key, typeName, brandName, barcode_patient_id, displayslectedtest;
    ArrayList<String> getProductCode;
    String showtest;
    ArrayList<String> testToPass;
    BarcodelistModel barcodelist;
    ProgressDialog barProgressDialog;
    ArrayList<BarcodelistModel> barcodelists;
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
    private TextView companycost_test;
    private String getTypeName;
    private ImageView back;
    private ImageView home;
    private String patientName, patientYearType;
    private String patientYear, patientGender;
    private String sampleCollectionDate, sampleCollectionTime;
    private String referenceBy, sampleCollectionPoint, sampleGivingClient;
    private String refeID;
    private String ageType;
    private String labAddress;
    private String labID;
    private String labName;
    private String btechID;
    private String campID;
    private String homeaddress;
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
    private String getRemark;
    private boolean barcodeExistsFlag = false;
    int b2b_rate = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan__barcode__ils);
        mActivity = Scan_Barcode_Outlabs_Activity.this;

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        myDb = new DatabaseHelper(Scan_Barcode_Outlabs_Activity.this);
        initUI();

        barcodescanninglist.setVisibility(View.VISIBLE);
        recycler_barcode.setVisibility(View.VISIBLE);
        linearLayoutManager1 = new LinearLayoutManager(Scan_Barcode_Outlabs_Activity.this);
        rec_trf.setLayoutManager(linearLayoutManager1);

        Bundle bundle1 = getIntent().getExtras();

        versionNameTopass = GlobalClass.getversion(mActivity);
        versionCode = GlobalClass.getversioncode(mActivity);

        initListeners();

        Intent intent = getIntent();
        if (intent.hasExtra("FinalBarcodeList"))
            FinalBarcodeDetailsList = intent.getExtras().getParcelableArrayList("FinalBarcodeList");

        getTypeName = intent.getStringExtra("getTypeName");

        if (bundle1 != null) {
            selectedOutlabTests = bundle1.getParcelableArrayList("getOutlablist");
            testsData = bundle1.getString("writeTestName");
            Log.e(TAG, "onCreate: size " + selectedOutlabTests.size());

            ArrayList<String> getProducts = new ArrayList<>();
            getProductCode = new ArrayList<>();
            trflist.clear();

            try {

                if (GlobalClass.CheckArrayList(selectedOutlabTests)) {
                    for (int i = 0; i < selectedOutlabTests.size(); i++) {
                        getProductCode.add(selectedOutlabTests.get(i).getProduct());
                        getProducts.add(selectedOutlabTests.get(i).getProduct());
                        displayslectedtest = TextUtils.join(",", getProductCode);
                        passProducts = TextUtils.join(",", getProducts);

                        if (!GlobalClass.isNull(selectedOutlabTests.get(i).getTrf()) && !GlobalClass.isNull(MessageConstants.YES) && selectedOutlabTests.get(i).getTrf().equalsIgnoreCase(MessageConstants.YES)) {
                            TRFModel trfModel = new TRFModel();
                            trfModel.setProduct(selectedOutlabTests.get(i).getProduct());
                            trflist.add(trfModel);
                            callTRFAdapter(trflist);
                            Log.e(TAG, "TRF list--->" + trflist.size());
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

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(Scan_Barcode_Outlabs_Activity.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        labAlerts = new ArrayList<>();

        try {
            if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getLAB_ALERTS())) {
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

        spinnerDialog = new SpinnerDialog(Scan_Barcode_Outlabs_Activity.this, labAlerts, "Search Lab Alerts", "Close");// With No Animation

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                GlobalClass.SetText(lab_alert_spin, s);
            }
        });

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefs.getString("WOEbrand", "");
        typeName = prefs.getString("woetype", "");

        GlobalClass.SetText(title, "Scan Barcode");

        scanIntegrator = new IntentIntegrator(Scan_Barcode_Outlabs_Activity.this);
        linearLayoutManager = new LinearLayoutManager(Scan_Barcode_Outlabs_Activity.this);
        recycler_barcode.setLayoutManager(linearLayoutManager);
        ArrayList<String> saveLocation = new ArrayList<>();
        int totalcount = 0;

        if (GlobalClass.CheckArrayList(selectedOutlabTests)) {
            for (int i = 0; i < selectedOutlabTests.size(); i++) {
                if (!GlobalClass.isNull(selectedOutlabTests.get(i).getIsCPL())) {
                    if (selectedOutlabTests.get(i).getIsCPL().equalsIgnoreCase("1")) {
                        saveLocation.add("CPL");
                    } else {
                        saveLocation.add("RPL");
                    }
                }

                if (GlobalClass.CheckArrayList(saveLocation)) {
                    if (saveLocation.contains("CPL")) {
                        totalcount = totalcount + Integer.parseInt(selectedOutlabTests.get(i).getRate().getB2c());

                        if (!GlobalClass.isNull(selectedOutlabTests.get(i).getRate().getCplr())) {
                            b2b_rate = b2b_rate + Integer.parseInt(selectedOutlabTests.get(i).getRate().getCplr());
                        } else {
                            b2b_rate = b2b_rate + Integer.parseInt(selectedOutlabTests.get(i).getRate().getB2b());
                        }

                    } else {
                        totalcount = totalcount + Integer.parseInt(selectedOutlabTests.get(i).getRate().getB2c());
                        if (!GlobalClass.isNull(selectedOutlabTests.get(i).getRate().getRplr())) {
                            b2b_rate = b2b_rate + Integer.parseInt(selectedOutlabTests.get(i).getRate().getRplr());
                        } else {
                            b2b_rate = b2b_rate + Integer.parseInt(selectedOutlabTests.get(i).getRate().getB2b());
                        }

                    }
                }

                Log.e(TAG, "b2b_rate: " + b2b_rate);
                Log.e(TAG, "onCreate: 11 " + totalcount);
            }
        }


        GlobalClass.SetText(setAmt, String.valueOf(totalcount));

        CLISO_ScanBarcodeAdapter bmc_scanBarcodeAdapter = new CLISO_ScanBarcodeAdapter(Scan_Barcode_Outlabs_Activity.this, selectedOutlabTests, FinalBarcodeDetailsList, this);
        bmc_scanBarcodeAdapter.setOnItemClickListener(new CLISO_ScanBarcodeAdapter.OnItemClickListener() {
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

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Scan_Barcode_Outlabs_Activity.this);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getAmount = setAmt.getText().toString();
                    getCollectedAmt = enterAmt.getText().toString();

                    String getTestSelection = show_selected_tests_data.getText().toString();
                    String getLab_alert = lab_alert_spin.getText().toString();

                    if (!GlobalClass.isNull(getLab_alert) && getLab_alert.equalsIgnoreCase("SELECT LAB ALERTS")) {
                        lab_alert_pass_toApi = "";
                    } else {
                        lab_alert_pass_toApi = lab_alert_spin.getText().toString();
                    }

                    if (GlobalClass.isNull(getTestSelection)) {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.SL_TEST, 2);
                    } else if (GlobalClass.isNull(getCollectedAmt)) {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.Enter_amount, 2);
                    } else if (Integer.parseInt(getCollectedAmt) < b2b_rate) {
                        GlobalClass.showTastyToast(Scan_Barcode_Outlabs_Activity.this, getResources().getString(R.string.amtcollval) + " " + b2b_rate, 2);
                    } else {
                        checklistData();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUI() {
        recycler_barcode = (RecyclerView) findViewById(R.id.recycler_barcode);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        setAmt = (TextView) findViewById(R.id.setAmt);
        companycost_test = (TextView) findViewById(R.id.companycost_test);
        title = (TextView) findViewById(R.id.title);
        enterAmt = (EditText) findViewById(R.id.enterAmt);
        next = (Button) findViewById(R.id.next);
        barcodescanninglist = (LinearLayout) findViewById(R.id.barcodescanninglist);
        amt_collected_and_total_amt_ll = (LinearLayout) findViewById(R.id.amt_collected_and_total_amt_ll);
        lab_alert_spin = (TextView) findViewById(R.id.lab_alert_spin);
        ll_uploadTRF = (LinearLayout) findViewById(R.id.ll_uploadTRF);
        rec_trf = (RecyclerView) findViewById(R.id.rec_trf);
    }

    private void callTRFAdapter(ArrayList<TRFModel> trflist) {
        if (GlobalClass.CheckArrayList(trflist)) {
            ll_uploadTRF.setVisibility(View.VISIBLE);
            TRFDisplayAdapter trfDisplayAdapter = new TRFDisplayAdapter(Scan_Barcode_Outlabs_Activity.this, trflist);
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
                    GlobalClass.showTastyToast(mActivity, MessageConstants.Failedto_load_image, 2);
                    return;
                }
                try {
                    trf_img = FileUtil.from(this, data.getData());
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(trf_img.length())));
                    trf_img = GlobalClass.getCompressedFile(mActivity, trf_img);
                    Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(trf_img.length())));

                    storeTRFImage(trf_img);
                } catch (IOException e) {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.Failed_to_read_img, 2);
                    e.printStackTrace();
                }
            }
        }
    }

    private void passBarcodeData(final String s) {
        boolean isbacodeduplicate = false;

        if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
            for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                if (!GlobalClass.isNull(FinalBarcodeDetailsList.get(i).getBarcode())) {
                    if (!GlobalClass.isNull(s) && FinalBarcodeDetailsList.get(i).getBarcode().equalsIgnoreCase(s)) {
                        isbacodeduplicate = true;
                    }
                }
            }
        }


        if (isbacodeduplicate) {
            GlobalClass.showTastyToast(mActivity, ToastFile.duplicate_barcd, 2);
        } else {

            if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
                for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                    if (!GlobalClass.isNull(FinalBarcodeDetailsList.get(i).getSpecimen_type()) &&
                            !GlobalClass.isNull(GlobalClass.specimenttype) &&
                            FinalBarcodeDetailsList.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                        FinalBarcodeDetailsList.get(i).setBarcode(s);
                        FinalBarcodeDetailsList.get(i).setRemark("Scan");
                        getRemark = "";
                        getRemark = FinalBarcodeDetailsList.get(i).getRemark();
                        Log.e(TAG, "passBarcodeData: show barcode" + s);
                    }
                }
            }

        }

        recycler_barcode.removeAllViews();

        CLISO_ScanBarcodeAdapter cliso_scanBarcodeAdapter = new CLISO_ScanBarcodeAdapter(Scan_Barcode_Outlabs_Activity.this, selectedOutlabTests, FinalBarcodeDetailsList, this);
        cliso_scanBarcodeAdapter.setOnItemClickListener(new CLISO_ScanBarcodeAdapter.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {
                scanIntegrator = new IntentIntegrator(activity);
                if (!GlobalClass.isNull(GlobalClass.specimenttype)) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator.initiateScan();
            }
        });
        recycler_barcode.setAdapter(cliso_scanBarcodeAdapter);
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from gallery",
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
                } else if (items[item].equals("Choose from gallery")) {
                    userChoosenTask = "Choose from gallery";
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

        if (GlobalClass.CheckArrayList(trflist)) {
            for (int i = 0; i < trflist.size(); i++) {
                if (!GlobalClass.isNull(trflist.get(i).getProduct()) &&
                        !GlobalClass.isNull(selectedProduct) &&
                        trflist.get(i).getProduct().equalsIgnoreCase(selectedProduct)) {
                    trflist.get(i).setTrf_image(trf_img);
                }
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
        GlobalClass.showTastyToast(Scan_Barcode_Outlabs_Activity.this, message, 1);
        Intent intent = new Intent(Scan_Barcode_Outlabs_Activity.this, SummaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("payment", getCollectedAmt);
        bundle.putString("TotalAmt", getAmount);
        bundle.putString("selectedTest", testsData);
        bundle.putString("patient_id", barcode_patient_id);
        bundle.putString("Outlbbarcodes", barcodes1);
        bundle.putParcelableArrayList("sendArraylist", FinalBarcodeDetailsList);


        if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
            for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                testToPass = new ArrayList<>();
                testToPass.add(FinalBarcodeDetailsList.get(i).getProducts());
                outTestToSend = FinalBarcodeDetailsList.get(i).getProducts();
                showtest = TextUtils.join(",", testToPass);
            }
        }


        bundle.putString("tetsts", displayslectedtest);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void checklistData() {
        if (GlobalClass.CheckArrayList(trflist)) {
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
        if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
            for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                if (GlobalClass.isNull(FinalBarcodeDetailsList.get(i).getBarcode())) {
                    barcodeExistsFlag = true;
                }
            }

            if (barcodeExistsFlag) {
                barcodeExistsFlag = false;
                GlobalClass.showTastyToast(mActivity, ToastFile.scan_barcode_all, 2);
            } else {
                if (getRemark == null) {
                    getRemark = "Manual";
                }

                preferences = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
                patientName = preferences.getString("name", "");
                patientYear = preferences.getString("age", "");
                patientYearType = preferences.getString("ageType", "");
                patientGender = preferences.getString("gender", "");
                brandName = preferences.getString("WOEbrand", "");
                if (!GlobalClass.isNull(brandName) && brandName.equalsIgnoreCase("EQNX")) {
                    brandName = "WHATERS";
                } else {
                    brandName = preferences.getString("WOEbrand", "");
                }
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
                getPincode = preferences.getString("pincode", "");
                getFinalEmailIdToPost = "";


                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                Date date = null;
                try {
                    date = inputFormat.parse(sampleCollectionDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                outputDateStr = outputFormat.format(date);
                //sampleCollectionTime
                Log.e(TAG, "fetchData: " + outputDateStr);
                if (!GlobalClass.isNull(patientYearType)) {
                    if (patientYearType.equalsIgnoreCase("Years")) {
                        ageType = "Y";
                    } else if (patientYearType.equalsIgnoreCase("Months")) {
                        ageType = "M";
                    } else if (patientYearType.equalsIgnoreCase("Days")) {
                        ageType = "D";
                    }
                }

                String getFulltime = sampleCollectionDate + " " + sampleCollectionTime;
                GlobalClass.Req_Date_Req(getFulltime, "hh:mm a", "HH:mm:ss");

                POstQue = Volley.newRequestQueue(Scan_Barcode_Outlabs_Activity.this);

                MyPojoWoe myPojoWoe = new MyPojoWoe();
                Woe woe = new Woe();
                woe.setAADHAR_NO("");
                woe.setADDRESS(homeaddress);
                woe.setAGE(patientYear);
                woe.setAGE_TYPE(ageType);
                woe.setALERT_MESSAGE(lab_alert_pass_toApi);
                woe.setAMOUNT_COLLECTED(getCollectedAmt);
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
                woe.setWO_MODE(Constants.WOEMODE);
                woe.setWO_STAGE(3);
                woe.setULCcode("");

                myPojoWoe.setWoe(woe);

                barcodelists = new ArrayList<>();
                getBarcodeArrList = new ArrayList<>();


                if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
                    for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                        barcodelist = new BarcodelistModel();
                        barcodelist.setSAMPLE_TYPE(FinalBarcodeDetailsList.get(i).getSpecimen_type());
                        barcodelist.setBARCODE(FinalBarcodeDetailsList.get(i).getBarcode());
                        getBarcodeArrList.add(FinalBarcodeDetailsList.get(i).getBarcode());
                        barcodelist.setTESTS(FinalBarcodeDetailsList.get(i).getProducts());
                        barcodelists.add(barcodelist);
                    }
                }


                myPojoWoe.setBarcodelistModel(barcodelists);
                myPojoWoe.setWoe_type("WOE");
                myPojoWoe.setApi_key(api_key);

                if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs_Activity.this)) {
                    Gson trfgson1 = new GsonBuilder().create();
                    String trfjson1 = trfgson1.toJson(trflist);
                    myPojoWoe.setTrfjson(trfjson1);
                }

                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myPojoWoe);
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!GlobalClass.isNetworkAvailable(Scan_Barcode_Outlabs_Activity.this)) {
                    barcodes1 = TextUtils.join(",", getBarcodeArrList);
                    if (!flagcallonce) {
                        flagcallonce = true;
                        boolean isInserted = myDb.insertData(barcodes1, json);
                        if (isInserted) {
                            GlobalClass.showTastyToast(Scan_Barcode_Outlabs_Activity.this, ToastFile.woeSaved, 1);
                            Intent intent = new Intent(Scan_Barcode_Outlabs_Activity.this, SummaryActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("payment", getCollectedAmt);
                            bundle.putString("TotalAmt", getAmount);
                            bundle.putString("selectedTest", testsData);
                            bundle.putString("patient_id", barcode_patient_id);
                            bundle.putString("Outlbbarcodes", barcodes1);
                            bundle.putParcelableArrayList("sendArraylist", FinalBarcodeDetailsList);

                            if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
                                for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                                    testToPass = new ArrayList<>();
                                    testToPass.add(FinalBarcodeDetailsList.get(i).getProducts());
                                    outTestToSend = FinalBarcodeDetailsList.get(i).getProducts();
                                    showtest = TextUtils.join(",", testToPass);
                                }
                            }
                            bundle.putString("tetsts", displayslectedtest);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            GlobalClass.showTastyToast(Scan_Barcode_Outlabs_Activity.this, ToastFile.woenotSaved, 2);
                        }
                    }
                } else {

                    if (!flagcallonce) {
                        flagcallonce = true;

                        try {
                            if (ControllersGlobalInitialiser.leadWoeController != null) {
                                ControllersGlobalInitialiser.leadWoeController = null;
                            }
                            ControllersGlobalInitialiser.leadWoeController = new LeadWoeController(mActivity, Scan_Barcode_Outlabs_Activity.this);
                            ControllersGlobalInitialiser.leadWoeController.getleadwoeController(jsonObj, POstQue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (GlobalClass.CheckArrayList(trflist))
                            new AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs_Activity.this, mActivity, api_key, user, barcode_patient_id, trflist).execute();
                        else {
                            getUploadFileResponse();
                        }
                    }
                }
            }
        } else {
            GlobalClass.showTastyToast(mActivity, ToastFile.scan_barcode_all, 2);
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
    public void recyclerViewListClicked(View v, int position) {

    }

    @Override
    public void putDataToscan(Activity activity, int position, String specimenttype) {
        specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();
    }

    public void doleadwoe(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            GlobalClass.hideProgress(mActivity, barProgressDialog);
            Gson woeGson = new Gson();
            WOEResponseModel woeResponseModel = woeGson.fromJson(String.valueOf(response), WOEResponseModel.class);
            barcode_patient_id = woeResponseModel.getBarcode_patient_id();
            Log.e(TAG, "BARCODE PATIENT ID --->" + barcode_patient_id);
            message = woeResponseModel.getMessage();
            if (woeResponseModel != null) {
                if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                    Log.e(TAG, "onResponse message --->: " + message);
                    if (GlobalClass.CheckArrayList(trflist))
                        new AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs_Activity.this, mActivity, api_key, user, barcode_patient_id, trflist).execute();
                    else {
                        getUploadFileResponse();
                    }
                } else if (!GlobalClass.isNull(woeResponseModel.getStatus()) && woeResponseModel.getStatus().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(Scan_Barcode_Outlabs_Activity.this);
                } else {
                    flagcallonce = false;
                    GlobalClass.showTastyToast(Scan_Barcode_Outlabs_Activity.this, message, 2);
                }
            } else {
                flagcallonce = false;
                GlobalClass.showTastyToast(Scan_Barcode_Outlabs_Activity.this, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doleadwoeError() {
        flagcallonce = false;
    }
}