package com.example.e5322.thyrosoft.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView name,product_id;
    public ImageView deleteProduct;
    public ProductViewHolder(View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.product_name);
        product_id = (TextView)itemView.findViewById(R.id.product_id);
        deleteProduct = (ImageView)itemView.findViewById(R.id.delete_product);
    }
}
