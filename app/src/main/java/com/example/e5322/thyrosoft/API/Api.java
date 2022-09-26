package com.example.e5322.thyrosoft.API;


public class Api {
    //TODO live
    public static String LIVEAPI = "https://b2capi.thyrocare.com/API/B2B/";
    public static String Cloud_base = "https://clisoapi.thyrocare.com/v1/";
    public static String URL_with_http = "https://b2capi.thyrocare.com/API/B2B/";
    public static String SGC = "https://b2capi.thyrocare.com/API/BDN/api/";
    public static String Static_API = "https://b2capi.thyrocare.com/API/B2B/";
    public static String THYROCARE = "https://b2capi.thyrocare.com/APIs/";
    public static String COVID = "https://b2capi.thyrocare.com/API/";
    public static String SUGARSO = "https://b2capi.thyrocare.com/API/SugarSo/api/";
    public static String SERVER_BASE_API_URL_PROD = "https://b2capi.thyrocare.com/API/BDN/api";
    public static String BASE_URL_TOCHECK = "https://b2capi.thyrocare.com/APIs/";
    public static String IMAGE_UPLOAD = "https://b2capi.thyrocare.com/API/";
    public static String SCANSOAPI = "http://scanso.nueclear.com/api/";
    public static String NUECLEAR = "http://api.nueclear.com/WebAPI/API";
    public static String Velso = "https://velso.thyrocare.cloud/api/";
    public static String Techso = "http://techsostng.thyrocare.cloud/";
    public static String Cloud_base_v2 = "http://clisoapi.thyrocare.com/v2/";

    //TODO staging
    /*public static String LIVEAPI = "https://b2capi.thyrocare.com/API_BETA/B2B/";
    public static String URL_with_http = "https://b2capi.thyrocare.com/API_BETA/B2B/";
    public static String Cloud_base = "http://stagingclisoapi.thyrocare.com/v1/";
    public static String SGC = "https://b2capi.thyrocare.com/API_BETA/BDN/api/";
    public static String Static_API = "https://b2capi.thyrocare.com/API_BETA/B2B/";
    public static String THYROCARE = "https://b2capi.thyrocare.com/API_BETA/";
    public static String COVID = "https://b2capi.thyrocare.com/API_BETA/";
    public static String SUGARSO = "https://b2capi.thyrocare.com/API_BETA/SugarSo/api/";
    public static String SERVER_BASE_API_URL_PROD = "https://b2capi.thyrocare.com/API_BETA/BDN/api";
    public static String BASE_URL_TOCHECK = "https://b2capi.thyrocare.com/API_BETA/";
    public static String IMAGE_UPLOAD = "https://b2capi.thyrocare.com/API_BETA/";
    public static String SCANSOAPI = "http://stagingscanso.nueclear.com/api/";
    public static String Velso = "https://velso.thyrocare.cloud/api/";
    public static String NUECLEAR = "http://api.nueclear.local/WebAPI/API";
    public static String Techso = " http://techsostng.thyrocare.cloud/";
    public static String Cloud_base_v2 = "http://stagingclisoapi.thyrocare.com/v2/";*/


    public static String imgURL = "http://www.charbi.com/assets/photo/Emp/";
    public static String traningvideo = "http://www.charbi.com//CDN//Applications//Android//XiaoYing_Video_1547111134141.mp4";
    public static final String APIKEYFORPAYMENTGATEWAY_PAYU = "yLZ4cKcEgPsnZn1s9b9FHhR9cUbO4AdM0z3fvmKQjiw=";

    public static String count = LIVEAPI + "WO.svc/";
    public static String BDN = LIVEAPI + "WO.svc/";

    //TODO SCANSO
    public static String MASTERInclusion = SCANSOAPI + "MasterData/GetInclusionMaster";
    public static String POSTADDINCLUSION = SCANSOAPI + "Billing/PostAddInclusion";
    public static String gettoken = SCANSOAPI + "Login/RegisteredUser";
    public static String LEAD_BOOKING = SCANSOAPI + "/LeadBooking/BookLead";
    public static String OTP_CREDIT_MIS = LIVEAPI + "COMMON.svc/";
    public static String FAQAPI = THYROCARE + "MASTER.svc/";
    public static String insertscandetail = LIVEAPI + "REPORT.svc/";
    public static String video_data = LIVEAPI + "COMMON.svc/";
    public static String video_url = LIVEAPI + "COMMON.svc/Cliso/Showvideo";
    public static String StockAvailability = LIVEAPI + "COMMON.svc/StockAvailability";
    public static String Materialupdate = LIVEAPI + "COMMON.svc/Materialupdate";
    public static String LOGIN = Cloud_base + "litelogin";
    public static String OTP = LIVEAPI + "common.svc/OTP";
    public static String deleteWOE = Cloud_base + "wodelete";
    public static String ForgotPass = LIVEAPI + "common.svc/PasswordMaster";
    public static String EmailPhoneOtp = LIVEAPI + "COMMON.svc/generateOTP";
    public static String getSource = LIVEAPI + "COMMON.svc/registerMasters";
    public static String Register_User = LIVEAPI + "COMMON.svc/postRegister";
    public static String multiple_windup = LIVEAPI + "WO.svc/postwindup";
    public static String sendGeoLocation = LIVEAPI + "COMMON.svc/litelog";
    public static String getData = LIVEAPI + "wo.svc/";
    public static String getAllTests = Cloud_base;
    public static String addrecheckWOE = LIVEAPI + "WO.svc/postaddrecheck";
    public static String scanBarcodeWithValidation = Cloud_base;
    public static String finalWorkOrderEntry = Cloud_base + "postworkorder";
    public static String finalWorkOrderEntryNew = Cloud_base + "postworkorder";
    public static String uploadDocument = SGC + "UploadDocument/PostRegister";
    public static String NoticeBoardData = LIVEAPI + "COMMON.svc/";
    public static String PushToken = LIVEAPI + "COMMON.svc/";
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
    public static String ValidateWorkOrderLeadId = Cloud_base;//validate OTP url
    public static String WORKoRDEReNTRYfIRSTpAGE = Cloud_base + "REPORT.svc/getresults/";//validate OTP url
    public static String getPartientDetailsList = LIVEAPI + "WO.svc/";//validate OTP url
    public static String windupApi = LIVEAPI + "WO.svc/";//lead id
    public static String addTestsUsingBarcode = Cloud_base;//lead id
    public static String checkBarcode = Cloud_base;//lead id
    public static String getBCTforSummary = LIVEAPI + "WO.svc/";//lead id
    public static String checkVersion = Cloud_base + "getVersion";//lead id
    public static String checkNumber = Cloud_base + "ValidateMob/";//lead id
    public static String static_pages_link = Static_API + "FAQ.svc/";
    public static String GenerateId = THYROCARE + "ORDER.svc/";
    public static String uploaddocumnet = SGC + "UploadDocument/PostRegister";
    public static String UPLOAD_TRF_RECEIPT = Cloud_base + "B2BDatasheet";
    public static String POST_GENERATE_OTP = "common.svc/otp";
    public static String VERIFY_OTP_BS = "BloodSugarEntry/VerifyMobileRes/";
    public static String MULTIPART_UPLOAD = "BloodSugarEntry/EntryCliso";
    public static String ENTERED_MIS = "BloodSugarEntry/GetBloodSugarEntryMISCliso";
    public static String POST_COMM_MULTIPART = SGC + "Common/PostCommunication";


    //Tejasap
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
    public static String BROADCAST = LIVEAPI + "COMMON.svc/";
}