package com.example.e5322.thyrosoft.Kotlin.KTAdapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.Kotlin.KTAdapter.KtCompanyAdapter.Viewholder
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTCompanydt_Response
import com.example.e5322.thyrosoft.R
import java.util.*

class KtCompanyAdapter(var mcontect: Context, var cmplist: ArrayList<KTCompanydt_Response.ContactArray>) : RecyclerView.Adapter<Viewholder>() {


    inner class Viewholder(v: View) : RecyclerView.ViewHolder(v) {
        var profile: ImageView? = null
        var name: TextView? = null
        var designation: android.widget.TextView? = null
        var role: android.widget.TextView? = null
        var email_id: android.widget.TextView? = null
        var mobile_number: android.widget.TextView? = null

        init {
            name = v.findViewById<View>(R.id.name) as TextView
            designation = v.findViewById<View>(R.id.designation) as TextView
            role = v.findViewById<View>(R.id.role) as TextView
            email_id = v.findViewById<View>(R.id.email_id) as TextView
            mobile_number = v.findViewById<View>(R.id.mobile_number) as TextView
            profile = v.findViewById<View>(R.id.profile) as ImageView
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Viewholder {
        val inflater = LayoutInflater.from(p0.context)
        val v = inflater.inflate(R.layout.profile_single_layout, p0, false)
        // set the view's size, margins, paddings and layout parameters
        return Viewholder(v)

    }

    override fun getItemCount(): Int {
        return cmplist.size
    }

    override fun onBindViewHolder(viewholder: Viewholder, p1: Int) {

        viewholder.name!!.text = cmplist.get(p1).NAME
        viewholder.designation!!.text = cmplist.get(p1).DESIGNATION
        viewholder.role!!.text = "Role: "+cmplist.get(p1).ROLE
        viewholder.email_id!!.text = cmplist.get(p1).EMAIL_ID

        Linkify.addLinks(viewholder.email_id, Linkify.EMAIL_ADDRESSES)
        viewholder.email_id!!.movementMethod=LinkMovementMethod.getInstance()

        if (cmplist.get(p1).CATEGORY.equals("MANAGER", true)) {
            viewholder.mobile_number!!.setVisibility(View.GONE)
        } else {
            if (cmplist.get(p1).MOBILE!= null) {
                viewholder.mobile_number!!.setVisibility(View.VISIBLE)
                viewholder.mobile_number!!.text="Mobile number: " + cmplist.get(p1).MOBILE
            } else {
                viewholder.mobile_number!!.setVisibility(View.GONE)
            }
        }

        val url = Api.imgURL + cmplist.get(p1).ECODE + ".jpg"
        if (url != null) {
            KTGlobalclass().DisplayImage(mcontect, url, viewholder.profile)
        } else {
            Glide.with(mcontect)
                    .load("")
                    .placeholder(mcontect.getResources().getDrawable(R.drawable.userprofile))
                    .into(viewholder.profile)
        }
    }
}