package com.example.e5322.thyrosoft.API;

/**
 * Created by E5322 on 24-03-2018.
 */

public class Api {

    //TODO live
  /*  public static String LIVEAPI = "https://www.thyrocare.com/API/B2B/";
    public static String URL_with_http = "https://www.thyrocare.com/API/B2B/";
    public static String SGC = "https://www.thyrocare.com/API/BDN/api/";
    public static String Static_API = "https://www.thyrocare.com/API/B2B/";
    public static String GenerateTid = "https://www.thyrocare.com/APIs/";
    public static String SERVER_BASE_API_URL_PROD = "https://www.thyrocare.com/API/BDN/api";
    public static String BASE_URL_TOCHECK = "https://www.thyrocare.com/APIs/";
    public static final String APIKEYFORPAYMENTGATEWAY_PAYU = "sNhdlQjqvoAHd0wM2XBmovfagV08jstDCTblJACEi7tTTPegWYYwDw==";
    public static String IMAGE_UPLOAD = "http://www.thyrocare.com/API/";*/


    //TODO staging
    public static String LIVEAPI = "https://www.thyrocare.com/API_BETA/B2B/";
    public static String URL_with_http = "http://www.thyrocare.com/API_BETA/B2B/";
    public static String SGC = "http://www.thyrocare.local/API/BDN/api/";
    public static String Static_API = "https://www.thyrocare.com/API_BETA/B2B/";
    public static String GenerateTid = "https://www.thyrocare.com/API_BETA/";
    public static String SERVER_BASE_API_URL_PROD = "https://www.thyrocare.local/API/BDN/api";
    public static String BASE_URL_TOCHECK = "https://www.thyrocare.com/API_BETA/";
    public static final String APIKEYFORPAYMENTGATEWAY_PAYU = "CYs3zmAdtXVFUa4tSwpzD407UaAGDkPH2eJV8L3Ch8s=";
    public static String IMAGE_UPLOAD = "http://www.thyrocare.local/API/";

    public static String traningvideo = "http://www.charbi.com//CDN//Applications//Android//XiaoYing_Video_1547111134141.mp4";
    public static String imgURL = "http://www.charbi.com/assets/photo/Emp/";
    public static String count = LIVEAPI + "WO.svc/";
    public static String video_url = LIVEAPI+"COMMON.svc/Showvideo";
    public static String LOGIN = LIVEAPI + "COMMON.svc/litelogin";
    public static String OTP = LIVEAPI + "common.svc/OTP";
    public static String deleteWOE = LIVEAPI + "WO.svc/wodelete";
    public static String ForgotPass = LIVEAPI + "common.svc/PasswordMaster";
    public static String EmailPhoneOtp = LIVEAPI + "COMMON.svc/generateOTP";
    public static String getSource = LIVEAPI + "COMMON.svc/registerMasters";
    public static String Register_User = LIVEAPI + "COMMON.svc/postRegister";
    public static String multiple_windup = LIVEAPI + "WO.svc/postwindup";
    public static String sendGeoLocation = LIVEAPI + "COMMON.svc/litelog";
    public static String getData = LIVEAPI + "wo.svc/";
    public static String getAllTests = LIVEAPI + "MASTER.svc/";
    public static String addrecheckWOE = LIVEAPI + "WO.svc/postaddrecheck";
    public static String scanBarcodeWithValidation = LIVEAPI + "WO.svc/";
    public static String finalWorkOrderEntry = LIVEAPI + "WO.svc/postworkorder";
    public static String finalWorkOrderEntryNew = LIVEAPI + "WO.svc/postworkorder";
    public static String uploadDocument = SGC + "UploadDocument/PostRegister";
    public static String NoticeBoardData = LIVEAPI + "COMMON.svc/";
    public static String OTPgenegrate = LIVEAPI + "MASTER.svc/";
    public static String feedback = LIVEAPI + "master.svc/Feedback";//feedback url
    public static String postResponseToCommunication = LIVEAPI + "COMMON.svc/postComm";//feedback url
    public static String SGCRegistration = SGC + "ClientEntry/PostRegister";//BDN/api/ClientEntry/PostRegister
    public static String checkValidEmail = SGC + "clientEntry/ValidateME";//BDN/api/ClientEntry/PostRegister
    public static String consignmentEntry = URL_with_http + "FAQ.svc/ConsignmentEntry";//BDN/api/ClientEntry/PostRegister
    public static String barcode_Check = URL_with_http + "FAQ.svc/";//BDN/api/ClientEntry/PostRegister
    public static String acknowledgeNoticeBoard = LIVEAPI + "COMMON.svc/acknowledgeNotice";//ack noticeboard url
    public static String SOURCEils = LIVEAPI + "MASTER.svc/";//Whatsapp and call url
    public static String ValidateOTP = LIVEAPI + "common.svc/validateOTP";//validate OTP url
    public static String sendEstimation = LIVEAPI + "COMMON.svc/sendest";
    public static String RateCal = LIVEAPI + "COMMON.svc/ratecal";
    public static String testDetails = LIVEAPI + "MASTER.svc/TestDetails";
    public static String ValidateWorkOrderLeadId = LIVEAPI + "ORDER.svc/";//validate OTP url
    public static String WORKoRDEReNTRYfIRSTpAGE = LIVEAPI + "REPORT.svc/getresults/";//validate OTP url
    public static String getPartientDetailsList = LIVEAPI + "WO.svc/";//validate OTP url
    public static String windupApi = LIVEAPI + "WO.svc/";//lead id
    public static String addTestsUsingBarcode = LIVEAPI + "WO.svc/";//lead id
    public static String checkBarcode = LIVEAPI + "WO.svc/";//lead id
    public static String getBCTforSummary = LIVEAPI + "WO.svc/";//lead id
    public static String checkVersion = LIVEAPI + "COMMON.svc/getliteversion";//lead id
    public static String ThyrosoftLiteVersion = LIVEAPI + "COMMON.svc/getliteversion";//lead id
    public static String checkNumber = LIVEAPI + "WO.svc/ValidateMob/";//lead id
    public static String static_pages_link = Static_API + "FAQ.svc/";
    public static String GenerateId = GenerateTid + "ORDER.svc/";
    public static String upload_documnet = SGC + "UploadDocument/PostRegister";
    public static String UPLOAD_TRF_RECEIPT = IMAGE_UPLOAD + "PICKSO/api/Datasheetupload/B2BDatasheet";

    //NEHA
    public static String billingDetLIVE = LIVEAPI + "MASTER.svc/billingDetail";
    public static String billingSUMLIVE = LIVEAPI + "MASTER.svc/billingSummary";
    public static String ResultLIVE = LIVEAPI + "REPORT.svc/getresults";
    public static String Receipt_mail = LIVEAPI + "REPORT.svc/receiptmail";
    public static String downloadreceipt = LIVEAPI + "REPORT.svc/receiptdownload";

    public static String postmailLive = LIVEAPI + "REPORT.svc/postreportmail";
    public static String trackbarcode = LIVEAPI + "REPORT.svc/trackBarcode";//track barcode
    public static String commGetLive = LIVEAPI + "Common.svc/getComm";
    public static String commPost = LIVEAPI + "Common.svc/postComm";
    public static String LedgerSummLive = LIVEAPI + "MASTER.svc/ledgerSummary";

    public static String LedgerDetLive = LIVEAPI + "MASTER.svc/ledgerDetail";
    public static String PostPref = LIVEAPI + "MASTER.svc/postpreference";
    public static String postcancellead = LIVEAPI + "REPORT.svc/postcancellead";
    public static String Result = LIVEAPI + "REPORT.svc/getresults";
    public static String chn_update = LIVEAPI + "REPORT.svc/postchnupdate";
    public static String consignmentperday = Static_API + "FAQ.svc/";
}