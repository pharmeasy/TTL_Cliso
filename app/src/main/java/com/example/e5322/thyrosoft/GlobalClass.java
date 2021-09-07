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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.crowdfire.cfalertdialog.BuildConfig;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.Installation;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Activity.MyHurlStack;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.MainModelForAllTests.MainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.TESTS_GETALLTESTS;
import com.example.e5322.thyrosoft.Models.BCT_LIST;
import com.example.e5322.thyrosoft.Models.BSTestDataModel;
import com.example.e5322.thyrosoft.Models.BarcodeResponseModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.Models.CAMP_LIST;
import com.example.e5322.thyrosoft.Models.Camp_Intimatgion_List_Model;
import com.example.e5322.thyrosoft.Models.CommInbox_Model;
import com.example.e5322.thyrosoft.Models.CommToCpl_Model;
import com.example.e5322.thyrosoft.Models.Ledger_DetailsModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster;
import com.example.e5322.thyrosoft.Models.billingDetailsModel;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist;
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model;
import com.example.e5322.thyrosoft.TestListModel.Tests;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;
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
    public static ArrayList<Integer> getPosition = new ArrayList<>();
    public static ArrayList<Integer> getPositionOutlab = new ArrayList<>();
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
    public static ArrayList<BarcodeResponseModel.BarcodeDTO> barcodeArrayList = new ArrayList<>();
    public static String transID = "";
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


    public static Date dateFromString(String dateStr, SimpleDateFormat dateFormat) {

        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    public static boolean buildModelContainsEmulatorHints(String buildModel) {
        return buildModel.startsWith("sdk")
                || "google_sdk".equals(buildModel)
                || buildModel.contains("Emulator")
                || buildModel.contains("Android SDK");
    }


    public static String getHeaderValue(Context pContext) {

        SharedPreferences profile_pref = pContext.getSharedPreferences("profile", MODE_PRIVATE);
        String header;
        header = Constants.APPNAME + "/" + getversion(pContext) + "(" + getversioncode(pContext) + ")/" + profile_pref.getString("user_code", "") + "/" + getSerialnum(pContext);
        System.out.println("" + header);
        return header;
    }

    public static boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static String bytes2long(long sizeInBytes) {
        double SPACE_KB = 1024;
        double SPACE_MB = 1024 * SPACE_KB;
        double SPACE_GB = 1024 * SPACE_MB;
        double SPACE_TB = 1024 * SPACE_GB;

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        try {
            if (sizeInBytes < SPACE_KB) {
                return nf.format(sizeInBytes) + " MB";
            } else if (sizeInBytes < SPACE_MB) {
                return nf.format(sizeInBytes / SPACE_KB) + " KB";
            } else if (sizeInBytes < SPACE_GB) {
                return nf.format(sizeInBytes / SPACE_MB) + " MB";
            } else if (sizeInBytes < SPACE_TB) {
                return nf.format(sizeInBytes / SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes / SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " MB";
        }

    }

    public static String getSerialnum(Context pContext) {
        String imeiNo = "";
        try {
            imeiNo = Settings.Secure.getString(pContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imeiNo;
    }

    public static RequestQueue setVolleyReq(Context mContext) {
        HttpStack stack = new MyHurlStack(mContext);
        return Volley.newRequestQueue(mContext, stack);
    }

    public static String getversion(Context context) {
        String version = "";
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        return version;
    }

    public static int getversioncode(Context context) {
        int versionCode;
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCode = pInfo.versionCode;
        return versionCode;
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

    public static boolean checkAudioPermission(Activity mActivity) {
        int result = ContextCompat.checkSelfPermission(mActivity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mActivity, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
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


    public static String generateRandomOrderID() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        while (random.length() < 15) { // length of the random string.
            int index = (int) (rnd.nextFloat() * alphanumeric.length());
            random.append(alphanumeric.charAt(index));
        }
        String generatedstr = random.toString();
        return generatedstr;
    }

    public static void requestAudioPermission(Activity activity, int code) {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, code);
    }

    public static boolean checkArray(Object[] array) {
        return array != null && array.length > 0;
    }

    public static boolean CheckArrayList(Collection<?> arrylist) {
        if (arrylist != null && arrylist.size() > 0) {
            return true;
        }
        return false;
    }

    public static void SetText(TextView txtview, String msg) {
        try {
            if (GlobalClass.isNull(msg)) {
                msg = "";
            }

            if (txtview != null) {
                txtview.setText(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SetAutocomplete(AutoCompleteTextView txtview, String msg) {
        try {
            if (GlobalClass.isNull(msg)) {
                msg = "";
            }

            if (txtview != null) {
                txtview.setText(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SetRadiobutton(RadioButton radioButton, String msg) {
        try {
            if (GlobalClass.isNull(msg)) {
                msg = "";
            }

            if (radioButton != null) {
                radioButton.setText(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SetSpannable(TextView txtview, String msg) {
        try {
            if (GlobalClass.isNull(msg)) {
                msg = "";
            }

            if (txtview != null) {
                SpannableString content = new SpannableString((msg));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                txtview.setText(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void EditSetText(EditText editText, String msg) {
        try {

            if (GlobalClass.isNull(msg)) {
                msg = "";
            }

            if (editText != null) {
                editText.setText(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SetHTML(TextView txtview, String msg) {
        try {
            if (GlobalClass.isNull(msg)) {
                msg = "";
            }

            if (txtview != null) {
                txtview.setText(Html.fromHtml(msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void SetButtonText(Button button, String msg) {
        try {
            if (msg == null) {
                msg = "";
            }

            if (button != null) {
                if (msg.equalsIgnoreCase("null")) {
                    button.setText("");
                } else {
                    button.setText("" + msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isWhatsApp(Activity mActivity) {
        try {
            PackageManager pm = mActivity.getPackageManager();
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Global.showCustomToast(mActivity, MessageConstants.WHATSAPP_NOT_INSTALLED);
        }
        return false;
    }

    public static URL buildWhatsappUrl(Uri myUri) {
        URL finalUrl = null;
        try {
            finalUrl = new URL(myUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return finalUrl;
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

    public static Bitmap RotateImage(Bitmap bm) {
        try {
            if (bm.getWidth() > bm.getHeight()) {
                Bitmap bMapRotate = null;
                Matrix mat = new Matrix();
                mat.postRotate(90);
                bMapRotate = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mat, true);
                bm.recycle();
                bm = null;
                return bMapRotate;
            } else {
                return bm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
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

    public static void Hidekeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // Hide the soft keyboard
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static String convertDate(String convDate, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(convDate);
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

        AppPreferenceManager appPreferenceManager = new AppPreferenceManager(activity);
        appPreferenceManager.clearAllPreferences();

        SharedPreferences.Editor getProfileName = activity.getSharedPreferences("profilename", 0).edit();
        getProfileName.clear();
        getProfileName.commit();

        SharedPreferences.Editor editor1 = activity.getSharedPreferences("profilename", 0).edit();
        editor1.clear();
        editor1.commit();

        SharedPreferences.Editor profile = activity.getSharedPreferences("profile", MODE_PRIVATE).edit();
        profile.clear();
        profile.commit();

        SharedPreferences.Editor editor = activity.getSharedPreferences("Userdetails", 0).edit();
        editor.clear();
        editor.commit();//
        SharedPreferences.Editor getprodutcs = activity.getSharedPreferences("MyObject", MODE_PRIVATE).edit();
        getprodutcs.clear();
        getprodutcs.commit();


        SharedPreferences appSharedPrefsDtaa = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor mydataModel = appSharedPrefsDtaa.edit();
        mydataModel.clear();
        mydataModel.commit();

        Constants.covidwoe_flag = "0";
        Constants.covidfrag_flag = "0";
        Constants.ratfrag_flag = "0";
        Constants.pushrat_flag = 0;
        Constants.universal = 0;
        Constants.PUSHNOT_FLAG = false;
    }

    public static void redirectToLogin(Activity activity) {
        try {
            TastyToast.makeText(activity, relogin, TastyToast.LENGTH_SHORT, TastyToast.INFO);
            Intent i = new Intent(activity, SplashScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            clearPreference(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String Req_Date_Req(String time, String inputPattern, String outputPattern) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

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

            SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.US);
            SimpleDateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
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

    public static String changetimeformate(String time) {
        //Date/time pattern of input date
        DateFormat df = new SimpleDateFormat("hh:mm a", Locale.US);
        //Date/time pattern of desired output date
        DateFormat outputformat = new SimpleDateFormat("HH:mm", Locale.US);
        Date date = null;
        String output = null;
        try {
            //Conversion of input String to date
            date = df.parse(time);
            //old date format to new date format
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
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


    public static void showTastyToast(Activity activity, String message, int flag) {
        if (flag == 0) {
            TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
        } else if (flag == 1) {
            TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
        } else if (flag == 2) {
            TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        } else if (flag == 3) {
            TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.INFO);
        } else if (flag == 4) {
            TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
        } else if (flag == 5) {
            TastyToast.makeText(activity, message, TastyToast.LENGTH_SHORT, TastyToast.WARNING);
        }

    }

    public static void redirectToHome(Activity fromactivity) {
        Intent myIntent = new Intent(fromactivity, ManagingTabsActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromactivity.startActivity(myIntent);
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

    public static void setEmailFilter(EditText edt_email) {
        try {
            edt_email.setFilters(new InputFilter[]{
                    new InputFilter.AllCaps() {
                        @Override
                        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                            return String.valueOf(source).toLowerCase();
                        }
                    }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
            DisplayImgWithPlaceholderFromURL(activity, url, imgView, R.drawable.img_no_img_aval);
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


    public static void storeProductsCachingTime(Activity activity, String Product) {
        SharedPreferences.Editor editor = null;
        if (Product.equalsIgnoreCase("Product")) {
            editor = activity.getSharedPreferences(Constants.PREF_PRODUCTS_CACHING, 0).edit();
        } else if (Product.equalsIgnoreCase("Profile")) {
            editor = activity.getSharedPreferences(Constants.PREF_Profile_CACHING, 0).edit();
        } else if (Product.equalsIgnoreCase("Video")) {
            editor = activity.getSharedPreferences(Constants.PREF_Video_CACHING, 0).edit();
        }

       /* SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(valid_until);
        if (new Date().before(strDate)) {
            catalog_outdated = 1;
        }*/
        editor.putLong("offer_millis", System.currentTimeMillis()); // add this line and comment below line for cache
        editor.apply();
    }


    public static void storeCachingDate(Activity activity, String type) {
        try {
            SharedPreferences.Editor editor = null;
            if (type.equalsIgnoreCase("Profile")) {
                editor = activity.getSharedPreferences(Constants.PREF_Profile_CACHING, 0).edit();
            } else if (type.equalsIgnoreCase("Video")) {
                editor = activity.getSharedPreferences(Constants.PREF_Video_CACHING, 0).edit();
            } else if (type.equalsIgnoreCase("SCTTech")) {
                editor = activity.getSharedPreferences(Constants.PREF_SCT_CACHING, 0).edit();
            } else if (type.equalsIgnoreCase("POCT")) {
                editor = activity.getSharedPreferences(Constants.PREF_POCT_CACHING, 0).edit();
            }
            editor.putString("date", getCurrentDate());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkSync(Activity mActivity, String type) {
        SharedPreferences preferences = null;
        if (type.equalsIgnoreCase("Profile")) {
            preferences = mActivity.getSharedPreferences(Constants.PREF_Profile_CACHING, 0);
        } else if (type.equalsIgnoreCase("Video")) {
            preferences = mActivity.getSharedPreferences(Constants.PREF_Video_CACHING, 0);
        } else if (type.equalsIgnoreCase("SCTTech")) {
            preferences = mActivity.getSharedPreferences(Constants.PREF_SCT_CACHING, 0);
        } else if (type.equalsIgnoreCase("POCT")) {
            preferences = mActivity.getSharedPreferences(Constants.PREF_POCT_CACHING, 0);
        }


        if (GlobalClass.isNull(preferences.getString("date", ""))) {
            return true;
        }
        Date storedDate = returnDate(preferences.getString("date", ""));
        Date currentDate = returnDate(getCurrentDate());

        return storedDate != null && storedDate.before(currentDate);
    }


    public static Date returnDate(String putDate) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public static int Dayscnt(Context context, String Product) {
        long offer_millis = 0, current_millis = 0, differ_millis = 0;
        SharedPreferences pref_prod_caching = null;
        if (Product.equalsIgnoreCase("Product")) {
            pref_prod_caching = context.getSharedPreferences(Constants.PREF_PRODUCTS_CACHING, MODE_PRIVATE);
        } else if (Product.equalsIgnoreCase("Profile")) {
            pref_prod_caching = context.getSharedPreferences(Constants.PREF_Profile_CACHING, MODE_PRIVATE);
        } else if (Product.equalsIgnoreCase("Video")) {
            pref_prod_caching = context.getSharedPreferences(Constants.PREF_Video_CACHING, MODE_PRIVATE);
        }
        offer_millis = pref_prod_caching.getLong("offer_millis", 0);
        current_millis = System.currentTimeMillis();
        differ_millis = current_millis - offer_millis;
        int days = (int) (differ_millis / (1000 * 60 * 60 * 24));
        Log.e("TAG11", "<< DAYS >> " + days);
        return days;
    }


    public static boolean syncProduct(Context context) {
        AppPreferenceManager appPreferenceManager = new AppPreferenceManager(context);
        return appPreferenceManager.getVersionResponseModel() != null && appPreferenceManager.getVersionResponseModel().getSyncproduct() != appPreferenceManager.getSynProductCount();
    }


    public static void StoresyncProduct(Context context) {
        try {
            AppPreferenceManager appPreferenceManager = new AppPreferenceManager(context);
            appPreferenceManager.setSynProductCount(appPreferenceManager.getVersionResponseModel().getSyncproduct());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return val == null || val.equals(null) || val.trim().equals("") || val.trim().equals("null") || val.trim() == "" || val.trim() == "null";
    }

    public static boolean isArraylistNotNull(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static String getIMEINo(Activity activity) {
        String imeiNo = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imeiNo = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                if (telephonyManager.getDeviceId() != null) {
                    imeiNo = telephonyManager.getDeviceId();
                } else {
                    imeiNo = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
            if (isNull(imeiNo)) {
                imeiNo = Installation.id(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imeiNo;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarcolor(Activity activity) {
        try {
            if (GlobalClass.checkForApi21()) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(activity.getResources().getColor(R.color.limaroon));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getvalue(int currentHourIn24Format) {
        String getval = "";

        if (currentHourIn24Format <= 5) {
            getval = "00";
        } else if (currentHourIn24Format <= 10) {
            getval = "05";
        } else if (currentHourIn24Format <= 15) {
            getval = "10";
        } else if (currentHourIn24Format <= 20) {
            getval = "15";
        } else if (currentHourIn24Format <= 25) {
            getval = "20";
        } else if (currentHourIn24Format <= 30) {
            getval = "25";
        } else if (currentHourIn24Format <= 35) {
            getval = "30";
        } else if (currentHourIn24Format <= 40) {
            getval = "35";
        } else if (currentHourIn24Format <= 45) {
            getval = "40";
        } else if (currentHourIn24Format <= 50) {
            getval = "45";
        } else if (currentHourIn24Format <= 55) {
            getval = "50";
        } else if (currentHourIn24Format <= 60) {
            getval = "55";
        }


        return getval;
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

    public void DisplayVideoImage(Activity activity, String Url, ImageView imageView) {
        if (!isNull(Url)) {
            GlideUrl glideUrl = new GlideUrl(Url, new LazyHeaders.Builder()
                    .addHeader(Constants.HEADER_USER_AGENT, getHeaderValue(activity))
                    .build());
            Glide.with(activity).load(glideUrl)
                    .asBitmap()
                    .into(imageView);
        }
    }


    public static void DisplayImgWithPlaceholderFromURL(Activity activity, String Url, ImageView imageView, int userprofile) {
        try {
            Glide.get(activity).clearMemory();
            if (!isNull(Url)) {
                GlideUrl glideUrl = new GlideUrl(Url, new LazyHeaders.Builder()
                        .addHeader(Constants.HEADER_USER_AGENT, getHeaderValue(activity))
                        .build());
                Glide.with(activity).load(glideUrl)
                        .asBitmap()
                        .placeholder(userprofile).dontAnimate()
                        .error(userprofile)
                        .into(imageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DisplayImgWithPlaceholder(Activity activity, String Url, ImageView imageView, int userprofile) {
        try {

            Glide.get(activity).clearMemory();
            Glide.with(activity).load(Url)
                    .asBitmap()
                    .placeholder(userprofile).dontAnimate()
                    .error(userprofile)
                    .into(imageView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DisplayCircularImgWithPlaceholderFromURL(Activity activity, String Url, CircleImageView imageView, int userprofile) {
        try {
            Glide.get(activity).clearMemory();
            if (!isNull(Url)) {
                GlideUrl glideUrl = new GlideUrl(Url, new LazyHeaders.Builder()
                        .addHeader(Constants.HEADER_USER_AGENT, getHeaderValue(activity))
                        .build());
                Glide.with(activity).load(glideUrl)
                        .asBitmap()
                        .placeholder(userprofile).dontAnimate()
                        .error(userprofile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static int getIntVal(String val) {
        int amnt = 0;
        if (val != null) {
            if (val.equalsIgnoreCase("null")) {
                amnt = 0;
            } else {
                double d = Double.parseDouble(val);
                amnt = (int) d;
            }
        }

        return amnt;
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = null;
        if (!GlobalClass.isNull(amount)) {
            formatter = new DecimalFormat("##,##,##,###.##");
            return formatter.format(Double.parseDouble(amount));
        } else {
            amount = "0";
        }
        return formatter.format(Double.parseDouble(amount));
    }

    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    public static String getBase64Image(Bitmap bitmap) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(bitmap.getRowBytes() *
                    bitmap.getHeight());
            bitmap.copyPixelsToBuffer(buffer);
            byte[] data = buffer.array();
            return Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static void showCustomToast(Activity activity, String message, int i) {


        if (activity != null) {
            if (i == 0) {
                Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "" + message, Toast.LENGTH_LONG).show();
            }

        }

    }

    public static boolean isValidURL(String str) {
        if (Patterns.WEB_URL.matcher(str).matches()) {
            return str.startsWith("http:") || str.startsWith("https:") || str.startsWith("www.");
        }
        return false;
    }

    public static boolean CheckEqualIgnoreCase(String str1, String str2) {
        return !isNull(str1) && !isNull(str2) && str1.equalsIgnoreCase(str2);
    }

    public static int getBSBPMinMaxValue(Activity activity, String valueType) {
        int returnFlag = 0;
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String json = appSharedPrefs.getString("MyObject", "");
        MainModel obj = new Gson().fromJson(json, MainModel.class);
        if (CheckEqualIgnoreCase(valueType, Constants.BS_MIN)) {
            returnFlag = obj != null ? obj.getBsmin() : 0;
        } else if (CheckEqualIgnoreCase(valueType, Constants.BS_MAX)) {
            returnFlag = obj != null ? obj.getBsmax() : 0;
        } else if (CheckEqualIgnoreCase(valueType, Constants.BP_MIN)) {
            returnFlag = obj != null ? obj.getBpmin() : 0;
        } else if (CheckEqualIgnoreCase(valueType, Constants.BP_MAX)) {
            returnFlag = obj != null ? obj.getBpmax() : 0;
        }
        return returnFlag;
    }

    public static String getBSBPValidationMsg(Activity activity, String valueType) {
        String returnFlag = "";
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String json = appSharedPrefs.getString("MyObject", "");
        MainModel obj = new Gson().fromJson(json, MainModel.class);
        if (CheckEqualIgnoreCase(valueType, Constants.BS_MSG)) {
            returnFlag = obj != null && !isNull(obj.getBsmsg()) ? obj.getBsmsg() : MessageConstants.ABSURD_VALUE;
        } else if (CheckEqualIgnoreCase(valueType, Constants.BP_MSG)) {
            returnFlag = obj != null && !isNull(obj.getBpmsg()) ? obj.getBpmsg() : MessageConstants.ABSURD_VALUE;
        }
        return returnFlag;
    }

    public static void cropImageActivity(Activity activity, int flag) {
        if (flag == 1) {
            ImagePicker.Companion.with(activity).crop(3f, 2f).compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        } else if (flag == 0) {
            ImagePicker.Companion.with(activity).crop(3f, 2f).cameraOnly().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        } else if (flag == 2) {
            ImagePicker.Companion.with(activity).crop(3f, 2f).galleryOnly().compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        }
    }

    public static void cropImageFragment(Fragment fragment, int flag) {

        if (flag == 1) {
            ImagePicker.Companion.with(fragment).crop(3f, 2f).compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).start();
        } else if (flag == 0) {
            ImagePicker.Companion.with(fragment).crop(3f, 2f).compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).cameraOnly().start();
        } else if (flag == 2) {
            ImagePicker.Companion.with(fragment).crop(3f, 2f).compress(Constants.MaxImageSize)
                    .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight).galleryOnly().start();
        }
    }
}
