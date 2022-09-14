package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.ProductLisitngActivityNew;

import java.util.ArrayList;

public class ProductRecommendedAdapter extends RecyclerView.Adapter<ProductRecommendedAdapter.ViewHolder> {


    ProductLisitngActivityNew productLisitngActivityNew;
    ArrayList<GetProductsRecommendedResModel.ProductListDTO> recolist;
    Activity mActivity;
    private int selectedPosition = -1;
    ProductRecommendedAdapter.RecommondedTestInterface recommondedTestInterface;
    private AdapterView.OnItemClickListener onItemClickListener;
    boolean isMainRBChecked = false;
    int reco_adapterPOS;

    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList, Activity activity, RecommondedTestInterface recommondedTestInterface) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.recolist = recoList;
        this.mActivity = activity;
        this.recommondedTestInterface = recommondedTestInterface;
    }

    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList, Activity mActivity, boolean isMainRBChecked, RecommondedTestInterface recommondedTestInterface) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.recolist = recoList;
        this.mActivity = mActivity;
        this.isMainRBChecked = isMainRBChecked;
        this.recommondedTestInterface = recommondedTestInterface;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.product_recommend_lay, viewGroup, false);
        ProductRecommendedAdapter.ViewHolder viewHolder = new ProductRecommendedAdapter.ViewHolder(view, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Global.setTextview(viewHolder.txt_reco_message, recolist.get(position).getRecommendationMsg());
//        Global.setTextview(viewHolder.txt_recoTestName, recolist.get(position).getTestsRecoDisplayName());
        Global.setTextview(viewHolder.txt_recoTestRate, mActivity.getString(R.string.rupeeSymbol) + " " + recolist.get(position).getPrice());
        viewHolder.rb_recoTestName.setText(recolist.get(position).getTestsRecoDisplayName());
        if (isMainRBChecked) {
            viewHolder.rb_recoTestName.setChecked(false);
            viewHolder.rb_recoTestName.setSelected(false);
        }
        viewHolder.rb_recoTestName.setChecked(position == selectedPosition);
        viewHolder.rb_recoTestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = viewHolder.getAdapterPosition();
                notifyDataSetChanged();
                recommondedTestInterface.SelectedTest(recolist.get(position));
            }
        });


    }


    @Override
    public int getItemCount() {
        return recolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_recoTestName, txt_reco_message, txt_recoTestRate;
        CheckBox rb_recoTestName; //TODO added checkbox as radiobutton due to task check and uncheck req.
        private ProductRecommendedAdapter productRecommendedAdapter;

        public ViewHolder(View itemView, ProductRecommendedAdapter productRecommendedAdapter) {
            super(itemView);

            this.productRecommendedAdapter = productRecommendedAdapter;
            /*txt_recoTestName = itemView.findViewById(R.id.txt_recoTestName);*/
            txt_reco_message = itemView.findViewById(R.id.txt_reco_message);
            txt_recoTestRate = itemView.findViewById(R.id.txt_recoTestRate);
            rb_recoTestName = itemView.findViewById(R.id.rb_recoTestName);


            // rb_recoTestName.setOnClickListener(this);


        }
/*
        @Override
        public void onClick(View view) {
            ischecked = true;
            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
            productRecommendedAdapter.onItemHolderClick(this);
            recommondedTestInterface.SelectedTest(recolist.get(selectedPosition));
        }*/


    }


    public interface RecommondedTestInterface {
        void SelectedTest(GetProductsRecommendedResModel.ProductListDTO testName);
    }

    private void onItemHolderClick(ViewHolder viewHolder) {

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, viewHolder.rb_recoTestName, viewHolder.getAdapterPosition(), viewHolder.getItemId());
        }
    }

}
