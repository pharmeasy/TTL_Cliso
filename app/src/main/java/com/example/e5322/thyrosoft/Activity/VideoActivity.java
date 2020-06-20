package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.VideoListAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetVideoLanguageWiseRequestModel;
import com.example.e5322.thyrosoft.Models.VideoLangaugesResponseModel;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;


public class VideoActivity extends AppCompatActivity {

    Activity mActivity;
    public VideoView videoView;
    private int a = 0;
    private GlobalClass globalClass;
    private Global global;
    private ArrayList<VideoLangaugesResponseModel.Outputlang> VideoLangArylist;
    private RecyclerView recView;
    ArrayList<VideosResponseModel.Outputlang> VideosArylist;
    private VideoListAdapter videoListAdapter;
    private TextView tv_languageSelected;
    private SharedPreferences prefs_Language;
    private TextView tv_noDatafound;
    ProgressDialog progressDialog;
    ImageView imgBack;
    ConnectionDetector connectionDetector;
    private Snackbar internetErrorSnackbar;
    View parentLayout;
    String TAG = getClass().getSimpleName();
    VideoDataposter videoDataposter;
    String VideoID;
    boolean rel_falg = false;
    boolean stop_flag = false;
    boolean onpause = false;
    boolean destroy_flag = false;
    boolean back_flag = false;
    boolean backpress_flag = false;

    private int b = 0;
    long milliseconds, minutes, seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = VideoActivity.this;

        prefs_Language = mActivity.getSharedPreferences("Video_lang_pref", 0);
        connectionDetector = new ConnectionDetector(VideoActivity.this);
        globalClass = new GlobalClass(mActivity);
        global = new Global(mActivity);

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_video);

        parentLayout = findViewById(android.R.id.content);
        videoView = (VideoView) findViewById(R.id.video_view1);
        recView = (RecyclerView) findViewById(R.id.recView);
        tv_noDatafound = (TextView) findViewById(R.id.tv_noDatafound);
        progressDialog = new ProgressDialog(mActivity);

        initToolbar();

        if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
            if (connectionDetector.isConnectingToInternet()) {
                CallVideoLanguagesAPI();
            } else {
                global.showCustomToast(mActivity, "Please check internet connection.");
            }

        } else {
            tv_languageSelected.setText(prefs_Language.getString("LanguageSelected", ""));

            if (connectionDetector.isConnectingToInternet()) {
                GetVideosBasedonLanguage(prefs_Language.getString("LanguageID", ""));
            } else {
                global.showCustomToast(mActivity, "Please check internet connection.");
            }

            tv_noDatafound.setVisibility(View.VISIBLE);
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().stop();
                videoView.setVisibility(View.GONE);
                // videoView.getPlayer().release();
            }
        }

        internetErrorSnackbar = Snackbar.make(parentLayout, "Unable to Play this Video. Please check your Internet Connection or try after sometime.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (videoView.getVideoInfo().getUri() != null) {
                            videoView.getPlayer().start();
                        }
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.maroon));
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarVideo);
        setSupportActionBar(toolbar);

        tv_languageSelected = (TextView) findViewById(R.id.tv_languageSelected);
        tv_languageSelected.setPaintFlags(tv_languageSelected.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_languageSelected.setTextColor(mActivity.getResources().getColor(R.color.maroon));
        imgBack = (ImageView) findViewById(R.id.imgBack);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (videoView.getVideoInfo().getUri() != null) {
                        backpress_flag = true;
                        if (videoView.getPlayer().isPlaying()) {
                            postdata(videoView.getPlayer());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        tv_languageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    CallVideoLanguagesAPI();
                } else {
                    global.showCustomToast(mActivity, "Please check internet connection.");
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Log.e("Toolbar", "Clicked");*/

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void CallVideoLanguagesAPI() {
        global.showProgressDialog();
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(APIInteface.class);
        Call<VideoLangaugesResponseModel> responseCall = apiInterface.getVideoLanguages();
        //Log.e(TAG, "V I D E O U R L ---->" + responseCall.request().url());

        responseCall.enqueue(new Callback<VideoLangaugesResponseModel>() {
            @Override
            public void onResponse(Call<VideoLangaugesResponseModel> call, Response<VideoLangaugesResponseModel> response) {

                global.hideProgressDialog();

                VideoLangArylist = new ArrayList<>();

                if (response.isSuccessful()) {
                    VideoLangaugesResponseModel model = response.body();
                    if (model.getOutput() != null || model.getResId().equalsIgnoreCase(Constants.RES0000)) {
                        VideoLangArylist = model.getOutput();
                    }
                }

                if (VideoLangArylist.size() > 0) {

                    SharedPreferences pref = mActivity.getSharedPreferences("Video_lang_pref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(VideoLangArylist);
                    editor.putString("LanguageArryList", json);
                    editor.apply();

                    if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
                        showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, false);
                        tv_noDatafound.setVisibility(View.GONE);
                    } else {
                        showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, true);
                    }
                } else {
                    Gson gson = new Gson();
                    String json = prefs_Language.getString("LanguageArryList", "");
                    VideoLangArylist = gson.fromJson(json, new TypeToken<List<VideoLangaugesResponseModel.Outputlang>>() {
                    }.getType());

                    if (VideoLangArylist != null) {
                        if (TextUtils.isEmpty(prefs_Language.getString("LanguageSelected", ""))) {
                            showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, false);
                            tv_noDatafound.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                        } else {
                            showLanguageDialogList("Select Language", VideoLangArylist, tv_languageSelected, true);
                        }
                    } else {
                        global.showCustomToast(mActivity, "Unable to fetch languages from server. Please try after sometime.");
                    }


                }
            }

            @Override
            public void onFailure(Call<VideoLangaugesResponseModel> call, Throwable t) {
                global.hideProgressDialog();
                tv_noDatafound.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                global.showCustomToast(mActivity, "Unable to fetch data from server.");
            }
        });
    }


    private void showLanguageDialogList(String title, final ArrayList<VideoLangaugesResponseModel.Outputlang> list, final TextView textView, boolean ShowCancelOption) {
        if (list != null && !list.isEmpty()) {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(mActivity);
            builderSingle.setTitle(title);
            builderSingle.setCancelable(false);
            final ArrayAdapter<VideoLangaugesResponseModel.Outputlang> arrayAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1);

            arrayAdapter.addAll(list);
            if (ShowCancelOption) {
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }


            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (which < arrayAdapter.getCount()) {

                        String strName = arrayAdapter.getItem(which).getLANGUAGE();
                        textView.setText(strName);

                        if (connectionDetector.isConnectingToInternet()) {
                            GetVideosBasedonLanguage(arrayAdapter.getItem(which).getIID_NEW());

                         /*   if (videoView.getVideoInfo().getUri() != null) {
                                if (videoView.getPlayer().isPlaying()) {
                                    videoView.getPlayer().stop();
                                    videoView.setVisibility(View.GONE);
                                    videoView.getPlayer().getDuration();
                                    Log.e(TAG, "GET VIDEO DUR ---->" + videoView.getPlayer().getDuration());
                                }
                            }*/

                            SharedPreferences pref = mActivity.getSharedPreferences("Video_lang_pref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("LanguageSelected", arrayAdapter.getItem(which).getLANGUAGE());
                            editor.putString("LanguageID", arrayAdapter.getItem(which).getIID_NEW());
                            editor.apply();

                        } else {
                            GlobalClass.toastyError(VideoActivity.this, MessageConstants.CHECK_INTERNET_CONN, false);
                        }
                    }
                    dialog.dismiss();
                }
            });

            builderSingle.show();
        } else {
            global.showCustomToast(mActivity, "List is not available");
        }

    }

    private void GetVideosBasedonLanguage(String LanguageID) {

        if (videoView != null && videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
            videoView.getPlayer().stop();
            videoView.setVisibility(View.GONE);
        }

        GetVideoLanguageWiseRequestModel model = new GetVideoLanguageWiseRequestModel();
        model.setApp(Constants.APP_ID);
        model.setLanguage(LanguageID);
        PostAPIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mActivity, Api.LIVEAPI).create(PostAPIInteface.class);
        Call<VideosResponseModel> responseCall = apiInterface.getVideobasedOnLanguage(model);
       // Log.e(TAG, "VIDEO LIST BODY ---->" + new GsonBuilder().create().toJson(model));
        //Log.e(TAG, "VIDEO LIST URL ---->" + responseCall.request().url());
        global.showProgressDialog();

        responseCall.enqueue(new Callback<VideosResponseModel>() {
            @Override
            public void onResponse(Call<VideosResponseModel> call, Response<VideosResponseModel> response) {
                global.hideProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    VideosResponseModel responseModel = response.body();

                    if (!TextUtils.isEmpty(responseModel.getResId()) && responseModel.getResId().equalsIgnoreCase("RSS0000") && responseModel.getOutput() != null && responseModel.getOutput().size() > 0) {
                        tv_noDatafound.setVisibility(View.GONE);
                        VideosArylist = new ArrayList<>();
                        VideosArylist = responseModel.getOutput();

                        if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                            videoView.getPlayer().stop();
                            videoView.setVisibility(View.GONE);
                        }

                        DisplayVideosInList();

                    } else {
                        recView.setVisibility(View.GONE);
                        tv_noDatafound.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.GONE);

                    }
                } else {
                    tv_noDatafound.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    //System.out.println("No Videos found");
                }
            }

            @Override
            public void onFailure(Call<VideosResponseModel> call, Throwable t) {
                global.hideProgressDialog();
                tv_noDatafound.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);

            }
        });

    }

    private void DisplayVideosInList() {
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);

        recView.setLayoutManager(mLayoutManager);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        recView.setHasFixedSize(true);
        recView.setLayoutManager(mLayoutManager);
        recView.setVisibility(View.VISIBLE);


        videoListAdapter = new VideoListAdapter(mActivity, VideosArylist);

        videoListAdapter.setOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
            @Override
            public void OnVideoItemSelected(ArrayList<VideosResponseModel.Outputlang> VideoArrylist1, VideosResponseModel.Outputlang SelectedVideo) {


                try {
                    if (videoView.getVideoInfo().getUri() != null && videoView.getVideoInfo().getUri().toString().equalsIgnoreCase(SelectedVideo.getPath())) {
                        if (!videoView.getVideoInfo().getUri().toString().equalsIgnoreCase(SelectedVideo.getPath())) {
                            videoView.getPlayer().stop();
                            videoView.setVisibility(View.GONE);
                        } else {
                            if (videoView.getPlayer().getCurrentState() == GiraffePlayer.STATE_PLAYBACK_COMPLETED) {
                                initializePlayer(SelectedVideo);
                                VideosArylist = VideoArrylist1;
                                videoListAdapter.notifyDataSetChanged();
                            } else {
                                videoView.getPlayer().start();
                            }
                        }
                    } else {
                        initializePlayer(SelectedVideo);
                        VideosArylist = VideoArrylist1;
                        videoListAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "OnVideoItemSelected error ---->" + e.getLocalizedMessage());
                }
            }
        });

        recView.setAdapter(videoListAdapter);

    }

    private void initializePlayer(final VideosResponseModel.Outputlang Video) {
        if (videoView.getVideoInfo().getUri() != null) {
            videoView.getPlayer().stop();
            videoView.setVisibility(View.GONE);
        }

        //   videoView.setVisibility(View.VISIBLE);

        videoView.setVideoPath(Video.getPath()).getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_NORMAL);
        videoView.getPlayer().getVideoInfo().setTitle(Video.getTitle()).setAspectRatio(VideoInfo.AR_ASPECT_FIT_PARENT).setBgColor(Color.BLACK).setShowTopBar(true).addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "multiple_requests", 1L));
        videoView.getPlayer().start();
        VideoID = Video.getId();

        videoView.setPlayerListener(new PlayerListener() {
            @Override
            public void onPrepared(GiraffePlayer giraffePlayer) {
                Log.e(TAG, "onPrepared:");
            }

            @Override
            public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
//                Log.e(TAG, "onBufferingUpdate: ");
            }

            @Override
            public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
                Log.e(TAG, "onInfo: ");
                return false;
            }

            @Override
            public void onCompletion(GiraffePlayer giraffePlayer) {
                Log.e(TAG, "onCompletion: ");
                postdata(giraffePlayer);
            }

            @Override
            public void onSeekComplete(GiraffePlayer giraffePlayer) {
                Log.e(TAG, "onSeekComplete: ");

            }

            @Override
            public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
                System.out.println("tejas >>> onError");

                if (a == 0) {
                    a = giraffePlayer.getCurrentPosition();

                }

                long milliseconds = giraffePlayer.getCurrentPosition();
                long minutes = (milliseconds / 1000) / 60;
                long seconds = (milliseconds / 1000) % 60;


                long milliseconds1 = giraffePlayer.getDuration();
                long minutes1 = (milliseconds1 / 1000) / 60;
                long seconds1 = (milliseconds1 / 1000) % 60;

                if (minutes != minutes1 && seconds != seconds1) {
                    internetErrorSnackbar.show();
                }

                return false;
            }

            @Override
            public void onPause(GiraffePlayer giraffePlayer) {

                if (VideosArylist != null && VideosArylist.size() > 0) {
                    for (int i = 0; i < VideosArylist.size(); i++) {
                        if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                            VideosArylist.get(i).setVideoPaused(true);
                            onpause = true;
                        }
                    }
                    videoListAdapter.notifyDataSetChanged();
                }

                try {
                    postdata(giraffePlayer);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onRelease(GiraffePlayer giraffePlayer) {
                Log.e(TAG, "onRelease----------------->  " + giraffePlayer.getDuration());
                if (onpause || stop_flag || back_flag | backpress_flag) {
                    Log.e(TAG, "<---- I AM IN IF ---->");
                } else {
                    postdata(giraffePlayer);
                }

            }

            @Override
            public void onStart(GiraffePlayer giraffePlayer) {
                Log.e(TAG, "onStart: ");

                if (internetErrorSnackbar != null && internetErrorSnackbar.isShown()) {
                    internetErrorSnackbar.dismiss();
                }


                if (VideosArylist != null && VideosArylist.size() > 0) {
                    for (int i = 0; i < VideosArylist.size(); i++) {
                        if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                            VideosArylist.get(i).setVideoPlaying(true);
                            VideosArylist.get(i).setVideoPaused(false);
                        } else {
                            VideosArylist.get(i).setVideoPlaying(false);
                            VideosArylist.get(i).setVideoPaused(true);
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }

                    System.out.println("Tejas >>> seek Time : " + a);
                    Log.e(TAG, " on Video Start ---->" + videoView.getPlayer().getCurrentPosition());

                }
            }

            @Override
            public void onTargetStateChange(int oldState, int newState) {
                Log.e(TAG, "onTargetStateChange: ");
            }

            @Override
            public void onCurrentStateChange(int oldState, int newState) {

                Log.e(TAG, "OLD STATE : " + oldState + " NEW STATE : " + newState);

                if (oldState == GiraffePlayer.STATE_ERROR && newState == GiraffePlayer.STATE_PLAYBACK_COMPLETED) {
                    long milliseconds1 = videoView.getPlayer().getDuration();
                    long minutes1 = (milliseconds1 / 1000) / 60;
                    long seconds1 = (milliseconds1 / 1000) % 60;

                    if (minutes == minutes1 && seconds == seconds1) {
                        if (VideosArylist != null && VideosArylist.size() > 0) {
                            for (int i = 0; i < VideosArylist.size(); i++) {
                                VideosArylist.get(i).setVideoPlaying(false);
                                VideosArylist.get(i).setVideoPaused(false);
                            }
                            videoListAdapter.notifyDataSetChanged();
                        }
                        a = 0;
                        videoView.setVisibility(View.GONE);
                    } else {
                        if (VideosArylist != null && VideosArylist.size() > 0) {
                            for (int i = 0; i < VideosArylist.size(); i++) {

                                if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                    VideosArylist.get(i).setVideoPlaying(true);
                                    VideosArylist.get(i).setVideoPaused(true);
                                } else {
                                    VideosArylist.get(i).setVideoPlaying(false);
                                }
                            }
                            videoListAdapter.notifyDataSetChanged();
                            videoView.setVisibility(View.VISIBLE);
                        }
                        // videoView.setVisibility(View.VISIBLE);
                    }

                    Log.e(TAG, "STATE_PLAYBACK_COMPLETED : " + VideoID);

                } else if ((oldState == GiraffePlayer.STATE_PLAYING && newState == GiraffePlayer.STATE_PLAYBACK_COMPLETED) || (oldState == GiraffePlayer.DISPLAY_NORMAL && newState == GiraffePlayer.STATE_RELEASE)) {
                    videoView.setVisibility(View.GONE);
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {
                            VideosArylist.get(i).setVideoPlaying(false);
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                } else if (oldState == GiraffePlayer.STATE_PREPARED && newState == GiraffePlayer.STATE_PLAYING) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {
                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(false);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        videoListAdapter.notifyDataSetChanged();
                    }
                } else if (oldState == GiraffePlayer.STATE_IDLE && (newState == GiraffePlayer.STATE_LAZYLOADING || newState == GiraffePlayer.STATE_PREPARING)) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(true);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        videoListAdapter.notifyDataSetChanged();
                        videoView.setVisibility(View.VISIBLE);
                    }
                    // videoView.setVisibility(View.VISIBLE);
                    Log.e(TAG, "STATE_IDLE : " + VideoID);
                } else if (oldState == GiraffePlayer.STATE_PLAYBACK_COMPLETED && newState == GiraffePlayer.STATE_PLAYING) {
                    if (VideosArylist != null && VideosArylist.size() > 0) {
                        for (int i = 0; i < VideosArylist.size(); i++) {

                            if (VideosArylist.get(i).getId().equalsIgnoreCase(Video.getId())) {
                                VideosArylist.get(i).setVideoPlaying(true);
                                VideosArylist.get(i).setVideoPaused(false);
                            } else {
                                VideosArylist.get(i).setVideoPlaying(false);
                            }
                        }
                        if (a > 0) {
                            videoView.getPlayer().seekTo(a);
                        }
                        videoListAdapter.notifyDataSetChanged();
                        videoView.setVisibility(View.VISIBLE);
                    }
                } else if (oldState == GiraffePlayer.STATE_PLAYING && newState == GiraffePlayer.STATE_RELEASE) {
                    videoView.setVisibility(View.GONE);
                    videoListAdapter.notifyDataSetChanged();
                } else {
                    videoView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onDisplayModelChange(int oldModel, int newModel) {
                Log.e(TAG, "onDisplayModelChange: " + VideoID);
            }

            @Override
            public void onPreparing(GiraffePlayer giraffePlayer) {
                Log.e(TAG, "onPreparing: " + VideoID);
            }

            @Override
            public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {
                Log.e(TAG, "onTimedText: " + VideoID);
            }

            @Override
            public void onLazyLoadProgress(GiraffePlayer giraffePlayer, int progress) {
                Log.e(TAG, "onLazyLoadProgress: " + VideoID);
            }

            @Override
            public void onLazyLoadError(GiraffePlayer giraffePlayer, String message) {
                Log.e(TAG, "onLazyLoadError: " + VideoID);
            }

        });

    }

    @Override
    protected void onPause() {
        try {
            if (videoView.getVideoInfo().getUri() != null) {
                stop_flag = true;
                if (!backpress_flag) {
                    if (videoView.getPlayer().isPlaying()) {
                        postdata(videoView.getPlayer());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();

    }


    @Override
    public void onBackPressed() {
        try {

            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().onBackPressed()) {
                back_flag = true;
                if (videoView.getPlayer().isPlaying()) {
                    postdata(videoView.getPlayer());
                }
                return;
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_FULL_WINDOW);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (videoView.getVideoInfo().getUri() != null && videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_NORMAL);
            }
        }
    }

    private void postdata(GiraffePlayer giraffePlayer) {
        milliseconds = giraffePlayer.getCurrentPosition();
        minutes = (milliseconds / 1000) / 60;
        seconds = (milliseconds / 1000) % 60;
        videoDataposter = new VideoDataposter(VideoActivity.this, minutes, seconds);
        videoDataposter.videoDatapost(VideoID, minutes, seconds);
    }
}
