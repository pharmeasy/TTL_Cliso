package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.R;

public class CustomMediaController extends MediaController {
    ImageButton fullScreen;
    private String isFullScreen;
    String URL;
    String VideoName;
    Activity mActivity;
    LeggyVideo_Activity leggyVideo_activity;
    private MediaPlayerControl mediaPlayer;

    public CustomMediaController(Activity context, String URL) {
        super(context);
        this.mActivity = context;
        this.URL = URL;
    }

    public CustomMediaController(LeggyVideo_Activity leggyVideo_activity, String url) {
        super(leggyVideo_activity);
        this.mActivity = leggyVideo_activity;
        this.leggyVideo_activity = leggyVideo_activity;
        this.URL = url;
    }

    public void setAnchorView(View view) {
        super.setAnchorView(view);
        fullScreen = new ImageButton(super.getContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;
        params.topMargin = 20;
        addView(fullScreen,params);

        isFullScreen = ((Activity)getContext()).getIntent().getStringExtra(Constants.setFullScreen);

        if ("Yes".equals(isFullScreen)) {
            fullScreen.setImageResource(R.drawable.fullscreenexit);
        } else {
            fullScreen.setImageResource(R.drawable.fullscreen);
        }


        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if("Yes".equals(isFullScreen)){

                    Constants.timeInterval = mediaPlayer.getCurrentPosition();
                    ((Activity) getContext()).finish();
                }else{
                    Intent intent = new Intent(getContext(),VideoFullScreen.class);
                    intent.putExtra(Constants.setVideoUrl,URL);
                    intent.putExtra(Constants.videoName,VideoName);
                    intent.putExtra(String.valueOf(Constants.timeInterval),mediaPlayer.getCurrentPosition());
                    intent.putExtra(Constants.setFullScreen, "Yes");
                    getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
         this.mediaPlayer = player;
    }
}
