package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.COVIDgetotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_req;
import com.example.e5322.thyrosoft.Models.Covid_validateotp_res;
import com.example.e5322.thyrosoft.Models.Covidotpresponse;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.MissedcallAPI_Response;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class Covidenter_Frag extends Fragment implements View.OnClickListener {
    Activity activity;
    ConnectionDetector cd;
    CountDownTimer countDownTimer;
    public RadioButton by_missed, by_generate;
    LinearLayout lin_by_missed, lin_by_generate, lin_generate_verify;
    Button btn_choosefile_presc, btn_choosefile_adhar, btn_choosefile_trf;
    Button btn_generate, btn_submit, btn_verify, btn_resend;
    TextView txt_nofilepresc, txt_nofileadhar, txt_nofiletrf, tv_timer, tv_missedcall_mob;
    private int PERMISSION_REQUEST_CODE = 200;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    private String userChoosenTask;
    private Camera camera;
    Uri imageUri;
    private boolean timerflag = false;
    String TAG = getClass().getSimpleName();
    Bitmap bitmapimage;
    File presc_file = null;
    File aadhar_file = null;
    File trf_file = null;
    boolean ispresciption, isadhar, istrf;
    EditText edt_fname, edt_lname, edt_missed_mobile, edt_verifycc;
    SharedPreferences preferences;
    String usercode, apikey;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View viewMain = (View) inflater.inflate(R.layout.covid_enter, container, false);
        activity = getActivity();
        cd = new ConnectionDetector(activity);
        return viewMain;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", null);
        apikey = preferences.getString("API_KEY", null);

        edt_fname = view.findViewById(R.id.edt_firstname);
        edt_lname = view.findViewById(R.id.edt_lastname);
        edt_missed_mobile = view.findViewById(R.id.edt_missed_mobile);
        edt_verifycc = view.findViewById(R.id.edt_verifycc);

        edt_fname.setFilters(new InputFilter[]{EMOJI_FILTER});
        int maxLength = 20;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        edt_fname.setFilters(FilterArray);

        edt_lname.setFilters(new InputFilter[]{EMOJI_FILTER});
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        edt_lname.setFilters(FilterArray);

        //TODO Buttons
        btn_choosefile_presc = view.findViewById(R.id.btn_choosefile_presc);
        btn_choosefile_adhar = view.findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_trf = view.findViewById(R.id.btn_choosefile_trf);
        btn_generate = view.findViewById(R.id.btn_generate);
        btn_verify = view.findViewById(R.id.btn_verify);
        btn_resend = view.findViewById(R.id.btn_resend);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_generate.setText("Get Missed call");

        //TODO LinearLaypouts
        lin_by_missed = view.findViewById(R.id.lin_missed_verify);
        lin_generate_verify = view.findViewById(R.id.lin_generate_verify);

        //TODO Textviews
        txt_nofilepresc = view.findViewById(R.id.txt_nofilepresc);
        txt_nofileadhar = view.findViewById(R.id.txt_nofileadhar);
        txt_nofiletrf = view.findViewById(R.id.txt_nofiletrf);
        tv_missedcall_mob = view.findViewById(R.id.tv_missedcall_mob);
        tv_timer = view.findViewById(R.id.tv_timer);

        by_missed = (RadioButton) view.findViewById(R.id.by_missed);
        by_generate = (RadioButton) view.findViewById(R.id.by_generate);

        by_missed.setOnClickListener(this);
        by_generate.setOnClickListener(this);

        btn_choosefile_presc.setOnClickListener(this);
        btn_choosefile_adhar.setOnClickListener(this);
        btn_choosefile_trf.setOnClickListener(this);
        btn_resend.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));

        txt_nofilepresc.setOnClickListener(this);
        txt_nofileadhar.setOnClickListener(this);
        txt_nofiletrf.setOnClickListener(this);

        btn_generate.setOnClickListener(this);


        edt_fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        edt_fname.setText("");
                    } else {
                        edt_fname.setText(enteredString.substring(1));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edt_lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        edt_lname.setText("");
                    } else {
                        edt_lname.setText(enteredString.substring(1));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:
                if (Validation()) {

                }
                break;

            case R.id.btn_verify:
                if (!TextUtils.isEmpty(edt_verifycc.getText().toString())) {
                    if (cd.isConnectingToInternet()) {
                        validateotp();
                    } else {
                        GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN);
                    }

                } else {
                    GlobalClass.showCustomToast(activity, "Kindly enter otp");
                }

                break;

            case R.id.btn_generate:
                genrateflow();
                break;

            case R.id.by_missed:
                lin_by_missed.setVisibility(View.VISIBLE);
                btn_generate.setText("Get Missed call");
                clearfields();
                break;

            case R.id.btn_resend:
                genrateflow();
                break;

            case R.id.by_generate:
                btn_generate.setText("Generate CCC");
                clearfields();
                break;

            case R.id.btn_choosefile_presc:
                if (checkPermission()) {
                    ispresciption = true;
                    isadhar = false;
                    istrf = false;
                    selectImage();
                } else {
                    requestPermission();
                }
                break;

            case R.id.btn_choosefile_adhar:
                if (checkPermission()) {
                    ispresciption = false;
                    isadhar = true;
                    istrf = false;
                    selectImage();
                } else {
                    requestPermission();
                }
                break;
            case R.id.btn_choosefile_trf:
                if (checkPermission()) {
                    ispresciption = false;
                    isadhar = false;
                    istrf = true;
                    selectImage();
                } else {
                    requestPermission();
                }
                break;
            case R.id.txt_nofilepresc:
                if (txt_nofilepresc.getText().toString().equalsIgnoreCase(getResources().getString(R.string.view))) {
                    GlobalClass.showImageDialog(activity, presc_file, "", 1);
                }
                break;

            case R.id.txt_nofileadhar:
                if (txt_nofileadhar.getText().toString().equalsIgnoreCase(getResources().getString(R.string.view))) {
                    GlobalClass.showImageDialog(activity, aadhar_file, "", 1);
                }
                break;

            case R.id.txt_nofiletrf:
                if (txt_nofiletrf.getText().toString().equalsIgnoreCase(getResources().getString(R.string.view))) {
                    GlobalClass.showImageDialog(activity, trf_file, "", 1);
                }
                break;
        }
    }

    private void genrateflow() {
        if (mobilenovalidation()) {
            if (btn_generate.getText().toString().equalsIgnoreCase("Generate CCC")) {
                if (cd.isConnectingToInternet()) {
                    generateOtP(edt_missed_mobile.getText().toString());
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN);
                }

            } else {
                if (cd.isConnectingToInternet()) {
                    callmissedcallAPI();
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.CHECK_INTERNET_CONN);
                }

            }


        }
    }

    private void callmissedcallAPI() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        Call<MissedcallAPI_Response> missedcallAPI_responseCall = postAPIInteface.missedcallapi();
        missedcallAPI_responseCall.enqueue(new Callback<MissedcallAPI_Response>() {
            @Override
            public void onResponse(Call<MissedcallAPI_Response> call, Response<MissedcallAPI_Response> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        tv_missedcall_mob.setVisibility(View.VISIBLE);
                        tv_missedcall_mob.setText("Give missed call on " + response.body().getMobile()
                                + " / " + response.body().getMobile1()
                                +" numbers to generate CCC");

                        //setCountDownTimer();
                        lin_generate_verify.setVisibility(View.VISIBLE);
                        lin_by_missed.setVisibility(View.VISIBLE);
                        btn_generate.setText("Get Missed call");
                        disablefields();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MissedcallAPI_Response> call, Throwable t) {

            }
        });
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(activity, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(activity);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        openCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        chooseFromGallery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildCamera() {
        camera = new com.mindorks.paracamera.Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {

                if (ispresciption) {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    presc_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + presc_file;
                    presc_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), presc_file);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(presc_file.length())));
                    txt_nofilepresc.setText(getResources().getString(R.string.view));
                    txt_nofilepresc.setTextColor(getResources().getColor(R.color.maroon));
                    txt_nofilepresc.setPaintFlags(txt_nofilepresc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    ispresciption = false;
                } else if (isadhar) {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    aadhar_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file;
                    aadhar_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), aadhar_file);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(aadhar_file.length())));
                    txt_nofileadhar.setText(getResources().getString(R.string.view));
                    txt_nofileadhar.setTextColor(getResources().getColor(R.color.maroon));
                    txt_nofileadhar.setPaintFlags(txt_nofileadhar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    isadhar = false;
                } else {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    trf_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file;
                    trf_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), trf_file);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(trf_file.length())));
                    txt_nofiletrf.setText(getResources().getString(R.string.view));
                    txt_nofiletrf.setTextColor(getResources().getColor(R.color.maroon));
                    txt_nofiletrf.setPaintFlags(txt_nofiletrf.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    istrf = false;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(activity, "Failed to load image!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                if (ispresciption) {
                    if (data.getData() != null) {
                        presc_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(presc_file.length())));
                    presc_file = GlobalClass.getCompressedFile(activity, presc_file);
                    Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(presc_file.length())));
                    txt_nofilepresc.setText(getResources().getString(R.string.view));
                    txt_nofilepresc.setTextColor(getResources().getColor(R.color.maroon));
                    txt_nofilepresc.setPaintFlags(txt_nofilepresc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else if (isadhar) {
                    if (data.getData() != null) {
                        aadhar_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(aadhar_file.length())));
                    aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                    Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(aadhar_file.length())));
                    txt_nofileadhar.setText(getResources().getString(R.string.view));
                    txt_nofileadhar.setTextColor(getResources().getColor(R.color.maroon));
                    txt_nofileadhar.setPaintFlags(txt_nofileadhar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else if (istrf) {
                    if (data.getData() != null) {
                        trf_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(trf_file.length())));
                    trf_file = GlobalClass.getCompressedFile(activity, trf_file);
                    Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(trf_file.length())));
                    txt_nofiletrf.setText(getResources().getString(R.string.view));
                    txt_nofiletrf.setTextColor(getResources().getColor(R.color.maroon));
                    txt_nofiletrf.setPaintFlags(txt_nofiletrf.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }

            } catch (IOException e) {
                Toast.makeText(activity, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private boolean Validation() {
        if (edt_fname.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            edt_fname.requestFocus();
            return false;
        }
        if (edt_fname.getText().toString().length() < 2) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_FNAME);
            edt_fname.requestFocus();
            return false;
        }

        if (edt_lname.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lname.requestFocus();
            return false;
        }
        if (edt_lname.getText().toString().length() < 1) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_LNAME);
            edt_lname.requestFocus();
            return false;
        }

        if (edt_missed_mobile.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }

        if (presc_file == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_PIMAGE);
            return false;
        }

        if (aadhar_file == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_ADHIMAGE);
            return false;
        }

        if (trf_file == null) {
            Global.showCustomToast(getActivity(), ToastFile.SELECT_TRFDHIMAGE);
            return false;
        }

        return true;
    }

    private void setCountDownTimer() {

        countDownTimer = new CountDownTimer(60000, 1000) {
            NumberFormat numberFormat = new DecimalFormat("00");

            public void onTick(long millisUntilFinished) {

                if (timerflag == false) {
                    btn_resend.setEnabled(false);
                    btn_resend.setClickable(false);
                    tv_timer.setVisibility(View.VISIBLE);
                } else {
                    tv_timer.setVisibility(View.GONE);
                }

                long time = millisUntilFinished / 1000;
                tv_timer.setText("Please wait 00:" + numberFormat.format(time));
            }

            public void onFinish() {
                tv_timer.setVisibility(View.GONE);
                btn_resend.setEnabled(true);
                btn_resend.setClickable(true);

                edt_missed_mobile.setEnabled(true);
                edt_missed_mobile.setClickable(true);

                edt_verifycc.setEnabled(true);
                edt_verifycc.setClickable(true);

            }
        }.start();
    }

    private void clearfields() {
        lin_generate_verify.setVisibility(View.GONE);
        edt_fname.setFocusable(true);
        edt_fname.setCursorVisible(true);
        edt_fname.getText().clear();
        edt_lname.getText().clear();
        edt_missed_mobile.getText().clear();
        edt_verifycc.getText().clear();
        edt_missed_mobile.setEnabled(true);
        edt_missed_mobile.setClickable(true);
        btn_generate.setEnabled(true);
        btn_generate.setClickable(true);
        btn_verify.setClickable(true);
        btn_verify.setEnabled(true);
        btn_resend.setEnabled(true);
        btn_resend.setClickable(true);
        tv_missedcall_mob.setVisibility(View.GONE);
        timerflag = false;
        presc_file = null;
        aadhar_file = null;
        trf_file = null;

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            tv_timer.setVisibility(View.GONE);
        }

    }

    private void disablefields() {
        edt_missed_mobile.setEnabled(false);
        btn_generate.setEnabled(false);
        btn_resend.setEnabled(false);

        edt_missed_mobile.setClickable(false);
        btn_generate.setClickable(false);
        btn_resend.setClickable(false);
    }


    private void generateOtP(String mobileno) {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        COVIDgetotp_req coviDgetotp_req = new COVIDgetotp_req();
        coviDgetotp_req.setApi_key(apikey);
        coviDgetotp_req.setMobile(mobileno);
        coviDgetotp_req.setScode(usercode);

        Call<Covidotpresponse> covidotpresponseCall = postAPIInteface.generateotp(coviDgetotp_req);
        covidotpresponseCall.enqueue(new Callback<Covidotpresponse>() {
            @Override
            public void onResponse(Call<Covidotpresponse> call, Response<Covidotpresponse> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        setCountDownTimer();
                        lin_generate_verify.setVisibility(View.VISIBLE);
                        lin_by_missed.setVisibility(View.VISIBLE);
                        btn_generate.setText("Generate CCC");
                        disablefields();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covidotpresponse> call, Throwable t) {

            }
        });
    }

    private void validateotp() {
        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(activity);
        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.LIVEAPI).create(PostAPIInteface.class);
        Covid_validateotp_req covid_validateotp_req = new Covid_validateotp_req();
        covid_validateotp_req.setApi_key(apikey);
        covid_validateotp_req.setMobile(edt_missed_mobile.getText().toString());
        covid_validateotp_req.setOtp(edt_verifycc.getText().toString());

        Call<Covid_validateotp_res> covidotpresponseCall = postAPIInteface.validateotp(covid_validateotp_req);
        covidotpresponseCall.enqueue(new Callback<Covid_validateotp_res>() {
            @Override
            public void onResponse(Call<Covid_validateotp_res> call, Response<Covid_validateotp_res> response) {
                GlobalClass.hideProgress(activity, progressDialog);
                try {
                    if (response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                        edt_missed_mobile.setEnabled(false);
                        btn_generate.setEnabled(false);
                        btn_resend.setEnabled(false);
                        tv_timer.setVisibility(View.GONE);
                        edt_missed_mobile.setClickable(false);
                        btn_generate.setClickable(false);
                        btn_resend.setClickable(false);
                        lin_generate_verify.setVisibility(View.GONE);
                        timerflag = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Covid_validateotp_res> call, Throwable t) {

            }
        });
    }

    private boolean mobilenovalidation() {
        if (edt_missed_mobile.getText().toString().length() == 0) {
            Global.showCustomToast(getActivity(), ToastFile.ENTER_MOBILE);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() < 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        if (edt_missed_mobile.getText().toString().length() > 10) {
            Global.showCustomToast(getActivity(), ToastFile.MOBILE_10_DIGITS);
            edt_missed_mobile.requestFocus();
            return false;
        }
        return true;
    }

}
