package com.example.e5322.thyrosoft.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.Activity.frags.BS_MISEntryFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Blood_sugar_entry_activity extends AppCompatActivity {
    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;
    private Object currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_sugar_entry_activity);

        initViews();

        final BS_EntryFragment bs_entryFragment = new BS_EntryFragment();
        replaceFragment(bs_entryFragment);

        initListneer();

    }


    private void initViews() {
        enter_ll_unselected = (LinearLayout) findViewById(R.id.enter_ll_unselected);
        unchecked_entered_ll = (LinearLayout) findViewById(R.id.unchecked_entered_ll);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        enter_arrow_enter = (ImageView) findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) findViewById(R.id.enter_arrow_entered);
        enter = (TextView) findViewById(R.id.enter);
        enetered = (TextView) findViewById(R.id.enetered);
        fragment_main = (FrameLayout) findViewById(R.id.fragment_mainLayout);
        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);
        enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_entered.setVisibility(View.GONE);

    }

    private void initListneer() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Blood_sugar_entry_activity.this);
            }
        });

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                BS_EntryFragment bs_entryFragment = new BS_EntryFragment();
                replaceFragment(bs_entryFragment);
            }
        });

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                BS_MISEntryFragment bs_misEntryFragment = new BS_MISEntryFragment();
                replaceFragment(bs_misEntryFragment);
            }
        });


    }

    @Override
    public void onBackPressed() {
        try {
            List fragments = getSupportFragmentManager().getFragments();
            currentFragment = fragments.get(fragments.size() - 1);
            if (currentFragment.toString().contains("BS_MISEntryFragment")) {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                BS_EntryFragment c = new BS_EntryFragment();
                replaceFragment(c);
            }
            if (currentFragment.toString().contains("BS_EntryFragment")) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
