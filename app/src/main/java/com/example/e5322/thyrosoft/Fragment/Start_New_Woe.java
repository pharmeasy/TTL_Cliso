package com.example.e5322.thyrosoft.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Activity.MultipleLeadActivity;
import com.example.e5322.thyrosoft.Activity.frags.RootFragment;
import com.example.e5322.thyrosoft.Adapter.AdapterRe;
import com.example.e5322.thyrosoft.Adapter.CustomListAdapter;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.Controller.Barcodedetail_Controller;
import com.example.e5322.thyrosoft.Controller.CheckNumber_Controller;
import com.example.e5322.thyrosoft.Controller.ClientController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Getorderdetails_Controller;
import com.example.e5322.thyrosoft.Controller.Getwomaster_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.OTPtoken_controller;
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
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBarcodeDetailsResponseModel;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.R;
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
import com.google.gson.JsonSyntaxException;

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
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Start_New_Woe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Start_New_Woe extends RootFragment implements View.OnClickListener {
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1111;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // private static final String TAG = "LocationActivity";
    // TODO: Rename and change types of parameters
    public static RequestQueue PostQueOtp;
    public static ArrayList<BCT_LIST> getBtechList;
    public ArrayList<String> items = new ArrayList<>();
    public String[] putData;
    public static InputFilter EMOJI_FILTER = new InputFilter() {
        ConnectionDetector cd;

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };
    public static CAMP_LIST[] camp_lists;
    AlertDialog alertDialog;
    DatePickerDialog datePickerDialog;
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
    EditText name, age, id_for_woe, barcode_woe, pincode_edt;
    REF_DR[] ref_drs;
    Brand_type[] brandType;
    ImageView male, female, male_red, female_red;
    Button next_btn, btn_snd_otp, btn_verifyotp;
    ArrayList<String> getWindupCount;
    int agesinteger;
    ArrayList<LABS> filterPatientsArrayList, labDetailsArrayList;
    SourceILSMainModel sourceILSMainModel;
    LABS[] labs;
    ArrayList<String> getReferenceNmae;
    ArrayList<REF_DR> getReferenceName1;
    TextView samplecollectionponit;
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
    TextView woedatestage, patientnamestage, subsourcestage, address, sctdata, testsubsource, amtcollected, scp_default;
    EditText pincode, patientAddress, reenterkycinfo, kycinfo, vial_number, et_otp;
    Spinner btechname, deliveymode, camp_spinner_olc;
    ArrayList<BarcodelistModel> barcodelists;
    TextView radio, tv_timer;
    LinearLayout refby_linear, camp_layout_woe, btech_linear_layout, labname_linear, home_layout, mobile_number_kyc, pincode_linear_data, ll_mobileno_otp, lin_otp;
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
    TextView enetered, enter, tv_mob_note;
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
    private SharedPreferences prefs;
    private String user;
    private String passwrd;
    private String access;
    private String referenceBy;
    private String checkifChecked;
    private String api_key;
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

    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSetdata.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private String mm11;
    private String dd11;
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
    SharedPreferences covid_pref;
    boolean covidacc = false;

    public Start_New_Woe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Start_New_Woe.
     */


    // TODO: Rename and change types and number of parameters
    public static Start_New_Woe newInstance(String param1, String param2) {
        Start_New_Woe fragment = new Start_New_Woe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mContext = getContext();
        cd = new ConnectionDetector(getActivity());
        viewMain = (View) inflater.inflate(R.layout.fragment_start__new__woe, container, false);
        covid_pref = getActivity().getSharedPreferences("COVIDETAIL", MODE_PRIVATE);
        covidacc = covid_pref.getBoolean("covidacc", false);

        initViews();


        GlobalClass.setflagToRefreshData = false;

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);

        GlobalClass.SetText(dateShow, showDate);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);
        minDate = myCalendar.getTime().getTime();
        myDb = new DatabaseHelper(mContext);


        if (GlobalClass.flagToSendfromnavigation) {
            GlobalClass.flagToSendfromnavigation = false;
            enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
            enter_arrow_enter.setVisibility(View.GONE);
            enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
            enter_arrow_entered.setVisibility(View.VISIBLE);
            scrollView2.setVisibility(View.GONE);
        } else {
            scrollView2.setVisibility(View.VISIBLE);
        }

        GlobalClass.isAutoTimeSelected(getActivity());

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Log.e("SDK_INT", "" + Build.VERSION.SDK_INT);
            samplecollectionponit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow_woe, 0);
            samplecollectionponit.setCompoundDrawablePadding(5);
        } else {
            samplecollectionponit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
        }

        iniListner();

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
        saveDetails.commit();

        getCurrentDateandTime = new Date();


        ref_check.setVisibility(View.GONE);
        uncheck_ref.setVisibility(View.VISIBLE);
        referenceBy = null;
        checkifChecked = null;
        refby_linear.setVisibility(View.VISIBLE);

        ref_check_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flagtoAdjustClisk == 0) {
                    flagtoAdjustClisk = 1;
                    referenceBy = "SELF";
                    GlobalClass.SetAutocomplete(referedbyText, "");
                    refby_linear.setVisibility(View.GONE);
                    ref_check.setVisibility(View.VISIBLE);
                    uncheck_ref.setVisibility(View.GONE);
                } else if (flagtoAdjustClisk == 1) {
                    flagtoAdjustClisk = 0;
                    referenceBy = null;
                    GlobalClass.SetAutocomplete(referedbyText, "");
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

        if (!GlobalClass.isNull(GlobalClass.setScp_Constant)) {
            GlobalClass.SetText(samplecollectionponit, GlobalClass.setScp_Constant);
        } else {
            GlobalClass.SetText(samplecollectionponit, "");
        }

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
                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);


                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(pincode_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(pincode_edt, "");
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
                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(home_kyc_format, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(home_kyc_format, "");
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

                    GlobalClass.showTastyToast(getActivity(), MessageConstants.ENTER_CORR_REFBY, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetAutocomplete(referedbyText, enteredString.substring(1));
                    } else {
                        GlobalClass.SetAutocomplete(referedbyText, "");
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
                if (!GlobalClass.isNull(setText)) {
                    ref_check_linear.setVisibility(View.GONE);
                }
            }

        });

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        myPojo = new MyPojo();
        String json = appSharedPrefs.getString("saveAlldata", "");
        myPojo = gson.fromJson(json, MyPojo.class);

        try {
            if (myPojo != null) {
                getBrandName = new ArrayList<>();
                spinnerBrandName = new ArrayList<String>();
                getDatafetch = new ArrayList();
                getSubSource = new ArrayList();

                try {
                    if (!GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST())) {
                        for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                            getDatafetch.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                            spinnerBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                            camp_lists = myPojo.getMASTERS().getCAMP_LIST();

                            if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                                String TspNumber = myPojo.getMASTERS().getTSP_MASTER().getNumber();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("TspNumber", 0).edit();
                                editor.putString("TSPMobileNumber", TspNumber);
                                editor.commit();
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (myPojo != null) {
                    if (myPojo.getMASTERS() != null && !GlobalClass.checkArray(myPojo.getMASTERS().getSUB_SOURCECODE())) {
                        for (int i = 0; i < myPojo.getMASTERS().getSUB_SOURCECODE().length; i++) {
                            getSubSource.add(myPojo.getMASTERS().getSUB_SOURCECODE()[i].getSub_source_code_pass());
                        }
                    }

                }

                spinnerTypeName = new ArrayList<>();
                getTypeListfirst = new ArrayList<>();
                getTypeListSMT = new ArrayList<>();

                try {
                    if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST())) {
                        if (GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type())) {
                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                            }
                        }
                    }


                    if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST())) {
                        if (myPojo.getMASTERS().getBRAND_LIST().length > 1) {

                            if (!GlobalClass.isNull(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_name()) && myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_name().contains("SMT")) {

                                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST())) {
                                    if (GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type())) {
                                        for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type().length; i++) {
                                            getTypeListSMT.add(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type()[i].getType());
                                        }
                                    }
                                }

                                try {
                                    if (myPojo.getMASTERS().getBRAND_LIST()[2] != null) {
                                        Brand_type[] c = myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type();
                                        Brand_type c1 = new Brand_type();
                                        getTypeListsecond = new ArrayList<>();
                                        for (int j = 0; j < c.length; j++) {
                                            Log.v("TAG", c[j].getType());
                                            String type12 = "";
                                            type12 = c[j].getType();
                                            getTypeListsecond.add(c[j].getType());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (myPojo.getMASTERS().getBRAND_LIST()[3] != null) {
                                        Brand_type[] data = myPojo.getMASTERS().getBRAND_LIST()[3].getBrand_type();
                                        Brand_type d1 = new Brand_type();
                                        getTypeListthird = new ArrayList<>();
                                        for (int k = 0; k < data.length; k++) {
                                            Log.v("TAG", data[k].getType());
                                            String type12 = "";
                                            type12 = data[k].getType();
                                            getTypeListthird.add(data[k].getType());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    if (myPojo.getMASTERS().getBRAND_LIST()[1] != null) {
                                        Brand_type[] c = myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_type();
                                        Brand_type c1 = new Brand_type();
                                        getTypeListsecond = new ArrayList<>();
                                        for (int j = 0; j < c.length; j++) {
                                            Log.v("TAG", c[j].getType());
                                            String type12 = "";
                                            type12 = c[j].getType();
                                            getTypeListsecond.add(c[j].getType());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (myPojo.getMASTERS().getBRAND_LIST()[2] != null) {
                                        Brand_type[] data = myPojo.getMASTERS().getBRAND_LIST()[2].getBrand_type();
                                        Brand_type d1 = new Brand_type();
                                        getTypeListthird = new ArrayList<>();
                                        for (int k = 0; k < data.length; k++) {
                                            Log.v("TAG", data[k].getType());
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

                        SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        Gson gsondata = new Gson();
                        String jsondata = appSharedPrefsdata.getString("savelabnames", "");
                        obj = new SourceILSMainModel();
                        obj = gsondata.fromJson(jsondata, SourceILSMainModel.class);

                        if (obj != null) {
                            if (obj.getMASTERS() != null && !GlobalClass.isNull(obj.getUSER_TYPE())) {
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
                    SharedPreferences.Editor ScpAddress = getActivity().getSharedPreferences("ScpAddress", 0).edit();
                    ScpAddress.putString("scp_addrr", getAddress);
                    ScpAddress.commit();
                }

                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getLAB_ALERTS())) {
                    putData = myPojo.getMASTERS().getLAB_ALERTS();
                    if (GlobalClass.checkArray(putData)) {
                        for (int i = 0; i < putData.length; i++) {
                            items.add(putData[i]);
                        }
                    }
                }


                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, spinnerBrandName);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brand_spinner.setAdapter(adapter2);
                brand_spinner.setSelection(0);
                startDataSetting();

            } else {
                if (!GlobalClass.isNetworkAvailable(getActivity())) {
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
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);
        getBtechList = new ArrayList<>();
        getCampNames = new ArrayList<>();
        btechSpinner = new ArrayList<>();

        if (myPojo != null) {
            if (myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                    getBtechList.add(myPojo.getMASTERS().getBCT_LIST()[j]);
                }
            } else {
                GlobalClass.showTastyToast(getActivity(), "Please register NED", 2);
            }

            try {
                if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                    getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getCampNames.add("Select Camp");
            if (myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getCAMP_LIST())) {

                for (int k = 0; k < myPojo.getMASTERS().getCAMP_LIST().length; k++) {
                    getCampNames.add(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE());
                }

                if (GlobalClass.CheckArrayList(getCampNames)) {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                            mContext, R.layout.spinnerproperty, getCampNames);
                    camp_spinner_olc.setAdapter(adapter2);
                    camp_spinner_olc.setSelection(0);
                }
            }

            btechSpinner.add("SELECT BTECH NAME");
            if (GlobalClass.CheckArrayList(getBtechList)) {
                for (int i = 0; i < getBtechList.size(); i++) {
                    btechSpinner.add(getBtechList.get(i).getNAME());
                    if (btechSpinner.size() != 0) {
                        btechname.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinnerproperty, btechSpinner));
                    }
                }
            }


        } else {
            getTspNumber();
            SharedPreferences getshared = getActivity().getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);
            prof = getshared.getString("prof", null);
            if (prof != null) {
                mobile = getshared.getString("mobile", "");
                nameofProfile = getshared.getString("name", "");
            }
        }

        if (GlobalClass.CheckArrayList(getLabNmae) && GlobalClass.CheckArrayList(getReferenceNmae)) {
            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// With

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    GlobalClass.SetAutocomplete(referedbyText, s);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    GlobalClass.SetAutocomplete(referedbyText, item);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });
        }

        List<String> hourSin = Arrays.asList("HR", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        List<String> minuteSpin = Arrays.asList("MIN", "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");

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

        if (GlobalClass.CheckArrayList(patientsagespinner)) {
            ArrayAdapter<String> adap = new ArrayAdapter<String>(
                    mContext, R.layout.name_age_spinner, patientsagespinner);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinyr.setAdapter(adap);
            spinyr.setSelection(0);
        }


        name.setFilters(new InputFilter[]{EMOJI_FILTER});
        patientAddress.setFilters(new InputFilter[]{EMOJI_FILTER});

        int maxLength = 40;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        name.setFilters(FilterArray);

        int maxLength1 = 180;
        InputFilter[] FilterArray1 = new InputFilter[1];
        FilterArray1[0] = new InputFilter.LengthFilter(maxLength1);
        patientAddress.setFilters(FilterArray1);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {

                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(name, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(name, "");
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
                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(patientAddress, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(patientAddress, "");
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
                if (!genderId) {
                    genderId = true;
                    saveGenderId = "M";
                    male_red.setVisibility(View.VISIBLE);
                    female.setVisibility(View.VISIBLE);
                    female_red.setVisibility(View.GONE);
                    male.setVisibility(View.GONE);
                } else if (genderId) {
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

                if (!genderId) {
                    genderId = true;
                    saveGenderId = "F";
                    female_red.setVisibility(View.VISIBLE);
                    male.setVisibility(View.VISIBLE);
                    male_red.setVisibility(View.GONE);
                    female.setVisibility(View.GONE);

                } else if (genderId) {
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
                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_age, 2);
                    if (enteredString.length() > 0) {

                        GlobalClass.SetEditText(age, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(age, "");
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

        return viewMain;
    }

    private void iniListner() {
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
        name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        patientAddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btn_snd_otp.setOnClickListener(this);
        btn_verifyotp.setOnClickListener(this);
        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.SetText(samplecollectionponit, MessageConstants.SEARCH_SMP_TYPE);
                Start_New_Woe fragment = new Start_New_Woe();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
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
                    GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetText(vial_number, enteredString.substring(1));
                    } else {
                        GlobalClass.SetText(vial_number, "");
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date, myCalendar
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
                    btn_snd_otp.setVisibility(View.VISIBLE);
                    GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                    Disablefields();
                } else {
                    btn_snd_otp.setVisibility(View.GONE);
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
                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_mob_num, 2);


                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(et_mobno, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(et_mobno, "");
                    }
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String checkNumber = s.toString();
                if (checkNumber.length() < 10) {
                    flag = true;
                }

                if (flag) {
                    if (s.length() == 10) {

                        if (!GlobalClass.isNetworkAvailable(getActivity())) {
                            flag = false;
                            GlobalClass.SetEditText(et_mobno, s.toString());
                        } else {
                            flag = false;

                            RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(getActivity());
                            try {
                                if (ControllersGlobalInitialiser.checkNumber_controller != null) {
                                    ControllersGlobalInitialiser.checkNumber_controller = null;
                                }
                                ControllersGlobalInitialiser.checkNumber_controller = new CheckNumber_Controller(getActivity(), Start_New_Woe.this, "1");
                                ControllersGlobalInitialiser.checkNumber_controller.getchecknumbercontroll(s.toString(), reques5tQueueCheckNumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }
                }
            }
        });
    }

    private void initViews() {
        timehr = (Spinner) viewMain.findViewById(R.id.timehr);
        timesecond = (Spinner) viewMain.findViewById(R.id.timesecond);
        selectTypeSpinner = (Spinner) viewMain.findViewById(R.id.selectTypeSpinner);
        brand_spinner = (Spinner) viewMain.findViewById(R.id.brand_spinner);
        camp_spinner_olc = (Spinner) viewMain.findViewById(R.id.camp_spinner_olc);
        btechname = (Spinner) viewMain.findViewById(R.id.btech_spinner);
        timeampm = (Spinner) viewMain.findViewById(R.id.timeampm);
        samplecollectionponit = (TextView) viewMain.findViewById(R.id.samplecollectionponit);
        referedbyText = (AutoCompleteTextView) viewMain.findViewById(R.id.referedby);//
        radio = (TextView) viewMain.findViewById(R.id.radio);
        tv_timer = viewMain.findViewById(R.id.tv_timer);
        refby_linear = (LinearLayout) viewMain.findViewById(R.id.refby_linear);
        camp_layout_woe = (LinearLayout) viewMain.findViewById(R.id.camp_layout_woe);
        btech_linear_layout = (LinearLayout) viewMain.findViewById(R.id.btech_linear_layout);
        labname_linear = (LinearLayout) viewMain.findViewById(R.id.labname_linear);
        home_layout = (LinearLayout) viewMain.findViewById(R.id.home_linear_data);
        pincode_linear_data = (LinearLayout) viewMain.findViewById(R.id.pincode_linear_data);

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
        btn_verifyotp = viewMain.findViewById(R.id.btn_verifyotp);
        btn_snd_otp = viewMain.findViewById(R.id.btn_sendotp);
        name = (EditText) viewMain.findViewById(R.id.name);
        age = (EditText) viewMain.findViewById(R.id.age);
        id_for_woe = (EditText) viewMain.findViewById(R.id.id_for_woe);
        barcode_woe = (EditText) viewMain.findViewById(R.id.barcode_woe);
        pincode_edt = (EditText) viewMain.findViewById(R.id.pincode_edt);
        kyc_format = (EditText) viewMain.findViewById(R.id.kyc_format);
        home_kyc_format = (EditText) viewMain.findViewById(R.id.home_kyc_format);
        patientAddress = (EditText) viewMain.findViewById(R.id.patientAddress);
        vial_number = (EditText) viewMain.findViewById(R.id.vial_number);

        et_mobno = viewMain.findViewById(R.id.et_mobno);
        et_otp = viewMain.findViewById(R.id.et_otp);

        chk_otp = viewMain.findViewById(R.id.chk_otp);
        lin_ckotp = viewMain.findViewById(R.id.lin_ckotp);

        male = (ImageView) viewMain.findViewById(R.id.male);
        male_red = (ImageView) viewMain.findViewById(R.id.male_red);
        female = (ImageView) viewMain.findViewById(R.id.female);
        female_red = (ImageView) viewMain.findViewById(R.id.female_red);
        next_btn = (Button) viewMain.findViewById(R.id.next_btn_patient);

    }

    private void enterNextFragment() {
        Woe_fragment a2Fragment = new Woe_fragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commitAllowingStateLoss();
    }

    private void requestJsonObject() {
        try {
            RequestQueue requestQueue = GlobalClass.setVolleyReq(mContext);
            try {
                if (ControllersGlobalInitialiser.getwomaster_controller != null) {
                    ControllersGlobalInitialiser.getwomaster_controller = null;
                }
                ControllersGlobalInitialiser.getwomaster_controller = new Getwomaster_Controller(getActivity(), Start_New_Woe.this);
                ControllersGlobalInitialiser.getwomaster_controller.getwoeMaster_Controller(api_key, user, requestQueue);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startDataSetting() {

        brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListfirst);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectTypeSpinner.setAdapter(adapter2);
                    selectTypeSpinner.setSelection(0);

                    selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            vial_number.getText().clear();
                            id_for_woe.getText().clear();

                            if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()) && selectTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("ILS")) {
                                Enablefields();

                                if (yourCountDownTimer != null) {
                                    yourCountDownTimer.cancel();
                                    yourCountDownTimer = null;
                                    btn_snd_otp.setEnabled(true);
                                    btn_snd_otp.setClickable(true);
                                }


                                GlobalClass.SetText(samplecollectionponit, MessageConstants.SEARCH_SMP_TYPE);
                                et_mobno.getText().clear();
                                ll_mobileno_otp.setVisibility(View.GONE);
                                tv_mob_note.setVisibility(View.GONE);
                                et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                lin_otp.setVisibility(View.GONE);
                                tv_timer.setVisibility(View.GONE);


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

                                labname_linear.setVisibility(View.VISIBLE);
                                Home_mobile_number_kyc.setVisibility(View.GONE);
                                namePatients.setVisibility(View.VISIBLE);
                                AGE_layout.setVisibility(View.VISIBLE);
                                time_layout.setVisibility(View.VISIBLE);
                                refby_linear.setVisibility(View.VISIBLE);
                                ref_check_linear.setVisibility(View.VISIBLE);
                                uncheck_ref.setVisibility(View.VISIBLE);
                                ref_check.setVisibility(View.GONE);


                                GlobalClass.SetAutocomplete(referedbyText, "");

                                brand_string = brand_spinner.getSelectedItem().toString();
                                type_string = selectTypeSpinner.getSelectedItem().toString();
                                id_woe = id_for_woe.getText().toString();
                                barcode_woe_str = barcode_woe.getText().toString();
                                referenceBy = "";

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
                                                Log.v("TAG", output);
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
                                        kycdata = kyc_format.getText().toString();
                                        leadlayout.setVisibility(View.GONE);
                                        kycdata = "";
                                        btechIDToPass = "";
                                        btechnameTopass = "";
                                        getcampIDtoPass = "";

                                        if (GlobalClass.isNull(woereferedby)) {
                                            if (referenceBy == null) {
                                                GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
                                            } else {
                                                if (!GlobalClass.isNull(referenceBy) && referenceBy.equalsIgnoreCase("SELF")) {
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


                                        if (!GlobalClass.isNull(woereferedby)) {
                                            if (obj != null) {
                                                if (GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }

                                            }
                                        } else {
                                            referenceBy = woereferedby;
                                            referredID = "";
                                        }


                                        if (!GlobalClass.isNull(ageString)) {
                                            conertage = Integer.parseInt(ageString);
                                        }

                                        if (GlobalClass.isNull(getVial_numbver)) {
                                            vial_number.setError(ToastFile.vial_no);
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                        } else if (GlobalClass.isNull(nameString)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                        } else if (nameString.length() < 2) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                        } else if (GlobalClass.isNull(ageString)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                        } else if (saveGenderId == null || saveGenderId == "") {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                        } else if (conertage > 120) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                        } else if (sctHr.equals("HR")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                        } else if (sctMin.equals("MIN")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                        } else if (sctSEc.equals("AM/PM")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                        } else if (scpoint.equalsIgnoreCase(MessageConstants.SEARCH_SMP_TYPE) || scpoint.equals("") || scpoint.equals(null)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_scp, 2);
                                        } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                        } else if (dCompare.after(getCurrentDateandTime)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                        } else if (getLabName.equalsIgnoreCase(MessageConstants.SEARCH_SMP_TYPE)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_sample_cll_point, 2);
                                        } else {
                                            if (!GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && !GlobalClass.isNull(woereferedby) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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
                                                if (GlobalClass.checkArray(labs)) {
                                                    for (int i = 0; i < labs.length; i++) {
                                                        if (getLabCode.contains("-")) {
                                                            String s2 = getLabCode.substring(getLabCode.indexOf("-") + 2);
                                                            s2.trim();
                                                            getLabCode = s2;
                                                        }
                                                        if (!GlobalClass.isNull(labs[i].getClientid()) && getLabCode.equalsIgnoreCase(labs[i].getClientid())) {
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

                                            sctHr = timehr.getSelectedItem().toString();
                                            sctMin = timesecond.getSelectedItem().toString();
                                            sctSEc = timeampm.getSelectedItem().toString();
                                            final String getFinalAge = age.getText().toString();
                                            final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                            final String getFinalDate = dateShow.getText().toString();

                                            if (!GlobalClass.isNull(referenceBy) && referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
                                                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                                        .setContentText(MessageConstants.PGC10_DEVBIT)
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

                                                                Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                                                                SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                saveDetails.putString("name", nameString);
                                                                saveDetails.putString("age", getFinalAge);
                                                                saveDetails.putString("gender", saveGenderId);
                                                                saveDetails.putString("sct", getFinalTime);
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
                                                                saveDetails.commit();
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        }).show();

                                            } else {
                                                Intent i = new Intent(mContext, ProductLisitngActivityNew.class);
                                                i.putExtra("name", nameString);
                                                i.putExtra("age", getFinalAge);
                                                i.putExtra("gender", saveGenderId);
                                                i.putExtra("sct", getFinalTime);
                                                i.putExtra("date", getFinalDate);
                                                GlobalClass.setReferenceBy_Name = referenceBy;
                                                startActivity(i);

                                                GlobalClass.Req_Date_Req(getFinalTime, "hh:mm a", "HH:mm:ss");
                                                Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                                                SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                saveDetails.putString("name", nameString);
                                                saveDetails.putString("age", getFinalAge);
                                                saveDetails.putString("gender", saveGenderId);
                                                saveDetails.putString("sct", getFinalTime);
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
                                                saveDetails.commit();
                                            }
                                        }


                                    }
                                });

                            } else if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()) && selectTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("DPS")) {

                                try {
                                    if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase(MessageConstants.YES)) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        chk_otp.setChecked(true);
                                        lin_ckotp.setVisibility(View.VISIBLE);

                                        if (chk_otp.isChecked()) {
                                            btn_snd_otp.setVisibility(View.VISIBLE);
                                            GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                        } else {
                                            btn_snd_otp.setVisibility(View.GONE);
                                        }

                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                        tv_mob_note.setVisibility(View.VISIBLE);

                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
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
                                btech_linear_layout.setVisibility(View.VISIBLE);
                                pincode_linear_data.setVisibility(View.VISIBLE);
                                home_layout.setVisibility(View.VISIBLE);
                                vial_number.setVisibility(View.VISIBLE);


                                labname_linear.setVisibility(View.GONE);
                                ref_check.setVisibility(View.GONE);
                                Home_mobile_number_kyc.setVisibility(View.GONE);
                                ref_check_linear.setVisibility(View.VISIBLE);
                                uncheck_ref.setVisibility(View.VISIBLE);


                                GlobalClass.SetAutocomplete(referedbyText, "");
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
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_mob_num, 2);

                                            if (enteredString.length() > 0) {
                                                GlobalClass.SetEditText(kyc_format, enteredString.substring(1));
                                            } else {
                                                GlobalClass.SetEditText(kyc_format, "");
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

                                                if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                    flag = false;
                                                    GlobalClass.SetEditText(kyc_format, checkNumber);
                                                } else {
                                                    flag = false;
                                                    RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(getActivity());

                                                    try {
                                                        if (ControllersGlobalInitialiser.checkNumber_controller != null) {
                                                            ControllersGlobalInitialiser.checkNumber_controller = null;
                                                        }
                                                        ControllersGlobalInitialiser.checkNumber_controller = new CheckNumber_Controller(getActivity(), Start_New_Woe.this, "2");
                                                        ControllersGlobalInitialiser.checkNumber_controller.getchecknumbercontroll(checkNumber, reques5tQueueCheckNumber);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

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
                                                Log.v("TAG", output);
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


                                        if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                            kycdata = et_mobno.getText().toString();
                                        } else {
                                            kycdata = kyc_format.getText().toString();
                                        }

                                        labAddressTopass = "";
                                        labIDTopass = "";
                                        getcampIDtoPass = "";

                                        if (!GlobalClass.isNull(btechname.getSelectedItem().toString()))
                                            btechnameTopass = btechname.getSelectedItem().toString();

                                        try {
                                            if (!GlobalClass.isNull(btechnameTopass)) {
                                                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (!GlobalClass.isNull(btechnameTopass) && !GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        if (GlobalClass.isNull(woereferedby)) {
                                            if (referenceBy == null) {
                                                GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
                                            } else {
                                                if (!GlobalClass.isNull(referenceBy) && referenceBy.equalsIgnoreCase("SELF")) {
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
                                            if (!GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName())
                                                                && !GlobalClass.isNull(woereferedby) &&
                                                                woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {

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


                                        if (!GlobalClass.isNull(ageString)) {
                                            conertage = Integer.parseInt(ageString);
                                        }

                                        if (getVial_numbver.equalsIgnoreCase("")) {
                                            vial_number.setError(ToastFile.vial_no);
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                        } else if (nameString.equalsIgnoreCase("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                        } else if (nameString.length() < 2) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                        } else if (ageString.equalsIgnoreCase("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                        } else if (conertage > 120) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                        } else if (saveGenderId == null || saveGenderId == "") {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                        } else if (sctHr.equalsIgnoreCase("HR")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                        } else if (sctMin.equalsIgnoreCase("MIN")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                        } else if (sctSEc.equalsIgnoreCase("AM/PM")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                        } else if (dCompare.after(getCurrentDateandTime)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                        } else if (patientAddressdataToPass.equals("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                        } else if (patientAddressdataToPass.length() < 25) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                        } else if (pincode_pass.equalsIgnoreCase("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                        } else if (pincode_pass.length() < 6) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                        } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                        } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                        } else {
                                            try {
                                                if (!GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && !GlobalClass.isNull(woereferedby) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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

                                            if (kycdata.length() == 0) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                            } else if (kycdata.length() < 10) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                            } else {

                                                try {
                                                    if (GlobalClass.isNull(woereferedby)) {
                                                        if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && !GlobalClass.isNull(woereferedby) &&
                                                                        woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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


                                                if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                    saveDetails.commit();
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();

                                                } else {

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
                                                    saveDetails.commit();
                                                }


                                            }
                                        }
                                    }
                                });
                            } else if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString())
                                    && selectTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("HOME")) {

                                try {
                                    if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        Home_mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        chk_otp.setChecked(true);
                                        lin_ckotp.setVisibility(View.VISIBLE);
                                        if (chk_otp.isChecked()) {
                                            btn_snd_otp.setVisibility(View.VISIBLE);
                                            GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                        } else {
                                            btn_snd_otp.setVisibility(View.GONE);
                                        }
                                        Home_mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                        tv_mob_note.setVisibility(View.VISIBLE);
                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
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
                                camp_layout_woe.setVisibility(View.GONE);
                                btech_linear_layout.setVisibility(View.VISIBLE);
                                pincode_linear_data.setVisibility(View.VISIBLE);
                                home_layout.setVisibility(View.VISIBLE);


                                vial_number.setVisibility(View.VISIBLE);
                                mobile_number_kyc.setVisibility(View.GONE);
                                labname_linear.setVisibility(View.GONE);
                                GlobalClass.SetEditText(patientAddress, "");

                                ref_check.setVisibility(View.GONE);
                                ref_check_linear.setVisibility(View.VISIBLE);
                                uncheck_ref.setVisibility(View.VISIBLE);
                                refby_linear.setVisibility(View.VISIBLE);
                                GlobalClass.SetAutocomplete(referedbyText, "");

                                referenceBy = "";


                                namePatients.setVisibility(View.VISIBLE);
                                AGE_layout.setVisibility(View.VISIBLE);
                                time_layout.setVisibility(View.VISIBLE);
//
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
                                            String output = null;
                                            try {
                                                dCompare = df.parse(input);
                                                output = outputformat.format(dCompare);
                                                Log.v("TAG", output);
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


                                        if (!GlobalClass.isNull(btechname.getSelectedItem().toString()))
                                            btechnameTopass = btechname.getSelectedItem().toString();


                                        if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                            kycdata = et_mobno.getText().toString();
                                        } else {
                                            kycdata = home_kyc_format.getText().toString();
                                        }


                                        labIDTopass = "";
                                        labAddressTopass = "";
                                        getcampIDtoPass = "";

                                        if (!GlobalClass.isNull(btechnameTopass)) {
                                            if (GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                    if (!GlobalClass.isNull(btechnameTopass) && !GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                        btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                    }
                                                }
                                            }

                                        }

                                        if (GlobalClass.isNull(woereferedby)) {
                                            if (referenceBy == null || referenceBy.length() <= 1) {
                                                GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
                                            } else {
                                                if (!GlobalClass.isNull(referenceBy) && referenceBy.equalsIgnoreCase("SELF")) {
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


                                        if (!GlobalClass.isNull(woereferedby)) {
                                            if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                    if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                        referenceBy = woereferedby;
                                                        referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                    }
                                                }
                                            }
                                        } else {
                                            referenceBy = woereferedby;
                                            referredID = "";
                                        }


                                        if (!GlobalClass.isNull(ageString)) {
                                            conertage = Integer.parseInt(ageString);
                                        }

                                        if (GlobalClass.isNull(getVial_numbver)) {
                                            vial_number.setError(ToastFile.vial_no);
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                        } else if (GlobalClass.isNull(nameString)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                        } else if (nameString.length() < 2) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);

                                        } else if (GlobalClass.isNull(ageString)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                        } else if (conertage > 120) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                        } else if (GlobalClass.isNull(saveGenderId)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                        } else if (sctHr.equalsIgnoreCase("HR")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                        } else if (sctMin.equalsIgnoreCase("MIN")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                        } else if (sctSEc.equalsIgnoreCase("AM/PM")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                        } else if (dCompare.after(getCurrentDateandTime)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                        } else if (patientAddressdataToPass.equalsIgnoreCase("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                        } else if (patientAddressdataToPass.length() < 25) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                        } else if (pincode_pass.equalsIgnoreCase("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                        } else if (pincode_pass.length() < 6) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                        } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                        } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                        } else {
                                            if (kycdata.length() == 0) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                            } else if (kycdata.length() < 10) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                            } else {

                                                if (!GlobalClass.isNull(woereferedby)) {

                                                    if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && !GlobalClass.isNull(woereferedby) &&
                                                                    woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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
                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                final String getFinalAge = age.getText().toString();
                                                final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                final String getFinalDate = dateShow.getText().toString();

                                                if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                    saveDetails.commit();
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();

                                                } else {
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
                                                    saveDetails.commit();
                                                }


                                            }

                                        }

                                    }
                                });

                            } else if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()) && selectTypeSpinner.getSelectedItem().toString().equals("CAMP")) {
                                Enablefields();
                                leadlayout.setVisibility(View.GONE);
                                id_layout.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.GONE);
                                pincode_linear_data.setVisibility(View.GONE);
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
                                GlobalClass.SetAutocomplete(referedbyText, "");
                                ref_check_linear.setVisibility(View.VISIBLE);
                                uncheck_ref.setVisibility(View.VISIBLE);
                                refby_linear.setVisibility(View.VISIBLE);
                                namePatients.setVisibility(View.VISIBLE);
                                AGE_layout.setVisibility(View.VISIBLE);
                                time_layout.setVisibility(View.VISIBLE);

                                GlobalClass.SetEditText(patientAddress, "");
                                referenceBy = "";

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
                                                Log.v("TAG", output);
                                            } catch (ParseException pe) {
                                                pe.printStackTrace();
                                            }
                                            e.printStackTrace();
                                        }

                                        typename = selectTypeSpinner.getSelectedItem().toString();
                                        brandNames = brand_spinner.getSelectedItem().toString();
                                        getVial_numbver = vial_number.getText().toString();
                                        if (!GlobalClass.isNull(typename) && typename.equals("CAMP")) {

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            if (!GlobalClass.isNull(camp_spinner_olc.getSelectedItem().toString())) {
                                                scpoint = camp_spinner_olc.getSelectedItem().toString();
                                            }
                                            kycdata = kyc_format.getText().toString();

                                            kycdata = "";
                                            labIDTopass = "";
                                            btechIDToPass = "";
                                            btechnameTopass = "";
                                            labAddressTopass = "";

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (GlobalClass.isNull(referenceBy)) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
                                                } else {
                                                    if (!GlobalClass.isNull(referenceBy) && referenceBy.equalsIgnoreCase("SELF")) {
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && !GlobalClass.isNull(woereferedby) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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
                                                if (!GlobalClass.isNull(scpoint)) {
                                                    if (GlobalClass.checkArray(myPojo.getMASTERS().getCAMP_LIST())) {
                                                        for (int k = 0; k < myPojo.getMASTERS().getCAMP_LIST().length; k++) {
                                                            if (!GlobalClass.isNull(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE()) && scpoint.equals(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE())) {
                                                                getcampIDtoPass = myPojo.getMASTERS().getCAMP_LIST()[k].getCAMP_ID();
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (!GlobalClass.isNull(ageString)) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (GlobalClass.isNull(ageString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                            } else if (conertage > 120) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else if (scpoint.equals("Select Camp")) {
                                                GlobalClass.showTastyToast(getActivity(), MessageConstants.SL_CAMPNAME, 2);
                                            } else if (dCompare.after(getCurrentDateandTime) && getCurrentDateandTime != null) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else {

                                                if (GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null && obj.getMASTERS() != null && obj.getMASTERS().getREF_DR() != null &&
                                                            obj.getMASTERS().getREF_DR().length != 0) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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


                                                if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {

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
                                                                    saveDetails.commit();
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();

                                                } else {
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
                                                    saveDetails.commit();
                                                }


                                            }
                                        }

                                    }
                                });

                            } else if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()) && selectTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("ORDERS")) {
                                LeadIdwoe();
                            } else if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()) && selectTypeSpinner.getSelectedItem().equals("ADD")) {

                                GlobalClass.SetEditText(barcode_woe, "");
                                leadbarcodelayout.setVisibility(View.GONE);
                                ll_mobileno_otp.setVisibility(View.GONE);
                                tv_mob_note.setVisibility(View.GONE);
                                id_layout.setVisibility(View.GONE);
                                pincode_linear_data.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.VISIBLE);
                                vial_number.setVisibility(View.GONE);
                                camp_layout_woe.setVisibility(View.GONE);
                                btech_linear_layout.setVisibility(View.GONE);
                                home_layout.setVisibility(View.GONE);
                                labname_linear.setVisibility(View.GONE);
                                GlobalClass.SetEditText(patientAddress, "");

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
                                            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                GlobalClass.showAlertDialog(getActivity());
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

                                        if (GlobalClass.isNull(type_string)) {
                                            GlobalClass.showTastyToast(getActivity(), MessageConstants.SL_TYPNAME, 2);
                                        } else if (barcode_woe.getText().toString().equals("")) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.entr_brcd, 2);
                                        } else {
                                            GlobalClass.branditem = brand_spinner.getSelectedItem().toString();
                                            GlobalClass.typeItem = selectTypeSpinner.getSelectedItem().toString();
                                            GlobalClass.id_value = id_for_woe.getText().toString();

                                            SharedPreferences.Editor savePrefe = getActivity().getSharedPreferences("AddTestType", 0).edit();
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

                                            Intent a = new Intent(getActivity(), AddWOETestsForSerum.class);
                                            startActivity(a);

                                        }
                                    }
                                });
                            } else if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()) &&
                                    selectTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("RECHECK")) {
                                GlobalClass.SetEditText(barcode_woe, "");
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
                                labname_linear.setVisibility(View.GONE);
                                GlobalClass.SetEditText(patientAddress, "");
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
                                            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                GlobalClass.showAlertDialog(getActivity());
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

                                        if (!GlobalClass.isNull(brand_string) && brand_string.equals("Select brand name")) {
                                            GlobalClass.showTastyToast(getActivity(), MessageConstants.SL_BRANDSTYPE, 2);
                                        } else if (GlobalClass.isNull(type_string)) {
                                            GlobalClass.showTastyToast(getActivity(), MessageConstants.SL_TYPNAME, 2);
                                        } else if (GlobalClass.isNull(barcode_woe.getText().toString())) {
                                            GlobalClass.showTastyToast(getActivity(), ToastFile.entr_brcd, 2);
                                        } else {
                                            SharedPreferences.Editor savePrefe = getActivity().getSharedPreferences("RecheckTestType", 0).edit();
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
                                            Intent a = new Intent(getActivity(), RecheckAllTest.class);
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


                if (position > 0) {
                    vial_number.getText().clear();
                    id_for_woe.getText().clear();
                    if (!GlobalClass.isNull(brand_spinner.getSelectedItem().toString()) &&
                            brand_spinner.getSelectedItem().toString().equalsIgnoreCase("SMT")) {
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListSMT);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);

                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (selectTypeSpinner.getSelectedItem().equals("DPS")) {

                                    try {
                                        if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                            Enablefields();
                                            mobile_number_kyc.setVisibility(View.VISIBLE);
                                            ll_mobileno_otp.setVisibility(View.GONE);
                                            tv_mob_note.setVisibility(View.GONE);
                                        } else if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("YES")) {
                                            Disablefields();
                                            et_mobno.setFocusable(true);
                                            et_mobno.requestFocus();
                                            chk_otp.setChecked(true);
                                            lin_ckotp.setVisibility(View.VISIBLE);

                                            if (chk_otp.isChecked()) {
                                                btn_snd_otp.setVisibility(View.VISIBLE);
                                                GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                            } else {
                                                btn_snd_otp.setVisibility(View.GONE);
                                            }

                                            mobile_number_kyc.setVisibility(View.GONE);
                                            ll_mobileno_otp.setVisibility(View.VISIBLE);
                                            tv_mob_note.setVisibility(View.VISIBLE);

                                        } else {
                                            GlobalClass.redirectToLogin(getActivity());
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
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);
                                    vial_number.setVisibility(View.VISIBLE);


                                    labname_linear.setVisibility(View.GONE);
                                    ref_check.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);

                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);

                                                if (enteredString.length() > 0) {
                                                    GlobalClass.SetEditText(kyc_format, enteredString.substring(1));
                                                } else {

                                                    GlobalClass.SetEditText(kyc_format, "");
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
                                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                        flag = false;
                                                        GlobalClass.SetEditText(kyc_format, checkNumber);
                                                    } else {
                                                        flag = false;
                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(getActivity());

                                                        try {
                                                            if (ControllersGlobalInitialiser.checkNumber_controller != null) {
                                                                ControllersGlobalInitialiser.checkNumber_controller = null;
                                                            }
                                                            ControllersGlobalInitialiser.checkNumber_controller = new CheckNumber_Controller(getActivity(), Start_New_Woe.this, "3");
                                                            ControllersGlobalInitialiser.checkNumber_controller.getchecknumbercontroll(checkNumber, reques5tQueueCheckNumber);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
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
                                                    Log.v("TAG", output);
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


                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechname.getSelectedItem() != null)
                                                btechnameTopass = btechname.getSelectedItem().toString();

                                            try {
                                                if (!GlobalClass.isNull(btechnameTopass)) {
                                                    if (myPojo != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                        for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                            if (!GlobalClass.isNull(btechnameTopass) && !GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                                btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                            }
                                                        }
                                                    }

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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
                                                if (GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(woereferedby) &&
                                                                    !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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


                                            if (!GlobalClass.isNull(ageString)) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (GlobalClass.isNull(ageString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                            } else if (conertage > 120) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else if (patientAddressdataToPass.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);

                                            } else if (pincode_pass.length() < 6) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else {

                                                try {
                                                    if (GlobalClass.isNull(woereferedby)) {
                                                        if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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

                                                if (kycdata.length() == 0) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                                } else if (kycdata.length() < 10) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                                } else {

                                                    try {
                                                        if (GlobalClass.isNull(woereferedby)) {
                                                            if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                                for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                    if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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


                                                    if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                        saveDetails.commit();
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();

                                                    } else {

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
                                                        saveDetails.commit();
                                                    }


                                                }
                                            }
                                        }
                                    });
                                } else if (selectTypeSpinner.getSelectedItem().equals("HOME")) {

                                    try {
                                        if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                            Enablefields();
                                            Home_mobile_number_kyc.setVisibility(View.VISIBLE);
                                            ll_mobileno_otp.setVisibility(View.GONE);
                                            tv_mob_note.setVisibility(View.GONE);
                                        } else if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("YES")) {
                                            Disablefields();
                                            et_mobno.setFocusable(true);
                                            et_mobno.requestFocus();
                                            chk_otp.setChecked(true);
                                            lin_ckotp.setVisibility(View.VISIBLE);
                                            if (chk_otp.isChecked()) {
                                                btn_snd_otp.setVisibility(View.VISIBLE);
                                                GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                            } else {
                                                btn_snd_otp.setVisibility(View.GONE);
                                            }
                                            Home_mobile_number_kyc.setVisibility(View.GONE);
                                            ll_mobileno_otp.setVisibility(View.VISIBLE);
                                            tv_mob_note.setVisibility(View.VISIBLE);
                                        } else {
                                            GlobalClass.redirectToLogin(getActivity());
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
                                    camp_layout_woe.setVisibility(View.GONE);
                                    btech_linear_layout.setVisibility(View.VISIBLE);
                                    pincode_linear_data.setVisibility(View.VISIBLE);
                                    home_layout.setVisibility(View.VISIBLE);


                                    vial_number.setVisibility(View.VISIBLE);
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    labname_linear.setVisibility(View.GONE);
                                    GlobalClass.SetEditText(patientAddress, "");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                    Log.v("TAG", output);
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

                                            if (!GlobalClass.isNull(btechname.getSelectedItem().toString()))
                                                btechnameTopass = btechname.getSelectedItem().toString();


                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = home_kyc_format.getText().toString();
                                            }


                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechnameTopass != null) {
                                                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {

                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null || referenceBy.length() <= 1) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
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


                                            if (!GlobalClass.isNull(ageString)) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (GlobalClass.isNull(ageString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                            } else if (conertage > 120) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else if (patientAddressdataToPass.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (pincode_pass.length() < 6) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else {

                                                if (kycdata.length() == 0) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                                } else if (kycdata.length() < 10) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                                } else {

                                                    if (GlobalClass.isNull(woereferedby)) {
                                                        if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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

                                                    if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                        saveDetails.commit();
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();

                                                    } else {
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
                                                        saveDetails.commit();
                                                    }


                                                }

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
                        vial_number.getText().clear();
                        id_for_woe.getText().clear();

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListsecond);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);
                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (selectTypeSpinner.getSelectedItemPosition() == 0) {
                                    GlobalClass.SetText(samplecollectionponit, MessageConstants.SEARCH_SMP_TYPE);
                                    et_mobno.getText().clear();
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    lin_otp.setVisibility(View.GONE);
                                    tv_timer.setVisibility(View.GONE);
                                    Enablefields();

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
                                    AGE_layout.setVisibility(View.GONE);
                                    time_layout.setVisibility(View.VISIBLE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                    Log.v("TAG", output);
                                                } catch (ParseException pe) {
                                                    pe.printStackTrace();
                                                }
                                                e.printStackTrace();
                                            }

                                            if (!GlobalClass.isNull(selectTypeSpinner.getSelectedItem().toString()))
                                                typename = selectTypeSpinner.getSelectedItem().toString();

                                            if (!GlobalClass.isNull(brand_spinner.getSelectedItem().toString()))
                                                brandNames = brand_spinner.getSelectedItem().toString();

                                            if (!GlobalClass.isNull(vial_number.getText().toString()))
                                                getVial_numbver = vial_number.getText().toString();

                                            if (!GlobalClass.isNull(age.getText().toString()))
                                                ageString = age.getText().toString();

                                            if (!GlobalClass.isNull(referedbyText.getText().toString())) {
                                                GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                                woereferedby = referedbyText.getText().toString();
                                            }

                                            if (!GlobalClass.isNull(samplecollectionponit.getText().toString())) {
                                                GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();
                                                scpoint = samplecollectionponit.getText().toString();
                                            }

                                            if (!GlobalClass.isNull(kyc_format.getText().toString()))
                                                kycdata = kyc_format.getText().toString();

                                            if (!GlobalClass.isNull(samplecollectionponit.getText().toString()))
                                                getLabName = samplecollectionponit.getText().toString();

                                            getVial_numbver = vial_number.getText().toString();

                                            typename = selectTypeSpinner.getSelectedItem().toString();

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

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null || referenceBy.length() <= 1) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
                                                } else {
                                                    if (!GlobalClass.isNull(referenceBy) && referenceBy.equalsIgnoreCase("SELF")) {
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName())&& woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }

                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (scpoint.equalsIgnoreCase(MessageConstants.SEARCH_SMP_TYPE) || scpoint.equals("") || scpoint.equals(null)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_scp, 2);
                                            } else if (referenceBy == null || referenceBy.equals("") || referenceBy.length() <= 1) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else if (getLabName.equalsIgnoreCase(MessageConstants.SEARCH_SMP_TYPE)) {
                                                GlobalClass.showTastyToast(getActivity(), MessageConstants.SL_SMPLTIME, 2);
                                            } else {

                                                if (GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) &&
                                                                    woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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
                                                    if (GlobalClass.checkArray(labs)) {
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

                                                sctHr = timehr.getSelectedItem().toString();
                                                sctMin = timesecond.getSelectedItem().toString();
                                                sctSEc = timeampm.getSelectedItem().toString();
                                                final String getFinalAge = age.getText().toString();
                                                final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                final String getFinalDate = dateShow.getText().toString();

                                                if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                    i.putExtra("sct", getFinalTime);
                                                                    i.putExtra("date", getFinalDate);
                                                                    i.putExtra("woetype", typename);
                                                                    startActivity(i);

                                                                    Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                    saveDetails.putString("name", nameString);
                                                                    saveDetails.putString("age", "");
                                                                    saveDetails.putString("gender", "");
                                                                    saveDetails.putString("sct", getFinalTime);
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
                                                                    saveDetails.commit();
                                                                }
                                                            })
                                                            .show();
                                                } else {


                                                    Intent i = new Intent(mContext, OutLabTestsActivity.class);
                                                    i.putExtra("name", nameString);
                                                    i.putExtra("age", getFinalAge);
                                                    i.putExtra("gender", saveGenderId);
                                                    i.putExtra("sct", getFinalTime);
                                                    i.putExtra("date", getFinalDate);
                                                    i.putExtra("woetype", typename);
                                                    startActivity(i);

                                                    Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                    saveDetails.putString("name", nameString);
                                                    saveDetails.putString("age", "");
                                                    saveDetails.putString("gender", "");
                                                    saveDetails.putString("sct", getFinalTime);
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
                                                    saveDetails.commit();
                                                }
                                            }


                                        }
                                    });


                                } else if (selectTypeSpinner.getSelectedItemPosition() == 1) {


                                    if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (!GlobalClass.isNull(Constants.preotp) &&  Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        chk_otp.setChecked(true);
                                        lin_ckotp.setVisibility(View.VISIBLE);
                                        if (chk_otp.isChecked()) {
                                            btn_snd_otp.setVisibility(View.VISIBLE);
                                            GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                        } else {
                                            btn_snd_otp.setVisibility(View.GONE);
                                        }


                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                        tv_mob_note.setVisibility(View.VISIBLE);
                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
                                    }

                                    Home_mobile_number_kyc.setVisibility(View.GONE);

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
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);

                                                if (enteredString.length() > 0) {
                                                    GlobalClass.SetEditText(kyc_format, enteredString.substring(1));
                                                } else {
                                                    GlobalClass.SetEditText(kyc_format, "");
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
                                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                        flag = false;
                                                        GlobalClass.SetEditText(kyc_format, checkNumber);
                                                    } else {
                                                        flag = false;

                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(getActivity());

                                                        try {
                                                            if (ControllersGlobalInitialiser.checkNumber_controller != null) {
                                                                ControllersGlobalInitialiser.checkNumber_controller = null;
                                                            }
                                                            ControllersGlobalInitialiser.checkNumber_controller = new CheckNumber_Controller(getActivity(), Start_New_Woe.this, "4");
                                                            ControllersGlobalInitialiser.checkNumber_controller.getchecknumbercontroll(checkNumber, reques5tQueueCheckNumber);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

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
                                                    Log.v("TAG", output);
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

                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!GlobalClass.isNull(btechnameTopass)) {
                                                if (GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (!GlobalClass.isNull(btechnameTopass) && !GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }
                                            }


                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (patientAddressdataToPass.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (pincode_pass.length() < 6) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else {
                                                if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                                                    getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                                                }


                                                if (GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null) {
                                                        if (GlobalClass.checkArray(obj.getMASTERS().getREF_DR())){
                                                            for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                                if (woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                    referenceBy = woereferedby;
                                                                    referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                                }
                                                            }
                                                        }

                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }

                                                if (kycdata.length() == 0) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                                } else if (kycdata.length() < 10) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                                } else {

                                                    final String getAgeType = spinyr.getSelectedItem().toString();
                                                    String sctDate = dateShow.getText().toString();
                                                    sctHr = timehr.getSelectedItem().toString();
                                                    sctMin = timesecond.getSelectedItem().toString();
                                                    sctSEc = timeampm.getSelectedItem().toString();
                                                    final String getFinalAge = age.getText().toString();
                                                    final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                    final String getFinalDate = dateShow.getText().toString();


                                                    if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                        i.putExtra("sct", getFinalTime);
                                                                        i.putExtra("date", getFinalDate);
                                                                        i.putExtra("woetype", typename);
                                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                                        startActivity(i);
                                                                        Log.e(TAG, "onClick: lab add and lab id " + getTSP_Address);
                                                                        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                        saveDetails.putString("name", nameString);
                                                                        saveDetails.putString("age", "");
                                                                        saveDetails.putString("gender", "");
                                                                        saveDetails.putString("sct", getFinalTime);
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
                                                                        saveDetails.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        Intent i = new Intent(mContext, OutLabTestsActivity.class);
                                                        i.putExtra("name", nameString);
                                                        i.putExtra("age", getFinalAge);
                                                        i.putExtra("gender", saveGenderId);
                                                        i.putExtra("sct", getFinalTime);
                                                        i.putExtra("date", getFinalDate);
                                                        i.putExtra("woetype", typename);
                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                        startActivity(i);
                                                        Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass);
                                                        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                        saveDetails.putString("name", nameString);
                                                        saveDetails.putString("age", "");
                                                        saveDetails.putString("gender", "");
                                                        saveDetails.putString("sct", getFinalTime);
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
                                                        saveDetails.commit();
                                                    }

                                                }


                                            }

                                        }
                                    });

                                } else if (selectTypeSpinner.getSelectedItemPosition() == 2) {


                                    if (Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        chk_otp.setChecked(true);
                                        lin_ckotp.setVisibility(View.VISIBLE);
                                        if (chk_otp.isChecked()) {
                                            btn_snd_otp.setVisibility(View.VISIBLE);
                                            GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                        } else {
                                            btn_snd_otp.setVisibility(View.GONE);
                                        }


                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                        tv_mob_note.setVisibility(View.VISIBLE);
                                        Home_mobile_number_kyc.setVisibility(View.GONE);
                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
                                    }


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
                                    GlobalClass.SetEditText(patientAddress, "");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                    Log.v("TAG", output);
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

                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (!GlobalClass.isNull(btechnameTopass)) {
                                                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (!GlobalClass.isNull(btechnameTopass) && !GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null &&
                                                        GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else if (patientAddressdataToPass.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_addre, 2);
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (pincode_pass.length() < 6) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else {

                                                if (kycdata.length() == 0) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                                } else if (kycdata.length() < 10) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                                } else {
                                                    final String getAgeType = spinyr.getSelectedItem().toString();
                                                    String sctDate = dateShow.getText().toString();
                                                    sctHr = timehr.getSelectedItem().toString();
                                                    sctMin = timesecond.getSelectedItem().toString();
                                                    sctSEc = timeampm.getSelectedItem().toString();
                                                    final String getFinalAge = age.getText().toString();
                                                    final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                    final String getFinalDate = dateShow.getText().toString();

                                                    if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                        i.putExtra("sct", getFinalTime);
                                                                        i.putExtra("date", getFinalDate);
                                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                                        startActivity(i);
                                                                        Log.e(TAG, "onClick: lab add and lab id " + patientAddressdataToPass + labIDTopass);
                                                                        SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                        saveDetails.putString("name", nameString);
                                                                        saveDetails.putString("age", "");
                                                                        saveDetails.putString("gender", "");
                                                                        saveDetails.putString("sct", getFinalTime);
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
                                                                        saveDetails.commit();
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();

                                                    } else {
                                                        Intent i = new Intent(mContext, OutLabTestsActivity.class);
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
                                                        saveDetails.putString("age", "");
                                                        saveDetails.putString("gender", "");
                                                        saveDetails.putString("sct", getFinalTime);
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
                                                        saveDetails.commit();
                                                    }


                                                }

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
                        vial_number.getText().clear();
                        id_for_woe.getText().clear();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListsecond);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);
                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (selectTypeSpinner.getSelectedItemPosition() == 0) {

                                    Enablefields();

                                    if (yourCountDownTimer != null) {
                                        yourCountDownTimer.cancel();
                                        yourCountDownTimer = null;
                                        btn_snd_otp.setEnabled(true);
                                        btn_snd_otp.setClickable(true);
                                    }

                                    et_mobno.getText().clear();
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                    tv_mob_note.setVisibility(View.GONE);
                                    et_mobno.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    lin_otp.setVisibility(View.GONE);
                                    tv_timer.setVisibility(View.GONE);

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
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                    Log.v("TAG", output);
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

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) &&
                                                                woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                            referenceBy = woereferedby;
                                                            referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                        }
                                                    }
                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!GlobalClass.isNull(ageString)) {
                                                conertage = Integer.parseInt(ageString);
                                            }

                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (GlobalClass.isNull(ageString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                            } else if (conertage > 120) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (scpoint.equals(MessageConstants.SEARCH_SMP_TYPE) || scpoint.equals(null)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_scp, 2);
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else if (getLabName.equalsIgnoreCase(MessageConstants.SEARCH_SMP_TYPE)) {
                                                GlobalClass.showTastyToast(getActivity(), MessageConstants.SL_SMPLTIME, 2);
                                            } else {

                                                if (GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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
                                                final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                final String getFinalDate = dateShow.getText().toString();

                                                if (referenceBy.equalsIgnoreCase("SELF") || TextUtils.isEmpty(referredID)) {
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
                                                                    i.putExtra("sct", getFinalTime);
                                                                    i.putExtra("date", getFinalDate);
                                                                    i.putExtra("woetype", typename);
                                                                    startActivity(i);

                                                                    Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                                    saveDetails.putString("name", nameString);
                                                                    saveDetails.putString("age", getFinalAge);
                                                                    saveDetails.putString("gender", saveGenderId);
                                                                    saveDetails.putString("sct", getFinalTime);
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
                                                                    saveDetails.commit();
                                                                }
                                                            })
                                                            .show();
                                                } else {


                                                    Intent i = new Intent(mContext, OutLabTestsActivity.class);
                                                    i.putExtra("name", nameString);
                                                    i.putExtra("age", getFinalAge);
                                                    i.putExtra("gender", saveGenderId);
                                                    i.putExtra("sct", getFinalTime);
                                                    i.putExtra("date", getFinalDate);
                                                    i.putExtra("woetype", typename);
                                                    startActivity(i);

                                                    Log.e(TAG, "onClick: lab add and lab id " + labAddressTopass + labIDTopass);
                                                    GlobalClass.setReferenceBy_Name = referenceBy;
                                                    SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
                                                    saveDetails.putString("name", nameString);
                                                    saveDetails.putString("age", getFinalAge);
                                                    saveDetails.putString("gender", saveGenderId);
                                                    saveDetails.putString("sct", getFinalTime);
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
                                                    saveDetails.commit();
                                                }
                                            }


                                        }
                                    });


                                } else if (selectTypeSpinner.getSelectedItemPosition() == 1) {


                                    if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        chk_otp.setChecked(true);
                                        lin_ckotp.setVisibility(View.VISIBLE);
                                        if (chk_otp.isChecked()) {
                                            btn_snd_otp.setVisibility(View.VISIBLE);
                                            GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                        } else {
                                            btn_snd_otp.setVisibility(View.GONE);
                                        }
                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                        tv_mob_note.setVisibility(View.VISIBLE);
                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
                                    }


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
                                    AGE_layout.setVisibility(View.VISIBLE);
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);

                                                if (enteredString.length() > 0) {
                                                    GlobalClass.SetEditText(kyc_format, enteredString.substring(1));
                                                } else {
                                                    GlobalClass.SetEditText(kyc_format, "");
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
                                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                        flag = false;
                                                        GlobalClass.SetEditText(kyc_format, checkNumber);
                                                    } else {
                                                        flag = false;
                                                        RequestQueue reques5tQueueCheckNumber = GlobalClass.setVolleyReq(getActivity());

                                                        try {
                                                            if (ControllersGlobalInitialiser.checkNumber_controller != null) {
                                                                ControllersGlobalInitialiser.checkNumber_controller = null;
                                                            }
                                                            ControllersGlobalInitialiser.checkNumber_controller = new CheckNumber_Controller(getActivity(), Start_New_Woe.this, "5");
                                                            ControllersGlobalInitialiser.checkNumber_controller.getchecknumbercontroll(checkNumber, reques5tQueueCheckNumber);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
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
                                                    Log.v("TAG", output);
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

                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }


                                            labAddressTopass = "";
                                            labIDTopass = "";
                                            getcampIDtoPass = "";

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {

                                                    for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                        if (!GlobalClass.isNull(woereferedby) && !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) &&woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
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
                                                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (!GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }
                                            }

                                            if (!GlobalClass.isNull(ageString)) {
                                                conertage = Integer.parseInt(ageString);
                                            }


                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (GlobalClass.isNull(ageString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                            } else if (conertage > 120) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else if (patientAddressdataToPass.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (pincode_pass.length() < 6) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else {
                                                try {
                                                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                                                        getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                if (GlobalClass.isNull(woereferedby)) {
                                                    if (obj != null && obj.getMASTERS() != null && obj.getMASTERS().getREF_DR() != null &&
                                                            obj.getMASTERS().getREF_DR().length != 0) {

                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(woereferedby) &&  !GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    referenceBy = woereferedby;
                                                    referredID = "";
                                                }

                                                if (kycdata.length() == 0) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                                } else if (kycdata.length() < 10) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                                } else {

                                                    final String getAgeType = spinyr.getSelectedItem().toString();
                                                    String sctDate = dateShow.getText().toString();
                                                    sctHr = timehr.getSelectedItem().toString();
                                                    sctMin = timesecond.getSelectedItem().toString();
                                                    sctSEc = timeampm.getSelectedItem().toString();
                                                    final String getFinalAge = age.getText().toString();
                                                    final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                    final String getFinalDate = dateShow.getText().toString();


                                                    if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                        i.putExtra("sct", getFinalTime);
                                                                        i.putExtra("date", getFinalDate);
                                                                        i.putExtra("woetype", typename);
                                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                                        startActivity(i);
                                                                        Log.e(TAG, "onClick: lab add and lab id " + getTSP_Address);
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
                                                                        saveDetails.commit();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        Intent i = new Intent(mContext, OutLabTestsActivity.class);
                                                        i.putExtra("name", nameString);
                                                        i.putExtra("age", getFinalAge);
                                                        i.putExtra("gender", saveGenderId);
                                                        i.putExtra("sct", getFinalTime);
                                                        i.putExtra("date", getFinalDate);
                                                        i.putExtra("woetype", typename);
                                                        GlobalClass.setReferenceBy_Name = referenceBy;
                                                        startActivity(i);
                                                        Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass);
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
                                                        saveDetails.commit();
                                                    }
                                                }
                                            }

                                        }
                                    });

                                } else if (selectTypeSpinner.getSelectedItemPosition() == 2) {


                                    if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                        tv_mob_note.setVisibility(View.GONE);
                                    } else if (!GlobalClass.isNull(Constants.preotp) && Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        chk_otp.setChecked(true);
                                        lin_ckotp.setVisibility(View.VISIBLE);
                                        if (chk_otp.isChecked()) {
                                            btn_snd_otp.setVisibility(View.VISIBLE);
                                            GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
                                        } else {
                                            btn_snd_otp.setVisibility(View.GONE);
                                        }
                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                        tv_mob_note.setVisibility(View.VISIBLE);

                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
                                    }


                                    Home_mobile_number_kyc.setVisibility(View.GONE);
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
                                    GlobalClass.SetEditText(patientAddress, "");
                                    ref_check.setVisibility(View.GONE);
                                    ref_check_linear.setVisibility(View.VISIBLE);
                                    uncheck_ref.setVisibility(View.VISIBLE);
                                    refby_linear.setVisibility(View.VISIBLE);
                                    GlobalClass.SetAutocomplete(referedbyText, "");
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
                                                    Log.v("TAG", output);
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

                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
                                            } else {
                                                kycdata = kyc_format.getText().toString();
                                            }

                                            labIDTopass = "";
                                            labAddressTopass = "";
                                            getcampIDtoPass = "";

                                            if (btechnameTopass != null) {
                                                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {
                                                    for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                                        if (!GlobalClass.isNull(btechnameTopass) && !GlobalClass.isNull(myPojo.getMASTERS().getBCT_LIST()[j].getNAME()) && btechnameTopass.equals(myPojo.getMASTERS().getBCT_LIST()[j].getNAME())) {
                                                            btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                                                        }
                                                    }
                                                }

                                            }

                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (referenceBy == null) {
                                                    GlobalClass.showTastyToast(getActivity(), MessageConstants.REFBYTOAST, 2);
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


                                            if (GlobalClass.isNull(woereferedby)) {
                                                if (obj != null) {
                                                    if (GlobalClass.checkArray(obj.getMASTERS().getREF_DR())){
                                                        for (int i = 0; i < obj.getMASTERS().getREF_DR().length; i++) {
                                                            if (!GlobalClass.isNull(obj.getMASTERS().getREF_DR()[i].getName()) && woereferedby.equalsIgnoreCase(obj.getMASTERS().getREF_DR()[i].getName())) {
                                                                referenceBy = woereferedby;
                                                                referredID = obj.getMASTERS().getREF_DR()[i].getId();
                                                            }
                                                        }
                                                    }

                                                }
                                            } else {
                                                referenceBy = woereferedby;
                                                referredID = "";
                                            }


                                            if (!GlobalClass.isNull(ageString)) {
                                                conertage = Integer.parseInt(ageString);
                                            }
                                            if (GlobalClass.isNull(getVial_numbver)) {
                                                vial_number.setError(ToastFile.vial_no);
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                                            } else if (GlobalClass.isNull(nameString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name, 2);
                                            } else if (nameString.length() < 2) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_name_woe, 2);
                                            } else if (GlobalClass.isNull(ageString)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_age, 2);
                                            } else if (conertage > 120) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.invalidage, 2);
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.ent_gender, 2);
                                            } else if (sctHr.equals("HR")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_hr, 2);
                                            } else if (sctMin.equals("MIN")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_min, 2);
                                            } else if (sctSEc.equals("AM/PM")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.slt_ampm, 2);
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.sct_grt_than_crnt_tm, 2);
                                            } else if (patientAddressdataToPass.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_addr, 2);
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.addre25long, 2);
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (pincode_pass.length() < 6) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_pincode, 2);
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.btech_name, 2);
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                GlobalClass.showTastyToast(getActivity(), ToastFile.crt_ref_by, 2);
                                            } else {

                                                if (kycdata.length() == 0) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_kyc_empty, 2);
                                                } else if (kycdata.length() < 10) {
                                                    GlobalClass.showTastyToast(getActivity(), ToastFile.crt_MOB_num, 2);
                                                } else {
                                                    final String getAgeType = spinyr.getSelectedItem().toString();
                                                    String sctDate = dateShow.getText().toString();
                                                    sctHr = timehr.getSelectedItem().toString();
                                                    sctMin = timesecond.getSelectedItem().toString();
                                                    sctSEc = timeampm.getSelectedItem().toString();
                                                    final String getFinalAge = age.getText().toString();
                                                    final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                                    final String getFinalDate = dateShow.getText().toString();

                                                    if (referenceBy.equalsIgnoreCase("SELF") || referredID.equalsIgnoreCase("")) {
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
                                                                        saveDetails.commit();
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();

                                                    } else {
                                                        Intent i = new Intent(mContext, OutLabTestsActivity.class);
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
                                                        saveDetails.commit();
                                                    }
                                                }


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

        if (leadOrderIdMainModel != null && leadOrderIdMainModel.getLeads() != null && leadOrderIdMainModel.getLeads()[0].getLeadData() != null
                && leadOrderIdMainModel.getLeads()[0].getLeadData().length != 0) {

            for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
                if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                    sizeflag = true;
                    break;
                }
            }
        }


        RecyclerView recyclerView = CustomLeaddialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        AdapterRe adapterRe = new AdapterRe((ManagingTabsActivity) getActivity(), leads, getVial_numbver, leadOrderIdMainModel, Start_New_Woe.this);

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
        requestQueueAddRecheck = GlobalClass.setVolleyReq(getActivity());

        try {
            if (ControllersGlobalInitialiser.barcodedetail_controller != null) {
                ControllersGlobalInitialiser.barcodedetail_controller = null;
            }
            ControllersGlobalInitialiser.barcodedetail_controller = new Barcodedetail_Controller(getActivity(), Start_New_Woe.this);
            ControllersGlobalInitialiser.barcodedetail_controller.getbarcodedetais(api_key, user, passBarcode, requestQueueAddRecheck);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTspNumber() {
        try {
            RequestQueue requestQueue = GlobalClass.setVolleyReq(getActivity());

            try {
                if (ControllersGlobalInitialiser.getwomaster_controller != null) {
                    ControllersGlobalInitialiser.getwomaster_controller = null;
                }
                ControllersGlobalInitialiser.getwomaster_controller = new Getwomaster_Controller(getActivity(), Start_New_Woe.this, 1);
                ControllersGlobalInitialiser.getwomaster_controller.getwoeMaster_Controller(api_key, user, requestQueue);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchData() {
        RequestQueue requestQueue = GlobalClass.setVolleyReq(getActivity());

        String url = Api.SOURCEils + "" + api_key + "/" + "" + user + "/B2BAPP/getclients";

        try {
            if (ControllersGlobalInitialiser.clientController != null) {
                ControllersGlobalInitialiser.clientController = null;
            }
            ControllersGlobalInitialiser.clientController = new ClientController(getActivity(), Start_New_Woe.this);
            ControllersGlobalInitialiser.clientController.getclientController(url, requestQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void autotextcompletefunction(JSONObject response) {
        Gson gson = new Gson();
        sourceILSMainModel = gson.fromJson(response.toString(), SourceILSMainModel.class);
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
        Gson gson22 = new Gson();
        String json22 = gson22.toJson(sourceILSMainModel);
        prefsEditor1.putString("savelabnames", json22);
        prefsEditor1.commit();
        callAdapter(sourceILSMainModel);

        SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gsondata = new Gson();
        String jsondata = appSharedPrefsdata.getString("savelabnames", "");
        obj = gsondata.fromJson(jsondata, SourceILSMainModel.class);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        GlobalClass.SetText(dateShow, putDate);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void callAdapter(final SourceILSMainModel obj) {

        if (obj != null && obj.getMASTERS() != null && GlobalClass.checkArray(obj.getMASTERS().getLABS())) {
            getReferenceNmae = new ArrayList<>();
            getLabNmae = new ArrayList<>();
            statusForColor = new ArrayList<>();

            for (int i = 0; i < obj.getMASTERS().getLABS().length; i++) {

                SpannableStringBuilder builder = new SpannableStringBuilder();

                getLabNmae.add(obj.getMASTERS().getLABS()[i].getLabName() + "-" + " " + obj.getMASTERS().getLABS()[i].getClientid());
                statusForColor.add(obj.getMASTERS().getLABS()[i].getStatus());
                labs = obj.getMASTERS().getLABS();
            }

            samplecollectionponit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogue(obj);
                }
            });


            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// WithAnimation


            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {

                    GlobalClass.SetAutocomplete(referedbyText, s);
                    add_ref.setVisibility(View.VISIBLE);
                }
            });

            spinnerDialogRef.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    GlobalClass.SetAutocomplete(referedbyText, item);
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

            }
        }

        getReferenceNmae = new ArrayList<>();
        getReferenceName1 = new ArrayList<>();

        if (GlobalClass.checkArray(obj.getMASTERS().getREF_DR())) {
            for (int j = 0; j < obj.getMASTERS().getREF_DR().length; j++) {
                getReferenceNmae.add(obj.getMASTERS().getREF_DR()[j].getName());
                getReferenceName1.add(obj.getMASTERS().getREF_DR()[j]);
                ref_drs = obj.getMASTERS().getREF_DR();
            }
        } else {
            ref_check_linear.setVisibility(View.VISIBLE);
            refby_linear.setVisibility(View.GONE);
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
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scp_name.setLayoutManager(linearLayoutManager);

        GlobalClass.SetText(title, "Search SCP");

        labDetailsArrayList = new ArrayList<>();
        if (obj.getMASTERS() != null) {
            for (int i = 0; i < obj.getMASTERS().getLABS().length; i++) {
                labDetailsArrayList.add(obj.getMASTERS().getLABS()[i]);
            }
        }

        final SampleCollectionAdapter sampleCollectionAdapter = new SampleCollectionAdapter(getActivity(), labDetailsArrayList);
        sampleCollectionAdapter.setOnItemClickListener(new SampleCollectionAdapter.OnItemClickListener() {
            @Override
            public void onPassSgcID(LABS pos) {
                GlobalClass.SetText(samplecollectionponit, pos.getLabName() + " - " + pos.getClientid());
                selectedLABS = pos;
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
                if (GlobalClass.CheckArrayList(labDetailsArrayList)) {
                    for (int i = 0; i < labDetailsArrayList.size(); i++) {

                        final String text1 = labDetailsArrayList.get(i).getLabName().toLowerCase();
                        final String text2 = labDetailsArrayList.get(i).getClientid().toLowerCase();

                        if (!GlobalClass.isNull(labDetailsArrayList.get(i).getClientid())) {
                            clientId = labDetailsArrayList.get(i).getClientid().toLowerCase();
                        }
                        if (!GlobalClass.isNull(labDetailsArrayList.get(i).getLabName())) {
                            labname = labDetailsArrayList.get(i).getLabName().toLowerCase();
                        }

                        if (text1.contains(s1) || (clientId != null && clientId.contains(s1)) ||
                                (labname != null && labname.contains(s1))) {
                            String testname = (labDetailsArrayList.get(i).getLabName());
                            filterPatientsArrayList.add(labDetailsArrayList.get(i));

                        }
                        sampleCollectionAdapter.filteredArraylist(filterPatientsArrayList);
                        sampleCollectionAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if (GlobalClass.setFlagToClose) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalClass.isAutoTimeSelected(getActivity());
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getView() != null) {
            isViewShown = true;
            if (GlobalClass.isNetworkAvailable(getActivity())) {
                ((ManagingTabsActivity) getActivity()).getProfileDetails(getActivity());
            }
        } else {
            isViewShown = false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sendotp:
                if (!GlobalClass.isNetworkAvailable(getActivity())) {
                    GlobalClass.showTastyToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 2);
                } else {

                    if (mobno_verify) {

                        if (!GlobalClass.isNull(btn_snd_otp.getText().toString()) && btn_snd_otp.getText().toString().equalsIgnoreCase("Reset")) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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

                                    GlobalClass.SetEditText(et_otp, "");
                                    GlobalClass.SetButtonText(btn_snd_otp, "Send OTP");
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

                        } else if (btn_snd_otp.getText().toString().equalsIgnoreCase(MessageConstants.RESENTOTP)) {
                            Log.e(TAG, "onClick: " + btn_snd_otp.getText().toString());

                            generateToken();

                        } else if (btn_snd_otp.getText().equals("Send OTP")) {
                            Log.e(TAG, "onClick: " + btn_snd_otp.getText().toString());
                            generateToken();

                        }
                    }
                }

                break;

            case R.id.btn_verifyotp:

                if (GlobalClass.isNull(et_otp.getText().toString())) {
                    GlobalClass.showTastyToast(getActivity(), MessageConstants.ENTER_OTP, 2);
                } else {
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        GlobalClass.showTastyToast(getActivity(), ToastFile.intConnection, 2);
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
        try {
            if (ControllersGlobalInitialiser.otPtoken_controller != null) {
                ControllersGlobalInitialiser.otPtoken_controller = null;
            }
            ControllersGlobalInitialiser.otPtoken_controller = new OTPtoken_controller(getActivity(), Start_New_Woe.this);
            ControllersGlobalInitialiser.otPtoken_controller.getotptokencontroller();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callsendOTP(String token, String requestId) {

        if (GlobalClass.isNull(et_mobno.getText().toString())) {
            GlobalClass.showTastyToast(getActivity(), MessageConstants.MOBNO, 2);
        } else if (et_mobno.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(getActivity(), MessageConstants.ValidMOb, 2);
            lin_otp.setVisibility(View.GONE);
        } else {

            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                GlobalClass.showAlertDialog(getActivity());
            } else {
                if (ControllersGlobalInitialiser.validateMob_controller != null) {
                    ControllersGlobalInitialiser.validateMob_controller = null;
                }
                ControllersGlobalInitialiser.validateMob_controller = new ValidateMob_Controller(Start_New_Woe.this);
                ControllersGlobalInitialiser.validateMob_controller.callvalidatemob(user, et_mobno.getText().toString(), token);
            }


        }
    }

    public void onvalidatemob(ValidateOTPmodel validateOTPmodel) {

        if (!GlobalClass.isNull(validateOTPmodel.getResponseId()) && validateOTPmodel.getResponseId().equalsIgnoreCase(Constants.RES0001)) {
            et_mobno.setEnabled(false);
            et_mobno.setClickable(false);

            lin_otp.setVisibility(View.VISIBLE);

            et_otp.setEnabled(true);
            et_otp.setClickable(true);

            btn_verifyotp.setEnabled(true);
            btn_verifyotp.setClickable(true);

            btn_verifyotp.setVisibility(View.VISIBLE);
            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.maroon));

            GlobalClass.SetButtonText(btn_verifyotp, "Verify");

            setCountDownTimer();


        } else {
            et_mobno.setEnabled(true);
            et_mobno.setClickable(true);
        }
    }


    public void onVerifyotp(VerifyotpModel validateOTPmodel) {
        if (!GlobalClass.isNull(validateOTPmodel.getResponse()) && validateOTPmodel.getResponse().equalsIgnoreCase("OTP Validated Successfully")) {
            timerflag = true;

            GlobalClass.showTastyToast(getActivity(), validateOTPmodel.getResponse(), 1);

            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer = null;
            }


            GlobalClass.SetButtonText(btn_verifyotp, "Verified");
            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.green));

            //new requirment
            et_otp.getText().clear();
            lin_otp.setVisibility(View.GONE);

            GlobalClass.SetButtonText(btn_snd_otp, "Reset");

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
            GlobalClass.SetEditText(et_otp, "");
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
                    GlobalClass.SetText(tv_timer, "Please wait 00:" + numberFormat.format(time));

                } else {
                    tv_timer.setVisibility(View.GONE);
                }

                btn_snd_otp.setEnabled(false);
                btn_snd_otp.setClickable(false);
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                GlobalClass.SetButtonText(btn_snd_otp, MessageConstants.RESENTOTP);

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

        btn_clear_data.setEnabled(false);
        btn_clear_data.setClickable(false);

        refby_linear.setVisibility(View.VISIBLE);
        referedbyText.setVisibility(View.VISIBLE);

        uncheck_ref.setVisibility(View.VISIBLE);
        ref_check.setVisibility(View.GONE);

    }

    public void Enablefields() {
        vial_number.setEnabled(true);
        vial_number.setClickable(true);

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

    public void getotptokenResponse(retrofit2.Response<Tokenresponse> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getRespId()) && response.body().getRespId().equalsIgnoreCase(Constants.RES0000)) {
                if (!GlobalClass.isNull(response.body().getToken())) {
                    Log.e(TAG, "TOKEN--->" + response.body().getToken());
                    callsendOTP(response.body().getToken(), response.body().getRequestId());

                }
            } else {
                GlobalClass.showTastyToast(getActivity(), response.body().getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getwoemasterResponse(JSONObject response) {

        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            if (response != null) {
                Gson gson = new Gson();
                myPojo = new MyPojo();
                myPojo = gson.fromJson(response.toString(), MyPojo.class);

                if (myPojo != null && myPojo.getRESPONSE() != null && !GlobalClass.isNull(myPojo.getRESPONSE()) && myPojo.getRESPONSE().equalsIgnoreCase(caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(getActivity());
                } else {
                    SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                        if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST())) {
                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                                getDatafetch.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                spinnerBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                camp_lists = myPojo.getMASTERS().getCAMP_LIST();
                                String TspNumber = myPojo.getMASTERS().getTSP_MASTER().getNumber();
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("TspNumber", 0).edit();
                                editor.putString("TSPMobileNumber", TspNumber);
                                editor.apply();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getSUB_SOURCECODE())) {
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

                    if (myPojo != null && myPojo.getMASTERS() != null && !GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST()) && myPojo.getMASTERS().getBRAND_LIST().length > 1) {

                        for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                            getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                        }

                        if (!GlobalClass.isNull(myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_name()) && myPojo.getMASTERS().getBRAND_LIST()[1].getBrand_name().equalsIgnoreCase("SMT")) {
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
                        if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBRAND_LIST()) && myPojo.getMASTERS().getBRAND_LIST().length == 1) {
                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                            }
                        }
                    }


                    try {
                        if (myPojo != null) {
                            if (myPojo.getMASTERS() != null && myPojo.getMASTERS().getTSP_MASTER() != null) {
                                String getAddress = myPojo.getMASTERS().getTSP_MASTER().getAddress();

                                SharedPreferences.Editor ScpAddress = getActivity().getSharedPreferences("ScpAddress", 0).edit();
                                ScpAddress.putString("scp_addrr", getAddress);
                                ScpAddress.commit();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getLAB_ALERTS() != null) {
                        putData = myPojo.getMASTERS().getLAB_ALERTS();
                    }

                    if (putData != null && putData.length != 0) {
                        for (int i = 0; i < putData.length; i++) {
                            items.add(putData[i]);
                        }
                    }

                    // Spinner adapter
                    try {
                        if (GlobalClass.CheckArrayList(spinnerBrandName)) {
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                                    mContext, R.layout.name_age_spinner, spinnerBrandName);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            brand_spinner.setAdapter(adapter2);
                            brand_spinner.setSelection(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    startDataSetting();
                }
            } else {
                GlobalClass.showTastyToast(getActivity(), MessageConstants.SOMETHING_WENT_WRONG, 2);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            GlobalClass.showTastyToast(getActivity(), "" + e, 2);
        }
    }

    public void getTSPResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);
            String getResponse = response.optString("RESPONSE", "");

            if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                GlobalClass.redirectToLogin(getActivity());
            } else {
                Gson gson = new Gson();
                MyPojo myPojo = gson.fromJson(response.toString(), MyPojo.class);

                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                Gson gson22 = new Gson();
                String json22 = gson22.toJson(myPojo);
                prefsEditor1.putString("getBtechnames", json22);

                prefsEditor1.commit();

                getBtechList = new ArrayList<>();
                if (myPojo != null && myPojo.getMASTERS() != null && GlobalClass.checkArray(myPojo.getMASTERS().getBCT_LIST())) {

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

                if (GlobalClass.CheckArrayList(getBtechList)) {
                    for (int i = 0; i < getBtechList.size(); i++) {
                        btechSpinner.add(getBtechList.get(i).getNAME());
                        btechname.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, btechSpinner));
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
        }

    }

    public void getclientResponse(JSONObject response) {

        try {
            Log.e(TAG, "getclient onResponse: RESPONSE" + response);
            autotextcompletefunction(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getorderdetailresponse(JSONObject response) {
        String getResponse = response.optString("RESPONSE", "");
        if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
            GlobalClass.redirectToLogin(getActivity());
        } else {
            Gson gson = new Gson();
            Log.e(TAG, "onResponse: RESPONSE" + response);

            leadOrderIdMainModel = new LeadOrderIdMainModel();
            leadOrderIdMainModel = gson.fromJson(response.toString(), LeadOrderIdMainModel.class);


            if (leadOrderIdMainModel != null && !GlobalClass.isNull(leadOrderIdMainModel.getRESPONSE()) && leadOrderIdMainModel.getRESPONSE().equals("SUCCESS")) {

                if (GlobalClass.checkArray(leadOrderIdMainModel.getLeads())) {

                    for (int i = 0; i < leadOrderIdMainModel.getLeads().length; i++) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("LeadOrderID", 0).edit();
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
                }


                if (leadOrderIdMainModel != null && leadOrderIdMainModel.getLeads() != null && leadOrderIdMainModel.getLeads().length >= 2) {
                    showDialog(getActivity(), leadOrderIdMainModel.getLeads());
                }

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LeadOrderID", MODE_PRIVATE);
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

                if (GlobalClass.CheckArrayList(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        leadTESTS += list.get(i).getTest() + ",";
                    }
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

                    GlobalClass.SetText(leadname, "Name :" + leadNAME);
                    GlobalClass.SetText(leadidtest, "Test :" + leadTESTS);
                    GlobalClass.SetText(leadrefdr, "Ref Dr :" + leadREF_BY);


                    if (leadOrderIdMainModel != null && GlobalClass.checkArray(leadOrderIdMainModel.getLeads()[0].getLeadData())) {
                        for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
                            if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                                sizeflag = true;
                                break;
                            }
                        }
                    }


                    leadlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sizeflag) {
                                Intent i = new Intent(getActivity(), MultipleLeadActivity.class);
                                i.putExtra("MyClass", leadOrderIdMainModel);
                                i.putExtra("fromcome", "woepage");
                                i.putExtra("TESTS", leadTESTS);
                                i.putExtra("SCT", leadSCT);
                                i.putExtra("LeadID", leadLEAD_ID);
                                i.putExtra("brandtype", brand_spinner.getSelectedItem().toString());
                                i.putExtra("SR_NO", getVial_numbver);
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                editor.putString("typeName", leadTYPE);
                                editor.putString("SR_NO", getVial_numbver);
                                // To retrieve object in second Activity
                                startActivity(i);
                            } else {

                                if (leadOrderIdMainModel != null && GlobalClass.checkArray(leadOrderIdMainModel.getLeads()[0].getLeadData())) {
                                    for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
                                        if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                                            sizeflag = true;
                                            break;
                                        }
                                    }
                                }

                                if (sizeflag) {
                                    Intent i = new Intent(getActivity(), MultipleLeadActivity.class);
                                    i.putExtra("MyClass", leadOrderIdMainModel);
                                    i.putExtra("fromcome", "woepage");
                                    i.putExtra("TESTS", leadTESTS);
                                    i.putExtra("SCT", leadSCT);
                                    i.putExtra("LeadID", leadLEAD_ID);
                                    i.putExtra("brandtype", brand_spinner.getSelectedItem().toString());
                                    i.putExtra("SR_NO", getVial_numbver);
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                    editor.putString("typeName", leadTYPE);
                                    editor.putString("SR_NO", getVial_numbver);
                                    // To retrieve object in second Activity
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(getActivity(), ScanBarcodeLeadId.class);
                                    i.putExtra("MyClass", leadOrderIdMainModel);
                                    i.putExtra("fromcome", "woepage");
                                    i.putExtra("TESTS", leadTESTS);
                                    i.putExtra("SCT", leadSCT);
                                    i.putExtra("LeadID", leadLEAD_ID);
                                    i.putExtra("brandtype", brand_spinner.getSelectedItem().toString());

                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                    editor.putString("typeName", leadTYPE);
                                    editor.putString("SR_NO", getVial_numbver);
                                    // To retrieve object in second Activity
                                    startActivity(i);
                                }

                            }
                        }
                    });
                }

            } else {
                leadlayout.setVisibility(View.GONE);
                next_btn.setVisibility(View.GONE);
                GlobalClass.showTastyToast(getActivity(), "No leads found", 2);
            }
        }
    }

    public void getbarcodedetail(JSONObject response) {
        Log.e(TAG, "onResponse: RESPONSE" + response);
        try {

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

                    if (!GlobalClass.isNull(RES_ID)&& RES_ID.equalsIgnoreCase(Constants.RES0000)) {
                        if (!GlobalClass.isNull(STATUS) && STATUS.equalsIgnoreCase(caps_invalidApikey)) {
                            GlobalClass.redirectToLogin(getActivity());
                        } else if (GlobalClass.isNull(PATIENT)) {
                            GlobalClass.showTastyToast(getActivity(), responseModel.getSTATUS(), 2);

                            GlobalClass.SetEditText(barcode_woe, "");
                            leadbarcodelayout.setVisibility(View.GONE);
                            next_btn.setVisibility(View.GONE);
                        } else {
                            leadbarcodelayout.setVisibility(View.VISIBLE);
                            next_btn.setVisibility(View.VISIBLE);

                            GlobalClass.SetText(leadbarcodename, "Name: " + PATIENT);
                            GlobalClass.SetText(leadidbarcodetest, "Tests: " + TESTS);
                            GlobalClass.SetText(leadbarcoderefdr, "Ref By: " + REF_DR);
                            GlobalClass.SetText(leadbarcodesct, "SCT: " + SDATE);
                        }
                    } else if (!GlobalClass.isNull(STATUS) && STATUS.equalsIgnoreCase(caps_invalidApikey)) {
                        GlobalClass.redirectToLogin(getActivity());
                    } else {
                        GlobalClass.SetEditText(barcode_woe, "");
                        GlobalClass.showTastyToast(getActivity(), responseModel.getSTATUS(), 2);
                    }
                } else {
                    GlobalClass.showTastyToast(getActivity(), ToastFile.something_went_wrong, 2);
                }
            } else {
                GlobalClass.showTastyToast(getActivity(), ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getchecknumberResp(String response, String fromwoe, String checkNumber) {

        if (!GlobalClass.isNull(fromwoe) && fromwoe.equalsIgnoreCase("1")) {
            Log.e(TAG, "onResponse: response" + response);

            String getResponse = response.toString();
            if (!GlobalClass.isNull(response.toString()) && response.equals("\"proceed\"")) {

                GlobalClass.SetEditText(et_mobno, checkNumber);
                mobno_verify = true;

                if (chk_otp.isChecked()) {
                    btn_snd_otp.setVisibility(View.VISIBLE);
                } else {
                    btn_snd_otp.setVisibility(View.GONE);
                }

            } else {
                mobno_verify = false;
                GlobalClass.SetEditText(et_mobno, "");
                GlobalClass.showTastyToast(getActivity(), getResponse, 2);
            }
        } else if (!GlobalClass.isNull(fromwoe) && fromwoe.equalsIgnoreCase("2")) {
            String getResponse = response;
            if (!GlobalClass.isNull(response) && response.equals("\"proceed\"")) {
                GlobalClass.SetEditText(kyc_format, checkNumber);
            } else {
                GlobalClass.SetEditText(kyc_format, "");

                GlobalClass.showTastyToast(getActivity(), getResponse, 2);

            }
        } else if (!GlobalClass.isNull(fromwoe) && fromwoe.equalsIgnoreCase("3")) {
            Log.e(TAG, "onResponse: response" + response);

            String getResponse = response;
            if (!GlobalClass.isNull(response) && response.equals("\"proceed\"")) {
                GlobalClass.SetEditText(kyc_format, checkNumber);
            } else {

                GlobalClass.SetEditText(kyc_format, "");
                GlobalClass.showTastyToast(getActivity(), getResponse, 2);
            }
        } else if (!GlobalClass.isNull(fromwoe) && fromwoe.equalsIgnoreCase("4")) {
            Log.e(TAG, "onResponse: response" + response);
            String getResponse = response;
            if (!GlobalClass.isNull(response) && response.equals("\"proceed\"")) {
                GlobalClass.SetEditText(kyc_format, checkNumber);
            } else {
                GlobalClass.SetEditText(kyc_format, "");
                GlobalClass.showTastyToast(getActivity(), getResponse, 2);
            }
        } else if (!GlobalClass.isNull(fromwoe) && fromwoe.equalsIgnoreCase("5")) {
            Log.e(TAG, "onResponse: response" + response);
            String getResponse = response;
            if (!GlobalClass.isNull(response) && response.equals("\"proceed\"")) {

                GlobalClass.SetEditText(kyc_format, checkNumber);
            } else {

                GlobalClass.SetEditText(kyc_format, "");
                GlobalClass.showTastyToast(getActivity(), getResponse, 2);
            }
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
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
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

            GlobalClass.SetText(dateShow, dd11 + "-" + mm11 + "-" + year);
        }
    }

    private void LeadIdwoe() {
        Enablefields();
        vial_number.getText().toString();
        id_for_woe.getText().clear();
        ll_mobileno_otp.setVisibility(View.GONE);
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

        GlobalClass.SetEditText(patientAddress, "");
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

                    GlobalClass.showTastyToast(getActivity(), ToastFile.ent_pin, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(id_for_woe, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(id_for_woe, "");
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
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        GlobalClass.showAlertDialog(getActivity());
                    } else {
                        getVial_numbver = vial_number.getText().toString();
                        if (!GlobalClass.isNull(getVial_numbver)) {
                            String getId = s.toString();
                            String getLeadId = getId.toString();
                            requestQueueNoticeBoard = GlobalClass.setVolleyReq(getActivity());


                            try {
                                if (ControllersGlobalInitialiser.getorderdetails_controller != null) {
                                    ControllersGlobalInitialiser.getorderdetails_controller = null;
                                }
                                ControllersGlobalInitialiser.getorderdetails_controller = new Getorderdetails_Controller(getActivity(), Start_New_Woe.this);
                                ControllersGlobalInitialiser.getorderdetails_controller.getorderdetailcontroller(api_key, user, getLeadId, brand_spinner, requestQueueNoticeBoard);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            vial_number.setError(ToastFile.vial_no);
                            GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                            id_for_woe.getText().clear();
                        }
                    }
                }
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLead = id_for_woe.getText().toString();
                getVial_numbver = vial_number.getText().toString();
                if (GlobalClass.isNull(getVial_numbver)) {
                    vial_number.setError(ToastFile.vial_no);
                    GlobalClass.showTastyToast(getActivity(), ToastFile.vial_no, 2);
                } else if (getLead.equals("")) {
                    GlobalClass.showTastyToast(getActivity(), "Please enter lead", 2);
                } else if (getLead.length() < 10) {
                    GlobalClass.showTastyToast(getActivity(), "Please enter correct lead", 2);
                } else if (leadNAME.equals(null)) {
                    GlobalClass.showTastyToast(getActivity(), "Please wait for some time", 2);
                } else {
                    if (leadOrderIdMainModel != null && GlobalClass.checkArray(leadOrderIdMainModel.getLeads())&& GlobalClass.checkArray(leadOrderIdMainModel.getLeads()[0].getLeadData())) {
                        for (int i = 0; i < leadOrderIdMainModel.getLeads()[0].getLeadData().length; i++) {
                            if (leadOrderIdMainModel.getLeads()[0].getLeadData()[i].getSample_type().length > 1) {
                                sizeflag = true;
                                break;
                            }
                        }
                    }


                    if (sizeflag) {
                        Intent i = new Intent(getActivity(), MultipleLeadActivity.class);
                        i.putExtra("MyClass", leadOrderIdMainModel);
                        i.putExtra("LeadID", leadLEAD_ID);
                        i.putExtra("SAMPLE_TYPE", leadSAMPLE_TYPE);
                        i.putExtra("fromcome", "woepage");
                        i.putExtra("TESTS", leadTESTS);
                        i.putExtra("SCT", leadSCT);
                        i.putExtra("SR_NO", getVial_numbver);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                        editor.putString("typeName", leadTYPE);
                        editor.putString("SR_NO", getVial_numbver);
                        // To retrieve object in second Activity
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getActivity(), ScanBarcodeLeadId.class);
                        i.putExtra("MyClass", leadOrderIdMainModel);
                        i.putExtra("LeadID", leadLEAD_ID);
                        i.putExtra("SAMPLE_TYPE", leadSAMPLE_TYPE);
                        i.putExtra("fromcome", "woepage");
                        i.putExtra("TESTS", leadTESTS);
                        i.putExtra("SCT", leadSCT);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                        editor.putString("typeName", leadTYPE);
                        editor.putString("SR_NO", getVial_numbver);
                        startActivity(i);
                    }

                }
            }
        });

    }


}