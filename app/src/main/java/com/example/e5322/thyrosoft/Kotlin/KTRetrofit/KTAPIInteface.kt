package com.example.e5322.thyrosoft.Kotlin.KTRetrofit

import com.example.e5322.thyrosoft.Kotlin.KTModels.*
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface KTAPIInteface {

    @GET("Get_Faq_Type_Spinner")
    fun getFAQspin(): Call<FAQSpinmodel?>?

    @GET("getNoticeMessages")
    fun getNoticeboard():Call<NoticeBoard_Model?>

    @GET("DynamicFaq")
    fun getFAQ():Call<KTFAQandANSArray?>

    @POST("insertrodtls")
     fun insertreason(@Body ktInsertReasonsReq: KTInsertReasonsReq): Call<KTInsertreasonResponse>

    @POST("COMMON.svc/acknowledgeNotice")
    fun postack(@Body ktAcknowledmentreq: KTAcknowledmentreq): Call<KTAcknowledeResponse>

}