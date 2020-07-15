package com.example.e5322.thyrosoft.API;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.StartCheckoutEvent;
import com.example.e5322.thyrosoft.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import static com.example.e5322.thyrosoft.API.Api.BASE_URL_TOCHECK;
import static com.example.e5322.thyrosoft.API.Constants.MAINURL;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_AMOUNT;
import static com.example.e5322.thyrosoft.API.Constants.PAYUMONEYKEY_PRODUCT;

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
    public static String BASE_URL = MAINURL;
    public static String SERVER_BASE_API_URL_PROD = BASE_URL.equals(BASE_URL_TOCHECK) ? "http://techso.thyrocare.cloud/techsoapi" : "http://techsostng.thyrocare.cloud/techsoapi";
    public static String B2B="";
    public static String B2C="";
    private Context context;


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

    public void StartCheckout_EVENTLOGGING(JSONObject jobj, int count, String Paytype) {
        System.out.println("Logging Checkout event in Fabrics!!");
        try {
            Answers.getInstance().logStartCheckout(new StartCheckoutEvent()
                    .putTotalPrice(BigDecimal.valueOf(Double.parseDouble(jobj.getString("rate"))))
                    .putCurrency(Currency.getInstance("INR"))
                    .putItemCount(count)
                    .putCustomAttribute("PayType", Paytype)
                    .putCustomAttribute("Products", jobj.getString("product")));
        } catch (JSONException e) {
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

    public void StartCheckout_EVENTLOGGING1(JSONObject jobj, int count, String Paytype, String Gateway) {
        System.out.println("Logging Checkout event in Fabrics!!");
        try {
            Answers.getInstance().logStartCheckout(new StartCheckoutEvent()
                    .putTotalPrice(BigDecimal.valueOf(Double.parseDouble(jobj.getString(PAYUMONEYKEY_AMOUNT))))
                    .putCurrency(Currency.getInstance("INR"))
                    .putItemCount(count)
                    .putCustomAttribute("PayType", Paytype)
                    .putCustomAttribute("Gateway", Gateway)
                    .putCustomAttribute("Products", jobj.getString(PAYUMONEYKEY_PRODUCT)));
        } catch (JSONException e) {
            e.printStackTrace();
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