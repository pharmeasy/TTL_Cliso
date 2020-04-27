package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class GetVideoResponse_Model {


    /**
     * Output : [{"Active":"True","App":"CLISO","Description":"This video explains about Food Intolerance Profile","Imageurl":"","Path":"http://crm.thyrocare.com/Video/ThyrocareFoodIntoleranceGuide.mp4","Thumbtime":"45","Title":"Thyrocare Food Intolerance Guide","id":"44"}]
     * resId : RSS0000
     * response : SUCCESS
     */

    private String resId;
    private String response;
    private List<OutputBean> Output;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<OutputBean> getOutput() {
        return Output;
    }

    public void setOutput(List<OutputBean> Output) {
        this.Output = Output;
    }

    public static class OutputBean {
        /**
         * Active : True
         * App : CLISO
         * Description : This video explains about Food Intolerance Profile
         * Imageurl :
         * Path : http://crm.thyrocare.com/Video/ThyrocareFoodIntoleranceGuide.mp4
         * Thumbtime : 45
         * Title : Thyrocare Food Intolerance Guide
         * id : 44
         */

        private String Active;
        private String App;
        private String Description;
        private String Imageurl;
        private String Path;
        private String Thumbtime;
        private String Title;
        private String id;

        public String getActive() {
            return Active;
        }

        public void setActive(String Active) {
            this.Active = Active;
        }

        public String getApp() {
            return App;
        }

        public void setApp(String App) {
            this.App = App;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getImageurl() {
            return Imageurl;
        }

        public void setImageurl(String Imageurl) {
            this.Imageurl = Imageurl;
        }

        public String getPath() {
            return Path;
        }

        public void setPath(String Path) {
            this.Path = Path;
        }

        public String getThumbtime() {
            return Thumbtime;
        }

        public void setThumbtime(String Thumbtime) {
            this.Thumbtime = Thumbtime;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
