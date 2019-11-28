package com.example.e5322.thyrosoft.Retrofit;


import com.example.e5322.thyrosoft.Models.GetVideoLanguageWiseRequestModel;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface PostAPIInteface {

    @POST("COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);
}