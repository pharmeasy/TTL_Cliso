package com.example.e5322.thyrosoft.Activity.frags;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.e5322.thyrosoft.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class A2Fragment extends RootFragment {


    public A2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_a2, container, false);

        rootView.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });

        return rootView;
    }


    private void enterNextFragment() {
        A3Fragment a3Fragment = new A3Fragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_mainLayout, a3Fragment).commit();
    }

}
