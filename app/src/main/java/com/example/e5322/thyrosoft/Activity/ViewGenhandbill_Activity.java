package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.HandbillAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Viewgenhandbill_Contoller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.HandbillsResponse;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewGenhandbill_Activity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    GlobalClass globalClass;
    ConnectionDetector cd;
    RecyclerView recy_handbill;
    HandbillAdapter handbillAdapter;
    ImageView back, home;
    TextView txt_nodata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_show_handbill);
        initilazation();
        initUI();
        callgenerateHandbllAPi();

    }

    private void callgenerateHandbllAPi() {
        try {
            if (ControllersGlobalInitialiser.viewgenhandbill_contoller != null) {
                ControllersGlobalInitialiser.viewgenhandbill_contoller = null;
            }
            ControllersGlobalInitialiser.viewgenhandbill_contoller = new Viewgenhandbill_Contoller(ViewGenhandbill_Activity.this, mActivity);
            ControllersGlobalInitialiser.viewgenhandbill_contoller.getgenhandbill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(this);
        recy_handbill = findViewById(R.id.rec_handbill);

        txt_nodata = findViewById(R.id.txt_nodata);

        TextView txtname = findViewById(R.id.txt_name);
        GlobalClass.SetText(txtname, mActivity.getString(R.string.dghandbill));

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    private void initilazation() {
        mActivity = ViewGenhandbill_Activity.this;
        globalClass = new GlobalClass(mActivity);
        cd = new ConnectionDetector(mActivity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                Intent myIntent = new Intent(mActivity, ShowTemplateActivity.class);
                startActivity(myIntent);
                break;

            case R.id.back:
                finish();
                break;

            case R.id.home:
                GlobalClass.redirectToHome(mActivity);
                break;
        }
    }

    public void getgeneratehandbillResp(HandbillsResponse body) {
        try {
            if (!GlobalClass.isNull(body.getResId()) &&
                    body.getResId().equalsIgnoreCase(Constants.RES0000)) {

                if (GlobalClass.CheckArrayList(body.getHandbillsByCleint())) {
                    recy_handbill.setVisibility(View.VISIBLE);
                    txt_nodata.setVisibility(View.GONE);
                    handbillAdapter = new HandbillAdapter(mActivity, (ArrayList<HandbillsResponse.HandbillsByCleintBean>) body.getHandbillsByCleint());
                    recy_handbill.setLayoutManager(new GridLayoutManager(mActivity, 2));
                    recy_handbill.setAdapter(handbillAdapter);

                } else {
                    recy_handbill.setVisibility(View.GONE);
                    txt_nodata.setVisibility(View.VISIBLE);
                    GlobalClass.showTastyToast(mActivity, MessageConstants.NODATA, 2);
                }
            } else {
                recy_handbill.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                GlobalClass.showTastyToast(mActivity, body.getResponse(), 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callgenerateHandbllAPi();
    }
}
