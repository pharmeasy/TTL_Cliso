package com.example.e5322.thyrosoft.Cliso_BMC;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;

import java.util.ArrayList;

public class BMC_SelectSampleTypeTTLAdapter extends RecyclerView.Adapter<BMC_SelectSampleTypeTTLAdapter.MyViewHolder> {

    ArrayList<ScannedBarcodeDetails> BMC_TTLBarcodeDetailsList;
    Activity activity;

    public BMC_SelectSampleTypeTTLAdapter(Activity activity, ArrayList<ScannedBarcodeDetails> BMC_TTLBarcodeDetailsList) {
        this.activity = activity;
        this.BMC_TTLBarcodeDetailsList = BMC_TTLBarcodeDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bmc_view_sample_type_ttl_item, viewGroup, false);
        return new BMC_SelectSampleTypeTTLAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.tv_product_name.setText(BMC_TTLBarcodeDetailsList.get(position).getProducts());
        myViewHolder.tv_sample_type.setText(BMC_TTLBarcodeDetailsList.get(position).getSpecimen_type());
    }

    @Override
    public int getItemCount() {
        return BMC_TTLBarcodeDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_name, tv_sample_type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_sample_type = (TextView) itemView.findViewById(R.id.tv_sample_type);
        }
    }
}
