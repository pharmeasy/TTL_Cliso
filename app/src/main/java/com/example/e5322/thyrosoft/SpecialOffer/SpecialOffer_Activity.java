package com.example.e5322.thyrosoft.SpecialOffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.CustomListAdapter;
import com.example.e5322.thyrosoft.Adapter.TestListAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetClientDetail_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ValidateMob_Controller;
import com.example.e5322.thyrosoft.Controller.VerifyotpController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.B2B_MASTERSMainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.Product_Rate_MasterModel;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.OTPrequest;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.SourceILSModel.REF_DR;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

/**
 * Created by Kalpesh Borane
 */

public class SpecialOffer_Activity extends AppCompatActivity implements View.OnClickListener {
    ConnectionDetector cd;
    public static String OTPAPPID = "9";
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
    Activity activity;
    public RadioButton rd_dps, rd_home;
    public Spinner spin_test, spinyr, timehr, timesecond, timeampm;
    public TextView dateShow, txt_availcnt, tv_timer, txt_ctime, txt_sctdefault;
    public Calendar myCalendar;
    public String putDate, getFormatDate, user, api_key, referenceBy = null;
    public ImageView male, male_red, female, female_red, uncheck_sct, check_sct;
    public ImageView ref_check, uncheck_ref;
    public SharedPreferences prefs;
    public ArrayList<REF_DR> getReferenceName1;
    public ArrayList<String> getReferenceNmae;
    public REF_DR[] ref_drs;
    public int flagtoAdjustClisk = 0;
    public String saveGenderId;
    public int agesinteger;
    Date dCompare;
    boolean genderId = false;
    boolean sctflag = false;
    int mYear, mMonth, mDay;
    String my_var, checktime = "Default";
    String strdate;
    String strtime;
    EditText patientAddress, pincode_edt;
    ArrayList<BaseModel.Barcodes> barcodesList = new ArrayList<>();
    String woereferedby, tspaddress, pincode;
    Spinner btechname;
    SharedPreferences preferences;
    String myFormat = "dd-MM-yyyy"; //In which you need put here
    SimpleDateFormat currsdf = new SimpleDateFormat("HH:mm");
    SimpleDateFormat twelvehoursdf = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
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
    private long minDate;
    private LinearLayout lin_otp, refby_linear, ref_check_linear, lin_changesct, time_layout;
    private Button btn_sendotp, btn_verifyotp, btn_next;
    private EditText et_mobno, et_otp, et_name, et_age;
    private String sctHr, sctMin, sctSEc, getFinalDate;
    private ArrayList<String> btechSpinner;
    private String str_dps = "DPS", str_home;
    private ProgressDialog progressDialog;
    private ArrayList<String> Selcted_Outlab_Test = new ArrayList<>();
    private boolean timerflag = false;
    private String TAG = getClass().getSimpleName();
    private ArrayList<BaseModel> testRateMasterModels = new ArrayList<BaseModel>();
    private ArrayList<B2B_MASTERSMainModel> b2bmasterarraylist;
    private TestListAdapter testListAdapter;
    private ProgressDialog barProgressDialog;
    private AutoCompleteTextView referedby;
    private String selecttest_name, btechIDToPass;
    private MyPojo myPojo;
    private String referredID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_spoffer);
        activity = SpecialOffer_Activity.this;
        cd = new ConnectionDetector(activity);
        initView();


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        String showDate = sdf.format(today);
        dateShow.setText(showDate);


        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        minDate = myCalendar.getTime().getTime();


        strdate = showDate;
        if (checktime.equalsIgnoreCase("Default")) {
            String showtime = currsdf.format(minDate);
            strtime = twelvehoursdf.format(minDate);
            txt_ctime.setText(strdate + " " + showtime);
        } else {
            strtime = currsdf.format(minDate);
            txt_ctime.setText(strdate + " " + strtime);
        }

        Log.e(TAG, "MIN DATE --->" + strdate);

        listner();
        getAllproduct();
        setSpinner();
        setTimeDate();
        textwatcher();
        agetextwatcher();
        Disablefields();

        referedby.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String setText = s.toString();
                if (setText != null || setText != "") {
                    ref_check_linear.setVisibility(View.GONE);
                }
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
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8") || enteredString.startsWith("9")) {
                    TastyToast.makeText(SpecialOffer_Activity.this, "Enter correct Ref By", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        referedby.setText(enteredString.substring(1));
                    } else {
                        referedby.setText("");
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
        });


    }

    private void setCountDownTimer() {

        new CountDownTimer(60000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {

                if (timerflag == false) {
                    tv_timer.setVisibility(View.VISIBLE);
                } else {
                    tv_timer.setVisibility(View.GONE);
                }

                long time = millisUntilFinished / 1000;
                tv_timer.setText("Please wait 00:" + numberFormat.format(time));
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                btn_sendotp.setText("Resend OTP");

                et_mobno.setEnabled(true);
                et_mobno.setClickable(true);

                et_otp.setEnabled(true);
                et_otp.setClickable(true);

            }
        }.start();
    }

    private void agetextwatcher() {

        et_age.addTextChangedListener(new TextWatcher() {
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
                    Toast.makeText(SpecialOffer_Activity.this,
                            ToastFile.crt_age,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        et_age.setText(enteredString.substring(1));
                    } else {
                        et_age.setText("");
                    }
                }

                if (et_age.getText().toString().equals("")) {

                } else {
                    if (agesinteger < 12) {
                        ArrayAdapter PatientsagespinnerAdapter = ArrayAdapter.createFromResource(SpecialOffer_Activity.this, R.array.Patientsagespinner,
                                R.layout.name_age_spinner);
                        PatientsagespinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(PatientsagespinnerAdapter);
                    }
                    if (agesinteger > 12) {
                        ArrayAdapter Patientsagespinner = ArrayAdapter.createFromResource(SpecialOffer_Activity.this, R.array.Patientspinyrday,
                                R.layout.name_age_spinner);
                        Patientsagespinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(Patientsagespinner);
                    }
                    if (agesinteger > 29) {
                        ArrayAdapter Patientsagesyr = ArrayAdapter.createFromResource(SpecialOffer_Activity.this, R.array.Patientspinyr,
                                R.layout.name_age_spinner);
                        Patientsagesyr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinyr.setAdapter(Patientsagesyr);
                    }
                }
            }

        });

    }

    private void setTimeDate() {

        List<String> hourSin = Arrays.asList("HR", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        List<String> minuteSpin = Arrays.asList("MIN", "00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");

        List<String> ampmSpine = Arrays.asList("AM/PM", "AM", "PM");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                SpecialOffer_Activity.this, R.layout.name_age_spinner, hourSin);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timehr.setAdapter(adapter2);
        timehr.setSelection(0);

        ArrayAdapter<String> timesecondspinner = new ArrayAdapter<String>(
                SpecialOffer_Activity.this, R.layout.name_age_spinner, minuteSpin);
        timesecondspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesecond.setAdapter(timesecondspinner);
        timesecond.setSelection(0);

        ArrayAdapter<String> timesecondspinnerdata = new ArrayAdapter<String>(
                SpecialOffer_Activity.this, R.layout.name_age_spinner, ampmSpine);
        timesecondspinnerdata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeampm.setAdapter(timesecondspinnerdata);
    }


    private void getAllproduct() {
        final ProgressDialog barProgressDialog = GlobalClass.ShowprogressDialog(SpecialOffer_Activity.this);

        RequestQueue requestQueuepoptestILS = GlobalClass.setVolleyReq(this);
        Log.e(TAG, "Product URL --->" + Api.getAllTests + api_key + "/ALL/getproductsOffer");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getproductsOffer", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: RESPONSE" + response);
               /* if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }*/

                GlobalClass.hideProgress(SpecialOffer_Activity.this, barProgressDialog);

                String getResponse = response.optString("RESPONSE", "");
                if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(SpecialOffer_Activity.this);
                } else {
                    Gson gson = new Gson();
                    MainModel mainModel;
                    mainModel = gson.fromJson(response.toString(), MainModel.class);

                    b2bmasterarraylist = new ArrayList<>();
                    b2bmasterarraylist.add(mainModel.B2B_MASTERS);
                    ArrayList<Product_Rate_MasterModel> finalproductlist = new ArrayList<Product_Rate_MasterModel>();

                    String testtype = "";
                    if (mainModel.B2B_MASTERS != null) {
                        if (!b2bmasterarraylist.get(0).getTESTS().isEmpty()) {
                            for (int i = 0; i < b2bmasterarraylist.size(); i++) {

                                Product_Rate_MasterModel product_rate_masterModel = new Product_Rate_MasterModel();
                                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getTESTS());

                                for (int j = 0; j < product_rate_masterModel.getTestRateMasterModels().size(); j++) {
                                    testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(j));
                                    Selcted_Outlab_Test.add(product_rate_masterModel.getTestRateMasterModels().get(j).getRate().getB2c());
                                }




                           /* product_rate_masterModel.setTestType(Constants.PRODUCT_POP);
                            product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPOP());
                            for (int j = 0; j < product_rate_masterModel.getTestRateMasterModels().size(); j++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(j));
                            }
                            product_rate_masterModel = new Product_Rate_MasterModel();
                            product_rate_masterModel.setTestType(Constants.PRODUCT_PROFILE);
                            product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getPROFILE());

                            for (int k = 0; k < product_rate_masterModel.getTestRateMasterModels().size(); k++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(k));
                            }
                            product_rate_masterModel = new Product_Rate_MasterModel();
                            product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
                            product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylist.get(i).getTESTS());
                            for (int l = 0; l < product_rate_masterModel.getTestRateMasterModels().size(); l++) {
                                testRateMasterModels.add(product_rate_masterModel.getTestRateMasterModels().get(l));
                            }*/

                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(SpecialOffer_Activity.this);
                            // Set the Alert Dialog Message
                            builder.setMessage("Special offer tests is not mapped to this login");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }

                        testListAdapter = new TestListAdapter(SpecialOffer_Activity.this,
                                R.layout.list_ite, R.id.title, testRateMasterModels);
                        spin_test.setAdapter(testListAdapter);
                        barcodesList.clear();

                        spin_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selecttest_name = parent.getItemAtPosition(position).toString();
                                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                barcodesList.addAll(Arrays.asList(testRateMasterModels.get(position).getBarcodes()));
                                if (testRateMasterModels.get(position).getBillrate() != null)
                                    txt_availcnt.setText(testRateMasterModels.get(position).getBillrate());
                                Log.e(TAG, "onItemSelected barcodesList size : " + barcodesList.size());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }

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
        requestQueuepoptestILS.add(jsonObjectRequestPop);
        Log.e(TAG, "onCreate: URL" + jsonObjectRequestPop);
    }


    private void setSpinner() {
        ArrayList<String> patientsagespinner = new ArrayList<>();
        patientsagespinner.add("Years");
        patientsagespinner.add("Months");
        patientsagespinner.add("Days");


        ArrayAdapter<String> adap = new ArrayAdapter<String>(SpecialOffer_Activity.this, R.layout.name_age_spinner, patientsagespinner);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinyr.setAdapter(adap);
        spinyr.setSelection(0);

    }

    private void listner() {
        rd_dps.setOnClickListener(this);
        rd_home.setOnClickListener(this);

        dateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SpecialOffer_Activity.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btn_next.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);

        uncheck_sct.setOnClickListener(this);
        check_sct.setOnClickListener(this);

        btn_sendotp.setOnClickListener(this);
        btn_verifyotp.setOnClickListener(this);

        ref_check_linear.setOnClickListener(this);
    }

    private void initView() {
        /**Initilize Sharedpref*/
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        api_key = prefs.getString("API_KEY", null);

        preferences = getSharedPreferences("profile", 0);
        tspaddress = preferences.getString("address", "");
        pincode = preferences.getString("pincode", "");


        TextView txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Special offers");
        txt_name.setAllCaps(true);
        txt_name.setTextColor(getResources().getColor(R.color.maroon));
        txt_name.setTextSize(20f);


        rd_dps = (RadioButton) findViewById(R.id.rd_dps);
        rd_home = (RadioButton) findViewById(R.id.rd_home);
        dateShow = findViewById(R.id.date);

        ///  btechname = (Spinner) findViewById(R.id.btech_spinner);

        /**Button*/
        btn_next = findViewById(R.id.next_btn_patient);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);


        /**ImageView*/
        uncheck_ref = findViewById(R.id.uncheck_ref);
        ref_check = findViewById(R.id.ref_check);


        /** Autocomplete */
        referedby = findViewById(R.id.referedby);

        /**Textview*/
        txt_availcnt = findViewById(R.id.txt_availcnt);
        tv_timer = findViewById(R.id.tv_timer);
        txt_ctime = findViewById(R.id.txt_ctime);
        txt_sctdefault = findViewById(R.id.txt_sctdefault);

        /** Edittext findViewById*/
        et_mobno = findViewById(R.id.et_mobno);
        et_otp = findViewById(R.id.et_otp);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);

        //   patientAddress = findViewById(R.id.patientAddress);
        //  pincode_edt = findViewById(R.id.pincode_edt);

        et_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        et_name.setFilters(new InputFilter[]{EMOJI_FILTER});

        int maxLength = 40;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        et_name.setFilters(FilterArray);

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(SpecialOffer_Activity.this, ToastFile.crt_name, Toast.LENGTH_SHORT).show();

                    if (enteredString.length() > 0) {
                        et_name.setText(enteredString.substring(1));
                    } else {
                        et_name.setText("");
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


 /*       patientAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(SpecialOffer_Activity.this, ToastFile.crt_name, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


/*
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
                    TastyToast.makeText(SpecialOffer_Activity.this, ToastFile.crt_pincode, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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
*/


        /**Initilize Button */
        btn_sendotp = findViewById(R.id.btn_sendotp);
        btn_verifyotp = findViewById(R.id.btn_verifyotp);


        /** Linear Layout*/
        lin_otp = findViewById(R.id.lin_otp);
        refby_linear = findViewById(R.id.refby_linear);
        ref_check_linear = findViewById(R.id.ref_check_linear);
        lin_changesct = findViewById(R.id.lin_changesct);
        time_layout = findViewById(R.id.time_layout);


        spin_test = findViewById(R.id.spin_ts_name);
        spinyr = findViewById(R.id.spinyr);
        timehr = findViewById(R.id.timehr);
        timesecond = findViewById(R.id.timesecond);
        timeampm = findViewById(R.id.timeampm);


        male = (ImageView) findViewById(R.id.male);
        male_red = (ImageView) findViewById(R.id.male_red);
        female = (ImageView) findViewById(R.id.female);
        female_red = (ImageView) findViewById(R.id.female_red);

        uncheck_sct = (ImageView) findViewById(R.id.uncheck_sct);
        check_sct = (ImageView) findViewById(R.id.check_sct);

        //Call getClient Doctor Name API
        if (!GlobalClass.isNetworkAvailable(SpecialOffer_Activity.this)) {
            TastyToast.makeText(SpecialOffer_Activity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        } else {
            if (ControllersGlobalInitialiser.getClientDetail_controller != null) {
                ControllersGlobalInitialiser.getClientDetail_controller = null;
            }

            ControllersGlobalInitialiser.getClientDetail_controller = new GetClientDetail_Controller(SpecialOffer_Activity.this);
            ControllersGlobalInitialiser.getClientDetail_controller.CallgetClient(user, api_key);

        }


        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(SpecialOffer_Activity.this);
        Gson gsonbtech = new Gson();
        myPojo = new MyPojo();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);

        getBtechList = new ArrayList<>();
        btechSpinner = new ArrayList<>();

        if (myPojo != null && myPojo.getMASTERS() != null) {
            if (myPojo.getMASTERS().getBCT_LIST() != null) {
                for (int j = 0; j < myPojo.getMASTERS().getBCT_LIST().length; j++) {
                    getBtechList.add(myPojo.getMASTERS().getBCT_LIST()[j]);
                    btechIDToPass = myPojo.getMASTERS().getBCT_LIST()[j].getNED_NUMBER();
                }
            } else {
                TastyToast.makeText(SpecialOffer_Activity.this, "Please register NED", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        } else {
            TastyToast.makeText(SpecialOffer_Activity.this, "Please register NED", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }

       /* btechSpinner.add("Select Btech name");
        for (int i = 0; i < getBtechList.size(); i++) {

            btechSpinner.add(getBtechList.get(i).getNAME());
            if (btechSpinner.size() != 0) {
                btechname.setAdapter(new ArrayAdapter<String>(SpecialOffer_Activity.this, R.layout.spinnerproperty, btechSpinner));
            }
        }*/

    }


    private void textwatcher() {
        et_mobno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GlobalClass.addTexwatcher(SpecialOffer_Activity.this, s, et_mobno);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!GlobalClass.isNetworkAvailable(SpecialOffer_Activity.this)) {
                    TastyToast.makeText(SpecialOffer_Activity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }

            }
        });

    }

    private void generateToken() {
        PackageInfo pInfo = null;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        int versionCode = pInfo.versionCode;
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.THYROCARE).create(PostAPIInteface.class);
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
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getRespId().equalsIgnoreCase(Constants.RES0000)) {
                        if (!GlobalClass.isNull(response.body().getToken())) {
                            Log.e(TAG, "TOKEN--->" + response.body().getToken());
                            callsendOTP(response.body().getToken(), response.body().getRequestId());
                        }
                    } else {
                        Toast.makeText(activity, response.body().getResponse(), Toast.LENGTH_SHORT).show();
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

    private void updateLabel() {

        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());
        dateShow.setText(putDate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rd_dps:
                //str_dps = rd_dps.getText().toString();
                break;

            case R.id.rd_home:
                str_dps = rd_home.getText().toString();
                break;

            case R.id.btn_sendotp:
                if (!GlobalClass.isNetworkAvailable(SpecialOffer_Activity.this)) {
                    TastyToast.makeText(SpecialOffer_Activity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    if (btn_sendotp.getText().equals("Reset")) {
                        Log.e(TAG, "onClick: " + btn_sendotp.getText().toString());
                        et_mobno.setEnabled(true);
                        et_mobno.setClickable(true);
                        btn_sendotp.setText("Send OTP");

                        btn_sendotp.setClickable(true);
                        btn_sendotp.setEnabled(true);

                        et_otp.setText("");

                        btn_verifyotp.setVisibility(View.GONE);
                        lin_otp.setVisibility(View.GONE);

                    } else if (btn_sendotp.getText().equals("Resend OTP")) {
                        Log.e(TAG, "onClick: " + btn_sendotp.getText().toString());
                        if (cd.isConnectingToInternet()) {
                            generateToken();
                        } else {
                            Global.showCustomToast(activity, ToastFile.intConnection);
                        }
                    } else if (btn_sendotp.getText().equals("Send OTP")) {
                        Log.e(TAG, "onClick: " + btn_sendotp.getText().toString());
                        if (cd.isConnectingToInternet()) {
                            generateToken();
                        } else {
                            Global.showCustomToast(activity, ToastFile.intConnection);
                        }

                    }
                }


                break;

            case R.id.btn_verifyotp:
                if (GlobalClass.isNull(et_otp.getText().toString())) {
                    Toast.makeText(SpecialOffer_Activity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (!GlobalClass.isNetworkAvailable(SpecialOffer_Activity.this)) {
                        TastyToast.makeText(SpecialOffer_Activity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        if (ControllersGlobalInitialiser.verifyotpController != null) {
                            ControllersGlobalInitialiser.verifyotpController = null;
                        }
                        ControllersGlobalInitialiser.verifyotpController = new VerifyotpController(SpecialOffer_Activity.this);
                        ControllersGlobalInitialiser.verifyotpController.verifyotp(user, et_mobno.getText().toString(), et_otp.getText().toString());
                    }
                }
                break;

            case R.id.uncheck_sct:
                checktime = "Custom";
                if (sctflag == false) {
                    sctflag = true;
                    check_sct.setVisibility(View.VISIBLE);
                    uncheck_sct.setVisibility(View.GONE);
                    txt_ctime.setVisibility(View.GONE);
                    txt_sctdefault.setVisibility(View.GONE);
                    time_layout.setVisibility(View.VISIBLE);

                } else {
                    sctflag = false;
                    check_sct.setVisibility(View.GONE);
                    uncheck_sct.setVisibility(View.VISIBLE);
                    txt_ctime.setVisibility(View.VISIBLE);
                    txt_sctdefault.setVisibility(View.VISIBLE);
                    time_layout.setVisibility(View.GONE);
                }

                break;


            case R.id.check_sct:
                checktime = "Default";
                if (sctflag == false) {
                    sctflag = true;
                    check_sct.setVisibility(View.GONE);
                    uncheck_sct.setVisibility(View.VISIBLE);
                    txt_ctime.setVisibility(View.VISIBLE);
                    txt_sctdefault.setVisibility(View.VISIBLE);
                    time_layout.setVisibility(View.GONE);

                } else {
                    sctflag = false;
                    check_sct.setVisibility(View.VISIBLE);
                    uncheck_sct.setVisibility(View.GONE);
                    txt_ctime.setVisibility(View.GONE);
                    txt_sctdefault.setVisibility(View.GONE);
                    time_layout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.male:

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
                break;

            case R.id.female:

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

                break;


            case R.id.back:
                finish();
                break;

            case R.id.home:
                startActivity(new Intent(SpecialOffer_Activity.this, ManagingTabsActivity.class));
                break;

            case R.id.ref_check_linear:

                if (flagtoAdjustClisk == 0) {
                    flagtoAdjustClisk = 1;
                    referenceBy = "SELF";
                    referedby.setText("");
                    refby_linear.setVisibility(View.GONE);
                    ref_check.setVisibility(View.VISIBLE);
                    uncheck_ref.setVisibility(View.GONE);
                    ref_check_linear.setVisibility(View.VISIBLE);

                } else if (flagtoAdjustClisk == 1) {
                    flagtoAdjustClisk = 0;
                    referenceBy = null;
                    referedby.setText("");
                    refby_linear.setVisibility(View.VISIBLE);
                    ref_check.setVisibility(View.GONE);
                    uncheck_ref.setVisibility(View.VISIBLE);
                    ref_check_linear.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.next_btn_patient:
                if (!GlobalClass.isNetworkAvailable(SpecialOffer_Activity.this)) {
                    GlobalClass.showAlertDialog(SpecialOffer_Activity.this);
                } else {
                    SendWoeData();
                }

                break;

            case R.id.date:
                showDatepicker();
                break;

        }
    }

    private void callsendOTP(String token, String requestId) {

        if (et_mobno.getText().toString().equals("")) {
            Toast.makeText(SpecialOffer_Activity.this, "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (et_mobno.getText().toString().length() < 10) {
            Toast.makeText(SpecialOffer_Activity.this, "Please enter valid Mobile Number", Toast.LENGTH_SHORT).show();
            lin_otp.setVisibility(View.GONE);
        } else {

            if (!GlobalClass.isNetworkAvailable(SpecialOffer_Activity.this)) {
                GlobalClass.showAlertDialog(SpecialOffer_Activity.this);
            } else {

                if (ControllersGlobalInitialiser.validateMob_controller != null) {
                    ControllersGlobalInitialiser.validateMob_controller = null;
                }

                ControllersGlobalInitialiser.validateMob_controller = new ValidateMob_Controller(SpecialOffer_Activity.this);
                ControllersGlobalInitialiser.validateMob_controller.callvalidatemob(user, et_mobno.getText().toString(), token);
            }


//            JSONObject jsonObject = null;
//            try {
//                GenerateOTPRequestModel requestModel = new GenerateOTPRequestModel();
//                requestModel.setApi_key(Constants.GENRATE_OTP_API_KEY);
//                requestModel.setMobile(et_mobno.getText().toString());
//                requestModel.setType("WOEROUTINE");
//                requestModel.setAccessToken(token);
//                requestModel.setReqId(requestId);
//
//                String json = new Gson().toJson(requestModel);
//                jsonObject = new JSONObject(json);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            if (cd.isConnectingToInternet()) {
//                try {
//                    if (ControllersGlobalInitialiser.getOTPController != null) {
//                        ControllersGlobalInitialiser.getOTPController = null;
//                    }
//                    ControllersGlobalInitialiser.getOTPController = new GetOTPController(activity, SpecialOffer_Activity.this);
//                    ControllersGlobalInitialiser.getOTPController.GetOTP(jsonObject);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Global.showCustomToast(activity, ToastFile.intConnection);
//            }
        }
    }

    private void SendWoeData() {

        Date getCurrentDateandTime = new Date();

        String sctSEc = timeampm.getSelectedItem().toString();
        System.out.println("Spinner value -->" + sctSEc);

        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();
        String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
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


        if (spin_test.getSelectedItem().toString().equals("Select Test")) {
            Toast.makeText(SpecialOffer_Activity.this, "Please Select Test", Toast.LENGTH_SHORT).show();
        } else if (GlobalClass.isNull(et_name.getText().toString())) {
            Toast.makeText(SpecialOffer_Activity.this, ToastFile.crt_name_woe, Toast.LENGTH_SHORT).show();
        } else if (GlobalClass.isNull(et_age.getText().toString())) {
            Toast.makeText(SpecialOffer_Activity.this, ToastFile.ent_age, Toast.LENGTH_SHORT).show();
        } else if (saveGenderId == null || saveGenderId == "") {
            Toast.makeText(SpecialOffer_Activity.this, ToastFile.ent_gender, Toast.LENGTH_SHORT).show();
        } else if (checktime.equals("Custom")) {
            if (timehr.getSelectedItem().toString().equals("HR")) {
                Toast.makeText(SpecialOffer_Activity.this, ToastFile.slt_hr, Toast.LENGTH_SHORT).show();
            } else if (timesecond.getSelectedItem().toString().equals("MIN")) {
                Toast.makeText(SpecialOffer_Activity.this, ToastFile.slt_min, Toast.LENGTH_SHORT).show();
            } else if (sctSEc.equals("AM/PM")) {
                Toast.makeText(SpecialOffer_Activity.this, ToastFile.slt_ampm, Toast.LENGTH_SHORT).show();
            } else if (dCompare.after(getCurrentDateandTime)) {
                Toast.makeText(SpecialOffer_Activity.this, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
            } else if (GlobalClass.isNull(referedby.getText().toString()) && referenceBy == null) {
                Toast.makeText(SpecialOffer_Activity.this, "Please select Refer By", Toast.LENGTH_SHORT).show();
            } else {
                sendIntent();
            }
        } else if (GlobalClass.isNull(referedby.getText().toString()) && referenceBy == null) {
            Toast.makeText(SpecialOffer_Activity.this, "Please select Refer By", Toast.LENGTH_SHORT).show();
        } else {
            sendIntent();
        }

    }

    private void sendIntent() {
        woereferedby = referedby.getText().toString();

        if (woereferedby.equals("") || woereferedby.equals(null)) {
            if (referenceBy == null) {
                Toast.makeText(SpecialOffer_Activity.this, "Please select Ref by", Toast.LENGTH_SHORT).show();
            } else {
                if (referenceBy.equalsIgnoreCase("SELF")) {
                    referenceBy = "SELF";
                    referredID = "";
                    woereferedby = referenceBy;
                } else {
                    referenceBy = referedby.getText().toString();
                }
            }

        } else {
            referenceBy = woereferedby;
            referredID = "";
        }

        sctHr = timehr.getSelectedItem().toString();
        sctMin = timesecond.getSelectedItem().toString();
        sctSEc = timeampm.getSelectedItem().toString();

        final String getFinalAge = et_age.getText().toString();
        final String getFinalTime = sctHr + ":" + sctMin + " " + sctSEc;
        final String getFinalDate = dateShow.getText().toString();

        Log.e(TAG, "Default time --->" + txt_ctime.getText().toString());
        Log.e(TAG, "Custom time --->" + getFinalTime);

        SharedPreferences.Editor saveDetails = getSharedPreferences("savePatientDetails", 0).edit();
        saveDetails.putString("name", et_name.getText().toString());
        saveDetails.putString("age", getFinalAge);
        saveDetails.putString("gender", saveGenderId);


        if (checktime.equals("Default")) {
            saveDetails.putString("sct", strtime);
            saveDetails.putString("date", strdate);
        } else {
            saveDetails.putString("sct", getFinalTime);
            saveDetails.putString("date", getFinalDate);
        }


        saveDetails.putString("ageType", spinyr.getSelectedItem().toString());
        saveDetails.putString("labname", "");
        saveDetails.putString("labAddress", "");
        saveDetails.putString("pincode", pincode);
        saveDetails.putString("patientAddress", tspaddress);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("labIDaddress", "");
        saveDetails.putString("btechIDToPass", btechIDToPass);
        saveDetails.putString("refBy", referenceBy);
        saveDetails.putString("refId", referredID);
        saveDetails.putString("SR_NO", "1");
        saveDetails.putString("woetype", str_dps);
        saveDetails.putString("btechNameToPass", "");
        saveDetails.putString("btechNameToPass", "");
        saveDetails.putString("getcampIDtoPass", "");
        saveDetails.putString("kycinfo", "");
        saveDetails.putString("WOEbrand", "");
        saveDetails.putString("SR_NO", "");
        saveDetails.putString("pincode", "");
        saveDetails.commit();

        int sum = 0;
        ArrayList<String> getTestNameLits = new ArrayList<>();
        for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
            sum = sum + Integer.parseInt(Selcted_Outlab_Test.get(i));
        }
        startActivity(new Intent(SpecialOffer_Activity.this, OfferScan_Activity.class).putParcelableArrayListExtra("barcodelist", barcodesList).putExtra("ts_name", selecttest_name).putExtra("payment", sum)
                .putExtra("type", str_dps).putExtra("contactno", et_mobno.getText().toString()));
    }

    private void showDatepicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(SpecialOffer_Activity.this, R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        /*datePickerDialog.getDatePicker().setMinDate(minDate);*/
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public void onclientData(SourceILSMainModel sourceILSMainModel) {
        callAdapter(sourceILSMainModel);
    }

    private void callAdapter(SourceILSMainModel sourceILSMainModel) {

        getReferenceNmae = new ArrayList<>();
        getReferenceName1 = new ArrayList<>();

        if (sourceILSMainModel.getMASTERS().getREF_DR() != null && sourceILSMainModel.getMASTERS().getREF_DR().length != 0) {
            for (int j = 0; j < sourceILSMainModel.getMASTERS().getREF_DR().length; j++) {
                getReferenceNmae.add(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName());
                getReferenceName1.add(sourceILSMainModel.getMASTERS().getREF_DR()[j]);
                ref_drs = sourceILSMainModel.getMASTERS().getREF_DR();
                if (referedby.getText().toString().equals(sourceILSMainModel.getMASTERS().getREF_DR()[j].getName())) {
                    referredID = sourceILSMainModel.getMASTERS().getREF_DR()[j].getId();
                }
            }
        } else {
            ref_check_linear.setVisibility(View.VISIBLE);
            refby_linear.setVisibility(View.GONE);
        }

        final CustomListAdapter adapter = new CustomListAdapter(SpecialOffer_Activity.this, R.layout.autocompleteitem, getReferenceName1, getReferenceNmae);
        referedby.setThreshold(1);
        referedby.setAdapter(adapter);
        referedby.setTextColor(Color.BLACK);

        referedby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                my_var = adapter.getItem(position).toString();
            }
        });


    }


    public void onvalidatemob(ValidateOTPmodel validateOTPmodel, ProgressDialog progressDialog) {

        if (validateOTPmodel.getResponseId().equals(Constants.RES0001)) {
            GlobalClass.hideProgress(SpecialOffer_Activity.this, progressDialog);


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
            GlobalClass.hideProgress(SpecialOffer_Activity.this, progressDialog);
        }
    }

    private void Disablefields() {
        et_name.setEnabled(false);
        et_name.setClickable(false);

        et_age.setEnabled(false);
        et_age.setClickable(false);

        referedby.setEnabled(false);
        referedby.setClickable(false);

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
/*
        pincode_edt.setEnabled(false);
        pincode_edt.setClickable(false);*/

      /*  btechname.setEnabled(false);
        btechname.setEnabled(false);*/

        timehr.setEnabled(false);
        timehr.setClickable(false);

        timesecond.setEnabled(false);
        timesecond.setClickable(false);

        timeampm.setEnabled(false);
        timeampm.setClickable(false);

        ref_check.setEnabled(false);
        ref_check.setClickable(false);

        ref_check_linear.setEnabled(false);
        ref_check_linear.setClickable(false);

        btn_next.setClickable(false);
        btn_next.setEnabled(false);

        uncheck_sct.setEnabled(false);
        uncheck_sct.setClickable(false);

        check_sct.setEnabled(false);
        check_sct.setClickable(false);
    }


    private void Enablefields() {

        uncheck_sct.setEnabled(true);
        uncheck_sct.setClickable(true);

        check_sct.setEnabled(true);
        check_sct.setClickable(true);

        et_name.setEnabled(true);
        et_name.setClickable(true);

        et_age.setEnabled(true);
        et_age.setClickable(true);

        referedby.setEnabled(true);
        referedby.setClickable(true);

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

        btn_next.setClickable(true);
        btn_next.setEnabled(true);

        // patientAddress.setEnabled(true);
        // patientAddress.setClickable(true);

       /* pincode_edt.setEnabled(true);
        pincode_edt.setClickable(true);
*/
    /*    btechname.setEnabled(true);
        btechname.setEnabled(true);
*/

    }

    public void onVerifyotp(VerifyotpModel validateOTPmodel) {

        if (validateOTPmodel.getResponseId().equals("RES0001")) {
            timerflag = true;
            Toast.makeText(SpecialOffer_Activity.this,
                    validateOTPmodel.getResponse(),
                    Toast.LENGTH_SHORT).show();

            btn_verifyotp.setText("Verified");
            btn_verifyotp.setBackgroundColor(getResources().getColor(R.color.green));

            btn_sendotp.setText("Reset");

            et_mobno.setEnabled(false);
            et_mobno.setClickable(false);

            et_otp.setEnabled(false);
            et_otp.setClickable(false);

            btn_sendotp.setClickable(true);
            btn_sendotp.setEnabled(true);

            btn_verifyotp.setClickable(false);
            btn_verifyotp.setEnabled(false);

            tv_timer.setVisibility(View.GONE);

            Enablefields();
        } else {
            et_otp.setText("");
        }
    }
}
