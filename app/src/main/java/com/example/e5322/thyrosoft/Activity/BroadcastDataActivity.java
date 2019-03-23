package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.e5322.thyrosoft.Models.Messages_Noticeboard;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class BroadcastDataActivity extends AppCompatActivity {
    int position;
    String gsonString;
    NoticeBoard_Model[] broadCastArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_data);

        Intent i = getIntent();
        position = i.getIntExtra("position",0);
        gsonString=i.getStringExtra("gsonString");

        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
        Gson gson=new Gson();
        broadCastArrayList=gson.fromJson(gsonString, NoticeBoard_Model[].class);

        System.out.println(gsonString);
    }
}