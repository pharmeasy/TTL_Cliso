package com.example.e5322.thyrosoft.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Fragment.SRFCovidWOEEnterFragment;
import com.example.e5322.thyrosoft.Fragment.SRFCovidWOEEnteredFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

public class SRFCovidWOEMainActivity extends AppCompatActivity {

    private LinearLayout enter_ll_unselected, unchecked_entered_ll;
    private TextView enter, entered;
    private ImageView back, home, enter_arrow_enter, enter_arrow_entered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srf_covid_woe_main);

        initUI();
        initListeners();

        if (Constants.SRFcovidWOEfrag_flag.equalsIgnoreCase("1")) {
            gotoEntered();
        } else {
            gotoEnter();
        }
    }

    private void initUI() {
        enter_ll_unselected = findViewById(R.id.enter_ll_unselected);
        unchecked_entered_ll = findViewById(R.id.unchecked_entered_ll);
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        enter_arrow_enter = findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = findViewById(R.id.enter_arrow_entered);
        enter = findViewById(R.id.enter);
        entered = findViewById(R.id.entered);
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
                GlobalClass.goToHome(SRFCovidWOEMainActivity.this);
            }
        });

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.SRFcovidWOEfrag_flag = "0";
                gotoEnter();
            }
        });

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.SRFcovidWOEfrag_flag = "0";
                gotoEntered();
            }
        });
    }

    private void gotoEnter() {
        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);
        entered.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_entered.setVisibility(View.GONE);
        SRFCovidWOEEnterFragment fragment = new SRFCovidWOEEnterFragment();
        replaceFragment(fragment);
    }

    private void gotoEntered() {
        entered.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_entered.setVisibility(View.VISIBLE);
        enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_enter.setVisibility(View.GONE);
        SRFCovidWOEEnteredFragment fragment = new SRFCovidWOEEnteredFragment();
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment destFragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);
        fragmentTransaction.commit();
    }
}