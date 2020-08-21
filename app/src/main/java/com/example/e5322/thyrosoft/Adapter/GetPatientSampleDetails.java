package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class GetPatientSampleDetails extends RecyclerView.Adapter<GetPatientSampleDetails.ViewHolder> {
    Context context1;
    ArrayList<Barcodelist> barcodelists;
    private int pass_to_api;

    public GetPatientSampleDetails(Context mContext, ArrayList<Barcodelist> barcodelists, int pass_to_api) {
        this.context1 = mContext;
        this.barcodelists = barcodelists;
        this.pass_to_api = pass_to_api;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_layout_summary_barcodes, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GlobalClass.SetText(holder.barcodes_patients, barcodelists.get(position).getSAMPLE_TYPE().toString() + "");
        if (!GlobalClass.isNull(barcodelists.get(position).getBARCODE())) {
            GlobalClass.SetText(holder.namesOftest, barcodelists.get(position).getBARCODE().toString());
        }
        if (pass_to_api > 0) {
            String getSr_no = String.valueOf(pass_to_api + position);
            GlobalClass.SetText(holder.serial_number, "Sr No: " + getSr_no);
        } else {
            holder.serial_number.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return barcodelists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barcodes_patients, namesOftest, serial_number;

        public ViewHolder(View itemView) {
            super(itemView);
            barcodes_patients = (TextView) itemView.findViewById(R.id.barcodes_patients);
            namesOftest = (TextView) itemView.findViewById(R.id.namesOftest);
            serial_number = (TextView) itemView.findViewById(R.id.serial_number);
        }
    }
}
