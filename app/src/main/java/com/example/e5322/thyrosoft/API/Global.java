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
import com.example.e5322.thyrosoft.GpsTracker;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.Models.MenusModel;
import com.example.e5322.thyrosoft.Models.RequestModels.LatLongDataModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ScannedBarcodeDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by e5426@thyrocare.com on 12/1/18.
 */

public class Global {

    public static boolean ToShowRewards = true;
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

    public static String Communication = "";
    public static String CommModes = "";

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
        TextView tv_missed_call = bottomSheet.findViewById(R.id.tv_missed_call);
        tv_sms.setText("" + usercode + " to 9870666333");
        tv_missed_call.setText("9870666333");
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

    public static LatLongDataModel getCurrentLatLong(Activity mActivity) {
        LatLongDataModel latLong = new LatLongDataModel();
        String lat = "0.0", longi = "0.0";
        try {
            GpsTracker gpsTracker = new GpsTracker(mActivity);
            if (gpsTracker.canGetLocation()) {
//                lat = new DecimalFormat("####.##########").format(gpsTracker.getLatitude());
                lat = String.valueOf(gpsTracker.getLatitude());
//                longi = new DecimalFormat("####.##########").format(gpsTracker.getLongitude());
                longi = String.valueOf(gpsTracker.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        latLong.setmLatitude(lat);
        latLong.setmLongitude(longi);
        return latLong;
    }

    /*public static String getIPAddress(Activity activity) {
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        return wm != null && wm.getConnectionInfo() != null ? Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()) : "";
    }*/

    /*public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }*/


    public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String randomBarcodeString(int length) {

        char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

    public static boolean testsNotAllowedBelow18(String code, String age) {
        return Integer.parseInt(age) < 18 && GlobalClass.checkEqualIgnoreCase(code, Constants.TEST_CODE_EGFR);
    }

    public static int getLoginType(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        return GlobalClass.checkEqualIgnoreCase(preferences.getString("SOURCE_TYPE", ""), "PE DAC") ? Constants.PEflag : Constants.TCflag;
    }

    public static String getAccessType(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        return preferences.getString("ACCESS_TYPE", "");
    }

    public static boolean isStaff(Activity activity) {
        return GlobalClass.checkEqualIgnoreCase(getAccessType(activity), "STAFF");
    }

    public static ArrayList<MenusModel> getMenusList() {
        ArrayList<MenusModel> arrayList = new ArrayList<>();

        MenusModel model;
        model = new MenusModel();
        model.setMenuName("LEDGER");
        model.setMenuIcon(R.drawable.ledger_d);
        model.setMenuPosition(Constants.LEDGER_MENU_POS);//7
        arrayList.add(model);

        model = new MenusModel();
        model.setMenuName("RECHARGE");
        model.setMenuIcon(R.drawable.ic_rupees);
        model.setMenuPosition(Constants.RECHARGE_MENU_POS);//101
        arrayList.add(model);

        model = new MenusModel();
        model.setMenuName("RESULT");
        model.setMenuIcon(R.drawable.result);
        model.setMenuPosition(Constants.RESULT_MENU_POS);//5
        arrayList.add(model);

        model = new MenusModel();
        model.setMenuName("RATE CALC");
        model.setMenuIcon(R.drawable.calculator_d);
        model.setMenuPosition(Constants.RATE_CAL_MENU_POS);//3
        arrayList.add(model);

        model = new MenusModel();
        model.setMenuName("NOVID WOE");
        model.setMenuIcon(R.drawable.woe_d);
        model.setMenuPosition(Constants.NOVID_WOE_MENU_POS);//2
        arrayList.add(model);


        model = new MenusModel();
        model.setMenuName("VIEW ALL");
        model.setMenuIcon(R.drawable.ic_viewall);
        model.setMenuPosition(Constants.NOVID_WOE_MENU_POS);//2
        arrayList.add(model);


        /*model = new MenusModel();
        model.setMenuName("WIND UP");
        model.setMenuIcon(R.drawable.wind_up_data);
        model.setMenuPosition(Constants.WINDUP_MENU_POS);//8
        arrayList.add(model);

        model = new MenusModel();
        model.setMenuName("RECEIPT");
        model.setMenuIcon(R.drawable.ic_rupees);
        model.setMenuPosition(Constants.RECEIPT_MENU_POS);//4
        arrayList.add(model);

        model = new MenusModel();
        model.setMenuName("COVID WOE");
        model.setMenuIcon(R.drawable.woe_d);
        model.setMenuPosition(Constants.COVID_WOE_MENU_POS);//0
        arrayList.add(model);*/

        return arrayList;
    }

    public static String bannersToDisplay(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        String loginType = String.valueOf(getLoginType(activity));
        String sourceType = preferences.getString("SOURCE_TYPE", "");
        String bannerHtml = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Cliso App</title>\n" +
                "    <style>\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body style=\"margin:0;\" >\n" +
                "    <div style=\"width: 100%;\">\n" +
                "    <iframe frameBorder = \"0\"\n" +
                "      id=\"bannerIFrame\" \n" +
                "      class=\"bannerIframeClass\"\n" +
                "      scrolling=\"no\"\n" +
                "      style=\"display: none; width: 100%;\" \n" +
                "      src=\"https://newcliso.thyrocare.com/banner\"\n" +
                "      allowfullscreen\n" +
                "      >\n" +
                "    </iframe>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "      // addEventListener with support for IE8\n" +
                "      function bindEvent(element, eventName, eventHandler) {\n" +
                "          if (element.addEventListener){\n" +
                "              element.addEventListener(eventName, eventHandler, false);\n" +
                "          } else if (element.attachEvent) {\n" +
                "              element.attachEvent('on' + eventName, eventHandler);\n" +
                "          }\n" +
                "        }\n" +
                "      \n" +
                "      // Selecting the iframe element\n" +
                "      var iframeEl = document.getElementById(\"bannerIFrame\");\n" +
                "      window.onload = function () {\n" +
                "        iframeEl.contentWindow.postMessage({type: 'bannerConditions', payload: {\n" +
                "        \"tenant_id\": \""+loginType+"\",\n" +
                "        \"citi_name\": \"Bengaluru\",\n" +
                "        \"category\": \""+sourceType+"\",\n" +
                "        \"banner_location_id\": \"CLISOAPP\",\n" +
                "        \"device\": \"MOBILE\"\n" +
                "    }, ruleName: \"dx_franchise_cliso_banners\"},\"*\");\n" +
                "      }\n" +
                "      \n" +
                "      bindEvent(window, 'message', function (e) {\n" +
                "        console.log(e);\n" +
                "            if('type' in e?.data){\n" +
                "              switch(e.data.type) {\n" +
                "                case 'no_banners_found':\n" +
                "                  console.log('removing iframe');\n" +
                "                  iframeEl.parentNode.removeChild(iframeEl);\n" +
                "                  break;\n" +
                "                case 'banners_loaded':\n" +
                "                  console.log('banners loaded', e.data);\n" +
                "                  iframeEl.scrollWidth = 0;\n" +
                "                  iframeEl.style.scrollWidth = 0;\n" +
                "                  setTimeout(()=> {\n" +
                "                    let width = iframeEl.clientWidth;\n" +
                "                     iframeEl.style.height = `${(width * parseInt(e.data.aspect_ratio))/100 }px`;},\n" +
                "                    // harcoded for now, correction needed in db\n" +
                "                   // iframeEl.style.height = `${(width * 43.21)/100 }px`;}, \n" +
                "                  100);\n" +
                "                  iframeEl.style.display = 'block';\n" +
                "                default:\n" +
                "                  break;\n" +
                "              }\n" +
                "            }\n" +
                "        });\n" +
                "      </script>\n" +
                "  </body>\n" +
                "  </html>";
        return bannerHtml;
    }


}