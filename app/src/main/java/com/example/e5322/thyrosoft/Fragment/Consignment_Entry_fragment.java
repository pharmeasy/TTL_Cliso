package com.example.e5322.thyrosoft.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Consignment_entry_form;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Consignment_Entry_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Consignment_Entry_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Consignment_Entry_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ManagingTabsActivity mContext;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View viewMain;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    Button consignment_edt_txt;
    RadioButton direct, through_tsp;
    TextView title, expected_departure_time, expected_arrival_time, dispatch_time, btn_submit, btn_to_next_ll;
    EditText total_consignment_edt_txt, bsv__edt_txt, cpl_edt_txt, source_code_pass, rpl_edt_txt, reenter_edt_txt, enter_edt_txt, name_edt_txt, bus_number, bus_edt_txt, flight_name, consignment_no_edt_txt;
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
            courier_name_layout, dispatch_time_layout, consignment_name_layout, packaging_spinner_layout,
            consignmnet_description_edt_layout, expected_spinner_transit_time_layout, temperature_spinner_layout,
            total_sample_layout, cpl_rpl_layout, source_code_to_pass, transport_details_ll, packaging_details_ll, consignment_number_ll, submit_ll;
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
    private OnFragmentInteractionListener mListener;

    public Consignment_Entry_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Consignment_Entry_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Consignment_Entry_fragment newInstance(String param1, String param2) {
        Consignment_Entry_fragment fragment = new Consignment_Entry_fragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (ManagingTabsActivity) getActivity();

        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        viewMain = (View) inflater.inflate(R.layout.activity_consignment_entry_form, container, false);

        title = (TextView) viewMain.findViewById(R.id.title);
        direct = (RadioButton) viewMain.findViewById(R.id.direct);
        through_tsp = (RadioButton) viewMain.findViewById(R.id.through_tsp);

        total_consignment_edt_txt = (EditText) viewMain.findViewById(R.id.total_consignment_edt_txt);

        cpl_edt_txt = (EditText) viewMain.findViewById(R.id.cpl_edt_txt);
        source_code_pass = (EditText) viewMain.findViewById(R.id.source_code_pass);
        rpl_edt_txt = (EditText) viewMain.findViewById(R.id.rpl_edt_txt);
//        reenter_edt_txt = (EditText) viewMain.findViewById(R.id.reenter_edt_txt);
//        enter_edt_txt = (EditText) viewMain.findViewById(R.id.enter_edt_txt);
        packaging_dtl_edt_txt = (Spinner) viewMain.findViewById(R.id.packaging_dtl_edt_txt);
        consignment_edt_txt = (Button) viewMain.findViewById(R.id.consignment_edt_txt);
        dispatch_time = (TextView) viewMain.findViewById(R.id.dispatch_time);
        name_edt_txt = (EditText) viewMain.findViewById(R.id.name_edt_txt);
        expected_arrival_time = (TextView) viewMain.findViewById(R.id.expected_arrival_time);
        expected_departure_time = (TextView) viewMain.findViewById(R.id.expected_departure_time);
        bus_number = (EditText) viewMain.findViewById(R.id.bus_number);
        bus_edt_txt = (EditText) viewMain.findViewById(R.id.bus_edt_txt);
        flight_name = (EditText) viewMain.findViewById(R.id.flight_name);
        consignment_no_edt_txt = (EditText) viewMain.findViewById(R.id.consignment_no_edt_txt);
        mode_spinner = (Spinner) viewMain.findViewById(R.id.mode_spinner);
        filght_spinner = (Spinner) viewMain.findViewById(R.id.filght_spinner);
        bus_spinner = (Spinner) viewMain.findViewById(R.id.bus_spinner);
        courier_spinner = (Spinner) viewMain.findViewById(R.id.courier_spinner);
        transit_time_spinner = (Spinner) viewMain.findViewById(R.id.transit_time_spinner);
        consignment_temp_spinner = (Spinner) viewMain.findViewById(R.id.consignment_temp_spinner);
        btn_submit = (TextView) viewMain.findViewById(R.id.btn_submit);
        btn_to_next_ll = (TextView) viewMain.findViewById(R.id.btn_to_next_ll);
        linear_layout = (LinearLayout) viewMain.findViewById(R.id.linear_layout);
        flight_name_layout = (LinearLayout) viewMain.findViewById(R.id.flight_name_layout);
        flight_number_layout = (LinearLayout) viewMain.findViewById(R.id.flight_number_layout);
        bus_spinner_name_layout = (LinearLayout) viewMain.findViewById(R.id.bus_spinner_name_layout);
        bus_name_layout = (LinearLayout) viewMain.findViewById(R.id.bus_name_layout);
        bus_number_layout = (LinearLayout) viewMain.findViewById(R.id.bus_number_layout);
        expected_depature_time_layout = (LinearLayout) viewMain.findViewById(R.id.expected_depature_time_layout);
        expected_arrival_time_layout = (LinearLayout) viewMain.findViewById(R.id.expected_arrival_time_layout);
        courier_spinner_name_layout = (LinearLayout) viewMain.findViewById(R.id.courier_spinner_name_layout);
        courier_name_layout = (LinearLayout) viewMain.findViewById(R.id.courier_name_layout);
        dispatch_time_layout = (LinearLayout) viewMain.findViewById(R.id.dispatch_time_layout);
        consignment_name_layout = (LinearLayout) viewMain.findViewById(R.id.consignment_name_layout);
        packaging_spinner_layout = (LinearLayout) viewMain.findViewById(R.id.packaging_spinner_layout);
//        consignmnet_description_edt_layout = (LinearLayout) viewMain.findViewById(R.id.consignmnet_description_edt_layout);
        expected_spinner_transit_time_layout = (LinearLayout) viewMain.findViewById(R.id.expected_spinner_transit_time_layout);
        temperature_spinner_layout = (LinearLayout) viewMain.findViewById(R.id.temperature_spinner_layout);
        total_sample_layout = (LinearLayout) viewMain.findViewById(R.id.total_sample_layout);


        source_code_to_pass = (LinearLayout) viewMain.findViewById(R.id.source_code_to_pass);
        transport_details_ll = (LinearLayout) viewMain.findViewById(R.id.transport_details_ll);
        packaging_details_ll = (LinearLayout) viewMain.findViewById(R.id.packaging_details_ll);
        consignment_number_ll = (LinearLayout) viewMain.findViewById(R.id.consignment_number_ll);
        submit_ll = (LinearLayout) viewMain.findViewById(R.id.submit_ll);


        fetchRplCplCount();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);
        minDate = myCalendar.getTime().getTime();

        setSpinnerTRansitTime = new ArrayList<>();
        setSpinnerTRansitTime.add("Select Expected Transit Time to CPL");
        setSpinnerTRansitTime.add("<6 hr");
        setSpinnerTRansitTime.add("6hr-8hr");
        setSpinnerTRansitTime.add("8hr-12hr");
        setSpinnerTRansitTime.add("12hr-24hr");
        setSpinnerTRansitTime.add("24hr-48hr");


        final ArrayAdapter mode_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.mode_spinner_values, R.layout.spinnerproperty);
        mode_spinner.setAdapter(mode_spinner_values);

        ArrayAdapter filght_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.filght_spinner_values, R.layout.spinnerproperty);
        filght_spinner.setAdapter(filght_spinner_values);

        ArrayAdapter bus_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.bus_spinner_values, R.layout.spinnerproperty);
        bus_spinner.setAdapter(bus_spinner_values);

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

            }
        });


        expected_departure_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        expected_arrival_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, datefor_expected_arrival_time, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        dispatch_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, dispatch_time_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


/*
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
                        Toast.makeText(mContext, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                    }
                } else if (getBsvBarcode.length() < enteredString.length()) {
                    enter_edt_txt.setText(enteredString.substring(1));
                } else {

                }
            }
        });
*/


/*
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
*/


/*
        enter_edt_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                final String search_barcode = enter_edt_txt.getText().toString();
                if (hasFocus) {

                } else if (!search_barcode.equals("")) {
                    barcodeDetails = Volley.newRequestQueue(mContext);//2c=/TAM03/TAM03136166236000078/geteditdata
                    progressDialog = new ProgressDialog(mContext);
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
                                progressDiaLog.dismiss();
                            } else {
                                enter_edt_txt.setText("");
                                progressDiaLog.dismiss();
                                Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new com.android.volley.Response.ErrorListener() {
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
*/

/*
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
                        total_consignment_edt_txt.setError(ToastFile.total_smple);
                    }


                    if (rpl_data.equals("")) {
                        Toast.makeText(mContext, "Please Enter RPL", Toast.LENGTH_SHORT).show();
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
*/


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
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                    } else if (d_convert.after(new Date())) {
                        dispatch_time.setText("");
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
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

                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);


                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);

                } else if (mode_value_compare.equals("Bus")) {

                    bus_spinner_name_layout.setVisibility(View.VISIBLE);
                    bus_number_layout.setVisibility(View.VISIBLE);
                    expected_depature_time_layout.setVisibility(View.VISIBLE);
                    expected_arrival_time_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    packaging_spinner_layout.setVisibility(View.VISIBLE);

                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);


                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);


                    bus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (bus_spinner.getSelectedItem().equals("OTHERS")) {
                                bus_name_layout.setVisibility(View.VISIBLE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);

                            } else {
                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else if (mode_value_compare.equals("Courier")) {

                    courier_spinner_name_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    packaging_spinner_layout.setVisibility(View.VISIBLE);

                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);


                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);


                    courier_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (courier_spinner.getSelectedItem().equals("OTHERS")) {
                                bus_spinner_name_layout.setVisibility(View.VISIBLE);

                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);

                            } else {

                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                bus_spinner_name_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);

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

                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                    temperature_spinner_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);


                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);


                    packaging_dtl_edt_txt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getSelectedItem_Pkg = packaging_dtl_edt_txt.getSelectedItem().toString();
                            if (getSelectedItem_Pkg.equals("Thermocol box with cooled gel pack")) {
                                courier_name_layout.setVisibility(View.VISIBLE);
                                dispatch_time_layout.setVisibility(View.VISIBLE);
                                packaging_spinner_layout.setVisibility(View.VISIBLE);

                                consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                                expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                                temperature_spinner_layout.setVisibility(View.VISIBLE);
                                total_sample_layout.setVisibility(View.VISIBLE);
                                cpl_rpl_layout.setVisibility(View.VISIBLE);


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

                                consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                                expected_spinner_transit_time_layout.setVisibility(View.VISIBLE);
                                temperature_spinner_layout.setVisibility(View.VISIBLE);
                                total_sample_layout.setVisibility(View.VISIBLE);
                                cpl_rpl_layout.setVisibility(View.VISIBLE);


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

                    consignmnet_description_edt_layout.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    cpl_rpl_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);


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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_to_next_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packaging_details_ll.setVisibility(View.VISIBLE);
                transport_details_ll.setVisibility(View.GONE);
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
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (flight_number_to_pass.equals("")) {
                        flight_name.setError(ToastFile.flight_num);
                        TastyToast.makeText(mContext, ToastFile.flight_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDeparture_time.equals("")) {
                        expected_departure_time.setError(ToastFile.dept_tm);
                        TastyToast.makeText(mContext, ToastFile.dept_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getArrival_time.equals("")) {
                        expected_arrival_time.setError(ToastFile.arrival_tm);
                        TastyToast.makeText(mContext, ToastFile.arrival_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_number.equals("")) {
                        consignment_edt_txt.setError(ToastFile.consign_num);
                        TastyToast.makeText(mContext, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(mContext, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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

                        barProgressDialog = new ProgressDialog(mContext);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(mContext);
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
                            jsonObjectOtp.put("Remarks", "");
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
                                    Log.e(TAG, "onResponse: " + response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(mContext, Consignment_entry_form.class);
                                        startActivity(i);
//                                        finish();
                                        TastyToast.makeText(mContext, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                        barProgressDialog.dismiss();
                                    }
                                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);

                        Log.e(TAG, "onClick: url" + jsonObjectOtp);
                        Log.e(TAG, "onClick: url" + jsonObjectRequest1);
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
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bus_number_to_pass.equals("")) {
                        bus_number.setError(ToastFile.bus_num);
                        TastyToast.makeText(mContext, ToastFile.bus_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDeparture_time.equals("")) {
                        expected_departure_time.setError(ToastFile.dept_tm);
                        TastyToast.makeText(mContext, ToastFile.dept_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getArrival_time.equals("")) {
                        expected_arrival_time.setError(ToastFile.arrival_tm);
                        TastyToast.makeText(mContext, ToastFile.arrival_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_number.equals("")) {
                        consignment_edt_txt.setError(ToastFile.consign_num);
                        TastyToast.makeText(mContext, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(mContext, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        barProgressDialog = new ProgressDialog(mContext);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(mContext);
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
                            jsonObjectOtp.put("Remarks", "");
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
                                    Log.e(TAG, "onResponse: " + response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(mContext, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(mContext, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        Log.e(TAG, "onClick: url" + jsonObjectOtp);
                        Log.e(TAG, "onClick: url" + jsonObjectRequest1);
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
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_number.equals("")) {
                        consignment_edt_txt.setError(ToastFile.consign_num);
                        TastyToast.makeText(mContext, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(mContext, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        barProgressDialog = new ProgressDialog(mContext);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(mContext);
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
                            jsonObjectOtp.put("Remarks", "");
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
                                    Log.e(TAG, "onResponse: " + response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(mContext, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(mContext, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        Log.e(TAG, "onClick: url" + jsonObjectOtp);
                        Log.e(TAG, "onClick: url" + jsonObjectRequest1);
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
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (nameof_person.equals("")) {
                        name_edt_txt.setError(ToastFile.per_name);
                        TastyToast.makeText(mContext, ToastFile.per_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.setError(ToastFile.dispt_tm);
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.setError(ToastFile.dispt_tm_condition);
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(mContext, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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

                        barProgressDialog = new ProgressDialog(mContext);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(mContext);
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
                            jsonObjectOtp.put("Remarks", "");
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
                                    Log.e(TAG, "onResponse: " + response);
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(mContext, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(mContext, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                        Log.e(TAG, "onClick: url" + jsonObjectOtp);
                        Log.e(TAG, "onClick: url" + jsonObjectRequest1);
                    }

                } else if (mode_value_compare.equals("LME")) {

                    String courier_name = courier_spinner.getSelectedItem().toString();
                    String consignment_temp = consignment_temp_spinner.getSelectedItem().toString();
                    String total_consignment_sample = total_consignment_edt_txt.getText().toString();
                    String pkg_details = packaging_dtl_edt_txt.getSelectedItem().toString();

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
//                        total_consignment_edt_txt.setError(ToastFile.total_smple);
                    }
                    if (rpl_data.equals("")) {
                        Toast.makeText(mContext, "Please Enter RPL", Toast.LENGTH_SHORT).show();
                    } else if (total_int == getTotal_count) {
                        cpl_edt_txt.setText(cpl_data);
                        rpl_edt_txt.setText(rpl_data);
                    } else if (total_int != getTotal_count) {
                        cpl_edt_txt.setError(ToastFile.crt_digit);
                        cpl_edt_txt.setText("");
                        rpl_edt_txt.setText("");
                        rpl_edt_txt.setError(ToastFile.crt_digit);
                    } else if (flagtsp == 0) {
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        enter_edt_txt.setError(ToastFile.consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_reenter_barcode.equals("")) {
                        reenter_edt_txt.setError(ToastFile.consign_cfrm_brcd);
                        TastyToast.makeText(mContext, ToastFile.consign_cfrm_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (total_consignment_sample.equals("")) {
                        total_consignment_edt_txt.setError(ToastFile.consign_total_sample);
                        TastyToast.makeText(mContext, ToastFile.consign_total_sample, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (rpl_sample.equals("")) {
                        rpl_edt_txt.setError(ToastFile.rpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.rpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (cpl_sample.equals("")) {
                        cpl_edt_txt.setError(ToastFile.cpl_consign_brcd);
                        TastyToast.makeText(mContext, ToastFile.cpl_consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bsv_edt_txt.equals("")) {
                        bsv__edt_txt.setError(ToastFile.bsv_brcd);
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }

                        barProgressDialog = new ProgressDialog(mContext);
                        barProgressDialog.setTitle("Kindly wait ...");
                        barProgressDialog.setMessage(ToastFile.processing_request);
                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                        barProgressDialog.setProgress(0);
                        barProgressDialog.setMax(20);
                        barProgressDialog.show();
                        barProgressDialog.setCanceledOnTouchOutside(false);
                        barProgressDialog.setCancelable(false);
                        PostQueAirCargo = Volley.newRequestQueue(mContext);
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
                            jsonObjectOtp.put("Remarks", "");
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
                                    Log.e(TAG, "onResponse: " + response);
                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                        barProgressDialog.dismiss();
                                    }
                                    String finalJson = response.toString();
                                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                                    Response = parentObjectOtp.getString("Response");
                                    message = parentObjectOtp.getString("Message");
                                    ResId = parentObjectOtp.getString("ResId");
                                    if (Response.equals("Success")) {

                                        bsv__edt_txt.setText("");
                                        enter_edt_txt.setText("");
                                        reenter_edt_txt.setText("");
                                        Intent i = new Intent(mContext, Consignment_entry_form.class);
                                        startActivity(i);

                                        TastyToast.makeText(mContext, ToastFile.consignment_done, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    } else {

                                        TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    }

                                } catch (JSONException e) {
                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                        barProgressDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                if (error != null) {
                                } else {

                                    System.out.println(error);
                                }
                            }
                        });
                        PostQueAirCargo.add(jsonObjectRequest1);
                        Log.e(TAG, "onClick: url" + jsonObjectOtp);
                        Log.e(TAG, "onClick: url" + jsonObjectRequest1);

                    }

                }

            }
        });

        // Inflate the layout for this fragment
        return viewMain;
    }

    private void fetchRplCplCount() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String passDate = sdf.format(d);

        requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + user + "/" + passDate + "/getsamplecount", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: response" + response);
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
                        TastyToast.makeText(mContext, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        Log.e(TAG, "fetchRplCplCount: URL" + jsonObjectRequest);

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
            timepickerdialog = new TimePickerDialog(mContext,
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
            timepickerdialog = new TimePickerDialog(mContext,
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
            timepickerdialog = new TimePickerDialog(mContext,
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
                                Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                                dispatch_time.setError(ToastFile.dispt_tm_condition);
                            } else if (d_convert.after(new Date())) {
                                dispatch_time.setText("");
                                Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                                dispatch_time.setError(ToastFile.dispt_tm_condition);
                            } else {
                                dispatch_time.setText(getDateToShow + " " + getTimetoPass);
                            }

                        }
                    }, CalendarHour, CalendarMinute, false);
            timepickerdialog.show();
        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
}
