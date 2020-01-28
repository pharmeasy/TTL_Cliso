package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQandANSArray;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class ExpandableListAdapter_FAQ extends BaseExpandableListAdapter {
    private Context _context;
    ArrayList<FAQandANSArray> faq_question_answer_list;
    private TextView faq_question;
    private TextView faq_question_answer;
    ImageView img;

    public ExpandableListAdapter_FAQ(ArrayList<FAQandANSArray> faq_list, Context context) {
        this.faq_question_answer_list = faq_list;
        this._context = context;
    }

    @Override
    public int getGroupCount() {
        return faq_question_answer_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faq_question_answer_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return faq_question_answer_list.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_title_name_single_view, null);
        }


        faq_question = (TextView) convertView.findViewById(R.id.faq_question);
        img = (ImageView) convertView.findViewById(R.id.img);
        img.setBackgroundResource(R.drawable.down_arrow);
        if (isExpanded) {
            img.setBackgroundResource(R.drawable.up_arrow);
        } else {
            img.setBackgroundResource(R.drawable.down_arrow);
        }
        faq_question.setText(faq_question_answer_list.get(groupPosition).getAck_TITLE());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.faq_child_name_single_view, null);
        }
        faq_question_answer = (TextView) convertView.findViewById(R.id.faq_question_answer);

        faq_question_answer.setText(Html.fromHtml(this.faq_question_answer_list.get(groupPosition).getFAQ()));


        Linkify.addLinks(faq_question_answer, Linkify.WEB_URLS);
//        EmailAddressResultParser.parseResult(holder.msgtext,)
        faq_question_answer.setMovementMethod(LinkMovementMethod.getInstance());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}