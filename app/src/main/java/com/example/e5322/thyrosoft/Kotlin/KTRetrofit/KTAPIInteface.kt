package com.example.e5322.thyrosoft.Kotlin.KTRetrofit

import com.example.e5322.thyrosoft.Kotlin.KTModels.*
import com.example.e5322.thyrosoft.Models.GetScanReq
import com.example.e5322.thyrosoft.Models.GetScanResponse
import com.example.e5322.thyrosoft.Models.InsertreasonResponse
import com.example.e5322.thyrosoft.Models.VideoLangaugesResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface KTAPIInteface {

    @GET("Get_Faq_Type_Spinner")
    fun getFAQspin(): Call<FAQSpinmodel?>?

    @GET("getNoticeMessages")
    fun getNoticeboard():Call<Noticeboardmodel?>

    @GET("DynamicFaq")
    fun getFAQ():Call<KTFAQandANSArray?>

    @POST("insertrodtls")
     fun insertreason(@Body ktInsertReasonsReq: KTInsertReasonsReq): Call<KTInsertreasonResponse>

}