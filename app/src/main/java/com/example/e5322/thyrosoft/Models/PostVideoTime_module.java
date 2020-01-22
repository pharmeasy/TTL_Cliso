package com.example.e5322.thyrosoft.Models;

public class PostVideoTime_module {
    /**
     * ClientId : TAM03
     * VideoId : 11
     * min_Duration : 11
     * Sec_Duration : 30
     */

    private String ClientId;
    private String VideoId;
    private String min_Duration;
    private String Sec_Duration;

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String ClientId) {
        this.ClientId = ClientId;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String VideoId) {
        this.VideoId = VideoId;
    }

    public String getMin_Duration() {
        return min_Duration;
    }

    public void setMin_Duration(String min_Duration) {
        this.min_Duration = min_Duration;
    }

    public String getSec_Duration() {
        return Sec_Duration;
    }

    public void setSec_Duration(String Sec_Duration) {
        this.Sec_Duration = Sec_Duration;
    }





  /*  public static class VideoBuilder {
        private String ClientId;
        private String VideoId;
        private String min_Duration;
        private String Sec_Duration;

        public VideoBuilder() {
        }

        public VideoBuilder setClientId(String clientId) {
            ClientId = clientId;
            return this;
        }

        public VideoBuilder setVideoId(String videoId) {
            VideoId = videoId;
            return this;
        }

        public VideoBuilder setMin_Duration(String min_Duration) {
            this.min_Duration = min_Duration;
            return this;
        }

        public VideoBuilder setSec_Duration(String sec_Duration) {
            Sec_Duration = sec_Duration;
            return this;
        }

        public PostVideoTime_module build() {
            return new PostVideoTime_module();
        }
    }*/
}
