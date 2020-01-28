package com.example.e5322.thyrosoft.Cliso_BMC;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Toast;

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
import com.example.e5322.thyrosoft.Adapter.CustomListAdapter;
import com.example.e5322.thyrosoft.Fragment.SampleCollectionAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BRAND_LIST;
import com.example.e5322.thyrosoft.Models.Brand_type;
import com.example.e5322.thyrosoft.Models.CAMP_LIST;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.SourceILSModel.LABS;
import com.example.e5322.thyrosoft.SourceILSModel.REF_DR;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

/**
 * A simple {@link Fragment} subclass.
 */
public class BMC_NEW_WOEFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = BMC_NEW_WOEFragment.class.getSimpleName();
    public static RequestQueue PostQueOtp;
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
    View viewMain;
    Date dCompare;
    AlertDialog alert;
    Context mContext;
    EditText name, age, id_for_woe, barcode_woe;
    REF_DR[] ref_drs;
    ImageView male, female, male_red, female_red;
    ProgressDialog barProgressDialog;
    Button next_btn;
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
    Calendar myCalendar;
    long minDate;
    int mYear, mMonth, mDay;
    Spinner timehr, timesecond, timeampm;
    TextView dateShow;
    TextView leadbarcodename;
    TextView leadidbarcodetest;
    TextView leadbarcoderefdr;
    TextView leadbarcodesct;
    String saveGenderId;
    boolean genderId = false;
    DatabaseHelper myDb;
    LinearLayout namePatients, id_layout, barcode_layout, leadbarcodelayout, AGE_layout, time_layout, lin_changesct;
    Spinner selectTypeSpinner, brand_spinner;
    ArrayList<String> getCampNames;
    EditText vial_number;
    Spinner btechname;
    TextView radio;
    LinearLayout refby_linear, btech_linear_layout, labname_linear, mobile_number_kyc;
    String woereferedby;
    boolean isLoaded = false;
    ArrayList<BRAND_LIST> getBrandName;
    ArrayList<String> spinnerBrandName;
    ArrayList<String> spinnerTypeName;
    ArrayList<String> getTypeListfirst;
    ArrayList<String> getTypeListsecond;
    ArrayList<String> getTypeListthird;
    ArrayList<String> getDatafetch;
    ArrayList<String> getSubSource;
    ImageView add_ref;
    ScrollView scrollView2;
    Button btn_clear_data;
    TextView enetered, enter, txt_ctime, txt_sctdefault;
    Date getCurrentDateandTime;
    ImageView enter_arrow_enter, enter_arrow_entered;
    int flagtoAdjustClisk = 0;
    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    String strdate, strtime;
    SimpleDateFormat timesdf = new SimpleDateFormat("hh:mm a");
    CheckBox chk_changeSCT;
    private ArrayList<String> getLabNmae;
    private ArrayList<String> statusForColor;
    private SharedPreferences prefs;
    private String user;
    private String passwrd;
    private String access;
    private String referenceBy;
    private String api_key;
    private ArrayList<String> btechSpinner;
    private MyPojo myPojo;
    private String referredID;
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
    private String labIDTopass;
    private String getTSP_Address = "";
    private String typename;
    private String nameString;
    private String ageString;
    private String btechnameTopass = "";
    private String btechIDToPass;
    private String getcampIDtoPass;
    private String kycdata;
    private String getTSP_AddressStringTopass;
    private String patientAddressdataToPass = "";
    private boolean flag = true;
    private String brand_string;
    private String type_string;
    private String brandNames;
    private String prof;
    private String mobile;
    private String nameofProfile;
    private SpinnerDialog spinnerDialogRef;
    private String getVial_numbver;
    private String sctHr, sctMin, sctSEc, getFinalTime, getFinalDate;
    private String getAmPm;
    private LABS selectedLABS;
    private String pincode_pass = "";
    private String mParam1, mParam2;
    private boolean isChangeSCTSelected = false;

    public BMC_NEW_WOEFragment() {
        // Required empty public constructor
    }

    public static BMC_NEW_WOEFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        BMC_NEW_WOEFragment fragment = new BMC_NEW_WOEFragment();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_bmc_new_woe, container, false);
        mContext = getContext();
        myDb = new DatabaseHelper(mContext);
        initUI();


        GlobalClass.setflagToRefreshData = false;

        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        getProfileDetails();

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

        strdate = showDate;
        strtime = timesdf.format(minDate);
        txt_ctime.setText(strdate + " " + strtime);

        name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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

        initListeners();

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

        referenceBy = null;
        refby_linear.setVisibility(View.VISIBLE);

        if (GlobalClass.setScp_Constant != null) {
            samplecollectionponit.setText(GlobalClass.setScp_Constant);
        } else {
            samplecollectionponit.setText("");
        }

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        myPojo = new MyPojo();
        String json = appSharedPrefs.getString("saveAlldata", "");
        myPojo = gson.fromJson(json, MyPojo.class);

        if (myPojo != null) {
            getBrandName = new ArrayList<>();
            spinnerBrandName = new ArrayList<String>();
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

//            sub_source_code_spinner.setAdapter(new ArrayAdapter<String>(getContext(),
//                    R.layout.spinner_item,
//                    getSubSource));

            spinnerTypeName = new ArrayList<>();
            getTypeListfirst = new ArrayList<>();
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
                    callWOMasterAPI();
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

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.no_img_spinner, spinnerBrandName);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brand_spinner.setAdapter(adapter2);
            brand_spinner.setSelection(0);
            brand_spinner.setEnabled(false);
            brand_spinner.setClickable(false);
            startDataSetting();
        } else {
            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                Toast.makeText(mContext, ToastFile.intConnection, Toast.LENGTH_SHORT).show();
            } else {
                if (isLoaded == false) {
                    callWOMasterAPI();
                }
            }
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
            if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
            }

            btechSpinner.add("Select Btech name");
            for (int i = 0; i < getBtechList.size(); i++) {

                btechSpinner.add(getBtechList.get(i).getNAME());
                if (btechSpinner != null || btechSpinner.size() != 0) {
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

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, hourSin);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timehr.setAdapter(adapter2);
        timehr.setSelection(0);

        ArrayAdapter<String> timesecondspinner = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, minuteSpin);
        timesecondspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesecond.setAdapter(timesecondspinner);
        timesecond.setSelection(0);

        ArrayAdapter<String> timesecondspinnerdata = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, ampmSpine);
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

        ArrayAdapter<String> adap = new ArrayAdapter<String>(mContext, R.layout.name_age_spinner, patientsagespinner);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinyr.setAdapter(adap);
        spinyr.setSelection(0);

        //vadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        name.setFilters(new InputFilter[]{EMOJI_FILTER});

        int maxLength = 40;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        name.setFilters(FilterArray);

        return viewMain;
    }

    private void initUI() {
        name = (EditText) viewMain.findViewById(R.id.name);
        age = (EditText) viewMain.findViewById(R.id.age);
        id_for_woe = (EditText) viewMain.findViewById(R.id.id_for_woe);
        barcode_woe = (EditText) viewMain.findViewById(R.id.barcode_woe);
        kyc_format = (EditText) viewMain.findViewById(R.id.kyc_format);
        vial_number = (EditText) viewMain.findViewById(R.id.vial_number);
        male = (ImageView) viewMain.findViewById(R.id.male);
        male_red = (ImageView) viewMain.findViewById(R.id.male_red);
        female = (ImageView) viewMain.findViewById(R.id.female);
        female_red = (ImageView) viewMain.findViewById(R.id.female_red);
        next_btn = (Button) viewMain.findViewById(R.id.next_btn_patient);
        spinyr = (Spinner) viewMain.findViewById(R.id.spinyr);
        scrollView2 = (ScrollView) viewMain.findViewById(R.id.scrollView2);
        dateShow = (TextView) viewMain.findViewById(R.id.date);
        leadbarcodename = (TextView) viewMain.findViewById(R.id.leadbarcodename);
        leadidbarcodetest = (TextView) viewMain.findViewById(R.id.leadidbarcodetest);
        leadbarcoderefdr = (TextView) viewMain.findViewById(R.id.leadbarcoderefdr);
        leadbarcodesct = (TextView) viewMain.findViewById(R.id.leadbarcodesct);
        add_ref = (ImageView) viewMain.findViewById(R.id.add_ref);
        timehr = (Spinner) viewMain.findViewById(R.id.timehr);
        timesecond = (Spinner) viewMain.findViewById(R.id.timesecond);
        selectTypeSpinner = (Spinner) viewMain.findViewById(R.id.selectTypeSpinner);
        brand_spinner = (Spinner) viewMain.findViewById(R.id.brand_spinner);
        btechname = (Spinner) viewMain.findViewById(R.id.btech_spinner);
        timeampm = (Spinner) viewMain.findViewById(R.id.timeampm);
        samplecollectionponit = (TextView) viewMain.findViewById(R.id.samplecollectionponit);
        referedbyText = (AutoCompleteTextView) viewMain.findViewById(R.id.referedby);//
        radio = (TextView) viewMain.findViewById(R.id.radio);
        refby_linear = (LinearLayout) viewMain.findViewById(R.id.refby_linear);
        btech_linear_layout = (LinearLayout) viewMain.findViewById(R.id.btech_linear_layout);
        labname_linear = (LinearLayout) viewMain.findViewById(R.id.labname_linear);
        mobile_number_kyc = (LinearLayout) viewMain.findViewById(R.id.mobile_number_kyc);
        enter_ll_unselected = (LinearLayout) viewMain.findViewById(R.id.enter_ll_unselected);
        leadbarcodelayout = (LinearLayout) viewMain.findViewById(R.id.leadbarcodelayout);
        namePatients = (LinearLayout) viewMain.findViewById(R.id.namePatients);
        AGE_layout = (LinearLayout) viewMain.findViewById(R.id.AGE_layout);
        time_layout = (LinearLayout) viewMain.findViewById(R.id.time_layout);
        id_layout = (LinearLayout) viewMain.findViewById(R.id.id_layout);
        barcode_layout = (LinearLayout) viewMain.findViewById(R.id.barcode_layout);
        unchecked_entered_ll = (LinearLayout) viewMain.findViewById(R.id.unchecked_entered_ll);
        enetered = (TextView) viewMain.findViewById(R.id.enetered);
        enter = (TextView) viewMain.findViewById(R.id.enter);
        enter_arrow_enter = (ImageView) viewMain.findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) viewMain.findViewById(R.id.enter_arrow_entered);
        btn_clear_data = (Button) viewMain.findViewById(R.id.btn_clear_data);

        lin_changesct = (LinearLayout) viewMain.findViewById(R.id.lin_changesct);
        txt_ctime = (TextView) viewMain.findViewById(R.id.txt_ctime);
        txt_sctdefault = (TextView) viewMain.findViewById(R.id.txt_sctdefault);
        chk_changeSCT = (CheckBox) viewMain.findViewById(R.id.chk_changeSCT);
    }

    private void initListeners() {
        viewMain.findViewById(R.id.unchecked_entered_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BMC_WOEFragment bmc_woeFragment = new BMC_WOEFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                //transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
                transaction.replace(R.id.frame_layout, bmc_woeFragment).commitAllowingStateLoss();
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
                BMC_NEW_WOEFragment fragment = new BMC_NEW_WOEFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        vial_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(getActivity(), ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        vial_number.setText(enteredString.substring(1));
                    } else {
                        vial_number.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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


        add_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_ref.setVisibility(View.GONE);
            }
        });

        kyc_format.addTextChangedListener(new TextWatcher() {
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

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(getActivity(), ToastFile.crt_name, Toast.LENGTH_SHORT).show();
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
                }
                String enteredString = s.toString();

                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(mContext, ToastFile.crt_age, Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        age.setText(enteredString.substring(1));
                    } else {
                        age.setText("");
                    }
                }
                if (age.getText().toString().equals("")) {

                } else {
                    if (agesinteger < 12) {
                        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.Patientsagespinner, R.layout.name_age_spinner);
                        PatientsagespinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(PatientsagespinnerAdapter);
                    }
                    if (agesinteger > 12) {
                        ArrayAdapter Patientsagespinner = ArrayAdapter.createFromResource(mContext, R.array.Patientspinyrday, R.layout.name_age_spinner);
                        Patientsagespinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(Patientsagespinner);
                    }
                    if (agesinteger > 29) {
                        ArrayAdapter Patientsagesyr = ArrayAdapter.createFromResource(mContext, R.array.Patientspinyr, R.layout.name_age_spinner);
                        Patientsagesyr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(Patientsagesyr);
                    }
                }
            }
        });

        chk_changeSCT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isChangeSCTSelected = isChecked;
                if (isChangeSCTSelected) {
                    txt_sctdefault.setVisibility(View.GONE);
                    txt_ctime.setVisibility(View.GONE);
                    time_layout.setVisibility(View.VISIBLE);
                    timehr.setSelection(0);
                    timesecond.setSelection(0);
                    timeampm.setSelection(0);
                } else {
                    txt_sctdefault.setVisibility(View.VISIBLE);
                    txt_ctime.setVisibility(View.VISIBLE);
                    time_layout.setVisibility(View.GONE);
                    timehr.setSelection(0);
                    timesecond.setSelection(0);
                    timeampm.setSelection(0);
                }
            }
        });
    }

    private void callWOMasterAPI() {
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
            Log.e(TAG, "callWOMasterAPI: ");
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.getData + "" + api_key + "/" + "" + user + "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String getResponse = response.optString("RESPONSE", "");
                        Log.e(TAG, "onResponse: RESPONSE" + response);
                        if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {

                            if (mContext instanceof Activity) {
                                if (!((Activity) mContext).isFinishing())
                                    barProgressDialog.dismiss();
                            }
                            GlobalClass.redirectToLogin(getActivity());
                        } else {
                            Gson gson = new Gson();
                            myPojo = new MyPojo();
                            myPojo = gson.fromJson(response.toString(), MyPojo.class);

                            if (mContext instanceof Activity) {
                                if (!((Activity) mContext).isFinishing())
                                    barProgressDialog.dismiss();
                            }
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
                                GlobalClass.items.add(GlobalClass.putData[i]);
                            }
                            // Spinner adapter

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.no_img_spinner, spinnerBrandName);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            brand_spinner.setAdapter(adapter2);
                            brand_spinner.setSelection(0);
                            brand_spinner.setEnabled(false);
                            brand_spinner.setClickable(false);
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
            Log.e(TAG, "callWOMasterAPI: URL" + jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startDataSetting() {
        brand_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, R.layout.no_img_spinner, getTypeListfirst);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    selectTypeSpinner.setAdapter(adapter2);
                    selectTypeSpinner.setSelection(1);
                    selectTypeSpinner.setEnabled(false);
                    selectTypeSpinner.setClickable(false);

                    selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (selectTypeSpinner.getSelectedItem().equals("DPS")) {
                                id_layout.setVisibility(View.GONE);
                                barcode_layout.setVisibility(View.GONE);
                                leadbarcodelayout.setVisibility(View.GONE);
                                brand_string = brand_spinner.getSelectedItem().toString();
                                type_string = selectTypeSpinner.getSelectedItem().toString();
                                next_btn.setVisibility(View.VISIBLE);
                                btech_linear_layout.setVisibility(View.VISIBLE);
                                vial_number.setVisibility(View.VISIBLE);
                                mobile_number_kyc.setVisibility(View.VISIBLE);
                                labname_linear.setVisibility(View.GONE);

                                referedbyText.setText("");
                                woereferedby = referedbyText.getText().toString();

                                refby_linear.setVisibility(View.VISIBLE);
                                referenceBy = "";


                                namePatients.setVisibility(View.VISIBLE);
                                AGE_layout.setVisibility(View.VISIBLE);
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
                                                    TastyToast.makeText(mContext, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                                                        if (mContext instanceof Activity) {
                                                                            if (!((Activity) mContext).isFinishing())
                                                                                barProgressDialog.dismiss();
                                                                        }
                                                                        kyc_format.setText(checkNumber);
                                                                    } else {
                                                                         /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                                                            barProgressDialog.dismiss();
                                                                        }*/
                                                                        if (mContext instanceof Activity) {
                                                                            if (!((Activity) mContext).isFinishing())
                                                                                barProgressDialog.dismiss();
                                                                        }
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


                                        String getDateToCompare;
                                        if (isChangeSCTSelected) {
                                            getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                                            getFinalDate = dateShow.getText().toString();
                                            getDateToCompare = getFinalDate + " " + getFinalTime;
                                        } else {
                                            getDateToCompare = strdate + " " + strtime;
                                        }

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
                                        patientAddressdataToPass = "";
                                        pincode_pass = "";
//                                        btechnameTopass = btechname.getSelectedItem().toString();
                                        kycdata = kyc_format.getText().toString();
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
                                            Toast.makeText(mContext, ToastFile.vial_no, Toast.LENGTH_SHORT).show();
                                        } else if (nameString.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_name, Toast.LENGTH_SHORT).show();
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
                                        } else if (referenceBy == null || referenceBy.equals("")) {
                                            Toast.makeText(mContext, ToastFile.crt_ref_by, Toast.LENGTH_SHORT).show();
                                        } else if (btechnameTopass.equalsIgnoreCase(ToastFile.slt_btech_name)) {
                                            Toast.makeText(getActivity(), ToastFile.btech_name, Toast.LENGTH_SHORT).show();
                                        } else if (isChangeSCTSelected) {
                                            if (sctHr.equals("HR")) {
                                                Toast.makeText(mContext, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
                                            } else if (sctMin.equals("MIN")) {
                                                Toast.makeText(mContext, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
                                            } else if (sctSEc.equals("AM/PM")) {
                                                Toast.makeText(mContext, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
                                            } else {
                                                storeWOEDetails();
                                            }
                                        } else if (txt_ctime.getText().toString().isEmpty()) {
                                            Toast.makeText(mContext, ToastFile.sct_blank, Toast.LENGTH_SHORT).show();
                                        } else if (dCompare.after(getCurrentDateandTime)) {
                                            Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                                        } else {
                                            storeWOEDetails();
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void storeWOEDetails() {
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
            Toast.makeText(getActivity(), ToastFile.crt_MOB_num, Toast.LENGTH_SHORT).show();
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
            final String getFinalAge = age.getText().toString();
            final String getFinalTime, getFinalDate;
            if (isChangeSCTSelected) {
                sctHr = timehr.getSelectedItem().toString();
                sctMin = timesecond.getSelectedItem().toString();
                sctSEc = timeampm.getSelectedItem().toString();
                getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
                getFinalDate = dateShow.getText().toString();
            } else {
                getFinalTime = strtime;
                getFinalDate = strdate;
            }


            if (myPojo.getMASTERS().getTSP_MASTER() != null) {
                getTSP_Address = myPojo.getMASTERS().getTSP_MASTER().getAddress();
            }

            GlobalClass.setReferenceBy_Name = referenceBy;
            Log.e(TAG, "onClick: lab add and lab id " + getTSP_AddressStringTopass + labIDTopass);
            SharedPreferences.Editor saveDetails = mContext.getSharedPreferences("savePatientDetails", 0).edit();
            saveDetails.putString("name", nameString.trim());
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

            Intent i = new Intent(mContext, BMC_ProductListingActivity.class);
            i.putExtra("name", nameString.trim());
            i.putExtra("age", getFinalAge);
            i.putExtra("gender", saveGenderId);
            i.putExtra("sct", getFinalTime);
            i.putExtra("date", getFinalDate);
            startActivity(i);
        }
    }

    private void getTspNumber() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.getData + "" + api_key + "/" + "" + user + "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
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
                         Toast.makeText(getActivity(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            Log.e(TAG, "getTspNumber: URL" + jsonObjectRequest2);
            GlobalClass.volleyRetryPolicy(jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "on error --->"+e.getLocalizedMessage());
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
        JsonObjectRequest jsonObjectRequestfetchData = new JsonObjectRequest(Request.Method.GET, Api.SOURCEils + "" + api_key + "/" + "" + user + "/B2BAPP/getclients", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                Log.e(TAG, "onResponse: RESPONSE" + response);
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
        Log.e(TAG, "fetchData: URL" + jsonObjectRequestfetchData);
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

    private void callAdapter(final SourceILSMainModel obj) {
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

            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", "Close");// With No Animation
            spinnerDialogRef = new SpinnerDialog(getActivity(), getReferenceNmae, "Search Ref by", R.style.DialogAnimations_SmileWindow, "Close");// WithAnimation

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

        } /*else {
            final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("Register SCP");
            alertDialog.setMessage(ToastFile.scp_not_mapped);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            samplecollectionponit.setEnabled(false);
        }*/

        getReferenceNmae = new ArrayList<>();
        getReferenceName1 = new ArrayList<>();

        if (obj.getMASTERS().getREF_DR().length != 0) {
            for (int j = 0; j < obj.getMASTERS().getREF_DR().length; j++) {
                getReferenceNmae.add(obj.getMASTERS().getREF_DR()[j].getName());
                getReferenceName1.add(obj.getMASTERS().getREF_DR()[j]);
                ref_drs = obj.getMASTERS().getREF_DR();
            }
        } else {
            refby_linear.setVisibility(View.VISIBLE);
        }

        CustomListAdapter adapter = new CustomListAdapter((Context) mContext, R.layout.autocompleteitem, getReferenceName1, getReferenceNmae);
        referedbyText.setThreshold(1);//will start working from first character
        referedbyText.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        referedbyText.setTextColor(Color.BLACK);
    }

    private void showDialogue(final SourceILSMainModel obj) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.custom_alert_scp, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();
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
        for (int i = 0; i < obj.getMASTERS().getLABS().length; i++) {
            labDetailsArrayList.add(obj.getMASTERS().getLABS()[i]);
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

                        }
                        sampleCollectionAdapter.filteredArraylist(filterPatientsArrayList);
                        sampleCollectionAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if (GlobalClass.setFlagToClose == true) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalClass.isAutoTimeSelected(getActivity());
    }

    private void getProfileDetails() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: " + response);
                            SharedPreferences.Editor saveProfileDetails = mContext.getSharedPreferences("profile", 0).edit();
                            saveProfileDetails.putString("prof", response.getString(Constants.tsp_image));
                            saveProfileDetails.putString("ac_code", response.getString(Constants.ac_code));
                            saveProfileDetails.putString("address", response.getString(Constants.address));
                            saveProfileDetails.putString("email", response.getString(Constants.email));
                            saveProfileDetails.putString("mobile", response.getString(Constants.mobile));
                            saveProfileDetails.putString("name", response.getString("name"));
                            saveProfileDetails.putString("pincode", response.getString(Constants.pincode));
                            saveProfileDetails.putString("user_code", response.getString(Constants.user_code));
                            saveProfileDetails.putString("closing_balance", response.getString(Constants.closing_balance));
                            saveProfileDetails.putString("credit_limit", response.getString(Constants.credit_limit));
                            saveProfileDetails.putString("doj", response.getString(Constants.doj));
                            saveProfileDetails.putString("source_code", response.getString(Constants.source_code));
                            saveProfileDetails.putString("tsp_image", response.getString(Constants.tsp_image));
                            saveProfileDetails.putString("address", response.getString(Constants.address));
                            saveProfileDetails.commit();

                            SharedPreferences.Editor saveProfileData = mContext.getSharedPreferences("profilename", 0).edit();
                            saveProfileData.putString("name", response.getString("name"));
                            saveProfileData.putString("usercode", response.getString(Constants.user_code));
                            saveProfileData.putString("mobile", response.getString(Constants.mobile));
                            saveProfileData.putString("image", response.getString(Constants.tsp_image));
                            saveProfileData.putString("email", response.getString(Constants.email));
                            saveProfileData.commit();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "getProfileDetails: url" + jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(150000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
