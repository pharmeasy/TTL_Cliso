package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.MultipleLeadActivity;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.WOE.ScanBarcodeLeadId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class AdapterRe extends RecyclerView.Adapter<AdapterRe.MyViewHolder> {

    private LayoutInflater inflater;
    Leads[] leads;
    Context context;
    String getVial_numbver;
    LeadOrderIdMainModel leadOrderIdMainModel;
    OnItemClickListener listener;
    boolean sizeflag = false;
    Start_New_Woe start_new_woe;
    private String json, leadTESTS;
    List<String> testname;

    public interface OnItemClickListener {
        void onItemClicked();
    }

    public void leadclick(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdapterRe(Context context, Leads[] leads, String getVial_numbver, LeadOrderIdMainModel leadOrderIdMainModel, Start_New_Woe start_new_woe) {
        this.leads = leads;
        this.leadOrderIdMainModel = leadOrderIdMainModel;
        this.getVial_numbver = getVial_numbver;
        this.start_new_woe = start_new_woe;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public AdapterRe.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.lay_leadid, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterRe.MyViewHolder holder, final int position) {
        testname = new ArrayList<>();

        if (GlobalClass.checkArray(leads) && GlobalClass.checkArray(leads[position].getLeadData())) {
            for (int i = 0; i < leads[position].getLeadData().length; i++) {
                testname.add(leads[position].getLeadData()[i].getTest());
                leadTESTS = TextUtils.join(",", testname);
                GlobalClass.SetText(holder.txt_name,"NAME : " + leadTESTS);
            }

        }

        GlobalClass.SetText(holder.txt_leadId,"LEAD ID : " + leads[position].getLEAD_ID());

        holder.lin_leadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClicked();
                }

                SharedPreferences.Editor editor1 = context.getSharedPreferences("LeadOrderID", 0).edit();
                json = new Gson().toJson(leads[position].getLeadData());
                editor1.putString("leadData", json);
                editor1.commit();

                if (GlobalClass.checkArray(leads) && GlobalClass.checkArray(leads[position].getLeadData())) {
                    for (int i = 0; i < leads[position].getLeadData().length; i++) {
                        if (GlobalClass.checkArray(leads[position].getLeadData()[i].getSample_type())){
                            if (leads[position].getLeadData()[i].getSample_type().length > 1) {
                                sizeflag = true;
                                break;
                            }
                        }

                    }
                }

                Intent i = null;
                if (sizeflag) {
                    i = new Intent(context, MultipleLeadActivity.class);
                } else {
                    i = new Intent(context, ScanBarcodeLeadId.class);
                }

                i.putExtra("MyClass", leadOrderIdMainModel);
                i.putExtra("LeadID", leads[position].getLEAD_ID());
                i.putExtra("SAMPLE_TYPE", leads[position].getSAMPLE_TYPE());
                i.putExtra("fromcome", "adapter");
                i.putExtra("TESTS", leadTESTS);
                i.putExtra("SCT", leads[position].getSCT());
                i.putExtra("leadData", json);

                SharedPreferences.Editor editor = context.getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                editor.putString("typeName", leads[position].getTYPE());
                editor.putString("SR_NO", getVial_numbver);
                editor.commit();

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return leads.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_leadId;
        LinearLayout lin_leadid;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.name);
            txt_leadId = (TextView) itemView.findViewById(R.id.txt_leadid);
            lin_leadid = itemView.findViewById(R.id.lin_leadid);
        }
    }
}
