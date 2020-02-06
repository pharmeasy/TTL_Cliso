package com.example.e5322.thyrosoft.Kotlin.KTRetrofit

import com.example.e5322.thyrosoft.Kotlin.KTModels.FAQSpinmodel
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTInsertReasonsReq
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTInsertreasonResponse
import com.example.e5322.thyrosoft.Kotlin.KTModels.Noticeboardmodel
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

    @POST("insertrodtls")
     fun insertreason(@Body ktInsertReasonsReq: KTInsertReasonsReq): Call<KTInsertreasonResponse>

}