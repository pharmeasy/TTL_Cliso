package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.MatrixRecyclerViewAdapter;
import com.example.e5322.thyrosoft.Controller.EscalationMatrixController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.EscalationMatrixRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.EscalationMatrixResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class EscalationMatrixActivity extends AppCompatActivity {

    ImageView back, home;
    TextView tv_no_record_found;
    RecyclerView rcv_escalation_list;
    private String user;
    private ConnectionDetector cd;
    TextView tv_whatsapp_number;
    LinearLayout ll_whatsapp, ll_whatsapp_main;
    private String wa_mobile_no = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalation_matrix);

        cd = new ConnectionDetector(this);

        initUI();
        initListeners();

        SharedPreferences prefs = getSharedPreferences(Constants.PREF_USER_DETAILS, MODE_PRIVATE);
        user = prefs.getString("Username", "");

        CallMatrixAPI();
    }

    private void initUI() {
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        tv_no_record_found = findViewById(R.id.tv_no_record_found);
        rcv_escalation_list = findViewById(R.id.rcv_escalation_list);
        tv_whatsapp_number = findViewById(R.id.tv_whatsapp_number);
        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        ll_whatsapp_main = findViewById(R.id.ll_whatsapp_main);
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.goToHome(EscalationMatrixActivity.this);
            }
        });

        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+91" + wa_mobile_no + "#"));
                startActivity(whatsappIntent);
            }
        });
    }

    private void CallMatrixAPI() {
        EscalationMatrixRequestModel requestModel = new EscalationMatrixRequestModel();
        requestModel.setClient_Code(user);
        requestModel.setType("Cliso");

        if (cd.isConnectingToInternet()) {
            EscalationMatrixController controller = new EscalationMatrixController(EscalationMatrixActivity.this);
            controller.CallEscalationMatrixAPI(requestModel);
        } else {
            GlobalClass.toastyError(this, MessageConstants.CHECK_INTERNET_CONN, false);
        }
    }

    public void getEscalationMatrixResponse(EscalationMatrixResponseModel responseModel) {
        if (responseModel != null) {
            if (GlobalClass.checkEqualIgnoreCase(responseModel.getResponseId(), Constants.RES0000)) {
                ArrayList<EscalationMatrixResponseModel.MatrixDetails> details = new ArrayList<>();
                details = responseModel.getMatrixDetails();
                wa_mobile_no = responseModel.getMobile();
                GlobalClass.SetText(tv_whatsapp_number, wa_mobile_no);
                if (GlobalClass.isArraylistNotNull(details)) {
                    tv_no_record_found.setVisibility(View.GONE);
                    rcv_escalation_list.setVisibility(View.VISIBLE);
                    ll_whatsapp_main.setVisibility(!GlobalClass.isNull(wa_mobile_no) ? View.VISIBLE : View.GONE);
                    setAdapter(details);
                } else {
                    showNoRecordFound();
                }
            } else {
                GlobalClass.showTastyToast(this, GlobalClass.checkAndGetStringValue(responseModel.getResponse(), MessageConstants.SOMETHING_WENT_WRONG), 2);
                showNoRecordFound();
            }
        } else {
            GlobalClass.showTastyToast(this, MessageConstants.SOMETHING_WENT_WRONG, 2);
            showNoRecordFound();
        }
    }

    private void showNoRecordFound() {
        tv_no_record_found.setVisibility(View.VISIBLE);
        rcv_escalation_list.setVisibility(View.GONE);
        ll_whatsapp_main.setVisibility(View.GONE);
    }

    private void setAdapter(ArrayList<EscalationMatrixResponseModel.MatrixDetails> details) {
        MatrixRecyclerViewAdapter adapter = new MatrixRecyclerViewAdapter(EscalationMatrixActivity.this, details);
        rcv_escalation_list.setAdapter(adapter);
    }

}