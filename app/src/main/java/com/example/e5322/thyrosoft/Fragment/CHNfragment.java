package com.example.e5322.thyrosoft.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.CHN_Adapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.Models.chn_test;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CHNfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CHNfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CHNfragment extends RootFragment {
    static final int DATE_DIALOG_ID = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RequestQueue PostQue;
    static CHNfragment fragment;
    View view;
    int mYear, mMonth, mDay;
    String yesterdayDate;
    JSONArray jsonArrayTest;
    ;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    List<String> items;
    ProgressDialog barProgressDialog;
    CHN_Adapter adapter;
    TextView selectDate, no_view;
    ExpandableListView ListReportStatus;
    LinearLayout parent_ll;
    LinearLayout offline_img;
    ArrayList FilterReport;
    Calendar myCalendar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String halfTime;
    private String convertedDate;
    private String outputDateStr;
    private String putDate;
    private String getFormatDate;
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

    public CHNfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CHNfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CHNfragment newInstance(String param1, String param2) {
        fragment = new CHNfragment();
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
        view = inflater.inflate(R.layout.fragment_chnfragment, container, false);
        ListReportStatus = (ExpandableListView) view.findViewById(R.id.ListReportStatus);
        selectDate = (TextView) view.findViewById(R.id.selectDate);
        no_view = (TextView) view.findViewById(R.id.no_view);
        parent_ll = (LinearLayout) view.findViewById(R.id.parent_ll);
        offline_img = (LinearLayout) view.findViewById(R.id.offline_img);


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
        }

        yesterdayDate = GlobalClass.getYesterdaysDate;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
            GetData();
        }


        return view;
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        putDate = sdf.format(myCalendar.getTime());
        getFormatDate = sdf.format(myCalendar.getTime());

        selectDate.setText(putDate);
        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
            GetData();
        }
    }

    private void GetData() {

        System.out.println(GlobalClass.CHN_Date);
        if (GlobalClass.passDate != null) {
            convertedDate = GlobalClass.passDate;//

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

                GlobalClass.CHN_Date = convertedDate;
//            getActivity().setTitle("WOE "+convertedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
//        }
        }

        barProgressDialog = new ProgressDialog(getContext());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        PostQue = Volley.newRequestQueue(getContext());
        SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        String user = prefs.getString("Username", null);
        String passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = Api.Result + "/" + api_key + "/CHN/" + user + "/" + convertedDate + "/key/value";
        Log.e(TAG, "CHN API -->" + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }

                            String responsetoshow = response.optString("response", "");

                            if (responsetoshow.equalsIgnoreCase(caps_invalidApikey)) {
                                GlobalClass.redirectToLogin(getActivity());
                            } else {
                                JSONArray jsonArray = response.optJSONArray(Constants.patients);
                                Log.e(TAG, "onResponse: " + response);

                                // if(GlobalClass.dashboarddata.equals("Cancelled")) {
                                //  }
                                if (jsonArray != null) {
                                    //   nodata.setVisibility(View.GONE);
                                    FilterReport = new ArrayList<TrackDetModel>();
                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                                        TrackDetModel trackdetails = new TrackDetModel();

                                        items = Arrays.asList(jsonObject.optString(Constants.chn_test).toString().split("\\s*,\\s*"));

                                        ArrayList<chn_test> canArray = new ArrayList();


                                        for (int i = 0; i < items.size(); i++) {
                                            chn_test can = new chn_test();
                                            can.setTest(items.get(i));
                                            canArray.add(can);
                                        }

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
                                        trackdetails.setWo_sl_no(jsonObject.optString(Constants.wo_sl_no).toString());
                                        trackdetails.setChn_test_list(canArray);
                                        FilterReport.add(trackdetails);
                                        no_view.setVisibility(View.GONE);
                                        ListReportStatus.setVisibility(View.VISIBLE);
                                    }
                                /*{
"RES_ID":"RES0000","pageNo":null,"
patients":[
{"Downloaded":null,"Ref_By":"SELF","Tests":"SCRE","barcode":"I4421436","cancel_tests_with_remark":null,"chn_pending":"TSP","chn_test":"SCRE","confirm_status":null,"date":"13-06-2018","email":null,"isOrder":null,"labcode":"15593","leadId":null,"name":"SANJAY  KUMAR (40Y\/M)","patient_id":"BIH03126248462744425","pdflink":null,"sample_type":null,"scp":null,"sct":null,"su_code2":null,"wo_sl_no":null}]
,"reportStatus":"ALLOW","response":"1","totalPages":null
}*/
                                    adapter = new CHN_Adapter(getContext(), FilterReport, items, fragment, convertedDate);
                                    ListReportStatus.setAdapter(adapter);
                                    // ListReportStatus.setOnGroupClickListener(myListGroupClicked);
                                } else {
                                    no_view.setVisibility(View.VISIBLE);
                                    ListReportStatus.setVisibility(View.GONE);
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
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
        queue.add(jsonObjectRequest);
        Log.e(TAG, "GetData: URL" + jsonObjectRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setNewFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(fragment).attach(CHNfragment.this).commit();
//        adapter.notifyDataSetChanged();
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
