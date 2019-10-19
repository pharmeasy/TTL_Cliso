package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.example.e5322.thyrosoft.Adapter.ResultDtlAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.CAlendar_Inteface;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.Models.getAllDays;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.e5322.thyrosoft.Fragment.FilterReport.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.e5322.thyrosoft.Fragment.FilterReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterReport extends Fragment implements CAlendar_Inteface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private SimpleDateFormat sdf;
    private String mParam1;
    private String mParam2;
    public String toDate = "";
    RecyclerView calendarView;
    public String fromDate = "";
    public String Date = "";
    public String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    TextView txt_date, nodata, month_txt, showDate;
    ProgressDialog progressDialog;
    Context mContext;
    Spinner spinnertype;
    public static RequestQueue PostQue;
    ResultDtlAdapter adapter;
    Spinner filterBy;
    private OnFragmentInteractionListener mListener;
    EditText searchbarcode;
    ListView ListReportStatus;
    Map<Integer, String> myMap;
    ArrayList<TrackDetModel> FilterReport = new ArrayList<TrackDetModel>();
    String send_Date = "";
    private String showDateTotxt;
    private String passToAPI;
    private String halfTime;
    ImageView back_month, next_month;
    LinearLayout offline_img;
    private String responsetoshow;
    private String passToSpinner;
    private ProgressDialog barProgressDialog;
    CustomCalendarAdapter customCalendarAdapter;
    public GregorianCalendar cal_month, cal_month_copy;
    //    private CalendarAdapter cal_adapter;
    private TextView tv_month;
    private String getonlyMonth;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<getAllDays> SelectedMonthData;
    String passDateTogetData;
    private int getPositionToset;
    public static boolean callOnce = false;
    LinearLayout full_ll;
    private String currentMonthString;
    private String getTextofMonth;
    private ArrayList<TrackDetModel> filterPatientsArrayList;
    String user, passwrd, access, api_key, user_code;

    public FilterReport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterReport.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.e5322.thyrosoft.Fragment.FilterReport newInstance(String param1, String param2) {
        com.example.e5322.thyrosoft.Fragment.FilterReport fragment = new FilterReport();
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
            mContext = (ManagingTabsActivity) getActivity();
            Calendar cl = Calendar.getInstance();
            sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            toDate = sdf.format(cl.getTime());
            Date = sdf.format(cl.getTime());
            //cl.add(Calendar.DAY_OF_MONTH, -7);
            fromDate = sdf.format(cl.getTime());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_filter_report, container, false);
        try {
            searchbarcode = (EditText) view.findViewById(R.id.searchbarcode);

            nodata = (TextView) view.findViewById(R.id.nodata);
            showDate = (TextView) view.findViewById(R.id.showDate);
            month_txt = (TextView) view.findViewById(R.id.month_txt);
            spinnertype = (Spinner) view.findViewById(R.id.spinnerfilter);
            back_month = (ImageView) view.findViewById(R.id.back_month);
            next_month = (ImageView) view.findViewById(R.id.next_month);
            offline_img = (LinearLayout) view.findViewById(R.id.offline_img);
            calendarView = (RecyclerView) view.findViewById(R.id.calendarView);
            filterBy = (Spinner) view.findViewById(R.id.filterBy);
            full_ll = (LinearLayout) view.findViewById(R.id.full_ll);
            ListReportStatus = (ListView) view.findViewById(R.id.ListReportStatus);
            linearLayoutManager = new LinearLayoutManager(this.getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            calendarView.setLayoutManager(linearLayoutManager);

            if (!GlobalClass.isNetworkAvailable(getActivity())) {
                offline_img.setVisibility(View.VISIBLE);

            } else {
                offline_img.setVisibility(View.GONE);
            }
            Calendar cl = Calendar.getInstance();
            sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            toDate = sdf.format(cl.getTime());
            Date = sdf.format(cl.getTime());

            //GlobalClass.saveDtaeTopass
            //cl.add(Calendar.DAY_OF_MONTH, -7);
            fromDate = sdf.format(cl.getTime());

            Date d = new Date();
            SimpleDateFormat sdfg = new SimpleDateFormat("dd-MM-yyyy");

            String getDateTopass = ("Result for " + sdfg.format(d));
            String getDate = getDateTopass;
            //?<---------------show date to red textbox------>

            showDate.setText(getDateTopass);
            //?<---------------show date to red textbox------>
            halfTime = getDate.substring(11, getDate.length() - 0);
            GlobalClass.saveDtaeTopass = halfTime;

            barProgressDialog = new ProgressDialog(getActivity(), R.style.ProgressBarColor);
            barProgressDialog.setTitle("Kindly wait ...");
            barProgressDialog.setMessage(ToastFile.processing_request);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(20);
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        user_code = prefs.getString("USER_CODE", null);

        full_ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = full_ll.getRootView().getHeight() - full_ll.getHeight();
                if (heightDiff > dpToPx(getContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                }
            }
        });

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

        month_txt.setText(month + " " + year);
        currentMonthString = month + " " + year;

        try {
            getTextofMonth = month_txt.getText().toString();
            if (getTextofMonth.equalsIgnoreCase(currentMonthString)) {
                next_month.setVisibility(View.GONE);
            } else {
                next_month.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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

        String[] spinner = {"Select Type", "Reported", "Pending", "Cancelled", "CHN"};
        String[] spinnerfilterby = {"Select Filter By", "All", "Name", "Barcode"};
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner);
        ArrayAdapter filterby = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinnerfilterby);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterby.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //   TextView dateview = getActivity().findViewById(R.id.show_date);
        // dateview.setVisibility(View.GONE);
        spinnertype.setAdapter(aa);
        filterBy.setAdapter(filterby);

        spinnertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                GetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (filterBy.getSelectedItem().equals("All")) {
                    searchbarcode.setHint("Search by patient name or barcode");
                } else if (filterBy.getSelectedItem().equals("Name")) {
                    searchbarcode.setHint("Search by patient name");
                } else if (filterBy.getSelectedItem().equals("Barcode")) {
                    searchbarcode.setHint("Search by patient barcode");
                } else {
                    searchbarcode.setHint("Search by patient name or barcode");
                }
                /*String filterstr = parent.getItemAtPosition(position).toString();
                searchbarcode.setQueryHint(filterstr);

                if (FilterReport.size() != 0) {
                    final List<TrackDetModel> filteredModelList = filter(FilterReport, filterstr);
                    adapter.setFilter(filteredModelList);
                    ListReportStatus.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {

                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*
        searchbarcode.addTextChangedListener(new TextWatcher() {
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
                if (s1.length() > 0) {
                    final List<TrackDetModel> filteredModelList = filter(FilterReport, s1);
                    adapter.setFilter(filteredModelList);
                    ListReportStatus.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter = new ResultDtlAdapter(getContext(), FilterReport);
                    ListReportStatus.setAdapter(adapter);
                }
            }
        });
*/

        if (filterBy.getSelectedItem().equals("All") || filterBy.getSelectedItem().equals("Select Filter By")) {
            searchbarcode.setHint("Search by patient name or barcode");
            searchbarcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        GetData();
                    }

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
                    if (FilterReport != null) {
                        for (int i = 0; i < FilterReport.size(); i++) {

                            if (filterBy.getSelectedItem().equals("All")) {
                                final String text = FilterReport.get(i).getBarcode().toLowerCase();

                                if (FilterReport.get(i).getBarcode() != null || !FilterReport.get(i).getBarcode().equals("")) {
                                    barcode = FilterReport.get(i).getBarcode().toLowerCase();
                                }
                                if (FilterReport.get(i).getName() != null || !FilterReport.get(i).getName().equals("")) {
                                    name = FilterReport.get(i).getName().toLowerCase();
                                }

                                if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                        (name != null && name.contains(s1))) {
                                    String testname = FilterReport.get(i).getName();
                                    filterPatientsArrayList.add(FilterReport.get(i));

                                }

                            } else if (filterBy.getSelectedItem().equals("Name")) {
                                final String text = FilterReport.get(i).getName().toLowerCase();

                                if (FilterReport.get(i).getName() != null || !FilterReport.get(i).getName().equals("")) {
                                    name = FilterReport.get(i).getName().toLowerCase();
                                }

                                if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                        (name != null && name.contains(s1))) {
                                    String testname = FilterReport.get(i).getName();
                                    filterPatientsArrayList.add(FilterReport.get(i));

                                }


                            } else if (filterBy.getSelectedItem().equals("Barcode")) {

                            }
                            callAdapter(filterPatientsArrayList);
                        }
                    }
                    // filter your list from your input
                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });
        } else if (filterBy.getSelectedItem().equals("Name")) {
            searchbarcode.setHint("Search by patient name ");
            searchbarcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        GetData();
                    }

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
                    if (FilterReport != null) {
                        for (int i = 0; i < FilterReport.size(); i++) {

                            final String text = FilterReport.get(i).getName().toLowerCase();

                            if (FilterReport.get(i).getName() != null || !FilterReport.get(i).getName().equals("")) {
                                name = FilterReport.get(i).getName().toLowerCase();
                            }

                            if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                    (name != null && name.contains(s1))) {
                                String testname = FilterReport.get(i).getName();
                                filterPatientsArrayList.add(FilterReport.get(i));

                            }
                     /*       adapter = new ResultDtlAdapter(getContext(), filterPatientsArrayList);
                            ListReportStatus.setAdapter(adapter);*/

                            callAdapter(filterPatientsArrayList);
                        }
                    }
                    // filter your list from your input
                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });

        } else if (filterBy.getSelectedItem().equals("Barcode")) {
            searchbarcode.setHint("Search by patient barcode");
            searchbarcode.addTextChangedListener(new TextWatcher() {
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
                    filterPatientsArrayList = new ArrayList<>();
                    String barcode = "";
                    String name = "";
                    if (FilterReport != null) {
                        for (int i = 0; i < FilterReport.size(); i++) {
                            final String text = FilterReport.get(i).getBarcode().toLowerCase();

                            if (FilterReport.get(i).getBarcode() != null || !FilterReport.get(i).getBarcode().equals("")) {
                                barcode = FilterReport.get(i).getBarcode().toLowerCase();
                            }
                            if (FilterReport.get(i).getBarcode() != null || !FilterReport.get(i).getBarcode().equals("")) {
                                barcode = FilterReport.get(i).getBarcode().toLowerCase();
                            }

                            if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                    (barcode != null && barcode.contains(s1))) {
                                String testname = FilterReport.get(i).getBarcode();
                                filterPatientsArrayList.add(FilterReport.get(i));

                            }

                            callAdapter(filterPatientsArrayList);
                        }
                    }
                    // filter your list from your input
                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });

        } else {
            searchbarcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        GetData();
                    }
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
                    if (FilterReport != null) {
                        for (int i = 0; i < FilterReport.size(); i++) {

                            if (filterBy.getSelectedItem().equals("All")) {
                                final String text = FilterReport.get(i).getBarcode().toLowerCase();

                                if (FilterReport.get(i).getBarcode() != null || !FilterReport.get(i).getBarcode().equals("")) {
                                    barcode = FilterReport.get(i).getBarcode().toLowerCase();
                                }
                                if (FilterReport.get(i).getName() != null || !FilterReport.get(i).getName().equals("")) {
                                    name = FilterReport.get(i).getName().toLowerCase();
                                }

                                if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                        (name != null && name.contains(s1))) {
                                    String testname = FilterReport.get(i).getName();
                                    filterPatientsArrayList.add(FilterReport.get(i));
                                }


                            } else if (filterBy.getSelectedItem().equals("Name")) {
                                final String text = FilterReport.get(i).getName().toLowerCase();

                                if (FilterReport.get(i).getName() != null || !FilterReport.get(i).getName().equals("")) {
                                    name = FilterReport.get(i).getName().toLowerCase();
                                }

                                if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                        (name != null && name.contains(s1))) {
                                    String testname = FilterReport.get(i).getName();
                                    filterPatientsArrayList.add(FilterReport.get(i));

                                } else {

                                }


                            } else if (filterBy.getSelectedItem().equals("Barcode")) {
                                final String text = FilterReport.get(i).getBarcode().toLowerCase();

                                if (FilterReport.get(i).getBarcode() != null || !FilterReport.get(i).getBarcode().equals("")) {
                                    barcode = FilterReport.get(i).getBarcode().toLowerCase();
                                }

                                if (text.contains(s1) || (barcode != null && barcode.contains(s1)) ||
                                        (barcode != null && barcode.contains(s1))) {
                                    String testname = FilterReport.get(i).getBarcode();
                                    filterPatientsArrayList.add(FilterReport.get(i));

                                } else {

                                }

                            }
                            callAdapter(filterPatientsArrayList);
                        }
                    }
                    // filter your list from your input
                    //you can use runnable postDelayed like 500 ms to delay search text
                }
            });

        }


        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

        java.util.Date date = null;
        date = new Date();
        showDateTotxt = outputFormat.format(date);

        Date currentDate = new Date();
        SimpleDateFormat sdfa = new SimpleDateFormat("yyyy-MM-dd");
        passDateTogetData = sdfa.format(currentDate);


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);

        } else {
            GetData();
            offline_img.setVisibility(View.GONE);
        }


        return view;
    }

    private void callAdapter(ArrayList<TrackDetModel> modelArrayList) {
        if (modelArrayList.size() > 0) {
            ListReportStatus.setVisibility(View.VISIBLE);
            adapter = new ResultDtlAdapter(getActivity(), modelArrayList);
            ListReportStatus.setAdapter(adapter);
            nodata.setVisibility(View.GONE);
        } else {
            ListReportStatus.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = null;
        try {
            metrics = context.getResources().getDisplayMetrics();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
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

    private void setCalendarAdapter(ArrayList<getAllDays> SelectedMonthData) {
        customCalendarAdapter = new CustomCalendarAdapter(getActivity(), SelectedMonthData, this);
        calendarView.setAdapter(customCalendarAdapter);
        calendarView.getLayoutManager().scrollToPosition(getPositionToset - 5);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    private List<TrackDetModel> filter(List<TrackDetModel> models, String query) {
        query = query.toLowerCase();
        final List<TrackDetModel> filteredModelList = new ArrayList<>();
        for (TrackDetModel model : models) {
            final String name = model.getName();
            final String barcode = model.getBarcode();
            final String type = model.getSample_type();
            if (model.getName().toString().contains(query.toUpperCase()) || model.getBarcode().contains(query.toUpperCase())) {
                filteredModelList.add(model);
                adapter.notifyDataSetChanged();
                break;
            }


        }
        return filteredModelList;
    }

    private void GetData() {
        barProgressDialog.show();

        PostQue = Volley.newRequestQueue(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            if (spinnertype.getSelectedItem().toString().equals("Select Type")) {
                passToSpinner = "Reported";
            } else {
                passToSpinner = spinnertype.getSelectedItem().toString();
            }
            jsonObject.put("API_Key", api_key);
            jsonObject.put("result_type", passToSpinner);
            jsonObject.put("tsp", user);
            jsonObject.put("tsp", user);
            jsonObject.put("date", GlobalClass.date);
            jsonObject.put("key", "");
            jsonObject.put("value", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                Request.Method.GET, Api.ResultLIVE + "/" + api_key + "/" + passToSpinner + "/" + user + "/" + passDateTogetData + "/" + "key/value", jsonObject,//passToAPI
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }
                            Log.e(TAG, "onResponse: RESPONSE" + response);
                            String reportStatus = response.optString("reportStatus");
                            responsetoshow = response.optString("response", "");

                            if (responsetoshow.equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(getActivity());
                            } else {
                                if (reportStatus.equals("ALLOW")) {
                                    JSONArray jsonArray = response.optJSONArray(Constants.patients);
                                    if (jsonArray != null) {
                                        nodata.setVisibility(View.GONE);
                                        FilterReport = new ArrayList<TrackDetModel>();

                                        for (int j = 0; j < jsonArray.length(); j++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                                            TrackDetModel trackdetails = new TrackDetModel();
                                            trackdetails.setDownloaded(jsonObject.optString(Constants.Downloaded).toString());
                                            trackdetails.setRef_By(jsonObject.optString(Constants.Ref_By).toString());
                                            trackdetails.setTests(jsonObject.optString(Constants.Tests).toString());
                                            trackdetails.setBarcode(jsonObject.optString(Constants.barcode).toString());
                                            // trackdetails.setCancel_tests_with_remark(jsonObject.optString(Constants.cancel_tests_with_remark).toString());
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

                                            FilterReport.add(trackdetails);
                                        }
                                        adapter = new ResultDtlAdapter(getContext(), FilterReport);
                                        ListReportStatus.setAdapter(adapter);
                                        ListReportStatus.setVisibility(View.VISIBLE);
                                        searchbarcode.setVisibility(View.VISIBLE);
                                    } else {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        ListReportStatus.setVisibility(View.GONE);
                                        searchbarcode.setVisibility(View.GONE);
                                        nodata.setVisibility(View.VISIBLE);

                                    }
                                } else {
                                    TastyToast.makeText(mContext, responsetoshow, TastyToast.LENGTH_SHORT, TastyToast.ERROR);


                                    final AlertDialog alertDialog = new AlertDialog.Builder(
                                            mContext).create();

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Update Ledger !");

                                    // Setting Dialog Message
                                    alertDialog.setMessage(ToastFile.update_ledger);
                                    // Setting OK Button
                                    alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent i = new Intent(mContext, Payment_Activity.class);
                                            i.putExtra("COMEFROM", "FilterReport");
                                            mContext.startActivity(i);
                                           /* Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                            httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                                            startActivity(httpIntent);*/
                                            // Write your code here to execute after dialog closed
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        } catch (Exception e) {

                            /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }*/
                            if (mContext instanceof Activity) {
                                if (!((Activity) mContext).isFinishing())
                                    barProgressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /*if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }*/
                if (mContext instanceof Activity) {
                    if (!((Activity) mContext).isFinishing())
                        barProgressDialog.dismiss();
                }
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
        Log.e(TAG, "onResponse: URL" + jsonObjectRequest);
        Log.e(TAG, "GetData: json" + jsonObject);
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
            showDate.setText("Result for " + getdateToSet);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        linearLayoutManager.scrollToPositionWithOffset(getPositionToset, 20);
        GetData();
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
