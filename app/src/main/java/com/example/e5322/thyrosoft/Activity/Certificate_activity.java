package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.Adapter.CertificateApdater;
import com.example.e5322.thyrosoft.Controller.GetCertificateController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CertificateRequestModel;
import com.example.e5322.thyrosoft.Models.CertificateResponseModel;
import com.example.e5322.thyrosoft.R;

public class Certificate_activity extends AppCompatActivity {


    RecyclerView rc_certificate;
    ConnectionDetector connectionDetector;
    Activity activity;
    private String sorce_CODE;
    SharedPreferences prefs;
    TextView tv_noRecordFound;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate_activity);

        initui();
        Navigation();
        CallApi();
    }


    private void Navigation() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Certificates");
     /*   mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    private void initui() {
        activity = this;
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        rc_certificate = findViewById(R.id.rc_certificate);
        tv_noRecordFound = findViewById(R.id.tv_noRecordFound);
        connectionDetector = new ConnectionDetector(activity);
        sorce_CODE = prefs.getString("USER_CODE", "");

    }

    private void CallApi() {
        CertificateRequestModel certificateRequestModel = new CertificateRequestModel();
        certificateRequestModel.setSource_code("" + sorce_CODE);
        if (connectionDetector.isConnectingToInternet()) {
            GetCertificateController getCertificateController = new GetCertificateController(this);
            getCertificateController.CallAPI(certificateRequestModel);
        } else {
            Toast.makeText(this, MessageConstants.CHECK_INTERNET_CONN, Toast.LENGTH_SHORT).show();
        }
    }

    public void getResponse(CertificateResponseModel responseModel) {

        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {
            rc_certificate.setVisibility(View.VISIBLE);
            tv_noRecordFound.setVisibility(View.GONE);
            if (GlobalClass.CheckArrayList(responseModel.getCertificatedtl())) {
                CertificateApdater certificateApdater = new CertificateApdater(Certificate_activity.this, responseModel.getCertificatedtl());
                rc_certificate.setAdapter(certificateApdater);
                certificateApdater.notifyDataSetChanged();
            }

        } else {
            rc_certificate.setVisibility(View.GONE);
            tv_noRecordFound.setVisibility(View.VISIBLE);
        }

    }
}