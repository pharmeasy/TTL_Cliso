package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.VerticalTextView;
import com.example.e5322.thyrosoft.Controller.HandbillAsyctask;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.HandbillRequest;
import com.example.e5322.thyrosoft.R;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class Previewhandbill_Activity extends AppCompatActivity implements View.OnClickListener {

    String str_name, str_mob, str_email, str_Addr, str_pincode, str_img, fromcome;
    ImageView img_back;
    PhotoView img_template;
    RelativeLayout lin_preview;
    LinearLayout rel_generate;
    Activity activity;
    TextView toolbarname, txt_name1, txt_mobileno, txt_email, txt_address, txt_avail;
    Button btn_generate, btn_download, btn_share, btn_delete;
    Bitmap myBitmap;
    private int mYear, mMonth, mDay;
    private static File f = null;
    private static String TAG = Previewhandbill_Activity.class.getSimpleName();
    private HandbillRequest handbillRequest;
    ConnectionDetector cd;
    String img_Id = "";
    SharedPreferences sharedPreferences;
    String enterby;
    GlobalClass globalClass;
    boolean isfrom = false;
    Calendar c;
    VerticalTextView txt_date;
    File fileaftergenerate = null;
    ImageView back, home;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalClass = new GlobalClass(Previewhandbill_Activity.this);
        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.maroon));
        }
        setContentView(R.layout.lay_previewhandbill);

        initilazation();
        initview();

        if (!GlobalClass.isNull(fromcome) && fromcome.equalsIgnoreCase("adapter")) {
            toolbarname.setText("Share & Download");
            btn_generate.setVisibility(View.GONE);
            btn_download.setVisibility(View.VISIBLE);
            btn_share.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);

            txt_name1.setVisibility(View.GONE);
            txt_mobileno.setVisibility(View.GONE);
            txt_email.setVisibility(View.GONE);
            txt_address.setVisibility(View.GONE);
            txt_avail.setVisibility(View.GONE);
            txt_date.setVisibility(View.GONE);
        }

    }

    private void initilazation() {
        Bundle bundle = getIntent().getExtras();
        activity = Previewhandbill_Activity.this;
        cd = new ConnectionDetector(activity);

        sharedPreferences = getSharedPreferences("Userdetails", MODE_PRIVATE);
        enterby = sharedPreferences.getString("mobile_user", "");

        if (bundle != null) {
            str_img = bundle.getString(Constants.IMAG_URL, "");
            str_name = bundle.getString(Constants.B_NAME, "");
            str_mob = bundle.getString(Constants.B_MOB, "");
            str_email = bundle.getString(Constants.B_EMAIL, "");
            str_Addr = bundle.getString(Constants.B_ADDR, "");
            str_pincode = bundle.getString(Constants.B_PINCODE, "");
            fromcome = bundle.getString(Constants.FROMCOME, "");
            img_Id = bundle.getString(Constants.IMGID, "");
        }
    }

    private void initview() {

        toolbarname = findViewById(R.id.txt_name);
        toolbarname.setText("Preview");

        txt_date = findViewById(R.id.txt_date);

        txt_date.setText(Currentdate() + " - " + enterby);


        txt_avail = findViewById(R.id.txt_avail);

        btn_download = findViewById(R.id.btn_download);
        btn_share = findViewById(R.id.btn_share);

        btn_download.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        img_template = findViewById(R.id.img);
        img_template.setZoomable(true);

        lin_preview = findViewById(R.id.lin_preview);

        rel_generate = findViewById(R.id.rel_generate);

        btn_generate = findViewById(R.id.btn_generate);
        btn_generate.setOnClickListener(this);

        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);


        txt_name1 = findViewById(R.id.txt_name1);
        txt_mobileno = findViewById(R.id.txt_mobileno);
        txt_email = findViewById(R.id.txt_email);
        txt_address = findViewById(R.id.txt_address);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);

        back.setOnClickListener(this);
        home.setOnClickListener(this);


        if (!GlobalClass.isNull(str_name)) {
            String output = str_name.substring(0, 1).toUpperCase() + str_name.substring(1);
            txt_name1.setText(output.trim());
        }

        if (!GlobalClass.isNull(str_mob)) {
            txt_mobileno.setText(str_mob.trim());
        }

        if (!GlobalClass.isNull(str_email)) {
            txt_email.setText(str_email.trim());
        }

        if (!GlobalClass.isNull(str_Addr) && !GlobalClass.isNull(str_pincode)) {
            txt_address.setVisibility(View.VISIBLE);
            txt_address.setText(str_Addr.trim() + "-" + str_pincode.trim());
        } else {
            if (!GlobalClass.isNull(str_Addr)) {
                txt_address.setText(str_Addr.trim());
            } else if (!GlobalClass.isNull(str_pincode)) {
                txt_address.setText(str_pincode.trim());
            } else {
                txt_address.setVisibility(View.GONE);
            }
        }


        if (!GlobalClass.isNull(str_img)) {

            if (fromcome.equalsIgnoreCase("form")) {
                img_template.setClickable(false);
                img_template.setEnabled(false);
            } else {
                img_template.setClickable(true);
                img_template.setEnabled(true);
            }

            GlobalClass.DisplayImgWithPlaceholderFromURL(activity, str_img.replace("\\", "/"), img_template, R.drawable.default_img);
        }
    }


    public static Bitmap captureScreen(View v) {
        Bitmap screenshot = null;
        try {
            if (v != null) {
                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }
        } catch (Exception e) {
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }
        return screenshot;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_download:

                if (img_template.isZoomable()) {
                    img_template.setZoomable(false);
                }

                myBitmap = captureScreen(lin_preview);
                try {
                    if (myBitmap != null) {
                        saveImage(myBitmap, str_mob);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GlobalClass.showTastyToast(activity, MessageConstants.FILE_DOWN_SUCCSS,1);
                img_template.setZoomable(true);
                break;

            case R.id.back:
                finish();
                break;

            case R.id.home:
                GlobalClass.redirectToHome(activity);
                break;

            case R.id.btn_share:

                if (img_template.isZoomable()) {
                    img_template.setZoomable(false);
                }
                myBitmap = captureScreen(lin_preview);
                try {

                    if (myBitmap != null) {
                        saveImage(myBitmap, str_mob);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                shareImage(f);

                break;

            case R.id.btn_delete:
                try {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity).create();
                    alertDialog.setMessage("Are you sure you want to delete this item?");
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }

                            });

                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (fromcome.equalsIgnoreCase("adapter")) {
                                myBitmap = captureScreen(img_template);

                                try {
                                    if (myBitmap != null) {
                                        //save image to SD card
                                        saveImage(myBitmap, str_mob);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (f != null) {
                                    postgerateImage();
                                }
                                dialog.dismiss();

                            }
                        }
                    });

                    if (!activity.isFinishing()) {
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btn_generate:
                myBitmap = captureScreen(lin_preview);

                if (myBitmap != null) {
                    //save image to SD card
                    saveImage(myBitmap, str_mob);
                }
                if (f != null) {
                    postgerateImage();
                }
                break;
        }
    }

    private void postgerateImage() {

        if (cd.isConnectingToInternet()) {

            if (f != null) {
                fileaftergenerate = f;
            }

            handbillRequest = new HandbillRequest();
            handbillRequest.setName(str_name.trim());
            handbillRequest.setAddress(str_Addr.trim());
            handbillRequest.setPincode(str_pincode.trim());
            handbillRequest.setMobile(str_mob.trim());
            handbillRequest.setEmail(str_email.trim());
            handbillRequest.setEnteredby(enterby.trim());
            handbillRequest.setImgStatus(1);

            if (img_Id.equalsIgnoreCase("")) {
                handbillRequest.setImgid("");
                handbillRequest.setAction("");
                handbillRequest.setFiles(f);

            } else {
                handbillRequest.setImgid(img_Id);
                handbillRequest.setAction("DELETE");
                handbillRequest.setFiles(f);
            }

            new HandbillAsyctask(Previewhandbill_Activity.this, activity, handbillRequest).execute();

        } else {
            GlobalClass.showTastyToast(activity, MessageConstants.CHECK_INTERNET_CONN,2);
        }

    }

    private void shareImage(File file) {
        Uri uri;
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".imageprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }

        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            GlobalClass.showTastyToast(activity, MessageConstants.APP_NOT_AVAILABLE, 2);
        }

    }

    public static void saveImage(Bitmap bitmap, String str_mob) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
            f = new File(Environment.getExternalStorageDirectory() + File.separator + str_mob + ".png");
            if (!f.exists())
                f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                GlobalClass.showTastyToast(activity, RESPONSE,1);

                btn_generate.setVisibility(View.GONE);
                toolbarname.setText("Share & Download");


                if (f != null) {
                    img_template.setEnabled(true);
                    img_template.setClickable(true);
                    img_template.setImageResource(0);

                    txt_name1.setVisibility(View.GONE);
                    txt_mobileno.setVisibility(View.GONE);
                    txt_email.setVisibility(View.GONE);
                    txt_address.setVisibility(View.GONE);
                    txt_avail.setVisibility(View.GONE);
                    txt_date.setVisibility(View.GONE);

                    str_img = f.toString();

                    GlobalClass.DisplayImgWithPlaceholderFromURL(activity, str_img.replace("\\", "/"), img_template, R.drawable.default_img);
                }

                if (!GlobalClass.isNull(fromcome) && fromcome.equalsIgnoreCase("adapter")) {
                    btn_download.setVisibility(View.VISIBLE);
                    btn_share.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(activity, ViewGenhandbill_Activity.class);
                   // intent.putExtra("goto", 36);
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    btn_generate.setVisibility(View.GONE);
                    btn_download.setVisibility(View.VISIBLE);
                    btn_share.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.GONE);
                }

                Log.e(TAG, "Image uploaded SuccessFully --->");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String Currentdate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(activity, ViewGenhandbill_Activity.class);
        startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (img_template.getVisibility() == View.VISIBLE) {
            img_template.setZoomable(true);
        }
    }
}
