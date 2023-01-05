package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.InclusionMasterModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.BundleConstants;
import com.example.e5322.thyrosoft.R;

/**
 * Created by e5233@thyrocare.com on 30/8/18.
 */

public class Dupl_InclusionAdapter extends RecyclerView.Adapter<Dupl_InclusionAdapter.MyViewHolder> {
    Context mContext;
    PETCT_Frag mPETCT_Frag;

    public Dupl_InclusionAdapter(PETCT_Frag pfragment, Context mContext) {
        this.mPETCT_Frag = pfragment;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dupl_inclusion_row_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            final InclusionMasterModel productModel = BundleConstants.lists_incl_dupl.get(position);
            holder.chkitemname.setText(productModel.getName());
            BundleConstants.lists_incl_dupl.get(position).setSelectedqty(Integer.parseInt("1"));
            GlobalClass.SetText(holder.txt_itemamnt, "Rs. " + setAmountqty(productModel));

            mPETCT_Frag.CallAmountdataFromDuplInclusion();

            holder.chkitemname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        BundleConstants.lists_incl_dupl.get(position).setSelected(true);
                    } else {
                        BundleConstants.lists_incl_dupl.get(position).setSelected(false);
                    }

                    notifyDataSetChanged();
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private String setAmountqty(InclusionMasterModel productModel) {
        String amnt = "";
        int s_amnt = 0;

        try {

            String s = productModel.getUnitCost();
            double d = Double.parseDouble(s);
            int df = (int) d;

            s_amnt = productModel.getSelectedqty() * df;

            amnt = "" + s_amnt;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return amnt;
    }

    @Override
    public int getItemCount() {
        return BundleConstants.lists_incl_dupl.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkitemname;
        TextView txt_itemamnt;

        MyViewHolder(View itemView) {
            super(itemView);
            this.chkitemname = (CheckBox) itemView.findViewById(R.id.chkitemname);
            this.txt_itemamnt = (TextView) itemView.findViewById(R.id.txt_itemamnt);
        }
    }
}
