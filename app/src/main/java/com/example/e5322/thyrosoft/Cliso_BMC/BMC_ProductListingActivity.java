package com.example.e5322.thyrosoft.Cliso_BMC;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.BMC_SelectSampleTypeActivity;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.BMC_B2B_MASTERSModel;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.BMC_BaseModel;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.BMC_MainModel;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

public class BMC_ProductListingActivity extends Activity {

    Activity mActivity = BMC_ProductListingActivity.this;
    EditText sv_testsList1;
    RecyclerView recycler_all_test;
    boolean isSelectedDueToParent;
    Button btn_save, btn_clear;
    ArrayList<BMC_BaseModel> Selected_TestList = new ArrayList<>();
    ImageView back, home;
    BMC_MainModel mainModel;
    ArrayList<BMC_BaseModel> bmc_baseModels = new ArrayList<>();
    ArrayList<BMC_BaseModel> filteredFiles;
    ArrayList<BMC_BaseModel> basicTESTArrayList;
    ArrayList<BMC_BaseModel> advanceTESTArrayList;
    ArrayList<BMC_BaseModel> basicSelectedArrayList;
    ArrayList<BMC_BaseModel> advanceSelectedArrayList;
    String TAG = BMC_ProductListingActivity.class.getSimpleName();
    TextView tv_selected_tests;
    ProgressDialog barProgressDialog;
    String user, passwrd, access, api_key;
    SharedPreferences prefs;
    LinearLayout ll_test_names, product_list_ll;
    TextView title, tv_basic, tv_advance;
    LinearLayoutManager linearLayoutManager;
    ArrayList<String> showTestNmaes = new ArrayList<>();
    private Global globalClass;
    private ArrayList<BMC_B2B_MASTERSModel> b2bmasterarraylist;
    private ArrayList<BMC_BaseModel> tempselectedTests;
    private List<String> tempselectedTests1, locationlist, selectedSubTypes;
    private AlertDialog.Builder alertDialogBuilder;
    private String parentTestCode;
    private boolean advanceIselected = false;
    private int advanceMax = 0, basicMax = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing_bmc);

        sv_testsList1 = (EditText) findViewById(R.id.sv_testsList1);
        recycler_all_test = (RecyclerView) findViewById(R.id.recycler_all_test);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        title = (TextView) findViewById(R.id.title);
        tv_basic = (TextView) findViewById(R.id.tv_basic);
        tv_advance = (TextView) findViewById(R.id.tv_advance);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        ll_test_names = (LinearLayout) findViewById(R.id.ll_test_names);
        product_list_ll = (LinearLayout) findViewById(R.id.product_list_ll);
        tv_selected_tests = (TextView) findViewById(R.id.tv_selected_tests);

        linearLayoutManager = new LinearLayoutManager(BMC_ProductListingActivity.this);
        recycler_all_test.setLayoutManager(linearLayoutManager);
        recycler_all_test.addItemDecoration(new DividerItemDecoration(BMC_ProductListingActivity.this, DividerItemDecoration.VERTICAL));
        recycler_all_test.setItemAnimator(new DefaultItemAnimator());

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ll_test_names.setVisibility(View.GONE);

        title.setText("Select Test(s)");
        Selected_TestList = new ArrayList<>();
        basicSelectedArrayList = new ArrayList<>();
        advanceSelectedArrayList = new ArrayList<>();

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        ShowPackages(false);
        initListeners();
    }

    private void ShowPackages(boolean b) {
        advanceIselected = b;
        if (b) {
            tv_basic.setBackgroundResource(R.drawable.square_bg_empty);
            tv_advance.setBackgroundResource(R.drawable.square_bg_filled);
            tv_basic.setTextColor(ContextCompat.getColor(mActivity, R.color.maroon));
            tv_advance.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        } else {
            tv_advance.setBackgroundResource(R.drawable.square_bg_empty);
            tv_basic.setBackgroundResource(R.drawable.square_bg_filled);
            tv_advance.setTextColor(ContextCompat.getColor(mActivity, R.color.maroon));
            tv_basic.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        }
        CheckProducts();
    }

    private void initListeners() {
        tv_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPackages(false);
            }
        });

        tv_advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPackages(true);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BMC_ProductListingActivity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BMC_ProductListingActivity.this, BMC_ProductListingActivity.class);
                startActivity(i);
                finish();
            }
        });

        sv_testsList1.addTextChangedListener(new TextWatcher() {
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

                if (s1.length() > 0) {
                    if (bmc_baseModels.size() != 0) {
                        for (int i = 0; i < bmc_baseModels.size(); i++) {
                            final String text = bmc_baseModels.get(i).getName().toLowerCase();
                            if (bmc_baseModels.get(i).getName() != null || !bmc_baseModels.get(i).getName().equals("")) {
                                name = bmc_baseModels.get(i).getName().toLowerCase();
                            }
                            if (bmc_baseModels.get(i).getCode() != null || !bmc_baseModels.get(i).getCode().equals("")) {
                                code = bmc_baseModels.get(i).getCode().toLowerCase();
                            }
                            if (bmc_baseModels.get(i).getProduct() != null || !bmc_baseModels.get(i).getProduct().equals("")) {
                                product = bmc_baseModels.get(i).getProduct().toLowerCase();
                            }
                            if (text.contains(s1) || (name != null && name.contains(s1)) || (code != null && code.contains(s1)) || (product != null && product.contains(s1))) {
                                String testname = bmc_baseModels.get(i).getName();
                                filteredFiles.add(bmc_baseModels.get(i));
                            }
                            sendDataToAdapter(filteredFiles);
                        }
                    } else {
                        ll_test_names.setVisibility(View.GONE);
                    }
                } else {
                    CheckProducts();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTExtdata = tv_selected_tests.getText().toString();
                if (Selected_TestList.size() == 0) {
                    Toast.makeText(mActivity, ToastFile.slt_test, Toast.LENGTH_SHORT).show();
                } else if (getTExtdata.equalsIgnoreCase("Selected Test")) {
                    Toast.makeText(mActivity, ToastFile.slt_test, Toast.LENGTH_SHORT).show();
                } else {
                    int sum = 0;
                    ArrayList<String> getTestNameLits = new ArrayList<>();
                    locationlist = new ArrayList<>();
                    selectedSubTypes = new ArrayList<>();
                    for (int i = 0; i < Selected_TestList.size(); i++) {
                        locationlist.add(Selected_TestList.get(i).getLocation());
                        sum = sum + Integer.parseInt(Selected_TestList.get(i).getRate().getB2c());
                        getTestNameLits.add(Selected_TestList.get(i).getProduct());
                        selectedSubTypes.add(Selected_TestList.get(i).getSubtypes());
                    }

                    boolean isBasic = false;
                    boolean isAdvance = false;
                    for (int i = 0; i < selectedSubTypes.size(); i++) {
                        if (selectedSubTypes.get(i).equalsIgnoreCase("BASIC")) {
                            isBasic = true;
                        } else if (selectedSubTypes.get(i).equalsIgnoreCase("ADVANCE")) {
                            isAdvance = true;
                        }
                    }

                    String payment = "";
                    if (isBasic && !isAdvance) {
                        payment = "223";
                    } else if (!isBasic && isAdvance) {
                        payment = "892";
                    } else if (isBasic && isAdvance) {
                        payment = "1115";
                    }

                    if (locationlist.contains("TTL-OTHERS")) {
                        Intent intent = new Intent(BMC_ProductListingActivity.this, BMC_SelectSampleTypeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("key", Selected_TestList);
                        bundle.putString("payment", payment);
                        bundle.putString("writeTestName", getTExtdata);
                        bundle.putStringArrayList("TestCodesPass", getTestNameLits);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(BMC_ProductListingActivity.this, BMC_Scan_BarcodeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("key", Selected_TestList);
                        bundle.putString("payment", payment);
                        bundle.putString("writeTestName", getTExtdata);
                        bundle.putStringArrayList("TestCodesPass", getTestNameLits);
                        bundle.putString("come_from", "ProductListing");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void CheckProducts() {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        final String json = appSharedPrefs.getString("MyObject", "");
        BMC_MainModel obj = gson.fromJson(json, BMC_MainModel.class);

        if (obj != null) {
            if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                if (obj.getB2B_MASTERS().getPOP().size() > 0 || obj.getB2B_MASTERS().getPROFILE().size() > 0
                        || obj.getB2B_MASTERS().getTESTS().size() > 0 || obj.getB2B_MASTERS().getTTLOthers().size() > 0)
                    callAdapter(obj);
                else {
                    if (!GlobalClass.isNetworkAvailable(BMC_ProductListingActivity.this)) {
                        TastyToast.makeText(BMC_ProductListingActivity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        CallGetProductsAPI();
                    }
                }
            } else {
                if (!GlobalClass.isNetworkAvailable(BMC_ProductListingActivity.this)) {
                    TastyToast.makeText(BMC_ProductListingActivity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    CallGetProductsAPI();
                }
            }
        } else {
            if (!GlobalClass.isNetworkAvailable(BMC_ProductListingActivity.this)) {
                TastyToast.makeText(BMC_ProductListingActivity.this, ToastFile.intConnection, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            } else {
                CallGetProductsAPI();
            }
        }
    }

    private void CallGetProductsAPI() {
        barProgressDialog = new ProgressDialog(BMC_ProductListingActivity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        RequestQueue requestQueuepoptestILS = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + api_key + "/ALL/getbmcproducts", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: RESPONSE" + response);
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                String getResponse = response.optString("RESPONSE", "");
                if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(BMC_ProductListingActivity.this);
                } else {
                    Gson gson = new Gson();
                    mainModel = new BMC_MainModel();
                    mainModel = gson.fromJson(response.toString(), BMC_MainModel.class);
                    SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(BMC_ProductListingActivity.this);
                    SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                    Gson gson22 = new Gson();
                    String json22 = gson22.toJson(mainModel);
                    prefsEditor1.putString("MyObject", json22);
                    prefsEditor1.commit();
                    callAdapter(mainModel);
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
        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(300000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuepoptestILS.add(jsonObjectRequestPop);
        Log.e(TAG, "onCreate: URL" + jsonObjectRequestPop);
    }

    private void callAdapter(BMC_MainModel mainModel) {
        b2bmasterarraylist = new ArrayList<>();
        b2bmasterarraylist.add(mainModel.B2B_MASTERS);
        bmc_baseModels = new ArrayList<>();
        basicTESTArrayList = new ArrayList<>();
        advanceTESTArrayList = new ArrayList<>();

        advanceMax = mainModel.getAdvancemax();
        basicMax = mainModel.getBasicmax();

        if (mainModel.B2B_MASTERS != null) {
            for (int i = 0; i < b2bmasterarraylist.size(); i++) {
                for (int j = 0; j < b2bmasterarraylist.get(i).getPOP().size(); j++) {
                    bmc_baseModels.add(b2bmasterarraylist.get(i).getPOP().get(j));
                }

                for (int k = 0; k < b2bmasterarraylist.get(i).getPROFILE().size(); k++) {
                    bmc_baseModels.add(b2bmasterarraylist.get(i).getPROFILE().get(k));
                }

                for (int l = 0; l < b2bmasterarraylist.get(i).getTESTS().size(); l++) {
                    bmc_baseModels.add(b2bmasterarraylist.get(i).getTESTS().get(l));
                }

                for (int n = 0; n < b2bmasterarraylist.get(i).getTTLOthers().size(); n++) {
                    bmc_baseModels.add(b2bmasterarraylist.get(i).getTTLOthers().get(n));
                }

                for (int m = 0; m < bmc_baseModels.size(); m++) {
                    if (bmc_baseModels.get(m).getSubtypes().equalsIgnoreCase("ADVANCE")) {
                        advanceTESTArrayList.add(bmc_baseModels.get(m));
                    } else if (bmc_baseModels.get(m).getSubtypes().equalsIgnoreCase("BASIC")) {
                        basicTESTArrayList.add(bmc_baseModels.get(m));
                    }
                }

                if (advanceIselected) {
                    sendDataToAdapter(advanceTESTArrayList);
                } else {
                    sendDataToAdapter(basicTESTArrayList);
                }
            }
        }
    }

    private void sendDataToAdapter(ArrayList<BMC_BaseModel> testRateMasterModels) {
        ViewAllTestAdapter viewAllTestAdapter = new ViewAllTestAdapter(BMC_ProductListingActivity.this, testRateMasterModels);
        recycler_all_test.setAdapter(viewAllTestAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    class ViewAllTestAdapter extends RecyclerView.Adapter<ViewAllTestAdapter.ViewHolder> {
        Context context;
        ArrayList<BMC_BaseModel> productList;
//        ArrayList<String> showTestNmaes = new ArrayList<>();

        public ViewAllTestAdapter(BMC_ProductListingActivity productLisitngActivityNew, ArrayList<BMC_BaseModel> testRateMasterModels) {
            this.context = productLisitngActivityNew;
            this.productList = testRateMasterModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.bmc_product_name_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.outlab_test.setText(productList.get(position).getName());
            holder.checked.setVisibility(View.GONE);
            holder.gray_check.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);
            boolean isChecked = false;

            holder.isSelectedDueToParent = false;
            holder.parentTestCode = "";
            final BMC_BaseModel testRateMasterModel = productList.get(position);

            if (Selected_TestList != null && Selected_TestList.size() > 0) {
                for (int i = 0; !isChecked && i < Selected_TestList.size(); i++) {
                    BMC_BaseModel selectedTestModel = Selected_TestList.get(i);
                    if (selectedTestModel.getCode().equals(testRateMasterModel.getCode())) {
                        holder.checked.setVisibility(View.VISIBLE);
                        holder.check.setVisibility(View.GONE);
                        holder.isSelectedDueToParent = false;
                        holder.parentTestCode = "";
                        isChecked = true;
                    } else if (selectedTestModel.getChilds() != null && testRateMasterModel.getChilds() != null && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                        holder.checked.setVisibility(View.GONE);
                        holder.check.setVisibility(View.GONE);
                        holder.gray_check.setVisibility(View.VISIBLE);
                        holder.isSelectedDueToParent = true;
                        holder.parentTestCode = selectedTestModel.getCode();
                        holder.parentTestname = selectedTestModel.getName();
                        isChecked = true;
                    } else {
                        if (selectedTestModel.getChilds() != null && selectedTestModel.getChilds().length > 0) {
                            for (BMC_BaseModel.Childs ctm : selectedTestModel.getChilds()) {
                                if (ctm.getCode().equals(testRateMasterModel.getCode())) {
                                    holder.check.setVisibility(View.GONE);
                                    holder.checked.setVisibility(View.GONE);
                                    holder.gray_check.setVisibility(View.VISIBLE);
                                    holder.isSelectedDueToParent = true;
                                    holder.parentTestCode = selectedTestModel.getCode();
                                    holder.parentTestname = selectedTestModel.getName();
                                    isChecked = true;
                                    break;
                                } else {
                                    holder.checked.setVisibility(View.GONE);
                                    holder.check.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            holder.checked.setVisibility(View.GONE);
                            holder.check.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else {
                holder.checked.setVisibility(View.GONE);
                holder.check.setVisibility(View.VISIBLE);
            }

            holder.parent_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //on click of blank box
                    if (holder.check.getVisibility() == View.VISIBLE) {
                        if (testRateMasterModel.getSubtypes().equalsIgnoreCase("BASIC")) {
                            if (basicSelectedArrayList.size() < basicMax) {
                                String str = "";
                                str = str + testRateMasterModel.getCode() + ",";
                                String slectedpackage = "";
                                slectedpackage = testRateMasterModel.getName();
                                tempselectedTests = new ArrayList<>();
                                tempselectedTests1 = new ArrayList<>();
                                if (testRateMasterModel.getChilds() != null) {
                                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                        for (int j = 0; j < Selected_TestList.size(); j++) {
                                            if (testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(Selected_TestList.get(j).getCode())) {
                                                System.out.println("Cart selectedtestlist Description :" + Selected_TestList.get(j).getName() + "Cart selectedtestlist Code :" + Selected_TestList.get(j).getCode());
                                                tempselectedTests1.add(Selected_TestList.get(j).getName());
                                                tempselectedTests.add(Selected_TestList.get(j));
                                            }
                                        }
                                    }
                                }
                                for (int j = 0; j < Selected_TestList.size(); j++) {
                                    BMC_BaseModel selectedTestModel123 = Selected_TestList.get(j);
                                    if (selectedTestModel123.getChilds() != null && testRateMasterModel.getChilds() != null && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                        tempselectedTests1.add(Selected_TestList.get(j).getName());
                                        tempselectedTests.add(selectedTestModel123);
                                    }
                                }

                                if (tempselectedTests != null && tempselectedTests.size() > 0) {
                                    HashSet<String> hashSet = new HashSet<String>();
                                    hashSet.addAll(tempselectedTests1);
                                    tempselectedTests1.clear();
                                    tempselectedTests1.addAll(hashSet);

                                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                                    alertDialogBuilder = new AlertDialog.Builder(BMC_ProductListingActivity.this);
                                    alertDialogBuilder
                                            .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                            .setCancelable(true)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                                for (int i = 0; i < tempselectedTests.size(); i++) {
                                    for (int j = 0; j < Selected_TestList.size(); j++) {
                                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(Selected_TestList.get(j).getCode())) {
                                            Selected_TestList.remove(j);
                                        }
                                    }
                                    for (int j = 0; j < basicSelectedArrayList.size(); j++) {
                                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(basicSelectedArrayList.get(j).getCode())) {
                                            basicSelectedArrayList.remove(j);
                                        }
                                    }
                                    for (int j = 0; j < advanceSelectedArrayList.size(); j++) {
                                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(advanceSelectedArrayList.get(j).getCode())) {
                                            advanceSelectedArrayList.remove(j);
                                        }
                                    }
                                }

                                if (testRateMasterModel.getSubtypes().equalsIgnoreCase("BASIC")) {
                                    basicSelectedArrayList.add(testRateMasterModel);
                                } else if (testRateMasterModel.getSubtypes().equalsIgnoreCase("ADVANCE")) {
                                    advanceSelectedArrayList.add(testRateMasterModel);
                                }

                                Selected_TestList.add(testRateMasterModel);
                                notifyDataSetChanged();
                                ll_test_names.setVisibility(View.VISIBLE);
                                showTestNmaes = new ArrayList<>();
                                for (int i = 0; i < Selected_TestList.size(); i++) {
                                    showTestNmaes.add(Selected_TestList.get(i).getName());
                                }
                                Set<String> setString = new HashSet<String>(showTestNmaes);
                                showTestNmaes.clear();
                                showTestNmaes.addAll(setString);
                                String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                tv_selected_tests.setText(displayslectedtest);
                                if (displayslectedtest.equals("")) {
                                    ll_test_names.setVisibility(View.GONE);
                                }
                            } else {
                                alertDialogBuilder = new AlertDialog.Builder(BMC_ProductListingActivity.this);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("Maximum " + basicMax + " test can be selected from basic package"))
                                        .setCancelable(true)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        } else if (testRateMasterModel.getSubtypes().equalsIgnoreCase("ADVANCE")) {
                            if (advanceSelectedArrayList.size() < advanceMax) {
                                String str = "";
                                str = str + testRateMasterModel.getCode() + ",";
                                String slectedpackage = "";
                                slectedpackage = testRateMasterModel.getName();
                                tempselectedTests = new ArrayList<>();
                                tempselectedTests1 = new ArrayList<>();
                                if (testRateMasterModel.getChilds() != null) {
                                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                        for (int j = 0; j < Selected_TestList.size(); j++) {
                                            if (testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(Selected_TestList.get(j).getCode())) {
                                                System.out.println("Cart selectedtestlist Description :" + Selected_TestList.get(j).getName() + "Cart selectedtestlist Code :" + Selected_TestList.get(j).getCode());
                                                tempselectedTests1.add(Selected_TestList.get(j).getName());
                                                tempselectedTests.add(Selected_TestList.get(j));
                                            }
                                        }
                                    }
                                }
                                for (int j = 0; j < Selected_TestList.size(); j++) {
                                    BMC_BaseModel selectedTestModel123 = Selected_TestList.get(j);
                                    if (selectedTestModel123.getChilds() != null && testRateMasterModel.getChilds() != null && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                        tempselectedTests1.add(Selected_TestList.get(j).getName());
                                        tempselectedTests.add(selectedTestModel123);
                                    }
                                }

                                if (tempselectedTests != null && tempselectedTests.size() > 0) {
                                    HashSet<String> hashSet = new HashSet<String>();
                                    hashSet.addAll(tempselectedTests1);
                                    tempselectedTests1.clear();
                                    tempselectedTests1.addAll(hashSet);

                                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                                    alertDialogBuilder = new AlertDialog.Builder(BMC_ProductListingActivity.this);
                                    alertDialogBuilder
                                            .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                            .setCancelable(true)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                                for (int i = 0; i < tempselectedTests.size(); i++) {
                                    for (int j = 0; j < Selected_TestList.size(); j++) {
                                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(Selected_TestList.get(j).getCode())) {
                                            Selected_TestList.remove(j);
                                        }
                                    }
                                    for (int j = 0; j < basicSelectedArrayList.size(); j++) {
                                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(basicSelectedArrayList.get(j).getCode())) {
                                            basicSelectedArrayList.remove(j);
                                        }
                                    }
                                    for (int j = 0; j < advanceSelectedArrayList.size(); j++) {
                                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(advanceSelectedArrayList.get(j).getCode())) {
                                            advanceSelectedArrayList.remove(j);
                                        }
                                    }
                                }

                                if (testRateMasterModel.getSubtypes().equalsIgnoreCase("BASIC")) {
                                    basicSelectedArrayList.add(testRateMasterModel);
                                } else if (testRateMasterModel.getSubtypes().equalsIgnoreCase("ADVANCE")) {
                                    advanceSelectedArrayList.add(testRateMasterModel);
                                }

                                Selected_TestList.add(testRateMasterModel);
                                notifyDataSetChanged();
                                ll_test_names.setVisibility(View.VISIBLE);
                                showTestNmaes = new ArrayList<>();
                                for (int i = 0; i < Selected_TestList.size(); i++) {
                                    showTestNmaes.add(Selected_TestList.get(i).getName());
                                }
                                Set<String> setString = new HashSet<String>(showTestNmaes);
                                showTestNmaes.clear();
                                showTestNmaes.addAll(setString);
                                String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                tv_selected_tests.setText(displayslectedtest);
                                if (displayslectedtest.equals("")) {
                                    ll_test_names.setVisibility(View.GONE);
                                }
                            } else {
                                alertDialogBuilder = new AlertDialog.Builder(BMC_ProductListingActivity.this);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("Maximum " + advanceMax + " test can be selected from advance package"))
                                        .setCancelable(true)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        }
                    }
                    //on click of checked box
                    else if (holder.checked.getVisibility() == View.VISIBLE) {
                        if (!isSelectedDueToParent) {
                            for (int i = 0; i < Selected_TestList.size(); i++) {
                                if (Selected_TestList.get(i).getCode().equals(testRateMasterModel.getCode())) {
                                    if (testRateMasterModel.getSubtypes().equalsIgnoreCase("BASIC")) {
                                        for (int j = 0; j < basicSelectedArrayList.size(); j++) {
                                            if (basicSelectedArrayList.get(j).getCode().equals(testRateMasterModel.getCode())) {
                                                basicSelectedArrayList.remove(j);
                                            }
                                        }
                                    } else if (testRateMasterModel.getSubtypes().equalsIgnoreCase("ADVANCE")) {
                                        for (int j = 0; j < advanceSelectedArrayList.size(); j++) {
                                            if (advanceSelectedArrayList.get(j).getCode().equals(testRateMasterModel.getCode())) {
                                                advanceSelectedArrayList.remove(j);
                                            }
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < Selected_TestList.size(); i++) {
                                if (Selected_TestList.get(i).getCode().equals(testRateMasterModel.getCode()))
                                    Selected_TestList.remove(i);
                            }
//                            Selected_TestList.remove(testRateMasterModel);
                            notifyDataSetChanged();
                            ll_test_names.setVisibility(View.VISIBLE);
                            showTestNmaes.remove(testRateMasterModel.getName());
                            String displayslectedtest = TextUtils.join(",", showTestNmaes);
                            tv_selected_tests.setText(displayslectedtest);
                            if (displayslectedtest.equals("")) {
                                ll_test_names.setVisibility(View.GONE);
                            }
                        } else {
                            alertDialogBuilder = new AlertDialog.Builder(BMC_ProductListingActivity.this);
                            alertDialogBuilder
                                    .setMessage(Html.fromHtml("This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode))
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                    Log.e(TAG, "AdvanceSelectedList size: " + advanceSelectedArrayList.size());
                    Log.e(TAG, "BasicSelectedList size: " + basicSelectedArrayList.size());
                }
            });
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView outlab_test;
            RelativeLayout parent_ll;
            boolean isSelectedDueToParent;
            String parentTestCode, parentTestname;
            ImageView check, checked, gray_check;

            public ViewHolder(View itemView) {
                super(itemView);
                parent_ll = (RelativeLayout) itemView.findViewById(R.id.ll_parent);
                outlab_test = (TextView) itemView.findViewById(R.id.outlab_test);
                check = (ImageView) itemView.findViewById(R.id.check);
                checked = (ImageView) itemView.findViewById(R.id.checked);
                gray_check = (ImageView) itemView.findViewById(R.id.gray_check);
            }
        }
    }
}
