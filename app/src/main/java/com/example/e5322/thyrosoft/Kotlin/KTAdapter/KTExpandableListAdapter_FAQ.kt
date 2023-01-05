package com.example.e5322.thyrosoft.Kotlin.KTAdapter

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.e5322.thyrosoft.Kotlin.KTActivity.FAQ_activity
import com.example.e5322.thyrosoft.Kotlin.KTModels.PromoterFaq
import com.example.e5322.thyrosoft.R
import java.util.*


class KTExpandableListAdapter_FAQ (var faq_list: ArrayList<PromoterFaq>, var context: Context): BaseExpandableListAdapter() {
    private var faq_question: TextView? = null
    private var faq_question_answer: TextView? = null
    internal lateinit var img: ImageView



    override fun getGroupCount(): Int {
        return faq_list.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return faq_list[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return faq_list[groupPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            val infalInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.faq_title_name_single_view, null)
        }


        faq_question = convertView!!.findViewById(R.id.faq_question) as TextView
        img = convertView.findViewById(R.id.img) as ImageView
        img.setBackgroundResource(R.drawable.down_arrow)
        if (isExpanded) {
            img.setBackgroundResource(R.drawable.up_arrow)
        } else {
            img.setBackgroundResource(R.drawable.down_arrow)
        }
        faq_question!!.text = faq_list[groupPosition].Questions
        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView


        if (convertView == null) {
            val infalInflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.faq_child_name_single_view, null)
        }
        faq_question_answer = convertView!!.findViewById(R.id.faq_question_answer) as TextView

        faq_question_answer!!.text = Html.fromHtml(this.faq_list.get(groupPosition).Answers)


        Linkify.addLinks(faq_question_answer!!, Linkify.WEB_URLS)
        faq_question_answer!!.movementMethod = LinkMovementMethod.getInstance()

        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}