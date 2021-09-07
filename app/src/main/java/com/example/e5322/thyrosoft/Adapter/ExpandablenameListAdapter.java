package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
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
    // child data in format of header title, child title
    private HashMap<ArrayList, ArrayList<billingDetailsModel>> _listDataChild;

    public ExpandablenameListAdapter(Context context, ArrayList<String> listDataHeader,
                                     HashMap<ArrayList, ArrayList<billingDetailsModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
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
        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView expand_img = (ImageView) convertView.findViewById(R.id.expand_img);
        lblListHeader.setText(headerTitle);


        if (isExpanded) {
            expand_img.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp);
        }else{
            expand_img.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        //final String childText = (String) getChild(groupPosition, childPosition);
        GlobalClass.billingDETArray.get(childPosition);

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


            barcode.setText( GlobalClass.billingDETArray.get(groupPosition).getBarcode());
            test.setText(GlobalClass.billingDETArray.get(groupPosition).getTests());
            billedamt.setText(GlobalClass.billingDETArray.get(groupPosition).getBilledAmount());
            collamount.setText( GlobalClass.billingDETArray.get(groupPosition).getCollectedAmount());
            type.setText(GlobalClass.billingDETArray.get(groupPosition).getWoetype());
            ref.setVisibility(View.GONE);
            ref.setText(GlobalClass.billingDETArray.get(groupPosition).getRefId());


            System.out.println("get data ***" + _listDataChild.get(Constants.barcode));
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
