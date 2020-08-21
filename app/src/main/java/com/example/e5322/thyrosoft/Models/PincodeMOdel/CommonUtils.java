package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Base64;
import android.util.TypedValue;
import android.widget.Toast;

import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class CommonUtils {

    /* public static ApiResponseModel getErrorReponseModel(String msg) {
         Gson gson = new Gson();
         MessageModel errorModel = new MessageModel();

         errorModel.setStatus("ERROR-BUSSINESS");
         String messages = msg;
         errorModel.setError_description(messages);

         ApiResponseModel apiResponseModel = new ApiResponseModel();
         apiResponseModel.setResponseData(gson.toJson(errorModel));
         apiResponseModel.setStatusCode(-888);

         return apiResponseModel;
       }*/
    public static int lastvisibleItemsave = 0;

    public static int ProgressVisible = 0;
    public static String Pre_Date_Filter_from = "";
    public static String Pre__Date_Filter_tO = "";
    public static String Post_Date_Filter_frOm = "";
    public static String Post_Date_Filter_tO = "";

    public static String from_date_BTS = "";
    public static String to_date_BTS = "";


    private static CommonUtils instance = null;
    public static String DD_MM_YYYY = "";
    public static int LocationNotFound = 0;
    private MessageModel messageModel;

    protected CommonUtils() {
        // Exists only to defeat instantiation.
    }

    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeImage(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static byte[] decodedImageBytes(String encodedImage) {
        return Base64.decode(encodedImage, Base64.DEFAULT);
    }

    public static float dpTopx(float dp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public static float getPxFromDp(float dp, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public static File createDirectory(Context activity) {
        File directoryFile = new File(activity.getFilesDir().getAbsolutePath());
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        return directoryFile;
    }

    public String getErrorJson(String msg) {
        Gson gson = new Gson();
        MessageModel errorModel = new MessageModel();

        messageModel = new MessageModel();
        MessageModel.FieldError f = new MessageModel.FieldError();
        f.setField("InterNet");
        f.setMessage(msg);

        MessageModel.FieldError[] messages = new MessageModel.FieldError[]{f};

        errorModel.setType("ERROR");
        errorModel.setStatusCode(400);
        errorModel.setMessages(messages);


        return gson.toJson(errorModel);
    }

    public void openAppOnMarket(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static String getAppVersion(Activity activity) {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap watermarkImage(Bitmap image, String[] lines) {
        Bitmap.Config config = image.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }

        Bitmap newBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        Canvas mCanvas = new Canvas(newBitmap);
        mCanvas.drawBitmap(image, 0, 0, null);

        Paint mPaint = new Paint();

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.parseColor("#FF444444"));
        paintText.setTextSize((float) (image.getWidth() * 0.03));
        paintText.setStyle(Paint.Style.FILL);

//		int xCo = (int) (image.getWidth() * 0.70);
        int xCo = 5;
        int yCo = 5;

        for (int index = 0; index < lines.length; index++) {
            String currentLine = lines[index];
            Rect rectText = new Rect();
            paintText.getTextBounds(currentLine, 0, currentLine.length(), rectText);
            yCo = yCo + rectText.height() + 5;
            if (index == 2) {
                yCo = yCo + 5;
            }
            mCanvas.drawText(currentLine, xCo, yCo, paintText);
        }

        return newBitmap;

    }

    public static int ReturnColor() {

        if (BundleConstants.ColorStat == 0) {
            return R.color.colorWhite;
        } else if (BundleConstants.ColorStat == 1) {

        }

        return 0;
    }





    private static int ToastLength(boolean b) {
        if (b) {
            return Toast.LENGTH_LONG;
        } else {
            return Toast.LENGTH_SHORT;
        }
    }

    public static String SetDate(String Date) {
        String[] parts = Date.split("-");
        String first = parts[0];
        String second = parts[1];
        String third = parts[2];

        if (first.length() > 2) {

            DD_MM_YYYY = third + "-" + second + "-" + first;
        } else {
            DD_MM_YYYY = first + "-" + second + "-" + third;
        }
        return DD_MM_YYYY;
    }

    public static String SetDateyearwiseBTS(String Date) {  //03-09-2017 --> 2017-08-10

        String[] parts = Date.split("-");
        String zero = parts[0];
        String one = parts[1];
        String two = parts[2];

        if (zero.length() > 2) {
            DD_MM_YYYY = zero + "-" + one + "-" + two;

        } else {
            DD_MM_YYYY = two + "-" + one + "-" + zero;
        }
        return DD_MM_YYYY;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir,context);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir, Context context) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]), context);
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}