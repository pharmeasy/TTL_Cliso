package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.e5322.thyrosoft.API.Product;
import com.example.e5322.thyrosoft.API.SqliteDatabase;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder>{
    private Context context;
    private List<Product> listProducts;
    private SqliteDatabase mDatabase;
    public ProductAdapter(Context context, List<Product> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
        mDatabase = new SqliteDatabase(context);
    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_firebase_notification, parent, false);
        return new ProductViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product singleProduct = listProducts.get(position);

        GlobalClass.SetText(holder.name,singleProduct.getName());
        GlobalClass.SetText(holder.product_id,singleProduct.getQuantity());

        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                mDatabase.deleteProduct(singleProduct.getId());
                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }
    @Override
    public int getItemCount() {
        return listProducts.size();
    }
    private void editTaskDialog(final Product product){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_prodct_layout, null);
        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText quantityField = (EditText)subView.findViewById(R.id.enter_quantity);

        if(product != null){
            GlobalClass.SetEditText(nameField,product.getName());
            GlobalClass.SetEditText(quantityField,product.getQuantity());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit product");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT PRODUCT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String quantity = quantityField.getText().toString();
                if(GlobalClass.isNull(name) || GlobalClass.isNull(quantity)){
                    GlobalClass.showTastyToast((Activity)context, ToastFile.something_went_wrong,2);
                }
                else{
                    mDatabase.updateProduct(new Product(product.getId(), name, quantity));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobalClass.showTastyToast((Activity)context, MessageConstants.TASK_CANCEL, 2);
            }
        });
        builder.show();
    }
}