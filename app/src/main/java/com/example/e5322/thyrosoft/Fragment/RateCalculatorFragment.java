package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.e5322.thyrosoft.Activity.SampleTypeColor;
import com.example.e5322.thyrosoft.Adapter.ExapandableAdpterForB2CRate_Calculator;
import com.example.e5322.thyrosoft.Adapter.RateCAlAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.InterfaceRateCAlculator;
import com.example.e5322.thyrosoft.MainModelForAllTests.OUTLAB_TESTLIST_GETALLTESTS;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.Models.RequestModels.CalculateRateRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.CalculateRateResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculator.Test_Rate_Fragment;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;


public class RateCalculatorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<String> testListnames;
    public static ArrayList<String> profileListnames;
    public static ArrayList<String> popListnames;
    public static RequestQueue POstQue;
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
    private static ManagingTabsActivity mContext;
    View viewfab, view;
    int gertdata;
    String getValue;
    String displayslectedtestRateCal;
    ArrayList<Base_Model_Rate_Calculator> getAllTests;
    String testToPassToAPI;
    TextView test_rate, profile_rate, pop_rate;
    EditText edit_rate;
    ArrayList<Base_Model_Rate_Calculator> finalproductlist;
    RateCAlAdapter rateCAlAdapter;
    RequestQueue requestQueuetest, requestQueueprofile, requestQueuepop;
    Button reset;
    RecyclerView outlab_list;
    int getFinalAmountTopass = 0;
    TextView companycost_test, out_lab_cost, out_lab_cost_b2b;
    int days = 0;
    ProgressDialog barProgressDialog;
    ListView testsforrate_calculator;
    View viewrate_calfrag, feedback_fragment;
    ArrayList<Base_Model_Rate_Calculator> selectedTests;
    GetMainModel mainModelRate;
    TextView show_selected_tests_list_test_ils1, show_rates, show_b2b_rates, b2b_rate_adm, b2bratell, poptab, profile_txt, test_txt;
    EditText sv_testsList_ttl;
    Button calculate_button;
    LinearLayout search_option_ttl;
    RequestQueue requestQueuepoptestILS;
    GetMainModel mainModel;
    ProgressDialog progressDialog;
    LinearLayout before_discount_layout2;
    ArrayList<Base_Model_Rate_Calculator> param;
    ArrayList<RateCalB2B_MASTERS_Main_Model> b2bmasterarraylistRate;
    ExapandableAdpterForB2CRate_Calculator expAdapter;
    ArrayList<Base_Model_Rate_Calculator> selectedTestsListRateCal = new ArrayList<>();
    Button resetbutton;
    List<String> showTestNmaesRateCal;
    List<String> getOnlyTestCode;
    List<String> getOnlyTestCode1;
    List<String> passToApi;
    Test_Rate_Fragment test_rate_fragment;
    SharedPreferences prefs;
    String user, passwrd, access, api_key;
    ArrayList<Integer> getvalue;
    int getFinaldata = 0;
    Button go_button;
    // TODO: Rename and change types of parameters
    RecyclerView containerlist;
    int add = 0;
    Spinner brand_name_rt_cal;
    LinearLayoutManager linearLayoutManager;
    LinearLayout pop_profile_test_bar;
    ArrayList<OUTLAB_TESTLIST_GETALLTESTS> outlab_testlist_getalltests;
    ArrayList<Outlabdetails_OutLab> Selcted_Outlab_Test = new ArrayList<>();
    ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs;
    ArrayList<Outlabdetails_OutLab> filteredFiles;
    LinearLayout linear_layout_data;
    LinearLayout offline_img;
    ArrayList<Base_Model_Rate_Calculator> filteredFilesttl;
    List<String> showTestNmaes = new ArrayList<>();
    LinearLayout lin_color, lin_color2;
    GetMainModel obj;
    Product_Rate_CalculatorModel product_rate_masterModel;
    boolean viewmoreflag = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private MyPojo myPojo;
    private TestModel testModel;
    private int finalPriceofTest;
    private int getFinalAmountTopassToTheAPI;
    private int totalcount;
    private String displayslectedtestRateCalToShow;
    private ArrayList<String> getBrandName;
    private boolean isLoaded = false;
    private EditText outlabtestsearch;
    private TextView show_selected_tests_list_test_ils;
    private LinearLayout lineargetselectedtestforILS;
    private int totalcountForOutlab;
    private int totalcountForOutlabb2b;
    private String getSpinnerSelectedItem;
    private ArrayList<Base_Model_Rate_Calculator> getProfileList;
    private ArrayList<Base_Model_Rate_Calculator> gettestsList;
    private ArrayList<Base_Model_Rate_Calculator> getPopList;
    private ArrayList<Base_Model_Rate_Calculator> selectedlist;
    private ArrayList<Base_Model_Rate_Calculator> totalproductlist;
    private String TAG = getClass().getSimpleName();
    private TextView txt_more;
    // ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<>();
    // ArrayList<Base_Model_Rate_Calculator> finalproduct_list = new ArrayList<>();

    public RateCalculatorFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RateCalculatorFragment newInstance(String param1, String param2) {
        RateCalculatorFragment fragment = new RateCalculatorFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = (ManagingTabsActivity) getActivity();
        viewrate_calfrag = inflater.inflate(R.layout.fragment_rate_calculator, container, false);
        getvalue = new ArrayList<>();


        go_button = (Button) viewrate_calfrag.findViewById(R.id.go_button);
        companycost_test = (TextView) viewrate_calfrag.findViewById(R.id.companycost_test);

        show_selected_tests_list_test_ils1 = (TextView) viewrate_calfrag.findViewById(R.id.show_selected_tests_list_test_ils1);
        show_selected_tests_list_test_ils1.setMovementMethod(new ScrollingMovementMethod());


        show_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_rates);
        b2b_rate_adm = (TextView) viewrate_calfrag.findViewById(R.id.b2b_rate_adm);
        b2bratell = (TextView) viewrate_calfrag.findViewById(R.id.b2bratell);
        show_b2b_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_b2b_rates);
        poptab = (TextView) viewrate_calfrag.findViewById(R.id.poptab);
        profile_txt = (TextView) viewrate_calfrag.findViewById(R.id.profile_txt);
        sv_testsList_ttl = (EditText) viewrate_calfrag.findViewById(R.id.sv_testsList12);
        test_txt = (TextView) viewrate_calfrag.findViewById(R.id.test_txt);
        linear_layout_data = (LinearLayout) viewrate_calfrag.findViewById(R.id.linear_layout_data);
        offline_img = (LinearLayout) viewrate_calfrag.findViewById(R.id.offline_img);
        containerlist = (RecyclerView) viewrate_calfrag.findViewById(R.id.containerlist);
        pop_profile_test_bar = (LinearLayout) viewrate_calfrag.findViewById(R.id.pop_profile_test_bar);
        search_option_ttl = (LinearLayout) viewrate_calfrag.findViewById(R.id.search_option_ttl);
        linearLayoutManager = new LinearLayoutManager(getContext());
        containerlist.setLayoutManager(linearLayoutManager);
        containerlist.setItemAnimator(new DefaultItemAnimator());
        containerlist.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        before_discount_layout2 = (LinearLayout) viewrate_calfrag.findViewById(R.id.before_discount_layout2);

        lin_color = viewrate_calfrag.findViewById(R.id.lin_color);
        lin_color2 = viewrate_calfrag.findViewById(R.id.lin_color2);

        txt_more = viewrate_calfrag.findViewById(R.id.txt_more);
        txt_more.setText("More..");

        txt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewmoreflag == false) {
                    lin_color2.setVisibility(View.VISIBLE);
                    txt_more.setText("Less");
                    viewmoreflag = true;
                } else {
                    lin_color2.setVisibility(View.GONE);
                    txt_more.setText("More");
                    viewmoreflag = false;
                }
            }
        });

        brand_name_rt_cal = (Spinner) viewrate_calfrag.findViewById(R.id.brand_name_rt_cal);
        out_lab_cost = (TextView) viewrate_calfrag.findViewById(R.id.out_lab_cost);
        out_lab_cost_b2b = (TextView) viewrate_calfrag.findViewById(R.id.out_lab_cost_b2b);
        outlab_list = (RecyclerView) viewrate_calfrag.findViewById(R.id.outlab_list);

        linearLayoutManager = new LinearLayoutManager(getContext());
        outlab_list.setLayoutManager(linearLayoutManager);
        outlab_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        outlab_list.setItemAnimator(new DefaultItemAnimator());


        outlabtestsearch = (EditText) viewrate_calfrag.findViewById(R.id.outlabtestsearch);
        show_selected_tests_list_test_ils = (TextView) viewrate_calfrag.findViewById(R.id.show_selected_tests_list_test_ils);
        show_selected_tests_list_test_ils.setMovementMethod(new ScrollingMovementMethod());

        lineargetselectedtestforILS = (LinearLayout) viewrate_calfrag.findViewById(R.id.lineargetselectedtestforILS);
        lineargetselectedtestforILS.setVisibility(View.GONE);

        before_discount_layout2 = (LinearLayout) viewrate_calfrag.findViewById(R.id.before_discount_layout2);
        before_discount_layout2.setVisibility(View.GONE);


        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        Log.e(TAG, "onCreateView TYPE : " + access);

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            linear_layout_data.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            linear_layout_data.setVisibility(View.VISIBLE);
        }
        //1st
        poptab.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
        poptab.setTextColor(getResources().getColor(R.color.colorWhite));
        profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));
        test_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        test_txt.setTextColor(getResources().getColor(R.color.colorBlack));

        days = GlobalClass.getStoreSynctime(getActivity());
        if (days >= Constants.DAYS_CNT) {
            getAllproduct();
        } else {
            getDataFromSharedPref();
        }


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


        List<String> colorview = Arrays.asList(Constants.EDTA, Constants.SERUM, Constants.URINE, Constants.FLUORIDE);
        List<String> colorview2 = Arrays.asList(Constants.LITHIUMHEPARIN, Constants.SODIUMHEPARIN, "OTHERS");

        for (String s : colorview) {
            if (s.equalsIgnoreCase(Constants.EDTA)) {
                dynamicolordot(getActivity(), lin_color, mContext.getResources().getColor(R.color.edta), Constants.EDTA);
            } else if (s.equalsIgnoreCase(Constants.SERUM)) {
                dynamicolordot(getActivity(), lin_color, mContext.getResources().getColor(R.color.serum), Constants.SERUM);
            } else if (s.equalsIgnoreCase(Constants.URINE)) {
                dynamicolordot(getActivity(), lin_color, mContext.getResources().getColor(R.color.urine), Constants.URINE);
            } else if (s.equalsIgnoreCase(Constants.FLUORIDE)) {
                dynamicolordot(getActivity(), lin_color, mContext.getResources().getColor(R.color.flouride), Constants.FLUORIDE);
            }
        }

        for (String s : colorview2) {
            if (s.equalsIgnoreCase(Constants.LITHIUMHEPARIN)) {
                dynamicolordot(getActivity(), lin_color2, mContext.getResources().getColor(R.color.lithium), Constants.LITHIUMHEPARIN);
            } else if (s.equalsIgnoreCase(Constants.SODIUMHEPARIN)) {
                dynamicolordot(getActivity(), lin_color2, mContext.getResources().getColor(R.color.sodium), Constants.SODIUMHEPARIN);
            } else {
                dynamicolordot(mContext, lin_color2, mContext.getResources().getColor(R.color.other), "OTHERS");
            }
        }

        brand_name_rt_cal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                sv_testsList_ttl.getText().clear();
                before_discount_layout2.setVisibility(View.GONE);
                getRatesofB2bandB2C(getSpinnerSelectedItem, mContext);
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
                            }

                            outlab_list.setVisibility(View.VISIBLE);
                            containerlist.setVisibility(View.GONE);
                            pop_profile_test_bar.setVisibility(View.GONE);
                            search_option_ttl.setVisibility(View.GONE);
                            OutLabAdapter outLabRecyclerView = new OutLabAdapter(getActivity(), filteredFiles);
                            outlab_list.setAdapter(outLabRecyclerView);

                        }
                    }
                } else {
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        offline_img.setVisibility(View.VISIBLE);
                        linear_layout_data.setVisibility(View.GONE);
                    } else {
                        offline_img.setVisibility(View.GONE);
                        linear_layout_data.setVisibility(View.VISIBLE);
                        getAllproduct();
                    }

                }

            }
        });

        poptab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poptab.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
                poptab.setTextColor(getResources().getColor(R.color.colorWhite));
                profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                test_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                test_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                finalproductlist = new ArrayList<>();

                ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<>();
                if (obj != null) {
                    if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                        b2bmasterarraylistRate = new ArrayList<>();

                        if (obj.B2B_MASTERS != null) {
                            b2bmasterarraylistRate.add(obj.B2B_MASTERS);
                        }

                        if (b2bmasterarraylistRate != null) {
                            for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                                for (int j = 0; j < b2bmasterarraylistRate.get(i).getPOP().size(); j++) {
                                    finalproductlist.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                                }
                            }
                        }

                        callAdapaterTosetData(finalproductlist, totalproductlist);

                    }
                } else {
                    for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getPOP().size(); j++) {
                            finalproductlist.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                            b2bmasterarraylistRate.get(i).getPOP().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getPOP().get(j).setIs_lock("no");
                            testRateMasterModels.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                        }
                    }

                    callAdapaterTosetData(finalproductlist, testRateMasterModels);
                }
            }
        });


        profile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //2nd
                profile_txt.setBackground(getResources().getDrawable(R.drawable.maroon_rect_box));
                profile_txt.setTextColor(getResources().getColor(R.color.colorWhite));
                poptab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                poptab.setTextColor(getResources().getColor(R.color.colorBlack));
                test_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                test_txt.setTextColor(getResources().getColor(R.color.colorBlack));

                finalproductlist = new ArrayList<Base_Model_Rate_Calculator>();
                ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<>();
                if (obj != null) {
                    if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                        b2bmasterarraylistRate = new ArrayList<>();
                        b2bmasterarraylistRate.add(obj.B2B_MASTERS);
                        for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                            for (int j = 0; j < b2bmasterarraylistRate.get(i).getPROFILE().size(); j++) {
                                finalproductlist.add(b2bmasterarraylistRate.get(i).getPROFILE().get(j));
                            }
                        }

                        callAdapaterTosetData(finalproductlist, totalproductlist);
                    } else {
                        //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getPROFILE().size(); j++) {
                            finalproductlist.add(b2bmasterarraylistRate.get(i).getPROFILE().get(j));
                            b2bmasterarraylistRate.get(i).getPROFILE().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getPROFILE().get(j).setIs_lock("no");
                            testRateMasterModels.add(b2bmasterarraylistRate.get(i).getPROFILE().get(j));
                        }
                    }
                    callAdapaterTosetData(finalproductlist, testRateMasterModels);
                }


            }
        });

        test_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3rd
                test_txt.setBackground(getResources().getDrawable(R.drawable.maroon_rect_right_round));
                test_txt.setTextColor(getResources().getColor(R.color.colorWhite));
                poptab.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                poptab.setTextColor(getResources().getColor(R.color.colorBlack));
                profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));

                finalproductlist = new ArrayList<Base_Model_Rate_Calculator>();
                ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<>();
                if (obj != null) {
                    if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                        b2bmasterarraylistRate = new ArrayList<>();
                        b2bmasterarraylistRate.add(obj.B2B_MASTERS);
                        for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                            for (int j = 0; j < b2bmasterarraylistRate.get(i).getTESTS().size(); j++) {
                                finalproductlist.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
                            }
                        }
                        callAdapaterTosetData(finalproductlist, totalproductlist);
                    } else {
                        //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getTESTS().size(); j++) {
                            finalproductlist.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
                            b2bmasterarraylistRate.get(i).getTESTS().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getTESTS().get(j).setIs_lock("no");
                            testRateMasterModels.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
                        }


                    }

                    callAdapaterTosetData(finalproductlist, testRateMasterModels);
                }
            }
        });

        sv_testsList_ttl.addTextChangedListener(new TextWatcher() {
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
                filteredFilesttl = new ArrayList<Base_Model_Rate_Calculator>();
                String name = "";
                String code = "";
                String product = "";

                if (finalproductlist != null && finalproductlist.size() != 0) {
                    for (int i = 0; i < finalproductlist.size(); i++) {
                        final String text = finalproductlist.get(i).getName().toLowerCase();
                        if (finalproductlist.get(i).getName() != null || !finalproductlist.get(i).getName().equals("")) {
                            name = finalproductlist.get(i).getName().toLowerCase();
                        }
                        if (finalproductlist.get(i).getCode() != null || !finalproductlist.get(i).getCode().equals("")) {
                            code = finalproductlist.get(i).getCode().toLowerCase();
                        }
                        if (finalproductlist.get(i).getProduct() != null || !finalproductlist.get(i).getProduct().equals("")) {
                            product = finalproductlist.get(i).getProduct().toLowerCase();
                        }
                        if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                (code != null && code.contains(s1)) ||
                                (product != null && product.contains(s1))) {
                            String testname = finalproductlist.get(i).getName();
                            filteredFilesttl.add(finalproductlist.get(i));
                        } else {
                        }
                        callAdapaterTosetData(filteredFilesttl, totalproductlist);
                    }
                } else {
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        });
        return viewrate_calfrag;
    }

    private void getAllproduct() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getActivity());

        RequestQueue requestQueuepoptestILS = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse for All products :::: " + response);

                String getResponse = response.optString("RESPONSE", "");
                if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(mContext);
                } else {
                    Gson gson = new Gson();
                    mainModel = new GetMainModel();
                    mainModelRate = gson.fromJson(response.toString(), GetMainModel.class);

                    GlobalClass.hideProgress(getActivity(), progressDialog);

                    SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                    Gson gson22 = new Gson();
                    String json23 = gson22.toJson(mainModelRate);
                    prefsEditor1.putString("MyObject", json23);
                    prefsEditor1.commit();

                    getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                    getRatesofB2bandB2C(getSpinnerSelectedItem,mContext);

                    try {
                        if (obj != null && obj.getB2B_MASTERS() != null && obj.getB2B_MASTERS().getOUTLAB_TESTLIST() != null) {
                            outlab_testlist_getalltests = obj.getB2B_MASTERS().getOUTLAB_TESTLIST();
                            outlabdetails_outLabs = new ArrayList<>();
                            if (outlab_testlist_getalltests != null) {
                                for (int i = 0; i < outlab_testlist_getalltests.size(); i++) {
                                    if (getSpinnerSelectedItem.equalsIgnoreCase(outlab_testlist_getalltests.get(i).getLOCATION())) {
                                        if (outlabdetails_outLabs != null) {
                                            outlabdetails_outLabs = null;
                                            outlabdetails_outLabs = new ArrayList<>();
                                            outlabdetails_outLabs = outlab_testlist_getalltests.get(i).getOutlabdetails();
                                        }
                                    }
                                }
                            }

                        }
                    } catch (Exception e) {
                        Log.e(TAG, "ON ERROR MESSAGE -->" + e.getLocalizedMessage());
                    }


                    b2bmasterarraylistRate = new ArrayList<>();
                    b2bmasterarraylistRate.add(mainModelRate.B2B_MASTERS);


                    ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<>();
                    ArrayList<Base_Model_Rate_Calculator> finalproduct_list = new ArrayList<>();

                    try {
                        if (b2bmasterarraylistRate != null) {
                            if (getSpinnerSelectedItem.equalsIgnoreCase("TTL")) {
                                for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                                    for (int j = 0; j < b2bmasterarraylistRate.get(i).getPOP().size(); j++) {
                                        finalproduct_list.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                                        b2bmasterarraylistRate.get(i).getPOP().get(j).setIsCart("no");
                                        b2bmasterarraylistRate.get(i).getPOP().get(j).setIs_lock("no");
                                        testRateMasterModels.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                                    }
                                }
                            } else {
                                for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                                    for (int j = 0; j < b2bmasterarraylistRate.get(i).getSMT().size(); j++) {
                                        finalproduct_list.add(b2bmasterarraylistRate.get(i).getSMT().get(j));
                                        b2bmasterarraylistRate.get(i).getSMT().get(j).setIsCart("no");
                                        b2bmasterarraylistRate.get(i).getSMT().get(j).setIs_lock("no");
                                        testRateMasterModels.add(b2bmasterarraylistRate.get(i).getSMT().get(j));
                                    }
                                }
                            }

                            callAdapaterTosetData(finalproduct_list, testRateMasterModels);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    private void getDataFromSharedPref() {
        SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        Gson gsondtaa = new Gson();
        String jsondata = appSharedPrefsdata.getString("MyObject", "");
        obj = gsondtaa.fromJson(jsondata, GetMainModel.class);

        if (obj != null) {
            if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                b2bmasterarraylistRate = new ArrayList<>();
                b2bmasterarraylistRate.add(obj.B2B_MASTERS);
                getAllTests = new ArrayList<>();
                totalproductlist = new ArrayList<>();
                finalproductlist = new ArrayList<>();

                if (getSpinnerSelectedItem.equalsIgnoreCase("TTL")) {
                    for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {

                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getPOP().size(); j++) {
                            finalproductlist.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                            b2bmasterarraylistRate.get(i).getPOP().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getPOP().get(j).setIs_lock("no");
                            getAllTests.add(b2bmasterarraylistRate.get(i).getPOP().get(j));
                        }

                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getPROFILE().size(); j++) {
                            b2bmasterarraylistRate.get(i).getPROFILE().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getPROFILE().get(j).setIs_lock("no");
                            getAllTests.add(b2bmasterarraylistRate.get(i).getPROFILE().get(j));
                        }

                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getTESTS().size(); j++) {
                            b2bmasterarraylistRate.get(i).getTESTS().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getTESTS().get(j).setIs_lock("no");
                            getAllTests.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
                        }

                    }
                } else {
                    for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                        for (int j = 0; j < b2bmasterarraylistRate.get(i).getSMT().size(); j++) {
                            finalproductlist.add(b2bmasterarraylistRate.get(i).getSMT().get(j));
                            b2bmasterarraylistRate.get(i).getSMT().get(j).setIsCart("no");
                            b2bmasterarraylistRate.get(i).getSMT().get(j).setIs_lock("no");
                            getAllTests.add(b2bmasterarraylistRate.get(i).getSMT().get(j));
                        }
                    }
                }


                totalproductlist = getAllTests;
                callAdapaterTosetData(finalproductlist, totalproductlist);

            } else {
                //Toast.makeText(mContext, ToastFile.no_data_fnd, Toast.LENGTH_SHORT).show();
                getAllproduct();
            }
        } else {
            getAllproduct();
        }
    }

    private void callAdapaterTosetData(final ArrayList<Base_Model_Rate_Calculator> finalproductlist, ArrayList<Base_Model_Rate_Calculator> getAllTests) {

        rateCAlAdapter = new RateCAlAdapter(mContext, finalproductlist, totalproductlist, selectedTestsListRateCal, new InterfaceRateCAlculator() {
            @Override
            public void onCheckChangeRateCalculator(ArrayList<Base_Model_Rate_Calculator> selectedTests) {

                System.out.println("check changed");
                selectedTestsListRateCal = null;
                selectedTestsListRateCal = selectedTests;

                if (selectedTestsListRateCal.size() != 0) {
                    showTestNmaesRateCal = new ArrayList<>();
                    getOnlyTestCode = new ArrayList<>();
                    HashSet<String> hashSet = new HashSet<String>();
                    ArrayList<Integer> Totalcunt = new ArrayList<>();

                    for (int i = 0; i < selectedTestsListRateCal.size(); i++) {
                        showTestNmaesRateCal.add(selectedTestsListRateCal.get(i).getName().toString());
                        if (selectedTestsListRateCal.get(i).getBarcodes().length != 0)
                            for (int j = 0; j < selectedTestsListRateCal.get(i).getBarcodes().length; j++) {
                                getOnlyTestCode.add(selectedTestsListRateCal.get(i).getBarcodes()[j].getCode());

                            }
                    }


                    if (getOnlyTestCode != null) {
                        hashSet.addAll(getOnlyTestCode);
                        getOnlyTestCode.clear();
                        getOnlyTestCode.addAll(hashSet);
                    }


                    totalcount = 0;

                    for (int j = 0; j < selectedTestsListRateCal.size(); j++) {
                        totalcount = totalcount + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                    }

                    System.out.println("Total value  :" + totalcount);

                    displayslectedtestRateCal = null;
                    displayslectedtestRateCalToShow = TextUtils.join(", ", showTestNmaesRateCal);
                    testToPassToAPI = TextUtils.join(",", getOnlyTestCode);

                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        offline_img.setVisibility(View.VISIBLE);
                        linear_layout_data.setVisibility(View.GONE);
                    } else {
                        offline_img.setVisibility(View.GONE);
                        linear_layout_data.setVisibility(View.VISIBLE);

                        fetchData();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                before_discount_layout2.setVisibility(View.GONE);
                                before_discount_layout2.setVisibility(View.VISIBLE);
                            }
                        });
                    }


                    before_discount_layout2.invalidate();
                    before_discount_layout2.postInvalidate();

                } else if (selectedTestsListRateCal.size() == 0) {
                    show_selected_tests_list_test_ils1.setText("");
                    show_rates.setText("0");
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        });

        containerlist.setAdapter(rateCAlAdapter);

    }

    private void getRatesofB2bandB2C(String getSpinnerSelectedItem, ManagingTabsActivity mContext) {

        GetMainModel obj = null;
        try {
            SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(mContext);
            Gson gsondtaa = new Gson();
            String jsondata = appSharedPrefsdata.getString("MyObject", "");
            obj = gsondtaa.fromJson(jsondata, GetMainModel.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }


        if (getSpinnerSelectedItem != null && !getSpinnerSelectedItem.equals("")) {
            if (getSpinnerSelectedItem.equals("TTL") || getSpinnerSelectedItem.equalsIgnoreCase("SMT")) {
                if (obj != null) {
                    if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                        if (getSpinnerSelectedItem.equalsIgnoreCase("SMT")) {
                            pop_profile_test_bar.setVisibility(View.GONE);
                        } else {
                            pop_profile_test_bar.setVisibility(View.VISIBLE);
                            show_selected_tests_list_test_ils1.scrollTo(0, 0);
                            poptab.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
                            poptab.setTextColor(getResources().getColor(R.color.colorWhite));
                            profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                            test_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            test_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                        }

                        callAdapter(obj);

                        outlabtestsearch.setVisibility(View.GONE);
                        lineargetselectedtestforILS.setVisibility(View.GONE);
                        outlab_list.setVisibility(View.GONE);
                        if (selectedTestsListRateCal != null) {
                            selectedTestsListRateCal.clear();
                        }
                        getDataFromSharedPref();
                        rateCAlAdapter.notifyDataSetChanged();
                        containerlist.setVisibility(View.VISIBLE);
                        search_option_ttl.setVisibility(View.VISIBLE);

                    }
                } else {
                    if (!GlobalClass.isNetworkAvailable(getActivity())) {
                        offline_img.setVisibility(View.VISIBLE);
                        linear_layout_data.setVisibility(View.GONE);
                    } else {
                        offline_img.setVisibility(View.GONE);
                        linear_layout_data.setVisibility(View.VISIBLE);
                        getAllproduct();
                    }

                }

            } else {
                show_selected_tests_list_test_ils.scrollTo(0, 0);
                outlab_list.setVisibility(View.VISIBLE);
                outlabtestsearch.setVisibility(View.VISIBLE);
                containerlist.setVisibility(View.GONE);
                pop_profile_test_bar.setVisibility(View.GONE);
                search_option_ttl.setVisibility(View.GONE);
                before_discount_layout2.setVisibility(View.GONE);

                if (Selcted_Outlab_Test != null && Selcted_Outlab_Test.size() > 0) {
                    Selcted_Outlab_Test.clear();
                    showTestNmaes.clear();
                    lineargetselectedtestforILS.setVisibility(View.GONE);
                }

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
                            brand_name_rt_cal.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinnerproperty, getBrandName));
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
        containerlist.setVisibility(View.GONE);
        pop_profile_test_bar.setVisibility(View.GONE);
        search_option_ttl.setVisibility(View.GONE);
    }

    private void fetchData() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getActivity());
        POstQue = Volley.newRequestQueue(mContext);
        JSONObject jsonObject = null;
        try {
            CalculateRateRequestModel requestModel = new CalculateRateRequestModel();
            requestModel.setApikey(api_key);
            requestModel.setTests(testToPassToAPI);
            requestModel.setBrand(getSpinnerSelectedItem);
            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "FETCH DATA --->" + jsonObject);
        Log.e(TAG, "FETCH DATA URL--->" + Api.RateCal);

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Api.RateCal, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: " + response);
                    GlobalClass.hideProgress(getActivity(), progressDialog);
                    CalculateRateResponseModel responseModel = new Gson().fromJson(String.valueOf(response), CalculateRateResponseModel.class);

                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getRESID()) && responseModel.getRESID().equalsIgnoreCase(Constants.RES0000)) {
                            test_rate_fragment = new Test_Rate_Fragment();

                            Bundle args = new Bundle();
                            args.putString("B2B", responseModel.getB2B());
                            args.putString("B2C", responseModel.getB2C());
                            args.putString("CHC", responseModel.getCHC());
                            args.putString("COMPANY", responseModel.getCOMPANY());
                            args.putString("PI", responseModel.getPI());
                            args.putString("RESID", responseModel.getRESID());
                            args.putString("RESPONSE", responseModel.getRESPONSE());
                            args.putString("YOURS", responseModel.getYOURS());
                            args.putParcelableArrayList("send", selectedTestsListRateCal);

                            before_discount_layout2.setVisibility(View.VISIBLE);
                            Log.e(TAG, "onResponse: " + displayslectedtestRateCalToShow);
                            show_selected_tests_list_test_ils1.setText(displayslectedtestRateCalToShow);
                            show_rates.setText(responseModel.getB2C());


                            if (access.equals("ADMIN")) {
                                show_b2b_rates.setText(String.valueOf(totalcount));
                                b2b_rate_adm.setVisibility(View.VISIBLE);
                                show_b2b_rates.setVisibility(View.VISIBLE);
                            } else {
                                b2b_rate_adm.setVisibility(View.GONE);
                                show_b2b_rates.setVisibility(View.GONE);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("send", selectedTestsListRateCal);
                        } else if (!GlobalClass.isNull(responseModel.getRESPONSE()) && responseModel.getRESPONSE().equalsIgnoreCase(caps_invalidApikey)) {
                            GlobalClass.redirectToLogin(mContext);
                        } else {
                            TastyToast.makeText(mContext, "" + responseModel.getRESPONSE(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Log.e(TAG, "TOAST MSG ------>" + responseModel.getRESPONSE());
                        }
                    } else {
                        Toast.makeText(mContext, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "TOAST MSG ------>" + responseModel.getRESPONSE());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.hideProgress(getActivity(), progressDialog);
                if (error != null) {
                } else {
                    System.out.println(error);
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
        POstQue.add(jsonObjectRequest1);
        Log.e(TAG, "fetchData: " + jsonObjectRequest1);
        Log.e(TAG, "fetchData: " + jsonObject);
    }

    private void callAdapter(GetMainModel mainModel) {

        b2bmasterarraylistRate = new ArrayList<>();
        b2bmasterarraylistRate.add(mainModel.B2B_MASTERS);

        ArrayList<Product_Rate_CalculatorModel> finalproductlist = new ArrayList<Product_Rate_CalculatorModel>();
        ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<Base_Model_Rate_Calculator>();

        if (getSpinnerSelectedItem.equalsIgnoreCase("TTL")) {
            for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                Product_Rate_CalculatorModel product_rate_masterModel = new Product_Rate_CalculatorModel();
                product_rate_masterModel.setTestType(Constants.PRODUCT_POP);
                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylistRate.get(i).getPOP());
                finalproductlist.add(product_rate_masterModel);
            }
        } else if (getSpinnerSelectedItem.equalsIgnoreCase("SMT")) {
            for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                Product_Rate_CalculatorModel product_rate_masterModel = new Product_Rate_CalculatorModel();
                product_rate_masterModel.setTestType(Constants.SMT_TEST);
                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylistRate.get(i).getSMT());
                finalproductlist.add(product_rate_masterModel);
            }
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void dynamicolordot(Context mContext, LinearLayout lin_color, int color, String sampletype) {
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(2, 0, 2, 2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMarginEnd(10);
        layoutParams.setMarginStart(10);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(GlobalClass.drawCircle(mContext, 40, 40, color));

        TextView textView = new TextView(mContext);
        textView.setText(sampletype);
        textView.setTextSize(4 * getResources().getDisplayMetrics().density);

        LinearLayout.LayoutParams textlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textlayoutParams.setMarginStart(10);
        textlayoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(textlayoutParams);

        lin_color.addView(imageView);
        lin_color.addView(textView);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void hideProgress() {
        if (barProgressDialog != null) {
            if (barProgressDialog.isShowing()) {
                Context context = ((ContextWrapper) barProgressDialog.getContext()).getBaseContext();
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                        barProgressDialog.dismiss();
                } else
                    barProgressDialog.dismiss();
            }
            barProgressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.rate_calculator_outlab_layout, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.lin_color.removeAllViews();
            holder.outlab_test.setText(outlabdetails_outLabs.get(position).getName());
            holder.outlab_test_rates.setText("₹ " + outlabdetails_outLabs.get(position).getRate().getB2c() + "/-");
            holder.checked.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);


            /*TODO Below logic for Outlab Sample type color code*/
            if (outlabdetails_outLabs != null || outlabdetails_outLabs.size() != 0) {
                SampleTypeColor sampleTypeColor = new SampleTypeColor(mContext, "outlab", outlabdetails_outLabs, position);
                sampleTypeColor.outLabcolor(holder.lin_color);
            }

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
                        out_lab_cost_b2b.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
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
            LinearLayout lin_color;

            public ViewHolder(View itemView) {
                super(itemView);
                outlab_test = (TextView) itemView.findViewById(R.id.outlab_test);
                outlab_test_rates = (TextView) itemView.findViewById(R.id.outlab_test_rates);
                check = (ImageView) itemView.findViewById(R.id.check);
                checked = (ImageView) itemView.findViewById(R.id.checked);
                lin_color = itemView.findViewById(R.id.lin_color);
            }
        }


    }
}
