package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.Models.BarcodeResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {


    ArrayList<BarcodeResponseModel.BarcodeDTO> barcodeDTOS;
    private Activity activity;
    OnClickListener onClickListener;



    public BarcodeAdapter(Activity activity, ArrayList<BarcodeResponseModel.BarcodeDTO> brandName) {
        this.barcodeDTOS = brandName;
        this.activity = activity;
    }




    @Override
    public BarcodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barcode_item, parent, false);
        return new BarcodeAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final BarcodeAdapter.ViewHolder holder, final int position) {

        holder.cb_check.setText("" + barcodeDTOS.get(position).getBarcode());


        holder.cb_check.setChecked(barcodeDTOS.get(position).isSelected());

        holder.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /* if (GlobalClass.CheckArrayList(GlobalClass.barcodelist)) {
                        for (int i = 0; i < GlobalClass.barcodelist.size(); i++) {
                            if (barcodeDTOS.get(position).getBarcode().equalsIgnoreCase(GlobalClass.barcodelist.get(i))) {
                                GlobalClass.barcodelist.remove(i);
                            }
                        }
                    }*/
                barcodeDTOS.get(position).setSelected(isChecked);
            }
        });
        if (onClickListener!=null){
            onClickListener.onchecked(barcodeDTOS);
        }


    }


    @Override
    public int getItemCount() {
        return barcodeDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox cb_check;


        public ViewHolder(View itemView) {
            super(itemView);
            cb_check = itemView.findViewById(R.id.cb_check);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnClickListener clickListener) {
        this.onClickListener = clickListener;
    }

    public interface OnClickListener {
        void onchecked(ArrayList<BarcodeResponseModel.BarcodeDTO> barcodeDTOS);
    }


}


