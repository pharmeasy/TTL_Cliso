package com.example.e5322.thyrosoft.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.Interface.LeadClickInterface;
import com.example.e5322.thyrosoft.LeadOrderIDModel.LeadOrderIdMainModel;
import com.example.e5322.thyrosoft.LeadOrderIDModel.Leads;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.WOE.ScanBarcodeLeadId;

import static android.content.Context.MODE_PRIVATE;

public class AdapterRe extends RecyclerView.Adapter<AdapterRe.MyViewHolder> {

    private LayoutInflater inflater;
    Leads[] leads;
    Dialog dialog;
    Context context;
    String getVial_numbver;
    LeadOrderIdMainModel leadOrderIdMainModel;
    OnItemClickListener listener;

    Start_New_Woe start_new_woe;

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

        holder.txt_name.setText("NAME : "+leads[position].getNAME());
        holder.txt_leadId.setText("LEAD ID : "+leads[position].getLEAD_ID());
        holder.lin_leadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onItemClicked();
                }
                Intent i = new Intent(context, ScanBarcodeLeadId.class);
                i.putExtra("MyClass", leadOrderIdMainModel);
                i.putExtra("LeadID", leads[position].getLEAD_ID());
                i.putExtra("SAMPLE_TYPE", leads[position].getSAMPLE_TYPE());
                i.putExtra("fromcome", "adapter");
                i.putExtra("TESTS", leads[position].getTESTS());
                i.putExtra("SCT", leads[position].getSCT());
                SharedPreferences.Editor editor = context.getSharedPreferences("getBrandTypeandName", MODE_PRIVATE).edit();
                editor.putString("typeName", leads[position].getTYPE());
                editor.putString("SR_NO", getVial_numbver);
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
