package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Fragment.FilterReport;
import com.example.e5322.thyrosoft.Interface.CAlendar_Inteface;
import com.example.e5322.thyrosoft.Models.getAllDays;
import com.example.e5322.thyrosoft.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomCalendarAdapter extends RecyclerView.Adapter<CustomCalendarAdapter.ViewHolder> {
    Activity context;
    ArrayList<getAllDays> mgetAllDays;
    private int getPosition = -1;
    private String getDataofThatPosition;
    CAlendar_Inteface listener;


    public CustomCalendarAdapter(Activity mContext, ArrayList<getAllDays> selectedMonthData,CAlendar_Inteface listener) {
        this.context = mContext;
        this.mgetAllDays = selectedMonthData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomCalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.calendar_ll, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final CustomCalendarAdapter.ViewHolder holder, final int position) {

        holder.dates_txt.setText(mgetAllDays.get(position).getStrDates());
        holder.days_txt.setText(mgetAllDays.get(position).getStrDays());
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String setDtae = sdf.format(d);
        if (setDtae.equalsIgnoreCase(mgetAllDays.get(position).getStrDates())) {
            holder.dates_txt.setBackgroundResource(R.drawable.circle);
            holder.dates_txt.setTextColor(Color.WHITE);

            if(FilterReport.callOnce==false){
                listener.onPassDateandPos(position,mgetAllDays.get(position).getStrWholeDate());
                FilterReport.callOnce=true;
            }

        }
        if (getPosition == position) {
            if (getDataofThatPosition.equalsIgnoreCase(mgetAllDays.get(position).getStrDates())) {
                holder.dates_txt.setBackgroundResource(R.drawable.circle);
                holder.dates_txt.setTextColor(Color.WHITE);
                holder.days_txt.setTextColor(Color.GRAY);
            } else {
                holder.dates_txt.setBackgroundResource(R.drawable.circle);
                holder.dates_txt.setTextColor(Color.WHITE);
                holder.days_txt.setTextColor(Color.GRAY);

                if (setDtae.equalsIgnoreCase(mgetAllDays.get(position).getStrDates())) {

                    if (getPosition != -1) {
                        holder.dates_txt.setBackgroundResource(R.drawable.faint_circle_drawable);
                        holder.dates_txt.setTextColor(Color.WHITE);
                    } else {
                        holder.dates_txt.setBackgroundResource(R.drawable.circle);
                        holder.dates_txt.setTextColor(Color.WHITE);
                    }
                }
            }
        } else {
            holder.dates_txt.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.dates_txt.setTextColor(Color.BLACK);
            holder.days_txt.setTextColor(Color.GRAY);
            if (setDtae.equalsIgnoreCase(mgetAllDays.get(position).getStrDates())) {
                if (getPosition != -1) {
                    holder.dates_txt.setBackgroundResource(R.drawable.faint_circle_drawable);
                    holder.dates_txt.setTextColor(Color.WHITE);
                } else {
                    holder.dates_txt.setBackgroundResource(R.drawable.circle);
                    holder.dates_txt.setTextColor(Color.WHITE);
                }
            }
        }
        holder.dates_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosition = position;
                getDataofThatPosition = holder.dates_txt.getText().toString();
                if (getDataofThatPosition.equalsIgnoreCase(mgetAllDays.get(position).getStrDates())) {
                    holder.dates_txt.setBackgroundResource(R.drawable.circle);
                    holder.dates_txt.setTextColor(Color.WHITE);
                    holder.days_txt.setTextColor(Color.GRAY);
                } else {
                    holder.dates_txt.setBackgroundResource(R.drawable.circle);
                    holder.dates_txt.setTextColor(Color.WHITE);
                    holder.days_txt.setTextColor(Color.GRAY);
                }
                listener.onPassDateandPos(position,mgetAllDays.get(position).getStrWholeDate());
                notifyDataSetChanged();


            }
        });
    }

    @Override
    public int getItemCount() {
        return mgetAllDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView days_txt, dates_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            days_txt = (TextView) itemView.findViewById(R.id.days_txt);
            dates_txt = (TextView) itemView.findViewById(R.id.dates_txt);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
