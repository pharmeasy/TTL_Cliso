package com.example.e5322.thyrosoft.Kotlin.KTRetrofit

import android.app.Activity
import com.example.e5322.thyrosoft.API.Constants
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.BuildConfig
import com.example.e5322.thyrosoft.GlobalClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class KTRetrofitClient {

    var apiClient: KTRetrofitClient? = null
    private var retrofit: Retrofit? = null

    fun getInstance(): KTRetrofitClient? {
        if (apiClient == null) {
            apiClient = KTRetrofitClient()
        }
        return apiClient
    }


    fun getClient(activity: Activity?, BASE_URL: String?): Retrofit? {
        val client = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY // development build
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE // production build
        }

        client.addInterceptor(interceptor)
        client.readTimeout(1, TimeUnit.MINUTES)
        client.writeTimeout(30, TimeUnit.SECONDS)
        client.connectTimeout(15, TimeUnit.SECONDS)
        /*client.addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader(Constants.HEADER_USER_AGENT, GlobalClass.getHeaderValue(activity)).build()
            chain.proceed(request)
        }*/
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build()) /*  .addConverterFactory(ScalarsConverterFactory.create())*/
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit
    }


}