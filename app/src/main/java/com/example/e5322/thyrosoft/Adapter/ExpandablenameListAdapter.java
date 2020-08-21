package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by e5426@thyrocare.com on 17/5/18.
 */

public class ExpandablenameListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> _listDataHeader; // header titles
    private HashMap<ArrayList, ArrayList<billingDetailsModel>> _listDataChild;
    public  ArrayList<billingDetailsModel> billingDETArray;
    public ExpandablenameListAdapter(Context context, ArrayList<String> listDataHeader,
                                     HashMap<ArrayList, ArrayList<billingDetailsModel>> listChildData, ArrayList<billingDetailsModel> billingDETArray) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.billingDETArray=billingDETArray;
    }


    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
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

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.billing_det_row_header, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView expand_img = (ImageView) convertView.findViewById(R.id.expand_img);

        GlobalClass.SetText(lblListHeader,headerTitle);


        if (isExpanded) {
            expand_img.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp);
        }else{
            expand_img.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        billingDETArray.get(childPosition);

        try {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.billing_det_row, null);
            }

            TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
            TextView test = (TextView) convertView.findViewById(R.id.test);
            TextView billedamt = (TextView) convertView.findViewById(R.id.billedamt);
            TextView collamount = (TextView) convertView.findViewById(R.id.collamount);
            TextView type = (TextView) convertView.findViewById(R.id.type);
            TextView ref = (TextView) convertView.findViewById(R.id.refID);



            GlobalClass.SetText(barcode,billingDETArray.get(groupPosition).getBarcode());
            GlobalClass.SetText(test,billingDETArray.get(groupPosition).getTests());
            GlobalClass.SetText(billedamt,billingDETArray.get(groupPosition).getBilledAmount());
            GlobalClass.SetText(collamount,billingDETArray.get(groupPosition).getCollectedAmount());
            GlobalClass.SetText(type,billingDETArray.get(groupPosition).getWoetype());

            ref.setVisibility(View.GONE);
            GlobalClass.SetText(ref,billingDETArray.get(groupPosition).getRefId());

            Log.v("TAG","get data ***" + _listDataChild.get(Constants.barcode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
