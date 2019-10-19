package com.example.e5322.thyrosoft.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.frags.RootFragment;
import com.example.e5322.thyrosoft.Adapter.CustomListAdapter;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.ValidateMob_Controller;
import com.example.e5322.thyrosoft.Controller.VerifyotpController;
import com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BRAND_LIST;
import com.example.e5322.thyrosoft.Models.Brand_type;
import com.example.e5322.thyrosoft.Models.CAMP_LIST;
import com.example.e5322.thyrosoft.Models.MyPojo;
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
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Start_New_Woe.OnFragmentInteractionListener} interface
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
    public static com.android.volley.RequestQueue PostQueOtp;
    public static ArrayList<BCT_LIST> getBtechList;
    public static InputFilter EMOJI_FILTER = new InputFilter() {

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
    private boolean timerflag = false;
    View view, viewMain;
    Date dCompare;
    String TAG = getClass().getSimpleName();
    AlertDialog alert;
    Drawable imgotp;
    Context mContext;
    boolean mobno_verify = false;
    EditText et_mobno;
    CountDownTimer yourCountDownTimer = null;
    EditText name, age, id_for_woe, barcode_woe, pincode_edt;
    REF_DR[] ref_drs;
    Brand_type[] brandType;
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
    private boolean isViewShown = false;
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
    ArrayList<com.example.e5322.thyrosoft.FinalWoeModelPost.BarcodelistModel> barcodelists;
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
    ArrayList<String> getTypeListsecond;
    ArrayList<String> getTypeListthird;
    ArrayList<String> getTypeListfourth;
    ArrayList<String> getDatafetch;
    ArrayList<String> getSubSource;
    LeadOrderIdMainModel leadOrderIdMainModel;
    String srnostr, sub_source_code_string, getLeadId, leadAddress, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE,
            leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData, getBarcode,
            emailPattern, getNumber;
    Toolbar toolbar;
    int a = 0;
    ImageView add_ref;
    ScrollView scrollView2;
    Button btn_clear_data;
    String getDatefromWOE, halfTime, DateToPass;
    TextView enetered, enter;
    Date getCurrentDateandTime;
    ImageView enter_arrow_enter, enter_arrow_entered, uncheck_ref, ref_check;
    int flagtoAdjustClisk = 0;
    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayListPatient;
    String convertedDate;
    PatientDtailsWoe patientDtailsWoe;
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
    private Start_New_Woe.OnFragmentInteractionListener mListener;
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
    private String sctHr, sctMin, sctSEc, getFinalTime, getFinalDate;
    private String getAmPm;
    private LABS selectedLABS;
    private String getLabCode;
    private Cursor cursor;
    private String labLabNAmeTopass;
    private String countData;
    private RequestQueue requestQueue;
    private String pincode_pass;
    private int numberOfLines = 10;

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
        viewMain = (View) inflater.inflate(R.layout.fragment_start__new__woe, container, false);
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

        male = (ImageView) viewMain.findViewById(R.id.male);
        male_red = (ImageView) viewMain.findViewById(R.id.male_red);
        female = (ImageView) viewMain.findViewById(R.id.female);
        female_red = (ImageView) viewMain.findViewById(R.id.female_red);
        next_btn = (Button) viewMain.findViewById(R.id.next_btn_patient);

        btn_snd_otp = viewMain.findViewById(R.id.btn_sendotp);
        btn_snd_otp.setOnClickListener(this);

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
        GlobalClass.setflagToRefreshData = false;
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
        enter_arrow_enter = (ImageView) viewMain.findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) viewMain.findViewById(R.id.enter_arrow_entered);
        uncheck_ref = (ImageView) viewMain.findViewById(R.id.uncheck_ref);
        ref_check = (ImageView) viewMain.findViewById(R.id.ref_check);
        btn_clear_data = (Button) viewMain.findViewById(R.id.btn_clear_data);
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
        dateShow.setText(showDate);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);
        minDate = myCalendar.getTime().getTime();
        myDb = new DatabaseHelper(mContext);

        name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        patientAddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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

        GlobalClass.isAutoTimeSelected(getActivity());

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Log.e("SDK_INT", "" + Build.VERSION.SDK_INT);
            samplecollectionponit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow_woe, 0);
            samplecollectionponit.setCompoundDrawablePadding(5);
        } else {
            samplecollectionponit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
        }

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

        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samplecollectionponit.setText("SEARCH SAMPLE COLLECTION POINT");
                Start_New_Woe fragment = new Start_New_Woe();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
//                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
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
                    Toast.makeText(getActivity(),
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
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
                    TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

                        if (!GlobalClass.isNetworkAvailable(getActivity())) {
                            flag = false;
                            et_mobno.setText(s);
                        } else {
                            flag = false;
                            barProgressDialog = GlobalClass.ShowprogressDialog(getActivity());
                            RequestQueue reques5tQueueCheckNumber = Volley.newRequestQueue(getActivity());
                            StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, Api.checkNumber + s, new
                                    Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e(TAG, "onResponse: response" + response);

                                            String getResponse = response;
                                            if (response.equals("\"proceed\"")) {

                                                GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                et_mobno.setText(s);
                                                mobno_verify = true;
                                            } else {
                                                mobno_verify = false;
                                                GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                et_mobno.setText("");
                                                TastyToast.makeText(getActivity(), getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

        if (GlobalClass.setScp_Constant != null) {
            samplecollectionponit.setText(GlobalClass.setScp_Constant);
        } else {
            samplecollectionponit.setText("");
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
                    TastyToast.makeText(getActivity(), ToastFile.crt_pincode, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

   /*     kyc_format.addTextChangedListener(new TextWatcher() {
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
                    TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    if (enteredString.length() > 0) {
                        kyc_format.setText(enteredString.substring(1));
                    } else {
                        kyc_format.setText("");
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

        });*/


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
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                ) {
                    TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                    TastyToast.makeText(getActivity(), "Enter correct Ref By", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        myPojo = new MyPojo();
        String json = appSharedPrefs.getString("saveAlldata", "");
        myPojo = gson.fromJson(json, MyPojo.class);

        try {
            if (myPojo != null) {
                getBrandName = new ArrayList<>();
                spinnerBrandName = new ArrayList<String>();
                /*spinnerBrandName.add("Select Brand Name");*/
                getDatafetch = new ArrayList();
                getSubSource = new ArrayList();

                if (myPojo.getMASTERS().getBRAND_LIST() != null) {
                    for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                        getDatafetch.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                        spinnerBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                        camp_lists = myPojo.getMASTERS().getCAMP_LIST();
                        // GlobalClass.getcamp_lists=camp_lists;
                        String TspNumber = myPojo.getMASTERS().getTSP_MASTER().getNumber();
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("TspNumber", 0).edit();
                        editor.putString("TSPMobileNumber", TspNumber);
                        editor.commit();
                    }
                }
                if (myPojo.getMASTERS().getSUB_SOURCECODE() != null) {
                    for (int i = 0; i < myPojo.getMASTERS().getSUB_SOURCECODE().length; i++) {
                        getSubSource.add(myPojo.getMASTERS().getSUB_SOURCECODE()[i].getSub_source_code_pass());
                    }
                }

                spinnerTypeName = new ArrayList<>();
                getTypeListfirst = new ArrayList<>();

                try {
                    if (myPojo.getMASTERS().getBRAND_LIST() != null && myPojo.getMASTERS().getBRAND_LIST().length != 0) {
                        if (myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type() != null) {
                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                            }
                        }
                    }

                    if (myPojo.getMASTERS().getBRAND_LIST().length != 0) {
                        if (myPojo.getMASTERS().getBRAND_LIST().length > 1) {
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
                        }

                        SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                        if (isLoaded == false) {
                            requestJsonObject();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String getAddress = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                SharedPreferences.Editor ScpAddress = getActivity().getSharedPreferences("ScpAddress", 0).edit();
                ScpAddress.putString("scp_addrr", getAddress);
                ScpAddress.commit();

                GlobalClass.putData = myPojo.getMASTERS().getLAB_ALERTS();
                for (int i = 0; i < GlobalClass.putData.length; i++) {

                    GlobalClass.items.add(GlobalClass.putData[i]);
                }
                // Spinner adapter

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
                    if (isLoaded == false) {
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

            if (myPojo.getMASTERS().getBCT_LIST() != null) {
                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                    getBtechList.add(myPojo.getMASTERS().getBCT_LIST()[j]);
                }
            } else {
                TastyToast.makeText(getActivity(), "Please register NED", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            try {
                if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                    getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            getCampNames.add("Select Camp");
            if (myPojo.getMASTERS().getCAMP_LIST() != null) {
                for (int k = 0; k < myPojo.getMASTERS().getCAMP_LIST().length; k++) {
                    getCampNames.add(myPojo.getMASTERS().getCAMP_LIST()[k].getVENUE());
                }
                if (getCampNames != null) {
//                    camp_spinner_olc.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinnerproperty, getCampNames));
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
                    btechname.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinnerproperty, btechSpinner));
                }
            }

        } else {
            getTspNumber();
            SharedPreferences getshared = getActivity().getApplicationContext().getSharedPreferences("profile", MODE_PRIVATE);
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

            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// With

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

        ArrayList<String> hourSin = new ArrayList<>();
        hourSin.add("HR");
        hourSin.add("01");
        hourSin.add("02");
        hourSin.add("03");
        hourSin.add("04");
        hourSin.add("05");
        hourSin.add("06");
        hourSin.add("07");
        hourSin.add("08");
        hourSin.add("09");
        hourSin.add("10");
        hourSin.add("11");
        hourSin.add("12");

        ArrayList<String> minuteSpin = new ArrayList<>();
        minuteSpin.add("MIN");
        minuteSpin.add("00");
        minuteSpin.add("05");
        minuteSpin.add("10");
        minuteSpin.add("15");
        minuteSpin.add("20");
        minuteSpin.add("25");
        minuteSpin.add("30");
        minuteSpin.add("35");
        minuteSpin.add("40");
        minuteSpin.add("45");
        minuteSpin.add("50");
        minuteSpin.add("55");

        ArrayList<String> ampmSpine = new ArrayList<>();
        ampmSpine.add("AM/PM");
        ampmSpine.add("AM");
        ampmSpine.add("PM");

        ArrayList<String> patientsagespinner = new ArrayList<>();
        patientsagespinner.add("Years");
        patientsagespinner.add("Months");
        patientsagespinner.add("Days");

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

        ArrayAdapter<String> adap = new ArrayAdapter<String>(
                mContext, R.layout.name_age_spinner, patientsagespinner);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinyr.setAdapter(adap);
        spinyr.setSelection(0);

        //vadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

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
                    Toast.makeText(getActivity(), ToastFile.crt_name, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getActivity(), ToastFile.crt_name, Toast.LENGTH_SHORT).show();
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

        return viewMain;
    }

    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;

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
            barProgressDialog = new ProgressDialog(getActivity());
            barProgressDialog.setTitle("Kindly wait ...");
            barProgressDialog.setMessage(ToastFile.processing_request);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(20);
            barProgressDialog.show();
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            Log.e(TAG, "request API: " + Api.getData + "" + api_key + "/" + "" + user +
                    "/B2BAPP/getwomaster");
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.getData + "" + api_key + "/" + "" + user +
                    "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String getResponse = response.optString("RESPONSE", "");
                        Log.e(TAG, "onResponse: RESPONSE" + response);
                        if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                        /*    if (mContext instanceof Activity) {
                                if (!((Activity) mContext).isFinishing())
                                    barProgressDialog.dismiss();
                            }*/
                            GlobalClass.hideProgress(getActivity(), barProgressDialog);
                            GlobalClass.redirectToLogin(getActivity());
                        } else {
                            Gson gson = new Gson();
                            myPojo = new MyPojo();
                            myPojo = gson.fromJson(response.toString(), MyPojo.class);
                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }*/
                           /* if (mContext instanceof Activity) {
                                if (!((Activity) mContext).isFinishing())
                                    barProgressDialog.dismiss();
                            }*/

                            GlobalClass.hideProgress(getActivity(), barProgressDialog);
                            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                            Gson gson22 = new Gson();
                            String json22 = gson22.toJson(myPojo);
                            prefsEditor1.putString("saveAlldata", json22);
                            prefsEditor1.commit();

                            fetchData();

                            isLoaded = true;
                            getBrandName = new ArrayList<>();
                            spinnerBrandName = new ArrayList<String>();
                            /*spinnerBrandName.add("Select Brand Name");*/
                            getDatafetch = new ArrayList();
                            getSubSource = new ArrayList();

                            try {
                                if (myPojo.getMASTERS().getBRAND_LIST() != null) {
                                    for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                                        getDatafetch.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                        spinnerBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                                        camp_lists = myPojo.getMASTERS().getCAMP_LIST();
                                        // GlobalClass.getcamp_lists=camp_lists;
                                        String TspNumber = myPojo.getMASTERS().getTSP_MASTER().getNumber();
                                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("TspNumber", 0).edit();
                                        editor.putString("TSPMobileNumber", TspNumber);
                                        editor.commit();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                if (myPojo.getMASTERS().getSUB_SOURCECODE() != null) {
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

                            if (myPojo.getMASTERS().getBRAND_LIST().length > 1) {

                                for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                    getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                                }
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


                            } else {
                                if (myPojo.getMASTERS().getBRAND_LIST().length == 1) {
                                    for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type().length; i++) {
                                        getTypeListfirst.add(myPojo.getMASTERS().getBRAND_LIST()[0].getBrand_type()[i].getType());
                                    }
                                }
                            }


                            String getAddress = myPojo.getMASTERS().getTSP_MASTER().getAddress();

                            SharedPreferences.Editor ScpAddress = getActivity().getSharedPreferences("ScpAddress", 0).edit();
                            ScpAddress.putString("scp_addrr", getAddress);
                            ScpAddress.commit();

                            GlobalClass.putData = myPojo.getMASTERS().getLAB_ALERTS();
                            for (int i = 0; i < GlobalClass.putData.length; i++) {
                                //                    System.out.println("length of arraylist"+GlobalClass.putData.length);
                                //                    Toast.makeText(getActivity(), "length of arraylist :"+GlobalClass.putData[i], Toast.LENGTH_SHORT).show();
                                GlobalClass.items.add(GlobalClass.putData[i]);
                            }
                            // Spinner adapter

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                                    mContext, R.layout.name_age_spinner, spinnerBrandName);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            brand_spinner.setAdapter(adapter2);
                            brand_spinner.setSelection(0);

                            startDataSetting();
                        }


                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
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

            jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest2);
            Log.e(TAG, "requestJsonObject: URL" + jsonObjectRequest2);
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
                            if (selectTypeSpinner.getSelectedItem().equals("ILS")) {
                                Enablefields();

                                if (yourCountDownTimer != null) {
                                    yourCountDownTimer.cancel();
                                    yourCountDownTimer = null;
                                    btn_snd_otp.setEnabled(true);
                                    btn_snd_otp.setClickable(true);
                                }

                                samplecollectionponit.setText("SEARCH SAMPLE COLLECTION POINT");
                                et_mobno.getText().clear();
                                ll_mobileno_otp.setVisibility(View.GONE);
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
                                ll_mobileno_otp.setVisibility(View.GONE);

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

                                        if (getVial_numbver.equals("")) {
                                            vial_number.setError(ToastFile.vial_no);
                                            Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.length() < 2) {
                                            Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                        } else if (ageString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                        } else if (saveGenderId == null || saveGenderId == "") {
                                            Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                        } else if (conertage > 120) {
                                            Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                        } else if (sctHr.equals("HR")) {
                                            Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                        } else if (sctMin.equals("MIN")) {
                                            Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                        } else if (sctSEc.equals("AM/PM")) {
                                            Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                        } else if (scpoint.equals("") || scpoint.equals(null)) {
                                            Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                        } else if (referenceBy == null || referenceBy.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                        } else if (dCompare.after(getCurrentDateandTime)) {
                                            Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                        } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                            Toast.makeText(mContext, ToastFile.slt_sample_cll_point, Toast.LENGTH_SHORT).show();
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

//                                            Req_Date_Req(getFinalTime, "hh:mm a", "HH:mm:ss");
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

                            } else if (selectTypeSpinner.getSelectedItem().equals("DPS")) {

                                if (Constants.preotp.equalsIgnoreCase("NO")) {
                                    Enablefields();
                                    mobile_number_kyc.setVisibility(View.VISIBLE);
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                } else if (Constants.preotp.equalsIgnoreCase("YES")) {
                                    Disablefields();
                                    et_mobno.setFocusable(true);
                                    et_mobno.requestFocus();
                                    mobile_number_kyc.setVisibility(View.GONE);
                                    ll_mobileno_otp.setVisibility(View.VISIBLE);
                                } else {
                                    GlobalClass.redirectToLogin(getActivity());
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
                                            TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

                                                if (!GlobalClass.isNetworkAvailable(getActivity())) {
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

                                                    RequestQueue reques5tQueueCheckNumber = Volley.newRequestQueue(getActivity());
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
                                                                        GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                                        kyc_format.setText(checkNumber);
                                                                    } else {
                                                                         /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                            barProgressDialog.dismiss();
                                                                        }*/
                                                                       /* if (mContext instanceof Activity) {
                                                                            if (!((Activity) mContext).isFinishing())
                                                                                barProgressDialog.dismiss();
                                                                        }*/

                                                                        GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                                        kyc_format.setText("");
                                                                        TastyToast.makeText(getActivity(), getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

                                        if (getVial_numbver.equals("")) {
                                            vial_number.setError(ToastFile.vial_no);
                                            Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.length() < 2) {
                                            Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                        } else if (ageString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                        } else if (conertage > 120) {
                                            Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                        } else if (saveGenderId == null || saveGenderId == "") {
                                            Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                        } else if (sctHr.equals("HR")) {
                                            Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                        } else if (sctMin.equals("MIN")) {
                                            Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                        } else if (sctSEc.equals("AM/PM")) {
                                            Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                        } else if (dCompare.after(getCurrentDateandTime)) {
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
                                            Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                        } else if (referenceBy == null || referenceBy.equals("")) {
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


                                            if (kycdata.length() < 10) {
                                                Toast.makeText(getActivity(), ToastFile.crt_kyc_num, Toast.LENGTH_SHORT).show();
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
                                                    saveDetails.commit();
                                                }


                                            }
                                        }
                                    }
                                });
                            } else if (selectTypeSpinner.getSelectedItem().equals("HOME")) {

                                if (Constants.preotp.equalsIgnoreCase("NO")) {
                                    Enablefields();
                                    Home_mobile_number_kyc.setVisibility(View.VISIBLE);
                                    ll_mobileno_otp.setVisibility(View.GONE);
                                } else if (Constants.preotp.equalsIgnoreCase("YES")) {
                                    Disablefields();
                                    et_mobno.setFocusable(true);
                                    et_mobno.requestFocus();
                                    Home_mobile_number_kyc.setVisibility(View.GONE);
                                    ll_mobileno_otp.setVisibility(View.VISIBLE);
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

                                        if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                            kycdata = et_mobno.getText().toString();
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

                                        if (getVial_numbver.equals("")) {
                                            vial_number.setError(ToastFile.vial_no);
                                            Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.length() < 2) {
                                            Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                        } else if (ageString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                        } else if (conertage > 120) {
                                            Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                        } else if (saveGenderId == null || saveGenderId == "") {
                                            Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                        } else if (sctHr.equals("HR")) {
                                            Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                        } else if (sctMin.equals("MIN")) {
                                            Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                        } else if (sctSEc.equals("AM/PM")) {
                                            Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                        } else if (dCompare.after(getCurrentDateandTime)) {
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
                                            Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                        } else if (referenceBy == null || referenceBy.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                        } else {

                                            if (kycdata.length() < 10) {
                                                Toast.makeText(getActivity(), ToastFile.crt_kyc_num, Toast.LENGTH_SHORT).show();
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

                            } else if (selectTypeSpinner.getSelectedItem().equals("CAMP")) {
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
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            }else  if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (scpoint.equals("Select Camp")) {
                                                Toast.makeText(getActivity(), "Please select camp name", Toast.LENGTH_SHORT).show();
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

                            } else if (selectTypeSpinner.getSelectedItem().equals("ORDERS")) {
                                Enablefields();
                                ll_mobileno_otp.setVisibility(View.GONE);
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
                                            Toast.makeText(getActivity(),
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

                                            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                GlobalClass.showAlertDialog(getActivity());
                                            } else {
                                                final ProgressDialog pd_dialog = GlobalClass.ShowprogressDialog(getActivity());

                                                String getId = s.toString();
                                                String getLeadId = getId.toString();
                                                requestQueueNoticeBoard = Volley.newRequestQueue(getActivity());
                                                JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.ValidateWorkOrderLeadId + api_key
                                                        + "/" + user + "/" + getLeadId + "/TTL/getorderdetails", new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {

                                                        String getResponse = response.optString("RESPONSE", "");
                                                        if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                                                            GlobalClass.redirectToLogin(getActivity());
                                                        } else {
                                                            Gson gson = new Gson();
                                                            Log.e(TAG, "onResponse: RESPONSE" + response);
                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                barProgressDialog.dismiss();
                                                            }*/
                                                           /* if (mContext instanceof Activity) {
                                                                if (!((Activity) mContext).isFinishing())
                                                                    barProgressDialog.dismiss();
                                                            }*/


//                                                    getLeadId= getId.toString();
                                                            leadOrderIdMainModel = new LeadOrderIdMainModel();
                                                            leadOrderIdMainModel = gson.fromJson(response.toString(), LeadOrderIdMainModel.class);
                                                            if (leadOrderIdMainModel.getRESPONSE().equals("SUCCESS")) {
                                                                GlobalClass.hideProgress(getActivity(), pd_dialog);
                                                                for (int i = 0; i < leadOrderIdMainModel.getLeads().length; i++) {
                                                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("LeadOrderID", 0).edit();
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
                                                                    editor.putString("leadData", leadOrderIdMainModel.getLeads()[i].getLeadData());

                                                                    editor.commit();

                                                                }
                                                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LeadOrderID", MODE_PRIVATE);

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


                                                                if (sharedPreferences != null) {
                                                                    leadlayout.setVisibility(View.VISIBLE);
                                                                    next_btn.setVisibility(View.VISIBLE);
                                                                    leadname.setText("Name :" + leadNAME);
                                                                    leadidtest.setText("Test :" + leadTESTS);
                                                                    leadrefdr.setText("Ref Dr :" + leadREF_BY);


                                                                    leadlayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            Intent i = new Intent(getActivity(), ScanBarcodeLeadId.class);
                                                                            i.putExtra("MyClass", leadOrderIdMainModel);
                                                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                                                            editor.putString("typeName", leadTYPE);
                                                                            editor.putString("SR_NO", getVial_numbver);
                                                                            // To retrieve object in second Activity
                                                                            startActivity(i);
                                                                        }
                                                                    });
                                                                }

                                                            } else {
                                                               GlobalClass.hideProgress(getActivity(), pd_dialog);

                                                                leadlayout.setVisibility(View.GONE);
                                                                next_btn.setVisibility(View.GONE);
                                                                Toast.makeText(getActivity(), "No leads found", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        if (error.networkResponse == null) {
                                                            if (error.getClass().equals(TimeoutError.class)) {
                                                               GlobalClass.hideProgress(getActivity(), pd_dialog);
                                                                TastyToast.makeText(getActivity(), "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                            }
//                                            getCityStateAPI(getId);
                                        } else {

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
                                        }
                                        if (getLead.equals("")) {
                                            Toast.makeText(getActivity(), "Please enter lead", Toast.LENGTH_SHORT).show();
                                        } else if (getLead.length() < 10) {
                                            Toast.makeText(getActivity(), "Please enter correct lead", Toast.LENGTH_SHORT).show();
                                        } else if (leadNAME.equals(null)) {
                                            Toast.makeText(getActivity(), "Please wait for some time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent i = new Intent(getActivity(), ScanBarcodeLeadId.class);
                                            i.putExtra("MyClass", leadOrderIdMainModel);
                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                                            editor.putString("typeName", leadTYPE);
                                            editor.putString("SR_NO", getVial_numbver);
                                            // To retrieve object in second Activity
                                            startActivity(i);

                                        }
                                    }
                                });


                            } else if (selectTypeSpinner.getSelectedItem().equals("ADD")) {
                                barcode_woe.setText("");
                                leadbarcodelayout.setVisibility(View.GONE);
                                ll_mobileno_otp.setVisibility(View.GONE);
                                id_layout.setVisibility(View.GONE);
                                pincode_linear_data.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.VISIBLE);
                                vial_number.setVisibility(View.GONE);
                                camp_layout_woe.setVisibility(View.GONE);
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

                                            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                GlobalClass.showAlertDialog(getActivity());
                                            } else {

                                                addRecheck(passBarcode);
                                            }


                                        } else {

                                        }
                                    }
                                });
                                referenceBy = "";

                                next_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (type_string.equals("")) {
                                            Toast.makeText(getActivity(), "Please select type name", Toast.LENGTH_SHORT).show();
                                        } else if (barcode_woe.getText().toString().equals("")) {
                                            Toast.makeText(getActivity(), ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
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
                            } else if (selectTypeSpinner.getSelectedItem().equals("RECHECK")) {
                                barcode_woe.setText("");
                                ll_mobileno_otp.setVisibility(View.GONE);
                                leadbarcodelayout.setVisibility(View.GONE);
                                id_layout.setVisibility(View.GONE);
                                pincode_linear_data.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.VISIBLE);
                                vial_number.setVisibility(View.GONE);
                                camp_layout_woe.setVisibility(View.GONE);
                                btech_linear_layout.setVisibility(View.GONE);
                                home_layout.setVisibility(View.GONE);
                                labname_linear.setVisibility(View.GONE);
                                patientAddress.setText("");
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

                                        } else {

                                        }
                                    }
                                });
                                referenceBy = "";

                                next_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (brand_string.equals("Select brand name")) {
                                            Toast.makeText(getActivity(), "Please select brand name", Toast.LENGTH_SHORT).show();
                                        } else if (type_string.equals("")) {
                                            Toast.makeText(getActivity(), "Please select type name", Toast.LENGTH_SHORT).show();
                                        } else if (barcode_woe.getText().toString().equals("")) {
                                            Toast.makeText(getActivity(), ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
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

                    if (brand_spinner.getSelectedItem().toString().equalsIgnoreCase("EQNX")) {
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, getTypeListsecond);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectTypeSpinner.setAdapter(adapter2);
                        selectTypeSpinner.setSelection(0);
                        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (selectTypeSpinner.getSelectedItemPosition() == 0) {

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

                                            /*typename = selectTypeSpinner.getSelectedItem().toString();
                                            brandNames = no_img_spinner.getSelectedItem().toString();
                                            getVial_numbver = vial_number.getText().toString();

                                            ageString = age.getText().toString();
                                            woereferedby = referedbyText.getText().toString();
                                            GlobalClass.setReferenceBy_Name = referedbyText.getText().toString();
                                            scpoint = samplecollectionponit.getText().toString();
                                            GlobalClass.setScp_Constant = samplecollectionponit.getText().toString();
                                            kycdata = kyc_format.getText().toString();
                                            getLabName = samplecollectionponit.getText().toString();*/

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

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else if (scpoint.equals("") || scpoint.equals(null)) {
                                                Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                                Toast.makeText(mContext, "Please select sample collection point", Toast.LENGTH_SHORT).show();
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


                                    mobile_number_kyc.setVisibility(View.VISIBLE);
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
                                                TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                        flag = false;
                                                        kyc_format.setText(checkNumber);
                                                    } else {
                                                        flag = false;
                                                        barProgressDialog = new ProgressDialog(getActivity());
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);
                                                        RequestQueue reques5tQueueCheckNumber = Volley.newRequestQueue(getActivity());
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
                                                                            GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                                            kyc_format.setText(checkNumber);
                                                                        } else {
                                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                                barProgressDialog.dismiss();
                                                                            }*/
                                                                            /*if (mContext instanceof Activity) {
                                                                                if (!((Activity) mContext).isFinishing())
                                                                                    barProgressDialog.dismiss();
                                                                            }*/

                                                                            GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                                            kyc_format.setText("");
                                                                            TastyToast.makeText(getActivity(), getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

                                            } else {

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
                                            btechnameTopass = btechname.getSelectedItem().toString();
                                            kycdata = kyc_format.getText().toString();
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
                                            } else if (nameString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_addr, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(mContext, ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else  if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else {
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

                                                if (kycdata.length() < 10) {
                                                    Toast.makeText(getActivity(), ToastFile.crt_kyc_num, Toast.LENGTH_SHORT).show();
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
                                    Home_mobile_number_kyc.setVisibility(View.VISIBLE);
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

                                            if (ll_mobileno_otp.getVisibility() == View.VISIBLE) {
                                                kycdata = et_mobno.getText().toString();
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


                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else  if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.equals("")) {
                                                Toast.makeText(getActivity(), ToastFile.ent_addre, Toast.LENGTH_SHORT).show();
                                            } else if (patientAddressdataToPass.length() < 25) {
                                                Toast.makeText(getActivity(), ToastFile.addre25long, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.equalsIgnoreCase("")) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (pincode_pass.length() < 6) {
                                                Toast.makeText(mContext, ToastFile.crt_pincode, Toast.LENGTH_SHORT).show();
                                            } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                                Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else  if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else {

                                                if (kycdata.length() < 10) {
                                                    Toast.makeText(getActivity(), ToastFile.crt_kyc_num, Toast.LENGTH_SHORT).show();
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
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {

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

                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else if (scpoint.equals("") || scpoint.equals(null)) {
                                                Toast.makeText(mContext, ToastFile.crt_scp, Toast.LENGTH_SHORT).show();
                                            } else if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
                                                Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                            } else if (getLabName.equalsIgnoreCase("SEARCH SAMPLE COLLECTION POINT")) {
                                                Toast.makeText(mContext, "Please select sample collection point", Toast.LENGTH_SHORT).show();
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


                                    if (Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                    } else if (Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
                                    }

                                   /* mobile_number_kyc.setVisibility(View.VISIBLE);
                                    Home_mobile_number_kyc.setVisibility(View.GONE);*/
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
                                                TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
                                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                                        flag = false;
                                                        kyc_format.setText(checkNumber);
                                                    } else {
                                                        flag = false;
                                                        barProgressDialog = new ProgressDialog(getActivity());
                                                        barProgressDialog.setTitle("Kindly wait ...");
                                                        barProgressDialog.setMessage(ToastFile.processing_request);
                                                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                                                        barProgressDialog.setProgress(0);
                                                        barProgressDialog.setMax(20);
                                                        barProgressDialog.show();
                                                        barProgressDialog.setCanceledOnTouchOutside(false);
                                                        barProgressDialog.setCancelable(false);
                                                        RequestQueue reques5tQueueCheckNumber = Volley.newRequestQueue(getActivity());
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
                                                                            GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                                            kyc_format.setText(checkNumber);
                                                                        } else {
                                                                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                                barProgressDialog.dismiss();
                                                                            }*/
                                                                           /* if (mContext instanceof Activity) {
                                                                                if (!((Activity) mContext).isFinishing())
                                                                                    barProgressDialog.dismiss();
                                                                            }*/
                                                                            GlobalClass.hideProgress(getActivity(), barProgressDialog);
                                                                            kyc_format.setText("");
                                                                            TastyToast.makeText(getActivity(), getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

                                            } else {

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


                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
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
                                                Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else  if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {
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

                                                if (kycdata.length() < 10) {
                                                    Toast.makeText(getActivity(), ToastFile.crt_kyc_num, Toast.LENGTH_SHORT).show();
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


                                    if (Constants.preotp.equalsIgnoreCase("NO")) {
                                        Enablefields();
                                        mobile_number_kyc.setVisibility(View.VISIBLE);
                                        ll_mobileno_otp.setVisibility(View.GONE);
                                    } else if (Constants.preotp.equalsIgnoreCase("YES")) {
                                        Disablefields();
                                        et_mobno.setFocusable(true);
                                        et_mobno.requestFocus();
                                        mobile_number_kyc.setVisibility(View.GONE);
                                        ll_mobileno_otp.setVisibility(View.VISIBLE);
                                    } else {
                                        GlobalClass.redirectToLogin(getActivity());
                                    }

                                   /* mobile_number_kyc.setVisibility(View.GONE);
                                    Home_mobile_number_kyc.setVisibility(View.VISIBLE);*/
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
                                            if (getVial_numbver.equals("")) {
                                                vial_number.setError(ToastFile.vial_no);
                                                Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
                                            } else if (nameString.length() < 2) {
                                                Toast.makeText(mContext, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
                                            } else if (ageString.equals("")) {
                                                Toast.makeText(mContext, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
                                            } else if (conertage > 120) {
                                                Toast.makeText(mContext, ToastFile.invalidage, Toast.LENGTH_SHORT).show();
                                            } else if (saveGenderId == null || saveGenderId == "") {
                                                Toast.makeText(mContext, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
                                            } else if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else if (dCompare.after(getCurrentDateandTime)) {
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
                                                Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                            } else  if (referenceBy == null || referenceBy.equals("")) {
                                                Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                            } else {

                                                if (kycdata.length() < 10) {
                                                    Toast.makeText(getActivity(), ToastFile.crt_kyc_num, Toast.LENGTH_SHORT).show();
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

    private void RecheckType(String passBarcode) {

        getBarcode = passBarcode;
        requestQueueAddRecheck = Volley.newRequestQueue(getActivity());
        barProgressDialog = new ProgressDialog(mContext);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.addTestsUsingBarcode + "" + api_key + "/" + user + "/" +
                getBarcode + "/getbarcodedtl", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String finalJson = response.toString();
                Log.e(TAG, "onResponse: RESPONSE" + response);
                JSONObject parentObjectOtp = null;
                try {
                    /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }*/
                    /*if (mContext instanceof Activity) {
                        if (!((Activity) mContext).isFinishing())
                            barProgressDialog.dismiss();
                    }*/

                    GlobalClass.hideProgress(getActivity(), barProgressDialog);
                    parentObjectOtp = new JSONObject(finalJson);
                    ALERT = parentObjectOtp.getString("ALERT");
                    BARCODE = parentObjectOtp.getString("BARCODE");
                    BVT_HRS = parentObjectOtp.getString("BVT_HRS");
                    LABCODE = parentObjectOtp.getString("LABCODE");
                    PATIENT = parentObjectOtp.getString("PATIENT");
                    REF_DR = parentObjectOtp.getString("REF_DR");
                    REQUESTED_ADDITIONAL_TESTS = parentObjectOtp.getString("REQUESTED_ADDITIONAL_TESTS");
                    REQUESTED_ON = parentObjectOtp.getString("REQUESTED_ON");
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    SDATE = parentObjectOtp.getString("SDATE");
                    SL_NO = parentObjectOtp.getString("SL_NO");
                    STATUS = parentObjectOtp.getString("STATUS");
                    SU_CODE1 = parentObjectOtp.getString("SU_CODE1");
                    SU_CODE2 = parentObjectOtp.getString("SU_CODE2");
                    TESTS = parentObjectOtp.getString("TESTS");


                    if (STATUS.equalsIgnoreCase(caps_invalidApikey)) {
                        GlobalClass.redirectToLogin(getActivity());
                    } else if (PATIENT.equals("null")) {
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


                } catch (JSONException e) {
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
                        TastyToast.makeText(getActivity(), "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        Log.e(TAG, "RecheckType: URL" + jsonObjectRequestProfile);
    }

    private void addRecheck(String passBarcode) {
        getBarcode = passBarcode.toString();
        requestQueueAddRecheck = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequestProfile = new JsonObjectRequest(Request.Method.GET, Api.addTestsUsingBarcode + "" + api_key + "/" + user + "/" +
                getBarcode + "/getbarcodedtl", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String finalJson = response.toString();
                JSONObject parentObjectOtp = null;

                Log.e(TAG, "onResponse: RESPONSE" + response);
                try {
                    parentObjectOtp = new JSONObject(finalJson);

                    ALERT = parentObjectOtp.getString("ALERT");
                    BARCODE = parentObjectOtp.getString("BARCODE");
                    BVT_HRS = parentObjectOtp.getString("BVT_HRS");
                    LABCODE = parentObjectOtp.getString("LABCODE");
                    PATIENT = parentObjectOtp.getString("PATIENT");
                    REF_DR = parentObjectOtp.getString("REF_DR");
                    REQUESTED_ADDITIONAL_TESTS = parentObjectOtp.getString("REQUESTED_ADDITIONAL_TESTS");
                    REQUESTED_ON = parentObjectOtp.getString("REQUESTED_ON");
                    RES_ID = parentObjectOtp.getString("RES_ID");
                    SDATE = parentObjectOtp.getString("SDATE");
                    SL_NO = parentObjectOtp.getString("SL_NO");
                    STATUS = parentObjectOtp.getString("STATUS");
                    SU_CODE1 = parentObjectOtp.getString("SU_CODE1");
                    SU_CODE2 = parentObjectOtp.getString("SU_CODE2");
                    TESTS = parentObjectOtp.getString("TESTS");


                    if (STATUS.equalsIgnoreCase(caps_invalidApikey)) {
                        GlobalClass.redirectToLogin(getActivity());
                    } else if (PATIENT.equals("null")) {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(getActivity(), "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        Log.e(TAG, "addRecheck: URL" + jsonObjectRequestProfile);
    }

    private void getTspNumber() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.getData + "" + api_key + "/" + "" + user +
                    "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e(TAG, "onResponse: RESPONSE" + response);
                        String getResponse = response.optString("RESPONSE", "");

                        if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
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
                            if (myPojo.getMASTERS().getBCT_LIST() != null) {
                                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                                    getBtechList.add(myPojo.getMASTERS().getBCT_LIST()[j]);
                                    // Toast.makeText(MainActivity.this, ""+myPojo.getMASTERS().getBCT_LIST().length, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                BCT_LIST bct_list = new BCT_LIST();
                                bct_list.setMOBILE_NUMBER(mobile);
                                bct_list.setNAME(nameofProfile);
                                getBtechList.add(bct_list);

                            }
                            btechSpinner = new ArrayList<>();
                            if (getBtechList.size() != 0) {
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
            GlobalClass.volleyRetryPolicy(jsonObjectRequest2);
            Log.e(TAG, "getTspNumber: URL" + jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchData() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        barProgressDialog = new ProgressDialog(getActivity());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        System.out.println(TAG + Api.SOURCEils + "" + api_key + "/" + "" + user + "/B2BAPP/getclients");
        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.SOURCEils + "" + api_key + "/" + "" + user +
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



   /*     APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.SOURCEils).create(APIInteface.class);
        Call<SourceILSMainModel> responseCall = apiInterface.getClient(api_key, user, "/B2BAPP/getclients");

        Log.e("TAG", "Client URL --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<SourceILSMainModel>() {
            @Override
            public void onResponse(Call<SourceILSMainModel> call, retrofit2.Response<SourceILSMainModel> response) {

                if (response.isSuccessful() && response.body() != null && response != null) {
                    SourceILSMainModel sourceILSMainModel = response.body();
                    if (sourceILSMainModel.getRESPONSE().equalsIgnoreCase("Success")) {
                        GlobalClass.hideProgress(getActivity(),barProgressDialog);
                        Log.e(TAG, "getclient success");
                    }
                } else {
                    Log.e(TAG, "getclient NULL");
                }

            }

            @Override
            public void onFailure(Call<SourceILSMainModel> call, Throwable t) {

                Log.e(TAG, "getclient failure-->" + t.getLocalizedMessage());
                System.out.println("HealthTips OnFailure");
            }
        });*/

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

            samplecollectionponit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogue(obj);
                }
            });

           /* referedbyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinnerDialogRef.showSpinerDialog();
                }
            });*/


//            spinnerDialog = new SpinnerDialog(getActivity(), getLabNmae, "Search SCP", "Close");// With No Animation
//            spinnerDialog = new SpinnerDialog(getActivity(), getLabNmae, "Search SCP", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation


            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// WithAnimation

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
//            TastyToast.makeText(getActivity(), ToastFile.no_data_fnd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
//            TastyToast.makeText(getActivity(), ToastFile.no_data_fnd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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


        title.setText("Search SCP");

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

        if (GlobalClass.setFlagToClose == true) {
            alertDialog.dismiss();
        } else {

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
            ((ManagingTabsActivity) getActivity()).getProfileDetails(getActivity());
        } else {
            isViewShown = false;
        }
    }

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
                if (!GlobalClass.isNetworkAvailable(getActivity())) {
                    TastyToast.makeText(getActivity(), ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {

                    if (mobno_verify == true) {

                        if (btn_snd_otp.getText().equals("Reset")) {

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

                                    btn_snd_otp.setText("Send OTP");
                                    et_otp.setText("");

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
                            callsendOTP();
                        } else if (btn_snd_otp.getText().equals("Send OTP")) {
                            Log.e(TAG, "onClick: " + btn_snd_otp.getText().toString());
                            callsendOTP();
                        }
                    }
                }
                break;

            case R.id.btn_verifyotp:
                if (TextUtils.isEmpty(et_otp.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        TastyToast.makeText(getActivity(), ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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

    private void callsendOTP() {

        if (et_mobno.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (et_mobno.getText().toString().length() < 10) {
            Toast.makeText(getActivity(), "Please enter valid Mobile Number", Toast.LENGTH_SHORT).show();
            lin_otp.setVisibility(View.GONE);
        } else {

            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                GlobalClass.showAlertDialog(getActivity());
            } else {
                if (ControllersGlobalInitialiser.validateMob_controller != null) {
                    ControllersGlobalInitialiser.validateMob_controller = null;
                }
                ControllersGlobalInitialiser.validateMob_controller = new ValidateMob_Controller(Start_New_Woe.this);
                ControllersGlobalInitialiser.validateMob_controller.callvalidatemob(user, et_mobno.getText().toString());
            }

        }
    }

    public void onvalidatemob(ValidateOTPmodel validateOTPmodel, ProgressDialog progressDialog) {

        if (validateOTPmodel.getResponse().equals("OTP Generated Successfully to your registered number")) {
            GlobalClass.hideProgress(getActivity(), progressDialog);


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
            GlobalClass.hideProgress(getActivity(), progressDialog);
        }
    }


    public void onVerifyotp(VerifyotpModel validateOTPmodel) {

        if (validateOTPmodel.getResponse().equals("OTP Validated Successfully")) {
            timerflag = true;
            Toast.makeText(getActivity(),
                    validateOTPmodel.getResponse(),
                    Toast.LENGTH_SHORT).show();

            if (yourCountDownTimer != null) {
                yourCountDownTimer.cancel();
                yourCountDownTimer = null;
            }

            btn_verifyotp.setText("Verified");
            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.green));

            //new requirment
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
                lin_otp.setVisibility(View.GONE);

                btn_snd_otp.setEnabled(true);
                btn_snd_otp.setClickable(true);

                et_mobno.setEnabled(true);
                et_mobno.setClickable(true);

                et_otp.setEnabled(true);
                et_otp.setClickable(true);

            }
        }.start();
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
            dateShow.setText(dd11 + "-" + mm11 + "-" + year);
        }
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

        btn_clear_data.setEnabled(false);
        btn_clear_data.setClickable(false);

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


}