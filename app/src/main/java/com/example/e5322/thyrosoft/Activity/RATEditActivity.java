package com.example.e5322.thyrosoft.Activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Covidmultipart_controller;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Covidpostdata;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RATEditActivity extends AppCompatActivity {

    TextView tv_name, tv_verifiedmob;
    ConnectionDetector cd;
    Activity activity;
    String usercode, apikey;
    Button btn_choosefile_adhar, btn_choosefile_trf, btn_choosefile_other;
    ImageView back, home;
    RelativeLayout rel_mobno;
    TextView txt_nofileadhar, txt_nofiletrf, txt_nofileother;
    Button btn_submit, btn_reset;
    LinearLayout lin_adhar_images, lin_trf_images, lin_other_images;
    Bitmap bitmapimage;
    File aadhar_file = null;
    File aadhar_file1 = null;
    File trf_file = null;
    File trf_file1 = null;
    File other_file = null;
    File other_file1 = null;
    List<String> aadharlist = new ArrayList<>();
    List<String> trflist = new ArrayList<>();
    List<String> otherlist = new ArrayList<>();
    boolean isadhar, istrf, isother;
    SharedPreferences preferences;
    String mobileno, name, PatientId, ppebarcode;
    TextView txt_adharfileupload, txt_trffileupload, txt_otherfileupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_edit);

        activity = RATEditActivity.this;
        cd = new ConnectionDetector(activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileno = bundle.getString("Mobile");
            name = bundle.getString("name");
            PatientId = bundle.getString("PatientID");
            ppebarcode = bundle.getString("barcode");
        }

        initUI();
        initListeners();
    }

    private void initUI() {
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        tv_name = findViewById(R.id.tv_name);
        tv_verifiedmob = findViewById(R.id.tv_verifiedmob);
        txt_adharfileupload = findViewById(R.id.txt_adharfileupload);
        txt_trffileupload = findViewById(R.id.txt_trffileupload);
        txt_otherfileupload = findViewById(R.id.txt_otherfileupload);
        btn_choosefile_adhar = findViewById(R.id.btn_choosefile_adhar);
        btn_choosefile_trf = findViewById(R.id.btn_choosefile_trf);
        btn_choosefile_other = findViewById(R.id.btn_choosefile_other);
        rel_mobno = findViewById(R.id.rel_mobno);
        txt_nofileadhar = findViewById(R.id.txt_nofileadhar);
        txt_nofiletrf = findViewById(R.id.txt_nofiletrf);
        txt_nofileother = findViewById(R.id.txt_nofileother);
        btn_reset = findViewById(R.id.btn_reset);
        btn_submit = findViewById(R.id.btn_submit);
        lin_adhar_images = findViewById(R.id.lin_adhar_images);
        lin_trf_images = findViewById(R.id.lin_trf_images);
        lin_other_images = findViewById(R.id.lin_other_images);

        tv_name.setText(name);
        tv_verifiedmob.setText(mobileno);

        aadharlist = new ArrayList<>();
        trflist = new ArrayList<>();
        otherlist = new ArrayList<>();

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(RATEditActivity.this);
            }
        });

        txt_adharfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aadharlist != null && aadharlist.size() > 0) {
                    setViewPager(aadharlist, "adhar");
                }
            }
        });

        txt_trffileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trflist != null && trflist.size() > 0) {
                    setViewPager(trflist, "trf");
                }
            }
        });

        txt_otherfileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otherlist != null && otherlist.size() > 0) {
                    setViewPager(otherlist, "other");
                }
            }
        });

        btn_choosefile_adhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (aadhar_file != null && aadhar_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = true;
                        istrf = false;
                        isother = false;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_trf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (trf_file != null && trf_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = false;
                        istrf = true;
                        isother = false;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        btn_choosefile_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    if (other_file != null && other_file1 != null) {
                        GlobalClass.showCustomToast(activity, "You can upload only two images", 0);
                    } else {
                        isadhar = false;
                        istrf = false;
                        isother = true;
                        selectImage();
                    }
                } else {
                    requestPermission();
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {
                    if (!GlobalClass.isNetworkAvailable(activity)) {
                        GlobalClass.showAlertDialog(activity);
                    } else {

                        try {
                            if (ControllersGlobalInitialiser.covidmultipart_controller != null) {
                                ControllersGlobalInitialiser.covidmultipart_controller = null;
                            }

                            Covidpostdata covidpostdata = new Covidpostdata();
                            covidpostdata.setUNIQUEID(PatientId);
                            covidpostdata.setSOURCECODE(usercode);
                            covidpostdata.setMOBILE(mobileno);
                            covidpostdata.setNAME(name);
                            covidpostdata.setTESTCODE("CRAT");
                            covidpostdata.setBARCODE(ppebarcode);
                            covidpostdata.setAPIKEY(apikey);

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

                            new Covidmultipart_controller(RATEditActivity.this, activity, covidpostdata).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void clearFields() {
        aadhar_file = null;
        aadhar_file1 = null;
        trf_file = null;
        trf_file1 = null;
        other_file = null;
        other_file1 = null;

        lin_adhar_images.setVisibility(View.GONE);
        lin_trf_images.setVisibility(View.GONE);
        lin_other_images.setVisibility(View.GONE);
        txt_nofileadhar.setVisibility(View.VISIBLE);
        txt_nofiletrf.setVisibility(View.VISIBLE);
        txt_nofileother.setVisibility(View.VISIBLE);

        txt_nofileadhar.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileadhar.setTextColor(getResources().getColor(R.color.black));
        txt_nofileadhar.setPaintFlags(0);

        txt_nofiletrf.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofiletrf.setTextColor(getResources().getColor(R.color.black));
        txt_nofiletrf.setPaintFlags(0);

        txt_nofileother.setText(getResources().getString(R.string.nofilechoosen));
        txt_nofileother.setTextColor(getResources().getColor(R.color.black));
        txt_nofileother.setPaintFlags(0);

        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));

        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
        btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));

        aadharlist.clear();
        trflist.clear();
        otherlist.clear();

        btn_submit.setBackground(getResources().getDrawable(R.drawable.clear_button));
        btn_submit.setTextColor(getResources().getColor(R.color.black));
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
    }

    private boolean Validation() {
        if (aadhar_file == null && aadhar_file1 == null) {
            // Global.showCustomToast(activity, ToastFile.SELECT_ADHIMAGE);
            return false;
        }

        if (trf_file == null && trf_file1 == null) {
            //   Global.showCustomToast(activity, ToastFile.SELECT_TRFDHIMAGE);
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
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void selectImage() {
        ImagePicker.Companion.with(RATEditActivity.this)
                .galleryOnly()
                .crop()
                .compress(Constants.MaxImageSize)
                .maxResultSize(Constants.MaxImageWidth, Constants.MaxImageHeight)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(activity, "Failed to load image!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (isadhar) {
                    if (lin_adhar_images.getVisibility() == View.VISIBLE && aadhar_file != null) {
                        if (data.getData() != null) {
                            if (aadhar_file1 == null) {
                                aadhar_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        aadhar_file1 = GlobalClass.getCompressedFile(activity, aadhar_file1);
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            if (aadhar_file == null) {
                                aadhar_file = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        aadhar_file = GlobalClass.getCompressedFile(activity, aadhar_file);
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileadhar.setVisibility(View.GONE);
                        aadharlist.add(aadhar_file.toString());
                    }
                    if (aadhar_file != null && aadhar_file1 != null) {
                        isadhar = false;
                        lin_adhar_images.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setVisibility(View.VISIBLE);
                        txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                    }
                    lin_adhar_images.setVisibility(View.VISIBLE);
                } else if (istrf) {
                    if (lin_trf_images.getVisibility() == View.VISIBLE && trf_file != null) {
                        if (data.getData() != null) {
                            if (trf_file1 == null) {
                                trf_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file1 = GlobalClass.getCompressedFile(activity, trf_file1);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            trf_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        trf_file = GlobalClass.getCompressedFile(activity, trf_file);
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofiletrf.setVisibility(View.GONE);
                        trflist.add(trf_file.toString());
                    }
                    if (trf_file != null && trf_file1 != null) {
                        istrf = false;
                        lin_trf_images.setVisibility(View.VISIBLE);
                        txt_trffileupload.setVisibility(View.VISIBLE);
                        txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                    }
                    lin_trf_images.setVisibility(View.VISIBLE);
                } else if (isother) {
                    if (lin_other_images.getVisibility() == View.VISIBLE && other_file != null) {
                        if (data.getData() != null) {
                            if (other_file1 == null) {
                                other_file1 = FileUtil.from(activity, data.getData());
                            }
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        other_file1 = GlobalClass.getCompressedFile(activity, other_file1);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file1.toString());
                    } else {
                        if (data.getData() != null) {
                            other_file = FileUtil.from(activity, data.getData());
                        }
                        Uri uri = data.getData();
                        bitmapimage = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                        other_file = GlobalClass.getCompressedFile(activity, other_file);
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        txt_nofileother.setVisibility(View.GONE);
                        otherlist.add(other_file.toString());
                    }
                    if (other_file != null && other_file1 != null) {
                        isother = false;
                        btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                        btn_choosefile_other.setTextColor(getResources().getColor(R.color.black));
                        lin_other_images.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setVisibility(View.VISIBLE);
                        txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                        txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    }
                    lin_other_images.setVisibility(View.VISIBLE);
                }
                buttonVal();
            } catch (IOException e) {
                Toast.makeText(activity, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void getUploadResponse(String response,String mobile) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String RESPONSE = jsonObject.optString("Response");
            String RESPONSEID = jsonObject.optString("ResId");

            if (RESPONSEID.equalsIgnoreCase(Constants.RES0000)) {
                Global.showCustomToast(activity, RESPONSE);
                new LogUserActivityTagging(activity,"WOE-NOVID",mobile);
                Intent i = new Intent(activity, ManagingTabsActivity.class);
                startActivity(i);
                Constants.ratfrag_flag = "1";
                finish();
            } else {
                Global.showCustomToast(activity, RESPONSE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buttonVal() {
        if (Validation()) {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.btn_bg));
            btn_submit.setTextColor(getResources().getColor(R.color.white));
            btn_submit.setEnabled(true);
            btn_submit.setClickable(true);
        } else {
            btn_submit.setBackground(getResources().getDrawable(R.drawable.clear_button));
            btn_submit.setTextColor(getResources().getColor(R.color.black));
            btn_submit.setEnabled(false);
            btn_submit.setClickable(false);
        }
    }

    public void setViewPager(List<String> imagelist, final String type) {
        final Dialog maindialog = new Dialog(activity);
        maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        maindialog.setContentView(R.layout.preview_dialog);
        maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity, imagelist, 0);
        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();

        final PageIndicatorView pageIndicatorView = maindialog.findViewById(R.id.pageIndicatorView);
        if (imagelist != null && imagelist.size() > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
            pageIndicatorView.setCount(imagelist.size()); // specify total count of indicators
            pageIndicatorView.setSelection(0);
            pageIndicatorView.setSelectedColor(activity.getResources().getColor(R.color.maroon));
        } else {
            pageIndicatorView.setVisibility(View.GONE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
                pageIndicatorView.setSelectedColor(getResources().getColor(R.color.maroon));
                pageIndicatorView.setUnselectedColor(getResources().getColor(R.color.grey));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Button btn_delete = maindialog.findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.VISIBLE);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Set the Alert Dialog Message
                builder.setMessage(ToastFile.deletefile);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (type.equalsIgnoreCase("adhar")) {
                            if (aadharlist != null && aadharlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (aadhar_file != null) {
                                        aadhar_file = null;
                                    } else {
                                        aadhar_file1 = null;
                                    }
                                    aadharlist.remove(aadharlist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    aadhar_file1 = null;
                                    if (aadharlist.size() == 2) {
                                        aadharlist.remove(aadharlist.get(1));
                                    } else {
                                        aadharlist.remove(aadharlist.get(0));
                                    }
                                }
                                if (aadharlist.size() == 1) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
                                    txt_adharfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (aadharlist.size() == 2) {
                                    lin_adhar_images.setVisibility(View.VISIBLE);
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.black));
                                    txt_adharfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_adharfileupload.setPaintFlags(txt_adharfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_adhar.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_adhar.setTextColor(getResources().getColor(R.color.white));
                                    txt_nofileadhar.setVisibility(View.VISIBLE);
                                    txt_adharfileupload.setVisibility(View.GONE);
                                }
                                buttonVal();
                            } else {
                                txt_nofileadhar.setVisibility(View.VISIBLE);
                                lin_adhar_images.setVisibility(View.GONE);
                            }
                        } else if (type.equalsIgnoreCase("trf")) {
                            if (trflist != null && trflist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (trf_file != null) {
                                        trf_file = null;
                                    } else {
                                        trf_file1 = null;
                                    }
                                    trflist.remove(trflist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    trf_file1 = null;
                                    if (trflist.size() == 2) {
                                        trflist.remove(trflist.get(1));
                                    } else {
                                        trflist.remove(trflist.get(0));
                                    }
                                }
                                if (trflist.size() == 1) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (trflist.size() == 2) {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.covidgreybtn));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.black));
                                    lin_trf_images.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_trffileupload.setPaintFlags(txt_trffileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_trf.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_trf.setTextColor(getResources().getColor(R.color.white));
                                    txt_nofiletrf.setVisibility(View.VISIBLE);
                                    txt_trffileupload.setVisibility(View.GONE);
                                }
                            } else {
                                txt_nofiletrf.setVisibility(View.VISIBLE);
                                lin_trf_images.setVisibility(View.GONE);
                            }
                            buttonVal();
                        } else if (type.equalsIgnoreCase("other")) {
                            if (otherlist != null && otherlist.size() > 0) {
                                if (0 == viewPager.getCurrentItem()) {
                                    if (other_file != null) {
                                        other_file = null;
                                    } else {
                                        other_file1 = null;
                                    }
                                    otherlist.remove(otherlist.get(0));
                                } else if (1 == viewPager.getCurrentItem()) {
                                    other_file1 = null;
                                    if (otherlist.size() == 2) {
                                        otherlist.remove(otherlist.get(1));
                                    } else {
                                        otherlist.remove(otherlist.get(0));
                                    }
                                }
                                if (otherlist.size() == 1) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("1 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else if (otherlist.size() == 2) {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    lin_other_images.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setText("2 " + getResources().getString(R.string.imgupload));
                                    txt_otherfileupload.setPaintFlags(txt_otherfileupload.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                } else {
                                    btn_choosefile_other.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                                    btn_choosefile_other.setTextColor(getResources().getColor(R.color.white));
                                    txt_nofileother.setVisibility(View.VISIBLE);
                                    txt_otherfileupload.setVisibility(View.GONE);
                                }
                            } else {
                                txt_nofileother.setVisibility(View.VISIBLE);
                                lin_other_images.setVisibility(View.GONE);
                            }
                            buttonVal();
                        }
                        dialog.dismiss();
                        maindialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        ImageView ic_close = (ImageView) maindialog.findViewById(R.id.img_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maindialog.dismiss();
            }
        });

        maindialog.setCancelable(true);
        maindialog.show();
    }
}