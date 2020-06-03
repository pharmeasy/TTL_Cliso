package com.example.e5322.thyrosoft.Retrofit;


import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.example.e5322.thyrosoft.Models.AppuserResponse;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.COVfiltermodel;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.CovidAccessReq;
import com.example.e5322.thyrosoft.Models.CovidMIS_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.CovidaccessRes;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.EmailModel;
import com.example.e5322.thyrosoft.Models.EmailValidationResponse;
import com.example.e5322.thyrosoft.Models.GetVideoLanguageWiseRequestModel;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.LeadRequestModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Models.OTPrequest;
import com.example.e5322.thyrosoft.Models.PostLeadDataModel;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostAPIInteface {

    @POST("COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);

    @POST("COMMON.svc/Token")
    Call<Tokenresponse> getotptoken(@Body OTPrequest otPrequest);

    @POST("Common.svc/CovidRates")
    Call<Covidratemodel> displayrates();

    @POST("Common.svc/PatientDetails")
    Call<Covidmis_response> getcovidmis(@Body CovidMIS_req covidMIS_req);

    @POST("COMMON.svc/VerifyMobile")
    Call<COVerifyMobileResponse> covmobileVerification(@Body CoVerifyMobReq coVerifyMobReq);

    @POST("Common.svc/CovidAccess")
    Call<CovidaccessRes> checkcovidaccess(@Body CovidAccessReq covidaccessRes);

    @POST("Common.svc/GeneratedOtp")
    Call<Covidotpresponse> generateotp(@Body COVIDgetotp_req coviDgetotp_req);

    @GET("Common.svc/GetCovidStatus")
    Call<COVfiltermodel>getfilter();

    @POST("Common.svc/Verifyotp")
    Call<Covid_validateotp_res> validateotp(@Body Covid_validateotp_req covid_validateotp_req);

    @POST("order.svc/Leads_products")
    Call<LeadResponseModel> getLead(@Body LeadRequestModel leadRequestModel);


    @POST("ORDER.svc/PostorderdataPromo")
    Call<LeadDataResponseModel> PostdataLead(@Body PostLeadDataModel postLeadDataModel);

    @POST("order.svc/Appuser")
    Call<AppuserResponse> PostUserLog(@Body AppuserReq appuserReq);

    @POST("MASTER.svc/Emailvalidate")
    Call<EmailValidationResponse> getvalidemail(@Body EmailModel emailModel);


}