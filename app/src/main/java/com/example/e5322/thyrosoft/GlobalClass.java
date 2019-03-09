package com.example.e5322.thyrosoft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.MainModelForAllTests.TESTS_GETALLTESTS;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.CAMP_LIST;
import com.example.e5322.thyrosoft.Models.CommInbox_Model;
import com.example.e5322.thyrosoft.Models.CommToCpl_Model;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.Models.commMaster_Model;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.TestListModel.Tests;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.e5322.thyrosoft.ToastFile.relogin;

/**
 * Created by E5322 on 21-03-2018.
 */

public class GlobalClass {
    public static int flag = 0;
    public static String setHomeAddress, getPhoneNumberTofinalPost, getFinalEmailAddressToPOst, getFinalPincode, selectedTestnamesOutlab;
    public static String windupCountDataToShow;
    public static ArrayList<BaseModel> saveSlectedList = new ArrayList<>();
    public static String saveDtaeTopass;
    public static boolean setflagToRefreshData;
    public static boolean toCPLflag;
    public static String setScp_Constant;
    public static String setReferenceBy_Name;
    public static String getSelectedItemFor_Rate_calculator;
    public static String CHN_Date;
    public static String mode, routine_code, flight_name, flight_number, departure_time, arrival_time, dispatch_time, CourierName, bsv_barcode, total_sample, rpl_sample, cpl_sample, Consignment_barcode;
    public static String passDate;
    public static String passDateFromLead;
    public static String lead_date;
    public static boolean setFlagBackToWoe = false;
    public static boolean setFlag_back_toWOE = false;

    public static String getCourier_name;
    public static String get_edt_bus_name;
    public static String bus_name;
    public static String bus_number;
    public static int flagtsp;
    public static String bus_name_to_pass;
    public static String consignment_number;
    public static String consignment_barcode;
    public static String bsv_barcode_num;
    public static String packaging_dtl;
    public static String expt_transit_tm;
    public static String temp_consignment;
    public static String getSelectedScp;
    public static boolean setFlagToClose=false;
    public static AlertDialog alert;
    public static boolean flagforRefresh=false;;
    private static String stringofconvertedTime;
    public static String cutString;

    private final Context context;
    int[] colors = {R.color.WOE, R.color.entered, R.color.confirmed, R.color.imported};
    int[] textcolors = {R.color.WOEtext, R.color.enteredtext, R.color.confirmedtext, R.color.importedtext};
    public static String[] putData;
    public static String getPatientIdforDeleteDetails, brandName, typeName;
    public static boolean setFlag = true;
    public static ArrayList<String> items = new ArrayList<>();
    public static ArrayList<Summary_model> summary_models;
    public static ArrayList<Barcodelist> barcodelists;

    public static boolean flagToSend = false;
    public static boolean flagToSendfromnavigation = false;
    public static boolean flagtoMove = false;

    public static String getAllPhoneNumber, getEmailAddre, getYesterdaysDate;
    public static String branditem, saveMobileNUmber, typeItem, subSourceCodeItem, srNo_item, mobile_number, email_id, id_value, globalNameAadhar, globalAgeAadhar, globalGenderAadhar, responseVariable, setSR_NO;
    public static String getDate, setWindUpCount, getscannedData;
    public static String getPhoneofTTlDPS, getEmailofTTlDPS;
    //Selected test for WOE adapter lisy
    public static ArrayList<String> selctedTestNames = new ArrayList<>();
    public static ArrayList<String> selctedTestNamesILS = new ArrayList<>();
    public static ArrayList<String> windupBarcodeList = new ArrayList<>();
    public static ArrayList<Tests> testCartArrayList = new ArrayList<>();
    public static ArrayList<TESTS_GETALLTESTS> putPopDtaa = new ArrayList<>();
    public static ArrayList<BCT_LIST> getBtechList = new ArrayList<>();
    public static ArrayList<SetBarcodeDetails> setScannedBarcodes = new ArrayList<>();
    public static ArrayList<SetBarcodeDetails> setScannedBarcodesULC = new ArrayList<>();
    public static ArrayList<CAMP_LIST> getcamp_list = new ArrayList<>();
    public static ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist = new ArrayList<>();
    public static ArrayList<String> saveBarcodes = new ArrayList<>();
    public static String specimenttype;


    public static List<Fragment> mFragmentList = new ArrayList<>();
    public static List<String> mFragmentTitleList = new ArrayList<>();


    public static ArrayList<ScannedBarcodeDetails> scannedBarcodeDetails = new ArrayList<>();
    //Selected profile for WOE adapter lisy
    public static ArrayList<String> selctedProfileNames = new ArrayList<>();
    public static ArrayList<String> selctedProfileNamesILS = new ArrayList<>();

    //Selected pop for WOE adapter lisy
    public static ArrayList<String> selctedPopNames = new ArrayList<>();
    public static ArrayList<String> selctedPopNamesILS = new ArrayList<>();


    public static ArrayList<String> selctedoutLabTestNames = new ArrayList<>();
    public static ArrayList<Integer> getPosition = new ArrayList<>();///for checkbox for employees in Employee adapetr
    public static ArrayList<Integer> getPositionOutlab = new ArrayList<>();///for checkbox for employees in Employee adapetr


    public static ArrayList<billingDetailsModel> billingDETArray = new ArrayList<billingDetailsModel>();
    public static ArrayList<String> billingDETheaderArray = new ArrayList<>();
    public static ArrayList<String> empList = new ArrayList<>();//new arralist for employee
    public static String date = "";
    public static String reg_name_glo, reg_landline_glo, reg_profession_glo, reg_qualification_glo, reg_int_location_glo, reg_pincode_glo, reg_Addr_glo, reg_city_glo, reg_state_glo, reg_country_glo, reg_email_glo, reg_number_glo;
    public static String type = "";

    public static ArrayList<CommunicationMaster> commSpinner = new ArrayList<CommunicationMaster>();


    public static ArrayList<CommInbox_Model> commFromCPL;
    public static ArrayList<CommToCpl_Model> commToCPL;


    public static int FromCPLInt = 0;

    //0 means from ,1 means to
    public static String MONTH = ""; //0 means from ,1 means to
    public static String YEAR = ""; //0 means from ,1 means to
    public static int CHANGEMONTH = 0; //0 means from ,1 means to

    public static ArrayList<String> listdata = new ArrayList<String>();
    public static ArrayList<BaseModel> selectedTestsList = new ArrayList<>();
    public static ArrayList<Base_Model_Rate_Calculator> selectedTestsListRateCal = new ArrayList<>();
    public static ArrayList<String> debitlist = new ArrayList<String>();
    public static ArrayList<Ledger_DetailsModel> CREDITLIST = new ArrayList<Ledger_DetailsModel>();
    public static ArrayList<Ledger_DetailsModel> DEBIT = new ArrayList<Ledger_DetailsModel>();
    ProgressDialog progressDialog;


    public GlobalClass(Context context) {
        this.context = context;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }



    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static void clearPreference(Activity activity) {
        SharedPreferences.Editor getProfileName = activity.getSharedPreferences("profilename", 0).edit();
        getProfileName.clear();
        getProfileName.commit();

        SharedPreferences.Editor editor1 = activity.getSharedPreferences("profilename", 0).edit();
        editor1.clear();
        editor1.commit();

        SharedPreferences.Editor editor = activity.getSharedPreferences("Userdetails", 0).edit();
        editor.clear();
        editor.commit();//

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
        prefsEditor1.clear();
        prefsEditor1.commit();

        SharedPreferences profilenamesharedpref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor profile_shared = profilenamesharedpref.edit();
        profile_shared.clear();
        profile_shared.commit();

        SharedPreferences prifleDataShared = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor profile_shared_pref = prifleDataShared.edit();
        profile_shared_pref.clear();
        profile_shared_pref.commit();


        SharedPreferences saveAlldata = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor deletepredf = saveAlldata.edit();
        deletepredf.clear();
        deletepredf.commit();

        SharedPreferences myData = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor prefsEditordata = myData.edit();
        prefsEditordata.clear();
        prefsEditordata.commit();

        SharedPreferences appSharedPrefsDtaa = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor mydataModel = appSharedPrefsDtaa.edit();
        mydataModel.clear();
        mydataModel.commit();
    }

    public static void redirectToLogin(Activity activity) {
        TastyToast.makeText(activity, relogin, TastyToast.LENGTH_SHORT, TastyToast.INFO);

        Intent i = new Intent(activity, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
        clearPreference(activity);
    }

    public void showProgressDialog(Activity activity) {
        if (progressDialog != null && !progressDialog.isShowing())

            if (!((Activity) context).isFinishing()) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle("Kindly wait ...");
                progressDialog.setMessage(ToastFile.processing_request);
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.setMax(20);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

    }

    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        stringofconvertedTime = null;
        try {
            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd");
            String convertedDate = sdf_format.format(date);
            String finalTime = convertedDate + " " + time;
            date = inputFormat.parse(finalTime);
            stringofconvertedTime = outputFormat.format(date);
            cutString = stringofconvertedTime.substring(11, stringofconvertedTime.length() - 0);
        } catch (ParseException e) {

            if (time.contains("AM")) {
                time = time.substring(0, time.length() - 2);
                time = time + "a.m.";
            } else if (time.contains("PM")) {
                time = time.substring(0, time.length() - 2);
                time = time + "p.m.";
            }

            SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            SimpleDateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = null;
            stringofconvertedTime = null;

            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd");
            String convertedDate = sdf_format.format(date);
            String finalTime = convertedDate + " " + time;
            try {
                date = inputFormat1.parse(finalTime);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            stringofconvertedTime = outputFormat1.format(date);
            cutString = stringofconvertedTime.substring(11, stringofconvertedTime.length() - 0);
            //Format of the date defined in the input String


        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringofconvertedTime;
    }


    public static void  goToHome(Activity activity){
        Intent i = new Intent(activity, ManagingTabsActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public void dismissProgressDialog() {

        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialog(final Activity activity){ AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set the Alert Dialog Message
        builder.setMessage(ToastFile.intConnection);
        builder.setCancelable(false);
        builder.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {

                        Intent i = new Intent(activity, ManagingTabsActivity.class);
                    }
                });
        alert = builder.create();
        alert.show();}

    public static Boolean checkForApi21() {    Boolean boolStatus = false;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            boolStatus = true;    } else {        boolStatus = false;    }
        return boolStatus;}


    public void DisplayImage(Activity activity, String Url, ImageView imageView) {

        //            Glide.get(mActivity).clearMemory();
        Glide.with(activity).load(Url)
                .asBitmap()
                .placeholder(R.drawable.userprofile).dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                .error(R.drawable.userprofile)
                .into(imageView);

    }

}
