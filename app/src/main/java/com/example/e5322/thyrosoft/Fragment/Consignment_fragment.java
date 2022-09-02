package com.example.e5322.thyrosoft.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.small_invalidApikey;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.HomeMenuActivity;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Adapter.BarcodeAdapter;
import com.example.e5322.thyrosoft.Adapter.ScannedBarcodeAdapter;
import com.example.e5322.thyrosoft.Controller.GetBarcodeListController;
import com.example.e5322.thyrosoft.Controller.GetScannedBarcodeList;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.PostBarcodeController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.ConsignmentRequestModel;
import com.example.e5322.thyrosoft.Models.PostBarcodeModel;
import com.example.e5322.thyrosoft.Models.PostBarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.ScanBarcodeResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Consignment_fragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int mode = 0;
    private static HomeMenuActivity mContext;
    View viewMain, view_pre_frag;
    Button consignment_barcd_btn, bsv_barcode_scanning, Submit_consignment;
    IntentIntegrator scanIntegrator;
    private OnFragmentInteractionListener mListener;
    ImageView enter_arrow_enterss, enter_arrow_enteredss, img_edt, img_edt_bsv, setback, setbackbsv, img_scan_consignment_barcode, img_scan_bsv_barcode;
    RadioButton direct, through_tsp;
    TextView title, consign_discription_data, expected_departure_time, expected_arrival_time, dispatch_time, total_consignment_edt_txt, cpl_edt_txt, rpl_edt_txt;
    Button btn_submit, btn_woe;
    EditText remark_edt_txt, source_code_pass, hand_name_edt_txt, name_edt_txt, bus_number, bus_edt_txt, flight_name;
    String user, passwrd, access, api_key, email_pref, mobile_pref;
    Spinner mode_spinner, filght_spinner, bus_spinner, courier_spinner, transit_time_spinner, consignment_temp_spinner;
    private SharedPreferences prefs;
    long minDate;
    EditText enter_barcode, reenter, reenterbsv, enter_barcodebsv;
    ArrayList<String> setSpinnerTRansitTime;
    ArrayList<String> setCouriernameSpinner;
    private int CalendarHour, CalendarMinute;
    TimePickerDialog timepickerdialog;
    int flagtsp = 0;
    TextView enter_consign, enetered_consign;
    Calendar myCalendar;
    ProgressDialog barProgressDialog;
    LinearLayout source_code_to_pass, flight_name_layout, flight_number_layout, bus_spinner_name_layout, bus_name_layout,
            bus_number_layout, expected_depature_time_layout, expected_arrival_time_layout, courier_spinner_name_layout,
            courier_name_layout, dispatch_time_layout, consignment_number_ll, consignment_name_layout, total_sample_layout,
            bsv_barcode_scanning_ll, btn_to_next_ll, submit_ll, hand_del_name_layout, lineareditbarcode, lineareditbarcodebsv;
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
    LinearLayout enter_ll_unselected_consign, unchecked_entered_ll_consign;
    private int getTotal_count;
    private String SaveTransitTime;
    private RequestQueue requestQueue;
    private String cpl_count, response1, rpl_count, total_count, ERROR, RES_ID, barcode;
    boolean flag = false;
    private RequestQueue barcodeDetailsdata;
    private String currentText;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String getBarcodeDetails;
    private String consignment_barcode;
    private String bsv_edt_txt;
    private String courier_name;
    private String getCourier_name;
    private String get_edt_bus_name;
    private String courier_name_to_pass;
    private String getDateDispatchTime;
    private RequestQueue PostQueueForConsignment;
    private Date dep_date_time, arr_date_time, dispatch_date_time;

    TextView tv_addpouch;
    RecyclerView recy_barcode;
    private Dialog pocuh_dialog;
    ConnectionDetector connectionDetector;
    RecyclerView recy_scannedbarcode;
    LinearLayout ll_pouch;
    private ArrayList<BarcodeResponseModel.BarcodeDTO> setGlobalListBarcode;
    private ArrayList<ScanBarcodeResponseModel.PouchCodeDTO> setBarcodePouch;
    private Button btn_pouch_barcd;
    private ArrayList<BarcodeResponseModel.BarcodeDTO> barcodelist = new ArrayList<>();
    private ArrayList<BarcodeResponseModel.BarcodeDTO> totalbarcodelist = new ArrayList<>();

    public Consignment_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Consignment_fragment newInstance(String param1, String param2) {
        Consignment_fragment fragment = new Consignment_fragment();
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

        mContext = (HomeMenuActivity) getActivity();
        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        viewMain = (View) inflater.inflate(R.layout.tm, container, false);
        connectionDetector = new ConnectionDetector(getActivity());
        barProgressDialog = new ProgressDialog(mContext);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        direct = (RadioButton) viewMain.findViewById(R.id.direct);
        through_tsp = (RadioButton) viewMain.findViewById(R.id.through_tsp);
        mode_spinner = (Spinner) viewMain.findViewById(R.id.mode_spinner);
        filght_spinner = (Spinner) viewMain.findViewById(R.id.filght_spinner);
        bus_spinner = (Spinner) viewMain.findViewById(R.id.bus_spinner);
        courier_spinner = (Spinner) viewMain.findViewById(R.id.courier_spinner);
        flight_name_layout = (LinearLayout) viewMain.findViewById(R.id.flight_name_layout);
        flight_number_layout = (LinearLayout) viewMain.findViewById(R.id.flight_number_layout);
        bus_spinner_name_layout = (LinearLayout) viewMain.findViewById(R.id.bus_spinner_name_layout);
        bus_name_layout = (LinearLayout) viewMain.findViewById(R.id.bus_name_layout);
        bus_number_layout = (LinearLayout) viewMain.findViewById(R.id.bus_number_layout);
        expected_depature_time_layout = (LinearLayout) viewMain.findViewById(R.id.expected_depature_time_layout);
        expected_arrival_time_layout = (LinearLayout) viewMain.findViewById(R.id.expected_arrival_time_layout);
        courier_spinner_name_layout = (LinearLayout) viewMain.findViewById(R.id.courier_spinner_name_layout);
        courier_name_layout = (LinearLayout) viewMain.findViewById(R.id.courier_name_layout);
        hand_del_name_layout = (LinearLayout) viewMain.findViewById(R.id.hand_del_name_layout);
        dispatch_time_layout = (LinearLayout) viewMain.findViewById(R.id.dispatch_time_layout);
        consignment_number_ll = (LinearLayout) viewMain.findViewById(R.id.consignment_number_ll);
        consignment_name_layout = (LinearLayout) viewMain.findViewById(R.id.consignment_name_layout);
        total_sample_layout = (LinearLayout) viewMain.findViewById(R.id.total_sample_layout);
        bsv_barcode_scanning_ll = (LinearLayout) viewMain.findViewById(R.id.bsv_barcode_scanning_ll);
        submit_ll = (LinearLayout) viewMain.findViewById(R.id.submit_ll);
        btn_to_next_ll = (LinearLayout) viewMain.findViewById(R.id.btn_to_next_ll);
        source_code_to_pass = (LinearLayout) viewMain.findViewById(R.id.source_code_to_pass);
        lineareditbarcode = (LinearLayout) viewMain.findViewById(R.id.lineareditbarcode);
        lineareditbarcodebsv = (LinearLayout) viewMain.findViewById(R.id.lineareditbarcodebsv);
        unchecked_entered_ll_consign = (LinearLayout) viewMain.findViewById(R.id.unchecked_entered_ll_consign);
        enter_ll_unselected_consign = (LinearLayout) viewMain.findViewById(R.id.enter_ll_unselected_consign);

        total_consignment_edt_txt = (TextView) viewMain.findViewById(R.id.total_consignment_edt_txt);
        cpl_edt_txt = (TextView) viewMain.findViewById(R.id.cpl_edt_txt);
        source_code_pass = (EditText) viewMain.findViewById(R.id.source_code_pass);
        enter_consign = (TextView) viewMain.findViewById(R.id.enter_consign);
        enetered_consign = (TextView) viewMain.findViewById(R.id.enetered_consign);
        rpl_edt_txt = (TextView) viewMain.findViewById(R.id.rpl_edt_txt);
        enter_barcode = (EditText) viewMain.findViewById(R.id.enter_barcode);
        reenter = (EditText) viewMain.findViewById(R.id.reenter);
        enter_barcodebsv = (EditText) viewMain.findViewById(R.id.enter_barcodebsv);
        reenterbsv = (EditText) viewMain.findViewById(R.id.reenterbsv);
        flight_name = (EditText) viewMain.findViewById(R.id.flight_name);
        name_edt_txt = (EditText) viewMain.findViewById(R.id.name_edt_txt);
        hand_name_edt_txt = (EditText) viewMain.findViewById(R.id.hand_name_edt_txt);
        bus_number = (EditText) viewMain.findViewById(R.id.bus_number);
        bus_edt_txt = (EditText) viewMain.findViewById(R.id.bus_edt_txt);

        expected_departure_time = (TextView) viewMain.findViewById(R.id.expected_departure_time);
        expected_arrival_time = (TextView) viewMain.findViewById(R.id.expected_arrival_time);
        dispatch_time = (TextView) viewMain.findViewById(R.id.dispatch_time);
        img_edt = (ImageView) viewMain.findViewById(R.id.img_edt);
        img_edt_bsv = (ImageView) viewMain.findViewById(R.id.img_edt_bsv);
        setback = (ImageView) viewMain.findViewById(R.id.setback);
        setbackbsv = (ImageView) viewMain.findViewById(R.id.setbackbsv);
        enter_arrow_enterss = (ImageView) viewMain.findViewById(R.id.enter_arrow_enterss);
        enter_arrow_enteredss = (ImageView) viewMain.findViewById(R.id.enter_arrow_enteredss);
        img_scan_consignment_barcode = (ImageView) viewMain.findViewById(R.id.img_scan_consignment_barcode);
        img_scan_bsv_barcode = (ImageView) viewMain.findViewById(R.id.img_scan_bsv_barcode);

        img_scan_consignment_barcode = (ImageView) viewMain.findViewById(R.id.img_scan_consignment_barcode);
        img_scan_bsv_barcode = (ImageView) viewMain.findViewById(R.id.img_scan_bsv_barcode);
        tv_addpouch = viewMain.findViewById(R.id.tv_addpouch);
        recy_scannedbarcode = viewMain.findViewById(R.id.recy_scannedbarcode);


        consignment_barcd_btn = (Button) viewMain.findViewById(R.id.consignment_barcd_btn);
        bsv_barcode_scanning = (Button) viewMain.findViewById(R.id.bsv_barcode_scanning);
        Submit_consignment = (Button) viewMain.findViewById(R.id.Submit_consignment);
        ll_pouch = viewMain.findViewById(R.id.ll_pouch);

        OnBackPressedCallback onBackPressedCallback =  new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                GlobalClass.redirectToHome(getActivity());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(),onBackPressedCallback);

        tv_addpouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });


        flight_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    Toast.makeText(getActivity(),
                            "Please enter correct flight Number",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        flight_name.setText(enteredString.substring(1));
                    } else {
                        flight_name.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        enter_barcode.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        enter_barcodebsv.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        enter_barcode.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        reenter.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        enter_barcodebsv.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        reenterbsv.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });


        source_code_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(getActivity(),
                            "Please enter correct source code",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        source_code_pass.setText(enteredString.substring(1));
                    } else {
                        source_code_pass.setText("");
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


        bus_edt_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(getActivity(),
                            "Please enter correct bus name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        bus_edt_txt.setText(enteredString.substring(1));
                    } else {
                        bus_edt_txt.setText("");
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

        setCouriernameSpinner = new ArrayList<>();
        setCouriernameSpinner.add("Select Courier Name");
        setCouriernameSpinner.add("ARAMEX INDIA PVT LTD (INTERNATIONAL)");
        setCouriernameSpinner.add("BLUE DART EXPRESS LIMITED - AIR");
        setCouriernameSpinner.add("BLUE DART EXPRESS LIMITED - SURFACE");
        setCouriernameSpinner.add("BLUE DART EXPRESS LTD-607924");
        setCouriernameSpinner.add("DMS COURIER SERVICES PVT LTD");
        setCouriernameSpinner.add("DTDC COURIER and CARGO LTD");
        setCouriernameSpinner.add("DTDC COURIER and CARGO LTD (AIR)");
        setCouriernameSpinner.add("FEDEX COURIER (AIR)");
        setCouriernameSpinner.add("FEDEX COURIER (SURFACE)");
        setCouriernameSpinner.add("GATI KINTETSU EXPRESS PRIVATE LIMITED");
        setCouriernameSpinner.add("GATI LIMITED (INTERNATIONAL)");
        setCouriernameSpinner.add("TN STC CBE");
        setCouriernameSpinner.add("TN38N2548");
        setCouriernameSpinner.add("TNNSTC CBE");
        setCouriernameSpinner.add("TNSCT CBE");
        setCouriernameSpinner.add("TNSCTC CBE");
        setCouriernameSpinner.add("TNSRC CBE");
        setCouriernameSpinner.add("TNST CBE");
        setCouriernameSpinner.add("TNSTC");
        setCouriernameSpinner.add("TNSTC CBE");
        setCouriernameSpinner.add("TNSTC CXBE");
        setCouriernameSpinner.add("TNSTC/CBE");
        setCouriernameSpinner.add("TNSTCE CBE");
        setCouriernameSpinner.add("TNSTSC CBE");
        setCouriernameSpinner.add("TTNSTC CBE");
        setCouriernameSpinner.add("OTHERS");


        enter_consign.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_enterss.setVisibility(View.GONE);
        enetered_consign.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enteredss.setVisibility(View.VISIBLE);

        unchecked_entered_ll_consign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_consign.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enterss.setVisibility(View.GONE);
                enetered_consign.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enteredss.setVisibility(View.VISIBLE);

                GlobalClass.flagToSend = true;
                GlobalClass.flagToSendfromnavigation = false;
                Consignment_fragment fragment = new Consignment_fragment();
                mContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                //   checkConsgnmentFortheDay();

            }
        });
        enter_ll_unselected_consign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enter_consign.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enterss.setVisibility(View.VISIBLE);
                enetered_consign.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enteredss.setVisibility(View.GONE);

                Wind_up_fragment a2Fragment = new Wind_up_fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
            }
        });

        final ArrayAdapter mode_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.mode_spinner_values, R.layout.spinner_white_prop);
        mode_spinner.setAdapter(mode_spinner_values);

        ArrayAdapter filght_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.filght_spinner_values, R.layout.spinner_white_prop);
        filght_spinner.setAdapter(filght_spinner_values);

        ArrayAdapter bus_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.bus_spinner_values, R.layout.spinner_white_prop);
        bus_spinner.setAdapter(bus_spinner_values);

        ArrayAdapter courier_spinner_values = new ArrayAdapter(mContext, R.layout.spinner_white_prop, setCouriernameSpinner);
        courier_spinner.setAdapter(courier_spinner_values);


        if (GlobalClass.flagtsp != 0) {
            if (GlobalClass.flagtsp == 1) {
                direct.setSelected(true);
                direct.setChecked(true);
                source_code_to_pass.setVisibility(View.GONE);
                through_tsp.setSelected(false);
                through_tsp.setChecked(false);
                routing_code = "direct";
                flagtsp = 1;
            } else if (GlobalClass.flagtsp == 2) {
                direct.setSelected(false);
                direct.setChecked(false);
                through_tsp.setSelected(true);
                through_tsp.setChecked(true);
                source_code_to_pass.setVisibility(View.VISIBLE);
                flagtsp = 2;
                routing_code = "through_tsp";
            }
        }
//        else {
//            direct.setSelected(false);
//            through_tsp.setSelected(false);
//        }
        if (GlobalClass.routine_code != null) {
            source_code_pass.setText(GlobalClass.routine_code);
        } else {
            source_code_pass.setText("");
        }

        if (GlobalClass.mode != null) {
            if (GlobalClass.mode.equals("Air Cargo")) {
                mode_spinner.setSelection(1);
            } else if (GlobalClass.mode.equals("Bus")) {
                mode_spinner.setSelection(2);
            } else if (GlobalClass.mode.equals("Courier")) {
                mode_spinner.setSelection(3);
            } else if (GlobalClass.mode.equals("Hand Delivery")) {
                mode_spinner.setSelection(4);
            } else {
                mode_spinner.setSelection(0);
            }
        } else {
            mode_spinner.setSelection(0);
        }
        if (GlobalClass.flight_name != null) {
            if (GlobalClass.flight_name.equals("Air India")) {
                filght_spinner.setSelection(1);
            } else if (GlobalClass.flight_name.equals("Indigo")) {
                filght_spinner.setSelection(2);
            } else if (GlobalClass.flight_name.equals("Spice jet")) {
                filght_spinner.setSelection(3);
            }
        } else {
            filght_spinner.setSelection(0);
        }
        if (GlobalClass.flight_number != null) {
            flight_name.setText(GlobalClass.flight_number);
        } else {
            flight_name.setText("");
        }
        if (GlobalClass.bus_name_to_pass != null) {
            if (GlobalClass.bus_name_to_pass.equals("AARTHI TRAVELS")) {
                bus_spinner.setSelection(1);
            } else if (GlobalClass.bus_name_to_pass.equals("TNSTC")) {
                bus_spinner.setSelection(2);
            } else if (GlobalClass.bus_name_to_pass.equals("OTHERS")) {
                bus_spinner.setSelection(3);
            }
        }
        if (GlobalClass.get_edt_bus_name != null) {
            bus_edt_txt.setText(GlobalClass.get_edt_bus_name);
        } else {
            bus_edt_txt.setText("");
        }
        if (GlobalClass.bus_number != null) {
            bus_number.setText(GlobalClass.bus_number);
        }

        if (GlobalClass.departure_time != null && GlobalClass.arrival_time != null || GlobalClass.dispatch_time != null) {
            expected_departure_time.setText(GlobalClass.departure_time);
            expected_arrival_time.setText(GlobalClass.arrival_time);
            dispatch_time.setText(GlobalClass.dispatch_time);
        } else {
            expected_departure_time.setText("");
            expected_arrival_time.setText("");
            dispatch_time.setText("");
        }
        if (GlobalClass.bsv_barcode != null) {
            enter_barcodebsv.setText(GlobalClass.bsv_barcode);
            reenterbsv.setText(GlobalClass.bsv_barcode);
            bsv_barcode_scanning.setText(GlobalClass.bsv_barcode);
        } else {
            bsv_barcode_scanning.setHint("BSV Barcode");
        }
        if (GlobalClass.Consignment_barcode != null) {
            enter_barcode.setText(GlobalClass.Consignment_barcode);
            reenter.setText(GlobalClass.Consignment_barcode);
            consignment_barcd_btn.setText(GlobalClass.Consignment_barcode);
        } else {
            consignment_barcd_btn.setHint(ToastFile.consign_brcd_name);
        }

        if (GlobalClass.CourierName != null) {
            for (int i = 0; i < setCouriernameSpinner.size(); i++) {
                if (GlobalClass.CourierName.equals(setCouriernameSpinner.get(i))) {
                    courier_spinner.setSelection(i);
                }
            }
        }

        if (GlobalClass.getCourier_name != null) {
            if (GlobalClass.mode.equals("Hand Delivery")) {
                hand_name_edt_txt.setText(GlobalClass.getCourier_name);
            } else if (GlobalClass.CourierName.equals("OTHERS")) {
                if (GlobalClass.getCourier_name != null || !GlobalClass.getCourier_name.equals("")) {
                    name_edt_txt.setText(GlobalClass.getCourier_name);
                } else {
                    name_edt_txt.setText("");
                    hand_name_edt_txt.setText("");
                }
            }
        }


        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing_code = "direct";
                source_code_to_pass.setVisibility(View.GONE);
                flagtsp = 1;
            }
        });
        through_tsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing_code = "through_tsp";
                source_code_to_pass.setVisibility(View.VISIBLE);
                flagtsp = 2;
                source_code_pass.setText(user);
            }
        });

        expected_departure_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mode = mode_spinner.getSelectedItem().toString();

                if (mode.equalsIgnoreCase("Air Cargo") || mode.equalsIgnoreCase("Bus")) {
                    if (dispatch_time.getText().toString().equals("")) {
                        TastyToast.makeText(getActivity(), "First select dispatch time !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DAY_OF_MONTH, -4);
                        Date result = cal.getTime();
                        datePickerDialog.getDatePicker().setMinDate(result.getTime());

                        Calendar cal1 = Calendar.getInstance();
                        cal1.add(Calendar.DAY_OF_MONTH, 2);
                        Date result1 = cal1.getTime();
                        datePickerDialog.getDatePicker().setMaxDate(result1.getTime());

                        datePickerDialog.show();
                    }

                } else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, -4);
                    Date result = cal.getTime();
                    datePickerDialog.getDatePicker().setMinDate(result.getTime());

                    Calendar cal1 = Calendar.getInstance();
                    cal1.add(Calendar.DAY_OF_MONTH, 2);
                    Date result1 = cal1.getTime();
                    datePickerDialog.getDatePicker().setMaxDate(result1.getTime());

                    datePickerDialog.show();
                }
            }
        });

        expected_arrival_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mode = mode_spinner.getSelectedItem().toString();
                if (mode.equalsIgnoreCase("Air Cargo") || mode.equalsIgnoreCase("Bus")) {
                    if (expected_departure_time.getText().toString().equals("")) {
                        TastyToast.makeText(getActivity(), "First select departure time !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, datefor_expected_arrival_time,
                                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }

                } else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, datefor_expected_arrival_time,
                            myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                    datePickerDialog.show();

                }

            }
        });

        dispatch_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mode = mode_spinner.getSelectedItem().toString();
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, dispatch_time_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });

        consignment_barcd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode.setVisibility(View.VISIBLE);
                consignment_name_layout.setVisibility(View.GONE);
            }
        });
        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode.setVisibility(View.GONE);
                consignment_name_layout.setVisibility(View.VISIBLE);
            }
        });
        bsv_barcode_scanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcodebsv.setVisibility(View.VISIBLE);
                bsv_barcode_scanning_ll.setVisibility(View.GONE);
            }
        });
        setbackbsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcodebsv.setVisibility(View.GONE);
                bsv_barcode_scanning_ll.setVisibility(View.GONE);
            }
        });

        enter_barcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                final String search_barcode = enter_barcode.getText().toString();
                if (hasFocus) {

                } else if (!search_barcode.equals("") && search_barcode.length() >= 8) {
                    barcodeDetails = GlobalClass.setVolleyReq(mContext);//2c=/TAM03/TAM03136166236000078/geteditdata
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
                            Log.v(TAG, "barcode respponse" + response);

                            Log.e(TAG, "onResponse: " + response);
                            if (response.equals("\"Valid\"")) {
                                enter_barcode.setText(search_barcode);
                                progressDialog.dismiss();
                            } else {
                                enter_barcode.setText("");
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
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
                    Log.e(TAG, "onFocusChange: url" + jsonObjectRequestPop);
                } else {
                    enter_barcode.setText("");
                }
            }
        });

        enter_barcodebsv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(mContext,
                            ToastFile.crt_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcodebsv.setText(enteredString.substring(1));
                    } else {
                        enter_barcodebsv.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        reenterbsv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(mContext, ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        reenterbsv.setText(enteredString.substring(1));
                    } else {
                        reenterbsv.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                int getlength = enter_barcodebsv.getText().length();
                if (getlength == 8) {
                    String getPreviouseText = enter_barcodebsv.getText().toString();
                    currentText = reenterbsv.getText().toString();
                    if (getPreviouseText.equals(currentText)) {
                        currentText = reenterbsv.getText().toString();
                        lineareditbarcodebsv.setVisibility(View.GONE);
                        bsv_barcode_scanning_ll.setVisibility(View.GONE);
                        bsv_barcode_scanning.setText(currentText);
                    } else {

                    }

                } else {

                }
            }
        });


        enter_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(mContext, ToastFile.entr_brcd, Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcode.setText(enteredString.substring(1));
                    } else {
                        enter_barcode.setText("");
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

        reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(mContext,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        reenter.setText(enteredString.substring(1));
                    } else {
                        reenter.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                int getlength = enter_barcode.getText().length();
                if (getlength >= 8) {
                    String getPreviouseText = enter_barcode.getText().toString();
                    currentText = reenter.getText().toString();
                    if (getPreviouseText.equals(currentText)) {
                        currentText = reenter.getText().toString();
                        lineareditbarcode.setVisibility(View.GONE);
                        consignment_name_layout.setVisibility(View.VISIBLE);
                        consignment_barcd_btn.setText(currentText);
                    } else {

                    }

                } else {

                }

            }
        });


        scanIntegrator = IntentIntegrator.forSupportFragment(this);
        img_scan_consignment_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1;
                scanIntegrator.initiateScan();
            }
        });


        img_scan_bsv_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 2;
                scanIntegrator.initiateScan();
            }
        });

        mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode_value_compare = mode_spinner.getSelectedItem().toString();

                if (mode_value_compare.equals("Select Mode")) {
                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    dispatch_time_layout.setVisibility(View.GONE);
                    btn_to_next_ll.setVisibility(View.VISIBLE);
                    ll_pouch.setVisibility(View.GONE);
                    total_sample_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);

                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);

                    total_sample_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);

                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    hand_del_name_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);
                    lineareditbarcode.setVisibility(View.GONE);
                    lineareditbarcodebsv.setVisibility(View.GONE);
                } else if (mode_value_compare.equals("Air Cargo")) {

                    flight_name_layout.setVisibility(View.VISIBLE);
                    flight_number_layout.setVisibility(View.VISIBLE);
                    expected_depature_time_layout.setVisibility(View.VISIBLE);
                    expected_arrival_time_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    btn_to_next_ll.setVisibility(View.VISIBLE);
                    ll_pouch.setVisibility(View.GONE);
                    total_sample_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);

                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);

                    total_sample_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);

                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    hand_del_name_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);
                    lineareditbarcode.setVisibility(View.GONE);
                    lineareditbarcodebsv.setVisibility(View.GONE);
                } else if (mode_value_compare.equals("Bus")) {

                    bus_spinner_name_layout.setVisibility(View.VISIBLE);
                    bus_number_layout.setVisibility(View.VISIBLE);
                    expected_depature_time_layout.setVisibility(View.VISIBLE);
                    expected_arrival_time_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);
                    btn_to_next_ll.setVisibility(View.VISIBLE);
                    ll_pouch.setVisibility(View.GONE);
                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    hand_del_name_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);
                    total_sample_layout.setVisibility(View.GONE);
                    consignment_name_layout.setVisibility(View.GONE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);
                    lineareditbarcode.setVisibility(View.GONE);
                    lineareditbarcodebsv.setVisibility(View.GONE);
                    bus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (bus_spinner.getSelectedItem().equals("OTHERS")) {
                                bus_name_layout.setVisibility(View.VISIBLE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                total_sample_layout.setVisibility(View.GONE);
                                consignment_name_layout.setVisibility(View.GONE);
                                bsv_barcode_scanning_ll.setVisibility(View.GONE);
                                lineareditbarcode.setVisibility(View.GONE);
                                lineareditbarcodebsv.setVisibility(View.GONE);
                            } else {
                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                courier_spinner_name_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                hand_del_name_layout.setVisibility(View.GONE);
                                total_sample_layout.setVisibility(View.GONE);
                                consignment_name_layout.setVisibility(View.GONE);
                                bsv_barcode_scanning_ll.setVisibility(View.GONE);
                                lineareditbarcode.setVisibility(View.GONE);
                                lineareditbarcodebsv.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else if (mode_value_compare.equals("Courier")) {
                    name_edt_txt.setHint("Enter courier name");
                    GetScannerBarcode();
                    courier_spinner_name_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);

                    total_sample_layout.setVisibility(View.VISIBLE);
                    ll_pouch.setVisibility(View.VISIBLE);
                    btn_to_next_ll.setVisibility(View.VISIBLE);
                    total_sample_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);

                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    hand_del_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);

                    consignment_number_ll.setVisibility(View.GONE);
                    lineareditbarcode.setVisibility(View.GONE);
                    lineareditbarcodebsv.setVisibility(View.GONE);

                    courier_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (courier_spinner.getSelectedItem().equals("OTHERS")) {
                                courier_name_layout.setVisibility(View.VISIBLE);
                                consignment_name_layout.setVisibility(View.VISIBLE);

                                hand_del_name_layout.setVisibility(View.GONE);
                                bus_spinner_name_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);

                                bus_number_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);

                                consignment_number_ll.setVisibility(View.GONE);
                                lineareditbarcode.setVisibility(View.GONE);
                                lineareditbarcodebsv.setVisibility(View.GONE);

                                total_sample_layout.setVisibility(View.VISIBLE);

                                bsv_barcode_scanning_ll.setVisibility(View.GONE);

                            } else {
                                consignment_name_layout.setVisibility(View.VISIBLE);
                                bsv_barcode_scanning_ll.setVisibility(View.GONE);
                                total_sample_layout.setVisibility(View.VISIBLE);

                                flight_name_layout.setVisibility(View.GONE);
                                flight_number_layout.setVisibility(View.GONE);
                                bus_spinner_name_layout.setVisibility(View.GONE);
                                bus_name_layout.setVisibility(View.GONE);
                                bus_number_layout.setVisibility(View.GONE);
                                courier_name_layout.setVisibility(View.GONE);
                                hand_del_name_layout.setVisibility(View.GONE);
                                expected_depature_time_layout.setVisibility(View.GONE);
                                expected_arrival_time_layout.setVisibility(View.GONE);
                                consignment_number_ll.setVisibility(View.GONE);
                                lineareditbarcode.setVisibility(View.GONE);
                                lineareditbarcodebsv.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else if (mode_value_compare.equals("Hand Delivery")) {
                    name_edt_txt.setHint("Enter name of person");
                    GetScannerBarcode();
                    hand_del_name_layout.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.VISIBLE);

                    total_sample_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.VISIBLE);
                    btn_to_next_ll.setVisibility(View.VISIBLE);
                    ll_pouch.setVisibility(View.VISIBLE);
                    courier_name_layout.setVisibility(View.GONE);
                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.GONE);

                    lineareditbarcode.setVisibility(View.GONE);
                    lineareditbarcodebsv.setVisibility(View.GONE);

                    total_sample_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);


                } else if (mode_value_compare.equals("LME")) {
                    GetScannerBarcode();
                    total_sample_layout.setVisibility(View.VISIBLE);
                    consignment_name_layout.setVisibility(View.VISIBLE);
                    bsv_barcode_scanning_ll.setVisibility(View.GONE);
                    submit_ll.setVisibility(View.VISIBLE);
                    ll_pouch.setVisibility(View.VISIBLE);
                    dispatch_time_layout.setVisibility(View.GONE);
                    bus_spinner_name_layout.setVisibility(View.GONE);
                    bus_name_layout.setVisibility(View.GONE);
                    bus_number_layout.setVisibility(View.GONE);
                    courier_spinner_name_layout.setVisibility(View.GONE);
                    courier_name_layout.setVisibility(View.GONE);
                    expected_depature_time_layout.setVisibility(View.GONE);
                    expected_arrival_time_layout.setVisibility(View.GONE);
                    flight_name_layout.setVisibility(View.GONE);
                    flight_number_layout.setVisibility(View.GONE);
                    btn_to_next_ll.setVisibility(View.GONE);
                    hand_del_name_layout.setVisibility(View.GONE);
                    lineareditbarcode.setVisibility(View.GONE);
                    lineareditbarcodebsv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_to_next_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode_value_compare = mode_spinner.getSelectedItem().toString();
                if (flagtsp == 0) {
                    TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (mode_value_compare.equals("Select Mode")) {
                    Toast.makeText(mContext, ToastFile.slt_mode, Toast.LENGTH_SHORT).show();
                } else if (mode_value_compare.equals("Air Cargo")) {

                    String flight_name_to_pass = filght_spinner.getSelectedItem().toString();
                    String flight_number_to_pass = flight_name.getText().toString();
                    String getDeparture_time = expected_departure_time.getText().toString();
                    String getArrival_time = expected_arrival_time.getText().toString();
                    String getDispatchtime = dispatch_time.getText().toString();
                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        getDateDispatchTime = sdf.format(d_pass);
                        compare = sdf.parse(getDateDispatchTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (flight_name_to_pass.equals("Select Airlines")) {
                        TastyToast.makeText(mContext, "Select Airlines", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (flight_number_to_pass.equals("")) {
                        flight_name.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.flight_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDeparture_time.equals("")) {
                        expected_departure_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dept_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getArrival_time.equals("")) {
                        expected_arrival_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.arrival_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.requestFocus();
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.requestFocus();
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
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


                        Bundle args = new Bundle();
                        args.putString("mode", mode_value_compare);
                        args.putString("routine_code", tsp_su_code);
                        args.putString("flight_name", flight_name_to_pass);
                        args.putString("flight_number", flight_number_to_pass);
                        args.putString("departure_time", depature_time_to_pass);
                        args.putString("arrival_time", arrival_time_to_pass);
                        args.putString("dispatch_time", dispatch_time_to_pass);
                        args.putString("CourierName", flight_name_to_pass);
                        args.putString("bsv_barcode", "");
                        args.putString("total_sample", "");
                        args.putString("rpl_sample", "");
                        args.putString("cpl_sample", "");
                        args.putString("Consignment_barcode", "");

                        GlobalClass.mode = mode_value_compare;
                        GlobalClass.routine_code = tsp_su_code;
                        GlobalClass.flagtsp = flagtsp;
                        GlobalClass.flight_name = flight_name_to_pass;
                        GlobalClass.flight_number = flight_number_to_pass;
                        GlobalClass.departure_time = getDeparture_time;
                        GlobalClass.arrival_time = getArrival_time;
                        GlobalClass.dispatch_time = getDispatchtime;
                        GlobalClass.CourierName = "";
                        GlobalClass.bsv_barcode = "";
                        GlobalClass.total_sample = "";
                        GlobalClass.rpl_sample = "";
                        GlobalClass.cpl_sample = "";
                        GlobalClass.Consignment_barcode = "";

                        Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                        a2Fragment.setArguments(args);
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
                    }
                } else if (mode_value_compare.equals("Bus")) {
                    String bus_name = bus_spinner.getSelectedItem().toString();
                    String bus_name_to_pass = bus_spinner.getSelectedItem().toString();
                    String bus_number_to_pass = bus_number.getText().toString();
                    String getDispatchtime = dispatch_time.getText().toString();
                    String getDeparture_time = expected_departure_time.getText().toString();
                    String getArrival_time = expected_arrival_time.getText().toString();


                    if (bus_name_to_pass.equals("OTHERS")) {
                        get_edt_bus_name = bus_edt_txt.getText().toString();
                        bus_name = bus_edt_txt.getText().toString();
                    } else {
                        bus_name_to_pass = bus_spinner.getSelectedItem().toString();
                    }

                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        getDateDispatchTime = sdf.format(d_pass);
                        compare = sdf.parse(getDateDispatchTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (flagtsp == 0) {
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bus_name.equals("Select Bus name")) {
                        TastyToast.makeText(mContext, "Select Bus Name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bus_number_to_pass.equals("")) {
                        bus_number.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.bus_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.requestFocus();
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.requestFocus();
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (getDeparture_time.equals("")) {
                        expected_departure_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dept_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getArrival_time.equals("")) {
                        expected_arrival_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.arrival_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (bus_name_to_pass.equalsIgnoreCase("OTHERS")) {
                        String getBusName = bus_edt_txt.getText().toString();
                        if (getBusName.equals("")) {
                            bus_edt_txt.requestFocus();
                            Toast.makeText(mContext, ToastFile.pls_ent_bus_name, Toast.LENGTH_SHORT).show();
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

                            Bundle args = new Bundle();
                            args.putString("mode", mode_value_compare);
                            args.putString("routine_code", tsp_su_code);
                            args.putString("flight_name", bus_name_to_pass);
                            args.putString("flight_number", bus_number_to_pass);
                            args.putString("departure_time", depature_time_to_pass);
                            args.putString("arrival_time", arrival_time_to_pass);
                            args.putString("dispatch_time", dispatch_time_to_pass);
                            args.putString("CourierName", bus_name);
                            args.putString("bsv_barcode", "");
                            args.putString("total_sample", "");
                            args.putString("rpl_sample", "");
                            args.putString("cpl_sample", "");
                            args.putString("Consignment_barcode", "");

                            GlobalClass.mode = mode_value_compare;
                            GlobalClass.routine_code = tsp_su_code;
                            GlobalClass.flagtsp = flagtsp;
                            GlobalClass.bus_name_to_pass = bus_name_to_pass;
                            GlobalClass.get_edt_bus_name = get_edt_bus_name;
                            GlobalClass.bus_number = bus_number_to_pass;

                            GlobalClass.departure_time = getDeparture_time;
                            GlobalClass.arrival_time = getArrival_time;
                            GlobalClass.dispatch_time = getDispatchtime;
                            GlobalClass.CourierName = "";
                            GlobalClass.bsv_barcode = "";
                            GlobalClass.total_sample = "";
                            GlobalClass.rpl_sample = "";
                            GlobalClass.cpl_sample = "";
                            GlobalClass.Consignment_barcode = "";


                            /*Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                            a2Fragment.setArguments(args);
                            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();*/
                            Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                            a2Fragment.setArguments(args);
                            mContext.getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_mainLayout,a2Fragment,a2Fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        }
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


                        Bundle args = new Bundle();
                        args.putString("mode", mode_value_compare);
                        args.putString("routine_code", tsp_su_code);
                        args.putString("flight_name", bus_name);
                        args.putString("flight_number", bus_number_to_pass);
                        args.putString("departure_time", depature_time_to_pass);
                        args.putString("arrival_time", arrival_time_to_pass);
                        args.putString("dispatch_time", dispatch_time_to_pass);
                        args.putString("CourierName", bus_name);
                        args.putString("bsv_barcode", "");
                        args.putString("total_sample", "");
                        args.putString("rpl_sample", "");
                        args.putString("cpl_sample", "");
                        args.putString("Consignment_barcode", "");

                        GlobalClass.mode = mode_value_compare;
                        GlobalClass.routine_code = tsp_su_code;
                        GlobalClass.flagtsp = flagtsp;
                        GlobalClass.bus_name_to_pass = bus_name_to_pass;
                        GlobalClass.get_edt_bus_name = get_edt_bus_name;
                        GlobalClass.bus_number = bus_number_to_pass;

                        GlobalClass.departure_time = getDeparture_time;
                        GlobalClass.arrival_time = getArrival_time;
                        GlobalClass.dispatch_time = getDispatchtime;
                        GlobalClass.CourierName = "";
                        GlobalClass.bsv_barcode = "";
                        GlobalClass.total_sample = "";
                        GlobalClass.rpl_sample = "";
                        GlobalClass.cpl_sample = "";
                        GlobalClass.Consignment_barcode = "";


                        /*Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                        a2Fragment.setArguments(args);
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();*/
                        Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                        a2Fragment.setArguments(args);
                        mContext.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_mainLayout,a2Fragment,a2Fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    }
                } else if (mode_value_compare.equals("Courier")) {
                    courier_name = courier_spinner.getSelectedItem().toString();

                    if (courier_name.equals("OTHERS")) {
                        getCourier_name = name_edt_txt.getText().toString();
                        courier_name_to_pass = getCourier_name;
                    }

                    String getDispatchtime = dispatch_time.getText().toString();
                    bsv_edt_txt = bsv_barcode_scanning.getText().toString();
                    consignment_barcode = consignment_barcd_btn.getText().toString();
                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        getDateDispatchTime = sdf.format(d_pass);
                        compare = sdf.parse(getDateDispatchTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (flagtsp == 0) {
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (courier_name.equals("Select Courier Name")) {
                        TastyToast.makeText(mContext, ToastFile.pls_slt_courier_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dispt_tm_condition, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.after(compare)) {
                        dispatch_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dispt_tm_condition, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.scan_consignment_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }/* else if (bsv_edt_txt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/ else if (courier_name.equals("OTHERS")) {
                        courier_name_to_pass = name_edt_txt.getText().toString();
                        if (courier_name_to_pass.equals("")) {
                            TastyToast.makeText(mContext, ToastFile.ent_courier_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            name_edt_txt.requestFocus();
                        } else {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                            Date myDate = null;
                            try {
                                myDate = dateFormat.parse(getDispatchtime);
                                SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                getDateDispatchTime = sdfdata.format(myDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (flagtsp == 1) {
                                tsp_su_code = "";
                            } else if (flagtsp == 2) {
                                tsp_su_code = source_code_pass.getText().toString();
                            }


                            Bundle args = new Bundle();
                            args.putString("mode", mode_value_compare);
                            args.putString("routine_code", tsp_su_code);
                            args.putString("flight_name", "");
                            args.putString("flight_number", "");
                            args.putString("departure_time", "");
                            args.putString("arrival_time", "");
                            args.putString("dispatch_time", getDateDispatchTime);
                            args.putString("CourierName", courier_name_to_pass);
                            args.putString("bsv_barcode", bsv_edt_txt);
                            args.putString("total_sample", total_count);
                            args.putString("rpl_sample", rpl_count);
                            args.putString("cpl_sample", cpl_count);
                            args.putString("Consignment_barcode", consignment_barcode);


                            GlobalClass.mode = mode_value_compare;
                            GlobalClass.routine_code = tsp_su_code;
                            GlobalClass.flight_name = "";
                            GlobalClass.flagtsp = flagtsp;
                            GlobalClass.flight_number = "";
                            GlobalClass.departure_time = "";
                            GlobalClass.arrival_time = "";
                            GlobalClass.dispatch_time = getDispatchtime;
                            GlobalClass.CourierName = courier_name;
                            GlobalClass.getCourier_name = courier_name_to_pass;
                            GlobalClass.bsv_barcode = bsv_edt_txt;
                            GlobalClass.total_sample = total_count;
                            GlobalClass.rpl_sample = rpl_count;
                            GlobalClass.cpl_sample = cpl_count;
                            GlobalClass.Consignment_barcode = consignment_barcode;


                            Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                            a2Fragment.setArguments(args);
                            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
                        }
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                        Date myDate = null;
                        try {
                            myDate = dateFormat.parse(getDispatchtime);
                            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            getDateDispatchTime = sdfdata.format(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (flagtsp == 1) {
                            tsp_su_code = "";
                        } else if (flagtsp == 2) {
                            tsp_su_code = source_code_pass.getText().toString();
                        }

                        Next_Consignment_Entry fragment = new Next_Consignment_Entry();

                        Bundle args = new Bundle();
                        args.putString("mode", mode_value_compare);
                        args.putString("routine_code", tsp_su_code);
                        args.putString("flight_name", "");
                        args.putString("flight_number", "");
                        args.putString("departure_time", "");
                        args.putString("arrival_time", "");
                        args.putString("dispatch_time", getDateDispatchTime);
                        args.putString("CourierName", courier_name);
                        args.putString("bsv_barcode", bsv_edt_txt);
                        args.putString("total_sample", total_count);
                        args.putString("rpl_sample", rpl_count);
                        args.putString("cpl_sample", cpl_count);
                        args.putString("Consignment_barcode", consignment_barcode);


                        GlobalClass.mode = mode_value_compare;
                        GlobalClass.routine_code = tsp_su_code;
                        GlobalClass.flight_name = "";
                        GlobalClass.flagtsp = flagtsp;
                        GlobalClass.flight_number = "";
                        GlobalClass.departure_time = "";
                        GlobalClass.arrival_time = "";
                        GlobalClass.dispatch_time = getDispatchtime;
                        GlobalClass.CourierName = courier_name;
                        GlobalClass.getCourier_name = getCourier_name;
                        GlobalClass.bsv_barcode = bsv_edt_txt;
                        GlobalClass.total_sample = total_count;
                        GlobalClass.rpl_sample = rpl_count;
                        GlobalClass.cpl_sample = cpl_count;
                        GlobalClass.Consignment_barcode = consignment_barcode;


                        fragment.setArguments(args);
                        mContext.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    }

                } else if (mode_value_compare.equals("Hand Delivery")) {
                    String nameof_person_hnd = hand_name_edt_txt.getText().toString();
                    String getDispatchtime = dispatch_time.getText().toString();
                    bsv_edt_txt = bsv_barcode_scanning.getText().toString();
                    consignment_barcode = consignment_barcd_btn.getText().toString();

                    Date d_convert = null;
                    Date compare = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
                        d_convert = sdf.parse(getDispatchtime);
                        Date d_pass = new Date();
                        getDateDispatchTime = sdf.format(d_pass);
                        compare = sdf.parse(getDateDispatchTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (flagtsp == 0) {
                        TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (nameof_person_hnd.equals("")) {
                        name_edt_txt.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.per_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getDispatchtime.equals("")) {
                        dispatch_time.requestFocus();
                        TastyToast.makeText(mContext, ToastFile.dispt_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (d_convert.equals(compare)) {
                        dispatch_time.requestFocus();
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (d_convert.after(compare)) {
                        dispatch_time.requestFocus();
                        Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
                    } else if (consignment_barcode.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }/* else if (bsv_edt_txt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/ else {
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


                        Bundle args = new Bundle();
                        args.putString("mode", mode_value_compare);
                        args.putString("routine_code", tsp_su_code);
                        args.putString("flight_name", "");
                        args.putString("flight_number", "");
                        args.putString("departure_time", "");
                        args.putString("arrival_time", "");
                        args.putString("dispatch_time", dispatch_time_to_pass);
                        args.putString("CourierName", nameof_person_hnd);
                        args.putString("bsv_barcode", bsv_edt_txt);
                        args.putString("total_sample", total_count);
                        args.putString("rpl_sample", rpl_count);
                        args.putString("cpl_sample", cpl_count);
                        args.putString("Consignment_barcode", consignment_barcode);

                        GlobalClass.mode = mode_value_compare;
                        GlobalClass.routine_code = tsp_su_code;
                        GlobalClass.flagtsp = flagtsp;
                        GlobalClass.flight_name = "";
                        GlobalClass.flight_number = "";
                        GlobalClass.departure_time = "";
                        GlobalClass.arrival_time = "";
                        GlobalClass.dispatch_time = getDispatchtime;
                        GlobalClass.getCourier_name = nameof_person_hnd;
                        GlobalClass.bsv_barcode = bsv_edt_txt;
                        GlobalClass.total_sample = total_count;
                        GlobalClass.rpl_sample = rpl_count;
                        GlobalClass.cpl_sample = cpl_count;
                        GlobalClass.Consignment_barcode = consignment_barcode;

                        /*Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                        a2Fragment.setArguments(args);
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();*/
                        Next_Consignment_Entry a2Fragment = new Next_Consignment_Entry();
                        a2Fragment.setArguments(args);
                        mContext.getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_mainLayout,a2Fragment,a2Fragment.getClass().getSimpleName()).addToBackStack(null).commit();


                    }
                }

            }
        });


        Submit_consignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode_value_compare = mode_spinner.getSelectedItem().toString();

                consignment_barcode = consignment_barcd_btn.getText().toString();
                bsv_edt_txt = bsv_barcode_scanning.getText().toString();


                if (mode_value_compare.equals("Select Mode")) {
                    TastyToast.makeText(mContext, ToastFile.slt_mode, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (flagtsp == 0) {
                    TastyToast.makeText(mContext, ToastFile.routine_cd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else if (consignment_barcode.equals("")) {
                    TastyToast.makeText(mContext, ToastFile.scan_consignment_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } /*else if (bsv_edt_txt.equals("")) {
                    TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }*/ else {
                    doTheConsignmentforLME();
                }
            }
        });


        // Inflate the layout for this fragment
        return viewMain;
    }


    private void checkConsgnmentFortheDay() {
        PostQueueForConsignment = GlobalClass.setVolleyReq(getContext());

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, Api.consignmentperday + user + "/ConfirmConsignmentEntry", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String finalJson = response.toString();
                Log.e(TAG, "onResponse: RESPONSE " + response);
                JSONObject parentObjectOtp = null;
                try {
                    parentObjectOtp = new JSONObject(finalJson);
                    String getCount = parentObjectOtp.getString("getCount");
                    String response1 = parentObjectOtp.getString("Response");
                    if (getCount.equals("0")) {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }
                        Consignment_fragment fragment = new Consignment_fragment();
                        mContext.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_mainLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    } else {
                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                            barProgressDialog.dismiss();
                        }
                        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Consignment Status")
                                .setContentText("Consignment entry already done for the day")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {

                    //Log.v(TAG,error);
                }
            }
        });
        PostQueueForConsignment.add(jsonObjectRequest1);
        Log.e(TAG, "checkConsgnmentFortheDay: URL " + jsonObjectRequest1);

    }


    private void doTheConsignmentforLME() {
        if (flagtsp == 1) {
            tsp_su_code = "";
        } else if (flagtsp == 2) {
            tsp_su_code = source_code_pass.getText().toString();
        }


        barProgressDialog.show();


        PostQueAirCargo = GlobalClass.setVolleyReq(mContext);
       /* JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("API_KEY", api_key);
            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode);
            jsonObjectOtp.put("ThroughTSPCode", tsp_su_code);
            jsonObjectOtp.put("DispatchTime", "");
            jsonObjectOtp.put("ConsignmentNo", "");
            jsonObjectOtp.put("TransitTime", "");
            jsonObjectOtp.put("Mode", mode_value_compare);
            jsonObjectOtp.put("SampleFromCPL", cpl_count);
            jsonObjectOtp.put("SampleFromRPL", rpl_count);
            jsonObjectOtp.put("PackagingDetails", "");
            jsonObjectOtp.put("FlightName", "");
            jsonObjectOtp.put("FlightNo", "");
            jsonObjectOtp.put("DepTime", "");
            jsonObjectOtp.put("ArrTime", "");
            jsonObjectOtp.put("BSVBarcode", bsv_edt_txt);
            jsonObjectOtp.put("Remarks", "");
            jsonObjectOtp.put("TotalSamples", total_count);
            jsonObjectOtp.put("ConsignmentTemperature", "");
            jsonObjectOtp.put("CourierName", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        ConsignmentRequestModel consignmentRequestModel = new ConsignmentRequestModel();
        consignmentRequestModel.setAPI_KEY(api_key);
        consignmentRequestModel.setConsignmen_Barcode(consignment_barcode);
        consignmentRequestModel.setThroughTSPCode(tsp_su_code);
        consignmentRequestModel.setDispatchTime("");
        consignmentRequestModel.setConsignmentNo("");
        consignmentRequestModel.setTransitTime("");
        consignmentRequestModel.setMode(mode_value_compare);
        consignmentRequestModel.setSampleFromCPL(cpl_count);
        consignmentRequestModel.setSampleFromRPL(rpl_count);
        consignmentRequestModel.setPackagingDetails("");
        consignmentRequestModel.setFlightName("");
        consignmentRequestModel.setFlightNo("");
        consignmentRequestModel.setDepTime("");
        consignmentRequestModel.setArrTime("");
        consignmentRequestModel.setBSVBarcode("");
        consignmentRequestModel.setRemarks("");
        consignmentRequestModel.setTotalSamples(total_count);
        consignmentRequestModel.setConsignmentTemperature("");
        consignmentRequestModel.setCourierName("");
        consignmentRequestModel.setPouchCodeDtlLists(getArrayList());


        JSONObject jsonObj = null;
        try {
            jsonObj = geneRateReqSSLkeyObject(consignmentRequestModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                try {
                    String finalJson = response.toString();
                    Log.e(TAG, "onResponse: " + response);
                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                    Response = parentObjectOtp.getString("Response");
                    message = parentObjectOtp.getString("Message");
                    ResId = parentObjectOtp.getString("ResId");
                    if (Response.equalsIgnoreCase("Success")) {
                        TastyToast.makeText(mContext, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Wind_up_fragment a2Fragment = new Wind_up_fragment();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();
                    } else if (Response.equalsIgnoreCase(small_invalidApikey)) {
                        GlobalClass.redirectToLogin(mContext);
                    } else {
                        TastyToast.makeText(mContext, "" + message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
            }
        });
        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PostQueAirCargo.add(jsonObjectRequest1);
        Log.e(TAG, "doTheConsignmentforLME: URL" + jsonObjectRequest1);
        Log.e(TAG, "doTheConsignmentforLME: object" + jsonObj);
    }

    private JSONObject geneRateReqSSLkeyObject(ConsignmentRequestModel consignmentRequestModel) throws JSONException {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(consignmentRequestModel);
        JSONObject jsonObj = new JSONObject(json);
        return jsonObj;
    }

    private ArrayList<ConsignmentRequestModel.PouchReqsDTO> getArrayList() {
        ArrayList<ConsignmentRequestModel.PouchReqsDTO> ent = new ArrayList<>();
        ConsignmentRequestModel.PouchReqsDTO ewfnw = null;
        if (setBarcodePouch != null) {
            for (int i = 0; i < setBarcodePouch.size(); i++) {
                ewfnw = new ConsignmentRequestModel.PouchReqsDTO();
                ewfnw.setBarcode(setBarcodePouch.get(i).getBarcode());
                ent.add(ewfnw);
            }
        }

        return ent;
    }

    private ArrayList<BarcodeResponseModel.BarcodeDTO> SelectUnselect(boolean b) {
        for (int i = 0; i < barcodelist.size(); i++) {
            barcodelist.get(i).setSelected(b);
        }
        return barcodelist;
    }

    private void OpenDialog() {
        pocuh_dialog = new Dialog(getActivity());
        pocuh_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pocuh_dialog.setCancelable(false);
        pocuh_dialog.setContentView(R.layout.add_pounch_dialog);

        LinearLayout enter_ll_unselected = pocuh_dialog.findViewById(R.id.enter_ll_unselected);
        LinearLayout unchecked_entered_ll = pocuh_dialog.findViewById(R.id.unchecked_entered_ll);
        final LinearLayout lineareditbarcode = pocuh_dialog.findViewById(R.id.lineareditbarcode);
        final LinearLayout consignment_name_layout = pocuh_dialog.findViewById(R.id.consignment_name_layout);
        final TextView enter = pocuh_dialog.findViewById(R.id.enter);
        final EditText edt_search = pocuh_dialog.findViewById(R.id.edt_search);
        final TextView enetered = pocuh_dialog.findViewById(R.id.enetered);
        final ImageView enter_arrow_enter = pocuh_dialog.findViewById(R.id.enter_arrow_enter);
        final ImageView enter_arrow_entered = pocuh_dialog.findViewById(R.id.enter_arrow_entered);
        final ImageView iv_cancel = pocuh_dialog.findViewById(R.id.iv_cancel);
        final ImageView setback = pocuh_dialog.findViewById(R.id.setback);
        final ImageView img_scan_pouch_barcode = pocuh_dialog.findViewById(R.id.img_scan_pouch_barcode);
        final Button btn_submit = pocuh_dialog.findViewById(R.id.btn_submit);
        btn_pouch_barcd = pocuh_dialog.findViewById(R.id.btn_pouch_barcd);
        final EditText enter_barcode = pocuh_dialog.findViewById(R.id.enter_barcode);
        final EditText reenter = pocuh_dialog.findViewById(R.id.reenter);
        recy_barcode = pocuh_dialog.findViewById(R.id.recycler_all_test);
        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);

        final Button btn_selectall = pocuh_dialog.findViewById(R.id.btn_selectall);
        btn_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_selectall.getText().toString().equalsIgnoreCase(getActivity().getResources().getString(R.string.select_all))) {
                    btn_selectall.setText(getActivity().getResources().getString(R.string.remove_all));
                    SetAdapter(SelectUnselect(true));
                } else {
                    btn_selectall.setText(getActivity().getResources().getString(R.string.select_all));
                    SetAdapter(SelectUnselect(false));
                }
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filter = s.toString().toLowerCase();
                if (filter.length() > 0) {
                    ArrayList<BarcodeResponseModel.BarcodeDTO> filterlist = new ArrayList<>();
                    if (GlobalClass.CheckArrayList(barcodelist)) {
                        for (int i = 0; i < barcodelist.size(); i++) {
                            if (barcodelist.get(i).getBarcode().toLowerCase().contains(filter)) {
                                filterlist.add(barcodelist.get(i));
                            }
                        }
                    }
                    SetAdapter(filterlist);
                } else {
                    SetAdapter(totalbarcodelist);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        GetBarcodeListController getBarcodeListController = new GetBarcodeListController(this);
        getBarcodeListController.getBarcodedetails();


        enter_barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(mContext,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcode.setText(enteredString.substring(1));
                    } else {
                        enter_barcode.setText("");
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

        reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(mContext,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        reenter.setText(enteredString.substring(1));
                    } else {
                        reenter.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                int gelength = enter_barcode.getText().length();
                if (enteredString.length() == gelength) {
                    String getPreviouseText = enter_barcode.getText().toString();
                    String currentText_nxt = reenter.getText().toString();
                    if (getPreviouseText.equals(currentText_nxt)) {
                        currentText_nxt = reenter.getText().toString();

                        lineareditbarcode.setVisibility(View.GONE);
                        consignment_name_layout.setVisibility(View.VISIBLE);
                        btn_pouch_barcd.setText(currentText_nxt);

                    } else {
                        reenter.setText("");
                        Toast.makeText(mContext, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pocuh_dialog.dismiss();
            }
        });

        btn_pouch_barcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode.setVisibility(View.VISIBLE);
                consignment_name_layout.setVisibility(View.GONE);
            }
        });


        img_scan_pouch_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 3;
                scanIntegrator.initiateScan();
            }
        });

        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode.setVisibility(View.GONE);
                consignment_name_layout.setVisibility(View.VISIBLE);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selectnone = false;
                for (int i = 0; i < totalbarcodelist.size(); i++) {
                    if (totalbarcodelist.get(i).isSelected()) {
                        selectnone = true;
                        break;
                    }
                }
                if (!selectnone) {
                    Toast.makeText(getActivity(), "No Barcodes Selected", Toast.LENGTH_SHORT).show();
                } else if (btn_pouch_barcd.getText().toString().equalsIgnoreCase(getActivity().getString(R.string.ssp_barcode))) {
                    Toast.makeText(getActivity(), "Scan SSP Barcode", Toast.LENGTH_SHORT).show();
                } else {
                    PostBarcodeModel postBarcodeModel = new PostBarcodeModel();
                    postBarcodeModel.setDacCode(user);
                    postBarcodeModel.setPouchCode(btn_pouch_barcd.getText().toString().trim());
                    ArrayList<PostBarcodeModel.ConsignmentBarcodesDTO> consignmentBarcodesDTOS = new ArrayList<>();
                    for (int i = 0; i < totalbarcodelist.size(); i++) {
                        if (totalbarcodelist.get(i).isSelected()) {
                            PostBarcodeModel.ConsignmentBarcodesDTO consignmentBarcodesDTO = new PostBarcodeModel.ConsignmentBarcodesDTO();
                            consignmentBarcodesDTO.setBarcode("" + totalbarcodelist.get(i).getBarcode());
                            consignmentBarcodesDTOS.add(consignmentBarcodesDTO);
                        }

                    }
                    postBarcodeModel.setConsignmentBarcodes(consignmentBarcodesDTOS);
                    PostBarcodeController postBarcodeController = new PostBarcodeController(Consignment_fragment.this);
                    postBarcodeController.CallAPI(postBarcodeModel);
                }
            }
        });

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);

            }
        });


        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
            }
        });


        int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.99);
        pocuh_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pocuh_dialog.getWindow().setLayout(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        pocuh_dialog.show();


    }

    private void SetAdapter(ArrayList<BarcodeResponseModel.BarcodeDTO> barcode) {
        BarcodeAdapter barcodeAdapter = new BarcodeAdapter(getActivity(), barcode);
        barcodeAdapter.setOnItemClickListener(new BarcodeAdapter.OnClickListener() {
            @Override
            public void onchecked(ArrayList<BarcodeResponseModel.BarcodeDTO> barcodeDTOS) {
                barcodelist = barcodeDTOS;
            }
        });
        recy_barcode.setAdapter(barcodeAdapter);
        barcodeAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "123: ");

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                getBarcodeDetails = result.getContents();
                if (mode == 1) {
                    passBarcodeData(getBarcodeDetails);
                } else if (mode == 2) {
                    bsv_barcode_scanning.setText(getBarcodeDetails);
                    enter_barcodebsv.setText(getBarcodeDetails);
                    reenterbsv.setText(getBarcodeDetails);
                } else if (mode == 3) {
                    btn_pouch_barcd.setText(getBarcodeDetails);
                }
            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(final String getBarcodeDetails) {
        barcodeDetails = GlobalClass.setVolleyReq(mContext);
        StringRequest jsonObjectRequestPop = new StringRequest(Request.Method.GET, Api.barcode_Check + getBarcodeDetails + "/CheckConsignmentBarcode"
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response);
                Log.v(TAG, "barcode respponse" + response);

                if (response.equals("\"Valid\"")) {
                    consignment_barcd_btn.setText(getBarcodeDetails);
                    enter_barcode.setText(getBarcodeDetails);
                    reenter.setText(getBarcodeDetails);

                } else {
                    consignment_barcd_btn.setText("");
                    Toast.makeText(mContext, ToastFile.consign_brcd_name + response, Toast.LENGTH_SHORT).show();
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
        barcodeDetails.add(jsonObjectRequestPop);
        GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
        Log.e(TAG, "passBarcodeData: url" + jsonObjectRequestPop);
    }


    private void fetchRplCplCount() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String passDate = sdf.format(d);

        requestQueue = GlobalClass.setVolleyReq(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + user + "/" + passDate + "/getsamplecount", new com.android.volley.Response.Listener<JSONObject>() {
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
                    total_consignment_edt_txt.setText("Total Sample: " + total_count);
                    rpl_edt_txt.setText("RPL: " + rpl_count);
                    cpl_edt_txt.setText("CPL: " + cpl_count);
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
            timepickerdialog = new TimePickerDialog(mContext, R.style.DialogTheme,
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
                            total_time = GlobalClass.convertDate(total_time, "dd-MM-yyyy hh:mm aa");

                            dep_date_time = GlobalClass.dateFromString(total_time, new SimpleDateFormat("dd-MM-yyyy hh:mm aa"));

                            if (arr_date_time != null) {
                                if (arr_date_time.equals(dep_date_time) && arr_date_time.before(dep_date_time))
                                    ;
                                expected_arrival_time.setText("");
                                TastyToast.makeText(getActivity(), "Arrival time should be greater than dispatch time and departure time!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } else {
                                getTimetoPass = GlobalClass.changetimeformate(getTimetoPass);
                                expected_departure_time.setText(getDateToShow + " " + getTimetoPass);
                                expected_departure_time.setError(null);
                            }


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
            timepickerdialog = new TimePickerDialog(mContext, R.style.DialogTheme,
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
                            total_time = GlobalClass.convertDate(total_time, "dd-MM-yyyy hh:mm aa");

                            arr_date_time = GlobalClass.dateFromString(total_time, new SimpleDateFormat("dd-MM-yyyy hh:mm aa"));
                            if (arr_date_time.after(dispatch_date_time) && arr_date_time.after(dep_date_time) && !arr_date_time.equals(dispatch_date_time) && !arr_date_time.equals(dep_date_time)) {
                                getTimetoPass = GlobalClass.changetimeformate(getTimetoPass);
                                expected_arrival_time.setText(getDateToShow + " " + getTimetoPass);
                            } else {
                                expected_arrival_time.setHint("Expected Time Arrival");
                                TastyToast.makeText(getActivity(), "Arrival time should be greater than dispatch time and departure time!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                            }


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
            timepickerdialog = new TimePickerDialog(mContext, R.style.DialogTheme,
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
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                            try {
                                d_convert = sdf.parse(total_time);
                                Date d_pass = new Date();
                                getDateDispatchTime = sdf.format(d_pass);
                                compare = sdf.parse(getDateDispatchTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            if (d_convert != null && compare != null) {
                                if (d_convert.equals(compare) && compare != null) {
                                    dispatch_time.setText("");
                                    Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                                dispatch_time.setError(ToastFile.dispt_tm_condition);
                                } else if (d_convert.after(new Date())) {
                                    dispatch_time.setText("");
                                    Toast.makeText(mContext, ToastFile.dispt_tm_condition, Toast.LENGTH_SHORT).show();
//                                dispatch_time.setError(ToastFile.dispt_tm_condition);
                                } else {
                                    total_time = getDateToShow + " " + getTimetoPass;
                                    total_time = GlobalClass.convertDate(total_time, "dd-MM-yyyy hh:mm aa");

                                    dispatch_date_time = GlobalClass.dateFromString(total_time, new SimpleDateFormat("dd-MM-yyyy hh:mm aa"));

                                    if (arr_date_time != null) {
                                        if (arr_date_time.after(dispatch_date_time) && !arr_date_time.equals(dispatch_date_time)) {
                                            getTimetoPass = GlobalClass.changetimeformate(getTimetoPass);
                                            dispatch_time.setText(getDateToShow + " " + getTimetoPass);
                                        } else {
                                            expected_arrival_time.setText("");
                                            TastyToast.makeText(getActivity(), "Dispatch time should be greater than arrival time !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                        }
                                    } else {
                                        getTimetoPass = GlobalClass.changetimeformate(getTimetoPass);
                                        dispatch_time.setText(getDateToShow + " " + getTimetoPass);
                                        dispatch_time.setError(null);
                                    }
                                }
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


    public void getBarcodeDetails(BarcodeResponseModel body) {
        try {
            if (!GlobalClass.isNull(body.getRespID()) && body.getRespID().equalsIgnoreCase("RES0000")) {
                if (GlobalClass.CheckArrayList(body.getBarcode())) {
                    barcodelist = body.getBarcode();
                    totalbarcodelist = body.getBarcode();
                    SetAdapter(body.getBarcode());
                } else {
                    Global.showCustomToast(getActivity(), "No Records Found..");
                }
            } else {
                Global.showCustomToast(getActivity(), "" + body.getResponse());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getBarcodeResponse(PostBarcodeResponseModel responseModel) {
        try {
            if (!GlobalClass.isNull(responseModel.getRespID()) && responseModel.getRespID().equalsIgnoreCase("RES0000")) {
                Global.showCustomToast(getActivity(), "" + responseModel.getResponse());
                if (pocuh_dialog != null && pocuh_dialog.isShowing()) {
                    pocuh_dialog.dismiss();
                    GetScannerBarcode();
                }
            } else {
                Global.showCustomToast(getActivity(), "" + responseModel.getResponse());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void GetScannerBarcode() {
        try {
            setBarcodePouch = null;
            if (connectionDetector.isConnectingToInternet()) {
                GetScannedBarcodeList getScannedBarcodeList = new GetScannedBarcodeList(this);
                getScannedBarcodeList.getScanneddetails();
            } else {
                GlobalClass.toastyError(getActivity(), MessageConstants.CHECK_INTERNET_CONN, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getScanBarcodeDetails(ScanBarcodeResponseModel body) {
        try {
            if (!GlobalClass.isNull(body.getRespID()) && body.getRespID().equalsIgnoreCase("RES0000")) {
                Global.showCustomToast(getActivity(), "" + body.getResponse());
                if (GlobalClass.CheckArrayList(body.getBarcode())) {
                    setBarcodePouch = body.getBarcode();
                    ScannedBarcodeAdapter scannedBarcodeAdapter = new ScannedBarcodeAdapter(getActivity(), body.getBarcode());
                    recy_scannedbarcode.setAdapter(scannedBarcodeAdapter);
                    scannedBarcodeAdapter.notifyDataSetChanged();
                }

            } else {
                Global.showCustomToast(getActivity(), "" + body.getResponse());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
