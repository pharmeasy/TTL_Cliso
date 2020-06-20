package com.example.e5322.thyrosoft.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Adapter.LeadSampleTypeAdapter;
import com.example.e5322.thyrosoft.Adapter.SingalLeadAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.WOE.ScanBarcodeLeadId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MultipleLeadActivity extends AppCompatActivity {
    String user, typeName, passwrd, access, api_key, srnostr, sub_source_code_string, getLeadId, leadAddress, leadAGE, leadAGE_TYPE, leadBCT, leadEDTA, leadEMAIL, leadERROR, leadFLUORIDE, leadGENDER, leadHEPARIN;
    String leadLAB_ID, leadLAB_NAME, leadLEAD_ID, leadMOBILE, leadNAME, leadORDER_NO, leadPACKAGE, leadPINCODE, leadPRODUCT, leadRATE;
    String leadREF_BY, leadRESPONSE, SR_NO, brandtype, leadSAMPLE_TYPE, leadSCT, leadSERUM, leadTESTS, leadTYPE, leadURINE, leadWATER, leadleadData, fromcome;
    ArrayList<ScannedBarcodeDetails> SingleBarcodeDetailsList;
    ArrayList<ScannedBarcodeDetails> MultileBarcodeDetailsList;
    ArrayList<ScannedBarcodeDetails> FinalBarcodeDetailsList;
    RecyclerView rec_sample_type_singal, rec_sample_type_multi;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    Leads.LeadData[] nameList;
    List<Leads.LeadData> list;
    List<Leads.LeadData> templist;
    LeadOrderIdMainModel leadOrderIdMainModel;
    ScannedBarcodeDetails scannedBarcodeDetails;
    SingalLeadAdapter singalLeadAdapter;
    LeadSampleTypeAdapter leadSampleTypeAdapter;
    Button next;
    TextView show_selected_tests_data;
    ImageView back, home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_multiplelead);

        SingleBarcodeDetailsList = new ArrayList<>();
        MultileBarcodeDetailsList = new ArrayList<>();
        FinalBarcodeDetailsList = new ArrayList<>();

        fromcome = getIntent().getExtras().getString("fromcome", "");
        leadOrderIdMainModel = (LeadOrderIdMainModel) getIntent().getParcelableExtra("MyClass");

        SharedPreferences sharedPreferences = getSharedPreferences("LeadOrderID", MODE_PRIVATE);

        if (fromcome.equalsIgnoreCase("woepage")) {
            brandtype = sharedPreferences.getString(" brandtype", null);
            leadAddress = sharedPreferences.getString("ADDRESS", null);
            leadAGE = sharedPreferences.getString("AGE", null);
            leadAGE_TYPE = sharedPreferences.getString("AGE_TYPE", null);
            leadBCT = sharedPreferences.getString("BCT", null);
            leadEDTA = sharedPreferences.getString("EDTA", null);
            leadEMAIL = sharedPreferences.getString("EMAIL", null);
            leadERROR = sharedPreferences.getString("ERROR", null);
            leadFLUORIDE = sharedPreferences.getString("FLUORIDE", null);
            leadGENDER = sharedPreferences.getString("GENDER", null);
            leadHEPARIN = sharedPreferences.getString("HEPARIN", null);

            leadLAB_ID = sharedPreferences.getString("LAB_ID", null);
            leadLAB_NAME = sharedPreferences.getString("LAB_NAME", null);
            leadLEAD_ID = sharedPreferences.getString("LEAD_ID", null);
            leadMOBILE = sharedPreferences.getString("MOBILE", null);
            leadNAME = sharedPreferences.getString("NAME", null);
            leadORDER_NO = sharedPreferences.getString("ORDER_NO", null);
            leadPACKAGE = sharedPreferences.getString("PACKAGE", null);
            leadPINCODE = sharedPreferences.getString("PINCODE", null);
            leadPRODUCT = sharedPreferences.getString("PRODUCT", null);
            leadRATE = sharedPreferences.getString("RATE", null);

            leadREF_BY = sharedPreferences.getString("REF_BY", null);
            leadRESPONSE = sharedPreferences.getString("RESPONSE", null);
            leadSAMPLE_TYPE = sharedPreferences.getString("SAMPLE_TYPE", null);
            leadSCT = sharedPreferences.getString("SCT", null);
            leadSERUM = sharedPreferences.getString("SERUM", null);
            leadTESTS = sharedPreferences.getString("TESTS", null);
            leadTYPE = sharedPreferences.getString("TYPE", null);
            leadURINE = sharedPreferences.getString("URINE", null);
            leadWATER = sharedPreferences.getString("WATER", null);
            leadleadData = sharedPreferences.getString("leadData", null);
            SR_NO = sharedPreferences.getString("SR_NO", null);
        } else {
            brandtype = getIntent().getStringExtra("brandtype");
            leadSAMPLE_TYPE = getIntent().getStringExtra("SAMPLE_TYPE");
            leadLEAD_ID = getIntent().getStringExtra("LeadID");
            leadTESTS = getIntent().getStringExtra("TESTS");
            leadSCT = getIntent().getStringExtra("SCT");
            SR_NO = getIntent().getStringExtra("SR_NO");
            leadleadData = getIntent().getStringExtra("leadData");
        }

        rec_sample_type_singal = findViewById(R.id.rec_sample_type_ttl);
        rec_sample_type_multi = findViewById(R.id.rec_sample_type_ttlothers);

        linearLayoutManager = new LinearLayoutManager(MultipleLeadActivity.this);
        rec_sample_type_singal.setLayoutManager(linearLayoutManager);

        linearLayoutManager1 = new LinearLayoutManager(MultipleLeadActivity.this);
        rec_sample_type_multi.setLayoutManager(linearLayoutManager1);


        next = findViewById(R.id.next);
        show_selected_tests_data = findViewById(R.id.show_selected_tests_data);
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.goToHome(MultipleLeadActivity.this);
            }
        });

        //TODO This is LeadData Array
        Gson gson = new Gson();
        nameList = gson.fromJson(leadleadData, Leads.LeadData[].class);
        list = Arrays.asList(nameList);
        leadleadData = "";
        leadSAMPLE_TYPE = "";

        leadTESTS = "";
        for (int i = 0; i < list.size(); i++) {
            leadTESTS += list.get(i).getTest() + ",";
        }

        try {
            if (leadTESTS.endsWith(",")) {
                leadTESTS = leadTESTS.substring(0, leadTESTS.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        show_selected_tests_data.setText(leadTESTS);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSample_type().length == 1) {
                    scannedBarcodeDetails = new ScannedBarcodeDetails();
                    scannedBarcodeDetails.setProducts(list.get(i).getTest());
                    scannedBarcodeDetails.setSpecimen_type(list.get(i).getSample_type()[0].getOutlab_sampletype());
                    SingleBarcodeDetailsList.add(scannedBarcodeDetails);
                } else {
                    scannedBarcodeDetails = new ScannedBarcodeDetails();
                    scannedBarcodeDetails.setProducts(list.get(i).getTest());
                    MultileBarcodeDetailsList.add(scannedBarcodeDetails);
                }
            }
        }


        if (SingleBarcodeDetailsList != null && SingleBarcodeDetailsList.size() > 0) {
            rec_sample_type_singal.setVisibility(View.VISIBLE);
            singalLeadAdapter = new SingalLeadAdapter(MultipleLeadActivity.this, SingleBarcodeDetailsList);
            rec_sample_type_singal.setAdapter(singalLeadAdapter);
        } else {
            rec_sample_type_singal.setVisibility(View.GONE);
        }

        if (MultileBarcodeDetailsList != null && MultileBarcodeDetailsList.size() > 0) {
            rec_sample_type_multi.setVisibility(View.VISIBLE);
            templist=new ArrayList<>();

            if (list!=null && list.size()>0){
                for (int i = 0; i <list.size() ; i++) {
                    for(int j=0;j<MultileBarcodeDetailsList.size();j++){
                        if (list.get(i).getTest().equalsIgnoreCase(MultileBarcodeDetailsList.get(j).getProducts())){
                            templist.add(list.get(i));
                        }
                    }
                }
            }

            leadSampleTypeAdapter = new LeadSampleTypeAdapter(MultipleLeadActivity.this, templist);
            leadSampleTypeAdapter.setOnItemClickListener(new LeadSampleTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemSelected(String product, String sample_type, int position) {
                    for (int i = 0; i < MultileBarcodeDetailsList.size(); i++) {
                        if (MultileBarcodeDetailsList.get(i).getProducts().equalsIgnoreCase(product)) {
                            MultileBarcodeDetailsList.get(i).setSpecimen_type(sample_type);
                        }
                    }
                }
            });
            rec_sample_type_multi.setAdapter(leadSampleTypeAdapter);
        } else {
            rec_sample_type_multi.setVisibility(View.GONE);
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sampleTypeNULL = false;

                for (int i = 0; i < MultileBarcodeDetailsList.size(); i++) {
                    if (MultileBarcodeDetailsList.get(i).getSpecimen_type() == null || MultileBarcodeDetailsList.get(i).getSpecimen_type().isEmpty()) {
                        sampleTypeNULL = true;
                    }
                }

                if (sampleTypeNULL) {
                    sampleTypeNULL = false;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MultipleLeadActivity.this);
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
                    if (FinalBarcodeDetailsList != null) {
                        FinalBarcodeDetailsList = null;
                    }
                    FinalBarcodeDetailsList = new ArrayList<>();
                    for (int i = 0; i < SingleBarcodeDetailsList.size(); i++) {
                        if (SingleBarcodeDetailsList.get(i).getProducts().contains("PPBS") || SingleBarcodeDetailsList.get(i).getProducts().contains("RBS") || SingleBarcodeDetailsList.get(i).getProducts().contains("FBS")) {
                            List<String> fluriodetest = Arrays.asList(SingleBarcodeDetailsList.get(i).getProducts().split(","));
                            if (fluriodetest.contains("PPBS") && fluriodetest.contains("RBS")) {
                                fluriodetest.remove("RBS");
                            }
                            for (int k = 0; k < fluriodetest.size(); k++) {
                                scannedBarcodeDetails = new ScannedBarcodeDetails();
                                scannedBarcodeDetails.setLocation(SingleBarcodeDetailsList.get(i).getLocation());
                                scannedBarcodeDetails.setProducts(fluriodetest.get(k));
                                scannedBarcodeDetails.setSpecimen_type(SingleBarcodeDetailsList.get(i).getSpecimen_type().trim());
                                FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                            }
                        } else {
                            scannedBarcodeDetails = new ScannedBarcodeDetails();
                            scannedBarcodeDetails.setLocation(SingleBarcodeDetailsList.get(i).getLocation());
                            scannedBarcodeDetails.setProducts(SingleBarcodeDetailsList.get(i).getProducts());
                            scannedBarcodeDetails.setSpecimen_type(SingleBarcodeDetailsList.get(i).getSpecimen_type());
                            scannedBarcodeDetails.setProducts(SingleBarcodeDetailsList.get(i).getProducts());
                            FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                        }

                    }

                    for (int i = 0; i < MultileBarcodeDetailsList.size(); i++) {
                        boolean isSameSampleType = false;
                        for (int j = 0; j < FinalBarcodeDetailsList.size(); j++) {
                            if (MultileBarcodeDetailsList.get(i).getSpecimen_type().equalsIgnoreCase(FinalBarcodeDetailsList.get(j).getSpecimen_type())) {
                                FinalBarcodeDetailsList.get(j).setProducts(FinalBarcodeDetailsList.get(j).getProducts() + "," + MultileBarcodeDetailsList.get(i).getProducts());
                                isSameSampleType = true;
                                break;
                            }
                        }

                        if (!isSameSampleType) {
                            scannedBarcodeDetails = new ScannedBarcodeDetails();
                            scannedBarcodeDetails.setLocation(MultileBarcodeDetailsList.get(i).getLocation());
                            scannedBarcodeDetails.setProducts(MultileBarcodeDetailsList.get(i).getProducts());
                            scannedBarcodeDetails.setSpecimen_type(MultileBarcodeDetailsList.get(i).getSpecimen_type());
                            scannedBarcodeDetails.setProducts(MultileBarcodeDetailsList.get(i).getProducts());
                            FinalBarcodeDetailsList.add(scannedBarcodeDetails);
                        }
                    }

                    Intent i = new Intent(MultipleLeadActivity.this, ScanBarcodeLeadId.class);
                    i.putExtra("MyClass", leadOrderIdMainModel);
                    i.putExtra("LeadID", leadLEAD_ID);
                    i.putExtra("SAMPLE_TYPE", leadSAMPLE_TYPE);
                    i.putExtra("fromcome", "multiple");
                    i.putExtra("TESTS", leadTESTS);
                    i.putExtra("SCT", leadSCT);
                    i.putExtra("SR_NO", SR_NO);
                    i.putExtra("leadData", leadleadData);
                    i.putExtra("FinalBarcodeDetailsList", FinalBarcodeDetailsList);
                    startActivity(i);
                }
            }
        });
    }


}

