package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e5322.thyrosoft.Controller.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.R;

public class DemoModule extends AppCompatActivity {


        MediaController mediaController;
        VideoView videoView;
        Uri uri;
        //    String URL = "http://www.charbi.local/CDN/Videos/Leggy_Video.mp4";
        String URL = "https://www.thyrocare.com/API_BETA/B2B/COMMON.svc/Cliso/Showvideo";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_leggy_video);

            videoView = findViewById(R.id.videoView);
            try {
                Intent i = getIntent();
                URL = i.getStringExtra(Constants.setVideoUrl);
                Log.e("TAG","got urllllll");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG",":"+e.getMessage());
            }
            mediaController = new MediaController(this);
            getVideo();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    finish();
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int j) {
                    finish();
                    return false;
                }
            });
        }


        private void getVideo() {
            if (URL != null) {
                mediaController.setAnchorView(videoView);
                uri = Uri.parse(URL);
                mediaController.setMediaPlayer(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        Log.i("TAG", "Duration = " +
                                videoView.getDuration());
                    }
                });
                videoView.start();
                videoView.requestFocus();

           /* View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);*/

           /*View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);*/
            }
        }

   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }

    public void hideNavigationBar() {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                               *//* View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE*//*
                );
    }*/

}
