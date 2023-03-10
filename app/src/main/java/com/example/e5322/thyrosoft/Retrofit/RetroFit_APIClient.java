package com.example.e5322.thyrosoft.Retrofit;

import android.app.Activity;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.BuildConfig;
import com.example.e5322.thyrosoft.GlobalClass;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFit_APIClient {

    public static RetroFit_APIClient apiClient;
    private Retrofit retrofit = null;

    public static RetroFit_APIClient getInstance() {
        if (apiClient == null) {
            apiClient = new RetroFit_APIClient();
        }
        return apiClient;
    }


    public Retrofit getClient(final Activity activity, String BASE_URL) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);// production build
        }
        client.addInterceptor(interceptor);
        client.readTimeout(1, TimeUnit.MINUTES);
        client.writeTimeout(30 , TimeUnit.SECONDS);
        client.connectTimeout(15, TimeUnit.SECONDS);
        client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader(Constants.HEADER_USER_AGENT, GlobalClass.getHeaderValue(activity)).build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                /*  .addConverterFactory(ScalarsConverterFactory.create())*/
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }


}
