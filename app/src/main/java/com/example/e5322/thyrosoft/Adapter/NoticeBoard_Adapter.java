package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by e5322 on 28-05-2018.
 */

public class NoticeBoard_Adapter extends RecyclerView.Adapter<NoticeBoard_Adapter.ViewHolder> {
    private ArrayList<NoticeBoard_Model> noticeboard_simple_models_list;
    Context mContext;
    SharedPreferences prefs;
    String ack_code, user, passwrd, access, api_key;
    String msgCode;
    String TAG = NoticeBoard_Adapter.class.getSimpleName().toString();
    RefreshNoticeBoard refreshNoticeBoard;

    public NoticeBoard_Adapter(Context context, ArrayList<NoticeBoard_Model> array_notice, String msgCode1) {
        mContext = context;
        noticeboard_simple_models_list = array_notice;
        msgCode = msgCode1;

    }

    public void clickListerforAckNoticeboard(RefreshNoticeBoard refreshNoticeBoard) {
        this.refreshNoticeBoard = refreshNoticeBoard;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout ack_id;
        public RelativeLayout parent_ll;
        public TextView senderId;
        public TextView msgtext;
        public TextView notice_date;

        public LinearLayout linear;

        public ViewHolder(View v) {
            super(v);
            ack_id = (LinearLayout) v.findViewById(R.id.ack_id);
            parent_ll = (RelativeLayout) v.findViewById(R.id.parent_ll);
            senderId = (TextView) v.findViewById(R.id.senderId);
            msgtext = (TextView) v.findViewById(R.id.msgtext);
            notice_date = (TextView) v.findViewById(R.id.notice_date);

            linear = (LinearLayout) v.findViewById(R.id.linear);
            prefs = mContext.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
            user = prefs.getString("Username", "");
            passwrd = prefs.getString("password", "");
            access = prefs.getString("ACCESS_TYPE", "");
            api_key = prefs.getString("API_KEY", "");


        }
    }

    @NonNull
    @Override
    public NoticeBoard_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.notice_board_singleview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        NoticeBoard_Adapter.ViewHolder vh = new NoticeBoard_Adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final NoticeBoard_Adapter.ViewHolder holder, final int position) {

        if (position % 2 == 1) {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#F5D2D9"));
        }

        if (!GlobalClass.isNull(noticeboard_simple_models_list.get(0).getMessages()[position].getIsAcknowledged())
                && noticeboard_simple_models_list.get(0).getMessages()[position].getIsAcknowledged().equalsIgnoreCase("Y")) {
            holder.ack_id.setVisibility(View.GONE);
        } else if (!GlobalClass.isNull(noticeboard_simple_models_list.get(0).getMessages()[position].getIsAcknowledged())
                && noticeboard_simple_models_list.get(0).getMessages()[position].getIsAcknowledged().equalsIgnoreCase("N")) {
            holder.ack_id.setVisibility(View.VISIBLE);
        }

        holder.ack_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refreshNoticeBoard != null) {
                    refreshNoticeBoard.onClickAcknowledge(noticeboard_simple_models_list.get(0).getMessages()[position].getMessageCode());
                }

            }
        });

        ack_code = noticeboard_simple_models_list.get(0).getMessages()[position].getIsAcknowledged();

        GlobalClass.SetText(holder.senderId, noticeboard_simple_models_list.get(0).getMessages()[position].getEnterBy());
        GlobalClass.SetHTML(holder.msgtext, noticeboard_simple_models_list.get(0).getMessages()[position].getNoticeMessage());


        Linkify.addLinks(holder.msgtext, Linkify.EMAIL_ADDRESSES);
        holder.msgtext.setMovementMethod(LinkMovementMethod.getInstance());
        GlobalClass.SetText(holder.notice_date, noticeboard_simple_models_list.get(0).getMessages()[position].getNoticeDate());

    }

    @Override
    public int getItemCount() {
        return noticeboard_simple_models_list.get(0).getMessages().length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
