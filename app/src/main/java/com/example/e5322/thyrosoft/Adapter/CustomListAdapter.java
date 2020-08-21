package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.SourceILSModel.REF_DR;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CustomListAdapter extends ArrayAdapter {

    private List<REF_DR> dataList;
    private List<String> getRefNames;
    private Context mContext;
    private int itemLayout;
    private ListFilter listFilter = new ListFilter();
    private List<String> dataListAllItems;

    public CustomListAdapter(Context context, int resource, List<REF_DR> storeDataLst, List<String> getRefNames) {
        super(context, resource, storeDataLst);
        this.dataList = storeDataLst;
        this.mContext = context;
        this.itemLayout = resource;
        this.getRefNames = getRefNames;

    }

    @Override
    public int getCount() {
        return getRefNames.size();
    }

    @Override
    public String getItem(int position) {
        return getRefNames.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.textView);

        if (GlobalClass.CheckArrayList(dataList) &&  !GlobalClass.isNull(dataList.get(position).getStatus()) && dataList.get(position).getStatus().equalsIgnoreCase("Y")) {
            GlobalClass.SetText(strName,getItem(position));
            strName.setTextColor(Color.parseColor("#189305"));
        }

        GlobalClass.SetText(strName,getItem(position));
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (!GlobalClass.CheckArrayList(dataListAllItems)) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<String>(getRefNames);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                getRefNames = (ArrayList<String>) results.values;
            } else {
                getRefNames = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
