package com.example.e5322.thyrosoft.API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by e5426@thyrocare.com on 12/1/18.
 */

public class Global {

    public static int OTP = 0;
    public static String type = "";
    public static String Username = "";
    public static int BatchCount;
    public static String BatchMocde;
    public static int Amounttotal;
    public static double TotalItemsCount;
    public static double TotalPaymentamount = 0;
    public static double addTtoal = 0;
    public static double TotalWeight = 0;
    public static Matrix matrrix;
    public static int InDetailsFlag = 0;
    public static int Serachnow = 0;
    public static String mobile = "";
    public static String newOtp = "";
    public static int detailsscreen = 0;
    public static int searchgetcontext = 0;
    public static String UserName_Profile = "";
    public static int mainActivity = 0;
    public static String B2B = "";
    public static String B2C = "";
    public static String sampletype;
    public static String test;
    public static ArrayList<Outlabdetails_OutLab> Selcted_Outlab_Test_global = new ArrayList<>();
    public static ArrayList<ScannedBarcodeDetails> FinalBarcodeDetailsList_global = new ArrayList<>();
    public static boolean OTPVERIFIED = false;
    public static boolean showratedialog = false;

    private Context context;
    public static String HHHTest = "";
    public static boolean isoffline = false;
    public static boolean isKYC = false;


    public static ArrayList<String> tabname_home = new ArrayList<>();


    ProgressDialog progressDialog;

    public Global(Context context) {
        this.context = context;
    }

    public void hideProgressDialog() {

        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void SetBottomSheet(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        String usercode = preferences.getString("USER_CODE", "");

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomSheet = LayoutInflater.from(activity).inflate(R.layout.custom_missed_call_verification_dialog, (ViewGroup) activity.findViewById(R.id.bottom_sheet_dialog_parent));
        TextView tv_sms = bottomSheet.findViewById(R.id.tv_sms);
        tv_sms.setText("" + usercode + " to 9870666333");
        bottomSheet.findViewById(R.id.cross_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public static boolean checkHardcodeTest(String testCode) {
        /*|| testCode.equalsIgnoreCase(Constants.CATC) || testCode.equalsIgnoreCase(Constants.CAGE) || testCode.equalsIgnoreCase(Constants.CAGCA)*/
        return !GlobalClass.isNull(testCode) && testCode.equalsIgnoreCase(Constants.WC2020) || testCode.equalsIgnoreCase(Constants.CAGCA)
                || testCode.equalsIgnoreCase(Constants.CATC) || testCode.equalsIgnoreCase(Constants.CAGE) || testCode.equalsIgnoreCase(Constants.AA_C);
    }

    public static boolean checkCovidTest(int flag) {
        return flag == 1;
    }


    public static void setTextview(TextView textview, String msg) {
        try {
            if (GlobalClass.isNull(msg)) {
                msg = "";
            }
            if (textview != null) {
                textview.setText(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public void setLoadingGIF(Activity activity) {
		/*InputStream stream = null;
		try {
			stream = activity.getAssets().open("thyrocare_gif.gif");
		} catch (IOException e) {
			e.printStackTrace();
		}
		GifWebView gifWebView = new GifWebView(activity, "file:///android_asset/thyrocare_gif.gif");
		gifWebView.setBackgroundColor(Color.TRANSPARENT);

		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.loading_dialog, null);
		LinearLayout linLoading = (LinearLayout) view.findViewById(R.id.linLoading);

        linLoading.removeAllViews();
        linLoading.addView(gifWebView);

		progressDialog = new ProgressDialog(activity);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setContentView(view);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		progressDialog.setCanceledOnTouchOutside(false);*/

//        progressDialog = ProgressDialog.show(activity, null, activity.getResources().getString(R.string.loading), false);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(null);
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e52d2e")));
    }

    public String getBtsSchema() {
        /*if (BuildConfig.DEBUG) {
			return "http://bts.dxscloud.com";
		} else {
			// for release
			return "https://www.dxscloud.com";
		}*/
        return "https://www.dxscloud.com";
    }

    public String convertNumberToPrice(String s) {
        Double price = Double.parseDouble(s);
        Locale locale = new Locale("en", "IN");
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        if (checkForApi14())
            symbols.setCurrencySymbol("\u20B9"); // Don't use null.
        else
            symbols.setCurrencySymbol("\u20A8"); // Don't use null.
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMaximumFractionDigits(0);
        //System.out.println(formatter.format(price));
        s = formatter.format(price);
        return s;
    }

    public Boolean checkForApi11() {
        Boolean boolStatus = false;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
    }

    public Boolean checkForApi14() {
        Boolean boolStatus = false;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            boolStatus = true;
        } else {
            boolStatus = false;
        }
        return boolStatus;
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

    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing())

            if (!((Activity) context).isFinishing()) {
                progressDialog.show();
            }
    }


    private class ScaleListener extends ScaleGestureDetector.
            SimpleOnScaleGestureListener {
        private Matrix matrix = new Matrix();

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            matrix.setScale(scaleFactor, scaleFactor);

            return true;
        }
    }
}