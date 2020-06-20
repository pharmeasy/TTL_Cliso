package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.BroadcastViewPagerActivity;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by e6637@thyrocare.com on 20/3/19.
 */

public class BroadcastAdapter extends RecyclerView.Adapter<BroadcastAdapter.MyViewholder> {

    private final Context mContext;
    ArrayList<NoticeBoard_Model> broadCastArrayList;

    public BroadcastAdapter(Context context, ArrayList<NoticeBoard_Model> array_notice) {
        mContext = context;
        broadCastArrayList = array_notice;
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        LinearLayout parent_ll;
        TextView tvHeader,tvPostedOn, tvPostedBy;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            parent_ll = (LinearLayout) itemView.findViewById(R.id.parent_ll);
            tvPostedOn = (TextView) itemView.findViewById(R.id.tvPostedOn);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tvPostedBy);
            tvHeader = (TextView) itemView.findViewById(R.id.tvHeader);

        }
    }

    @NonNull
    @Override
    public BroadcastAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.broadcat_single_row, viewGroup, false);
        BroadcastAdapter.MyViewholder vh = new BroadcastAdapter.MyViewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastAdapter.MyViewholder holder, final int position) {
        if (position % 3 == 0) {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#A0C4FF"));
        }
        else if (position % 3 == 1){
            holder.parent_ll.setBackgroundColor(Color.parseColor("#FFB848"));
        }
        else {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#F7DC6F"));
        }


        holder.tvHeader.setText(Html.fromHtml(broadCastArrayList.get(0).getMessages()[position].getNoticeMessage()));
        holder.tvPostedBy.setText("Posted by "+broadCastArrayList.get(0).getMessages()[position].getEnterBy());
        //holder.msgtext.setText(Html.fromHtml(broadCastArrayList.get(0).getMessages()[position].getNoticeMessage()));

        //Linkify.addLinks(holder.msgtext, Linkify.EMAIL_ADDRESSES);
        //holder.msgtext.setMovementMethod(LinkMovementMethod.getInstance());

        holder.tvPostedOn.setText("Posted on "+broadCastArrayList.get(0).getMessages()[position].getNoticeDate());

        holder.parent_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                String gsonString=gson.toJson(broadCastArrayList);
                //Intent i = new Intent(mContext, BroadcastDataActivity.class);
                Intent i = new Intent(mContext, BroadcastViewPagerActivity.class);
                i.putExtra("position", position);
                i.putExtra("gsonString", gsonString);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return broadCastArrayList.get(0).getMessages().length;
    }


}
