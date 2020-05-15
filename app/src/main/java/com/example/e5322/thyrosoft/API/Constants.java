package com.example.e5322.thyrosoft.API;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static com.example.e5322.thyrosoft.API.Api.BASE_URL_TOCHECK;

/**
 * Created by e5426@thyrocare.com on 10/1/18.
 */

public class Constants {
    public static String HEADER_USER_AGENT = "User-Agent";

    public static String PRODUCT_POP = "POP";
    public static String PRODUCT_PROFILE = "PROFILE";
    public static String PRODUCT_TEST = "TEST";
    public static String SMT_TEST = "SMT";
    public static int DAYS_CNT = 1;
    public static String APPID = "6";
    public static String TOPIC = "CLISO_ALLDEVICE";
    public static String APPNAME = "CLISO";

    public static String setVideoUrl = "VideoURL";
    public static String setFullScreen;
    public static int timeInterval = 0;
    public static String videoName;

    //shared preference Name

    public static String PROFILEPREF = "profile";

    public static String APP_ID = "1";

    /*TODO * Screen IDs*/
    public static String IsFromNotification = "IsFromNotification";
    public static int SCR_1 = 1;
    public static int SCR_2 = 2;
    public static int SCR_3 = 3;
    public static int SCR_4 = 4;
    public static int SCR_5 = 5;

    public static String caps_invalidApikey = "INVALID API KEY";
    public static String small_invalidApikey = "Invalid API Key";

    public static String APK_NAME = "ThyrosoftLite.apk";
    public static String availcoupon = "Avail Coupon";
    public static String APKTYPE = "application/vnd.android.package-archive";

    //TODO PET CT Constant
    public static final String PNAME = "pname";
    public static final String FNAME = "fname";
    public static final String MKNAME = "mname";
    public static final String LNAME = "lname";
    public static final String PGENDER = "pgender";
    public static final String PDIEB = "diabetics";
    public static final String PDOB = "dob";
    public static final String appointdate = "appointdate";
    public static final String PMOB = "pmobileno";
    public static final String PEMAIL = "p_emailid";
    public static final String PCITY = "city";
    public static final String PREMARK = "remark";
    public static final String SERVICETYPE = "servicetype";
    public static final String AVAILBAL = "availbal";
    public static final String PAID_BAL = "paidbal";
    public static final String HEADER = "header";
    public static final String CALAGE = "calage";
    public static final String CENTERID = "centerid";
    public static final String SERVICEID = "serviceid";
    public static final String AMT_Coll = "amtcoll";
    public static final String SLOT = "slot";

    public static final String STAFF = "STAFF";
    public static final String ADMIN = "ADMIN";
    public static final String NHF = "NHF";

    public static final String DATEFORMATE = "dd-MM-yyyy";
    public static final String YEARFORMATE = "yyyy-MM-dd";
    public static final String NHF_Whatsapp = "8422849939";

    public static final String PREF_PRODUCTS_CACHING = "syncpref";

    public static final String SH_FIRE = "sharefirebase";
    public static final String F_TOKEN = "token";
    public static final String servertoken = "IsTokensendToServer";


    //NHF Login

    public static final String NHF_EMAIL = "COMM_NHF";

    public static String tab_flag = "0";

    //login details//
    public static String Status = "Status";
    public static String Ledger_Balance = "Ledger_Balance";
    public static String LoginName = "LoginName";
    public static String LoginType = "LoginType";
    public static String Password = "Password";
    public static String TSP = "TSP";
    public static String Sort = "sort";
    public static String TABS = "Tabs";
    public static String TABS_Name = "TabName";
    public static String BRANDING = "BRANDING";
    public static String OPERATIONAL = "OPERATIONAL";
    public static String PROMOTIONAL = "PROMOTIONAL";
    public static String NUECLEAR = "NUECLEAR";
    public static String OFFICE_STATIONARY = "OFFICE STATIONARY";
    public static String MOST_POPULAR = "MOST POPULAR";
    public static String Material_Category = "Material_Category";
    public static String Materials = "Materials";
    public static String Materials_Details = "Material_Details";
    public static String RECENTLY_ADDED = "RECENTLY ADDED";
    public static String Related_Materials = "Related_Materials";
    public static String AMOUNT = "amount";
    public static String MCODE = "MCODE";
    public static String MNAME = "MNAME";
    public static String STOCK = "STOCK";
    public static String TAX = "TAX";
    public static String Count = "Count";
    public static String UNITRATE = "UNITRATE";
    public static String UNITSIZE = "UNITSIZE";
    public static String CATEGORY = "CATEGORY";
    public static String DESCRIPTION = "DESCRIPTION";
    public static String WEIGHT = "WEIGHT";
    public static String ORWEIGHT = "ORWEIGHT";
    public static String QUANTITY = "QUANTITY";
    public static String Total_Materials = "Total_Materials";
    public static String Order_Amount = "Order_Amount";
    public static String Order_Date = "Order_Date";
    public static String Order_Id = "Order_Id";
    public static String Order_Status = "Order_Status";
    public static String DESPATCHED = "DESPATCHED";
    public static String CANCELLED = "CANCELLED";
    public static String DELIVERED = "DELIVERED";
    public static String PACKED = "PACKED";
    public static String ORDER_PLACED = "ORDER PLACED";
    public static String CONFIRMED = "CONFIRMED";
    public static String GMGS_thyroshop = "ORDER PENDING GMGS ONLINE ORDER PLACED FROM THYROSHOP APP";
    public static String PENDING_THYROSHOP = "ORDER PENDING ORDER PLACED FROM THYROSHOP APP";
    public static String GMGS = "ORDER PENDING GMGS ONLINE ORDER";
    public static String new_GMGS = "GMGS ONLINE ORDER THROUGH THYROSHOP";
    public static String Order_Details = "Order_Details";
    public static String Orders = "Orders";
    public static String SortType = "SortType";
    public static String Confirmed = "Confirmed";
    public static String Delivered = "Delivered";
    public static String Despatched = "Despatched";
    public static String Packed = "Packed";
    public static String Placed = "Placed";
    public static String LedgerPay = "LEDGER";
    public static String Cardpay = "ONLINE";
    public static String TID = "TID";
    public static String ADDRESS = "Address";
    public static String DOJ = "DOJ";
    public static String MobileNumber = "MobileNumber";
    public static String EmailId = "EmailId";  //"EmailId" -> "tam03@thyrocare.com"
    public static String Version = "Version";
    public static String URL = "URL";
    public static String Emoji = "Emoji";
    public static String emoji_happy = "HAPPY";
    public static String emoji_exicited = "Excited";
    public static String emoji_sad = "NUETRAL";
    public static String emoji_cry = "SAD";
    public static String Comments_feedback = "Remark";
    public static String New_Address = "Address";
    public static String Pay_Amount = "PayAmount";

    public static String TCC = "TCC";
    public static String OLC = "OLC";
    public static String DISTRIBUTOR = "DISTRIBUTOR";


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String UserDetails = "Userdetails";
    public static final String UserName = "username";
    public static final String AccessType = "ACCESS_TYPE";
    public static final String API_KEY = "API_KEY";
    public static final String password = "password";
    public static final String PortalType = "portalType";
    public static final String B2BAPP = "B2BAPP";
    public static final String UserCode = "USER_CODE";
    public static final String UserCode_billing = "userCode";
    public static final String fromDate = "fromDate";
    public static final String toDate = "toDate";
    public static final String API_KEY_billing = "apiKey";
    public static final String billingList = "billingList";
    public static final String billedAmount = "billedAmount";
    public static final String billingDate = "billingDate";
    public static final String workLoad = "workLoad";
    public static final String billingDetails = "billingDetails";
    public static final String collectedAmount = "collectedAmount";
    public static final String patient = "patient";
    public static final String tests = "tests";
    public static final String woetype = "woetype";
    public static final String refId = "refId";
    public static final String Result_Type = "result_type";
    public static final String Key = "key";
    public static final String Value = "value";


    public static final String patients = "patients";
    public static final String Downloaded = "Downloaded";
    public static final String Ref_By = "Ref_By";
    public static final String Tests = "Tests";
    public static final String barcode = "barcode";
    public static final String cancel_tests_with_remark = "cancel_tests_with_remark";
    public static final String chn_pending = "chn_pending";
    public static final String chn_test = "chn_test";
    public static final String confirm_status = "confirm_status";
    public static final String date = "date";
    public static final String email = "email";
    public static final String isOrder = "isOrder";
    public static final String labcode = "labcode";
    public static final String leadId = "leadId";
    public static final String name = "name";
    public static final String patient_id = "patient_id";
    public static final String pdflink = "pdflink";
    public static final String scp = "scp";
    public static final String sct = "sct";
    public static final String su_code2 = "su_code2";
    public static final String wo_sl_no = "wo_sl_no";


    //trackbarcode billStatus;
    public static final String billed = "billed";
    public static final String bvt = "bvt";
    public static final String collected = "collected";
    public static final String etr = "etr";
    public static final String kycType = "kycType";
    public static final String orderId = "orderId";
    public static final String orderNo = "orderNo";
    public static final String pendingCancelledTests = "pendingCancelledTests";
    public static final String portalType = "portalType";
    public static final String refBy = "refBy";
    public static final String reportAddress = "reportAddress";
    public static final String resId = "resId";
    public static final String response = "response";
    public static final String rrt = "rrt";
    public static final String sampleDetails = "sampleDetails";
    public static final String status = "status";
    public static final String statusFlag = "statusFlag";
    public static final String woeEdit = "woeEdit";
    public static final String woeStage = "woeStage";
    public static final String woeTime = "woeTime";
    public static final String woiLocation = "woiLocation";
    public static final String billStatus = "billStatus";


    //communication
    public static final String communicationMaster = "communicationMaster";
    public static final String inboxes = "inboxes";
    public static final String sents = "sents";

    public static final String TAT = "TAT";
    public static final String commBy = "commBy";
    public static final String commDate = "commDate";
    public static final String commId = "commId";
    public static final String department = "department";
    public static final String question = "question";
    public static final String resDate = "resDate";

    public static final String displayName = "displayName";
    public static final String forwardTo = "forwardTo";
    public static final String id = "id";
    public static final String isBarcode = "isBarcode";

    public static final String communication = "communication";
    public static final String responseBy = "responseBy";

    //Ledger

    public static final String month = "month";
    public static final String year = "year";

    public static final String billAmount = "billAmount";
    public static final String cashCheque = "cashCheque";
    public static final String closingBalance = "closingBalance";
    public static final String creditNotes = "creditNotes";
    public static final String debitNotes = "debitNotes";
    public static final String openingBalance = "openingBalance";

    public static final String unbilledMaterial = "Unbilledmaterial";
    public static final String unbilledWOE = "UnbilledWOE";
    //public static String setVideoUrl="VideoURL";

    public static final String ledgerListDetails = "ledgerListDetails";

    public static final String amount = "amount";
    public static final String amountType = "amountType";
    public static final String narration = "narration";
    public static final String transactionType = "transactionType";
    public static final String credits = "credits";
    public static final String debits = "debits";

    public static final String ac_code = "ac_code";
    public static final String aadhar_no = "aadhar_no";
    public static final String address = "address";
    public static final String closing_balance = "closing_balance";
    public static final String balance = "balance";
    public static final String credit_limit = "credit_limit";
    public static final String doj = "doj";
    public static final String mobile = "mobile";
    public static final String pincode = "pincode";
    public static final String source_code = "source_code";
    public static final String Billamount = "Billamount";
    public static final String DAYAVG = "Days";

    public static final String tsp_image = "tsp_image";
    public static final String user_code = "user_code";
    public static String preotp = "PreOTP";

    public static String DAC = "DAC";


    public static final String automail = "automail";
    public static final String autosms = "autosms";
    public static final String brand = "brand";
    public static final String cancelaction = "cancelaction";
    public static final String reportformat = "reportformat";
    public static final String reportmail = "reportmail";
    public static final String sample_type = "sample_type";
    public static final String tsppreference = "tsppreference";


    //for firebase
    public static final String CHANNEL_ID = "my_channel_01";
    public static final String CHANNEL_NAME = "Simplified Coding Notification";
    public static final String CHANNEL_DESCRIPTION = "www.simplifiedcoding.net";


    public static final String remarks = "remarks";
    public static final String remark = "remark";
    public static final String test_code = "test_code";
    public static final String requesttype = "requesttype";


    //     public static final String MAINURL = "https://api.thyrocare.com/APIs/"; // ---- Live(New)
//    public static final String MAINURL = "https://www.thyrocare.com/APIs/"; // ---- Live
    public static final String MAINURL = "https://www.thyrocare.com/APIs/"; // ---- Staging

    //        public static String BASE_URL_TOCHECK ="https://api.thyrocare.com/APIs/"; // ---- Live (New)

    public static final String PARAMETER_SEP = "&";
    public static final String PARAMETER_EQUALS = "=";
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";
    public static final String INTENT_EXTRA_FROM_SKIP = "from_skip";

    public static String USER_TYPE_PROMOTER = "PROMOTER";
    public static String USER_TYPE_LOYALTY = "LOYALTY";

    public static String ORDER_STATUS_APPOINTMENT = "appointment";
    public static String ORDER_STATUS_YET_TO_ASSIGN = "yet to assign";
    public static String ORDER_STATUS_CANCELLED = "CANCELLED";
    public static String ORDER_STATUS_REJECTED = "REJECTED";
    public static String ORDER_STATUS_SERVICED = "SERVICED";
    public static String ORDER_STATUS_DONE = "DONE";
    public static String ORDER_STATUS_CREDITED = "CREDITED";
    public static String ORDER_STATUS_DILEVERY = "DILEVERY";
    public static String ORDER_STATUS_REPORTED = "REPORTED";
    public static String ORDER_STATUS_PARTIALY_DONE = "PARTIALY DONE";
    public static String ORDER_STATUS_DISPATCHED = "DISPATCHED";

    public static String VERSION_API_URL = "/api";
    public static String ORDER_BOOKING = VERSION_API_URL + "/OrderBooking";
    public static String CART = VERSION_API_URL + "/Cart";
    public static String LOGIN = "/Token";
    public static String FETCH_BRAND_WISE_TEST_MASTER = VERSION_API_URL + "/BrandTestRateList/2";
    public static String FETCH_ORDER_DETAILS = VERSION_API_URL + "/OrderDetailsByVisit";
    public static String REMOVE_BENEFICIARY = VERSION_API_URL + "/RemoveBeneficiary";
    public static String REMOVE_BENEFICIARY_SMS = VERSION_API_URL + "/AllSMS/PostRemoveBenSMS";

    public static String AuthKey = "Bearer oM0QM47aDgds97SHB74HlBtRhh-IU_PerltsXiWVEetwpusVQd21ZzrdarnBzIV-4YcILOY2J8B0GNZyMocmoRBinkuCpgVjcLjG9XYOYBcuj3KD8Yex3wnEh3lHXFxlokgmx12iag8tRXuu6dxdfukQwzXctDs0LczXNv_4v75y7CvH6_Y-caaSxxqniw2SWB8v277sXKIWOFRBwvA43mvIJYLzTboIHprbaQ4Ng1fxGV2sUSQk1FcDcI01jtx1jN9glS2ME4Z5TVahoe7WYTAVEC9C84oBBFlgxOrO79H-v116HEsvH0mVdTEFU6Wl5t3fSSG81ngO2Bds24s9_Iskvcb1-kJXB-LMppT0jlRVgiWTIKMcZrbjsf3wEr4EMCtBPE9pSigedml1HOl8sXo9gSS408k1KkRLoK0CGtGnG3FDWIq3AhaVeaxlic3YMOGtV-FuSDqawqAF7UwsO0C5ZRKZv5tXydADacPezux7TdRhdBg_ZBGX-pZKB1H4AaTAF6xYS_GOZ9vWPH4i4g";
    public static String statusBen = "Select Beneficiary Name";
    public static String AppUserName = "shilpi@thyrocare.com";
    public static String UserPassWord = "Thyroc1234";
    public static String Grant_type = "password";
    public static String AccessToken = "AccessToken";

    public static String VITAMIND_CODE = "VITDC";
    public static String PROFILE_HEMOGRAM_CODE = "H6";
    public static String TESTS_HEMOGRAM_CODE = "HEMOGRAM - 6 PART (DIFF)";
    public static String HBA1c_CODE = "HBA";
    public static String VitaminD_name = "VITAMIN D TOTAL";
    public static String HEMOGRAM_CBC_name = "HEMOGRAM/CBC";
    public static String HBA_name = "HBA";
    public static String HBA_name_1 = "HbA1c";

    public static String TECHSO_RECEIPTS_API = "/api/Ledger/OrderReceipt/";
    public static String GENERATE_PAYMENT_COLLECTION_LINK = VERSION_API_URL + "/PayThyrocare/StartTransaction";
    public static String POSTUPDATEORDERS = VERSION_API_URL + "/YNCStatusChange/PostUpdateOrderHistory";

    public static String SPOT_LIGHT_FLAG = "spotlightflag";
    public static String SPOT_LIGHT_FLAG_KEY = "spotlightflagkey";
    public static String SPOT_LIGHT_FLAG_KEY_ORDERDETAIL = "spotlightflagkey_orderdetails";
    public static String SPOT_LIGHT_FLAG_KEY_ORDERSUMMARY = "spotlightflagkey_ordersummary";
    public static String SPOT_LIGHT_FLAG_KEY_ADD_BENEFICIARY = "spotlightflagkey_addbeneficiary";
    public static String SPOT_LIGHT_FLAG_KEY_ADD_NEW_BENEFICIARY = "spotlightflagkey_addnewbeneficiary";

    public static String IBM_USER_DETAILS = "ibm_userdetails";
    public static String IBM_USER_CHANNEL_ID = "IBMUserChannelID";
    public static String IBM_USER_ID = "IBMUserID";
    public static String ACCESSCODE_URL_IBM = "https://api7.ibmmarketingcloud.com/oauth/token";
    public static String LOOKUP_URL_IBM = "https://api7.silverpop.com:443/rest/databases/162662/contactbylookupkey"; // 162662 IS DATABASE ID OF THYROCARE IN  IBM WATSON CONSOLE---
    public static String CLIENTID_KEY = "client_id";
    public static String CLIENTID_VALUE = "eaefdaf0-133e-4ad3-9d8e-46fe3bf6f6be";
    public static String CLIENTSECRET_KEY = "client_secret";
    public static String CLIENTSECRET_VALUE = "046688a3-9a53-45ac-a3fe-3a091f87516b";
    public static String GRANTTYPE_KEY = "grant_type";
    public static String GRANTTYPE_VALUE = "refresh_token";
    public static String REFRESHTOKEN_KEY = "refresh_token";
    public static String REFRESHTOKEN_VALUE = "r5NatTI5Ma3yD2o1AeRshpkjfjz-Sx_GEci3BohTeoNwS1";
    public static String IS_IBM_TOKEN_EXECUTED_FLAG = "IBM_Flag";

    public static boolean isEditProfilefragment = false;

    public static String CUSTOMER_PHONE_NO_COLUMN_NAME = "Customer Mobile Number";
    public static String CUSTOMER_EMAIL_ID_COLUMN_NAME = "Email";
    public static String CUSTOMER_NAME_COLUMN_NAME = "Customer Name";

    //Activity redirection KEY
    public static String COMEFROM = "COMEFROM";


    //CONSTANTS FOR PAYU------
    public static final String PAYUMONEYKEY_MERCHANTKEY = MAINURL.equalsIgnoreCase(BASE_URL_TOCHECK) ? "y04wX2" : "xKDXHX";
    public static final String PAYUMONEYKEY_SALT = MAINURL.equals(BASE_URL_TOCHECK) ? "fLheLI5D" : "V76vIBJq";


    public static final String PAYTM_UPDATE_PAYMENT_URL = "PaymentGateway.svc/PaytmResponse";
    public static final String PAYTM_PAYMENT_REQUEST_URL = "PaymentGateway.svc/PaytmRequest";
    public static final String UPDATE_PAYMENT_URL = "PaymentGateway.svc/ClientPayment";


    public static final String PAYTMKEY_APIKEY = "api_key";
    public static final String PAYTMKEY_TYPE = "type";
    public static final String PAYTMVALUE_TYPE = "GENCHECKSUM";
    public static final String PAYTMKEY_ORDERID = "txtORDID";
    public static final String PAYTMKEY_AMOUNT = "txtAmount";
    public static final String PAYTMKEY_CALLBACK = "txtCallBack";
    public static final String PAYTMKEY_MOBILE = "txtMobile";
    public static final String PAYTMKEY_EMAIL = "txtEmail";
    public static final String PAYTMKEY_WEBSITE = "txtWebsite";
    public static final String PAYTMKEY_CUSTID = "txtCustID";
    public static final String PAYTMKEY_INDUSTRY = "txtINDID";


    public static final String PAYUMONEYKEY_APIKEY = "api_key";
    public static final String PAYUMONEYKEY_TRANSACTIONID = "txtID";
    public static final String PAYUMONEYKEY_AMOUNT = "txtAmount";
    public static final String PAYUMONEYKEY_PRODUCT = "txtProduct";
    public static final String PAYUMONEYKEY_FIRSTNAME = "txtFirstname";
    public static final String PAYUMONEYKEY_EMAIL = "txtEmail";
    public static final String PAYUMONEYKEY_OPTION1 = "txtOpt1";
    public static final String PAYUMONEYKEY_OPTION2 = "txtOpt2";
    public static final String PAYUMONEYKEY_OPTION3 = "txtOpt3";
    public static final String PAYUMONEYKEY_OPTION4 = "txtOpt4";
    public static final String PAYUMONEYKEY_OPTION5 = "txtOpt5";
    public static final String PAYUMONEYKEY_OPTION6 = "txtOpt6";
    public static final String PAYUMONEYKEY_OPTION7 = "txtOpt7";
    public static final String PAYUMONEYKEY_OPTION8 = "txtOpt8";
    public static final String PAYUMONEYKEY_OPTION9 = "txtOpt9";
    public static final String PAYUMONEYKEY_OPTION10 = "txtOpt10";
    public static final String PAYUMONEYKEY_USERCREDENTIALS = "user_credentials";
    public static final String PAYUMONEYKEY_TRANSACTION_STATUS = "trn_status";
    public static final String PAYUMONEYKEY_TRANSACTION_HASH = "trn_hash";
    public static final String PAYUMONEYKEY_REQUEST_CHECKSUM = "checksum";

    public static final String PAYTM_REQUEST = "PAYTM_REQUEST";
    public static final String PAYTM_RESPONSE = "PAYTM_RESPONSE";
    public static final String PAYUMONEY_REQUEST = "PAYU_REQUEST";
    public static final String PAYUMONEY_RESPONSE = "PAYU_RESPONSE";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";


    public static final String DICTIONARY = "dictionary";
    public static final String GATEWAY = "getwaytype";
    public static final String ORDERID = "OrderNo";
    public static final String PAYMENTSTATUS = "paymentstatus";

    public static final String HOMEMODULE = "Home Screen";
    public static final String PROFILEMODULE = "Profile Screen";
    public static final String BOOKTESTMODULE = "Test/Search Screen";
    public static final String OFFERSMODULE = "Offers Screen";
    public static final String REPORTMOUDLE = "Report Screen";
    public static final String MABCMODULE = "Disorders Screen";
    public static final String THYRONOMICSMODULE = "Thyronomics Screen";

    public static final String HOMEBUTTON = "Home Button";
    public static final String OFFERSBUTTON = "Offers Button";
    public static final String PROFILESBUTTON = "Profile Button";
    public static final String DISORDERSBUTTON = "Disorders Button";
    public static final String TESTBUTTON = "Test Button";
    public static final String COMPAREBUTTON = "Compare Button";
    public static final String MYACCOUNTBUTTON = "MyAccount Button";
    public static final String MYCARTBUTTON = "MyCart Button";
    public static final String MYWALLETBUTTON = "MyWallet Button";
    public static final String ABOUTBUTTON = "Aboutus Button";
    public static final String CONTACTUSBUTTON = "Contactus Button";
    public static final String FEEDBACKBUTTON = "Feedback Button";
    public static final String SHAREBUTTON = "Share Button";
    public static final String FAQBUTTON = "FAQ Button";
    public static final String REFERAFRIEND = "Refer A Friend Button";
    public static final String SERVICEAVAILABILITY = "Service Availability Button";
    public static final String REPORTS = "My Reports Button";
    public static final String RECEIPTS = "My receipt Button";

    public static final String TNC_BUTTON = "Terms and Condition Button";
    public static final String FB_BUTTON = "Facebook Button";
    public static final String TWITTER_BUTTON = "Twitter Button";
    public static final String LINKEDIN_BUTTON = "Linkedin Button";
    public static final String INSTAGRAM_BUTTON = "Instagram Button";
    public static final String GOOGLEPLUS_BUTTON = "GooglePlus Button";

    public static final String BOOKING_API_KEY = "DAuWZtKHyVb8OhjhHSuAwCB6uAyzVzASVTxXcCT8@0fkrvZ5fG9lw533tKKmxVnu";


    //Log types
    public static String ERROR = "Error";
    public static String INFO = "Info";
    public static String DEBUG = "Debug";
    public static String WARNING = "Warning";
    public static String SOUT = "sout";


    public static final String DSA = "DSA";
    public static final String DOMAIN_VALUE = "DSAWTCAPP";
    public static final String ORDER_NO = "OrderNo";
    public static final String ORDER_TYPE = "Order_Type";
    public static final String CLIENT_TYPE = "client_type";
    public static final String CLIENT_STATUS = "client_status";
    public static final String DOMAIN = "domain";
    public static final String MOBILE = "mobile";
    public static final String NAME = "name";

    public static int Offermodule = 4;
    public static int Testsmodule = 2;

    public static int Imagepadding = 25;

    public static int PAYAMOUNT = 5000;

    public static String THYROTEST = "Thyro";
    public static String THYRONAME = "";

    public static String PREF_SAVEPATIENTDETAILS = "savePatientDetails";
    public static String PREF_USER_DETAILS = "Userdetails";
    public static String PREF_SHOWSELECTEDTEST = "showSelectedTest";


    //   USER LOG SHAREDPREF KEY
    public static String SHR_USERLOG="Userlog";
    public static String SHR_APPID= "sappId";
    public static String SHR_IMEI= "imei";
    public static String SHR_ISLOGIN= "islogin";
    public static String SHR_MODTYPE= "modtype";
    public static String SHR_FIRSTINSTALL= "isfirstinstall";
    public static String SHR_OS= "os";
    public static String SHR_TOKEN= "token";
    public static String SHR_USERNAME= "username";
    public static String SHR_VERSION= "version";
    public static final int USER_APPID=5;

    public static String SHLOGIN= "LOGIN";
    public static String LOGOUT= "LOGOUT";
    /**
     * test Color Code
     */
    public static final String EDTA = "EDTA";
    public static final String SERUM = "SERUM";
    public static final String URINE = "URINE";
    public static final String FLUORIDE = "FLUORIDE";
    public static final String LITHIUMHEPARIN = "LITHIUM HEPARIN";
    public static final String SODIUMHEPARIN = "SODIUM HEPARIN";
    // public static String GENRATE_OTP_API_KEY = "sNhdlQjqvoD7zCbzf56sxppBJX3MmdWSAomi@RBhXRrVcGyko7hIzQ==";
    public static String GENRATE_OTP_API_KEY = "yLZ4cKcEgPsnZn1s9b9FHhR9cUbO4AdM0z3fvmKQjiw=";

    public static String RES0000 = "RES0000";

    public static String returnToken(Activity mContext) {
        String str = "";
        SharedPreferences sharedPref = mContext.getPreferences(Context.MODE_PRIVATE);
        str = sharedPref.getString(AccessToken, "");

        return str;
    }

    public static String parastart = "<li>\n" + "<pre style=\"white-space: pre-wrap;\"align=\"justify\"><span style=\"font-family: 'arial', serif;\"><span style=\"font-size: medium;\">";
    public static String paraend = "</span></span></pre>\n" + "</li>\n";

    public static final String WHATSNEWTEXT = "<ol>\n" +
            parastart + "New Login Screen" + paraend +
            parastart + "UI Changes." + paraend +
            parastart + "Bug Fixes." + paraend +
            "</ol>";

    public static final String WalletHelpContent = "<ol>\n" + parastart + "You can gain this CASHBACK by booking done through ThyroApp for you and your friends." + paraend +
            parastart + "Once the booked customer undergoes the test, you will be gifted with the CASHBACK in your wallet." + paraend +
            parastart + "You can use this CASHBACK against your future bookings." + paraend +
            parastart + "This wallet balance can be only used in ThyroApp." + paraend +
            parastart + "Cashback is not applicable on discounted offers" + paraend +
            "</ol>";

    public static final String CHASBACKNOTE = "<ol>\n" +
            parastart + "" + paraend +
            parastart + "Cashback is not applicable on discounted offers" + paraend +
            "</ol>";

    public static final String VIDEOPATH = "Path";
    public static final String VIDEOID = "id";

}
