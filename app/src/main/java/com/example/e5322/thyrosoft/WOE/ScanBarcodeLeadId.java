package com.example.e5322.thyrosoft.WOE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Adapter.LeadIdAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class ScanBarcodeLeadId extends AppCompatActivity implements RecyclerInterface {
    TextView ordersct, date, show_selected_tests_data, title;
    Calendar myCalendar;
    long minDate;
    private String putDate;
    private String getFormatDate;
    Spinner timehr, timesecond, timeampm;
    RecyclerView recycler_barcode_leads;
    String typeName, leadAddress, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE, leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, brandtype, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData, fromcome;
    Leads.LeadData[] nameList;
    List<Leads.LeadData> list;
    LinearLayoutManager linearLayoutManager;
    public int year;
    public int month;
    public int day;
    Button next;
    public IntentIntegrator scanIntegrator;
    LeadIdAdapter leadIdAdapter;
    LeadOrderIdMainModel leadOrderIdMainModel;
    ScannedBarcodeDetails scannedBarcodeDetails;
    ImageView back, home;
    private String pass_full_date;
    private String formFullTime;
    private String convertDate;
    private Date dateTocompare, getCurrentDate;
    boolean flagintent = false;
    ArrayList<ScannedBarcodeDetails> FinalBarcodeDetailsList;

    final DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode_lead_id);

        initViews();

        initListner();

    }

    private void initListner() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(ScanBarcodeLeadId.this);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScanBarcodeLeadId.this, R.style.DialogTheme, datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getDateTopAss = date.getText().toString();
                String getHour = timehr.getSelectedItem().toString();
                String getSecond = timesecond.getSelectedItem().toString();
                String getampm = timeampm.getSelectedItem().toString();
                formFullTime = getHour + ":" + getSecond + " " + getampm;

                getCurrentDate = new Date();
                SimpleDateFormat sdfDtae = new SimpleDateFormat("dd-MM-yyyyhh:mm a");
                String getFullDtae = sdfDtae.format(getCurrentDate);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyhh:mm a");
                try {
                    dateTocompare = sdf.parse(getDateTopAss + formFullTime);

                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }

                SimpleDateFormat sdfs = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat outsdf22 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateTopass = sdfs.parse(getDateTopAss);
                    convertDate = outsdf22.format(dateTopass);
                    pass_full_date = convertDate + " " + formFullTime;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {

                    try {
                        for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                            if (GlobalClass.isNull(FinalBarcodeDetailsList.get(i).getBarcode())) {
                                GlobalClass.showTastyToast(ScanBarcodeLeadId.this, ToastFile.pls_scn_br + FinalBarcodeDetailsList.get(i).getSpecimen_type(), 2);
                                flagintent = false;
                                break;
                            } else if (dateTocompare.after(getCurrentDate)) {
                                GlobalClass.showTastyToast(ScanBarcodeLeadId.this, ToastFile.sct_grt_than_crnt_tm, 2);
                            } else {
                                flagintent = true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flagintent) {
                        Intent j = new Intent(ScanBarcodeLeadId.this, Summary_leadId.class);
                        j.putExtra("LeadID", leadLEAD_ID);
                        j.putExtra("brandtype", brandtype);
                        j.putExtra("MyClass", leadOrderIdMainModel);
                        j.putExtra("fromcome", fromcome);
                        j.putParcelableArrayListExtra("leadArraylist", FinalBarcodeDetailsList);
                        j.putExtra("Date", convertDate);
                        j.putExtra("Time", formFullTime);
                        j.putExtra("DateTime", pass_full_date);
                        startActivity(j);
                        finish();
                        flagintent = false;
                    }

                }

            }

        });
    }

    private void initViews() {
        ordersct = (TextView) findViewById(R.id.ordersct);
        date = (TextView) findViewById(R.id.date);
        title = (TextView) findViewById(R.id.title);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        next = (Button) findViewById(R.id.next);


        timehr = (Spinner) findViewById(R.id.timehr);
        timesecond = (Spinner) findViewById(R.id.timesecond);
        timeampm = (Spinner) findViewById(R.id.timeampm);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        recycler_barcode_leads = (RecyclerView) findViewById(R.id.recycler_barcode_leads);


        linearLayoutManager = new LinearLayoutManager(ScanBarcodeLeadId.this);
        recycler_barcode_leads.setLayoutManager(linearLayoutManager);

        ArrayAdapter timehrspinner = ArrayAdapter.createFromResource(this, R.array.hours, R.layout.name_age_spinner);
        timehrspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //vadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        timehr.setAdapter(timehrspinner);
        timehr.setSelection(7);

        ArrayAdapter timesecondspinner = ArrayAdapter.createFromResource(this, R.array.minutes, R.layout.name_age_spinner);
        timesecondspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //vadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        timesecond.setAdapter(timesecondspinner);

        ArrayAdapter timeampmspinner = ArrayAdapter.createFromResource(this, R.array.timeformat, R.layout.name_age_spinner);
        timeampmspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //vadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        timeampm.setAdapter(timeampmspinner);

        SharedPreferences prefe = getSharedPreferences("getBrandTypeandName", MODE_PRIVATE);

        typeName = prefe.getString("typeName", null);

        GlobalClass.SetText(title, "Scan Barcode(Lead)");

        GlobalClass.isAutoTimeSelected(ScanBarcodeLeadId.this);

        fromcome = getIntent().getExtras().getString("fromcome", "");
        leadOrderIdMainModel = (LeadOrderIdMainModel) getIntent().getParcelableExtra("MyClass");

        SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);

        if (!GlobalClass.isNull(fromcome) && fromcome.equalsIgnoreCase("woepage")) {
            brandtype = sharedPreferences.getString(" brandtype", "");
            leadAddress = sharedPreferences.getString("ADDRESS", "");
            leadAGE = sharedPreferences.getString("AGE", "");
            leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", "");
            leadBCT = sharedPreferences.getString("BCT", "");
            leadEDTA = sharedPreferences.getString("EDTA", "");
            leadEMAIL = sharedPreferences.getString("EMAIL", "");
            leadERROR = sharedPreferences.getString("ERROR", "");
            leadFLUORIDE = sharedPreferences.getString("FLUORIDE", "");
            leadGENDER = sharedPreferences.getString("GENDER", "");
            leadHEPARIN = sharedPreferences.getString("HEPARIN", "");

            leadLAB_ID = sharedPreferences.getString("LAB_ID", "");
            leadLAB_NAME = sharedPreferences.getString("LAB_NAME", "");
            leadLEAD_ID = sharedPreferences.getString("LEAD_ID", "");
            leadMOBILE = sharedPreferences.getString("MOBILE", "");
            leadNAME = sharedPreferences.getString("NAME", "");
            leadORDER_NO = sharedPreferences.getString("ORDER_NO", "");
            leadPACKAGE = sharedPreferences.getString("PACKAGE", "");
            leadPINCODE = sharedPreferences.getString("PINCODE", "");
            leadPRODUCT = sharedPreferences.getString("PRODUCT", "");
            leadRATE = sharedPreferences.getString("RATE", "");

            leadREF_BY = sharedPreferences.getString("REF_BY", "");
            leadRESPONSE = sharedPreferences.getString("RESPONSE", "");
            leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", "");
            leadSCT = sharedPreferences.getString("SCT", "");
            leadSERUM = sharedPreferences.getString("SERUM", "");
            leadTESTS = sharedPreferences.getString("TESTS", "");
            leadTYPE = sharedPreferences.getString("TYPE", "");
            leadURINE = sharedPreferences.getString("URINE", "");
            leadWATER = sharedPreferences.getString("WATER", "");
            leadleadData = sharedPreferences.getString("leadData", "");

            try {
                if (sharedPreferences != null) {
                    GlobalClass.SetText(ordersct, "SCT :" + leadSCT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!GlobalClass.isNull(fromcome) && fromcome.equalsIgnoreCase("adapter")) {
            brandtype = getIntent().getStringExtra("brandtype");
            leadSAMPLE_TYPE = getIntent().getStringExtra("SAMPLE_TYPE");
            leadLEAD_ID = getIntent().getStringExtra("LeadID");
            leadTESTS = getIntent().getStringExtra("TESTS");
            leadSCT = getIntent().getStringExtra("SCT");
            leadleadData = sharedPreferences.getString("leadData", "");

            GlobalClass.SetText(ordersct, "SCT :" + leadSCT);
        } else {
            brandtype = getIntent().getStringExtra("brandtype");
            leadSAMPLE_TYPE = getIntent().getStringExtra("SAMPLE_TYPE");
            leadLEAD_ID = getIntent().getStringExtra("LeadID");
            leadTESTS = getIntent().getStringExtra("TESTS");
            leadSCT = getIntent().getStringExtra("SCT");
            leadleadData = sharedPreferences.getString("leadData", "");
            GlobalClass.SetText(ordersct, "SCT :" + leadSCT);
        }

        //TODO This is LeadData Array
        Gson gson = new Gson();
        nameList = gson.fromJson(leadleadData, Leads.LeadData[].class);
        list = Arrays.asList(nameList);
        leadSAMPLE_TYPE = "";


        leadTESTS = "";

        if (GlobalClass.CheckArrayList(list)) {
            for (int i = 0; i < list.size(); i++) {
                leadTESTS += list.get(i).getTest() + ",";
            }
        }


        try {
            if (!GlobalClass.isNull(leadTESTS) && leadTESTS.endsWith(",")) {
                leadTESTS = leadTESTS.substring(0, leadTESTS.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlobalClass.SetText(show_selected_tests_data, leadTESTS);

        if (!GlobalClass.isNull(fromcome) && fromcome.equalsIgnoreCase("woepage") || fromcome.equalsIgnoreCase("adapter")) {
            FinalBarcodeDetailsList = new ArrayList<>();
            if (GlobalClass.CheckArrayList(list)) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTest().contains("PPBS") || list.get(i).getTest().contains("RBS") || list.get(i).getTest().contains("FBS")) {
                        List<String> fluriodetest = Arrays.asList(list.get(i).getTest().split(","));

                        try {
                            if (fluriodetest.contains("PPBS") && fluriodetest.contains("RBS")) {
                                fluriodetest.remove("RBS");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (GlobalClass.CheckArrayList(fluriodetest)) {
                            for (int k = 0; k < fluriodetest.size(); k++) {
                                scannedBarcodeDetails = new ScannedBarcodeDetails();
                                scannedBarcodeDetails.setProducts(fluriodetest.get(k));
                                scannedBarcodeDetails.setSpecimen_type(list.get(i).getSample_type()[0].getOutlab_sampletype().trim());
                                FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                            }
                        }

                    } else {
                        scannedBarcodeDetails = new ScannedBarcodeDetails();
                        scannedBarcodeDetails.setProducts(list.get(i).getTest());
                        scannedBarcodeDetails.setSpecimen_type(list.get(i).getSample_type()[0].getOutlab_sampletype().trim());
                        FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                    }
                }
            }
        } else {
            FinalBarcodeDetailsList = getIntent().getParcelableArrayListExtra("FinalBarcodeDetailsList");
        }

        leadIdAdapter = new LeadIdAdapter(ScanBarcodeLeadId.this, FinalBarcodeDetailsList, ScanBarcodeLeadId.this);
        recycler_barcode_leads.setAdapter(leadIdAdapter);
        leadIdAdapter.notifyDataSetChanged();

        if (leadIdAdapter != null) {
            leadIdAdapter.setOnItemClickListener(new LeadIdAdapter.OnItemClickListener() {
                @Override
                public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {

                    if (!GlobalClass.isNull(GlobalClass.specimenttype)) {
                        GlobalClass.specimenttype = "";
                    }

                    GlobalClass.specimenttype = Specimenttype;
                    scanIntegrator = new IntentIntegrator(activity);
                    scanIntegrator.initiateScan();

                }
            });

        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);
        myCalendar = Calendar.getInstance();

        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        minDate = myCalendar.getTime().getTime();

        myCalendar.setTime(d);
        GlobalClass.SetText(date, showDate);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());
        GlobalClass.SetText(date, putDate);

    }


    @Override
    public void putDataToscan(Activity activity, int position, String specimenttype) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "123: ");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                String getBarcodeDetails = result.getContents();
                passBarcodeData(getBarcodeDetails);
            }
        } else {
            Log.e(TAG, "else: ");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        GlobalClass.isAutoTimeSelected(ScanBarcodeLeadId.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GlobalClass.isAutoTimeSelected(ScanBarcodeLeadId.this);
    }


    private void passBarcodeData(String getBracode) {
        boolean isbacodeduplicate = false;
        Log.e(TAG, "passBarcodeData: get the barcode number " + getBracode);

        if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
            for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                if (!GlobalClass.isNull(FinalBarcodeDetailsList.get(i).getBarcode())) {
                    if (!GlobalClass.isNull(getBracode) && FinalBarcodeDetailsList.get(i).getBarcode().equalsIgnoreCase(getBracode)) {
                        isbacodeduplicate = true;
                    }
                }
            }
        }


        if (isbacodeduplicate) {
            GlobalClass.showTastyToast(ScanBarcodeLeadId.this, ToastFile.duplicate_barcd, 2);
        } else {

            if (GlobalClass.CheckArrayList(FinalBarcodeDetailsList)) {
                for (int i = 0; i < FinalBarcodeDetailsList.size(); i++) {
                    if (!GlobalClass.isNull(FinalBarcodeDetailsList.get(i).getSpecimen_type()) &&
                            !GlobalClass.isNull(GlobalClass.specimenttype) &&
                            FinalBarcodeDetailsList.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                        FinalBarcodeDetailsList.get(i).setBarcode(getBracode);
                        Log.e(TAG, "passBarcodeData: show barcode" + getBracode);
                    }
                }
            }


        }
        recycler_barcode_leads.removeAllViews();
        leadIdAdapter = new LeadIdAdapter(ScanBarcodeLeadId.this, FinalBarcodeDetailsList, this);
        leadIdAdapter.setOnItemClickListener(new LeadIdAdapter.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {

                if (!GlobalClass.isNull(GlobalClass.specimenttype)) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;
                scanIntegrator = new IntentIntegrator(activity);
                scanIntegrator.initiateScan();
            }
        });

        recycler_barcode_leads.setAdapter(leadIdAdapter);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

}
