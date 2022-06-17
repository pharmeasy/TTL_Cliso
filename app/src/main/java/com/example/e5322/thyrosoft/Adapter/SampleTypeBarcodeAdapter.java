package com.example.e5322.thyrosoft.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.Scan_Barcode_Outlabs_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.SendScanBarcodeDetails;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;

import java.util.ArrayList;

public class SampleTypeBarcodeAdapter extends RecyclerView.Adapter<SampleTypeBarcodeAdapter.ViewHolder> implements SendScanBarcodeDetails {
    public Context context;
    SharedPreferences prefs;
    String user, passwrd, access, api_key;
    Scan_Barcode_ILS_New scan_barcode_ils_new;
    Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity;
    Scan_Barcode_Outlabs scan_barcode_outlabs;
    ArrayList<BaseModel> selctedProductArraylist;
    ArrayList<ScannedBarcodeDetails> distinctspecimentlist;
    String barcode;
    int Flag;


    public SampleTypeBarcodeAdapter(Scan_Barcode_ILS_New scan_barcode_ils, ArrayList<BaseModel> selectedlist, ArrayList<ScannedBarcodeDetails> distinctspecimentlist) {
        this.scan_barcode_ils_new = scan_barcode_ils;
        this.context = scan_barcode_ils;
        this.selctedProductArraylist = selectedlist;
        this.distinctspecimentlist = distinctspecimentlist;
        Flag = 1;
    }

    public SampleTypeBarcodeAdapter(Scan_Barcode_Outlabs_Activity scan_barcode_outlabs_activity, ArrayList<ScannedBarcodeDetails> finalBarcodeDetailsList) {
        this.scan_barcode_outlabs_activity = scan_barcode_outlabs_activity;
        this.distinctspecimentlist = finalBarcodeDetailsList;
        this.context = scan_barcode_outlabs_activity;
        Flag = 2;
    }

    public SampleTypeBarcodeAdapter(Scan_Barcode_Outlabs scan_barcode_outlabs, ArrayList<ScannedBarcodeDetails> finalBarcodeDetailsList) {
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        this.distinctspecimentlist = finalBarcodeDetailsList;
        this.context = scan_barcode_outlabs;
        Flag = 3;
    }

    @Override
    public SampleTypeBarcodeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.sample_type_lay, viewGroup, false);
        SampleTypeBarcodeAdapter.ViewHolder viewHolder = new SampleTypeBarcodeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SampleTypeBarcodeAdapter.ViewHolder holder, int position) {
        prefs = context.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        if (distinctspecimentlist != null) {
            if (GlobalClass.isNumeric(distinctspecimentlist.get(position).getBarcode())) {
                holder.text_type.setText(distinctspecimentlist.get(position).getSpecimen_type());
                holder.text_barcode.setText(distinctspecimentlist.get(position).getBarcode());
            } else {
                holder.ll_sample.setVisibility(View.GONE);
            }
        }
        holder.img_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Flag == 1) {
                    scan_barcode_ils_new.imgActionClicked(position);
                } else if (Flag == 2) {
                    scan_barcode_outlabs_activity.imgActionClicked(position);
                } else if (Flag == 3) {
                    scan_barcode_outlabs.imgActionClicked(position);
                }

            }
        });
        if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), Constants.EDTA)) {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.edta)));
        } else if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), Constants.SERUM)) {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.serum)));
        } else if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), Constants.FLUORIDE)) {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.flouride)));
        } else if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), Constants.URINE)) {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.urine)));
        } else if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), Constants.LITHIUMHEPARIN)) {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.lithium)));
        } else if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), Constants.SODIUMHEPARIN)) {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.sodium)));
        } else /*if (GlobalClass.checkEqualIgnoreCase(distinctspecimentlist.get(position).getSpecimen_type(), "OTHERS"))*/ {
            holder.sample_indicator.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, context.getResources().getColor(R.color.other)));
        }

    }

    @Override
    public int getItemCount() {

            return distinctspecimentlist.size();

    }

    @Override
    public void barcodeDetails(Context v, String s) {
        GlobalClass.getscannedData = s;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_type, text_barcode;
        ImageView img_action, sample_indicator;
        LinearLayout ll_sample;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_type = itemView.findViewById(R.id.text_type);
            text_barcode = itemView.findViewById(R.id.text_barcode);
            img_action = itemView.findViewById(R.id.img_action);
            sample_indicator = itemView.findViewById(R.id.sample_indicator);
            ll_sample = itemView.findViewById(R.id.ll_sample);
        }
    }
}
