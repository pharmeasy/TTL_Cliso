package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetScanResponse;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.List;

public class MISAdapter extends RecyclerView.Adapter<MISAdapter.Viewholder> {
    Context context;
    List<GetScanResponse.RODETAILSBean> mismodelList;

    public MISAdapter(Context context, List<GetScanResponse.RODETAILSBean> mismodelList) {
        this.context = context;
        this.mismodelList = mismodelList;

    }

    @NonNull
    @Override
    public MISAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.adapter_scansumm, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MISAdapter.Viewholder viewholder, final int i) {
        final GetScanResponse.RODETAILSBean mismodel = mismodelList.get(i);
        int Count = 1 + i;


        try {
            GlobalClass.SetText(viewholder.srno, "" + Count);
            GlobalClass.SetText(viewholder.txt_used, mismodel.getUsed_stock());
            GlobalClass.SetText(viewholder.txt_os, mismodel.getOpening_balance());
            GlobalClass.SetText(viewholder.txt_cs, mismodel.getClosing_stock());
            GlobalClass.SetText(viewholder.txt_wastage, mismodel.getWastage_stock());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*TODO BELOW CODE IS FOR FILTERLIST*/
    public void filterList(ArrayList<GetScanResponse.RODETAILSBean> filterdNames) {
        this.mismodelList = filterdNames;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mismodelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView srno, txt_os, txt_used, txt_wastage, txt_cs;
        private LinearLayout lin_main;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            srno = itemView.findViewById(R.id.txt_srno);
            txt_os = itemView.findViewById(R.id.txt_os);
            txt_used = itemView.findViewById(R.id.txt_used);
            txt_wastage = itemView.findViewById(R.id.txt_wastage);
            txt_cs = itemView.findViewById(R.id.txt_cs);
            lin_main = itemView.findViewById(R.id.lin_main);
            GlobalClass.setBordertoView(context, lin_main, context.getResources().getColor(R.color.black), context.getResources().getColor(R.color.white));
        }
    }
}
