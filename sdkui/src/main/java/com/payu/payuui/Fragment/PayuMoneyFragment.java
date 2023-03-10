package com.payu.payuui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.R;

import androidx.fragment.app.Fragment;


public class PayuMoneyFragment extends Fragment {

    public PayuMoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payu_money, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            getActivity().setResult(resultCode, data);
            getActivity().finish();
        }
    }

}
