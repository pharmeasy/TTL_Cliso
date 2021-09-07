package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.widget.TextView;

import com.example.e5322.thyrosoft.Models.InclusionMasterModel;

import java.util.ArrayList;

/**
 * Created by Orion on 4/27/2017.
 */

public class BundleConstants {
    //Intent and Activity Result related constants
    public static final int VOMD_START = 1004;
    public static final int VOMD_ARRIVED = 1005;
    public static final int HMD_START = 1006;
    public static final int HMD_ARRIVED = 1007;
    public static final int ADD_TESTS_START = 1008;
    public static final int ADD_TESTS_FINISH = 1009;
    public static final int ADD_EDIT_START = 1010;
    public static final int ADD_EDIT_FINISH = 1011;
    public static final int BCMD_START = 1012;
    public static final int BCMD_ARRIVED = 1013;
    public static final int START_BARCODE_SCAN = 0x0000c0de;
    public static final int APPROVE_CAMP_START = 1014;
    public static final int APPROVE_CAMP_FINISH = 1015;
    //Bundle Constants
    public static final String ACTIVITY = "activity";
    public static final String VISIT_ORDER_DETAILS_MODEL = "visitOrderDetailsModel";
    public static final String BENEFICIARY_DETAILS_MODEL = "beneficiaryDetailsModel";
    public static final String ORDER_DETAILS_MODEL = "orderDetailsModel";
    public static final String HUB_BTECH_MODEL = "hubBtechModel";
    public static final String BTECH_CLIENTS_MODEL = "btechClientsModel";
    public static final String BENEFICIARY_TEST_LIST = "beneficiaryTestList";
    public static final String TESTS_LIST = "testsList";
    public static final String SELECTED_TESTS_LIST = "selectedTestsList";
    public static final String IS_BENEFICIARY_EDIT = "isBeneficiaryEdit";
    public static final String SELECTED_TESTS_COST = "selectedTestsCost";
    public static final String SELECTED_TESTS_TOTAL_COST = "selectedTestsTotalCost";
    public static final String REST_BEN_TESTS_LIST = "restBeneficiaryTestList";
    public static final String SELECTED_TESTS_DISCOUNT = "selectedTestsDiscount";
    public static final String SELECTED_TESTS_INCENTIVE = "selectedTestsIncentive";
    public static final String VISIT_ID = "visitID";
    public static final String B_TECH_ID = "bTechId";
    public static final String B_TECH_STATUS = "BTecStatus";
    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_IN_ROUTE = "inroute";
    public static final String STATUS_COLLECTING = "collecting";
    public static final String STATUS_OFFLINE = "offline";
    public static final String STATUS_IDLE = "idle";
    public static final String LATTITUDE = "LATTITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String COMPLAINT_ID = "complain_id";
    public static final String SLOT_ID = "slotID";
    public static final String INTENT_TYPE = "intentType";
    public static final int TYPE_B_TECH_ID = 0;
    public static final int TYPE_SLOT_ID = 1;
    public static final String BEN_NAME = "BenName";
    public static final String BEN_AGE = "BenAge";
    public static final String BEN_TESTS = "BenTests";
    public static final String BEN_GENDER = "BenGender";
    public static final int GET_BEN_EDITS = 0;
    public static final int GET_BEN_TESTS = 1;
    public static final String BTS_RATING = "BtsRating";
    public static final String BTS_LEAVETYPE = "LeaveType";
    public static final String BTS_LEAVETYPE_BTECHID = "LeaveTypeBtechid";
    public static final String BTS_LEAVETYPE_BTECHNAME = "LeaveTypeBtechname";
    public static final String BTS_RATING_DETAILS = "BtsRatingDetails";
    public static final String BTS_ORDEREXEC_PICKUP_DETAILS = "BtsPickupDetails";
    public static final String BTS_ORDEREXEC_COLLECTION_DETAILS = "BtsOrderExecCollectionDetails";
    public static final String BTS_ORDERSTATUS_DETAILS = "OrderStatus";
    public static final String APPROVE_LEAVE_MODEL = "LeaveDetailsModel";
    public static final String BTS_ORDEREXEC_ORDER_DETAILS = "BtsOrderExec_OrderDetails";
    public static final String IS_TEST_EDIT = "isTestEdit";
    public static final String CAME_FROM = "cameFrom";
    public static final String CALLER_PHONE_NUMBER = "callerPhoneNumber";
    public static final String SLIDER_PAGE_NO = "sliderPageNo";
    public static final String SLIDER_PAGE_COUNT = "sliderPageCount";
    public static final String IS_ADD_BEN = "isAddBen";
    // Flag for Refresh Activity OrderPending
    public static int APP_RefreshorderPending_a = 0;
    public static int FlagForR = 0;
    public static int SetDownLoadExcelCount = 0;
    public static final String Directory_Folder = "/BTSDocuments";
    public static ArrayList<InclusionMasterModel> lists_incl_dupl;

    //Image Paading from left right top bottom boom menu item
    public static int Imagepadding=13;

    //Color background Change For 0=white , 1=othercolor
    public static int ColorStat=0;
    public static String statusText="SELECT STATUS";
    public static String remarksText="Select Remarks";
    public static String reasonText="Select Reason";
    public static int FlagFor_Spinner=0;
    public static int Selected_SpinnerFlg=0;
    public static String BtechText="SELECT BTECH";
    public static String stat_Postpaid="POSTPAID";
    public static String stat_Prepaid = "PREPAID";
    public static String PersuationText="PERSUASION";
    public static String CallBackText="CALLBACK";
    public static String selectAcCd="Select Code";
    public static String narationtext="Select Naration";
    public static String selectslot="Select Slot";
    public static TextView tv_prepostanalytics_count;
    public static boolean addremvflag =false ;

    public static final String PAYMENT_STATUS = "PaymentStatus";

    public static int loadSize = 20;
    public static final String B_TECH_NAME = "bTechName"; //TODO NEHA
    public static final String FROM_DATE = "fromdate"; //TODO NEHA
    public static final String To_Date = "todate"; //TODO NEHA
    public static String selectbtechtransactionText = "SELECT BTS Code";

    public static final String ID = "ID";
}
