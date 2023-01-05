package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.Models.ScanBarcodeResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class ScannedBarcodeAdapter extends RecyclerView.Adapter<ScannedBarcodeAdapter.ViewHolder> {


    ArrayList<ScanBarcodeResponseModel.PouchCodeDTO> barcodeDTOS;
    private Activity activity;
    BrandAdapter.OnClickListener onClickListener;



    public ScannedBarcodeAdapter(Activity activity, ArrayList<ScanBarcodeResponseModel.PouchCodeDTO> brandName) {
        this.barcodeDTOS = brandName;
        this.activity = activity;
    }


    @Override
    public ScannedBarcodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanned_barcode_item, parent, false);
        return new ScannedBarcodeAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ScannedBarcodeAdapter.ViewHolder holder, final int position) {



        holder.tv_barcode.setText("" + barcodeDTOS.get(position).getBarcode());



    }


    @Override
    public int getItemCount() {
        return barcodeDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_barcode;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_barcode = itemView.findViewById(R.id.tv_barcode);


        }
    }

}

