package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.TrackDetAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ResultLIVE_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
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
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
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
    String user = "", yesterdayDate, getDateOnly;
    String api_key = "", convertedDate;
    SharedPreferences sharedpreferences;
    TrackDetAdapter adapter;
    public static RequestQueue PostQue;
    SearchView search;
    private OnFragmentInteractionListener mListener;
    Button buttonnow;
    LinearLayout main;
    TextView nodata, nodatatv;

    ConnectionDetector cd;
    Activity mActivity;
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
    String getDate = GlobalClass.getYesterdaysDate;
    ArrayList<TrackDetModel> trackDetArray = new ArrayList<TrackDetModel>();
    private String passToAPI;
    private String halfTime;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();

    public ResultFragment() {
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
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        cd = new ConnectionDetector(mActivity);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedpreferences = getActivity().getSharedPreferences(Constants.MyPREFERENCES, MODE_PRIVATE);
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        halfTime = getDate.substring(11, getDate.length() - 0);


        GlobalClass.saveDtaeTopass = halfTime;

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = inputFormat.parse(halfTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        Date = outputDateStr;
        GlobalClass.date = getDate;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);


        initViews(view);

        iniListner();

        GetData();

        return view;
    }

    private void iniListner() {

        buttonnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FilterReport filt = new FilterReport();
                    FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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

                if (position == 0) {
                    listviewreport.setVisibility(View.VISIBLE);
                    GetData();
                } else if (position == 2) {
                    ArrayList<TrackDetModel> DownloadList = new ArrayList<TrackDetModel>();

                    if (GlobalClass.CheckArrayList(trackDetArray)){
                        for (int i = 0; i < trackDetArray.size(); i++) {
                            TrackDetModel trackdetails = new TrackDetModel();
                            trackdetails = trackDetArray.get(i);

                            if (!GlobalClass.isNull(trackDetArray.get(i).getDownloaded())) {
                                DownloadList.add(trackdetails);
                                adapter = new TrackDetAdapter(getContext(), DownloadList);
                                listviewreport.setAdapter(adapter);
                                notifyAll();
                            }
                        }
                    }


                    if (DownloadList.size() == 0) {
                        ShowAlert();
                        listviewreport.setVisibility(View.GONE);
                    } else {
                        listviewreport.setVisibility(View.VISIBLE);
                    }
                } else {
                    GetData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<TrackDetModel> filteredModelList = filter(trackDetArray, newText);

                adapter.setFilter(filteredModelList);
                listviewreport.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    private void initViews(View view) {
        listviewreport = (ListView) view.findViewById(R.id.listviewreport);
        setHasOptionsMenu(true);
        spinnertype = (Spinner) view.findViewById(R.id.spinnertype);
        buttonnow = (Button) view.findViewById(R.id.buttonnow);
        search = (SearchView) view.findViewById(R.id.searchView);
        main = (LinearLayout) view.findViewById(R.id.main);

        nodata = (TextView) view.findViewById(R.id.nodata);
        nodatatv = (TextView) view.findViewById(R.id.nodatatv);

        java.util.Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String[] spinner = {"All", "Ready", "Downloaded"};
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, spinner);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnertype.setAdapter(aa);

        yesterdayDate = GlobalClass.getYesterdaysDate;
        getDateOnly = yesterdayDate.substring(11, yesterdayDate.length());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(getDateOnly);
            SimpleDateFormat sdfdata = new SimpleDateFormat("yyyy-MM-dd");
            convertedDate = sdfdata.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");//dd-MM-yyyy
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");//yyyy-MM-dd

        Date date = null;
        try {
            date = inputFormat.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        passToAPI = outputFormat.format(date);

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


        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", null);
            api_key = prefs.getString("API_KEY", null);
            jsonObject.put("API_Key", api_key);
            jsonObject.put("result_type", "Reported");
            jsonObject.put("tsp", user);
            jsonObject.put("date", Date);
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
            ControllersGlobalInitialiser.resultLIVE_controller = new ResultLIVE_Controller(mActivity, ResultFragment.this);
            ControllersGlobalInitialiser.resultLIVE_controller.getresultcontroller(api_key, "/REPORTED", user, Date, queue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public void ShowAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setMessage(ToastFile.noDataAvailable)
                .setCancelable(false)
                .create();

        alertDialog.show();
        alertDialog.dismiss();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.reportmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: RESPONSE" + response);

            if (!GlobalClass.isNull(response.optString("response")) && response.optString("response").equals(ToastFile.no_sample_found)) {
                nodatatv.setVisibility(View.VISIBLE);
                GlobalClass.SetText(nodatatv, response.optString("response"));
                listviewreport.setVisibility(View.GONE);
            } else {
                nodatatv.setVisibility(View.GONE);
                listviewreport.setVisibility(View.VISIBLE);

                JSONArray jsonArray = response.optJSONArray(Constants.patients);
                if (jsonArray != null) {

                    trackDetArray = new ArrayList<TrackDetModel>();

                    if (jsonArray.length()!=0){
                        for (int j = 0; j < jsonArray.length(); j++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(j);
                            TrackDetModel trackdetails = new TrackDetModel();

                            trackdetails.setDownloaded(jsonObject.optString(Constants.Downloaded).toString());
                            trackdetails.setRef_By(jsonObject.optString(Constants.Ref_By).toString());
                            trackdetails.setTests(jsonObject.optString(Constants.Tests).toString());
                            trackdetails.setBarcode(jsonObject.optString(Constants.barcode).toString());
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
                    }


                    if (trackDetArray.size() == 0) {
                        yesterday();
                        nodata.setVisibility(View.VISIBLE);

                    } else {
                        nodata.setVisibility(View.GONE);
                        adapter = new TrackDetAdapter(getContext(), trackDetArray);
                        listviewreport.setAdapter(adapter);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterReport:
                FilterReport filt = new FilterReport();
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_mainLayout, filt);
                fragmentTransaction.commit();

                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
