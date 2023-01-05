package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class StartPayTmRespModel {


    /**
     * ResponseCode : RES000
     * TransactionId : 2529727
     * ResponseMessage : SUCCESS
     * tokenData :  <html> <img src='data: image / png; base64,iVBORw0KGgoAAAANSUhEUgAAAeAAAAHgAQAAAABVr4M4AAAEI0lEQVR42u2dO3bbQAxFoeOCJZegpWhp1tK0FC5BJQsdIUP8STlpkibEYxObnDtpcAa/NzDxXzwEGDBgwP8CXkif2/j1Nf79ftDMrwuPH67j1Vs+PSd5w8u82vIZcCd4FnO5b/C29Nu323ZZ6TLexi6DWb9k+QK4F7wZzYAXmsYKvo/fJjG1sYsYFt30P9DtvsTmADeFxZ62FWOXzebo+iR9M34YjJga4N6wnDOyizgteTZGPgk8HsA9YfNVtotspyvGUvFVvAU0FvT8xtEBPjfsUa8yussffvg5ZAZ8ajie7axRF8WrJ0tiYXowSWr02+Qb8KlhNxpLjfKT+ioWRiKb/b6AG8Hjg7gfS3skNXKG/Y36sxH1qtPS+gvgNvASoa2bmka9khpNXn/xT7ovEeBWcJw+i1TZrCYrh44m0tvS5yRFOsmfj44OcA+YuRjWzTwTR7mW3cLkPHp/RL2Azw5bsCuFewttn+qrzDNZ/cU+yRuuvgpwB3hiL669dob1qMeQMlnJBdwKThflibS1ecyNSRwjPSGp6evBlLVewA1gS402FzWZH7KlGRBHKT8EB/v8GfDp4Sni2JfV9O08Uu+l+748stE2oUdDgFvAegyVJJm0YZzyAk+NjAnlCuA+cNZkNUl2c1PDOlZyVxccAO4Eh6pg5ijFilCJyM4j1g6Q+ireH0OAe8ARyEYBzqUnGeLowWSNH8DtYFU6ivhVxShsvkoeNSxODdNBpAC4CRyJEHna8zJmzTaPCqxnTn8GuA1s7se1j+KQLKN2WaQpZYt0iUpeBfj8cJE8srUAfTtLlr5TzKSNn6NiCfDpYQrPNKVQqahSNFu2+ksKJQE3gr0C6/0er7upZJYiR6KiM4jGD+AmMJs+KZuCsysPyKu0th2Z9C1rvYB7wFLTL1f77InGT9wD5NIAWAB3glNnQLaL3fEThktqZFb4mT8DPjucDT8Tv3r4co/Gz60KlaxfyID7wZEk+wCCoqKuhhVqSMCt4JA8ikOSmzl+EfQHDcqLDtEQ4PPDFqzcI46xHCkYS4S8/pInFOA28E46sJNTP6zupp6Jc6RFqekDbgJr5+8W827cF8VQrTpCSwPiffIN+Pyweya7mMFx/a/cGt1L7XfREOAG8DMuXYQGhY5xzNPc2DX0kYB7wRRXdCKijZq+6gzKMMeVDjp9wA3gJVuAsdS6O0XfRnSMjAE3gnMCzlz6xJcU3+9aQaYzANwLLkKlciN0fwxds4Abs9gAd4JzKh+znz5pYT7A4nVQNQHuBecQ2C+TFxTPdPmpu3zUPgLuA3NMqdgHu1ymIxXBCuCO8JpGpG3CJ1VfZeXad20IAG4Cx93g3QCLmFthveR31TB9ODrA54arMFqXpj4pdjFfdbXZBI/PMcOATwzjr0gBBgz4v4V/AYxn4A1gFLoEAAAAAElFTkSuQmCC' id = 'ItemPreview'></html>
     * ReqParameters : {"NameValueCollection":[{"Key":"ModeId","Value":"3","Required":"Provider","Hint":null},{"Key":"TransactionId","Value":"2529727","Required":"Provider","Hint":null}],"URLId":10,"APIUrl":"https://techso.thyrocare.cloud/techsoapi/api/PayThyrocare/RecheckResponse"}
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
         * NameValueCollection : [{"Key":"ModeId","Value":"3","Required":"Provider","Hint":null},{"Key":"TransactionId","Value":"2529727","Required":"Provider","Hint":null}]
         * URLId : 10
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
             * Value : 3
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
