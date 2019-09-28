package com.example.e5322.thyrosoft;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.e5322.thyrosoft.API.Constants;


public class LeggyVideo extends AppCompatActivity {

   MediaController mediaController;
    VideoView videoView;
    Uri uri;
//    String URL = "http://www.charbi.local/CDN/Videos/Leggy_Video.mp4";
    String URL = "https://www.thyrocare.com/API_BETA/B2B/COMMON.svc/Cliso/Showvideo";
    /*SimpleExoPlayerView videoView;
    SimpleExoPlayer exoPlayer;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggy_video);
        /*videoView = findViewById(R.id.videoView);

        try {
            Intent i = getIntent();
            URL = i.getStringExtra(Constants.setVideoUrl);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error",":"+e.getMessage());
        }

        getVideo();*/
        videoView = findViewById(R.id.videoView);
        try {
            Intent i = getIntent();
            URL = i.getStringExtra(Constants.setVideoUrl);
        } catch (Exception e) {
            e.printStackTrace();
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

    /*public void getVideo() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new
                    AdaptiveTrackSelection.Factory(bandwidthMeter));

            exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
            Uri uri = Uri.parse(URL);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(uri,dataSourceFactory,extractorsFactory,null,null);
            videoView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error playing video",":"+e.getMessage());
        }
    }*/

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
                    videoView.start();
                }
            });
            videoView.requestFocus();
        }
    }
}
