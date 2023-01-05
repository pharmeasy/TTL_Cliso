package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class VerifyPayURespModel {


    /**
     * ResponseCode : RES000
     * TransactionId : 2529732
     * OrderNo : VLB8A980
     * SourceCode : CUSTOMEMOB9004844180
     * ACCode : PP004
     * Status : PAYMENT PENDING
     * ChequeNo : Not Found
     * ResponseMessage : Payment Pending
     * ReqParameters : {"NameValueCollection":[{"Key":"ModeId","Value":"10","Required":"Provider","Hint":null},{"Key":"OrderNo","Value":"VLB8A980","Required":"System","Hint":null},{"Key":"TransactionId","Value":"2529732","Required":"Provider","Hint":null}],"URLId":20,"APIUrl":"https://techso.thyrocare.cloud/techsoapi/api/PayThyrocare/RecheckResponse"}
     */

    private String ResponseCode;
    private Integer TransactionId;
    private String OrderNo;
    private String SourceCode;
    private String ACCode;
    private String Status;
    private String ChequeNo;
    private String ResponseMessage;
    private ReqParametersDTO ReqParameters;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public Integer getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(Integer TransactionId) {
        this.TransactionId = TransactionId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String SourceCode) {
        this.SourceCode = SourceCode;
    }

    public String getACCode() {
        return ACCode;
    }

    public void setACCode(String ACCode) {
        this.ACCode = ACCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String ChequeNo) {
        this.ChequeNo = ChequeNo;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public ReqParametersDTO getReqParameters() {
        return ReqParameters;
    }

    public void setReqParameters(ReqParametersDTO ReqParameters) {
        this.ReqParameters = ReqParameters;
    }

    public static class ReqParametersDTO {
        /**
         * NameValueCollection : [{"Key":"ModeId","Value":"10","Required":"Provider","Hint":null},{"Key":"OrderNo","Value":"VLB8A980","Required":"System","Hint":null},{"Key":"TransactionId","Value":"2529732","Required":"Provider","Hint":null}]
         * URLId : 20
         * APIUrl : https://techso.thyrocare.cloud/techsoapi/api/PayThyrocare/RecheckResponse
         */

        private Integer URLId;
        private String APIUrl;
        private List<NameValueCollectionDTO> NameValueCollection;

        public Integer getURLId() {
            return URLId;
        }

        public void setURLId(Integer URLId) {
            this.URLId = URLId;
        }

        public String getAPIUrl() {
            return APIUrl;
        }

        public void setAPIUrl(String APIUrl) {
            this.APIUrl = APIUrl;
        }

        public List<NameValueCollectionDTO> getNameValueCollection() {
            return NameValueCollection;
        }

        public void setNameValueCollection(List<NameValueCollectionDTO> NameValueCollection) {
            this.NameValueCollection = NameValueCollection;
        }

        public static class NameValueCollectionDTO {
            /**
             * Key : ModeId
             * Value : 10
             * Required : Provider
             * Hint : null
             */

            private String Key;
            private String Value;
            private String Required;
            private Object Hint;

            public String getKey() {
                return Key;
            }

            public void setKey(String Key) {
                this.Key = Key;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }

            public String getRequired() {
                return Required;
            }

            public void setRequired(String Required) {
                this.Required = Required;
            }

            public Object getHint() {
                return Hint;
            }

            public void setHint(Object Hint) {
                this.Hint = Hint;
            }
        }
    }
}
