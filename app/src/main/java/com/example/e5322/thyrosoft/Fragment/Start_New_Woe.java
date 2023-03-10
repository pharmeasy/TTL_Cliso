package com.example.e5322.thyrosoft.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Activity.MultipleLeadActivity;
import com.example.e5322.thyrosoft.Activity.frags.RootFragment;
import com.example.e5322.thyrosoft.Adapter.AdapterRe;
import com.example.e5322.thyrosoft.Adapter.CustomListAdapter;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.CleverTapHelper;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetLeadgerDetailsController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ValidateMob_Controller;
import com.example.e5322.thyrosoft.Controller.VerifyotpController;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BRAND_LIST;
import com.example.e5322.thyrosoft.Models.Brand_type;
import com.example.e5322.thyrosoft.Models.CAMP_LIST;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.GetLeadgerBalnce;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.OTPrequest;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.BundleConstants;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.Models.RequestModels.GetPatientDetailsRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBarcodeDetailsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBaselineDetailsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PatientDetailsAPiResponseModel;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInterface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ProductLisitngActivityNew;
import com.example.e5322.thyrosoft.SourceILSModel.LABS;
import com.example.e5322.thyrosoft.SourceILSModel.REF_DR;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.OutLabTestsActivity;
import com.example.e5322.thyrosoft.WOE.RecheckAllTest;
import com.example.e5322.thyrosoft.WOE.ScanBarcodeLeadId;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.AddWOETestsForSerum;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.WOE_Model_Patient_Details;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class Start_New_Woe extends RootFragment implements View.OnClickListener {
    public static RequestQueue PostQueOtp;
    public static ArrayList<BCT_LIST> getBtechList;
    public static CAMP_LIST[] camp_lists;
    AlertDialog alertDialog;
    boolean sizeflag = false;
    View view, viewMain;
    Date dCompare;
    String TAG = getClass().getSimpleName();
    AlertDialog alert;
    Drawable imgotp;
    Context mContext;
    boolean mobno_verify = false;
    EditText et_mobno;
    CheckBox chk_otp;
    LinearLayout lin_ckotp;
    public static String OTPAPPID = "9";
    CountDownTimer yourCountDownTimer = null;
    EditText age, id_for_woe, barcode_woe, pincode_edt;
    AutoCompleteTextView name;
    ImageView img_restPatientData;
    REF_DR[] ref_drs;
    Brand_type[] brandType;
    Button btn_generate, btn_resend, btn_verify;
    EditText edt_missed_mobile, edt_verifycc;
    RadioButton by_missed, by_generate, by_sendsms;
    RelativeLayout rel_verify_mobile;
    RelativeLayout rel_mobno;
    LinearLayout lin_generate_verify, lin_by_missed, lin_missed_verify;
    TextView tv_timer, tv_resetno, tv_mobileno;
    CountDownTimer countDownTimer;
    RadioGroup radiogrp2;
    ImageView male, female, male_red, female_red;
    ProgressDialog barProgressDialog;
    Button next_btn, btn_snd_otp, btn_verifyotp;
    ArrayList<String> getWindupCount;
    int agesinteger;
    ArrayList<LABS> filterPatientsArrayList, labDetailsArrayList;
    SourceILSMainModel sourceILSMainModel;
    LABS[] labs;
    ArrayList<String> getReferenceNmae;
    ArrayList<REF_DR> getReferenceName1;
    //    TextView samplecollectionponit;
    EditText samplecollectionponit;
    RecyclerView scp_name;
    AutoCompleteTextView referedbyText;
    SourceILSMainModel obj;
    Spinner spinyr;
    EditText kyc_format;
    String namestr;
    int cur = 0;
    Calendar myCalendar;
    long minDate;
    int mYear, mMonth, mDay;
    Spinner timehr, timesecond, timeampm;
    TextView dateShow;
    TextView leadbarcodename;
    TextView leadidbarcodetest;
    TextView leadbarcoderefdr;
    TextView leadbarcodesct;
    TextView setTime;
    String saveGenderId;
    boolean genderId = false;
    DatabaseHelper myDb;
    String enteredString, preOTP;
    BarcodelistModel barcodelist;
    LinearLayout namePatients, btech_layout, ref_check_linear, id_layout, barcode_layout, leadlayout, leadbarcodelayout, AGE_layout, time_layout;
    String RES_ID, barcode, ALERT, BARCODE, BVT_HRS, LABCODE, PATIENT, REF_DR, REQUESTED_ADDITIONAL_TESTS, REQUESTED_ON, SDATE,
            SL_NO, STATUS, SU_CODE1, SU_CODE2, TESTS, REF_DR_data;
    LocationManager locationManager;
    LABS getFullDataLabs;
    Spinner selectTypeSpinner, brand_spinner;
    ArrayList<String> getCampNames;
    Button next_btn_stage2;
    TextView tv_unbilled, tv_avl_bal;
    EditText pincode, patientAddress, reenterkycinfo, kycinfo, vial_number, et_otp;
    Spinner btechname, deliveymode, camp_spinner_olc;
    ArrayList<BarcodelistModel> barcodelists;
    TextView radio, tv_timer_new;
    LinearLayout refby_linear, ll_miscallotp, camp_layout_woe, btech_linear_layout, labname_linear, home_layout, mobile_number_kyc, pincode_linear_data, ll_mobileno_otp, lin_otp;
    WOE_Model_Patient_Details woe_model_patient_details;
    String woereferedby;
    boolean isLoaded = false;
    String blockCharacterSetdata = "~#^|$%&*!+:`";
    ArrayList<BRAND_LIST> getBrandName;
    ArrayList<String> spinnerBrandName;
    ArrayList<String> spinnerTypeName;
    ArrayList<String> getTypeListfirst;
    ArrayList<String> getTypeListSMT;
    ArrayList<String> getTypeListsecond;
    ArrayList<String> getTypeListthird;
    ArrayList<String> getTypeListfourth;
    ArrayList<String> getDatafetch;
    ArrayList<String> getSubSource;
    LeadOrderIdMainModel leadOrderIdMainModel;
    String srnostr, sub_source_code_string, getLeadId, leadAddress, brandtype, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE,
            leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData,
            emailPattern, getNumber;
    Toolbar toolbar;
    int a = 0;
    ImageView add_ref;
    ScrollView scrollView2;
    Button btn_clear_data;
    String getDatefromWOE, halfTime, DateToPass;
    TextView enetered, enter, tv_mob_note, tv_verifiedmob;
    Date getCurrentDateandTime;
    ImageView enter_arrow_enter, enter_arrow_entered, uncheck_ref, ref_check;
    int flagtoAdjustClisk = 0;
    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayListPatient;
    String convertedDate;
    PatientDtailsWoe patientDtailsWoe;
    private boolean timerflag = false;
    private boolean isViewShown = false;
    private String mParam1;
    private String mParam2;
    private String passToAPI;
    private ArrayList<String> getLabNmae;
    private ArrayList<String> statusForColor;
    private int minute;
    private int hour;
    private SharedPreferences prefs, getRandomId;
    private String user;
    private String passwrd;
    private String access, usercode;
    private String referenceBy;
    private String checkifChecked;
    private String api_key;
    private String source_type;
    private ArrayList<String> btechSpinner;
    private String blockCharacterSet = "#$^*+-/|><";
    private MyPojo myPojo;
    private RequestQueue POstQue;
    private String labAddress;
    private String labid;
    private String referredID, getBtechID;
    private String barcode_patient_id, message, status;
    private String scpdefaultdata;
    private String campname;
    private String campID;
    private String outputDateStr;
    private OnFragmentInteractionListener mListener;
    private LocationManager mLocationManager;
    private ArrayList<PatientDetailsAPiResponseModel.PatientInfoBean> patientDetailAryList;

    private String putDate;
    private String getFormatDate;

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private int conertage;
    private String labAddressTopass;
    private String labIDTopass;
    private String scpoint;
    private String getTSP_Address;
    private String typename;
    private String nameString;
    private String ageString;
    private String btechnameTopass;
    private String btechIDToPass;
    private String getcampIDtoPass;
    private String kycdata;
    private String getTSP_AddressStringTopass;
    private String patientAddressdataToPass;
    private boolean flag = true;
    private EditText home_kyc_format;
    private LinearLayout Home_mobile_number_kyc;
    private String brand_string;
    private String type_string;
    private String id_woe;
    private String barcode_woe_str;
    private RequestQueue requestQueueNoticeBoard;
    private TextView leadname, leadidtest, leadrefdr;
    private RequestQueue requestQueueAddRecheck;
    private String brandNames;
    private String getLabName;
    private String prof;
    private String mobile;
    private String nameofProfile;
    private SpinnerDialog spinnerDialog, spinnerDialogRef;
    private String getVial_numbver;
    private boolean flagFromtext = false;
    private String sctHr = "", sctMin = "", sctSEc = "", getFinalTime, getFinalDate;
    private String getAmPm;
    private LABS selectedLABS;
    private String getLabCode;
    private Cursor cursor;
    private String labLabNAmeTopass;
    private String countData;
    private RequestQueue requestQueue;
    private String pincode_pass;
    private int numberOfLines = 10;
    private Dialog CustomLeaddialog;
    ConnectionDetector cd;
    AppPreferenceManager appPreferenceManager;

    SharedPreferences getshared;
    private boolean covidacc;

    TextView tv_help;
    EditText edt_email;

    LinearLayout ll_email, LL_main2, ll_communication_main;
    List<String> hourSin, minuteSpin;
    private String CLIENT_TYPE;
    CheckBox cb_kyc_verification, cb_report, cb_receipt, cb_comm, cb_sms, cb_email, cb_wa;
    ArrayList<String> reportArray = new ArrayList<>();
    ArrayList<String> typeArray = new ArrayList<>();
    String mode;
    TextView tv_baseline;
    String Baseline, CMT, Shortage;
    GetBaselineDetailsResponseModel getBaselineDetailsResponseModel;
    Activity activity;
    CleverTapHelper cleverTapHelper;

    public Start_New_Woe() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Start_New_Woe newInstance(String param1, String param2) {
        Start_New_Woe fragment = new Start_New_Woe();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewMain = (View) inflater.inflate(R.layout.fragment_start__new__woe, container, false);
        init();
        SetCurrenttime();
        listner();
        callLedgerDetailsAPI();
        //TODO hide baseline as per input 25/04/2022
       /* showBaselineDetails();
        if (Global.getLoginType(activity) != Constants.PEflag) {
            showRewardsDialog();
        }*/

        return viewMain;
    }

    public void showRewardsDialog() {
        try {
            if (Global.ToShowRewards) {
                if (appPreferenceManager.getBaselineDetailsResponse().getBaseDetails() != null) {
                    String URL = appPreferenceManager.getBaselineDetailsResponse().getBaseDetails().get(0).getURL();
                    if (!GlobalClass.isNull(URL)) {
                        String date = appPreferenceManager.getRewardPopUpDate();
                        if (!date.equalsIgnoreCase(GlobalClass.getCurrentDate())) {
                            appPreferenceManager.setRewardPopUpDate(GlobalClass.getCurrentDate());
                            String pdfurl = URL.replaceAll("\\\\", "//");
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfurl));
                            startActivity(browserIntent);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBaselineDetails() {
        try {
            getBaselineDetailsResponseModel = new GetBaselineDetailsResponseModel();
            getBaselineDetailsResponseModel = appPreferenceManager.getBaselineDetailsResponse();

            if (getBaselineDetailsResponseModel != null && getBaselineDetailsResponseModel.getBaseDetails() != null) {
                Baseline = "" + getBaselineDetailsResponseModel.getBaseDetails().get(0).getBASELINE();
                CMT = "" + getBaselineDetailsResponseModel.getBaseDetails().get(0).getCURRENTMONTH();
                Shortage = "" + getBaselineDetailsResponseModel.getBaseDetails().get(0).getINCREMENTAL();
            }

            if (GlobalClass.isNull(Baseline)) {
                tv_baseline.setVisibility(View.GONE);
            } else {
                tv_baseline.setVisibility(View.VISIBLE);
                tv_baseline.setSelected(true);
                tv_baseline.setText("  BASELINE : " + GlobalClass.currencyFormat(Baseline) + "        " + "CMT : " + GlobalClass.currencyFormat(CMT) + "        " + "SHORTAGE : " + GlobalClass.currencyFormat(Shortage) + " ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetCurrenttime() {
        try {
            Calendar rightNow = Calendar.getInstance();
            int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentminIn24Format = rightNow.get(Calendar.MINUTE);
            String value = GlobalClass.getvalue(currentminIn24Format);
            timehr.setSelection(currentHourIn24Format + 1);
            for (int i = 0; i < minuteSpin.size(); i++) {
                if (value.equalsIgnoreCase(minuteSpin.get(i))) {
                    timesecond.setSelection(i);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void listner() {

        cb_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode = BundleConstants.REPORT;
                if (isChecked) {
                    reportArray.add(mode);
                    LL_main2.setVisibility(View.VISIBLE);
                   /* if (reportArray.contains("Communication")) {
                        reportArray.clear();
                    } else {
                        reportArray.add(mode);
                    }
                    if (cb_comm.isChecked()) {
                        cb_comm.setChecked(false);
                    }
                    LL_main2.setVisibility(View.VISIBLE);*/
                } else {
                    reportArray.remove(mode);
                    if (cb_receipt.isChecked() || cb_comm.isChecked()) {
                        LL_main2.setVisibility(View.VISIBLE);
                    } else {
                        setResetCommModes();
                        LL_main2.setVisibility(View.GONE);
                    }
                }
            }
        });

        cb_receipt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode = BundleConstants.RECEIPT;
                if (isChecked) {
                    /*if (reportArray.contains("Communication")) {
                        reportArray.clear();
                    } else {
                        reportArray.add(mode);
                    }
                    if (cb_comm.isChecked()) {
                        cb_comm.setChecked(false);
                    } else {
                        cb_report.setChecked(false);
                        cb_comm.setChecked(false);
                    }*/
                    reportArray.add(mode);
                    LL_main2.setVisibility(View.VISIBLE);
                } else {
                    reportArray.remove(mode);
                    if (cb_report.isChecked() || cb_comm.isChecked()) {
                        LL_main2.setVisibility(View.VISIBLE);
                    } else {
                        setResetCommModes();
                        LL_main2.setVisibility(View.GONE);
                    }
                }
            }
        });

        cb_comm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode = "Communication";
                if (isChecked) {
//                    checkArray();
                    /*cb_receipt.setChecked(false);
                    cb_report.setChecked(false);
                    cb_sms.setChecked(true);
                    cb_email.setChecked(true);*/
                    reportArray.add(mode);
                    LL_main2.setVisibility(View.VISIBLE);
                } else {
                    reportArray.remove(mode);
                    if (cb_report.isChecked() || cb_receipt.isChecked()) {
                        LL_main2.setVisibility(View.VISIBLE);
                    } else {
                        setResetCommModes();
                        LL_main2.setVisibility(View.GONE);
                    }
                }
            }
        });

        cb_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Global.CommModes = "SMS";
                    cb_kyc_verification.setVisibility(View.GONE);
                    typeArray.add(Global.CommModes);
//                    cb_kyc_verification.setChecked(true);
                    ll_miscallotp.setVisibility(View.VISIBLE);
                } else {
                    if (!cb_wa.isChecked()) {
                        ll_miscallotp.setVisibility(View.GONE);
                        cb_kyc_verification.setVisibility(View.VISIBLE);
                        cb_kyc_verification.setChecked(false);
                    } else {
                        cb_kyc_verification.setVisibility(View.GONE);
                        ll_miscallotp.setVisibility(View.VISIBLE);
                    }
                    if (typeArray.contains("SMS")) {
                        typeArray.remove("SMS");
                    }
                    /*cb_kyc_verification.setChecked(false);
                    ll_miscallotp.setVisibility(View.GONE);*/
                }
            }
        });

        cb_email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edt_email.setHint("EMAIL-ID*");
                    Global.CommModes = "EMAIL";
                    typeArray.add(Global.CommModes);
                } else {
                    if (typeArray.contains("EMAIL")) {
                        typeArray.remove("EMAIL");
                    }
                    edt_email.setHint("EMAIL-ID");
                }
            }
        });

        cb_wa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_kyc_verification.setVisibility(View.GONE);
                    Global.CommModes = "WhatsApp";
                    typeArray.add(Global.CommModes);
                    ll_miscallotp.setVisibility(View.VISIBLE);
                } else {
                    if (!cb_sms.isChecked()) {
                        ll_miscallotp.setVisibility(View.GONE);
                        cb_kyc_verification.setVisibility(View.VISIBLE);
                        cb_kyc_verification.setChecked(false);
                    } else {
                        cb_kyc_verification.setVisibility(View.GONE);
                        ll_miscallotp.setVisibility(View.VISIBLE);
                    }
                    if (typeArray.contains("WhatsApp")) {
                        typeArray.remove("WhatsApp");
                    }
                }
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

        by_sendsms.setOnClickListener(new View.OnClickListener() {
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

        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.SetBottomSheet(activity);
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

        viewMain.findViewById(R.id.unchecked_entered_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                scrollView2.setVisibility(View.VISIBLE);
            }
        });
        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                samplecollectionponit.setText("SEARCH SAMPLE COLLECTION POINT");
                samplecollectionponit.setHint("SEARCH SAMPLE COLLECTION POINT*");

                Constants.covidwoe_flag = "1";
                Intent intent = new Intent(activity, ManagingTabsActivity.class);
                intent.putExtra("passToWoefragment", "frgamnebt");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

             /*   Start_New_Woe fragment = new Start_New_Woe();
                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/
            }
        });

        vial_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(activity,
                            ToastFile.vial_no,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        vial_number.setText(enteredString.substring(1));
                    } else {
                        vial_number.setText("");
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

        dateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        chk_otp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    ll_miscallotp.setVisibility(View.VISIBLE);
                    btn_snd_otp.setVisibility(View.VISIBLE);
                    btn_snd_otp.setText("Send OTP");
                    Disablefields();
                } else {
                    btn_snd_otp.setVisibility(View.GONE);
                    ll_miscallotp.setVisibility(View.GONE);
                    et_mobno.setEnabled(true);
                    et_mobno.setClickable(true);

                    if (yourCountDownTimer != null) {
                        yourCountDownTimer.cancel();
                        yourCountDownTimer = null;
                        btn_snd_otp.setEnabled(true);
                        btn_snd_otp.setClickable(true);
                        lin_otp.setVisibility(View.GONE);
                        tv_timer.setVisibility(View.GONE);
                    }

                    Enablefields();
                }

            }
        });


        et_mobno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                ) {
                    TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        et_mobno.setText(enteredString.substring(1));
                    } else {
                        et_mobno.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String checkNumber = s.toString();
                if (checkNumber.length() < 10) {
                    flag = true;
                }

                if (flag == true) {
                    if (s.length() == 10) {

                        if (!GlobalClass.isNetworkAvailable(activity)) {
                            flag = false;
                            et_mobno.setText(s);
                        } else {
                            flag = false;
                            barProgressDialog = GlobalClass.ShowprogressDialog(activity);
                            RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(activity);
                            StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + s, new
                                    Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e(TAG, "onResponse: response" + response);

                                            String getResponse = response;
                                            if (response.equals("\"proceed\"")) {

                                                GlobalClass.hideProgress(activity, barProgressDialog);
                                                et_mobno.setText(s);
                                                mobno_verify = true;

                                                if (chk_otp.isChecked()) {
                                                    btn_snd_otp.setVisibility(View.VISIBLE);
                                                } else {
                                                    btn_snd_otp.setVisibility(View.GONE);
                                                }

                                            } else {
                                                mobno_verify = false;
                                                GlobalClass.hideProgress(activity, barProgressDialog);
                                                et_mobno.setText("");
                                                TastyToast.makeText(activity, getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                            reques5tQueueCheckNumber.add(jsonObjectRequestPop);
                            Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);
                        }


                    }
                }
            }
        });

        ref_check_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flagtoAdjustClisk == 0) {
                    flagtoAdjustClisk = 1;
                    referenceBy = "SELF";
                    referedbyText.setText("");
                    refby_linear.setVisibility(View.GONE);
                    ref_check.setVisibility(View.VISIBLE);
                    uncheck_ref.setVisibility(View.GONE);
                } else if (flagtoAdjustClisk == 1) {
                    flagtoAdjustClisk = 0;
                    referenceBy = null;
                    referedbyText.setText("");
                    refby_linear.setVisibility(View.VISIBLE);
                    ref_check.setVisibility(View.GONE);
                    uncheck_ref.setVisibility(View.VISIBLE);
                }

            }
        });

        add_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref_check_linear.setVisibility(View.VISIBLE);
                add_ref.setVisibility(View.GONE);
            }
        });

        pincode_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0")) {
                    TastyToast.makeText(activity, ToastFile.crt_pincode, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        pincode_edt.setText(enteredString.substring(1));
                    } else {
                        pincode_edt.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                String enteredString = s.toString();
            }

        });


        home_kyc_format.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                    TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        home_kyc_format.setText(enteredString.substring(1));
                    } else {
                        home_kyc_format.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        referedbyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8") || enteredString.startsWith("9")) {
                    TastyToast.makeText(activity, "Enter correct Ref By", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        referedbyText.setText(enteredString.substring(1));
                    } else {
                        referedbyText.setText("");
                    }
                    if (enteredString.equals("")) {
                        ref_check_linear.setVisibility(View.VISIBLE);
                    } else {
                        ref_check_linear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String setText = s.toString();
                if (setText.equals("")) {
                    ref_check_linear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                String setText = s.toString();
                if (setText != null || setText != "") {
                    ref_check_linear.setVisibility(View.GONE);
                }
            }

        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(activity, ToastFile.crt_name, Toast.LENGTH_SHORT).show();

                    if (enteredString.length() > 0) {
                        name.setText(enteredString.substring(1));
                    } else {
                        name.setText("");
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

        patientAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(activity, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        patientAddress.setText(enteredString.substring(1));
                    } else {
                        patientAddress.setText("");
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


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genderId == false) {
                    genderId = true;
                    saveGenderId = "M";
                    male_red.setVisibility(View.VISIBLE);
                    female.setVisibility(View.VISIBLE);
                    female_red.setVisibility(View.GONE);
                    male.setVisibility(View.GONE);
                } else if (genderId == true) {
                    genderId = false;
                    saveGenderId = "M";
                    male_red.setVisibility(View.VISIBLE);
                    female.setVisibility(View.VISIBLE);
                    female_red.setVisibility(View.GONE);
                    male.setVisibility(View.GONE);
                }

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (genderId == false) {
                    genderId = true;
                    saveGenderId = "F";
                    female_red.setVisibility(View.VISIBLE);
                    male.setVisibility(View.VISIBLE);
                    male_red.setVisibility(View.GONE);
                    female.setVisibility(View.GONE);

                } else if (genderId == true) {
                    genderId = false;
                    saveGenderId = "F";
                    female_red.setVisibility(View.VISIBLE);
                    male.setVisibility(View.VISIBLE);
                    male_red.setVisibility(View.GONE);
                    female.setVisibility(View.GONE);

                }

            }
        });


        age.addTextChangedListener(new TextWatcher() {
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
                } else {

                }
                String enteredString = s.toString();

                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(mContext,
                            ToastFile.crt_age,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        age.setText(enteredString.substring(1));
                    } else {
                        age.setText("");
                    }
                }
                if (age.getText().toString().equals("")) {

                } else {
                    if (agesinteger < 12) {
                        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.Patientsagespinner,
                                R.layout.name_age_spinner);
                        PatientsagespinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(PatientsagespinnerAdapter);
                    }
                    if (agesinteger > 12) {
                        ArrayAdapter Patientsagespinner = ArrayAdapter.createFromResource(mContext, R.array.Patientspinyrday,
                                R.layout.name_age_spinner);
                        Patientsagespinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(Patientsagespinner);
                    }
                    if (agesinteger > 29) {
                        ArrayAdapter Patientsagesyr = ArrayAdapter.createFromResource(mContext, R.array.Patientspinyr,
                                R.layout.name_age_spinner);
                        Patientsagesyr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(Patientsagesyr);
                    }
                }
            }
        });


    }

    private String getRandomString() {
        String char_random = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str_random = new StringBuilder();
        Random rnd = new Random();
        while (str_random.length() < 16) { // length of the random string.
            int index = (int) (rnd.nextFloat() * char_random.length());
            str_random.append(char_random.charAt(index));
        }
        String final_random_str = str_random.toString();
        return final_random_str;

    }

    private void setResetCommModes() {
        cb_email.setChecked(false);
        cb_sms.setChecked(false);
        cb_comm.setChecked(false);
        cb_receipt.setChecked(false);
        cb_report.setChecked(false);
        cb_wa.setChecked(false);
    }

    private void checkArray() {
        if (GlobalClass.isArraylistNotNull(reportArray) && reportArray.size() > 0) {
            reportArray.clear();
            mode = "Communication";
            reportArray.add(mode);
        } else {
            mode = "Communication";
            reportArray.add(mode);
        }
    }

    private void init() {
        mContext = getContext();
        activity = getActivity();
        cd = new ConnectionDetector(activity);
        appPreferenceManager = new AppPreferenceManager(activity);

        covidacc = appPreferenceManager.getCovidAccessResponseModel().isCovidRegistration();
        PackageInfo pInfo = null;
        try {
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        cleverTapHelper = new CleverTapHelper(activity);
        String Header = "Cliso App" +","+ versionName;
        if (GlobalClass.ComingFrom != null) {
            cleverTapHelper.NovidPageLoadEvent(Header, GlobalClass.ComingFrom);
            GlobalClass.ComingFrom = "";
        }


        name = (AutoCompleteTextView) viewMain.findViewById(R.id.name);
        img_restPatientData = (ImageView) viewMain.findViewById(R.id.img_restPatientData);
        age = (EditText) viewMain.findViewById(R.id.age);
        id_for_woe = (EditText) viewMain.findViewById(R.id.id_for_woe);
        barcode_woe = (EditText) viewMain.findViewById(R.id.barcode_woe);
        pincode_edt = (EditText) viewMain.findViewById(R.id.pincode_edt);
        kyc_format = (EditText) viewMain.findViewById(R.id.kyc_format);
        home_kyc_format = (EditText) viewMain.findViewById(R.id.home_kyc_format);
        patientAddress = (EditText) viewMain.findViewById(R.id.patientAddress);
        vial_number = (EditText) viewMain.findViewById(R.id.vial_number);
        ll_email = (LinearLayout) viewMain.findViewById(R.id.ll_email);

        et_mobno = viewMain.findViewById(R.id.et_mobno);
        et_otp = viewMain.findViewById(R.id.et_otp);

        tv_unbilled = viewMain.findViewById(R.id.tv_unbilled);
        tv_avl_bal = viewMain.findViewById(R.id.tv_avl_bal);


        chk_otp = viewMain.findViewById(R.id.chk_otp);
        lin_ckotp = viewMain.findViewById(R.id.lin_ckotp);

        male = (ImageView) viewMain.findViewById(R.id.male);
        male_red = (ImageView) viewMain.findViewById(R.id.male_red);
        female = (ImageView) viewMain.findViewById(R.id.female);
        female_red = (ImageView) viewMain.findViewById(R.id.female_red);
        next_btn = (Button) viewMain.findViewById(R.id.next_btn_patient);
        edt_email = (EditText) viewMain.findViewById(R.id.edt_email);

        btn_snd_otp = viewMain.findViewById(R.id.btn_sendotp);
        btn_snd_otp.setOnClickListener(this);
        tv_help = viewMain.findViewById(R.id.tv_help);
        tv_help.setText(Html.fromHtml("<u> Help</u>"));
        btn_verifyotp = viewMain.findViewById(R.id.btn_verifyotp);
        btn_verifyotp.setOnClickListener(this);

        spinyr = (Spinner) viewMain.findViewById(R.id.spinyr);
        scrollView2 = (ScrollView) viewMain.findViewById(R.id.scrollView2);
        dateShow = (TextView) viewMain.findViewById(R.id.date);
        leadbarcodename = (TextView) viewMain.findViewById(R.id.leadbarcodename);
        leadidbarcodetest = (TextView) viewMain.findViewById(R.id.leadidbarcodetest);
        leadbarcoderefdr = (TextView) viewMain.findViewById(R.id.leadbarcoderefdr);
        leadbarcodesct = (TextView) viewMain.findViewById(R.id.leadbarcodesct);
        leadname = (TextView) viewMain.findViewById(R.id.leadname);
        leadidtest = (TextView) viewMain.findViewById(R.id.leadidtest);
        leadrefdr = (TextView) viewMain.findViewById(R.id.leadrefdr);
        add_ref = (ImageView) viewMain.findViewById(R.id.add_ref);
        tv_verifiedmob = (TextView) viewMain.findViewById(R.id.tv_verifiedmob);
        GlobalClass.setflagToRefreshData = false;
        timehr = (Spinner) viewMain.findViewById(R.id.timehr);
        timesecond = (Spinner) viewMain.findViewById(R.id.timesecond);
        selectTypeSpinner = (Spinner) viewMain.findViewById(R.id.selectTypeSpinner);
        brand_spinner = (Spinner) viewMain.findViewById(R.id.brand_spinner);
        camp_spinner_olc = (Spinner) viewMain.findViewById(R.id.camp_spinner_olc);
        btechname = (Spinner) viewMain.findViewById(R.id.btech_spinner);
        timeampm = (Spinner) viewMain.findViewById(R.id.timeampm);
        samplecollectionponit = viewMain.findViewById(R.id.samplecollectionponit);
        scp_name = (RecyclerView) viewMain.findViewById(R.id.scp_name);
        referedbyText = (AutoCompleteTextView) viewMain.findViewById(R.id.referedby);//
        radio = (TextView) viewMain.findViewById(R.id.radio);
        tv_timer = viewMain.findViewById(R.id.tv_timer);
        refby_linear = (LinearLayout) viewMain.findViewById(R.id.refby_linear);
        ll_miscallotp = (LinearLayout) viewMain.findViewById(R.id.ll_miscallotp);
        camp_layout_woe = (LinearLayout) viewMain.findViewById(R.id.camp_layout_woe);
        btech_linear_layout = (LinearLayout) viewMain.findViewById(R.id.btech_linear_layout);
        labname_linear = (LinearLayout) viewMain.findViewById(R.id.labname_linear);
        home_layout = (LinearLayout) viewMain.findViewById(R.id.home_linear_data);
        pincode_linear_data = (LinearLayout) viewMain.findViewById(R.id.pincode_linear_data);

        btn_generate = viewMain.findViewById(R.id.btn_generate);
        btn_generate.setText(getResources().getString(R.string.enterccc));
        edt_missed_mobile = viewMain.findViewById(R.id.edt_missed_mobile);
        btn_resend = viewMain.findViewById(R.id.btn_resend);
        tv_timer_new = viewMain.findViewById(R.id.tv_timer);
        edt_verifycc = viewMain.findViewById(R.id.edt_verifycc);
        tv_resetno = viewMain.findViewById(R.id.tv_resetno);
        rel_mobno = viewMain.findViewById(R.id.rel_mobno);
        rel_verify_mobile = viewMain.findViewById(R.id.rel_verify_mobile);
        radiogrp2 = viewMain.findViewById(R.id.radiogrp2);

        by_sendsms = viewMain.findViewById(R.id.by_sendsms);
        radiogrp2.check(by_sendsms.getId());

        lin_generate_verify = viewMain.findViewById(R.id.lin_generate_verify);
        tv_mobileno = viewMain.findViewById(R.id.tv_mobileno);
        lin_by_missed = viewMain.findViewById(R.id.lin_missed_verify);
        lin_missed_verify = viewMain.findViewById(R.id.lin_missed_verify);
        by_missed = viewMain.findViewById(R.id.by_missed);
        by_generate = viewMain.findViewById(R.id.by_generate);
        btn_verify = viewMain.findViewById(R.id.btn_verify);
        mobile_number_kyc = (LinearLayout) viewMain.findViewById(R.id.mobile_number_kyc);
        Home_mobile_number_kyc = (LinearLayout) viewMain.findViewById(R.id.Home_mobile_number_kyc);
        ll_mobileno_otp = viewMain.findViewById(R.id.ll_mobileno_otp);
        lin_otp = viewMain.findViewById(R.id.lin_otp);

        enter_ll_unselected = (LinearLayout) viewMain.findViewById(R.id.enter_ll_unselected);
        leadbarcodelayout = (LinearLayout) viewMain.findViewById(R.id.leadbarcodelayout);
        ref_check_linear = (LinearLayout) viewMain.findViewById(R.id.ref_check_linear);
        namePatients = (LinearLayout) viewMain.findViewById(R.id.namePatients);
        AGE_layout = (LinearLayout) viewMain.findViewById(R.id.AGE_layout);
        time_layout = (LinearLayout) viewMain.findViewById(R.id.time_layout);
        id_layout = (LinearLayout) viewMain.findViewById(R.id.id_layout);
        barcode_layout = (LinearLayout) viewMain.findViewById(R.id.barcode_layout);
        leadlayout = (LinearLayout) viewMain.findViewById(R.id.leadlayout);
        unchecked_entered_ll = (LinearLayout) viewMain.findViewById(R.id.unchecked_entered_ll);
        enetered = (TextView) viewMain.findViewById(R.id.enetered);
        enter = (TextView) viewMain.findViewById(R.id.enter);
        tv_mob_note = (TextView) viewMain.findViewById(R.id.tv_mob_note);
        enter_arrow_enter = (ImageView) viewMain.findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) viewMain.findViewById(R.id.enter_arrow_entered);
        uncheck_ref = (ImageView) viewMain.findViewById(R.id.uncheck_ref);
        ref_check = (ImageView) viewMain.findViewById(R.id.ref_check);
        btn_clear_data = (Button) viewMain.findViewById(R.id.btn_clear_data);
        cb_kyc_verification = (CheckBox) viewMain.findViewById(R.id.cb_kyc_verification);
        tv_baseline = viewMain.findViewById(R.id.tv_baseline);

        getshared = activity.getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);

        prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");
        access = prefs.getString("ACCESS_TYPE", "");
        usercode = prefs.getString("USER_CODE", "");
        api_key = prefs.getString("API_KEY", "");
        source_type = prefs.getString("SOURCE_TYPE", "");

        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);

        cb_report = viewMain.findViewById(R.id.cb_report);
        cb_receipt = viewMain.findViewById(R.id.cb_receipt);
        cb_comm = viewMain.findViewById(R.id.cb_comm);
        cb_sms = viewMain.findViewById(R.id.cb_sms);
        cb_email = viewMain.findViewById(R.id.cb_email);
        cb_wa = viewMain.findViewById(R.id.cb_wa);
        LL_main2 = viewMain.findViewById(R.id.LL_main2);
        ll_communication_main = viewMain.findViewById(R.id.ll_communication_main);

        if (Global.isKYC) {
            cb_kyc_verification.setVisibility(View.GONE);
            by_sendsms.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.GONE);
            by_missed.setVisibility(View.GONE);
        } else {
            cb_kyc_verification.setVisibility(View.GONE);
            by_missed.setVisibility(View.VISIBLE);
            by_generate.setVisibility(View.VISIBLE);
            by_sendsms.setVisibility(View.VISIBLE);
        }


        // Inflate the layout for this fragment
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);
        dateShow.setText(showDate);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);
        minDate = myCalendar.getTime().getTime();
        myDb = new DatabaseHelper(mContext);

        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40), new InputFilter.AllCaps()});

        if (GlobalClass.flagToSendfromnavigation == true) {
            GlobalClass.flagToSendfromnavigation = false;
            enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
            enter_arrow_enter.setVisibility(View.GONE);
            enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
            enter_arrow_entered.setVisibility(View.VISIBLE);
            scrollView2.setVisibility(View.GONE);
        } else {
            scrollView2.setVisibility(View.VISIBLE);
        }

        GlobalClass.isAutoTimeSelected(activity);

//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            Log.e("SDK_INT", "" + Build.VERSION.SDK_INT);
//            samplecollectionponit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow_woe, 0);
//            samplecollectionponit.setCompoundDrawablePadding(5);
//        } else {
//            samplecollectionponit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
//        }

        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.remove("name");
        saveDetails.remove("age");
        saveDetails.remove("gender");
        saveDetails.remove("sct");
        saveDetails.remove("date");
        saveDetails.remove("ageType");
        saveDetails.remove("labname");
        saveDetails.remove("labAddress");
        saveDetails.remove("patientAddress");
        saveDetails.remove("refBy");
        saveDetails.remove("refId");
        saveDetails.remove("labIDaddress");
        saveDetails.remove("btechIDToPass");
        saveDetails.remove("btechNameToPass");
        saveDetails.remove("getcampIDtoPass");
        saveDetails.remove("kycinfo");
        saveDetails.remove("woetype");
        saveDetails.remove("WOEbrand");
        saveDetails.remove("SR_NO");
        saveDetails.remove("EMAIL_ID");
        saveDetails.remove("Communication");
        saveDetails.remove("CommModes");
        saveDetails.commit();

        getCurrentDateandTime = new Date();


        ref_check.setVisibility(View.GONE);
        uncheck_ref.setVisibility(View.VISIBLE);
        referenceBy = null;
        checkifChecked = null;
        refby_linear.setVisibility(View.VISIBLE);

        if (GlobalClass.setScp_Constant != null) {
            samplecollectionponit.setText(GlobalClass.setScp_Constant);
        } else {
            samplecollectionponit.setText("");
        }


        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        Gson gson = new Gson();
        myPojo = new MyPojo();
        String json = appSharedPrefs.getString("saveAlldata", "");
        myPojo = gson.fromJson(json, MyPojo.class);

        try {
            String date = appPreferenceManager.getWoMasterSyncDate();
            if (!date.equalsIgnoreCase(GlobalClass.getCurrentDate())) {
                requestJsonObject();
            } else {

                if (myPojo != null) {
                    getBrandName = new ArrayList<>();
                    spinnerBrandName = new ArrayList<String>();
                    getDatafetch = new ArrayList();
                    getSubSource = new ArrayList();

                    try {
                        if (myPojo.getMASTERS().getBRAND_LIST() != null) {
                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                                getDatafetch.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                spinnerBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                camp_lists = myPojo.getMASTERS().getCAMP_LIST();
                                // GlobalClass.getcamp_lists=camp_lists;

                                if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                                    String TspNumber = myPojo.getMASTERS().getTSP_MASTER().getNumber();
                                    SharedPreferences.Editor editor = activity.getSharedPreferences("TspNumber", 0).edit();
                                    editor.putString("TSPMobileNumber", TspNumber);
                                    editor.commit();
                                }

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (myPojo != null) {
                        if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getSUB_SOURCECODE() != null) {
                            for (int i = 0; i < myPojo.getMASTERS().getSUB_SOURCECODE().length; i++) {
                                getSubSource.add(myPojo.getMASTERS().getSUB_SOURCECODE()[i].getSub_source_code_pass());
                            }
                        }

                    }

                    spinnerTypeName = new ArrayList<>();
                    getTypeListfirst = new ArrayList<>();
                    getTypeListSMT = new ArrayList<>();

                    try {
                        if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null && myPojo.getMASTERS().getBRAND_LIST().length != 0) {
                            if (myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type() != null) {
                                for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                    getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                                }
                            }
                        }


                        if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null && myPojo.getMASTERS().getBRAND_LIST().length != 0) {
                            if (myPojo.getMASTERS().getBRAND_LIST().length > 1) {

                                if (myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_name().contains("SMT")) {

                                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null && myPojo.getMASTERS().getBRAND_LIST().length != 0) {
                                        if (myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type() != null) {
                                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type().length; i++) {
                                                getTypeListSMT.add(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type()[i].getType());
                                            }
                                        }
                                    }

                                    if (myPojo.getMASTERS().getBRAND_LIST()[2] != null) {
                                        Brand_type[] c = myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type();
                                        Brand_type c1 = new Brand_type();
                                        getTypeListsecond = new ArrayList<>();
                                        for (int j = 0; j < c.length; j++) {
                                            System.out.println(c[j].getType());
                                            String type12 = "";
                                            type12 = c[j].getType();
                                            getTypeListsecond.add(c[j].getType());
                                        }
                                    }

                                    if (myPojo.getMASTERS().getBRAND_LIST()[3] != null) {
                                        Brand_type[] data = myPojo.getMASTERS().getBRAND_LIST()[3].getBrand_type();
                                        Brand_type d1 = new Brand_type();
                                        getTypeListthird = new ArrayList<>();
                                        for (int k = 0; k < data.length; k++) {
                                            System.out.println(data[k].getType());
                                            String type12 = "";
                                            type12 = data[k].getType();
                                            getTypeListthird.add(data[k].getType());
                                        }
                                    }
                                } else {
                                    if (myPojo.getMASTERS().getBRAND_LIST()[1] != null) {
                                        Brand_type[] c = myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type();
                                        Brand_type c1 = new Brand_type();
                                        getTypeListsecond = new ArrayList<>();
                                        for (int j = 0; j < c.length; j++) {
                                            System.out.println(c[j].getType());
                                            String type12 = "";
                                            type12 = c[j].getType();
                                            getTypeListsecond.add(c[j].getType());
                                        }
                                    }

                                    try {
                                        if (myPojo.getMASTERS().getBRAND_LIST()[2] != null) {
                                            Brand_type[] data = myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type();
                                            Brand_type d1 = new Brand_type();
                                            getTypeListthird = new ArrayList<>();
                                            for (int k = 0; k < data.length; k++) {
                                                System.out.println(data[k].getType());
                                                String type12 = "";
                                                type12 = data[k].getType();
                                                getTypeListthird.add(data[k].getType());
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(activity);
                            Gson gsondata = new Gson();
                            String jsondata = appSharedPrefsdata.getString("savelabnames", "");
                            obj = new SourceILSMainModel();
                            obj = gsondata.fromJson(jsondata, SourceILSMainModel.class);

                            if (obj != null) {
                                if (obj.getMASTERS() != null && obj.getUSER_TYPE() != null) {
                                    callAdapter(obj);
                                } else {
                                    fetchData();
                                }
                            } else {
                                fetchData();
                            }
                        } else {
                            if (!isLoaded) {
                                requestJsonObject();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String getAddress = "";

                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                        getAddress = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                        SharedPreferences.Editor ScpAddress = activity.getSharedPreferences("ScpAddress", 0).edit();
                        ScpAddress.putString("scp_addrr", getAddress);
                        ScpAddress.commit();
                    }

                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getLAB_ALERTS() != null) {
                        GlobalClass.putData = myPojo.getMASTERS().getLAB_ALERTS();
                        for (int i = 0; i < GlobalClass.putData.length; i++) {
                            GlobalClass.items.add(GlobalClass.putData[i]);
                        }
                    }

                    SetBrandSpinner();


                } else {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        // Set the Alert Dialog Message
                        builder.setMessage(ToastFile.intConnection)
                                .setCancelable(false)
                                .setPositiveButton("Retry",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                Start_New_Woe fragment = new Start_New_Woe();
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_mainLayout, fragment, fragment.getClass
                                                        ().getSimpleName()).addToBackStack(null).commit();
                                            }
                                        });
                        alert = builder.create();
                        alert.show();
                    } else {
                        if (!isLoaded) {
                            requestJsonObject();
                        }
                    }
                }

            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        getBtechList = new ArrayList<>();
        getCampNames = new ArrayList<>();
        btechSpinner = new ArrayList<>();

        if (myPojo != null) {
            if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getBCT_LIST() != null) {
                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                    getBtechList.add(myPojo.getMASTERS().getBCT_LIST()[j]);
                }
            } else {
                TastyToast.makeText(activity, "Please register NED", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            try {
                if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                    getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getCampNames.add("Select Camp");
            if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getCAMP_LIST() != null) {
                for (int k = 0; k < myPojo.getMASTERS().getCAMP_LIST().length; k++) {
                    getCampNames.add(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE());
                }
                if (getCampNames != null) {
//                    camp_spinner_olc.setAdapter(new ArrayAdapter<String>(activity, R.layout.spinnerproperty, getCampNames));
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                            mContext, R.layout.spinnerproperty, getCampNames);
                    camp_spinner_olc.setAdapter(adapter2);
                    camp_spinner_olc.setSelection(0);
                }
            }

            btechSpinner.add("SELECT BTECH NAME");
            for (int i = 0; i < getBtechList.size(); i++) {
                btechSpinner.add(getBtechList.get(i).getNAME());
                if (btechSpinner.size() != 0) {
                    btechname.setAdapter(new ArrayAdapter<String>(activity, R.layout.spinnerproperty, btechSpinner));
                }
            }

        } else {
            getTspNumber();

            prof = getshared.getString("prof", null);
            if (prof != null) {
                String ac_code = getshared.getString("ac_code", null);
                String address = getshared.getString("address", null);
                String email = getshared.getString("email", null);
                mobile = getshared.getString("mobile", null);
                nameofProfile = getshared.getString("name", null);
                String pincode = getshared.getString("pincode", null);
                String user_code = getshared.getString("user_code", null);
                String closing_balance = getshared.getString("closing_balance", null);
                String credit_limit = getshared.getString("credit_limit", null);
                String doj = getshared.getString("doj", "");
                String source_code = getshared.getString("source_code", null);
                String tsp_img = getshared.getString("tsp_image", null);
            }
        }

        if (getLabNmae != null && getReferenceNmae != null) {
            spinnerDialogRef = new SpinnerDialog(activity, getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(activity, getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// With

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    referedbyText.setText(s);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    referedbyText.setText(item);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });
        }

        hourSin = Arrays.asList("HR*", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

        minuteSpin = Arrays.asList("MIN*", "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");

        List<String> ampmSpine = Arrays.asList("AM/PM", "AM", "PM");

        List<String> patientsagespinner = Arrays.asList("Years", "Months", "Days");


        Date setDateToSpin = new Date();

        SimpleDateFormat sdfParse = new SimpleDateFormat("hh mm a");
        SimpleDateFormat sdfParseDate = new SimpleDateFormat("hh:mm");

        String getDateToset = sdfParse.format(setDateToSpin);

        if (getDateToset.endsWith("PM") || getDateToset.endsWith("p.m.")) {
            getAmPm = "PM";
        } else {
            getAmPm = "AM";
        }

        String getDateToParse = sdfParseDate.format(setDateToSpin);
        String timme = getDateToParse;
        String[] time = timme.split(":");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                mContext, R.layout.name_age_spinner, hourSin);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timehr.setAdapter(adapter2);
        timehr.setSelection(0);

        ArrayAdapter<String> timesecondspinner = new ArrayAdapter<String>(
                mContext, R.layout.name_age_spinner, minuteSpin);
        timesecondspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesecond.setAdapter(timesecondspinner);
        timesecond.setSelection(0);

        ArrayAdapter<String> timesecondspinnerdata = new ArrayAdapter<String>(
                mContext, R.layout.name_age_spinner, ampmSpine);
        timesecondspinnerdata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeampm.setAdapter(timesecondspinnerdata);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.DAY_OF_MONTH, -2);
        minDate = c.getTime().getTime();

        namestr = name.getText().toString();
        saveGenderId = "";

        if (patientsagespinner != null) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    mContext, R.layout.name_age_spinner, patientsagespinner);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinyr.setAdapter(adap);
            spinyr.setSelection(0);
        }

        getRandomId = activity.getSharedPreferences("Temp_Wo_Id", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor1 = getRandomId.edit();
        prefsEditor1.putString("Temp_Wo_Id", getRandomString());
        prefsEditor1.apply();
        System.out.println(activity.getSharedPreferences("Temp_Wo_Id",Context.MODE_PRIVATE).getString("Temp_Wo_Id", getRandomString()) + "Random Id");
    }

    private void SetBrandSpinner() {
        try {
            if (GlobalClass.CheckArrayList(spinnerBrandName)) {
                if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                    spinnerBrandName.clear();
                    spinnerBrandName.add("NHL");
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, spinnerBrandName);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brand_spinner.setAdapter(adapter2);
                } else {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, spinnerBrandName);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brand_spinner.setAdapter(adapter2);
                }
                brand_spinner.setSelection(0);
                startDataSetting();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enterNextFragment() {
        Woe_fragment a2Fragment = new Woe_fragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        //transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commitAllowingStateLoss();
    }

    private void requestJsonObject() {
        try {
            barProgressDialog = new ProgressDialog(activity);
            barProgressDialog.setTitle("Kindly wait ...");
            barProgressDialog.setMessage(ToastFile.processing_request);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(20);
            barProgressDialog.show();
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);
            RequestQueue requestQueue = GlobalClass.setVolleyReq(mContext);
            String url = Api.Cloud_base + api_key + "/" + user + "/B2BAPP/getwomaster";
            Log.e(TAG, "request API: " + url);
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        GlobalClass.hideProgress(activity, barProgressDialog);
                        Log.e(TAG, "onResponse: RESPONSE" + response);
                        if (response != null) {
                            //Sushil Store data on success
                            appPreferenceManager.setWoMasterSyncDate(GlobalClass.getCurrentDate());
                            Gson gson = new Gson();
                            myPojo = new MyPojo();
                            myPojo = gson.fromJson(response.toString(), MyPojo.class);

                            if (myPojo != null && !GlobalClass.isNull(myPojo.getRESPONSE()) && myPojo.getRESPONSE().equalsIgnoreCase(caps_invalidApikey)) {

                                GlobalClass.redirectToLogin(activity);
                            } else {
                                GlobalClass.hideProgress(activity, barProgressDialog);
                                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                                Gson gson22 = new Gson();
                                String json22 = gson22.toJson(myPojo);
                                prefsEditor1.putString("saveAlldata", json22);
                                prefsEditor1.apply();

                                fetchData();

                                isLoaded = true;
                                getBrandName = new ArrayList<>();
                                spinnerBrandName = new ArrayList<String>();
                                /*spinnerBrandName.add("Select Brand Name");*/
                                getDatafetch = new ArrayList();
                                getSubSource = new ArrayList();

                                try {
                                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null) {
                                        for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                                            getDatafetch.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                            spinnerBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                            camp_lists = myPojo.getMASTERS().getCAMP_LIST();
                                            // GlobalClass.getcamp_lists=camp_lists;
                                            String TspNumber = myPojo.getMASTERS().getTSP_MASTER().getNumber();
                                            SharedPreferences.Editor editor = activity.getSharedPreferences("TspNumber", 0).edit();
                                            editor.putString("TSPMobileNumber", TspNumber);
                                            editor.apply();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getSUB_SOURCECODE() != null) {
                                        for (int i = 0; i < myPojo.getMASTERS().getSUB_SOURCECODE().length; i++) {
                                            getSubSource.add(myPojo.getMASTERS().getSUB_SOURCECODE()[i].getSub_source_code_pass());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                spinnerTypeName = new ArrayList<>();
                                getTypeListfirst = new ArrayList<>();
                                getTypeListsecond = new ArrayList<>();
                                getTypeListSMT = new ArrayList<>();

                                if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null && myPojo.getMASTERS().getBRAND_LIST().length > 1) {

                                    for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                        getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                                    }

                                    if (myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_name().equalsIgnoreCase("SMT")) {
                                        for (int k = 0; k < myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type().length; k++) {
                                            getTypeListSMT.add(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type()[k].getType());
                                        }

                                        for (int k = 0; k < myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type().length; k++) {
                                            getTypeListsecond.add(myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type()[k].getType());
                                        }

                                        getTypeListthird = new ArrayList<>();
                                        for (int l = 0; l < myPojo.getMASTERS().getBRAND_LIST()[3].getBrand_type().length; l++) {
                                            getTypeListthird.add(myPojo.getMASTERS().getBRAND_LIST()[3].getBrand_type()[l].getType());
                                        }
                                        getTypeListthird = new ArrayList<>();
                                        for (int l = 0; l < myPojo.getMASTERS().getBRAND_LIST()[4].getBrand_type().length; l++) {
                                            getTypeListthird.add(myPojo.getMASTERS().getBRAND_LIST()[4].getBrand_type()[l].getType());
                                        }
                                    } else {
                                        for (int k = 0; k < myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type().length; k++) {
                                            getTypeListsecond.add(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type()[k].getType());
                                        }
                                        getTypeListthird = new ArrayList<>();
                                        for (int l = 0; l < myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type().length; l++) {
                                            getTypeListthird.add(myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type()[l].getType());
                                        }
                                        getTypeListthird = new ArrayList<>();
                                        for (int l = 0; l < myPojo.getMASTERS().getBRAND_LIST()[3].getBrand_type().length; l++) {
                                            getTypeListthird.add(myPojo.getMASTERS().getBRAND_LIST()[3].getBrand_type()[l].getType());
                                        }
                                    }

                                } else {
                                    if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null && myPojo.getMASTERS().getBRAND_LIST().length == 1) {
                                        for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                            getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                                        }
                                    }
                                }


                                try {
                                    if (myPojo != null) {
                                        if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                                            String getAddress = myPojo.getMASTERS().getTSP_MASTER().getAddress();

                                            SharedPreferences.Editor ScpAddress = activity.getSharedPreferences("ScpAddress", 0).edit();
                                            ScpAddress.putString("scp_addrr", getAddress);
                                            ScpAddress.commit();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getLAB_ALERTS() != null) {
                                    GlobalClass.putData = myPojo.getMASTERS().getLAB_ALERTS();
                                }


                                if (GlobalClass.putData != null) {
                                    for (int i = 0; i < GlobalClass.putData.length; i++) {
                                        GlobalClass.items.add(GlobalClass.putData[i]);
                                    }
                                }

                                // Spinner adapter
                                SetBrandSpinner();


                                startDataSetting();
                            }
                        } else {

                            Toast.makeText(mContext, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse == null) {
                        GlobalClass.hideProgress(activity, barProgressDialog);

                    }
                }
            });

            jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getOTPFlag() {
        return getshared.getString("PriOTP", "NO");
    }

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    mobileverify(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN, 0);
                }
            } else {
                mobileverify(edt_missed_mobile.getText().toString());
            }
        }
    }


    private boolean mobilenovalidation() {
        if (edt_missed_mobile.getText().toString().length() == 0) {
            Global.showCustomToast(activity, ToastFile.ENTER_MOBILE);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            Global.showCustomToast(activity, ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            Global.showCustomToast(activity, ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        return true;
    }

    private void mobileverify(String mobileno) {

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        CoVerifyMobReq coVerifyMobReq = new CoVerifyMobReq();
        coVerifyMobReq.setApi_key(api_key);
        coVerifyMobReq.setMobile(mobileno);
        coVerifyMobReq.setScode(usercode);

        final Call<COVerifyMobileResponse> covidmis_responseCall = postAPIInterface.covmobileVerification(coVerifyMobReq);
        Log.e(TAG, "MOB VERIFY URL--->" + covidmis_responseCall.request().url());
        Log.e(TAG, "MOB VERIFY BODY--->" + new GsonBuilder().create().toJson(coVerifyMobReq));
        covidmis_responseCall.enqueue(new Callback<COVerifyMobileResponse>() {
            @Override
            public void onResponse(Call<COVerifyMobileResponse> call, retrofit2.Response<COVerifyMobileResponse> response) {
                Log.e(TAG, "on Response-->" + response.body().getResponse());
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        if (response.body().getResponse().equalsIgnoreCase("NOT VERIFIED")) {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            TastyToast.makeText(getContext(), response.body().getResponse(), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            timerflag = true;
                            Global.OTPVERIFIED = true;
//                            edt_email.setHint("EMAIL-ID*");

                            if (yourCountDownTimer != null) {
                                yourCountDownTimer.cancel();
                                yourCountDownTimer = null;
                            }

                            btn_verifyotp.setText("Verified");
                            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.green));


                            et_otp.getText().clear();
                            lin_otp.setVisibility(View.GONE);

                            btn_snd_otp.setText("Reset");

                            et_mobno.setEnabled(false);
                            et_mobno.setClickable(false);

                            imgotp = getContext().getResources().getDrawable(R.drawable.otpverify);
                            imgotp.setBounds(40, 40, 40, 40);
                            et_mobno.setCompoundDrawablesWithIntrinsicBounds(null, null, imgotp, null);

                            et_otp.setEnabled(false);
                            et_otp.setClickable(false);

                            btn_snd_otp.setClickable(true);
                            btn_snd_otp.setEnabled(true);

                            btn_verifyotp.setClickable(false);
                            btn_verifyotp.setEnabled(false);

                            tv_timer.setVisibility(View.GONE);
                            lin_ckotp.setVisibility(View.GONE);

                            CallAPIToGetPatientDetails();
                            Enablefields();

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

    private void CallAPIToGetPatientDetails() {
        resetpatientFields();
        final GetPatientDetailsRequestModel requestModel = new GetPatientDetailsRequestModel();
        requestModel.setApi_key(prefs.getString("API_KEY", ""));
        requestModel.setMobile(edt_missed_mobile.getText().toString().trim());
        requestModel.setScode(user);

        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);
        Call<PatientDetailsAPiResponseModel> responseModelCall = apiInterface.CallGetPatientDetailsAPI(requestModel);
        final ProgressDialog progressDialog1 = GlobalClass.ShowprogressDialog(activity);
        responseModelCall.enqueue(new Callback<PatientDetailsAPiResponseModel>() {
            @Override
            public void onResponse(Call<PatientDetailsAPiResponseModel> call, retrofit2.Response<PatientDetailsAPiResponseModel> response) {
                try {
                    GlobalClass.hideProgress(activity, progressDialog1);
                    if (response.body() != null) {
                        PatientDetailsAPiResponseModel responseModel = response.body();
                        if (!GlobalClass.isNull(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase(Constants.RES0000) && responseModel.getPatientInfo() != null && responseModel.getPatientInfo().size() > 0) {
                            patientDetailAryList = responseModel.getPatientInfo();
                            SetAutoCompleteViewAdapterToView(true);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PatientDetailsAPiResponseModel> call, Throwable t) {
                GlobalClass.hideProgress(activity, progressDialog1);
            }
        });
    }

    private void SetAutoCompleteViewAdapterToView(boolean applyLogic) {
        name.setAdapter(null);
        resetpatientFields();
        Constants.selectedPatientData = null;
        if (applyLogic) {
            if (patientDetailAryList != null && patientDetailAryList.size() > 0) {
                ArrayAdapter<PatientDetailsAPiResponseModel.PatientInfoBean> adapter = new ArrayAdapter<PatientDetailsAPiResponseModel.PatientInfoBean>(mContext, android.R.layout.simple_dropdown_item_1line, patientDetailAryList);
                name.setAdapter(adapter);
                name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (patientDetailAryList != null && patientDetailAryList.size() > position) {
                            Constants.selectedPatientData = new PatientDetailsAPiResponseModel.PatientInfoBean();
                            Constants.selectedPatientData = patientDetailAryList.get(position);
                            AutoFillPatientDetails(Constants.selectedPatientData);
                        } else {
                            resetpatientFields();
                        }
                    }
                });

                name.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        name.showDropDown();
                        return false;
                    }
                });
            }
        }

    }

    private void AutoFillPatientDetails(PatientDetailsAPiResponseModel.PatientInfoBean selectedPatientData) {

        name.setEnabled(false);
        img_restPatientData.setVisibility(View.VISIBLE);
        img_restPatientData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpatientFields();
            }
        });

        int AgeOfPatient = DateUtils.getAgeFromDate(selectedPatientData.getDOB(), "dd/MM/yyyy hh:mm:ss a");
        if (AgeOfPatient > 0) {
            GlobalClass.SetText(age, "" + AgeOfPatient);
        }
        GlobalClass.SetText(patientAddress, selectedPatientData.getAddress());
        GlobalClass.SetText(pincode_edt, selectedPatientData.getPincode());
        GlobalClass.SetText(edt_email, selectedPatientData.getEmail());


        if (!GlobalClass.isNull(selectedPatientData.getGender())) {
            if (selectedPatientData.getGender().equalsIgnoreCase("M")) {
                genderId = true;
                saveGenderId = "M";
                male_red.setVisibility(View.VISIBLE);
                female.setVisibility(View.VISIBLE);
                female_red.setVisibility(View.GONE);
                male.setVisibility(View.GONE);
            } else if (selectedPatientData.getGender().equalsIgnoreCase("F")) {
                genderId = false;
                saveGenderId = "F";
                male_red.setVisibility(View.GONE);
                female.setVisibility(View.GONE);
                female_red.setVisibility(View.VISIBLE);
                male.setVisibility(View.VISIBLE);
            }
        }
    }

    private void resetpatientFields() {
        Constants.selectedPatientData = null;
        img_restPatientData.setVisibility(View.GONE);
        name.setEnabled(true);
        name.setText("");
        GlobalClass.SetText(age, "");
        GlobalClass.SetText(patientAddress, "");
        GlobalClass.SetText(pincode_edt, "");

        genderId = false;
        saveGenderId = "";
        male_red.setVisibility(View.GONE);
        female_red.setVisibility(View.GONE);

        male.setVisibility(View.VISIBLE);
        female.setVisibility(View.VISIBLE);
    }

    private void disablefields() {

        rel_verify_mobile.setVisibility(View.VISIBLE);
        tv_verifiedmob.setText(edt_missed_mobile.getText().toString());
        lin_by_missed.setVisibility(View.GONE);
        tv_mobileno.setVisibility(View.GONE);
        edt_missed_mobile.setEnabled(false);
        // btn_generate.setEnabled(false);
        btn_resend.setEnabled(false);
        btn_resend.setVisibility(View.GONE);
        btn_generate.setVisibility(View.GONE);
        lin_generate_verify.setVisibility(View.GONE);
        tv_resetno.setVisibility(View.GONE);
        radiogrp2.setVisibility(View.GONE);

        timerflag = true;


        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }

    private void resetOTPFields() {
        Global.OTPVERIFIED = false;

        by_missed.setChecked(true);
        by_generate.setChecked(false);
        radiogrp2.setVisibility(View.VISIBLE);
        btn_generate.setText(getResources().getString(R.string.enterccc));

        edt_missed_mobile.setEnabled(true);
        edt_missed_mobile.setText("");
        btn_generate.setVisibility(View.VISIBLE);
        btn_generate.setEnabled(true);
        btn_resend.setEnabled(true);
        lin_generate_verify.setVisibility(View.GONE);
        lin_by_missed.setVisibility(View.VISIBLE);

        edt_verifycc.setEnabled(true);
        edt_verifycc.setText("");

        tv_mobileno.setVisibility(View.GONE);
        tv_mobileno.setText("");

        tv_verifiedmob.setText("");
        rel_verify_mobile.setVisibility(View.GONE);

        ll_miscallotp.setVisibility(View.GONE);
        cb_kyc_verification.setVisibility(View.GONE);


        timerflag = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }
    }

    private void generateOtP(String mobileno) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInterface postAPIInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInterface.class);

        COVIDgetotp_req coviDgetotp_req = new COVIDgetotp_req();
        coviDgetotp_req.setApi_key(api_key);
        coviDgetotp_req.setMobile(mobileno);
        coviDgetotp_req.setScode(usercode);

        Call<Covidotpresponse> covidotpresponseCall = postAPIInterface.generateotp(coviDgetotp_req);
        covidotpresponseCall.enqueue(new Callback<Covidotpresponse>() {
            @Override
            public void onResponse(Call<Covidotpresponse> call, retrofit2.Response<Covidotpresponse> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        setCountDownTimernew();
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

            }
        });
    }

    private void setCountDownTimernew() {

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
        covid_validateotp_req.setApi_key(api_key);
        covid_validateotp_req.setMobile(edt_missed_mobile.getText().toString());
        covid_validateotp_req.setOtp(edt_verifycc.getText().toString());
        covid_validateotp_req.setScode(usercode);

        Call<Covid_validateotp_res> covidotpresponseCall = postAPIInterface.validateotp(covid_validateotp_req);
        covidotpresponseCall.enqueue(new Callback<Covid_validateotp_res>() {
            @Override
            public void onResponse(Call<Covid_validateotp_res> call, retrofit2.Response<Covid_validateotp_res> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        disablefields();
                        timerflag = true;
                        Global.OTPVERIFIED = true;
//                        edt_email.setHint("EMAIL-ID*");

                        if (yourCountDownTimer != null) {
                            yourCountDownTimer.cancel();
                            yourCountDownTimer = null;
                        }

                        btn_verifyotp.setText("Verified");
                        btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.green));
                        et_otp.getText().clear();
                        lin_otp.setVisibility(View.GONE);
                        btn_snd_otp.setText("Reset");
                        et_mobno.setEnabled(false);
                        et_mobno.setClickable(false);
                        imgotp = getContext().getResources().getDrawable(R.drawable.otpverify);
                        imgotp.setBounds(40, 40, 40, 40);
                        et_mobno.setCompoundDrawablesWithIntrinsicBounds(null, null, imgotp, null);
                        et_otp.setEnabled(false);
                        et_otp.setClickable(false);
                        btn_snd_otp.setClickable(true);
                        btn_snd_otp.setEnabled(true);
                        btn_verifyotp.setClickable(false);
                        btn_verifyotp.setEnabled(false);
                        tv_timer.setVisibility(View.GONE);
                        lin_ckotp.setVisibility(View.GONE);
                        Enablefields();
                        CallAPIToGetPatientDetails();
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

            }
        });
    }

    private void startDataSetting() {
        brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (brand_spinner.getSelectedItem().toString().equalsIgnoreCase("TTL")) {
                    ll_communication_main.setVisibility(View.VISIBLE);
                    ll_miscallotp.setVisibility(View.GONE);
                    cb_kyc_verification.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListfirst);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectTypeSpinner.setAdapter(adapter2);
                    selectTypeSpinner.setSelection(0);
                    selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
                            setResetCommModes();
                            resetOTPFields();
                            SetAutoCompleteViewAdapterToView(false);  // TODO code to reset patient details selected from Dropdownlist

                            Global.OTPVERIFIED = false;
                            vial_number.getText().clear();
                            id_for_woe.getText().clear();

                            if (selectTypeSpinner.getSelectedItem().equals("ILS")) {
                                Enablefields();
                                try {
                                    if (yourCountDownTimer != null) {
                                        yourCountDownTimer.cancel();
                                        yourCountDownTimer = null;
                                        btn_snd_otp.setEnabled(true);
                                        btn_snd_otp.setClickable(true);
                                    }

//                                    samplecollectionponit.setText("SEARCH SAMPLE COLLECTION POINT");
                                    samplecollectionponit.setHint("SEARCH SAMPLE COLLECTION POINT*");
                                    et_mobno.getText().clear();
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    lin_otp.setVisibility(View.GONE);
                                    tv_timer.setVisibility(View.GONE);
                                    edt_missed_mobile.setHint("Mobile Number");

                                    ll_miscallotp.setVisibility(View.GONE);
                                    cb_kyc_verification.setVisibility(View.VISIBLE);
                                    checkKYCIsChecked();
//
                                    checkDuplicate();
                                    radiogrp2.check(by_sendsms.getId());
                                    ll_email.setVisibility(View.VISIBLE);
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    next_btn.setVisibility(View.VISIBLE);
                                    vial_number.setVisibility(View.VISIBLE);
                                    ll_communication_main.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.GONE);
                                    home_layout.setVisibility(View.GONE);
                                    pincode_linear_data.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    mobile_number_kyc.setVisibility(View.GONE);

                                  /*  if(source_type.equalsIgnoreCase("PUC")){
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                    }else{
                                        mobile_number_kyc.setVisibility(View.GONE);
                                    }*/

                                    labname_linear.setVisibility(View.VISIBLE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    ref_check.setVisibility(View.GONE);

                                    referedbyText.setText("");

                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    id_woe = id_for_woe.getText().toString();
                                    barcode_woe_str = barcode_woe.getText().toString();
                                    referenceBy = "";
//                                            btnClick();
                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");

                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);

                                            } catch (ParseException e) {

                                               /* if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            scpoint = samplecollectionponit.getText().toString();
                                            getLabName = samplecollectionponit.getText().toString();
                                            GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();
                                            leadlayout.setVisibility(View.GONE);
                                           /* if(source_type.equalsIgnoreCase("PUC")){
                                                kycdata = kyc_format.getText().toString();
                                            }else{
                                                kycdata = "";
                                            }*/

                                            btechIDToPass = "";
                                            btechnameTopass = "";
                                            getcampIDtoPass = "";

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {
                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }

//                                            checkSelectMode();
                                            checkDuplicate();

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {

                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } /*else if (mobile_number_kyc.getVisibility() == View.VISIBLE) {
                                                if (!GlobalClass.isNull(kycdata)) {
                                                    if (kycdata.length() < 10) {
                                                        Toast.makeText(mContext, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }*/ else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } */ else if (scpoint.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT") || scpoint.equals("") || scpoint.equals(null)) {
                                                Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                                Toast.makeText(mContext, ToastFile.slt_sample_cll_point, Toast.LENGTH_SHORT).show();
                                            } else {
                                               /* if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        TTL_ILS();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        if (checkSelectMode()) {
                                                            TTL_ILS();
                                                        }
                                                    }
                                                } else {
                                                    if (checkSelectMode()) {
                                                        TTL_ILS();
                                                    }
                                                    ;
                                                }
//                                                }
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (selectTypeSpinner.getSelectedItem().equals("DPS")) {
                                try {
                                    if (getOTPFlag().equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (getOTPFlag().equalsIgnoreCase("YES")) {
                                        // Disablefields();
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
//
                                        checkDuplicate();
                                        radiogrp2.check(by_sendsms.getId());
                                    } else {
                                        GlobalClass.redirectToLogin(activity);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);
                                    vial_number.setVisibility(View.VISIBLE);
                                    ll_email.setVisibility(View.VISIBLE);
                                    ll_communication_main.setVisibility(View.VISIBLE);
                                    labname_linear.setVisibility(View.GONE);
                                    ref_check.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);

                                    referedbyText.setText("");
                                    woereferedby = referedbyText.getText().toString();

                                    refby_linear.setVisibility(View.VISIBLE);
                                    referenceBy = "";


                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
                                    referenceBy = "";
                                    getTSP_AddressStringTopass = getTSP_Address;

                                    kyc_format.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before,
                                                                  int count) {
                                            String enteredString = s.toString();
                                            if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                                                    enteredString.startsWith("#") || enteredString.startsWith("$") ||
                                                    enteredString.startsWith("%") || enteredString.startsWith("^") ||
                                                    enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                                                    || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                                                    || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                                                TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                if (enteredString.length() > 0) {
                                                    kyc_format.setText(enteredString.substring(1));
                                                } else {
                                                    kyc_format.setText("");
                                                }
                                            }
                                        }

                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                                      int after) {
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            final String checkNumber = s.toString();
                                            if (checkNumber.length() < 10) {
                                                flag = true;
                                            }

                                            if (flag == true) {
                                                if (checkNumber.length() == 10) {

                                                    if (!GlobalClass.isNetworkAvailable(activity)) {
                                                        flag = false;
                                                        kyc_format.setText(checkNumber);
                                                    } else {
                                                        flag = false;
                                                        barProgressDialog = new ProgressDialog(mContext);
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);

                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(activity);
                                                        StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + checkNumber, new
                                                                Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.e(TAG, "onResponse: response" + response);

                                                                        String getResponse = response;
                                                                        if (response.equals("\"proceed\"")) {

                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText(checkNumber);
                                                                        } else {
                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText("");
                                                                            TastyToast.makeText(activity, getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                        reques5tQueueCheckNumber.add(jsonObjectRequestPop);
                                                        Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);
                                                    }


                                                }

                                            }
                                        }
                                    });

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");

                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                               /* if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();


                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechname.getSelectedItem() != null)
                                                btechnameTopass = btechname.getSelectedItem().toString();

                                            try {
                                                if (btechnameTopass != null) {
                                                    if (myPojo != null && myPojo.getMASTERS().getBCT_LIST() != null) {
                                                        for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                            if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                                btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                            }
                                                        }
                                                    }

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {
                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            try {
                                                if (woereferedby != null) {
                                                    if (obj != null && obj.getMASTERS() != null && obj.getMASTERS().getREF_DR() != null) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            checkDuplicate();

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } /*else if (!GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                                                if (edt_missed_mobile.getText().toString().length() < 10) {
                                                    Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                                }

                                            }*/ else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {
                                              /*  if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        TTL_DPS();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        if (checkSelectMode()) {
                                                            TTL_DPS();
                                                        }
                                                    }
                                                } else {
                                                    if (checkSelectMode()) {
                                                        TTL_DPS();
                                                    }
                                                }
//                                                }

                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (selectTypeSpinner.getSelectedItem().equals("HOME")) {
                                try {
                                    if (getOTPFlag().equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        Home_mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (getOTPFlag().equalsIgnoreCase("YES")) {
                                        // Disablefields();
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
                                        radiogrp2.check(by_sendsms.getId());
                                    } else {
                                        GlobalClass.redirectToLogin(activity);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    home_layout.setVisibility(View.VISIBLE);
                                    ll_email.setVisibility(View.VISIBLE);

                                    vial_number.setVisibility(View.VISIBLE);
                                    ll_communication_main.setVisibility(View.VISIBLE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.GONE);
                                    patientAddress.setText("");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    referenceBy = "";


                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
//                                             btnClick();
                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                              /*  if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();
                                            // btechnameTopass = btechname.getSelectedItem().toString();

                                            if (btechname.getSelectedItem() != null)
                                                btechnameTopass = btechname.getSelectedItem().toString();


                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = home_kyc_format.getText().toString();
                                            }


                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechnameTopass != null) {
                                                if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }

//                                            checkSelectMode();
                                            checkDuplicate();

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            }/* else if (GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                                                Toast.makeText(mContext, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                                            }*/ else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } /*else if (edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            }*/ else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {
                                            /*    if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        TTL_HOME();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        if (checkSelectMode()) {
                                                            TTL_HOME();
                                                        }
                                                    }
                                                } else {
                                                    if (checkSelectMode()) {
                                                        TTL_HOME();
                                                    }
                                                }
//                                                }


                                            }

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (selectTypeSpinner.getSelectedItem().equals("CAMP")) {
                                Enablefields();
                                try {
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    pincode_linear_data.setVisibility(View.GONE);
                                    ll_miscallotp.setVisibility(View.GONE);
                                    cb_kyc_verification.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.GONE);
                                    vial_number.setVisibility(View.VISIBLE);
                                    ref_check.setVisibility(View.GONE);
                                    ll_email.setVisibility(View.VISIBLE);
                                    labname_linear.setVisibility(View.GONE);
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    camp_layout_woe.setVisibility(View.VISIBLE);
                                    btech_linear_layout.setVisibility(View.GONE);
                                    home_layout.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.GONE);
                                    refby_linear.setVisibility(View.GONE);
                                    referedbyText.setText("");
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);

                                    patientAddress.setText("");
                                    referenceBy = "";

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                              /*  if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();
                                            if (typename.equals("CAMP")) {

                                                ageString = age.getText().toString();
                                                woereferedby = referedbyText.getText().toString();
                                                GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                // scpoint = camp_spinner_olc.getSelectedItem().toString();
                                                if (camp_spinner_olc.getSelectedItem().toString() != null) {
                                                    scpoint = camp_spinner_olc.getSelectedItem().toString();
                                                }
                                                kycdata = kyc_format.getText().toString();

                                                kycdata = "";
                                                labIDTopass = "";
                                                btechIDToPass = "";
                                                btechnameTopass = "";
                                                labAddressTopass = "";

                                                if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                    if (referenceBy == null) {
                                                        Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        if (referenceBy.equalsIgnoreCase("SELF")) {
                                                            referenceBy = "SELF";
                                                            referredID = "";
                                                            woereferedby = referenceBy;
                                                        } else {

                                                            referenceBy = referedbyText.getText().toString();
                                                        }
                                                    }

                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                if (woereferedby != null) {
                                                    if (obj != null) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                if (scpoint != null) {
                                                    if (myPojo.getMASTERS().getCAMP_LIST() != null) {
                                                        for (int k = 0; k < myPojo.getMASTERS().getCAMP_LIST().length; k++) {
                                                            if (scpoint.equals(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE())) {
                                                                getcampIDtoPass = myPojo.getMASTERS().getCAMP_LIST()[k].getCAMP_ID();
                                                            }
                                                        }
                                                    }
                                                }

                                                if (!ageString.equals("")) {
                                                    conertage = Integer.parseInt(ageString);
                                                }

                                                if (getVial_numbver.equals("")) {
                                                    vial_number.setError(ToastFile.vial_no);
                                                    Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                                } else if (nameString.equals("")) {
                                                    name.setError(ToastFile.crt_name);
                                                    Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                                } else if (nameString.length() < 2) {
                                                    name.setError(ToastFile.crt_name_woe);
                                                    Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                                } else if (ageString.equals("")) {
                                                    age.setError(ToastFile.ent_age);
                                                    Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                                } else if (saveGenderId == null || saveGenderId == "") {
                                                    Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                                } else if (conertage > 120) {
                                                    Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                                } else if (sctHr.equals("HR*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                                } else if (sctMin.equals("MIN*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                                } /*else if (sctSEc.equals("AM/PM")) {
                                                    Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                                }*/ else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                                } else if (scpoint.equals("Select Camp")) {
                                                    Toast.makeText(activity, "Please select camp name", Toast.LENGTH_SHORT).show();
                                                } else if (dCompare.after(getCurrentDateandTime) && getCurrentDateandTime != null) {
                                                    Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                                } else {

                                                    if (woereferedby != null) {
                                                        if (obj != null) {
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                    referenceBy = woereferedby;
                                                                    referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        referenceBy = woereferedby;
                                                        referredID = "";
                                                    }

                                                    final String getAgeType = spinyr.getSelectedItem().toString();
                                                    String sctDate = dateShow.getText().toString();
                                                    sctHr = timehr.getSelectedItem().toString();
                                                    sctMin = timesecond.getSelectedItem().toString();
                                                    sctSEc = timeampm.getSelectedItem().toString();
                                                    final String getFinalAge = age.getText().toString();
                                                    final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                    final String getFinalDate = dateShow.getText().toString();

                                                    //TODO Commented as per the input given on 14-03-2022
                                                    /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
                                                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                                                .setContentText("You can register the PGC to avoid 10 Rs debit")
                                                                .setConfirmText("Ok")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                                        i.putExtra("name", nameString);
                                                                        i.putExtra("age", getFinalAge);
                                                                        i.putExtra("gender", saveGenderId);
                                                                        i.putExtra("sct", getFinalTime);
                                                                        i.putExtra("date", getFinalDate);
                                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                                        startActivity(i);
                                                                        Log.e(TAG, "onClick: lab add and lab id " + scpoint + labIDTopass);
                                                                        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                        saveDetails.putString("name", nameString);
                                                                        saveDetails.putString("age", getFinalAge);
                                                                        saveDetails.putString("gender", saveGenderId);
                                                                        saveDetails.putString("sct", getFinalTime);
                                                                        saveDetails.putString("date", getFinalDate);
                                                                        saveDetails.putString("ageType", getAgeType);
                                                                        saveDetails.putString("labname", "");
                                                                        saveDetails.putString("labAddress", scpoint);
                                                                        saveDetails.putString("patientAddress", scpoint);
                                                                        saveDetails.putString("refBy", referenceBy);
                                                                        saveDetails.putString("refId", referredID);
                                                                        saveDetails.putString("labIDaddress", labIDTopass);
                                                                        saveDetails.putString("btechIDToPass", btechIDToPass);
                                                                        saveDetails.putString("btechNameToPass", btechnameTopass);
                                                                        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                                        saveDetails.putString("kycinfo", kycdata);
                                                                        saveDetails.putString("WOEbrand", brandNames);
                                                                        saveDetails.putString("woetype", typename);
                                                                        saveDetails.putString("SR_NO", getVial_numbver);
                                                                        saveDetails.putString("pincode", "");
                                                                        saveDetails.putString("Communication", Global.Communication);
                                                                        saveDetails.putString("CommModes", Global.CommModes);
                                                                        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                                        saveDetails.commit();
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {*/
                                                    Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                    i.putExtra("name", nameString);
                                                    i.putExtra("age", getFinalAge);
                                                    i.putExtra("gender", saveGenderId);
                                                    i.putExtra("sct", getFinalTime);
                                                    i.putExtra("date", getFinalDate);
                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                    startActivity(i);
                                                    Log.e(TAG, "onClick: lab add and lab id " + scpoint + labIDTopass);
                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                    saveDetails.putString("name", nameString);
                                                    saveDetails.putString("age", getFinalAge);
                                                    saveDetails.putString("gender", saveGenderId);
                                                    saveDetails.putString("sct", getFinalTime);
                                                    saveDetails.putString("date", getFinalDate);
                                                    saveDetails.putString("ageType", getAgeType);
                                                    saveDetails.putString("labname", "");
                                                    saveDetails.putString("labAddress", scpoint);
                                                    saveDetails.putString("patientAddress", scpoint);
                                                    saveDetails.putString("refBy", referenceBy);
                                                    saveDetails.putString("refId", referredID);
                                                    saveDetails.putString("labIDaddress", labIDTopass);
                                                    saveDetails.putString("btechIDToPass", btechIDToPass);
                                                    saveDetails.putString("btechNameToPass", btechnameTopass);
                                                    saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                    saveDetails.putString("kycinfo", kycdata);
                                                    saveDetails.putString("WOEbrand", brandNames);
                                                    saveDetails.putString("woetype", typename);
                                                    saveDetails.putString("SR_NO", getVial_numbver);
                                                    saveDetails.putString("pincode", "");
                                                    saveDetails.putString("Communication", Global.Communication);
                                                    saveDetails.putString("CommModes", Global.CommModes);
                                                    saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                    saveDetails.commit();
//                                                    }

                                                }
                                            }

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else if (selectTypeSpinner.getSelectedItem().equals("ORDERS")) {
                                LeadIdwoe();
                            } else if (selectTypeSpinner.getSelectedItem().equals("ADD")) {
                                barcode_woe.setText("");
                                leadbarcodelayout.setVisibility(View.GONE);
                                ll_mobileno_otp.setVisibility(View.GONE);
                                tv_mob_note.setVisibility(View.GONE);
                                id_layout.setVisibility(View.GONE);
                                pincode_linear_data.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.VISIBLE);
                                ll_miscallotp.setVisibility(View.GONE);
                                cb_kyc_verification.setVisibility(View.GONE);
                                vial_number.setVisibility(View.GONE);
                                ll_communication_main.setVisibility(View.GONE);
                                camp_layout_woe.setVisibility(View.GONE);
                                ll_email.setVisibility(View.GONE);
                                btech_linear_layout.setVisibility(View.GONE);
                                home_layout.setVisibility(View.GONE);
                                labname_linear.setVisibility(View.GONE);
                                patientAddress.setText("");
                                mobile_number_kyc.setVisibility(View.GONE);
                                Home_mobile_number_kyc.setVisibility(View.GONE);
                                leadlayout.setVisibility(View.GONE);
                                brand_string = brand_spinner.getSelectedItem().toString();
                                type_string = selectTypeSpinner.getSelectedItem().toString();
                                ref_check_linear.setVisibility(View.GONE);
                                refby_linear.setVisibility(View.GONE);
                                namePatients.setVisibility(View.GONE);
                                AGE_layout.setVisibility(View.GONE);
                                time_layout.setVisibility(View.GONE);
                                next_btn.setVisibility(View.GONE);

                                barcode_woe.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (s.length() < 8) {
                                            leadbarcodelayout.setVisibility(View.GONE);
                                        }
                                        if (s.length() == 8) {
                                            String passBarcode = s.toString();
                                            if (!GlobalClass.isNetworkAvailable(activity)) {
                                                GlobalClass.showAlertDialog(activity);
                                            } else {
                                                RecheckType(passBarcode);
                                            }
                                        }
                                    }
                                });
                                referenceBy = "";

                                next_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (type_string.equals("")) {
                                            Toast.makeText(activity, "Please select type name", Toast.LENGTH_SHORT).show();
                                        } else if (barcode_woe.getText().toString().equals("")) {
                                            Toast.makeText(activity, ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
                                        } else {
                                            GlobalClass.branditem = brand_spinner.getSelectedItem().toString();
                                            GlobalClass.typeItem = selectTypeSpinner.getSelectedItem().toString();
                                            GlobalClass.id_value = id_for_woe.getText().toString();

                                            SharedPreferences.Editor savePrefe = activity.getSharedPreferences("AddTestType", 0).edit();
                                            savePrefe.putString("ALERT", ALERT);
                                            savePrefe.putString("BARCODE", BARCODE);
                                            savePrefe.putString("BVT_HRS", BVT_HRS);
                                            savePrefe.putString("LABCODE", LABCODE);
                                            savePrefe.putString("PATIENT", PATIENT);
                                            savePrefe.putString("REF_DR", REF_DR);
                                            savePrefe.putString("REQUESTED_ADDITIONAL_TESTS", REQUESTED_ADDITIONAL_TESTS);
                                            savePrefe.putString("REQUESTED_ON", REQUESTED_ON);
                                            savePrefe.putString("RES_ID", RES_ID);
                                            savePrefe.putString("SDATE", SDATE);
                                            savePrefe.putString("SL_NO", SL_NO);
                                            savePrefe.putString("STATUS", STATUS);
                                            savePrefe.putString("SU_CODE1", SU_CODE1);
                                            savePrefe.putString("SU_CODE2", SU_CODE2);
                                            savePrefe.putString("TESTS", TESTS);
                                            savePrefe.commit();

                                            Intent a = new Intent(activity, AddWOETestsForSerum.class);
                                            startActivity(a);

                                        }
                                    }
                                });
                            } else if (selectTypeSpinner.getSelectedItem().equals("RECHECK")) {
                                barcode_woe.setText("");
                                ll_mobileno_otp.setVisibility(View.GONE);
                                tv_mob_note.setVisibility(View.GONE);
                                leadbarcodelayout.setVisibility(View.GONE);
                                id_layout.setVisibility(View.GONE);
                                pincode_linear_data.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.VISIBLE);
                                vial_number.setVisibility(View.GONE);
                                ll_communication_main.setVisibility(View.GONE);
                                camp_layout_woe.setVisibility(View.GONE);
                                btech_linear_layout.setVisibility(View.GONE);
                                home_layout.setVisibility(View.GONE);
                                ll_miscallotp.setVisibility(View.GONE);
                                cb_kyc_verification.setVisibility(View.GONE);
                                labname_linear.setVisibility(View.GONE);
                                patientAddress.setText("");
                                ll_email.setVisibility(View.GONE);
                                mobile_number_kyc.setVisibility(View.GONE);
                                Home_mobile_number_kyc.setVisibility(View.GONE);
                                ref_check_linear.setVisibility(View.GONE);
                                refby_linear.setVisibility(View.GONE);
                                namePatients.setVisibility(View.GONE);
                                AGE_layout.setVisibility(View.GONE);

                                time_layout.setVisibility(View.GONE);
                                leadlayout.setVisibility(View.GONE);
                                brand_string = brand_spinner.getSelectedItem().toString();
                                type_string = selectTypeSpinner.getSelectedItem().toString();


                                next_btn.setVisibility(View.GONE);
                                barcode_woe.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (s.length() < 8) {
                                            leadbarcodelayout.setVisibility(View.GONE);
                                        }
                                        if (s.length() == 8) {
                                            if (!GlobalClass.isNetworkAvailable(activity)) {
                                                GlobalClass.showAlertDialog(activity);
                                            } else {
                                                String passBarcode = s.toString();
                                                RecheckType(passBarcode);
                                            }
                                        }
                                    }
                                });
                                referenceBy = "";

                                next_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (brand_string.equals("Select brand name")) {
                                            Toast.makeText(activity, "Please select brand name", Toast.LENGTH_SHORT).show();
                                        } else if (type_string.equals("")) {
                                            Toast.makeText(activity, "Please select type name", Toast.LENGTH_SHORT).show();
                                        } else if (barcode_woe.getText().toString().equals("")) {
                                            Toast.makeText(activity, ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
                                        } else {
                                            SharedPreferences.Editor savePrefe = activity.getSharedPreferences("RecheckTestType", 0).edit();
                                            savePrefe.putString("ALERT", ALERT);
                                            savePrefe.putString("BARCODE", BARCODE);
                                            savePrefe.putString("BVT_HRS", BVT_HRS);
                                            savePrefe.putString("LABCODE", LABCODE);
                                            savePrefe.putString("PATIENT", PATIENT);
                                            savePrefe.putString("REF_DR", REF_DR);
                                            savePrefe.putString("REQUESTED_ADDITIONAL_TESTS", REQUESTED_ADDITIONAL_TESTS);
                                            savePrefe.putString("REQUESTED_ON", REQUESTED_ON);
                                            savePrefe.putString("RES_ID", RES_ID);
                                            savePrefe.putString("SDATE", SDATE);
                                            savePrefe.putString("SL_NO", SL_NO);
                                            savePrefe.putString("STATUS", STATUS);
                                            savePrefe.putString("SU_CODE1", SU_CODE1);
                                            savePrefe.putString("SU_CODE2", SU_CODE2);
                                            savePrefe.putString("TESTS", TESTS);

                                            savePrefe.commit();
                                            GlobalClass.branditem = brand_spinner.getSelectedItem().toString();
                                            GlobalClass.typeItem = selectTypeSpinner.getSelectedItem().toString();
                                            Intent a = new Intent(activity, RecheckAllTest.class);
                                            startActivity(a);

                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (brand_spinner.getSelectedItem().toString().equalsIgnoreCase("NHL")) {
                    ll_communication_main.setVisibility(View.GONE);
                    {
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListfirst);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);
                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
                                setResetCommModes();
                                resetOTPFields();
                                SetAutoCompleteViewAdapterToView(false);  // TODO code to reset patient details selected from Dropdownlist

                                Global.OTPVERIFIED = false;
                                vial_number.getText().clear();
                                id_for_woe.getText().clear();

                                if (selectTypeSpinner.getSelectedItem().equals("ILS")) {
                                    Enablefields();
                                    try {
                                        if (yourCountDownTimer != null) {
                                            yourCountDownTimer.cancel();
                                            yourCountDownTimer = null;
                                            btn_snd_otp.setEnabled(true);
                                            btn_snd_otp.setClickable(true);
                                        }

//                                        samplecollectionponit.setText("SEARCH SAMPLE COLLECTION POINT");
                                        samplecollectionponit.setHint("SEARCH SAMPLE COLLECTION POINT*");
                                        et_mobno.getText().clear();
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                        et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        lin_otp.setVisibility(View.GONE);
                                        tv_timer.setVisibility(View.GONE);
                                        edt_missed_mobile.setHint("Mobile Number");

//                                        ll_miscallotp.setVisibility(View.VISIBLE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
                                        radiogrp2.check(by_sendsms.getId());
                                        ll_email.setVisibility(View.VISIBLE);
                                        leadlayout.setVisibility(View.GONE);
                                        id_layout.setVisibility(View.GONE);
                                        barcode_layout.setVisibility(View.GONE);
                                        leadlayout.setVisibility(View.GONE);
                                        next_btn.setVisibility(View.VISIBLE);
                                        vial_number.setVisibility(View.VISIBLE);
                                        camp_layout_woe.setVisibility(View.GONE);
                                        btech_linear_layout.setVisibility(View.GONE);
                                        home_layout.setVisibility(View.GONE);
                                        pincode_linear_data.setVisibility(View.GONE);
                                        leadbarcodelayout.setVisibility(View.GONE);
                                        mobile_number_kyc.setVisibility(View.GONE);

                                  /*  if(source_type.equalsIgnoreCase("PUC")){
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                    }else{
                                        mobile_number_kyc.setVisibility(View.GONE);
                                    }*/

                                        labname_linear.setVisibility(View.VISIBLE);
                                        Home_mobile_number_kyc.setVisibility(View.GONE);
                                        namePatients.setVisibility(View.VISIBLE);
                                        AGE_layout.setVisibility(View.VISIBLE);
                                        time_layout.setVisibility(View.VISIBLE);
                                        refby_linear.setVisibility(View.VISIBLE);
                                        ref_check_linear.setVisibility(View.VISIBLE);
                                        uncheck_ref.setVisibility(View.VISIBLE);
                                        ref_check.setVisibility(View.GONE);

                                        referedbyText.setText("");

                                        brand_string = brand_spinner.getSelectedItem().toString();
                                        type_string = selectTypeSpinner.getSelectedItem().toString();
                                        id_woe = id_for_woe.getText().toString();
                                        barcode_woe_str = barcode_woe.getText().toString();
                                        referenceBy = "";
//                                            btnClick();
                                        next_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                nameString = name.getText().toString();
                                                nameString = nameString.replaceAll("\\s+", " ");

                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                getFinalTime = sctHr + ":" + sctMin;
                                                getFinalDate = dateShow.getText().toString();

                                                String getDateToCompare = getFinalDate + " " + getFinalTime;

                                                SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                                try {
                                                    dCompare = sdfform.parse(getDateToCompare);

                                                } catch (ParseException e) {

                                               /* if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                    String input = getDateToCompare;
                                                    //Format of the date defined in the input String
                                                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                    //Desired format: 24 hour format: Change the pattern as per the need
                                                    DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                    Date date = null;
                                                    String output = null;
                                                    try {
                                                        dCompare = df.parse(input);
                                                        output = outputformat.format(dCompare);
                                                        System.out.println(output);
                                                    } catch (ParseException pe) {
                                                        pe.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }

                                                typename = selectTypeSpinner.getSelectedItem().toString();
                                                brandNames = brand_spinner.getSelectedItem().toString();
                                                getVial_numbver = vial_number.getText().toString();

                                                ageString = age.getText().toString();
                                                woereferedby = referedbyText.getText().toString();
                                                GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                scpoint = samplecollectionponit.getText().toString();
                                                getLabName = samplecollectionponit.getText().toString();
                                                GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();
                                                leadlayout.setVisibility(View.GONE);
                                           /* if(source_type.equalsIgnoreCase("PUC")){
                                                kycdata = kyc_format.getText().toString();
                                            }else{
                                                kycdata = "";
                                            }*/

                                                btechIDToPass = "";
                                                btechnameTopass = "";
                                                getcampIDtoPass = "";

                                                if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                    if (referenceBy == null) {
                                                        Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        if (referenceBy.equalsIgnoreCase("SELF")) {
                                                            referenceBy = "SELF";
                                                            referredID = "";
                                                            woereferedby = referenceBy;
                                                        } else {
                                                            referenceBy = referedbyText.getText().toString();
                                                        }
                                                    }

                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                if (woereferedby != null) {
                                                    if (obj != null) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                if (!ageString.equals("")) {
                                                    conertage = Integer.parseInt(ageString);
                                                }

                                                checkDuplicate();

                                                if (getVial_numbver.equals("")) {
                                                    vial_number.setError(ToastFile.vial_no);
                                                    Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                                } else if (nameString.equals("")) {
                                                    name.setError(ToastFile.crt_name);
                                                    Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                                } else if (nameString.length() < 2) {
                                                    name.setError(ToastFile.crt_name_woe);
                                                    Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                                } else if (ageString.equals("")) {
                                                    age.setError(ToastFile.ent_age);
                                                    Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                                } else if (saveGenderId == null || saveGenderId == "") {
                                                    Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                                } else if (conertage > 120) {
                                                    Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                                }/*else if (mobile_number_kyc.getVisibility() == View.VISIBLE){
                                              if(!GlobalClass.isNull(kycdata)){
                                                  if(kycdata.length()<10){
                                                      Toast.makeText(mContext, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                            }*/ else if (sctHr.equals("HR*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                                } else if (sctMin.equals("MIN*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                                }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } */ else if (scpoint.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT") || scpoint.equals("") || scpoint.equals(null)) {
                                                    Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                                } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                                } else if (dCompare.after(getCurrentDateandTime)) {
                                                    Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                                } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                                    Toast.makeText(mContext, ToastFile.slt_sample_cll_point, Toast.LENGTH_SHORT).show();
                                                } else {
                                                 /*   if (Global.OTPVERIFIED) {
                                                        if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                            GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                            edt_email.requestFocus();
                                                        } else {
                                                            TTL_ILS();
                                                        }
                                                    } else {*/
                                                    if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                        if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                            GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                            edt_email.requestFocus();
                                                        } else {
                                                            TTL_ILS();
                                                        }
                                                    } else {
                                                        TTL_ILS();
                                                    }
//                                                    }
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (selectTypeSpinner.getSelectedItem().equals("DPS")) {
                                    try {
                                        if (getOTPFlag().equalsIgnoreCase("NO")) {
                                            Enablefields();
                                            mobile_number_kyc.setVisibility(View.VISIBLE);
                                            ll_mobileno_otp.setVisibility(View.GONE);
                                            ll_miscallotp.setVisibility(View.GONE);
                                            cb_kyc_verification.setVisibility(View.GONE);
                                            tv_mob_note.setVisibility(View.GONE);
                                        } else if (getOTPFlag().equalsIgnoreCase("YES")) {
                                            // Disablefields();
//                                            ll_miscallotp.setVisibility(View.VISIBLE);
                                            cb_kyc_verification.setVisibility(View.VISIBLE);
                                            checkKYCIsChecked();
                                            radiogrp2.check(by_sendsms.getId());
                                        } else {
                                            GlobalClass.redirectToLogin(activity);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        leadlayout.setVisibility(View.GONE);
                                        id_layout.setVisibility(View.GONE);
                                        barcode_layout.setVisibility(View.GONE);
                                        leadbarcodelayout.setVisibility(View.GONE);
                                        leadlayout.setVisibility(View.GONE);
                                        brand_string = brand_spinner.getSelectedItem().toString();
                                        type_string = selectTypeSpinner.getSelectedItem().toString();
                                        next_btn.setVisibility(View.VISIBLE);
//                                        edt_missed_mobile.setHint("Mobile Number*");
                                        camp_layout_woe.setVisibility(View.GONE);
                                        btech_linear_layout.setVisibility(View.VISIBLE);
                                        pincode_linear_data.setVisibility(View.VISIBLE);
                                        home_layout.setVisibility(View.VISIBLE);
                                        vial_number.setVisibility(View.VISIBLE);
                                        ll_email.setVisibility(View.VISIBLE);

                                        labname_linear.setVisibility(View.GONE);
                                        ref_check.setVisibility(View.GONE);
                                        Home_mobile_number_kyc.setVisibility(View.GONE);
                                        ref_check_linear.setVisibility(View.VISIBLE);
                                        uncheck_ref.setVisibility(View.VISIBLE);

                                        referedbyText.setText("");
                                        woereferedby = referedbyText.getText().toString();

                                        refby_linear.setVisibility(View.VISIBLE);
                                        referenceBy = "";


                                        namePatients.setVisibility(View.VISIBLE);
                                        AGE_layout.setVisibility(View.VISIBLE);
                                        time_layout.setVisibility(View.VISIBLE);
                                        referenceBy = "";
                                        getTSP_AddressStringTopass = getTSP_Address;

                                        kyc_format.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before,
                                                                      int count) {
                                                String enteredString = s.toString();
                                                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                                                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                                                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                                                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                                                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                                                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                                                    TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                    if (enteredString.length() > 0) {
                                                        kyc_format.setText(enteredString.substring(1));
                                                    } else {
                                                        kyc_format.setText("");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count,
                                                                          int after) {
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                final String checkNumber = s.toString();
                                                if (checkNumber.length() < 10) {
                                                    flag = true;
                                                }

                                                if (flag == true) {
                                                    if (checkNumber.length() == 10) {

                                                        if (!GlobalClass.isNetworkAvailable(activity)) {
                                                            flag = false;
                                                            kyc_format.setText(checkNumber);
                                                        } else {
                                                            flag = false;
                                                            barProgressDialog = new ProgressDialog(mContext);
                                                            barProgressDialog.setTitle("Kindly wait ...");
                                                            barProgressDialog.setMessage(ToastFile.processing_request);
                                                            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                            barProgressDialog.setProgress(0);
                                                            barProgressDialog.setMax(20);
                                                            barProgressDialog.show();
                                                            barProgressDialog.setCanceledOnTouchOutside(false);
                                                            barProgressDialog.setCancelable(false);

                                                            RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(activity);
                                                            StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + checkNumber, new
                                                                    Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            Log.e(TAG, "onResponse: response" + response);

                                                                            String getResponse = response;
                                                                            if (response.equals("\"proceed\"")) {

                                                                                GlobalClass.hideProgress(activity, barProgressDialog);
                                                                                kyc_format.setText(checkNumber);
                                                                            } else {
                                                                                GlobalClass.hideProgress(activity, barProgressDialog);
                                                                                kyc_format.setText("");
                                                                                TastyToast.makeText(activity, getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                            reques5tQueueCheckNumber.add(jsonObjectRequestPop);
                                                            Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);
                                                        }


                                                    }

                                                }
                                            }
                                        });

                                        next_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                nameString = name.getText().toString();
                                                nameString = nameString.replaceAll("\\s+", " ");

                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                getFinalTime = sctHr + ":" + sctMin;
                                                getFinalDate = dateShow.getText().toString();

                                                String getDateToCompare = getFinalDate + " " + getFinalTime;

                                                SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                try {
                                                    dCompare = sdfform.parse(getDateToCompare);
                                                } catch (ParseException e) {
                                               /* if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                    String input = getDateToCompare;
                                                    //Format of the date defined in the input String
                                                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                    //Desired format: 24 hour format: Change the pattern as per the need
                                                    DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                    Date date = null;
                                                    String output = null;
                                                    try {
                                                        dCompare = df.parse(input);
                                                        output = outputformat.format(dCompare);
                                                        System.out.println(output);
                                                    } catch (ParseException pe) {
                                                        pe.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }

                                                typename = selectTypeSpinner.getSelectedItem().toString();
                                                brandNames = brand_spinner.getSelectedItem().toString();
                                                getVial_numbver = vial_number.getText().toString();
                                                brandNames = brand_spinner.getSelectedItem().toString();

                                                ageString = age.getText().toString();
                                                woereferedby = referedbyText.getText().toString();
                                                GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                patientAddressdataToPass = patientAddress.getText().toString();
                                                pincode_pass = pincode_edt.getText().toString();


                                                if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                    kycdata = edt_missed_mobile.getText().toString();
                                                } else {
                                                    kycdata = kyc_format.getText().toString();
                                                }

                                                labAddressTopass = "";
                                                labIDTopass = "";
                                                getcampIDtoPass = "";

                                                if (btechname.getSelectedItem() != null)
                                                    btechnameTopass = btechname.getSelectedItem().toString();

                                                try {
                                                    if (btechnameTopass != null) {
                                                        if (myPojo != null && myPojo.getMASTERS().getBCT_LIST() != null) {
                                                            for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                                if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                                    btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                                }
                                                            }
                                                        }

                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                    if (referenceBy == null) {
                                                        Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        if (referenceBy.equalsIgnoreCase("SELF")) {
                                                            referenceBy = "SELF";
                                                            referredID = "";
                                                            woereferedby = referenceBy;
                                                        } else {
                                                            referenceBy = referedbyText.getText().toString();
                                                        }
                                                    }

                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                try {
                                                    if (woereferedby != null) {
                                                        if (obj != null && obj.getMASTERS() != null && obj.getMASTERS().getREF_DR() != null) {
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                    referenceBy = woereferedby;
                                                                    referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        referenceBy = woereferedby;
                                                        referredID = "";
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                if (!ageString.equals("")) {
                                                    conertage = Integer.parseInt(ageString);
                                                }

                                                if (getVial_numbver.equals("")) {
                                                    vial_number.setError(ToastFile.vial_no);
                                                    Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                                }/* else if (GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                                                    Toast.makeText(mContext, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                                                } */ else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                        edt_missed_mobile.getText().toString().startsWith("1") ||
                                                        edt_missed_mobile.getText().toString().startsWith("2") ||
                                                        edt_missed_mobile.getText().toString().startsWith("3") ||
                                                        edt_missed_mobile.getText().toString().startsWith("4") ||
                                                        edt_missed_mobile.getText().toString().startsWith("5")) {
                                                    Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                                } /*else if (edt_missed_mobile.getText().toString().length() < 10) {
                                                    Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                                } */ else if (nameString.equals("")) {
                                                    name.setError(ToastFile.crt_name);
                                                    Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                                } else if (nameString.length() < 2) {
                                                    name.setError(ToastFile.crt_name_woe);
                                                    Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                                } else if (ageString.equals("")) {
                                                    age.setError(ToastFile.ent_age);
                                                    Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                                } else if (conertage > 120) {
                                                    Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                                } else if (saveGenderId == null || saveGenderId == "") {
                                                    Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                                } else if (sctHr.equals("HR*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                                } else if (sctMin.equals("MIN*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                                } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                    Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                                } else if (patientAddressdataToPass.equals("")) {
                                                    Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                                } else if (patientAddressdataToPass.length() < 25) {
                                                    Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                                } else if (pincode_pass.equalsIgnoreCase("")) {
                                                    Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                                } else if (pincode_pass.length() < 6) {
                                                    Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                                } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                    Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                                } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    /*if (Global.OTPVERIFIED) {
                                                        if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                            GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                            edt_email.requestFocus();
                                                        } else {
                                                            TTL_DPS();
                                                        }
                                                    } else {*/
                                                    if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                        if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                            GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                            edt_email.requestFocus();
                                                        } else {
                                                            TTL_DPS();
                                                        }
                                                    } else {
                                                        TTL_DPS();
                                                    }
//                                                    }

                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (selectTypeSpinner.getSelectedItem().equals("HOME")) {
                                    try {
                                        if (getOTPFlag().equalsIgnoreCase("NO")) {
                                            Enablefields();
                                            Home_mobile_number_kyc.setVisibility(View.VISIBLE);
                                            ll_mobileno_otp.setVisibility(View.GONE);
                                            ll_miscallotp.setVisibility(View.GONE);
                                            cb_kyc_verification.setVisibility(View.GONE);
                                            tv_mob_note.setVisibility(View.GONE);
                                        } else if (getOTPFlag().equalsIgnoreCase("YES")) {
                                            // Disablefields();
//                                            ll_miscallotp.setVisibility(View.VISIBLE);
                                            cb_kyc_verification.setVisibility(View.VISIBLE);
                                            checkKYCIsChecked();
                                            radiogrp2.check(by_sendsms.getId());
                                        } else {
                                            GlobalClass.redirectToLogin(activity);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        leadlayout.setVisibility(View.GONE);
                                        id_layout.setVisibility(View.GONE);
                                        barcode_layout.setVisibility(View.GONE);
                                        leadlayout.setVisibility(View.GONE);
                                        leadbarcodelayout.setVisibility(View.GONE);
                                        brand_string = brand_spinner.getSelectedItem().toString();
                                        type_string = selectTypeSpinner.getSelectedItem().toString();
                                        next_btn.setVisibility(View.VISIBLE);
                                        camp_layout_woe.setVisibility(View.GONE);
                                        btech_linear_layout.setVisibility(View.VISIBLE);
                                        pincode_linear_data.setVisibility(View.VISIBLE);
//                                        edt_missed_mobile.setHint("Mobile Number*");
                                        home_layout.setVisibility(View.VISIBLE);
                                        ll_email.setVisibility(View.VISIBLE);

                                        vial_number.setVisibility(View.VISIBLE);
                                        mobile_number_kyc.setVisibility(View.GONE);
                                        labname_linear.setVisibility(View.GONE);
                                        patientAddress.setText("");
                                        ref_check.setVisibility(View.GONE);
                                        ref_check_linear.setVisibility(View.VISIBLE);
                                        uncheck_ref.setVisibility(View.VISIBLE);
                                        refby_linear.setVisibility(View.VISIBLE);
                                        referedbyText.setText("");
                                        referenceBy = "";


                                        namePatients.setVisibility(View.VISIBLE);
                                        AGE_layout.setVisibility(View.VISIBLE);
                                        time_layout.setVisibility(View.VISIBLE);
//                                             btnClick();
                                        next_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                nameString = name.getText().toString();
                                                nameString = nameString.replaceAll("\\s+", " ");
                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                getFinalTime = sctHr + ":" + sctMin;
                                                getFinalDate = dateShow.getText().toString();

                                                String getDateToCompare = getFinalDate + " " + getFinalTime;

                                                SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                                                try {
                                                    dCompare = sdfform.parse(getDateToCompare);
                                                } catch (ParseException e) {
                                              /*  if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                    String input = getDateToCompare;
                                                    //Format of the date defined in the input String
                                                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                    //Desired format: 24 hour format: Change the pattern as per the need
                                                    DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                    Date date = null;
                                                    String output = null;
                                                    try {
                                                        dCompare = df.parse(input);
                                                        output = outputformat.format(dCompare);
                                                        System.out.println(output);
                                                    } catch (ParseException pe) {
                                                        pe.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }

                                                typename = selectTypeSpinner.getSelectedItem().toString();
                                                brandNames = brand_spinner.getSelectedItem().toString();
                                                getVial_numbver = vial_number.getText().toString();

                                                brandNames = brand_spinner.getSelectedItem().toString();

                                                ageString = age.getText().toString();
                                                woereferedby = referedbyText.getText().toString();
                                                GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                patientAddressdataToPass = patientAddress.getText().toString();
                                                pincode_pass = pincode_edt.getText().toString();
                                                // btechnameTopass = btechname.getSelectedItem().toString();

                                                if (btechname.getSelectedItem() != null)
                                                    btechnameTopass = btechname.getSelectedItem().toString();


                                                if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                    kycdata = edt_missed_mobile.getText().toString();
                                                } else {
                                                    kycdata = home_kyc_format.getText().toString();
                                                }


                                                labIDTopass = "";
                                                labAddressTopass = "";
                                                getcampIDtoPass = "";

                                                if (btechnameTopass != null) {
                                                    if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                        for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                            if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                                btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                            }
                                                        }
                                                    }

                                                }

                                                if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                    if (referenceBy == null || referenceBy.length() <= 1) {
                                                        Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        if (referenceBy.equalsIgnoreCase("SELF")) {
                                                            referenceBy = "SELF";
                                                            referredID = "";
                                                            woereferedby = referenceBy;
                                                        } else {

                                                            referenceBy = referedbyText.getText().toString();
                                                        }
                                                    }

                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                if (woereferedby != null) {
                                                    if (obj != null) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }


                                                if (!ageString.equals("")) {
                                                    conertage = Integer.parseInt(ageString);
                                                }
                                                checkSelectMode();
                                                checkDuplicate();

                                                if (getVial_numbver.equals("")) {
                                                    vial_number.setError(ToastFile.vial_no);
                                                    Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                                } /*else if (GlobalClass.isNull(edt_missed_mobile.getText().toString())) {
                                                    Toast.makeText(mContext, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                                                }*/ else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                        edt_missed_mobile.getText().toString().startsWith("1") ||
                                                        edt_missed_mobile.getText().toString().startsWith("2") ||
                                                        edt_missed_mobile.getText().toString().startsWith("3") ||
                                                        edt_missed_mobile.getText().toString().startsWith("4") ||
                                                        edt_missed_mobile.getText().toString().startsWith("5")) {
                                                    Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                                } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                    Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                                }/* else if (edt_missed_mobile.getText().toString().length() < 10) {
                                                    Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                                }*/ else if (nameString.equals("")) {
                                                    name.setError(ToastFile.crt_name);
                                                    Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                                } else if (nameString.length() < 2) {
                                                    name.setError(ToastFile.crt_name_woe);
                                                    Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                                } else if (ageString.equals("")) {
                                                    age.setError(ToastFile.ent_age);
                                                    Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                                } else if (conertage > 120) {
                                                    Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                                } else if (saveGenderId == null || saveGenderId == "") {
                                                    Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                                } else if (sctHr.equals("HR*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                                } else if (sctMin.equals("MIN*")) {
                                                    Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                                } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                    Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                                } else if (patientAddressdataToPass.equals("")) {
                                                    Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                                } else if (patientAddressdataToPass.length() < 25) {
                                                    Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                                } else if (pincode_pass.equalsIgnoreCase("")) {
                                                    Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                                } else if (pincode_pass.length() < 6) {
                                                    Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                                } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                    Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                                } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                                } else {
                                                 /*   if (Global.OTPVERIFIED) {
                                                        if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                            GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                            edt_email.requestFocus();
                                                        } else {
                                                            TTL_HOME();
                                                        }
                                                    } else {*/
                                                    if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                        if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                            GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                            edt_email.requestFocus();
                                                        } else {
                                                            TTL_HOME();
                                                        }
                                                    } else {
                                                        TTL_HOME();
                                                    }
//                                                    }


                                                }

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (selectTypeSpinner.getSelectedItem().equals("CAMP")) {
                                    Enablefields();
                                    try {
                                        leadlayout.setVisibility(View.GONE);
                                        id_layout.setVisibility(View.GONE);
                                        barcode_layout.setVisibility(View.GONE);
                                        pincode_linear_data.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        leadlayout.setVisibility(View.GONE);
                                        leadbarcodelayout.setVisibility(View.GONE);
                                        brand_string = brand_spinner.getSelectedItem().toString();
                                        type_string = selectTypeSpinner.getSelectedItem().toString();
                                        next_btn.setVisibility(View.VISIBLE);
                                        camp_layout_woe.setVisibility(View.GONE);
                                        btech_linear_layout.setVisibility(View.VISIBLE);
                                        home_layout.setVisibility(View.GONE);
                                        vial_number.setVisibility(View.VISIBLE);
                                        ref_check.setVisibility(View.GONE);
                                        ll_email.setVisibility(View.VISIBLE);
                                        labname_linear.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                        mobile_number_kyc.setVisibility(View.GONE);
                                        Home_mobile_number_kyc.setVisibility(View.GONE);
                                        camp_layout_woe.setVisibility(View.VISIBLE);
                                        btech_linear_layout.setVisibility(View.GONE);
                                        home_layout.setVisibility(View.GONE);
                                        labname_linear.setVisibility(View.GONE);
                                        ref_check_linear.setVisibility(View.GONE);
                                        refby_linear.setVisibility(View.GONE);
                                        referedbyText.setText("");
                                        ref_check_linear.setVisibility(View.VISIBLE);
                                        uncheck_ref.setVisibility(View.VISIBLE);
                                        refby_linear.setVisibility(View.VISIBLE);
                                        namePatients.setVisibility(View.VISIBLE);
                                        AGE_layout.setVisibility(View.VISIBLE);
                                        time_layout.setVisibility(View.VISIBLE);

                                        patientAddress.setText("");
                                        referenceBy = "";

                                        next_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                nameString = name.getText().toString();
                                                nameString = nameString.replaceAll("\\s+", " ");
                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                getFinalTime = sctHr + ":" + sctMin;
                                                getFinalDate = dateShow.getText().toString();

                                                String getDateToCompare = getFinalDate + " " + getFinalTime;

                                                SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                try {
                                                    dCompare = sdfform.parse(getDateToCompare);
                                                } catch (ParseException e) {
                                              /*  if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                    String input = getDateToCompare;
                                                    //Format of the date defined in the input String
                                                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                    //Desired format: 24 hour format: Change the pattern as per the need
                                                    DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                    Date date = null;
                                                    String output = null;
                                                    try {
                                                        dCompare = df.parse(input);
                                                        output = outputformat.format(dCompare);
                                                        System.out.println(output);
                                                    } catch (ParseException pe) {
                                                        pe.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }

                                                typename = selectTypeSpinner.getSelectedItem().toString();
                                                brandNames = brand_spinner.getSelectedItem().toString();
                                                getVial_numbver = vial_number.getText().toString();
                                                if (typename.equals("CAMP")) {

                                                    ageString = age.getText().toString();
                                                    woereferedby = referedbyText.getText().toString();
                                                    GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                    // scpoint = camp_spinner_olc.getSelectedItem().toString();
                                                    if (camp_spinner_olc.getSelectedItem().toString() != null) {
                                                        scpoint = camp_spinner_olc.getSelectedItem().toString();
                                                    }
                                                    kycdata = kyc_format.getText().toString();

                                                    kycdata = "";
                                                    labIDTopass = "";
                                                    btechIDToPass = "";
                                                    btechnameTopass = "";
                                                    labAddressTopass = "";

                                                    if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                        if (referenceBy == null) {
                                                            Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            if (referenceBy.equalsIgnoreCase("SELF")) {
                                                                referenceBy = "SELF";
                                                                referredID = "";
                                                                woereferedby = referenceBy;
                                                            } else {

                                                                referenceBy = referedbyText.getText().toString();
                                                            }
                                                        }

                                                    } else {
                                                        referenceBy = woereferedby;
                                                        referredID = "";
                                                    }


                                                    if (woereferedby != null) {
                                                        if (obj != null) {
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                    referenceBy = woereferedby;
                                                                    referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        referenceBy = woereferedby;
                                                        referredID = "";
                                                    }


                                                    if (scpoint != null) {
                                                        if (myPojo.getMASTERS().getCAMP_LIST() != null) {
                                                            for (int k = 0; k < myPojo.getMASTERS().getCAMP_LIST().length; k++) {
                                                                if (scpoint.equals(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE())) {
                                                                    getcampIDtoPass = myPojo.getMASTERS().getCAMP_LIST()[k].getCAMP_ID();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (!ageString.equals("")) {
                                                        conertage = Integer.parseInt(ageString);
                                                    }
                                                    checkSelectMode();
                                                    checkDuplicate();
                                                    if (getVial_numbver.equals("")) {
                                                        vial_number.setError(ToastFile.vial_no);
                                                        Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                                    } else if (nameString.equals("")) {
                                                        name.setError(ToastFile.crt_name);
                                                        Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                                    } else if (nameString.length() < 2) {
                                                        name.setError(ToastFile.crt_name_woe);
                                                        Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                                    } else if (ageString.equals("")) {
                                                        age.setError(ToastFile.ent_age);
                                                        Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                                    } else if (saveGenderId == null || saveGenderId == "") {
                                                        Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                                    } else if (conertage > 120) {
                                                        Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                                    } else if (sctHr.equals("HR*")) {
                                                        Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                                    } else if (sctMin.equals("MIN*")) {
                                                        Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                                    } /*else if (sctSEc.equals("AM/PM")) {
                                                    Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                                }*/ else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                        Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                                    } else if (scpoint.equals("Select Camp")) {
                                                        Toast.makeText(activity, "Please select camp name", Toast.LENGTH_SHORT).show();
                                                    } else if (dCompare.after(getCurrentDateandTime) && getCurrentDateandTime != null) {
                                                        Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        if (woereferedby != null) {
                                                            if (obj != null) {
                                                                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                        referenceBy = woereferedby;
                                                                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            referenceBy = woereferedby;
                                                            referredID = "";
                                                        }

                                                        final String getAgeType = spinyr.getSelectedItem().toString();
                                                        String sctDate = dateShow.getText().toString();
                                                        sctHr = timehr.getSelectedItem().toString();
                                                        sctMin = timesecond.getSelectedItem().toString();
                                                        sctSEc = timeampm.getSelectedItem().toString();
                                                        final String getFinalAge = age.getText().toString();
                                                        final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                        final String getFinalDate = dateShow.getText().toString();

                                                        //TODO Commented as per the input given on 14-03-2022
                                                        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
                                                            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                                                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                                                                    .setConfirmText("Ok")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                                            i.putExtra("name", nameString);
                                                                            i.putExtra("age", getFinalAge);
                                                                            i.putExtra("gender", saveGenderId);
                                                                            i.putExtra("sct", getFinalTime);
                                                                            i.putExtra("date", getFinalDate);
                                                                            GlobalClass.setReferenceBy_Name = referenceBy;
                                                                            startActivity(i);
                                                                            Log.e(TAG, "onClick: lab add and lab id " + scpoint + labIDTopass);
                                                                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                            saveDetails.putString("name", nameString);
                                                                            saveDetails.putString("age", getFinalAge);
                                                                            saveDetails.putString("gender", saveGenderId);
                                                                            saveDetails.putString("sct", getFinalTime);
                                                                            saveDetails.putString("date", getFinalDate);
                                                                            saveDetails.putString("ageType", getAgeType);
                                                                            saveDetails.putString("labname", "");
                                                                            saveDetails.putString("labAddress", scpoint);
                                                                            saveDetails.putString("patientAddress", scpoint);
                                                                            saveDetails.putString("refBy", referenceBy);
                                                                            saveDetails.putString("refId", referredID);
                                                                            saveDetails.putString("labIDaddress", labIDTopass);
                                                                            saveDetails.putString("btechIDToPass", btechIDToPass);
                                                                            saveDetails.putString("btechNameToPass", btechnameTopass);
                                                                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                                            saveDetails.putString("kycinfo", kycdata);
                                                                            saveDetails.putString("WOEbrand", brandNames);
                                                                            saveDetails.putString("woetype", typename);
                                                                            saveDetails.putString("SR_NO", getVial_numbver);
                                                                            saveDetails.putString("pincode", "");
                                                                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                                            saveDetails.commit();
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    })
                                                                    .show();
                                                        } else {*/
                                                        Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                        i.putExtra("name", nameString);
                                                        i.putExtra("age", getFinalAge);
                                                        i.putExtra("gender", saveGenderId);
                                                        i.putExtra("sct", getFinalTime);
                                                        i.putExtra("date", getFinalDate);
                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                        startActivity(i);
                                                        Log.e(TAG, "onClick: lab add and lab id " + scpoint + labIDTopass);
                                                        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                        saveDetails.putString("name", nameString);
                                                        saveDetails.putString("age", getFinalAge);
                                                        saveDetails.putString("gender", saveGenderId);
                                                        saveDetails.putString("sct", getFinalTime);
                                                        saveDetails.putString("date", getFinalDate);
                                                        saveDetails.putString("ageType", getAgeType);
                                                        saveDetails.putString("labname", "");
                                                        saveDetails.putString("labAddress", scpoint);
                                                        saveDetails.putString("patientAddress", scpoint);
                                                        saveDetails.putString("refBy", referenceBy);
                                                        saveDetails.putString("refId", referredID);
                                                        saveDetails.putString("labIDaddress", labIDTopass);
                                                        saveDetails.putString("btechIDToPass", btechIDToPass);
                                                        saveDetails.putString("btechNameToPass", btechnameTopass);
                                                        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                        saveDetails.putString("kycinfo", kycdata);
                                                        saveDetails.putString("WOEbrand", brandNames);
                                                        saveDetails.putString("woetype", typename);
                                                        saveDetails.putString("SR_NO", getVial_numbver);
                                                        saveDetails.putString("pincode", "");
                                                        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                        saveDetails.commit();
//                                                        }


                                                    }
                                                }

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (selectTypeSpinner.getSelectedItem().equals("ORDERS")) {
                                    LeadIdwoe();
                                } else if (selectTypeSpinner.getSelectedItem().equals("ADD")) {
                                    barcode_woe.setText("");
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    pincode_linear_data.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.VISIBLE);
                                    ll_miscallotp.setVisibility(View.GONE);
                                    cb_kyc_verification.setVisibility(View.GONE);
                                    vial_number.setVisibility(View.GONE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    ll_email.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.GONE);
                                    home_layout.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.GONE);
                                    patientAddress.setText("");
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    ref_check_linear.setVisibility(View.GONE);
                                    refby_linear.setVisibility(View.GONE);
                                    namePatients.setVisibility(View.GONE);
                                    AGE_layout.setVisibility(View.GONE);
                                    time_layout.setVisibility(View.GONE);
                                    next_btn.setVisibility(View.GONE);

                                    barcode_woe.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            if (s.length() < 8) {
                                                leadbarcodelayout.setVisibility(View.GONE);
                                            }
                                            if (s.length() == 8) {
                                                String passBarcode = s.toString();
                                                if (!GlobalClass.isNetworkAvailable(activity)) {
                                                    GlobalClass.showAlertDialog(activity);
                                                } else {
                                                    RecheckType(passBarcode);
                                                }
                                            }
                                        }
                                    });
                                    referenceBy = "";

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type_string.equals("")) {
                                                Toast.makeText(activity, "Please select type name", Toast.LENGTH_SHORT).show();
                                            } else if (barcode_woe.getText().toString().equals("")) {
                                                Toast.makeText(activity, ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
                                            } else {
                                                GlobalClass.branditem = brand_spinner.getSelectedItem().toString();
                                                GlobalClass.typeItem = selectTypeSpinner.getSelectedItem().toString();
                                                GlobalClass.id_value = id_for_woe.getText().toString();

                                                SharedPreferences.Editor savePrefe = activity.getSharedPreferences("AddTestType", 0).edit();
                                                savePrefe.putString("ALERT", ALERT);
                                                savePrefe.putString("BARCODE", BARCODE);
                                                savePrefe.putString("BVT_HRS", BVT_HRS);
                                                savePrefe.putString("LABCODE", LABCODE);
                                                savePrefe.putString("PATIENT", PATIENT);
                                                savePrefe.putString("REF_DR", REF_DR);
                                                savePrefe.putString("REQUESTED_ADDITIONAL_TESTS", REQUESTED_ADDITIONAL_TESTS);
                                                savePrefe.putString("REQUESTED_ON", REQUESTED_ON);
                                                savePrefe.putString("RES_ID", RES_ID);
                                                savePrefe.putString("SDATE", SDATE);
                                                savePrefe.putString("SL_NO", SL_NO);
                                                savePrefe.putString("STATUS", STATUS);
                                                savePrefe.putString("SU_CODE1", SU_CODE1);
                                                savePrefe.putString("SU_CODE2", SU_CODE2);
                                                savePrefe.putString("TESTS", TESTS);
                                                savePrefe.commit();

                                                Intent a = new Intent(activity, AddWOETestsForSerum.class);
                                                startActivity(a);

                                            }
                                        }
                                    });
                                } else if (selectTypeSpinner.getSelectedItem().equals("RECHECK")) {
                                    barcode_woe.setText("");
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    pincode_linear_data.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.VISIBLE);
                                    vial_number.setVisibility(View.GONE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.GONE);
                                    home_layout.setVisibility(View.GONE);
                                    ll_miscallotp.setVisibility(View.GONE);
                                    cb_kyc_verification.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.GONE);
                                    patientAddress.setText("");
                                    ll_email.setVisibility(View.GONE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.GONE);
                                    refby_linear.setVisibility(View.GONE);
                                    namePatients.setVisibility(View.GONE);
                                    AGE_layout.setVisibility(View.GONE);

                                    time_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();


                                    next_btn.setVisibility(View.GONE);
                                    barcode_woe.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            if (s.length() < 8) {
                                                leadbarcodelayout.setVisibility(View.GONE);
                                            }
                                            if (s.length() == 8) {
                                                if (!GlobalClass.isNetworkAvailable(activity)) {
                                                    GlobalClass.showAlertDialog(activity);
                                                } else {
                                                    String passBarcode = s.toString();
                                                    RecheckType(passBarcode);
                                                }
                                            }
                                        }
                                    });
                                    referenceBy = "";

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (brand_string.equals("Select brand name")) {
                                                Toast.makeText(activity, "Please select brand name", Toast.LENGTH_SHORT).show();
                                            } else if (type_string.equals("")) {
                                                Toast.makeText(activity, "Please select type name", Toast.LENGTH_SHORT).show();
                                            } else if (barcode_woe.getText().toString().equals("")) {
                                                Toast.makeText(activity, ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
                                            } else {
                                                SharedPreferences.Editor savePrefe = activity.getSharedPreferences("RecheckTestType", 0).edit();
                                                savePrefe.putString("ALERT", ALERT);
                                                savePrefe.putString("BARCODE", BARCODE);
                                                savePrefe.putString("BVT_HRS", BVT_HRS);
                                                savePrefe.putString("LABCODE", LABCODE);
                                                savePrefe.putString("PATIENT", PATIENT);
                                                savePrefe.putString("REF_DR", REF_DR);
                                                savePrefe.putString("REQUESTED_ADDITIONAL_TESTS", REQUESTED_ADDITIONAL_TESTS);
                                                savePrefe.putString("REQUESTED_ON", REQUESTED_ON);
                                                savePrefe.putString("RES_ID", RES_ID);
                                                savePrefe.putString("SDATE", SDATE);
                                                savePrefe.putString("SL_NO", SL_NO);
                                                savePrefe.putString("STATUS", STATUS);
                                                savePrefe.putString("SU_CODE1", SU_CODE1);
                                                savePrefe.putString("SU_CODE2", SU_CODE2);
                                                savePrefe.putString("TESTS", TESTS);

                                                savePrefe.commit();
                                                GlobalClass.branditem = brand_spinner.getSelectedItem().toString();
                                                GlobalClass.typeItem = selectTypeSpinner.getSelectedItem().toString();
                                                Intent a = new Intent(activity, RecheckAllTest.class);
                                                startActivity(a);

                                            }
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }

                if (position > 0) {
                    vial_number.getText().clear();
                    id_for_woe.getText().clear();
                    if (brand_spinner.getSelectedItem().toString().equalsIgnoreCase("SMT")) {
                        ll_communication_main.setVisibility(View.GONE);
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListSMT);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);

                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                setResetCommModes();
                                resetOTPFields();
                                SetAutoCompleteViewAdapterToView(false);  // TODO code to reset patient details selected from Dropdownlist
                                if (selectTypeSpinner.getSelectedItem().equals("DPS")) {

                                    try {
                                        if (getOTPFlag().equalsIgnoreCase("NO")) {
                                            Enablefields();
                                            mobile_number_kyc.setVisibility(View.VISIBLE);
                                            ll_mobileno_otp.setVisibility(View.GONE);
                                            ll_miscallotp.setVisibility(View.GONE);
                                            cb_kyc_verification.setVisibility(View.GONE);
                                            tv_mob_note.setVisibility(View.GONE);
                                        } else if (getOTPFlag().equalsIgnoreCase("YES")) {
//                                            Disablefields();
//                                            ll_miscallotp.setVisibility(View.VISIBLE);
                                            cb_kyc_verification.setVisibility(View.VISIBLE);
                                            checkKYCIsChecked();
                                            radiogrp2.check(by_sendsms.getId());
                                        } else {
                                            GlobalClass.redirectToLogin(activity);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    ll_email.setVisibility(View.VISIBLE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);
                                    vial_number.setVisibility(View.VISIBLE);

//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    labname_linear.setVisibility(View.GONE);
                                    ref_check.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);

                                    referedbyText.setText("");
                                    woereferedby = referedbyText.getText().toString();

                                    refby_linear.setVisibility(View.VISIBLE);
                                    referenceBy = "";


                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
                                    referenceBy = "";
                                    getTSP_AddressStringTopass = getTSP_Address;

                                    kyc_format.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before,
                                                                  int count) {
                                            String enteredString = s.toString();
                                            if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                                                    enteredString.startsWith("#") || enteredString.startsWith("$") ||
                                                    enteredString.startsWith("%") || enteredString.startsWith("^") ||
                                                    enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                                                    || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                                                    || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                                                TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                if (enteredString.length() > 0) {
                                                    kyc_format.setText(enteredString.substring(1));
                                                } else {
                                                    kyc_format.setText("");
                                                }
                                            }
                                        }

                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                                      int after) {
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            final String checkNumber = s.toString();
                                            if (checkNumber.length() < 10) {
                                                flag = true;
                                            }

                                            if (flag == true) {
                                                if (checkNumber.length() == 10) {

                                                    if (!GlobalClass.isNetworkAvailable(activity)) {
                                                        flag = false;
                                                        kyc_format.setText(checkNumber);
                                                    } else {
                                                        flag = false;
                                                        barProgressDialog = new ProgressDialog(mContext);
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);

                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(activity);
                                                        StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + checkNumber, new
                                                                Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.e(TAG, "onResponse: response" + response);

                                                                        String getResponse = response;
                                                                        if (response.equals("\"proceed\"")) {

                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText(checkNumber);
                                                                        } else {
                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText("");
                                                                            TastyToast.makeText(activity, getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                        reques5tQueueCheckNumber.add(jsonObjectRequestPop);
                                                        Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);
                                                    }


                                                }

                                            }
                                        }
                                    });

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");

                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                                if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();


                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechname.getSelectedItem() != null)
                                                btechnameTopass = btechname.getSelectedItem().toString();

                                            try {
                                                if (btechnameTopass != null) {
                                                    if (myPojo != null && myPojo.getMASTERS().getBCT_LIST() != null) {
                                                        for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                            if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                                btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                            }
                                                        }
                                                    }

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {
                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            try {
                                                if (woereferedby != null) {
                                                    if (obj != null && obj.getMASTERS() != null && obj.getMASTERS().getREF_DR() != null) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }
                                            checkSelectMode();
                                            checkDuplicate();

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {

                                                try {
                                                    if (woereferedby != null) {
                                                        if (obj != null) {
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                    referenceBy = woereferedby;
                                                                    referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        referenceBy = woereferedby;
                                                        referredID = "";
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            /*    if (kycdata.length() == 0) {
                                                    Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
                                                } else if (kycdata.length() < 10) {
                                                    Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
                                                } else {*/

                                                try {
                                                    if (woereferedby != null) {
                                                        if (obj != null) {
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                    referenceBy = woereferedby;
                                                                    referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        referenceBy = woereferedby;
                                                        referredID = "";
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                final String getAgeType = spinyr.getSelectedItem().toString();
                                                String sctDate = dateShow.getText().toString();
                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                final String getFinalAge = age.getText().toString();
                                                final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                final String getFinalDate = dateShow.getText().toString();

                                                //TODO Commented as per the input given on 14-03-2022
                                               /* if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
                                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                                            .setContentText("You can register the PGC to avoid 10 Rs debit")
                                                            .setConfirmText("Ok")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                                                                        getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                                                                    }

                                                                    Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                                    i.putExtra("name", nameString);
                                                                    i.putExtra("age", getFinalAge);
                                                                    i.putExtra("gender", saveGenderId);
                                                                    i.putExtra("sct", getFinalTime);
                                                                    i.putExtra("date", getFinalDate);
                                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                                    startActivity(i);
                                                                    Log.e(TAG, "onClick: lab add and lab id " + getTSP_Address + labIDTopass);
                                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                    saveDetails.putString("name", nameString);
                                                                    saveDetails.putString("age", getFinalAge);
                                                                    saveDetails.putString("gender", saveGenderId);
                                                                    saveDetails.putString("sct", getFinalTime);
                                                                    saveDetails.putString("date", getFinalDate);
                                                                    saveDetails.putString("ageType", getAgeType);
                                                                    saveDetails.putString("labname", "");
                                                                    saveDetails.putString("labAddress", getTSP_Address);
                                                                    saveDetails.putString("patientAddress", patientAddressdataToPass);
                                                                    saveDetails.putString("refBy", referenceBy);
                                                                    saveDetails.putString("refId", referredID);
                                                                    saveDetails.putString("labIDaddress", "");
                                                                    saveDetails.putString("btechIDToPass", btechIDToPass);
                                                                    saveDetails.putString("btechNameToPass", btechnameTopass);
                                                                    saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                                    saveDetails.putString("kycinfo", kycdata);
                                                                    saveDetails.putString("woetype", typename);
                                                                    saveDetails.putString("WOEbrand", brandNames);
                                                                    saveDetails.putString("SR_NO", getVial_numbver);
                                                                    saveDetails.putString("pincode", pincode_pass);
                                                                    saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                                    saveDetails.commit();
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();
                                                } else {*/
                                                if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                                                    getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                                                }

                                                Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                i.putExtra("name", nameString);
                                                i.putExtra("age", getFinalAge);
                                                i.putExtra("gender", saveGenderId);
                                                i.putExtra("sct", getFinalTime);
                                                i.putExtra("date", getFinalDate);
                                                GlobalClass.setReferenceBy_Name = referenceBy;
                                                startActivity(i);
                                                Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass + labIDTopass);
                                                SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                saveDetails.putString("name", nameString);
                                                saveDetails.putString("age", getFinalAge);
                                                saveDetails.putString("gender", saveGenderId);
                                                saveDetails.putString("sct", getFinalTime);
                                                saveDetails.putString("date", getFinalDate);
                                                saveDetails.putString("ageType", getAgeType);
                                                saveDetails.putString("labname", "");
                                                saveDetails.putString("labAddress", getTSP_Address);
                                                saveDetails.putString("patientAddress", patientAddressdataToPass);
                                                saveDetails.putString("refBy", referenceBy);
                                                saveDetails.putString("refId", referredID);
                                                saveDetails.putString("labIDaddress", "");
                                                saveDetails.putString("btechIDToPass", btechIDToPass);
                                                saveDetails.putString("btechNameToPass", btechnameTopass);
                                                saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                saveDetails.putString("kycinfo", kycdata);
                                                saveDetails.putString("woetype", typename);
                                                saveDetails.putString("WOEbrand", brandNames);
                                                saveDetails.putString("SR_NO", getVial_numbver);
                                                saveDetails.putString("pincode", pincode_pass);
                                                saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                saveDetails.commit();
//                                                }


//                                                }
                                            }
                                        }
                                    });
                                } else if (selectTypeSpinner.getSelectedItem().equals("HOME")) {
                                    try {
                                        if (getOTPFlag().equalsIgnoreCase("NO")) {
                                            Enablefields();
                                            Home_mobile_number_kyc.setVisibility(View.VISIBLE);
                                            ll_mobileno_otp.setVisibility(View.GONE);
                                            ll_miscallotp.setVisibility(View.GONE);
                                            cb_kyc_verification.setVisibility(View.GONE);

                                            tv_mob_note.setVisibility(View.GONE);
                                        } else if (getOTPFlag().equalsIgnoreCase("YES")) {
//                                            Disablefields();
//                                            ll_miscallotp.setVisibility(View.VISIBLE);
                                            cb_kyc_verification.setVisibility(View.VISIBLE);
                                            checkKYCIsChecked();
                                            radiogrp2.check(by_sendsms.getId());
                                        } else {
                                            GlobalClass.redirectToLogin(activity);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    ll_email.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);

//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    vial_number.setVisibility(View.VISIBLE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.GONE);
                                    patientAddress.setText("");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    referenceBy = "";


                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
//                                             btnClick();
                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                                if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();
                                            // btechnameTopass = btechname.getSelectedItem().toString();

                                            if (btechname.getSelectedItem() != null)
                                                btechnameTopass = btechname.getSelectedItem().toString();


                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = home_kyc_format.getText().toString();
                                            }


                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechnameTopass != null) {
                                                if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {

                                               /* if (kycdata.length() == 0) {
                                                    Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
                                                } else if (kycdata.length() < 10) {
                                                    Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
                                                } else {*/

                                                if (woereferedby != null) {
                                                    if (obj != null) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }

                                                final String getAgeType = spinyr.getSelectedItem().toString();
                                                String sctDate = dateShow.getText().toString();
                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                final String getFinalAge = age.getText().toString();
                                                final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                final String getFinalDate = dateShow.getText().toString();

                                                //TODO Commented as per the input given on 14-03-2022
                                                /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
                                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                                            .setContentText("You can register the PGC to avoid 10 Rs debit")
                                                            .setConfirmText("Ok")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                                    i.putExtra("name", nameString);
                                                                    i.putExtra("age", getFinalAge);
                                                                    i.putExtra("gender", saveGenderId);
                                                                    i.putExtra("sct", getFinalTime);
                                                                    i.putExtra("date", getFinalDate);
                                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                                    startActivity(i);
                                                                    Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
                                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                    saveDetails.putString("name", nameString);
                                                                    saveDetails.putString("age", getFinalAge);
                                                                    saveDetails.putString("gender", saveGenderId);
                                                                    saveDetails.putString("sct", getFinalTime);
                                                                    saveDetails.putString("date", getFinalDate);
                                                                    saveDetails.putString("ageType", getAgeType);
                                                                    saveDetails.putString("labname", "");
                                                                    saveDetails.putString("labAddress", patientAddressdataToPass);
                                                                    saveDetails.putString("patientAddress", patientAddressdataToPass);
                                                                    saveDetails.putString("refBy", referenceBy);
                                                                    saveDetails.putString("refId", referredID);
                                                                    saveDetails.putString("labIDaddress", labIDTopass);
                                                                    saveDetails.putString("btechIDToPass", btechIDToPass);
                                                                    saveDetails.putString("btechNameToPass", btechnameTopass);
                                                                    saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                                    saveDetails.putString("kycinfo", kycdata);
                                                                    saveDetails.putString("woetype", typename);
                                                                    saveDetails.putString("WOEbrand", brandNames);
                                                                    saveDetails.putString("SR_NO", getVial_numbver);
                                                                    saveDetails.putString("pincode", pincode_pass);
                                                                    saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                                    saveDetails.commit();
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();
                                                } else {*/
                                                Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                i.putExtra("name", nameString);
                                                i.putExtra("age", getFinalAge);
                                                i.putExtra("gender", saveGenderId);
                                                i.putExtra("sct", getFinalTime);
                                                i.putExtra("date", getFinalDate);
                                                GlobalClass.setReferenceBy_Name = referenceBy;
                                                startActivity(i);
                                                Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
                                                SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                saveDetails.putString("name", nameString);
                                                saveDetails.putString("age", getFinalAge);
                                                saveDetails.putString("gender", saveGenderId);
                                                saveDetails.putString("sct", getFinalTime);
                                                saveDetails.putString("date", getFinalDate);
                                                saveDetails.putString("ageType", getAgeType);
                                                saveDetails.putString("labname", "");
                                                saveDetails.putString("labAddress", patientAddressdataToPass);
                                                saveDetails.putString("patientAddress", patientAddressdataToPass);
                                                saveDetails.putString("refBy", referenceBy);
                                                saveDetails.putString("refId", referredID);
                                                saveDetails.putString("labIDaddress", labIDTopass);
                                                saveDetails.putString("btechIDToPass", btechIDToPass);
                                                saveDetails.putString("btechNameToPass", btechnameTopass);
                                                saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                                                saveDetails.putString("kycinfo", kycdata);
                                                saveDetails.putString("woetype", typename);
                                                saveDetails.putString("WOEbrand", brandNames);
                                                saveDetails.putString("SR_NO", getVial_numbver);
                                                saveDetails.putString("pincode", pincode_pass);
                                                saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                                                saveDetails.commit();
//                                                }


//                                                }

                                            }

                                        }
                                    });

                                } else if (selectTypeSpinner.getSelectedItem().equals("ORDERS")) {
                                    LeadIdwoe();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    } else if (brand_spinner.getSelectedItem().toString().equalsIgnoreCase("EQNX")) {
                        ll_communication_main.setVisibility(View.GONE);
                        vial_number.getText().clear();
                        id_for_woe.getText().clear();

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListsecond);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);
                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                setResetCommModes();
                                resetOTPFields();
                                SetAutoCompleteViewAdapterToView(false);  // TODO code to reset patient details selected from Dropdownlist
                                if (selectTypeSpinner.getSelectedItemPosition() == 0) {
//                                    samplecollectionponit.setText("SEARCH SAMPLE COLLECTION POINT");
                                    samplecollectionponit.setHint("SEARCH SAMPLE COLLECTION POINT*");
                                    et_mobno.getText().clear();
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    lin_otp.setVisibility(View.GONE);
                                    tv_timer.setVisibility(View.GONE);
                                    Enablefields();
                                    ll_email.setVisibility(View.VISIBLE);
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    pincode_linear_data.setVisibility(View.GONE);
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.GONE);
                                    home_layout.setVisibility(View.GONE);
                                    edt_missed_mobile.setHint("Mobile Number");
                                    ref_check.setVisibility(View.GONE);
                                    vial_number.setVisibility(View.VISIBLE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.VISIBLE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.GONE);
                                    time_layout.setVisibility(View.VISIBLE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    refby_linear.setVisibility(View.VISIBLE);
                                    referenceBy = "";
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    id_woe = id_for_woe.getText().toString();
                                    barcode_woe_str = barcode_woe.getText().toString();
//                                            btnClick();
                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;
                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                                /*if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            if (selectTypeSpinner.getSelectedItem().toString() != null && selectTypeSpinner.getSelectedItem().toString().length() > 0)
                                                typename = selectTypeSpinner.getSelectedItem().toString();

                                            if (brand_spinner.getSelectedItem().toString() != null && brand_spinner.getSelectedItem().toString().length() > 0)
                                                brandNames = brand_spinner.getSelectedItem().toString();

                                            if (vial_number.getText().toString() != null && vial_number.getText().toString().length() > 0)
                                                getVial_numbver = vial_number.getText().toString();

                                            if (age.getText().toString() != null && age.getText().toString().length() > 0)
                                                ageString = age.getText().toString();

                                            if (referedbyText.getText().toString() != null && referedbyText.getText().toString().length() > 0) {
                                                GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                woereferedby = referedbyText.getText().toString();
                                            }

                                            if (samplecollectionponit.getText().toString() != null && samplecollectionponit.getText().toString().length() > 0) {
                                                GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();
                                                scpoint = samplecollectionponit.getText().toString();
                                            }

                                            if (kyc_format.getText().toString() != null && kyc_format.getText().toString().length() > 0)
                                                kycdata = kyc_format.getText().toString();

                                            if (samplecollectionponit.getText().toString() != null && samplecollectionponit.getText().toString().length() > 0)
                                                getLabName = samplecollectionponit.getText().toString();

                                            getVial_numbver = vial_number.getText().toString();

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            // brandNames = no_img_spinner.getSelectedItem().toString();


                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();

                                            GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();

                                            getLabName = samplecollectionponit.getText().toString();
                                            kycdata = kyc_format.getText().toString();


                                            kycdata = "";
                                            btechIDToPass = "";
                                            btechnameTopass = "";
                                            getcampIDtoPass = "";
                                            scpoint = samplecollectionponit.getText().toString();

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null || referenceBy.length() <= 1) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } */ else if (scpoint.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT") || scpoint.equals("") || scpoint.equals(null)) {
                                                Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                                Toast.makeText(mContext, "Please select sample collection point", Toast.LENGTH_SHORT).show();
                                            } else {
                                              /*  if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        OutLabs_Eqnx_ILS();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        OutLabs_Eqnx_ILS();
                                                    }
                                                } else {
                                                    OutLabs_Eqnx_ILS();
                                                }
//                                                }

                                            }

                                        }
                                    });


                                } else if (selectTypeSpinner.getSelectedItemPosition() == 1) {


                                    if (getOTPFlag().equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (getOTPFlag().equalsIgnoreCase("YES")) {
//                                        Disablefields();
//                                        ll_miscallotp.setVisibility(View.VISIBLE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
                                        radiogrp2.check(by_sendsms.getId());
                                    } else {
                                        GlobalClass.redirectToLogin(activity);
                                    }


                                    //mobile_number_kyc.setVisibility(View.VISIBLE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    ll_email.setVisibility(View.VISIBLE);
//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);

                                    leadlayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);

                                    labname_linear.setVisibility(View.GONE);
                                    vial_number.setVisibility(View.VISIBLE);


                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    ref_check.setVisibility(View.GONE);
                                    refby_linear.setVisibility(View.VISIBLE);

                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.GONE);
                                    referedbyText.setText("");
                                    time_layout.setVisibility(View.VISIBLE);
                                    referenceBy = "";
                                    getTSP_AddressStringTopass = getTSP_Address;
                                    kyc_format.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before,
                                                                  int count) {
                                            String enteredString = s.toString();
                                            if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                                                    enteredString.startsWith("#") || enteredString.startsWith("$") ||
                                                    enteredString.startsWith("%") || enteredString.startsWith("^") ||
                                                    enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                                                    || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                                                    || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                                            ) {
                                                TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                if (enteredString.length() > 0) {
                                                    kyc_format.setText(enteredString.substring(1));
                                                } else {
                                                    kyc_format.setText("");
                                                }
                                            }
                                        }

                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                                      int after) {
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            final String checkNumber = s.toString();
                                            if (checkNumber.length() < 10) {
                                                flag = true;
                                            }
                                            if (flag == true) {
                                                if (checkNumber.length() == 10) {
                                                    if (!GlobalClass.isNetworkAvailable(activity)) {
                                                        flag = false;
                                                        kyc_format.setText(checkNumber);
                                                    } else {
                                                        flag = false;
                                                        barProgressDialog = new ProgressDialog(activity);
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);
                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(activity);
                                                        StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + checkNumber, new
                                                                Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        Log.e(TAG, "onResponse: response" + response);
                                                                        String getResponse = response;
                                                                        if (response.equals("\"proceed\"")) {

                                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                                barProgressDialog.dismiss();
                                                                            }*/
                                                                            /*if (mContext instanceof Activity) {
                                                                                if (!((Activity) mContext).isFinishing())
                                                                                    barProgressDialog.dismiss();
                                                                            }*/
                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText(checkNumber);
                                                                        } else {
                                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                                barProgressDialog.dismiss();
                                                                            }*/
                                                                            /*if (mContext instanceof Activity) {
                                                                                if (!((Activity) mContext).isFinishing())
                                                                                    barProgressDialog.dismiss();
                                                                            }*/

                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText("");
                                                                            TastyToast.makeText(activity, getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                        reques5tQueueCheckNumber.add(jsonObjectRequestPop);
                                                        Log.e(TAG, "afterTextChanged: url" + jsonObjectRequestPop);
                                                    }

                                                }

                                            }

                                        }
                                    });

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                                /*if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();
                                            btechnameTopass = btechname.getSelectedItem().toString();

                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (btechnameTopass != null) {
                                                if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }
                                            }


                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } */ else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else {

                                             /*   if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlabs_EQNX_DPS();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlabs_EQNX_DPS();
                                                    }
                                                } else {
                                                    Outlabs_EQNX_DPS();
                                                }
//                                                }


                                            }

                                        }
                                    });

                                } else if (selectTypeSpinner.getSelectedItemPosition() == 2) {


                                    if (getOTPFlag().equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (getOTPFlag().equalsIgnoreCase("YES")) {
//                                        Disablefields();
//                                        ll_miscallotp.setVisibility(View.VISIBLE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
                                        radiogrp2.check(by_sendsms.getId());
                                    } else {
                                        GlobalClass.redirectToLogin(activity);
                                    }
//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    ll_email.setVisibility(View.VISIBLE);
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);

                                    vial_number.setVisibility(View.VISIBLE);
                                    labname_linear.setVisibility(View.GONE);
                                    patientAddress.setText("");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    referenceBy = "";


                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.GONE);

                                    time_layout.setVisibility(View.VISIBLE);
//                                             btnClick();
                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                                if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();
                                            btechnameTopass = btechname.getSelectedItem().toString();
                                            //kycdata = home_kyc_format.getText().toString();

                                            /*if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = home_kyc_format.getText().toString();
                                            }*/

                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechnameTopass != null) {
                                                if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            checkSelectMode();
                                            checkDuplicate();
                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(activity, ToastFile.ent_addre, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(activity, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else {

                                              /*  if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlab_EQNX_Home();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlab_EQNX_Home();
                                                    }
                                                } else {
                                                    Outlab_EQNX_Home();
                                                }
//                                                }

                                            }

                                        }
                                    });
                                } else if (selectTypeSpinner.getSelectedItemPosition() == 3) {
                                    LeadIdwoe();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        ll_communication_main.setVisibility(View.GONE);
                        vial_number.getText().clear();
                        id_for_woe.getText().clear();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListsecond);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);
                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                setResetCommModes();
                                resetOTPFields();
                                SetAutoCompleteViewAdapterToView(false);  // TODO code to reset patient details selected from Dropdownlist
                                if (selectTypeSpinner.getSelectedItemPosition() == 0) {

                                    Enablefields();

                                    if (yourCountDownTimer != null) {
                                        yourCountDownTimer.cancel();
                                        yourCountDownTimer = null;
                                        btn_snd_otp.setEnabled(true);
                                        btn_snd_otp.setClickable(true);
                                    }
                                    ll_email.setVisibility(View.VISIBLE);
                                    et_mobno.getText().clear();
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    edt_missed_mobile.setHint("Mobile Number");
                                    et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    lin_otp.setVisibility(View.GONE);
                                    tv_timer.setVisibility(View.GONE);
//                                    ll_miscallotp.setVisibility(View.VISIBLE);
                                    cb_kyc_verification.setVisibility(View.VISIBLE);
                                    checkKYCIsChecked();
                                    radiogrp2.check(by_sendsms.getId());
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    pincode_linear_data.setVisibility(View.GONE);
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.GONE);
                                    home_layout.setVisibility(View.GONE);
                                    ref_check.setVisibility(View.GONE);
                                    vial_number.setVisibility(View.VISIBLE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.VISIBLE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    refby_linear.setVisibility(View.VISIBLE);
                                    referenceBy = "";
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    id_woe = id_for_woe.getText().toString();
                                    barcode_woe_str = barcode_woe.getText().toString();


                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                               /* if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            scpoint = samplecollectionponit.getText().toString();
                                            GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();
                                            kycdata = kyc_format.getText().toString();
                                            getLabName = samplecollectionponit.getText().toString();

                                            kycdata = "";
                                            btechIDToPass = "";
                                            btechnameTopass = "";
                                            getcampIDtoPass = "";

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }


                                            checkDuplicate();
                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            }/* else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } */ else if (scpoint.equals("SEARCH SAMPLE COLLECTION POINT") || scpoint.equals(null)) {
                                                Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                                Toast.makeText(mContext, "Please select sample collection point", Toast.LENGTH_SHORT).show();
                                            } else {
                                               /* if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        OutLabs_others_ILS();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        OutLabs_others_ILS();
                                                    }
                                                } else {
                                                    OutLabs_others_ILS();
                                                }
//                                                }
                                            }


                                        }
                                    });


                                } else if (selectTypeSpinner.getSelectedItemPosition() == 1) {
                                    if (getOTPFlag().equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (getOTPFlag().equalsIgnoreCase("YES")) {
//                                        Disablefields();
//                                        ll_miscallotp.setVisibility(View.VISIBLE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
                                        radiogrp2.check(by_sendsms.getId());
                                    } else {
                                        GlobalClass.redirectToLogin(activity);
                                    }
//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    ll_email.setVisibility(View.VISIBLE);

                                    leadlayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);

                                    labname_linear.setVisibility(View.GONE);
                                    vial_number.setVisibility(View.VISIBLE);

                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    ref_check.setVisibility(View.GONE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    time_layout.setVisibility(View.VISIBLE);
                                    referenceBy = "";
                                    getTSP_AddressStringTopass = getTSP_Address;
                                    kyc_format.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before,
                                                                  int count) {
                                            String enteredString = s.toString();
                                            if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                                                    enteredString.startsWith("#") || enteredString.startsWith("$") ||
                                                    enteredString.startsWith("%") || enteredString.startsWith("^") ||
                                                    enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                                                    || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                                                    || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                                            ) {
                                                TastyToast.makeText(activity, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                                                if (enteredString.length() > 0) {
                                                    kyc_format.setText(enteredString.substring(1));
                                                } else {
                                                    kyc_format.setText("");
                                                }
                                            }
                                        }

                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                                      int after) {
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            final String checkNumber = s.toString();
                                            if (checkNumber.length() < 10) {
                                                flag = true;
                                            }
                                            if (flag) {
                                                if (checkNumber.length() == 10) {
                                                    if (!GlobalClass.isNetworkAvailable(activity)) {
                                                        flag = false;
                                                        kyc_format.setText(checkNumber);
                                                    } else {
                                                        flag = false;
                                                        barProgressDialog = new ProgressDialog(activity);
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);
                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(activity);
                                                        StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + checkNumber, new
                                                                Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        Log.e(TAG, "onResponse: response" + response);
                                                                        String getResponse = response;
                                                                        if (response.equals("\"proceed\"")) {

                                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                                barProgressDialog.dismiss();
                                                                            }*/
                                                                           /* if (mContext instanceof Activity) {
                                                                                if (!((Activity) mContext).isFinishing())
                                                                                    barProgressDialog.dismiss();
                                                                            }*/
                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText(checkNumber);
                                                                        } else {
                                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                                barProgressDialog.dismiss();
                                                                            }*/
                                                                           /* if (mContext instanceof Activity) {
                                                                                if (!((Activity) mContext).isFinishing())
                                                                                    barProgressDialog.dismiss();
                                                                            }*/
                                                                            GlobalClass.hideProgress(activity, barProgressDialog);
                                                                            kyc_format.setText("");
                                                                            TastyToast.makeText(activity, getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                        reques5tQueueCheckNumber.add(jsonObjectRequestPop);
                                                        Log.e(TAG, "afterTextChanged: url" + jsonObjectRequestPop);
                                                    }

                                                }

                                            }

                                        }
                                    });

                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                               /* if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }*/

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();
                                            btechnameTopass = btechname.getSelectedItem().toString();
                                            /* kycdata = kyc_format.getText().toString();*/

                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }


                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (btechnameTopass != null) {
                                                if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }
                                            }

                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            checkDuplicate();
                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {
                                               /* if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlab_others_DPS();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlab_others_DPS();
                                                    }
                                                } else {
                                                    Outlab_others_DPS();
                                                }
//                                                }

                                            }

                                        }
                                    });

                                } else if (selectTypeSpinner.getSelectedItemPosition() == 2) {


                                    if (getOTPFlag().equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        ll_miscallotp.setVisibility(View.GONE);
                                        cb_kyc_verification.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (getOTPFlag().equalsIgnoreCase("YES")) {
//                                        Disablefields();
//                                        ll_miscallotp.setVisibility(View.VISIBLE);
                                        cb_kyc_verification.setVisibility(View.VISIBLE);
                                        checkKYCIsChecked();
                                        radiogrp2.check(by_sendsms.getId());
                                    } else {
                                        GlobalClass.redirectToLogin(activity);
                                    }

                                    ll_email.setVisibility(View.VISIBLE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
//                                    edt_missed_mobile.setHint("Mobile Number*");
                                    leadlayout.setVisibility(View.GONE);
                                    id_layout.setVisibility(View.GONE);
                                    barcode_layout.setVisibility(View.GONE);
                                    leadlayout.setVisibility(View.GONE);
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    brand_string = brand_spinner.getSelectedItem().toString();
                                    type_string = selectTypeSpinner.getSelectedItem().toString();
                                    next_btn.setVisibility(View.VISIBLE);
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);
                                    vial_number.setVisibility(View.VISIBLE);
                                    labname_linear.setVisibility(View.GONE);
                                    patientAddress.setText("");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    referedbyText.setText("");
                                    referenceBy = "";


                                    namePatients.setVisibility(View.VISIBLE);
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    time_layout.setVisibility(View.VISIBLE);
//                                             btnClick();
                                    next_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            nameString = name.getText().toString();
                                            nameString = nameString.replaceAll("\\s+", " ");
                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            getFinalTime = sctHr + ":" + sctMin;
                                            getFinalDate = dateShow.getText().toString();

                                            String getDateToCompare = getFinalDate + " " + getFinalTime;

                                            SimpleDateFormat sdfform = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                            try {
                                                dCompare = sdfform.parse(getDateToCompare);
                                            } catch (ParseException e) {
                                                if (getDateToCompare.contains("AM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "a.m.";
                                                } else if (getDateToCompare.contains("PM")) {
                                                    getDateToCompare = getDateToCompare.substring(0, getDateToCompare.length() - 2);
                                                    getDateToCompare = getDateToCompare + "p.m.";
                                                }

                                                String input = getDateToCompare;
                                                //Format of the date defined in the input String
                                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                                                //Desired format: 24 hour format: Change the pattern as per the need
                                                DateFormat outputformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                                Date date = null;
                                                String output = null;
                                                try {
                                                    dCompare = df.parse(input);
                                                    output = outputformat.format(dCompare);
                                                    System.out.println(output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = brand_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            brandNames = brand_spinner.getSelectedItem().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            patientAddressdataToPass = patientAddress.getText().toString();
                                            pincode_pass = pincode_edt.getText().toString();
                                            btechnameTopass = btechname.getSelectedItem().toString();
                                            //kycdata = home_kyc_format.getText().toString();

                                            if (getOTPFlag().equalsIgnoreCase("YES")) {
                                                kycdata = edt_missed_mobile.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechnameTopass != null) {
                                                if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (woereferedby.equals("") || woereferedby.equals(null)) {
                                                if (referenceBy == null) {
                                                    Toast.makeText(mContext, "Please select Ref by", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (referenceBy.equalsIgnoreCase("SELF")) {
                                                        referenceBy = "SELF";
                                                        referredID = "";
                                                        woereferedby = referenceBy;
                                                    } else {

                                                        referenceBy = referedbyText.getText().toString();
                                                    }
                                                }

                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (woereferedby != null) {
                                                if (obj != null) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!ageString.equals("")) {
                                                conertage = Integer.parseInt(ageString);
                                            }
//                                            checkSelectMode();
                                            checkDuplicate();
                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().startsWith("0") ||
                                                    edt_missed_mobile.getText().toString().startsWith("1") ||
                                                    edt_missed_mobile.getText().toString().startsWith("2") ||
                                                    edt_missed_mobile.getText().toString().startsWith("3") ||
                                                    edt_missed_mobile.getText().toString().startsWith("4") ||
                                                    edt_missed_mobile.getText().toString().startsWith("5")) {
                                                Toast.makeText(mContext, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                                            } else if (edt_missed_mobile.getText().toString().length() > 0 && edt_missed_mobile.getText().toString().length() < 10) {
                                                Toast.makeText(mContext, ToastFile.crt_mob_num, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                name.setError(ToastFile.crt_name);
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                name.setError(ToastFile.crt_name_woe);
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                age.setError(ToastFile.ent_age);
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR*")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN*")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } /*else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }*/ else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(activity, ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {
                                              /*  if (Global.OTPVERIFIED) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlab_others_Home();
                                                    }
                                                } else {*/
                                                if (!GlobalClass.isNull(edt_email.getText().toString())) {
                                                    if (!GlobalClass.emailValidation(edt_email.getText().toString())) {
                                                        GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                                                        edt_email.requestFocus();
                                                    } else {
                                                        Outlab_others_Home();
                                                    }
                                                } else {
                                                    Outlab_others_Home();
                                                }
//                                                }

                                            }

                                        }
                                    });
                                } else if (selectTypeSpinner.getSelectedItemPosition() == 3) {
                                    LeadIdwoe();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkDuplicate() {
        HashSet<String> listToSet = new HashSet<String>(reportArray);
        List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
        Global.Communication = TextUtils.join(",", listWithoutDuplicates);
    }

    private boolean checkSelectMode() {
        if (cb_report.isChecked()) {
            if (!cb_sms.isChecked() && !cb_email.isChecked() && !cb_wa.isChecked()) {
                Toast.makeText(activity, "Kindly select any one communication mode for report", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (cb_receipt.isChecked()) {
            if (!cb_sms.isChecked() && !cb_email.isChecked() && !cb_wa.isChecked()) {
                Toast.makeText(activity, "Kindly select any one mode for receipt", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (cb_comm.isChecked()) {
            if (!cb_sms.isChecked() && !cb_email.isChecked() && !cb_wa.isChecked()) {
                Toast.makeText(activity, "Kindly select any one mode for communication", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (cb_email.isChecked()) {
            if (edt_email.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "Kindly enter Email-ID", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!GlobalClass.isNull(edt_email.getText().toString().trim())) {
                if (!GlobalClass.emailValidation(edt_email.getText().toString().trim())) {
                    GlobalClass.showCustomToast(activity, MessageConstants.VALID_EMAIL, 1);
                    edt_email.requestFocus();
                    return false;
                }
            }
        }

        if (cb_sms.isChecked()) {
            if (lin_missed_verify.getVisibility() == View.VISIBLE) {
                Toast.makeText(mContext, "Kindly verify mobile number", Toast.LENGTH_SHORT).show();
                return false;
            } else if (rel_verify_mobile.getVisibility() == View.VISIBLE) {
                kycdata = tv_verifiedmob.getText().toString().trim();
            } else {
                Toast.makeText(mContext, "Kindly verify mobile number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (cb_wa.isChecked()) {
            if (lin_missed_verify.getVisibility() == View.VISIBLE) {
                Toast.makeText(mContext, "Kindly verify mobile number", Toast.LENGTH_SHORT).show();
                return false;
            } else if (rel_verify_mobile.getVisibility() == View.VISIBLE) {
                kycdata = tv_verifiedmob.getText().toString().trim();
            } else {
                Toast.makeText(mContext, "Kindly verify mobile number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void Outlab_others_Home() {
   /*     if (kycdata.length() == 0) {
            Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
        } else if (kycdata.length() < 10) {
            Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
        } else {*/
        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");
        final String getFinalDate = dateShow.getText().toString();

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent i = new Intent(mContext, OutLabTestsActivity.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);
                            Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", getFinalAge);
                            saveDetails.putString("gender", saveGenderId);
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", getAgeType);
                            saveDetails.putString("labname", "");
                            saveDetails.putString("labAddress", patientAddressdataToPass);
                            saveDetails.putString("patientAddress", patientAddressdataToPass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", labIDTopass);
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", pincode_pass);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        } else {*/
        Intent i = new Intent(mContext, OutLabTestsActivity.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);
        Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", getAgeType);
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", patientAddressdataToPass);
        saveDetails.putString("patientAddress", patientAddressdataToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", labIDTopass);
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", pincode_pass);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
//        }
    }

    private void Outlab_others_DPS() {
        try {
            if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (woereferedby != null) {
            if (obj != null) {
                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                        referenceBy = woereferedby;
                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                    }
                }
            }
        } else {
            referenceBy = woereferedby;
            referredID = "";
        }

     /*   if (kycdata.length() == 0) {
            Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
        } else if (kycdata.length() < 10) {
            Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
        } else {*/

        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");
        final String getFinalDate = dateShow.getText().toString();

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent i = new Intent(mContext, OutLabTestsActivity.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            i.putExtra("woetype", typename);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);
                            Log.e(TAG, "onClick: lab add and lab id " + getTSP_Address);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", getFinalAge);
                            saveDetails.putString("gender", saveGenderId);
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", getAgeType);
                            saveDetails.putString("labname", "");
                            saveDetails.putString("labAddress", getTSP_Address);
                            saveDetails.putString("patientAddress", patientAddressdataToPass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", "");
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", pincode_pass);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                        }
                    })
                    .show();
        } else {*/
        Intent i = new Intent(mContext, OutLabTestsActivity.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        i.putExtra("woetype", typename);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);
        Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", getAgeType);
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", getTSP_Address);
        saveDetails.putString("patientAddress", patientAddressdataToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", "");
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", pincode_pass);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
//        }
    }

    private void OutLabs_others_ILS() {
        if (woereferedby != null) {
            if (obj != null) {
                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                        referenceBy = woereferedby;
                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                    }
                }
            }
        } else {
            referenceBy = woereferedby;
            referredID = "";
        }


        try {
            String s1 = getLabName.substring(getLabName.indexOf("-") + 2);
            s1.trim();
            getLabCode = s1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (labs == null) {
            labAddressTopass = "";
            labIDTopass = "";
        } else {
            if (labs.length != 0) {
                for (int i = 0; i < labs.length; i++) {
                    if (getLabCode.contains("-")) {
                        String s2 = getLabCode.substring(getLabCode.indexOf("-") + 2);
                        s2.trim();
                        getLabCode = s2;
                    }
                    if (getLabCode.equalsIgnoreCase(labs[i].getClientid())) {
                        getFullDataLabs = new LABS();
                        getFullDataLabs.setPincode(labs[i].getPincode());
                        getFullDataLabs.setPassingname(labs[i].getPassingname());
                        getFullDataLabs.setName(labs[i].getName());
                        getFullDataLabs.setMobile(labs[i].getMobile());
                        getFullDataLabs.setLabName(labs[i].getLabName());
                        getFullDataLabs.setLabAddress(labs[i].getLabAddress());
                        getFullDataLabs.setIdaddress(labs[i].getIdaddress());
                        getFullDataLabs.setEmail(labs[i].getEmail());
                        getFullDataLabs.setClientid(labs[i].getClientid());
                        labAddressTopass = getFullDataLabs.getLabAddress();
                        labIDTopass = getFullDataLabs.getIdaddress();
                        labLabNAmeTopass = getFullDataLabs.getLabName();
                    }
                }
            }

        }

        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");
        final String getFinalDate = dateShow.getText().toString();

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || GlobalClass.isNull(referredID)) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent i = new Intent(mContext, OutLabTestsActivity.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            i.putExtra("woetype", typename);
                            startActivity(i);

                            Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", getFinalAge);
                            saveDetails.putString("gender", saveGenderId);
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", getAgeType);
                            saveDetails.putString("labname", labLabNAmeTopass);
                            saveDetails.putString("labAddress", labAddressTopass);
                            saveDetails.putString("patientAddress", labAddressTopass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", labIDTopass);
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", "");
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                        }
                    })
                    .show();
        } else {*/
        Intent i = new Intent(mContext, OutLabTestsActivity.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        i.putExtra("woetype", typename);
        startActivity(i);

        Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
        GlobalClass.setReferenceBy_Name = referenceBy;
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", getAgeType);
        saveDetails.putString("labname", labLabNAmeTopass);
        saveDetails.putString("labAddress", labAddressTopass);
        saveDetails.putString("patientAddress", labAddressTopass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", labIDTopass);
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", "");
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
    }

    private void Outlab_EQNX_Home() {
    /*    if (kycdata.length() == 0) {
            Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
        } else if (kycdata.length() < 10) {
            Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
        } else {*/
        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");
        final String getFinalDate = dateShow.getText().toString();

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent i = new Intent(mContext, OutLabTestsActivity.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);
                            Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", "");
                            saveDetails.putString("gender", "");
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", "");
                            saveDetails.putString("labname", "");
                            saveDetails.putString("labAddress", patientAddressdataToPass);
                            saveDetails.putString("patientAddress", patientAddressdataToPass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", labIDTopass);
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", pincode_pass);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        } else {*/
        Intent i = new Intent(mContext, OutLabTestsActivity.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);
        Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", "");
        saveDetails.putString("gender", "");
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", "");
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", patientAddressdataToPass);
        saveDetails.putString("patientAddress", patientAddressdataToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", labIDTopass);
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", pincode_pass);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }


//        }
    }

    private void Outlabs_EQNX_DPS() {
        if (myPojo.getMASTERS().getTSP_MASTER() != null) {
            getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
        }


        if (woereferedby != null) {
            if (obj != null) {
                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                        referenceBy = woereferedby;
                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                    }
                }
            }
        } else {
            referenceBy = woereferedby;
            referredID = "";
        }

     /*   if (kycdata.length() == 0) {
            Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
        } else if (kycdata.length() < 10) {
            Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
        } else {*/

        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");
        final String getFinalDate = dateShow.getText().toString();

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent i = new Intent(mContext, OutLabTestsActivity.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            i.putExtra("woetype", typename);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);
                            Log.e(TAG, "onClick: lab add and lab id " + getTSP_Address);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", "");
                            saveDetails.putString("gender", "");
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", "");
                            saveDetails.putString("labname", "");
                            saveDetails.putString("labAddress", getTSP_Address);
                            saveDetails.putString("patientAddress", patientAddressdataToPass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", "");
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", pincode_pass);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                        }
                    })
                    .show();
        } else {*/
        Intent i = new Intent(mContext, OutLabTestsActivity.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        i.putExtra("woetype", typename);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);
        Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", "");
        saveDetails.putString("gender", "");
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", "");
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", getTSP_Address);
        saveDetails.putString("patientAddress", patientAddressdataToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", "");
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", pincode_pass);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }

//        }
    }

    private void OutLabs_Eqnx_ILS() {
        if (woereferedby != null) {
            if (obj != null) {
                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                        referenceBy = woereferedby;
                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                    }
                }
            }
        } else {
            referenceBy = woereferedby;
            referredID = "";
        }

        String s1 = getLabName.substring(getLabName.indexOf("-") + 2);
        s1.trim();
        getLabCode = s1;

        if (labs == null) {
            labAddressTopass = "";
            labIDTopass = "";
        } else {
            if (labs.length != 0) {
                for (int i = 0; i < labs.length; i++) {
                    if (getLabCode.contains("-")) {
                        String s2 = getLabCode.substring(getLabCode.indexOf("-") + 2);
                        s2.trim();
                        getLabCode = s2;
                    }
                    if (getLabCode.equalsIgnoreCase(labs[i].getClientid())) {
                        getFullDataLabs = new LABS();
                        getFullDataLabs.setPincode(labs[i].getPincode());
                        getFullDataLabs.setPassingname(labs[i].getPassingname());
                        getFullDataLabs.setName(labs[i].getName());
                        getFullDataLabs.setMobile(labs[i].getMobile());
                        getFullDataLabs.setLabName(labs[i].getLabName());
                        getFullDataLabs.setLabAddress(labs[i].getLabAddress());
                        getFullDataLabs.setIdaddress(labs[i].getIdaddress());
                        getFullDataLabs.setEmail(labs[i].getEmail());
                        getFullDataLabs.setClientid(labs[i].getClientid());
                        labAddressTopass = getFullDataLabs.getLabAddress();
                        labIDTopass = getFullDataLabs.getIdaddress();
                        labLabNAmeTopass = getFullDataLabs.getLabName();
                    }
                }
            }

        }

        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");
        final String getFinalDate = dateShow.getText().toString();

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent i = new Intent(mContext, OutLabTestsActivity.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            i.putExtra("woetype", typename);
                            startActivity(i);

                            Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", "");
                            saveDetails.putString("gender", "");
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", "");
                            saveDetails.putString("labname", labLabNAmeTopass);
                            saveDetails.putString("labAddress", labAddressTopass);
                            saveDetails.putString("patientAddress", labAddressTopass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", labIDTopass);
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", "");
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                        }
                    })
                    .show();
        } else {*/
        Intent i = new Intent(mContext, OutLabTestsActivity.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        i.putExtra("woetype", typename);
        startActivity(i);
        Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
        GlobalClass.setReferenceBy_Name = referenceBy;
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", "");
        saveDetails.putString("gender", "");
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", "");
        saveDetails.putString("labname", labLabNAmeTopass);
        saveDetails.putString("labAddress", labAddressTopass);
        saveDetails.putString("patientAddress", labAddressTopass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", labIDTopass);
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", "");
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
    }

    private void TTL_HOME() {
    /*    if (kycdata.length() == 0) {
            Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
        } else if (kycdata.length() < 10) {
            Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
        } else {*/

        if (woereferedby != null) {
            if (obj != null) {
                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                        referenceBy = woereferedby;
                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                    }
                }
            }
        } else {
            referenceBy = woereferedby;
            referredID = "";
        }

        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String getFinalDate = dateShow.getText().toString();
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Register PGC to avoid Rs. 50 extra billing")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);
                            Global.CommModes = TextUtils.join(",", typeArray);
                            Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", getFinalAge);
                            saveDetails.putString("gender", saveGenderId);
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", getAgeType);
                            saveDetails.putString("labname", "");
                            saveDetails.putString("labAddress", patientAddressdataToPass);
                            saveDetails.putString("patientAddress", patientAddressdataToPass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", labIDTopass);
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", pincode_pass);
                            saveDetails.putString("Communication", Global.Communication);
                            saveDetails.putString("CommModes", Global.CommModes);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();

        } else {*/
        Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);
        Global.CommModes = TextUtils.join(",", typeArray);
        Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", getAgeType);
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", patientAddressdataToPass);
        saveDetails.putString("patientAddress", patientAddressdataToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", labIDTopass);
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", pincode_pass);
        saveDetails.putString("Communication", Global.Communication);
        saveDetails.putString("CommModes", Global.CommModes);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
        //TODO Commented as per the input given on 14-03-2022

//        }
    }

    private void TTL_DPS() {
        try {
            if (woereferedby != null) {
                if (obj != null) {
                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                            referenceBy = woereferedby;
                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                        }
                    }
                }
            } else {
                referenceBy = woereferedby;
                referredID = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   if (kycdata.length() == 0) {
            Toast.makeText(activity, ToastFile.crt_kyc_empty, Toast.LENGTH_SHORT).show();
        } else if (kycdata.length() < 10) {
            Toast.makeText(activity, ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
        } else {*/

        try {
            if (woereferedby != null) {
                if (obj != null) {
                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                        if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                            referenceBy = woereferedby;
                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                        }
                    }
                }
            } else {
                referenceBy = woereferedby;
                referredID = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();
        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String getFinalDate = dateShow.getText().toString();
        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");

        //TODO Commented as per the input given on 14-03-2022
        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Register PGC to avoid Rs. 50 extra billing")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                                getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                            }

                            Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                            Global.CommModes = TextUtils.join(",", typeArray);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);
                            Log.e(TAG, "onClick: lab add and lab id " + getTSP_Address + labIDTopass);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", getFinalAge);
                            saveDetails.putString("gender", saveGenderId);
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", getAgeType);
                            saveDetails.putString("labname", "");
                            saveDetails.putString("labAddress", getTSP_Address);
                            saveDetails.putString("patientAddress", patientAddressdataToPass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", "");
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", pincode_pass);
                            saveDetails.putString("Communication", Global.Communication);
                            saveDetails.putString("CommModes", Global.CommModes);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();

        } else {*/

        if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
            getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
        }

        Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
        Global.CommModes = TextUtils.join(",", typeArray);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);
        Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass + labIDTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", getAgeType);
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", getTSP_Address);
        saveDetails.putString("patientAddress", patientAddressdataToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", "");
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", pincode_pass);
        saveDetails.putString("Communication", Global.Communication);
        saveDetails.putString("CommModes", Global.CommModes);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
//TODO Commented as per the input given on 14-03-2022

//        }
    }

    private void TTL_ILS() {
        if (woereferedby != null) {
            if (obj != null) {
                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                    if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                        referenceBy = woereferedby;
                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                    }
                }
            }
        } else {
            referenceBy = woereferedby;
            referredID = "";
        }


        String s1 = getLabName.substring(getLabName.indexOf("-") + 2);
        s1.trim();
        getLabCode = s1;
        if (labs == null) {
            labAddressTopass = "";
            labIDTopass = "";
        } else {
            if (labs.length != 0) {
                for (int i = 0; i < labs.length; i++) {
                    if (getLabCode.contains("-")) {
                        String s2 = getLabCode.substring(getLabCode.indexOf("-") + 2);
                        s2.trim();
                        getLabCode = s2;
                    }
                    if (getLabCode.equalsIgnoreCase(labs[i].getClientid())) {
                        getFullDataLabs = new LABS();
                        getFullDataLabs.setPincode(labs[i].getPincode());
                        getFullDataLabs.setPassingname(labs[i].getPassingname());
                        getFullDataLabs.setName(labs[i].getName());
                        getFullDataLabs.setMobile(labs[i].getMobile());
                        getFullDataLabs.setLabName(labs[i].getLabName());
                        getFullDataLabs.setLabAddress(labs[i].getLabAddress());
                        getFullDataLabs.setIdaddress(labs[i].getIdaddress());
                        getFullDataLabs.setEmail(labs[i].getEmail());
                        getFullDataLabs.setClientid(labs[i].getClientid());

                        labAddressTopass = getFullDataLabs.getLabAddress();
                        labIDTopass = getFullDataLabs.getIdaddress();
                        labLabNAmeTopass = getFullDataLabs.getLabName();
                    }
                }
            }

        }

        nameString = nameString.replaceAll("\\s+", " ");

        final String getAgeType = spinyr.getSelectedItem().toString();
        String sctDate = dateShow.getText().toString();

        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        final String getFinalAge = age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin;
        final String getFinalDate = dateShow.getText().toString();

        final String timetopass = DateUtils.Req_Date_Req(getFinalTime, "HH:mm", "hh:mm a");

        /*if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("You can register the PGC to avoid 10 Rs debit")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                            i.putExtra("name", nameString);
                            i.putExtra("age", getFinalAge);
                            i.putExtra("gender", saveGenderId);
                            i.putExtra("sct", timetopass);
                            i.putExtra("date", getFinalDate);
                            GlobalClass.setReferenceBy_Name = referenceBy;
                            startActivity(i);

                            Global.CommModes = TextUtils.join(",", typeArray);
                            Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                            saveDetails.putString("name", nameString);
                            saveDetails.putString("age", getFinalAge);
                            saveDetails.putString("gender", saveGenderId);
                            saveDetails.putString("sct", timetopass);
                            saveDetails.putString("date", getFinalDate);
                            saveDetails.putString("ageType", getAgeType);
                            saveDetails.putString("labname", labLabNAmeTopass);
                            saveDetails.putString("labAddress", labAddressTopass);
                            saveDetails.putString("patientAddress", labAddressTopass);
                            saveDetails.putString("refBy", referenceBy);
                            saveDetails.putString("refId", referredID);
                            saveDetails.putString("labIDaddress", labIDTopass);
                            saveDetails.putString("btechIDToPass", btechIDToPass);
                            saveDetails.putString("btechNameToPass", btechnameTopass);
                            saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
                            saveDetails.putString("kycinfo", kycdata);
                            saveDetails.putString("woetype", typename);
                            saveDetails.putString("WOEbrand", brandNames);
                            saveDetails.putString("SR_NO", getVial_numbver);
                            saveDetails.putString("pincode", "");
                            saveDetails.putString("Communication", Global.Communication);
                            saveDetails.putString("CommModes", Global.CommModes);
                            saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
                            saveDetails.commit();
                            sDialog.dismissWithAnimation();
                        }
                    }).show();
        } else {*/
        Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
        i.putExtra("name", nameString);
        i.putExtra("age", getFinalAge);
        i.putExtra("gender", saveGenderId);
        i.putExtra("sct", timetopass);
        i.putExtra("date", getFinalDate);
        GlobalClass.setReferenceBy_Name = referenceBy;
        startActivity(i);

        Global.CommModes = TextUtils.join(",", typeArray);
//            GlobalClass.Req_Date_Req(getFinalTime, "hh:mm a", "HH:mm:ss");
        Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", nameString);
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);
        saveDetails.putString("sct", timetopass);
        saveDetails.putString("date", getFinalDate);
        saveDetails.putString("ageType", getAgeType);
        saveDetails.putString("labname", labLabNAmeTopass);
        saveDetails.putString("labAddress", labAddressTopass);
        saveDetails.putString("patientAddress", labAddressTopass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", labIDTopass);
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("btechNameToPass", btechnameTopass);
        saveDetails.putString("getcampIDtoPass", getcampIDtoPass);
        saveDetails.putString("kycinfo", kycdata);
        saveDetails.putString("woetype", typename);
        saveDetails.putString("WOEbrand", brandNames);
        saveDetails.putString("SR_NO", getVial_numbver);
        saveDetails.putString("pincode", "");
        saveDetails.putString("Communication", Global.Communication);
        saveDetails.putString("CommModes", Global.CommModes);
        saveDetails.putString("EMAIL_ID", "" + edt_email.getText().toString());
        saveDetails.commit();
//        }
    }

    private void showDialog(Activity activity, Leads[] leads) {
        CustomLeaddialog = new Dialog(activity);
        CustomLeaddialog.setContentView(R.layout.leadid_recyclerview);
        CustomLeaddialog.setCancelable(false);

        Window window = CustomLeaddialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView ic_close = CustomLeaddialog.findViewById(R.id.img_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomLeaddialog.dismiss();
                vial_number.getText().clear();
                id_for_woe.getText().clear();
            }
        });

        for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
            if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                sizeflag = true;
                break;
            }
        }
        RecyclerView recyclerView = CustomLeaddialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        AdapterRe adapterRe = new AdapterRe((ManagingTabsActivity) activity, leads, getVial_numbver, leadOrderIdMainModel, Start_New_Woe.this);

        adapterRe.leadclick(new AdapterRe.OnItemClickListener() {
            @Override
            public void onItemClicked() {
                CustomLeaddialog.dismiss();
                selectTypeSpinner.setSelection(0);
                brand_spinner.setSelection(0);
                vial_number.getText().clear();
                id_for_woe.getText().clear();
            }
        });

        recyclerView.setAdapter(adapterRe);

        if (!activity.isFinishing() && CustomLeaddialog != null && !CustomLeaddialog.isShowing()) {
            CustomLeaddialog.show();
        }
    }

    private void RecheckType(String passBarcode) {
        requestQueueAddRecheck = GlobalClass.setVolleyReq(activity);
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        String url = Api.addTestsUsingBarcode + api_key + "/" + user + "/" + passBarcode + "/getbarcodedtl";
        Log.e(TAG, "RECHEKC API ====>" + url);
        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: RESPONSE" + response);
                try {
                    GlobalClass.hideProgress(activity, progressDialog);
                    if (response != null) {
                        Gson gson = new Gson();
                        GetBarcodeDetailsResponseModel responseModel = gson.fromJson(String.valueOf(response), GetBarcodeDetailsResponseModel.class);

                        if (responseModel != null) {
                            ALERT = responseModel.getALERT();
                            BARCODE = responseModel.getBARCODE();
                            BVT_HRS = responseModel.getBVT_HRS();
                            LABCODE = responseModel.getLABCODE();
                            PATIENT = responseModel.getPATIENT();
                            REF_DR = responseModel.getREF_DR();
                            REQUESTED_ADDITIONAL_TESTS = responseModel.getREQUESTED_ADDITIONAL_TESTS();
                            REQUESTED_ON = responseModel.getREQUESTED_ON();
                            RES_ID = responseModel.getRES_ID();
                            SDATE = responseModel.getSDATE();
                            SL_NO = responseModel.getSL_NO();
                            STATUS = responseModel.getSTATUS();
                            SU_CODE1 = responseModel.getSU_CODE1();
                            SU_CODE2 = responseModel.getSU_CODE2();
                            TESTS = responseModel.getTESTS();

                            if (RES_ID != null && RES_ID.equalsIgnoreCase(Constants.RES0000)) {
                                if (STATUS.equalsIgnoreCase(caps_invalidApikey)) {
                                    GlobalClass.redirectToLogin(activity);
                                } else if (GlobalClass.isNull(PATIENT)) {
                                    TastyToast.makeText(activity, responseModel.getSTATUS(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    barcode_woe.setText("");
                                    leadbarcodelayout.setVisibility(View.GONE);
                                    next_btn.setVisibility(View.GONE);
                                } else {
                                    leadbarcodelayout.setVisibility(View.VISIBLE);
                                    next_btn.setVisibility(View.VISIBLE);
                                    leadbarcodename.setText("Name: " + PATIENT);
                                    leadidbarcodetest.setText("Tests: " + TESTS);
                                    leadbarcoderefdr.setText("Ref By: " + REF_DR);
                                    leadbarcodesct.setText("SCT: " + SDATE);
                                }
                            } else if (STATUS != null && STATUS.equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(activity);
                            } else {
                                barcode_woe.setText("");
                                TastyToast.makeText(activity, responseModel.getSTATUS(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        } else {
                            TastyToast.makeText(activity, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        TastyToast.makeText(activity, ToastFile.something_went_wrong, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (Exception e) {
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(activity, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }

                }
            }
        });

        jsonObjectRequestProfile.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueueAddRecheck.add(jsonObjectRequestProfile);
        jsonObjectRequestProfile.setShouldCache(false);
        Log.e(TAG, "RecheckType: URL" + jsonObjectRequestProfile);
    }

    private void getTspNumber() {
        try {
            RequestQueue requestQueue = GlobalClass.setVolleyReq(activity);
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + "" + api_key + "/" + "" + user +
                    "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e(TAG, "onResponse: RESPONSE" + response);
                        String getResponse = response.optString("RESPONSE", "");

                        if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                            GlobalClass.redirectToLogin(activity);
                        } else {
                            Gson gson = new Gson();
                            MyPojo myPojo = gson.fromJson(response.toString(), MyPojo.class);

                            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                            SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                            Gson gson22 = new Gson();
                            String json22 = gson22.toJson(myPojo);
                            prefsEditor1.putString("getBtechnames", json22);

                            prefsEditor1.commit();

                            getBtechList = new ArrayList<>();
                            if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getBCT_LIST() != null) {
                                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                    getBtechList.add(myPojo.getMASTERS().getBCT_LIST()[j]);
                                }
                            } else {
                                BCT_LIST bct_list = new BCT_LIST();
                                bct_list.setMOBILE_NUMBER(mobile);
                                bct_list.setNAME(nameofProfile);
                                getBtechList.add(bct_list);
                            }

                            btechSpinner = new ArrayList<>();

                            if (getBtechList != null && getBtechList.size() != 0) {
                                for (int i = 0; i < getBtechList.size(); i++) {
                                    btechSpinner.add(getBtechList.get(i).getNAME());
                                    btechname.setAdapter(new ArrayAdapter<String>(activity, R.layout.spinner_item, btechSpinner));
                                }
                            } else {
                                BCT_LIST bct_list = new BCT_LIST();
                                bct_list.setMOBILE_NUMBER(mobile);
                                bct_list.setNAME(nameofProfile);
                                getBtechList.add(bct_list);

                            }
                        }


                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        // Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
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
            requestQueue.add(jsonObjectRequest2);
            jsonObjectRequest2.setShouldCache(false);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchData() {
        RequestQueue requestQueue = GlobalClass.setVolleyReq(activity);
        barProgressDialog = new ProgressDialog(activity);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        Log.e(TAG, Api.Cloud_base + "" + api_key + "/" + "" + user + "/B2BAPP/getclients");
        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + "" + api_key + "/" + "" + user +
                "/B2BAPP/getclients", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                Log.e(TAG, "getclient onResponse: RESPONSE" + response);
                autotextcompletefunction(response);
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

        requestQueue.add(jsonObjectRequestfetchData);
        GlobalClass.volleyRetryPolicy(jsonObjectRequestfetchData);
        Log.e(TAG, "fetchData: URL" + jsonObjectRequestfetchData);


    }

    private void autotextcompletefunction(JSONObject response) {
        try {
            Gson gson = new Gson();
            if (response != null) {
                sourceILSMainModel = gson.fromJson(response.toString(), SourceILSMainModel.class);
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                Gson gson22 = new Gson();
                String json22 = gson22.toJson(sourceILSMainModel);
                prefsEditor1.putString("savelabnames", json22);
                prefsEditor1.commit();
                if (sourceILSMainModel != null) {
                    callAdapter(sourceILSMainModel);
                }
                SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(activity);
                Gson gsondata = new Gson();
                String jsondata = appSharedPrefsdata.getString("savelabnames", "");
                obj = gsondata.fromJson(jsondata, SourceILSMainModel.class);
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        dateShow.setText(putDate);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void callAdapter(final SourceILSMainModel obj) {
//        if (obj.getMASTERS().getLABS().length != 0) {
        // if (obj.getMASTERS().getLABS().length != 0 && obj.getMASTERS().getLABS() != null) {
        if (obj.getMASTERS().getLABS().length != 0 && obj.getMASTERS().getLABS() != null) {
            getReferenceNmae = new ArrayList<>();
            getLabNmae = new ArrayList<>();
            statusForColor = new ArrayList<>();

            for (int i = 0; i < obj.getMASTERS().getLABS().length; i++) {

                SpannableStringBuilder builder = new SpannableStringBuilder();

                getLabNmae.add(obj.getMASTERS().getLABS()[i].getLabName() + "-" + " " + obj.getMASTERS().getLABS()[i].getClientid());
                statusForColor.add(obj.getMASTERS().getLABS()[i].getStatus());
                labs = obj.getMASTERS().getLABS();
            }

//            samplecollectionponit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showDialogue(obj);
//                }
//            });
            LinearLayoutManager linearLayoutManager = null;
            linearLayoutManager = new LinearLayoutManager(this.activity);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            scp_name.setLayoutManager(linearLayoutManager);

            labDetailsArrayList = new ArrayList<>();
            if (obj.getMASTERS() != null) {
                for (int i = 0; i < obj.getMASTERS().getLABS().length; i++) {
                    labDetailsArrayList.add(obj.getMASTERS().getLABS()[i]);
                }
            }

            final SampleCollectionAdapter sampleCollectionAdapter = new SampleCollectionAdapter(activity, labDetailsArrayList);
            sampleCollectionAdapter.setOnItemClickListener(new SampleCollectionAdapter.OnItemClickListener() {
                @Override
                public void onPassSgcID(LABS pos) {
                    samplecollectionponit.setText(pos.getLabName() + " - " + pos.getClientid());
                    selectedLABS = pos;
                    scp_name.setVisibility(View.GONE);
                }

           /* @Override
            public void onPassSgcName(String name) {
                samplecollectionponit.setText(name);
                alertDialog.dismiss();
            }*/
            });
            scp_name.setAdapter(sampleCollectionAdapter);

            samplecollectionponit.addTextChangedListener(new TextWatcher() {
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
                    scp_name.setVisibility(View.VISIBLE);
                    String s1 = s.toString().toLowerCase();

                    filterPatientsArrayList = new ArrayList<>();
                    String labname = "";
                    String clientId = "";
                    if (labDetailsArrayList != null) {
                        for (int i = 0; i < labDetailsArrayList.size(); i++) {

                            final String text1 = labDetailsArrayList.get(i).getLabName().toLowerCase();
                            final String text2 = labDetailsArrayList.get(i).getClientid().toLowerCase();

                            if (labDetailsArrayList.get(i).getClientid() != null || !labDetailsArrayList.get(i).getClientid().equals("")) {
                                clientId = labDetailsArrayList.get(i).getClientid().toLowerCase();
                            }
                            if (labDetailsArrayList.get(i).getLabName() != null || !labDetailsArrayList.get(i).getLabName().equals("")) {
                                labname = labDetailsArrayList.get(i).getLabName().toLowerCase();
                            }

                            if (text1.contains(s1) || (clientId != null && clientId.contains(s1)) ||
                                    (labname != null && labname.contains(s1))) {
                                String testname = (labDetailsArrayList.get(i).getLabName());
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

           /* referedbyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinnerDialogRef.showSpinerDialog();
                }
            });*/


//            spinnerDialog = new SpinnerDialog(activity, getLabNmae, "Search SCP", "Close");// With No Animation
//            spinnerDialog = new SpinnerDialog(activity, getLabNmae, "Search SCP", R.style.DialogAnimations_SmileWindow, "Close");// With Animation


            spinnerDialogRef = new SpinnerDialog(activity, getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(activity, getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// WithAnimation

//            spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
//                @Override
//                public void onClick(String s, int i) {
//                    samplecollectionponit.setText(s);
//                }
//            });
/*
            spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    samplecollectionponit.setText(item);
                }
            });*/

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    referedbyText.setText(s);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    referedbyText.setText(item);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });

        } else {
            if (!covidacc) {
                final AlertDialog alertDialog = new AlertDialog.Builder(
                        mContext).create();

                // Setting Dialog Title
                alertDialog.setTitle("Register SCP");

                // Setting Dialog Message
                alertDialog.setMessage(ToastFile.scp_not_mapped);
                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        // Write your code here to execute after dialog closed
                    }
                });

                alertDialog.show();
                samplecollectionponit.setEnabled(false);
//            TastyToast.makeText(activity, ToastFile.no_data_fnd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        }

        getReferenceNmae = new ArrayList<>();
        getReferenceName1 = new ArrayList<>();

        if (obj.getMASTERS().getREF_DR() != null && obj.getMASTERS().getREF_DR().length != 0) {
            for (int j = 0; j < obj.getMASTERS().getREF_DR().length; j++) {
                getReferenceNmae.add(obj.getMASTERS().getREF_DR()[j].getName());
                getReferenceName1.add(obj.getMASTERS().getREF_DR()[j]);
                ref_drs = obj.getMASTERS().getREF_DR();
            }
        } else {
            ref_check_linear.setVisibility(View.VISIBLE);
            refby_linear.setVisibility(View.GONE);
//            TastyToast.makeText(activity, ToastFile.no_data_fnd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }


        CustomListAdapter adapter = new CustomListAdapter((Context) mContext, R.layout.autocompleteitem, getReferenceName1, getReferenceNmae);
        referedbyText.setThreshold(1);//will start working from first character
        referedbyText.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        referedbyText.setTextColor(Color.BLACK);
    }

    private void showDialogue(final SourceILSMainModel obj) {

        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.custom_alert_scp, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();

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
        linearLayoutManager = new LinearLayoutManager(this.activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scp_name.setLayoutManager(linearLayoutManager);


        title.setText("Search SCP");

        labDetailsArrayList = new ArrayList<>();
        if (obj.getMASTERS() != null) {
            for (int i = 0; i < obj.getMASTERS().getLABS().length; i++) {
                labDetailsArrayList.add(obj.getMASTERS().getLABS()[i]);
            }
        }

        final SampleCollectionAdapter sampleCollectionAdapter = new SampleCollectionAdapter(activity, labDetailsArrayList);
        sampleCollectionAdapter.setOnItemClickListener(new SampleCollectionAdapter.OnItemClickListener() {
            @Override
            public void onPassSgcID(LABS pos) {
                samplecollectionponit.setText(pos.getLabName() + " - " + pos.getClientid());
                selectedLABS = pos;
                alertDialog.dismiss();
            }

           /* @Override
            public void onPassSgcName(String name) {
                samplecollectionponit.setText(name);
                alertDialog.dismiss();
            }*/
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

                        final String text1 = labDetailsArrayList.get(i).getLabName().toLowerCase();
                        final String text2 = labDetailsArrayList.get(i).getClientid().toLowerCase();

                        if (labDetailsArrayList.get(i).getClientid() != null || !labDetailsArrayList.get(i).getClientid().equals("")) {
                            clientId = labDetailsArrayList.get(i).getClientid().toLowerCase();
                        }
                        if (labDetailsArrayList.get(i).getLabName() != null || !labDetailsArrayList.get(i).getLabName().equals("")) {
                            labname = labDetailsArrayList.get(i).getLabName().toLowerCase();
                        }

                        if (text1.contains(s1) || (clientId != null && clientId.contains(s1)) ||
                                (labname != null && labname.contains(s1))) {
                            String testname = (labDetailsArrayList.get(i).getLabName());
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

    @Override
    public void onResume() {
        super.onResume();
        GlobalClass.isAutoTimeSelected(activity);
    }


   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getView() != null) {
            isViewShown = true;
            if (GlobalClass.isNetworkAvailable(activity)) {
                ((ManagingTabsActivity) activity).getProfileDetails(activity);
            }
        } else {
            isViewShown = false;
        }
    }*/

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendotp:
                if (!GlobalClass.isNetworkAvailable(activity)) {
                    TastyToast.makeText(activity, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {

                    if (mobno_verify) {

                        if (btn_snd_otp.getText().equals("Reset")) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                            alert.setMessage("Are you sure you want to reset?");
                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e(TAG, "onClick: " + btn_snd_otp.getText().toString());
                                    Disablefields();

                                    et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                    et_mobno.setEnabled(true);
                                    et_mobno.setClickable(true);

                                    btn_snd_otp.setEnabled(true);
                                    btn_snd_otp.setClickable(true);

                                    lin_otp.setVisibility(View.GONE);


                                    et_otp.setText("");
                                    btn_snd_otp.setText("Send OTP");
                                    lin_ckotp.setVisibility(View.VISIBLE);

                                    btn_verifyotp.setVisibility(View.GONE);
                                    lin_otp.setVisibility(View.GONE);

                                    dialog.dismiss();
                                }
                            });

                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = alert.create();
                            dialog.show();

                        } else if (btn_snd_otp.getText().equals("Resend OTP")) {
                            Log.e(TAG, "onClick: " + btn_snd_otp.getText().toString());

                            if (cd.isConnectingToInternet()) {
                                generateToken();
                            } else {
                                Global.showCustomToast(activity, ToastFile.intConnection);
                            }
                        } else if (btn_snd_otp.getText().equals("Send OTP")) {
                            Log.e(TAG, "onClick: " + btn_snd_otp.getText().toString());
                            if (cd.isConnectingToInternet()) {
                                generateToken();
                            } else {
                                Global.showCustomToast(activity, ToastFile.intConnection);
                            }
                        }
                    }
                }

                break;

            case R.id.btn_verifyotp:

                if (GlobalClass.isNull(et_otp.getText().toString())) {
                    Toast.makeText(activity, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        TastyToast.makeText(activity, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        if (ControllersGlobalInitialiser.verifyotpController != null) {
                            ControllersGlobalInitialiser.verifyotpController = null;
                        }
                        ControllersGlobalInitialiser.verifyotpController = new VerifyotpController(Start_New_Woe.this);
                        ControllersGlobalInitialiser.verifyotpController.verifyotp(user, et_mobno.getText().toString(), et_otp.getText().toString());
                    }
                }
                break;

        }
    }

    private void generateToken() {
        PackageInfo pInfo = null;
        try {
            pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        int versionCode = pInfo.versionCode;
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getContext());
        PostAPIInterface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.THYROCARE).create(PostAPIInterface.class);
        OTPrequest otPrequest = new OTPrequest();
        otPrequest.setAppId(OTPAPPID);
        otPrequest.setPurpose("OTP");
        otPrequest.setVersion("" + versionCode);
        Call<Tokenresponse> responseCall = apiInterface.getotptoken(otPrequest);
        Log.e(TAG, "TOKEN LIST BODY ---->" + new GsonBuilder().create().toJson(otPrequest));
        Log.e(TAG, "TOKEN LIST URL ---->" + responseCall.request().url());

        responseCall.enqueue(new Callback<Tokenresponse>() {
            @Override
            public void onResponse(Call<Tokenresponse> call, retrofit2.Response<Tokenresponse> response) {
                GlobalClass.hideProgress(getContext(), progressDialog);
                try {
                    if (response.body().getRespId().equalsIgnoreCase(Constants.RES0000)) {
                        if (!GlobalClass.isNull(response.body().getToken())) {
                            Log.e(TAG, "TOKEN--->" + response.body().getToken());
                            callsendOTP(response.body().getToken(), response.body().getRequestId());

                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Tokenresponse> call, Throwable t) {

            }
        });
    }

    private void callsendOTP(String token, String requestId) {

        if (et_mobno.getText().toString().equals("")) {
            Toast.makeText(activity, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        } else if (et_mobno.getText().toString().length() < 10) {
            Toast.makeText(activity, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            lin_otp.setVisibility(View.GONE);
        } else {

            if (!GlobalClass.isNetworkAvailable(activity)) {
                GlobalClass.showAlertDialog(activity);
            } else {
                if (ControllersGlobalInitialiser.validateMob_controller != null) {
                    ControllersGlobalInitialiser.validateMob_controller = null;
                }
                ControllersGlobalInitialiser.validateMob_controller = new ValidateMob_Controller(Start_New_Woe.this);
                ControllersGlobalInitialiser.validateMob_controller.callvalidatemob(user, et_mobno.getText().toString(), token);
            }


        }
    }

    public void onvalidatemob(ValidateOTPmodel validateOTPmodel, ProgressDialog progressDialog) {

        if (validateOTPmodel.getResponseId().equals(Constants.RES0001)) {
            GlobalClass.hideProgress(activity, progressDialog);


        /*    et_mobno.setEnabled(true);
            et_mobno.setClickable(true);
            btn_sendotp.setText("Send OTP");
            et_otp.setText("");
            et_otp.setVisibility(View.GONE);

            btn_verifyotp.setVisibility(View.GONE);
            lin_otp.setVisibility(View.GONE);*/


            et_mobno.setEnabled(false);
            et_mobno.setClickable(false);

            lin_otp.setVisibility(View.VISIBLE);

            et_otp.setEnabled(true);
            et_otp.setClickable(true);

            btn_verifyotp.setEnabled(true);
            btn_verifyotp.setClickable(true);

            btn_verifyotp.setVisibility(View.VISIBLE);
            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.maroon));
            btn_verifyotp.setText("Verify");

            setCountDownTimer();


        } else {
            et_mobno.setEnabled(true);
            et_mobno.setClickable(true);
            GlobalClass.hideProgress(activity, progressDialog);
        }
    }


    public void onVerifyotp(VerifyotpModel validateOTPmodel) {
        if (validateOTPmodel.getResponse().equals("OTP Validated Successfully")) {
            timerflag = true;
            Global.OTPVERIFIED = true;
            Toast.makeText(activity,
                    validateOTPmodel.getResponse(),
                    Toast.LENGTH_SHORT).show();

            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer = null;
            }

            btn_verifyotp.setText("Verified");
            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.green));


            et_otp.getText().clear();
            lin_otp.setVisibility(View.GONE);

            btn_snd_otp.setText("Reset");

            et_mobno.setEnabled(false);
            et_mobno.setClickable(false);

            imgotp = getContext().getResources().getDrawable(R.drawable.otpverify);
            imgotp.setBounds(40, 40, 40, 40);
            et_mobno.setCompoundDrawablesWithIntrinsicBounds(null, null, imgotp, null);

            et_otp.setEnabled(false);
            et_otp.setClickable(false);

            btn_snd_otp.setClickable(true);
            btn_snd_otp.setEnabled(true);

            btn_verifyotp.setClickable(false);
            btn_verifyotp.setEnabled(false);

            tv_timer.setVisibility(View.GONE);

            lin_ckotp.setVisibility(View.GONE);

            Enablefields();

        } else {
            et_otp.setText("");
        }
    }

    private void setCountDownTimer() {
        tv_timer.setVisibility(View.VISIBLE);
        yourCountDownTimer = new CountDownTimer(60000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;

                if (lin_otp.getVisibility() == View.VISIBLE) {
                    tv_timer.setVisibility(View.VISIBLE);
                    tv_timer.setText("Please wait 00:" + numberFormat.format(time));

                } else {
                    tv_timer.setVisibility(View.GONE);
                }

                btn_snd_otp.setEnabled(false);
                btn_snd_otp.setClickable(false);
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                btn_snd_otp.setText("Resend OTP");

                et_otp.getText().clear();
                lin_otp.setVisibility(View.VISIBLE);

                btn_snd_otp.setEnabled(true);
                btn_snd_otp.setClickable(true);

                et_mobno.setEnabled(true);
                et_mobno.setClickable(true);

                et_otp.setEnabled(true);
                et_otp.setClickable(true);

            }
        }.start();
    }

    public void Disablefields() {

        vial_number.getText().clear();
        vial_number.setEnabled(false);
        vial_number.setClickable(false);

        et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_mobno.getText().clear();
        et_mobno.setEnabled(true);
        et_mobno.setClickable(true);

        lin_otp.setVisibility(View.GONE);
        et_otp.getText().clear();
        et_otp.setEnabled(false);
        et_otp.setClickable(false);

        name.getText().clear();
        name.setEnabled(false);
        name.setClickable(false);

        name.setEnabled(false);
        name.setClickable(false);

        age.getText().clear();
        age.setEnabled(false);
        age.setClickable(false);

        patientAddress.getText().clear();
        patientAddress.setEnabled(false);
        patientAddress.setClickable(false);

        referedbyText.getText().clear();
        referedbyText.setEnabled(false);
        referedbyText.setClickable(false);

        male.setEnabled(false);
        male.setClickable(false);

        female.setEnabled(false);
        female.setClickable(false);


        dateShow.setEnabled(false);
        dateShow.setClickable(false);

        spinyr.setEnabled(false);
        spinyr.setClickable(false);

        // patientAddress.setEnabled(false);
        // patientAddress.setClickable(false);
        pincode_edt.getText().clear();
        pincode_edt.setEnabled(false);
        pincode_edt.setClickable(false);


        btechname.setEnabled(false);
        btechname.setEnabled(false);
        btechname.setSelection(0);


        timehr.setEnabled(false);
        timehr.setClickable(false);
        timehr.setSelection(0);

        timesecond.setEnabled(false);
        timesecond.setClickable(false);
        timesecond.setSelection(0);

        timeampm.setEnabled(false);
        timeampm.setClickable(false);
        timeampm.setSelection(0);

        ref_check.setEnabled(false);
        ref_check.setClickable(false);

        ref_check_linear.setEnabled(false);
        ref_check_linear.setClickable(false);

        next_btn.setEnabled(false);
        next_btn.setClickable(false);

//        btn_clear_data.setEnabled(false);
//        btn_clear_data.setClickable(false);

        refby_linear.setVisibility(View.VISIBLE);
        referedbyText.setVisibility(View.VISIBLE);

        uncheck_ref.setVisibility(View.VISIBLE);
        ref_check.setVisibility(View.GONE);

/*        btn_next.setClickable(false);
        btn_next.setEnabled(false);

        uncheck_sct.setEnabled(false);
        uncheck_sct.setClickable(false);

        check_sct.setEnabled(false);
        check_sct.setClickable(false);*/


    }

    public void Enablefields() {
        vial_number.setEnabled(true);
        vial_number.setClickable(true);

       /* uncheck_sct.setEnabled(true);
        uncheck_sct.setClickable(true);

        check_sct.setEnabled(true);
        check_sct.setClickable(true);*/

        name.setEnabled(true);
        name.setClickable(true);

        age.setEnabled(true);
        age.setClickable(true);


        referedbyText.setEnabled(true);
        referedbyText.setClickable(true);


        male.setEnabled(true);
        male.setClickable(true);

        female.setEnabled(true);
        female.setClickable(true);

        dateShow.setEnabled(true);
        dateShow.setClickable(true);

        spinyr.setEnabled(true);
        spinyr.setClickable(true);

        timehr.setEnabled(true);
        timehr.setClickable(true);

        timesecond.setEnabled(true);
        timesecond.setClickable(true);


        timeampm.setEnabled(true);
        timeampm.setClickable(true);

        ref_check.setEnabled(true);
        ref_check.setClickable(true);

        ref_check_linear.setEnabled(true);
        ref_check_linear.setClickable(true);

        next_btn.setClickable(true);
        next_btn.setEnabled(true);

        btn_clear_data.setEnabled(true);
        btn_clear_data.setClickable(true);


        patientAddress.setEnabled(true);
        patientAddress.setClickable(true);

        pincode_edt.setEnabled(true);
        pincode_edt.setClickable(true);


        btechname.setEnabled(true);
        btechname.setEnabled(true);
    }

    private void callLedgerDetailsAPI() {
        try {
            GetLeadgerDetailsController getLeadgerDetailsController = new GetLeadgerDetailsController(activity, Start_New_Woe.this);
            getLeadgerDetailsController.getLedgerBal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLeaderDetails(GetLeadgerBalnce responseModel) {
        try {
            Global.setTextview(tv_unbilled, activity.getString(R.string.unbilled_woe_materials) + " " + GlobalClass.currencyFormat(String.valueOf(responseModel.getTodayBill())));
            Global.setTextview(tv_avl_bal, activity.getString(R.string.available_balance_with_cl) + " " + GlobalClass.currencyFormat(String.valueOf(responseModel.getBalance())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private String mm11;
        private String dd11;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(activity, this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            if (month < 10) {

                mm11 = "0" + month;
            }
            if (day < 10) {

                dd11 = "0" + day;
            }
            dateShow.setText(dd11 + "-" + mm11 + "-" + year);
        }

    }

    private void LeadIdwoe() {
        Enablefields();
        vial_number.getText().toString();
        id_for_woe.getText().clear();
        ll_communication_main.setVisibility(View.GONE);
        ll_mobileno_otp.setVisibility(View.GONE);
        ll_email.setVisibility(View.GONE);
        tv_mob_note.setVisibility(View.GONE);
        leadlayout.setVisibility(View.GONE);
        id_layout.setVisibility(View.VISIBLE);
        barcode_layout.setVisibility(View.GONE);
        pincode_linear_data.setVisibility(View.GONE);
        leadbarcodelayout.setVisibility(View.GONE);
        leadlayout.setVisibility(View.GONE);
        mobile_number_kyc.setVisibility(View.GONE);
        Home_mobile_number_kyc.setVisibility(View.GONE);
        camp_layout_woe.setVisibility(View.GONE);
        btech_linear_layout.setVisibility(View.GONE);
        home_layout.setVisibility(View.GONE);
        labname_linear.setVisibility(View.GONE);
        patientAddress.setText("");
        ref_check_linear.setVisibility(View.GONE);
        refby_linear.setVisibility(View.GONE);
        namePatients.setVisibility(View.GONE);
        AGE_layout.setVisibility(View.GONE);
        vial_number.setVisibility(View.VISIBLE);
        time_layout.setVisibility(View.GONE);
        next_btn.setVisibility(View.GONE);
        referenceBy = "";


        id_for_woe.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(activity,
                            ToastFile.ent_pin,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        id_for_woe.setText(enteredString.substring(1));
                    } else {
                        id_for_woe.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                //getData = reg_pincode.getText().toString();

                if (s.length() < 10) {
                    leadlayout.setVisibility(View.GONE);
                }
                if (s.length() == 10) {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        GlobalClass.showAlertDialog(activity);
                    } else {
                        getVial_numbver = vial_number.getText().toString();
                        if (!GlobalClass.isNull(getVial_numbver)) {
                            final ProgressDialog pd_dialog = GlobalClass.ShowprogressDialog(activity);
                            String getId = s.toString();
                            String getLeadId = getId.toString();
                            requestQueueNoticeBoard = GlobalClass.setVolleyReq(activity);
                            JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.ValidateWorkOrderLeadId + api_key
                                    + "/" + user + "/" + getLeadId + "/" + brand_spinner.getSelectedItem().toString() + "/getorderdetails", new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    String getResponse = response.optString("RESPONSE", "");

                                    if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                                        GlobalClass.redirectToLogin(activity);
                                    } else {
                                        Gson gson = new Gson();
                                        Log.e(TAG, "onResponse: RESPONSE" + response);

                                        leadOrderIdMainModel = new LeadOrderIdMainModel();
                                        leadOrderIdMainModel = gson.fromJson(response.toString(), LeadOrderIdMainModel.class);


                                        if (!GlobalClass.isNull(leadOrderIdMainModel.getRESPONSE()) && leadOrderIdMainModel.getRESPONSE().equals("SUCCESS")) {
                                            GlobalClass.hideProgress(activity, pd_dialog);

                                            for (int i = 0; i < leadOrderIdMainModel.getLeads().length; i++) {


                                                SharedPreferences.Editor editor = activity.getSharedPreferences("LeadOrderID", 0).edit();
                                                editor.putString("brandtype", brand_spinner.getSelectedItem().toString());
                                                editor.putString("ADDRESS", leadOrderIdMainModel.getLeads()[i].getADDRESS());
                                                editor.putString("AGE", leadOrderIdMainModel.getLeads()[i].getAGE());
                                                editor.putString("AGE_TYPE", leadOrderIdMainModel.getLeads()[i].getAGE_TYPE());
                                                editor.putString("BCT", leadOrderIdMainModel.getLeads()[i].getBCT());
                                                editor.putString("EDTA", leadOrderIdMainModel.getLeads()[i].getEDTA());
                                                editor.putString("EMAIL", leadOrderIdMainModel.getLeads()[i].getEMAIL());
                                                editor.putString("ERROR", leadOrderIdMainModel.getLeads()[i].getERROR());
                                                editor.putString("FLUORIDE", leadOrderIdMainModel.getLeads()[i].getFLUORIDE());
                                                editor.putString("GENDER", leadOrderIdMainModel.getLeads()[i].getGENDER());
                                                editor.putString("HEPARIN", leadOrderIdMainModel.getLeads()[i].getHEPARIN());

                                                editor.putString("LAB_ID", leadOrderIdMainModel.getLeads()[i].getLAB_ID());
                                                editor.putString("LAB_NAME", leadOrderIdMainModel.getLeads()[i].getLAB_NAME());
                                                editor.putString("LEAD_ID", leadOrderIdMainModel.getLeads()[i].getLEAD_ID());
                                                editor.putString("MOBILE", leadOrderIdMainModel.getLeads()[i].getMOBILE());
                                                editor.putString("NAME", leadOrderIdMainModel.getLeads()[i].getNAME());
                                                editor.putString("ORDER_NO", leadOrderIdMainModel.getLeads()[i].getORDER_NO());
                                                editor.putString("PACKAGE", leadOrderIdMainModel.getLeads()[i].getPACKAGE());
                                                editor.putString("PINCODE", leadOrderIdMainModel.getLeads()[i].getPINCODE());
                                                editor.putString("PRODUCT", leadOrderIdMainModel.getLeads()[i].getPRODUCT());
                                                editor.putString("RATE", leadOrderIdMainModel.getLeads()[i].getRATE());

                                                editor.putString("REF_BY", leadOrderIdMainModel.getLeads()[i].getREF_BY());
                                                editor.putString("RESPONSE", leadOrderIdMainModel.getLeads()[i].getRESPONSE());
                                                editor.putString("SAMPLE_TYPE", leadOrderIdMainModel.getLeads()[i].getSAMPLE_TYPE());
                                                editor.putString("SCT", leadOrderIdMainModel.getLeads()[i].getSCT());
                                                editor.putString("SERUM", leadOrderIdMainModel.getLeads()[i].getSERUM());
                                                editor.putString("TESTS", leadOrderIdMainModel.getLeads()[i].getTESTS());
                                                editor.putString("TYPE", leadOrderIdMainModel.getLeads()[i].getTYPE());
                                                editor.putString("URINE", leadOrderIdMainModel.getLeads()[i].getURINE());
                                                editor.putString("WATER", leadOrderIdMainModel.getLeads()[i].getWATER());
                                                String json = new Gson().toJson(leadOrderIdMainModel.getLeads()[i].getLeadData());
                                                editor.putString("leadData", json);
                                                editor.commit();

                                            }

                                            if (leadOrderIdMainModel != null && leadOrderIdMainModel.getLeads() != null && leadOrderIdMainModel.getLeads().length >= 2) {
                                                showDialog(activity, leadOrderIdMainModel.getLeads());
                                            }

                                            SharedPreferences sharedPreferences = activity.getSharedPreferences("LeadOrderID", MODE_PRIVATE);
                                            brandtype = sharedPreferences.getString("brandtype", null);
                                            leadAddress = sharedPreferences.getString("ADDRESS", null);
                                            leadAGE = sharedPreferences.getString("AGE", null);
                                            leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", null);
                                            leadBCT = sharedPreferences.getString("BCT", null);
                                            leadEDTA = sharedPreferences.getString("EDTA", null);
                                            leadEMAIL = sharedPreferences.getString("EMAIL", null);
                                            leadERROR = sharedPreferences.getString("ERROR", null);
                                            leadFLUORIDE = sharedPreferences.getString("FLUORIDE", null);
                                            leadGENDER = sharedPreferences.getString("GENDER", null);
                                            leadHEPARIN = sharedPreferences.getString("HEPARIN", null);

                                            leadLAB_ID = sharedPreferences.getString("LAB_ID", null);
                                            leadLAB_NAME = sharedPreferences.getString("LAB_NAME", null);
                                            leadLEAD_ID = sharedPreferences.getString("LEAD_ID", null);
                                            leadMOBILE = sharedPreferences.getString("MOBILE", null);
                                            leadNAME = sharedPreferences.getString("NAME", null);
                                            leadORDER_NO = sharedPreferences.getString("ORDER_NO", null);
                                            leadPACKAGE = sharedPreferences.getString("PACKAGE", null);
                                            leadPINCODE = sharedPreferences.getString("PINCODE", null);
                                            leadPRODUCT = sharedPreferences.getString("PRODUCT", null);
                                            leadRATE = sharedPreferences.getString("RATE", null);

                                            leadREF_BY = sharedPreferences.getString("REF_BY", null);
                                            leadRESPONSE = sharedPreferences.getString("RESPONSE", null);
                                            leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", null);
                                            leadSCT = sharedPreferences.getString("SCT", null);
                                            leadSERUM = sharedPreferences.getString("SERUM", null);
                                            leadTESTS = sharedPreferences.getString("TESTS", null);
                                            leadTYPE = sharedPreferences.getString("TYPE", null);
                                            leadURINE = sharedPreferences.getString("URINE", null);
                                            leadWATER = sharedPreferences.getString("WATER", null);
                                            leadleadData = sharedPreferences.getString("leadData", null);


                                            Gson gson1 = new Gson();
                                            Leads.LeadData[] nameList = gson1.fromJson(leadleadData, Leads.LeadData[].class);
                                            List<Leads.LeadData> list = Arrays.asList(nameList);
                                            leadTESTS = "";
                                            for (int i = 0; i < list.size(); i++) {
                                                leadTESTS += list.get(i).getTest() + ",";
                                            }

                                            try {
                                                if (leadTESTS.endsWith(",")) {
                                                    leadTESTS = leadTESTS.substring(0, leadTESTS.length() - 1);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (sharedPreferences != null) {
                                                if (leadOrderIdMainModel.getLeads().length >= 2) {
                                                    leadlayout.setVisibility(View.GONE);
                                                } else {
                                                    leadlayout.setVisibility(View.VISIBLE);
                                                }

                                                next_btn.setVisibility(View.VISIBLE);
                                                leadname.setText("Name :" + leadNAME);
                                                leadidtest.setText("Test :" + leadTESTS);
                                                leadrefdr.setText("Ref Dr :" + leadREF_BY);


                                                for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
                                                    if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                                                        sizeflag = true;
                                                        break;
                                                    }
                                                }

                                                leadlayout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (sizeflag) {
                                                            Intent i = new Intent(activity, MultipleLeadActivity.class);
                                                            i.putExtra("MyClass", leadOrderIdMainModel);
                                                            i.putExtra("fromcome", "woepage");
                                                            i.putExtra("TESTS", leadTESTS);
                                                            i.putExtra("SCT", leadSCT);
                                                            i.putExtra("LeadID", leadLEAD_ID);
                                                            i.putExtra("brandtype", brand_spinner.getSelectedItem().toString());
                                                            i.putExtra("SR_NO", getVial_numbver);
                                                            SharedPreferences.Editor editor = activity.getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                                            editor.putString("typeName", leadTYPE);
                                                            editor.putString("SR_NO", getVial_numbver);
                                                            // To retrieve object in second Activity
                                                            startActivity(i);
                                                        } else {
                                                            Intent i = new Intent(activity, ScanBarcodeLeadId.class);
                                                            i.putExtra("MyClass", leadOrderIdMainModel);
                                                            i.putExtra("fromcome", "woepage");
                                                            i.putExtra("TESTS", leadTESTS);
                                                            i.putExtra("SCT", leadSCT);
                                                            i.putExtra("LeadID", leadLEAD_ID);
                                                            i.putExtra("brandtype", brand_spinner.getSelectedItem().toString());

                                                            SharedPreferences.Editor editor = activity.getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                                            editor.putString("typeName", leadTYPE);
                                                            editor.putString("SR_NO", getVial_numbver);
                                                            // To retrieve object in second Activity
                                                            startActivity(i);

                                                        }
                                                    }
                                                });
                                            }

                                        } else {
                                            GlobalClass.hideProgress(activity, pd_dialog);

                                            leadlayout.setVisibility(View.GONE);
                                            next_btn.setVisibility(View.GONE);
                                            Toast.makeText(activity, "No leads found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.networkResponse == null) {
                                        if (error.getClass().equals(TimeoutError.class)) {
                                            GlobalClass.hideProgress(activity, pd_dialog);
                                            TastyToast.makeText(activity, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                            // Show timeout error message
                                        }
                                    }
                                }
                            });
                            jsonObjectRequestProfile.setRetryPolicy(new DefaultRetryPolicy(
                                    150000,
                                    3,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            requestQueueNoticeBoard.add(jsonObjectRequestProfile);
                            Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestProfile);
                        } else {
                            vial_number.setError(ToastFile.vial_no);
                            Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                            id_for_woe.getText().clear();
                        }
                    }
//                                            getCityStateAPI(getId);
                }
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLead = id_for_woe.getText().toString();
                getVial_numbver = vial_number.getText().toString();
                if (getVial_numbver.equals("")) {
                    vial_number.setError(ToastFile.vial_no);
                    Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                } else if (getLead.equals("")) {
                    Toast.makeText(activity, "Please enter lead", Toast.LENGTH_SHORT).show();
                } else if (getLead.length() < 10) {
                    Toast.makeText(activity, "Please enter correct lead", Toast.LENGTH_SHORT).show();
                } else if (leadNAME.equals(null)) {
                    Toast.makeText(activity, "Please wait for some time", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
                        if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                            sizeflag = true;
                            break;
                        }
                    }
                    Intent i;
                    if (sizeflag) {
                        i = new Intent(activity, MultipleLeadActivity.class);
                        i.putExtra("MyClass", leadOrderIdMainModel);
                        i.putExtra("LeadID", leadLEAD_ID);
                        i.putExtra("SAMPLE_TYPE", leadSAMPLE_TYPE);
                        i.putExtra("fromcome", "woepage");
                        i.putExtra("TESTS", leadTESTS);
                        i.putExtra("SCT", leadSCT);
                        i.putExtra("SR_NO", getVial_numbver);
                        SharedPreferences.Editor editor = activity.getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                        editor.putString("typeName", leadTYPE);
                        editor.putString("SR_NO", getVial_numbver);
                        // To retrieve object in second Activity
                    } else {
                        i = new Intent(activity, ScanBarcodeLeadId.class);
                        i.putExtra("MyClass", leadOrderIdMainModel);
                        i.putExtra("LeadID", leadLEAD_ID);
                        i.putExtra("SAMPLE_TYPE", leadSAMPLE_TYPE);
                        i.putExtra("fromcome", "woepage");
                        i.putExtra("TESTS", leadTESTS);
                        i.putExtra("SCT", leadSCT);
                        SharedPreferences.Editor editor = activity.getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                        editor.putString("typeName", leadTYPE);
                        editor.putString("SR_NO", getVial_numbver);
                        // To retrieve object in second Activity
                    }
                    startActivity(i);

                }
            }
        });

    }

    private void checkKYCIsChecked() {
        cb_kyc_verification.setChecked(false);
        cb_kyc_verification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ll_miscallotp.setVisibility(View.VISIBLE);
                } else {
                    ll_miscallotp.setVisibility(View.GONE);
                }
            }
        });
    }

}