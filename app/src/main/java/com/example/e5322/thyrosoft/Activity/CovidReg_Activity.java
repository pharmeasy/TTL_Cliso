package com.example.e5322.thyrosoft.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.Covidentered_frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import java.util.List;

public class CovidReg_Activity extends Fragment {
    LinearLayout enter_ll_unselected, unchecked_entered_ll;
    TextView enter, enetered;
    ImageView back, home, enter_arrow_enter, enter_arrow_entered;
    FrameLayout fragment_main;
    private Object currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.lay_covidreg, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enter_ll_unselected = (LinearLayout)view. findViewById(R.id.enter_ll_unselected);
        unchecked_entered_ll = (LinearLayout)view.  findViewById(R.id.unchecked_entered_ll);

        back = (ImageView)view.  findViewById(R.id.back);
        home = (ImageView)view.  findViewById(R.id.home);
        enter_arrow_enter = (ImageView) view. findViewById(R.id.enter_arrow_enter);
        enter_arrow_entered = (ImageView)view.  findViewById(R.id.enter_arrow_entered);
        enter = (TextView) view. findViewById(R.id.enter);
        enetered = (TextView) view. findViewById(R.id.enetered);
        fragment_main = (FrameLayout) view. findViewById(R.id.fragment_mainLayout);
        enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
        enter_arrow_enter.setVisibility(View.VISIBLE);
        enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
        enter_arrow_entered.setVisibility(View.GONE);


        final Covidenter_Frag covidenter_frag = new Covidenter_Frag();
        replaceFragment(covidenter_frag);

        enter_ll_unselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                enetered.setBackground(getResources().getDrawable(R.drawable.enter_button));
                enter_arrow_entered.setVisibility(View.VISIBLE);
                enter.setBackgroundColor(getResources().getColor(R.color.lightgray));
                enter_arrow_enter.setVisibility(View.GONE);
                Covidentered_frag covidentered_frag = new Covidentered_frag();
                replaceFragment(covidentered_frag);
            }
        });
    }


//    @Override
//    public void onBackPressed() {
//        try {
//            List fragments = getSupportFragmentManager().getFragments();
//            currentFragment = fragments.get(fragments.size() - 1);
//            if (currentFragment.toString().contains("Covidenter_Frag")) {
//                enter.setBackground(getResources().getDrawable(R.drawable.enter_button));
//                enter_arrow_enter.setVisibility(View.VISIBLE);
//                enetered.setBackgroundColor(getResources().getColor(R.color.lightgray));
//                enter_arrow_entered.setVisibility(View.GONE);
//                Covidenter_Frag c = new Covidenter_Frag();
//                replaceFragment(c);
//            }
//            if (currentFragment.toString().contains("Covidentered_frag")) {
//                finish();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Begin Fragment transaction.
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}