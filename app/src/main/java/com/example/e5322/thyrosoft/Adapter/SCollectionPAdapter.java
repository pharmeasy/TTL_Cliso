package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.Fragment.RATEnterFrag;
import com.example.e5322.thyrosoft.Fragment.SRFCovidWOEEnterFragment;
import com.example.e5322.thyrosoft.Models.ResponseModels.getSCPTechnicianModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class SCollectionPAdapter extends RecyclerView.Adapter<SCollectionPAdapter.ViewHolder> {
    Context mContext;
    ArrayList<getSCPTechnicianModel.getTechnicianlist> LabDetailsArraList;
    private OnItemClickListener onItemClickListener;

    public SCollectionPAdapter(SRFCovidWOEEnterFragment fragment, Activity mContext, ArrayList<getSCPTechnicianModel.getTechnicianlist> labDetailsArrayList) {
        this.mContext = mContext;
        this.LabDetailsArraList=new ArrayList<>();
        this.LabDetailsArraList = labDetailsArrayList;
    }

    public SCollectionPAdapter(Covidenter_Frag covidenter_frag, Activity activity, ArrayList<getSCPTechnicianModel.getTechnicianlist> labDetailsArrayList) {
        this.mContext = activity;
        this.LabDetailsArraList=new ArrayList<>();
        this.LabDetailsArraList = labDetailsArrayList;
    }

    public SCollectionPAdapter(RATEnterFrag ratEnterFrag, Activity activity, ArrayList<getSCPTechnicianModel.getTechnicianlist> labDetailsArrayList) {
        this.mContext = activity;
        this.LabDetailsArraList=new ArrayList<>();
        this.LabDetailsArraList = labDetailsArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void filteredArraylist(ArrayList<getSCPTechnicianModel.getTechnicianlist> LabDetailsArraList) {

        this.LabDetailsArraList = LabDetailsArraList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.spinner_item_ll, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.text1.setText(LabDetailsArraList.get(position).getNAME() + " - " + LabDetailsArraList.get(position).getNED_NUMBER());
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ItemName = holder.text1.getText().toString();
                if (onItemClickListener != null) {
                    onItemClickListener.onPassSgcID(LabDetailsArraList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return LabDetailsArraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.textspnner);
        }
    }


    public interface OnItemClickListener {
        void onPassSgcID(getSCPTechnicianModel.getTechnicianlist pos);
    }

}