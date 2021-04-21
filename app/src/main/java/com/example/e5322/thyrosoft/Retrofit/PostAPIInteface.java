package com.example.e5322.thyrosoft.Retrofit;


import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.example.e5322.thyrosoft.Models.AppuserResponse;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.COVerifyMobileResponse;
import com.example.e5322.thyrosoft.Models.COVfiltermodel;
import com.example.e5322.thyrosoft.Models.CertificateRequestModel;
import com.example.e5322.thyrosoft.Models.CertificateResponseModel;
import com.example.e5322.thyrosoft.Models.CoVerifyMobReq;
import com.example.e5322.thyrosoft.Models.CovidAccessReq;
import com.example.e5322.thyrosoft.Models.CovidAccessResponseModel;
import com.example.e5322.thyrosoft.Models.CovidMIS_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.Covidratemodel;
import com.example.e5322.thyrosoft.Models.EmailModel;
import com.example.e5322.thyrosoft.Models.EmailValidationResponse;
import com.example.e5322.thyrosoft.Models.GetVideoLanguageWiseRequestModel;
import com.example.e5322.thyrosoft.Models.Hospital_model;
import com.example.e5322.thyrosoft.Models.Hospital_req;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.LeadRequestModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Models.OTPrequest;
import com.example.e5322.thyrosoft.Models.PayTmChecksumRequestModel;
import com.example.e5322.thyrosoft.Models.PayTmChecksumResponseModel;
import com.example.e5322.thyrosoft.Models.PostBarcodeModel;
import com.example.e5322.thyrosoft.Models.PostBarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.PostLeadDataModel;
import com.example.e5322.thyrosoft.Models.RATEnteredRequestModel;
import com.example.e5322.thyrosoft.Models.RATEnteredResponseModel;
import com.example.e5322.thyrosoft.Models.RequestModels.AckBroadcastMsgRequestModel;
import com.example.e5322.thyrosoft.Models.RequestModels.GetPatientDetailsRequestModel;
import com.example.e5322.thyrosoft.Models.RequestModels.PaytmVerifyChecksumRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.AckBroadcastMsgResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PatientDetailsAPiResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PaytmVerifyChecksumResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.Tokenresponse;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;
import com.example.e5322.thyrosoft.Models.WOERequestModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostAPIInteface {

    @POST("COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);

    @POST("COMMON.svc/Token")
    Call<Tokenresponse> getotptoken(@Body OTPrequest otPrequest);

    @POST("GetWOEHospital")
    Call<Hospital_model> GetWOEHospital(@Body Hospital_req hospital_req);

    @POST("CovidRates")
    Call<Covidratemodel> displayrates();

    @POST("COMMON.svc/CertificateDetails")
    Call<CertificateResponseModel> getCertificates(@Body CertificateRequestModel certificateRequestModel);

    @POST(" ORDER.svc/PostConsignment")
    Call<PostBarcodeResponseModel> postBarcodes(@Body PostBarcodeModel postBarcodeModel);

    @POST("PatientDetails")
    Call<Covidmis_response> getcovidmis(@Body CovidMIS_req covidMIS_req);

    @POST("VerifyMobile")
    Call<COVerifyMobileResponse> covmobileVerification(@Body CoVerifyMobReq coVerifyMobReq);

    @POST("PrivilegeAcess")
    Call<CovidAccessResponseModel> checkcovidaccess(@Body CovidAccessReq covidaccessRes);

    @POST("GeneratedOtp")
    Call<Covidotpresponse> generateotp(@Body COVIDgetotp_req coviDgetotp_req);

    @GET("GetCovidStatus")
    Call<COVfiltermodel> getfilter();

    @POST("Verifyotp")
    Call<Covid_validateotp_res> validateotp(@Body Covid_validateotp_req covid_validateotp_req);

    @POST("order.svc/Leads_products")
    Call<LeadResponseModel> getLead(@Body LeadRequestModel leadRequestModel);

    @POST("ORDER.svc/PostorderdataPromo")
    Call<LeadDataResponseModel> PostdataLead(@Body PostLeadDataModel postLeadDataModel);

    @POST("GetWOEPatientImagedetails")
    Call<RATEnteredResponseModel> GetEnteredResponse(@Body RATEnteredRequestModel enteredRequestModel);

    @POST("Appuser")
    Call<AppuserResponse> PostUserLog(@Body AppuserReq appuserReq);

    @POST("MASTER.svc/Emailvalidate")
    Call<EmailValidationResponse> getvalidemail(@Body EmailModel emailModel);

    @POST("B2B/WO.svc/postworkorder")
    Call<WOEResponseModel> PostUserLog(@Body WOERequestModel requestModel);

    @POST("PostBroadCastAck")
    Call<AckBroadcastMsgResponseModel> PostAckBroadcastMsgAPI(@Body AckBroadcastMsgRequestModel requestModel);

    @POST("PaymentGateway.svc/PayTMAccess")
    Call<PayTmChecksumResponseModel> GetPayTmCheckSum(@Body PayTmChecksumRequestModel payTmChecksumRequestModel);

    @POST("PaymentGateway.svc/PayTMStatus")
    Call<PaytmVerifyChecksumResponseModel> callPaytmVerifyChecksumAPI(@Body PaytmVerifyChecksumRequestModel requestModel);

    @GET("GetSRFCovidStatus")
    Call<COVfiltermodel> getSRFCovidWOEStatus();

    @POST("GetSRFDetails")
    Call<Covidmis_response> getSRFcovidWOEmis(@Body CovidMIS_req covidMIS_req);

    @POST("getPatients")
    Call<PatientDetailsAPiResponseModel> CallGetPatientDetailsAPI(@Body GetPatientDetailsRequestModel requestModel);
}