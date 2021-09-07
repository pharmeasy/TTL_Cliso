package com.example.e5322.thyrosoft.WOE;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.Cliso_SelctSampleActivity;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.OUTLAB_TESTLIST_GETALLTESTS;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    int days = 0;
    ProgressDialog barProgressDialog;
    private SharedPreferences prefsavedetails;
    private String getTypeName;
    private ImageView back;
    private ImageView home;
    private String source_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_lab_tests);
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
        source_type = prefs.getString("SOURCE_TYPE", "");


        SharedPreferences prefs = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefs.getString("WOEbrand", "");
        typeName = prefs.getString("woetype", "");

        if (typeName == null) {
            title.setText("Outlab Tests");
        } else {
            title.setText("Outlab Tests(" + typeName + ")");
        }


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

        // SharedPreferences prefs = getSharedPreferences("getBrandTypeandName", MODE_PRIVATE);
        prefsavedetails = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefsavedetails.getString("WOEbrand", null);
        typeName = prefsavedetails.getString("typeName", null);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (show_selected_tests_list_test_ils.getText().toString().equals("")) {
                    Toast.makeText(OutLabTestsActivity.this, ToastFile.slt_test, Toast.LENGTH_SHORT).show();
                } else {
                    if (Selcted_Outlab_Test != null) {


                     /*   if (brandName.equalsIgnoreCase("TTL-OTHERS")) {
                            if (!source_type.equalsIgnoreCase("OLC")) {
                                if (Global.OTPVERIFIED) {
                                    isNhlAvailable = 0;
                                } else {
                                    isNhlAvailable = getValueFromSelectedList(Selcted_Outlab_Test);
                                }
                            } else {
                                isNhlAvailable = getValueFromSelectedList(Selcted_Outlab_Test);
                            }
                        }*/
                        if (brandName.equalsIgnoreCase("TTL-OTHERS")) {
                            String sendTestNames = show_selected_tests_list_test_ils.getText().toString();
                            if (Selcted_Outlab_Test.size() == 1) {
                                intent = new Intent(OutLabTestsActivity.this, Scan_Barcode_Outlabs.class);
                                Bundle bundle = new Bundle();
                                Log.e(TAG, "onClick: " + Selcted_Outlab_Test.size());
                                Global.Selcted_Outlab_Test_global = Selcted_Outlab_Test;
                                // bundle.putParcelableArrayList("getOutlablist", Selcted_Outlab_Test);
                                bundle.putString("selectedTest", sendTestNames);
                                bundle.putString("getTypeName", getTypeName);
                                GlobalClass.selectedTestnamesOutlab = sendTestNames;
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OutLabTestsActivity.this, "Cannot select more than 1 test", Toast.LENGTH_LONG).show();
                            }

                        }else {
                            String sendTestNames = show_selected_tests_list_test_ils.getText().toString();
                            if (Selcted_Outlab_Test.size() == 1) {
                                intent = new Intent(OutLabTestsActivity.this, Scan_Barcode_Outlabs.class);
                            } else {
                                intent = new Intent(OutLabTestsActivity.this, Cliso_SelctSampleActivity.class);
                            }
                            Bundle bundle = new Bundle();
                            Log.e(TAG, "onClick: " + Selcted_Outlab_Test.size());
                            Global.Selcted_Outlab_Test_global = Selcted_Outlab_Test;
                            // bundle.putParcelableArrayList("getOutlablist", Selcted_Outlab_Test);
                            bundle.putString("selectedTest", sendTestNames);
                            bundle.putString("getTypeName", getTypeName);
                            GlobalClass.selectedTestnamesOutlab = sendTestNames;
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }


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
                            OutLabAdapter outLabRecyclerView = new OutLabAdapter(OutLabTestsActivity.this, filteredFiles);
                            outlab_list.setAdapter(outLabRecyclerView);
                        }
                    }
                }

            }
        });


        if (GlobalClass.syncProduct(OutLabTestsActivity.this)) {
            if (!GlobalClass.isNetworkAvailable(OutLabTestsActivity.this)) {
                TastyToast.makeText(OutLabTestsActivity.this, ToastFile.intConnection, Toast.LENGTH_SHORT, TastyToast.ERROR);
            } else {
                getAlltTestData();
            }
        } else {
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Gson gson = new Gson();
            String json = appSharedPrefs.getString("MyObject", "");
            MainModel obj = gson.fromJson(json, MainModel.class);

            if (obj != null) {
                if (obj.getB2B_MASTERS() != null && obj.getUSER_TYPE() != null) {
                    if (obj.getB2B_MASTERS().getOUTLAB_TESTLIST() != null) {
                        outlab_testlist_getalltests = obj.getB2B_MASTERS().getOUTLAB_TESTLIST();
                    }

                    outlabdetails_outLabs = new ArrayList<>();
                    if (outlab_testlist_getalltests != null) {
                        for (int i = 0; i < outlab_testlist_getalltests.size(); i++) {
                            if (brandName.equalsIgnoreCase(outlab_testlist_getalltests.get(i).getLOCATION())) {
                                if (outlabdetails_outLabs != null) {
                                    outlabdetails_outLabs = null;
                                    outlabdetails_outLabs = new ArrayList<>();
                                    outlabdetails_outLabs = outlab_testlist_getalltests.get(i).getOutlabdetails();
                                }
                                callAdapter();
                            }
                        }
                    }

                } else {
                    if (!GlobalClass.isNetworkAvailable(OutLabTestsActivity.this)) {
                        TastyToast.makeText(OutLabTestsActivity.this, ToastFile.intConnection, Toast.LENGTH_SHORT, TastyToast.ERROR);
                    } else {
                        getAlltTestData();
                    }
                }
            } else {
                if (!GlobalClass.isNetworkAvailable(OutLabTestsActivity.this)) {
                    TastyToast.makeText(OutLabTestsActivity.this, ToastFile.intConnection, Toast.LENGTH_SHORT, TastyToast.ERROR);
                } else {
                    getAlltTestData();
                }
            }
        }


    }


    private int getValueFromSelectedList(ArrayList<Outlabdetails_OutLab> selcted_Outlab_Test) {
        int value = 1;
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (selcted_Outlab_Test != null) {
            for (int i = 0; i < selcted_Outlab_Test.size(); i++) {
                arrayList.add(selcted_Outlab_Test.get(i).getIsNHL());
                if (arrayList.contains(0)) {
                    value = 0;
                }
                /*if (selcted_test.get(i).getIsNHL() == 1) {
                    value = 1;
                }*/
            }
        }
        return value;
    }

    private void getAlltTestData() {
        barProgressDialog = new ProgressDialog(OutLabTestsActivity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        //    globalClass.showProgressDialog(this);
        requestQueuepoptestILS = GlobalClass.setVolleyReq(this);
        //Log.e(TAG,"API product ----->"+Api.getAllTests + "" + api_key + "/ALL/getproducts");
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.getAllTests + "" + api_key + "/ALL/getproducts", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: RESPONSE" + response);

                String getResponse = response.optString("RESPONSE", "");
                if (getResponse.equalsIgnoreCase(caps_invalidApikey)) {
                    redirectToLogin(OutLabTestsActivity.this);
                } else {
            /*        Gson gson = new Gson();
                    mainModel = new MainModel();
                    mainModel = gson.fromJson(response.toString(), MainModel.class);*/

                    Gson gson = new Gson();
                    mainModel = new MainModel();
                    mainModel = gson.fromJson(response.toString(), MainModel.class);
                    SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(OutLabTestsActivity.this);
                    SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                    Gson gson22 = new Gson();
                    String json22 = gson22.toJson(mainModel);
                    prefsEditor1.putString("MyObject", json22);
                    prefsEditor1.commit();

                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }

                    GlobalClass.StoresyncProduct(OutLabTestsActivity.this);

                    try {
                        if (mainModel != null && mainModel.getB2B_MASTERS() != null && mainModel.getB2B_MASTERS().getOUTLAB_TESTLIST() != null) {
                            outlab_testlist_getalltests = mainModel.getB2B_MASTERS().getOUTLAB_TESTLIST();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    outlabdetails_outLabs = new ArrayList<>();

                    try {
                        if (outlab_testlist_getalltests != null) {
                            for (int i = 0; i < outlab_testlist_getalltests.size(); i++) {
                                if (brandName.equalsIgnoreCase(outlab_testlist_getalltests.get(i).getLOCATION())) {
                                    if (outlabdetails_outLabs != null) {
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
        Log.e(TAG, "getAlltTestData: URL" + jsonObjectRequestPop);

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
            holder.outlab_test.setText(outlabdetails_outLabs.get(position).getName());
            holder.checked.setVisibility(View.GONE);
            holder.un_check.setVisibility(View.VISIBLE);

            for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
                if (outlabdetails_outLabs.get(position).getName().equals(Selcted_Outlab_Test.get(i).getName())) {
                    holder.checked.setVisibility(View.VISIBLE);
                    holder.un_check.setVisibility(View.GONE);
                }
            }

            holder.parent_ll.setTag(outlabdetails_outLabs.get(position));
            holder.parent_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.un_check.getVisibility() == View.VISIBLE) {

                        if (brandName.equals("TTL-OTHERS")) {
                            if (Selcted_Outlab_Test != null && Selcted_Outlab_Test.size() > 0) {

//                                if (Selcted_Outlab_Test.size() >= 1) {
//                                    Toast.makeText(context, "Cannot select more than 1 test", Toast.LENGTH_LONG).show();
//                                } else {
                                    Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                                    notifyDataSetChanged();
//                                }

                            } else {
                                Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                            }

                            if (showTestNmaes != null && showTestNmaes.size() > 0) {

//                                if (Selcted_Outlab_Test.size() >= 1) {
//                                } else {
                                    showTestNmaes.add(outlabdetails_outLabs.get(position).getName());
                                    notifyDataSetChanged();
//                                }
                            } else {
                                showTestNmaes.add(outlabdetails_outLabs.get(position).getName());
                            }

                        } else {
                            showTestNmaes.add(outlabdetails_outLabs.get(position).getName().toString());
                            Selcted_Outlab_Test.add(outlabdetails_outLabs.get(position));
                        }

                 /*       if (brandName.equals("TTL-OTHERS")) {
                            if (Selcted_Outlab_Test.size() == 1) {
                                holder.checked.setVisibility(View.VISIBLE);
                                holder.un_check.setVisibility(View.GONE);
                            } else {
                                holder.checked.setVisibility(View.GONE);
                                holder.un_check.setVisibility(View.VISIBLE);
                                lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                                show_selected_tests_list_test_ils.setText("");
                                String displayslectedtest = TextUtils.join(",", showTestNmaes);
                                show_selected_tests_list_test_ils.setText(displayslectedtest);
                            }
                        } else {*/
                            holder.checked.setVisibility(View.VISIBLE);
                            holder.un_check.setVisibility(View.GONE);
                            lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                            show_selected_tests_list_test_ils.setText("");
                            String displayslectedtest = TextUtils.join(",", showTestNmaes);
                            show_selected_tests_list_test_ils.setText(displayslectedtest);
//                        }


                    } else {
                        Selcted_Outlab_Test.remove(outlabdetails_outLabs.get(position));
                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                        showTestNmaes.remove(outlabdetails_outLabs.get(position).getName().toString());
                        holder.un_check.setVisibility(View.VISIBLE);
                        holder.checked.setVisibility(View.GONE);
                        lineargetselectedtestforILS.setVisibility(View.VISIBLE);
                        show_selected_tests_list_test_ils.setText("");
                        String displayslectedtest = TextUtils.join(",", showTestNmaes);
                        show_selected_tests_list_test_ils.setText(displayslectedtest);
                        if (showTestNmaes == null) {
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
            // CheckBox check;
            boolean isSelectedDueToParent;
            String parentTestCode, parentTestname;
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
