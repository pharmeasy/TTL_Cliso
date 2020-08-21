package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.CancelledAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ResultLIVE_Controller;
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

import androidx.fragment.app.Fragment;

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
    Calendar myCalendar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<TrackDetModel> FilterReport = new ArrayList<TrackDetModel>();
    ArrayList canTest = new ArrayList<CancelledModel>();
    private OnFragmentInteractionListener mListener;
    View rootview;
    ExpandableListView ListReportStatus;
    CancelledAdapter adapter;
    JSONArray jsonArraycan;
    private String convertedDate;
    static DashboardFragment fragment;
    private String outputDateStr;

    TextView selectDate;
    private String putDate;
    private String getFormatDate;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    Activity mActivity;

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
        mActivity = getActivity();
        rootview = inflater.inflate(R.layout.cancelledfragment_dashboard, container, false);

        initview();
        initListner();

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String showDate = sdf.format(d);
        myCalendar = Calendar.getInstance();
        myCalendar.add(Calendar.DAY_OF_MONTH, -2);
        myCalendar.setTime(d);


        GlobalClass.SetText(selectDate, showDate);
        GetData();
        return rootview;
    }

    private void initListner() {
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
    }

    private void initview() {
        ListReportStatus = (ExpandableListView) rootview.findViewById(R.id.ListReportStatus);
        selectDate = (TextView) rootview.findViewById(R.id.selectDate);
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
        GlobalClass.SetText(selectDate, putDate);
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

            GlobalClass.SetText(selectDate, outputDateStr);
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

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        String user = prefs.getString("Username", null);
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

        try {
            if (ControllersGlobalInitialiser.resultLIVE_controller != null) {
                ControllersGlobalInitialiser.resultLIVE_controller = null;
            }
            ControllersGlobalInitialiser.resultLIVE_controller = new ResultLIVE_Controller(mActivity, DashboardFragment.this);
            ControllersGlobalInitialiser.resultLIVE_controller.getresultcontroller(api_key, "Cancelled", user, convertedDate, queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNewFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).attach(DashboardFragment.this).commit();
    }

    public void getResponse(JSONObject response) {

        try {
            JSONArray jsonArray = response.optJSONArray(Constants.patients);

            Log.e(TAG, "onResponse: " + response);

            if (jsonArray != null) {
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

            } else {
                GlobalClass.showTastyToast(mActivity, ToastFile.no_sample_found, 2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
