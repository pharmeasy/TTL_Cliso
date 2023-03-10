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
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.Covidentered_frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.R;

public class CovidReg_Activity extends Fragment {
    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;
    private Object currentFragment;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String ComingFlag;
    private CovidReg_Activity.OnFragmentInteractionListener mListener;
    LinearLayout ll_mainCovidReg,ll_noauth;
    AppPreferenceManager appPreferenceManager;

    public CovidReg_Activity() {
        // Required empty public constructor
    }

    public static CovidReg_Activity newInstance(String param1, String param2) {
        CovidReg_Activity fragment = new CovidReg_Activity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lay_covidreg, container, false);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getView() != null){

            GlobalClass.ComingFrom = "Covid_Registration";
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appPreferenceManager = new AppPreferenceManager(getActivity());
        ll_mainCovidReg = (LinearLayout) view.findViewById(R.id.ll_mainCovidReg);
        ll_noauth = (LinearLayout) view.findViewById(R.id.ll_noauth);
        if (appPreferenceManager.getCovidAccessResponseModel().isCovidRegistration()) {
            ll_mainCovidReg.setVisibility(View.VISIBLE);
            ll_noauth.setVisibility(View.GONE);
            enter_ll_unselected = (LinearLayout) view.findViewById(R.id.enter_ll_unselected);
            enter_ll_unselected = (LinearLayout) view.findViewById(R.id.enter_ll_unselected);
            unchecked_entered_ll = (LinearLayout) view.findViewById(R.id.unchecked_entered_ll);

            back = (ImageView) view.findViewById(R.id.back);
            home = (ImageView) view.findViewById(R.id.home);
            enter_arrow_enter = (ImageView) view.findViewById(R.id.enter_arrow_enter);
            enter_arrow_entered = (ImageView) view.findViewById(R.id.enter_arrow_entered);
            enter = (TextView) view.findViewById(R.id.enter);
            enetered = (TextView) view.findViewById(R.id.enetered);
            fragment_main = (FrameLayout) view.findViewById(R.id.fragment_mainLayout);


            if (Constants.covidfrag_flag.equalsIgnoreCase("1")) {
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                Covidentered_frag covidentered_frag = new Covidentered_frag();
                replaceFragment(covidentered_frag);
            } else {
                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_enter.setVisibility(View.VISIBLE);
                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_entered.setVisibility(View.GONE);
                final Covidenter_Frag covidenter_frag = new Covidenter_Frag();
                replaceFragment(covidenter_frag);
            }

            enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.covidfrag_flag = "0";
                    enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
                    enter_arrow_enter.setVisibility(View.VISIBLE);
                    enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    enter_arrow_entered.setVisibility(View.GONE);
                    Covidenter_Frag covidenter_frag = new Covidenter_Frag();
                    replaceFragment(covidenter_frag);
                }
            });

            unchecked_entered_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.covidfrag_flag = "0";
                    enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                    enter_arrow_entered.setVisibility(View.VISIBLE);
                    enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    enter_arrow_enter.setVisibility(View.GONE);
                    Covidentered_frag covidentered_frag = new Covidentered_frag();
                    replaceFragment(covidentered_frag);
                }
            });
        } else {
            ll_mainCovidReg.setVisibility(View.GONE);
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