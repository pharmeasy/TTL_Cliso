package com.example.e5322.thyrosoft.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import androidx.appcompat.app.AppCompatActivity;

public class VidoePlayerActivity extends AppCompatActivity {
    VideoView videoView;
    ImageView back, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vidoe_player);


        initView();
        initListner();


        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);

        String link = Api.traningvideo;

        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(Uri.parse(link));

    }

    private void initListner() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(VidoePlayerActivity.this);
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {

                videoView.start();

            }
        });
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        videoView = findViewById(R.id.videoView);
    }

}
