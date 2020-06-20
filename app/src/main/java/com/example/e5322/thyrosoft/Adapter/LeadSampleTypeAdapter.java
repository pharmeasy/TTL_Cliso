package com.example.e5322.thyrosoft.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.R;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeadSampleTypeAdapter extends RecyclerView.Adapter<LeadSampleTypeAdapter.MyViewHolder> {
    Activity activity;
    String sampleType;
    private ArrayList<String> sampletypeslist;
    private LeadSampleTypeAdapter.OnItemClickListener onItemClickListener;
    List<Leads.LeadData> list;
    boolean isspinnerflag = false;

    public LeadSampleTypeAdapter(Activity activity, List<Leads.LeadData> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setOnItemClickListener(LeadSampleTypeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public LeadSampleTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lead_sample_type, viewGroup, false);
        return new LeadSampleTypeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LeadSampleTypeAdapter.MyViewHolder myViewHolder, final int position) {


        myViewHolder.txt_testname.setText(list.get(position).getTest());

        sampletypeslist = new ArrayList<>();
        sampletypeslist.add("Select sample type");

        if (list.get(position).getSample_type() != null) {
            for (int m = 0; m < list.get(position).getSample_type().length; m++) {
                sampletypeslist.add(list.get(position).getSample_type()[m].getOutlab_sampletype().trim());
            }

            myViewHolder.sample_type_spinner.setVisibility(View.VISIBLE);

        }

        myViewHolder.sample_type_spinner.setAdapter(new ArrayAdapter<>(activity, R.layout.spinnerproperty, sampletypeslist));

        myViewHolder.sample_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (myViewHolder.sample_type_spinner.getSelectedItemPosition() == 0)
                    sampleType = "";
                else
                    sampleType = myViewHolder.sample_type_spinner.getSelectedItem().toString().trim();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemSelected(list.get(position).getTest(), sampleType, position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemSelected(String product, String sample_type, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout sample_type_linear, ll_linear;
        Spinner sample_type_spinner;
        TextView txt_testname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sample_type_linear = (LinearLayout) itemView.findViewById(R.id.sample_type_linear);
            sample_type_spinner = (Spinner) itemView.findViewById(R.id.sample_type_spinner);
            txt_testname = itemView.findViewById(R.id.txt_testname);
            ll_linear = itemView.findViewById(R.id.ll_linear);
        }

    }
}
