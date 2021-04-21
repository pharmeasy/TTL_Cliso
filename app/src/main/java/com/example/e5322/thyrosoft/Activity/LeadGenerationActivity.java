package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Controller.LeadChannelController;
import com.example.e5322.thyrosoft.Controller.LeadPurposeController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.PostLeadsController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.LeadChannelRespModel;
import com.example.e5322.thyrosoft.Models.LeadDataResponseModel;
import com.example.e5322.thyrosoft.Models.LeadPurposeResponseModel;
import com.example.e5322.thyrosoft.Models.LeadReqModel;
import com.example.e5322.thyrosoft.Models.LeadRespModel;
import com.example.e5322.thyrosoft.Models.LeadResponseModel;
import com.example.e5322.thyrosoft.Models.TestBookingResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mindorks.paracamera.Camera;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;

public class LeadGenerationActivity extends AppCompatActivity {

    EditText edt_name, edt_mobile, edt_mail, edt_address, edt_pincode, edt_remarks;
    Spinner spr_package;
    Button btn_submit;
    private ArrayList<LeadResponseModel.ProductlistBean> leadResponseModel;
    private ArrayList<String> leadpurpsearay = new ArrayList<>();
    TextView tv_reset;
    LeadDataResponseModel leadDataResponseModel;
    private MediaRecorder mediaRecorder;
    String TAG = getClass().getSimpleName();
    private int status_code = 0;
    String Product_id, tests;
    int Rate, is_booking, pass_rate = 0;
    String pass_product = "", pass_reportcode = "";
    private ConnectionDetector cd;
    private TestBookingResponseModel testBookingResponseModel;
    private Activity mActivity;
    private String name, mobile, email, address, userChoosenTask, remarks;
    ImageView back, home;
    private String type = "TEST";
    private String user, api_key;
    private RelativeLayout rel_upload_img, rel_upload_voice;
    private static final int PERMISSION_REQUEST_CODE = 200;
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
    private int PICK_PHOTO_FROM_GALLERY = 202;
    Spinner spr_leadpurpose;
    LinearLayout ll_upload, ll_purpose;

    LinearLayout ll_channel;
    Spinner spr_channel, spr_from;
    TextView tv_spr_purpose;
    private LeadPurposeResponseModel leadmodel;

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
        // lead_id = GlobalClass.generateBookingID();

        prefs_user = mActivity.getSharedPreferences("Userdetails", 0);

        init();
        GetLeadPurpose();
        //  GetLeadChannel();
        initlistner();

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
    }

    private void GetLeadPurpose() {
        if (cd.isConnectingToInternet()) {
            LeadPurposeController leadPurposeController = new LeadPurposeController(LeadGenerationActivity.this);
            leadPurposeController.GetLeadPurpose();
        } else {
            GlobalClass.showCustomToast(mActivity, ToastFile.intConnection, 1);
        }
    }

    private void GetLeadChannel() {
        if (cd.isConnectingToInternet()) {
            LeadChannelController leadChannelController = new LeadChannelController(LeadGenerationActivity.this);
            leadChannelController.GetLeadChannel();
        } else {
            GlobalClass.showCustomToast(mActivity, ToastFile.intConnection, 1);
        }
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
                if (checkCameraPermission()) {
                    selectImage();
                } else {
                    requestCameraPermission();
                }
            }
        });

        spr_leadpurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (leadmodel != null) {
                    if (GlobalClass.CheckArrayList(leadmodel.getPurposeList())) {
                        if (spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Select*")) {
                            tv_spr_purpose.setVisibility(View.GONE);
                            edt_name.setVisibility(View.GONE);
                            edt_mobile.setVisibility(View.GONE);
                            edt_remarks.setVisibility(View.GONE);
                            ll_upload.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.GONE);
                            tv_reset.setVisibility(View.GONE);
                        } else {
                            btn_submit.setVisibility(View.VISIBLE);
                            tv_reset.setVisibility(View.VISIBLE);

                            for (int i = 0; i < leadmodel.getPurposeList().size(); i++) {
                                if (spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase(leadmodel.getPurposeList().get(i).getData())) {
                                    if (!GlobalClass.isNull(leadmodel.getPurposeList().get(i).getRemarks())) {
                                        tv_spr_purpose.setVisibility(View.VISIBLE);
                                        tv_spr_purpose.setText("" + leadmodel.getPurposeList().get(i).getRemarks());
                                    } else {
                                        tv_spr_purpose.setVisibility(View.GONE);
                                    }

                                }
                            }


                            if (spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Order") || spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Orders")) {
                                ll_upload.setVisibility(View.VISIBLE);
                                edt_name.setVisibility(View.VISIBLE);
                                edt_mobile.setVisibility(View.VISIBLE);
                                edt_remarks.setVisibility(View.VISIBLE);
                                ll_channel.setVisibility(View.GONE);
                                edt_remarks.setHint("Remarks");
                            } else {
                                ll_upload.setVisibility(View.GONE);
                                edt_remarks.setHint("Remarks");
                                edt_name.setVisibility(View.VISIBLE);
                                edt_mobile.setVisibility(View.VISIBLE);
                                edt_remarks.setVisibility(View.VISIBLE);
                                // ll_channel.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        if (leadResponseModel != null) {
                            if (leadResponseModel.size() != 0) {
                                Product_id = leadResponseModel.get(i).getProduct_id();
                                tests = leadResponseModel.get(i).getTests();
                                is_booking = leadResponseModel.get(i).getIs_booking();
                                Rate = leadResponseModel.get(i).getRate();
                            }
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

                nameWithProperSpacing = edt_name.getText().toString().toUpperCase().trim();
                name = nameWithProperSpacing.replaceAll("\\s+", " ");
                mobile = edt_mobile.getText().toString();
                email = edt_mail.getText().toString();
                address = edt_address.getText().toString();
                remarks = edt_remarks.getText().toString().trim();

                LeadReqModel leadReqModel = new LeadReqModel();
                try {
                    if (ll_purpose.getVisibility() == View.VISIBLE) {
                        leadReqModel.setName(name);
                        leadReqModel.setMobile(mobile);
                        leadReqModel.setAddress(address);
                        leadReqModel.setPincode(edt_pincode.getText().toString());
                        leadReqModel.setRemarks(remarks + " ~CLISO");
                        leadReqModel.setEmail(email);
                        leadReqModel.setPurpose(spr_leadpurpose.getSelectedItem().toString());
                        leadReqModel.setAppName("CLISO");
                        leadReqModel.setEntryBy(prefs_user.getString("Username", ""));
//                        leadReqModel.setChannel(spr_channel.getSelectedItem().toString());
//                        leadReqModel.setFrom(spr_from.getSelectedItem().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (cd.isConnectingToInternet()) {
                    if (Validate()) {
                        if (edt_mail.getText().toString().length() > 0) {
                            if (emailValidation()) {
                                if (checkRemarksValidation()) {
                                    if (cd.isConnectingToInternet()) {
                                        if (spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Order") || spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Orders")) {
                                            new AsyncTaskPost_uploadfile(name, mobile, email, address, remarks, type, imagefile, f_AudioSavePathInDevice).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } else {
                                            PostLeadsController postLeadsController = new PostLeadsController(LeadGenerationActivity.this);
                                            postLeadsController.CallAPI(leadReqModel);
                                        }
                                    } else {
                                        GlobalClass.showCustomToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 0);
                                    }
                                }
                            }
                        } else {
                            if (checkRemarksValidation()) {
                                if (spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Order") || spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Orders")) {
                                    new AsyncTaskPost_uploadfile(name, mobile, email, address, remarks, type, imagefile, f_AudioSavePathInDevice).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    PostLeadsController postLeadsController = new PostLeadsController(LeadGenerationActivity.this);
                                    postLeadsController.CallAPI(leadReqModel);
                                }
                            }
                        }
                    }
                } else {
                    GlobalClass.showCustomToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 0);
                }


            }
        });
    }

    private boolean checkRemarksValidation() {
        if (edt_remarks.getText().toString().startsWith(",")) {
            GlobalClass.showCustomToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPECIAL_CHAR, 0);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(".")) {
            GlobalClass.showCustomToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPECIAL_CHAR, 0);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith("-")) {
            GlobalClass.showCustomToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPECIAL_CHAR, 0);
            edt_remarks.requestFocus();
            return false;
        }
        if (edt_remarks.getText().toString().startsWith(" ")) {
            GlobalClass.showCustomToast(mActivity, MessageConstants.REMARKS_SHOULD_NOT_START_SPACE, 0);
            edt_remarks.requestFocus();
            return false;
        }
        return true;
    }

    private boolean emailValidation() {
        if (edt_mail.getText().toString().startsWith(" ")) {
            GlobalClass.showCustomToast(mActivity, "Email ID should not start with space", 0);
            edt_mail.requestFocus();
            return false;
        }
        return true;
    }

    private void clearfileds() {
        try {
            edt_name.requestFocus();
            edt_name.setText("");
            edt_mobile.setText("");
            edt_mail.setText("");
            edt_address.setText("");
            edt_pincode.setText("");
            edt_remarks.setText("");

            //  spr_package.setSelection(0);

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
            spr_leadpurpose.setSelection(0);
            tv_spr_purpose.setText("");
            tv_spr_purpose.setVisibility(View.GONE);
            spr_channel.setSelection(0);
            spr_from.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectImage() {
        CharSequence[] items = new CharSequence[0];
        if (imagefile != null) {
            if (imagefile.exists()) {
                items = new CharSequence[]{"Discard Previous Image", "Take Photo", "Choose from gallery", "Cancel"};
            }
        } else {
            items = new CharSequence[]{"Take Photo", "Choose from gallery", "Cancel"};
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
                } else if (finalItems[item].equals("Choose from gallery")) {
                    chooseFromGallery();
                } else if (finalItems[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                GlobalClass.printLog(Constants.ERROR, TAG, "Image path:-", "" + imagefile);
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
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_PHOTO_FROM_CAMERA);*/

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
                .setCompression(Constants.setcompression)
                .setImageHeight(Constants.setheight)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private boolean Validate() {
        if (edt_name.getText().toString().equals("")) {
            edt_name.setError("Enter Valid Name");
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().length() < 2) {
            edt_name.setError("Minimum length should be 2");
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().startsWith(" ")) {
            edt_name.setError("Cannot start with Space");
            edt_name.requestFocus();
            return false;
        }

        if (edt_name.getText().toString().contains("  ")) {
            edt_name.setError("Cannot have Double Space");
            edt_name.requestFocus();
            return false;
        }

        if (edt_mobile.getText().length() != 10) {
            edt_mobile.setError("Enter Valid Number");
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
                edt_address.setError("Enter Valid Address");
                edt_address.requestFocus();
                return false;
            }

            if (edt_address.getText().toString().length() < 2) {
                edt_address.setError("Minimum length should be 2");
                edt_address.requestFocus();
                return false;
            }

            if (edt_address.getText().toString().startsWith(" ")) {
                edt_address.setError("Cannot start with Space");
                edt_address.requestFocus();
                return false;
            }

            if (edt_address.getText().toString().contains("  ")) {
                edt_address.setError("Cannot have Double Space");
                edt_address.requestFocus();
                return false;
            }
        }


        if (!GlobalClass.isNull(edt_pincode.getText().toString())) {
            if (edt_pincode.getText().toString().equals("")) {
                edt_pincode.setError("Enter Valid Pincode");
                edt_pincode.requestFocus();
                return false;
            }

            if (edt_pincode.getText().toString().startsWith("0")) {
                edt_pincode.setError("Cannot start with 0");
                edt_pincode.requestFocus();
                return false;
            }

            if (edt_pincode.getText().toString().startsWith("9")) {
                edt_pincode.setError("Cannot start with 9");
                edt_pincode.requestFocus();
                return false;
            }

            if (edt_pincode.getText().length() < 6) {
                edt_pincode.setError("Enter Valid Pincode");
                edt_pincode.requestFocus();
                return false;
            }
        }

        if (!GlobalClass.isNull(edt_remarks.getText().toString())) {
            if (edt_remarks.getText().toString().startsWith(" ")) {
                edt_remarks.setError("Cannot start with Space");
                edt_remarks.requestFocus();
                return false;
            }
        }

       /* try {
            if (!spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Order") || spr_leadpurpose.getSelectedItem().toString().equalsIgnoreCase("Orders")) {
                if (spr_channel.getSelectedItem().toString().equalsIgnoreCase("Select Channel*")) {
                    Toast.makeText(mActivity, "Select Channel", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (spr_from.getSelectedItem().toString().equalsIgnoreCase("Select From*")) {
                    Toast.makeText(mActivity, "Select From", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }*/

       /* if (spr_package.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Select Package Name", Toast.LENGTH_SHORT).show();
            return false;
        }
*/

        return true;
    }


    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(mActivity, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    public void init() {
        SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        api_key = "DAuWZtKHyVb8OhjhHSuAwCB6uAyzVzASVTxXcCT8@0fkrvZ5fG9lw533tKKmxVnu";
        mobile = prefs.getString("mobile_user", "");

        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        edt_name = findViewById(R.id.edt_name);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_address = findViewById(R.id.edt_address);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_remarks = findViewById(R.id.edt_remarks);
        edt_mail = findViewById(R.id.edt_mail);
        tv_reset = findViewById(R.id.tv_reset);
        tv_reset.setText(Html.fromHtml("<u>" + "Reset" + "</u>"));
        spr_package = findViewById(R.id.spr_package);
        btn_submit = findViewById(R.id.btn_submit);
        ll_upload = findViewById(R.id.ll_upload);
        ll_purpose = findViewById(R.id.ll_purpose);
        spr_leadpurpose = findViewById(R.id.spr_leadpurpose);
        ll_channel = findViewById(R.id.ll_channel);
        spr_channel = findViewById(R.id.spr_channel);
        spr_from = findViewById(R.id.spr_from);
        img_tick = (ImageView) findViewById(R.id.img_tick);
        img_tick2 = (ImageView) findViewById(R.id.img_tick2);


        rel_upload_img = (RelativeLayout) findViewById(R.id.rel_upload_img);
        rel_upload_voice = (RelativeLayout) findViewById(R.id.rel_upload_voice);

        tv_spr_purpose = findViewById(R.id.tv_spr_purpose);

        // edt_address.setFilters(new InputFilter[]{EMOJI_FILTER});

        edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {

                    Toast.makeText(LeadGenerationActivity.this, ToastFile.crt_name, Toast.LENGTH_SHORT).show();

                    if (enteredString.length() > 0) {
                        edt_address.setText(enteredString.substring(1));
                    } else {
                        edt_address.setText("");
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

                    TastyToast.makeText(LeadGenerationActivity.this, ToastFile.crt_txt, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                    if (enteredString.length() > 0) {
                        edt_remarks.setText(enteredString.substring(1));
                    } else {
                        edt_remarks.setText("");
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

    public void getleadresponse(LeadResponseModel body) {
        try {
            CallLeadResponse(body.getProductlist());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallLeadResponse(ArrayList<LeadResponseModel.ProductlistBean> productlist) {

        leadResponseModel = new ArrayList<>();
        LeadResponseModel.ProductlistBean ent = new LeadResponseModel.ProductlistBean();
        ent.setProduct_name("Select Package*");
        leadResponseModel.add(ent);

        if (productlist != null) {
            for (int i = 0; i < productlist.size(); i++) {
                leadResponseModel.add(productlist.get(i));
            }

        }

        ArrayAdapter<LeadResponseModel.ProductlistBean> adapter = new ArrayAdapter<LeadResponseModel.ProductlistBean>(this, R.layout.spinner_layout, leadResponseModel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_package.setAdapter(adapter);
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

    public void getLeadPurpose(LeadPurposeResponseModel leadPurposeResponseModel) {
        if (leadPurposeResponseModel == null) {
            ll_purpose.setVisibility(View.GONE);
        } else {
            leadmodel = leadPurposeResponseModel;
            ll_purpose.setVisibility(View.VISIBLE);
            if (!GlobalClass.isNull(leadPurposeResponseModel.getRespId()) && leadPurposeResponseModel.getRespId().equalsIgnoreCase("RES00001")) {
                if (GlobalClass.CheckArrayList(leadPurposeResponseModel.getPurposeList())) {
                    leadpurpsearay.add(0,"Select*");
                    for (int i = 0; i < leadPurposeResponseModel.getPurposeList().size(); i++) {
                        if (!GlobalClass.isNull(leadPurposeResponseModel.getPurposeList().get(i).getData())) {
                            leadpurpsearay.add(leadPurposeResponseModel.getPurposeList().get(i).getData());
                        }
                    }
                }

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mActivity, R.layout.spinner_layout, leadpurpsearay);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spr_leadpurpose.setAdapter(adapter2);

            } else {
                GlobalClass.showShortToast(mActivity, leadPurposeResponseModel.getResponse());
            }


        }
    }

    public void getpostresponse(LeadRespModel leadResponseModel) {
        if (!GlobalClass.isNull(leadResponseModel.getRespId()) && leadResponseModel.getRespId().equalsIgnoreCase("RES02024")) {
            clearfileds();
        }
        Global.showCustomToast(mActivity, leadResponseModel.getResponse());
    }

    public void getLeadChannel(LeadChannelRespModel leadChannelRespModel) {

        ArrayList<String> channelList = new ArrayList<>();
        ArrayList<String> fromList = new ArrayList<>();


        channelList.add("Select Channel*");
        fromList.add("Select From*");
        if (leadChannelRespModel != null) {
            if (!GlobalClass.isNull(leadChannelRespModel.getRespId()) && leadChannelRespModel.getRespId().equalsIgnoreCase("RES00001")) {

                if (GlobalClass.CheckArrayList(leadChannelRespModel.getChannelList())) {
                    for (int i = 0; i < leadChannelRespModel.getChannelList().size(); i++) {
                        if (!GlobalClass.isNull(leadChannelRespModel.getChannelList().get(i).getData())) {
                            channelList.add(leadChannelRespModel.getChannelList().get(i).getData());
                        }
                    }
                }

                if (GlobalClass.CheckArrayList(leadChannelRespModel.getFromList())) {
                    for (int i = 0; i < leadChannelRespModel.getFromList().size(); i++) {
                        if (!GlobalClass.isNull(leadChannelRespModel.getFromList().get(i).getData())) {
                            fromList.add(leadChannelRespModel.getFromList().get(i).getData());
                        }
                    }
                }

                ArrayAdapter<String> chanel = new ArrayAdapter<String>(mActivity, R.layout.spinner_layout, channelList);
                chanel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_channel.setAdapter(chanel);

                ArrayAdapter<String> from = new ArrayAdapter<String>(mActivity, R.layout.spinner_layout, fromList);
                from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_from.setAdapter(from);
            }
        } else {
            ll_channel.setVisibility(View.GONE);
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
//                    f_AudioSavePathInDevice.delete();
                }
//                f_AudioSavePathInDevice = null;
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
                    // Dismiss the popup window
                    customDialogClass.cancel();
                    /*if (f_AudioSavePathInDevice != null) {
                        if (f_AudioSavePathInDevice.exists()) {
                            f_AudioSavePathInDevice.delete();
                        }
                        f_AudioSavePathInDevice = null;
                    }
                    img_tick2.setVisibility(View.INVISIBLE);*/
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
                            if (type.equals("TEST")) {
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
                            tv_record_title.setText(getString(R.string.stop_record));
                            closeButton.setClickable(false);
                            img_record.setImageResource(R.drawable.ic_stop_record);
                            btn_save.setEnabled(false);
                            btn_save.setBackgroundResource(R.drawable.btn_disabled_bg);
                            btn_discard.setEnabled(false);
                            btn_discard.setBackgroundResource(R.drawable.btn_disabled_bg);
                            GlobalClass.showCustomToast(mActivity, "Recording started", 0);
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

                        tv_record_title.setText(getString(R.string.start_record));
                        closeButton.setClickable(true);
                        img_record.setImageResource(R.drawable.ic_start_record);
                        btn_save.setEnabled(true);
                        btn_save.setBackgroundResource(R.drawable.bg_bg1);
                        btn_discard.setEnabled(true);
                        btn_discard.setBackgroundResource(R.drawable.bg_bg1);
                        GlobalClass.showCustomToast(mActivity, "Recording completed", 0);
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

        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(mActivity, ToastFile.failed_to_open, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                imagefile = FileUtil.from(this, data.getData());
                imagefile = GlobalClass.getCompressedFile(mActivity, imagefile);
                img_tick.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Toast.makeText(mActivity, "Failed to read image data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
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
            //System.out.println(strUrl);

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
                builder.addPart("ref_code", new StringBody("" + prefs_user.getString("Username", "")));
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
                httpPost.setHeader(Constants.HEADER_USER_AGENT, GlobalClass.getHeaderValue(mActivity));
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                GlobalClass.printLog(Constants.ERROR, TAG, "Status Line: ", "" + httpResponse.getStatusLine());
                status_code = httpResponse.getStatusLine().getStatusCode();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    System.out.println("Response : " + result);
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
//                        Global.showCustomToast(mActivity, "Test booking done successfully !");
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

                        booking_ID.setText(testBookingResponseModel.getREF_ORDERID());
                        tv_name.setText(name);
                        tv_mobile.setText(mobile);

                     /*   if (email.isEmpty()) {
                            ll_email_id.setVisibility(View.GONE);
                        } else {
                            tv_email_id.setText(email);
                        }

                        if (address.isEmpty()) {
                            ll_address.setVisibility(View.GONE);
                        } else {
                            tv_city.setText(address);
                        }

                        if (remarks.isEmpty()) {
                            ll_remarks.setVisibility(View.GONE);
                        } else {
                            tv_remarks.setText(remarks);
                        }*/

                        note.setText(name + " will be contacted for availing service.");

                        img_whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (GlobalClass.isWhatsApp(mActivity)) {
                                    ShareModeType = "WhatsApp";
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl(name, mobile, testBookingResponseModel.getREF_ORDERID(), type)));
                                    startActivity(intent);
                                } else {
                                    GlobalClass.showCustomToast(mActivity, "WhatsApp not Installed", 0);
                                }
                            }
                        });

                        img_mail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ShareModeType = "Email";
                                Intent emailintent = new Intent(Intent.ACTION_SEND);
                                emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                if (type.contains("TEST")) {
                                    emailintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.test_subject));

                                    emailintent.putExtra(Intent.EXTRA_TEXT, "Lead ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                                }
                                emailintent.setType("message/rfc822");
                                try {
                                    startActivity(Intent.createChooser(emailintent, "Choose an Email client to which you want to share details:"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    GlobalClass.showCustomToast(mActivity, "There is no email client installed", 0);
                                }
                            }
                        });

                        img_sms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ShareModeType = "SMS";
                                Uri uri = Uri.parse("smsto:" + mobile);
                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                                if (type.contains("TEST")) {
                                    /*if (email.isEmpty() && address.isEmpty() && remarks.isEmpty()) {
                                        sendIntent.putExtra("sms_body", getString(R.string.test_subject) + "\n" + "Lead ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                                    } else {

                                    }*/

                                    sendIntent.putExtra("sms_body", getString(R.string.test_subject) + "\n" + "Lead ID :- " + testBookingResponseModel.getREF_ORDERID() + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile);
                                }
                                startActivity(sendIntent);
                            }
                        });

                        CustomDialogforSuccess.show();
                    } else if (testBookingResponseModel.getRESPONSE().contains("MOBILE NUMBER. IS ALREADY HAS BEEN USED")) {
                        alertDialogBuilder = new AlertDialog.Builder(mActivity);
                        alertDialogBuilder
                                .setMessage(MessageConstants.MOBILE_NO_USED_10_TIMES)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                GlobalClass.showCustomToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 0);
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
        if (type.contains("TEST")) {
            TEXT_VALUE = "Test details" + "\n" + "Lead ID :- " + booking_id + "\n" + "Name :- " + name + "\n" + "Mobile :- " + mobile;
        }
        String newUrl = BASE_URL + "send";
        Uri builtUri = Uri.parse(newUrl).buildUpon()
                .appendQueryParameter(PARAM_PHONE_NUMBER, WHATSAPP_PHONE_NUMBER)
                .appendQueryParameter(PARAM_TEXT, TEXT_VALUE)
                .build();

        return GlobalClass.buildWhatsappUrl(builtUri).toString();
    }

    public class ValidateEmailAsyncTask extends AsyncTask<Void, Void, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = GlobalClass.ShowprogressDialog(LeadGenerationActivity.this);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String strUrl = Api.LIVEAPI + "MASTER.svc/EmailValidate";
            Log.e(TAG, "EMAIL URL--->" + strUrl);

            JSONObject jobj = new JSONObject();
            try {
                jobj.put("AppID", "2");
                jobj.put("EmailID", edt_mail.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.v(strUrl);

            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                String strJson = "";
                strJson = jobj.toString();
                Log.v("Sending data: ", strJson);
                StringEntity se = new StringEntity(strJson);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.v("Response : ", result);
                }

            } catch (Exception e) {
                GlobalClass.printLog(Constants.DEBUG, TAG, "InputStream", e.getLocalizedMessage());
            }

            JSONObject json = null;
            try {

                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            boolean isemailvalid = false;
            if (result != null) {
                String isSuccess = result.optString("Sucess", "");
                String isValid = result.optString("Message", "");
                if (isSuccess.equalsIgnoreCase("TRUE")) {
                    isemailvalid = true;
                } else {
                    isemailvalid = false;
                }
            } else {
                isemailvalid = false;
            }
            GlobalClass.hideProgress(LeadGenerationActivity.this, progressDialog);

            if (checkRemarksValidation()) {
                if (!isemailvalid) {
                    GlobalClass.showCustomToast(mActivity, MessageConstants.INVALID_EMAIL, 0);
                    edt_mail.requestFocus();
                } else {
                    name = edt_name.getText().toString().toUpperCase().trim();
                    mobile = edt_mobile.getText().toString();
                    email = edt_mail.getText().toString();
                    address = edt_address.getText().toString();
                    remarks = edt_remarks.getText().toString().trim();

                    if (cd.isConnectingToInternet()) {
                        new AsyncTaskPost_uploadfile(name, mobile, email, address, remarks, type, imagefile, f_AudioSavePathInDevice).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        GlobalClass.showCustomToast(mActivity, MessageConstants.CHECK_INTERNET_CONN, 0);
                    }
                }
            }
        }
    }


}

