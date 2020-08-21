package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.AsteriskPasswordTransformationMethod;
import com.example.e5322.thyrosoft.Controller.CongsigmentBarcode_Controller;
import com.example.e5322.thyrosoft.Controller.ConsigmentEntry_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.SampleCountController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.small_invalidApikey;


public class Next_Consignment_Entry extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ManagingTabsActivity mContext;
    String user, passwrd, access, api_key;
    View viewMain;
    TextView rpl_edt_txt_nxt, total_consignment_edt_txt_nxt, cpl_edt_txt_nxt;
    String modedata_nxt, routine_code_nxt, flight_name_nxt, flight_number_nxt, departure_time_nxt, arrival_time_nxt, dispatch_time_nxt, CourierName_nxt, bsv_barcode_number_nxt, total_sample_nxt, rpl_sample_nxt,
            cpl_sample_nxt;
    int mode_nxt = 0;
    IntentIntegrator scanIntegrator_nxt;
    ArrayList<String> setSpinnerTRansitTime_nxt;
    Button consignment_barcd_btn_nxt, bsv_barcode_scanning_nxt;
    ImageView img_edt_nxt, img_edt_bsv_nxt, setbackbsv_nxt, setback_nxt, img_scan_consignment_barcode, img_scan_bsv_barcode;
    EditText enter_barcode_nxt, reenter_nxt, enter_barcodebsv_nxt, reenterbsv_nxt;
    LinearLayout consignment_number_layout_nxt, consignment_name_layout_nxt, packaging_spinner_layout_nxt, consignment_bar_layout_nxt, expected_spinner_transit_time_layout_nxt,
            temperature_spinner_layout_nxt, total_sample_layout_nxt, bsv_bar_layout_nxt, btn_to_pre_ll_nxt, bsv_barcode_scanning_ll_nxt, lineareditbarcode_nxt, lineareditbarcodebsv_nxt;
    EditText consignment_edt_txt_nxt;
    Spinner packaging_dtl_edt_txt_nxt, transit_time_spinner_nxt, consignment_temp_spinner_nxt;
    Button consignment_br_txt_nxt, btn_submit_nxt, bsv_barcode_nxt;
    boolean flag_nxt = false;
    ProgressDialog barProgressDialog_nxt;
    TextView enetered_nxt, enterwindup_nxt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences prefs;
    private RequestQueue requestQueue_nxt;
    private String cpl_count_nxt, response1_nxt, rpl_count_nxt, total_count_nxt;
    private OnFragmentInteractionListener mListener;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String getBarcodeDetails_nxt;
    private RequestQueue barcodeDetails_nxt;
    private String packaging_dtl_nxt;
    private String temp_consignment_nxt;
    private String expt_transit_tm_nxt;
    private String consignment_number_nxt;
    private String consignment_barcode_nxt;
    private String bsv_barcode_num_nxt;
    private RequestQueue PostQue_nxt;
    private String ResponseData_nxt, message_nxt, ResId_nxt;
    private String currentText_nxt;
    Activity mActivity;
    ConnectionDetector cd;
    private String search_barcode;
    private String getBarcodeDetails;

    public Next_Consignment_Entry() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Next_Consignment_Entry newInstance(String param1, String param2) {
        Next_Consignment_Entry fragment = new Next_Consignment_Entry();
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

        mActivity = getActivity();
        cd = new ConnectionDetector(mActivity);

        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        modedata_nxt = getArguments().getString("mode");
        routine_code_nxt = getArguments().getString("routine_code");
        flight_name_nxt = getArguments().getString("flight_name");
        flight_number_nxt = getArguments().getString("flight_number");
        departure_time_nxt = getArguments().getString("departure_time");
        arrival_time_nxt = getArguments().getString("arrival_time");
        dispatch_time_nxt = getArguments().getString("dispatch_time");
        CourierName_nxt = getArguments().getString("CourierName");
        bsv_barcode_number_nxt = getArguments().getString("bsv_barcode");
        total_sample_nxt = getArguments().getString("total_sample");
        rpl_sample_nxt = getArguments().getString("rpl_sample");
        cpl_sample_nxt = getArguments().getString("cpl_sample");
        consignment_barcode_nxt = getArguments().getString("Consignment_barcode");
        viewMain = (View) inflater.inflate(R.layout.fragment_next__consignment__entry, container, false);


        initViews();
        initListner();

        setSpinnerTRansitTime_nxt = new ArrayList<>();
        setSpinnerTRansitTime_nxt.add("Select expected transit time");
        setSpinnerTRansitTime_nxt.add("<6 hr");
        setSpinnerTRansitTime_nxt.add("6hr-8hr");
        setSpinnerTRansitTime_nxt.add("8hr-12hr");
        setSpinnerTRansitTime_nxt.add("12hr-24hr");
        setSpinnerTRansitTime_nxt.add("24hr-48hr");

        ArrayAdapter packaging_dtl_edt_txt_values = ArrayAdapter.createFromResource(mContext, R.array.packaging_dtl_edt_txt_values, R.layout.spinner_white_prop);
        packaging_dtl_edt_txt_nxt.setAdapter(packaging_dtl_edt_txt_values);

        ArrayAdapter transit_time_spinner_values = new ArrayAdapter(mContext, R.layout.spinner_white_prop, setSpinnerTRansitTime_nxt);
        transit_time_spinner_nxt.setAdapter(transit_time_spinner_values);

        ArrayAdapter consignment_temp_spinner_values = ArrayAdapter.createFromResource(mContext, R.array.consignment_temp_spinner_values, R.layout.spinner_white_prop);
        consignment_temp_spinner_nxt.setAdapter(consignment_temp_spinner_values);

        enter_barcode_nxt.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        enter_barcodebsv_nxt.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        fetchRplCplCount();

        consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
        bsv_barcode_num_nxt = bsv_barcode_scanning_nxt.getText().toString();
        packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
        expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
        temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();

        if (!GlobalClass.isNull(GlobalClass.consignment_number)) {
            GlobalClass.SetEditText(consignment_edt_txt_nxt, GlobalClass.consignment_number);
        } else {
            GlobalClass.SetEditText(consignment_edt_txt_nxt, "");
        }
        if (!GlobalClass.isNull(GlobalClass.consignment_barcode)) {
            GlobalClass.SetEditText(enter_barcode_nxt, GlobalClass.consignment_barcode);
            GlobalClass.SetEditText(reenter_nxt, GlobalClass.consignment_barcode);
            GlobalClass.SetButtonText(consignment_barcd_btn_nxt, GlobalClass.consignment_barcode);
        } else {
            GlobalClass.SetButtonText(consignment_barcd_btn_nxt, "");
        }
        if (!GlobalClass.isNull(GlobalClass.bsv_barcode_num)) {
            GlobalClass.SetEditText(enter_barcodebsv_nxt, GlobalClass.bsv_barcode_num);
            GlobalClass.SetEditText(reenterbsv_nxt, GlobalClass.bsv_barcode_num);
            GlobalClass.SetButtonText(bsv_barcode_scanning_nxt, GlobalClass.bsv_barcode_num);
        } else {
            GlobalClass.SetButtonText(bsv_barcode_scanning_nxt, "");
        }

        if (!GlobalClass.isNull(GlobalClass.packaging_dtl)) {
            if (GlobalClass.packaging_dtl.equals("Thermocol box with cooled gel pack")) {
                packaging_dtl_edt_txt_nxt.setSelection(1);
            } else if (packaging_dtl_nxt.equals("Chiller box with cooled gel pack")) {
                packaging_dtl_edt_txt_nxt.setSelection(2);
            } else {
                packaging_dtl_edt_txt_nxt.setSelection(0);
            }
        } else {
            packaging_dtl_edt_txt_nxt.setSelection(0);
        }

        if (!GlobalClass.isNull(GlobalClass.expt_transit_tm)) {
            for (int i = 0; i < setSpinnerTRansitTime_nxt.size(); i++) {
                if (GlobalClass.expt_transit_tm.equals(setSpinnerTRansitTime_nxt.get(i))) {
                    transit_time_spinner_nxt.setSelection(i);
                }
            }

        } else {
            transit_time_spinner_nxt.setSelection(0);
        }
        if (!GlobalClass.isNull(GlobalClass.temp_consignment)) {
            if (GlobalClass.temp_consignment.equals("Between 4C to 12C")) {
                consignment_temp_spinner_nxt.setSelection(1);
            } else if (GlobalClass.temp_consignment.equals("Between 12C - 25C")) {
                consignment_temp_spinner_nxt.setSelection(2);
            } else {
                consignment_temp_spinner_nxt.setSelection(0);
            }
        } else {
            consignment_temp_spinner_nxt.setSelection(0);
        }


        if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Air Cargo")) {
            consignment_number_layout_nxt.setVisibility(View.VISIBLE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.VISIBLE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.VISIBLE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.VISIBLE);
        } else if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Bus")) {
            consignment_number_layout_nxt.setVisibility(View.VISIBLE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.VISIBLE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.VISIBLE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.VISIBLE);
        } else if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Courier")) {
            consignment_number_layout_nxt.setVisibility(View.VISIBLE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.GONE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.GONE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
        } else if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Hand Delivery")) {
            consignment_number_layout_nxt.setVisibility(View.GONE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.GONE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.GONE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
        }


        return viewMain;
    }

    private void initListner() {

        btn_to_pre_ll_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
                consignment_number_nxt = consignment_number_nxt.replaceAll("\\s+", "");
                consignment_number_nxt = consignment_number_nxt.replaceAll("\\.", "");
                consignment_barcode_nxt = consignment_barcd_btn_nxt.getText().toString();
                bsv_barcode_num_nxt = bsv_barcode_scanning_nxt.getText().toString();
                packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();

                GlobalClass.consignment_number = consignment_number_nxt;
                GlobalClass.consignment_barcode = consignment_barcode_nxt;
                GlobalClass.bsv_barcode_num = bsv_barcode_num_nxt;
                GlobalClass.packaging_dtl = packaging_dtl_nxt;
                GlobalClass.expt_transit_tm = expt_transit_tm_nxt;
                GlobalClass.temp_consignment = temp_consignment_nxt;


                Consignment_fragment a2Fragment = new Consignment_fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();

            }
        });


        scanIntegrator_nxt = IntentIntegrator.forSupportFragment(this);
        img_scan_consignment_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode_nxt = 1;
                scanIntegrator_nxt.initiateScan();
            }
        });
        img_scan_bsv_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode_nxt = 2;
                scanIntegrator_nxt.initiateScan();
            }
        });


        consignment_barcd_btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode_nxt.setVisibility(View.VISIBLE);
                consignment_name_layout_nxt.setVisibility(View.GONE);
            }
        });
        setback_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcode_nxt.setVisibility(View.GONE);
                consignment_name_layout_nxt.setVisibility(View.VISIBLE);
            }
        });
        bsv_barcode_scanning_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcodebsv_nxt.setVisibility(View.VISIBLE);
                bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
            }
        });
        setbackbsv_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineareditbarcodebsv_nxt.setVisibility(View.GONE);
                bsv_barcode_scanning_ll_nxt.setVisibility(View.VISIBLE);
            }
        });

        btn_submit_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (modedata_nxt.equals("Air Cargo")) {
                    consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\s+", "");
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\.", "");
                    consignment_barcode_nxt = consignment_barcd_btn_nxt.getText().toString();
                    bsv_barcode_num_nxt = bsv_barcode_scanning_nxt.getText().toString();
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (consignment_number_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.consign_num, 2);
                    } else if (packaging_dtl_nxt.equals("Select packaging details")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_pkg_dtl, 2);
                    } else if (consignment_barcode_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.consign_brcd, 2);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.expt_transit_tm, 2);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_temp_consign, 2);
                    } else if (bsv_barcode_num_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.bsv_brcd, 2);
                    } else {
                        doConsignment();
                    }

                } else if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Bus")) {

                    consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\s+", "");
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\.", "");
                    consignment_barcode_nxt = consignment_barcd_btn_nxt.getText().toString();
                    bsv_barcode_num_nxt = bsv_barcode_scanning_nxt.getText().toString();
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (consignment_number_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.consign_num, 2);
                    } else if (packaging_dtl_nxt.equals("Select packaging details")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_pkg_dtl, 2);
                    } else if (consignment_barcode_nxt.equals("")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.consignment_brcd, 2);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.expt_transit_tm, 2);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_temp_consign, 2);
                    } else if (bsv_barcode_num_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.bsv_brcd, 2);
                    } else {
                        doConsignment();
                    }
                } else if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Courier")) {
                    consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\s+", "");
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\.", "");
                    bsv_barcode_num_nxt = bsv_barcode_number_nxt;
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (consignment_number_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.consign_num, 2);
                    } else if (packaging_dtl_nxt.equals("Select packaging details")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_pkg_dtl, 2);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.expt_transit_tm, 2);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_temp_consign, 2);
                    } else if (bsv_barcode_num_nxt.equals("")) {
                        GlobalClass.showTastyToast(mActivity, ToastFile.bsv_brcd, 2);
                    } else {
                        doConsignment();
                    }


                } else if (!GlobalClass.isNull(modedata_nxt) && modedata_nxt.equals("Hand Delivery")) {

                    bsv_barcode_num_nxt = bsv_barcode_number_nxt;
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (packaging_dtl_nxt.equals("Select packaging details")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_pkg_dtl, 2);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.expt_transit_tm, 2);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        GlobalClass.showTastyToast(mContext, ToastFile.slt_temp_consign, 2);
                    } else {
                        doConsignment();
                    }
                }
            }
        });


        enter_barcode_nxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                search_barcode = enter_barcode_nxt.getText().toString();
                if (hasFocus) {

                } else if (!search_barcode.equals("") && search_barcode.length() > 8) {
                    barcodeDetails_nxt = Volley.newRequestQueue(mContext);//2c=/TAM03/TAM03136166236000078/geteditdata

                    String url = Api.barcode_Check + search_barcode + "/CheckConsignmentBarcode";

                    try {
                        if (ControllersGlobalInitialiser.congsigmentBarcode_controller != null) {
                            ControllersGlobalInitialiser.congsigmentBarcode_controller = null;
                        }
                        ControllersGlobalInitialiser.congsigmentBarcode_controller = new CongsigmentBarcode_Controller(mActivity, Next_Consignment_Entry.this, "normal");
                        ControllersGlobalInitialiser.congsigmentBarcode_controller.getcon_barcode(url, barcodeDetails_nxt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        enter_barcode_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    GlobalClass.showTastyToast(mContext,
                            ToastFile.crt_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(enter_barcode_nxt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(enter_barcode_nxt, "");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                String enteredString = s.toString();

            }
        });
        enter_barcode_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mContext,
                            ToastFile.entr_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(enter_barcode_nxt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(enter_barcode_nxt, "");
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

        reenter_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mContext,
                            ToastFile.entr_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(reenter_nxt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(reenter_nxt, "");
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
                int gelength = enter_barcode_nxt.getText().length();
                if (enteredString.length() == gelength) {
                    String getPreviouseText = enter_barcode_nxt.getText().toString();
                    currentText_nxt = reenter_nxt.getText().toString();
                    if (getPreviouseText.equals(currentText_nxt)) {
                        currentText_nxt = reenter_nxt.getText().toString();

                        lineareditbarcode_nxt.setVisibility(View.GONE);
                        consignment_name_layout_nxt.setVisibility(View.VISIBLE);
                        GlobalClass.SetButtonText(consignment_barcd_btn_nxt, currentText_nxt);

                    } else {
                        GlobalClass.SetEditText(reenter_nxt, "");
                        GlobalClass.showTastyToast(mContext, ToastFile.crt_brcd, 2);
                    }

                }
            }
        });
        enter_barcodebsv_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    GlobalClass.showTastyToast(mContext,
                            ToastFile.crt_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(enter_barcodebsv_nxt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(enter_barcodebsv_nxt, "");
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

        reenterbsv_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mContext,
                            ToastFile.entr_brcd,
                            2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(reenterbsv_nxt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(reenterbsv_nxt, "");
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
                int gelength = enter_barcodebsv_nxt.getText().length();
                if (enteredString.length() == gelength) {
                    String getPreviouseText = enter_barcodebsv_nxt.getText().toString();
                    currentText_nxt = reenterbsv_nxt.getText().toString();
                    if (getPreviouseText.equals(currentText_nxt)) {
                        currentText_nxt = reenterbsv_nxt.getText().toString();

                        lineareditbarcodebsv_nxt.setVisibility(View.GONE);
                        bsv_barcode_scanning_ll_nxt.setVisibility(View.VISIBLE);
                        GlobalClass.SetButtonText(bsv_barcode_scanning_nxt, currentText_nxt);
                    } else {
                        GlobalClass.SetEditText(reenter_nxt, "");
                        GlobalClass.showTastyToast(mContext, ToastFile.crt_brcd, 2);
                    }

                }

            }
        });

        consignment_edt_txt_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.consign_num, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(consignment_edt_txt_nxt, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(consignment_edt_txt_nxt, "");
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


        enter_barcode_nxt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        enter_barcodebsv_nxt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        reenter_nxt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        reenterbsv_nxt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
    }

    private void initViews() {

        consignment_number_layout_nxt = (LinearLayout) viewMain.findViewById(R.id.consignment_number_layout);
        consignment_name_layout_nxt = (LinearLayout) viewMain.findViewById(R.id.consignment_name_layout);
        packaging_spinner_layout_nxt = (LinearLayout) viewMain.findViewById(R.id.packaging_spinner_layout);
        expected_spinner_transit_time_layout_nxt = (LinearLayout) viewMain.findViewById(R.id.expected_spinner_transit_time_layout);
        temperature_spinner_layout_nxt = (LinearLayout) viewMain.findViewById(R.id.temperature_spinner_layout);
        total_sample_layout_nxt = (LinearLayout) viewMain.findViewById(R.id.total_sample_layout);
        btn_to_pre_ll_nxt = (LinearLayout) viewMain.findViewById(R.id.btn_to_pre_ll);
        bsv_barcode_scanning_ll_nxt = (LinearLayout) viewMain.findViewById(R.id.bsv_barcode_scanning_ll);
        lineareditbarcode_nxt = (LinearLayout) viewMain.findViewById(R.id.lineareditbarcode);
        lineareditbarcodebsv_nxt = (LinearLayout) viewMain.findViewById(R.id.lineareditbarcodebsv);
        consignment_edt_txt_nxt = (EditText) viewMain.findViewById(R.id.consignment_edt_txt);
        rpl_edt_txt_nxt = (TextView) viewMain.findViewById(R.id.rpl_edt_txt);
        total_consignment_edt_txt_nxt = (TextView) viewMain.findViewById(R.id.total_consignment_edt_txt);
        cpl_edt_txt_nxt = (TextView) viewMain.findViewById(R.id.cpl_edt_txt);
        enter_barcode_nxt = (EditText) viewMain.findViewById(R.id.enter_barcode);
        reenter_nxt = (EditText) viewMain.findViewById(R.id.reenter);
        enter_barcodebsv_nxt = (EditText) viewMain.findViewById(R.id.enter_barcodebsv);
        reenterbsv_nxt = (EditText) viewMain.findViewById(R.id.reenterbsv);
        packaging_dtl_edt_txt_nxt = (Spinner) viewMain.findViewById(R.id.packaging_dtl_edt_txt);
        transit_time_spinner_nxt = (Spinner) viewMain.findViewById(R.id.transit_time_spinner);
        consignment_temp_spinner_nxt = (Spinner) viewMain.findViewById(R.id.consignment_temp_spinner);
        btn_submit_nxt = (Button) viewMain.findViewById(R.id.btn_submit);
        consignment_barcd_btn_nxt = (Button) viewMain.findViewById(R.id.consignment_barcd_btn);
        bsv_barcode_scanning_nxt = (Button) viewMain.findViewById(R.id.bsv_barcode_scanning);
        img_edt_nxt = (ImageView) viewMain.findViewById(R.id.img_edt_consignment);
        img_edt_bsv_nxt = (ImageView) viewMain.findViewById(R.id.img_edt_bsv);
        setbackbsv_nxt = (ImageView) viewMain.findViewById(R.id.setbackbsv);
        setback_nxt = (ImageView) viewMain.findViewById(R.id.setback);
        enetered_nxt = (TextView) viewMain.findViewById(R.id.enetered);
        img_scan_consignment_barcode = (ImageView) viewMain.findViewById(R.id.img_scan_consignment_barcode);
        img_scan_bsv_barcode = (ImageView) viewMain.findViewById(R.id.img_scan_bsv_barcode);
    }

    private void doConsignment() {

        JSONObject jsonObjectOtp = new JSONObject();
        try {
            jsonObjectOtp.put("API_KEY", api_key);
            jsonObjectOtp.put("Consignmen_Barcode", consignment_barcode_nxt);
            jsonObjectOtp.put("ThroughTSPCode", routine_code_nxt);
            jsonObjectOtp.put("DispatchTime", dispatch_time_nxt);
            jsonObjectOtp.put("ConsignmentNo", consignment_number_nxt);
            jsonObjectOtp.put("TransitTime", expt_transit_tm_nxt);
            jsonObjectOtp.put("Mode", modedata_nxt);
            jsonObjectOtp.put("SampleFromCPL", cpl_count_nxt);
            jsonObjectOtp.put("SampleFromRPL", rpl_count_nxt);
            jsonObjectOtp.put("PackagingDetails", packaging_dtl_nxt);
            jsonObjectOtp.put("FlightName", flight_name_nxt);
            jsonObjectOtp.put("FlightNo", flight_number_nxt);
            jsonObjectOtp.put("DepTime", departure_time_nxt);
            jsonObjectOtp.put("ArrTime", arrival_time_nxt);
            jsonObjectOtp.put("BSVBarcode", bsv_barcode_num_nxt);
            jsonObjectOtp.put("Remarks", "");
            jsonObjectOtp.put("TotalSamples", total_count_nxt);
            jsonObjectOtp.put("ConsignmentTemperature", temp_consignment_nxt);
            jsonObjectOtp.put("CourierName", CourierName_nxt);

            RequestQueue PostQue_nxt = Volley.newRequestQueue(mContext);
            try {
                if (ControllersGlobalInitialiser.consigmentEntry_controller != null) {
                    ControllersGlobalInitialiser.consigmentEntry_controller = null;
                }
                ControllersGlobalInitialiser.consigmentEntry_controller = new ConsigmentEntry_Controller(mActivity, Next_Consignment_Entry.this);
                ControllersGlobalInitialiser.consigmentEntry_controller.getconsigmentController(jsonObjectOtp, PostQue_nxt);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "123: ");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.e(TAG, "onActivityResult: " + result);
            if (result.getContents() != null) {
                getBarcodeDetails_nxt = result.getContents();
                if (mode_nxt == 1) {
                    if (getBarcodeDetails_nxt.length() >= 8 && getBarcodeDetails_nxt.length() <= 15) {
                        passBarcodeData(getBarcodeDetails_nxt);
                    } else {
                        GlobalClass.showTastyToast(mContext, ToastFile.invalid_brcd, 2);
                    }
                } else if (mode_nxt == 2) {

                    GlobalClass.SetButtonText(bsv_barcode_scanning_nxt, getBarcodeDetails_nxt);
                    GlobalClass.SetEditText(enter_barcodebsv_nxt, getBarcodeDetails_nxt);
                    GlobalClass.SetEditText(reenterbsv_nxt, getBarcodeDetails_nxt);

                }
            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(final String getBarcodeDetails) {
        this.getBarcodeDetails = getBarcodeDetails;
        barcodeDetails_nxt = Volley.newRequestQueue(mContext);
        String url = Api.barcode_Check + getBarcodeDetails + "/CheckConsignmentBarcode";

        try {
            if (ControllersGlobalInitialiser.congsigmentBarcode_controller != null) {
                ControllersGlobalInitialiser.congsigmentBarcode_controller = null;
            }
            ControllersGlobalInitialiser.congsigmentBarcode_controller = new CongsigmentBarcode_Controller(mActivity, Next_Consignment_Entry.this, "passbarcode");
            ControllersGlobalInitialiser.congsigmentBarcode_controller.getcon_barcode(url, barcodeDetails_nxt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void fetchRplCplCount() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String passDate = sdf.format(d);

        requestQueue_nxt = Volley.newRequestQueue(mContext);

        try {
            if (ControllersGlobalInitialiser.sampleCountController != null) {
                ControllersGlobalInitialiser.sampleCountController = null;
            }
            ControllersGlobalInitialiser.sampleCountController = new SampleCountController(mActivity, Next_Consignment_Entry.this);
            ControllersGlobalInitialiser.sampleCountController.samplecnt_Controller(user, passDate, requestQueue_nxt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getconsignimentResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);
            String finalJson = response.toString();
            JSONObject parentObjectOtp = new JSONObject(finalJson);
            ResponseData_nxt = parentObjectOtp.getString("Response");
            message_nxt = parentObjectOtp.getString("Message");
            ResId_nxt = parentObjectOtp.getString("ResId");
            if (ResponseData_nxt.equals("Success")) {

                GlobalClass.showTastyToast(mActivity, message_nxt, 1);


                Wind_up_fragment a2Fragment = new Wind_up_fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_mainLayout, a2Fragment).commitAllowingStateLoss();


                GlobalClass.consignment_number = null;
                GlobalClass.consignment_barcode = null;
                GlobalClass.bsv_barcode_num = null;
                GlobalClass.packaging_dtl = null;
                GlobalClass.expt_transit_tm = null;
                GlobalClass.temp_consignment = null;
                GlobalClass.getCourier_name = null;
                GlobalClass.bus_name_to_pass = null;
                GlobalClass.get_edt_bus_name = null;
                GlobalClass.bus_number = null;
                GlobalClass.consignment_barcode = null;
                GlobalClass.mode = null;
                GlobalClass.routine_code = null;
                GlobalClass.flagtsp = 0;
                GlobalClass.flight_name = null;
                GlobalClass.flight_number = null;
                GlobalClass.departure_time = null;
                GlobalClass.arrival_time = null;
                GlobalClass.dispatch_time = null;
                GlobalClass.CourierName = null;
                GlobalClass.bsv_barcode = null;


            } else if (ResponseData_nxt.equalsIgnoreCase(small_invalidApikey)) {
                GlobalClass.hideProgress(mActivity, barProgressDialog_nxt);
                GlobalClass.redirectToLogin(mContext);
            } else {
                GlobalClass.hideProgress(mActivity, barProgressDialog_nxt);
                GlobalClass.showTastyToast(mActivity, "" + message_nxt, 2);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getbarcodeResponse(String response, String fromcome) {

        if (fromcome.equalsIgnoreCase("normal")) {
            Log.e(TAG, "onResponse: " + response);
            Log.v("TAG", "barcode respponse" + response);

            if (!GlobalClass.isNull(response) && response.equals("\"Valid\"")) {

                GlobalClass.SetEditText(enter_barcode_nxt, search_barcode);
                GlobalClass.hideProgress(mActivity, barProgressDialog_nxt);
            } else {
                GlobalClass.SetEditText(enter_barcode_nxt, "");
                GlobalClass.hideProgress(mActivity, barProgressDialog_nxt);
                GlobalClass.showTastyToast(mContext, "" + response, 2);
            }
        } else if (fromcome.equalsIgnoreCase("passbarcode")) {
            Log.v("TAG", "barcode respponse" + response);

            Log.e(TAG, "onResponse: " + response);
            if (!GlobalClass.isNull(response) && response.equals("\"Valid\"")) {
                GlobalClass.SetButtonText(consignment_barcd_btn_nxt, getBarcodeDetails);
                GlobalClass.SetEditText(enter_barcode_nxt, getBarcodeDetails);
                GlobalClass.SetEditText(reenter_nxt, getBarcodeDetails);

            } else {
                GlobalClass.SetButtonText(consignment_barcd_btn_nxt, "");
                GlobalClass.showTastyToast(mActivity, ToastFile.consign_brcd, 2);
            }

        }


    }

    public void getsamplecount(JSONObject response) {
        Log.e(TAG, "onResponse: response" + response);
        String finalJson = response.toString();
        JSONObject parentObjectOtp = null;
        try {
            parentObjectOtp = new JSONObject(finalJson);
            cpl_count_nxt = parentObjectOtp.getString("cpl_count");
            response1_nxt = parentObjectOtp.getString("response");
            rpl_count_nxt = parentObjectOtp.getString("rpl_count");
            total_count_nxt = parentObjectOtp.getString("total_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!GlobalClass.isNull(response1_nxt) && response1_nxt.equals("SUCCESS")) {
            GlobalClass.SetText(total_consignment_edt_txt_nxt, "Total Sample: " + total_count_nxt);

            GlobalClass.SetText(rpl_edt_txt_nxt, "RPL: " + rpl_count_nxt);
            GlobalClass.SetText(cpl_edt_txt_nxt, "CPL: " + cpl_count_nxt);
        }
    }
}

interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);

}
