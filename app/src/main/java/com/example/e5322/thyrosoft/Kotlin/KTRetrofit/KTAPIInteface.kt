package com.example.e5322.thyrosoft.Kotlin.KTRetrofit

import com.example.e5322.thyrosoft.Kotlin.KTModels.FAQSpinmodel
import com.example.e5322.thyrosoft.Kotlin.KTModels.Noticeboardmodel
import com.example.e5322.thyrosoft.Models.VideoLangaugesResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface KTAPIInteface {

    @GET("Get_Faq_Type_Spinner")
    fun getFAQspin(): Call<FAQSpinmodel?>?

    @GET("getNoticeMessages")
    fun getNoticeboard():Call<Noticeboardmodel?>

}