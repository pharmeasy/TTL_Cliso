package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
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


    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList, Activity activity, RecommondedTestInterface recommondedTestInterface) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.recolist = recoList;
        this.mActivity = activity;
        this.recommondedTestInterface = recommondedTestInterface;
    }

    public ProductRecommendedAdapter(ProductLisitngActivityNew productLisitngActivityNew, ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList, Activity mActivity) {
        this.productLisitngActivityNew = productLisitngActivityNew;
        this.recolist = recoList;
        this.mActivity = mActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.product_recommend_lay, viewGroup, false);
        ProductRecommendedAdapter.ViewHolder viewHolder = new ProductRecommendedAdapter.ViewHolder(view, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Global.setTextview(viewHolder.txt_reco_message, recolist.get(i).getRecommendationMsg());
//        Global.setTextview(viewHolder.txt_recoTestName, recolist.get(i).getTestsRecoDisplayName());
        Global.setTextview(viewHolder.txt_recoTestRate, mActivity.getString(R.string.rupeeSymbol) + " " + recolist.get(i).getPrice());
        viewHolder.rb_recoTestName.setText(recolist.get(i).getTestsRecoDisplayName());
        viewHolder.rb_recoTestName.setChecked(i == selectedPosition);
    }


    @Override
    public int getItemCount() {
      return recolist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_recoTestName, txt_reco_message, txt_recoTestRate;
        RadioButton rb_recoTestName;
        RadioGroup rgb_recoTest;
        private ProductRecommendedAdapter productRecommendedAdapter;

        public ViewHolder(View itemView, ProductRecommendedAdapter productRecommendedAdapter) {
            super(itemView);

            this.productRecommendedAdapter = productRecommendedAdapter;
            /*txt_recoTestName = itemView.findViewById(R.id.txt_recoTestName);*/
            txt_reco_message = itemView.findViewById(R.id.txt_reco_message);
            txt_recoTestRate = itemView.findViewById(R.id.txt_recoTestRate);
            rb_recoTestName = itemView.findViewById(R.id.rb_recoTestName);
            rgb_recoTest = itemView.findViewById(R.id.rgb_recoTest);

            rb_recoTestName.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            boolean ischecked = rgb_recoTest.isSelected();
            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
            productRecommendedAdapter.onItemHolderClick(this);
            recommondedTestInterface.SelectedTest(recolist.get(selectedPosition),ischecked);
        }
    }

    public interface RecommondedTestInterface {
        void SelectedTest(GetProductsRecommendedResModel.ProductListDTO testName, boolean ischecked);
    }

    private void onItemHolderClick(ViewHolder viewHolder) {

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, viewHolder.rb_recoTestName, viewHolder.getAdapterPosition(), viewHolder.getItemId());
        }
    }
}
