package com.example.e5322.thyrosoft.RateCalculator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Adapter.ExapandableAdpterForB2CRate_Calculator;
import com.example.e5322.thyrosoft.Interface.InterfaceRateCAlculator;
import com.example.e5322.thyrosoft.MainModelForAllTests.OUTLAB_TESTLIST_GETALLTESTS;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.RateCalculatorForModels.GetMainModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Product_Rate_CalculatorModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.RateCalB2B_MASTERS_Main_Model;
import com.example.e5322.thyrosoft.TestListModel.TestModel;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Rate_CAlculator_B2C_Rate_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Rate_CAlculator_B2C_Rate_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rate_CAlculator_B2C_Rate_List extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyPojo myPojo;
    View viewfab, view;
    int gertdata;
    String getValue;
    String displayslectedtestRateCal;

    String testToPassToAPI;
    TextView test_rate, profile_rate, pop_rate;
    EditText edit_rate;
    RequestQueue requestQueuetest, requestQueueprofile, requestQueuepop;
    private TestModel testModel;
    Button reset;
    RecyclerView outlab_list;
    int getFinalAmountTopass = 0;
    String B2BRate, B2CRate, CollCharge, toCompany, LogCost, resID, responseData,
            yours;
    TextView companycost_test, out_lab_cost, out_lab_cost_b2b;

    ProgressDialog barProgressDialog;
    int i;
    ListView testsforrate_calculator;
    public static ArrayList<String> testListnames;
    public static ArrayList<String> profileListnames;
    public static ArrayList<String> popListnames;
    View viewrate_calfrag, feedback_fragment;
    ArrayList<Base_Model_Rate_Calculator> selectedTests;
    private static ManagingTabsActivity mContext;
    GetMainModel mainModelRate;
    TextView show_selected_tests_list_test_ils1, show_rates, show_b2b_rates, b2b_rate_adm, b2bratell;
    SearchView sv_testsList12;

    Button calculate_button;
    RequestQueue requestQueuepoptestILS;
    GetMainModel mainModel;
    ProgressDialog progressDialog;
    LinearLayout before_discount_layout2;
    ArrayList<Base_Model_Rate_Calculator> param;

    ExpandableListView exp_list;
    ArrayList<RateCalB2B_MASTERS_Main_Model> b2bmasterarraylistRate;
    ArrayList<RateCalB2B_MASTERS_Main_Model> finalproductlist;
    ExapandableAdpterForB2CRate_Calculator expAdapter;
    ArrayList<Base_Model_Rate_Calculator> selectedTestsListRateCal;
    Button resetbutton;
    List<String> showTestNmaesRateCal;
    List<String> getOnlyTestCode;
    List<String> passToApi;
    Test_Rate_Fragment test_rate_fragment;
    public static com.android.volley.RequestQueue POstQue;
    SharedPreferences prefs;
    String user, passwrd, access, api_key;
    ArrayList<Integer> getvalue;
    int getFinaldata = 0;
    Button go_button;
    // TODO: Rename and change types of parameters


    private Rate_CAlculator_B2C_Rate_List.OnFragmentInteractionListener mListener;
    private int finalPriceofTest;
    private int getFinalAmountTopassToTheAPI;
    int add = 0;
    private int totalcount;
    private String displayslectedtestRateCalToShow;
    Spinner brand_name_rt_cal;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OUTLAB_TESTLIST_GETALLTESTS> outlab_testlist_getalltests;
    ArrayList<Outlabdetails_OutLab> Selcted_Outlab_Test = new ArrayList<>();
    ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs;
    ArrayList<Outlabdetails_OutLab> filteredFiles;
    private ArrayList<String> getBrandName;
    private boolean isLoaded = false;
    private EditText outlabtestsearch;
    private TextView show_selected_tests_list_test_ils;
    private LinearLayout lineargetselectedtestforILS;
    List<String> showTestNmaes = new ArrayList<>();
    private LinearLayout search_option;
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
    private int totalcountForOutlab;
    private int totalcountForOutlabb2b;
    private String getSpinnerSelectedItem;
    private String TAG = Rate_CAlculator_B2C_Rate_List.class.getSimpleName().toString();


    public Rate_CAlculator_B2C_Rate_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rate_CAlculator_B2C_Rate_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Rate_CAlculator_B2C_Rate_List newInstance(String param1, String param2) {
        Rate_CAlculator_B2C_Rate_List fragment = new Rate_CAlculator_B2C_Rate_List();
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
    //ExapandableAdpterForB2CRate_Calculator

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (ManagingTabsActivity) getActivity();

        //  View view3 = getActivity().findViewById(R.id.show_date);
        // view3.setVisibility(View.GONE);

        viewrate_calfrag = inflater.inflate(R.layout.fragment_rate__calculator__b2_c__rate__list, container, false);

        getvalue = new ArrayList<>();

        go_button = (Button) viewrate_calfrag.findViewById(R.id.go_button);

        companycost_test = (TextView) viewrate_calfrag.findViewById(R.id.companycost_test);
        show_selected_tests_list_test_ils1 = (TextView) viewrate_calfrag.findViewById(R.id.show_selected_tests_list_test_ils1);
        show_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_rates);
        b2b_rate_adm = (TextView) viewrate_calfrag.findViewById(R.id.b2b_rate_adm);
        b2bratell = (TextView) viewrate_calfrag.findViewById(R.id.b2bratell);
        show_b2b_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_b2b_rates);
        sv_testsList12 = (SearchView) viewrate_calfrag.findViewById(R.id.sv_testsList12);
        exp_list = (ExpandableListView) viewrate_calfrag.findViewById(R.id.exp_list);


        before_discount_layout2 = (LinearLayout) viewrate_calfrag.findViewById(R.id.before_discount_layout2);
        brand_name_rt_cal = (Spinner) viewrate_calfrag.findViewById(R.id.brand_name_rt_cal);

        out_lab_cost = (TextView) viewrate_calfrag.findViewById(R.id.out_lab_cost);
        out_lab_cost_b2b = (TextView) viewrate_calfrag.findViewById(R.id.out_lab_cost_b2b);

        outlab_list = (RecyclerView) viewrate_calfrag.findViewById(R.id.outlab_list);
        search_option = (LinearLayout) viewrate_calfrag.findViewById(R.id.search_option);
        linearLayoutManager = new LinearLayoutManager(getContext());
        outlab_list.setLayoutManager(linearLayoutManager);
        outlab_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        outlab_list.setItemAnimator(new DefaultItemAnimator());

        outlabtestsearch = (EditText) viewrate_calfrag.findViewById(R.id.outlabtestsearch);
        show_selected_tests_list_test_ils = (TextView) viewrate_calfrag.findViewById(R.id.show_selected_tests_list_test_ils);
        lineargetselectedtestforILS = (LinearLayout) viewrate_calfrag.findViewById(R.id.lineargetselectedtestforILS);

        before_discount_layout2 = (LinearLayout) viewrate_calfrag.findViewById(R.id.before_discount_layout2);
        before_discount_layout2.setVisibility(View.GONE);

        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        sv_testsList12.setSearchableInfo(searchManager.getSearchableInfo(mContext.getComponentName()));
        sv_testsList12.setIconifiedByDefault(false);


        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("saveAlldata", "");
        myPojo = gson.fromJson(json, MyPojo.class);
        if (myPojo != null) {
            getBrandName = new ArrayList<>();

            if (myPojo != null) {
                if (myPojo.getMASTERS().getBRAND_LIST() != null) {
                    for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                        getBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                    }
                    brand_name_rt_cal.setAdapter(new ArrayAdapter<String>(getActivity(),
                            R.layout.spinnerproperty,
                            getBrandName));
                }

            }
        } else {
            requestJsonObject();
        }


        brand_name_rt_cal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                getRatesofB2bandB2C(getSpinnerSelectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        outlabtestsearch.setFilters(new InputFilter[]{EMOJI_FILTER});

        outlabtestsearch.addTextChangedListener(new TextWatcher() {
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
                filteredFiles = new ArrayList<>();
                String name = "";
                String code = "";
                String product = "";

                if (outlabdetails_outLabs != null) {
                    if (outlabdetails_outLabs.size() != 0) {
                        for (int i = 0; i < outlabdetails_outLabs.size(); i++) {
                            final String text = outlabdetails_outLabs.get(i).getName().toLowerCase();
                            if (outlabdetails_outLabs.get(i).getName() != null || !outlabdetails_outLabs.get(i).getName().equals("")) {
                                name = outlabdetails_outLabs.get(i).getName().toLowerCase();
                            }
                            if (outlabdetails_outLabs.get(i).getCode() != null || !outlabdetails_outLabs.get(i).getCode().equals("")) {
                                code = outlabdetails_outLabs.get(i).getCode().toLowerCase();
                            }
                            if (outlabdetails_outLabs.get(i).getProduct() != null || !outlabdetails_outLabs.get(i).getProduct().equals("")) {
                                product = outlabdetails_outLabs.get(i).getProduct().toLowerCase();
                            }
                            if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                    (code != null && code.contains(s1)) ||
                                    (product != null && product.contains(s1))) {
                                String testname = outlabdetails_outLabs.get(i).getName();
                                filteredFiles.add(outlabdetails_outLabs.get(i));
                            } else {

                            }
                            outlab_list.setVisibility(View.VISIBLE);
                            OutLabAdapter outLabRecyclerView = new OutLabAdapter(getActivity(), filteredFiles);
                            outlab_list.setAdapter(outLabRecyclerView);
                        }
                    }
                } else {
                    barProgressDialog = new ProgressDialog(mContext);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    RequestQueue requestQueuepoptestILS = Volley.newRequestQueue(mContext);
                    JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse: " + response);

                            String getResponse = response.optString("RESPONSE", "");
                            if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                                redirectToLogin(mContext);
                            } else {
                                Gson gson = new Gson();
                                mainModel = new GetMainModel();
                                mainModelRate = gson.fromJson(response.toString(), GetMainModel.class);
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }

                                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                                Gson gson22 = new Gson();
                                String json23 = gson22.toJson(mainModelRate);
                                callAdapter(mainModelRate);

                                prefsEditor1.putString("MyObject", json23);
                                prefsEditor1.commit();
                            }
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
                    requestQueuepoptestILS.add(jsonObjectRequestPop);
                    Log.e(TAG, "afterTextChanged: url" + jsonObjectRequestPop);
                }

            }
        });

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expAdapter != null) {
                    expAdapter.filterData(sv_testsList12.getQuery().toString());
                    if (!sv_testsList12.getQuery().toString().isEmpty()) {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.expandGroup(i);
                    } else {
                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
                            exp_list.collapseGroup(i);
                    }
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != getActivity().getCurrentFocus())
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getApplicationWindowToken(), 0);
            }
        });

//         getAlltTestData();

        sv_testsList12.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        sv_testsList12.setIconifiedByDefault(false);

        sv_testsList12.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (expAdapter != null) {
//                    expAdapter.filterData(query);
//                    if (!query.isEmpty()) {
//                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
//                            exp_list.expandGroup(i);
//                    } else {
//                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
//                            exp_list.collapseGroup(i);
//                    }
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String query) {
                if (expAdapter != null) {
//                    expAdapter.filterData(query);
//                    if (!query.isEmpty()) {
//                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
//                            exp_list.expandGroup(i);
//                    } else {
//                        for (int i = 0; i < expAdapter.getGroupCount(); i++)
//                            exp_list.collapseGroup(i);
//                    }
                }
                return false;
            }
        });


        // Inflate the layout for this fragment
        return viewrate_calfrag;
    }

    private void getRatesofB2bandB2C(String getSpinnerSelectedItem) {

        SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        Gson gsondtaa = new Gson();
        String jsondata = appSharedPrefsdata.getString("MyObject", "");
        GetMainModel obj = gsondtaa.fromJson(jsondata, GetMainModel.class);

        if (getSpinnerSelectedItem != null && !getSpinnerSelectedItem.equals("")) {

            if (getSpinnerSelectedItem.equals("TTL")) {


                if (obj != null) {
                    if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                        callAdapter(obj);
                        outlabtestsearch.setVisibility(View.GONE);
                        lineargetselectedtestforILS.setVisibility(View.GONE);
                        outlab_list.setVisibility(View.GONE);
                        search_option.setVisibility(View.VISIBLE);
                        exp_list.setVisibility(View.VISIBLE);

                    } else {
                        //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();

                    }
                } else {
                    barProgressDialog = new ProgressDialog(mContext);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    RequestQueue requestQueuepoptestILS = Volley.newRequestQueue(mContext);
                    JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String getResponse = response.optString("RESPONSE", "");
                            if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                                redirectToLogin(mContext);
                            } else {
                                Log.e(TAG, "onResponse: " + response);
                                Gson gson = new Gson();
                                mainModel = new GetMainModel();
                                mainModelRate = gson.fromJson(response.toString(), GetMainModel.class);
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }

                                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                                Gson gson22 = new Gson();
                                String json23 = gson22.toJson(mainModelRate);
                                callAdapter(mainModelRate);

                                prefsEditor1.putString("MyObject", json23);
                                prefsEditor1.commit();
                            }
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
                    requestQueuepoptestILS.add(jsonObjectRequestPop);
                    Log.e(TAG, "getRatesofB2bandB2C: url" + jsonObjectRequestPop);
                }


            } else {
                outlab_list.setVisibility(View.VISIBLE);
                outlabtestsearch.setVisibility(View.VISIBLE);

                before_discount_layout2.setVisibility(View.GONE);
                search_option.setVisibility(View.GONE);
                exp_list.setVisibility(View.GONE);

                if (obj != null) {
                    if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                        outlab_testlist_getalltests = obj.getB2B_MASTERS().getOUTLAB_TESTLIST();
                        outlabdetails_outLabs = new ArrayList<>();
                        for (int i = 0; i < outlab_testlist_getalltests.size(); i++) {
                            if (getSpinnerSelectedItem.equalsIgnoreCase(outlab_testlist_getalltests.get(i).getLOCATION())) {
                                if (outlabdetails_outLabs != null) {
                                    outlabdetails_outLabs = null;
                                    outlabdetails_outLabs = new ArrayList<>();
                                    outlabdetails_outLabs = outlab_testlist_getalltests.get(i).getOutlabdetails();
                                }
                                callAdapterforOutlab();
                            }
                        }
                    } else {
//                        Toast.makeText(getActivity(), ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }


    }

    private void requestJsonObject() {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.getData + "" + api_key + "/" + "" + user +
                    "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        Log.e(TAG, "onResponse: " + response);
                        Gson gson = new Gson();
                        myPojo = gson.fromJson(response.toString(), MyPojo.class);

                        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();

                        Gson gson22 = new Gson();
                        String json22 = gson22.toJson(myPojo);
                        prefsEditor1.putString("saveAlldata", json22);
                        prefsEditor1.commit();

                        isLoaded = true;
                        getBrandName = new ArrayList<>();
                        /*spinnerBrandName.add("Select Brand Name");*/
                        if (myPojo.getMASTERS().getBRAND_LIST() != null) {
                            for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                                getBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                            }
                            brand_name_rt_cal.setAdapter(new ArrayAdapter<String>(getActivity(),
                                    R.layout.spinnerproperty,
                                    getBrandName));
                        }

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                    }
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

            jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest2);
            Log.e(TAG, "requestJsonObject: url" + jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callAdapterforOutlab() {
        outlab_list.setVisibility(View.VISIBLE);
        OutLabAdapter outLabAdapter = new OutLabAdapter(getActivity(), outlabdetails_outLabs);
        outlab_list.setAdapter(outLabAdapter);
    }


    private class OutLabAdapter extends RecyclerView.Adapter<OutLabAdapter.ViewHolder> {
        boolean flag = true;
        Context context;

        ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs;

        public OutLabAdapter(Activity context, ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs) {
            this.context = context;
            this.outlabdetails_outLabs = outlabdetails_outLabs;
        }

        @NonNull
        @Override
        public OutLabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.rate_calculator_outlab_layout, parent, false);
            OutLabAdapter.ViewHolder vh = new OutLabAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final OutLabAdapter.ViewHolder holder, final int position) {
            holder.outlab_test.setText(outlabdetails_outLabs.get(position).getName());

            holder.outlab_test_rates.setText("₹ " + outlabdetails_outLabs.get(position).getRate().getB2c() + "/-");
            holder.checked.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);

            if (showTestNmaes != null) {

                for (int j = 0; j < showTestNmaes.size(); j++) {
                    if (showTestNmaes.get(j).equalsIgnoreCase(outlabdetails_outLabs.get(position).getName())) {
                        holder.checked.setVisibility(View.VISIBLE);
                        holder.check.setVisibility(View.GONE);

                    }
                }
            }


            holder.check.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                    lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                    holder.checked.setVisibility(View.VISIBLE);
                    holder.check.setVisibility(View.GONE);
                    showTestNmaes.add(outlabdetails_outLabs.get(position).getName().toString());
                    lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                    show_selected_tests_list_test_ils.setVisibility(View.VISIBLE);
                    out_lab_cost.setVisibility(View.VISIBLE);


                    if (access.equalsIgnoreCase("ADMIN")) {

                        out_lab_cost_b2b.setVisibility(View.VISIBLE);
                        b2bratell.setVisibility(View.VISIBLE);
                    } else {
                        out_lab_cost_b2b.setVisibility(View.GONE);
                        b2bratell.setVisibility(View.GONE);
                    }
                    show_selected_tests_list_test_ils.setText("");
                    out_lab_cost.setText("");
                    out_lab_cost_b2b.setText("");

                    totalcountForOutlab = 0;
                    totalcountForOutlabb2b = 0;
                    if (Selcted_Outlab_Test != null) {
                        for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                            totalcountForOutlab = totalcountForOutlab + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2c());
                            totalcountForOutlabb2b = totalcountForOutlabb2b + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2b());
                        }
                    }
                    System.out.println("Total value  :" + totalcountForOutlab);
                    System.out.println("Total value  :" + totalcountForOutlabb2b);

                    String displayslectedtest = TextUtils.join(", ", showTestNmaes);
                    show_selected_tests_list_test_ils.setText(displayslectedtest);
                    out_lab_cost.setText(String.valueOf(totalcountForOutlab));
                    out_lab_cost_b2b.setText(String.valueOf(totalcountForOutlabb2b));
                }
            });

            holder.checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Selcted_Outlab_Test.remove(outlabdetails_outLabs.get(position));
                    lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                    showTestNmaes.remove(outlabdetails_outLabs.get(position).getName().toString());

                    holder.check.setVisibility(View.VISIBLE);
                    holder.checked.setVisibility(View.GONE);
                    lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                    show_selected_tests_list_test_ils.setText("");
                    out_lab_cost.setText("");
                    out_lab_cost_b2b.setText("");
                    String displayslectedtest = TextUtils.join(",", showTestNmaes);


                    totalcountForOutlab = 0;
                    totalcountForOutlabb2b = 0;

                    if (!displayslectedtest.equals("")) {
                        for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                            totalcountForOutlab = totalcountForOutlab + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2c());
                            totalcountForOutlabb2b = totalcountForOutlabb2b + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2b());
                        }
                        show_selected_tests_list_test_ils.setText(displayslectedtest);
                        out_lab_cost.setText(String.valueOf(totalcountForOutlab));
                        out_lab_cost_b2b.setText(String.valueOf(totalcountForOutlabb2b));

                    } else {
                        if (displayslectedtest.equals("")) {
                            lineargetselectedtestforILS.setVisibility(View.GONE);
                            show_selected_tests_list_test_ils.setText("");
                            out_lab_cost.setText("");
                            out_lab_cost_b2b.setText("");
                            show_selected_tests_list_test_ils.setVisibility(View.GONE);
                            out_lab_cost.setVisibility(View.GONE);
                            out_lab_cost_b2b.setVisibility(View.GONE);
                        }
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return outlabdetails_outLabs.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView outlab_test, outlab_test_rates;
            // CheckBox check;
            boolean isSelectedDueToParent;
            String parentTestCode, parentTestname;
            ImageView check, checked;

            public ViewHolder(View itemView) {
                super(itemView);
                outlab_test = (TextView) itemView.findViewById(R.id.outlab_test);
                outlab_test_rates = (TextView) itemView.findViewById(R.id.outlab_test_rates);
                check = (ImageView) itemView.findViewById(R.id.check);
                checked = (ImageView) itemView.findViewById(R.id.checked);
            }
        }


    }


    private void fetchData() {
        barProgressDialog = new ProgressDialog(getActivity());
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        POstQue = Volley.newRequestQueue(mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("apikey", api_key);
            jsonObject.put("tests", testToPassToAPI);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.RateCal, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e(TAG, "onResponse: " + response);
                    String finalJson = response.toString();
                    JSONObject parentObjectOtp = new JSONObject(finalJson);

                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                    B2BRate = parentObjectOtp.getString("B2B");
                    B2CRate = parentObjectOtp.getString("B2C");
                    CollCharge = parentObjectOtp.getString("CHC");
                    toCompany = parentObjectOtp.getString("COMPANY");
                    LogCost = parentObjectOtp.getString("PI");
                    resID = parentObjectOtp.getString("RESID");
                    responseData = parentObjectOtp.getString("RESPONSE");
                    yours = parentObjectOtp.getString("YOURS");

                    if (resID.equals("RES0000")) {

                        //pass data to next screen

                        test_rate_fragment = new Test_Rate_Fragment();
//                        TastyToast.makeText(mContext, "" + responseData, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        Bundle args = new Bundle();
                        args.putString("B2B", B2BRate);
                        args.putString("B2C", B2CRate);
                        args.putString("CHC", CollCharge);
                        args.putString("COMPANY", toCompany);
                        args.putString("PI", LogCost);
                        args.putString("RESID", resID);
                        args.putString("RESPONSE", responseData);
                        args.putString("YOURS", yours);
                        args.putParcelableArrayList("send", selectedTestsListRateCal);

                        before_discount_layout2.setVisibility(View.VISIBLE);
                        show_selected_tests_list_test_ils1.setText(displayslectedtestRateCalToShow);
                        show_rates.setText(B2CRate);

                        if (access.equals("ADMIN")) {
                            show_b2b_rates.setText(String.valueOf(totalcount));
                            b2b_rate_adm.setVisibility(View.VISIBLE);
                            show_b2b_rates.setVisibility(View.VISIBLE);
                        } else {
                            b2b_rate_adm.setVisibility(View.GONE);
                            show_b2b_rates.setVisibility(View.GONE);
                        }


                       /* Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("send", selectedTestsListRateCal);*/
//                        test_rate_fragment.setArguments(args);

                    } else {

                        TastyToast.makeText(mContext, "" + responseData, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                } catch (JSONException e) {
                    TastyToast.makeText(mContext, "" + responseData, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                } else {

                    System.out.println(error);
                }
            }
        });
        POstQue.add(jsonObjectRequest1);
        Log.e(TAG, "fetchData: " + jsonObjectRequest1);
        Log.e(TAG, "fetchData: " + jsonObject);
    }


    private void callAdapter(GetMainModel mainModel) {

        b2bmasterarraylistRate = new ArrayList<>();
        b2bmasterarraylistRate.add(mainModel.B2B_MASTERS);

        ArrayList<Product_Rate_CalculatorModel> finalproductlist = new ArrayList<Product_Rate_CalculatorModel>();
        ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<Base_Model_Rate_Calculator>();

        String testtype = "";
        for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {

            Product_Rate_CalculatorModel product_rate_masterModel = new Product_Rate_CalculatorModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_POP);
            product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylistRate.get(i).getPOP());
            finalproductlist.add(product_rate_masterModel);

            product_rate_masterModel = new Product_Rate_CalculatorModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_PROFILE);
            product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylistRate.get(i).getPROFILE());
            finalproductlist.add(product_rate_masterModel);

            product_rate_masterModel = new Product_Rate_CalculatorModel();
            product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
            product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylistRate.get(i).getTESTS());
            finalproductlist.add(product_rate_masterModel);
        }

        expAdapter = new ExapandableAdpterForB2CRate_Calculator(mContext, finalproductlist, selectedTestsListRateCal, new InterfaceRateCAlculator() {
            @Override
            public void onCheckChangeRateCalculator(ArrayList<Base_Model_Rate_Calculator> selectedTests) {
                System.out.println("check changed");
                selectedTestsListRateCal = selectedTests;
                expAdapter.notifyDataSetChanged();
                if (selectedTestsListRateCal.size() != 0) {

                    showTestNmaesRateCal = new ArrayList<>();
                    getOnlyTestCode = new ArrayList<>();
                    ArrayList<Integer> Totalcunt = new ArrayList<>();

                    for (int i = 0; i < selectedTestsListRateCal.size(); i++) {
                        showTestNmaesRateCal.add(selectedTestsListRateCal.get(i).getName().toString());
                        if (selectedTestsListRateCal.get(i).getBarcodes().length != 0)
                            for (int j = 0; j < selectedTestsListRateCal.get(i).getBarcodes().length; j++) {
                                getOnlyTestCode.add(selectedTestsListRateCal.get(i).getBarcodes()[j].getCode());
                            }
                        else {

                        }
//                        int getB2BrateToCalculate = Integer.parseInt(selectedTestsListRateCal.get(i).getRate().getB2b());
//                        int getdata = getB2BrateToCalculate*10/100;
//                        int totalValue = getdata+ getB2BrateToCalculate;
                    }
                    totalcount = 0;
                    for (int j = 0; j < selectedTestsListRateCal.size(); j++) {
                        totalcount = totalcount + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                    }
                    System.out.println("Total value  :" + totalcount);
                    displayslectedtestRateCal = TextUtils.join(",", getOnlyTestCode);
                    displayslectedtestRateCalToShow = TextUtils.join(", ", showTestNmaesRateCal);
                    testToPassToAPI = TextUtils.join(",", getOnlyTestCode);
                    fetchData();
                } else if (selectedTestsListRateCal.size() == 0) {
                    show_selected_tests_list_test_ils1.setText("");
                    show_rates.setText("0");
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        });

        exp_list.setAdapter(expAdapter);
    }

    private void showTable() {

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
