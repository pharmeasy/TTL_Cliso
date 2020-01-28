package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PostVideoTime_module;
import com.example.e5322.thyrosoft.Models.VideoTime_Model;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDataposter {
    Context context;
    String TAG = getClass().getSimpleName();

    SharedPreferences preferences;
    String usercode;
    long minutes, seconds;

    public VideoDataposter(Context context, long minutes, long seconds) {
        this.context = context;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void videoDatapost(String videoID, long minutes, long seconds) {

        preferences = context.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", null);

        if (GlobalClass.isNetworkAvailable((Activity) context)) {

            if (minutes != 0 || seconds != 0) {

                APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(Api.LIVEAPI).create(APIInteface.class);

                PostVideoTime_module postVideoTime_module = new PostVideoTime_module();
                postVideoTime_module.setClientId(usercode);
                postVideoTime_module.setMin_Duration("" + minutes);
                postVideoTime_module.setSec_Duration("" + seconds);
                postVideoTime_module.setVideoId(videoID);

                Call<VideoTime_Model> responseCall = apiInterface.postvideotime(postVideoTime_module);
                Log.e("TAG", "V I D E O D A T A B O D Y --->" + new GsonBuilder().create().toJson(postVideoTime_module));
                Log.e("TAG", "V I D E O D A T A u r l --->" + responseCall.request().url());

                responseCall.enqueue(new Callback<VideoTime_Model>() {
                    @Override
                    public void onResponse(Call<VideoTime_Model> call, Response<VideoTime_Model> response) {
                        try {
                            if (response.body().getResId().equalsIgnoreCase(Constants.RES0000))
                                Log.e(TAG, "o n R e s p o n s e : " + response.body().getResponse());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoTime_Model> call, Throwable t) {
                        Log.e(TAG, "o n E r r o r ---->" + t.getLocalizedMessage());
                    }
                });
            } else {
                Log.e(TAG, "MIN  or sec is ZERO: ");
            }
        }
    }

}
