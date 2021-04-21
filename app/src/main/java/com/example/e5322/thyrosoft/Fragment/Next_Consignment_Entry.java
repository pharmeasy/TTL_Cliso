package com.example.e5322.thyrosoft.Fragment;

import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    TextView enetered_nxt, enterwindup_nxt, tv_addpouch;
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
    RecyclerView recy_barcode;
    private Dialog pocuh_dialog;
    ConnectionDetector connectionDetector;
    RecyclerView recy_scannedbarcode;
    private ArrayList<ScanBarcodeResponseModel.PouchCodeDTO> setBarcodePouch;
    LinearLayout ll_pouch;
    private Button btn_pouch_barcd;
    private ArrayList<BarcodeResponseModel.BarcodeDTO> barcodelist = new ArrayList<>();
    private ArrayList<BarcodeResponseModel.BarcodeDTO> totalbarcodelist = new ArrayList<>();

    public Next_Consignment_Entry() {
        // Required empty public constructor
    }

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
        viewMain = (View) inflater.inflate(R.layout.fragment_next__consignment__entry, container, false);
        prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        connectionDetector = new ConnectionDetector(getActivity());
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
        tv_addpouch = viewMain.findViewById(R.id.tv_addpouch);
        recy_scannedbarcode = viewMain.findViewById(R.id.recy_scannedbarcode);
        ll_pouch = viewMain.findViewById(R.id.ll_pouch);
//        enterwindup = (TextView) viewMain.findViewById(R.id.enterwindup);

        if (modedata_nxt.equalsIgnoreCase("HAND DELIVERY") || modedata_nxt.equalsIgnoreCase("COURIER") || modedata_nxt.equalsIgnoreCase("LME")) {
            ll_pouch.setVisibility(View.GONE);
        } else {
            GetScannerBarcode();
            ll_pouch.setVisibility(View.VISIBLE);
        }


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


        consignment_edt_txt_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(getActivity(),
                            ToastFile.consign_num,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        consignment_edt_txt_nxt.setText(enteredString.substring(1));
                    } else {
                        consignment_edt_txt_nxt.setText("");
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


        consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();

        bsv_barcode_num_nxt = bsv_barcode_scanning_nxt.getText().toString();
        packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
        expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
        temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();

        if (GlobalClass.consignment_number != null) {
            consignment_edt_txt_nxt.setText(GlobalClass.consignment_number);
        } else {
            consignment_edt_txt_nxt.setText("");
        }
        if (GlobalClass.consignment_barcode != null) {
            enter_barcode_nxt.setText(GlobalClass.consignment_barcode);
            reenter_nxt.setText(GlobalClass.consignment_barcode);
            consignment_barcd_btn_nxt.setText(GlobalClass.consignment_barcode);
        } else {
            consignment_barcd_btn_nxt.setText("");
        }
        if (GlobalClass.bsv_barcode_num != null) {
            enter_barcodebsv_nxt.setText(GlobalClass.bsv_barcode_num);
            reenterbsv_nxt.setText(GlobalClass.bsv_barcode_num);
            bsv_barcode_scanning_nxt.setText(GlobalClass.bsv_barcode_num);
        } else {
            bsv_barcode_scanning_nxt.setText("");
        }
        if (GlobalClass.packaging_dtl != null) {

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
        if (GlobalClass.expt_transit_tm != null) {
            for (int i = 0; i < setSpinnerTRansitTime_nxt.size(); i++) {
                if (GlobalClass.expt_transit_tm.equals(setSpinnerTRansitTime_nxt.get(i))) {
                    transit_time_spinner_nxt.setSelection(i);
                }
            }

        } else {
            transit_time_spinner_nxt.setSelection(0);
        }
        if (GlobalClass.temp_consignment != null) {
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

        tv_addpouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });


        enter_barcode_nxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                final String search_barcode = enter_barcode_nxt.getText().toString();
                if (hasFocus) {

                } else if (!search_barcode.equals("") && search_barcode.length() > 8) {
                    barcodeDetails_nxt = GlobalClass.setVolleyReq(mContext);//2c=/TAM03/TAM03136166236000078/geteditdata
                    barProgressDialog_nxt = new ProgressDialog(mContext);
                    barProgressDialog_nxt.setTitle("Kindly wait ...");
                    barProgressDialog_nxt.setMessage(ToastFile.processing_request);
                    barProgressDialog_nxt.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    barProgressDialog_nxt.setProgress(0);
                    barProgressDialog_nxt.setMax(20);
                    barProgressDialog_nxt.setCanceledOnTouchOutside(false);
                    barProgressDialog_nxt.setCancelable(false);
                    barProgressDialog_nxt.show();

                    barcodeDetails_nxt = GlobalClass.setVolleyReq(mContext);
                    StringRequest jsonObjectRequestPop = new StringRequest(Request.Method.GET, Api.barcode_Check + search_barcode + "/CheckConsignmentBarcode"
                            , new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "onResponse: " + response);
                            System.out.println("barcode respponse" + response);

                            if (response.equals("\"Valid\"")) {
                                enter_barcode_nxt.setText(search_barcode);
                                if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                                    barProgressDialog_nxt.dismiss();
                                }
                            } else {
                                enter_barcode_nxt.setText("");
                                if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                                    barProgressDialog_nxt.dismiss();
                                }
                                Toast.makeText(mContext, "" + response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse == null) {
                                if (error.getClass().equals(TimeoutError.class)) {
                                    // Show timeout error message_nxt
                                }
                            }
                        }
                    });
                    jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                            300000,
                            3,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    barcodeDetails_nxt.add(jsonObjectRequestPop);
                    Log.e(TAG, "onFocusChange: url" + jsonObjectRequestPop);
                }
            }
        });


        enter_barcode_nxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".") || enteredString.startsWith("0")) {
                    Toast.makeText(mContext,
                            ToastFile.crt_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcode_nxt.setText(enteredString.substring(1));
                    } else {
                        enter_barcode_nxt.setText("");
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
                    Toast.makeText(mContext,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcode_nxt.setText(enteredString.substring(1));
                    } else {
                        enter_barcode_nxt.setText("");
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
                    Toast.makeText(mContext,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        reenter_nxt.setText(enteredString.substring(1));
                    } else {
                        reenter_nxt.setText("");
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
                        consignment_barcd_btn_nxt.setText(currentText_nxt);

                    } else {
                        reenter_nxt.setText("");
                        Toast.makeText(mContext, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext,
                            ToastFile.crt_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        enter_barcodebsv_nxt.setText(enteredString.substring(1));
                    } else {
                        enter_barcodebsv_nxt.setText("");
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
                    Toast.makeText(mContext,
                            ToastFile.entr_brcd,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        reenterbsv_nxt.setText(enteredString.substring(1));
                    } else {
                        reenterbsv_nxt.setText("");
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
                        bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
                        bsv_barcode_scanning_nxt.setText(currentText_nxt);

                    } else {
                        reenter_nxt.setText("");
                        Toast.makeText(mContext, ToastFile.crt_brcd, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        if (modedata_nxt.equals("Air Cargo")) {
            consignment_number_layout_nxt.setVisibility(View.VISIBLE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.VISIBLE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.VISIBLE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
        } else if (modedata_nxt.equals("Bus")) {
            consignment_number_layout_nxt.setVisibility(View.VISIBLE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.VISIBLE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.VISIBLE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
        } else if (modedata_nxt.equals("Courier")) {
            consignment_number_layout_nxt.setVisibility(View.VISIBLE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.GONE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.GONE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
        } else if (modedata_nxt.equals("Hand Delivery")) {
            consignment_number_layout_nxt.setVisibility(View.GONE);
            packaging_spinner_layout_nxt.setVisibility(View.VISIBLE);
            consignment_name_layout_nxt.setVisibility(View.GONE);
            expected_spinner_transit_time_layout_nxt.setVisibility(View.VISIBLE);
            temperature_spinner_layout_nxt.setVisibility(View.VISIBLE);
            total_sample_layout_nxt.setVisibility(View.GONE);
            bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
        }

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
                bsv_barcode_scanning_ll_nxt.setVisibility(View.GONE);
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
                        TastyToast.makeText(mContext, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (packaging_dtl_nxt.equals("Select packaging details")) {
                        TastyToast.makeText(mContext, ToastFile.slt_pkg_dtl, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.consign_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        TastyToast.makeText(mContext, ToastFile.expt_transit_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        TastyToast.makeText(mContext, ToastFile.slt_temp_consign, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }/* else if (bsv_barcode_num_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/ else {
                        doConsignment();
                    }

                } else if (modedata_nxt.equals("Bus")) {

                    consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\s+", "");
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\.", "");
                    consignment_barcode_nxt = consignment_barcd_btn_nxt.getText().toString();
                    bsv_barcode_num_nxt = bsv_barcode_scanning_nxt.getText().toString();
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (consignment_number_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (packaging_dtl_nxt.equals("Select packaging details")) {
                        TastyToast.makeText(mContext, ToastFile.slt_pkg_dtl, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (consignment_barcode_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.consignment_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        TastyToast.makeText(mContext, ToastFile.expt_transit_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        TastyToast.makeText(mContext, ToastFile.slt_temp_consign, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } /*else if (bsv_barcode_num_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/ else {
                        doConsignment();
                    }
                } else if (modedata_nxt.equals("Courier")) {
                    consignment_number_nxt = consignment_edt_txt_nxt.getText().toString();
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\s+", "");
                    consignment_number_nxt = consignment_number_nxt.replaceAll("\\.", "");
                    bsv_barcode_num_nxt = bsv_barcode_number_nxt;
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (consignment_number_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.consign_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (packaging_dtl_nxt.equals("Select packaging details")) {
                        TastyToast.makeText(mContext, ToastFile.slt_pkg_dtl, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        TastyToast.makeText(mContext, ToastFile.expt_transit_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        TastyToast.makeText(mContext, ToastFile.slt_temp_consign, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } /*else if (bsv_barcode_num_nxt.equals("")) {
                        TastyToast.makeText(mContext, ToastFile.bsv_brcd, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/ else {
                        doConsignment();
                    }


                } else if (modedata_nxt.equals("Hand Delivery")) {

                    bsv_barcode_num_nxt = bsv_barcode_number_nxt;
                    packaging_dtl_nxt = packaging_dtl_edt_txt_nxt.getSelectedItem().toString();
                    expt_transit_tm_nxt = transit_time_spinner_nxt.getSelectedItem().toString();
                    temp_consignment_nxt = consignment_temp_spinner_nxt.getSelectedItem().toString();
                    if (packaging_dtl_nxt.equals("Select packaging details")) {
                        TastyToast.makeText(mContext, ToastFile.slt_pkg_dtl, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (expt_transit_tm_nxt.equals("Select expected transit time")) {
                        TastyToast.makeText(mContext, ToastFile.expt_transit_tm, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (temp_consignment_nxt.equals("Select Temperature of the Consignment")) {
                        TastyToast.makeText(mContext, ToastFile.slt_temp_consign, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        doConsignment();
                    }
                }
            }
        });

        return viewMain;
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
                mode_nxt = 3;
                scanIntegrator_nxt.initiateScan();
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
                if (GlobalClass.CheckArrayList(totalbarcodelist)) {
                    for (int i = 0; i < totalbarcodelist.size(); i++) {
                        if (totalbarcodelist.get(i).isSelected()) {
                            selectnone = true;
                            break;
                        }
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
                    PostBarcodeController postBarcodeController = new PostBarcodeController(Next_Consignment_Entry.this);
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


    private void doConsignment() {
        barProgressDialog_nxt = new ProgressDialog(mContext);
        barProgressDialog_nxt.setTitle("Kindly wait ...");
        barProgressDialog_nxt.setMessage(ToastFile.processing_request);
        barProgressDialog_nxt.setProgressStyle(barProgressDialog_nxt.STYLE_SPINNER);
        barProgressDialog_nxt.setProgress(0);
        barProgressDialog_nxt.setMax(20);
        barProgressDialog_nxt.show();
        barProgressDialog_nxt.setCanceledOnTouchOutside(false);
        barProgressDialog_nxt.setCancelable(false);
        PostQue_nxt = GlobalClass.setVolleyReq(mContext);
        ConsignmentRequestModel consignmentRequestModel = new ConsignmentRequestModel();
        consignmentRequestModel.setAPI_KEY(api_key);
        consignmentRequestModel.setConsignmen_Barcode(consignment_barcode_nxt);
        consignmentRequestModel.setThroughTSPCode(routine_code_nxt);
        consignmentRequestModel.setDispatchTime(dispatch_time_nxt);
        consignmentRequestModel.setConsignmentNo(consignment_number_nxt);
        consignmentRequestModel.setTransitTime(expt_transit_tm_nxt);
        consignmentRequestModel.setMode(modedata_nxt);
        consignmentRequestModel.setSampleFromCPL(cpl_count_nxt);
        consignmentRequestModel.setSampleFromRPL(rpl_count_nxt);
        consignmentRequestModel.setPackagingDetails(packaging_dtl_nxt);
        consignmentRequestModel.setFlightName(flight_name_nxt);
        consignmentRequestModel.setFlightNo(flight_number_nxt);
        consignmentRequestModel.setDepTime(departure_time_nxt);
        consignmentRequestModel.setArrTime(arrival_time_nxt);
        consignmentRequestModel.setBSVBarcode("");
        consignmentRequestModel.setRemarks("");
        consignmentRequestModel.setTotalSamples(total_count_nxt);
        consignmentRequestModel.setConsignmentTemperature(temp_consignment_nxt);
        consignmentRequestModel.setCourierName(CourierName_nxt);
        consignmentRequestModel.setPouchCodeDtlLists(getArrayList());


        JSONObject jsonObj = null;
        try {
            jsonObj = geneRateReqSSLkeyObject(consignmentRequestModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
           /* jsonObjectOtp.put("API_KEY", api_key);
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
            jsonObjectOtp.put("pouchReqs", CourierName_nxt);*/


        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.consignmentEntry, jsonObj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e(TAG, "onResponse: " + response);
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);
                    ResponseData_nxt = parentObjectOtp.getString("Response");
                    message_nxt = parentObjectOtp.getString("Message");
                    ResId_nxt = parentObjectOtp.getString("ResId");
                    if (ResponseData_nxt.equals("Success")) {
                        if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                            barProgressDialog_nxt.dismiss();
                        }
                        TastyToast.makeText(mContext, message_nxt, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        Wind_up_fragment a2Fragment = new Wind_up_fragment();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commitAllowingStateLoss();


                        GlobalClass.consignment_number = "";
                        GlobalClass.consignment_barcode = "";
                        GlobalClass.bsv_barcode_num = "";
                        GlobalClass.packaging_dtl = "";
                        GlobalClass.expt_transit_tm = "";
                        GlobalClass.temp_consignment = "";
                        GlobalClass.getCourier_name = "";
                        GlobalClass.bus_name_to_pass = "";
                        GlobalClass.get_edt_bus_name = "";
                        GlobalClass.bus_number = "";
                        GlobalClass.consignment_barcode = "";
                        GlobalClass.mode = "";
                        GlobalClass.routine_code = "";
                        GlobalClass.flagtsp = 0;
                        GlobalClass.flight_name = "";
                        GlobalClass.flight_number = "";
                        GlobalClass.departure_time = "";
                        GlobalClass.arrival_time = "";
                        GlobalClass.dispatch_time = "";
                        GlobalClass.CourierName = "";
                        GlobalClass.bsv_barcode = "";


                    } else if (ResponseData_nxt.equalsIgnoreCase(small_invalidApikey)) {
                        if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                            barProgressDialog_nxt.dismiss();
                        }
                        GlobalClass.redirectToLogin(mContext);
                    } else {
                        if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                            barProgressDialog_nxt.dismiss();
                        }
                        TastyToast.makeText(mContext, "" + message_nxt, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                    barProgressDialog_nxt.dismiss();
                }
            }
        });
        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PostQue_nxt.add(jsonObjectRequest1);
        Log.e(TAG, "doConsignment: json" + jsonObj);
        Log.e(TAG, "doConsignment: json" + jsonObjectRequest1);
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
                        Toast.makeText(mContext, ToastFile.invalid_brcd, Toast.LENGTH_SHORT).show();
                    }
                } else if (mode_nxt == 2) {
                    bsv_barcode_scanning_nxt.setText(getBarcodeDetails_nxt);
                    enter_barcodebsv_nxt.setText(getBarcodeDetails_nxt);
                    reenterbsv_nxt.setText(getBarcodeDetails_nxt);
                } else if (mode_nxt == 3) {
                    btn_pouch_barcd.setText(getBarcodeDetails_nxt);
                }
            }
        } else {
            Log.e(TAG, "else: ");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void passBarcodeData(final String getBarcodeDetails) {
        barcodeDetails_nxt = GlobalClass.setVolleyReq(mContext);
        StringRequest jsonObjectRequestPop = new StringRequest(Request.Method.GET, Api.barcode_Check + getBarcodeDetails + "/CheckConsignmentBarcode"
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("barcode respponse" + response);

                Log.e(TAG, "onResponse: " + response);
                if (response.equals("\"Valid\"")) {
                    consignment_barcd_btn_nxt.setText(getBarcodeDetails);
                    enter_barcode_nxt.setText(getBarcodeDetails);
                    reenter_nxt.setText(getBarcodeDetails);


                } else {
                    consignment_barcd_btn_nxt.setText("");
                    Toast.makeText(mContext, ToastFile.consign_brcd_name + response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message_nxt
                    }
                }
            }
        });
        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        barcodeDetails_nxt.add(jsonObjectRequestPop);
        Log.e(TAG, "passBarcodeData: url" + jsonObjectRequestPop);
    }


    private void fetchRplCplCount() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String passDate = sdf.format(d);

        requestQueue_nxt = GlobalClass.setVolleyReq(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.static_pages_link + user + "/" + passDate + "/getsamplecount", new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
                if (response1_nxt.equals("SUCCESS")) {
                    total_consignment_edt_txt_nxt.setText("Total Sample: " + total_count_nxt);
                    rpl_edt_txt_nxt.setText("RPL: " + rpl_count_nxt);
                    cpl_edt_txt_nxt.setText("CPL: " + cpl_count_nxt);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        TastyToast.makeText(mContext, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        // Show timeout error message_nxt
                    }
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_nxt.add(jsonObjectRequest);
        Log.e(TAG, "fetchRplCplCount: url" + jsonObjectRequest);
    }


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
