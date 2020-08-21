package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.Windup_adapter;
import com.example.e5322.thyrosoft.Controller.ConfirmConsignmentEntry_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.FetchwoeListDoneByTSP_Controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.Multiplewindup_Controller;
import com.example.e5322.thyrosoft.Controller.WindupControllerr;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.CountInterface;
import com.example.e5322.thyrosoft.Models.RequestModels.WindupRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.CommonResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.ConsignmentCountResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.WOE_Model_Patient_Details;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Wind_up_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Wind_up_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wind_up_fragment extends RootFragment implements CountInterface {
    static final int DATE_DIALOG_ID = 999;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RequestQueue PostQueOtp;
    public static RequestQueue PostQueueForConsignment;
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
    private static ManagingTabsActivity mContext;
    ImageView enter_arrow_enterss, enter_arrow_enteredss;
    View viewMain;
    Windup_adapter windup_adapter;
    EditText edtSearch;
    LinearLayout linearlayout2, pick_up_ll;
    String DateToPass;
    TextView wind_up, woe_cal, wind_up_multiple, pick_up_txt;
    RequestQueue requestQueue, requestQueueWindup;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView enetered_consign, enter_consign, title_txt;
    LinearLayout enter_entered_layout_consign, enter_ll_unselected_consign, unchecked_entered_ll_consign, packaging_details_ll;
    WOE_Model_Patient_Details woe_model_patient_details;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayList;
    ArrayList<String> getWindupCount;
    LinearLayout enter_entered_layout;
    View view_1, view_2;
    LinearLayout offline_img;
    Calendar myCalendar;
    LinearLayout windup_ll_below;
    String putDate, getFormatDate;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    SharedPreferences prefs;
    String user, passwrd, access, api_key;
    String blockCharacterSet = "~#^|$%&*!+:`";
    LinearLayout logistic_ll;
    Activity mActivity;
    ConnectionDetector cd;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private OnFragmentInteractionListener mListener;
    private String countData;
    private String outputDateStr;
    private String passToAPI;
    private String DatePassToApi;
    private String client_type;

    public Wind_up_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Woe_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Wind_up_fragment newInstance(String param1, String param2) {

        Wind_up_fragment fragment = new Wind_up_fragment();
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

        viewMain = (View) inflater.inflate(R.layout.fragment_woe_add_test, container, false);

        initViews();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        GlobalClass.SetText(woe_cal, sdf.format(d));

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            linearlayout2.setVisibility(View.GONE);

        } else {
            offline_img.setVisibility(View.GONE);
            linearlayout2.setVisibility(View.VISIBLE);
        }


        getActivity().setTitle("WOE");
        Date dateNew = new Date();
        SimpleDateFormat sdfToday = new SimpleDateFormat("dd-MM-yyyy");
        DateToPass = sdfToday.format(dateNew);
        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        client_type = prefs.getString("CLIENT_TYPE", null);

        if (!GlobalClass.isNull(client_type)) {
            if (!GlobalClass.isNull(client_type) && client_type.equalsIgnoreCase("OLC")) {
                logistic_ll.setVisibility(View.GONE);//--TODO if client type is OLC then make logistic_ll visible(Logistic charges)
            } else {
                logistic_ll.setVisibility(View.GONE);
            }
        }


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            linearlayout2.setVisibility(View.GONE);
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        } else {
            offline_img.setVisibility(View.GONE);
            linearlayout2.setVisibility(View.VISIBLE);
            fetchWoeListDoneByTSP();
        }
        myCalendar = Calendar.getInstance();

        enter_consign.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enterss.setVisibility(View.VISIBLE);
        enetered_consign.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_enteredss.setVisibility(View.GONE);

        initListner();

        return viewMain;
    }

    private void initListner() {

        unchecked_entered_ll_consign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO blocked consignment entry if it is done for the day
                checkConsgnmentFortheDay();
            }
        });

        enter_ll_unselected_consign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_consign.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enterss.setVisibility(View.VISIBLE);
                enetered_consign.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enteredss.setVisibility(View.GONE);
                if (cd.isConnectingToInternet()) {
                    fetchWoeListDoneByTSP();
                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
                }
            }
        });


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

                        windup_adapter = new Windup_adapter(mContext, filterPatientsArrayList, Wind_up_fragment.this);
                        recyclerView.setAdapter(windup_adapter);
                    }
                }

            }
        });

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        wind_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Wind-up All !");
                builder.setMessage(getResources().getString(R.string.windup));
                builder.setCancelable(false);
                builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd

                        Date date = null;
                        try {
                            date = inputFormat.parse(DateToPass);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        DatePassToApi = outputFormat.format(date);
                        requestQueueWindup = Volley.newRequestQueue(getContext());//2c=/TAM03/TAM03136166236000078/geteditdata

                        try {
                            if (ControllersGlobalInitialiser.windupControllerr != null) {
                                ControllersGlobalInitialiser.windupControllerr = null;
                            }
                            ControllersGlobalInitialiser.windupControllerr = new WindupControllerr(mActivity, Wind_up_fragment.this);
                            ControllersGlobalInitialiser.windupControllerr.getwindupcontroller(api_key,user,DatePassToApi,requestQueueWindup);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
            }
        });

        wind_up_multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalClass.CheckArrayList(GlobalClass.windupBarcodeList)) {
                    String SEPARATOR = ",";

                    StringBuilder csvBuilder = new StringBuilder();

                    for (String patientlist : GlobalClass.windupBarcodeList) {
                        csvBuilder.append(patientlist);
                        csvBuilder.append(SEPARATOR);
                    }
                    String csv = csvBuilder.toString();
                    csv = csv.substring(0, csv.length() - SEPARATOR.length());
                    Log.v("TAG", csv);

                    PostQueOtp = Volley.newRequestQueue(getContext());
                    JSONObject jsonObject = null;
                    try {
                        WindupRequestModel requestModel = new WindupRequestModel();
                        requestModel.setApi_key(api_key);
                        requestModel.setPatient_id(csv);
                        requestModel.setTsp(user);
                        requestModel.setDate(passToAPI);

                        String json = new Gson().toJson(requestModel);
                        jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    try {
                        if (ControllersGlobalInitialiser.multiplewindup_controller != null) {
                            ControllersGlobalInitialiser.multiplewindup_controller = null;
                        }
                        ControllersGlobalInitialiser.multiplewindup_controller = new Multiplewindup_Controller(mActivity, Wind_up_fragment.this);
                        ControllersGlobalInitialiser.multiplewindup_controller.multiplewindupcontroller(jsonObject, PostQueOtp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    GlobalClass.showTastyToast(mActivity, ToastFile.slt_name_for_windup, 2);
                }
            }
        });
    }

    private void initViews() {
        wind_up = (TextView) viewMain.findViewById(R.id.wind_up);
        wind_up_multiple = (TextView) viewMain.findViewById(R.id.wind_up_multiple);
        woe_cal = (TextView) viewMain.findViewById(R.id.woe_cal);
        edtSearch = (EditText) viewMain.findViewById(R.id.edtSearch);
        windup_ll_below = (LinearLayout) viewMain.findViewById(R.id.windup_ll_below);
        offline_img = (LinearLayout) viewMain.findViewById(R.id.offline_img);
        recyclerView = (RecyclerView) viewMain.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        enetered_consign = (TextView) viewMain.findViewById(R.id.enetered_consign);
        pick_up_txt = (TextView) viewMain.findViewById(R.id.pick_up_txt);
        enter_consign = (TextView) viewMain.findViewById(R.id.enter_consign);
        linearlayout2 = (LinearLayout) viewMain.findViewById(R.id.linearlayout2);
        pick_up_ll = (LinearLayout) viewMain.findViewById(R.id.pick_up_ll);
        logistic_ll = (LinearLayout) viewMain.findViewById(R.id.logistic_ll);
        enter_entered_layout = (LinearLayout) viewMain.findViewById(R.id.enter_entered_layout);
        enter_entered_layout_consign = (LinearLayout) viewMain.findViewById(R.id.enter_entered_layout_consign);
        unchecked_entered_ll_consign = (LinearLayout) viewMain.findViewById(R.id.unchecked_entered_ll_consign);
        enter_ll_unselected_consign = (LinearLayout) viewMain.findViewById(R.id.enter_ll_unselected_consign);
        enter_arrow_enteredss = (ImageView) viewMain.findViewById(R.id.enter_arrow_enteredss);
        enter_arrow_enterss = (ImageView) viewMain.findViewById(R.id.enter_arrow_enterss);
        view_1 = (View) viewMain.findViewById(R.id.view_1);
        view_2 = (View) viewMain.findViewById(R.id.view_2);
        enter_entered_layout.setVisibility(View.GONE);

        view_1.setVisibility(View.VISIBLE);
        view_2.setVisibility(View.VISIBLE);
        windup_ll_below.setVisibility(View.VISIBLE);

        enter_entered_layout_consign.setVisibility(View.VISIBLE);
        enter_consign.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enterss.setVisibility(View.VISIBLE);
        enetered_consign.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_enteredss.setVisibility(View.GONE);
    }

    private void checkConsgnmentFortheDay() {
        PostQueueForConsignment = Volley.newRequestQueue(getContext());


        try {
            if (ControllersGlobalInitialiser.confirmConsignmentEntry_controller != null) {
                ControllersGlobalInitialiser.confirmConsignmentEntry_controller = null;
            }
            ControllersGlobalInitialiser.confirmConsignmentEntry_controller = new ConfirmConsignmentEntry_Controller(mActivity, Wind_up_fragment.this);
            ControllersGlobalInitialiser.confirmConsignmentEntry_controller.getconfirmcongsgment(user,PostQueueForConsignment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enterNextFragment() {
        Consignment_fragment a2Fragment = new Consignment_fragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_mainLayout, a2Fragment).commitAllowingStateLoss();

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


        getActivity().setTitle("WOE " + outputDateStr);
        GlobalClass.SetText(woe_cal, putDate);


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            linearlayout2.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            linearlayout2.setVisibility(View.VISIBLE);
            fetchPatientDetails();
        }

    }


    private void fetchPatientDetails() {

        DateToPass = getActivity().getTitle().toString().substring(4, getActivity().getTitle().toString().length() - 0);

        GlobalClass.SetText(woe_cal, DateToPass);

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            linearlayout2.setVisibility(View.GONE);
            GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
        } else {
            offline_img.setVisibility(View.GONE);
            linearlayout2.setVisibility(View.VISIBLE);
            fetchWoeListDoneByTSP();
        }

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

        requestQueue = Volley.newRequestQueue(mContext);
        String url = Api.WORKoRDEReNTRYfIRSTpAGE + "" + api_key + "/WORK_ORDERS/" + "" + user + "/" + passToAPI + "/key/value";
        Log.e(TAG, "TAG: WORKoRDEReNTRYfIRSTpAGE-->" + url);
        try {
            if (ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller != null) {
                ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller = null;
            }
            ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller = new FetchwoeListDoneByTSP_Controller(mActivity, Wind_up_fragment.this);
            ControllersGlobalInitialiser.fetchWoeListDoneByTSP_controller.woelistdone_controller(requestQueue, url);
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


    @Override
    public void getclickcount(int count) {
        GlobalClass.SetText(wind_up_multiple, "Wind up( " + count + " )");
    }

    public void getwindupResponse(CommonResponseModel responseModel) {

        if (responseModel != null) {
            if (!GlobalClass.isNull(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase(Constants.RES0000)) {

                GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 1);
                fetchWoeListDoneByTSP();
                GlobalClass.windupBarcodeList = new ArrayList<>();
                Wind_up_fragment a2Fragment = new Wind_up_fragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_mainLayout, a2Fragment).commitAllowingStateLoss();


            } else {
                GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 2);
            }
        } else {
            GlobalClass.showTastyToast(mActivity, ToastFile.something_went_wrong, 2);
        }
    }

    public void getwoedonelistResponse(JSONObject response) {
        Log.e(TAG, "onResponse: response" + response);

        String responsetoshow = response.optString("response", "");

        if (!GlobalClass.isNull(responsetoshow) && responsetoshow.equalsIgnoreCase(caps_invalidApikey)) {
            GlobalClass.redirectToLogin(getActivity());
        } else {
            Gson gson = new Gson();
            woe_model_patient_details = new WOE_Model_Patient_Details();
            woe_model_patient_details = gson.fromJson(response.toString(), WOE_Model_Patient_Details.class);
            patientsArrayList = new ArrayList<>();
            getWindupCount = new ArrayList<>();


            SharedPreferences preferences = mContext.getSharedPreferences("saveWOEinDraft", MODE_PRIVATE);
            String json2 = preferences.getString("DraftWOE", null);
            if (json2 != null) {
                Gson gson1 = new Gson();
                String json = preferences.getString("DraftWOE", null);
                if (json2 != null) {
                    MyPojoWoe obj = gson1.fromJson(json, MyPojoWoe.class);
                }
            }

            int getPersentfiveB2b = Integer.parseInt(woe_model_patient_details.getLogcharge());

            if (!GlobalClass.isNull(woe_model_patient_details.getPersentfiveB2b())
                    && !woe_model_patient_details.getPersentfiveB2b().equalsIgnoreCase("0") && getPersentfiveB2b != 0) {
                pick_up_ll.setVisibility(View.VISIBLE);
                GlobalClass.SetText(pick_up_txt, woe_model_patient_details.getLogcharge());

            } else {
                pick_up_ll.setVisibility(View.VISIBLE);
                GlobalClass.SetText(pick_up_txt, "0");
            }

            //set Adpter
            if (GlobalClass.CheckArrayList(woe_model_patient_details.getPatients())) {
                for (int i = 0; i < woe_model_patient_details.getPatients().size(); i++) {
                    patientsArrayList.add(woe_model_patient_details.getPatients().get(i));

                    if (GlobalClass.CheckArrayList(patientsArrayList)){
                        for (int j = 0; j < patientsArrayList.size(); j++) {
                            getWindupCount.add(String.valueOf(patientsArrayList.get(j).getConfirm_status().equals("NO")));
                        }
                    }


                }

                recyclerView.setVisibility(View.VISIBLE);

                windup_adapter = new Windup_adapter(mContext, patientsArrayList, Wind_up_fragment.this);
                recyclerView.setAdapter(windup_adapter);
                windup_adapter.notifyDataSetChanged();


                ArrayList<String> getNoStatus = new ArrayList<>();
                if (GlobalClass.CheckArrayList(patientsArrayList)) {
                    for (int i = 0; i < patientsArrayList.size(); i++) {
                        if (patientsArrayList.get(i).getConfirm_status().equals("NO")) {
                            getNoStatus.add(patientsArrayList.get(i).getName());
                        }
                        int getCount = getNoStatus.size();
                        countData = String.valueOf(getCount);
                        GlobalClass.windupCountDataToShow = countData;
                    }
                }

                if (GlobalClass.isNull(GlobalClass.windupCountDataToShow) && !GlobalClass.windupCountDataToShow.equals("0")) {
                    GlobalClass.SetText(wind_up, "Wind up all (" + GlobalClass.windupCountDataToShow + ")");
                    wind_up_multiple.setVisibility(View.VISIBLE);
                    wind_up.setVisibility(View.VISIBLE);
                } else {
                    wind_up.setVisibility(View.GONE);
                    wind_up_multiple.setVisibility(View.GONE);
                    view_1.setVisibility(View.GONE);
                    view_2.setVisibility(View.GONE);
                    GlobalClass.SetText(wind_up, "Wind up (" + "0)");
                    GlobalClass.SetText(wind_up_multiple, "Wind up (" + "0)");
                }

            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                wind_up.setVisibility(View.GONE);
                wind_up_multiple.setVisibility(View.GONE);
                view_1.setVisibility(View.GONE);
                view_2.setVisibility(View.GONE);

            }

        }
    }

    public void getwindResponse(JSONObject response) {
        Log.e(TAG, "response " + response);
        try {
            CommonResponseModel responseModel = new Gson().fromJson(String.valueOf(response), CommonResponseModel.class);

            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getRES_ID()) && responseModel.getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
                    GlobalClass.showTastyToast(mContext, responseModel.getResponse(), 1);

                    fetchWoeListDoneByTSP();

                    Wind_up_fragment a2Fragment = new Wind_up_fragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        getChildFragmentManager().beginTransaction().detach(a2Fragment).commitNow();
                        getChildFragmentManager().beginTransaction().attach(a2Fragment).commitNow();
                    } else {
                        getChildFragmentManager().beginTransaction().detach(a2Fragment).attach(a2Fragment).commit();
                    }
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.fragment_mainLayout, a2Fragment).commit();

                } else {
                    GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 2);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, ToastFile.something_went_wrong, 2);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getcongsigmentResp(JSONObject response) {
        Log.e(TAG, "onResponse: RESPONSE" + response);
        ConsignmentCountResponseModel responseModel = new Gson().fromJson(String.valueOf(response), ConsignmentCountResponseModel.class);

        if (responseModel != null) {
            if (!GlobalClass.isNull(responseModel.getGetCount()) && responseModel.getGetCount().equalsIgnoreCase("0")) {
                enterNextFragment();
            } else {
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
        } else {
            GlobalClass.showTastyToast(mActivity, ToastFile.something_went_wrong, 2);

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
