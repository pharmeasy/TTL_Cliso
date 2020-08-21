package com.example.e5322.thyrosoft.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GenerateTokenController;
import com.example.e5322.thyrosoft.Controller.GetCenterlist_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.Myprofile_Controller;
import com.example.e5322.thyrosoft.Controller.PETCTSLOT_Controller;
import com.example.e5322.thyrosoft.Controller.PETCTServiceTypes_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CenterList_Model;
import com.example.e5322.thyrosoft.Models.ServiceModel;
import com.example.e5322.thyrosoft.Models.SlotModel;
import com.example.e5322.thyrosoft.Models.TokenModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class PETCT_Frag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String centerId = "", centerAddress, centerCity, centerName, price, patientName, mobile, email, remarks;
    public Button btn_submit, btn_reset;
    Calendar myCalendar;
    RadioButton rb_gender, rb_dib, rb_age;
    Date fromdate;
    RadioGroup radio_gender, radio_diab, radio_age;
    Date To_date;
    RelativeLayout rel_dob;
    String user;
    ImageView enter_arrow_enter;
    private String myFormat = "dd-MM-yyyy";
    private Spinner spn_city, spn_service, spn_slot;
    private Activity activity;
    private String TAG = PETCT_Frag.class.getSimpleName();
    private EditText edt_name, edt_mname, edt_lname, edt_mobile, edt_email, edt_remarks, edt_coupon, edt_age;
    private Gson gson;
    private String CLIENT_TYPE;
    private ConnectionDetector cd;
    private TextView tv_discount, enter;
    private LinearLayout ll_discount, ll_coupon, sub_lin, enter_ll_unselected;
    private Context context;
    private List<CenterList_Model> centerListModels;
    private List<ServiceModel> serviceListModels;
    private List<SlotModel> slotModelList;
    private String serviceId = "";
    private String cal_age;
    private String gendername = "", diabeticsname = "", str_slot, str_age = "";
    private String serviceName = "", To_formateDate;
    private DecimalFormat decimalFormat;
    private TextView tv_date, txt_dialog, txt_appoint_dialog, txt_appdate, txt_ledgerbal;
    private String putDate;
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private String getFormatDate, from_formateDate;

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            cal_age = GlobalClass.getAge(year, monthOfYear, dayOfMonth);
            Log.e(TAG, "calculate age ---->" + cal_age);
            updateLabel();
        }
    };
    private int mYear, mMonth, mDay;
    private String header;
    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            putDate = sdf.format(myCalendar.getTime());
            getFormatDate = sdf.format(myCalendar.getTime());
            To_date = returndate(To_date, putDate);
            To_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);

            GlobalClass.SetText(txt_appdate, putDate);

            try {
                if (ControllersGlobalInitialiser.petctslot_controller != null) {
                    ControllersGlobalInitialiser.petctslot_controller = null;
                }
                ControllersGlobalInitialiser.petctslot_controller = new PETCTSLOT_Controller(activity, PETCT_Frag.this, header);
                ControllersGlobalInitialiser.petctslot_controller.getSlotsController(txt_appdate, header, centerId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private String servicetype;
    private String closingbal;
    private SharedPreferences prefs;
    private String api_key = "";

    public static PETCT_Frag newInstance(String param1, String param2) {
        PETCT_Frag fragment = new PETCT_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lay_petct, container, false);

        context = getContext();
        activity = getActivity();
        myCalendar = Calendar.getInstance();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        cd = new ConnectionDetector(getActivity());
        decimalFormat = new DecimalFormat("##,##,##,###");

        getAPIKeyDetails();
        initUI(view);
        generateToken();
        initListners();
        getProfileDetails(context);

        return view;
    }

    private void getAPIKeyDetails() {
        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        api_key = prefs.getString("API_KEY", null);
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);
    }


    private void updateLabel() {
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        fromdate = returndate(fromdate, putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);

        GlobalClass.SetText(tv_date, putDate);
    }

    private Date returndate(Date date, String putDate) {
        try {
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void getProfileDetails(final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);


        String sturl = Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile";


        try {
            if (ControllersGlobalInitialiser.myprofile_controller != null) {
                ControllersGlobalInitialiser.myprofile_controller = null;
            }
            ControllersGlobalInitialiser.myprofile_controller = new Myprofile_Controller(activity, PETCT_Frag.this);
            ControllersGlobalInitialiser.myprofile_controller.getmyprofilecontroller(sturl, queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initUI(View v) {

        SharedPreferences preferences = getActivity().getSharedPreferences("profile", 0);
        closingbal = preferences.getString("balance", "");
        Log.e(TAG, "BALANCE ---->" + closingbal);

        SharedPreferences preferences1 = getActivity().getSharedPreferences("Userdetails", 0);
        user = preferences1.getString("USER_CODE", null);

        edt_name = (EditText) v.findViewById(R.id.edt_name);
        edt_mname = (EditText) v.findViewById(R.id.edt_mname);
        edt_lname = (EditText) v.findViewById(R.id.edt_lname);
        edt_mobile = (EditText) v.findViewById(R.id.edt_mobile);
        edt_email = (EditText) v.findViewById(R.id.edt_email);
        edt_remarks = (EditText) v.findViewById(R.id.edt_remarks);
        edt_age = v.findViewById(R.id.edt_age);
        edt_coupon = v.findViewById(R.id.edt_coupon);

        btn_submit = (Button) v.findViewById(R.id.btn_submit);
        rel_dob = v.findViewById(R.id.rel_dob);

        btn_reset = v.findViewById(R.id.btn_reset);
        tv_discount = v.findViewById(R.id.tv_discount);
        spn_city = v.findViewById(R.id.spn_city);
        spn_service = v.findViewById(R.id.spn_service);
        spn_slot = v.findViewById(R.id.spn_slot);
        ll_discount = v.findViewById(R.id.ll_discount);
        ll_coupon = v.findViewById(R.id.ll_coupon);
        tv_date = v.findViewById(R.id.txt_date);
        txt_dialog = v.findViewById(R.id.txt_dialog);
        txt_appoint_dialog = v.findViewById(R.id.txt_appoint_dialog);
        txt_appdate = v.findViewById(R.id.txt_appdate);

        radio_gender = (RadioGroup) v.findViewById(R.id.radioSex);
        radio_diab = (RadioGroup) v.findViewById(R.id.radio_diab);
        radio_age = (RadioGroup) v.findViewById(R.id.radio_age);
        sub_lin = v.findViewById(R.id.sub_lin);

        enter_ll_unselected = (LinearLayout) v.findViewById(R.id.enter_ll_unselected);
        enter = (TextView) v.findViewById(R.id.enter);
        enter_arrow_enter = (ImageView) v.findViewById(R.id.enter_arrow_enter);


        txt_ledgerbal = v.findViewById(R.id.txt_ledgerbal);

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    GlobalClass.showTastyToast(getActivity(), getString(R.string.email_no_space), 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_email, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_email, "");
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


        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ")) {
                    GlobalClass.showTastyToast(activity, getString(R.string.space_val_remarks), 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_remarks, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_remarks, "");
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


        if (!GlobalClass.isNull(closingbal)) {
            GlobalClass.SetText(txt_ledgerbal, getResources().getString(R.string.str_legderbal) + " : " + "Rs " + GlobalClass.currencyFormat(closingbal));
        } else {
            GlobalClass.SetText(txt_ledgerbal, "");
            closingbal = "0";
        }

        txt_dialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });


        txt_appoint_dialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void generateToken() {
        if (cd.isConnectingToInternet()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Email", Constants.NHF_EMAIL);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (ControllersGlobalInitialiser.generateTokenController != null) {
                ControllersGlobalInitialiser.generateTokenController = null;
            }
            ControllersGlobalInitialiser.generateTokenController = new GenerateTokenController(activity, PETCT_Frag.this);
            ControllersGlobalInitialiser.generateTokenController.generateToken(true, jsonObject);
        } else {
            GlobalClass.showTastyToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 2);

        }

    }

    private void initListners() {

        spn_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servicetype = spn_service.getSelectedItem().toString().trim();

                if (GlobalClass.CheckArrayList(serviceListModels)) {
                    for (int i = 0; i < serviceListModels.size(); i++) {
                        if (serviceListModels.get(i).getServiceName().toUpperCase().equalsIgnoreCase(servicetype)) {
                            price = "" + serviceListModels.get(i).getNHF_Rate();
                            serviceId = serviceListModels.get(i).getServiceId();
                            serviceName = serviceListModels.get(i).getServiceName();
                        }
                    }


                    Log.e(TAG, "service type ---> " + servicetype);

                    if (!GlobalClass.isNull(servicetype) && !servicetype.equalsIgnoreCase("Select Scan Type*")) {
                        if (spn_city.getSelectedItem().toString().equalsIgnoreCase("Select City*")) {
                            spn_service.setSelection(0);
                            tv_discount.setVisibility(View.GONE);
                            GlobalClass.SetText(tv_discount, "");
                        } else {
                            try {
                                if (Integer.parseInt(closingbal) < Integer.parseInt(price)) {
                                    showDialog(true);
                                    sub_lin.setVisibility(View.GONE);
                                } else {
                                    sub_lin.setVisibility(View.VISIBLE);
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            patientName = edt_name.getText().toString().trim() + " " + edt_mname.getText().toString().trim() + " " + edt_lname.getText().toString().trim();
                        }
                    } else {
                        edt_name.getText().clear();
                        sub_lin.setVisibility(View.GONE);
                        tv_discount.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radio_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb_gender = (RadioButton) group.findViewById(checkedId);
                gendername = rb_gender.getText().toString();

            }
        });

        radio_diab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb_dib = (RadioButton) group.findViewById(checkedId);
                diabeticsname = rb_dib.getText().toString();

            }
        });


        radio_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb_age = (RadioButton) group.findViewById(checkedId);
                str_age = rb_age.getText().toString();

                if (str_age.equalsIgnoreCase("DOB")) {
                    rel_dob.setVisibility(View.VISIBLE);
                    edt_age.setVisibility(View.GONE);
                    edt_age.getText().clear();
                } else {
                    edt_age.setVisibility(View.VISIBLE);
                    rel_dob.setVisibility(View.GONE);
                    GlobalClass.SetText(tv_date, "");
                }

            }
        });

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = spn_city.getSelectedItem().toString().trim();


                if (!GlobalClass.isNull(city) && !city.equalsIgnoreCase("Select City*")) {
                    if (GlobalClass.CheckArrayList(centerListModels)) {
                        for (int i = 0; i < centerListModels.size(); i++) {
                            if (city.equalsIgnoreCase(centerListModels.get(i).getLocation())) {
                                centerId = centerListModels.get(i).getCenterId();
                                centerName = centerListModels.get(i).getName();
                                centerAddress = centerListModels.get(i).getAddress();
                                centerCity = centerListModels.get(i).getLocation();
                            }
                        }


                        callServiceTypesAPI(centerId);
                        try {
                            if (ControllersGlobalInitialiser.petctslot_controller != null) {
                                ControllersGlobalInitialiser.petctslot_controller = null;
                            }
                            ControllersGlobalInitialiser.petctslot_controller = new PETCTSLOT_Controller(activity, PETCT_Frag.this, header);
                            ControllersGlobalInitialiser.petctslot_controller.getSlotsController(txt_appdate, header, centerId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else if (!GlobalClass.isNull(city) && city.equalsIgnoreCase("Select City*")) {
                    spn_service.setSelection(0);
                    spn_slot.setSelection(0);
                    ll_discount.setVisibility(View.GONE);
                    sub_lin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    patientName = edt_name.getText().toString().trim() + " " + edt_mname.getText().toString().trim() + " " + edt_lname.getText().toString().trim();
                    displayDiscount(patientName, centerName, centerAddress, price);
                } else {
                    tv_discount.setVisibility(View.GONE);
                    GlobalClass.SetText(tv_discount, "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValidation()) {
                    if (edt_email.getText().toString().length() > 0) {
                        if (emailValidation(edt_email.getText().toString()) == true) {
                            if (cd.isConnectingToInternet()) {
                                try {
                                    if (Integer.parseInt(closingbal) >= Integer.parseInt(price)) {
                                        gotoconfirmbooking();
                                    } else {
                                        showDialog(false);
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                GlobalClass.showTastyToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 2);
                            }
                        } else {
                            GlobalClass.showTastyToast(getActivity(), "Enter valid email id !", 2);
                        }
                    } else {
                        if (checkRemarksValidation()) {
                            if (cd.isConnectingToInternet()) {
                                try {
                                    if (Integer.parseInt(closingbal) >= Integer.parseInt(price)) {
                                        gotoconfirmbooking();
                                    } else {
                                        showDialog(false);
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                GlobalClass.showTastyToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 2);
                            }
                        }
                    }
                }
            }
        });


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFields();
            }
        });

    }

    private void gotoconfirmbooking() {

        Intent intent = new Intent(getContext(), ConfirmbookDetail.class);
        intent.putExtra(Constants.PNAME, patientName);
        intent.putExtra(Constants.FNAME, edt_name.getText().toString().trim());
        intent.putExtra(Constants.MNAME, edt_mname.getText().toString().trim());
        intent.putExtra(Constants.LNAME, edt_lname.getText().toString().trim());
        intent.putExtra(Constants.PGENDER, gendername);
        intent.putExtra(Constants.PDIEB, diabeticsname);
        intent.putExtra(Constants.PDOB, tv_date.getText().toString());
        intent.putExtra(Constants.appointdate, txt_appdate.getText().toString());
        intent.putExtra(Constants.PMOB, edt_mobile.getText().toString());
        intent.putExtra(Constants.PEMAIL, edt_email.getText().toString());
        intent.putExtra(Constants.PCITY, spn_city.getSelectedItem().toString());
        intent.putExtra(Constants.PREMARK, edt_remarks.getText().toString());
        intent.putExtra(Constants.SERVICETYPE, spn_service.getSelectedItem().toString());
        intent.putExtra(Constants.AVAILBAL, closingbal);
        intent.putExtra(Constants.PAID_BAL, price);
        intent.putExtra(Constants.HEADER, header);
        intent.putExtra(Constants.CENTERID, centerId);
        intent.putExtra(Constants.SERVICEID, serviceId);
        intent.putExtra(Constants.CALAGE, edt_age.getText().toString());
        intent.putExtra(Constants.SLOT, str_slot);
        startActivity(intent);
    }


    public void clearAllFields() {
        edt_name.requestFocus();
        GlobalClass.SetEditText(edt_name, "");
        GlobalClass.SetEditText(edt_mname, "");
        GlobalClass.SetEditText(edt_lname, "");
        GlobalClass.SetEditText(edt_mobile, "");
        GlobalClass.SetEditText(edt_email, "");
        GlobalClass.SetEditText(edt_remarks, "");

        try {
            rb_gender.setChecked(false);
            rb_dib.setChecked(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gendername = "";
        diabeticsname = "";


        GlobalClass.SetText(tv_date, "");
        GlobalClass.SetText(txt_appdate, "");

        tv_discount.setVisibility(View.GONE);
        GlobalClass.SetText(tv_discount, "");


        if (spn_city.isShown())
            spn_city.setSelection(0);
        if (spn_service.isShown())
            spn_service.setSelection(0);
        if (spn_slot.isShown())
            spn_slot.setSelection(0);

    }


    public void displayDiscount(String patientName, String centerName, String centerAddress, String price) {
        tv_discount.setVisibility(View.VISIBLE);
        try {
            String servicetype = spn_service.getSelectedItem().toString().trim();
            String msg = "";
            ll_discount.setVisibility(View.VISIBLE);
            msg = "You will be charged Rs. " + decimalFormat.format(Integer.parseInt(price)) + " for " + servicetype + " of " + patientName.trim() + " at " + centerName + ".";

            GlobalClass.SetText(tv_discount, msg);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private boolean checkRemarksValidation() {
        if (edt_remarks.getText().toString().startsWith(",")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.remarks_val), 2);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(".")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.remarks_val), 2);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith("-")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.remarks_val), 2);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(" ")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.space_val_remarks), 2);
            edt_remarks.requestFocus();
            return false;
        }
        return true;
    }

    private boolean emailValidation(String stremail) {
        if (stremail.startsWith(" ")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.email_no_space), 2);
            edt_email.requestFocus();
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (stremail == null)
            return false;
        return pat.matcher(stremail).matches();

    }

    private boolean checkValidation() {

        if (TextUtils.isEmpty(edt_name.getText().toString())) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_enter_name), 2);
            //edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().startsWith(" ")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.space_val_name), 2);
            edt_name.requestFocus();
            return false;
        }


        if (edt_name.getText().toString().length() < 2) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.valid_name), 2);
            edt_name.requestFocus();
            return false;
        }
        if (edt_mname.getText().toString().length() > 0) {
            if (edt_mname.getText().toString().startsWith(" ")) {
                GlobalClass.showTastyToast(getActivity(), getString(R.string.space_val_name), 2);
                edt_mname.requestFocus();
                return false;
            }
        }
        if (edt_lname.getText().toString().length() > 0) {
            if (edt_lname.getText().toString().startsWith(" ")) {
                GlobalClass.showTastyToast(getActivity(), getString(R.string.space_val_name), 2);
                edt_lname.requestFocus();
                return false;
            }
        }

        if (gendername.equalsIgnoreCase("")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_gender_val), 2);
            return false;
        }

        if (diabeticsname.equalsIgnoreCase("")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_diab_val), 2);
            return false;
        }

        if (TextUtils.isEmpty(str_age)) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_slc), 2);
            return false;
        } else {
            if (str_age.equalsIgnoreCase("DOB")) {
                if (tv_date.getText().toString().equalsIgnoreCase("")) {
                    GlobalClass.showTastyToast(getActivity(), getString(R.string.str_dob_val), 2);
                    return false;
                }
            } else {
                if (edt_age.getText().toString().equalsIgnoreCase("")) {
                    GlobalClass.showTastyToast(getActivity(), getString(R.string.str_age), 2);
                    return false;
                }
            }
        }

        if (txt_appdate.getText().toString().equalsIgnoreCase("")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_appdt_val), 2);
            return false;
        }

        if (!GlobalClass.isNull(str_slot) && str_slot.equalsIgnoreCase("Select Slot*")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_slot), 2);
            return false;
        }
        if (edt_mobile.getText().toString().isEmpty()) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_enter_mobile_number), 2);
            edt_mobile.requestFocus();
            return false;
        }
        if (edt_mobile.getText().toString().length() < 10) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_should_be_ten_digits), 2);
            edt_mobile.requestFocus();
            return false;
        }

        if (!edt_mobile.getText().toString().startsWith("9") && !edt_mobile.getText().toString().startsWith("8")
                && !edt_mobile.getText().toString().startsWith("7") && !edt_mobile.getText().toString().startsWith("6")) {
            GlobalClass.showTastyToast(getActivity(), getString(R.string.str_valid_mobile_no), 2);
            edt_mobile.requestFocus();
            return false;
        }

        return true;
    }

    private void callServiceTypesAPI(String centerId) {
        try {
            if (ControllersGlobalInitialiser.petctServiceTypesController != null) {
                ControllersGlobalInitialiser.petctServiceTypesController = null;
            }
            ControllersGlobalInitialiser.petctServiceTypesController = new PETCTServiceTypes_Controller(getActivity(), PETCT_Frag.this, header);
            ControllersGlobalInitialiser.petctServiceTypesController.getPetctService(header, centerId, user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setServiceData(final ArrayList<String> bookType) {
        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bookType);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_service.setAdapter(bookAdapter);

        if (GlobalClass.CheckArrayList(bookType)) {
            if (bookType.size() == 2) {
                spn_service.setSelection(1);
            }
        }

    }

    private void showDialog(final boolean b) {
        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.lay_dialog, null);


            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setView(alertLayout);
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(ToastFile.update_ledger);
            builder.setCancelable(false);
            builder.setPositiveButton("UPDATE LEDGER",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            startActivityForResult(new Intent(context, Payment_Activity.class), 1);
                            dialog.dismiss();
                        }
                    });

            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (b == false) {
                        dialog.dismiss();
                    } else {
                        spn_service.setSelection(0);
                    }
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

    }


    public void getGenerateTokenResponse(JSONObject response) {
        try {
            TokenModel tokenResponseModel = gson.fromJson(response.toString(), TokenModel.class);
            if (tokenResponseModel != null) {
                if (!GlobalClass.isNull(tokenResponseModel.getRespId()) && tokenResponseModel.getRespId().equalsIgnoreCase(Constants.RES0000)) {
                    if (!GlobalClass.isNull(tokenResponseModel.getAccess_token()) && !GlobalClass.isNull(tokenResponseModel.getToken_type())) {

                        if (cd.isConnectingToInternet()) {
                            try {
                                if (ControllersGlobalInitialiser.getCenterlist_controller != null) {
                                    ControllersGlobalInitialiser.getCenterlist_controller = null;
                                }
                                ControllersGlobalInitialiser.getCenterlist_controller = new GetCenterlist_Controller(PETCT_Frag.this, getActivity());
                                ControllersGlobalInitialiser.getCenterlist_controller.getcenterlist(tokenResponseModel, user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            GlobalClass.showTastyToast(activity, ToastFile.intConnection, 2);
                        }


                    }
                } else {

                    GlobalClass.showTastyToast(getActivity(), tokenResponseModel.getResponseMessage(), 2);
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCenterListResponse(List<CenterList_Model> body, String header) {
        this.header = header;
        ArrayList<String> centerLocation = new ArrayList<>();
        centerLocation.add("Select City*");
        try {
            centerListModels = new ArrayList<>();
            centerListModels.clear();
            centerListModels.addAll(body);

            if (GlobalClass.CheckArrayList(centerListModels)) {
                for (int i = 0; i < centerListModels.size(); i++) {
                    centerLocation.add(centerListModels.get(i).getLocation().trim());
                }
            }

            Log.e(TAG, "centerLocation size ---->" + centerLocation.size());

            if (centerLocation.size() == 2) {
                centerLocation.remove(0);
                setDataToCitySpinner(centerLocation);
            } else {
                setDataToCitySpinner(centerLocation);
            }

        } catch (Exception e) {
            Log.e("TAG", "on failure: " + e.getLocalizedMessage());
        }
    }

    private void setDataToCitySpinner(ArrayList<String> centerName) {
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, centerName);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_city.setAdapter(cityAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            try {
                if (Integer.parseInt(closingbal) < Integer.parseInt(price)) {
                    spn_service.setSelection(0);
                    showDialog(false);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (GlobalClass.isNetworkAvailable(getActivity())) {
                ((ManagingTabsActivity) getActivity()).getProfileDetails(getContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSlotResponse(List<SlotModel> body) {

        ArrayList<String> slotlist = new ArrayList<>();
        slotlist.add("Select Slot*");
        try {
            slotModelList = new ArrayList<>();
            slotModelList.clear();
            slotModelList.addAll(body);

            if (GlobalClass.CheckArrayList(slotModelList)) {
                for (int i = 0; i < slotModelList.size(); i++) {
                    slotlist.add(slotModelList.get(i).getSlot().trim());
                }

                ArrayAdapter<String> slotadapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, slotlist);
                slotadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_slot.setAdapter(slotadapter);

                spn_slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        str_slot = spn_slot.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        } catch (Exception e) {
            Log.e("TAG", "on failure: " + e.getLocalizedMessage());
        }
    }

    public void getserviceResponse(List<ServiceModel> body, ProgressDialog progressDialog) {
        if (body != null) {
            GlobalClass.hideProgress(getContext(), progressDialog);
            ArrayList<String> servicelist = new ArrayList<>();
            servicelist.add("Select Scan Type*");
            serviceListModels = new ArrayList<>();
            serviceListModels.addAll(body);

            if (GlobalClass.CheckArrayList(serviceListModels)) {
                for (int i = 0; i < serviceListModels.size(); i++) {
                    serviceName = serviceListModels.get(i).getServiceName();
                    servicelist.add(serviceName);
                }
            }

            if (cd.isConnectingToInternet()) {
                if (servicelist.size() == 2) {
                    setServiceData(servicelist);
                } else {
                    setServiceData(servicelist);
                }
            } else {
                GlobalClass.showTastyToast(getActivity(), MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        }
    }

    public void getMyprofileRespponse(JSONObject response) {
        try {
            if (response != null) {

                SharedPreferences.Editor saveProfileDetails = getActivity().getSharedPreferences("profile", 0).edit();
                saveProfileDetails.putString("balance", response.getString(Constants.balance));
                saveProfileDetails.commit();

                if (!GlobalClass.isNull(response.getString(Constants.balance))) {
                    closingbal = response.getString(Constants.balance);
                    GlobalClass.SetText(txt_ledgerbal, getResources().getString(R.string.str_legderbal) + " : " + "Rs " + GlobalClass.currencyFormat(response.getString(Constants.balance)));
                } else {
                    GlobalClass.SetText(txt_ledgerbal, "");
                    closingbal = "0";
                }


            } else {
                GlobalClass.showTastyToast(getActivity(), MessageConstants.SOMETHING_WENT_WRONG, 2);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
