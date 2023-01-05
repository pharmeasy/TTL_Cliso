package com.example.e5322.thyrosoft.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageResponseModel {

    private String audit_log_async_id;
    private long audit_log_id;
    private Object experiment_id;
    private Object experiment_split_log_id;
    private ResultDTO result;
    private Object split_identifier_value;
    private int version_id;

    public String getAudit_log_async_id() {
        return audit_log_async_id;
    }

    public void setAudit_log_async_id(String audit_log_async_id) {
        this.audit_log_async_id = audit_log_async_id;
    }

    public long getAudit_log_id() {
        return audit_log_id;
    }

    public void setAudit_log_id(long audit_log_id) {
        this.audit_log_id = audit_log_id;
    }

    public Object getExperiment_id() {
        return experiment_id;
    }

    public void setExperiment_id(Object experiment_id) {
        this.experiment_id = experiment_id;
    }

    public Object getExperiment_split_log_id() {
        return experiment_split_log_id;
    }

    public void setExperiment_split_log_id(Object experiment_split_log_id) {
        this.experiment_split_log_id = experiment_split_log_id;
    }

    public ResultDTO getResult() {
        return result;
    }

    public void setResult(ResultDTO result) {
        this.result = result;
    }

    public Object getSplit_identifier_value() {
        return split_identifier_value;
    }

    public void setSplit_identifier_value(Object split_identifier_value) {
        this.split_identifier_value = split_identifier_value;
    }

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public static class ResultDTO {
        private List<WidgetDTO> widget;

        public List<WidgetDTO> getWidget() {
            return widget;
        }

        public void setWidget(List<WidgetDTO> widget) {
            this.widget = widget;
        }

        public static class WidgetDTO {
            private int b2b_price;
            private int b2c_price;
            private String button_text;
            private String button_url;
            private String image_url;
            private int margin;
            private String name;
            private String tip_description;
            private String tip_heading;
            @SerializedName("TAT")
            private String tAT;

            public int getB2b_price() {
                return b2b_price;
            }

            public void setB2b_price(int b2b_price) {
                this.b2b_price = b2b_price;
            }

            public int getB2c_price() {
                return b2c_price;
            }

            public void setB2c_price(int b2c_price) {
                this.b2c_price = b2c_price;
            }

            public String getButton_text() {
                return button_text;
            }

            public void setButton_text(String button_text) {
                this.button_text = button_text;
            }

            public String getButton_url() {
                return button_url;
            }

            public void setButton_url(String button_url) {
                this.button_url = button_url;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public int getMargin() {
                return margin;
            }

            public void setMargin(int margin) {
                this.margin = margin;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTip_description() {
                return tip_description;
            }

            public void setTip_description(String tip_description) {
                this.tip_description = tip_description;
            }

            public String getTip_heading() {
                return tip_heading;
            }

            public void setTip_heading(String tip_heading) {
                this.tip_heading = tip_heading;
            }

            public String getTAT() {
                return tAT;
            }

            public void setTAT(String tAT) {
                this.tAT = tAT;
            }
        }
    }
}
