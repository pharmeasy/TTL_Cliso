package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.R;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> imagelist;

    public ViewPagerAdapter(Context context, List<String> imagelist) {
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

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_slider, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Glide.with(context)
                .load(imagelist.get(position))
                .placeholder(context.getResources().getDrawable(R.drawable.img_no_img_aval))
                .into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}