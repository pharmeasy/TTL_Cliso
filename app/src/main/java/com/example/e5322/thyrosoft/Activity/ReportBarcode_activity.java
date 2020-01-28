package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Fragment.ReportScanFrag;
import com.example.e5322.thyrosoft.Fragment.ReportScansummaryFrag;
import com.example.e5322.thyrosoft.R;

public class ReportBarcode_activity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout scan_ll_unselected, unchecked_scansumm_ll;
    TextView txt_scan, txt_scansumm;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_reportbarcode);


        TextView txttitle = findViewById(R.id.txt_name);
        txttitle.setText( getResources().getString(R.string.tltreport));
        txttitle.setTextColor(getResources().getColor(R.color.maroon));

        scan_ll_unselected = (LinearLayout) findViewById(R.id.scan_ll_unselected);
        unchecked_scansumm_ll = (LinearLayout) findViewById(R.id.unchecked_scansumm_ll);

        enter_arrow_enter = (ImageView) findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) findViewById(R.id.enter_arrow_entered);

        txt_scan = (TextView) findViewById(R.id.txt_scan);
        txt_scansumm = (TextView) findViewById(R.id.txt_scansumm);
        fragment_main = (FrameLayout) findViewById(R.id.fragment_mainLayout);
        txt_scan.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);
        txt_scansumm.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_entered.setVisibility(View.GONE);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(this);
        home.setOnClickListener(this);


        ReportScanFrag petct_frag = new ReportScanFrag();
        replaceFragment(petct_frag);

        scan_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_scan.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                txt_scansumm.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);

                ReportScanFrag petct_frag = new ReportScanFrag();
                replaceFragment(petct_frag);
            }
        });

        unchecked_scansumm_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_scansumm.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                txt_scan.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);

                ReportScansummaryFrag scanSummaryActivity = new ReportScansummaryFrag();
                replaceFragment(scanSummaryActivity);
            }
        });
    }


    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        fragment_main.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin Fragment transaction.
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.home:
                startActivity(new Intent(ReportBarcode_activity.this, ManagingTabsActivity.class));
                break;

        }
    }
}
