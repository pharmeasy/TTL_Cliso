package com.example.e5322.thyrosoft.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.BillingSummaryAdapter;
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
    // ProgressDialog barProgressDialog;
    SharedPreferences sharedpreferences;
    ListView list_billingSummary;
    BillingSummaryAdapter adapter;
    String user;
    String passwrd;
    LinearLayout parent_ll;
    LinearLayout offline_img, ll_noauth, ll_fragment_billing;
    String access;
    java.util.Date daysBeforeDate;
    String api_key;
    int fromday, frommonth, fromyear;
    SharedPreferences prefsBilling;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private SimpleDateFormat sdf;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private int mYear, mMonth, mDay;
    private DatePicker dpAppointmentDate;
    private Date result, fromDate, toDate;
    private SimpleDateFormat format;
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getView() != null){
            GlobalClass.ComingFrom = "Billing_Screen";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_billing, container, false);

        txtFromDate = (TextView) rootView.findViewById(R.id.txt_from_date);
        txt_to_date = (TextView) rootView.findViewById(R.id.txt_to_date);
        txt_nodata = rootView.findViewById(R.id.txt_nodata);
        list_billingSummary = (ListView) rootView.findViewById(R.id.list_billingSummary);
        offline_img = (LinearLayout) rootView.findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) rootView.findViewById(R.id.parent_ll);
        ll_noauth = (LinearLayout) rootView.findViewById(R.id.ll_noauth);
        ll_fragment_billing = (LinearLayout) rootView.findViewById(R.id.ll_fragment_billing);
  /*      barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
*/

        //  ProgressDialog barProgressDialog = GlobalClass.ShowprogressDialog(getContext());

        txtFromDate.setText(fromDateTxt);
        txt_to_date.setText(toDateTxt);

        prefsBilling = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefsBilling.getString("Username", null);
        passwrd = prefsBilling.getString("password", null);
        access = prefsBilling.getString("ACCESS_TYPE", null);
        api_key = prefsBilling.getString("API_KEY", null);

        if (Global.isStaff(getActivity()) || Global.getLoginType(getActivity()) == Constants.PEflag) {
            ll_noauth.setVisibility(View.VISIBLE);
            ll_fragment_billing.setVisibility(View.GONE);
        } else {
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

                                        String getDateSecond = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

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
                        datePickerDialog.getDatePicker().setMaxDate(result.getTime() - 2);
                        datePickerDialog.show();
                    }
                }
            });
        }


        return rootView;
    }


    private void GetData() {
        final ProgressDialog barProgressDialog = GlobalClass.ShowprogressDialog(getActivity());

        PostQue = GlobalClass.setVolleyReq(getContext());

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.billingSUMLIVE, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: " + response);
                        try {
                            GlobalClass.hideProgress(getActivity(), barProgressDialog);

                            BillingSummaryResponseModel responseModel = new Gson().fromJson(String.valueOf(response), BillingSummaryResponseModel.class);
                            if (responseModel != null) {
                                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(Constants.caps_invalidApikey)) {
                                    GlobalClass.redirectToLogin(getActivity());
                                } else {
                                    if (responseModel.getBillingList() != null && responseModel.getBillingList().size() > 0) {
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
                                Toast.makeText(getActivity(), ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
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
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
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