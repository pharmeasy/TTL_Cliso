package com.example.e5322.thyrosoft.Retrofit;

import com.example.e5322.thyrosoft.Models.CampIdRequestModel;
import com.example.e5322.thyrosoft.Models.CampIdResponseModel;
import com.example.e5322.thyrosoft.Models.CenterList_Model;
import com.example.e5322.thyrosoft.Models.Cmpdt_Model;
import com.example.e5322.thyrosoft.Models.FirebaseModel;
import com.example.e5322.thyrosoft.Models.Firebasepost;
import com.example.e5322.thyrosoft.Models.GetScanReq;
import com.example.e5322.thyrosoft.Models.GetScanResponse;
import com.example.e5322.thyrosoft.Models.GetVideoResponse_Model;
import com.example.e5322.thyrosoft.Models.GetVideopost_model;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.Models.InsertReasonsReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailReq;
import com.example.e5322.thyrosoft.Models.InsertScandetailRes;
import com.example.e5322.thyrosoft.Models.InsertreasonResponse;
import com.example.e5322.thyrosoft.Models.OTPCreditMISRequestModel;
import com.example.e5322.thyrosoft.Models.PaitientDataResponseModel;
import com.example.e5322.thyrosoft.Models.PaitientdataRequestModel;
import com.example.e5322.thyrosoft.Models.PostValidateRequest;
import com.example.e5322.thyrosoft.Models.PostVideoTime_module;
import com.example.e5322.thyrosoft.Models.ResponseModels.OTPCreditResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.WOEResponseModel;
import com.example.e5322.thyrosoft.Models.ScansummaryModel;
import com.example.e5322.thyrosoft.Models.ServiceModel;
import com.example.e5322.thyrosoft.Models.SlotModel;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
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

    @GET("{passSpinner_value}/{Contact_Details}")
    Call<Cmpdt_Model.ContactArrayListBean> getemployeedt(@Path("passSpinner_value") String passSpinner_value, @Path("Contact_Details") String Contact_Details);


    @GET("MasterData/GetCenterMaster/{user}")
    Call<List<CenterList_Model>> getcenterList(@Header("Authorization") String header, @Path("user") String user);


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


    @POST("WO.svc/postworkorder")
    Call<WOEResponseModel> GetResponse(@Body WOERequestModel woeRequestModel);

    @POST("Common.svc/PostPatientData")
    Call<PaitientDataResponseModel> PostPatientdetails(@Body PaitientdataRequestModel paitientdataRequestModel);


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


}
