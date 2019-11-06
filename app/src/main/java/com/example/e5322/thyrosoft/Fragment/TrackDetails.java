package com.example.e5322.thyrosoft.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.CustomCalendarAdapter;
import com.example.e5322.thyrosoft.Adapter.TrackDetAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.CAlendar_Inteface;
import com.example.e5322.thyrosoft.Models.Mail_Model_Receipt;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.Models.Track_BarcodeModel;
import com.example.e5322.thyrosoft.Models.getAllDays;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackDetails extends Fragment implements CAlendar_Inteface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listviewreport;
    View viewresultfrag;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinnertype;
    public String Date = "";
    private SimpleDateFormat sdf;
    Calendar myCalendar;
    String user = "";
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    ArrayList<Mail_Model_Receipt> barCodeDetail = new ArrayList<Mail_Model_Receipt>();
    String api_key = "";
    SharedPreferences sharedpreferences;
    TrackDetAdapter adapter;
    public static RequestQueue PostQue;
    EditText search;
    long minDate;
    ProgressDialog barProgressDialog_nxt;
    TextView getDate, month_txt;
    private ResultFragment.OnFragmentInteractionListener mListener;
    Button buttonnow;
    CustomCalendarAdapter customCalendarAdapter;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String Barcodesend = "";
    ImageView download, mail;
    LinearLayout main, searchbarcodelistlinear, offline_img;
    TextView nodata, patient, bill_status, refBy, wo_order, sct, RRT, collected, billed, tedtedat, collectedat, receipt, bvt, nodatatv, set_selectedDate;
    ArrayList<TrackDetModel> trackDetArray = new ArrayList<TrackDetModel>();
    ProgressDialog barProgressDialog;
    private String convertedDate;
    private String passToAPI;
    private String datetoPass;
    private String barcode_response;
    private String date_passto_api;
    private String convertdate;
    Calendar dateSelected = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    private String email_id_string;
    ImageView back_month, next_month;
    RecyclerView calendarView;
    private LinearLayoutManager linearLayoutManager;
    private int getPositionToset;
    private ArrayList<getAllDays> SelectedMonthData;
    private String passDateTogetData;
    private String currentMonthString;
    private String getTextofMonth;

    public TrackDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackDetails newInstance(String param1, String param2) {
        TrackDetails fragment = new TrackDetails();
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
        sharedpreferences = getActivity().getSharedPreferences(Constants.MyPREFERENCES, MODE_PRIVATE);
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date = sdf.format(cl.getTime());
        GlobalClass.date = Date;
        datetoPass = GlobalClass.date;

        Date d = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);
        minDate = myCalendar.getTime().getTime();
//       String  halfTime =datetoPass.substring(11,datetoPass.length()-0);


        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

        Date date = null;
        try {
            date = inputFormat.parse(GlobalClass.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passToAPI = outputFormat.format(date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;

//        try {
//            myDate = dateFormat.parse(halfTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd");
        convertedDate = datetoPass;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        listviewreport = (ListView) view.findViewById(R.id.listviewreport);
        setHasOptionsMenu(true);
        spinnertype = (Spinner) view.findViewById(R.id.spinnertype);
        buttonnow = (Button) view.findViewById(R.id.buttonnow);

        main = (LinearLayout) view.findViewById(R.id.main);
        searchbarcodelistlinear = (LinearLayout) view.findViewById(R.id.searchbarcodelistlinear);
        nodata = (TextView) view.findViewById(R.id.nodata); //patient,bill_status,refBy,wo_order,sct,RRT;
        patient = (TextView) view.findViewById(R.id.patient);
        bill_status = (TextView) view.findViewById(R.id.bill_status);
        refBy = (TextView) view.findViewById(R.id.refBy);
        wo_order = (TextView) view.findViewById(R.id.wo_order);
        sct = (TextView) view.findViewById(R.id.sct);
        RRT = (TextView) view.findViewById(R.id.RRT);
        bvt = (TextView) view.findViewById(R.id.bvt);
        month_txt = (TextView) view.findViewById(R.id.month_txt);

        set_selectedDate = (TextView) view.findViewById(R.id.set_selectedDate);
        back_month = (ImageView) view.findViewById(R.id.back_month);
        next_month = (ImageView) view.findViewById(R.id.next_month);
        calendarView = (RecyclerView) view.findViewById(R.id.calendarView);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        calendarView.setLayoutManager(linearLayoutManager);

        // TextView dateview = getActivity().findViewById(R.id.show_date);
        //dateview.setVisibility(View.GONE);
        collected = (TextView) view.findViewById(R.id.collected);
        billed = (TextView) view.findViewById(R.id.billed);
        tedtedat = (TextView) view.findViewById(R.id.tedtedat);
        collectedat = (TextView) view.findViewById(R.id.collectedat);
        download = (ImageView) view.findViewById(R.id.download);
        mail = (ImageView) view.findViewById(R.id.mail);
        offline_img = (LinearLayout) view.findViewById(R.id.offline_img);

        search = (EditText) view.findViewById(R.id.searchView);


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            listviewreport.setVisibility(View.GONE);
            searchbarcodelistlinear.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            listviewreport.setVisibility(View.VISIBLE);
            searchbarcodelistlinear.setVisibility(View.GONE);
        }

        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        final Calendar c = Calendar.getInstance();

        final String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        final String month = monthName[c.get(MONTH)];
        System.out.println("Month name:" + month);
        final int year = c.get(YEAR);
        getPositionToset = c.get(Calendar.DATE);
        try {
            SelectedMonthData = getAllDaysInMonth(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setCalendarAdapter(SelectedMonthData);
        currentMonthString = month + " " + year;

        month_txt.setText(month + " " + year);

        getTextofMonth = month_txt.getText().toString();

        if (getTextofMonth.equalsIgnoreCase(currentMonthString)) {
            next_month.setVisibility(View.GONE);
        } else {
            next_month.setVisibility(View.VISIBLE);
        }

        Date currentDate = new Date();
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy-MM-dd");
        passDateTogetData = sdfa.format(currentDate);

        back_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_month = month_txt.getText().toString();
                c.add(MONTH, -1);
                final String month = monthName[c.get(MONTH)];
                final int year = c.get(YEAR);
                month_txt.setText(month + " " + year);
                try {
                    SelectedMonthData = getAllDaysInMonth(c);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setCalendarAdapter(SelectedMonthData);

                getTextofMonth = month_txt.getText().toString();
                if (getTextofMonth.equalsIgnoreCase(currentMonthString)) {
                    next_month.setVisibility(View.GONE);
                } else {
                    next_month.setVisibility(View.VISIBLE);
                }
            }
        });


        next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get_month = month_txt.getText().toString();
                c.add(MONTH, +1);
                final String month = monthName[c.get(MONTH)];
                final int year = c.get(YEAR);
                month_txt.setText(month + " " + year);

                try {
                    SelectedMonthData = getAllDaysInMonth(c);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setCalendarAdapter(SelectedMonthData);

                getTextofMonth = month_txt.getText().toString();
                if (getTextofMonth.equalsIgnoreCase(currentMonthString)) {
                    next_month.setVisibility(View.GONE);
                } else {
                    next_month.setVisibility(View.VISIBLE);
                }
            }
        });

        Date datae = new Date();
        SimpleDateFormat sdfg = new SimpleDateFormat("dd-MM-yyyy");

        String getDateTopass = ("Track details for " + sdfg.format(datae));
        set_selectedDate.setText(getDateTopass);

        Date datee = new Date();
        SimpleDateFormat sdf22 = new SimpleDateFormat("dd-MM-yyyy");
        String getcurrentdate = sdf22.format(datee);

        String[] spinner = {"All", "Ready", "Downloaded"};

        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnertype.setAdapter(aa);
        buttonnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FilterReport filt = new FilterReport();
                    android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_mainLayout, filt);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                if (s.length() == 0) {
                    GetData();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().toLowerCase();
                String querylength = s1;
                if (querylength.length() == 0) {
                    listviewreport.setVisibility(View.VISIBLE);
                    searchbarcodelistlinear.setVisibility(View.GONE);
                    offline_img.setVisibility(View.GONE);
                } else if (querylength.length() == 8) {
                    DownloadReceipt(s1);
                    final List<TrackDetModel> filteredModelList = filter(trackDetArray, s1);
                    try {
                        if (filteredModelList != null) {
                            listviewreport.setVisibility(View.GONE);
                            offline_img.setVisibility(View.GONE);
                            searchbarcodelistlinear.setVisibility(View.VISIBLE);
                            TrackBrcodeData(s1, filteredModelList.get(0).getDate());
                        } else {
                            listviewreport.setVisibility(View.VISIBLE);
                            offline_img.setVisibility(View.GONE);
                            searchbarcodelistlinear.setVisibility(View.GONE);
                            // Toast.makeText(getActivity(), ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        Toast.makeText(getActivity(), ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "on errror ---->" + e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                    callAdapter(trackDetArray);

                }
                // filter your list from your input
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);

        } else {
            GetData();
            offline_img.setVisibility(View.GONE);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        getTextofMonth = month_txt.getText().toString();
        if (getTextofMonth.equalsIgnoreCase(currentMonthString)) {
            next_month.setVisibility(View.GONE);
        } else {
            next_month.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    private void callAdapter(ArrayList<TrackDetModel> modelArrayList) {
        if (modelArrayList.size() > 0) {
            listviewreport.setVisibility(View.VISIBLE);
            adapter = new TrackDetAdapter(getContext(), trackDetArray);
            listviewreport.setAdapter(adapter);
            nodata.setVisibility(View.GONE);
        } else {
            listviewreport.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }

    }

    private void setCalendarAdapter(ArrayList<getAllDays> SelectedMonthData) {
        customCalendarAdapter = new CustomCalendarAdapter(getActivity(), SelectedMonthData, this);
        calendarView.setAdapter(customCalendarAdapter);
        calendarView.getLayoutManager().scrollToPosition(getPositionToset - 5);
    }

    public ArrayList<getAllDays> getAllDaysInMonth(Calendar c) throws ParseException {
        int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<getAllDays> arrayList = new ArrayList<getAllDays>();
        for (int i = 1; i <= monthMaxDays; i++) {
            String input = "";
            input = Integer.toString(i) + "-" + (c.get(MONTH) + 1) + "-" + c.get(YEAR);

            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inFormat.parse(input);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
            SimpleDateFormat getOnlyDates = new SimpleDateFormat("dd");
            String weekday = outFormat.format(date);
            String dates_pass = getOnlyDates.format(date);

            SimpleDateFormat outFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            String wholeday = outFormat1.format(date);

            getAllDays entity = new getAllDays();
            entity.setStrWholeDate(wholeday);
            entity.setStrDays(weekday);
            entity.setStrDates(dates_pass);

            arrayList.add(entity);
        }
        return arrayList;
    }


    private String dayToShow, monthToShow;
    private String getDateToShow, passMonth_Finally;
    final DatePickerDialog.OnDateSetListener setTime = new DatePickerDialog.OnDateSetListener() {

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

            getDate.setText(getDateToShow);
            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                offline_img.setVisibility(View.VISIBLE);

            } else {
                GetData();
                offline_img.setVisibility(View.GONE);
            }
        }
    };

    private void DownloadReceipt(String barcode) {  //

        PostQue = Volley.newRequestQueue(getContext());

        try {
            JSONObject jsonObject = new JSONObject();
            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            api_key = prefs.getString("API_KEY", null);

            try {
                jsonObject.put("apikey", api_key);
                jsonObject.put("barcode", barcode);
                Barcodesend = barcode;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, Api.downloadreceipt, jsonObject,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            try { //Track_BarcodeModel
                                barCodeDetail = new ArrayList<Mail_Model_Receipt>();
                                Mail_Model_Receipt model = new Mail_Model_Receipt();
                                model.setAddress(response.optString("address"));
                                model.setAmount(response.optString("amount"));
                                model.setAmount_word(response.optString("amount_word"));
                                model.setEmail(response.optString("email"));
                                model.setMobile(response.optString("mobile"));
                                model.setName(response.optString("name"));
                                model.setOrderdate(response.optString("orderdate"));
                                model.setTest(response.optString("test"));
                                model.setUrl(response.optString("url"));
                                barCodeDetail.add(model);
/*{"address":"","amount":"200","amount_word":"Two Hundred ","email":"sarathykodai@gmail.com","mobile":"9842187270","name":"MRS LINDA (31Y\/F)",
"orderdate":"31-05-2018","res_id":"RES0000","response":"SUCCESS","test":",FT3,FT4,TSH","url":"https:\\www.thyrocare.com\\Wellness\\AutoReceipt\\G7919583.pdf"}*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        System.out.println("error ala parat " + error);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            queue.add(jsonObjectRequest);
            Log.e(TAG, "DownloadReceipt: URL" + jsonObjectRequest);
            Log.e(TAG, "DownloadReceipt: json" + jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void TrackBrcodeData(String Barcode, String dateStr) {

        PostQue = Volley.newRequestQueue(getContext());
        barProgressDialog_nxt = new ProgressDialog(getContext());
        barProgressDialog_nxt.setTitle("Kindly wait ...");
        barProgressDialog_nxt.setMessage(ToastFile.processing_request);
        barProgressDialog_nxt.setProgressStyle(barProgressDialog_nxt.STYLE_SPINNER);
        barProgressDialog_nxt.setProgress(0);
        barProgressDialog_nxt.setMax(20);
        barProgressDialog_nxt.show();
        barProgressDialog_nxt.setCanceledOnTouchOutside(false);
        barProgressDialog_nxt.setCancelable(false);

        try {
            JSONObject jsonObject = new JSONObject();
            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            api_key = prefs.getString("API_KEY", null);

            try {
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                java.util.Date date = inputFormat.parse(dateStr);
                String outputDateStr = outputFormat.format(date);

                jsonObject.put("apiKey", api_key);
                jsonObject.put("barcode", Barcode);
                jsonObject.put("userCode", user);
                jsonObject.put("forwardTo", outputDateStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, Api.trackbarcode, jsonObject,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try { //Track_BarcodeModel
                                if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                                    barProgressDialog_nxt.dismiss();
                                }
                                Log.e(TAG, "onResponse: URL" + response);
                                String finalJson = response.toString();
                                JSONObject parentObject = new JSONObject(finalJson);
                                barcode_response = parentObject.getString("response");

                                if (barcode_response.equals("INVALID BARCODE")) {
                                    Toast.makeText(getActivity(), ToastFile.search_using_brcd, Toast.LENGTH_SHORT).show();
                                    listviewreport.setVisibility(View.VISIBLE);
                                    offline_img.setVisibility(View.GONE);
                                    searchbarcodelistlinear.setVisibility(View.GONE);
                                } else {
                                    ArrayList<Track_BarcodeModel> trackBarArray = new ArrayList<Track_BarcodeModel>();
                                    listviewreport.setVisibility(View.GONE);
                                    offline_img.setVisibility(View.GONE);
                                    searchbarcodelistlinear.setVisibility(View.VISIBLE);

                                    final Track_BarcodeModel trackBarcode = new Track_BarcodeModel();

                                    trackBarcode.setBillStatus(response.getString(Constants.billStatus));
                                    trackBarcode.setBilled(response.getString(Constants.billed));
                                    trackBarcode.setBvt(response.getString(Constants.bvt));
                                    trackBarcode.setCollected(response.getString(Constants.collected));
                                    trackBarcode.setEmail(response.getString(Constants.email));
                                    trackBarcode.setEtr(response.getString(Constants.etr));
                                    trackBarcode.setIsOrder(response.getString(Constants.isOrder));
                                    trackBarcode.setKycType(response.getString(Constants.kycType));
                                    trackBarcode.setLeadId(response.getString(Constants.leadId));
                                    trackBarcode.setOrderId(response.getString(Constants.orderId));
                                    trackBarcode.setOrderNo(response.getString(Constants.orderNo));
                                    trackBarcode.setPatient(response.getString(Constants.patient));
                                    trackBarcode.setPendingCancelledTests(response.getString(Constants.pendingCancelledTests));
                                    trackBarcode.setPortalType(response.getString(Constants.portalType));
                                    trackBarcode.setRefBy(response.getString(Constants.refBy));
                                    trackBarcode.setReportAddress(response.getString(Constants.reportAddress));
                                    trackBarcode.setResId(response.getString(Constants.resId));
                                    trackBarcode.setResponse(response.getString(Constants.response));
                                    trackBarcode.setRrt(response.getString(Constants.rrt));
                                    trackBarcode.setSampleDetails(response.getString(Constants.sampleDetails));
                                    trackBarcode.setResponse(response.getString(Constants.response));
                                    trackBarcode.setScp(response.getString(Constants.scp));
                                    trackBarcode.setSct(response.getString(Constants.sct));
                                    trackBarcode.setStatus(response.getString(Constants.status));
                                    trackBarcode.setStatusFlag(response.getString(Constants.statusFlag));
                                    trackBarcode.setWoeEdit(response.getString(Constants.woeEdit));
                                    trackBarcode.setWoeStage(response.getString(Constants.woeStage));
                                    trackBarcode.setWoeTime(response.getString(Constants.woeTime));
                                    trackBarcode.setWoiLocation(response.getString(Constants.woiLocation));
                                    trackBarArray.add(trackBarcode);

                                    if (trackBarcode.getCollected().equals("null")) {
                                        trackBarcode.setCollected("NA");
                                    }
                                    patient.setText(trackBarcode.getPatient().toString());
                                    bill_status.setText(trackBarcode.getBillStatus().toString());
                                    refBy.setText(trackBarcode.getRefBy().toString());
                                    wo_order.setText(trackBarcode.getWoeTime().toString());
                                    sct.setText(trackBarcode.getSct().toString());
                                    bvt.setText(trackBarcode.getBvt().toString());
                                    RRT.setText(trackBarcode.getRrt().toString());
                                    collected.setText(trackBarcode.getCollected().toString());
                                    billed.setText(trackBarcode.getBilled().toString());
                                    tedtedat.setText(trackBarcode.getWoiLocation());
                                    collectedat.setText(trackBarcode.getReportAddress());

                                    mail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            SendReceiptMail();
                                        }
                                    });

                                    download.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!barCodeDetail.get(0).getUrl().isEmpty() && barCodeDetail.get(0).getUrl() != null && !barCodeDetail.get(0).getUrl().equalsIgnoreCase("null")) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(barCodeDetail.get(0).getUrl()));
                                                startActivity(browserIntent);
                                            } else {
                                                TastyToast.makeText(getActivity(), "Receipt is not generated yet!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                                            }
                                        }
                                    });
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (barProgressDialog_nxt != null && barProgressDialog_nxt.isShowing()) {
                            barProgressDialog_nxt.dismiss();
                        }
                        System.out.println("error ala parat " + error);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            queue.add(jsonObjectRequest);
            Log.e(TAG, "TrackBrcodeData: URL" + jsonObjectRequest);
            Log.e(TAG, "TrackBrcodeData: json" + jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TrackDetModel> filter(List<TrackDetModel> models, String query) {
        query = query.toLowerCase();
        final List<TrackDetModel> filteredModelList = new ArrayList<>();
        for (TrackDetModel model : models) {
            final String name = model.getName().toLowerCase();
            final String ref = model.getRef_By().toLowerCase();
            final String test = model.getTests().toLowerCase();
            final String barcode = model.getBarcode().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            } else if (ref.contains(query)) {
                filteredModelList.add(model);
            } else if (barcode.contains(query)) {
                filteredModelList.add(model);
            } else if (test.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void GetData() {

        barProgressDialog.show();

        PostQue = Volley.newRequestQueue(getContext());

        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            api_key = prefs.getString("API_KEY", null);

            jsonObject.put("API_Key", api_key);
            jsonObject.put("result_type", "Reported");
            jsonObject.put("tsp", user);
            jsonObject.put("date", convertdate);
            jsonObject.put("key", "");
            jsonObject.put("value", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.ResultLIVE + "/" + api_key + "/REPORTED" + "/" + user + "/" + passDateTogetData + "/" + "key/value", jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            String responsetoshow = response.optString("response", "");

                            if (responsetoshow.equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(getActivity());
                            } else {
                                JSONArray jsonArray = response.optJSONArray(Constants.patients);
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    trackDetArray = new ArrayList<TrackDetModel>();
                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        TrackDetModel trackdetails = new TrackDetModel();
                                        trackdetails.setDownloaded(jsonObject.optString(Constants.Downloaded).toString());
                                        trackdetails.setRef_By(jsonObject.optString(Constants.Ref_By).toString());
                                        trackdetails.setTests(jsonObject.optString(Constants.Tests).toString());
                                        trackdetails.setBarcode(jsonObject.optString(Constants.barcode).toString());
                                        //  trackdetails.setCancel_tests_with_remark(jsonObject.optString(Constants.cancel_tests_with_remark).toString());
                                        trackdetails.setChn_pending(jsonObject.optString(Constants.chn_pending).toString());
                                        trackdetails.setChn_test(jsonObject.optString(Constants.chn_test).toString());
                                        trackdetails.setConfirm_status(jsonObject.optString(Constants.confirm_status).toString());
                                        trackdetails.setDate(jsonObject.optString(Constants.date).toString());
                                        trackdetails.setEmail(jsonObject.optString(Constants.email).toString());
                                        trackdetails.setIsOrder(jsonObject.optString(Constants.isOrder).toString());
                                        trackdetails.setLabcode(jsonObject.optString(Constants.labcode).toString());
                                        trackdetails.setLeadId(jsonObject.optString(Constants.leadId).toString());
                                        trackdetails.setName(jsonObject.optString(Constants.name).toString());
                                        trackdetails.setPatient_id(jsonObject.optString(Constants.patient_id).toString());
                                        trackdetails.setPdflink(jsonObject.optString(Constants.pdflink).toString());
                                        trackdetails.setSample_type(jsonObject.optString(Constants.sample_type).toString());
                                        trackdetails.setScp(jsonObject.optString(Constants.scp).toString());
                                        trackdetails.setSct(jsonObject.optString(Constants.sct).toString());
                                        trackdetails.setSu_code2(jsonObject.optString(Constants.su_code2).toString());
                                        trackdetails.setWo_sl_no(jsonObject.optString(Constants.wo_sl_no).toString());
                                        trackDetArray.add(trackdetails);
                                    }
                                    if (trackDetArray.size() == 0) {
                                        nodata.setVisibility(View.VISIBLE);
                                        search.setVisibility(View.GONE);
                                    } else {
                                        //  if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}
                                        nodata.setVisibility(View.GONE);
                                        search.setVisibility(View.VISIBLE);
                                        adapter = new TrackDetAdapter(getContext(), trackDetArray);
                                        listviewreport.setAdapter(adapter);
                                        listviewreport.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    nodata.setVisibility(View.VISIBLE);
                                    search.setVisibility(View.GONE);
                                    listviewreport.setVisibility(View.GONE);
                                    searchbarcodelistlinear.setVisibility(View.GONE);
//                                Toast.makeText(getActivity(), ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: json" + jsonObject);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPassDateandPos(int position, String pasDate) {
        getPositionToset = position;
        passDateTogetData = pasDate;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d = input.parse(passDateTogetData);
            String getdateToSet = output.format(d);
            set_selectedDate.setText("Track details for " + getdateToSet);
            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                offline_img.setVisibility(View.VISIBLE);

            } else {
                GetData();
                offline_img.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void ShowMailAlert(final String patient_ID, final String barcode, final String email, final String date) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.mail_alert);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 200;
        lp.gravity = Gravity.CENTER;
        final EditText emailid = (EditText) dialog.findViewById(R.id.email);
        if (!email.equals("")) {
            emailid.setText(email);
        } else {
            Toast.makeText(getContext(), ToastFile.crt_eml, Toast.LENGTH_SHORT).show();
        }
        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetData(patient_ID,barcode,email,date);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void SendReceiptMail() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.track_receipt_mail);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 200;
        lp.gravity = Gravity.CENTER;
        final EditText emailid = (EditText) dialog.findViewById(R.id.email);
        final EditText mob = (EditText) dialog.findViewById(R.id.mob);
        final EditText name = (EditText) dialog.findViewById(R.id.name);

        if (barCodeDetail.get(0).getEmail() != null && !barCodeDetail.get(0).getEmail().equalsIgnoreCase("null")) {
            emailid.setText(barCodeDetail.get(0).getEmail());
        } else {
            emailid.setText("");
        }

        if (barCodeDetail.get(0).getMobile() != null && !barCodeDetail.get(0).getMobile().equalsIgnoreCase("null")) {
            mob.setText(barCodeDetail.get(0).getMobile());
        } else {
            mob.setText("");
        }

        if (barCodeDetail.get(0).getName() != null && !barCodeDetail.get(0).getName().equalsIgnoreCase("null")) {
            name.setText(barCodeDetail.get(0).getName());
        } else {
            name.setText("");
        }


        final Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_id_string = emailid.getText().toString();
                if (!email_id_string.equals("")) {
                    if (!emailid.getText().toString().matches(emailPattern)) {
                        Toast.makeText(getContext(), ToastFile.invalid_eml, Toast.LENGTH_SHORT).show();
                    } else {
                        SendMail(mob, name);
                        dialog.dismiss();
                    }

                } else {
                    Toast.makeText(getContext(), ToastFile.crt_eml, Toast.LENGTH_SHORT).show();
                }
                //GetData(patient_ID,barcode,email,date);
//                SendMail();


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void SendMail(EditText mob, EditText name) {

        PostQue = Volley.newRequestQueue(getContext());

        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            api_key = prefs.getString("API_KEY", null);

            jsonObject.put("apikey", api_key);
            jsonObject.put("barcode", Barcodesend);

            String[] twoStringArray = barCodeDetail.get(0).getName().split("\\(", 2); //the main line

            jsonObject.put("name", twoStringArray[0]);

            if (!TextUtils.isEmpty(mob.getText().toString())) {
                jsonObject.put("mob", barCodeDetail.get(0).getMobile());
            }

            jsonObject.put("email", email_id_string);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        Log.e(TAG, "SEND EMAIL RESPONSE ---->" + jsonObject.toString());
        Log.e(TAG, "SEND EMAIL API ---->" + Api.Receipt_mail);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.Receipt_mail, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            /*"name":"SELVI HARSHINI III (14Y\/F)"*/
                            TastyToast.makeText(getContext(), response.optString("response"), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonObjectRequest);
        Log.e(TAG, "SendMail: " + jsonObjectRequest);
        Log.e(TAG, "SendMail: json" + jsonObject);

    }
}
