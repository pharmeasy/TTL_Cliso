package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class DynamicPaymentResponseModel {


    /**
     * RespId : RES0000
     * Response : SUCCESS
     * payModeLists : [{"PaymentGatway":"PayTM"},{"PaymentGatway":"PayTM"}]
     */

    private String RespId;
    private String Response;
    private ArrayList<PayModeListsDTO> payModeLists;

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String RespId) {
        this.RespId = RespId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ArrayList<PayModeListsDTO> getPayModeLists() {
        return payModeLists;
    }

    public void setPayModeLists(ArrayList<PayModeListsDTO> payModeLists) {
        this.payModeLists = payModeLists;
    }

    public static class PayModeListsDTO {
        /**
         * PaymentGatway : PayTM
         */

        private String PaymentGatway;

        public String getPaymentGatway() {
            return PaymentGatway;
        }

        public void setPaymentGatway(String PaymentGatway) {
            this.PaymentGatway = PaymentGatway;
        }
    }
}
