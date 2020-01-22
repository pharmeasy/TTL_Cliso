package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.R;

import java.io.File;
import java.util.List;

public class TRFDisplayAdapter extends RecyclerView.Adapter<TRFDisplayAdapter.MyViewHolder> {
    private Activity activity;
    private List<TRFModel> trfModelArrayList;
    private OnItemClickListener onItemClickListener;
    private Dialog dialog;

    public TRFDisplayAdapter(Activity activity, List<TRFModel> trfModelArrayList) {
        this.activity = activity;
        this.trfModelArrayList = trfModelArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trf_item, viewGroup, false);
        return new TRFDisplayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int pos) {

        myViewHolder.tv_productName.setText(trfModelArrayList.get(pos).getProduct());
        myViewHolder.tv_productName.setPaintFlags(myViewHolder.tv_productName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        File file = trfModelArrayList.get(pos).getTrf_image();
        if (file != null) {
            myViewHolder.img_preview.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.img_preview.setVisibility(View.INVISIBLE);
        }


        myViewHolder.tv_productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = trfModelArrayList.get(pos).getProduct();
                if (productName != null && !productName.isEmpty()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onUploadClick(productName);
                    }
                }
            }
        });

        myViewHolder.img_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = trfModelArrayList.get(pos).getTrf_image();
                if (file != null)
                    GlobalClass.showImageDialog(activity, file,"",1);
                else
                    Toast.makeText(activity, "Image not found", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return trfModelArrayList.size();
    }

    public interface OnItemClickListener {
        void onUploadClick(String product_name);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_productName;
        ImageView img_preview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_productName = itemView.findViewById(R.id.tv_productName);
            img_preview = itemView.findViewById(R.id.img_preview);
        }
    }
}
