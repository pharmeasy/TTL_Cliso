package com.example.e5322.thyrosoft.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.SampleTypeColor;
import com.example.e5322.thyrosoft.Adapter.RateCAlAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.InterfaceRateCAlculator;
import com.example.e5322.thyrosoft.MainModelForAllTests.OUTLAB_TESTLIST_GETALLTESTS;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.RateCalculatorForModels.GetMainModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Product_Rate_CalculatorModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.RateCalB2B_MASTERS_Main_Model;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
    String displayslectedtestRateCal;
    ArrayList<Base_Model_Rate_Calculator> getAllTests;
    String testToPassToAPI;
    ArrayList<Base_Model_Rate_Calculator> finalproductlist;
    RateCAlAdapter rateCAlAdapter;
    RecyclerView outlab_list;
    TextView companycost_test, out_lab_cost, out_lab_cost_b2b;
    ProgressDialog barProgressDialog;
    View viewrate_calfrag;
    GetMainModel mainModelRate;
    TextView show_selected_tests_list_test_ils1, show_rates, show_b2b_rates, show_rpl_rates, b2b_rate_label, rpl_rate_label, b2bratell, profile_txt, test_txt;
    EditText sv_testsList_ttl;
    LinearLayout search_option_ttl;
    GetMainModel mainModel;
    LinearLayout before_discount_layout2;
    ArrayList<RateCalB2B_MASTERS_Main_Model> b2bmasterarraylistRate;
    ArrayList<Base_Model_Rate_Calculator> selectedTestsListRateCal;
    List<String> showTestNmaesRateCal = new ArrayList<>();
    List<String> getOnlyTestCode;
    SharedPreferences prefs, preferences;
    String user, passwrd, access, api_key;
    ArrayList<Integer> getvalue;
    Button go_button;
    RecyclerView containerlist;
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
    boolean viewmoreflag = false;
    RadioGroup location_radio_grp;
    RadioButton cpl_rdo, rpl_rdo;
    private int rate_percent, max_amt, tpc_perc;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private MyPojo myPojo;
    private int B2B_RATE;
    private String displayslectedtestRateCalToShow;
    private ArrayList<String> getBrandName;
    private EditText outlabtestsearch;
    private TextView show_selected_tests_list_test_ils;
    private LinearLayout lineargetselectedtestforILS;
    private int totalcountForOutlab;
    private int totalcountForOutlabb2b;
    private String getSpinnerSelectedItem;
    private ArrayList<Base_Model_Rate_Calculator> totalproductlist;
    private String TAG = getClass().getSimpleName();
    private TextView txt_more;
    //    private ArrayList<String> locationsList;
    private Activity mActivity;

    public RateCalculatorFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mContext = (ManagingTabsActivity) getActivity();
        viewrate_calfrag = inflater.inflate(R.layout.fragment_rate_calculator, container, false);
        getvalue = new ArrayList<>();
        selectedTestsListRateCal = new ArrayList<>();

        initUI();

        show_selected_tests_list_test_ils1.setMovementMethod(new ScrollingMovementMethod());

        linearLayoutManager = new LinearLayoutManager(getContext());
        containerlist.setLayoutManager(linearLayoutManager);
        containerlist.setItemAnimator(new DefaultItemAnimator());
        containerlist.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));

        txt_more.setText("More..");

        initListeners();

        linearLayoutManager = new LinearLayoutManager(getContext());
        outlab_list.setLayoutManager(linearLayoutManager);
        outlab_list.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        outlab_list.setItemAnimator(new DefaultItemAnimator());

        show_selected_tests_list_test_ils.setMovementMethod(new ScrollingMovementMethod());
        lineargetselectedtestforILS.setVisibility(View.GONE);
        before_discount_layout2.setVisibility(View.GONE);

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");

        //todo get rate percent from profile details
        preferences = getActivity().getSharedPreferences("profile", MODE_PRIVATE);
        rate_percent = preferences.getInt(Constants.rate_percent, 0);
        tpc_perc = preferences.getInt(Constants.tpcPercent, 0);
        max_amt = preferences.getInt(Constants.max_amt, 0);

        System.out.println("<< TPC percent >>" + tpc_perc);
        System.out.println("<< rate percent >>" + rate_percent);
        System.out.println("<< max amount >>" + max_amt);
        Log.e(TAG, "Access Type : " + access);

        if (!GlobalClass.isNetworkAvailable(getActivity())) {
            offline_img.setVisibility(View.VISIBLE);
            linear_layout_data.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            linear_layout_data.setVisibility(View.VISIBLE);
        }
        profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));
        test_txt.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
        test_txt.setTextColor(getResources().getColor(R.color.colorWhite));
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("saveAlldata", "");
        myPojo = gson.fromJson(json, MyPojo.class);
        if (myPojo != null) {
            getBrandName = new ArrayList<>();
            if (myPojo != null && myPojo.getMASTERS() != null) {
                if (myPojo.getMASTERS().getBRAND_LIST() != null) {
                    for (int i = 0; i < myPojo.getMASTERS().getBRAND_LIST().length; i++) {
                        getBrandName.add(myPojo.getMASTERS().getBRAND_LIST()[i].getBrand_name());
                    }
                    brand_name_rt_cal.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinnerproperty, getBrandName));
                }
            }
        } else {
            requestJsonObject();
        }

        brand_name_rt_cal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                    sv_testsList_ttl.getText().clear();
                    before_discount_layout2.setVisibility(View.GONE);
                    getRatesofB2bandB2C(getSpinnerSelectedItem, mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (GlobalClass.syncProduct(getActivity())) {
            getAllproduct();
        } else {
            getDataFromSharedPref();
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

                try {
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

                                if (text.contains(s1) || (name != null && name.contains(s1)) || (code != null && code.contains(s1)) || (product != null && product.contains(s1))) {
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
                        if (!GlobalClass.isNetworkAvailable(mActivity)) {
                            offline_img.setVisibility(View.VISIBLE);
                            linear_layout_data.setVisibility(View.GONE);
                        } else {
                            offline_img.setVisibility(View.GONE);
                            linear_layout_data.setVisibility(View.VISIBLE);
                            getAllproduct();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        profile_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sv_testsList_ttl.setText("");
                    test_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    test_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                    profile_txt.setBackground(getResources().getDrawable(R.drawable.maroon_rect_right_round));
                    profile_txt.setTextColor(getResources().getColor(R.color.colorWhite));
                    finalproductlist = new ArrayList<>();
                    ArrayList<Base_Model_Rate_Calculator> testRateMasterModels = new ArrayList<>();
                    if (obj != null) {
                        if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                            b2bmasterarraylistRate = new ArrayList<>();
                            b2bmasterarraylistRate.add(obj.B2B_MASTERS);
                            for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                                finalproductlist.addAll(b2bmasterarraylistRate.get(i).getPOP());
                                finalproductlist.addAll(b2bmasterarraylistRate.get(i).getPROFILE());
                            }
                            callAdapaterTosetData(finalproductlist, totalproductlist);
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
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        test_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sv_testsList_ttl.setText("");
                    test_txt.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
                    test_txt.setTextColor(getResources().getColor(R.color.colorWhite));
                    profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                    finalproductlist = new ArrayList<>();
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
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
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
                filteredFilesttl = new ArrayList<>();
                String name = "";
                String code = "";
                String product = "";
                if (finalproductlist != null && finalproductlist.size() != 0) {
                    for (int i = 0; i < finalproductlist.size(); i++) {
                        if (finalproductlist.get(i).getName() != null || !finalproductlist.get(i).getName().equals("")) {
                            name = finalproductlist.get(i).getName().toLowerCase();
                        }
                        if (finalproductlist.get(i).getCode() != null || !finalproductlist.get(i).getCode().equals("")) {
                            code = finalproductlist.get(i).getCode().toLowerCase();
                        }
                        if (finalproductlist.get(i).getProduct() != null || !finalproductlist.get(i).getProduct().equals("")) {
                            product = finalproductlist.get(i).getProduct().toLowerCase();
                        }
                        if ((name != null && name.contains(s1)) || (code != null && code.contains(s1)) || (product != null && product.contains(s1))) {
                            filteredFilesttl.add(finalproductlist.get(i));
                        }
                    }
                    callAdapaterTosetData(filteredFilesttl, totalproductlist);
                } else {
                    before_discount_layout2.setVisibility(View.GONE);
                }
            }
        });
        return viewrate_calfrag;
    }

    private void initUI() {
        go_button = (Button) viewrate_calfrag.findViewById(R.id.go_button);
        companycost_test = (TextView) viewrate_calfrag.findViewById(R.id.companycost_test);
        show_selected_tests_list_test_ils1 = (TextView) viewrate_calfrag.findViewById(R.id.show_selected_tests_list_test_ils1);
        location_radio_grp = (RadioGroup) viewrate_calfrag.findViewById(R.id.location_radio_grp);
        cpl_rdo = (RadioButton) viewrate_calfrag.findViewById(R.id.cpl_rdo);
        rpl_rdo = (RadioButton) viewrate_calfrag.findViewById(R.id.rpl_rdo);
        show_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_rates);
        b2b_rate_label = (TextView) viewrate_calfrag.findViewById(R.id.b2b_rate_label);
        rpl_rate_label = (TextView) viewrate_calfrag.findViewById(R.id.rpl_rate_label);
        b2bratell = (TextView) viewrate_calfrag.findViewById(R.id.b2bratell);
        show_b2b_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_b2b_rates);
        show_rpl_rates = (TextView) viewrate_calfrag.findViewById(R.id.show_rpl_rates);
        profile_txt = (TextView) viewrate_calfrag.findViewById(R.id.profile_txt);
        sv_testsList_ttl = (EditText) viewrate_calfrag.findViewById(R.id.sv_testsList12);
        test_txt = (TextView) viewrate_calfrag.findViewById(R.id.test_txt);
        linear_layout_data = (LinearLayout) viewrate_calfrag.findViewById(R.id.linear_layout_data);
        offline_img = (LinearLayout) viewrate_calfrag.findViewById(R.id.offline_img);
        containerlist = (RecyclerView) viewrate_calfrag.findViewById(R.id.containerlist);
        pop_profile_test_bar = (LinearLayout) viewrate_calfrag.findViewById(R.id.pop_profile_test_bar);
        search_option_ttl = (LinearLayout) viewrate_calfrag.findViewById(R.id.search_option_ttl);
        before_discount_layout2 = (LinearLayout) viewrate_calfrag.findViewById(R.id.before_discount_layout2);
        lin_color = viewrate_calfrag.findViewById(R.id.lin_color);
        lin_color2 = viewrate_calfrag.findViewById(R.id.lin_color2);
        txt_more = viewrate_calfrag.findViewById(R.id.txt_more);
        brand_name_rt_cal = (Spinner) viewrate_calfrag.findViewById(R.id.brand_name_rt_cal);
        out_lab_cost = (TextView) viewrate_calfrag.findViewById(R.id.out_lab_cost);
        out_lab_cost_b2b = (TextView) viewrate_calfrag.findViewById(R.id.out_lab_cost_b2b);
        outlab_list = (RecyclerView) viewrate_calfrag.findViewById(R.id.outlab_list);
        outlabtestsearch = (EditText) viewrate_calfrag.findViewById(R.id.outlabtestsearch);
        show_selected_tests_list_test_ils = (TextView) viewrate_calfrag.findViewById(R.id.show_selected_tests_list_test_ils);
        lineargetselectedtestforILS = (LinearLayout) viewrate_calfrag.findViewById(R.id.lineargetselectedtestforILS);
        before_discount_layout2 = (LinearLayout) viewrate_calfrag.findViewById(R.id.before_discount_layout2);
    }

    private void initListeners() {
        txt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewmoreflag) {
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

        cpl_rdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv_testsList_ttl.setText("");
                rpl_rate_label.setVisibility(View.GONE);
                show_rpl_rates.setVisibility(View.GONE);
                if (access.equals("ADMIN")) {
                    show_b2b_rates.setText(String.valueOf(B2B_RATE));
                    b2b_rate_label.setVisibility(View.VISIBLE);
                    show_b2b_rates.setVisibility(View.VISIBLE);
                } else {
                    b2b_rate_label.setVisibility(View.GONE);
                    show_b2b_rates.setVisibility(View.GONE);
                }
                try {
                    getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                    getRatesofB2bandB2C(getSpinnerSelectedItem, mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rpl_rdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv_testsList_ttl.setText("");
                show_rpl_rates.setVisibility(View.GONE);//todo changed to gone as per input hide RPL rates and show B2B rates
                rpl_rate_label.setVisibility(View.GONE);//todo changed to gone as per input hide RPL rates and show B2B rates
                if (access.equals("ADMIN")) {
                    show_b2b_rates.setText(String.valueOf(B2B_RATE));
                    b2b_rate_label.setVisibility(View.VISIBLE);
                    show_b2b_rates.setVisibility(View.VISIBLE);
                } else {
                    b2b_rate_label.setVisibility(View.GONE);
                    show_b2b_rates.setVisibility(View.GONE);
                }
                try {
                    getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                    getRatesofB2bandB2C(getSpinnerSelectedItem, mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAllproduct() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(getActivity());
        RequestQueue requestQueuepoptestILS = GlobalClass.setVolleyReq(mContext);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GlobalClass.hideProgress(getActivity(), progressDialog);
                Log.e(TAG, "onResponse for All products :::: " + response);
                GlobalClass.StoresyncProduct(getActivity());
                String getResponse = response.optString("RESPONSE", "");
                if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(mContext);
                } else {
                    Gson gson = new Gson();
                    mainModel = new GetMainModel();
                    mainModelRate = gson.fromJson(response.toString(), GetMainModel.class);

                    SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                    Gson gson22 = new Gson();
                    String json23 = gson22.toJson(mainModelRate);
                    prefsEditor1.putString("MyObject", json23);
                    prefsEditor1.apply();

                    try {
                        getSpinnerSelectedItem = brand_name_rt_cal.getSelectedItem().toString();
                        getRatesofB2bandB2C(getSpinnerSelectedItem, mContext);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                                    for (int j = 0; j < b2bmasterarraylistRate.get(i).getTESTS().size(); j++) {
                                        finalproduct_list.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
                                        b2bmasterarraylistRate.get(i).getTESTS().get(j).setIsCart("no");
                                        b2bmasterarraylistRate.get(i).getTESTS().get(j).setIs_lock("no");
                                        testRateMasterModels.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
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
                GlobalClass.hideProgress(getActivity(), progressDialog);
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequestPop);
        requestQueuepoptestILS.add(jsonObjectRequestPop);
        Log.e(TAG, "afterTextChanged: url" + jsonObjectRequestPop);
    }

    private void getDataFromSharedPref() {
        try {
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
                    if (getSpinnerSelectedItem != null) {
                        if (getSpinnerSelectedItem.equalsIgnoreCase("TTL")) {
                            for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                                for (int j = 0; j < b2bmasterarraylistRate.get(i).getPOP().size(); j++) {
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
                                    finalproductlist.add(b2bmasterarraylistRate.get(i).getTESTS().get(j));
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
                    }
                    totalproductlist = getAllTests;
                    callAdapaterTosetData(finalproductlist, totalproductlist);
                } else {
                    getAllproduct();
                }
            } else {
                getAllproduct();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void callAdapaterTosetData(final ArrayList<Base_Model_Rate_Calculator> finalproductlist, ArrayList<Base_Model_Rate_Calculator> getAllTests) {
        //TODO commented to hide CPL RPL radio buttons as per input 12-10-2020
        /*ArrayList<Base_Model_Rate_Calculator> tempfinalproductlist = new ArrayList<>();
        if (finalproductlist != null && finalproductlist.size() > 0) {
            for (int i = 0; i < finalproductlist.size(); i++) {
                if (!GlobalClass.isNull(finalproductlist.get(i).getIsCPL())) {
                    if (cpl_rdo.isChecked()) {
                        if (finalproductlist.get(i).getIsCPL().equalsIgnoreCase("1")) {
                            tempfinalproductlist.add(finalproductlist.get(i));
                        }
                    } else {
                        if (finalproductlist.get(i).getIsCPL().equalsIgnoreCase("0")) {
                            tempfinalproductlist.add(finalproductlist.get(i));
                        }
                    }
                }
            }
        }*/

        rateCAlAdapter = new RateCAlAdapter(mContext, finalproductlist, totalproductlist, selectedTestsListRateCal, new InterfaceRateCAlculator() {
            @Override
            public void onCheckChangeRateCalculator(ArrayList<Base_Model_Rate_Calculator> selectedTests) {
                System.out.println("check changed");
                selectedTestsListRateCal = selectedTests;
                if (selectedTestsListRateCal.size() != 0) {
                    showTestNmaesRateCal = new ArrayList<>();
                    getOnlyTestCode = new ArrayList<>();
                    for (int i = 0; i < selectedTestsListRateCal.size(); i++) {
                        showTestNmaesRateCal.add(selectedTestsListRateCal.get(i).getName());
                        if (selectedTestsListRateCal.get(i).getBarcodes().length != 0)
                            for (int j = 0; j < selectedTestsListRateCal.get(i).getBarcodes().length; j++) {
                                getOnlyTestCode.add(selectedTestsListRateCal.get(i).getBarcodes()[j].getCode());
                            }
                    }
                    /*if (selectedTestsListRateCal != null && selectedTestsListRateCal.size() > 0) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < selectedTestsListRateCal.size(); i++) {
                            if (!GlobalClass.isNull(selectedTestsListRateCal.get(i).getIsCPL())) {
                                list.add(selectedTestsListRateCal.get(i).getIsCPL());
                                if (list.size() > 0) {
                                    if (list.contains("0")) {
                                        b2b_rate_label.setVisibility(View.VISIBLE);
                                        show_b2b_rates.setVisibility(View.VISIBLE);
                                    } else {
                                        b2b_rate_label.setVisibility(View.VISIBLE);
                                        show_b2b_rates.setVisibility(View.VISIBLE);
                                        rpl_rate_label.setVisibility(View.GONE);
                                        show_rpl_rates.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }*/

                    if (getOnlyTestCode != null) {
                        HashSet<String> hashSet = new HashSet<String>(getOnlyTestCode);
                        getOnlyTestCode.clear();
                        getOnlyTestCode.addAll(hashSet);
                    }
                    B2B_RATE = 0;//TODO renamed from CPL_RATE to B2B_RATE to remove CPL rates
                    int B2Crate = 0;
                    int RPL_RATE = 0;
                    int HARDCODE_CPL_RATE = 0;

                    if (!brand_name_rt_cal.getSelectedItem().toString().equalsIgnoreCase("TTL")) {
                        for (int j = 0; j < selectedTestsListRateCal.size(); j++) {
                            /*if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getRate().getCplr())) {
                                CPL_RATE = CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getCplr());
                            } else {*/
                            B2B_RATE = B2B_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
//                            }
                            B2Crate = B2Crate + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2c());
                        }
                    } else {
//                        locationsList = new ArrayList<>();
                        /*boolean isWC2020Present = false;
                        for (int i = 0; i < selectedTestsListRateCal.size(); i++) {
                            if (Global.checkHardcodeTest(selectedTestsListRateCal.get(i).getCode())) {
                                isWC2020Present = true;
                                break;
                            }
                        }*/
                        for (int j = 0; j < selectedTestsListRateCal.size(); j++) {
//                            locationsList.add(selectedTestsListRateCal.get(j).getIsCPL());
                            //todo commented to hide CPL RPL rates as per the input 09-10-2020
                            /*if (Global.checkCovidTest(selectedTestsListRateCal.get(j).getIsAB())) {
                                if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getIsCPL()) && selectedTestsListRateCal.get(j).getIsCPL().equalsIgnoreCase("0")) {
                                    if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getRate().getRplr())) {
                                        HARDCODE_CPL_RATE = HARDCODE_CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getRplr());
                                    } else {
                                    HARDCODE_CPL_RATE = HARDCODE_CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                                    }
                                } else {
                                    if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getRate().getCplr())) {
                                        HARDCODE_CPL_RATE = HARDCODE_CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getCplr());
                                    } else {
                                    HARDCODE_CPL_RATE = HARDCODE_CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                                    }
                                }
                            } else {*/
                                /*if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getRate().getCplr())) {
                                    CPL_RATE = CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getCplr());
                                } else {*/
                            if (Global.checkHardcodeTest(selectedTestsListRateCal.get(j).getCode())) {
                                HARDCODE_CPL_RATE = HARDCODE_CPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                            } else {
                                B2B_RATE = B2B_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                            }
                            /*}*/
                            /*}*/
                            B2Crate = B2Crate + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2c());
                            /*if (locationsList != null && locationsList.contains("0")) {
                                if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getIsCPL()) && selectedTestsListRateCal.get(j).getIsCPL().equalsIgnoreCase("0")) {
                                    *//*if (!Global.checkCovidTest(selectedTestsListRateCal.get(j).getIsAB())) {
                                        if (!GlobalClass.isNull(selectedTestsListRateCal.get(j).getRate().getRplr())) {
                                            RPL_RATE = RPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getRplr());
                                        } else {*//*
                                    RPL_RATE = RPL_RATE + Integer.parseInt(selectedTestsListRateCal.get(j).getRate().getB2b());
                                        *//*}
                                    }*//*
                                }
                            }*/
                        }
                        try {
                            System.out.println("B2B_RATE value :" + B2B_RATE);
                            System.out.println("HARDCODE value :" + HARDCODE_CPL_RATE);
                            //todo calculate the amount as per rate percent
                            B2B_RATE = calcAmount(B2B_RATE, HARDCODE_CPL_RATE);
                            System.out.println("RPL_RATE value :" + RPL_RATE);
                            /*if (locationsList != null && locationsList.contains("1") && locationsList.contains("0")) {
                                RPL_RATE = CPL_RATE;
                            } else {
                                RPL_RATE = calcAmount(RPL_RATE, HARDCODE_CPL_RATE, isWC2020Present);
                            }*/
                            show_rpl_rates.setText("" + RPL_RATE);
                            System.out.println("Calculated B2B rate with per :" + B2B_RATE);
                            System.out.println("Calculated RPL rate with per :" + RPL_RATE);
                            System.out.println("B2Crate value  :" + B2Crate);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    displayslectedtestRateCal = null;
                    displayslectedtestRateCalToShow = TextUtils.join(", ", showTestNmaesRateCal);
                    testToPassToAPI = TextUtils.join(",", getOnlyTestCode);
                    if (!GlobalClass.isNetworkAvailable(mActivity)) {
                        offline_img.setVisibility(View.VISIBLE);
                        linear_layout_data.setVisibility(View.GONE);
                    } else {
                        offline_img.setVisibility(View.GONE);
                        linear_layout_data.setVisibility(View.VISIBLE);
                        before_discount_layout2.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onResponse: " + displayslectedtestRateCalToShow);
                        show_selected_tests_list_test_ils1.setText(displayslectedtestRateCalToShow);
                        show_rates.setText("" + B2Crate);
                        if (access.equals("ADMIN")) {
                            show_b2b_rates.setText(String.valueOf(B2B_RATE));
                            b2b_rate_label.setVisibility(View.VISIBLE);
                            show_b2b_rates.setVisibility(View.VISIBLE);
                        } else {
                            b2b_rate_label.setVisibility(View.GONE);
                            show_b2b_rates.setVisibility(View.GONE);
                        }
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
                } else {
                    selectedTestsListRateCal.size();
                    show_selected_tests_list_test_ils1.setText("");
                    show_rates.setText("0");
                    before_discount_layout2.setVisibility(View.GONE);
                }
                manageCPLRPLView();
            }
        });
        containerlist.setAdapter(rateCAlAdapter);
        manageCPLRPLView();
    }

    private int calcAmount(int RATE, int HARDCODE_CPL_RATE) {
        try {
            float aFloat = Float.parseFloat(String.valueOf(RATE));
            float per_amt = ((aFloat / 100) * rate_percent);
            System.out.println("Calculated percent rate :" + per_amt);
            int per_amt1 = Math.round(per_amt);
            if (per_amt1 < max_amt) {
                RATE = RATE + per_amt1;
            } else {
                RATE = RATE + max_amt;
            }
            //TODO Added logic for TPC client for rates.
            aFloat = Float.parseFloat(String.valueOf(RATE));
            float per_amt2 = ((aFloat / 100) * tpc_perc);
            int tpcrate = Math.round(per_amt2);
            System.out.println("Calculated TCP Client rate :" + tpcrate);
            RATE = RATE + tpcrate;
            RATE = RATE + HARDCODE_CPL_RATE;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return RATE;
    }

    private void manageCPLRPLView() {
        try {
            if (!GlobalClass.isNull(access) && access.equalsIgnoreCase("ADMIN")) {
                /*if (GlobalClass.CheckArrayList(locationsList) && locationsList.contains("0")) {
                    rpl_rate_label.setVisibility(View.GONE);//todo changed to gone as per input hide RPL rates and show B2B rates
                    show_rpl_rates.setVisibility(View.GONE);//todo changed to gone as per input hide RPL rates and show B2B rates
                } else {*/
                rpl_rate_label.setVisibility(View.GONE);
                show_rpl_rates.setVisibility(View.GONE);
                /*}*/
            } else {
                rpl_rate_label.setVisibility(View.GONE);
                show_rpl_rates.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRatesofB2bandB2C(String getSpinnerSelectedItem, ManagingTabsActivity mContext) {
        if (!brand_name_rt_cal.getSelectedItem().toString().equalsIgnoreCase("TTL")) {
            if (selectedTestsListRateCal != null) {
                selectedTestsListRateCal.clear();
            }
            location_radio_grp.setVisibility(View.GONE);
        } else {
            location_radio_grp.setVisibility(View.GONE);//TODO changed to GONE to remove CPL RPL radio buttons as per input 12-10-2020
        }
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
                            profile_txt.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            profile_txt.setTextColor(getResources().getColor(R.color.colorBlack));
                            test_txt.setBackground(getResources().getDrawable(R.drawable.maroon_rect_left_round));
                            test_txt.setTextColor(getResources().getColor(R.color.colorWhite));
                        }
                        callAdapter(obj);
                        outlabtestsearch.setVisibility(View.GONE);
                        lineargetselectedtestforILS.setVisibility(View.GONE);
                        outlab_list.setVisibility(View.GONE);
                        if (!brand_name_rt_cal.getSelectedItem().toString().equalsIgnoreCase("TTL")) {
                            if (selectedTestsListRateCal != null) {
                                selectedTestsListRateCal.clear();
                            }
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
            RequestQueue requestQueue = GlobalClass.setVolleyReq(getContext());
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, Api.getData + "" + api_key + "/" + "" + user + "/B2BAPP/getwomaster", new Response.Listener<JSONObject>() {
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
                        prefsEditor1.apply();
                        getBrandName = new ArrayList<>();

                        if (myPojo != null && myPojo.getMASTERS() != null && myPojo.getMASTERS().getBRAND_LIST() != null) {
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

                    }
                }
            });
            GlobalClass.volleyRetryPolicy(jsonObjectRequest2);
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

    private void callAdapter(GetMainModel mainModel) {
        b2bmasterarraylistRate = new ArrayList<>();
        b2bmasterarraylistRate.add(mainModel.B2B_MASTERS);
        ArrayList<Product_Rate_CalculatorModel> finalproductlist = new ArrayList<Product_Rate_CalculatorModel>();
        if (getSpinnerSelectedItem.equalsIgnoreCase("TTL")) {
            for (int i = 0; i < b2bmasterarraylistRate.size(); i++) {
                Product_Rate_CalculatorModel product_rate_masterModel = new Product_Rate_CalculatorModel();
                product_rate_masterModel.setTestType(Constants.PRODUCT_TEST);
                product_rate_masterModel.setTestRateMasterModels(b2bmasterarraylistRate.get(i).getTESTS());
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
                @SuppressLint("WrongConstant")
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
                    for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                        if (!GlobalClass.isNull(Selcted_Outlab_Test.get(j).getRate().getCplr())) {
                            totalcountForOutlabb2b = totalcountForOutlabb2b + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getCplr());
                        } else {
                            totalcountForOutlabb2b = totalcountForOutlabb2b + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2b());
                        }
                        totalcountForOutlab = totalcountForOutlab + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2c());
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

                    if (!GlobalClass.isNull(displayslectedtest)) {
                        for (int j = 0; j < Selcted_Outlab_Test.size(); j++) {
                            if (!GlobalClass.isNull(Selcted_Outlab_Test.get(j).getRate().getCplr())) {
                                totalcountForOutlabb2b = totalcountForOutlabb2b + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getCplr());
                            } else {
                                totalcountForOutlabb2b = totalcountForOutlabb2b + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2b());
                            }
                            totalcountForOutlab = totalcountForOutlab + Integer.parseInt(Selcted_Outlab_Test.get(j).getRate().getB2c());
                        }
                        show_selected_tests_list_test_ils.setText(displayslectedtest);
                        out_lab_cost.setText(String.valueOf(totalcountForOutlab));
                        out_lab_cost_b2b.setText(String.valueOf(totalcountForOutlabb2b));
                    } else {
                        lineargetselectedtestforILS.setVisibility(View.GONE);
                        show_selected_tests_list_test_ils.setText("");
                        out_lab_cost.setText("");
                        out_lab_cost_b2b.setText("");
                        show_selected_tests_list_test_ils.setVisibility(View.GONE);
                        out_lab_cost.setVisibility(View.GONE);
                        out_lab_cost_b2b.setVisibility(View.GONE);
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