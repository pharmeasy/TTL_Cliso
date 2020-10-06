package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerImageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> imagelist;


    public ViewPagerImageAdapter(Context context, ArrayList<String> imagelist) {
        this.context = context;
        this.imagelist = imagelist;
    }




    @Override
    public int getCount() {
        return imagelist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {


        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = null;
        try {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.custom_slider_hhh, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            GlobalClass.DisplayImgWithPlaceholderFromURL(((Activity) context), imagelist.get(position), imageView, R.drawable.loading);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }




}
