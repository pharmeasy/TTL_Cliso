package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.FetchwoeListDoneByTSP_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Woe_activity extends AppCompatActivity {

    PatientDtailsWoe patientDtailsWoe;
    EditText edtSearch;
    String halfTime, DateToPass;
    TextView woe_cal;
    RequestQueue requestQueue;
    // TODO: Rename and change types of parameters
    RecyclerView recyclerView;
    private Global globalClass;
    LinearLayoutManager linearLayoutManager;
    WOE_Model_Patient_Details woe_model_patient_details;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayList;
    ArrayList<String> getWindupCount;
    Calendar myCalendar;
    LinearLayout app_bar_layout, enter_entered_layout;
    public static RequestQueue PostQueOtp;
    String putDate, getFormatDate, convertedDate;
    SharedPreferences prefs;
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

    private String countData;
    private String outputDateStr;
    private String passToAPI;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    Activity mActivity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_woe_add_test);
        mActivity = Woe_activity.this;

        initViews();
        initListner();

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

        Log.v("TAG", finalDate);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        woe_cal = (TextView) findViewById(R.id.woe_cal);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        app_bar_layout = (LinearLayout) findViewById(R.id.enter_entered_layout_consign);
        linearLayoutManager = new LinearLayoutManager(Woe_activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        enetered = (TextView) findViewById(R.id.enetered);
        enter = (TextView) findViewById(R.id.enter);


        app_bar_layout.setVisibility(View.GONE);
        enter.setVisibility(View.GONE);
        enetered.setVisibility(View.GONE);
        enter_entered_layout.setVisibility(View.GONE);


        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }
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




    }

    private void initListner() {
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
                if (GlobalClass.CheckArrayList(patientsArrayList)) {
                    for (int i = 0; i < patientsArrayList.size(); i++) {

                        final String text = patientsArrayList.get(i).getBarcode().toLowerCase();

                        if (!GlobalClass.isNull(patientsArrayList.get(i).getBarcode())) {
                            barcode = patientsArrayList.get(i).getBarcode().toLowerCase();
                        }
                        if (!GlobalClass.isNull(patientsArrayList.get(i).getName())) {
                            name = patientsArrayList.get(i).getName().toLowerCase();
                        }

                        if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                (name != null && name.contains(s1))) {
                            String testname = patientsArrayList.get(i).getName();
                            filterPatientsArrayList.add(patientsArrayList.get(i));

                        }
                        patientDtailsWoe = new PatientDtailsWoe(Woe_activity.this, filterPatientsArrayList);
                        recyclerView.setAdapter(patientDtailsWoe);
                    }
                }

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        String formatToset = "yyyy-MM-dd";
        getFormatDate = sdf.format(myCalendar.getTime());


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

        GlobalClass.SetText(woe_cal, putDate);
        fetchPatientDetails();
    }

    @Override
    public void onStart() {
        fetchPatientDetails();
        super.onStart();
    }

    private void fetchPatientDetails() {
        DateToPass = getTitle().toString().substring(4, getTitle().toString().length() - 0);
        GlobalClass.SetText(woe_cal, DateToPass);
        fetchWoeListDoneByTSP();
    }

    private void fetchWoeListDoneByTSP() {

        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd
        Date date = null;
        try {
            date = inputFormat.parse(DateToPass);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passToAPI = outputFormat.format(date);
        requestQueue = Volley.newRequestQueue(Woe_activity.this);

        String strurl = Api.WORKoRDEReNTRYfIRSTpAGE + "" + api_key + "/WORK_ORDERS/" + "" + user + "/" + passToAPI + "/key/value";
        com.example.e5322.thyrosoft.Controller.Log.e(TAG, strurl);

        try {
            if (ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller != null) {
                ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller = null;
            }
            ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller = new FetchwoeListDoneByTSP_Controller(mActivity, Woe_activity.this);
            ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller.woelistdone_controller(requestQueue,strurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getwoedonelistResponse(JSONObject response) {

        com.example.e5322.thyrosoft.Controller.Log.e(TAG, "onResponse: " + response);
        Gson gson = new Gson();
        woe_model_patient_details = new WOE_Model_Patient_Details();
        woe_model_patient_details = gson.fromJson(response.toString(), WOE_Model_Patient_Details.class);
        patientsArrayList = new ArrayList<>();
        getWindupCount = new ArrayList<>();

//        SharedPreferences preferences = getSharedPreferences("saveWOEinDraft", MODE_PRIVATE);
//        String json2 = preferences.getString("DraftWOE", "");
//
//        if (!GlobalClass.isNull(json2)) {
//            Gson gson1 = new Gson();
//            String json = preferences.getString("DraftWOE", "");
//            if (!GlobalClass.isNull(json)) {
//                MyPojoWoe obj = gson1.fromJson(json, MyPojoWoe.class);
//            }
//        }

        if (woe_model_patient_details != null && GlobalClass.CheckArrayList(woe_model_patient_details.getPatients())) {
            for (int i = 0; i < woe_model_patient_details.getPatients().size(); i++) {
                patientsArrayList.add(woe_model_patient_details.getPatients().get(i));

                if (GlobalClass.CheckArrayList(patientsArrayList)) {
                    for (int j = 0; j < patientsArrayList.size(); j++) {
                        getWindupCount.add(String.valueOf(patientsArrayList.get(j).getConfirm_status().equals("NO")));
                    }
                }

            }
            recyclerView.setVisibility(View.VISIBLE);
            patientDtailsWoe = new PatientDtailsWoe(Woe_activity.this, patientsArrayList);
            recyclerView.setAdapter(patientDtailsWoe);

            ArrayList<String> getNoStatus = new ArrayList<>();
            if (GlobalClass.CheckArrayList(patientsArrayList)) {
                for (int i = 0; i < patientsArrayList.size(); i++) {
                    if (!GlobalClass.isNull(patientsArrayList.get(i).getConfirm_status())&&
                            patientsArrayList.get(i).getConfirm_status().equalsIgnoreCase("NO")) {
                        getNoStatus.add(patientsArrayList.get(i).getName());
                    }

                    int getCount = getNoStatus.size();

                    countData = String.valueOf(getCount);
                    GlobalClass.windupCountDataToShow = countData;
                }
            }


        } else {
            recyclerView.setVisibility(View.INVISIBLE);

        }
    }
}
