package com.example.e5322.thyrosoft.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ScanSummaryActivity;
import com.example.e5322.thyrosoft.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NHFFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered;
    ImageView enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;


    public static NHFFragment newInstance(String param1, String param2) {
        NHFFragment fragment = new NHFFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lay_nhf, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);


        PETCT_Frag petct_frag = new PETCT_Frag();
        replaceFragment(petct_frag);

        initListner();

    }

    private void initViews(View view) {
        enter_ll_unselected = (LinearLayout) view.findViewById(R.id.enter_ll_unselected);
        unchecked_entered_ll = (LinearLayout) view.findViewById(R.id.unchecked_entered_ll);


        enter_arrow_enter = (ImageView) view.findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView) view.findViewById(R.id.enter_arrow_entered);
        enter = (TextView) view.findViewById(R.id.enter);
        enetered = (TextView) view.findViewById(R.id.enetered);
        fragment_main = (FrameLayout) view.findViewById(R.id.fragment_mainLayout);
        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);
        enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_entered.setVisibility(View.GONE);

    }

    private void initListner() {

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);

                PETCT_Frag petct_frag = new PETCT_Frag();
                replaceFragment(petct_frag);
            }
        });

        unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);

                ScanSummaryActivity scanSummaryActivity = new ScanSummaryActivity();
                replaceFragment(scanSummaryActivity);

            }
        });
    }


    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getChildFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.addToBackStack(null);

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();

    }
}
