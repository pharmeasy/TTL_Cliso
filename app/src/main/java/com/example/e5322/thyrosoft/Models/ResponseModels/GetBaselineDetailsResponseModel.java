package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.ArrayList;

public class GetBaselineDetailsResponseModel {

    /**
     * Response : Success
     * ResponseId : RES0000
     * BaseDetails : [{"BASELINE":541000,"CURRENTMONTH":240262,"INCREMENTAL":-300738,"M2":485208,"M3":565351,"M4":574776,"M5":501763,"M6":283320,"M13":514086}]
     */

    private String Response;
    private String ResponseId;
    private ArrayList<BaseDetailsDTO> BaseDetails;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String ResponseId) {
        this.ResponseId = ResponseId;
    }

    public ArrayList<BaseDetailsDTO> getBaseDetails() {
        return BaseDetails;
    }

    public void setBaseDetails(ArrayList<BaseDetailsDTO> BaseDetails) {
        this.BaseDetails = BaseDetails;
    }

    public static class BaseDetailsDTO {
        /**
         * BASELINE : 541000
         * CURRENTMONTH : 240262
         * INCREMENTAL : -300738
         * M2 : 485208
         * M3 : 565351
         * M4 : 574776
         * M5 : 501763
         * M6 : 283320
         * M13 : 514086
         */

        private Integer BASELINE;
        private Integer CURRENTMONTH;
        private Integer INCREMENTAL;
        private Integer M2;
        private Integer M3;
        private Integer M4;
        private Integer M5;
        private Integer M6;
        private Integer M13;
        private String URL;

        public Integer getBASELINE() {
            return BASELINE;
        }

        public void setBASELINE(Integer BASELINE) {
            this.BASELINE = BASELINE;
        }

        public Integer getCURRENTMONTH() {
            return CURRENTMONTH;
        }

        public void setCURRENTMONTH(Integer CURRENTMONTH) {
            this.CURRENTMONTH = CURRENTMONTH;
        }

        public Integer getINCREMENTAL() {
            return INCREMENTAL;
        }

        public void setINCREMENTAL(Integer INCREMENTAL) {
            this.INCREMENTAL = INCREMENTAL;
        }

        public Integer getM2() {
            return M2;
        }

        public void setM2(Integer M2) {
            this.M2 = M2;
        }

        public Integer getM3() {
            return M3;
        }

        public void setM3(Integer M3) {
            this.M3 = M3;
        }

        public Integer getM4() {
            return M4;
        }

        public void setM4(Integer M4) {
            this.M4 = M4;
        }

        public Integer getM5() {
            return M5;
        }

        public void setM5(Integer M5) {
            this.M5 = M5;
        }

        public Integer getM6() {
            return M6;
        }

        public void setM6(Integer M6) {
            this.M6 = M6;
        }

        public Integer getM13() {
            return M13;
        }

        public void setM13(Integer M13) {
            this.M13 = M13;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
