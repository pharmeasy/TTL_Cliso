package com.example.e5322.thyrosoft.Retrofit;

import com.example.e5322.thyrosoft.HHHtest.Model.CampDetailsResponseModel;
import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientDetailRequestModel;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientResponseModel;
import com.example.e5322.thyrosoft.Models.BarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.CampIdRequestModel;
import com.example.e5322.thyrosoft.Models.CampIdResponseModel;
import com.example.e5322.thyrosoft.Models.CategoryResModel;
import com.example.e5322.thyrosoft.Models.CenterList_Model;
import com.example.e5322.thyrosoft.Models.Cmpdt_Model;
import com.example.e5322.thyrosoft.Models.ConatctsResponseModel;
import com.example.e5322.thyrosoft.Models.ContactsReqModel;
import com.example.e5322.thyrosoft.Models.ConvertBTBNRequestModel;
import com.example.e5322.thyrosoft.Models.ConvertBTBNResponseModel;
import com.example.e5322.thyrosoft.Models.DyanamicPaymentReqModel;
import com.example.e5322.thyrosoft.Models.DynamicPaymentResponseModel;
import com.example.e5322.thyrosoft.Models.FeedbackPostQuestionsRequestModel;
import com.example.e5322.thyrosoft.Models.FeedbackQuestionsResponseModel;
import com.example.e5322.thyrosoft.Models.FirebaseModel;
import com.example.e5322.thyrosoft.Models.Firebasepost;
import com.example.e5322.thyrosoft.Models.GetLeadgerBalnce;
import com.example.e5322.thyrosoft.Models.GetLocationReqModel;
import com.example.e5322.thyrosoft.Models.GetLocationRespModel;
import com.example.e5322.thyrosoft.Models.GetScanReq;
import com.example.e5322.thyrosoft.Models.GetScanResponse;
import com.example.e5322.thyrosoft.Models.GetTestCodeResponseModel;
import com.example.e5322.thyrosoft.Models.GetVideoResponse_Model;
import com.example.e5322.thyrosoft.Models.GetVideopost_model;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.Models.InsertReasonsReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailRes;
import com.example.e5322.thyrosoft.Models.InsertreasonResponse;
import com.example.e5322.thyrosoft.Models.LeadChannelRespModel;
import com.example.e5322.thyrosoft.Models.LeadPurposeResponseModel;
import com.example.e5322.thyrosoft.Models.LeadReqModel;
import com.example.e5322.thyrosoft.Models.LeadRespModel;
import com.example.e5322.thyrosoft.Models.OTPCreditMISRequestModel;
import com.example.e5322.thyrosoft.Models.PaitientDataResponseModel;
import com.example.e5322.thyrosoft.Models.PaitientdataRequestModel;
import com.example.e5322.thyrosoft.Models.PayUReqModel;
import com.example.e5322.thyrosoft.Models.PostValidateRequest;
import com.example.e5322.thyrosoft.Models.PostVideoTime_module;
import com.example.e5322.thyrosoft.Models.RequestModels.GetBaselineDetailsRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.FeedbackPostQuestionsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.FeedbackQuestionRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBaselineDetailsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBroadcastsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetTermsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.HandbillsResponse;
import com.example.e5322.thyrosoft.Models.ResponseModels.OTPCreditResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.PostTermsRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.TemplateResponse;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.ScanBarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.Models.ServiceModel;
import com.example.e5322.thyrosoft.Models.SlotModel;
import com.example.e5322.thyrosoft.Models.StartPayTmRespModel;
import com.example.e5322.thyrosoft.Models.StartPayURespModel;
import com.example.e5322.thyrosoft.Models.StartPaytmReqModel;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Models.VerifyPayTMReqModel;
import com.example.e5322.thyrosoft.Models.VerifyPayTMRespModel;
import com.example.e5322.thyrosoft.Models.VerifyPayURespModel;
import com.example.e5322.thyrosoft.Models.VerifyPayUreqModel;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.Models.VerifyotpRequest;
import com.example.e5322.thyrosoft.Models.VideoLangaugesResponseModel;
import com.example.e5322.thyrosoft.Models.VideoTime_Model;
import com.example.e5322.thyrosoft.Models.Videopoppost;
import com.example.e5322.thyrosoft.Models.Videopoppost_response;
import com.example.e5322.thyrosoft.Models.WOERequestModel;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInteface {


    @GET("ORDER.svc/Health")
    Call<HealthTipsApiResponseModel> getHealth();

    @GET("Persuasion/Gethandbills")
    Call<TemplateResponse> getTemplate();

    @GET("Persuasion/GethandbillsofCustomer/{user}")
    Call<HandbillsResponse> gethandbills(@Path("user") String user);

    @GET("{passSpinner_value}/{Contact_Details}")
    Call<Cmpdt_Model.ContactArrayListBean> getemployeedt(@Path("passSpinner_value") String passSpinner_value, @Path("Contact_Details") String Contact_Details);

    @POST("commonservices/LeadPurpose")
    Call<LeadPurposeResponseModel> getLeadPurpose();

    @GET("Common.svc/GetTestList")
    Call<GetTestResponseModel> gettestdata();

    @POST("bookingmaster/lead")
    Call<LeadRespModel> PostLead(@Body LeadReqModel leadRequestModel);

    @POST("ORDER.svc/GetResPaymentMode")
    Call<DynamicPaymentResponseModel> GetDynamicPayment(@Body DyanamicPaymentReqModel dyanamicPaymentReqModel);


    @GET("LedgerDetails/{SourceCode}")
    Call<GetLeadgerBalnce> getLedgerDetails(@Path("SourceCode") String SourceCode);


    @GET("ORDER.svc/{SourceCode}/CovidBarcodeDetails")
    Call<BarcodeResponseModel> getBarcodeDetails(@Path("SourceCode") String SourceCode);

    @GET("ORDER.svc/{SourceCode}/PouchCodeDetails")
    Call<ScanBarcodeResponseModel> getScannedBarcode(@Path("SourceCode") String SourceCode);


/*    @GET("Showlang")
    Call<Language_Model> getlanguage();*/

    @POST("Common.svc/CampDetails")
    Call<CampDetailsResponseModel> GetCampDetails(@Body CampIdRequestModel campIdRequestModel);


    @GET("MasterData/GetCenterMaster/{user}")
    Call<List<CenterList_Model>> getcenterList(@Header("Authorization") String header, @Path("user") String user);


    @GET("GetTestCode/{user}")
    Call<GetTestCodeResponseModel> getTestCode(@Path("user") String user);



    @GET("MasterData/GetSlots/{date}/{centerId}")
    Call<List<SlotModel>> getslot(@Header("Authorization") String header, @Path("date") String date, @Path("centerId") String centerId);

    @GET("LeadBooking/GetFMELeads/{fromDate}/{toDate}/{ReferenceId}")
    Call<List<ScansummaryModel>> getsummarydetail(@Header("Authorization") String header, @Path("fromDate") String fromDate, @Path("toDate") String toDate, @Path("ReferenceId") String ReferenceId);


    @GET("MasterData/GetServiceMaster/{centerID}/{user}")
    Call<List<ServiceModel>> getservicelist(@Header("Authorization") String header, @Path("centerID") String centerID, @Path("user") String user);

    @GET("{api_key}/{user}/{getclient}")
    Call<SourceILSMainModel> getClient(@Path("api_key") String api_key, @Path("user") String user, @Path("getclient") String getclient);

    @POST("Otpgenerate")
    Call<ValidateOTPmodel> ValidateMob(@Body PostValidateRequest postValidateRequest);

    @POST("COMMON.svc/VideoData")
    Call<VideoTime_Model> postvideotime(@Body PostVideoTime_module postVideoTime_module);

    @POST("Common.svc/GetCampid")
    Call<CampIdResponseModel> GetCampID(@Body CampIdRequestModel campIdRequestModel);


    @POST("postworkorder")
    Call<WOEResponseModel> GetResponse(@Body WOERequestModel woeRequestModel);

    @POST("Common.svc/PostPatientData")
    Call<PaitientDataResponseModel> PostPatientdetails(@Body PaitientdataRequestModel paitientdataRequestModel);


    @POST("Common.svc/GetPOCTWOEPatientImagedetails")
    Call<PatientResponseModel> getResponse(@Body PatientDetailRequestModel patientDetailRequestModel);

    @POST("techsoapi/api/PayThyrocare/StartTransaction")
    Call<StartPayTmRespModel> startPaytmTrans(@Body StartPaytmReqModel startPaytmReqModel);

    @POST("techsoapi/api/PayThyrocare/StartTransaction")
    Call<StartPayURespModel> startPayUTrans(@Body PayUReqModel payUReqModel);

    @POST("techsoapi/api/PayThyrocare/RecheckResponse")
    Call<VerifyPayTMRespModel> VerifyPayTM(@Body VerifyPayTMReqModel verifyPayTMReqModel);


    @POST("techsoapi/api/PayThyrocare/RecheckResponse")
    Call<VerifyPayURespModel> VerifyPayU(@Body VerifyPayUreqModel verifyPayUreqModel);

    @POST("Mapping")
    Call<FirebaseModel> pushtoken(@Body Firebasepost firebasepost);


    @GET("COMMON.svc/Showlang")
    Call<VideoLangaugesResponseModel> getVideoLanguages();

    @POST("OtpVerification")
    Call<VerifyotpModel> Verifyotp(@Body VerifyotpRequest verifyotpRequest);

    @POST("Showvideodata")
    Call<GetVideoResponse_Model> getVideoData(@Body GetVideopost_model getVideopost_model);


    @POST("Videopoppost")
    Call<Videopoppost_response> Videopost(@Body Videopoppost videopoppost);

    @POST("insertrodetails")
    Call<InsertScandetailRes> insertScandetail(@Body InsertScandetailReq insertScandetailReq);

    @POST("insertrodtls")
    Call<InsertreasonResponse> insertreason(@Body InsertReasonsReq insertReasonsReq);

    @POST("displayrodeails")
    Call<GetScanResponse> getMIS(@Body GetScanReq getScanReq);

    @POST("OtpLedgermis")
    Call<OTPCreditResponseModel> OTPCreditMIS(@Body OTPCreditMISRequestModel creditMISRequestModel);

    @GET("{api_key}/getBroadCast")
    Call<GetBroadcastsResponseModel> callGetBroadcastAPI(@Path("api_key") String api_key);

    @POST("commonservices/LeadChannel")
    Call<LeadChannelRespModel> getLeadChannel();

    @POST("SubmitNHLdata")
    Call<ConvertBTBNResponseModel> ConvertBttoBn(@Body ConvertBTBNRequestModel convertBTBNRequestModel);

    @POST("GetTestCode")
    Call<GetLocationRespModel> GetLocation(@Body  GetLocationReqModel getLocationReqModel);

    @POST("FAQ.svc/GetContacts")
    Call<ConatctsResponseModel> GetConatcts(@Body ContactsReqModel contactsReqModel);

    @POST("GetQuestions")
    Call<FeedbackQuestionsResponseModel> GetFeedbackQuestions(@Body FeedbackQuestionRequestModel feedbackQuestionRequestModel);

    @POST("Postfeddetails")
    Call<FeedbackPostQuestionsResponseModel> PostFeedbackQuestions(@Body FeedbackPostQuestionsRequestModel feedbackPostQuestionsRequestModels);

    @POST("GetBaselineDeatils")
    Call<GetBaselineDetailsResponseModel> GetBaselineDetails(@Body GetBaselineDetailsRequestModel getBaselineDetailsRequestModel);

    @POST("GetTermsDeatils")
    Call<GetTermsResponseModel> GetTermsDetails(@Body GetBaselineDetailsRequestModel getBaselineDetailsRequestModel);

    @POST("PostTermsDeatils")
    Call<GetTermsResponseModel> PostTermsDetails(@Body PostTermsRequestModel requestModel);

    @GET("ClientEntry/GetCategoryList")
    Call<CategoryResModel> getCategoryList();

}
