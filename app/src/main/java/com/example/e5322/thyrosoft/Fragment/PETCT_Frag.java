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
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ConfirmbookDetail;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GenerateTokenController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BookTypesModel;
import com.example.e5322.thyrosoft.Models.CenterList_Model;
import com.example.e5322.thyrosoft.Models.ServiceModel;
import com.example.e5322.thyrosoft.Models.SlotModel;
import com.example.e5322.thyrosoft.Models.TokenModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdsmdg.tastytoast.TastyToast;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ProgressDialog progressDialog;
    Date To_date;
    RelativeLayout rel_dob;
    String user;
    ImageView enter_arrow_enter, enter_arrow_entered;
    private String myFormat = "dd-MM-yyyy";
    private Spinner spn_city, spn_service, spn_slot;
    private Activity activity;
    private String TAG = PETCT_Frag.class.getSimpleName();
    private EditText edt_name, edt_mname, edt_lname, edt_mobile, edt_email, edt_remarks, edt_coupon, edt_age;
    private Gson gson;
    private String CLIENT_TYPE;
    private ConnectionDetector cd;
    private TextView tv_discount, enter;
    private boolean resultcheck = false;
    private View v;
    private boolean isViewShown = false;
    private LinearLayout ll_discount, ll_coupon, sub_lin, enter_ll_unselected;
    private Context context;
    private List<CenterList_Model> centerListModels;
    private ArrayList<BookTypesModel> bookTypesModels;
    private List<ServiceModel> serviceListModels;
    private List<SlotModel> slotModelList;
    private int newAmount;
    private String serviceId = "";
    private String couponCode, cal_age;
    private String fullname, gendername = "", diabeticsname = "", str_slot, str_age = "";
    private String serviceName = "", To_formateDate;
    private DecimalFormat decimalFormat;
    private TextView tv_date, txt_dialog, txt_appoint_dialog, txt_appdate, txt_ledgerbal;
    private String putDate, showDate;
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
            txt_appdate.setText(putDate);

            getSlot(centerId);

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
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");
    }


    private void updateLabel() {
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        fromdate = returndate(fromdate, putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);
        tv_date.setText(putDate);
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

        RequestQueue queue = GlobalClass.setVolleyReq(context);
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(context);
        Log.e(TAG, "Get my Profile ---->" + Api.Cloud_base + api_key + "/" + user + "/" + "getmyprofile");


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + api_key + "/" + user + "/" + "getmyprofile",
                new com.android.volley.Response.Listener<JSONObject>() {
                    public String tsp_img;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                GlobalClass.hideProgress(context, progressDialog);

                                SharedPreferences.Editor saveProfileDetails = getActivity().getSharedPreferences("profile", 0).edit();
                                saveProfileDetails.putString("balance", response.getString(Constants.balance));
                                saveProfileDetails.commit();

                                if (!GlobalClass.isNull(response.getString(Constants.balance))) {
                                    closingbal = response.getString(Constants.balance);
                                    GlobalClass.SetText(txt_ledgerbal, getResources().getString(R.string.str_legderbal) + " : " + "Rs " + GlobalClass.currencyFormat(response.getString(Constants.balance)));
                                } else {
                                    txt_ledgerbal.setText("");
                                    closingbal = "0";
                                }


                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "getProfileDetails: url" + jsonObjectRequest);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
    }


    private void initUI(View v) {

        SharedPreferences preferences = getActivity().getSharedPreferences("profile", 0);
        closingbal = preferences.getString("balance", "");
        Log.e(TAG, "BALANCE ---->" + closingbal);
        // closingbal = "1000000";

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
                    GlobalClass.toastyError(getActivity(), getString(R.string.email_no_space), false);
                    if (enteredString.length() > 0) {
                        edt_email.setText(enteredString.substring(1));
                    } else {
                        edt_email.setText("");
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
                    GlobalClass.toastyError(getActivity(), getString(R.string.space_val_remarks), false);

                    if (enteredString.length() > 0) {
                        edt_remarks.setText(enteredString.substring(1));
                    } else {
                        edt_remarks.setText("");
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


        try {
            if (!GlobalClass.isNull(closingbal)) {
                GlobalClass.SetText(txt_ledgerbal, getResources().getString(R.string.str_legderbal) + " : " + "Rs " + GlobalClass.currencyFormat(closingbal));
            } else {
                txt_ledgerbal.setText("");
                closingbal = "0";
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
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
            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
        }

    }

    private void initListners() {

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
                    tv_date.setText("");
                }

            }
        });

        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = spn_city.getSelectedItem().toString().trim();


                if (!city.equalsIgnoreCase("Select City*")) {
                    if (centerListModels != null && centerListModels.size() > 0) {
                        for (int i = 0; i < centerListModels.size(); i++) {
                            if (city.equalsIgnoreCase(centerListModels.get(i).getLocation())) {
                                centerId = centerListModels.get(i).getCenterId();
                                centerName = centerListModels.get(i).getName();
                                centerAddress = centerListModels.get(i).getAddress();
                                centerCity = centerListModels.get(i).getLocation();
                            }
                        }

                        if (cd.isConnectingToInternet()) {
                            callServiceTypesAPI(centerId);
                            getSlot(centerId);
                        } else {
                            GlobalClass.toastyError(context, MessageConstants.CHECK_INTERNET_CONN, false);
                        }

                    }

                } else if (city.equalsIgnoreCase("Select City*")) {
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
                    tv_discount.setText("");
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
                                GlobalClass.toastyError(getActivity(), MessageConstants.CHECK_INTERNET_CONN, false);
                            }
                        } else {
                            TastyToast.makeText(getContext(), "Enter valid email id !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
                                GlobalClass.toastyError(getActivity(), MessageConstants.CHECK_INTERNET_CONN, false);
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

    private void getSlot(String centerId) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(context);
            APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.SCANSOAPI).create(APIInteface.class);
            Call<List<SlotModel>> responseCall = apiInterface.getslot(header, GlobalClass.formatDate("dd-mm-yyyy", "yyyy-mm-dd", txt_appdate.getText().toString()), centerId);
            Log.e("TAG", "SLOT URL --->" + responseCall.request().url());

            responseCall.enqueue(new Callback<List<SlotModel>>() {
                @Override
                public void onResponse(Call<List<SlotModel>> call, Response<List<SlotModel>> response) {
                    try {
                        if (response.body() != null) {
                            GlobalClass.hideProgress(context, progressDialog);
                            ArrayList<String> slotlist = new ArrayList<>();
                            slotlist.add("Select Slot*");
                            try {
                                slotModelList = new ArrayList<>();
                                slotModelList.clear();
                                slotModelList.addAll(response.body());
                                if (slotModelList != null) {
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
                        } else {
                            GlobalClass.hideProgress(context, progressDialog);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<SlotModel>> call, Throwable t) {

                }
            });
        } else {
            GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
        }


    }


    public void clearAllFields() {

        edt_name.setText("");
        edt_name.requestFocus();
        edt_mname.setText("");
        edt_lname.setText("");
        edt_mobile.setText("");
        edt_email.setText("");
        edt_remarks.setText("");

        try {
            rb_gender.setChecked(false);
            rb_dib.setChecked(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gendername = "";
        diabeticsname = "";

        tv_date.setText("");
        txt_appdate.setText("");

        tv_discount.setVisibility(View.GONE);
        tv_discount.setText("");


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

            tv_discount.setText(msg);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private boolean checkRemarksValidation() {
        if (edt_remarks.getText().toString().startsWith(",")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.remarks_val), false);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(".")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.remarks_val), false);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith("-")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.remarks_val), false);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(" ")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.space_val_remarks), false);
            edt_remarks.requestFocus();
            return false;
        }
        return true;
    }

    private boolean emailValidation(String stremail) {
        if (stremail.startsWith(" ")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.email_no_space), false);
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

        if (GlobalClass.isNull(edt_name.getText().toString())) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_enter_name), false);
            //edt_name.requestFocus();
            return false;
        }
        if (edt_name.getText().toString().startsWith(" ")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.space_val_name), false);
            edt_name.requestFocus();
            return false;
        }


        if (edt_name.getText().toString().length() < 2) {
            GlobalClass.toastyError(getActivity(), getString(R.string.valid_name), false);
            edt_name.requestFocus();
            return false;
        }
        if (edt_mname.getText().toString().length() > 0) {
            if (edt_mname.getText().toString().startsWith(" ")) {
                GlobalClass.toastyError(getActivity(), getString(R.string.space_val_name), false);
                edt_mname.requestFocus();
                return false;
            }
        }
        if (edt_lname.getText().toString().length() > 0) {
            if (edt_lname.getText().toString().startsWith(" ")) {
                GlobalClass.toastyError(getActivity(), getString(R.string.space_val_name), false);
                edt_lname.requestFocus();
                return false;
            }
        }

        if (gendername.equalsIgnoreCase("")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_gender_val), false);
            return false;
        }

        if (diabeticsname.equalsIgnoreCase("")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_diab_val), false);
            return false;
        }

        if (GlobalClass.isNull(str_age)) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_slc), false);
            return false;
        } else {
            if (str_age.equalsIgnoreCase("DOB")) {
                if (tv_date.getText().toString().equalsIgnoreCase("")) {
                    GlobalClass.toastyError(getActivity(), getString(R.string.str_dob_val), false);
                    return false;
                }
            } else {
                if (edt_age.getText().toString().equalsIgnoreCase("")) {
                    GlobalClass.toastyError(getActivity(), getString(R.string.str_age), false);
                    return false;
                }
            }
        }

        int conertage = Integer.parseInt(edt_age.getText().toString());
        if (conertage > 120) {
            Toast.makeText(getActivity(), ToastFile.invalidage, Toast.LENGTH_SHORT).show();
            edt_age.requestFocus();
            return false;

        }

        if (txt_appdate.getText().toString().equalsIgnoreCase("")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_appdt_val), false);
            return false;
        }


        if (str_slot.equalsIgnoreCase("Select Slot*")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_slot), false);
            return false;
        }
        if (edt_mobile.getText().toString().isEmpty()) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_enter_mobile_number), false);
            edt_mobile.requestFocus();
            return false;
        }
        if (edt_mobile.getText().toString().length() < 10) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_should_be_ten_digits), false);
            edt_mobile.requestFocus();
            return false;
        }

        if (!edt_mobile.getText().toString().startsWith("9") && !edt_mobile.getText().toString().startsWith("8")
                && !edt_mobile.getText().toString().startsWith("7") && !edt_mobile.getText().toString().startsWith("6")) {
            GlobalClass.toastyError(getActivity(), getString(R.string.str_valid_mobile_no), true);
            edt_mobile.requestFocus();
            return false;
        }

        return true;
    }

    private void callServiceTypesAPI(String centerId) {
        progressDialog = GlobalClass.ShowprogressDialog(context);
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.SCANSOAPI).create(APIInteface.class);
        Log.e(TAG, "HEADER ----->" + header);
        Call<List<ServiceModel>> responseCall = apiInterface.getservicelist(header, centerId, user);
        Log.e("TAG", "SERVICE URL --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<List<ServiceModel>>() {
            @Override
            public void onResponse(Call<List<ServiceModel>> call, Response<List<ServiceModel>> response) {
                try {
                    if (response.body() != null) {
                        GlobalClass.hideProgress(getContext(), progressDialog);
                        ArrayList<String> servicelist = new ArrayList<>();
                        servicelist.add("Select Scan Type*");
                        serviceListModels = new ArrayList<>();
                        serviceListModels.addAll(response.body());

                        if (serviceListModels != null && serviceListModels.size() > 0) {
                            for (int i = 0; i < serviceListModels.size(); i++) {
                                /* if (serviceListModels.get(i).getServiceId().toUpperCase().equalsIgnoreCase("F")) {*/
                                serviceName = serviceListModels.get(i).getServiceName();
                                servicelist.add(serviceName);

                                /*    }*/
                            }
                        }

                        if (cd.isConnectingToInternet()) {
                            if (servicelist.size() == 2) {
                                // servicelist.remove(0);
                                setServiceData(servicelist);
                            } else {
                                setServiceData(servicelist);
                            }

                        } else {
                            GlobalClass.toastyError(context, MessageConstants.CHECK_INTERNET_CONN, false);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ServiceModel>> call, Throwable t) {

            }
        });

    }

    private void setServiceData(final ArrayList<String> bookType) {
        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, bookType);
        bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_service.setAdapter(bookAdapter);

        if (bookType != null) {
            if (bookType.size() == 2) {
                spn_service.setSelection(1);
            }
        }

        spn_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servicetype = spn_service.getSelectedItem().toString().trim();

                if (serviceListModels != null) {
                    for (int i = 0; i < serviceListModels.size(); i++) {
                        if (serviceListModels.get(i).getServiceName().toUpperCase().equalsIgnoreCase(servicetype)) {
                            price = "" + serviceListModels.get(i).getNHF_Rate();
                            serviceId = serviceListModels.get(i).getServiceId();
                            serviceName = serviceListModels.get(i).getServiceName();
                        }
                    }


                    Log.e(TAG, "service type ---> " + servicetype);

                    if (!servicetype.equalsIgnoreCase("Select Scan Type*")) {
                        if (spn_city.getSelectedItem().toString().equalsIgnoreCase("Select City*")) {
                            spn_service.setSelection(0);
                            tv_discount.setVisibility(View.GONE);
                            tv_discount.setText("");
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

    }

    private void showDialog(final boolean b) {
        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.lay_dialog, null);

            TextView txt_ledgerbal = (TextView) alertLayout.findViewById(R.id.txt_ledgerbal);

            if (!GlobalClass.isNull(closingbal)) {
                txt_ledgerbal.setText(Html.fromHtml("Your ledger balance is Rs " + GlobalClass.currencyFormat(closingbal) + ". Kindly make payment to proceed with booking. For queries write to " + "<font color='#05128c'><u>" + "accounts@nueclear.com" + "</u></font>"));

                //  GlobalClass.SetText(txt_ledgerbal, "Your ledger balance is Rs " + GlobalClass.currencyFormat(closingbal) + ". Kindly make payment to proceed with booking. For queries write to " + Html.fromHtml("<u>" + "accounts@nueclear.com" + "</u>"));
            } else {
                txt_ledgerbal.setText(Html.fromHtml("Your ledger balance is Rs " + 0 + ". Kindly make payment to proceed with booking.  For queries write to " + "<font color='#05128c'><u>" + "accounts@nueclear.com" + "</u></font>"));

            }


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
            /*builder.setPositiveButton("UPDATE LEDGER",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            startActivityForResult(new Intent(context, Payment_Activity.class), 1);
                            dialog.dismiss();
                        }
                    });*/

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
                if (tokenResponseModel.getRespId().equalsIgnoreCase(Constants.RES0000)) {
                    if (!GlobalClass.isNull(tokenResponseModel.getAccess_token()) && !GlobalClass.isNull(tokenResponseModel.getToken_type())) {
                        if (cd.isConnectingToInternet()) {
                            if (tokenResponseModel != null) {
                                getCenterList(tokenResponseModel);
                            }
                        }
                    } else
                        GlobalClass.hideProgress(context, progressDialog);
                } else {
                    GlobalClass.hideProgress(context, progressDialog);
                    GlobalClass.toastyError(activity, tokenResponseModel.getResponseMessage(), false);
                }
            } else {
                GlobalClass.hideProgress(context, progressDialog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCenterList(TokenModel tokenResponseModel) {
        progressDialog = GlobalClass.ShowprogressDialog(context);
        header = tokenResponseModel.getToken_type() + " " + tokenResponseModel.getAccess_token();
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.SCANSOAPI).create(APIInteface.class);
        Call<List<CenterList_Model>> responseCall = apiInterface.getcenterList(header, user);
        Log.e("TAG", "header --->" + header);
        Log.e("TAG", "S C A N S O URL --->" + responseCall.request().url());


        responseCall.enqueue(new Callback<List<CenterList_Model>>() {
            @Override
            public void onResponse(Call<List<CenterList_Model>> call, Response<List<CenterList_Model>> response) {
                try {
                    if (response.body() != null) {
                        GlobalClass.hideProgress(context, progressDialog);
                        ArrayList<String> centerLocation = new ArrayList<>();
                        centerLocation.add("Select City*");
                        try {
                            centerListModels = new ArrayList<>();
                            centerListModels.clear();
                            centerListModels.addAll(response.body());

                            if (centerListModels != null) {
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

                    } else {
                        GlobalClass.hideProgress(context, progressDialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CenterList_Model>> call, Throwable t) {
                Log.e("TAG", "on failure: " + t.getLocalizedMessage());
            }
        });
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
            if (GlobalClass.checkSync(getActivity(), "Profile")) {
                if (GlobalClass.isNetworkAvailable(getActivity())) {
                    ((ManagingTabsActivity) getActivity()).getProfileDetails(getContext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
