package com.example.e5322.thyrosoft.Activity.frags;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class RapidAntibodyFrag extends AppCompatActivity {

    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;
    private RapidAntibodyFrag.OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rapid_antibody);
        enter_ll_unselected = (LinearLayout) findViewById(R.id.enter_ll_unselected);
        unchecked_entered_ll = (LinearLayout) findViewById(R.id.unchecked_entered_ll);

        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        enter_arrow_enter = (ImageView) findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) findViewById(R.id.enter_arrow_entered);
        enter = (TextView) findViewById(R.id.enter);
        enetered = (TextView) findViewById(R.id.enetered);
        fragment_main = (FrameLayout) findViewById(R.id.fragment_mainLayout);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);


        if (Constants.ratfrag_flag.equalsIgnoreCase("1")) {
            Constants.ratfrag_flag = "0";
            enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
            enter_arrow_entered.setVisibility(View.VISIBLE);
            enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
            enter_arrow_enter.setVisibility(View.GONE);
            AnitbodyEnteredFrag covidentered_frag = new AnitbodyEnteredFrag();
            replaceFragment(covidentered_frag);
        } else {
            enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
            enter_arrow_enter.setVisibility(View.VISIBLE);
            enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
            enter_arrow_entered.setVisibility(View.GONE);
            final AntiBodyEnterFrag covidenter_frag = new AntiBodyEnterFrag();
            replaceFragment(covidenter_frag);
        }

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.ratfrag_flag = "0";
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                AntiBodyEnterFrag covidenter_frag = new AntiBodyEnterFrag();
                replaceFragment(covidenter_frag);
            }
        });

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.ratfrag_flag = "0";
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                AnitbodyEnteredFrag covidentered_frag = new AnitbodyEnteredFrag();
                replaceFragment(covidentered_frag);
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

    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}