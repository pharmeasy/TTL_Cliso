package com.example.e5322.thyrosoft.HHHtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.R;

public class HHHMainActivity extends AppCompatActivity {

    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered, txt_header;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_h_h_main);

        enter_ll_unselected = (LinearLayout) findViewById(R.id.enter_ll_unselected);
        unchecked_entered_ll = (LinearLayout) findViewById(R.id.unchecked_entered_ll);

        back = (ImageView) findViewById(R.id.back);
        txt_header = (TextView) findViewById(R.id.txt_header);
        home = (ImageView) findViewById(R.id.home);
        enter_arrow_enter = (ImageView) findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) findViewById(R.id.enter_arrow_entered);
        enter = (TextView) findViewById(R.id.enter);
        enetered = (TextView) findViewById(R.id.enetered);
        fragment_main = (FrameLayout) findViewById(R.id.fragment_mainLayout);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        Intent i = getIntent();
        String testname = i.getStringExtra("Name");
        txt_header.setText(testname);

        if (Constants.ratfrag_flag.equalsIgnoreCase("1")) {
            enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
            enter_arrow_entered.setVisibility(View.VISIBLE);
            enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
            enter_arrow_enter.setVisibility(View.GONE);
            HHHEnteredFrag hhhEnteredFrag = new HHHEnteredFrag();
            replaceFragment(hhhEnteredFrag);
        } else {
            enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
            enter_arrow_enter.setVisibility(View.VISIBLE);
            enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
            enter_arrow_entered.setVisibility(View.GONE);
            final HHHEnterFrag hhhEnterFrag = new HHHEnterFrag();
            replaceFragment(hhhEnterFrag);
        }

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                HHHEnterFrag hhhEnterFrag = new HHHEnterFrag();
                replaceFragment(hhhEnterFrag);
            }
        });

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                HHHEnteredFrag hhhEnteredFrag = new HHHEnteredFrag();
                replaceFragment(hhhEnteredFrag);
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
                Intent i = new Intent(HHHMainActivity.this, ManagingTabsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}