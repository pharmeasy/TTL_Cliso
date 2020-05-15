package com.example.e5322.thyrosoft.Activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Adapter.HealthTipsPagerAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ViewModel.HealthviewModel;

import java.util.ArrayList;
import java.util.List;

public class HealthArticle_Activity extends AppCompatActivity implements View.OnClickListener {

    HealthTipsPagerAdapter mAdapter;
    TextView tvname;
    HealthviewModel healthviewModel;
    private ArrayList<HealthTipsApiResponseModel.HArt> HealthTipsArrylst;
    private VerticalViewPager healthTipViewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        initView();
    }

    private void initView() {
        healthTipViewpager = findViewById(R.id.healthTipViewpager);
        tvname = findViewById(R.id.txt_name);
        tvname.setText("Health Articles");
        tvname.setAllCaps(true);
        tvname.setTextColor(getResources().getColor(R.color.maroon));
        tvname.setTextSize(20f);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);

        /**Below code is checking Internet Connection*/
        if (GlobalClass.isNetworkAvailable(HealthArticle_Activity.this))
            CallHealthTipsApi();
        else
            GlobalClass.showAlertDialog(HealthArticle_Activity.this);
    }

    private void CallHealthTipsApi() {
        /**Initilize HealthViewmodel */
        healthviewModel = ViewModelProviders.of(HealthArticle_Activity.this).get(HealthviewModel.class);

        final Observer<List<HealthTipsApiResponseModel.HArt>> listObserver = new Observer<List<HealthTipsApiResponseModel.HArt>>() {
            @Override
            public void onChanged(List<HealthTipsApiResponseModel.HArt> healthTipsApiResponseModels) {
                /**Set response data to  HealthViewmodel */
                healthviewModel.setListdata(healthTipsApiResponseModels);
            }
        };

        /**getData method is written in Viewmodel,here we observe view */
        healthviewModel.getData(HealthArticle_Activity.this, healthTipViewpager).observe(HealthArticle_Activity.this, listObserver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back:
                finish();
                break;

            case R.id.home:
                startActivity(new Intent(HealthArticle_Activity.this, ManagingTabsActivity.class));
                break;

        }
    }
}
