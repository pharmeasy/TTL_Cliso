package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class StartPayURespModel {


    /**
     * ResponseCode : RES000
     * TransactionId : null
     * ResponseMessage : SUCCESS
     * tokenData : Payment Link will sent to Customer Shortly
     * ReqParameters : {"NameValueCollection":[{"Key":"ModeId","Value":"10","Required":"Provider","Hint":null},{"Key":"OrderNo","Value":null,"Required":"System","Hint":null},{"Key":"TransactionId","Value":"","Required":"Provider","Hint":null}],"URLId":20,"APIUrl":"https://techso.thyrocare.cloud/techsoapi/api/PayThyrocare/RecheckResponse"}
     */

    private String ResponseCode;
    private String TransactionId;
    private String ResponseMessage;
    private String tokenData;
    private ReqParametersDTO ReqParameters;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String TransactionId) {
        this.TransactionId = TransactionId;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public String getTokenData() {
        return tokenData;
    }

    public void setTokenData(String tokenData) {
        this.tokenData = tokenData;
    }

    public ReqParametersDTO getReqParameters() {
        return ReqParameters;
    }

    public void setReqParameters(ReqParametersDTO ReqParameters) {
        this.ReqParameters = ReqParameters;
    }

    public static class ReqParametersDTO {
        /**
         * NameValueCollection : [{"Key":"ModeId","Value":"10","Required":"Provider","Hint":null},{"Key":"OrderNo","Value":null,"Required":"System","Hint":null},{"Key":"TransactionId","Value":"","Required":"Provider","Hint":null}]
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
