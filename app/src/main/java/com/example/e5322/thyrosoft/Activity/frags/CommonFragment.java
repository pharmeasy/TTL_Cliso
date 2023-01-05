package com.example.e5322.thyrosoft.Activity.frags;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e5322.thyrosoft.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class CommonFragment extends RootFragment {

    public CommonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_common, container, false);

        rootView.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNextFragment();
            }
        });

        return rootView;
    }

    private void enterNextFragment() {
        CommonFragment commonFragment = new CommonFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_mainLayout, commonFragment).commit();
    }

}
