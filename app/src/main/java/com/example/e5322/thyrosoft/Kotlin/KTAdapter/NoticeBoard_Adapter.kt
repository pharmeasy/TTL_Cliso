package com.example.e5322.thyrosoft.Kotlin.KTAdapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model
import com.example.e5322.thyrosoft.R
import java.util.*

class NoticeBoard_Adapter(var mContext: Context, private val noticeboard_simple_models_list: ArrayList<NoticeBoard_Model>,
                          var msgCode: String) : RecyclerView.Adapter<NoticeBoard_Adapter.ViewHolder>() {
    private val resource = 0
    private val inflater: LayoutInflater? = null
    var prefs: SharedPreferences? = null
    var messages: String? = null
    var resId: String? = null
    var response1: String? = null
    var ack_code: String? = null
    var user: String? = null
    var passwrd: String? = null
    var access: String? = null
    var api_key: String? = null
    var TAG = NoticeBoard_Adapter::class.java.simpleName.toString()
    var refreshNoticeBoard: RefreshNoticeBoard? = null

    fun clickListerforAckNoticeboard(refreshNoticeBoard: RefreshNoticeBoard?) {
        this.refreshNoticeBoard = refreshNoticeBoard
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var ack_id: LinearLayout
        var parent_ll: RelativeLayout
        var senderId: TextView
        var msgtext: TextView
        var notice_date: TextView
        //var linear: LinearLayout

        init {
            ack_id = v.findViewById<View>(R.id.ack_id) as LinearLayout
            parent_ll = v.findViewById<View>(R.id.parent_ll) as RelativeLayout
            senderId = v.findViewById<View>(R.id.senderId) as TextView
            msgtext = v.findViewById<View>(R.id.msgtext) as TextView
            notice_date = v.findViewById<View>(R.id.notice_date) as TextView
//            linear = v.findViewById<View>(R.id.linear) as LinearLayout
            prefs = mContext.getSharedPreferences("Userdetails", Context.MODE_PRIVATE)
            user = prefs!!.getString("Username", null)
            passwrd = prefs!!.getString("password", null)
            access = prefs!!.getString("ACCESS_TYPE", null)
            api_key = prefs!!.getString("API_KEY", null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.notice_board_singleview, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position % 2 == 1) {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#FFFFFF"))
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#F5D2D9"))
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        if (noticeboard_simple_models_list[0].messages[position].isAcknowledged == "Y") {
            holder.ack_id.visibility = View.GONE
        } else if (noticeboard_simple_models_list[0].messages[position].isAcknowledged == "N") {
            holder.ack_id.visibility = View.VISIBLE
        }
        holder.ack_id.setTag(noticeboard_simple_models_list[0].messages[position].messageCode);
        holder.ack_id.setOnClickListener {
            if (refreshNoticeBoard != null) {
                Log.e(TAG, "MESSAGE CODE---->" + noticeboard_simple_models_list[0].messages[position].messageCode);
                refreshNoticeBoard!!.onClickAcknowledge(noticeboard_simple_models_list[0].messages[position].messageCode)
            }
        }
        ack_code = noticeboard_simple_models_list[0].messages[position].isAcknowledged
        holder.senderId.text = noticeboard_simple_models_list[0].messages[position].enterBy
        holder.msgtext.text = Html.fromHtml(noticeboard_simple_models_list[0].messages[position].noticeMessage)
        Linkify.addLinks(holder.msgtext, Linkify.EMAIL_ADDRESSES)
        holder.msgtext.movementMethod = LinkMovementMethod.getInstance()
        holder.notice_date.text = noticeboard_simple_models_list[0].messages[position].noticeDate
    }

    override fun getItemCount(): Int {
        return noticeboard_simple_models_list[0].messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
