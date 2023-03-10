package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ConatctsResponseModel;
import com.example.e5322.thyrosoft.R;

import java.io.InputStream;
import java.util.ArrayList;

import static com.example.e5322.thyrosoft.API.Api.imgURL;

public class Company_Adapter extends RecyclerView.Adapter<Company_Adapter.ViewHolder> {

    private ArrayList<ConatctsResponseModel.ContactArrayListDTO> contact_array_lists_to_pass;
    // private ArrayList<Cmpdt_Model.ContactArrayListBean> contact_array_lists_to_pass;
    private Context context1;
    private Bitmap bmp;
    private GlobalClass globalClass;

    @NonNull
    @Override
    public Company_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_single_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public Company_Adapter(Context context, ArrayList<ConatctsResponseModel.ContactArrayListDTO> contact_array_lists_to_pass) {
        this.contact_array_lists_to_pass = contact_array_lists_to_pass;
        this.context1 = context;
        globalClass = new GlobalClass(context);
    }

    @Override
    public void onBindViewHolder(@NonNull final Company_Adapter.ViewHolder holder, final int position) {

        holder.name.setText(""+contact_array_lists_to_pass.get(position).getNAME());
        holder.designation.setText(""+contact_array_lists_to_pass.get(position).getDESIGNATION());
        holder.role.setText("Role: " + contact_array_lists_to_pass.get(position).getROLE());
        holder.email_id.setText(""+contact_array_lists_to_pass.get(position).getEMAIL_ID().toLowerCase());

        Linkify.addLinks(holder.email_id, Linkify.EMAIL_ADDRESSES);
        holder.email_id.setMovementMethod(LinkMovementMethod.getInstance());

        if (contact_array_lists_to_pass.get(position).getCATEGORY().equals("MANAGER")) {
            holder.mobile_number.setVisibility(View.GONE);
        } else {
            if (contact_array_lists_to_pass.get(position).getMOBILE() != null) {
                holder.mobile_number.setVisibility(View.VISIBLE);
                holder.mobile_number.setText("Mobile number: " + contact_array_lists_to_pass.get(position).getMOBILE());
            } else {
                holder.mobile_number.setVisibility(View.GONE);
            }
        }
//        mobile_number.setText(contact_array_lists_to_pass.get(position).get())
        String url = imgURL + contact_array_lists_to_pass.get(position).getECODE() + ".jpg";
        GlobalClass.DisplayImgWithPlaceholderFromURL(((Activity) context1), url, holder.profile, R.drawable.userprofile);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView profile;

        public DownloadImageTask(ImageView bmImage) {
            this.profile = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            profile.setImageBitmap(result);
        }
    }


    @Override
    public int getItemCount() {
        return contact_array_lists_to_pass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, designation, role, email_id, mobile_number;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            designation = (TextView) itemView.findViewById(R.id.designation);
            role = (TextView) itemView.findViewById(R.id.role);
            email_id = (TextView) itemView.findViewById(R.id.email_id);
            mobile_number = (TextView) itemView.findViewById(R.id.mobile_number);
            profile = (ImageView) itemView.findViewById(R.id.profile);
        }
    }

}
