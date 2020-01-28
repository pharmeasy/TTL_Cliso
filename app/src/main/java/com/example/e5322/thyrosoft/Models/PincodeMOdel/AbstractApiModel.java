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

    public String TOP10_DEFAULT_SGC = "/ClientEntry/GetDefaultSGC";
    public String SGC_DATA = "/ClientEntry/GetPincodeSGC";
    public String TSP_DATA = "/ClientEntry/GetPincodeWiseTSP";
    public String SPPINER_SGC = "/ClientEntry/GetTspOlcForSGC";
    public String POSTREGISTER = "/ClientEntry/PostRegister";
    public String OLCREGISTER = "/ClientEntry/SendJotForm";
    public String SGCDETAILINFO = "/ClientEntry/GetSGCDetail";
    public String UPDATESGC = "/ClientEntry/UpdateSGC";
    public String DEFAULTTSPTAGSC = "/TspTagSc/GetDefaultData";
    public String TSPDATA = "/TspTagSc/GetTspFilterData";
    public String SOURCEMASTER = "/TspTagSc/GetSourceMasterData";
    public String CLIENTDATA = "/TspTagSc/GetClientData";
    public String DEFAULTCLIENTVERFICATION = "/ClientVerification/GetDefaultVerificationData";
    public String DEFAULTMKGSGCVERFICATION = "/ClientVerification/GetStateVerificationData";
    public String DEFAULTPGC = "/ClientVerification/GetDefaultPGCVerificationData";
    public String DEFAULTMKGPGCVERFICATION = "/ClientVerification/GetPGCVerificationData";
    public String GETSTATES = "/ClientVerification/GetStates";
    public String STATEWISE = "/ClientVerification/GetStateVerificationData";
    public String ALLTSP = "/ClientVerification/GetStateVerificationData";
    public String SUBCLUSTER = "/ClientVerification/GetClusters";
    public String FILTER = "/ClientVerification/GetFilterResult";
    public String GETVERFIEDDATA = "/ClientVerification/GetVerifiedData";
    public String DEFAULTMERGE = "/ClientMerge/GetSearchedData";
    public String MISUPLOAD = "/UploadDocument/GetDefaultDataUploadDocumentMIS";
    public String TSPSPINNER = "/UploadDocument/GetTsp";
    public String DOCUMENTSPINNER = "/UploadDocument/GetuploadDoc";
    public String DEFAULTUPLOAD = "/UploadDocument/GetDefaultDataUploadDocument";
    public String MIS2 = "/UploadDocument/GetDefaultMIS2DOc";
    public String VERIFIED = "/ClientVerification/GetVerifiedData";
    public String PGCVERIFIED = "/ClientVerification/GetPGCVerifiedData";
    public String GETSMS = "/SMS/GetSMS";
    public String POSTSMS = "/sms/PostSMSDate";
    public String SENDSMS = "/SMS/SendSMS";
    public String ADDTOCONTACTS = "/SMS/AddContact";
    public String GETCONTATCS = "/sms/getcontacts";
    public String AUTOFILL_VERIFIED = "/ClientVerification/GetClientDetails";
    public String MERGE_POST = "/ClientMerge/MergeClients";
    public String VERIFICATIONPOST = "/ClientVerification/UpdateSgcVerification";
    public String AUTOFILL_VERIFIED_PGC = "/ClientVerification/GetPGCClientDetails";
    public String VERIFIED_PGC_POST = "/ClientVerification/UpdatePgcVerification";
    public String POSTREGISTERUPLOADDOCUMENT = "/UploadDocument/PostRegister";
    public String GETNED = "/UploadDocument/GetNED/";
    public String GETSGC= "/UploadDocument/GetSGC";
    public String GETPGC= "/UploadDocument/GetPGC";
    public String GETSTAFF= "/UploadDocument/GetStaff";
    public String POSTREGISTERR="/UploadDocument/PostRegisterStaff";
    public String VIEWIMAGE="/UploadDocument/GetImage";
    public String GETTSP="/UploadDocument/GetMISTSP";



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