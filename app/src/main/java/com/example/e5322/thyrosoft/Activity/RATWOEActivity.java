package com.example.e5322.thyrosoft.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.Fragment.RATEnteredFrag;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RATWOEActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RATWOEActivity extends Fragment {

    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;
    private Object currentFragment;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RATWOEActivity.OnFragmentInteractionListener mListener;
    LinearLayout ll_mainCrat,ll_noauth;
    AppPreferenceManager appPreferenceManager;


    public RATWOEActivity() {
        // Required empty public constructor
    }


    public static RATWOEActivity newInstance(String param1, String param2) {
        RATWOEActivity fragment = new RATWOEActivity();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rat_woe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferenceManager = new AppPreferenceManager(getActivity());
        ll_mainCrat = (LinearLayout) view.findViewById(R.id.ll_mainCrat);
        ll_noauth = (LinearLayout) view.findViewById(R.id.ll_noauth);

        if (appPreferenceManager.getCovidAccessResponseModel().isRat()) {
            ll_mainCrat.setVisibility(View.VISIBLE);
            ll_noauth.setVisibility(View.GONE);

            enter_ll_unselected = (LinearLayout) view.findViewById(R.id.enter_ll_unselected);
            unchecked_entered_ll = (LinearLayout) view.findViewById(R.id.unchecked_entered_ll);

            back = (ImageView) view.findViewById(R.id.back);
            home = (ImageView) view.findViewById(R.id.home);
            enter_arrow_enter = (ImageView) view.findViewById(R.id.enter_arrow_enter);
            enter_arrow_entered = (ImageView) view.findViewById(R.id.enter_arrow_entered);
            enter = (TextView) view.findViewById(R.id.enter);
            enetered = (TextView) view.findViewById(R.id.enetered);
            fragment_main = (FrameLayout) view.findViewById(R.id.fragment_mainLayout);


            if (Constants.ratfrag_flag.equalsIgnoreCase("1")) {
                Constants.universal = 0;
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                RATEnteredFrag covidentered_frag = new RATEnteredFrag();
                replaceFragment(covidentered_frag);
            } else {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                final RATEnterFrag covidenter_frag = new RATEnterFrag();
                replaceFragment(covidenter_frag);
            }

            enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.ratfrag_flag = "0";
                    Constants.universal = 0;
                    enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                    enter_arrow_enter.setVisibility(View.VISIBLE);
                    enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    enter_arrow_entered.setVisibility(View.GONE);
                    RATEnterFrag covidenter_frag = new RATEnterFrag();
                    replaceFragment(covidenter_frag);
                }
            });

            unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.ratfrag_flag = "0";
                    Constants.universal = 0;
                    enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                    enter_arrow_entered.setVisibility(View.VISIBLE);
                    enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    enter_arrow_enter.setVisibility(View.GONE);
                    RATEnteredFrag covidentered_frag = new RATEnteredFrag();
                    replaceFragment(covidentered_frag);
                }
            });

        } else {
            ll_mainCrat.setVisibility(View.GONE);
            ll_noauth.setVisibility(View.VISIBLE);

        }
    }


    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getChildFragmentManager();

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