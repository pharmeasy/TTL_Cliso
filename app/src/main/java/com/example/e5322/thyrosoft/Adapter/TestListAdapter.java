package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;

import java.util.List;

public class TestListAdapter extends ArrayAdapter<BaseModel> {
    LayoutInflater flater;
    BaseModel baseModel;

    public TestListAdapter(Activity context, int resouceId, int textviewId, List<BaseModel> list) {
        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        baseModel = getItem(position);
        View rowview = flater.inflate(R.layout.list_ite, null, true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        GlobalClass.SetText(txtTitle, baseModel.getCode());
        txtTitle.setTextColor(Color.BLACK);

        return rowview;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        baseModel = getItem(position);

        View rowview = flater.inflate(R.layout.list_ite, null, true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        GlobalClass.SetText(txtTitle, baseModel.getCode());
        txtTitle.setTextColor(Color.BLACK);

        return rowview;
    }
}
