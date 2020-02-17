package com.example.e5322.thyrosoft;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.MainModelForAllTests.TESTS_GETALLTESTS;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BSTestDataModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.CAMP_LIST;
import com.example.e5322.thyrosoft.Models.Camp_Intimatgion_List_Model;
import com.example.e5322.thyrosoft.Models.CommInbox_Model;
import com.example.e5322.thyrosoft.Models.CommToCpl_Model;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.TestListModel.Tests;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

import static android.content.Context.MODE_PRIVATE;
import static com.example.e5322.thyrosoft.ToastFile.relogin;

/**
 * Created by E5322 on 21-03-2018.
 */

public class GlobalClass {

    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
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
    public static long syntime = 0;
    public static long curr_time = 0;
    public static long differ_millis = 0;
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
    public static boolean setFlagToClose = false;
    public static AlertDialog alert;
    public static boolean flagforRefresh = false;
    public static String cutString;
    public static String[] putData;
    public static String getPatientIdforDeleteDetails, brandName, typeName;
    public static boolean setFlag = true;
    public static ArrayList<String> items = new ArrayList<>();
    public static ArrayList<Summary_model> summary_models;
    public static ArrayList<Barcodelist> barcodelists;
    public static ArrayList<Barcodelist> BMC_barcodelists;
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
    public static ArrayList<Camp_Intimatgion_List_Model> global_camp_intimatgion_list_models_arrlst = new ArrayList<>();
    public static ArrayList<CAMP_LIST> getcamp_list = new ArrayList<>();
    public static ArrayList<ScannedBarcodeDetails> finalspecimenttypewiselist = new ArrayList<>();
    public static String specimenttype;
    public static ArrayList<SetBarcodeDetails> BMC_setScannedBarcodes = new ArrayList<>();
    public static ArrayList<ScannedBarcodeDetails> BMC_BarcodeDetailsList = new ArrayList<>();
    ;
    public static ArrayList<ScannedBarcodeDetails> BMC_BarcodeDetailsTTLOTHERSList = new ArrayList<>();
    public static String BMC_specimenttype;
    public static Bitmap TRF_BITMAP;
    public static Bitmap RECEIPT_BITMAP;
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
    private static Dialog dialog;
    private static String stringofconvertedTime;
    private final Context context;
    int[] colors = {R.color.WOE, R.color.entered, R.color.confirmed, R.color.imported};
    int[] textcolors = {R.color.WOEtext, R.color.enteredtext, R.color.confirmedtext, R.color.importedtext};
    ArrayList<Base_Model_Rate_Calculator> selectedTestsListCampIntimation = new ArrayList<>();
    ProgressDialog progressDialog;

    public GlobalClass(Context context) {
        this.context = context;
    }

    public static String generateBookingID() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        while (random.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * alphanumeric.length());
            random.append(alphanumeric.charAt(index));
        }
        String generatedstr = random.toString();
        return generatedstr;
    }


    public static Date dateFromString(String dateStr, SimpleDateFormat dateFormat) {

        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static ProgressDialog progress(Context context, boolean autcancel) {


        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Kindly Wait..");
        progress.setMessage("Processing Request");
        progress.setCanceledOnTouchOutside(autcancel);
        progress.setCancelable(autcancel);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progress;
    }


    public static void ShowError(final Activity activity, String title, String message, final boolean setFinish) {
        try {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(activity)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle(title)
                    .setCancelable(false)
                    .setMessage(message)
                    .addButton("OK", -1, activity.getResources().getColor(R.color.maroon), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (setFinish) {
                                activity.finish();
                            }
                        }
                    });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static Bitmap rotate(Bitmap bitmap, String photoPath) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static File getCompressedFile(final Activity activity, File actualImage) {
        File compressedFile = null;

        try {
            compressedFile = new Compressor(activity)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFile(actualImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedFile;
    }

    public static String convertDate(String convDate) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            date = sdf.parse(convDate);
//            sdf = new SimpleDateFormat("yyyy-MM-dd");
            convDate = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convDate;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static ProgressDialog ShowprogressDialog(Context mContext) {

        ProgressDialog barProgressDialog = new ProgressDialog(mContext);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        // barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        return barProgressDialog;

    }

    public static void requestStoragePermission(final Context context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        } else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
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
        try {
            TastyToast.makeText(activity, relogin, TastyToast.LENGTH_SHORT, TastyToast.INFO);
            Intent i = new Intent(activity, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            clearPreference(activity);
        } catch (Exception e) {
            e.printStackTrace();
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
            time = time.substring(11, time.length() - 0);
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
            stringofconvertedTime = null;

            date = new Date();
            SimpleDateFormat sdf_format = new SimpleDateFormat("yyyy-MM-dd");
            String convertedDate = sdf_format.format(date);
            time = time.substring(11, time.length() - 0);
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

    public static void goToHome(Activity activity) {
        Intent i = new Intent(activity, ManagingTabsActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    public static void hideProgress(Context mcontext, ProgressDialog progressDialog) {

        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                mcontext = ((ContextWrapper) progressDialog.getContext()).getBaseContext();
                if (mcontext instanceof Activity) {
                    if (!((Activity) mcontext).isFinishing() && !((Activity) mcontext).isDestroyed())
                        progressDialog.dismiss();
                } else
                    progressDialog.dismiss();
            }
        }
    }

    public static void addTexwatcher(Context context, CharSequence s, EditText kyc_format) {

        String enteredString = s.toString();
        if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                enteredString.startsWith("#") || enteredString.startsWith("$") ||
                enteredString.startsWith("%") || enteredString.startsWith("^") ||
                enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
        ) {
            TastyToast.makeText(context, ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            if (enteredString.length() > 0) {
                kyc_format.setText(enteredString.substring(1));
            } else {
                kyc_format.setText("");
            }

        }
    }

    public static void showAlertDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
        alert.show();
    }

    public static Boolean checkForApi21() {
        Boolean boolStatus = false;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public static ArrayList<BSTestDataModel> getTestList() {

        ArrayList<BSTestDataModel> entity = new ArrayList<>();

        BSTestDataModel ent = new BSTestDataModel();
        ent.setTestName("Select");
        ent.setMinVal(0);
        ent.setMaxVal(0);
        ent.setRangeVal("");
        entity.add(ent);

        ent = new BSTestDataModel();
        ent.setTestName("FBS");
        ent.setMinVal(70);
        ent.setMaxVal(120);
        ent.setRangeVal("70-120");
        entity.add(ent);

        /*ent = new BSTestDataModel();
        ent.setTestName("PPBS");
        ent.setMinVal(90);
        ent.setMaxVal(140);
        ent.setRangeVal("90-140");
        entity.add(ent);*/

        ent = new BSTestDataModel();
        ent.setTestName("RBS");
        ent.setMinVal(70);
        ent.setMaxVal(160);
        ent.setRangeVal("70-160");
        entity.add(ent);

        return entity;
    }

    public static ArrayList<String> getCollAmount() {
        ArrayList<String> entity = new ArrayList<>();
        entity.add("Select collected amount");
        entity.add("0");
        entity.add("5");
        entity.add("10");
        entity.add("15");
        entity.add("20");
        entity.add("25");

        return entity;
    }

    public static boolean isAutoTimeSelected(final Activity activity) {
        final boolean[] isAutoTimeSelected = new boolean[1];
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1 && Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME_ZONE) == 1) {
                    isAutoTimeSelected[0] = true;
                } else {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    builder1.setTitle("Warning ").setMessage("You have to Enable Automatic date and time/Timezone settings").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (Settings.Global.getInt(activity.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                                        isAutoTimeSelected[0] = true;
                                    } else {
                                        isAutoTimeSelected[0] = false;

                                    }
                                }
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder1.setCancelable(false);
                    builder1.show();
                }

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return isAutoTimeSelected[0];
    }

    public static String formatDate(String currentFormat, String outputFormat, String date) {

        SimpleDateFormat curFormater = new SimpleDateFormat(currentFormat);
        SimpleDateFormat postFormater = new SimpleDateFormat(outputFormat);
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(date);
            date = postFormater.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void SetText(TextView txtview, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }

            if (txtview != null) {
//                txtview.setText("" + msg);
                if (msg.equalsIgnoreCase("null")) {
                    txtview.setText("");
                } else {
                    txtview.setText("" + msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month - 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static void SetEditText(EditText txtview, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }

            if (txtview != null) {
                if (msg.equalsIgnoreCase("null")) {
                    txtview.setText("");
                } else {
                    txtview.setText("" + msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String IsNull(String msg) {
        try {
            if (msg == null) {
                msg = "";
            }

            if (msg != null) {
                if (msg.equalsIgnoreCase("null")) {
                    msg = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static String getStringwithOutSpace(String str_gbls) {
        String str = "";
        str = str_gbls.replaceAll(" ", "%20");

        return str;
    }

    public static void toastyError(Context context, String string, boolean b) {
        TastyToast.makeText(context, string, ToastLength(b), TastyToast.INFO);
    }

    public static void toastySuccess(Context context, String string, boolean b) {
        TastyToast.makeText(context, string, ToastLength(b), TastyToast.SUCCESS);
    }

    public static void toastyInfo(Context context, String string, boolean b) {
        TastyToast.makeText(context, string, ToastLength(b), TastyToast.INFO);
    }

    private static int ToastLength(boolean b) {
        if (b) {
            return Toast.LENGTH_LONG;
        } else {
            return Toast.LENGTH_SHORT;
        }
    }


    public static void printLog(String Type, String Tag, String Label, String msg) {
        try {
            if (BuildConfig.DEBUG) {
                if (Type.equalsIgnoreCase("Error")) {
                    Log.e(Tag, " " + Label + ": " + msg);
                } else if (Type.equalsIgnoreCase("Info")) {
                    Log.i(Tag, " " + Label + ": " + msg);
                } else if (Type.equalsIgnoreCase("Debug")) {
                    Log.d(Tag, " " + Label + ": " + msg);
                } else if (Type.equalsIgnoreCase("Warning")) {
                    Log.w(Tag, " " + Label + ": " + msg);
                } else if (Type.equalsIgnoreCase("sout")) {
                    System.out.println(Tag + " " + Label + ": " + msg);
                }
            } else {
                System.out.println("Live....");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImageDialog(Activity activity, File file, String url, int flag) {
        dialog = new Dialog(activity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.trf_img_dialog);
        dialog.setCancelable(true);

        ImageView imgView = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        if (flag == 1) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (myBitmap != null)
                imgView.setImageBitmap(myBitmap);
            else
                Global.showCustomToast(activity, "Image not found");
        } else {
            Glide.with(activity)
                    .load(url)
                    .placeholder(activity.getResources().getDrawable(R.drawable.img_no_img_aval))
                    .into(imgView);
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        // dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    public static void storeProductsCachingTime(Activity activity) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.PREF_PRODUCTS_CACHING, 0).edit();
        editor.putLong("offer_millis", System.currentTimeMillis()); // add this line and comment below line for cache
        editor.apply();
    }


    public static int Dayscnt(Context context) {
        long offer_millis = 0, current_millis = 0, differ_millis = 0;
        SharedPreferences pref_prod_caching = context.getSharedPreferences(Constants.PREF_PRODUCTS_CACHING, MODE_PRIVATE);
        offer_millis = pref_prod_caching.getLong("offer_millis", 0);
        current_millis = System.currentTimeMillis();
        differ_millis = current_millis - offer_millis;
        int days = (int) (differ_millis / (1000 * 60 * 60 * 24));
        Log.e("TAG11", "<< DAYS >> " + days);
        return days;
    }

    public static void dynamicolordot(Context mContext, LinearLayout lin_color, int color) {
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(2, 0, 2, 2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMarginStart(10);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(GlobalClass.drawCircle(mContext, 50, 50, color));
        lin_color.addView(imageView);
    }

    public static int getStoreSynctime(Context context) {
        SharedPreferences synpref = context.getSharedPreferences("Syncpref", MODE_PRIVATE);
        syntime = synpref.getLong("synctime", 0);
        curr_time = System.currentTimeMillis();
        differ_millis = curr_time - syntime;
        int days = (int) (differ_millis / (1000 * 60 * 60 * 24));
        System.out.println("<< Days >> " + days);
        return days;
    }

    public static ShapeDrawable drawCircle(Context context, int width, int height, int color) {

        //////Drawing oval & Circle programmatically /////////////
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        oval.setIntrinsicHeight(height);
        oval.setIntrinsicWidth(width);
        oval.getPaint().setColor(color);
        return oval;
    }

    public static void showVolleyError(VolleyError error, Activity activity) {
        if (error instanceof TimeoutError) {
            Global.showCustomToast(activity, "Timeout Error");
        } else if (error instanceof ServerError) {
            Global.showCustomToast(activity, "Server Error");
        } else if (error instanceof NetworkError) {
            Global.showCustomToast(activity, "Network Error");
        } else if (error instanceof ParseError) {
            Global.showCustomToast(activity, "Parse Error");
        } else if (error instanceof NoConnectionError) {
            Global.showCustomToast(activity, "NoConnection Error");
        } else {
            Global.showCustomToast(activity, ToastFile.something_went_wrong);
        }
    }

    public static void volleyRetryPolicy(JsonObjectRequest request) {
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    public static void volleyRetryPolicy(StringRequest request) {
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    public static boolean isNull(String val) {
        if (val == null || val.equals(null) || val.trim().equals("") || val.trim().equals("null") || val.trim() == "" || val.trim() == "null")
            return true;
        return false;
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

    public void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisplayImage(Activity activity, String Url, ImageView imageView) {

        //            Glide.get(mActivity).clearMemory();
        Glide.with(activity).load(Url)
                .asBitmap()
                .placeholder(R.drawable.userprofile).dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                .error(R.drawable.userprofile)
                .fitCenter()
                .into(imageView);

    }

    public void DisplayVideoImage(Activity activity, String Url, ImageView imageView) {

        //            Glide.get(mActivity).clearMemory();
        Glide.with(activity).load(Url)
                .asBitmap()
//                .placeholder(R.drawable.thumbnailicon).dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
//                .error(R.drawable.thumbnailicon)
                .fitCenter()
                .into(imageView);

    }


    public void DisplayImage1(Activity activity, String Url, ImageView imageView) {
        Glide.with(activity).load(Url)
                .asBitmap()
                .placeholder(R.drawable.userprofile).dontAnimate()
                .error(R.drawable.userprofile)
                .into(imageView);
    }

    public void showcenterCustomToast(Activity activity, String message) {
        Context context = activity.getApplicationContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
        RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
        TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

        relItem.getBackground().setAlpha(204);
        txtToast.setText(message);

        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = null;
        if (!TextUtils.isEmpty(amount)) {
            formatter = new DecimalFormat("##,##,##,###.##");
            return formatter.format(Double.parseDouble(amount));
        } else {
            amount = "0";
        }
        return formatter.format(Double.parseDouble(amount));
    }


    public static void setLoadingGIF(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(null);
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e52d2e")));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void hideProgressDialog(ProgressDialog barProgressDialog, Context context) {
        if (barProgressDialog != null) {
            if (barProgressDialog.isShowing()) {
                context = ((ContextWrapper) barProgressDialog.getContext()).getBaseContext();
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                        barProgressDialog.dismiss();
                } else
                    barProgressDialog.dismiss();
            }
            barProgressDialog = null;
        }

    }

    public void showupdate(String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(null);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        if (progressDialog != null && !progressDialog.isShowing())
            if (!((Activity) context).isFinishing()) {
                progressDialog.show();
            }
    }

    public void hideProgressDialog1() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShortToast(Activity activity, String message) {
        if (activity != null) {
            Context context = activity.getApplicationContext();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void setBordertoView(Context context, LinearLayout lin_main, int color, int backcolor) {

        GradientDrawable border = new GradientDrawable();
        border.setColor(backcolor);
        border.setStroke(1, color); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            lin_main.setBackgroundDrawable(border);
        } else {
            lin_main.setBackground(border);
        }
    }

    public static void showCustomToast(Activity activity, String message) {

        if (activity != null) {
            Context context = activity.getApplicationContext();
            LayoutInflater inflater = activity.getLayoutInflater();

            View toastRoot = inflater.inflate(R.layout.custom_toast, null);
            RelativeLayout relItem = (RelativeLayout) toastRoot.findViewById(R.id.relItem);
            TextView txtToast = (TextView) toastRoot.findViewById(R.id.txtToast);

            relItem.getBackground().setAlpha(204);
            txtToast.setText(message);

            Toast toast = new Toast(context);
            toast.setView(toastRoot);
            //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }

    }


}
