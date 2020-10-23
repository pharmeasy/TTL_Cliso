package com.example.e5322.thyrosoft;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.BroadcastActivity;
import com.example.e5322.thyrosoft.Adapter.BroadcastAdapter;
import com.example.e5322.thyrosoft.Controller.AckBroadcastMsgController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.GetBroadcastsListController;
import com.example.e5322.thyrosoft.Models.RequestModels.AckBroadcastMsgRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.AckBroadcastMsgResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBroadcastsResponseModel;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BroadcastDetailsActivity extends AppCompatActivity {

    private Activity mActivity;
    private ImageView back, home;
    private CircleImageView iv_enteryImg;
    private TextView tvHeader, tvPostedOn, tvPostedBy, tvAck;
    private WebView webView_Data;
    private CheckBox chk_ack;
    private LinearLayout remark_ll;
    private EditText edt_Remarks;
    private Button btn_submit;
    private GetBroadcastsResponseModel.MessagesBean messagesBean;
    private ConnectionDetector cd;
    private String api_key, user, name_tsp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_details);

        mActivity = BroadcastDetailsActivity.this;
        cd = new ConnectionDetector(mActivity);

        GlobalClass.setStatusBarcolor(mActivity);

        Object object = Objects.requireNonNull(getIntent().getExtras()).getSerializable("BroadcastMessage");
        messagesBean = (GetBroadcastsResponseModel.MessagesBean) object;

        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        api_key = prefs.getString("API_KEY", "");

        SharedPreferences getProfileName = getSharedPreferences("profile", MODE_PRIVATE);
        name_tsp = getProfileName.getString("name", "");

        initUI();
        initListeners();
        bindData();
    }

    private void initUI() {
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        iv_enteryImg = findViewById(R.id.iv_enteryImg);
        tvHeader = findViewById(R.id.tvHeader);
        tvPostedOn = findViewById(R.id.tvPostedOn);
        tvPostedBy = findViewById(R.id.tvPostedBy);
        webView_Data = findViewById(R.id.webView_Data);
        chk_ack = findViewById(R.id.chk_ack);
        remark_ll = findViewById(R.id.remark_ll);
        edt_Remarks = findViewById(R.id.edt_Remarks);
        btn_submit = findViewById(R.id.btn_submit);
        tvAck = findViewById(R.id.tvAck);
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
                GlobalClass.goToHome(BroadcastDetailsActivity.this);
            }
        });

        edt_Remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.ent_feedback, 2);
                    if (enteredString.length() > 0) {
                        edt_Remarks.setText(enteredString.substring(1));
                    } else {
                        edt_Remarks.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    callAPIAckBroadcastsMsg();
                }
            }
        });
    }

    private void bindData() {
        GlobalClass.SetText(tvHeader, messagesBean.getTitle());
        GlobalClass.SetText(tvPostedOn, messagesBean.getNoticeDate());
        GlobalClass.SetText(tvPostedBy, messagesBean.getEnterBy());
        GlobalClass.DisplayCircularImgWithPlaceholderFromURL(mActivity, messagesBean.getImgURL(), iv_enteryImg, R.drawable.user_profile);
        webView_Data.getSettings().setJavaScriptEnabled(true);
        webView_Data.loadData(messagesBean.getNoticeMessage(), "text/html", "UTF-8");
        if (!GlobalClass.isNull(messagesBean.getIsAcknowledged()) && messagesBean.getIsAcknowledged().equalsIgnoreCase("Y")) {
            tvAck.setVisibility(View.VISIBLE);
            remark_ll.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
            chk_ack.setVisibility(View.GONE);
        } else {
            tvAck.setVisibility(View.GONE);
            remark_ll.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
            chk_ack.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkValidation() {
        if (!chk_ack.isChecked()) {
            GlobalClass.showTastyToast(mActivity, ToastFile.ack_broadcast, 2);
            return false;
        }
        if (GlobalClass.isNull(edt_Remarks.getText().toString().trim())) {
            GlobalClass.showTastyToast(mActivity, ToastFile.remark, 2);
            edt_Remarks.requestFocus();
            return false;
        }
        return true;
    }

    private void callAPIAckBroadcastsMsg() {
        try {
            AckBroadcastMsgRequestModel requestModel = new AckBroadcastMsgRequestModel();
            requestModel.setApikey(api_key);
            requestModel.setSourceCode(user);
            requestModel.setSourceName(name_tsp);
            requestModel.setMsgID(messagesBean.getMessageCode());
            requestModel.setRemarks(edt_Remarks.getText().toString().trim());
            requestModel.setTitle(messagesBean.getTitle());

            if (cd.isConnectingToInternet()) {
                if (ControllersGlobalInitialiser.ackBroadcastMsgController != null) {
                    ControllersGlobalInitialiser.ackBroadcastMsgController = null;
                }
                ControllersGlobalInitialiser.ackBroadcastMsgController = new AckBroadcastMsgController(BroadcastDetailsActivity.this);
                ControllersGlobalInitialiser.ackBroadcastMsgController.ackBroadcastMsg(requestModel);
            } else {
                GlobalClass.showTastyToast(mActivity, ToastFile.intConnection, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postAckBroadcastMsgResponse(AckBroadcastMsgResponseModel responseModel) {
        try {
            if (!GlobalClass.isNull(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase(Constants.RES0000)) {
                tvAck.setVisibility(View.VISIBLE);
                remark_ll.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                chk_ack.setVisibility(View.GONE);
            } else {
                GlobalClass.showTastyToast(mActivity, responseModel.getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
