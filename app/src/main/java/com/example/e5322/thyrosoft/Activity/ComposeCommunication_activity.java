package com.example.e5322.thyrosoft.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.CommonItils.AccessRuntimePermissions;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.MultipartComposeCommController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ComposeCommPOSTModel;
import com.example.e5322.thyrosoft.Models.FileUtil;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ComposeCommunication_activity extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
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
    private static Dialog dialog;
    Spinner spinnercomm;
    ConnectionDetector cd;
    EditText commuTXT;
    ImageView back, home, iv_tick_voice, iv_tick_img, iv_preview, iv_playAudio;
    LinearLayout parent_ll, offline_img;
    Button sendcomm, btn_reset;
    private LinearLayout ll_upVoice, ll_upImage;
    private TextView tv_UpVoice, tv_UpImage;
    private String TAG = ManagingTabsActivity.class.getSimpleName();
    private Activity mActivity;
    private int PERMISSION_REQUEST_CODE = 200;
    private String userChoosenTask;
    private int PICK_PHOTO_FROM_GALLERY = 202;
    private Camera camera;
    private File selectedFile = null;
    private File tmp_selectedFile = null;
    private MediaRecorder mediaRecorder;
    private String audioPath = null;
    private Boolean ButtonClicked = false;
    private MediaPlayer mediaPlayer;
    private RadioButton rd_audio, rd_image;
    private boolean audioChecked, imageChecked;
    private String strForwardTo;
    private boolean flag = false;
    private boolean audioflag = false;
    private String temp_audioPath = null;
    boolean mediarecorderflag = false;
    ArrayList<CommunicationMaster> commSpinner = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compose_communication);

        mActivity = ComposeCommunication_activity.this;
        cd = new ConnectionDetector(mActivity);

        if (getIntent().getExtras() != null) {
            commSpinner = getIntent().getExtras().getParcelableArrayList("commSpinner");
        }

        initUI();

        if (!GlobalClass.isNetworkAvailable(ComposeCommunication_activity.this)) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
        }

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        commuTXT.setFilters(new InputFilter[]{EMOJI_FILTER});

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("-Select-");


        if (GlobalClass.CheckArrayList(commSpinner)) {
            for (int i = 0; i < commSpinner.size(); i++) {
                stringArrayList.add(commSpinner.get(i).getDisplayName());
            }
        }

        ArrayAdapter aa = new ArrayAdapter(mActivity, android.R.layout.simple_spinner_item, stringArrayList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercomm.setAdapter(aa);

        initListeners();
    }

    private void initUI() {
        spinnercomm = (Spinner) findViewById(R.id.spinnercomm);
        sendcomm = (Button) findViewById(R.id.sendcomm);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        commuTXT = (EditText) findViewById(R.id.commuTXT);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        offline_img = (LinearLayout) findViewById(R.id.offline_img);
        parent_ll = (LinearLayout) findViewById(R.id.parent_ll);
        ll_upVoice = (LinearLayout) findViewById(R.id.ll_upVoice);
        ll_upImage = (LinearLayout) findViewById(R.id.ll_upImage);
        tv_UpVoice = (TextView) findViewById(R.id.tv_UpVoice);
        tv_UpImage = (TextView) findViewById(R.id.tv_UpImage);
        iv_tick_voice = (ImageView) findViewById(R.id.iv_tick_voice);
        iv_tick_img = (ImageView) findViewById(R.id.iv_tick_img);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_playAudio = (ImageView) findViewById(R.id.iv_playAudio);
        rd_audio = (RadioButton) findViewById(R.id.rd_audio);
        rd_image = (RadioButton) findViewById(R.id.rd_image);
    }

    private void initListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(ComposeCommunication_activity.this);
            }
        });

        spinnercomm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (GlobalClass.CheckArrayList(commSpinner)) {
                    for (int i = 0; i < commSpinner.size(); i++) {
                        if (!GlobalClass.isNull(spinnercomm.getSelectedItem().toString()) && !GlobalClass.isNull(commSpinner.get(i).getDisplayName()) && spinnercomm.getSelectedItem().toString().equalsIgnoreCase(commSpinner.get(i).getDisplayName())) {
                            strForwardTo = commSpinner.get(i).getForwardTo();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        commuTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    GlobalClass.showTastyToast(mActivity, ToastFile.crt_txt, 2);


                    if (enteredString.length() > 0) {
                        GlobalClass.SetText(commuTXT, enteredString.substring(1));
                    } else {
                        GlobalClass.SetText(commuTXT, "");
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

        sendcomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCommunication = commuTXT.getText().toString().trim();

                if (!GlobalClass.isNull(spinnercomm.getSelectedItem().toString()) && spinnercomm.getSelectedItem().toString().equalsIgnoreCase("-Select-")) {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.Sl_RELATED, 2);
                } else if (GlobalClass.isNull(getCommunication)) {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.CMP_MSG, 2);
                    commuTXT.requestFocus();
                } else if (rd_audio.isSelected() || rd_audio.isChecked()) {
                    if (selectedFile == null || !selectedFile.exists()) {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.AUD_UPLOAD, 2);
                    } else {

                        proceed();
                    }
                } else if (rd_image.isSelected() || rd_image.isChecked()) {
                    if (selectedFile == null || !selectedFile.exists()) {
                        GlobalClass.showTastyToast(mActivity, MessageConstants.SL_UPLOAD, 2);
                    } else {
                        proceed();
                    }
                } else {
                    proceed();
                }
            }
        });

        rd_audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audioChecked = isChecked;
                if (audioChecked) {
                    if (selectedFile != null && selectedFile.exists()) {
                        if (!flag) {
                            showAlertDialog("audio");
                        }
                    } else {
                        ll_upVoice.setVisibility(View.VISIBLE);
                        ll_upImage.setVisibility(View.GONE);
                        rd_image.setChecked(false);
                        rd_image.setSelected(false);
                    }
                    manageAudioView(selectedFile);
                }
            }
        });

        rd_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                imageChecked = isChecked;
                if (imageChecked) {
                    if (selectedFile != null && selectedFile.exists()) {
                        if (!flag) {
                            showAlertDialog("image");
                        }
                    } else {
                        ll_upVoice.setVisibility(View.GONE);
                        ll_upImage.setVisibility(View.VISIBLE);
                        rd_audio.setChecked(false);
                        rd_audio.setSelected(false);
                    }
                    manageImageView(selectedFile);
                }
            }
        });

        ll_upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessRuntimePermissions.checkcameraPermission(mActivity)) {
                    selectImage();
                } else {
                    AccessRuntimePermissions.requestCamerapermission(mActivity);
                }
            }
        });

        iv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFile != null) {
                    GlobalClass.showImageDialog(mActivity, selectedFile, "", 1);
                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.FILENOTFOUND, 2);
                }
            }
        });

        ll_upVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecordAudioDialog(mActivity);
            }
        });

        iv_playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayAudioDialog(mActivity);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callIntent();
            }
        });
    }

    private void proceed() {
        if (!GlobalClass.isNetworkAvailable(ComposeCommunication_activity.this)) {
            offline_img.setVisibility(View.VISIBLE);
            parent_ll.setVisibility(View.GONE);
        } else {
            PostData();
            offline_img.setVisibility(View.GONE);
            parent_ll.setVisibility(View.VISIBLE);
        }
    }

    private void showAlertDialog(final String value) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setMessage("At a time you can upload only one, voice or image. Do you want to delete the selected file ?");
        alertDialog.setPositiveButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (selectedFile.exists()) {
                    selectedFile.delete();
                    selectedFile = null;
                }
                if (value.equalsIgnoreCase("audio")) {
                    ll_upVoice.setVisibility(View.VISIBLE);
                    ll_upImage.setVisibility(View.GONE);
                    rd_image.setChecked(false);
                    rd_image.setSelected(false);
                    manageAudioView(selectedFile);
                } else {
                    ll_upVoice.setVisibility(View.GONE);
                    ll_upImage.setVisibility(View.VISIBLE);
                    rd_audio.setChecked(false);
                    rd_audio.setSelected(false);
                    manageImageView(selectedFile);
                }
            }
        });
        alertDialog.setNegativeButton(MessageConstants.NO, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                flag = true;
                if (value.equalsIgnoreCase("audio")) {
                    ll_upVoice.setVisibility(View.GONE);
                    ll_upImage.setVisibility(View.VISIBLE);
                    rd_audio.setChecked(false);
                    rd_audio.setSelected(false);
                    rd_image.setChecked(true);
                    rd_image.setSelected(true);
                    manageImageView(selectedFile);
                } else {
                    ll_upVoice.setVisibility(View.VISIBLE);
                    ll_upImage.setVisibility(View.GONE);
                    rd_image.setChecked(false);
                    rd_image.setSelected(false);
                    rd_audio.setChecked(true);
                    rd_audio.setSelected(true);
                    manageAudioView(selectedFile);
                }
            }
        });
        alertDialog.show();
    }

    private void PostData() {
        try {
            SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);

            ComposeCommPOSTModel composeCommPOSTModel = new ComposeCommPOSTModel();
            composeCommPOSTModel.setApiKey(api_key);
            composeCommPOSTModel.setUserCode(user);
            composeCommPOSTModel.setSource("CLISO APP");
            composeCommPOSTModel.setType("REQUEST");
            composeCommPOSTModel.setForwardTo(strForwardTo);
            composeCommPOSTModel.setCommunication("" + commuTXT.getText().toString().trim());
            composeCommPOSTModel.setFile(selectedFile);

            new MultipartComposeCommController(mActivity, ComposeCommunication_activity.this, composeCommPOSTModel).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPOSTCommunicationResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String Response = jsonObject.optString("response", "");
            String respId = jsonObject.optString("resId", "");
            if (!GlobalClass.isNull(respId) && respId.equalsIgnoreCase("RES000")) {
                GlobalClass.showTastyToast(mActivity, Response, 1);
                callIntent();
            } else {
                GlobalClass.showTastyToast(mActivity, Response, 2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callIntent() {
        Intent intent = new Intent(ComposeCommunication_activity.this, ComposeCommunication_activity.class);
        startActivity(intent);
        finish();
    }


    private void selectImage() {
        CharSequence[] items = new CharSequence[]{"Take Photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Upload Image !");
        final CharSequence[] finalItems = items;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (finalItems[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    openCamera();
                } else if (finalItems[item].equals("Choose from gallery")) {
                    userChoosenTask = "Choose from gallery";
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY);
                } else if (finalItems[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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
                .setDirectory("/Uploads/Images")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(com.mindorks.paracamera.Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                String imageurl = camera.getCameraBitmapPath();
                selectedFile = new File(imageurl);
                Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(selectedFile.length())));
                manageImageView(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_PHOTO_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                GlobalClass.showTastyToast(mActivity, ToastFile.failed_to_open, 2);
            }
            try {
                selectedFile = FileUtil.from(this, data.getData());
                Log.e(TAG, "" + String.format("ActualSize : %s", GlobalClass.getReadableFileSize(selectedFile.length())));
                selectedFile = GlobalClass.getCompressedFile(mActivity, selectedFile);
                Log.e(TAG, "" + String.format("CompressedSize : %s", GlobalClass.getReadableFileSize(selectedFile.length())));
                manageImageView(selectedFile);
            } catch (IOException e) {
                GlobalClass.showTastyToast(mActivity, MessageConstants.Failed_to_read_img, 2);
                e.printStackTrace();
            }
        }
    }

    private void manageImageView(File imageFile) {
        if (imageFile != null) {
            flag = false;
            iv_tick_img.setVisibility(View.VISIBLE);
            iv_preview.setVisibility(View.VISIBLE);
            GlobalClass.SetText(tv_UpImage, getString(R.string.re_upload_image));
        } else {
            iv_tick_img.setVisibility(View.GONE);
            iv_preview.setVisibility(View.GONE);
            GlobalClass.SetText(tv_UpImage, getString(R.string.upload_image));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (camera != null) {
                camera.deleteImage();
            }
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRecordAudioDialog(final Activity activity) {
        dialog = new Dialog(activity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.record_audio_custom_dailog);
        dialog.setCancelable(false);

        final ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.ib_close);
        final TextView tv_record_title = (TextView) dialog.findViewById(R.id.tv_record_title);
        final ImageView img_record = (ImageView) dialog.findViewById(R.id.img_record);
        final Button btn_discard = (Button) dialog.findViewById(R.id.btn_discard);
        final Button btn_save = (Button) dialog.findViewById(R.id.btn_save);

        if (selectedFile != null) {
            if (selectedFile.exists()) {
                btn_discard.setEnabled(true);
                btn_discard.setBackgroundResource(R.drawable.maroon_rect_box);
            }
        } else {
            btn_discard.setEnabled(false);
            btn_discard.setBackgroundResource(R.drawable.disabled_rect_box);
        }
        btn_save.setEnabled(false);
        btn_save.setBackgroundResource(R.drawable.disabled_rect_box);
        Log.e(TAG, "Audio file initUI: " + selectedFile);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (tmp_selectedFile != null) {
                    selectedFile = tmp_selectedFile;
                    audioPath = temp_audioPath;
                    if (audioflag) {
                        audioflag = false;
                    } else {
                        audioflag = true;
                    }

                    manageAudioView(selectedFile);
                }
            }
        });

        img_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ButtonClicked) {
                    if (checkAudioPermission()) {
                        ButtonClicked = true;
                        File sdCard = Environment.getExternalStorageDirectory();
                        File directory = new File(sdCard.getAbsolutePath() + "/Uploads/Audio");
                        if (!directory.isDirectory()) {
                            directory.mkdirs();
                        }

                        if (audioflag) {
                            temp_audioPath = directory + "/" + "audio.Mp3";
                        } else {
                            temp_audioPath = directory + "/" + "tempaudio.Mp3";
                        }

                        Log.e(TAG, "Audio File Path ----> " + temp_audioPath);
                        tmp_selectedFile = new File(temp_audioPath);
                        MediaRecorderReady(tmp_selectedFile);

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
                        btn_save.setBackgroundResource(R.drawable.disabled_rect_box);
                        btn_discard.setEnabled(false);
                        btn_discard.setBackgroundResource(R.drawable.disabled_rect_box);
                        GlobalClass.showTastyToast(activity, MessageConstants.Recording_Started, 1);
                    } else {
                        requestAudioPermission();
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
                    btn_save.setBackgroundResource(R.drawable.maroon_rect_box);
                    btn_discard.setEnabled(true);
                    btn_discard.setBackgroundResource(R.drawable.maroon_rect_box);
                    GlobalClass.showTastyToast(mActivity, MessageConstants.Recording_completed, 1);
                }
            }
        });

        dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void manageAudioView(File selectedFile) {
        if (selectedFile != null) {
            flag = false;
            iv_tick_voice.setVisibility(View.VISIBLE);
            GlobalClass.SetText(tv_UpVoice, getString(R.string.reupload_voice_message));
            iv_playAudio.setVisibility(View.VISIBLE);
        } else {
            iv_tick_voice.setVisibility(View.INVISIBLE);
            GlobalClass.SetText(tv_UpVoice, getString(R.string.upload_voice_message));
            iv_playAudio.setVisibility(View.GONE);
        }

    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public boolean checkAudioPermission() {
        int result = ContextCompat.checkSelfPermission(mActivity, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mActivity, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void MediaRecorderReady(File tmp_selectedFile) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(tmp_selectedFile.getAbsolutePath());
    }

    private void showPlayAudioDialog(final Activity activity) {
        dialog = new Dialog(activity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.play_audio_dailog);
        dialog.setCancelable(false);

        final ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.ib_close);
        final Button btn_play = (Button) dialog.findViewById(R.id.btn_play);
        final Button btn_stop = (Button) dialog.findViewById(R.id.btn_stop);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioPath);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
