package com.example.e5322.thyrosoft.Fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.BillingSummaryAdapter;
import com.example.e5322.thyrosoft.Controller.BilligsumController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.BillingSummaryRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.BillingSummaryResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

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
    public static RequestQueue PostQue;
    public String toDateTxt = "";
    public String fromDateTxt = "";
    public String Date = "", tempMonth = "";
    TextView txtFromDate, txt_to_date;
    ListView list_billingSummary;
    BillingSummaryAdapter adapter;
    String user;
    String passwrd;
    LinearLayout parent_ll;
    LinearLayout offline_img;
    String access;
    java.util.Date daysBeforeDate;
    String api_key;
    SharedPreferences prefsBilling;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private SimpleDateFormat sdf;
    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;
    private int mYear, mMonth, mDay;
    private Date result, fromDate, toDate;
    TextView txt_nodata;

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

        Calendar cal = Calendar.getInstance();
        result = cal.getTime();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_billing, container, false);

        initViews(rootView);

        GlobalClass.SetText(txtFromDate, fromDateTxt);
        GlobalClass.SetText(txt_to_date, toDateTxt);

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
        }
        GetData();
        initListner();

        return rootView;
    }

    private void initListner() {

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

                                    String getDateSecond = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                    try {
                                        fromDate = sdf.parse(getDateSecond);
                                        fromDateTxt = sdf.format(fromDate);
                                        GlobalClass.SetText(txtFromDate, fromDateTxt);
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

                    datePickerDialog.getDatePicker().setMaxDate(toDate.getTime() - 2);
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

                                    String getDateSecond = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;


                                    try {
                                        toDate = sdf.parse(getDateSecond);
                                        toDateTxt = sdf.format(toDate);
                                        GlobalClass.SetText(txt_to_date, toDateTxt);
                                        Log.v("TAG", txt_to_date.getText().toString());
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
                    datePickerDialog.getDatePicker().setMaxDate(result.getTime() - 2);
                    datePickerDialog.show();
                }
            }
        });


    }

    private void initViews(View rootView) {
        txtFromDate = (TextView) rootView.findViewById(R.id.txt_from_date);
        txt_to_date = (TextView) rootView.findViewById(R.id.txt_to_date);
        txt_nodata = rootView.findViewById(R.id.txt_nodata);
        list_billingSummary = (ListView) rootView.findViewById(R.id.list_billingSummary);
        offline_img = (LinearLayout) rootView.findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) rootView.findViewById(R.id.parent_ll);


    }


    private void GetData() {

        JSONObject jsonObject = null;
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

            BillingSummaryRequestModel requestModel = new BillingSummaryRequestModel();
            requestModel.setApiKey(api_key);
            requestModel.setDate(Date);
            requestModel.setUserCode(user);
            requestModel.setFromDate(outputDateStr);
            requestModel.setToDate(outputDateStr1);

            Gson gson = new Gson();
            String json = gson.toJson(requestModel);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = GlobalClass.setVolleyReq(getContext());

        try {
            if (ControllersGlobalInitialiser.billigsumController != null) {
                ControllersGlobalInitialiser.billigsumController = null;
            }
            ControllersGlobalInitialiser.billigsumController = new BilligsumController(getActivity(), BillingSummary.this);
            ControllersGlobalInitialiser.billigsumController.getbillsummcontroller(jsonObject, queue);
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

    public void getbillsummaryresp(JSONObject response) {
        Log.e(TAG, "onResponse: " + response);
        try {
            BillingSummaryResponseModel responseModel = new Gson().fromJson(String.valueOf(response), BillingSummaryResponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(Constants.caps_invalidApikey)) {
                    GlobalClass.redirectToLogin(getActivity());
                } else {
                    if (GlobalClass.CheckArrayList(responseModel.getBillingList())) {
                        list_billingSummary.setVisibility(View.VISIBLE);
                        txt_nodata.setVisibility(View.GONE);
                        adapter = new BillingSummaryAdapter(getContext(), responseModel.getBillingList());
                        list_billingSummary.setAdapter(adapter);
                    } else {
                        list_billingSummary.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                GlobalClass.showTastyToast(getActivity(), ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
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