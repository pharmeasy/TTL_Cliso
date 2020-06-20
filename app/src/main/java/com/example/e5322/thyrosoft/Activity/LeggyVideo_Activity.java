package com.example.e5322.thyrosoft.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;

public class LeggyVideo_Activity extends AppCompatActivity implements MyGestureListener.SimpleGestureListener {
    RecyclerView videoList;
    RecyclerView.Adapter recyclerAdapter;
    ImageView back, home;
    String URL = "";
    LeggyVideo_Activity mActivity;
    Context mContext;
    VideoView videoView;
    MediaController mediaController;
    RelativeLayout video;
    TextView videoName,loading2;
    Uri uri;
    MyGestureListener myGestureListener;
    private int currentVolume;
    AudioManager audioManager;
    private ImageView volumes;
    ProgressBar loading,loading3;
    ProgressDialog progressDialog;
    String TAG = getClass().getSimpleName();
    LeggyVideoAdapter leggyVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_video);

        mActivity = this;
        mContext = this;
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        videoList = findViewById(R.id.videoList);
        video = findViewById(R.id.video);
        videoName = findViewById(R.id.videoName);
        videoView = findViewById(R.id.videoView);
        videoName.setBackgroundColor(Color.TRANSPARENT);
//        progressDialog = GlobalClass.ShowprogressDialog(LeggyVideo_Activity.this);

//        progressDialog.show();

        loading = findViewById(R.id.loading);
        loading2 = findViewById(R.id.loading2);
        loading3 = findViewById(R.id.loading3);

        loading2.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(LeggyVideo_Activity.this);
        progressDialog.setTitle("Kindly Wait");
        progressDialog.setMessage("Processing your Request");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        leggyVideoAdapter = new LeggyVideoAdapter();

        getVideoList();
        try {
            Intent i = getIntent();
            URL = i.getStringExtra(Constants.setVideoUrl);
            String FullScreenExit = i.getStringExtra(Constants.setFullScreen);

            if ("Exit".equals(FullScreenExit)) {
                URL = i.getStringExtra(Constants.setVideoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(LeggyVideo_Activity.this);
            }
        });

        myGestureListener = new MyGestureListener(LeggyVideo_Activity.this, this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volumes = findViewById(R.id.volumes);


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
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

    private void getVideoList() {

//        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(LeggyVideo_Activity.this);
        Log.e(TAG, "getVideoList: " + Api.video_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.video_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("FETCHED VIDEO LIST", ">>" + response);
                try {
                    Gson gson = new Gson();
                    LeggyVideoModel mainModel = gson.fromJson(response.toString(), LeggyVideoModel.class);

                    if (mainModel != null) {
                        if (mainModel.getRESPONSE() != null) {
                            if (mainModel.getRESPONSE().equalsIgnoreCase("YES")) {
                                if (mainModel.getOutput() != null) {

//                                    GlobalClass.hideProgress(LeggyVideo_Activity.this, progressDialog);
                                    //loading.setVisibility(View.GONE);
                                    recyclerAdapter = new LeggyVideoAdapter(mActivity, mContext, mainModel.getOutput());
                                    videoList.setAdapter(recyclerAdapter);
                                    videoList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                }
                            }
                        }
                    }
                    loading2.setVisibility(View.GONE);
                    progressDialog.dismiss();
//                    GlobalClass.hideProgress(LeggyVideo_Activity.this,progressDialog);
                    //                    loading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error", ":" + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "API Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void ShowVideo(LeggyVideoListModel leggyVideoListModel) {
//        progressDialog.show();
        GlobalClass.isNetworkAvailable(LeggyVideo_Activity.this);

        /*if (!videoView.isPlaying()) {
            loading.setVisibility(View.VISIBLE);
        }*/

        if (leggyVideoListModel != null) {
            if (leggyVideoListModel.getPath() != null) {
                if (!leggyVideoListModel.getPath().equalsIgnoreCase("")) {
                    URL = leggyVideoListModel.getPath().toString().trim();
//                    leggyVideoAdapter.loading3.setVisibility(View.GONE);
                    getVideo();
//                    progressDialog.dismiss();
                }
            }
        }
    }

    public void getVideo() {
        video.setVisibility(View.VISIBLE);
        try {
            mediaController = new CustomMediaController(this, URL);
            mediaController.setAnchorView(videoView);
            uri = Uri.parse(URL);
            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);

            if (!videoView.isPlaying()) {
                progressDialog.show();
            }

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                            if (i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                                progressDialog.dismiss();
                            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                progressDialog.show();
                            if (i == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                progressDialog.dismiss();
                            return false;
                        }
                    });
                    if (Constants.timeInterval > 0) {
                        videoView.seekTo(Constants.timeInterval);
                        videoView.start();
                        videoView.getBufferPercentage();
                        Constants.timeInterval = 0;
                    }
                }
            });
            videoView.start();
            videoView.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying() && progressDialog==null) {
            assert false;
            progressDialog.show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (videoView != null && videoView.isPlaying() && Constants.timeInterval > 0) {
            videoView.seekTo(Constants.timeInterval);
        }
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

    @Override
    public void onPause() {
        super.onPause();
        if (!videoView.isPlaying() && progressDialog==null) {
            assert false;
            progressDialog.show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.dismiss();
            finish();
        }
    }
}
