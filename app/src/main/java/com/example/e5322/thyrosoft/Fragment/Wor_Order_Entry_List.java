package com.example.e5322.thyrosoft.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.PatientDtailsWoe;
import com.example.e5322.thyrosoft.FinalWoeModelPost.MyPojoWoe;
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
 * Activities that contain this fragment must implement the
 * {@link Wor_Order_Entry_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Wor_Order_Entry_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wor_Order_Entry_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ManagingTabsActivity mContext;
    ImageView add;
    View viewfab;
    View viewMain;
    View view;
    SharedPreferences mPrefs;
    PatientDtailsWoe patientDtailsWoe;
    EditText edtSearch;
    String getDatefromWOE, halfTime, DateToPass;
    static final int DATE_DIALOG_ID = 999;
    //    TextView wind_up, wind_up_multiple;
    TextView woe_cal;
    String TAG=Wor_Order_Entry_List.class.getSimpleName().toString();
    ProgressDialog barProgressDialog;
    RequestQueue requestQueue, requestQueueWindup;
    Button defaultFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    //layouts defined in fragment
    LinearLayout layoutCartItems, layoutCartPayments, layoutCartNoItems;
    Fragment fragment_woe_ad_test;
    WOE_Model_Patient_Details woe_model_patient_details;
    ArrayList<Patients> patientsArrayList;
    ArrayList<Patients> filterPatientsArrayList;
    ArrayList<String> getWindupCount;
    Calendar myCalendar;
    LinearLayout app_bar_layout,enter_entered_layout;
    public static com.android.volley.RequestQueue PostQueOtp;
    String putDate, getFormatDate, convertedDate;
    SharedPreferences prefs;
    View view_line,view_line1;
    TextView enetered, enter;
    String user, passwrd, access, api_key;
    String blockCharacterSet = "~#^|$%&*!+:`";
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
    private String DatePassToApi;

    public Wor_Order_Entry_List() {
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
    public static Woe_fragment newInstance(String param1, String param2) {

        Woe_fragment fragment = new Woe_fragment();
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

        viewMain = (View) inflater.inflate(R.layout.fragment_woe_add_test, container, false);

//        wind_up = (TextView) viewMain.findViewById(R.id.wind_up);
//        wind_up_multiple = (TextView) viewMain.findViewById(R.id.wind_up_multiple);
        woe_cal = (TextView) viewMain.findViewById(R.id.woe_cal);
        edtSearch = (EditText) viewMain.findViewById(R.id.edtSearch);
//        ImageView  add = (ImageView)viewMain.findViewById(R.id.add);
        recyclerView = (RecyclerView) viewMain.findViewById(R.id.recycler_view);
        app_bar_layout = (LinearLayout) viewMain.findViewById(R.id.enter_entered_layout_consign);

        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        enetered = (TextView)viewMain. findViewById(R.id.enetered);
        enter = (TextView) viewMain.findViewById(R.id.enter);
        view_line = (View)viewMain.findViewById(R.id.view_line);
        view_line1 =(View) viewMain.findViewById(R.id.view_line1);

        app_bar_layout.setVisibility(View.GONE);
        enter_entered_layout.setVisibility(View.GONE);


        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        myCalendar = Calendar.getInstance();

        Date dateget = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        halfTime = sdf.format(dateget);
//        if (getDatefromWOE.equals("Work order")) {
//            Date d = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            halfTime = sdf.format(d);
//        } else {
//            halfTime = getDatefromWOE.substring(11, getDatefromWOE.length() - 0);
//        }


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
                        if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                (name != null && name.contains(s1))) {
                            String testname = patientsArrayList.get(i).getName();
                            filterPatientsArrayList.add(patientsArrayList.get(i));
                        } else {

                        }
                        patientDtailsWoe = new PatientDtailsWoe(mContext, filterPatientsArrayList);
                        recyclerView.setAdapter(patientDtailsWoe);
                    }
                }
                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
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
//        wind_up.setText("Wind up (Pending:"+getWindupCount.size()+")");


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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


//        wind_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setTitle("Wind-up All !");
//                builder.setMessage(getResources().getString(R.string.windup));
//                builder.setCancelable(false);
//                builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
//                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd
//
//                        Date date = null;
//                        try {
//                            date = inputFormat.parse(DateToPass);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        DatePassToApi = outputFormat.format(date);
//
//                        barProgressDialog = new ProgressDialog(getContext());
//                        barProgressDialog.setTitle("Kindly wait ...");
//                        barProgressDialog.setMessage(ToastFile.processing_request);
//                        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
//                        barProgressDialog.setProgress(0);
//                        barProgressDialog.setMax(20);
//                        barProgressDialog.show();
//                        barProgressDialog.setCanceledOnTouchOutside(false);
//                        barProgressDialog.setCancelable(false);
//                        requestQueueWindup = GlobalClass.setVolleyReq(getContext());//2c=/TAM03/TAM03136166236000078/geteditdata
//                        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.windupApi + "" + api_key + "/" + user + "/" + DatePassToApi + "/getwowindup"
//                                , new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                System.out.println("barcode respponse" + response);
//
//                                String finalJson = response.toString();
//                                JSONObject parentObjectOtp = null;
//                                try {
//                                    parentObjectOtp = new JSONObject(finalJson);
//                                    String RES_ID = parentObjectOtp.getString("RES_ID");
//                                    String response1 = parentObjectOtp.getString("response");
//                                    if (RES_ID.equals("RES0000")) {
//                                        TastyToast.makeText(mContext, response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                                        if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
//                                        fetchWoeListDoneByTSP();
//                                    } else {
//                                        Toast.makeText(getContext(), "" + response1, Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                if (error.networkResponse == null) {
//                                    if (error.getClass().equals(TimeoutError.class)) {
//                                        // Show timeout error message
//                                    }
//                                }
//                            }
//                        });
//                        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
//                                300000,
//                                3,
//                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        requestQueueWindup.add(jsonObjectRequestPop);
//
//
//                    }
//                });
//
//                builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//                builder.show();
//            }
//        });

//        wind_up_multiple.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                barProgressDialog = new ProgressDialog(getActivity());
//                barProgressDialog.setTitle("Kindly wait ...");
//                barProgressDialog.setMessage(ToastFile.processing_request);
//                barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
//                barProgressDialog.setProgress(0);
//                barProgressDialog.setMax(20);
//                barProgressDialog.show();
//                barProgressDialog.setCanceledOnTouchOutside(false);
//                barProgressDialog.setCancelable(false);
//
//
//                if (GlobalClass.windupBarcodeList.size() != 0) {
//                    String SEPARATOR = ",";
//                    StringBuilder csvBuilder = new StringBuilder();
//
//                    for (String patientlist : GlobalClass.windupBarcodeList) {
//                        csvBuilder.append(patientlist);
//                        csvBuilder.append(SEPARATOR);
//                    }
//                    String csv = csvBuilder.toString();
//
//                    csv = csv.substring(0, csv.length() - SEPARATOR.length());
//
//                    System.out.println(csv);
//
//
//                    PostQueOtp = GlobalClass.setVolleyReq(getContext());
//                    JSONObject jsonObjectOtp = new JSONObject();
//                    try {
//
//                        jsonObjectOtp.put("api_key", api_key);
//                        jsonObjectOtp.put("Patient_id", csv);
//                        jsonObjectOtp.put("tsp", user);
//                        jsonObjectOtp.put("date", passToAPI);
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.multiple_windup, jsonObjectOtp, new com.android.volley.Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            String finalJson = response.toString();
//                            JSONObject parentObjectOtp = null;
//                            try {
//                                parentObjectOtp = new JSONObject(finalJson);
//                                String RES_ID = parentObjectOtp.getString("RES_ID");
//                                String response1 = parentObjectOtp.getString("response");
//                                if (RES_ID.equals("RES0000")) {
//                                    TastyToast.makeText(mContext, response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
//                                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
//                                    fetchWoeListDoneByTSP();
//                                } else {
//                                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
//                                    Toast.makeText(getContext(), "" + response1, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new com.android.volley.Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            if (error != null) {
//                            } else {
//
//                                System.out.println(error);
//                            }
//                        }
//                    });
//                    PostQueOtp.add(jsonObjectRequest1);
//
//                } else {
//                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
//                    Toast.makeText(getContext(), ToastFile.slt_name_for_windup, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        return viewMain;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        String formatToset = "yyyy-MM-dd";
        getFormatDate = sdf.format(myCalendar.getTime());

        //change date format from yyyy-MM-dd to dd-MM-yyyy

        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

        Date date = null;
        try {
            date = inputFormat.parse(getFormatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outputDateStr = outputFormat.format(date);


        woe_cal.setText(putDate);
        fetchPatientDetails();
    }

    @Override
    public void onStart() {
        fetchPatientDetails();
        super.onStart();
    }


    private void fetchPatientDetails() {

        DateToPass = getActivity().getTitle().toString().substring(4, getActivity().getTitle().toString().length() - 0);

        woe_cal.setText(DateToPass);

        fetchWoeListDoneByTSP();
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

        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd

        Date date = null;
        try {
            date = inputFormat.parse(DateToPass);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passToAPI = outputFormat.format(date);

        requestQueue = GlobalClass.setVolleyReq(mContext);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.Cloud_base + "getresults/" + api_key + "/WORK_ORDERS/" + "" + user + "/" + passToAPI + "/key/value", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: response"+response );
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
                        //getWindupCount.add(String.valueOf(woe_model_patient_details.getPatients().get(i).getConfirm_status().equals("NO")));
                        //patients.get(position).getConfirm_status().equals("YES")
                    }
//                    wind_up.setVisibility(View.GONE);
//                    wind_up_multiple.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    patientDtailsWoe = new PatientDtailsWoe(mContext, patientsArrayList);
                    recyclerView.setAdapter(patientDtailsWoe);
                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}

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
//                        wind_up.setText("Wind up all(Pending:" + GlobalClass.windupCountDataToShow + ")");

//                        wind_up_multiple.setVisibility(View.VISIBLE);
//                        wind_up.setVisibility(View.VISIBLE);
                    } else {
//                        wind_up.setText("Wind up(Pending:" + "0)");
//                        wind_up_multiple.setText("Wind up(Pending:" + "0)");
                    }



//                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
//                    wind_up.setVisibility(View.GONE);
//                    wind_up_multiple.setVisibility(View.GONE);
                    //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}

                }

                if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
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
        Log.e(TAG, "fetchWoeListDoneByTSP: URL"+ jsonObjectRequestPop);
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
