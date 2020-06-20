package com.example.e5322.thyrosoft;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.e5322.thyrosoft.Activity.BroadcastActivity;
import com.example.e5322.thyrosoft.Adapter.Sliding_Adapter;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.google.gson.Gson;

public class BroadcastViewPagerActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    int position_click;
    String gsonString;
    ImageView back, home;

    NoticeBoard_Model[] broadCastArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_view_pager);
        mPager = (ViewPager) findViewById(R.id.pager);

        back= (ImageView) findViewById(R.id.back);
        home= (ImageView) findViewById(R.id.home);

        Intent i = getIntent();
        position_click = i.getIntExtra("position", 0);
        gsonString = i.getStringExtra("gsonString");

        Gson gson = new Gson();
        broadCastArrayList = gson.fromJson(gsonString, NoticeBoard_Model[].class);
        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplication(),BroadcastActivity.class);
                startActivity(i);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(BroadcastViewPagerActivity.this);
            }
        });
    }

    private void init() {


        mPager.setAdapter(new Sliding_Adapter(BroadcastViewPagerActivity.this, broadCastArrayList));

        NUM_PAGES = broadCastArrayList.length;

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
        mPager.setCurrentItem(position_click, true);
    }
}
