package com.example.e5322.thyrosoft.WOE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.Cliso_SelctSampleActivity;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.ProductListController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.OUTLAB_TESTLIST_GETALLTESTS;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.e5322.thyrosoft.API.Constants.caps_invalidApikey;
import static com.example.e5322.thyrosoft.GlobalClass.redirectToLogin;

public class OutLabTestsActivity extends AppCompatActivity {
    private static final String TAG = OutLabTestsActivity.class.getSimpleName();
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
    RecyclerView outlab_list;
    EditText outlabtestsearch;
    List<String> showTestNmaes = new ArrayList<>();
    MainModel mainModel;
    RequestQueue requestQueuepoptestILS;
    String user, passwrd, access, api_key, brandName, typeName;
    SharedPreferences prefs;
    ArrayList<OUTLAB_TESTLIST_GETALLTESTS> outlab_testlist_getalltests;
    ArrayList<Outlabdetails_OutLab> Selcted_Outlab_Test = new ArrayList<>();
    ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs;
    ArrayList<Outlabdetails_OutLab> filteredFiles;
    LinearLayoutManager linearLayoutManager;
    LinearLayout lineargetselectedtestforILS;
    TextView show_selected_tests_list_test_ils, title;
    Button next_btn;
    private SharedPreferences prefsavedetails;
    private String getTypeName;
    private ImageView back;
    private ImageView home;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_lab_tests);

        activity = OutLabTestsActivity.this;

        initViews();


        prefsavedetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefsavedetails.getString("WOEbrand", "");
        typeName = prefsavedetails.getString("typeName", "");


        if (GlobalClass.Dayscnt(OutLabTestsActivity.this) >= Constants.DAYS_CNT) {
            getAlltTestData();
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");
            MainModel obj = gson.fromJson(json, MainModel.class);

            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && !GlobalClass.isNull(obj.getUSER_TYPE())) {
                    if (GlobalClass.CheckArrayList(obj.getB2B_MASTERS().getOUTLAB_TESTLIST())) {
                        outlab_testlist_getalltests = obj.getB2B_MASTERS().getOUTLAB_TESTLIST();
                    }

                    outlabdetails_outLabs = new ArrayList<>();
                    if (GlobalClass.CheckArrayList(outlab_testlist_getalltests)) {
                        for (int i = 0; i < outlab_testlist_getalltests.size(); i++) {
                            if (!GlobalClass.isNull(brandName) &&
                                    !GlobalClass.isNull(outlab_testlist_getalltests.get(i).getLOCATION()) &&
                                    brandName.equalsIgnoreCase(outlab_testlist_getalltests.get(i).getLOCATION())) {
                                if (GlobalClass.CheckArrayList(outlabdetails_outLabs)) {
                                    outlabdetails_outLabs = null;
                                    outlabdetails_outLabs = new ArrayList<>();
                                    outlabdetails_outLabs = outlab_testlist_getalltests.get(i).getOutlabdetails();
                                }
                                callAdapter();
                            }
                        }
                    }

                } else {
                    getAlltTestData();
                }
            } else {
                getAlltTestData();
            }
        }

        initListner();
    }

    private void initListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(OutLabTestsActivity.this);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (GlobalClass.isNull(show_selected_tests_list_test_ils.getText().toString())) {
                    GlobalClass.showTastyToast(OutLabTestsActivity.this, ToastFile.slt_test, 2);
                } else {
                    if (GlobalClass.CheckArrayList(Selcted_Outlab_Test)) {
                        String sendTestNames = show_selected_tests_list_test_ils.getText().toString();
                        if (Selcted_Outlab_Test.size() == 1) {
                            intent = new Intent(OutLabTestsActivity.this, Scan_Barcode_Outlabs.class);
                        } else {
                            intent = new Intent(OutLabTestsActivity.this, Cliso_SelctSampleActivity.class);
                        }
                        Bundle bundle = new Bundle();
                        Log.e(TAG, "onClick: " + Selcted_Outlab_Test.size());
                        bundle.putParcelableArrayList("getOutlablist", Selcted_Outlab_Test);
                        bundle.putString("selectedTest", sendTestNames);
                        bundle.putString("getTypeName", getTypeName);
                        GlobalClass.selectedTestnamesOutlab = sendTestNames;
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }
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
                            if (!GlobalClass.isNull(outlabdetails_outLabs.get(i).getName())) {
                                name = outlabdetails_outLabs.get(i).getName().toLowerCase();
                            }
                            if (!GlobalClass.isNull(outlabdetails_outLabs.get(i).getCode())) {
                                code = outlabdetails_outLabs.get(i).getCode().toLowerCase();
                            }
                            if (!GlobalClass.isNull(outlabdetails_outLabs.get(i).getProduct())) {
                                product = outlabdetails_outLabs.get(i).getProduct().toLowerCase();
                            }
                            if (text.contains(s1) || (name != null && name.contains(s1)) ||
                                    (code != null && code.contains(s1)) ||
                                    (product != null && product.contains(s1))) {
                                String testname = outlabdetails_outLabs.get(i).getName();
                                filteredFiles.add(outlabdetails_outLabs.get(i));
                            }

                            OutLabAdapter outLabRecyclerView = new OutLabAdapter(OutLabTestsActivity.this, filteredFiles);
                            outlab_list.setAdapter(outLabRecyclerView);
                        }
                    }
                }

            }
        });

    }


    private void initViews() {
        outlab_list = (RecyclerView) findViewById(R.id.outlab_list);
        outlabtestsearch = (EditText) findViewById(R.id.outlabtestsearch);
        next_btn = (Button) findViewById(R.id.next_btn);
        show_selected_tests_list_test_ils = (TextView) findViewById(R.id.show_selected_tests_list_test_ils);
        title = (TextView) findViewById(R.id.title);
        lineargetselectedtestforILS = (LinearLayout) findViewById(R.id.lineargetselectedtestforILS);
        linearLayoutManager = new LinearLayoutManager(OutLabTestsActivity.this);
        outlab_list.setLayoutManager(linearLayoutManager);
        outlab_list.addItemDecoration(new DividerItemDecoration(OutLabTestsActivity.this, DividerItemDecoration.VERTICAL));
        outlab_list.setItemAnimator(new DefaultItemAnimator());
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);


        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");


        SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefs.getString("WOEbrand", "");
        typeName = prefs.getString("woetype", "");

        if (GlobalClass.isNull(typeName)) {
            GlobalClass.SetText(title, "Outlab Tests");
        } else {
            GlobalClass.SetText(title, "Outlab Tests(" + typeName + ")");
        }
    }

    private void getAlltTestData() {

        requestQueuepoptestILS = Volley.newRequestQueue(this);

        String url = Api.getAllTests + "" + api_key + "/ALL/getproducts";

        try {
            if (ControllersGlobalInitialiser.productListController != null) {
                ControllersGlobalInitialiser.productListController = null;
            }
            ControllersGlobalInitialiser.productListController = new ProductListController(activity, OutLabTestsActivity.this);
            ControllersGlobalInitialiser.productListController.productListingController(url, requestQueuepoptestILS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callAdapter() {
        OutLabAdapter outLabRecyclerView = new OutLabAdapter(OutLabTestsActivity.this, outlabdetails_outLabs);
        outlab_list.setAdapter(outLabRecyclerView);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getProductListResponse(JSONObject response) {
        Log.e(TAG, "onResponse: RESPONSE" + response);

        String getResponse = response.optString("RESPONSE", "");
        if (!GlobalClass.isNull(getResponse) && getResponse.equalsIgnoreCase(caps_invalidApikey)) {
            redirectToLogin(OutLabTestsActivity.this);
        } else {
            Gson gson = new Gson();
            mainModel = new MainModel();
            mainModel = gson.fromJson(response.toString(), MainModel.class);
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(OutLabTestsActivity.this);
            SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
            Gson gson22 = new Gson();
            String json22 = gson22.toJson(mainModel);
            prefsEditor1.putString("MyObject", json22);
            prefsEditor1.commit();


            GlobalClass.storeProductsCachingTime(OutLabTestsActivity.this);

            try {
                if (mainModel != null && mainModel.getB2B_MASTERS() != null && GlobalClass.CheckArrayList(mainModel.getB2B_MASTERS().getOUTLAB_TESTLIST())) {
                    outlab_testlist_getalltests = mainModel.getB2B_MASTERS().getOUTLAB_TESTLIST();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            outlabdetails_outLabs = new ArrayList<>();

            try {
                if (GlobalClass.CheckArrayList(outlab_testlist_getalltests)) {
                    for (int i = 0; i < outlab_testlist_getalltests.size(); i++) {
                        if (!GlobalClass.isNull(brandName) && !GlobalClass.isNull(outlab_testlist_getalltests.get(i).getLOCATION()) &&
                                brandName.equalsIgnoreCase(outlab_testlist_getalltests.get(i).getLOCATION())) {
                            if (GlobalClass.CheckArrayList(outlabdetails_outLabs)) {
                                outlabdetails_outLabs = null;
                                outlabdetails_outLabs = new ArrayList<>();
                                outlabdetails_outLabs = outlab_testlist_getalltests.get(i).getOutlabdetails();
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            callAdapter();
        }
    }

    private class OutLabAdapter extends RecyclerView.Adapter<OutLabAdapter.ViewHolder> {
        Context context;
        ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs;

        public OutLabAdapter(OutLabTestsActivity outLabTestsActivity, ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs) {
            this.context = outLabTestsActivity;
            this.outlabdetails_outLabs = outlabdetails_outLabs;
        }

        @NonNull
        @Override
        public OutLabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.outlab_activity, parent, false);
            OutLabAdapter.ViewHolder vh = new OutLabAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final OutLabAdapter.ViewHolder holder, final int position) {
            GlobalClass.SetText(holder.outlab_test, outlabdetails_outLabs.get(position).getName());
            holder.checked.setVisibility(View.GONE);
            holder.un_check.setVisibility(View.VISIBLE);

            if (GlobalClass.CheckArrayList(Selcted_Outlab_Test)){
                for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
                    if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getName()) &&
                            !GlobalClass.isNull(Selcted_Outlab_Test.get(i).getName()) &&
                            outlabdetails_outLabs.get(position).getName().equalsIgnoreCase(Selcted_Outlab_Test.get(i).getName())) {
                        holder.checked.setVisibility(View.VISIBLE);
                        holder.un_check.setVisibility(View.GONE);
                    }
                }
            }


            holder.parent_ll.setTag(outlabdetails_outLabs.get(position));
            holder.parent_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.un_check.getVisibility() == View.VISIBLE) {

                        if (!GlobalClass.isNull(brandName)&& brandName.equals("TTL-OTHERS")) {
                            if (GlobalClass.CheckArrayList(Selcted_Outlab_Test)) {
                                Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                                notifyDataSetChanged();
                            } else {
                                Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                            }

                            if (GlobalClass.CheckArrayList(showTestNmaes)) {
                                showTestNmaes.add(outlabdetails_outLabs.get(position).getName());
                                notifyDataSetChanged();
                            } else {
                                showTestNmaes.add(outlabdetails_outLabs.get(position).getName());
                            }

                        } else {
                            showTestNmaes.add(outlabdetails_outLabs.get(position).getName());
                            Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                        }

                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                        holder.checked.setVisibility(View.VISIBLE);
                        holder.un_check.setVisibility(View.GONE);

                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);

                        GlobalClass.SetText(show_selected_tests_list_test_ils, "");

                        String displayslectedtest = TextUtils.join(",", showTestNmaes);

                        GlobalClass.SetText(show_selected_tests_list_test_ils, displayslectedtest);
                    } else {
                        Selcted_Outlab_Test.remove(outlabdetails_outLabs.get(position));
                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                        showTestNmaes.remove(outlabdetails_outLabs.get(position).getName().toString());
                        holder.un_check.setVisibility(View.VISIBLE);
                        holder.checked.setVisibility(View.GONE);
                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                        GlobalClass.SetText(show_selected_tests_list_test_ils, "");
                        String displayslectedtest = TextUtils.join(",", showTestNmaes);
                        GlobalClass.SetText(show_selected_tests_list_test_ils, displayslectedtest);
                        if (!GlobalClass.CheckArrayList(showTestNmaes)) {
                            lineargetselectedtestforILS.setVisibility(View.GONE);
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
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView outlab_test;
            ImageView un_check, checked;
            LinearLayout parent_ll;

            public ViewHolder(View itemView) {
                super(itemView);
                outlab_test = (TextView) itemView.findViewById(R.id.outlab_test);
                un_check = (ImageView) itemView.findViewById(R.id.check);
                checked = (ImageView) itemView.findViewById(R.id.checked);
                parent_ll = (LinearLayout) itemView.findViewById(R.id.parent_ll);
            }
        }
    }
}
