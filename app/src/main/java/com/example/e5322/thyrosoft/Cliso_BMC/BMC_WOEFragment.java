package com.example.e5322.thyrosoft.Cliso_BMC;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
import com.example.e5322.thyrosoft.Fragment.Woe_fragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.Patients;
import com.example.e5322.thyrosoft.WorkOrder_entry_Model.WOE_Model_Patient_Details;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BMC_WOEFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RequestQueue PostQueOtp;
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
    final Calendar myCalendar = Calendar.getInstance();
    Context mContext;
    View viewMain;
    ImageView enter_arrow_enter, enter_arrow_entered;
    BMC_PatientDetailsWOEAdapter bmc_patientDetailsWOEAdapter;
    EditText edtSearch;
    String halfTime;
    TextView woe_cal;
    ProgressDialog barProgressDialog;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    //layouts defined in fragment
    LinearLayout enter_ll_unselected, unchecked_entered_ll, offline_img, wind_up_ll;
    WOE_Model_Patient_Details woe_model_patient_details;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayList;
    ArrayList<String> getWindupCount;
    String convertedDate;
    SharedPreferences prefs;
    TextView enetered, enter;
    String user, passwrd, access, api_key;
    String blockCharacterSet = "~#^|$%&*!+:`";
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
    private Woe_fragment.OnFragmentInteractionListener mListener;
    private String countData;
    private String outputDateStr;
    private String passToAPI;
    private String TAG = BMC_WOEFragment.class.getSimpleName();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

    public BMC_WOEFragment() {
        // Required empty public constructor
    }

    public static BMC_WOEFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        BMC_WOEFragment fragment = new BMC_WOEFragment();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewMain = inflater.inflate(R.layout.fragment_bmc_woe, container, false);

        mContext = getContext();
//        mContext = (BMC_MainActivity) getActivity();

        woe_cal = (TextView) viewMain.findViewById(R.id.woe_cal);
        edtSearch = (EditText) viewMain.findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) viewMain.findViewById(R.id.recycler_view);


        unchecked_entered_ll = (LinearLayout) viewMain.findViewById(R.id.unchecked_entered_ll);
        offline_img = (LinearLayout) viewMain.findViewById(R.id.offline_img);
        wind_up_ll = (LinearLayout) viewMain.findViewById(R.id.wind_up_ll);
        enter_ll_unselected = (LinearLayout) viewMain.findViewById(R.id.enter_ll_unselected);
        enetered = (TextView) viewMain.findViewById(R.id.enetered);
        enter = (TextView) viewMain.findViewById(R.id.enter);
        enter_arrow_enter = (ImageView) viewMain.findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) viewMain.findViewById(R.id.enter_arrow_entered);

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String showTotxt = sdf1.format(d);
        woe_cal.setText(showTotxt);
        passToAPI = sdf.format(d);

        enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_entered.setVisibility(View.VISIBLE);
        enter.setBackgroundColor(getResources().getColor(R.color.lightgray));

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enterCurrentFragment();
            }
        });

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                enterNextFragment();
            }
        });

        woe_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(), R.style.DialogTheme,date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            wind_up_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            wind_up_ll.setVisibility(View.VISIBLE);
        }

        SimpleDateFormat sdfdatatomap = new SimpleDateFormat("dd-MM-yyyy");
        String get_date_to_mapp = sdfdatatomap.format(new Date());
        halfTime = get_date_to_mapp;

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
                if (patientsArrayList != null) {
                    for (int i = 0; i < patientsArrayList.size(); i++) {
                        final String text = patientsArrayList.get(i).getBarcode().toLowerCase();
                        if (patientsArrayList.get(i).getBarcode() != null || !patientsArrayList.get(i).getBarcode().equals("")) {
                            barcode = patientsArrayList.get(i).getBarcode().toLowerCase();
                        }
                        if (patientsArrayList.get(i).getName() != null || !patientsArrayList.get(i).getName().equals("")) {
                            name = patientsArrayList.get(i).getName().toLowerCase();
                        }
                        if (text.contains(s1) || (barcode != null && barcode.contains(s1)) || (name != null && name.contains(s1))) {
                            filterPatientsArrayList.add(patientsArrayList.get(i));
                        }
                        bmc_patientDetailsWOEAdapter = new BMC_PatientDetailsWOEAdapter(mContext, filterPatientsArrayList);
                        recyclerView.setAdapter(bmc_patientDetailsWOEAdapter);
                    }
                }
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(halfTime);
            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd");
            convertedDate = sdfdata.format(myDate);
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

            Date date = null;
            try {
                date = inputFormat.parse(convertedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            outputDateStr = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = timeFormat.format(myDate);

        System.out.println(finalDate);

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            wind_up_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            fetchWoeListDoneByTSP();
            wind_up_ll.setVisibility(View.VISIBLE);
        }

        return viewMain;
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        passToAPI = sdf1.format(myCalendar.getTime());
        woe_cal.setText(sdf.format(myCalendar.getTime()));
        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            wind_up_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            fetchWoeListDoneByTSP();
            wind_up_ll.setVisibility(View.VISIBLE);
        }
    }

    private void enterCurrentFragment() {
        BMC_WOEFragment bmc_woeFragment = new BMC_WOEFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_layout, bmc_woeFragment).commit();
    }

    private void enterNextFragment() {
        BMC_NEW_WOEFragment bmc_new_woeFragment = new BMC_NEW_WOEFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_layout, bmc_new_woeFragment).commit();
    }

    private void fetchWoeListDoneByTSP() {
        barProgressDialog = new ProgressDialog(getActivity());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


        requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.WORKoRDEReNTRYfIRSTpAGE + "" + api_key + "/WORK_ORDERS/" + "" + user + "/" + passToAPI + "/key/value", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response);
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
                //set Adpter
                if (woe_model_patient_details.getPatients() != null) {
                    for (int i = 0; i < woe_model_patient_details.getPatients().size(); i++) {
                        patientsArrayList.add(woe_model_patient_details.getPatients().get(i));
                        for (int j = 0; j < patientsArrayList.size(); j++) {
                            getWindupCount.add(String.valueOf(patientsArrayList.get(j).getConfirm_status().equals("NO")));
                        }
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    bmc_patientDetailsWOEAdapter = new BMC_PatientDetailsWOEAdapter(mContext, patientsArrayList);
                    recyclerView.setAdapter(bmc_patientDetailsWOEAdapter);
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                    ArrayList<String> getNoStatus = new ArrayList<>();
                    for (int i = 0; i < patientsArrayList.size(); i++) {
                        if (patientsArrayList.get(i).getConfirm_status().equals("NO")) {
                            getNoStatus.add(patientsArrayList.get(i).getName());
                        }
                        int getCount = getNoStatus.size();
                        countData = String.valueOf(getCount);
                        GlobalClass.windupCountDataToShow = countData;
                    }
                    if (GlobalClass.windupCountDataToShow != null) {

                    } else {

                    }
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                }

                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
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
        requestQueue.add(jsonObjectRequestPop);
        Log.e(TAG, "fetchWoeListDoneByTSP: URL" + jsonObjectRequestPop);
    }

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
