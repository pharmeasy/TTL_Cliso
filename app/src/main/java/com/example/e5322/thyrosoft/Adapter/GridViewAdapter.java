package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

/**
 * Created by E5322 on 21-03-2018.
 */

public class GridViewAdapter extends BaseAdapter {

    private int selectedPosition = 0;
    private int selectedPosition1 = 1;
    private int selectedPosition2 = 2;
    private int selectedPosition3 = 3;
    private Context mContext;
    TextView textViewAndroid1 ;
    TextView textViewAndroidcountWoe;
   // TextView textViewAndroid2;
    TextView textViewAndroidcountresult;
    private final String[] gridViewString;
    ArrayList<String> arrayList;

  //  private final String[] gridViewString1;
    private int[] gridViewImageId;
    private int[] gridViewcolor;

    public GridViewAdapter(Context context, String[] gridViewString, int[] gridViewImageId,ArrayList<String> arraylist, int[] gridViewcolor) {//String[] gridViewString, String[] gridViewImageIddata,int[] gridViewImageId
        mContext = context;
       // this.gridViewString1 = gridViewImageIddata;
        this.arrayList = arraylist;
        this.gridViewString = gridViewString;
        this.gridViewImageId = gridViewImageId;
        this.gridViewcolor = gridViewcolor;
    }

    @Override
    public int getCount() {

        return arrayList.size();
    }
    private void setSelectedPosition(int position)
    {
        selectedPosition=position;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup parent) {
        View gridViewAndroid;
        View gridViewAndroid1;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = inflater.inflate(R.layout.single_layout, null);

            LinearLayout linearLayout = (LinearLayout) gridViewAndroid.findViewById(R.id.SingleView);
            //textview

             textViewAndroid1  = (TextView) gridViewAndroid.findViewById(R.id.describe);
             textViewAndroidcountWoe = (TextView) gridViewAndroid.findViewById(R.id.count);
             textViewAndroid1.setText(gridViewString[i]);
             //textViewAndroid2.setText(gridViewImageId[i]);

            textViewAndroid1.setTextColor(mContext.getResources().getColor(R.color.colorWhite));

            linearLayout.setBackgroundColor(mContext.getResources().getColor(gridViewcolor[i]));

            if(arrayList.size()!=0){

                    if(arrayList.get(i)==null){
                        textViewAndroidcountWoe.setText("0");
                    }else{
                        textViewAndroidcountWoe.setText(arrayList.get(i));
                    }
            }


        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }

}