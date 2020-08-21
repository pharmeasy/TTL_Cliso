package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Models.TestBookingResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mindorks.paracamera.Camera;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class LeadGenerationActivity extends AppCompatActivity {

    EditText edt_name, edt_mobile, edt_mail, edt_address, edt_pincode, edt_remarks;
    Spinner spr_package;
    Button btn_submit;
    private ArrayList<LeadResponseModel.ProductlistBean> leadResponseModel;
    TextView tv_reset;
    LeadDataResponseModel leadDataResponseModel;
    private MediaRecorder mediaRecorder;
    String TAG = getClass().getSimpleName();
    private int status_code = 0;
    String Product_id, tests;
    int Rate, is_booking, pass_rate = 0;
    private ConnectionDetector cd;
    private TestBookingResponseModel testBookingResponseModel;
    private Activity mActivity;
    private String name, mobile, email, address, userChoosenTask, remarks;
    ImageView back, home;
    private String type = "TEST";
    private String user, api_key;
    private RelativeLayout rel_upload_img, rel_upload_voice;
    public static final int RequestPermissionCode = 1;
    private File f_AudioSavePathInDevice;
    CustomDialogClass customDialogClass;
    private ImageView img_tick, img_tick2;
    private String AudioSavePathInDevice = null;
    private String lead_id = "";
    private File imagefile;
    Camera camera;
    private Gson gson;
    private Dialog CustomDialogforSuccess;
    SharedPreferences prefs_user;
    private String ShareModeType = "";
    private int width, height;
    boolean mediarecorderflag = false;
    String nameWithProperSpacing;


    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_generation);

        mActivity = LeadGenerationActivity.this;
        cd = new ConnectionDetector(mActivity);
        prefs_user = mActivity.getSharedPreferences("Userdetails", 0);

        init();
        initlistner();


    }


    @Override
    protected void onResume() {
        super.onResume();
        lead_id = GlobalClass.generateBookingID();
    }

    private void initlistner() {

        rel_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessRuntimePermissions.checkcameraPermission(mActivity)) {
                    selectImage();
                } else {
                    AccessRuntimePermissions.requestCamerapermission(mActivity);
                }
            }
        });

        rel_upload_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customDialogClass != null) {
                    customDialogClass = null;
                }
                customDialogClass = new CustomDialogClass(mActivity);
                customDialogClass.show();
                customDialogClass.setCancelable(false);
                customDialogClass.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearfileds();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(LeadGenerationActivity.this);
            }
        });


        spr_package.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    if (i != 0) {
                        if (GlobalClass.CheckArrayList(leadResponseModel)) {
                            Product_id = leadResponseModel.get(i).getProduct_id();
                            tests = leadResponseModel.get(i).getTests();
                            is_booking = leadResponseModel.get(i).getIs_booking();
                            Rate = leadResponseModel.get(i).getRate();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if (Validate()) {
                        if (edt_mail.getText().toString().length() > 0) {
                            if (emailValidation()) {
                                nameWithProperSpacing = edt_name.getText().toString().toUpperCase().trim();
                                name = nameWithProperSpacing.replaceAll("\\s+", " ");

                                mobile = edt_mobile.getText().toString();
                                email = edt_mail.getText().toString();
                                address = edt_address.getText().toString();
                                remarks = edt_remarks.getText().toString().trim();

                                if (checkRemarksValidation()) {
                                    if (cd.isConnectingToInternet()) {
                                        new AsyncTaskPost_uploadfile(name, mobile, email, address, remarks, type, imagefile, f_AudioSavePathInDevice).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
                                    }
                                }
                            }
                        } else {
                            if (checkRemarksValidation()) {
                                if (cd.isConnectingToInternet()) {
                                    nameWithProperSpacing = edt_name.getText().toString().toUpperCase().trim();
                                    name = nameWithProperSpacing.replaceAll("\\s+", " ");

                                    mobile = edt_mobile.getText().toString();
                                    email = edt_mail.getText().toString();
                                    address = edt_address.getText().toString();
                                    remarks = edt_remarks.getText().toString().trim();
                                    new AsyncTaskPost_uploadfile(name, mobile, email, address, remarks, type, imagefile, f_AudioSavePathInDevice).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
                                }
                            }
                        }
                    }
                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
                }


            }
        });
    }

    private boolean checkRemarksValidation() {
        if (edt_remarks.getText().toString().startsWith(",")) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPECIAL_CHAR, 2);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(".")) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPECIAL_CHAR, 2);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith("-")) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPECIAL_CHAR, 2);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(" ")) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPACE, 2);
            edt_remarks.requestFocus();
            return false;
        }
        return true;
    }

    private boolean emailValidation() {
        if (edt_mail.getText().toString().startsWith(" ")) {
            GlobalClass.showTastyToast(mActivity, MessageConstants.EMAIL_SHOULD_NOT_START_SPACE, 2);
            edt_mail.requestFocus();
            return false;
        }
        return true;
    }

    private void clearfileds() {
        edt_name.requestFocus();

        GlobalClass.SetEditText(edt_name, "");
        GlobalClass.SetEditText(edt_mobile, "");
        GlobalClass.SetEditText(edt_mail, "");
        GlobalClass.SetEditText(edt_address, "");
        GlobalClass.SetEditText(edt_pincode, "");
        GlobalClass.SetEditText(edt_remarks, "");


        img_tick.setVisibility(View.INVISIBLE);
        img_tick2.setVisibility(View.INVISIBLE);

        if (imagefile != null) {
            if (imagefile.exists()) {
                imagefile.delete();
            }
            imagefile = null;
        }

        if (f_AudioSavePathInDevice != null) {
            if (f_AudioSavePathInDevice.exists()) {
                f_AudioSavePathInDevice.delete();
            }
            f_AudioSavePathInDevice = null;
        }
    }

    private void selectImage() {
        CharSequence[] items = new CharSequence[0];
        if (imagefile != null) {
            if (imagefile.exists()) {
                items = new CharSequence[]{"Discard Previous Image", "Take Photo", "Cancel"};
            }
        } else {
            items = new CharSequence[]{"Take Photo", "Cancel"};
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(LeadGenerationActivity.this);
        builder.setTitle("Upload Prescription !");
        final CharSequence[] finalItems = items;

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (finalItems[item].equals("Discard Previous Image")) {
                    if (imagefile != null) {
                        if (imagefile.exists()) {
                            imagefile.delete();
                        }
                        imagefile = null;
                        img_tick.setVisibility(View.INVISIBLE);
                    }
                } else if (finalItems[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (finalItems[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                GlobalClass.printLog(Constants.ERROR, TAG, "Image path:-", "" + imagefile);
            }
        });

        builder.show();
    }


    private void cameraIntent() {

        buildCamera();
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private boolean Validate() {
        if (edt_name.getText().toString().equals("")) {
            edt_name.setError(MessageConstants.enter_valid_name);
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().length() < 2) {
            edt_name.setError(MessageConstants.min_length_souuld_be_2);
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().startsWith(" ")) {
            edt_name.setError(MessageConstants.NAME_SHOULD_NOT_START_SPACE);
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().contains("  ")) {
            edt_name.setError(MessageConstants.CANNOT_DOUBLE_SPACE);
            edt_name.requestFocus();
            return false;
        }

        if (edt_mobile.getText().length() != 10) {
            edt_mobile.setError(MessageConstants.ENTER_VALID_MOBILE);
            edt_mobile.requestFocus();
            return false;
        }

        if (edt_mobile.getText().toString().startsWith("0") || edt_mobile.getText().toString().startsWith("1") || edt_mobile.getText().toString().startsWith("2") || edt_mobile.getText().toString().startsWith("3") || edt_mobile.getText().toString().startsWith("4") || edt_mobile.getText().toString().startsWith("5")) {
            edt_mobile.setError("Cannot start with 0,1,2,3,4,5");
            edt_mobile.requestFocus();
            return false;
        }

        if (!GlobalClass.isNull(edt_mail.getText().toString())) {
            if (!GlobalClass.isValidEmail(edt_mail.getText().toString())) {
                edt_mail.setError("Enter valid Email-id");
                edt_mail.requestFocus();
                return false;
            }
            if (edt_mail.getText().toString().equals("")) {
                edt_mail.setError("Enter Valid Email");
                edt_mail.requestFocus();
                return false;
            }
        }

        if (!GlobalClass.isNull(edt_address.getText().toString())) {
            if (edt_address.getText().toString().equals("")) {
                edt_address.setError(MessageConstants.ENTER_ADDRESS);
                edt_address.requestFocus();
                return false;
            }

            if (edt_address.getText().toString().length() < 20) {
                edt_address.setError(MessageConstants.min_length_souuld_be_20);
                edt_address.requestFocus();
                return false;
            }

            if (edt_address.getText().toString().startsWith(" ")) {
                edt_address.setError(MessageConstants.ADDRESS_SHOULD_NOT_START_SPACE);
                edt_address.requestFocus();
                return false;
            }

            if (edt_address.getText().toString().contains("  ")) {
                edt_address.setError(MessageConstants.CANNOT_DOUBLE_SPACE);
                edt_address.requestFocus();
                return false;
            }
        }


        if (!GlobalClass.isNull(edt_address.getText().toString())) {
            if (edt_pincode.getText().toString().equals("")) {
                edt_pincode.setError(MessageConstants.ENTER_PINCODE);
                edt_pincode.requestFocus();
                return false;
            }

            if (edt_pincode.getText().toString().startsWith("0")) {
                edt_pincode.setError(MessageConstants.PINCODE_SHOULD_NOT_START);
                edt_pincode.requestFocus();
                return false;
            }

            if (edt_pincode.getText().toString().startsWith("9")) {
                edt_pincode.setError(MessageConstants.PINCODE_SHOULD_NOT_START_9);
                edt_pincode.requestFocus();
                return false;
            }

            if (edt_pincode.getText().length() != 6) {
                edt_pincode.setError(MessageConstants.ENTER_PINCODE);
                edt_pincode.requestFocus();
                return false;
            }
        }

        if (!GlobalClass.isNull(edt_remarks.getText().toString())) {
            if (edt_remarks.getText().toString().startsWith(" ")) {
                edt_remarks.setError(MessageConstants.Cannot_start_with_space);
                edt_remarks.requestFocus();
                return false;
            }
        }


        return true;
    }


    public void init() {
        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        api_key = "DAuWZtKHyVb8OhjhHSuAwCB6uAyzVzASVTxXcCT8@0fkrvZ5fG9lw533tKKmxVnu";
        mobile = prefs.getString("mobile_user", null);

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        edt_name = findViewById(R.id.edt_name);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_address = findViewById(R.id.edt_address);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_remarks = findViewById(R.id.edt_remarks);
        edt_mail = findViewById(R.id.edt_mail);
        tv_reset = findViewById(R.id.tv_reset);
        GlobalClass.SetHTML(tv_reset, "Reset");
        spr_package = findViewById(R.id.spr_package);
        btn_submit = findViewById(R.id.btn_submit);

        img_tick = (ImageView) findViewById(R.id.img_tick);
        img_tick2 = (ImageView) findViewById(R.id.img_tick2);


        rel_upload_img = (RelativeLayout) findViewById(R.id.rel_upload_img);
        rel_upload_voice = (RelativeLayout) findViewById(R.id.rel_upload_voice);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (displayMetrics != null) {
            try {
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {

                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_name, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_address, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_address, "");
                    }

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {

                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_txt, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.SetEditText(edt_remarks, enteredString.substring(1));
                    } else {
                        GlobalClass.SetEditText(edt_remarks, "");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });
    }

    public void getleaddataresponse(LeadDataResponseModel body) {
        try {
            leadDataResponseModel = body;
            if (leadDataResponseModel.getRES_ID().equalsIgnoreCase("RES0000")) {
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Success")
                        .setCancelable(false)
                        .setMessage(leadDataResponseModel.getRESPONSE())
                        .addButton("OK", -1, getResources().getColor(R.color.maroon), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                if (true) {
                                    Intent intent = new Intent(LeadGenerationActivity.this.getApplicationContext(), LeadGenerationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    LeadGenerationActivity.this.startActivity(intent);
                                }
                            }
                        });

                builder.show();

            } else {
                GlobalClass.ShowError(this, "", "" + leadDataResponseModel.getRESPONSE(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomDialogClass extends Dialog {

        public Activity activity;
        Boolean ButtonClicked = false;
        Button btn_discard, btn_save;
        ImageView img_record;
        TextView tv_record_title;
        ImageButton closeButton;

        public CustomDialogClass(Activity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.audio_record_customdailog);

            initDialogUI();
            initDialogListners();
        }

        private void initDialogUI() {
            closeButton = (ImageButton) findViewById(R.id.ib_close);
            img_record = (ImageView) findViewById(R.id.img_record);
            btn_discard = (Button) findViewById(R.id.btn_discard);
            btn_save = (Button) findViewById(R.id.btn_save);
            tv_record_title = (TextView) findViewById(R.id.tv_record_title);

            if (f_AudioSavePathInDevice != null) {
                if (f_AudioSavePathInDevice.exists()) {
                    btn_discard.setEnabled(true);
                    btn_discard.setBackgroundResource(R.drawable.bg_bg1);
                }
            } else {
                btn_discard.setEnabled(false);
                btn_discard.setBackgroundResource(R.drawable.btn_disabled_bg);
            }
            btn_save.setEnabled(false);
            btn_save.setBackgroundResource(R.drawable.btn_disabled_bg);
            GlobalClass.printLog(Constants.ERROR, TAG, "Audio file initUI: ", "" + f_AudioSavePathInDevice);
        }

        private void initDialogListners() {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    customDialogClass.cancel();
                    GlobalClass.printLog(Constants.ERROR, TAG, "On cancel Audio file: ", "" + f_AudioSavePathInDevice);
                }
            });

            btn_discard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialogClass.cancel();
                    if (f_AudioSavePathInDevice != null) {
                        if (f_AudioSavePathInDevice.exists()) {
                            f_AudioSavePathInDevice.delete();
                        }
                        f_AudioSavePathInDevice = null;
                    }
                    GlobalClass.printLog(Constants.ERROR, TAG, "On discard Audio file: ", "" + f_AudioSavePathInDevice);
                    img_tick2.setVisibility(View.INVISIBLE);
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialogClass.cancel();
                    GlobalClass.printLog(Constants.ERROR, TAG, "On Save Audio file: ", "" + f_AudioSavePathInDevice);
                    img_tick2.setVisibility(View.VISIBLE);
                }
            });

            img_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ButtonClicked) {
                        if (GlobalClass.checkAudioPermission(mActivity)) {
                            ButtonClicked = true;
                            File sdCard = Environment.getExternalStorageDirectory();
                            File directory = new File(sdCard.getAbsolutePath() + "/Uploads" + "/" + "Audio Records");
                            if (!directory.isDirectory()) {
                                directory.mkdirs();
                            }
                            if (!GlobalClass.isNull(type) && type.equals("TEST")) {
                                AudioSavePathInDevice = directory + "/" + lead_id + "_TestBooking.Mp3";
                            }
                            f_AudioSavePathInDevice = new File(AudioSavePathInDevice);
                            GlobalClass.printLog(Constants.ERROR, TAG, "Audio File Path: ", "" + AudioSavePathInDevice);
                            MediaRecorderReady();
                            try {

                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                mediarecorderflag = true;
                            } catch (IllegalStateException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            GlobalClass.SetText(tv_record_title, getString(R.string.stop_record));
                            closeButton.setClickable(false);
                            img_record.setImageResource(R.drawable.ic_stop_record);
                            btn_save.setEnabled(false);
                            btn_save.setBackgroundResource(R.drawable.btn_disabled_bg);
                            btn_discard.setEnabled(false);
                            btn_discard.setBackgroundResource(R.drawable.btn_disabled_bg);
                            GlobalClass.showTastyToast(mActivity, MessageConstants.Recording_Started, 1);
                        } else {
                            GlobalClass.requestAudioPermission(mActivity, RequestPermissionCode);
                        }
                    } else {
                        ButtonClicked = false;
                        try {
                            if (mediarecorderflag) {
                                if (mediaRecorder != null) {
                                    mediaRecorder.release();
                                    mediarecorderflag = false;
                                }
                            }

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }

                        GlobalClass.SetText(tv_record_title, getString(R.string.start_record));
                        closeButton.setClickable(true);
                        img_record.setImageResource(R.drawable.ic_start_record);
                        btn_save.setEnabled(true);
                        btn_save.setBackgroundResource(R.drawable.bg_bg1);
                        btn_discard.setEnabled(true);
                        btn_discard.setBackgroundResource(R.drawable.bg_bg1);
                        GlobalClass.showTastyToast(mActivity, MessageConstants.Recording_completed, 1);
                    }
                }
            });
        }
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            img_tick.setVisibility(View.VISIBLE);

            String imageurl = camera.getCameraBitmapPath();
            imagefile = new File(imageurl);

        }
    }


    class AsyncTaskPost_uploadfile extends AsyncTask<Void, Void, String> {
        String name, mobile, email, address, remarks, type;
        File imagefile, f_AudioSavePathInDevice;
        ProgressDialog progressDialog;

        public AsyncTaskPost_uploadfile(String name, String mobile, String email, String address, String remarks, String type, File imagefile, File f_AudioSavePathInDevice) {
            this.name = name;
            this.mobile = mobile;
            this.email = email;
            this.address = address;
            this.remarks = remarks;
            this.type = type;
            this.imagefile = imagefile;
            this.f_AudioSavePathInDevice = f_AudioSavePathInDevice;
            status_code = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = GlobalClass.ShowprogressDialog(LeadGenerationActivity.this);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String strUrl = Api.NUECLEAR + "/Aayushman/Booking";
            Log.e(TAG, "POST API ---->" + strUrl);
            //Log.v(strUrl);

            InputStream inputStream = null;
            String result = "";

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            StringBody filename = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);

                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("bookingtype", new StringBody("" + type));
                builder.addPart("order_by", new StringBody("" + name));
                builder.addPart("mobile", new StringBody("" + mobile));
                builder.addPart("email", new StringBody("" + email));
                builder.addPart("address", new StringBody("" + address));
                builder.addPart("remarks", new StringBody("" + remarks + " ~CLISO"));
                builder.addPart("report_code", new StringBody(""));
                builder.addPart("bencount", new StringBody("1"));
                builder.addPart("pincode", new StringBody("" + edt_pincode.getText().toString().trim()));
                builder.addPart("product", new StringBody(""));
                builder.addPart("rate", new StringBody("0"));
                builder.addPart("reports", new StringBody(""));
                builder.addPart("ref_code", new StringBody("" + prefs_user.getString("mobile_user", "")));
                builder.addPart("service_type", new StringBody("H"));
                builder.addPart("api_key", new StringBody("" + api_key));
                builder.addPart("bendataxml", new StringBody("<NewDataSet><Ben_details><Name>" + name + "</Name><Age>" + "" + "</Age><Gender>" + "" + "</Gender></Ben_details></NewDataSet>"));
                builder.addPart("orderid", new StringBody("" + lead_id));
                builder.addPart("hc", new StringBody("0"));
                builder.addPart("tsp", new StringBody(""));
                builder.addPart("pay_type", new StringBody("Postpaid"));

                if (imagefile != null) {
                    if (imagefile.exists()) {
                        builder.addBinaryBody("file_img", imagefile);
                    }
                }
                if (f_AudioSavePathInDevice != null) {
                    if (f_AudioSavePathInDevice.exists()) {
                        builder.addBinaryBody("file_aud", f_AudioSavePathInDevice);
                    }
                }
                GlobalClass.printLog(Constants.ERROR, TAG, "Post params:- ", "{" + "\"bookingtype\"" + ":\"" + type + "\"," + "\"order_by\"" + ":\"" + name + "\"," + "\"mobile\"" + ":\"" + mobile + "\"," + "\"email\"" + ":\"" + email + "\"," + "\"address\"" + ":\"" + address + "\","
                        + "\"remarks\"" + ":\"" + remarks + "\"" + "," + "\"report_code\"" + ":" + "\"\"" + "," + "\"bencount\"" + ":" + "1" + "," + "\"pincode\"" + ":\"" + "\"," + "\"product\"" + ":" + "\"\"" + "," +
                        "\"rate\"" + ":" + "0" + "," + "\"reports\"" + ":" + "\"\"" + "," + "\"ref_code\"" + ":\"" + prefs_user.getString("mobile", "") + "\"," + "\"service_type\"" + ":" + "\"H\"" + "," +
                        "\"api_key\"" + ":\"" + api_key + "\","
                        + "\"bendataxml\"" + ":\"" + "<NewDataSet><Ben_details><Name>" + name + "</Name><Age>" + "" + "</Age><Gender>" + "" + "</Gender></Ben_details></NewDataSet>" + "\","
                        + "\"orderid\"" + ":\"" + lead_id + "\"," + "\"hc\"" + ":" + "0" + "," + "\"tsp\"" + ":" + "\"\"" + "," + "\"pay_type\"" + ":" + "\"Postpaid\"" + ","
                        + "\"file_img\"" + ":\"" + imagefile + "\"," + "\"file_aud\"" + ":\"" + f_AudioSavePathInDevice + "\"}");

                httpPost.setEntity(builder.build());
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                GlobalClass.printLog(Constants.ERROR, TAG, "Status Line: ", "" + httpResponse.getStatusLine());
                status_code = httpResponse.getStatusLine().getStatusCode();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.v("Response : ", result);
                }
            } catch (Exception e) {
                GlobalClass.printLog(Constants.DEBUG, TAG, "InputStream", e.getLocalizedMessage());
                result = "Something went wrong !!";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            GlobalClass.hideProgress(LeadGenerationActivity.this, progressDialog);
            if (status_code == 200) {
                if (response != null && !response.isEmpty()) {
                    GlobalClass.printLog(Constants.ERROR, TAG, "Response: ", response);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gson = gsonBuilder.create();
                    testBookingResponseModel = gson.fromJson(response, TestBookingResponseModel.class);
                    AlertDialog.Builder alertDialogBuilder;
                    if (testBookingResponseModel.getRES_ID().equals("RES0000")) {

                        clearfileds();

                        CustomDialogforSuccess = new Dialog(mActivity);
                        CustomDialogforSuccess.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        CustomDialogforSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        CustomDialogforSuccess.setContentView(R.layout.refer_a_patient_success_dialog);
                        CustomDialogforSuccess.setCancelable(false);

                        TextView tv_name, tv_mobile, tv_email_id, tv_city, booking_ID, tv_remarks, note;
                        ImageView img_whatsapp, img_mail, img_sms, btnclose;
                        LinearLayout ll_address, ll_email_id, ll_remarks, ll_main;

                        tv_name = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_name);
                        tv_mobile = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_mobile);
                        tv_email_id = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_email_id);
                        tv_city = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_city);
                        booking_ID = (TextView) CustomDialogforSuccess.findViewById(R.id.booking_ID);
                        tv_remarks = (TextView) CustomDialogforSuccess.findViewById(R.id.tv_remarks);
                        btnclose = (ImageView) CustomDialogforSuccess.findViewById(R.id.btnclose);
                        img_whatsapp = (ImageView) CustomDialogforSuccess.findViewById(R.id.img_whatsapp);
                        img_mail = (ImageView) CustomDialogforSuccess.findViewById(R.id.img_mail);
                        img_sms = (ImageView) CustomDialogforSuccess.findViewById(R.id.img_sms);
                        ll_address = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_address);
                        ll_email_id = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_email_id);
                        ll_remarks = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_remarks);
                        ll_main = (LinearLayout) CustomDialogforSuccess.findViewById(R.id.ll_main);
                        note = (TextView) CustomDialogforSuccess.findViewById(R.id.note);


                        ll_email_id.setVisibility(View.GONE);
                        ll_remarks.setVisibility(View.GONE);
                        ll_address.setVisibility(View.GONE);


                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width - 150, FrameLayout.LayoutParams.WRAP_CONTENT);
                        ll_main.setLayoutParams(lp);

                        btnclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CustomDialogforSuccess.dismiss();
                                LeadGenerationActivity.this.onResume();
                            }
                        });


                        GlobalClass.SetText(booking_ID, testBookingResponseModel.getREF_ORDERID());
                        GlobalClass.SetText(tv_name, name);
                        GlobalClass.SetText(tv_mobile, mobile);
                        GlobalClass.SetText(note, mobile);
                        GlobalClass.SetText(note, name + " will be contacted for availing service.");


                        img_whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (GlobalClass.isWhatsApp(mActivity)) {
                                    ShareModeType = "WhatsApp";
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl(name, mobile, testBookingResponseModel.getREF_ORDERID(), type)));
                                    startActivity(intent);
                                } else {
                                    GlobalClass.showTastyToast(mActivity, MessageConstants.WHTSAPP_NOT_INSTALL, 2);
                                }
                            }
                        });

                        img_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ShareModeType = "Email";
                                Intent emailintent = new Intent(Intent.ACTION_SEND);
                                emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                if (!GlobalClass.isNull(type) && type.contains("TEST")) {
                                    emailintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.test_subject));

                                    emailintent.putExtra(Intent.EXTRA_TEXT, "Lead ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                                }
                                emailintent.setType("message/rfc822");
                                try {
                                    startActivity(Intent.createChooser(emailintent, "Choose an Email client to which you want to share details:"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    GlobalClass.showTastyToast(mActivity, "There is no email client installed", 0);
                                }
                            }
                        });

                        img_sms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ShareModeType = "SMS";
                                Uri uri = Uri.parse("smsto:" + mobile);
                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                                if (!GlobalClass.isNull(type) && type.contains("TEST")) {
                                    sendIntent.putExtra("sms_body", getString(R.string.test_subject) + "\n" + "Lead ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                                }
                                startActivity(sendIntent);
                            }
                        });

                        CustomDialogforSuccess.show();
                    } else if (!GlobalClass.isNull(testBookingResponseModel.getRESPONSE()) && testBookingResponseModel.getRESPONSE().contains(MessageConstants.mobileno_has_already_used)) {
                        alertDialogBuilder = new AlertDialog.Builder(mActivity);
                        alertDialogBuilder
                                .setMessage(MessageConstants.MOBILE_NO_USED_10_TIMES)
                                .setCancelable(false)
                                .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        if (!mActivity.isFinishing()) {
                            alertDialog.show();
                        }
                    } else {
                        alertDialogBuilder = new AlertDialog.Builder(mActivity);
                        alertDialogBuilder
                                .setMessage(testBookingResponseModel.getRESPONSE())
                                .setCancelable(false)
                                .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        if (!mActivity.isFinishing()) {
                            alertDialog.show();
                        }
                    }
                }
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 2);
            }
        }
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    public static String whatsappUrl(String name, String mobile, String booking_id, String type) {

        final String BASE_URL = "https://api.whatsapp.com/";
        final String WHATSAPP_PHONE_NUMBER = "91" + mobile;    //'62' is country code for Indonesia
        final String PARAM_PHONE_NUMBER = "phone";
        final String PARAM_TEXT = "text";
        String TEXT_VALUE = null;
        if (!GlobalClass.isNull(type) && type.contains("TEST")) {
            TEXT_VALUE = "Test details" + "\n" + "Lead ID :- " + booking_id + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile;
        }
        String newUrl = BASE_URL + "send";
        Uri builtUri = Uri.parse(newUrl).buildUpon()
                .appendQueryParameter(PARAM_PHONE_NUMBER, WHATSAPP_PHONE_NUMBER)
                .appendQueryParameter(PARAM_TEXT, TEXT_VALUE)
                .build();

        return GlobalClass.buildWhatsappUrl(builtUri).toString();
    }

}
