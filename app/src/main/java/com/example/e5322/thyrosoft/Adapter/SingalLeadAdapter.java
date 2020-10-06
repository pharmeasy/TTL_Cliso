package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingalLeadAdapter extends RecyclerView.Adapter<SingalLeadAdapter.MyViewHolder> {
    private ArrayList<ScannedBarcodeDetails> singalLead_list;
    Activity activity;

    public SingalLeadAdapter(Activity activity, ArrayList<ScannedBarcodeDetails> singalLead_list) {
        this.activity = activity;
        this.singalLead_list = singalLead_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.signallead, viewGroup, false);
        return new SingalLeadAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.tv_product_name.setText(singalLead_list.get(position).getProducts());
        myViewHolder.tv_sample_type.setText(singalLead_list.get(position).getSpecimen_type());
    }

    @Override
    public int getItemCount() {
        return singalLead_list.size();
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

