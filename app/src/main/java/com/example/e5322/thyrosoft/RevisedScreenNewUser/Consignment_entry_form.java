package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Consignment_entry_form extends AppCompatActivity {
    ImageView back, home;
    RadioButton direct, through_tsp;
    TextView title, consign_discription_data, expected_departure_time, expected_arrival_time, dispatch_time;
    Button btn_submit, btn_woe;
    EditText total_consignment_edt_txt, remark_edt_txt, bsv__edt_txt, cpl_edt_txt, source_code_pass, rpl_edt_txt, reenter_edt_txt, enter_edt_txt, consignment_edt_txt, name_edt_txt, bus_number, bus_edt_txt, flight_name;
    String user, passwrd, access, api_key, email_pref, mobile_pref;
    Spinner mode_spinner, filght_spinner, bus_spinner, courier_spinner, transit_time_spinner, consignment_temp_spinner, packaging_dtl_edt_txt;
    private SharedPreferences prefs;
    long minDate;
    ArrayList<String> setSpinnerTRansitTime;
    private int CalendarHour, CalendarMinute;
    TimePickerDialog timepickerdialog;
    int flagtsp = 0;
    Calendar myCalendar;
    ProgressDialog barProgressDialog;
    LinearLayout linear_layout, flight_name_layout, flight_number_layout, bus_spinner_name_layout, bus_name_layout,
            bus_number_layout, expected_depature_time_layout, expected_arrival_time_layout, courier_spinner_name_layout,
            courier_name_layout, dispatch_time_layout, consignment_name_layout, packaging_spinner_layout, description_layout,
            consignmnet_description_edt_layout, expected_spinner_transit_time_layout, temperature_spinner_layout,
            total_sample_layout, cpl_rpl_layout, bsv_barcode_layout, enter_source_code_layout, source_code_to_pass;
    private String mode_value_compare;
    private String getSelectedItem_Pkg;
    private String routing_code;
    private RequestQueue PostQueAirCargo;
    private String Response, message, ResId;
    private String tsp_su_code;
    private String dispatch_time_to_pass, depature_time_to_pass, arrival_time_to_pass;
    private RequestQueue barcodeDetails;
    private ProgressDialog progressDialog;
    private int total_int;
    private int getTotal_count;
    private String SaveTransitTime;
    private RequestQueue requestQueue;
    private String cpl_count, response1, rpl_count, total_count;
    private String TAG=Consignment_entry_form.class.getSimpleName().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_entry_form);

        initUI();

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        title.setText("WELCOME " + user);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);
        minDate = myCalendar.getTime().getTime();

        setSpinnerTRansitTime = new ArrayList<>();
        setSpinnerTRansitTime.add("<6 hr");
        setSpinnerTRansitTime.add("6hr-8hr");
        setSpinnerTRansitTime.add("8hr-12hr");
        setSpinnerTRansitTime.add("12hr-24hr");
        setSpinnerTRansitTime.add("24hr-48hr");


        final ArrayAdapter mode_spinner_values = ArrayAdapter.createFromResource(Consignment_entry_form.this, R.array.mode_spinner_values, R.layout.spinnerproperty);
        mode_spinner.setAdapter(mode_spinner_values);

        ArrayAdapter filght_spinner_values = ArrayAdapter.createFromResource(Consignment_entry_form.this, R.array.filght_spinner_values, R.layout.spinnerproperty);
        filght_spinner.setAdapter(filght_spinner_values);

        ArrayAdapter packaging_dtl_edt_txt_values = ArrayAdapter.createFromResource(Consignment_entry_form.this, R.array.packaging_dtl_edt_txt_values, R.layout.spinnerproperty);
        packaging_dtl_edt_txt.setAdapter(packaging_dtl_edt_txt_values);

        ArrayAdapter bus_spinner_values = ArrayAdapter.createFromResource(Consignment_entry_form.this, R.array.bus_spinner_values, R.layout.spinnerproperty);
        bus_spinner.setAdapter(bus_spinner_values);

//        ArrayAdapter courier_spinner_values = ArrayAdapter.createFromResource(Consignment_entry_form.this, R.array.courier_spinner_values, R.layout.spinnerproperty);
//        courier_spinner.setAdapter(courier_spinner_values);

        ArrayAdapter transit_time_spinner_values = new ArrayAdapter(Consignment_entry_form.this, R.layout.spinnerproperty, setSpinnerTRansitTime);
        transit_time_spinner.setAdapter(transit_time_spinner_values);

        ArrayAdapter consignment_temp_spinner_values = ArrayAdapter.createFromResource(Consignment_entry_form.this, R.array.consignment_temp_spinner_values, R.layout.spinnerproperty);
        consignment_temp_spinner.setAdapter(consignment_temp_spinner_values);


        if (SaveTransitTime != null) {
            for (int i = 0; i < setSpinnerTRansitTime.size(); i++) {
                if (SaveTransitTime.equals(setSpinnerTRansitTime.get(i))) {
                    transit_time_spinner.setSelection(i);
                }
            }
        } else {
            transit_time_spinner.setSelection(0);
        }

        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing_code = "direct";
                source_code_to_pass.setVisibility(View.GONE);
                direct.setText("Direct");
                flagtsp = 1;
                through_tsp.setText("Through another TSP");
            }
        });
        through_tsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing_code = "through_tsp";
                source_code_to_pass.setVisibility(View.VISIBLE);
                direct.setText("Direct to Mumbai");
                through_tsp.setText("Through");
                flagtsp = 2;
                source_code_pass.setText(user);
                remark_edt_txt.setText(user);

            }
        });


        expected_departure_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Consignment_entry_form.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        expected_arrival_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Consignment_entry_form.this, datefor_expected_arrival_time, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        dispatch_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Consignment_entry_form.this, dispatch_time_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        reenter_edt_txt.addTextChangedListener(new TextWatcher() {
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
                String enteredString = s.toString();
                String getBsvBarcode = enter_edt_txt.getText().toString();
                if (getBsvBarcode.length() == enteredString.length()) {
                    if (getBsvBarcode.equals(enteredString)) {

                    } else {
                        reenter_edt_txt.setText("");
                        Toast.makeText(Consignment_entry_form.this, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                    }
                } else if (getBsvBarcode.length() < enteredString.length()) {
                    enter_edt_txt.setText(enteredString.substring(1));
                } else {

                }
            }
        });


        cpl_edt_txt.addTextChangedListener(new TextWatcher() {
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
//                String enteredString = s.toString();
//                String getTotakSmapleConsignment = total_consignment_edt_txt.getText().toString();
//                String rplCount = rpl_edt_txt.getText().toString();
//
//                int totalConsign = Integer.parseInt(getTotakSmapleConsignment);
//                int rplcount = Integer.parseInt(rplCount);

            }
        });

        fetchRplCplCount();


        enter_edt_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                final String search_barcode = enter_edt_txt.getText().toString();
                if (hasFocus) {

                } else if (!search_barcode.equals("")) {
                    barcodeDetails = Volley.newRequestQueue(Consignment_entry_form.this);//2c=/TAM03/TAM03136166236000078/geteditdata
                    progressDialog = new ProgressDialog(Consignment_entry_form.this);
                    progressDialog.setTitle("Kindly wait ...");
                    progressDialog.setMessage(ToastFile.processing_request);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(20);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StringRequest jsonObjectRequestPop = new StringRequest(Request.Method.GET, Api.barcode_Check + search_barcode + "/CheckConsignmentBarcode"
                            , new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("barcode respponse" + response);

                            if (response.equals("\"Valid\"")) {
                                enter_edt_txt.setText(search_barcode);
                                progressDialog.dismiss();
                            } else {
                                enter_edt_txt.setText("");
                                progressDialog.dismiss();
                                Toast.makeText(Consignment_entry_form.this, "" + response, Toast.LENGTH_SHORT).show();
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
                    barcodeDetails.add(jsonObjectRequestPop);
                }
            }
        });


        bsv__edt_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                final String bsv_search_barcode = bsv__edt_txt.getText().toString();
                if (hasFocus) {

                } else if (!bsv_search_barcode.equals("")) {
                    barcodeDetails = Volley.newRequestQueue(Consignment_entry_form.this);//2c=/TAM03/TAM03136166236000078/geteditdata
                    progressDialog = new ProgressDialog(Consignment_entry_form.this);
                    progressDialog.setTitle("Kindly wait ...");
                    progressDialog.setMessage(ToastFile.processing_request);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.setProgress(0);
                    progressDialog.setMax(20);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    StringRequest jsonObjectRequestPop = new StringRequest(Request.Method.GET, Api.barcode_Check + user + "/" + bsv_search_barcode + "/CheckBSVBarcode"
                            , new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("barcode respponse" + response);

                            if (response.equals("\"Valid\"")) {
                                bsv__edt_txt.setText(bsv_search_barcode);
                                progressDialog.dismiss();
                            } else {
                                bsv__edt_txt.setText("");
                                progressDialog.dismiss();
                                Toast.makeText(Consignment_entry_form.this, "" + response, Toast.LENGTH_SHORT).show();
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
                    barcodeDetails.add(jsonObjectRequestPop);
                }
            }
        });

        cpl_edt_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {

                } else {
                    final String cpl_data = cpl_edt_txt.getText().toString();
                    final String rpl_data = rpl_edt_txt.getText().toString();
                    final String total_consignment = total_consignment_edt_txt.getText().toString();

                    if (!total_consignment.equals("")) {

                        int cpl_int = Integer.parseInt(cpl_data);
                        int rpl_int = Integer.parseInt(rpl_data);
                        total_int = Integer.parseInt(total_consignment);
                        getTotal_count = cpl_int + rpl_int;
                    } else {
                        total_consignment_edt_txt.setError("");
                    }


                    if (rpl_data.equals("")) {
                        Toast.makeText(Consignment_entry_form.this, "Please Enter RPL", Toast.LENGTH_SHORT).show();
                    } else if (total_int == getTotal_count) {
                        cpl_edt_txt.setText(cpl_data);
                        rpl_edt_txt.setText(rpl_data);
                    } else {
                        cpl_edt_txt.setError(ToastFile.crt_digit);
                        cpl_edt_txt.setText("");
                        rpl_edt_txt.setText("");
                        rpl_edt_txt.setError(ToastFile.crt_digit);
                    }
                }
            }
        });


        dispatch_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String getTimefromTxt = dispatch_time.getText().toString();
                if (hasFocus) {

                } else {
                    String total_time = getDateToShow + " " + getTimetoPass;
//                            String getDispatchTime = dispatch_time.getText().toString();
                    Date d_convert = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    try {
                        d_convert = sdf.parse(total_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (d_convert.equals(new Date())) {
                        dispatch_time.setText("");
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                    } else if (d_convert.after(new Date())) {
                        dispatch_time.setText("");
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                    } else {
                        dispatch_time.setText(getDateToShow + " " + getTimetoPass);
                    }
                }
            }
        });


        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode_value_compare = mode_spinner.getSelectedItem().toString();
                if (mode_value_compare.equals("Air Cargo")) {

                    flight_name_layout.setVisibility(View.VISIBLE);
                    flight_number_layout.setVisibility(View.VISIBLE);
                    expected_depature_time_layout.setVisibility(View.VISIBLE);
                    expected_arrival_time_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    packaging_spinner_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_layout.setVisibility(View.VISIBLE);
                    enter_source_code_layout.setVisibility(View.VISIBLE);

                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    consign_discription_data.setVisibility(View.GONE);
                } else if (mode_value_compare.equals("Bus")) {

                    bus_spinner_name_layout.setVisibility(View.VISIBLE);
                    bus_number_layout.setVisibility(View.VISIBLE);
                    expected_depature_time_layout.setVisibility(View.VISIBLE);
                    expected_arrival_time_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    packaging_spinner_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_layout.setVisibility(View.VISIBLE);
                    enter_source_code_layout.setVisibility(View.VISIBLE);

                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    consign_discription_data.setVisibility(View.GONE);

                    bus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (bus_spinner.getSelectedItem().equals("OTHERS")) {
                                bus_name_layout.setVisibility(View.VISIBLE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                consign_discription_data.setVisibility(View.GONE);
                            } else {
                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                consign_discription_data.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else if (mode_value_compare.equals("Courier")) {

                    courier_spinner_name_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);

                    packaging_spinner_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_layout.setVisibility(View.VISIBLE);
                    enter_source_code_layout.setVisibility(View.VISIBLE);

                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    consign_discription_data.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);

                    courier_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (courier_spinner.getSelectedItem().equals("OTHERS")) {
                                bus_spinner_name_layout.setVisibility(View.VISIBLE);
                                consign_discription_data.setVisibility(View.VISIBLE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);
                                consign_discription_data.setText("(Enter 11 digit barcode from CPL, if not received please contact your pin officer now.eg:ILTAM030001)");
                            } else {
                                consign_discription_data.setVisibility(View.VISIBLE);
                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                bus_spinner_name_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                consignment_name_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);
                                consign_discription_data.setText("(Enter the barcode of Selectde Courier Which has sent by CPL.If not received please contact your pin officer.)");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else if (mode_value_compare.equals("Hand Delivery")) {

                    courier_name_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    packaging_spinner_layout.setVisibility(View.VISIBLE);
                    description_layout.setVisibility(View.VISIBLE);
                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_layout.setVisibility(View.VISIBLE);
                    enter_source_code_layout.setVisibility(View.VISIBLE);


                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    consign_discription_data.setVisibility(View.GONE);


                    packaging_dtl_edt_txt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getSelectedItem_Pkg = packaging_dtl_edt_txt.getSelectedItem().toString();
                            if (getSelectedItem_Pkg.equals("Thermocol box with cooled gel pack")) {
                                courier_name_layout.setVisibility(View.VISIBLE);
                                dispatch_time_layout.setVisibility(View.VISIBLE);
                                packaging_spinner_layout.setVisibility(View.VISIBLE);
                                description_layout.setVisibility(View.VISIBLE);
                                consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                                expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                                temperature_spinner_layout.setVisibility(View.VISIBLE);
                                total_sample_layout.setVisibility(View.VISIBLE);
                                cpl_rpl_layout.setVisibility(View.VISIBLE);
                                bsv_barcode_layout.setVisibility(View.VISIBLE);
                                enter_source_code_layout.setVisibility(View.VISIBLE);
                                consign_discription_data.setVisibility(View.VISIBLE);
                                consign_discription_data.setText("( Enter the barcode of Selected Courier Which has sent by CPL. if not received please contact your pin officer.)");


                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                consignment_name_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                bus_spinner_name_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);
                            } else if (getSelectedItem_Pkg.equals("Chiller box with cooled gel pack")) {
                                courier_name_layout.setVisibility(View.VISIBLE);
                                dispatch_time_layout.setVisibility(View.VISIBLE);
                                packaging_spinner_layout.setVisibility(View.VISIBLE);
                                description_layout.setVisibility(View.VISIBLE);
                                consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                                expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                                temperature_spinner_layout.setVisibility(View.VISIBLE);
                                total_sample_layout.setVisibility(View.VISIBLE);
                                cpl_rpl_layout.setVisibility(View.VISIBLE);
                                bsv_barcode_layout.setVisibility(View.VISIBLE);
                                enter_source_code_layout.setVisibility(View.VISIBLE);
                                consign_discription_data.setVisibility(View.VISIBLE);
                                consign_discription_data.setText("( Enter the barcode number as seen on Chiller Box. For eg: ICLCHL600880 )");


                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                consignment_name_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                bus_spinner_name_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);
                                //
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else if (mode_value_compare.equals("LME")) {
                    description_layout.setVisibility(View.VISIBLE);
                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_layout.setVisibility(View.VISIBLE);
                    enter_source_code_layout.setVisibility(View.VISIBLE);


                    packaging_spinner_layout.setVisibility(View.GONE);
                    dispatch_time_layout.setVisibility(View.GONE);
                    expected_spinner_transit_time_layout.setVisibility(View.GONE);
                    temperature_spinner_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    consign_discription_data.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Consignment_entry_form.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTransitTime = transit_time_spinner.getSelectedItem().toString();
                mode_value_compare = mode_spinner.getSelectedItem().toString();
                if (mode_value_compare.equals("Air Cargo")) {

                    String getDispatchtime = dispatch_time.getText().toString();
                    String getDeparture_time = expected_departure_time.getText().toString();
                    String getArrival_time = expected_arrival_time.getText().toString();
                    String courier_name = courier_spinner.getSelectedItem().toString();
                    String consignment_number = consignment_edt_txt.getText().toString();
                    String pkg_details = packaging_dtl_edt_txt.getSelectedItem().toString();
                    String expected_transit_time = transit_time_spinner.getSelectedItem().toString();
                    String consignment_barcode = enter_edt_txt.getText().toString();
                    String consignment_reenter_barcode = reenter_edt_txt.getText().toString();
                    String consignment_temp = consignment_temp_spinner.getSelectedItem().toString();
                    String rpl_sample = rpl_edt_txt.getText().toString();
                    String cpl_sample = cpl_edt_txt.getText().toString();
                    String total_consignment_sample = total_consignment_edt_txt.getText().toString();
                    String bsv_edt_txt = bsv__edt_txt.getText().toString();
                    String remark_to_pass = remark_edt_txt.getText().toString();
                    String flight_name_to_pass = filght_spinner.getSelectedItem().toString();
                    String flight_number_to_pass = flight_name.getText().toString();

                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        String getDate = sdf.format(d_pass);
                        compare = sdf.parse(getDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (flagtsp == 0) {
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (flight_number_to_pass.equals("")) {
                        flight_name.setError(ToastFile.flight_num);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.flight_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDeparture_time.equals("")) {
                        expected_departure_time.setError(ToastFile.dept_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.dept_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getArrival_time.equals("")) {
                        expected_arrival_time.setError(ToastFile.arrival_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.arrival_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_number.equals("")) {
                        consignment_edt_txt.setError(ToastFile.consign_num);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remark_to_pass.equals("")) {
                        remark_edt_txt.setError(ToastFile.remark);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.remark, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date myDate = null;
                        Date myDateDeparture_time = null;
                        Date myDateArrival_time = null;
                        try {
                            myDate = dateFormat.parse(getDispatchtime);
                            myDateDeparture_time = dateFormat.parse(getDeparture_time);
                            myDateArrival_time = dateFormat.parse(getArrival_time);
                            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            dispatch_time_to_pass = sdfdata.format(myDate);
                            depature_time_to_pass = sdfdata.format(myDateDeparture_time);
                            arrival_time_to_pass = sdfdata.format(myDateArrival_time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }

                        barProgressDialog = new ProgressDialog(Consignment_entry_form.this);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(Consignment_entry_form.this);
                        JSONObject jsonObjectOtp = new JSONObject();
                        try {
                            jsonObjectOtp.put("API_KEY", api_key);
                            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode);
                            jsonObjectOtp.put("ThroughTSPCode", tsp_su_code);
                            jsonObjectOtp.put("DispatchTime", dispatch_time_to_pass);
                            jsonObjectOtp.put("ConsignmentNo", consignment_number);
                            jsonObjectOtp.put("TransitTime", expected_transit_time);
                            jsonObjectOtp.put("Mode", mode_value_compare);
                            jsonObjectOtp.put("SampleFromCPL", cpl_sample);
                            jsonObjectOtp.put("SampleFromRPL", rpl_sample);
                            jsonObjectOtp.put("PackagingDetails", pkg_details);
                            jsonObjectOtp.put("FlightName", flight_name_to_pass);
                            jsonObjectOtp.put("FlightNo", flight_number_to_pass);
                            jsonObjectOtp.put("DepTime", depature_time_to_pass);
                            jsonObjectOtp.put("ArrTime", arrival_time_to_pass);
                            jsonObjectOtp.put("BSVBarcode", bsv_edt_txt);
                            jsonObjectOtp.put("Remarks", remark_to_pass);
                            jsonObjectOtp.put("TotalSamples", total_consignment_sample);
                            jsonObjectOtp.put("ConsignmentTemperature", consignment_temp);
                            jsonObjectOtp.put("CourierName", courier_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(Consignment_entry_form.this, Consignment_entry_form.class);
                                        startActivity(i);
//                                        finish();
                                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        TastyToast.makeText(Consignment_entry_form.this, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error != null) {
                                } else {
                                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                    Toast.makeText(Consignment_entry_form.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);


                    }


                } else if (mode_value_compare.equals("Bus")) {
                    String getDispatchtime = dispatch_time.getText().toString();
                    String getDeparture_time = expected_departure_time.getText().toString();
                    String getArrival_time = expected_arrival_time.getText().toString();

                    String consignment_number = consignment_edt_txt.getText().toString();
                    String pkg_details = packaging_dtl_edt_txt.getSelectedItem().toString();
                    String expected_transit_time = transit_time_spinner.getSelectedItem().toString();
                    String consignment_barcode = enter_edt_txt.getText().toString();
                    String consignment_reenter_barcode = reenter_edt_txt.getText().toString();
                    String consignment_temp = consignment_temp_spinner.getSelectedItem().toString();
                    String rpl_sample = rpl_edt_txt.getText().toString();
                    String cpl_sample = cpl_edt_txt.getText().toString();
                    String total_consignment_sample = total_consignment_edt_txt.getText().toString();
                    String bsv_edt_txt = bsv__edt_txt.getText().toString();
                    String remark_to_pass = remark_edt_txt.getText().toString();
                    String bus_name = bus_spinner.getSelectedItem().toString();
                    if (bus_name.equals("OTHERS")) {

                        bus_name = "OTHERS-" + bus_edt_txt.getText().toString();
                    } else {
                        bus_name = bus_spinner.getSelectedItem().toString();
                    }
                    String bus_number_to_pass = bus_number.getText().toString();
                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        String getDate = sdf.format(d_pass);
                        compare = sdf.parse(getDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (flagtsp == 0) {
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bus_number_to_pass.equals("")) {
                        bus_number.setError(ToastFile.bus_num);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.bus_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDeparture_time.equals("")) {
                        expected_departure_time.setError(ToastFile.dept_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.dept_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getArrival_time.equals("")) {
                        expected_arrival_time.setError(ToastFile.arrival_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.arrival_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_number.equals("")) {
                        consignment_edt_txt.setError(ToastFile.consign_num);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remark_to_pass.equals("")) {
                        remark_edt_txt.setError(ToastFile.remark);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.remark, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date myDate = null;
                        Date myDateDeparture_time = null;
                        Date myDateArrival_time = null;
                        try {
                            myDate = dateFormat.parse(getDispatchtime);
                            myDateDeparture_time = dateFormat.parse(getDeparture_time);
                            myDateArrival_time = dateFormat.parse(getArrival_time);
                            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            dispatch_time_to_pass = sdfdata.format(myDate);
                            depature_time_to_pass = sdfdata.format(myDateDeparture_time);
                            arrival_time_to_pass = sdfdata.format(myDateArrival_time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }
                        barProgressDialog = new ProgressDialog(Consignment_entry_form.this);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(Consignment_entry_form.this);
                        JSONObject jsonObjectOtp = new JSONObject();
                        try {
                            jsonObjectOtp.put("API_KEY", api_key);
                            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode);
                            jsonObjectOtp.put("ThroughTSPCode", tsp_su_code);
                            jsonObjectOtp.put("DispatchTime", dispatch_time_to_pass);
                            jsonObjectOtp.put("ConsignmentNo", consignment_number);
                            jsonObjectOtp.put("TransitTime", expected_transit_time);
                            jsonObjectOtp.put("Mode", mode_value_compare);
                            jsonObjectOtp.put("SampleFromCPL", cpl_sample);
                            jsonObjectOtp.put("SampleFromRPL", rpl_sample);
                            jsonObjectOtp.put("PackagingDetails", pkg_details);
                            jsonObjectOtp.put("FlightName", bus_name);
                            jsonObjectOtp.put("FlightNo", bus_number_to_pass);
                            jsonObjectOtp.put("DepTime", depature_time_to_pass);
                            jsonObjectOtp.put("ArrTime", arrival_time_to_pass);
                            jsonObjectOtp.put("BSVBarcode", bsv_edt_txt);
                            jsonObjectOtp.put("Remarks", remark_to_pass);
                            jsonObjectOtp.put("TotalSamples", total_consignment_sample);
                            jsonObjectOtp.put("ConsignmentTemperature", consignment_temp);
                            jsonObjectOtp.put("CourierName", bus_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(Consignment_entry_form.this, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        TastyToast.makeText(Consignment_entry_form.this, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error != null) {
                                } else {

                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);

                    }


                } else if (mode_value_compare.equals("Courier")) {
                    String getDispatchtime = dispatch_time.getText().toString();

                    String courier_name = courier_spinner.getSelectedItem().toString();
                    String consignment_number = consignment_edt_txt.getText().toString();
                    String pkg_details = packaging_dtl_edt_txt.getSelectedItem().toString();
                    String expected_transit_time = transit_time_spinner.getSelectedItem().toString();
                    String consignment_barcode = enter_edt_txt.getText().toString();
                    String consignment_reenter_barcode = reenter_edt_txt.getText().toString();
                    String consignment_temp = consignment_temp_spinner.getSelectedItem().toString();
                    String rpl_sample = rpl_edt_txt.getText().toString();
                    String cpl_sample = cpl_edt_txt.getText().toString();
                    String total_consignment_sample = total_consignment_edt_txt.getText().toString();
                    String bsv_edt_txt = bsv__edt_txt.getText().toString();
                    String remark_to_pass = remark_edt_txt.getText().toString();
                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        String getDate = sdf.format(d_pass);
                        compare = sdf.parse(getDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (flagtsp == 0) {
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_number.equals("")) {
                        consignment_edt_txt.setError(ToastFile.consign_num);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remark_to_pass.equals("")) {
                        remark_edt_txt.setError(ToastFile.remark);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.remark, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date myDate = null;
                        try {
                            myDate = dateFormat.parse(getDispatchtime);
                            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            dispatch_time_to_pass = sdfdata.format(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }
                        barProgressDialog = new ProgressDialog(Consignment_entry_form.this);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(Consignment_entry_form.this);
                        JSONObject jsonObjectOtp = new JSONObject();
                        try {
                            jsonObjectOtp.put("API_KEY", api_key);
                            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode);
                            jsonObjectOtp.put("ThroughTSPCode", tsp_su_code);
                            jsonObjectOtp.put("DispatchTime", dispatch_time_to_pass);
                            jsonObjectOtp.put("ConsignmentNo", consignment_number);
                            jsonObjectOtp.put("TransitTime", expected_transit_time);
                            jsonObjectOtp.put("Mode", mode_value_compare);
                            jsonObjectOtp.put("SampleFromCPL", cpl_sample);
                            jsonObjectOtp.put("SampleFromRPL", rpl_sample);
                            jsonObjectOtp.put("PackagingDetails", pkg_details);
                            jsonObjectOtp.put("FlightName", "");
                            jsonObjectOtp.put("FlightNo", "");
                            jsonObjectOtp.put("DepTime", "");
                            jsonObjectOtp.put("ArrTime", "");
                            jsonObjectOtp.put("BSVBarcode", bsv_edt_txt);
                            jsonObjectOtp.put("Remarks", remark_to_pass);
                            jsonObjectOtp.put("TotalSamples", total_consignment_sample);
                            jsonObjectOtp.put("ConsignmentTemperature", consignment_temp);
                            jsonObjectOtp.put("CourierName", courier_name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(Consignment_entry_form.this, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        TastyToast.makeText(Consignment_entry_form.this, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error != null) {
                                } else {

                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);

                    }


                } else if (mode_value_compare.equals("Hand Delivery")) {

                    String getDispatchtime = dispatch_time.getText().toString();


                    String nameof_person = name_edt_txt.getText().toString();
                    String pkg_details = packaging_dtl_edt_txt.getSelectedItem().toString();
                    String expected_transit_time = transit_time_spinner.getSelectedItem().toString();
                    String consignment_barcode = enter_edt_txt.getText().toString();
                    String consignment_reenter_barcode = reenter_edt_txt.getText().toString();
                    String consignment_temp = consignment_temp_spinner.getSelectedItem().toString();
                    String rpl_sample = rpl_edt_txt.getText().toString();
                    String cpl_sample = cpl_edt_txt.getText().toString();
                    String total_consignment_sample = total_consignment_edt_txt.getText().toString();
                    String bsv_edt_txt = bsv__edt_txt.getText().toString();
                    String remark_to_pass = remark_edt_txt.getText().toString();
                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        String getDate = sdf.format(d_pass);
                        compare = sdf.parse(getDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (flagtsp == 0) {
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (nameof_person.equals("")) {
                        name_edt_txt.setError(ToastFile.per_name);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.per_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remark_to_pass.equals("")) {
                        remark_edt_txt.setError(ToastFile.remark);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.remark, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date myDate = null;
                        try {
                            myDate = dateFormat.parse(getDispatchtime);
                            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            dispatch_time_to_pass = sdfdata.format(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }

                        barProgressDialog = new ProgressDialog(Consignment_entry_form.this);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(Consignment_entry_form.this);
                        JSONObject jsonObjectOtp = new JSONObject();
                        try {
                            jsonObjectOtp.put("API_KEY", api_key);
                            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode);
                            jsonObjectOtp.put("ThroughTSPCode", tsp_su_code);
                            jsonObjectOtp.put("DispatchTime", dispatch_time_to_pass);
                            jsonObjectOtp.put("ConsignmentNo", "");
                            jsonObjectOtp.put("TransitTime", expected_transit_time);
                            jsonObjectOtp.put("Mode", mode_value_compare);
                            jsonObjectOtp.put("SampleFromCPL", cpl_sample);
                            jsonObjectOtp.put("SampleFromRPL", rpl_sample);
                            jsonObjectOtp.put("PackagingDetails", pkg_details);
                            jsonObjectOtp.put("FlightName", "");
                            jsonObjectOtp.put("FlightNo", "");
                            jsonObjectOtp.put("DepTime", "");
                            jsonObjectOtp.put("ArrTime", "");
                            jsonObjectOtp.put("BSVBarcode", bsv_edt_txt);
                            jsonObjectOtp.put("Remarks", remark_to_pass);
                            jsonObjectOtp.put("TotalSamples", total_consignment_sample);
                            jsonObjectOtp.put("ConsignmentTemperature", consignment_temp);
                            jsonObjectOtp.put("CourierName", nameof_person);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(Consignment_entry_form.this, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        TastyToast.makeText(Consignment_entry_form.this, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error != null) {
                                } else {

                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);

                    }

                } else if (mode_value_compare.equals("LME")) {

                    String courier_name = courier_spinner.getSelectedItem().toString();
                    String consignment_temp = consignment_temp_spinner.getSelectedItem().toString();
                    String total_consignment_sample = total_consignment_edt_txt.getText().toString();
                    String pkg_details = packaging_dtl_edt_txt.getSelectedItem().toString();
                    String remark_to_pass = remark_edt_txt.getText().toString();
                    String bsv_edt_txt = bsv__edt_txt.getText().toString();
                    String rpl_sample = rpl_edt_txt.getText().toString();
                    String cpl_sample = cpl_edt_txt.getText().toString();
                    String consignment_barcode = enter_edt_txt.getText().toString();
                    String consignment_reenter_barcode = reenter_edt_txt.getText().toString();

                    final String cpl_data = cpl_edt_txt.getText().toString();
                    final String rpl_data = rpl_edt_txt.getText().toString();
                    final String total_consignment = total_consignment_edt_txt.getText().toString();

                    if (!total_consignment.equals("")) {
                        int cpl_int = Integer.parseInt(cpl_data);
                        int rpl_int = Integer.parseInt(rpl_data);
                        total_int = Integer.parseInt(total_consignment);
                        getTotal_count = cpl_int + rpl_int;
                    } else {
                        total_consignment_edt_txt.setError("");
                    }
                    if (rpl_data.equals("")) {
                        Toast.makeText(Consignment_entry_form.this, "Please Enter RPL", Toast.LENGTH_SHORT).show();
                    } else if (total_int == getTotal_count) {
                        cpl_edt_txt.setText(cpl_data);
                        rpl_edt_txt.setText(rpl_data);
                    } else if (total_int != getTotal_count) {
                        cpl_edt_txt.setError(ToastFile.crt_digit);
                        cpl_edt_txt.setText("");
                        rpl_edt_txt.setText("");
                        rpl_edt_txt.setError(ToastFile.crt_digit);
                    } else if (flagtsp == 0) {
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (remark_to_pass.equals("")) {
                        remark_edt_txt.setError(ToastFile.remark);
                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.remark, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }

                        barProgressDialog = new ProgressDialog(Consignment_entry_form.this);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(Consignment_entry_form.this);
                        JSONObject jsonObjectOtp = new JSONObject();
                        try {
                            jsonObjectOtp.put("API_KEY", api_key);
                            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode);
                            jsonObjectOtp.put("ThroughTSPCode", tsp_su_code);
                            jsonObjectOtp.put("DispatchTime", "");
                            jsonObjectOtp.put("ConsignmentNo", "");
                            jsonObjectOtp.put("TransitTime", "");
                            jsonObjectOtp.put("Mode", mode_value_compare);
                            jsonObjectOtp.put("SampleFromCPL", cpl_sample);
                            jsonObjectOtp.put("SampleFromRPL", rpl_sample);
                            jsonObjectOtp.put("PackagingDetails", "");
                            jsonObjectOtp.put("FlightName", "");
                            jsonObjectOtp.put("FlightNo", "");
                            jsonObjectOtp.put("DepTime", "");
                            jsonObjectOtp.put("ArrTime", "");
                            jsonObjectOtp.put("BSVBarcode", bsv_edt_txt);
                            jsonObjectOtp.put("Remarks", remark_to_pass);
                            jsonObjectOtp.put("TotalSamples", total_consignment_sample);
                            jsonObjectOtp.put("ConsignmentTemperature", "");
                            jsonObjectOtp.put("CourierName", "");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Log.e(TAG, "onResponse: "+response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(Consignment_entry_form.this, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(Consignment_entry_form.this, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        TastyToast.makeText(Consignment_entry_form.this, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error != null) {
                                } else {

                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);
                        Log.e(TAG, "onClick: "+jsonObjectOtp );
                        Log.e(TAG, "onClick: "+jsonObjectRequest1);
                    }

                }

            }
        });
    }

    private void fetchRplCplCount() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String passDate = sdf.format(d);

        requestQueue = Volley.newRequestQueue(Consignment_entry_form.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + user + "/" + passDate + "/getsamplecount", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                String finalJson = response.toString();
                JSONObject parentObjectOtp = null;
                try {
                    parentObjectOtp = new JSONObject(finalJson);
                    cpl_count = parentObjectOtp.getString("cpl_count");
                    response1 = parentObjectOtp.getString("response");
                    rpl_count = parentObjectOtp.getString("rpl_count");
                    total_count = parentObjectOtp.getString("total_count");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response1.equals("SUCCESS")) {
                    total_consignment_edt_txt.setText(total_count);
                    rpl_edt_txt.setText(rpl_count);
                    cpl_edt_txt.setText(cpl_count);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(Consignment_entry_form.this, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message
                    }
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }

    private String getDateToShow;
    private String format;
    private String getTimetoPass;
    private String dayToShow, monthToShow;
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (dayOfMonth < 10) {
                dayToShow = "0" + dayOfMonth;
            } else {
                dayToShow = String.valueOf(dayOfMonth);
            }

            int getMonth_num = monthOfYear;
            int get_final_month = getMonth_num + 1;

            if (get_final_month < 10) {
                monthToShow = "0" + get_final_month;
            } else {
                monthToShow = String.valueOf(get_final_month);
            }
            passMonth_Finally = String.valueOf(get_final_month);

            getDateToShow = dayToShow + "-" + monthToShow + "-" + year;

            Calendar calendar = Calendar.getInstance();
            CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
            CalendarMinute = calendar.get(Calendar.MINUTE);
            timepickerdialog = new TimePickerDialog(Consignment_entry_form.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }
                            if (hourOfDay < 10) {
                                hourOfDaytoshow = "0" + hourOfDay;
                            } else {
                                hourOfDaytoshow = String.valueOf(hourOfDay);
                            }
                            if (minute < 10) {
                                minutetoshow = "0" + minute;
                            } else {
                                minutetoshow = String.valueOf(minute);
                            }
                            getTimetoPass = hourOfDaytoshow + ":" + minutetoshow + " " + format;
                            String total_time = getDateToShow + " " + getTimetoPass;

                            expected_departure_time.setText(getDateToShow + " " + getTimetoPass);
                        }
                    }, CalendarHour, CalendarMinute, false);
            timepickerdialog.show();

        }
    };

    final DatePickerDialog.OnDateSetListener datefor_expected_arrival_time = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (dayOfMonth < 10) {
                dayToShow = "0" + dayOfMonth;
            } else {
                dayToShow = String.valueOf(dayOfMonth);
            }

            int getMonth_num = monthOfYear;
            int get_final_month = getMonth_num + 1;

            if (get_final_month < 10) {
                monthToShow = "0" + get_final_month;
            } else {
                monthToShow = String.valueOf(get_final_month);
            }
            passMonth_Finally = String.valueOf(get_final_month);

            getDateToShow = dayToShow + "-" + monthToShow + "-" + year;

            Calendar calendar = Calendar.getInstance();
            CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
            CalendarMinute = calendar.get(Calendar.MINUTE);
            timepickerdialog = new TimePickerDialog(Consignment_entry_form.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }

                            if (hourOfDay < 10) {
                                hourOfDaytoshow = "0" + hourOfDay;
                            } else {
                                hourOfDaytoshow = String.valueOf(hourOfDay);
                            }
                            if (minute < 10) {
                                minutetoshow = "0" + minute;
                            } else {
                                minutetoshow = String.valueOf(minute);
                            }
                            getTimetoPass = hourOfDaytoshow + ":" + minutetoshow + " " + format;
                            expected_arrival_time.setText(getDateToShow + " " + getTimetoPass);
                        }
                    }, CalendarHour, CalendarMinute, false);
            timepickerdialog.show();
        }
    };
    private String hourOfDaytoshow;
    private String minutetoshow;
    private String passMonth_Finally;
    final DatePickerDialog.OnDateSetListener dispatch_time_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (dayOfMonth < 10) {
                dayToShow = "0" + dayOfMonth;
            } else {
                dayToShow = String.valueOf(dayOfMonth);
            }
            int getMonth_num = monthOfYear;
            int get_final_month = getMonth_num + 1;

            if (get_final_month < 10) {
                monthToShow = "0" + get_final_month;
            } else {
                monthToShow = String.valueOf(get_final_month);
            }
            getDateToShow = dayToShow + "-" + monthToShow + "-" + year;

            Calendar calendar = Calendar.getInstance();
            CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
            CalendarMinute = calendar.get(Calendar.MINUTE);
            timepickerdialog = new TimePickerDialog(Consignment_entry_form.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }

                            if (hourOfDay < 10) {
                                hourOfDaytoshow = "0" + hourOfDay;
                            } else {
                                hourOfDaytoshow = String.valueOf(hourOfDay);
                            }
                            if (minute < 10) {
                                minutetoshow = "0" + minute;
                            } else {
                                minutetoshow = String.valueOf(minute);
                            }

                            getTimetoPass = hourOfDaytoshow + ":" + minutetoshow + " " + format;
//                            dispatch_time.setText(getDateToShow + " " + getTimetoPass);
                            String total_time = getDateToShow + " " + getTimetoPass;
//                            String getDispatchTime = dispatch_time.getText().toString();
                            Date d_convert = null;
                            Date compare = null;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                            try {
                                d_convert = sdf.parse(total_time);
                                Date d_pass = new Date();
                                String getDate = sdf.format(d_pass);
                                compare = sdf.parse(getDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (d_convert.equals(compare)) {
                                dispatch_time.setText("");
                                Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                                dispatch_time.setError(ToastFile.dispt_tm_condition);
                            } else if (d_convert.after(new Date())) {
                                dispatch_time.setText("");
                                Toast.makeText(Consignment_entry_form.this, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                                dispatch_time.setError(ToastFile.dispt_tm_condition);
                            } else {
                                dispatch_time.setText(getDateToShow + " " + getTimetoPass);
                            }

                        }
                    }, CalendarHour, CalendarMinute, false);
            timepickerdialog.show();
        }
    };


    private void initUI() {
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        direct = (RadioButton) findViewById(R.id.direct);
        through_tsp = (RadioButton) findViewById(R.id.through_tsp);

        total_consignment_edt_txt = (EditText) findViewById(R.id.total_consignment_edt_txt);

        cpl_edt_txt = (EditText) findViewById(R.id.cpl_edt_txt);
        source_code_pass = (EditText) findViewById(R.id.source_code_pass);
        rpl_edt_txt = (EditText) findViewById(R.id.rpl_edt_txt);
//        reenter_edt_txt = (EditText) findViewById(R.id.reenter_edt_txt);
//        enter_edt_txt = (EditText) findViewById(R.id.enter_edt_txt);
        packaging_dtl_edt_txt = (Spinner) findViewById(R.id.packaging_dtl_edt_txt);
        consignment_edt_txt = (EditText) findViewById(R.id.consignment_edt_txt);
        dispatch_time = (TextView) findViewById(R.id.dispatch_time);
        name_edt_txt = (EditText) findViewById(R.id.name_edt_txt);
        expected_arrival_time = (TextView) findViewById(R.id.expected_arrival_time);
        expected_departure_time = (TextView) findViewById(R.id.expected_departure_time);
        bus_number = (EditText) findViewById(R.id.bus_number);
        bus_edt_txt = (EditText) findViewById(R.id.bus_edt_txt);
        flight_name = (EditText) findViewById(R.id.flight_name);
        mode_spinner = (Spinner) findViewById(R.id.mode_spinner);
        filght_spinner = (Spinner) findViewById(R.id.filght_spinner);
        bus_spinner = (Spinner) findViewById(R.id.bus_spinner);
        courier_spinner = (Spinner) findViewById(R.id.courier_spinner);
        transit_time_spinner = (Spinner) findViewById(R.id.transit_time_spinner);
        consignment_temp_spinner = (Spinner) findViewById(R.id.consignment_temp_spinner);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
        flight_name_layout = (LinearLayout) findViewById(R.id.flight_name_layout);
        flight_number_layout = (LinearLayout) findViewById(R.id.flight_number_layout);
        bus_spinner_name_layout = (LinearLayout) findViewById(R.id.bus_spinner_name_layout);
        bus_name_layout = (LinearLayout) findViewById(R.id.bus_name_layout);
        bus_number_layout = (LinearLayout) findViewById(R.id.bus_number_layout);
        expected_depature_time_layout = (LinearLayout) findViewById(R.id.expected_depature_time_layout);
        expected_arrival_time_layout = (LinearLayout) findViewById(R.id.expected_arrival_time_layout);
        courier_spinner_name_layout = (LinearLayout) findViewById(R.id.courier_spinner_name_layout);
        courier_name_layout = (LinearLayout) findViewById(R.id.courier_name_layout);
        dispatch_time_layout = (LinearLayout) findViewById(R.id.dispatch_time_layout);
        consignment_name_layout = (LinearLayout) findViewById(R.id.consignment_name_layout);
        packaging_spinner_layout = (LinearLayout) findViewById(R.id.packaging_spinner_layout);
//        consignmnet_description_edt_layout = (LinearLayout) findViewById(R.id.consignmnet_description_edt_layout);
        expected_spinner_transit_time_layout = (LinearLayout) findViewById(R.id.expected_spinner_transit_time_layout);
        temperature_spinner_layout = (LinearLayout) findViewById(R.id.temperature_spinner_layout);
        total_sample_layout = (LinearLayout) findViewById(R.id.total_sample_layout);
        source_code_to_pass = (LinearLayout) findViewById(R.id.source_code_to_pass);
    }
}
