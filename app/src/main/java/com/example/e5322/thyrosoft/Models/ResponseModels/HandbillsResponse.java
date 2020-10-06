package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.List;

public class HandbillsResponse {

    /**
     * response : Success
     * resId : RES0000
     * HandbillsByCleint : [{"Imageurl":"https://WWW.THYROCARE.COM\\API\\BDN\\IMAGES\\sumitsahani_18022020_637176457689480976.jpg","Name":"sumitsahani","Address":"chembur vashi naka mahada colony mumbai 40074","Pincode":"400074","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"2"},{"Imageurl":"https://WWW.THYROCARE.COM\\API\\BDN\\IMAGES\\sumitsahani_18022020_637176389712939695.jpg","Name":"sumitsahani","Address":"chembur vashi naka mahada colony mumbai 40074","Pincode":"400074","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"5"},{"Imageurl":"https://WWW.THYROCARE.COM\\API\\BDN\\IMAGES\\sumitsahani_18022020_637176457234712200.jpg","Name":"sumitsahani","Address":"chembur vashi naka mahada colony mumbai 40074","Pincode":"400074","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"7"},{"Imageurl":"https://WWW.THYROCARE.COM\\API_BETA\\BDN\\IMAGES\\7738943013_19022020_637177246092827082.png","Name":"sumitsahani","Address":"chembur vashi naka mahada colony mumbai 40074","Pincode":"400074","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"8"},{"Imageurl":"https://WWW.THYROCARE.COM\\API_BETA\\BDN\\IMAGES\\7738943013_22022020_637179910985311737.png","Name":"sumitsahani","Address":"chembur vashi naka mahada colony mumbai 40074","Pincode":"400074","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"9"},{"Imageurl":"https://WWW.THYROCARE.COM\\API_BETA\\BDN\\IMAGES\\7738943013_22022020_637179911235116715.png","Name":"sumitsahani","Address":"","Pincode":"","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"10"},{"Imageurl":"https://WWW.THYROCARE.COM\\API_BETA\\BDN\\IMAGES\\7738943013_22022020_637179919188842008.png","Name":"sumitsahani","Address":"chembur vashi naka mahada colony mumbai 40074","Pincode":"400074","Mobile":"9004844180","Email":"sumit.sahani@thyrocare.com","Status":null,"Imgid":"11"},{"Imageurl":"https://WWW.THYROCARE.COM\\API_BETA\\BDN\\IMAGES\\7738943013_24022020_637181407088841931.png","Name":"KALPESH","Address":"nvxngxhmchmjcxbxhxhxhxhcncncbcbcbc","Pincode":"868686","Mobile":"9645464645","Email":"kalpesh.borane@thyrocare.com","Status":null,"Imgid":"12"}]
     */

    private String response;
    private String resId;
    private List<HandbillsByCleintBean> HandbillsByCleint;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public List<HandbillsByCleintBean> getHandbillsByCleint() {
        return HandbillsByCleint;
    }

    public void setHandbillsByCleint(List<HandbillsByCleintBean> HandbillsByCleint) {
        this.HandbillsByCleint = HandbillsByCleint;
    }

    public static class HandbillsByCleintBean {
        /**
         * Imageurl : https://WWW.THYROCARE.COM\API\BDN\IMAGES\sumitsahani_18022020_637176457689480976.jpg
         * Name : sumitsahani
         * Address : chembur vashi naka mahada colony mumbai 40074
         * Pincode : 400074
         * Mobile : 9004844180
         * Email : sumit.sahani@thyrocare.com
         * Status : null
         * Imgid : 2
         */

        private String Imageurl;
        private String Name;
        private String Address;
        private String Pincode;
        private String Mobile;
        private String Email;
        private Object Status;
        private String Imgid;

        public String getImageurl() {
            return Imageurl;
        }

        public void setImageurl(String Imageurl) {
            this.Imageurl = Imageurl;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public Object getStatus() {
            return Status;
        }

        public void setStatus(Object Status) {
            this.Status = Status;
        }

        public String getImgid() {
            return Imgid;
        }

        public void setImgid(String Imgid) {
            this.Imgid = Imgid;
        }
    }
}
