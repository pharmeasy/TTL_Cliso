package com.example.e5322.thyrosoft.Controller;

import com.example.e5322.thyrosoft.Activity.HealthArticle_Activity;
import com.example.e5322.thyrosoft.GlobalClass;

public class GetHealthTipsController {

    // HealthTipsFragment mhealthTipsFragment;
    HealthArticle_Activity mContext;
    GlobalClass globalClass;
    int flag = 0;

    public GetHealthTipsController(HealthArticle_Activity mContext) {
        this.mContext = mContext;
        globalClass = new GlobalClass(mContext);
        flag = 1;

    }

    public void CallHealthTipsApi() {

      /*  globalClass.setLoadingGIF(mContext);
        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(mContext, HEALTH_BASE_URL).create(APIInteface.class);
        Call<HealthTipsApiResponseModel> responseCall = apiInterface.getHealthtips();
        Log.e("TAG", "URL --->" + responseCall.request().url());

        responseCall.enqueue(new Callback<HealthTipsApiResponseModel>() {
            @Override
            public void onResponse(Call<HealthTipsApiResponseModel> call, Response<HealthTipsApiResponseModel> response) {
                globalClass.hideProgressDialog();
                System.out.println("HealthTips Onsuccess");

                if (response.isSuccessful() && response.body() != null && response != null) {
                    HealthTipsApiResponseModel healthTipsApiResponseModel = response.body();
                    if (healthTipsApiResponseModel.getRESPONSE().equalsIgnoreCase("Success")) {
                        if (flag == 1) {
                            mContext.onHealthTpisApiResponse(healthTipsApiResponseModel);
                        }
                    }
                } else {
                    globalClass.showcenterCustomToast(mContext, "Something went wrong please try after sometime.");
                }
            }

            @Override
            public void onFailure(Call<HealthTipsApiResponseModel> call, Throwable t) {
                globalClass.hideProgressDialog();
                // globalClass.showcenterCustomToast(mContext , UnableToConnectMsg);
                Log.d("Errror", t.getMessage());
                System.out.println("HealthTips OnFailure");
            }
        });*/
    }
}
