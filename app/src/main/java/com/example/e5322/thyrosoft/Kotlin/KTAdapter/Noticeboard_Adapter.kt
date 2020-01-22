package com.example.e5322.thyrosoft.Kotlin.KTAdapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.RequestQueue
import com.example.e5322.thyrosoft.Adapter.NoticeBoard_Adapter
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard
import com.example.e5322.thyrosoft.Kotlin.KTModels.Noticeboardmodel
import com.example.e5322.thyrosoft.R
import kotlinx.android.synthetic.main.notice_board_singleview.view.*

class Noticeboard_Adapter(context: Context, arrayNotice: ArrayList<Noticeboardmodel>, msgCode: String?) : RecyclerView.Adapter<Noticeviewholder>() {

    private val noticeboard_simple_models_list: java.util.ArrayList<Noticeboardmodel>? = null
    private val resource = 0
    private val inflater: LayoutInflater? = null
    var mContext: Context? = null
    var prefs: SharedPreferences? = null
    var messages: String? = null
    var resId: kotlin.String? = null
    var response1: kotlin.String? = null
    var ack_code: kotlin.String? = null
    var user: kotlin.String? = null
    var passwrd: kotlin.String? = null
    var access: kotlin.String? = null
    var api_key: kotlin.String? = null
    var PostQueOtp: RequestQueue? = null
    var msgCode: String? = null
    var TAG = NoticeBoard_Adapter::class.java.simpleName.toString()
    var refreshNoticeBoard: RefreshNoticeBoard? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Noticeviewholder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.notice_board_singleview, p0, false)

        prefs = mContext!!.getSharedPreferences("Userdetails", Context.MODE_PRIVATE)
        user = prefs!!.getString("Username", null)
        passwrd = prefs!!.getString("password", null)
        access = prefs!!.getString("ACCESS_TYPE", null)
        api_key = prefs!!.getString("API_KEY", null)

        return Noticeviewholder(view)
    }

    fun clickListerforAckNoticeboard(refreshNoticeBoard: RefreshNoticeBoard?) {
        this.refreshNoticeBoard = refreshNoticeBoard
    }


    override fun getItemCount(): Int {
        return noticeboard_simple_models_list!!.size
    }

    override fun onBindViewHolder(holder: Noticeviewholder, position: Int) {
        val noticeboardModel=noticeboard_simple_models_list!!.get(position)

        if (position % 2 == 1) {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.parent_ll.setBackgroundColor(Color.parseColor("#F5D2D9"))
        }

        if (noticeboardModel.messages.get(position).isAcknowledged.equals("Y")){
            holder.parent_ll.visibility=View.GONE
        }else if (noticeboardModel.messages.get(position).isAcknowledged.equals("N")){
            holder.parent_ll.visibility=View.VISIBLE
        }

        holder.ack_id.setOnClickListener {
            if (refreshNoticeBoard!=null){

            }
        }

    }
}

class Noticeviewholder(v: View) : RecyclerView.ViewHolder(v) {

    var ack_id = v.ack_id
    var parent_ll = v.parent_ll
    var senderId = v.senderId
    var msgtext = v.msgtext
    var notice_date = v.notice_date


}
