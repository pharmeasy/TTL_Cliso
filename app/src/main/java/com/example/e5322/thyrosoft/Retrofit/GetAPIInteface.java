package com.example.e5322.thyrosoft.Retrofit;

import com.example.e5322.thyrosoft.Models.Cmpdt_Model;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.Models.PostValidateRequest;
import com.example.e5322.thyrosoft.Models.ValidateOTPmodel;
import com.example.e5322.thyrosoft.Models.VerifyotpModel;
import com.example.e5322.thyrosoft.Models.VerifyotpRequest;
import com.example.e5322.thyrosoft.SourceILSModel.SourceILSMainModel;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetAPIInteface {


    @GET("ORDER.svc/Health")
    Call<HealthTipsApiResponseModel> getHealth();

    @GET("{passSpinner_value}/{Contact_Details}")
    Call<Cmpdt_Model.ContactArrayListBean> getemployeedt(@Path("passSpinner_value") String passSpinner_value, @Path("Contact_Details") String Contact_Details);

    @GET("{api_key}/{user}/{getclient}")
    Call<SourceILSMainModel> getClient(@Path("api_key") String api_key, @Path("user") String user, @Path("getclient") String getclient);

    @POST("Otpgenerate")
    Call<ValidateOTPmodel> ValidateMob(@Body PostValidateRequest postValidateRequest);

    @POST("OtpVerification")
    Call<VerifyotpModel> Verifyotp(@Body VerifyotpRequest verifyotpRequest);

}
