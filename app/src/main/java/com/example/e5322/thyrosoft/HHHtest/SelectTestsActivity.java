package com.example.e5322.thyrosoft.HHHtest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.Controller.GetTests;
import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SelectTestsActivity extends AppCompatActivity {

    Button btn_submit;
    Spinner spr_test;
    ImageView back, home;
    ConnectionDetector cd;
    GetTestResponseModel getTestResponseModel;
    AppPreferenceManager appPreferenceManager;
    List<String> testlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tests);

        btn_submit = findViewById(R.id.btn_submit);
        appPreferenceManager = new AppPreferenceManager(this);
        spr_test = findViewById(R.id.spr_test);
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        cd = new ConnectionDetector(this);
        CallGetTests();


        spr_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(SelectTestsActivity.this, "Kindly Select the test to Proceed..", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(SelectTestsActivity.this, HHHMainActivity.class);
                    i.putExtra("Name", spr_test.getSelectedItem().toString());
                    Global.HHHTest = spr_test.getSelectedItem().toString();
                    spr_test.setSelection(0);
                    startActivity(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                finish();

            }
        });


    }

    private void CallGetTests() {

        if (GlobalClass.checkSync(this, "POCT")) {
            if (cd.isConnectingToInternet()) {
                GetTests getTests = new GetTests(this);
                getTests.GetTests();
            } else {
                Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }else {
            SetValues();

        }

    }

    public void getresponse(GetTestResponseModel body) {
        try {

            if (!GlobalClass.isNull(body.getResId()) && body.getResId().equalsIgnoreCase("RES0000")) {
                GlobalClass.storeCachingDate(this,"POCT");
                appPreferenceManager.setTestResponseModel(body);
                SetValues();
            } else {
                Toast.makeText(this, "" + body.getResponse(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            GlobalClass.ShowError(this, "Server Error", "Kindly try after some time.", false);
            e.printStackTrace();
        }
    }

    private void SetValues() {
        if (appPreferenceManager.getTestResponseModel()!=null){
            getTestResponseModel = appPreferenceManager.getTestResponseModel();
            final HashSet<String> hashSet = new HashSet<String>();
            if (getTestResponseModel.getPoctHHHData() != null) {
                if (getTestResponseModel.getPoctHHHData() != null && getTestResponseModel.getPoctHHHData().size() > 0) {
                    for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                        testlist.add(getTestResponseModel.getPoctHHHData().get(i).getDisplay());
                        hashSet.addAll(testlist);
                        testlist.clear();
                        testlist.addAll(hashSet);
                    }
                    testlist.add(0, "Select POCT");
                    ArrayAdapter<String> adap = new ArrayAdapter<String>(this, R.layout.name_age_spinner, testlist);
                    adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spr_test.setAdapter(adap);
                    spr_test.setSelection(0);
                }

            }
        }else {
            if (cd.isConnectingToInternet()) {
                GetTests getTests = new GetTests(this);
                getTests.GetTests();
            } else {
                Toast.makeText(this, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}