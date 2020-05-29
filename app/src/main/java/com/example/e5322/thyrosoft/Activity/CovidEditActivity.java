package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CovidEditActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_name, tv_verifiedmob;
    ConnectionDetector cd;
    Activity activity;
    String usercode, apikey;
    Button btn_choosefile_presc, btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_vial, btn_choosefile_other;
    ImageView img_pres_preview, img_adhar_preview1, img_adhar_preview2, img_trf_preview1, img_trf_preview2,img_vialpreview1,  img_otherpreview1, img_other_preview2,back,home;
    RelativeLayout rel_adhar1, rel_adhar2, rel_trf1, rel_trf2,rel_other1,rel_other2, rel_mobno;
    TextView txt_nofilepresc, txt_nofileadhar, txt_nofiletrf, txt_nofilevial, txt_nofileother;
    Button btn_submit, btn_reset;
    LinearLayout lin_pres_preview, lin_adhar_images, lin_trf_images,lin_vial_images,lin_other_images;
    boolean verifyotp = true;
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
    File aadhar_file1 = null;
    File trf_file = null;
    File trf_file1 = null;
    File vial_file = null;
    File other_file = null;
    File other_file1 = null;

    boolean ispresciption, isadhar, istrf, isvial, isother;
    SharedPreferences preferences;
    String mobileno, name,UniqueId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_covidedit);
        activity = CovidEditActivity.this;
        cd = new ConnectionDetector(activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileno = bundle.getString("Mobile");
            name = bundle.getString("name");
            UniqueId=bundle.getString("UniqueId");
            Log.e(TAG,"uniqueid-->"+UniqueId);
        }

        initview();
    }

    private void initview() {
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", null);
        apikey = preferences.getString("API_KEY", null);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);

        tv_name = findViewById(R.id.tv_name);
        tv_verifiedmob = findViewById(R.id.tv_verifiedmob);

        tv_name.setText(name);
        tv_verifiedmob.setText(mobileno);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(CovidEditActivity.this);
            }
        });

        //TODO Buttons
        btn_choosefile_presc = findViewById(R.id.btn_choosefile_presc);
        btn_choosefile_adhar = findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_trf = findViewById(R.id.btn_choosefile_trf);
        btn_choosefile_vial = findViewById(R.id.btn_choosefile_vial);
        btn_choosefile_other = findViewById(R.id.btn_choosefile_other);

        //TODO IMAGEVIEW

        img_pres_preview = findViewById(R.id.img_pres_preview);
        img_adhar_preview1 = findViewById(R.id.img_adhar_preview1);
        img_adhar_preview2 = findViewById(R.id.img_adhar_preview2);

        img_trf_preview1 = findViewById(R.id.img_trf_preview1);
        img_trf_preview2 = findViewById(R.id.img_trf_preview2);

        img_vialpreview1= findViewById(R.id.img_vialpreview1);
        img_otherpreview1=findViewById(R.id.img_otherpreview1);
        img_other_preview2 = findViewById(R.id.img_other_preview2);

        //TODO Relativelayout

        rel_mobno = findViewById(R.id.rel_mobno);

        rel_adhar1 = findViewById(R.id.rel_adhar1);
        rel_adhar2 = findViewById(R.id.rel_adhar2);

        rel_trf1 = findViewById(R.id.rel_trf1);
        rel_trf2 = findViewById(R.id.rel_trf2);

        rel_other1 = findViewById(R.id.rel_other1);
        rel_other2 = findViewById(R.id.rel_other2);

        txt_nofilepresc = findViewById(R.id.txt_nofilepresc);
        txt_nofileadhar = findViewById(R.id.txt_nofileadhar);
        txt_nofiletrf = findViewById(R.id.txt_nofiletrf);
        txt_nofilevial= findViewById(R.id.txt_nofilevial);
        txt_nofileother= findViewById(R.id.txt_nofileother);

        btn_reset = findViewById(R.id.btn_reset);
        btn_submit = findViewById(R.id.btn_submit);

        lin_pres_preview = findViewById(R.id.lin_pres_preview);
        lin_adhar_images = findViewById(R.id.lin_adhar_images);
        lin_trf_images = findViewById(R.id.lin_trf_images);
        lin_vial_images=findViewById(R.id.lin_vial_images);
        lin_other_images=findViewById(R.id.lin_other_images);

        btn_reset.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));

        img_pres_preview.setOnClickListener(this);

        img_adhar_preview1.setOnClickListener(this);
        img_adhar_preview2.setOnClickListener(this);


        img_trf_preview1.setOnClickListener(this);
        img_trf_preview2.setOnClickListener(this);

        img_vialpreview1.setOnClickListener(this);
        img_otherpreview1.setOnClickListener(this);
        img_other_preview2.setOnClickListener(this);

        btn_choosefile_presc.setOnClickListener(this);
        btn_choosefile_adhar.setOnClickListener(this);
        btn_choosefile_trf.setOnClickListener(this);
        btn_choosefile_vial.setOnClickListener(this);
        btn_choosefile_other.setOnClickListener(this);


    }

    private void clearfields() {

        presc_file = null;
        aadhar_file = null;
        aadhar_file1 = null;

        trf_file = null;
        trf_file1 = null;

        vial_file=null;
        other_file=null;
        other_file1=null;

        lin_pres_preview.setVisibility(View.GONE);
        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);
        lin_vial_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);

        rel_adhar1.setVisibility(View.GONE);
        rel_adhar2.setVisibility(View.GONE);

        rel_trf1.setVisibility(View.GONE);
        rel_trf2.setVisibility(View.GONE);

        rel_other1.setVisibility(View.GONE);
        rel_other2.setVisibility(View.GONE);

        txt_nofilepresc.setVisibility(View.VISIBLE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);
        txt_nofilevial.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);

        txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilepresc.setTextColor(getResources().getColor(R.color.black));
        txt_nofilepresc.setPaintFlags(0);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofilevial.setTextColor(getResources().getColor(R.color.black));
        txt_nofilevial.setPaintFlags(0);

        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setTextColor(getResources().getColor(R.color.black));
        txt_nofileother.setPaintFlags(0);

        btn_choosefile_presc.setBackground(getResources().getDrawable(R.color.maroon));
        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.color.maroon));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_trf.setBackground(getResources().getDrawable(R.color.maroon));
        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_vial.setBackground(getResources().getDrawable(R.color.maroon));
        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.color.maroon));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                if (Validation()) {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        GlobalClass.showAlertDialog(activity);
                    } else {

                        try {
                            if (ControllersGlobalInitialiser.covidmultipart_controller != null) {
                                ControllersGlobalInitialiser.covidmultipart_controller = null;
                            }


                            Covidpostdata covidpostdata = new Covidpostdata();
                            covidpostdata.setUNIQUEID(UniqueId);
                            covidpostdata.setSOURCECODE(usercode);
                            covidpostdata.setMOBILE(mobileno);
                            covidpostdata.setNAME(name);
                            covidpostdata.setPRESCRIPTION(presc_file);
                            covidpostdata.setVIAIMAGE(vial_file);


                            if (aadhar_file != null && aadhar_file1 != null) {
                                covidpostdata.setADHAR(aadhar_file);
                                covidpostdata.setADHAR1(aadhar_file1);
                            } else if (aadhar_file != null && aadhar_file1 == null) {
                                covidpostdata.setADHAR(aadhar_file);
                            } else {
                                covidpostdata.setADHAR(aadhar_file1);
                            }

                            if (trf_file != null && trf_file1 != null) {
                                covidpostdata.setTRF(trf_file);
                                covidpostdata.setTRF1(trf_file1);
                            } else if (trf_file != null && trf_file1 == null) {
                                covidpostdata.setTRF(trf_file);
                            } else {
                                covidpostdata.setTRF(trf_file1);
                            }

                            if (other_file != null && other_file1 != null) {
                                covidpostdata.setOTHER(other_file);
                                covidpostdata.setOTHER1(other_file1);
                            } else if (other_file != null && other_file1 == null) {
                                covidpostdata.setOTHER(other_file);
                            } else {
                                covidpostdata.setOTHER(other_file1);
                            }

                            new Covidmultipart_controller(CovidEditActivity.this, activity, covidpostdata).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                break;

            case R.id.btn_reset:
                clearfields();
                break;

            case R.id.btn_choosefile_presc:
                if (verifyotp) {
                    if (checkPermission()) {

                        if (presc_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one image");
                        } else {
                            ispresciption = true;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;

            case R.id.btn_choosefile_adhar:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (aadhar_file != null && aadhar_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {
                            ispresciption = false;
                            isadhar = true;
                            istrf = false;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }

                    } else {
                        requestPermission();
                    }

                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }

                break;
            case R.id.btn_choosefile_trf:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (trf_file != null && trf_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = true;
                            isvial = false;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;


            case R.id.btn_choosefile_vial:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (vial_file != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only one images");
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = true;
                            isother = false;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;

            case R.id.btn_choosefile_other:
                if (verifyotp) {
                    if (checkPermission()) {
                        if (other_file != null && other_file1 != null) {
                            GlobalClass.showCustomToast(activity, "You can upload only two images");
                        } else {
                            ispresciption = false;
                            isadhar = false;
                            istrf = false;
                            isvial = false;
                            isother = true;
                            selectImage();
                        }
                    } else {
                        requestPermission();
                    }
                } else {
                    GlobalClass.showCustomToast(activity, MessageConstants.VERIFY);
                }
                break;

            case R.id.img_pres_preview:
                showImageDialog1(activity, presc_file, "", 1, "prec");
                break;
            case R.id.img_adhar_preview1:
                showImageDialog1(activity, aadhar_file, "", 1, "adhar1");
                break;
            case R.id.img_adhar_preview2:
                showImageDialog1(activity, aadhar_file1, "", 1, "adhar2");
                break;
            case R.id.img_trf_preview1:
                showImageDialog1(activity, trf_file, "", 1, "trf1");
                break;
            case R.id.img_trf_preview2:
                showImageDialog1(activity, trf_file1, "", 1, "trf2");
                break;
            case R.id.img_vialpreview1:
                showImageDialog1(activity, vial_file, "", 1, "vial");
                break;
            case R.id.img_otherpreview1:
                showImageDialog1(activity, other_file, "", 1, "other");
                break;
            case R.id.img_other_preview2:
                showImageDialog1(activity, other_file1, "", 1, "other1");
                break;

        }
    }

    private boolean Validation() {
        if (presc_file == null) {
            Global.showCustomToast(activity, ToastFile.SELECT_PIMAGE);
            return false;
        }

        if (aadhar_file == null && aadhar_file1 == null) {
            Global.showCustomToast(activity, ToastFile.SELECT_ADHIMAGE);
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            Global.showCustomToast(activity, ToastFile.SELECT_TRFDHIMAGE);
            return false;
        }
        if (vial_file == null) {
            Global.showCustomToast(activity, ToastFile.SELECT_VIALDHIMAGE);
            return false;
        }

        return true;
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

    public void showImageDialog1(final Activity activity, final File file, String url, int flag, final String type) {

        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.covid_preview_dialog);
        maindialog.setCancelable(true);

        ImageView imgView = (ImageView) maindialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) maindialog.findViewById(R.id.img_close);
        Button btn_delete = maindialog.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Set the Alert Dialog Message
                builder.setMessage(ToastFile.deletefile);
                builder.setCancelable(false);
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if (type.equalsIgnoreCase("prec")) {
                                    presc_file = null;
                                    txt_nofilepresc.setVisibility(View.VISIBLE);
                                    txt_nofilepresc.setText(getResources().getString(R.string.nofilechoosen));
                                    lin_pres_preview.setVisibility(View.GONE);

                                } else if (type.equalsIgnoreCase("adhar1")) {
                                    aadhar_file = null;
                                    rel_adhar1.setVisibility(View.GONE);
                                    if (rel_adhar2.getVisibility() == View.VISIBLE) {
                                        txt_nofileadhar.setVisibility(View.GONE);
                                    } else {
                                        txt_nofileadhar.setVisibility(View.VISIBLE);
                                        lin_adhar_images.setVisibility(View.GONE);
                                    }

                                } else if (type.equalsIgnoreCase("adhar2")) {
                                    aadhar_file1 = null;
                                    rel_adhar2.setVisibility(View.GONE);
                                    if (rel_adhar1.getVisibility() == View.VISIBLE) {
                                        txt_nofileadhar.setVisibility(View.GONE);
                                    } else {
                                        txt_nofileadhar.setVisibility(View.VISIBLE);
                                        lin_adhar_images.setVisibility(View.GONE);
                                    }
                                } else if (type.equalsIgnoreCase("trf1")) {
                                    trf_file = null;
                                    rel_trf1.setVisibility(View.GONE);
                                    if (rel_trf2.getVisibility() == View.VISIBLE) {
                                        txt_nofiletrf.setVisibility(View.GONE);
                                    } else {
                                        txt_nofiletrf.setVisibility(View.VISIBLE);
                                        lin_trf_images.setVisibility(View.GONE);
                                    }
                                } else if (type.equalsIgnoreCase("trf2")) {
                                    trf_file1 = null;
                                    rel_trf2.setVisibility(View.GONE);
                                    if (rel_trf1.getVisibility() == View.VISIBLE) {
                                        txt_nofiletrf.setVisibility(View.GONE);
                                    } else {
                                        txt_nofiletrf.setVisibility(View.VISIBLE);
                                        lin_trf_images.setVisibility(View.GONE);
                                    }
                                } else if (type.equalsIgnoreCase("vial")) {
                                    vial_file = null;
                                    txt_nofilevial.setVisibility(View.VISIBLE);
                                    txt_nofilevial.setText(getResources().getString(R.string.nofilechoosen));
                                    lin_vial_images.setVisibility(View.GONE);
                                } else if (type.equalsIgnoreCase("other")) {
                                    other_file = null;
                                    rel_other1.setVisibility(View.GONE);
                                    if (rel_other2.getVisibility() == View.VISIBLE) {
                                        txt_nofileother.setVisibility(View.GONE);
                                    } else {
                                        txt_nofileother.setVisibility(View.VISIBLE);
                                        lin_other_images.setVisibility(View.GONE);
                                    }
                                }else if (type.equalsIgnoreCase("other1")){
                                    other_file1 = null;
                                    rel_other2.setVisibility(View.GONE);
                                    if (rel_other1.getVisibility() == View.VISIBLE) {
                                        txt_nofileother.setVisibility(View.GONE);
                                    } else {
                                        txt_nofileother.setVisibility(View.VISIBLE);
                                        lin_other_images.setVisibility(View.GONE);
                                    }
                                }


                                if (presc_file == null) {
                                    btn_choosefile_presc.setBackground(getResources().getDrawable(R.color.maroon));
                                    btn_choosefile_presc.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (aadhar_file == null || aadhar_file1 == null) {
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.color.maroon));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (trf_file == null || trf_file1 == null) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.color.maroon));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (vial_file == null) {
                                    btn_choosefile_vial.setBackground(getResources().getDrawable(R.color.maroon));
                                    btn_choosefile_vial.setTextColor(getResources().getColor(R.color.white));
                                }

                                if (other_file == null || other_file1 == null) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.color.maroon));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                }

                                dialog.dismiss();
                                maindialog.dismiss();
                            }

                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.dismiss();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
//                alert.dismiss();

            }
        });

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
                maindialog.dismiss();
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.90);

        maindialog.getWindow().setLayout(width, height);
        // dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        maindialog.show();
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
                    lin_pres_preview.setVisibility(View.VISIBLE);
                    txt_nofilepresc.setVisibility(View.GONE);
                    if (presc_file != null) {
                        ispresciption = false;
                        btn_choosefile_presc.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                    }

                } else if (isadhar) {
                    if (rel_adhar1.getVisibility() == View.VISIBLE) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        aadhar_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file1;
                        aadhar_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), aadhar_file1);
                        rel_adhar2.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        aadhar_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + aadhar_file;
                        aadhar_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), aadhar_file);
                        rel_adhar1.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    }


                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                    }
                    lin_adhar_images.setVisibility(View.VISIBLE);

                } else if (istrf) {

                    if (rel_trf1.getVisibility() == View.VISIBLE) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file1;
                        trf_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file1);
                        rel_trf2.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        trf_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + trf_file;
                        trf_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), trf_file);
                        rel_trf1.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);
                    }


                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                    }
                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    bitmapimage = camera.getCameraBitmap();
                    String imageurl = camera.getCameraBitmapPath();
                    vial_file = new File(imageurl);
                    String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + vial_file;
                    vial_file = new File(destFile);
                    GlobalClass.copyFile(new File(imageurl), vial_file);
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_nofilevial.setVisibility(View.GONE);
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                    }

                } else if (isother) {
                    if (rel_other1.getVisibility() == View.VISIBLE) {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        other_file1 = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + other_file1;
                        other_file1 = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), other_file1);
                        rel_other2.setVisibility(View.VISIBLE);
                        txt_nofileother.setVisibility(View.GONE);
                    } else {
                        bitmapimage = camera.getCameraBitmap();
                        String imageurl = camera.getCameraBitmapPath();
                        other_file = new File(imageurl);
                        String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + other_file;
                        other_file = new File(destFile);
                        GlobalClass.copyFile(new File(imageurl), other_file);
                        rel_other1.setVisibility(View.VISIBLE);
                        txt_nofileother.setVisibility(View.GONE);
                    }


                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                    }
                    lin_other_images.setVisibility(View.VISIBLE);
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
                    presc_file = GlobalClass.getCompressedFile(activity, presc_file);
                    lin_pres_preview.setVisibility(View.VISIBLE);
                    txt_nofilepresc.setVisibility(View.GONE);
                    if (presc_file != null) {
                        ispresciption = false;
                        btn_choosefile_presc.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_presc.setTextColor(getResources().getColor(R.color.black));
                    }
                } else if (isadhar) {
                    if (rel_adhar1.getVisibility() == View.VISIBLE) {
                        if (data.getData() != null) {
                            if (aadhar_file1 == null) {
                                aadhar_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        aadhar_file1 = GlobalClass.getCompressedFile(activity, aadhar_file1);
                        rel_adhar2.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    } else {
                        if (data.getData() != null) {
                            if (aadhar_file == null) {
                                aadhar_file = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                        rel_adhar1.setVisibility(View.VISIBLE);
                        txt_nofileadhar.setVisibility(View.GONE);
                    }

                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                    }

                    lin_adhar_images.setVisibility(View.VISIBLE);


                } else if (istrf) {
                    if (rel_trf1.getVisibility() == View.VISIBLE) {
                        if (data.getData() != null) {
                            if (trf_file1 == null) {
                                trf_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file1 = GlobalClass.getCompressedFile(activity, trf_file1);
                        rel_trf2.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);

                    } else {
                        if (data.getData() != null) {
                            trf_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file = GlobalClass.getCompressedFile(activity, trf_file);
                        rel_trf1.setVisibility(View.VISIBLE);
                        txt_nofiletrf.setVisibility(View.GONE);
                    }


                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                    }

                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isvial) {
                    if (data.getData() != null) {
                        vial_file = FileUtil.from(activity, data.getData());
                    }
                    Uri uri = data.getData();
                    bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                    vial_file = GlobalClass.getCompressedFile(activity, vial_file);
                    lin_vial_images.setVisibility(View.VISIBLE);
                    txt_nofilevial.setVisibility(View.GONE);
                    if (vial_file != null) {
                        isvial = false;
                        btn_choosefile_vial.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_vial.setTextColor(getResources().getColor(R.color.black));
                    }
                } else if (isother) {
                    if (rel_other1.getVisibility() == View.VISIBLE) {
                        if (data.getData() != null) {
                            if (other_file1 == null) {
                                other_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        other_file1 = GlobalClass.getCompressedFile(activity, other_file1);
                        rel_other2.setVisibility(View.VISIBLE);
                        txt_nofileother.setVisibility(View.GONE);

                    } else {
                        if (data.getData() != null) {
                            other_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        other_file = GlobalClass.getCompressedFile(activity, other_file);
                        rel_other1.setVisibility(View.VISIBLE);
                        txt_nofileother.setVisibility(View.GONE);
                    }


                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.color.flouride));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                    }

                    lin_other_images.setVisibility(View.VISIBLE);
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

    public void getUploadResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);

                Intent i = new Intent(activity, ManagingTabsActivity.class);
                startActivity(i);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
}
