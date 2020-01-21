package com.example.e5322.thyrosoft.Activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LeggyVideo_Activity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_video);
        videoView = findViewById(R.id.videoView);

        mediaController = new MediaController(this);

        //  getVideo();

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

        mediaController.setAnchorView(videoView);
        uri = Uri.parse("http://www.charbi.local/CDN/Videos/Leggy_Video.mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    mediaController.show();
                } catch (Exception e) {
                    Log.e("TAG", "onPrepared error : "+e.getLocalizedMessage());
                }
            }
        });
    }

    private void getVideo() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://www.charbi.local/CDN/Videos/Leggy_Video.mp4", new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.d("FETCHED VIDEO", ">>" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optString("Exits", "").equals("YES")) {
                        mediaController.setAnchorView(videoView);
                        uri = Uri.parse("http://www.charbi.local/CDN/Videos/Leggy_Video.mp4");
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
                Toast.makeText(LeggyVideo_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
