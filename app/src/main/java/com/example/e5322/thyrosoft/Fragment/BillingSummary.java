package com.example.e5322.thyrosoft.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.BillingSummaryAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BillingSummaryMOdel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.API.Constants.toDate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillingSummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillingSummary extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SimpleDateFormat sdf;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String toDateTxt = "";
    public String fromDateTxt = "";
    public String Date = "", tempMonth="";
    private OnFragmentInteractionListener mListener;
    TextView txtFromDate, txt_to_date;
    private int mYear, mMonth, mDay;
    public static RequestQueue PostQue;
    ProgressDialog barProgressDialog;
    SharedPreferences sharedpreferences;
    ListView list_billingSummary;
    BillingSummaryAdapter adapter;
    private DatePicker dpAppointmentDate;
    String user;
    String passwrd;
    LinearLayout parent_ll;
    LinearLayout offline_img;
    String access;
    java.util.Date daysBeforeDate;
    String api_key;
    int fromday, frommonth, fromyear;
    SharedPreferences prefsBilling;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private Date result, fromDate, toDate;
    private SimpleDateFormat format;

    public BillingSummary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillingSummaryMOdel.
     */
    // TODO: Rename and change types and number of parameters
    public static BillingSummary newInstance(String param1, String param2) {
        BillingSummary fragment = new BillingSummary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        toDateTxt = sdf.format(cl.getTime());
        try {
            toDate = sdf.parse(toDateTxt);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calbackdate = Calendar.getInstance();
        calbackdate.setTime(new Date());
        calbackdate.add(Calendar.DAY_OF_YEAR, -10);
        daysBeforeDate = calbackdate.getTime();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateTxt = sdf.format(daysBeforeDate);
        try {
            fromDate = sdf.parse(fromDateTxt);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal= Calendar.getInstance();
         result=cal.getTime();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_billing, container, false);

        txtFromDate = (TextView) rootView.findViewById(R.id.txt_from_date);
        txt_to_date = (TextView) rootView.findViewById(R.id.txt_to_date);
        list_billingSummary = (ListView) rootView.findViewById(R.id.list_billingSummary);
        offline_img = (LinearLayout) rootView.findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) rootView.findViewById(R.id.parent_ll);

        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        txtFromDate.setText(fromDateTxt);
        txt_to_date.setText(toDateTxt);

        prefsBilling = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefsBilling.getString("Username", null);
        passwrd = prefsBilling.getString("password", null);
        access = prefsBilling.getString("ACCESS_TYPE", null);
        api_key = prefsBilling.getString("API_KEY", null);

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
            GetData();
        }

        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == txtFromDate) {
                    final Calendar c = Calendar.getInstance();
                    c.setTime(fromDate);
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                                    String getDateSecond = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;

                                    try {
                                        fromDate = sdf.parse(getDateSecond);
                                        fromDateTxt = sdf.format(fromDate);
                                        txtFromDate.setText(fromDateTxt);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                        offline_img.setVisibility(View.VISIBLE);
                                        parent_ll.setVisibility(View.GONE);
                                    } else {
                                        offline_img.setVisibility(View.GONE);
                                        parent_ll.setVisibility(View.VISIBLE);
                                        GetData();
                                    }
                                }
                            }, mYear, mMonth, mDay);

                    datePickerDialog.getDatePicker().setMaxDate(toDate.getTime()-2);
                    datePickerDialog.show();
                }
            }
        });


        txt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txt_to_date) {
                    final Calendar c = Calendar.getInstance();
                    c.setTime(toDate);
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    long time3 = c.getTimeInMillis();

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                                    String getDateSecond = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;


                                    try {
                                        toDate = sdf.parse(getDateSecond);
                                        toDateTxt = sdf.format(toDate);
                                        txt_to_date.setText(toDateTxt);
                                        System.out.println(txt_to_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }



                                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                                        offline_img.setVisibility(View.VISIBLE);
                                        parent_ll.setVisibility(View.GONE);
                                    } else {
                                        offline_img.setVisibility(View.GONE);
                                        parent_ll.setVisibility(View.VISIBLE);
                                        GetData();
                                    }

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(fromDate.getTime());
                    datePickerDialog.getDatePicker().setMaxDate(result.getTime()-2);
                    datePickerDialog.show();
                }
            }
        });


        return rootView;
    }


    private void GetData() {
        barProgressDialog.show();

        PostQue = Volley.newRequestQueue(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            String inputDate = txtFromDate.getText().toString();
            String inputDate1 = txt_to_date.getText().toString();


            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

            Date date = null;
            try {
                date = inputFormat.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(date);


            Date date1 = null;
            try {
                date1 = inputFormat.parse(inputDate1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr1 = outputFormat.format(date1);


            jsonObject.put("apiKey", api_key);
            jsonObject.put(Constants.date, Date);
            jsonObject.put(Constants.UserCode_billing, user);
            jsonObject.put(Constants.fromDate, outputDateStr);
            jsonObject.put(Constants.toDate, outputDateStr1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.billingSUMLIVE, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }

                            String responsetoshow = response.optString("response", "");

                            if (responsetoshow.equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(getActivity());
                            } else {
                                JSONArray jsonArray = response.optJSONArray(Constants.billingList);
                                if (jsonArray != null) {


                                    ArrayList<BillingSummaryMOdel> billingsumArray = new ArrayList<BillingSummaryMOdel>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        try {


                                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                                            BillingSummaryMOdel billsum = new BillingSummaryMOdel();

                                            billsum.setBilledAmount(jsonObject.optString(Constants.billedAmount).toString());
                                            billsum.setBillingDate(jsonObject.optString(Constants.billingDate).toString());
                                            billsum.setWorkLoad(jsonObject.optString(Constants.workLoad).toString());


                                            billingsumArray.add(billsum);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (billingsumArray.size() == 0) {
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }

                                    } else {
                                        adapter = new BillingSummaryAdapter(getContext(), billingsumArray);
                                        list_billingSummary.setAdapter(adapter);
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }

                                    }
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