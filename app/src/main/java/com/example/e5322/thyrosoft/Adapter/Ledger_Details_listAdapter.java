package com.example.e5322.thyrosoft.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

/**
 * Created by e5426@thyrocare.com on 25/5/18.
 */

public class Ledger_Details_listAdapter extends BaseAdapter {

    Context context;
    ArrayList<Ledger_DetailsModel> ledgerDetAll;

    public Ledger_Details_listAdapter(Context context, ArrayList<Ledger_DetailsModel> ledgerDetList) {

        this.context = context;
        this.ledgerDetAll = ledgerDetList;
    }

    @Override
    public int getCount() {
        return ledgerDetAll.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {


            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                convertView = inflater.inflate(R.layout.ledger_list, parent, false);
            }


            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView amount = (TextView) convertView.findViewById(R.id.amount);
            TextView narration = (TextView) convertView.findViewById(R.id.narration);
            TextView icon = (TextView) convertView.findViewById(R.id.icon);


            if (!GlobalClass.isNull(ledgerDetAll.get(position).getAmountType()) && ledgerDetAll.get(position).getAmountType().equalsIgnoreCase("CREDIT")) {

                GlobalClass.SetText(date, ledgerDetAll.get(position).getDate());
                GlobalClass.SetText(icon, "C");
                GlobalClass.SetText(narration, ledgerDetAll.get(position).getNarration().toString());
                GlobalClass.SetText(amount, "+" + ledgerDetAll.get(position).getAmount().toString() + "");

                amount.setTextColor(context.getResources().getColor(R.color.darkgreen));

            } else {
                GlobalClass.SetText(date, ledgerDetAll.get(position).getDate());
                GlobalClass.SetText(narration, ledgerDetAll.get(position).getNarration().toString());
                GlobalClass.SetText(icon, "D");
                GlobalClass.SetText(amount, "-" + ledgerDetAll.get(position).getAmount().toString() + "");
                amount.setTextColor(context.getResources().getColor(R.color.red));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
