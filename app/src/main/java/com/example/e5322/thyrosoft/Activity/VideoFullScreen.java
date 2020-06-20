package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.R;

public class VideoFullScreen extends AppCompatActivity implements MyGestureListener.SimpleGestureListener {
    VideoView videoView;
    Uri uri;
    MediaController mediaController;
    String URL;
    String fullScreen;
    int time;
    String Videoname;
    TextView videoName;
    private int currentApiVersion;
    MyGestureListener myGestureListener;
    private int currentVolume;
    AudioManager audioManager;
    private ImageView volumes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full_screen);

        videoView = findViewById(R.id.videoView);
        videoName = findViewById(R.id.videoName);
        try {
            Intent i = getIntent();
            fullScreen = i.getStringExtra(Constants.setFullScreen);
            URL = i.getStringExtra(Constants.setVideoUrl);
            time = i.getIntExtra(String.valueOf(Constants.timeInterval), 0);
            Videoname = i.getStringExtra(Constants.videoName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoName.setText(Constants.videoName);
        videoName.setBackgroundColor(Color.TRANSPARENT);


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

        myGestureListener = new MyGestureListener(VideoFullScreen.this, this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volumes = findViewById(R.id.volumes);


        if ("Yes".equals(fullScreen)) {
//            getSupportActionBar().hide();
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(flags);
                final View decorView = getWindow().getDecorView();
                decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
            }
        }

        mediaController = new CustomMediaController(VideoFullScreen.this, URL);
        mediaController.setAnchorView(videoView);
        uri = Uri.parse(URL);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(time);
                videoView.start();
            }
        });

        videoView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.timeInterval = videoView.getCurrentPosition();
    }


    public boolean dispatchTouchEvent(MotionEvent me) {
        this.myGestureListener.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case MyGestureListener.SWIPE_DOWN:
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0);
                volumes.setVisibility(View.VISIBLE);
                volumes.setImageResource(R.drawable.volumedown);
                if (currentVolume == 0) {
                    volumes.setVisibility(View.VISIBLE);
                    volumes.setImageResource(R.drawable.volumemute);
                }
                break;
            case MyGestureListener.SWIPE_UP:
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 1, 0);
                volumes.setVisibility(View.VISIBLE);
                volumes.setImageResource(R.drawable.volumeup);
                break;
        }
        volumes.postDelayed(new Runnable() {
            public void run() {
                volumes.setVisibility(View.GONE);
            }
        }, 6000);
    }

}