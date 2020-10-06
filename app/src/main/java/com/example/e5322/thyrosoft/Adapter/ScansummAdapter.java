package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.e5322.thyrosoft.DialogFragmnet.ReceiptDialog;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.List;

public class ScansummAdapter extends RecyclerView.Adapter<ScansummAdapter.Viewholder> {

    Context context;
    List<ScansummaryModel> scansummlist = new ArrayList<>();

    public ScansummAdapter(Context context, List<ScansummaryModel> scansummaryModels) {
        this.context = context;
        this.scansummlist = scansummaryModels;
    }

    @NonNull
    @Override
    public ScansummAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_mis, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScansummAdapter.Viewholder viewholder, int i) {

        try {
            if (scansummlist != null) {

                ScansummaryModel scansummaryModel = scansummlist.get(i);

                GlobalClass.SetText(viewholder.txt_bkdt, scansummaryModel.getBOOKEDAT());
                GlobalClass.SetText(viewholder.txt_apppointdt, scansummaryModel.getAPPOINTMENT_DATE());
                GlobalClass.SetText(viewholder.txt_appointfixon, scansummaryModel.getAPPOINTMENT_DATE());

                GlobalClass.SetText(viewholder.txt_leadid, scansummaryModel.getLEAD_ID()+" / "+scansummaryModel.getMOBILE());
                GlobalClass.SetText(viewholder.txt_name, scansummaryModel.getNAME());
                GlobalClass.SetText(viewholder.txt_leadsts, scansummaryModel.getSTATUS()+" / Rs : "+scansummaryModel.getBilling_Rate());

        /*        GlobalClass.SetText(viewholder.txt_billing, "Rs "+scansummaryModel.getBilling_Rate());*/

                viewholder.btn_receipt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentActivity activity = (FragmentActivity) (context);
                        FragmentManager fm = activity.getSupportFragmentManager();
                        ReceiptDialog alertDialog = new ReceiptDialog();
                        alertDialog.show(fm, "receiptdialog");
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*TODO BELOW CODE IS FOR FILTERLIST*/
    public void filterList(ArrayList<ScansummaryModel> filterdNames) {
        this.scansummlist = filterdNames;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return scansummlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView txt_bkdt, txt_leadid, txt_name, txt_mobno, txt_leadsts, txt_appointfixon, txt_apppointdt, txt_billing;
        Button btn_receipt;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            txt_bkdt = itemView.findViewById(R.id.txt_bk_dt);
            txt_leadid = itemView.findViewById(R.id.txt_leadid);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_leadsts = itemView.findViewById(R.id.txt_leadsts);
            txt_appointfixon = itemView.findViewById(R.id.txt_app_fix);
            txt_apppointdt = itemView.findViewById(R.id.txt_app_dt);
            btn_receipt = itemView.findViewById(R.id.btn_receipt);

        }
    }
}
