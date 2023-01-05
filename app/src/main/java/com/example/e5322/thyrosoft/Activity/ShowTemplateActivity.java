package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.TemplateAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Handbilltemplate_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.TemplateResponse;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

public class ShowTemplateActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recy_handbill;
    ConnectionDetector cd;
    private GlobalClass globalClass;
    public String TAG = getClass().getSimpleName();
    Activity mActivity;
    TemplateAdapter templateAdapter;
    ImageView back, home;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalClass = new GlobalClass(ShowTemplateActivity.this);
        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.maroon));
        }
        setContentView(R.layout.fragment_shw_template);
        mActivity = ShowTemplateActivity.this;
        initView();
        setListerner();
        calltemplateApi();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        cd = new ConnectionDetector(ShowTemplateActivity.this);
        recy_handbill = findViewById(R.id.rec_handbill);

        TextView txtname = findViewById(R.id.txt_name);
        GlobalClass.SetText(txtname, mActivity.getString(R.string.dg_template));

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

    }

    private void setListerner() {
        back.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    private void calltemplateApi() {
        try {
            if (ControllersGlobalInitialiser.handbilltemplate_controller != null) {
                ControllersGlobalInitialiser.handbilltemplate_controller = null;
            }
            ControllersGlobalInitialiser.handbilltemplate_controller = new Handbilltemplate_Controller(ShowTemplateActivity.this, mActivity);
            ControllersGlobalInitialiser.handbilltemplate_controller.gethandteplates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gethndTemplateResp(Response<TemplateResponse> response) {
        if (response.body() != null) {
            if (!GlobalClass.isNull(response.body().getResId()) &&
                    response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                if (GlobalClass.CheckArrayList(response.body().getHandbill())) {
                    templateAdapter = new TemplateAdapter(ShowTemplateActivity.this, (ArrayList<TemplateResponse.HandbillBean>) response.body().getHandbill());
                    recy_handbill.setLayoutManager(new GridLayoutManager(ShowTemplateActivity.this, 2));
                    recy_handbill.setAdapter(templateAdapter);
                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.NODATA, 2);
                }
            } else {

                GlobalClass.showTastyToast(mActivity, response.body().getResponse(), 2);
            }
        } else {
            GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.home:
                GlobalClass.redirectToHome(mActivity);
                break;
        }
    }
}
