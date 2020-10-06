package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.viewpager.widget.PagerAdapter;
import androidx.appcompat.app.AlertDialog;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.HealthTipsDetail_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.List;

public class HealthTipsPagerAdapter extends PagerAdapter {

    private GlobalClass globalclass;
    private List<HealthTipsApiResponseModel.HArt> HealthTipsArrylst;

    SharedPreferences prefs_user;
    Context context;

    private AlertDialog.Builder alertDialogBuilder;

    public HealthTipsPagerAdapter(Context context, List<HealthTipsApiResponseModel.HArt> HealthTipsArrylst) {
        this.context = context;
        this.HealthTipsArrylst = HealthTipsArrylst;
        globalclass = new GlobalClass(context);
        // prefs_user = mActivity.getSharedPreferences("login_detail", 0);
    }

    @Override
    public int getCount() {
        return HealthTipsArrylst.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.healthtip_pageritem, container, false);

        ImageView img_healthtips = (ImageView) itemView.findViewById(R.id.img_healthtips);
        TextView tv_helthTipsdescription = (TextView) itemView.findViewById(R.id.tv_helthTipsdescription);
        TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        TextView tv_readMore = (TextView) itemView.findViewById(R.id.tv_readMore);

        if (HealthTipsArrylst != null && HealthTipsArrylst.size() > 0) {
            GlobalClass.DisplayImgWithPlaceholderFromURL(((Activity) context), HealthTipsArrylst.get(position).getImageurl(), img_healthtips,R.drawable.userprofile);
            tv_title.setText(HealthTipsArrylst.get(position).getArtical_Name());
            tv_helthTipsdescription.setText(HealthTipsArrylst.get(position).getTeaser());
        }

        tv_readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, HealthTipsDetail_Activity.class).putExtra("url",
                        HealthTipsArrylst.get(position).getArtical_link()));
                Log.e("TAG", " ARTICAL LINK : "+ HealthTipsArrylst.get(position).getArtical_link());
            }
        });

        container.addView(itemView);
        return itemView;
    }

 /*   private void ShowLoginAlert() {
        globalclass.ShowLoginPOP_UP(mActivity);
    }*/

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


}

