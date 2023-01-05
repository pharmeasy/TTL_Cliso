package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.ClisoInterfaces.AppInterfaces;
import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ProductLisitngActivityNew;

import java.util.ArrayList;

public class ProductRecommendedAdapter extends RecyclerView.Adapter<ProductRecommendedAdapter.ViewHolder> {

    ProductLisitngActivityNew productLisitngActivityNew;
    ArrayList<GetProductsRecommendedResModel.ProductListDTO> listDTOS;
    Activity mActivity;
    private int lastCheckedPostion = -1;
     AppInterfaces.OnClickRecoTestListner onClickRecoTestListner;
    boolean isMainChecked = false;
    boolean makeMainRBChecked = false;
    String recoSelectedTest = "";

    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> listDTOS, Activity mActivity) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.listDTOS = listDTOS;
        this.mActivity = mActivity;
    }

    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> listDTOS, Activity mActivity,/* boolean isMainChecked,*/ AppInterfaces.OnClickRecoTestListner onClickRecoTestListner) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.listDTOS = listDTOS;
        this.mActivity = mActivity;
        this.isMainChecked = isMainChecked;
        this.onClickRecoTestListner = onClickRecoTestListner;

    }

    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> listDTOS, Activity mActivity, boolean isMainChecked) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.listDTOS = listDTOS;
        this.mActivity = mActivity;
        this.isMainChecked = isMainChecked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.product_recommend_lay, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Global.setTextview(viewHolder.txt_reco_message, listDTOS.get(position).getRecommendationMsg());
        Global.setTextview(viewHolder.txt_recoTestRate, mActivity.getString(R.string.rupeeSymbol) + " " + listDTOS.get(position).getPrice());
        viewHolder.rb_recoTestName.setText(listDTOS.get(position).getTestsRecoDisplayName());

        viewHolder.rb_recoTestName.setChecked(position == lastCheckedPostion);

        viewHolder.rb_recoTestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int CopyOfLastCheckedPosition = lastCheckedPostion;
                lastCheckedPostion = viewHolder.getAdapterPosition();
                notifyItemChanged(CopyOfLastCheckedPosition);
                notifyItemChanged(lastCheckedPostion);
                isMainChecked = true;
                     recoSelectedTest = listDTOS.get(position).getTestsRecommended();
            }
        });

        viewHolder.rb_recoTestName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    /*ProductLisitngActivityNew*/
                    if (onClickRecoTestListner != null) {
                        makeMainRBChecked=true;
                        recoSelectedTest = listDTOS.get(position).getTestsRecommended();
                        onClickRecoTestListner.onchecked(listDTOS.get(position), false, makeMainRBChecked, recoSelectedTest);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_reco_message, txt_recoTestRate;
        RadioButton rb_recoTestName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_reco_message = itemView.findViewById(R.id.txt_reco_message);
            txt_recoTestRate = itemView.findViewById(R.id.txt_recoTestRate);
            rb_recoTestName = itemView.findViewById(R.id.rb_recoTestName);

        }
    }


}
