package com.example.e5322.thyrosoft.WOE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Adapter.LeadIdAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RecyclerInterface;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ScanBarcodeLeadId extends AppCompatActivity implements RecyclerInterface {
    TextView ordersct, date, show_selected_tests_data, title;
    Spinner timehr, timesecond, timeampm;
    RecyclerView recycler_barcode_leads;
    String user, typeName, passwrd, access, api_key, srnostr, sub_source_code_string, getLeadId, leadAddress, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE, leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData, fromcome;
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;

    LinearLayoutManager linearLayoutManager;
    private int year;
    private int month;
    private int day;
    Button next;
    public int position1 = 0;
    List<String> fixedLenghtList;
    ArrayList<String> sample_type_array;
    public String specimenttype1;
    static final int DATE_PICKER_ID = 1111;
    public IntentIntegrator scanIntegrator;
    LeadIdAdapter leadIdAdapter;
    LeadOrderIdMainModel leadOrderIdMainModel;
    RequestQueue requestQueueNoticeBoard;
    ScannedBarcodeDetails scannedBarcodeDetails;

    ImageView back, home;
    private boolean isfor1sttime = false;
    private String pass_full_date;
    private String formFullTime;
    private String convertDate;
    private Date dateTocompare, getCurrentDate;
    Leads[] leads;
    boolean flagintent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode_lead_id);

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

        GlobalClass.finalspecimenttypewiselist = new ArrayList<>();

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
        title.setText("Scan Barcode(Lead)");

        GlobalClass.isAutoTimeSelected(ScanBarcodeLeadId.this);

        fromcome = getIntent().getExtras().getString("fromcome", "");

        SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);

        if (fromcome.equalsIgnoreCase("woepage")) {
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
                ordersct.setText("SCT :" + leadSCT);
                show_selected_tests_data.setText(leadTESTS);
            }
        } else {
            leadSAMPLE_TYPE = getIntent().getStringExtra("SAMPLE_TYPE");
            leadLEAD_ID = getIntent().getStringExtra("LeadID");
            leadTESTS = getIntent().getStringExtra("TESTS");
            leadSCT = getIntent().getStringExtra("SCT");
            ordersct.setText("SCT :" + leadSCT);
            show_selected_tests_data.setText(leadTESTS);
        }


        String[] elements = leadSAMPLE_TYPE.split(",");
        fixedLenghtList = Arrays.asList(elements);

        sample_type_array = new ArrayList<String>(fixedLenghtList);

        leadOrderIdMainModel = (LeadOrderIdMainModel) getIntent().getParcelableExtra("MyClass");

        for (int i = 0; i < sample_type_array.size(); i++) {
            scannedBarcodeDetails = new ScannedBarcodeDetails();
            scannedBarcodeDetails.setSpecimen_type(sample_type_array.get(i));
            GlobalClass.finalspecimenttypewiselist.add(scannedBarcodeDetails);
        }

        if (!isfor1sttime) {
            isfor1sttime = true;

            for (int k = 0; k < GlobalClass.finalspecimenttypewiselist.size(); k++) {

                if (GlobalClass.finalspecimenttypewiselist.get(k).getSpecimen_type().equals("EDTA")) {
                    if (leadOrderIdMainModel.getLeads()[0].getEDTA() != null && !leadOrderIdMainModel.getLeads()[0].getEDTA().isEmpty()) {
                        GlobalClass.finalspecimenttypewiselist.get(k).setProducts(leadOrderIdMainModel.getLeads()[0].getEDTA());
                    }
                } else if (GlobalClass.finalspecimenttypewiselist.get(k).getSpecimen_type().equals("SERUM")) {
                    if (leadOrderIdMainModel.getLeads()[0].getSERUM() != null && !leadOrderIdMainModel.getLeads()[0].getSERUM().isEmpty()) {
                        GlobalClass.finalspecimenttypewiselist.get(k).setProducts(leadOrderIdMainModel.getLeads()[0].getSERUM());
                    }
                } else if (GlobalClass.finalspecimenttypewiselist.get(k).getSpecimen_type().equals("URINE")) {
                    if (leadOrderIdMainModel.getLeads()[0].getURINE() != null && !leadOrderIdMainModel.getLeads()[0].getURINE().isEmpty()) {
                        GlobalClass.finalspecimenttypewiselist.get(k).setProducts(leadOrderIdMainModel.getLeads()[0].getURINE());
                    }
                } else if (GlobalClass.finalspecimenttypewiselist.get(k).getSpecimen_type().equals("HEPARIN")) {
                    if (leadOrderIdMainModel.getLeads()[0].getHEPARIN() != null && !leadOrderIdMainModel.getLeads()[0].getHEPARIN().isEmpty()) {
                        GlobalClass.finalspecimenttypewiselist.get(k).setProducts(leadOrderIdMainModel.getLeads()[0].getHEPARIN());
                    }
                } else if (GlobalClass.finalspecimenttypewiselist.get(k).getSpecimen_type().equals("FLUORIDE")) {
                    if (leadOrderIdMainModel.getLeads()[0].getFLUORIDE() != null && !leadOrderIdMainModel.getLeads()[0].getFLUORIDE().isEmpty()) {
                        GlobalClass.finalspecimenttypewiselist.get(k).setProducts(leadOrderIdMainModel.getLeads()[0].getFLUORIDE());
                    }
                }
            }
        }


        leadIdAdapter = new LeadIdAdapter(ScanBarcodeLeadId.this, GlobalClass.finalspecimenttypewiselist, this);
        leadIdAdapter.setOnItemClickListener(new LeadIdAdapter.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {

                if (GlobalClass.specimenttype != null) {
                    GlobalClass.specimenttype = "";
                }
                GlobalClass.specimenttype = Specimenttype;

                scanIntegrator = new IntentIntegrator(activity);
                scanIntegrator.initiateScan();
            }
        });

        recycler_barcode_leads.setAdapter(leadIdAdapter);

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
                    Date dateSelected = sdf.parse(getFullDtae);

                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }

                /* else if (dCompare.after(getCurrentDateandTime)) {
                    Toast.makeText(mContext, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                }*/

                SimpleDateFormat sdfs = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat outsdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat outsdf22 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateTopass = sdfs.parse(getDateTopAss);
                    convertDate = outsdf22.format(dateTopass);
                    pass_full_date = convertDate + " " + formFullTime;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (GlobalClass.finalspecimenttypewiselist != null) {
                    for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                        if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() == null || GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().equals("")) {
                            Toast.makeText(ScanBarcodeLeadId.this, ToastFile.pls_scn_br + GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type(), Toast.LENGTH_SHORT).show();
                            flagintent = false;
                            break;
                        } else if (dateTocompare.after(getCurrentDate)) {
                            Toast.makeText(ScanBarcodeLeadId.this, ToastFile.sct_grt_than_crnt_tm, Toast.LENGTH_SHORT).show();
                        } else {
                            flagintent = true;
                        }
                    }

                    if (flagintent) {
                        Intent j = new Intent(ScanBarcodeLeadId.this, Summary_leadId.class);
                        j.putExtra("LeadID", leadLEAD_ID);
                        j.putExtra("MyClass", leadOrderIdMainModel);
                        j.putExtra("fromcome", fromcome);
                        j.putParcelableArrayListExtra("leadArraylist", GlobalClass.finalspecimenttypewiselist);
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


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        String setDateFormat = day + "-" + (month + 1) + "-" + year;
        SimpleDateFormat sdft = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date getDate = sdft.parse(setDateFormat);
            setDateFormat = sdft.format(getDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(setDateFormat);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            String setDateFormat = day + "-" + (month + 1) + "-" + year;
            SimpleDateFormat sdft = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date getDate = sdft.parse(setDateFormat);
                setDateFormat = sdft.format(getDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date.setText(setDateFormat);
            // Show selected date

        }
    };

    @Override
    public void putDataToscan(Activity activity, int position, String specimenttype) {
        /*specimenttype1 = specimenttype;
        position1 = position;
        scanIntegrator.initiateScan();*/
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

        for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
            if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode() != null && !GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().isEmpty()) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getBarcode().equalsIgnoreCase(getBracode)) {
                    isbacodeduplicate = true;
                }
            }
        }

        if (isbacodeduplicate) {
            Toast.makeText(ScanBarcodeLeadId.this, ToastFile.duplicate_barcd, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < GlobalClass.finalspecimenttypewiselist.size(); i++) {
                if (GlobalClass.finalspecimenttypewiselist.get(i).getSpecimen_type().equalsIgnoreCase(GlobalClass.specimenttype)) {
                    GlobalClass.finalspecimenttypewiselist.get(i).setBarcode(getBracode);
                    Log.e(TAG, "passBarcodeData: show barcode" + getBracode);
                }
            }
        }
        recycler_barcode_leads.removeAllViews();
        leadIdAdapter = new LeadIdAdapter(ScanBarcodeLeadId.this, GlobalClass.finalspecimenttypewiselist, this);
        leadIdAdapter.setOnItemClickListener(new LeadIdAdapter.OnItemClickListener() {
            @Override
            public void onScanbarcodeClickListener(String Specimenttype, Activity activity) {

                if (GlobalClass.specimenttype != null) {
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
