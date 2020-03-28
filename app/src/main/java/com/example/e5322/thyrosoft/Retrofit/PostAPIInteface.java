package com.example.e5322.thyrosoft.Retrofit;


import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.example.e5322.thyrosoft.Models.AppuserResponse;
import com.example.e5322.thyrosoft.Models.GetVideoLanguageWiseRequestModel;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.LeadRequestModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Models.PostLeadDataModel;
import com.example.e5322.thyrosoft.Models.VideosResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface PostAPIInteface {

    @POST("COMMON.svc/Showvideo")
    Call<VideosResponseModel> getVideobasedOnLanguage(@Body GetVideoLanguageWiseRequestModel languageWiseRequestModel);


    @POST("order.svc/Leads_products")
    Call<LeadResponseModel> getLead(@Body LeadRequestModel leadRequestModel);


    @POST("ORDER.svc/PostorderdataPromo")
    Call<LeadDataResponseModel> PostdataLead(@Body PostLeadDataModel postLeadDataModel);

    @POST("order.svc/Appuser")
    Call<AppuserResponse> PostUserLog(@Body AppuserReq appuserReq);



}