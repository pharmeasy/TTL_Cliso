package com.example.e5322.thyrosoft.Fragment;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.ToastFile.invalid_brcd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.HomeMenuActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.SCollectionPAdapter;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.BuildConfig;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.CovidRatesController;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.getSCPSRFAPIController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.CovidRateReqModel;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.Hospital_model;
import com.example.e5322.thyrosoft.Models.Hospital_req;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.getSCPTechnicianModel;
import com.example.e5322.thyrosoft.Models.WOERequestModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;
import com.rd.PageIndicatorView;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RATEnterFrag extends Fragment {

    Button btn_generate, btn_resend, btn_verify;
    View root;
    String hospt_ID;
    EditText edt_missed_mobile, edt_verifycc;
    ConnectionDetector cd;
    Activity activity;
    String usercode, apikey;
    SharedPreferences preferences;
    CountDownTimer countDownTimer;
    String TAG = getClass().getSimpleName();
    TextView tv_timer, tv_resetno, tv_mobileno, tv_verifiedmob, txt_barcode;
    private boolean timerflag = false;
    RelativeLayout rel_mobno;
    LinearLayout lin_generate_verify, lin_by_missed, ll_enterView;
    RadioButton by_missed, by_generate, by_sendsms;
    ImageView img_scanbarcode;
    RequestQueue requestQueue;
    private Camera camera;
    String ERROR, RES_ID, barcode, response1;
    RelativeLayout rel_verify_mobile;
    Button btn_submit, btn_reset, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_other;
    Spinner spr_gender;
    SearchableSpinner spr_hospital;
    private String userChoosenTask;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    EditText edt_firstname, edt_lastname, edt_age, edt_amtcollected, edt_email;
    RadioButton rd_home, rd_dps;
    File aadhar_file = null;

    File aadhar_file1 = null;
    private int PERMISSION_REQUEST_CODE = 200;
    boolean isadhar, istrf, isother;
    File trf_file = null;
    File trf_file1 = null;
    File other_file = null;
    File other_file1 = null;
    LinearLayout lin_adhar_images, lin_trf_images, lin_other_images;
    TextView txt_adharfileupload, txt_nofileadhar, txt_trffileupload, txt_nofiletrf, txt_otherfileupload, txt_nofileother;
    List<String> aadharlist = new ArrayList<>();
    List<String> trflist = new ArrayList<>();
    List<String> otherlist = new ArrayList<>();
    List<String> patientsagespinner = Arrays.asList("Gender", "Male", "Female");
    List<Hospital_model.HospitalDETAILSBean> hospitalDETAILSBeanList = new ArrayList<>();
    List<String> hospitalname = new ArrayList<>();
    private int agesinteger;
    AppPreferenceManager appPreferenceManager;

    TextView tv_help;
    boolean OTPAccess;
    RadioGroup radiogrp2;

    TextView samplecollectionponit;
    private getSCPTechnicianModel.getTechnicianlist selectedSCT;
    private ArrayList<getSCPTechnicianModel.getTechnicianlist> filterPatientsArrayList;
    private boolean isgetDOB = false;
    String b2b = "0", b2c = "0";

    public RATEnterFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_r_a_t_enter, container, false);

        activity = getActivity();
        cd = new ConnectionDetector(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        try {
            if (appPreferenceManager != null && appPreferenceManager.getCovidAccessResponseModel() != null) {
                OTPAccess = appPreferenceManager.getCovidAccessResponseModel().isCovidOtpAllow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initui(root);
        initlistner();
        callRatesApi();
        if (cd.isConnectingToInternet()) {
            GetHospitalList();
        }
        return root;
    }

    private void initui(View root) {
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        btn_generate = root.findViewById(R.id.btn_generate);
        btn_generate.setText(getResources().getString(R.string.enterccc));
        edt_missed_mobile = root.findViewById(R.id.edt_missed_mobile);
        btn_resend = root.findViewById(R.id.btn_resend);
        tv_timer = root.findViewById(R.id.tv_timer);
        edt_verifycc = root.findViewById(R.id.edt_verifycc);
        tv_resetno = root.findViewById(R.id.tv_resetno);
        rel_mobno = root.findViewById(R.id.rel_mobno);
        lin_other_images = root.findViewById(R.id.lin_other_images);
        lin_generate_verify = root.findViewById(R.id.lin_generate_verify);
        tv_mobileno = root.findViewById(R.id.tv_mobileno);
        lin_by_missed = root.findViewById(R.id.lin_missed_verify);
        by_missed = root.findViewById(R.id.by_missed);
        by_generate = root.findViewById(R.id.by_generate);
        by_sendsms = root.findViewById(R.id.by_sendsms);
        btn_verify = root.findViewById(R.id.btn_verify);
        edt_email = root.findViewById(R.id.edt_email);
        tv_help = root.findViewById(R.id.tv_help);
        tv_help.setText(Html.fromHtml("<u> Help</u>"));
        spr_gender = root.findViewById(R.id.spr_gender);
        btn_choosefile_other = root.findViewById(R.id.btn_choosefile_other);
        spr_hospital = root.findViewById(R.id.spr_hospital);
        spr_hospital.setTitle("");
        txt_otherfileupload = root.findViewById(R.id.txt_otherfileupload);
        txt_nofileadhar = root.findViewById(R.id.txt_nofileadhar);
        txt_adharfileupload = root.findViewById(R.id.txt_adharfileupload);
        lin_adhar_images = root.findViewById(R.id.lin_adhar_images);
        btn_choosefile_trf = root.findViewById(R.id.btn_choosefile_trf);
        txt_trffileupload = root.findViewById(R.id.txt_trffileupload);
        txt_nofiletrf = root.findViewById(R.id.txt_nofiletrf);
        txt_nofileother = root.findViewById(R.id.txt_nofileother);
        lin_trf_images = root.findViewById(R.id.lin_trf_images);
        btn_choosefile_adhar = root.findViewById(R.id.btn_choosefile_adhar);
        if (patientsagespinner != null) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    getContext(), R.layout.name_age_spinner, patientsagespinner);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr_gender.setAdapter(adap);
            spr_gender.setSelection(0);
        }
        ll_enterView = root.findViewById(R.id.ll_enterView);
        img_scanbarcode = root.findViewById(R.id.img_scanbarcode);
        rel_verify_mobile = root.findViewById(R.id.rel_verify_mobile);
        tv_verifiedmob = root.findViewById(R.id.tv_verifiedmob);
        btn_submit = root.findViewById(R.id.btn_submit);
        btn_reset = root.findViewById(R.id.btn_reset);
        edt_firstname = root.findViewById(R.id.edt_firstname);
        edt_lastname = root.findViewById(R.id.edt_lastname);
        edt_age = root.findViewById(R.id.edt_age);
        edt_amtcollected = root.findViewById(R.id.edt_amtcollected);
        txt_barcode = root.findViewById(R.id.txt_barcode);
        rd_home = root.findViewById(R.id.rd_home);
        rd_dps = root.findViewById(R.id.rd_dps);
        radiogrp2 = root.findViewById(R.id.radiogrp2);

        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            edt_email.setHint("EMAIL ID*");
        } else {
            edt_email.setHint("EMAIL ID");
        }

        samplecollectionponit = root.findViewById(R.id.samplecollectionponit);
        samplecollectionponit.setText(Constants.setSCPmsg);

        if (Global.isKYC) {
            by_sendsms.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.GONE);
            by_missed.setVisibility(View.GONE);
        } else {
            by_missed.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.VISIBLE);
            by_sendsms.setVisibility(View.VISIBLE);
        }

        if (!OTPAccess) {
            radiogrp2.setVisibility(View.VISIBLE);
            tv_help.setVisibility(View.VISIBLE);
            btn_generate.setVisibility(View.VISIBLE);
        } else {
            radiogrp2.setVisibility(View.GONE);
            tv_help.setVisibility(View.GONE);
            btn_generate.setVisibility(View.GONE);
            ll_enterView.setVisibility(View.VISIBLE);
        }


        edt_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    agesinteger = Integer.parseInt(s.toString());
                }

                String enteredString = s.toString();

                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(activity,
                            ToastFile.crt_age,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        edt_age.setText(enteredString.substring(1));
                    } else {
                        edt_age.setText("");
                    }
                }

            }

        });
    }

    private void initlistner() {
        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.SetBottomSheet(activity);
            }
        });

        spr_hospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String hospital = spr_hospital.getSelectedItem().toString().trim();
                if (!hospital.equalsIgnoreCase("Select Hospital Name")) {
                    if (hospitalDETAILSBeanList != null) {
                        for (int i = 0; i < hospitalDETAILSBeanList.size(); i++) {
                            if (hospitalDETAILSBeanList.get(i).getName().toUpperCase().equalsIgnoreCase(hospital)) {
                                hospt_ID = hospitalDETAILSBeanList.get(i).getId();
                                hospital = hospitalDETAILSBeanList.get(i).getName();
                            }
                        }

                        Log.e(TAG, "Hospial type ---> " + hospital);
                        Log.e(TAG, "hospt_ID  ---> " + hospt_ID);

                    }
                } else {
                    hospt_ID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genrateflow();
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genrateflow();
            }
        });

        txt_adharfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadharlist != null && aadharlist.size() > 0) {
                    setviewpager(aadharlist, "adhar");
                }
            }
        });
        txt_otherfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherlist != null && otherlist.size() > 0) {
                    setviewpager(otherlist, "other");
                }
            }
        });

        txt_trffileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trflist != null && trflist.size() > 0) {
                    setviewpager(trflist, "trf");
                }
            }
        });

        btn_choosefile_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (trf_file != null && trf_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = false;
                        isother = false;
                        istrf = true;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (other_file != null && other_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = false;
                        istrf = false;
                        isother = true;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        GlobalClass.showAlertDialog(getActivity());
                    } else {
                        try {
                            if (ControllersGlobalInitialiser.covidmultipart_controller != null) {
                                ControllersGlobalInitialiser.covidmultipart_controller = null;
                            }
                            String fname = edt_firstname.getText().toString().trim();
                            String lname = edt_lastname.getText().toString().trim();

                            String fullname = fname + " " + lname;
                            Log.e("NAME LENGHTH---->", "" + fullname.length());

                            Covidpostdata covidpostdata = new Covidpostdata();
                            covidpostdata.setSOURCECODE(usercode);
                            covidpostdata.setMOBILE(edt_missed_mobile.getText().toString());
                            covidpostdata.setNAME(fullname);
                            covidpostdata.setAMOUNTCOLLECTED(edt_amtcollected.getText().toString());
                            covidpostdata.setTESTCODE("CRAT");
                            covidpostdata.setAGE(edt_age.getText().toString());
                            covidpostdata.setBARCODE(txt_barcode.getText().toString());
                            covidpostdata.setEMAIL("" + edt_email.getText().toString());
                            covidpostdata.setTECHNICIAN(selectedSCT.getNED_NUMBER());
                            covidpostdata.setAPIKEY(apikey);

                            if (!GlobalClass.isNull(hospt_ID)) {
                                covidpostdata.setHOSPITAL(hospt_ID);
                            }
                            covidpostdata.setENTERBY(usercode);
                            if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Male")) {
                                covidpostdata.setGENDER("M");
                            } else {
                                covidpostdata.setGENDER("F");
                            }

                            covidpostdata.setAPIKEY(apikey);

                            if (aadhar_file != null && aadhar_file1 != null) {
                                covidpostdata.setADHAR(aadhar_file);
                                covidpostdata.setADHAR1(aadhar_file1);
                            } else if (aadhar_file != null && aadhar_file1 == null) {
                                covidpostdata.setADHAR(aadhar_file);
                            } else {
                                covidpostdata.setADHAR(aadhar_file1);
                            }

                            if (trf_file != null && trf_file1 != null) {
                                covidpostdata.setTRF(trf_file);
                                covidpostdata.setTRF1(trf_file1);
                            } else if (trf_file != null && trf_file1 == null) {
                                covidpostdata.setTRF(trf_file);
                            } else {
                                covidpostdata.setTRF(trf_file1);
                            }

                            if (other_file != null && other_file1 != null) {
                                covidpostdata.setOTHER(other_file);
                                covidpostdata.setOTHER1(other_file1);
                            } else if (other_file != null && other_file1 == null) {
                                covidpostdata.setOTHER(other_file);
                            } else {
                                covidpostdata.setOTHER(other_file1);
                            }
                            new Covidmultipart_controller(RATEnterFrag.this, activity, covidpostdata).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearFields();
            }
        });


        img_scanbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* scanIntegrator = new IntentIntegrator(activity);
                scanIntegrator.initiateScan();*/

                IntentIntegrator.forSupportFragment(RATEnterFrag.this).initiateScan();


            }
        });

        txt_barcode.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    txt_barcode.setInputType(EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_AUTO_CORRECT);
                } else {
                    txt_barcode.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE | EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                }
            }

        });

        txt_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) {
                    txt_barcode.setText("" + Global.randomBarcodeString(8));
                } else {
                    IntentIntegrator.forSupportFragment(RATEnterFrag.this).initiateScan();
                }
            }
        });

        tv_resetno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_by_missed.setVisibility(View.VISIBLE);
                lin_generate_verify.setVisibility(View.GONE);
                edt_missed_mobile.setEnabled(true);
                btn_generate.setVisibility(View.VISIBLE);
                btn_generate.setEnabled(true);
                tv_resetno.setVisibility(View.GONE);
                edt_verifycc.getText().clear();
                tv_mobileno.setVisibility(View.GONE);
                rel_mobno.setVisibility(View.GONE);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                    tv_timer.setVisibility(View.GONE);
                }
            }
        });

        by_missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_generate.setText(getResources().getString(R.string.enterccc));
            }
        });
        by_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_generate.setText(getResources().getString(R.string.btngenerateccc));
                btn_generate.setVisibility(View.VISIBLE);
                btn_resend.setVisibility(View.VISIBLE);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                    if (!GlobalClass.isNull(edt_verifycc.getText().toString())) {
                        if (cd.isConnectingToInternet()) {
                            validateotp();
                        } else {
                            GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN, 0);
                        }
                    } else {
                        GlobalClass.showCustomToast(activity, "Kindly enter otp", 0);
                    }
                } else {
                    GlobalClass.showCustomToast(activity, "Kindly enter mobile number", 0);
                }
            }
        });

        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    if (aadhar_file != null && aadhar_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        selectImage();
                        isadhar = true;
                        istrf = false;
                        isother = false;
                    }

                } else {
                    requestPermission();
                }
            }
        });

        samplecollectionponit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallAPIForSCP();
            }
        });
    }

    private void callRatesApi() {
        if (cd.isConnectingToInternet()) {
            CovidRateReqModel covidRateReqModel = new CovidRateReqModel();
            covidRateReqModel.setUsercode("" + usercode);
            covidRateReqModel.setAPIKEY("" + apikey);
            covidRateReqModel.setTestcode("CRAT");

            CovidRatesController covidRatesController = new CovidRatesController(RATEnterFrag.this, activity);
            covidRatesController.fetchRates(covidRateReqModel);
        }
    }

    public void getRatesResponse(Covidratemodel covidratemodel) {
        b2b = !GlobalClass.isNull(covidratemodel.getB2B()) ? covidratemodel.getB2B() : "0";
        b2c = !GlobalClass.isNull(covidratemodel.getB2C()) ? covidratemodel.getB2C() : "0";
    }

    private void GetHospitalList() {
        Hospital_req hospital_req = new Hospital_req();
        hospital_req.setApiKey(apikey);
        hospital_req.setUserCode(usercode);

        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Call<Hospital_model> covidratemodelCall = postAPIInterface.GetWOEHospital(hospital_req);

        covidratemodelCall.enqueue(new Callback<Hospital_model>() {
            @Override
            public void onResponse(Call<Hospital_model> call, Response<Hospital_model> response) {
                try {
                    if (response.body().getResID().equalsIgnoreCase(Constants.RES0000)) {
                        if (response.body().getHospitalDETAILS() != null && response.body().getHospitalDETAILS().size() > 0) {
                            hospitalname.add("Select Hospital Name");
                            hospitalDETAILSBeanList.addAll(response.body().getHospitalDETAILS());

                            for (int i = 0; i < hospitalDETAILSBeanList.size(); i++) {
                                if (!GlobalClass.isNull(hospitalDETAILSBeanList.get(i).getName())) {
                                    hospitalname.add(hospitalDETAILSBeanList.get(i).getName());
                                }
                            }

                            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                                    activity, R.layout.name_age_spinner, hospitalname);
                            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spr_hospital.setAdapter(adap);
                            spr_hospital.setSelection(0);

                        } else {
                            Toast.makeText(activity, MessageConstants.NODATA, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, MessageConstants.CHECK_INTERNET_CONN, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Hospital_model> call, Throwable t) {
            }
        });

    }

    private void ClearFields() {
        edt_firstname.setText("");
        edt_lastname.setText("");
        edt_email.setText("");
        edt_age.setText("");
        edt_amtcollected.setText("");

        if (!OTPAccess) {
            ll_enterView.setVisibility(View.GONE);
            lin_by_missed.setVisibility(View.VISIBLE);
            lin_generate_verify.setVisibility(View.GONE);
            btn_generate.setVisibility(View.VISIBLE);
            btn_generate.setEnabled(true);
        } else {
            ll_enterView.setVisibility(View.VISIBLE);
        }
        spr_gender.setSelection(0);
        txt_barcode.setText("Barcode*");
        edt_missed_mobile.setEnabled(true);
        rel_verify_mobile.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);
        edt_verifycc.getText().clear();
        tv_mobileno.setVisibility(View.GONE);
        rel_mobno.setVisibility(View.GONE);
        edt_missed_mobile.setText("");
        samplecollectionponit.setText(Constants.setSCPmsg);
        isgetDOB = false;
        if (by_missed.isChecked() || by_sendsms.isChecked()) {
            btn_generate.setText(getResources().getString(R.string.enterccc));
        } else {
            btn_generate.setText(getResources().getString(R.string.btngenerateccc));
        }


        if (Global.isKYC) {
            by_sendsms.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.GONE);
            by_missed.setVisibility(View.GONE);
        } else {
            by_missed.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.VISIBLE);
            by_sendsms.setVisibility(View.VISIBLE);
        }


        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            edt_email.setHint("EMAIL ID*");
        } else {
            edt_email.setHint("EMAIL ID");
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }


    }

    private boolean Validate() {
        if (edt_missed_mobile.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }

        if (edt_firstname.getText().toString().length() == 0) {
            Toast.makeText(activity, "" + ToastFile.ENTER_FNAME, Toast.LENGTH_SHORT).show();
            Global.showCustomToast(activity, ToastFile.ENTER_FNAME);
            edt_firstname.requestFocus();
            return false;
        }

        if (edt_firstname.getText().toString().length() < 2) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            edt_firstname.requestFocus();
            return false;
        }


        if (edt_lastname.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lastname.requestFocus();
            return false;
        }
        if (edt_lastname.getText().toString().length() < 1) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lastname.requestFocus();
            return false;
        }

        if (GlobalClass.isNull(edt_age.getText().toString())) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_AGE);
            edt_age.requestFocus();
            return false;
        }
        if (Integer.parseInt(edt_age.getText().toString()) > 120) {
            Global.showCustomToast(getActivity(), ToastFile.AGE_SHOULD_BE_BETWEEN_1_TO_120);
            edt_age.requestFocus();
            return false;
        }
        if (spr_gender.getSelectedItem().toString().equalsIgnoreCase("Gender")) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_GENDER);
            return false;
        }

        if (txt_barcode.getText().toString().equalsIgnoreCase("Barcode*")) {
            Global.showCustomToast(getActivity(), "Kindly Scan the Barcode");
            return false;
        }

        if (GlobalClass.isNull(edt_amtcollected.getText().toString())) {
            Global.showCustomToast(getActivity(), ToastFile.AMTCOLL);
            edt_amtcollected.requestFocus();
            return false;
        }

        if (appPreferenceManager.getCovidAccessResponseModel().isDRC()) {
            if (GlobalClass.isNull(edt_email.getText().toString())) {
                Global.showCustomToast(getActivity(), "Enter Email-ID");
                edt_email.requestFocus();
                return false;
            }

            if (!GlobalClass.isValidEmail(edt_email.getText().toString())) {
                Global.showCustomToast(getActivity(), "Enter valid Email-ID");
                edt_email.requestFocus();
                return false;
            }
        } else {
            if (!TextUtils.isEmpty(edt_email.getText().toString())) {
                if (!GlobalClass.isValidEmail(edt_email.getText().toString())) {
                    Global.showCustomToast(getActivity(), "Enter valid Email-ID");
                    edt_email.requestFocus();
                    return false;
                }
            }
        }

        try {
            int amt = Integer.parseInt(edt_amtcollected.getText().toString());
            if (amt == 0) {
                Global.showCustomToast(getActivity(), "Please enter proper collected amount");
                return false;
            } else if (amt > Integer.parseInt(b2c)) {
                Global.showCustomToast(getActivity(), "You cannot enter amount collected more than " + b2c);
                return false;
            } else if (amt < Integer.parseInt(b2b)) {
                Global.showCustomToast(getActivity(), "You cannot enter amount collected less than " + b2b);
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();

        }

        if (aadhar_file == null && aadhar_file1 == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_ADHIMAGE);
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_TRFDHIMAGE);
            return false;
        }

        if (samplecollectionponit.getText().toString().trim().equalsIgnoreCase(Constants.setSCPmsg)) {
            Toast.makeText(activity, ToastFile.slt_technicain, Toast.LENGTH_SHORT).show();
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
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage() {

        ImagePicker.Companion.with(RATEnterFrag.this)
                .galleryOnly()
                .crop()
                .compress(Constants.MaxImageSize)
                .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight)
                .start();


      /*  final CharSequence[] items = {"Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);
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
        builder.show();*/
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

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    mobileverify(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 0);
                }
            } else {
                mobileverify(edt_missed_mobile.getText().toString());
            }
        }
    }

    private boolean mobilenovalidation() {
        if (edt_missed_mobile.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        return true;
    }

    private void mobileverify(String mobileno) {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        CoVerifyMobReq coVerifyMobReq = new CoVerifyMobReq();
        coVerifyMobReq.setApi_key(apikey);
        coVerifyMobReq.setMobile(mobileno);
        coVerifyMobReq.setScode(usercode);

        final Call<COVerifyMobileResponse> covidmis_responseCall = postAPIInterface.covmobileVerification(coVerifyMobReq);
        Log.e(TAG, "MOB VERIFY URL--->" + covidmis_responseCall.request().url());
        Log.e(TAG, "MOB VERIFY BODY--->" + new GsonBuilder().create().toJson(coVerifyMobReq));
        covidmis_responseCall.enqueue(new Callback<COVerifyMobileResponse>() {
            @Override
            public void onResponse(Call<COVerifyMobileResponse> call, Response<COVerifyMobileResponse> response) {
                Log.e(TAG, "on Response-->" + response.body().getResponse());
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        if (response.body().getResponse().equalsIgnoreCase("NOT VERIFIED")) {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        }
                        disablefields();
                    } else if (response.body().getResId().equalsIgnoreCase(Constants.RES0082)) {
                        TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                            generateOtP(edt_missed_mobile.getText().toString());
                        } else {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<COVerifyMobileResponse> call, Throwable t) {
                GlobalClass.hideProgress(activity, progressDialog);
            }
        });

    }

    private void generateOtP(String mobileno) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);

        COVIDgetotp_req coviDgetotp_req = new COVIDgetotp_req();
        coviDgetotp_req.setApi_key(apikey);
        coviDgetotp_req.setMobile(mobileno);
        coviDgetotp_req.setScode(usercode);

        Call<Covidotpresponse> covidotpresponseCall = postAPIInterface.generateotp(coviDgetotp_req);
        covidotpresponseCall.enqueue(new Callback<Covidotpresponse>() {
            @Override
            public void onResponse(Call<Covidotpresponse> call, Response<Covidotpresponse> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        setCountDownTimer();
                        lin_by_missed.setVisibility(View.GONE);
                        tv_mobileno.setVisibility(View.VISIBLE);
                        tv_mobileno.setText(edt_missed_mobile.getText().toString());
                        rel_mobno.setVisibility(View.VISIBLE);
                        lin_generate_verify.setVisibility(View.VISIBLE);
                        tv_resetno.setVisibility(View.VISIBLE);
                        tv_resetno.setText(getResources().getString(R.string.reset_mob));
                        tv_resetno.setPaintFlags(tv_resetno.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }
                    Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidotpresponse> call, Throwable t) {
                GlobalClass.hideProgress(activity, progressDialog);
            }
        });
    }

    private void setCountDownTimer() {

        countDownTimer = new CountDownTimer(60000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {

                if (timerflag == false) {
                    btn_resend.setEnabled(false);
                    btn_resend.setClickable(false);
                    btn_resend.setVisibility(View.GONE);
                    tv_timer.setVisibility(View.VISIBLE);
                } else {
                    tv_timer.setVisibility(View.GONE);
                    btn_resend.setVisibility(View.VISIBLE);
                }

                long time = millisUntilFinished / 1000;
                tv_timer.setText("Please wait 00:" + numberFormat.format(time));
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                btn_resend.setEnabled(true);
                btn_resend.setClickable(true);
                btn_resend.setVisibility(View.VISIBLE);

                edt_missed_mobile.setEnabled(true);
                edt_missed_mobile.setClickable(true);

                edt_verifycc.setEnabled(true);
                edt_verifycc.setClickable(true);

            }
        }.start();
    }

    private void validateotp() {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Covid_validateotp_req covid_validateotp_req = new Covid_validateotp_req();
        covid_validateotp_req.setApi_key(apikey);
        covid_validateotp_req.setMobile(edt_missed_mobile.getText().toString());
        covid_validateotp_req.setOtp(edt_verifycc.getText().toString());
        covid_validateotp_req.setScode(usercode);

        Call<Covid_validateotp_res> covidotpresponseCall = postAPIInterface.validateotp(covid_validateotp_req);
        covidotpresponseCall.enqueue(new Callback<Covid_validateotp_res>() {
            @Override
            public void onResponse(Call<Covid_validateotp_res> call, Response<Covid_validateotp_res> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        disablefields();

                        Toast.makeText(activity, "" + response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    } else {
                        GlobalClass.showCustomToast(activity, response.body().getResponse(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covid_validateotp_res> call, Throwable t) {
                GlobalClass.hideProgress(activity, progressDialog);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE && data != null) {
                if (isadhar) {
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
                        CallAadharImageOCR(aadhar_file1);
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
                        CallAadharImageOCR(aadhar_file);
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
                    lin_adhar_images.setVisibility(View.VISIBLE);


                } else if (istrf) {

                    if (lin_trf_images.getVisibility() == View.VISIBLE && trf_file != null) {
                        if (data.getData() != null) {
                            if (trf_file1 == null) {
                                trf_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        trf_file1 = GlobalClass.getCompressedFile(activity, trf_file1);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            trf_file = FileUtil.from(activity, data.getData());
                        }
                        trf_file = GlobalClass.getCompressedFile(activity, trf_file);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file.toString());
                    }


                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));

                    }

                    lin_trf_images.setVisibility(View.VISIBLE);
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

                    }

                    lin_other_images.setVisibility(View.VISIBLE);
                }
            } else if (result != null) {
                Log.e(TAG, "onActivityResult: " + result);
                if (result.getContents() != null) {
                    String getBarcodeDetails = result.getContents();
                    if (getBarcodeDetails.length() == 8) {
                        passBarcodeData(getBarcodeDetails);
                    } else {
                        Toast.makeText(getContext(), invalid_brcd, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void passBarcodeData(final String getBarcodeDetails) {
        requestQueue = GlobalClass.setVolleyReq(getContext());
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Kindly wait...");
        progressDialog.setMessage(ToastFile.processing_request);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(20);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(context);

        System.out.println("barcode url  --> " + Api.checkBarcode + apikey + "/" + getBarcodeDetails + "/getcheckbarcode");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + apikey + "/" + getBarcodeDetails + "/getcheckbarcode"
                , new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("barcode respponse" + response);
                GlobalClass.hideProgress(getContext(), progressDialog);
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
                        txt_barcode.setText(getBarcodeDetails.toUpperCase());

                    } else {
                        Toast.makeText(getContext(), "" + response1, Toast.LENGTH_SHORT).show();
                        txt_barcode.setText("Barcode*");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.hideProgress(getContext(), progressDialog);
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
        requestQueue.add(jsonObjectRequestPop);
        Log.e(TAG, "onBindViewHolder: url" + jsonObjectRequestPop);
    }

    private void disablefields() {

        rel_verify_mobile.setVisibility(View.VISIBLE);
        tv_verifiedmob.setText(edt_missed_mobile.getText().toString());
        ll_enterView.setVisibility(View.VISIBLE);
        lin_by_missed.setVisibility(View.GONE);

        tv_mobileno.setVisibility(View.GONE);

        edt_missed_mobile.setEnabled(false);
        // btn_generate.setEnabled(false);
        btn_resend.setEnabled(false);
        btn_resend.setVisibility(View.GONE);
        btn_generate.setVisibility(View.GONE);
        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);

        timerflag = true;


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }

    public void POSTWOE(WOERequestModel requestModel) {
        try {


            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getContext());
            progressDialog.show();

            PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(getActivity(), Api.BASE_URL_TOCHECK).create(PostAPIInterface.class);
            Call<WOEResponseModel> responseModelCall = apiInterface.PostUserLog(requestModel);
            responseModelCall.enqueue(new Callback<WOEResponseModel>() {
                @Override
                public void onResponse(Call<WOEResponseModel> call, retrofit2.Response<WOEResponseModel> response) {
                    try {
                        GlobalClass.hideProgress(getContext(), progressDialog);
                        if (response.body() != null) {
                            WOEResponseModel responseModel = response.body();
                            Toast.makeText(activity, "" + responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            ClearFields();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<WOEResponseModel> call, Throwable t) {
                    GlobalClass.hideProgress(getContext(), progressDialog);
                    Log.e(TAG, "onFailure: " + "Something went wrong");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setviewpager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        //maindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
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
        btn_delete.setVisibility(View.VISIBLE);
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

                        if (type.equalsIgnoreCase("adhar")) {

                            if (aadharlist != null && aadharlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (aadhar_file != null) {
                                        aadhar_file = null;
                                    } else {
                                        aadhar_file1 = null;
                                    }

                                    aadharlist.remove(aadharlist.get(0));
                                    // viewPagerAdapter.notifyDataSetChanged();
                                } else if (1 == viewPager.getCurrentItem()) {
                                    aadhar_file1 = null;
                                    if (aadharlist.size() == 2) {
                                        aadharlist.remove(aadharlist.get(1));
                                    } else {
                                        aadharlist.remove(aadharlist.get(0));
                                    }
                                    // viewPagerAdapter.notifyDataSetChanged();
                                }

                                if (aadharlist.size() == 1) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
                                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (aadharlist.size() == 2) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
                                    txt_nofileadhar.setVisibility(View.VISIBLE);
                                    txt_adharfileupload.setVisibility(View.GONE);

                                }


                            } else {
                                txt_nofileadhar.setVisibility(View.VISIBLE);
                                lin_adhar_images.setVisibility(View.GONE);
                            }
                        } else if (type.equalsIgnoreCase("trf")) {
                            if (trflist != null && trflist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (trf_file != null) {
                                        trf_file = null;
                                    } else {
                                        trf_file1 = null;
                                    }
                                    trflist.remove(trflist.get(0));
                                    // viewPagerAdapter.notifyDataSetChanged();
                                } else if (1 == viewPager.getCurrentItem()) {
                                    trf_file1 = null;
                                    if (trflist.size() == 2) {
                                        trflist.remove(trflist.get(1));
                                    } else {
                                        trflist.remove(trflist.get(0));
                                    }
                                    //viewPagerAdapter.notifyDataSetChanged();
                                }

                                if (trflist.size() == 1) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (trflist.size() == 2) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
                                    txt_nofiletrf.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.GONE);
                                }

                            } else {
                                txt_nofiletrf.setVisibility(View.VISIBLE);
                                lin_trf_images.setVisibility(View.GONE);
                            }

                        } else if (type.equalsIgnoreCase("other")) {
                            if (otherlist != null && otherlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (other_file != null) {
                                        other_file = null;
                                    } else {
                                        other_file1 = null;
                                    }

                                    otherlist.remove(otherlist.get(0));
                                    //   viewPagerAdapter.notifyDataSetChanged();
                                } else if (1 == viewPager.getCurrentItem()) {
                                    other_file1 = null;
                                    if (otherlist.size() == 2) {
                                        otherlist.remove(otherlist.get(1));
                                    } else {
                                        otherlist.remove(otherlist.get(0));
                                    }
                                    //  viewPagerAdapter.notifyDataSetChanged();
                                }

                                if (otherlist.size() == 1) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (otherlist.size() == 2) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    txt_nofileother.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.GONE);
                                }

                            } else {
                                txt_nofileother.setVisibility(View.VISIBLE);
                                lin_other_images.setVisibility(View.GONE);
                            }

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

    public void getUploadResponse(String response, String mobileno) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
                new LogUserActivityTagging(activity, "WOE-NOVID", mobileno);
                ClearFields();
                Constants.universal = 1;
                Intent i = new Intent(activity, HomeMenuActivity.class);
                GlobalClass.redirectToPreviousPosition(activity,Constants.RAT_MENU_POS);
                startActivity(i);
                Constants.ratfrag_flag = "1";

                getActivity().finish();

            } else {
                Global.showCustomToast(activity, RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialogue(final ArrayList<getSCPTechnicianModel.getTechnicianlist> obj) {

        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.custom_alert_scp, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        LinearLayoutManager linearLayoutManager = null;

        EditText search_view = (EditText) promptsView.findViewById(R.id.search_view);
        TextView title = (TextView) promptsView.findViewById(R.id.spinerTitle);
        ImageView close = (ImageView) promptsView.findViewById(R.id.close);
        final RecyclerView scp_name = (RecyclerView) promptsView.findViewById(R.id.scp_name);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scp_name.setLayoutManager(linearLayoutManager);


        title.setText(Constants.setSCPmsg);

        final ArrayList<getSCPTechnicianModel.getTechnicianlist> labDetailsArrayList = new ArrayList<>();

        for (int i = 0; i < obj.size(); i++) {
            labDetailsArrayList.add(obj.get(i));
        }

        final SCollectionPAdapter sampleCollectionAdapter = new SCollectionPAdapter(RATEnterFrag.this, activity, labDetailsArrayList);
        sampleCollectionAdapter.setOnItemClickListener(new SCollectionPAdapter.OnItemClickListener() {
            @Override
            public void onPassSgcID(getSCPTechnicianModel.getTechnicianlist pos) {
                samplecollectionponit.setText(pos.getNAME() + " - " + pos.getNED_NUMBER());
                selectedSCT = pos;
                alertDialog.dismiss();
            }
        });
        scp_name.setAdapter(sampleCollectionAdapter);

        search_view.addTextChangedListener(new TextWatcher() {
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

                filterPatientsArrayList = new ArrayList<>();
                String labname = "";
                String clientId = "";
                if (labDetailsArrayList != null) {
                    for (int i = 0; i < labDetailsArrayList.size(); i++) {

                        final String text1 = labDetailsArrayList.get(i).getNAME().toLowerCase();
                        final String text2 = labDetailsArrayList.get(i).getNED_NUMBER().toLowerCase();

                        if (labDetailsArrayList.get(i).getNED_NUMBER() != null || !labDetailsArrayList.get(i).getNED_NUMBER().equals("")) {
                            clientId = labDetailsArrayList.get(i).getNED_NUMBER().toLowerCase();
                        }
                        if (labDetailsArrayList.get(i).getNAME() != null || !labDetailsArrayList.get(i).getNAME().equals("")) {
                            labname = labDetailsArrayList.get(i).getNAME().toLowerCase();
                        }

                        if (text1.contains(s1) || (clientId != null && clientId.contains(s1)) ||
                                (labname != null && labname.contains(s1))) {
                            String testname = (labDetailsArrayList.get(i).getNAME());
                            filterPatientsArrayList.add(labDetailsArrayList.get(i));

                        } else {

                        }
                        sampleCollectionAdapter.filteredArraylist(filterPatientsArrayList);
                        sampleCollectionAdapter.notifyDataSetChanged();
                    }
                }
                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        if (GlobalClass.setFlagToClose) {
            alertDialog.dismiss();
        }
    }

    private void CallAPIForSCP() {
        SharedPreferences sdfsd_pref = activity.getSharedPreferences("SCPDATA", MODE_PRIVATE);
        if (GlobalClass.isNull(sdfsd_pref.getString("scplist", ""))) {
            CallAPItogetSCPData();
        } else {
            if (GlobalClass.checkSync(activity, "SCTTech")) {
                CallAPItogetSCPData();
            } else {
                setFinalSCPList(sdfsd_pref.getString("scplist", ""));
            }
        }


    }

    private void CallAPItogetSCPData() {
        if (GlobalClass.isNetworkAvailable(activity)) {
            try {
                ControllersGlobalInitialiser.getSCPSRFAPIController = new getSCPSRFAPIController(RATEnterFrag.this, activity);
                ControllersGlobalInitialiser.getSCPSRFAPIController.getSCPtechnician(true, usercode, apikey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFinalSCPList(String response) {
        if (response != null) {
            if (!response.toString().trim().equalsIgnoreCase("")) {
                Gson gson = new Gson();
                getSCPTechnicianModel getSCP_model = gson.fromJson(response, getSCPTechnicianModel.class);
                if (getSCP_model != null) {
                    if (getSCP_model.getResId() != null) {
                        if (getSCP_model.getResId().toString().trim().equalsIgnoreCase(Constants.RES0000)) {
                            GlobalClass.storeCachingDate(activity, "SCTTech");
                            showDialogue(getSCP_model.getTechnicianlist());
                        }
                    }
                }
            }
        }
    }

    public void setSCPresData(String scpdata) {
        SharedPreferences.Editor saveProfileDetails = activity.getSharedPreferences("SCPDATA", 0).edit();
        saveProfileDetails.putString("scplist", scpdata);
        saveProfileDetails.apply();
    }

    private void CallAadharImageOCR(File aadhar_imgfile) {
        try {
            String filePath = aadhar_imgfile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            CalltextRecFromImage(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CalltextRecFromImage(Bitmap bitmap) {
        try {
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            TextRecognizer recognizer = TextRecognition.getClient();

            // [START run_detector]
            Task<Text> result = recognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text visionText) {
                            for (Text.TextBlock block : visionText.getTextBlocks()) {
                                Rect boundingBox = block.getBoundingBox();
                                Point[] cornerPoints = block.getCornerPoints();
                                String text = block.getText();

                                for (Text.Line line : block.getLines()) {
                                    // ...
                                    for (Text.Element element : line.getElements()) {
                                        // ...
                                    }
                                }
                            }
                            processTextBlock(visionText);
                        }
                    }).addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                }
                            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTextBlock(Text result) {
        try {
            String resultText = result.getText();
            if (result.getTextBlocks().size() != 0) {

                for (int j = 0; j < result.getTextBlocks().size(); j++) {
                    Text.TextBlock block = result.getTextBlocks().get(j);
                    String blockText = block.getText();
                    setNameFromList(blockText, j, result.getTextBlocks());
                    for (int i = 0; i < block.getLines().size(); i++) {
                        Text.Line line = block.getLines().get(i);
                        String lineText = line.getText();
                        Point[] lineCornerPoints = line.getCornerPoints();
                        Rect lineFrame = line.getBoundingBox();

                        setName(lineText, i, block.getLines());
                        setGender(lineText);
                        setDOB(lineText);

                        setPassPortCode(lineText);
                        setPassportGender(lineText);
                    }
                }

                getLastNameFromFinalLastString(result.getTextBlocks());
                getFirstNameFromFinalLastString(result.getTextBlocks());
                getPassPortDOBDate(result.getTextBlocks(), getPassPortFromList());
                // [END mlkit_process_text_block]
            } else {
//                Toast.makeText(activity, "Please select proper image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNameFromList(String lineText, int i, List<Text.TextBlock> lines) {
        try {
            if (lines != null) {
                if (lineText.toString().trim().contains("D0B") || lineText.toString().trim().contains("DOB") || lineText.toString().trim().contains("Year Of Birth") || lineText.toString().trim().contains("Year of Birth") || lineText.toString().trim().contains("Birth")) {
                    if (isValidNameOnly(lines.get(i - 1).getText())) {
                        SetName(lines.get(i - 1).getText());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setName(String lineText, int i, List<Text.Line> lines) {
        try {
            if (lines != null) {
                if (lines.size() > i + 1) {
                    Text.Line line = lines.get(i + 1);
                    if (line.getText().toString().trim().contains("D0B") || line.getText().toString().trim().contains("DOB") || line.getText().toString().trim().contains("Year Of Birth") || line.getText().toString().trim().contains("Year of Birth") || line.getText().toString().trim().contains("Birth")) {
                        //                    txt_aadharname.setText("Name: " + lineText);
                        SetName(lineText);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetName(String lineText) {
        try {
            if (lineText != null) {
                if (!lineText.toString().trim().equalsIgnoreCase("")) {
                    String[] splited = lineText.split("\\s+");
                    if (splited.length != 0) {
                        if (splited.length == 1) {
                            if (edt_firstname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[0].toString().trim().contains("GOVERN") || splited[0].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_firstname.setText(splited[0].toString().trim());
                                }

                            }
                            if (edt_lastname.getText().toString().trim().equalsIgnoreCase("")) {
                                edt_lastname.setText("");
                            }
                        } else if (splited.length == 2) {
                            if (edt_firstname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[0].toString().trim().contains("GOVERN") || splited[0].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_firstname.setText(splited[0].toString().trim());
                                }
                            }
                            if (edt_lastname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[1].toString().trim().contains("GOVERN") || splited[1].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_lastname.setText(splited[1].toString().trim());
                                }
                            }
                        } else if (splited.length == 3) {
                            if (edt_firstname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[0].toString().trim().contains("GOVERN") || splited[0].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_firstname.setText(splited[0].toString().trim());
                                }
                            }

                            if (edt_lastname.getText().toString().trim().equalsIgnoreCase("")) {
                                if (splited[2].toString().trim().contains("GOVERN") || splited[2].toString().trim().contains("INDIA")) {

                                } else {
                                    edt_lastname.setText(splited[2].toString().trim());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String regxName = "([A-Z][a-z]*\\s*)+";

    public static boolean isValidNameOnly(String str) {
        String regex = regxName;
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    private void setGender(String lineText) {
        try {
            if (lineText.contains("MALE") || lineText.contains("Male")) {
                spr_gender.setSelection(1);
            } else if (lineText.contains("FEMALE") || lineText.contains("Female")) {
                spr_gender.setSelection(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDOB(String lineText) {
        if (lineText.contains("DOB") || lineText.contains("D0B")) {
            if (getBirthdate(lineText).toString().trim().length() != 0) {
                setDate(getBirthdate(lineText).toString().trim());
            }
        } else if (lineText.contains("Year Of Birth") || lineText.contains("Year of Birth")) {
            if (getBirthYear(lineText).toString().trim().length() != 0) {
                setDateFromYear(getBirthYear(lineText).toString().trim());
            }
        }
    }

    private void setDate(String datetext) {
        try {
            if (isValidDateFormat(datetext)) {
                if (!isgetDOB) {
                    isgetDOB = true;
                    CalCulateAgeFromDate(datetext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDateFromYear(String datetext) {
        try {
            if (!isgetDOB) {
                isgetDOB = true;
                CalCulateAgeFromYear(datetext);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CalCulateAgeFromDate(String datetext) {
        try {
            if (datetext != null) {
                if (datetext.toString().trim().equalsIgnoreCase("")) {

                } else {
                    try {
                        String age = DateUtils.getAge(Integer.parseInt(DateUtils.Req_Date_Req(datetext, mAadhardateFormat, "yyyy")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, mAadhardateFormat, "MM")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, mAadhardateFormat, "dd")));
                        if (age != null) {
                            if (age.toString().trim().equalsIgnoreCase("")) {

                            } else {
                                if (edt_age.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_age.setText(age);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void CalCulateAgeFromYear(String datetext) {
        try {
            if (datetext != null) {
                if (datetext.toString().trim().equalsIgnoreCase("")) {

                } else {
                    try {
                        String age = DateUtils.getAge(Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyyy", "yyyy")), 1, 1);
                        if (age != null) {
                            if (age.toString().trim().equalsIgnoreCase("")) {

                            } else {
                                if (edt_age.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_age.setText(age);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String mAadhardateFormat = "dd/MM/yyyy";

    private boolean isValidDateFormat(String datetext) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat(mAadhardateFormat);
        try {
            Date javaDate = sdfrmt.parse(datetext);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static String getBirthdate(String str) {
        String st = "";
//        Pattern p = Pattern.compile("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$");
        Pattern p = Pattern.compile("(^1[01]|[12][0-9]|0[1-9])/(3[0-2]|0[1-9])/[0-9]{4}$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            st = "" + m.group(0);
        }
        return st;
    }

    public static String getBirthYear(String str) {
        Pattern p = Pattern.compile("[0-9]{4}$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return "" + m.group(0);
        }
        return "";
    }

    private void getFirstNameFromFinalLastString(List<Text.TextBlock> allText) {
        try {
            String st = "";
            if (allText != null) {
                if (allText.size() != 0) {
                    for (int i = 0; i < allText.size(); i++) {
                        String stsd = allText.get(i).getText().toString().trim();
                        if (stsd.startsWith("P<") || stsd.startsWith("p<")) {
                            st = getFinalFirstname(stsd);
                        }
                    }
                }
            }

            if (edt_firstname.getText().toString().trim().equalsIgnoreCase("")) {
                edt_firstname.setText(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFinalFirstname(String fstr) {
        String st = "";
        try {
            String[] splited = fstr.split("<<");
            if (splited.length != 0 && splited.length > 1) {
                String stksh = splited[1].toString().trim();
                stksh = stksh.replaceAll("\\s+", "");
                stksh = stksh.replaceAll("<", "");
                System.out.println("Nitya --- " + stksh);
                st = stksh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;
    }

    private void getLastNameFromFinalLastString(List<Text.TextBlock> allText) {
        try {
            String st = "";
            if (allText != null) {
                if (allText.size() != 0) {
                    for (int i = 0; i < allText.size(); i++) {
                        String stsd = allText.get(i).getText().toString().trim();
                        if (stsd.startsWith("P<") || stsd.startsWith("p<")) {
                            System.out.println("Nitya =Name= " + stsd);
                            st = getFinalSurname(stsd);
                        }
                    }
                }
            }

            if (edt_lastname.getText().toString().trim().equalsIgnoreCase("")) {
                edt_lastname.setText(st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFinalSurname(String fstr) {
        String st = "";
        try {
            String[] splited = fstr.split("<");
            if (splited.length != 0 && splited.length > 1) {
                String stksh = splited[1].toString().trim();
                stksh = stksh.substring(3, stksh.length());
                st = stksh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    ArrayList<String> l_PassPortcard = new ArrayList<>();

    private void setPassPortCode(String lineText) {
        try {
            if (isValidPassPortNumber(lineText)) {
                if (!l_PassPortcard.contains(getPassPortCode(lineText)))
                    l_PassPortcard.add(getPassPortCode(lineText));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidPassPortNumber(String str) {
//        String regex = "^[A-Z]{1}[0-9]{7}$";
        String regex = "^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String getPassPortCode(String str) {
        String regex = "^[A-PR-WYa-pr-wy][1-9]\\d\\s?\\d{4}[1-9]$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return "" + m.group(0);
        }
        return "";
    }

    private String getPassPortFromList() {
        String st = "";
        try {
            if (l_PassPortcard != null) {
                for (int i = 0; i < l_PassPortcard.size(); i++) {
                    st = st + l_PassPortcard.get(0).toString().trim();
                    return st;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    private void getPassPortDOBDate(List<Text.TextBlock> allText, String passPorttext) {
        try {
            if (allText != null) {
                if (allText.size() != 0) {
                    for (int i = 0; i < allText.size(); i++) {
                        String stsd = allText.get(i).getText().toString().trim();
                        if (stsd.contains(passPorttext.toString().trim())) {
                            if (stsd.length() > 9) {
                                System.out.println("Nitya == " + stsd);
                                getDateDateFromString(stsd.replaceAll("\\s+", ""));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDateDateFromString(String dkgdgd) {
        try {
            int speciallchar = 0;
            if (dkgdgd != null) {
                for (int i = 0; i < dkgdgd.length(); i++) {
                    if (dkgdgd.charAt(i) == '<') {
                        System.out.println(i);
                        speciallchar = i;
                        break;
                    }
                }
                if (speciallchar != 0) {
                    String sdgsdg = dkgdgd.substring(speciallchar + 5, speciallchar + 5 + 6);
                    System.out.println(">>" + sdgsdg);
                    setDOBFinalDate(sdgsdg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDOBFinalDate(String str) {
        try {
            if (str != null) {
                CalCulateAgeFromPassDate(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPassportGender(String lineText) {
        try {
            if (lineText.toString().trim().equals("F") || lineText.toString().trim().equals("f")) {
                spr_gender.setSelection(2);
            } else if (lineText.toString().trim().equals("M") || lineText.toString().trim().equals("m")) {
                spr_gender.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CalCulateAgeFromPassDate(String datetext) {
        try {
            if (datetext != null) {
                if (datetext.toString().trim().equalsIgnoreCase("")) {

                } else {
                    try {
                        String age = DateUtils.getAge(Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyMMdd", "yyyy")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyMMdd", "MM")), Integer.parseInt(DateUtils.Req_Date_Req(datetext, "yyMMdd", "dd")));
                        if (age != null) {
                            if (age.toString().trim().equalsIgnoreCase("")) {

                            } else {
                                if (edt_age.getText().toString().trim().equalsIgnoreCase("")) {
                                    edt_age.setText(age);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}