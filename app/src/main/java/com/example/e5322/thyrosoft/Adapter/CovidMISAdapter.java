package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CovidMISmodel;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CovidMISAdapter extends RecyclerView.Adapter<CovidMISAdapter.Viewholder> {
    Context context;
    List<Covidmis_response.OutputBean> covidMISmodelList;

    public CovidMISAdapter(Context context, List<Covidmis_response.OutputBean> covidMISmodelList) {
        this.context = context;
        this.covidMISmodelList = covidMISmodelList;
    }

    @NonNull
    @Override
    public CovidMISAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_covidmis, viewGroup, false);
        return new CovidMISAdapter.Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidMISAdapter.Viewholder viewholder, int i) {
        final Covidmis_response.OutputBean covidMISmodel=covidMISmodelList.get(i);

        String name  = "<b>" +"Patient name :"+ "</b> " + covidMISmodel.getPatientName();
        String mobile  = "<b>" +"Mobile No :"+ "</b> " + covidMISmodel.getMobile();
        String ccc  = "<b>" +"CCC :"+ "</b> " + covidMISmodel.getCcc();
        viewholder.txt_name.setText(Html.fromHtml(name));
        viewholder.txt_mob.setText(Html.fromHtml(mobile));
        viewholder.txt_ccc.setText(Html.fromHtml(ccc));

        viewholder.txt_presc.setPaintFlags(viewholder.txt_presc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewholder.txt_adhar.setPaintFlags(viewholder.txt_adhar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewholder.txt_trf.setPaintFlags(viewholder.txt_trf.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        viewholder.txt_presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(covidMISmodel.getPrescription())){
                    String url = covidMISmodel.getPrescription().replaceAll("\\\\", "//");
                    if (url != null && !url.isEmpty())
                        GlobalClass.showImageDialog((Activity)context, null, url, 2);
                    else
                        Global.showCustomToast((Activity)context, "Image not found");
                }
            }
        });

        viewholder.txt_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(covidMISmodel.getAadhar())){
                    String url = covidMISmodel.getAadhar().replaceAll("\\\\", "//");
                    if (url != null && !url.isEmpty())
                        GlobalClass.showImageDialog((Activity)context, null, url, 2);
                    else
                        Global.showCustomToast((Activity)context, "Image not found");
                }
            }
        });

        viewholder.txt_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(covidMISmodel.getTrf())){
                    String url = covidMISmodel.getTrf().replaceAll("\\\\", "//");
                    if (url != null && !url.isEmpty())
                        GlobalClass.showImageDialog((Activity)context, null, url, 2);
                    else
                        Global.showCustomToast((Activity)context, "Image not found");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return covidMISmodelList.size();
    }
    public void filterList(ArrayList<Covidmis_response.OutputBean> filterdNames) {
        this.covidMISmodelList = filterdNames;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_mob,txt_ccc,txt_trf,txt_adhar,txt_presc;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.patientName);
            txt_mob=itemView.findViewById(R.id.txt_mobile);
            txt_ccc=itemView.findViewById(R.id.txt_ccc);
            txt_presc=itemView.findViewById(R.id.txt_presc);
            txt_adhar=itemView.findViewById(R.id.txt_adhar);
            txt_trf=itemView.findViewById(R.id.txt_trf);
        }
    }
}
