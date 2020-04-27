package com.example.e5322.thyrosoft.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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
import com.example.e5322.thyrosoft.Adapter.CancelledAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CancelledModel;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RequestQueue PostQue;
    int Flag = 0;
    String yesterdaysDate;
    Calendar myCalendar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog barProgressDialog;
    ArrayList<TrackDetModel> FilterReport = new ArrayList<TrackDetModel>();
    ArrayList canTest = new ArrayList<CancelledModel>();
    private OnFragmentInteractionListener mListener;
    View rootview;
    LinearLayout childView;
    ExpandableListView ListReportStatus;
    CancelledAdapter adapter;
    JSONArray jsonArraycan;
    private String convertedDate;
    static DashboardFragment fragment;
    private String outputDateStr;

    TextView selectDate;
    private String putDate;
    private String getFormatDate;
    private String TAG= ManagingTabsActivity.class.getSimpleName().toString();

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootview = inflater.inflate(R.layout.cancelledfragment_dashboard, container, false);
        ListReportStatus = (ExpandableListView) rootview.findViewById(R.id.ListReportStatus);
        selectDate = (TextView) rootview.findViewById(R.id.selectDate);

        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);

        selectDate.setText(showDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        GetData();
        return rootview;
    }

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

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        selectDate.setText(putDate);
        GetData();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void GetData() {


        if (GlobalClass.passDateFromLead != null) {
            convertedDate = GlobalClass.passDateFromLead;//
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

            Date date = null;
            try {
                date = inputFormat.parse(convertedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            outputDateStr = outputFormat.format(date);

            selectDate.setText(outputDateStr);
//            getActivity().setTitle("WOE "+convertedDate);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date myDate = null;
            try {
                String passDate = selectDate.getText().toString();
                myDate = dateFormat.parse(passDate);
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

                GlobalClass.lead_date = convertedDate;
//            getActivity().setTitle("WOE "+convertedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
//        }
        }

        PostQue = Volley.newRequestQueue(getContext());

        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        String user = prefs.getString("Username", null);
        String passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("API_Key", api_key);
            jsonObject.put("result_type", "Cancelled");
            jsonObject.put("tsp", user);
            jsonObject.put("tsp", user);
            jsonObject.put("date", convertedDate);
            jsonObject.put("key", "");
            jsonObject.put("value", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.Result + "/" + api_key + "/" + "Cancelled" + "/" + user + "/" + convertedDate + "/" + "key/value", jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(barProgressDialog!=null && barProgressDialog.isShowing()){               barProgressDialog.dismiss();}

                            JSONArray jsonArray = response.optJSONArray(Constants.patients);

                            Log.e(TAG, "onResponse: "+response );
                            if (jsonArray != null) {
                                //   nodata.setVisibility(View.GONE);
                                FilterReport = new ArrayList<TrackDetModel>();
                                canTest = new ArrayList<CancelledModel>();
                                for (int j = 0; j < jsonArray.length(); j++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    TrackDetModel trackdetails = new TrackDetModel();
                                    jsonArraycan = jsonObject.optJSONArray((Constants.cancel_tests_with_remark));
                                    ArrayList<CancelledModel> canArray = new ArrayList();

                                    for (int i = 0; i < jsonArraycan.length(); i++) {
                                        JSONObject canObj = jsonArraycan.getJSONObject(i);
                                        CancelledModel can = new CancelledModel();
                                        can.setRemarks(canObj.getString(Constants.remarks));
                                        can.setTest_code(canObj.getString(Constants.test_code));
                                        canArray.add(can);
                                    }
                                    trackdetails.setDownloaded(jsonObject.optString(Constants.Downloaded).toString());
                                    trackdetails.setRef_By(jsonObject.optString(Constants.Ref_By).toString());
                                    trackdetails.setTests(jsonObject.optString(Constants.Tests).toString());
                                    trackdetails.setBarcode(jsonObject.optString(Constants.barcode).toString());
                                    trackdetails.setCancel_tests_with_remark(canArray);
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

                                    FilterReport.add(trackdetails);
                                }

                                adapter = new CancelledAdapter(getContext(), fragment, FilterReport);
                                ListReportStatus.setAdapter(adapter);

                                // ListReportStatus.setOnGroupClickListener(myListGroupClicked);
                            } else {
                                Toast.makeText(getActivity(), ToastFile.no_sample_found, Toast.LENGTH_SHORT).show();
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
        Log.e(TAG, "GetData: URL"+ jsonObjectRequest);
        Log.e(TAG, "GetData: JSON"+ jsonObject);

    }

    public void setNewFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).attach(DashboardFragment.this).commit();
    }


    /*private ExpandableListView.OnGroupClickListener myListGroupClicked =  new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            //get the group header
         LinearLayout   cancelled_Layout = (LinearLayout) v.findViewById(R.id.cancelled_Layout);
          childView = (LinearLayout) v.findViewById(R.id.childView);
            cancelled_Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Flag==0) {
                        childView.setVisibility(View.VISIBLE);
                        Flag=1;
                    }else{
                        Flag=0;
                    }
                }
            });


            //display it or do something with it

            return false;
        }
    };

    */

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
