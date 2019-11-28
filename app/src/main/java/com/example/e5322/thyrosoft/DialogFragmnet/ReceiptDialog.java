package com.example.e5322.thyrosoft.DialogFragmnet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.e5322.thyrosoft.R;

public class ReceiptDialog extends DialogFragment {

    Spinner spn_paymd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lay_receiptdialog, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);


    }

    private void initView(View v) {
        spn_paymd = v.findViewById(R.id.spn_slot);
        ArrayAdapter pmode_adapter = ArrayAdapter.createFromResource(getContext(), R.array.pmode, R.layout.whitetext_spin);
        pmode_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        spn_paymd.setAdapter(pmode_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.dp_360);
        int height = getResources().getDimensionPixelSize(R.dimen.dp_290);
        getDialog().getWindow().setLayout(width, height);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
