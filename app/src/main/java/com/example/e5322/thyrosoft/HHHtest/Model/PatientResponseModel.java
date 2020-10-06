package com.example.e5322.thyrosoft.HHHtest.Model;

import java.util.ArrayList;
import java.util.List;

public class PatientResponseModel {


    /**
     * PatientDetails : [{"Id":"27","barcode":"16dgttwf","mobile":"9594487199","name":"IT HI","result":" ","status":"0","testcode":"6","uploaded":[{"imageNAME":"Not Uploaded","imageURL":"","imgid":"12","tatmax":"240","tatmin":"0","uploadedtime":""},{"imageNAME":"HIV RESULT","imageURL":"http://www.thyrocare.com\\API_BETA\\PICKSO\\Files\\DatasheetTRF\\27_HIV RESULT.png","imgid":"13","tatmax":"1440","tatmin":"0","uploadedtime":"9/15/2020 12:19:44 PM"}]},{"Id":"28","barcode":"16dgttwf","mobile":"7718874661","name":"AMRUTA S","result":" ","status":"0","testcode":"6","uploaded":[{"imageNAME":"Not Uploaded","imageURL":"","imgid":"12","tatmax":"240","tatmin":"0","uploadedtime":""},{"imageNAME":"HIV RESULT","imageURL":"","imgid":"13","tatmax":"1440","tatmin":"0","uploadedtime":""}]}]
     * currenttime : 15-09-2020 14:15
     * resid : RES0000
     * response : Sucessful
     * result : [{"imagename":"Not Uploaded","imagesquence":"0","imgid":"12","tatmax":"240","tatmin":"0"},{"imagename":"HIV RESULT","imagesquence":"1","imgid":"13","tatmax":"1440","tatmin":"0"}]
     */

    private String currenttime;
    private String resid;
    private String response;
    private ArrayList<PatientDetailsBean> PatientDetails;
    private ArrayList<ResultBean> result;

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<PatientDetailsBean> getPatientDetails() {
        return PatientDetails;
    }

    public void setPatientDetails(ArrayList<PatientDetailsBean> PatientDetails) {
        this.PatientDetails = PatientDetails;
    }

    public ArrayList<ResultBean> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultBean> result) {
        this.result = result;
    }

    public static class PatientDetailsBean {
        /**
         * Id : 27
         * barcode : 16dgttwf
         * mobile : 9594487199
         * name : IT HI
         * result :
         * status : 0
         * testcode : 6
         * uploaded : [{"imageNAME":"Not Uploaded","imageURL":"","imgid":"12","tatmax":"240","tatmin":"0","uploadedtime":""},{"imageNAME":"HIV RESULT","imageURL":"http://www.thyrocare.com\\API_BETA\\PICKSO\\Files\\DatasheetTRF\\27_HIV RESULT.png","imgid":"13","tatmax":"1440","tatmin":"0","uploadedtime":"9/15/2020 12:19:44 PM"}]
         */

        private String Id;
        private String barcode;
        private String mobile;
        private String name;
        private String result;
        private String status;
        private String testcode;
        private List<UploadedBean> uploaded;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTestcode() {
            return testcode;
        }

        public void setTestcode(String testcode) {
            this.testcode = testcode;
        }

        public List<UploadedBean> getUploaded() {
            return uploaded;
        }

        public void setUploaded(List<UploadedBean> uploaded) {
            this.uploaded = uploaded;
        }

        public static class UploadedBean {
            /**
             * imageNAME : Not Uploaded
             * imageURL :
             * imgid : 12
             * tatmax : 240
             * tatmin : 0
             * uploadedtime :
             */

            private String imageNAME;
            private String imageURL;
            private String imgid;
            private String tatmax;
            private String tatmin;
            private String uploadedtime;

            public String getImageNAME() {
                return imageNAME;
            }

            public void setImageNAME(String imageNAME) {
                this.imageNAME = imageNAME;
            }

            public String getImageURL() {
                return imageURL;
            }

            public void setImageURL(String imageURL) {
                this.imageURL = imageURL;
            }

            public String getImgid() {
                return imgid;
            }

            public void setImgid(String imgid) {
                this.imgid = imgid;
            }

            public String getTatmax() {
                return tatmax;
            }

            public void setTatmax(String tatmax) {
                this.tatmax = tatmax;
            }

            public String getTatmin() {
                return tatmin;
            }

            public void setTatmin(String tatmin) {
                this.tatmin = tatmin;
            }

            public String getUploadedtime() {
                return uploadedtime;
            }

            public void setUploadedtime(String uploadedtime) {
                this.uploadedtime = uploadedtime;
            }
        }
    }

    public static class ResultBean {
        /**
         * imagename : Not Uploaded
         * imagesquence : 0
         * imgid : 12
         * tatmax : 240
         * tatmin : 0
         */

        private String imagename;
        private String imagesquence;
        private String imgid;
        private String tatmax;
        private String tatmin;

        public String getImagename() {
            return imagename;
        }

        public void setImagename(String imagename) {
            this.imagename = imagename;
        }

        public String getImagesquence() {
            return imagesquence;
        }

        public void setImagesquence(String imagesquence) {
            this.imagesquence = imagesquence;
        }

        public String getImgid() {
            return imgid;
        }

        public void setImgid(String imgid) {
            this.imgid = imgid;
        }

        public String getTatmax() {
            return tatmax;
        }

        public void setTatmax(String tatmax) {
            this.tatmax = tatmax;
        }

        public String getTatmin() {
            return tatmin;
        }

        public void setTatmin(String tatmin) {
            this.tatmin = tatmin;
        }
    }
}
