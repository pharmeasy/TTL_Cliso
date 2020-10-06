package com.example.e5322.thyrosoft.Activity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

public class VidoePlayerActivity extends AppCompatActivity {
    VideoView videoView;
    int index = 0;
    ProgressDialog barProgressDialog;
    ImageView back,home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vidoe_player);

        back=(ImageView)findViewById(R.id.back);
        home=(ImageView)findViewById(R.id.home);

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


        videoView = findViewById(R.id.videoView);

        barProgressDialog = new ProgressDialog(this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);


        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);

        String link = Api.traningvideo;

        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(Uri.parse(link));


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {
                barProgressDialog.dismiss();
                videoView.start();

            }
        });

    }

}
