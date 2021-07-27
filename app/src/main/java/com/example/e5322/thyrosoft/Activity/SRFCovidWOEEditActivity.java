package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.SRFCovidWOEmultipart_controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CovidRateReqModel;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.ResponseModels.VerifyBarcodeResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

public class SRFCovidWOEEditActivity extends AppCompatActivity {

    private Activity activity;
    private ConnectionDetector cd;
    private List<String> presclist = new ArrayList<>();
    private List<String> aadharlist = new ArrayList<>();
    private List<String> viallist = new ArrayList<>();
    private List<String> otherlist = new ArrayList<>();
    private List<String> selfielist = new ArrayList<>();
    private LinearLayout lin_selfie, lin_pres_preview, lin_adhar_images, lin_vial_images, lin_other_images, ll_submit, mainlinear;
    private Button btn_choosefile_presc, btn_selfie, btn_choosefile_adhar, btn_choosefile_vial, btn_choosefile_other, btn_submit, btn_reset;
    private TextView txt_nofilepresc, txt_nofileadhar, txt_selfie, txt_nofilevial, txt_nofileother, tv_verifiedmob, tv_title;
    private final int PICK_PHOTO_FROM_GALLERY = 202;
    private Camera camera;
    private String TAG = SRFCovidWOEEditActivity.class.getSimpleName();
    private File presc_file = null, aadhar_file = null, aadhar_file1 = null, vial_file = null, other_file = null, other_file1 = null, selfie_file = null;
    private TextView txt_presfileupload, txt_selfiefileupload, txt_adharfileupload, txt_vialrfileupload, txt_otherfileupload, tv_coll_date, tv_coll_time;
    private boolean ispresciption, isadhar, isvial, isother, iscamera = false, genderId = false, flag = true, verifyBarcodeFlag = false, editDetailsFlag = false;
    private EditText edt_fname, edt_email, edt_amt, edt_lname, edt_srfid, edt_age, edt_patient_address, edt_patient_pincode, edt_coll_address, edt_coll_pincode, edt_barcode, edt_re_enter_barcode;
    private String usercode, apikey, gender = "", currentText, b2b, b2c, userChoosenTask, selDate = "", selTime = "", ageFlag;
    private RelativeLayout rel_verify_mobile;
    private Spinner spn_age, spn_scp;
    private ImageView male, female, male_red, female_red, back, home;
    private int mYear, mMonth, mDay, mHour, mMinute, selfie_flag = 0, agesinteger;
    private Date minDate;
    private Covidmis_response.OutputBean woeDetailsModel;

    EditText enter_barcode, reenter;
    Button btn_barcd;
    ImageView img_scan__barcode;
    LinearLayout consignment_name_layout, lineareditbarcode;
    private ImageView setback;
    AppPreferenceManager appPreferenceManager;
    public IntentIntegrator scanIntegrator;
    ImageView img_camera_prescription, img_gallery_prescription, img_camera_aadhar, img_gallery_aadhar;
    ImageView img_camera_vial, img_gallery_vial, img_camera_other, img_gallery_other, img_camera_selfie;

    LinearLayout ll_selfie, ll_other, ll_vial, ll_aadhar, ll_prescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srf_covid_woe_edit);

        activity = SRFCovidWOEEditActivity.this;
        cd = new ConnectionDetector(activity);
        scanIntegrator = new IntentIntegrator(activity);
        appPreferenceManager = new AppPreferenceManager(activity);
        Bundle bundle = getIntent().getExtras();
        woeDetailsModel = (Covidmis_response.OutputBean) bundle.getSerializable("covid_model");

        initUI();

        setAgeSpinner(R.array.Patientsagespinner);
        setSCPSpinner();

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_MONTH, mDay - 2);
        minDate = c1.getTime();

        if (cd.isConnectingToInternet()) {
            displayrate();
        }
        if (!GlobalClass.isNull(woeDetailsModel.getStatusName()) && woeDetailsModel.getStatusName().equalsIgnoreCase("WOE DONE") || woeDetailsModel.getStatusName().equalsIgnoreCase("PENDING")) {
            manageViews(false);
        } else if (!GlobalClass.isNull(woeDetailsModel.getStatusName()) && woeDetailsModel.getStatusName().equalsIgnoreCase("REJECTED")) {
            manageViews(true);
        }
        initListeners();
        setData();
    }

    private void initUI() {
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        tv_title = findViewById(R.id.tv_title);
        edt_fname = findViewById(R.id.edt_firstname);
        edt_amt = findViewById(R.id.edt_amt);
        edt_lname = findViewById(R.id.edt_lastname);
        edt_srfid = findViewById(R.id.edt_srfid);
        edt_patient_address = findViewById(R.id.edt_patient_address);
        edt_patient_pincode = findViewById(R.id.edt_patient_pincode);
        edt_coll_address = findViewById(R.id.edt_coll_address);
        edt_coll_pincode = findViewById(R.id.edt_coll_pincode);
        edt_barcode = findViewById(R.id.edt_barcode);
        edt_re_enter_barcode = findViewById(R.id.edt_re_enter_barcode);
        tv_coll_date = findViewById(R.id.tv_coll_date);
        tv_coll_time = findViewById(R.id.tv_coll_time);
        edt_age = findViewById(R.id.edt_age);
        spn_age = findViewById(R.id.spn_age);
        spn_scp = findViewById(R.id.spn_scp);
        male = findViewById(R.id.male);
        male_red = findViewById(R.id.male_red);
        female = findViewById(R.id.female);
        female_red = findViewById(R.id.female_red);
        btn_choosefile_presc = findViewById(R.id.btn_choosefile_presc);
        btn_choosefile_adhar = findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_vial = findViewById(R.id.btn_choosefile_vial);
        btn_choosefile_other = findViewById(R.id.btn_choosefile_other);
        txt_selfiefileupload = findViewById(R.id.txt_selfiefileupload);
        btn_reset = findViewById(R.id.btn_reset);
        btn_submit = findViewById(R.id.btn_submit);
        txt_selfie = findViewById(R.id.txt_selfie);
        lin_selfie = findViewById(R.id.lin_selfie);
        rel_verify_mobile = findViewById(R.id.rel_verify_mobile);
        tv_verifiedmob = findViewById(R.id.tv_verifiedmob);
        lin_pres_preview = findViewById(R.id.lin_pres_preview);
        lin_adhar_images = findViewById(R.id.lin_adhar_images);
        lin_vial_images = findViewById(R.id.lin_vial_images);
        lin_other_images = findViewById(R.id.lin_other_images);
        ll_submit = findViewById(R.id.ll_submit);
        mainlinear = findViewById(R.id.mainlinear);
        txt_presfileupload = findViewById(R.id.txt_presfileupload);
        txt_adharfileupload = findViewById(R.id.txt_adharfileupload);
        txt_vialrfileupload = findViewById(R.id.txt_vialrfileupload);
        txt_otherfileupload = findViewById(R.id.txt_otherfileupload);
        txt_nofilepresc = findViewById(R.id.txt_nofilepresc);
        txt_nofileadhar = findViewById(R.id.txt_nofileadhar);
        txt_nofilevial = findViewById(R.id.txt_nofilevial);
        txt_nofileother = findViewById(R.id.txt_nofileother);
        btn_selfie = findViewById(R.id.btn_selfie);
        edt_email = findViewById(R.id.edt_email);


        setback = findViewById(R.id.setback);
        enter_barcode = findViewById(R.id.enter_barcode);
        reenter = findViewById(R.id.reenter);
        lineareditbarcode = findViewById(R.id.lineareditbarcode);
        img_scan__barcode = findViewById(R.id.img_scan_barcode);
        btn_barcd = findViewById(R.id.btn_barcd);
        consignment_name_layout = findViewById(R.id.consignment_name_layout);


        img_camera_prescription = findViewById(R.id.img_camera_prescription);
        img_gallery_prescription = findViewById(R.id.img_gallery_prescription);
        img_camera_aadhar = findViewById(R.id.img_camera_aadhar);
        img_gallery_aadhar = findViewById(R.id.img_gallery_aadhar);
        img_camera_vial = findViewById(R.id.img_camera_vial);
        img_gallery_vial = findViewById(R.id.img_gallery_vial);
        img_camera_other = findViewById(R.id.img_camera_other);
        img_gallery_other = findViewById(R.id.img_gallery_other);
        img_camera_selfie = findViewById(R.id.img_camera_selfie);

        ll_selfie = findViewById(R.id.ll_selfie);
        ll_other = findViewById(R.id.ll_other);
        ll_vial = findViewById(R.id.ll_vial);
        ll_aadhar = findViewById(R.id.ll_aadhar);
        ll_prescription = findViewById(R.id.ll_prescription);


        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            edt_email.setHint("EMAIL ID*");
        } else {
            edt_email.setHint("EMAIL ID");
        }


    }

    private void initListeners() {

        img_camera_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selfie_flag == 0) {
                    boolean result = Utility.checkPermission(activity);
                    if (result) {
                        ispresciption = false;
                        isadhar = false;
                        isvial = false;
                        isother = false;
                        iscamera = true;
                        openCamera();
                    }

                } else {
                    GlobalClass.showCustomToast(activity, "Only 1 selfie can be Uploaded", 0);
                }
            }
        });

        img_gallery_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    if (other_file != null && other_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = false;
                        isvial = false;
                        isother = true;
                        chooseFromGallery();
                    }
                } else {
                    requestPermission();
                }
            }
        });


        img_camera_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    if (other_file != null && other_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = false;
                        isvial = false;
                        isother = true;
                        iscamera = false;
                        openCamera();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        img_gallery_vial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    /*if (editDetailsFlag) {
                        if (viallist != null && viallist.size() > 0) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image");
                        } else {*/
                    if (vial_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = false;
                        isvial = true;
                        isother = false;
//                        chooseFromGallery();
                        GlobalClass.cropImageActivity(SRFCovidWOEEditActivity.this, 2);
                    }
                        /*}
                    }*/
                } else {
                    requestPermission();
                }
            }
        });


        img_camera_vial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    /*if (editDetailsFlag) {
                        if (viallist != null && viallist.size() > 0) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image");
                        } else {*/
                    if (vial_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = false;
                        isvial = true;
                        isother = false;
//                        openCamera();
                        GlobalClass.cropImageActivity(SRFCovidWOEEditActivity.this, 0);

                    }
                        /*}
                    }*/
                } else {
                    requestPermission();
                }
            }
        });


        img_gallery_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (aadhar_file != null && aadhar_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = true;
                        isvial = false;
                        isother = false;
                        chooseFromGallery();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        img_camera_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (aadhar_file != null && aadhar_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = true;
                        isvial = false;
                        isother = false;
                        iscamera = false;
                        openCamera();
                    }
                } else {
                    requestPermission();
                }
            }
        });


        img_gallery_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (presc_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                    } else {
                        ispresciption = true;
                        isadhar = false;
                        isvial = false;
                        isother = false;
                        chooseFromGallery();
                    }
                } else {
                    requestPermission();
                }
            }
        });
        img_camera_prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (presc_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                    } else {
                        ispresciption = true;
                        isadhar = false;
                        isvial = false;
                        isother = false;
                        iscamera = false;
                        openCamera();
                    }
                } else {
                    requestPermission();
                }
            }
        });


        btn_barcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consignment_name_layout.setVisibility(View.GONE);
                lineareditbarcode.setVisibility(View.VISIBLE);
            }
        });


        img_scan__barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanIntegrator.initiateScan();
            }
        });

        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode.setVisibility(View.GONE);
                consignment_name_layout.setVisibility(View.VISIBLE);
            }
        });


        reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (enter_barcode.getText().length() != 8) {
                    Toast.makeText(activity, "Enter Valid Enter Barcode", Toast.LENGTH_SHORT).show();
                } else {
                    if (s.toString().equalsIgnoreCase(enter_barcode.getText().toString())) {
                        verifyBarcode(s.toString());
                    } else {
                        Toast.makeText(activity, "Barcode not Matched", Toast.LENGTH_SHORT).show();
                    }
                }
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
                GlobalClass.goToHome(SRFCovidWOEEditActivity.this);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderId = !genderId;
                setSelectedMale();
                buttonval();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderId = !genderId;
                setSelectedFemale();
                buttonval();
            }
        });

        edt_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    if (enteredString.length() > 1) {
                        edt_age.setText(enteredString.substring(1));
                    } else {
                        edt_age.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 0) {
                    int result = Integer.parseInt(enteredString);
                    if (result > 120) {
                        edt_age.setText(enteredString.substring(0, 2));
                        edt_age.setText("");
                    }
                }
                if (s.length() > 0) {
                    agesinteger = Integer.parseInt(s.toString());
                }
                if (!GlobalClass.isNull(edt_age.getText().toString())) {
                    if (agesinteger < 12) {
                        ageFlag = "3";
                        setAgeSpinner(R.array.Patientsagespinner);
                    }
                    if (agesinteger > 12) {
                        ageFlag = "2";
                        setAgeSpinner(R.array.Patientspinyrday);
                    }
                    if (agesinteger > 29) {
                        ageFlag = "1";
                        setAgeSpinner(R.array.Patientspinyr);
                    }
                }
                buttonval();
            }
        });

        edt_patient_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(activity, ToastFile.patient_crt_addr, 0);
                    if (enteredString.length() > 0) {
                        edt_patient_address.setText(enteredString.substring(1));
                    } else {
                        edt_patient_address.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        edt_patient_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0")) {
                    GlobalClass.showCustomToast(activity, ToastFile.patient_crt_pincode, 0);
                    if (enteredString.length() > 0) {
                        edt_patient_pincode.setText(enteredString.substring(1));
                    } else {
                        edt_patient_pincode.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        edt_coll_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(activity, ToastFile.coll_crt_addr, 0);
                    if (enteredString.length() > 0) {
                        edt_coll_address.setText(enteredString.substring(1));
                    } else {
                        edt_coll_address.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        edt_coll_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0")) {
                    GlobalClass.showCustomToast(activity, ToastFile.coll_crt_pincode, 0);
                    if (enteredString.length() > 0) {
                        edt_coll_pincode.setText(enteredString.substring(1));
                    } else {
                        edt_coll_pincode.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });

        edt_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(activity, ToastFile.entr_brcd, 0);
                    if (enteredString.length() > 0) {
                        edt_barcode.setText(enteredString.substring(1));
                    } else {
                        edt_barcode.setText("");
                    }
                }
                if (enteredString.length() == 0) {
                    verifyBarcodeFlag = true;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 8) {
                    flag = true;
                } else {
                    GlobalClass.showCustomToast(activity, ToastFile.crt_brcd, 0);
                }
                if (s.length() == 8) {
                    if (verifyBarcodeFlag) {
                        verifyBarcodeFlag = false;
                        if (flag) {
                            flag = false;
                            if (!cd.isConnectingToInternet()) {
                                edt_barcode.setText(s);
                            } else {
                                verifyBarcode(s.toString());
                            }
                        }
                    }
                }
                buttonval();
            }
        });

        edt_re_enter_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showCustomToast(activity, ToastFile.entr_brcd, 0);
                    if (enteredString.length() > 0) {
                        edt_re_enter_barcode.setText(enteredString.substring(1));
                    } else {
                        edt_re_enter_barcode.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (verifyBarcodeFlag) {
                    String enteredString = s.toString();
                    if (enteredString.length() == 8) {
                        String getPreviouseText = edt_barcode.getText().toString();
                        currentText = edt_re_enter_barcode.getText().toString();
                        if (getPreviouseText.equals(currentText)) {
                            GlobalClass.showCustomToast(activity, ToastFile.mtch_brcd, 0);
                        } else {
                            edt_re_enter_barcode.setText("");
                            GlobalClass.showCustomToast(activity, ToastFile.crt_brcd, 0);
                        }
                    }
                }
                buttonval();
            }
        });

        btn_choosefile_presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    /*if (editDetailsFlag) {
                        if (presclist != null && presclist.size() > 0) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image");
                        } else {*/
                    if (presc_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only one image", 0);
                    } else {
                        ispresciption = true;
                        isadhar = false;
                        isvial = false;
                        isother = false;
                        selectImage();
                    }
                        /*}
                    }*/
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    /*if (editDetailsFlag) {
                        if (aadharlist != null && aadharlist.size() == 2) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {*/
                    if (aadhar_file != null && aadhar_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = true;
                        isvial = false;
                        isother = false;
                        selectImage();
                    }
                       /* }
                    }*/
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_vial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    /*if (editDetailsFlag) {
                        if (viallist != null && viallist.size() > 0) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image");
                        } else {*/
                    if (vial_file != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only one images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = false;
                        isvial = true;
                        isother = false;
//                        selectImage();
                        GlobalClass.cropImageActivity(SRFCovidWOEEditActivity.this, 2);

                    }
                        /*}
                    }*/
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    /*if (editDetailsFlag) {
                        if (otherlist != null && otherlist.size() == 2) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {*/
                    if (other_file != null && other_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        ispresciption = false;
                        isadhar = false;
                        isvial = false;
                        isother = true;
                        selectImage();
                    }
                        /*}
                    }*/
                } else {
                    requestPermission();
                }
            }
        });

        btn_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (editDetailsFlag) {
                    if (selfielist != null && selfielist.size() > 0) {
                        GlobalClass.showCustomToast(activity, "Only 1 selfie can be Uploaded");
                    } else {*/
                if (selfie_flag == 0) {
                    boolean result = Utility.checkPermission(activity);
                    if (result) {
                        openCamera();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, "Only 1 selfie can be Uploaded", 0);
                }
                    /*}
                }*/
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearonreset();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {
                    if (amtvalidation()) {
                        callSRFCovidWOEAPI();
                    }
                }
            }
        });

        txt_presfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presclist != null && presclist.size() > 0) {
                    setviewpager(presclist, "pres");
                }
            }
        });

        txt_adharfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aadharlist != null && aadharlist.size() > 0) {
                    setviewpager(aadharlist, "adhar");
                }
            }
        });

        txt_vialrfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viallist != null && viallist.size() > 0) {
                    setviewpager(viallist, "vial");
                }
            }
        });

        txt_otherfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherlist != null && otherlist.size() > 0) {
                    setviewpager(otherlist, "other");
                }
            }
        });

        txt_selfiefileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setviewpager(selfielist, "selfie");
            }
        });

        mainlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.Hidekeyboard(view);
            }
        });

        edt_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonval();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        edt_fname.setText("");
                    } else {
                        edt_fname.setText(enteredString.substring(1));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        edt_lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        edt_lname.setText("");
                    } else {
                        edt_lname.setText(enteredString.substring(1));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonval();
            }
        });

        tv_coll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        selDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        selDate = GlobalClass.convertDate(selDate, "dd-MM-yyyy");
                        GlobalClass.SetText(tv_coll_date, selDate);
                        if (!GlobalClass.isNull(selTime)) {
                            validateSelectedDateTime();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        tv_coll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selTime = hourOfDay + ":" + minute;
                                selTime = GlobalClass.convertDate(selTime, "HH:mm");
                                GlobalClass.SetText(tv_coll_time, selTime);
                                if (!GlobalClass.isNull(selDate)) {
                                    validateSelectedDateTime();
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        spn_scp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                buttonval();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSelectedFemale() {
        gender = "F";
        female_red.setVisibility(View.VISIBLE);
        male.setVisibility(View.VISIBLE);
        male_red.setVisibility(View.GONE);
        female.setVisibility(View.GONE);
    }

    private void setSelectedMale() {
        gender = "M";
        male_red.setVisibility(View.VISIBLE);
        female.setVisibility(View.VISIBLE);
        female_red.setVisibility(View.GONE);
        male.setVisibility(View.GONE);
    }

    private void manageViews(boolean b) {
        edt_fname.setEnabled(b);
        edt_lname.setEnabled(b);
        edt_amt.setEnabled(b);
        edt_email.setEnabled(b);
        edt_srfid.setEnabled(b);
        edt_age.setEnabled(b);
        spn_age.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);
        edt_patient_address.setEnabled(b);
        edt_patient_pincode.setEnabled(b);
        spn_scp.setEnabled(b);
        edt_coll_address.setEnabled(b);
        edt_coll_pincode.setEnabled(b);
        tv_coll_date.setEnabled(b);
        tv_coll_time.setEnabled(b);
        edt_barcode.setEnabled(b);
        edt_re_enter_barcode.setEnabled(b);
        img_scan__barcode.setEnabled(b);
        btn_barcd.setEnabled(b);
        if (b) {
            ll_submit.setVisibility(View.VISIBLE);
            GlobalClass.SetText(tv_title, "Edit details");
            verifyBarcodeFlag = false;
            editDetailsFlag = true;
            edt_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            edt_re_enter_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        } else {
            GlobalClass.SetText(tv_title, "View details");
            ll_submit.setVisibility(View.GONE);
            verifyBarcodeFlag = false;
            editDetailsFlag = false;
            btn_choosefile_presc.setVisibility(View.GONE);
            btn_choosefile_adhar.setVisibility(View.GONE);
            btn_choosefile_vial.setVisibility(View.GONE);
            btn_choosefile_other.setVisibility(View.GONE);
            btn_selfie.setVisibility(View.GONE);

            ll_prescription.setVisibility(View.GONE);
            ll_aadhar.setVisibility(View.GONE);
            ll_vial.setVisibility(View.GONE);
            ll_other.setVisibility(View.GONE);
            ll_selfie.setVisibility(View.GONE);
            GlobalClass.SetButtonText(btn_barcd, woeDetailsModel.getBarcode());


        }
    }

    private void setData() {
        String[] separated = woeDetailsModel.getPatientName().split(" ");
        if (separated.length == 1) {
            GlobalClass.EditSetText(edt_fname, separated[0].trim());
        } else if (separated.length == 2) {
            for (int i = 0; i < separated.length; i++) {
                if (i == 0) {
                    GlobalClass.EditSetText(edt_fname, separated[0].trim());
                } else {
                    GlobalClass.EditSetText(edt_lname, separated[1].trim());
                }
            }
        } else if (separated.length == 3) {
            for (int i = 0; i < separated.length; i++) {
                if (i == 0) {
                    GlobalClass.EditSetText(edt_fname, separated[0].trim());
                } else {
                    GlobalClass.EditSetText(edt_lname, separated[1].trim());
                }
            }
        }
        GlobalClass.SetText(tv_verifiedmob, woeDetailsModel.getMobile());
        GlobalClass.EditSetText(edt_amt, woeDetailsModel.getAmount_Collected());
        GlobalClass.EditSetText(edt_srfid, woeDetailsModel.getSRFID());
        GlobalClass.EditSetText(edt_age, woeDetailsModel.getAge());
        GlobalClass.EditSetText(edt_email, woeDetailsModel.getEMAIL());
        if (!GlobalClass.isNull(ageFlag)) {
            if (ageFlag.equals("3")) {
                switch (woeDetailsModel.getAgeType()) {
                    case "Y":
                        spn_age.setSelection(0);
                        break;
                    case "M":
                        spn_age.setSelection(1);
                        break;
                    case "D":
                        spn_age.setSelection(2);
                        break;
                }
            }
            if (ageFlag.equals("2")) {
                switch (woeDetailsModel.getAgeType()) {
                    case "Y":
                        spn_age.setSelection(0);
                        break;
                    case "D":
                        spn_age.setSelection(1);
                        break;
                }
            }
            if (ageFlag.equals("1")) {
                switch (woeDetailsModel.getAgeType()) {
                    case "Y":
                        spn_age.setSelection(0);
                        break;
                }
            }
        }
        if (!GlobalClass.isNull(woeDetailsModel.getGender())) {
            if (woeDetailsModel.getGender().equalsIgnoreCase("M")) {
                setSelectedMale();
            } else if (woeDetailsModel.getGender().equalsIgnoreCase("F")) {
                setSelectedFemale();
            }
        }
        GlobalClass.EditSetText(edt_patient_address, woeDetailsModel.getPatientAddress());
        GlobalClass.EditSetText(edt_patient_pincode, woeDetailsModel.getPatientPincode());
        if (!GlobalClass.isNull(woeDetailsModel.getSCP())) {
            if (woeDetailsModel.getSCP().equalsIgnoreCase("HOME")) {
                spn_scp.setSelection(1);
            } else if (woeDetailsModel.getSCP().equalsIgnoreCase("DPS")) {
                spn_scp.setSelection(2);
            }
        }
        GlobalClass.EditSetText(edt_coll_address, woeDetailsModel.getCollectionAddress());
        GlobalClass.EditSetText(edt_coll_pincode, woeDetailsModel.getCollectionPincode());
        String[] sct = woeDetailsModel.getSCT().split(" ");
        if (sct.length == 1) {
            GlobalClass.SetText(tv_coll_date, sct[0].trim());
            selDate = sct[0].trim();
        } else if (sct.length == 2) {
            for (int i = 0; i < sct.length; i++) {
                if (i == 0) {
                    GlobalClass.SetText(tv_coll_date, sct[0].trim());
                    selDate = sct[0].trim();
                } else {
                    GlobalClass.SetText(tv_coll_time, sct[1].trim());
                    selTime = sct[1].trim();
                }
            }
        }
        GlobalClass.EditSetText(edt_barcode, woeDetailsModel.getBarcode());
        GlobalClass.EditSetText(edt_re_enter_barcode, woeDetailsModel.getBarcode());
        manageImagesView();
    }

    private void manageImagesView() {
        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_selfie.setText(getResources().getString(R.string.nofilechoosen));
        if (!editDetailsFlag) {
            presclist.clear();
            if (!GlobalClass.isNull(woeDetailsModel.getPrescription())) {
                btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                lin_pres_preview.setVisibility(View.VISIBLE);
                presclist.add(woeDetailsModel.getPrescription().replaceAll("\\\\", "//"));
                txt_nofilepresc.setVisibility(View.GONE);
                txt_presfileupload.setVisibility(View.VISIBLE);
                txt_presfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_presfileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                lin_pres_preview.setVisibility(View.GONE);
                txt_nofilepresc.setVisibility(View.VISIBLE);
                txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
            }
            aadharlist.clear();
            if (!GlobalClass.isNull(woeDetailsModel.getAadhar())) {
                lin_adhar_images.setVisibility(View.VISIBLE);
                txt_adharfileupload.setVisibility(View.VISIBLE);
                txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_nofileadhar.setVisibility(View.GONE);
                aadharlist.add(woeDetailsModel.getAadhar().replaceAll("\\\\", "//"));
            } else {
                lin_adhar_images.setVisibility(View.GONE);
                txt_nofileadhar.setVisibility(View.VISIBLE);
                txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
            }
            if (!GlobalClass.isNull(woeDetailsModel.getAadhar1())) {
                lin_adhar_images.setVisibility(View.VISIBLE);
                aadharlist.add(woeDetailsModel.getAadhar1().replaceAll("\\\\", "//"));
                txt_adharfileupload.setVisibility(View.VISIBLE);
                txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_nofileadhar.setVisibility(View.GONE);
            }
            if (!GlobalClass.isNull(woeDetailsModel.getAadhar()) && !GlobalClass.isNull(woeDetailsModel.getAadhar1())) {
                btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
            }
            viallist.clear();
            if (!GlobalClass.isNull(woeDetailsModel.getVialImage())) {
                btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                lin_vial_images.setVisibility(View.VISIBLE);
                viallist.add(woeDetailsModel.getVialImage().replaceAll("\\\\", "//"));
                txt_vialrfileupload.setVisibility(View.VISIBLE);
                txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_nofilevial.setVisibility(View.GONE);
            } else {
                lin_vial_images.setVisibility(View.GONE);
                txt_nofilevial.setVisibility(View.VISIBLE);
                txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
            }
            otherlist.clear();
            if (!GlobalClass.isNull(woeDetailsModel.getOther())) {
                lin_other_images.setVisibility(View.VISIBLE);
                otherlist.add(woeDetailsModel.getOther().replaceAll("\\\\", "//"));
                txt_otherfileupload.setVisibility(View.VISIBLE);
                txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_nofileother.setVisibility(View.GONE);
            } else {
                lin_other_images.setVisibility(View.GONE);
                txt_nofileother.setVisibility(View.VISIBLE);
                txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
            }
            if (!GlobalClass.isNull(woeDetailsModel.getOther1())) {
                lin_other_images.setVisibility(View.VISIBLE);
                otherlist.add(woeDetailsModel.getOther1().replaceAll("\\\\", "//"));
                txt_otherfileupload.setVisibility(View.VISIBLE);
                txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_nofileother.setVisibility(View.GONE);
            }
            if (!GlobalClass.isNull(woeDetailsModel.getOther()) && !GlobalClass.isNull(woeDetailsModel.getOther1())) {
                btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
            }
            selfielist.clear();
            if (!GlobalClass.isNull(woeDetailsModel.getSelfie())) {
                btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                btn_selfie.setTextColor(getResources().getColor(R.color.black));
                lin_selfie.setVisibility(View.VISIBLE);
                selfielist.add(woeDetailsModel.getSelfie().replaceAll("\\\\", "//"));
                txt_selfiefileupload.setVisibility(View.VISIBLE);
                txt_selfiefileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_selfiefileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_selfie.setVisibility(View.GONE);
            } else {
                lin_selfie.setVisibility(View.GONE);
                txt_selfie.setVisibility(View.VISIBLE);
                txt_selfie.setText(getResources().getString(R.string.nofilechoosen));
            }
        }
    }

    private void callSRFCovidWOEAPI() {
        if (!cd.isConnectingToInternet()) {
            GlobalClass.showAlertDialog(activity);
        } else {
            try {
                String age_type;
                if (spn_age.getSelectedItem().toString().equalsIgnoreCase("Years")) {
                    age_type = "Y";
                } else if (spn_age.getSelectedItem().toString().equalsIgnoreCase("Months")) {
                    age_type = "M";
                } else {
                    age_type = "D";
                }
                String fname = edt_fname.getText().toString().trim();
                String lname = edt_lname.getText().toString().trim();
                String fullname = fname + " " + lname;

                Covidpostdata covidpostdata = new Covidpostdata();
                covidpostdata.setUNIQUEID(woeDetailsModel.getUniqueId());
                covidpostdata.setSOURCECODE(usercode);
                covidpostdata.setNAME(fullname);
                covidpostdata.setMOBILE(tv_verifiedmob.getText().toString());
                covidpostdata.setAMOUNTCOLLECTED(edt_amt.getText().toString().trim());
                covidpostdata.setSRFID(edt_srfid.getText().toString().trim());
                covidpostdata.setAGE(edt_age.getText().toString().trim());
                covidpostdata.setAGETYPE(age_type);
                covidpostdata.setGENDER(gender);
                covidpostdata.setTESTCODE("" + woeDetailsModel.getTestCode());
                covidpostdata.setPATIENTADDRESS(edt_patient_address.getText().toString().trim());
                covidpostdata.setPATIENTPINCODE(edt_patient_pincode.getText().toString().trim());
                covidpostdata.setSCP(spn_scp.getSelectedItem().toString());
                covidpostdata.setCOLLECTIONADDRESS(edt_coll_address.getText().toString().trim());
                covidpostdata.setCOLLECTIONPINCODE(edt_coll_pincode.getText().toString().trim());
                covidpostdata.setSPECIMENDATE(selDate);
                covidpostdata.setSPECIMENTIME(selTime);
                covidpostdata.setBARCODE(btn_barcd.getText().toString().trim());
                covidpostdata.setVIAIMAGE(vial_file);
                covidpostdata.setEMAIL(edt_email.getText().toString());
                covidpostdata.setAPIKEY(apikey);
                if (selfie_file != null) {
                    covidpostdata.setSELFIE(selfie_file);
                }
                if (presc_file != null) {
                    covidpostdata.setPRESCRIPTION(presc_file);
                }
                if (aadhar_file != null && aadhar_file1 != null) {
                    covidpostdata.setADHAR(aadhar_file);
                    covidpostdata.setADHAR1(aadhar_file1);
                } else if (aadhar_file != null && aadhar_file1 == null) {
                    covidpostdata.setADHAR(aadhar_file);
                } else {
                    covidpostdata.setADHAR(aadhar_file1);
                }
                if (other_file != null && other_file1 != null) {
                    covidpostdata.setOTHER(other_file);
                    covidpostdata.setOTHER1(other_file1);
                } else if (other_file != null && other_file1 == null) {
                    covidpostdata.setOTHER(other_file);
                } else {
                    covidpostdata.setOTHER(other_file1);
                }
                new SRFCovidWOEmultipart_controller(SRFCovidWOEEditActivity.this, covidpostdata).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void validateSelectedDateTime() {
        String selDateTime = selDate + " " + selTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = GlobalClass.dateFromString(selDateTime, simpleDateFormat);
        if (date.after(new Date())) {
            GlobalClass.showCustomToast(activity, ToastFile.SELECT_VALID_COLL_TIME, 0);
            tv_coll_time.setText("");
        }
    }

    private void displayrate() {

        CovidRateReqModel covidRateReqModel = new CovidRateReqModel();
        covidRateReqModel.setUsercode("" + usercode);
        covidRateReqModel.setAPIKEY("" + apikey);
        covidRateReqModel.setTestcode("" + woeDetailsModel.getTestCode());

        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);
        Call<Covidratemodel> covidratemodelCall = postAPIInteface.displayrates(covidRateReqModel);
        covidratemodelCall.enqueue(new Callback<Covidratemodel>() {
            @Override
            public void onResponse(Call<Covidratemodel> call, Response<Covidratemodel> response) {
                try {
                    if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        if (!GlobalClass.isNull(response.body().getB2B())) {
                            b2b = response.body().getB2B();
                            Global.B2B = b2b;
                        }
                        if (!GlobalClass.isNull(response.body().getB2C())) {
                            b2c = response.body().getB2C();
                            Global.B2C = b2c;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidratemodel> call, Throwable t) {

            }
        });

    }

    private void verifyBarcode(final String s) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        RequestQueue requestQueue = GlobalClass.setVolleyReq(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + apikey + "/" + s + "/getcheckbarcode", new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    GlobalClass.printLog(Constants.ERROR, TAG, "Barcode response: ", "" + response);
                    VerifyBarcodeResponseModel responseModel = new Gson().fromJson(String.valueOf(response), VerifyBarcodeResponseModel.class);
                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("BARCODE DOES NOT EXIST")) {
                            btn_barcd.setText(s);
                            lineareditbarcode.setVisibility(View.GONE);
                            consignment_name_layout.setVisibility(View.VISIBLE);
                        } else if (!GlobalClass.isNull(responseModel.getERROR()) && responseModel.getERROR().equalsIgnoreCase(caps_invalidApikey)) {
                            GlobalClass.redirectToLogin(activity);
                        } else {
                            edt_barcode.setText("");
                            edt_re_enter_barcode.setText("");
                            GlobalClass.showCustomToast(activity, "" + responseModel.getResponse(), 0);
                        }
                    } else {
                        GlobalClass.showCustomToast(activity, ToastFile.something_went_wrong, 0);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        requestQueue.add(jsonObjectRequest);
        GlobalClass.printLog(Constants.ERROR, TAG, "Barcode response: ", "" + jsonObjectRequest);
    }

    private void setAgeSpinner(int array) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, array, R.layout.name_age_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_age.setAdapter(adapter);
    }

    private void setSCPSpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(activity, R.array.scp_entries, R.layout.name_age_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_scp.setAdapter(adapter);
    }

    private void buttonval() {
        if (Validation()) {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.covidbtn));
            btn_submit.setTextColor(getResources().getColor(R.color.maroon));
            btn_submit.setEnabled(true);
            btn_submit.setClickable(true);
        } else {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
            btn_submit.setTextColor(getResources().getColor(R.color.black));
            btn_submit.setEnabled(false);
            btn_submit.setClickable(false);
        }
    }

    private void clearonreset() {
        rel_verify_mobile.setVisibility(View.VISIBLE);
        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        edt_fname.getText().clear();
        edt_lname.getText().clear();
        edt_amt.getText().clear();
        edt_srfid.getText().clear();
        edt_age.getText().clear();
        male_red.setVisibility(View.GONE);
        female.setVisibility(View.VISIBLE);
        btn_barcd.setText("BARCODE*");
        enter_barcode.getText().clear();
        reenter.getText().clear();
        female_red.setVisibility(View.GONE);
        male.setVisibility(View.VISIBLE);
        edt_patient_address.getText().clear();
        edt_patient_pincode.getText().clear();
        spn_age.setSelection(0);
        spn_scp.setSelection(0);
        edt_coll_address.getText().clear();
        edt_coll_pincode.getText().clear();
        tv_coll_date.setText("");
        tv_coll_time.setText("");
        edt_barcode.getText().clear();
        edt_re_enter_barcode.getText().clear();

        presc_file = null;
        aadhar_file = null;
        aadhar_file1 = null;
        vial_file = null;
        other_file = null;
        other_file1 = null;
        selfie_file = null;
        selfie_flag = 0;

        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            edt_email.setHint("EMAIL ID*");
        } else {
            edt_email.setHint("EMAIL ID");
        }

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);
        lin_selfie.setVisibility(View.GONE);

        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);
        txt_selfie.setVisibility(View.VISIBLE);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_selfie.setText(getResources().getString(R.string.nofilechoosen));
        txt_selfie.setTextColor(getResources().getColor(R.color.black));
        txt_selfie.setPaintFlags(0);

        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setTextColor(getResources().getColor(R.color.black));
        txt_nofileother.setPaintFlags(0);

        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.maroon));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));

        btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidbtn));
        btn_selfie.setTextColor(getResources().getColor(R.color.maroon));

        presclist.clear();
        aadharlist.clear();
        viallist.clear();
        otherlist.clear();
        selfielist.clear();

        btn_submit.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
        btn_submit.setTextColor(getResources().getColor(R.color.black));
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
    }

    private boolean amtvalidation() {
        if (GlobalClass.isNull(edt_amt.getText().toString())) {
            return false;
        }
        if (GlobalClass.isNull(edt_amt.getText().toString())) {
            return false;
        }
        try {
            if (!GlobalClass.isNull(b2c) && Integer.parseInt(edt_amt.getText().toString()) > Integer.parseInt(b2c)) {
                GlobalClass.showCustomToast(activity, "You cannot enter collected amount more than " + b2c, 0);
                return false;
            } else if (!GlobalClass.isNull(b2b) && Integer.parseInt(edt_amt.getText().toString()) < Integer.parseInt(b2b)) {
                GlobalClass.showCustomToast(activity, "You cannot enter collected amount less than " + b2b, 0);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            if (GlobalClass.isNull(edt_email.getText().toString())) {
                Global.showCustomToast(activity, "Enter Email-ID");
                edt_email.requestFocus();
                return false;
            }

            if (!GlobalClass.isValidEmail(edt_email.getText().toString())) {
                Global.showCustomToast(activity, "Enter valid Email-ID");
                edt_email.requestFocus();
                return false;
            }
        } else {
            if (!TextUtils.isEmpty(edt_email.getText().toString())) {
                if (!GlobalClass.isValidEmail(edt_email.getText().toString())) {
                    Global.showCustomToast(activity, "Enter valid Email-ID");
                    edt_email.requestFocus();
                    return false;
                }
            }
        }


        if (btn_barcd.getText().toString().equalsIgnoreCase("BARCODE*")) {
            Global.showCustomToast(activity, "Enter Barcode");
            return false;
        }

        return true;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);
                if (items[item].equals("Choose from Library")) {
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
        Uri imageUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        int REQUEST_CAMERA = 1;
        startActivityForResult(intent, REQUEST_CAMERA);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
          /* try {
                String imageurl = camera.getCameraBitmapPath();
                selfie_file = new File(imageurl);
                txt_selfie.setVisibility(View.GONE);
                lin_selfie.setVisibility(View.VISIBLE);
                selfie_flag = 1;
                txt_selfiefileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_selfiefileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                btn_selfie.setTextColor(getResources().getColor(R.color.black));
                btn_selfie.setEnabled(false);
                selfielist.add(selfie_file.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try {
                if (iscamera) {
                    String imageurl = camera.getCameraBitmapPath();
                    selfie_file = new File(imageurl);
                    txt_selfie.setVisibility(View.GONE);
                    lin_selfie.setVisibility(View.VISIBLE);
                    selfie_flag = 1;
                    txt_selfiefileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_selfiefileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                    btn_selfie.setTextColor(getResources().getColor(R.color.black));
                    btn_selfie.setEnabled(false);
                    selfielist.add(selfie_file.toString());
                } else if (ispresciption) {
                    presc_file = new File(camera.getCameraBitmapPath());
                    lin_pres_preview.setVisibility(View.VISIBLE);
                    txt_presfileupload.setVisibility(View.VISIBLE);
                    txt_presfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_presfileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilepresc.setVisibility(View.GONE);
                    presclist = new ArrayList<>();
                    presclist.clear();
                    presclist.add(presc_file.toString());
                    if (presc_file != null) {
                        ispresciption = false;
                        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                } else if (isadhar) {
                    if (lin_adhar_images.getVisibility() == View.VISIBLE && aadhar_file != null) {
                        aadhar_file1 = new File(camera.getCameraBitmapPath());
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file1.toString());
                    } else {
                        aadhar_file = new File(camera.getCameraBitmapPath());
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file.toString());
                    }
                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                    }
                    buttonval();
                    lin_adhar_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    vial_file = new File(camera.getCameraBitmapPath());
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilevial.setVisibility(View.GONE);
                    viallist.add(vial_file.toString());
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                } else if (isother) {
                    if (lin_other_images.getVisibility() == View.VISIBLE && other_file != null) {
                        other_file1 = new File(camera.getCameraBitmapPath());

                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file1.toString());
                    } else {
                        other_file = new File(camera.getCameraBitmapPath());
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file.toString());
                    }
                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    }
                    buttonval();
                    lin_other_images.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                GlobalClass.showCustomToast(activity, "Failed to load image!", 0);
                return;
            }
            try {
                if (data.getData() != null) {
                    vial_file = ImagePicker.Companion.getFile(data);
                }
                vial_file = GlobalClass.getCompressedFile(activity, vial_file);
                lin_vial_images.setVisibility(View.VISIBLE);
                txt_vialrfileupload.setVisibility(View.VISIBLE);
                txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                txt_nofilevial.setVisibility(View.GONE);
                viallist.add(vial_file.toString());
                if (vial_file != null) {
                    isvial = false;
                    btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                    btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                    buttonval();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                GlobalClass.showCustomToast(activity, "Failed to load image!", 0);
                return;
            }
            try {
                if (ispresciption) {
                    if (data.getData() != null) {
                        presc_file = FileUtil.from(activity, data.getData());
                    }
                    presc_file = GlobalClass.getCompressedFile(activity, presc_file);
                    lin_pres_preview.setVisibility(View.VISIBLE);
                    txt_presfileupload.setVisibility(View.VISIBLE);
                    txt_presfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_presfileupload.setPaintFlags(txt_presfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilepresc.setVisibility(View.GONE);
                    presclist.add(presc_file.toString());
                    if (presc_file != null) {
                        ispresciption = false;
                        btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                } else if (isadhar) {
                    if (lin_adhar_images.getVisibility() == View.VISIBLE && aadhar_file != null) {
                        if (data.getData() != null) {
                            if (aadhar_file1 == null) {
                                aadhar_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        aadhar_file1 = GlobalClass.getCompressedFile(activity, aadhar_file1);
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            if (aadhar_file == null) {
                                aadhar_file = FileUtil.from(activity, data.getData());
                            }
                        }
                        aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file.toString());
                    }
                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                    }
                    buttonval();
                    lin_adhar_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    if (data.getData() != null) {
                        vial_file = FileUtil.from(activity, data.getData());
                    }
                    vial_file = GlobalClass.getCompressedFile(activity, vial_file);
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilevial.setVisibility(View.GONE);
                    viallist.add(vial_file.toString());
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                } else if (isother) {
                    if (lin_other_images.getVisibility() == View.VISIBLE && other_file != null) {
                        if (data.getData() != null) {
                            if (other_file1 == null) {
                                other_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        other_file1 = GlobalClass.getCompressedFile(activity, other_file1);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            other_file = FileUtil.from(activity, data.getData());
                        }
                        other_file = GlobalClass.getCompressedFile(activity, other_file);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file.toString());
                    }
                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        buttonval();
                    }
                    lin_other_images.setVisibility(View.VISIBLE);
                }

            } catch (IOException e) {
                GlobalClass.showCustomToast(activity, "Failed to read image data!", 0);
                e.printStackTrace();
            }
        } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if (isvial) {
                    if (data.getData() != null) {
                        vial_file = ImagePicker.Companion.getFile(data);
                    }
                    vial_file = GlobalClass.getCompressedFile(activity, vial_file);
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setVisibility(View.VISIBLE);
                    txt_vialrfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                    txt_vialrfileupload.setPaintFlags(txt_vialrfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    txt_nofilevial.setVisibility(View.GONE);
                    viallist.add(vial_file.toString());
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                        buttonval();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result != null) {
            if (result.getContents() != null) {
                if (result.getContents().length() == 8) {
                    verifyBarcode(result.getContents());
                } else {
                    Global.showCustomToast(activity, "Invalid Barcode");
                }
            } else {
                Global.showCustomToast(activity, "Something Went Wrong");
            }
        }
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        int SELECT_FILE = 0;
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private boolean Validation() {
        if (edt_fname.getText().toString().length() == 0) {
            return false;
        }
        if (edt_fname.getText().toString().length() < 2) {
            return false;
        }
        if (edt_lname.getText().toString().length() == 0) {
            return false;
        }
        if (edt_lname.getText().toString().length() < 1) {
            return false;
        }
        if (GlobalClass.isNull(edt_amt.getText().toString())) {
            return false;
        }
        if (GlobalClass.isNull(edt_srfid.getText().toString())) {
            return false;
        }

        if (edt_srfid.getText().length() < 10) {
            Global.showCustomToast(activity, "SRF-ID should be minimum of 10 digits");
            return false;
        }

        if (GlobalClass.isNull(edt_age.getText().toString())) {
            return false;
        }
        if (GlobalClass.isNull(gender)) {
            return false;
        }
        if (GlobalClass.isNull(edt_patient_address.getText().toString())) {
            return false;
        }
        if (edt_patient_address.getText().toString().length() < 25) {
            return false;
        }
        if (GlobalClass.isNull(edt_patient_pincode.getText().toString())) {
            return false;
        }
        if (edt_patient_pincode.getText().toString().length() < 6) {
            return false;
        }
        if (edt_patient_pincode.getText().toString().startsWith("0") || edt_patient_pincode.getText().toString().startsWith("9")) {
            return false;
        }
        if (spn_scp.getSelectedItemPosition() == 0) {
            return false;
        }
        if (GlobalClass.isNull(edt_coll_address.getText().toString())) {
            return false;
        }
        if (edt_coll_address.getText().toString().length() < 25) {
            return false;
        }
        if (GlobalClass.isNull(edt_coll_pincode.getText().toString())) {
            return false;
        }
        if (edt_coll_pincode.getText().toString().length() < 6) {
            return false;
        }
        if (edt_coll_pincode.getText().toString().startsWith("0") || edt_coll_pincode.getText().toString().startsWith("9")) {
            return false;
        }
        if (GlobalClass.isNull(selDate)) {
            return false;
        }
        if (GlobalClass.isNull(selTime)) {
            return false;
        }

       /* if (GlobalClass.isNull(edt_barcode.getText().toString())) {
            return false;
        }
        if (edt_barcode.getText().toString().length() < 8) {
            return false;
        }
        if (GlobalClass.isNull(edt_re_enter_barcode.getText().toString())) {
            return false;
        }
        if (edt_re_enter_barcode.getText().toString().length() < 8) {
            return false;
        }
        if (!edt_barcode.getText().toString().trim().equalsIgnoreCase(edt_re_enter_barcode.getText().toString().trim())) {
            return false;
        }*/


        if (editDetailsFlag) {
            if (viallist != null && viallist.size() > 0) {
                return true;
            } else {
                if (vial_file == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void getUploadResponse(String response,String mobile) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");
            if (!GlobalClass.isNull(RESPONSEID) && RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
                new LogUserActivityTagging(activity,"WOE-SRF(COVID)",mobile);

                Intent i = new Intent(activity, SRFCovidWOEMainActivity.class);
                startActivity(i);
                finish();
                Constants.SRFcovidWOEfrag_flag = "1";
            } else {
                Global.showCustomToast(activity, RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setviewpager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = maindialog.findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, imagelist, 0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        final PageIndicatorView pageIndicatorView = maindialog.findViewById(R.id.pageIndicatorView);
        if (imagelist != null && imagelist.size() > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
            pageIndicatorView.setCount(imagelist.size()); // specify total count of indicators
            pageIndicatorView.setSelection(0);
            pageIndicatorView.setSelectedColor(activity.getResources().getColor(R.color.maroon));
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
        if (editDetailsFlag) {
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Set the Alert Dialog Message
                builder.setMessage(ToastFile.deletefile);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (type.equalsIgnoreCase("pres")) {
                            presc_file = null;
                            presclist.clear();
                            txt_nofilepresc.setVisibility(View.VISIBLE);
                            txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
                            lin_pres_preview.setVisibility(View.GONE);
                            btn_choosefile_presc.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_choosefile_presc.setTextColor(getResources().getColor(R.color.maroon));
                            txt_nofilepresc.setVisibility(View.VISIBLE);
                            buttonval();
                        } else if (type.equalsIgnoreCase("adhar")) {
                            if (aadharlist != null && aadharlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (aadhar_file != null) {
                                        aadhar_file = null;
                                    } else {
                                        aadhar_file1 = null;
                                    }
                                    aadharlist.remove(aadharlist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    aadhar_file1 = null;
                                    if (aadharlist.size() == 2) {
                                        aadharlist.remove(aadharlist.get(1));
                                    } else {
                                        aadharlist.remove(aadharlist.get(0));
                                    }
                                }
                                if (aadharlist.size() == 1) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (aadharlist.size() == 2) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_nofileadhar.setVisibility(View.VISIBLE);
                                    txt_adharfileupload.setVisibility(View.GONE);
                                }
                                buttonval();
                            } else {
                                txt_nofileadhar.setVisibility(View.VISIBLE);
                                lin_adhar_images.setVisibility(View.GONE);
                            }
                        } else if (type.equalsIgnoreCase("vial")) {
                            vial_file = null;
                            viallist.clear();
                            txt_nofilevial.setVisibility(View.VISIBLE);
                            txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
                            lin_vial_images.setVisibility(View.GONE);
                            btn_choosefile_vial.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_choosefile_vial.setTextColor(getResources().getColor(R.color.maroon));
                            txt_nofilevial.setVisibility(View.VISIBLE);
                            buttonval();
                        } else if (type.equalsIgnoreCase("other")) {
                            if (otherlist != null && otherlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (other_file != null) {
                                        other_file = null;
                                    } else {
                                        other_file1 = null;
                                    }
                                    otherlist.remove(otherlist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    other_file1 = null;
                                    if (otherlist.size() == 2) {
                                        otherlist.remove(otherlist.get(1));
                                    } else {
                                        otherlist.remove(otherlist.get(0));
                                    }
                                }
                                if (otherlist.size() == 1) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (otherlist.size() == 2) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.maroon));
                                    txt_nofileother.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.GONE);
                                }
                            } else {
                                txt_nofileother.setVisibility(View.VISIBLE);
                                lin_other_images.setVisibility(View.GONE);
                            }
                            buttonval();
                        } else if (type.equalsIgnoreCase("selfie")) {
                            selfie_file = null;
                            selfie_flag = 0;
                            selfielist.clear();
                            lin_selfie.setVisibility(View.GONE);
                            txt_selfie.setVisibility(View.VISIBLE);
                            btn_selfie.setEnabled(true);
                            btn_selfie.setBackground(getResources().getDrawable(R.drawable.covidbtn));
                            btn_selfie.setTextColor(getResources().getColor(R.color.maroon));
                        }
                        dialog.dismiss();
                        maindialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ImageView ic_close = maindialog.findViewById(R.id.img_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maindialog.dismiss();
            }
        });
        maindialog.setCancelable(true);
        maindialog.show();
    }
}