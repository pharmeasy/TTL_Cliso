package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.CLISO_SelectSampleTypeTTLOthersAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;

import java.util.ArrayList;

public class Cliso_SelctSampleActivity extends AppCompatActivity {

    ArrayList<Outlabdetails_OutLab> Selcted_Outlab_Test = new ArrayList<>();
    ArrayList<Outlabdetails_OutLab> TTLOthersSelectedList;
    String selectedTest;
    String getTypeName;
    RecyclerView rec_sample_type_ttlothers, rec_sample_type_ttl;
    TextView show_selected_tests_data, tv_ttl;
    TextView title;
    Button next;
    ImageView back;
    ImageView home;
    SharedPreferences prefe, prefs;
    String brandName, typeName, testsnames;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<String> locationlist;
    CLISO_SelectSampleTypeTTLOthersAdapter cliso_selectSampleTypeTTLOthersAdapter;
    private boolean sampleTypeNULL = false;
    ArrayList<ScannedBarcodeDetails> BMC_TTLOthersBarcodeDetailsList;
    ArrayList<ScannedBarcodeDetails> BMC_FinalBarcodeDetailsList;
    private ArrayList<String> temparraylist;
    private AlertDialog.Builder alertDialogBuilder;
    ScannedBarcodeDetails scannedBarcodeDetails;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliso_sampletype);

        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        initUI();

        BMC_TTLOthersBarcodeDetailsList = new ArrayList<>();
        BMC_FinalBarcodeDetailsList = new ArrayList<>();

        prefe = getSharedPreferences(Constants.PREF_SAVEPATIENTDETAILS, MODE_PRIVATE);
        brandName = prefe.getString("WOEbrand", null);
        typeName = prefe.getString("woetype", null);
        title.setText("Select sample types");

        Bundle bundle = getIntent().getExtras();
        Selcted_Outlab_Test = Global.Selcted_Outlab_Test_global;
        testsnames = bundle.getString("selectedTest");
        Log.e("TAG", "test names ::: " + testsnames);
        show_selected_tests_data.setText(testsnames);

        linearLayoutManager = new LinearLayoutManager(Cliso_SelctSampleActivity.this);
        rec_sample_type_ttlothers.setLayoutManager(linearLayoutManager);

        //  temparraylist = new ArrayList<>();

        initListeners();

        locationlist = new ArrayList<>();
        TTLOthersSelectedList = new ArrayList<>();


        if (BMC_TTLOthersBarcodeDetailsList != null) {
            BMC_TTLOthersBarcodeDetailsList = null;
        }

        BMC_TTLOthersBarcodeDetailsList = new ArrayList<>();

        for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
            ScannedBarcodeDetails scannedBarcodeDetails = new ScannedBarcodeDetails();
            scannedBarcodeDetails.setLocation("TTL-OTHERS");
            scannedBarcodeDetails.setSpecimen_type("");
            scannedBarcodeDetails.setProducts(Selcted_Outlab_Test.get(i).getProduct());
            BMC_TTLOthersBarcodeDetailsList.add(scannedBarcodeDetails);
        }


        cliso_selectSampleTypeTTLOthersAdapter = new CLISO_SelectSampleTypeTTLOthersAdapter(Cliso_SelctSampleActivity.this, Selcted_Outlab_Test, BMC_TTLOthersBarcodeDetailsList);
        cliso_selectSampleTypeTTLOthersAdapter.setOnItemClickListener(new CLISO_SelectSampleTypeTTLOthersAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(String product, String sample_type) {
                for (int i = 0; i < BMC_TTLOthersBarcodeDetailsList.size(); i++) {
                    if (BMC_TTLOthersBarcodeDetailsList.get(i).getProducts().equalsIgnoreCase(product)) {
                        BMC_TTLOthersBarcodeDetailsList.get(i).setSpecimen_type(sample_type);
                    }
                }
            }
        });
        rec_sample_type_ttlothers.setAdapter(cliso_selectSampleTypeTTLOthersAdapter);

    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Cliso_SelctSampleActivity.this);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedTestNames = show_selected_tests_data.getText().toString();

                for (int i = 0; i < BMC_TTLOthersBarcodeDetailsList.size(); i++) {
                    if (BMC_TTLOthersBarcodeDetailsList.get(i).getSpecimen_type() == null || BMC_TTLOthersBarcodeDetailsList.get(i).getSpecimen_type().isEmpty()) {
                        sampleTypeNULL = true;
                    }
                }

                if (sampleTypeNULL) {
                    sampleTypeNULL = false;
                    alertDialogBuilder = new AlertDialog.Builder(Cliso_SelctSampleActivity.this);
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
                    for (int i = 0; i < Selcted_Outlab_Test.size(); i++) {
                        getTestNameLits.add(Selcted_Outlab_Test.get(i).getProduct());
                    }
                    Intent intent = new Intent(Cliso_SelctSampleActivity.this, Scan_Barcode_Outlabs_Activity.class);
                    Bundle bundle = new Bundle();

                    Global.Selcted_Outlab_Test_global= Selcted_Outlab_Test;
                    // bundle.putString("payment", getAmount);
                    bundle.putString("writeTestName", selectedTestNames);
                    bundle.putStringArrayList("TestCodesPass", getTestNameLits);
                    Global.FinalBarcodeDetailsList_global=BMC_FinalBarcodeDetailsList;
//                    bundle.putParcelableArrayList("FinalBarcodeList", BMC_FinalBarcodeDetailsList);
                    bundle.putString("come_from", "SelectSampleType");
                    intent.putExtras(bundle);
                    startActivity(intent);

                }


            }
        });

    }


    private void initUI() {
        rec_sample_type_ttlothers = (RecyclerView) findViewById(R.id.rec_sample_type_ttlothers);
        show_selected_tests_data = (TextView) findViewById(R.id.show_selected_tests_data);
        title = (TextView) findViewById(R.id.title);
        next = (Button) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
    }
}
