package com.example.e5322.thyrosoft.Cliso_BMC;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Cliso_BMC.Models.BMC_BaseModel;
import com.example.e5322.thyrosoft.Models.MyPojo;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.ProductWithBarcode;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BMC_SelectSampleTypeActivity extends AppCompatActivity {

    ArrayList<ScannedBarcodeDetails> BMC_TTLBarcodeDetailsList;
    ArrayList<ScannedBarcodeDetails> BMC_TTLOthersBarcodeDetailsList;
    ArrayList<ScannedBarcodeDetails> BMC_FinalBarcodeDetailsList;
    TextView title;
    Button next;
    ImageView back;
    ImageView home;
    ArrayList<BMC_BaseModel> selctedTest, TTLOthersSelectedList;
    ArrayList<String> setSpecimenTypeCodes;
    SharedPreferences prefe, prefs;
    String brandName, typeName;
    TextView show_selected_tests_data, tv_ttl;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    RecyclerView rec_sample_type_ttlothers, rec_sample_type_ttl;
    BMC_SelectSampleTypeTTLOthersAdapter bmc_selectSampleTypeTTLOthersAdapter;
    BMC_SelectSampleTypeTTLAdapter bmc_selectSampleTypeTTLAdapter;
    ScannedBarcodeDetails scannedBarcodeDetails;
    private MyPojo myPojo;
    private ArrayList<String> temparraylist;
    private ArrayList<ProductWithBarcode> getproductDetailswithBarcodes;
    private String getAmount;
    private String testsnames;
    private String user, passwrd, access, api_key;
    private String TAG = BMC_SelectSampleTypeActivity.this.getClass().getSimpleName();
    private Global globalClass;
    private String testsCodesPassingToProduct;
    private ArrayList<String> locationlist;
    private boolean sampleTypeNULL = false;
    private AlertDialog.Builder alertDialogBuilder;
    ArrayList<String> getTestNameToPAss;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmc_select_sample_type);

        rec_sample_type_ttlothers = (RecyclerView) findViewById(R.id.rec_sample_type_ttlothers);
        rec_sample_type_ttl = (RecyclerView) findViewById(R.id.rec_sample_type_ttl);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        tv_ttl = (TextView) findViewById(R.id.tv_ttl);
        title = (TextView) findViewById(R.id.title);
        next = (Button) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        prefe = getSharedPreferences("savePatientDetails", MODE_PRIVATE);
        brandName = prefe.getString("WOEbrand", null);
        typeName = prefe.getString("woetype", null);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        title.setText("Select sample types");

        BMC_TTLBarcodeDetailsList = new ArrayList<>();
        BMC_TTLOthersBarcodeDetailsList = new ArrayList<>();
        BMC_FinalBarcodeDetailsList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        selctedTest = bundle.getParcelableArrayList("key");

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        SharedPreferences appSharedPrefsbtech = PreferenceManager.getDefaultSharedPreferences(BMC_SelectSampleTypeActivity.this);
        Gson gsonbtech = new Gson();
        String jsonbtech = appSharedPrefsbtech.getString("getBtechnames", "");
        myPojo = gsonbtech.fromJson(jsonbtech, MyPojo.class);

        getTestNameToPAss = new ArrayList<>();
        getAmount = bundle.getString("payment");
        testsnames = bundle.getString("writeTestName");
        getTestNameToPAss = bundle.getStringArrayList("TestCodesPass");
        testsCodesPassingToProduct = TextUtils.join(",", getTestNameToPAss);

        show_selected_tests_data.setText(testsnames);
        System.out.println("" + selctedTest.toString());
        prefs = getSharedPreferences("showSelectedTest", MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(BMC_SelectSampleTypeActivity.this);
        rec_sample_type_ttlothers.setLayoutManager(linearLayoutManager);

        linearLayoutManager1 = new LinearLayoutManager(BMC_SelectSampleTypeActivity.this);
        rec_sample_type_ttl.setLayoutManager(linearLayoutManager1);

        temparraylist = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BMC_SelectSampleTypeActivity.this, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        });

        //TODO display TTL test sample types
        if (selctedTest != null) {
            getproductDetailswithBarcodes = new ArrayList<>();
            for (int i = 0; i < selctedTest.size(); i++) {
                if (!selctedTest.get(i).getLocation().equalsIgnoreCase("TTL-OTHERS")) {
                    for (int j = 0; j < selctedTest.get(i).getBarcodes().length; j++) {
                        ProductWithBarcode productWithBarcode = new ProductWithBarcode();
                        temparraylist.add(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                        productWithBarcode.setBarcode(selctedTest.get(i).getBarcodes()[j].getSpecimen_type());
                        productWithBarcode.setProduct(selctedTest.get(i).getBarcodes()[j].getCode());
                        getproductDetailswithBarcodes.add(productWithBarcode);
                    }
                }
            }
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(temparraylist);
        temparraylist.clear();
        temparraylist.addAll(hs);

        for (int i = 0; i < temparraylist.size(); i++) {
            ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
            setSpecimenTypeCodes = new ArrayList<>();
            for (int j = 0; j < getproductDetailswithBarcodes.size(); j++) {
                if (temparraylist.get(i).equalsIgnoreCase(getproductDetailswithBarcodes.get(j).getBarcode())) {
                    setSpecimenTypeCodes.add(getproductDetailswithBarcodes.get(j).getProduct());
                }
            }
            scannedBarcodeDetails.setSpecimen_type(temparraylist.get(i));
            for (int j = 0; j < setSpecimenTypeCodes.size(); j++) {
                HashSet<String> listToSet = new HashSet<String>(setSpecimenTypeCodes);
                List<String> listWithoutDuplicates = new ArrayList<String>(listToSet);
                String setProducts = TextUtils.join(",", listWithoutDuplicates);
                HashSet<String> test = new HashSet<String>(Arrays.asList(setProducts.split(",")));
                String setFinalProducts = TextUtils.join(",", test);
                scannedBarcodeDetails.setProducts(setFinalProducts);
                scannedBarcodeDetails.setLocation("TTL");
            }
            BMC_TTLBarcodeDetailsList.add(scannedBarcodeDetails);
        }

        if (BMC_TTLBarcodeDetailsList.size() > 0)
            tv_ttl.setVisibility(View.VISIBLE);
        else
            tv_ttl.setVisibility(View.GONE);

        bmc_selectSampleTypeTTLAdapter = new BMC_SelectSampleTypeTTLAdapter(BMC_SelectSampleTypeActivity.this, BMC_TTLBarcodeDetailsList);
        rec_sample_type_ttl.setAdapter(bmc_selectSampleTypeTTLAdapter);

        //TODO display TTL-OTHERS test sample types
        locationlist = new ArrayList<>();
        TTLOthersSelectedList = new ArrayList<>();
        for (int i = 0; i < selctedTest.size(); i++) {
            locationlist.add(selctedTest.get(i).getLocation());
            if (selctedTest.get(i).getLocation().equalsIgnoreCase("TTL-OTHERS")) {
                TTLOthersSelectedList.add(selctedTest.get(i));
            }
        }

        if (BMC_TTLOthersBarcodeDetailsList != null) {
            BMC_TTLOthersBarcodeDetailsList = null;
        }
        BMC_TTLOthersBarcodeDetailsList = new ArrayList<>();
        for (int i = 0; i < TTLOthersSelectedList.size(); i++) {
            ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
            scannedBarcodeDetails.setLocation("TTL-OTHERS");
            scannedBarcodeDetails.setSpecimen_type("");
            scannedBarcodeDetails.setProducts(TTLOthersSelectedList.get(i).getProduct());
            BMC_TTLOthersBarcodeDetailsList.add(scannedBarcodeDetails);
        }

        bmc_selectSampleTypeTTLOthersAdapter = new BMC_SelectSampleTypeTTLOthersAdapter(BMC_SelectSampleTypeActivity.this, TTLOthersSelectedList, BMC_TTLOthersBarcodeDetailsList);
        bmc_selectSampleTypeTTLOthersAdapter.setOnItemClickListener(new BMC_SelectSampleTypeTTLOthersAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(String product, String sample_type) {
                for (int i = 0; i < BMC_TTLOthersBarcodeDetailsList.size(); i++) {
                    if (BMC_TTLOthersBarcodeDetailsList.get(i).getProducts().equalsIgnoreCase(product)) {
                        BMC_TTLOthersBarcodeDetailsList.get(i).setSpecimen_type(sample_type);
                    }
                }
            }
        });
        rec_sample_type_ttlothers.setAdapter(bmc_selectSampleTypeTTLOthersAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTestNames = show_selected_tests_data.getText().toString();
                for (int i = 0; i < BMC_TTLOthersBarcodeDetailsList.size(); i++) {
                    if (BMC_TTLOthersBarcodeDetailsList.get(i).getSpecimen_type() == null || BMC_TTLOthersBarcodeDetailsList.get(i).getSpecimen_type().isEmpty()) {
                        sampleTypeNULL = true;
                    }
                }

                if (sampleTypeNULL) {
                    sampleTypeNULL = false;
                    alertDialogBuilder = new AlertDialog.Builder(BMC_SelectSampleTypeActivity.this);
                    alertDialogBuilder
                            .setMessage(ToastFile.select_sample_type)
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    if (BMC_FinalBarcodeDetailsList != null) {
                        BMC_FinalBarcodeDetailsList = null;
                    }
                    BMC_FinalBarcodeDetailsList = new ArrayList<>();
                    for (int i = 0; i < BMC_TTLBarcodeDetailsList.size(); i++) {
                        scannedBarcodeDetails = new ScannedBarcodeDetails();
                        scannedBarcodeDetails.setLocation(BMC_TTLBarcodeDetailsList.get(i).getLocation());
                        scannedBarcodeDetails.setProducts(BMC_TTLBarcodeDetailsList.get(i).getProducts());
                        scannedBarcodeDetails.setSpecimen_type(BMC_TTLBarcodeDetailsList.get(i).getSpecimen_type());
                        scannedBarcodeDetails.setProducts(BMC_TTLBarcodeDetailsList.get(i).getProducts());
                        BMC_FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                    }

                    for (int i = 0; i < BMC_TTLOthersBarcodeDetailsList.size(); i++) {
                        boolean isSameSampleType = false;
                        for (int j = 0; j < BMC_FinalBarcodeDetailsList.size(); j++) {
                            if (BMC_TTLOthersBarcodeDetailsList.get(i).getSpecimen_type().equalsIgnoreCase(BMC_FinalBarcodeDetailsList.get(j).getSpecimen_type())) {
                                BMC_FinalBarcodeDetailsList.get(j).setProducts(BMC_FinalBarcodeDetailsList.get(j).getProducts() + "," + BMC_TTLOthersBarcodeDetailsList.get(i).getProducts());
                                isSameSampleType = true;
                                break;
                            }
                        }
                        if (!isSameSampleType) {
                            scannedBarcodeDetails = new ScannedBarcodeDetails();
                            scannedBarcodeDetails.setLocation(BMC_TTLOthersBarcodeDetailsList.get(i).getLocation());
                            scannedBarcodeDetails.setProducts(BMC_TTLOthersBarcodeDetailsList.get(i).getProducts());
                            scannedBarcodeDetails.setSpecimen_type(BMC_TTLOthersBarcodeDetailsList.get(i).getSpecimen_type());
                            scannedBarcodeDetails.setProducts(BMC_TTLOthersBarcodeDetailsList.get(i).getProducts());
                            BMC_FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                        }
                    }

                    ArrayList<String> getTestNameLits = new ArrayList<>();
                    for (int i = 0; i < selctedTest.size(); i++) {
                        getTestNameLits.add(selctedTest.get(i).getProduct());
                    }
                    Intent intent = new Intent(BMC_SelectSampleTypeActivity.this, BMC_Scan_BarcodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("key", selctedTest);
                    bundle.putString("payment", getAmount);
                    bundle.putString("writeTestName", selectedTestNames);
                    bundle.putStringArrayList("TestCodesPass", getTestNameLits);
                    bundle.putParcelableArrayList("FinalBarcodeList", BMC_FinalBarcodeDetailsList);
                    bundle.putString("come_from", "SelectSampleType");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}