package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.Fragment.Woe_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.WOE_Model_Patient_Details;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Woe_activity extends AppCompatActivity {

    PatientDtailsWoe patientDtailsWoe;
    EditText edtSearch;
    String getDatefromWOE, halfTime, DateToPass;
    static final int DATE_DIALOG_ID = 999;
    //    TextView wind_up, wind_up_multiple;
    TextView woe_cal;
    ProgressDialog barProgressDialog;
    RequestQueue requestQueue, requestQueueWindup;
    Button defaultFragment;
    // TODO: Rename and change types of parameters
    RecyclerView recyclerView;
    private Global globalClass;
    LinearLayoutManager linearLayoutManager;
    //layouts defined in fragment
    LinearLayout layoutCartItems, layoutCartPayments, layoutCartNoItems;
    Fragment fragment_woe_ad_test;
    WOE_Model_Patient_Details woe_model_patient_details;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayList;
    ArrayList<String> getWindupCount;
    Calendar myCalendar;
    LinearLayout app_bar_layout, enter_entered_layout;
    public static RequestQueue PostQueOtp;
    String putDate, getFormatDate, convertedDate;
    SharedPreferences prefs;
    View view_line, view_line1;
    TextView enetered, enter;
    String user, passwrd, access, api_key;
    String blockCharacterSet = "~#^|$%&*!+:`";
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
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private Woe_fragment.OnFragmentInteractionListener mListener;
    private String countData;
    private String outputDateStr;
    private String passToAPI;
    private String DatePassToApi;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_woe_add_test);

        woe_cal = (TextView) findViewById(R.id.woe_cal);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
//        ImageView  add = (ImageView)findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        app_bar_layout = (LinearLayout) findViewById(R.id.enter_entered_layout_consign);


        linearLayoutManager = new LinearLayoutManager(Woe_activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        enetered = (TextView) findViewById(R.id.enetered);
        enter = (TextView) findViewById(R.id.enter);
        view_line = (View) findViewById(R.id.view_line);
        view_line1 = (View) findViewById(R.id.view_line1);

        app_bar_layout.setVisibility(View.GONE);
        enter.setVisibility(View.GONE);
        enetered.setVisibility(View.GONE);
        enter_entered_layout.setVisibility(View.GONE);

        if (globalClass.checkForApi21()) {    Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));}


        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        myCalendar = Calendar.getInstance();

        SimpleDateFormat sdfdatatomap = new SimpleDateFormat("dd-MM-yyyy");
        String get_date_to_mapp = sdfdatatomap.format(new Date());
        halfTime = get_date_to_mapp;
        setTitle("WOE " + get_date_to_mapp);

        ArrayList<String> getSampleTypes = new ArrayList<>();
        ArrayList<String> getbarcode = new ArrayList<>();

        edtSearch.setFilters(new InputFilter[]{filter});
        edtSearch.setFilters(new InputFilter[]{EMOJI_FILTER});

        edtSearch.addTextChangedListener(new TextWatcher() {
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
                String barcode = "";
                String name = "";
                if (patientsArrayList != null) {
                    for (int i = 0; i < patientsArrayList.size(); i++) {

                        final String text = patientsArrayList.get(i).getBarcode().toLowerCase();

                        if (patientsArrayList.get(i).getBarcode() != null || !patientsArrayList.get(i).getBarcode().equals("")) {
                            barcode = patientsArrayList.get(i).getBarcode().toLowerCase();
                        }
                        if (patientsArrayList.get(i).getName() != null || !patientsArrayList.get(i).getName().equals("")) {
                            name = patientsArrayList.get(i).getName().toLowerCase();
                        }

                        if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                (name != null && name.contains(s1))) {
                            String testname = patientsArrayList.get(i).getName();
                            filterPatientsArrayList.add(patientsArrayList.get(i));

                        } else {

                        }
                        patientDtailsWoe = new PatientDtailsWoe(Woe_activity.this, filterPatientsArrayList);
                        recyclerView.setAdapter(patientDtailsWoe);
                    }
                }
                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(halfTime);
            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd");
            convertedDate = sdfdata.format(myDate);
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

            Date date = null;
            try {
                date = inputFormat.parse(convertedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            outputDateStr = outputFormat.format(date);
            setTitle("WOE " + outputDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = timeFormat.format(myDate);

        System.out.println(finalDate);
//        wind_up.setText("Wind up (Pending:"+getWindupCount.size()+")");


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
        woe_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Woe_activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        String formatToset = "yyyy-MM-dd";
        getFormatDate = sdf.format(myCalendar.getTime());

        //change date format from yyyy-MM-dd to dd-MM-yyyy

        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

        Date date = null;
        try {
            date = inputFormat.parse(getFormatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outputDateStr = outputFormat.format(date);
        setTitle("WOE " + outputDateStr);
        woe_cal.setText(putDate);
        fetchPatientDetails();
    }

    @Override
    public void onStart() {
        fetchPatientDetails();
        super.onStart();
    }

    private void fetchPatientDetails() {
        DateToPass = getTitle().toString().substring(4, getTitle().toString().length() - 0);
        woe_cal.setText(DateToPass);
        fetchWoeListDoneByTSP();
    }

    private void fetchWoeListDoneByTSP() {

        barProgressDialog = new ProgressDialog(Woe_activity.this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd
        Date date = null;
        try {
            date = inputFormat.parse(DateToPass);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passToAPI = outputFormat.format(date);
        requestQueue = GlobalClass.setVolleyReq(Woe_activity.this);
        com.example.e5322.thyrosoft.Controller.Log.e(TAG,Api.Cloud_base + "getresults/" + api_key + "/WORK_ORDERS/" + "" + user + "/" + passToAPI + "/key/value");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + "getresults/" + api_key + "/WORK_ORDERS/" + "" + user + "/" + passToAPI + "/key/value", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                com.example.e5322.thyrosoft.Controller.Log.e(TAG, "onResponse: " + response);
                Gson gson = new Gson();
                woe_model_patient_details = new WOE_Model_Patient_Details();
                woe_model_patient_details = gson.fromJson(response.toString(), WOE_Model_Patient_Details.class);
                patientsArrayList = new ArrayList<>();
                getWindupCount = new ArrayList<>();

                SharedPreferences preferences = getSharedPreferences("saveWOEinDraft", MODE_PRIVATE);
                String json2 = preferences.getString("DraftWOE", null);
                if (json2 != null) {
                    Gson gson1 = new Gson();
                    String json = preferences.getString("DraftWOE", null);
                    if (json2 != null) {
                        MyPojoWoe obj = gson1.fromJson(json, MyPojoWoe.class);
                    }
                }
                //set Adpter
                if (woe_model_patient_details.getPatients() != null) {
                    for (int i = 0; i < woe_model_patient_details.getPatients().size(); i++) {
                        patientsArrayList.add(woe_model_patient_details.getPatients().get(i));
                        for (int j = 0; j < patientsArrayList.size(); j++) {
                            getWindupCount.add(String.valueOf(patientsArrayList.get(j).getConfirm_status().equals("NO")));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    patientDtailsWoe = new PatientDtailsWoe(Woe_activity.this, patientsArrayList);
                    recyclerView.setAdapter(patientDtailsWoe);
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }

                    ArrayList<String> getNoStatus = new ArrayList<>();
                    for (int i = 0; i < patientsArrayList.size(); i++) {
                        if (patientsArrayList.get(i).getConfirm_status().equals("NO")) {
                            getNoStatus.add(patientsArrayList.get(i).getName());
                        }

                        int getCount = getNoStatus.size();

                        countData = String.valueOf(getCount);
                        GlobalClass.windupCountDataToShow = countData;
                    }
                    if (GlobalClass.windupCountDataToShow != null) {

                    } else {

                    }

                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
//                    Toast.makeText(Woe_activity.this, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                }

                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
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
        requestQueue.add(jsonObjectRequestPop);
        Log.e(TAG, "fetchWoeListDoneByTSP: URL" + jsonObjectRequestPop);
    }


}
