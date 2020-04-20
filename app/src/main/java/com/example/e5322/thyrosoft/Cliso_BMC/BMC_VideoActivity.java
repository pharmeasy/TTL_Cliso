package com.example.e5322.thyrosoft.Cliso_BMC;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;

import org.json.JSONException;
import org.json.JSONObject;

public class BMC_VideoActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    Uri uri;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmcvideo);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        videoView = findViewById(R.id.videoView);
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
        Log.e("BMC_VideoActivity", "VideoAPI: " + Api.video_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.video_url, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("BMC_VideoActivity", "VideoAPIResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("Exits", "").equalsIgnoreCase("YES")) {
                        mediaController.setAnchorView(videoView);
                        uri = Uri.parse(jsonObject.optString("Url", ""));
                        videoView.setMediaController(mediaController);
                        videoView.setVideoURI(uri);
                        videoView.start();
                        videoView.requestFocus();
                    } else {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BMC_VideoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = GlobalClass.setVolleyReq(this);
        requestQueue.add(stringRequest);
    }
}