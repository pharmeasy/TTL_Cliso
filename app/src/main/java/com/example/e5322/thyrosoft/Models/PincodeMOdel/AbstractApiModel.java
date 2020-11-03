package com.example.e5322.thyrosoft.Models.PincodeMOdel;


import com.example.e5322.thyrosoft.Interface.AppConstants;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.util.HashMap;
import java.util.List;

import static com.example.e5322.thyrosoft.API.Api.SERVER_BASE_API_URL_PROD;

public class AbstractApiModel implements AppConstants {

    public static final String FILE_UPLOAD = "file_upload";


    /* Requset urlt_layout of api call */
    private String requestUrl;

    /* Json string to post/put method */
    private String postData = "";

    /* Headers to be added as list */
    private HashMap<String, String> headerMap;

    public int methodType;

    private UrlEncodedFormEntity entity;

    //  public static String appEnvironment = "DEVELOPMENT";
    //public static String appEnvironment = "DEMO";
    public static String appEnvironment = "PRODUCTION";


    // New DB changes Amazon Dev
    public static String SERVER_BASE_API_URL_DEV = "http://apibeta.thyrocare.com";


    // New DB changes Demo Amazon
    public static String SERVER_BASE_API_URL_DEMO = "http://apibeta.thyrocare.com";


    //public static String SERVER_BASE_API_URL_PROD = "https://www.dxscloud.com/techsoapi"; //TODO LIVE
    //public static final String API_VERSION = "https://www.thyrocare.com/APIs";



    public static String SERVER_BASE_API_URL = appEnvironment.equals("DEVELOPMENT") ? SERVER_BASE_API_URL_DEV : appEnvironment.equals("DEMO") ? SERVER_BASE_API_URL_DEMO : appEnvironment.equals("PRODUCTION") ? SERVER_BASE_API_URL_PROD : SERVER_BASE_API_URL_DEV;


    /**
     * RESPECTIVE URLS
     */

    public static String DEVICE_TYPE = "android";

    public static String APP_NAME = "BD";

    // Version Specific Base URL 51 apis used


    public String DOCUMENTSPINNER = "/UploadDocument/GetuploadDoc";
    public String DEFAULTUPLOAD = "/UploadDocument/GetDefaultDataUploadDocument";

    public String GETNED = "/UploadDocument/GetNED/";
    public String GETSGC= "/UploadDocument/GetSGC";
    public String GETPGC= "/UploadDocument/GetPGC";




    public String VERSION_API_URL = "/api";


    public static final String X_API_KEY = "x-api-key";

    public static final String AUTHORIZATION = "Authorization";

    public static final String ACCEPT = "Accept";

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_X_WWW_FROM_URLENCODED = "application/x-www-form-urlencoded";

    /* Json string to post/put method */
    private String postJsonString;
    /* Headers to be added as list */
    private List<HeaderData> header;


    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String rquestUrl) {
        this.requestUrl = rquestUrl;
    }

    public String getPostJsonString() {
        return postJsonString;
    }

    public void setPostJsonString(String postJsonString) {
        this.postJsonString = postJsonString;
    }

    public List<HeaderData> getHeader() {
        return header;
    }

    public void setHeader(List<HeaderData> header) {
        this.header = header;
    }

    public void createJson() {

    }

    public void setParam(String key, String value) {

        if (this.requestUrl == null) {
            return;
        } else if (!this.requestUrl.contains("?")) {
            this.requestUrl = this.requestUrl + "?" + key + "=" + value;
        } else if (key.equals("action")) {
            this.requestUrl = this.requestUrl + key + "=" + value;
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + value;
        }
    }

    public void setParam(String key, float value) {

        if (this.requestUrl == null) {
            return;
        } else if (!this.requestUrl.contains("?")) {
            this.requestUrl = this.requestUrl + "?" + key + "=" + (value);
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + (value);
        }
    }

    public void setParam(String key, int value) {

        if (this.requestUrl == null) {
            return;
        } else if (!this.requestUrl.contains("?")) {
            this.requestUrl = this.requestUrl + "?" + key + "=" + (value);
        } else {
            this.requestUrl = this.requestUrl + "&" + key + "=" + (value);
        }
    }

    public AbstractApiModel() {
        super();
        headerMap = new HashMap<String, String>();
    }

    public String getRequestUrlt_layout() {
        return requestUrl;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public void putHeader(String key, String value) {

        headerMap.put(key, value);
    }

    public HashMap<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(HashMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public UrlEncodedFormEntity getEntity() {
        return entity;
    }

    public void setEntity(UrlEncodedFormEntity entity) {
        this.entity = entity;
    }
}