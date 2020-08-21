package com.example.e5322.thyrosoft.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Adapter.RATEnteredAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.RATEnteredController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RATEnteredRequestModel;
import com.example.e5322.thyrosoft.Models.RATEnteredResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.mindorks.paracamera.Camera;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RATEnteredFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RATEnteredFrag extends Fragment {

    RadioButton rd_pending, rd_done, rd_expired;
    RecyclerView rc_view;
    View root;
    Calendar myCalendar;
    ConnectionDetector cd;
    Activity activity;
    private String TAG = RATEnteredFrag.class.getSimpleName();
    SharedPreferences preferences;
    String usercode, apikey;
    LinearLayout ll_fromDate, ll_date;
    private String myFormat = "dd-MM-yyyy";
    TextView tv_fromDate;
    RATEnteredAdapter ratEnteredAdapter;
    Date fromdate;
    Bitmap bitmapimage;
    private Camera camera;
    File sll_file;
    private int status_code;
    private String patientid;
    RadioGroup rg_radio;
    private int flag;
    ProgressDialog barProgressDialog;
    ArrayList<RATEnteredResponseModel.PatientDETAILSBean> patientDETAILSBeans = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private String putDate, showDate, from_formateDate;
    private String Value;
    Activity mActivity;

    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    public RATEnteredFrag() {
        // Required empty public constructor
    }


    public static RATEnteredFrag newInstance(String param1, String param2) {
        RATEnteredFrag fragment = new RATEnteredFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_r_a_t_entered, container, false);
        mActivity = getActivity();

        initui(root);
        initlistner();

        return root;
    }

    private void initlistner() {

        rd_pending.setChecked(true);
        if (rd_pending.isChecked() || rd_pending.isSelected()) {
            CallAPI("1");
            ll_date.setVisibility(View.GONE);
        }
        rg_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (rd_pending.isChecked() || rd_pending.isSelected()) {
                    CallAPI("1");
                    ll_date.setVisibility(View.GONE);
                } else if (rd_done.isChecked() || rd_done.isSelected()) {
                    CallAPI("2");
                    ll_date.setVisibility(View.VISIBLE);
                } else if (rd_expired.isChecked() || rd_expired.isSelected()) {
                    CallAPI("3");
                    ll_date.setVisibility(View.VISIBLE);
                }
            }
        });

        ll_fromDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });
    }


    private void CallAPI(String status) {
        RATEnteredRequestModel ratEnteredRequestModel = new RATEnteredRequestModel();
        ratEnteredRequestModel.setApikey(apikey);
        ratEnteredRequestModel.setSourceCODE(usercode);
        ratEnteredRequestModel.setStatus(status);
        ratEnteredRequestModel.setTest("CRAT");
        if (rd_pending.isChecked() || rd_pending.isSelected()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());
            ratEnteredRequestModel.setToDATE(currentDateandTime);
        } else {
            ratEnteredRequestModel.setToDATE(from_formateDate);
        }


        RATEnteredController ratEnteredController = new RATEnteredController(this, ratEnteredRequestModel);
        ratEnteredController.CallAPI();
    }

    private void initui(View root) {
        cd = new ConnectionDetector(getContext());
        activity = getActivity();
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        showDate = sdf.format(today);

        fromdate = returndate(fromdate, showDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

        rd_pending = root.findViewById(R.id.rd_pending);
        rd_done = root.findViewById(R.id.rd_done);
        rd_expired = root.findViewById(R.id.rd_expired);
        rc_view = root.findViewById(R.id.rc_view);
        ll_fromDate = root.findViewById(R.id.ll_fromDate);
        tv_fromDate = root.findViewById(R.id.tv_fromDate);
        ll_date = root.findViewById(R.id.ll_date);
        rg_radio = root.findViewById(R.id.rg_radio);

        GlobalClass.SetText(tv_fromDate, showDate);

    }

    public void getresponse(RATEnteredResponseModel body) {

        try {
            if (!GlobalClass.isNull(body.getResID()) && body.getResID().equalsIgnoreCase("RES0000")) {
                patientDETAILSBeans = body.getPatientDETAILS();
                int timespan = 0;
                if (body.getTimespan() != 0) {
                    timespan = body.getTimespan();
                } else {
                    timespan = 240;
                }
                ratEnteredAdapter = new RATEnteredAdapter(this, patientDETAILSBeans, body.getCurrentTIME(), timespan);
                ratEnteredAdapter.Click(new RATEnteredAdapter.Passdata() {
                    @Override
                    public void pass(RATEnteredResponseModel.PatientDETAILSBean postmaterial) {
                        patientid = postmaterial.getPatientID();
                        flag = postmaterial.getStatus();
                        openCamera();
                    }
                });
                rc_view.setVisibility(View.VISIBLE);
                rc_view.setAdapter(ratEnteredAdapter);
                ratEnteredAdapter.notifyDataSetChanged();
            } else {
                GlobalClass.showTastyToast(mActivity, "" + body.getResponse(), 2);
                rc_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
        }

    }

    private void updateLabel() {
        putDate = sdf.format(myCalendar.getTime());

        GlobalClass.SetText(tv_fromDate, putDate);

        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);


        RATEnteredRequestModel ratEnteredRequestModel = new RATEnteredRequestModel();
        ratEnteredRequestModel.setApikey(apikey);
        ratEnteredRequestModel.setSourceCODE(usercode);
        if (rd_done.isChecked()) {
            ratEnteredRequestModel.setStatus("2");
        } else if (rd_expired.isChecked()) {
            ratEnteredRequestModel.setStatus("3");
        }
        ratEnteredRequestModel.setTest("CRAT");
        ratEnteredRequestModel.setToDATE(from_formateDate);
        RATEnteredController ratEnteredController = new RATEnteredController(this, ratEnteredRequestModel);
        ratEnteredController.CallAPI();

    }


    private Date returndate(Date date, String putDate) {
        try {
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private void buildCamera() {
        camera = new com.mindorks.paracamera.Camera.Builder()
                .resetToCorrectOrientation(false)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_PNG)
                .setCompression(50)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                bitmapimage = camera.getCameraBitmap();
                String imageurl = camera.getCameraBitmapPath();
                sll_file = new File(imageurl);
                String destFile = Environment.getExternalStorageDirectory().getAbsolutePath() + sll_file;
                sll_file = new File(destFile);
                GlobalClass.copyFile(new File(imageurl), sll_file);
                if (flag == 0) {
                    new AsyncTaskPost_uploadfile().execute();
                } else if (flag == 1) {
                    showImageDialog(sll_file);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showImageDialog(File file) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.result_upload_dialog);
        dialog.setCancelable(true);

        ImageView imgView = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        RadioGroup rd_grp = (RadioGroup) dialog.findViewById(R.id.rd_grp);
        final RadioButton rd_positive = (RadioButton) dialog.findViewById(R.id.rd_positive);
        final RadioButton rd_negative = (RadioButton) dialog.findViewById(R.id.rd_negative);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (myBitmap != null)
            imgView.setImageBitmap(myBitmap);
        else
            GlobalClass.showTastyToast(activity, MessageConstants.Image_not_found, 0);

        rd_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rd_positive.isChecked()) {
                    Value = "+";
                } else if (rd_negative.isChecked()) {
                    Value = "-";
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rd_negative.isChecked() && !rd_positive.isChecked()) {
                    GlobalClass.showTastyToast(mActivity, "Select the result status", 2);
                } else {
                    dialog.dismiss();
                    new AsyncTaskPost_uploadfile().execute();
                }

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }


    public class AsyncTaskPost_uploadfile extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = new ProgressDialog(getActivity());
            barProgressDialog.setTitle("Kindly wait ...");
            barProgressDialog.setMessage(ToastFile.processing_request);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);
            barProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String strUrl = Api.IMAGE_UPLOAD + "PICKSO/api/TRFUpload/RATTRFUpload";
            Log.v("URL", strUrl);

            InputStream inputStream = null;
            String result = "";

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                httpPost.setEntity(builder.build());
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                builder.addPart("KEY", new StringBody("" + apikey));
                builder.addPart("SOURCECODE", new StringBody("" + usercode));
                builder.addPart("PATIENTID", new StringBody("" + patientid));
                if (flag == 0) {
                    builder.addPart("TYPE", new StringBody("SII"));
                } else if (flag == 1) {
                    builder.addPart("TYPE", new StringBody("RESULT"));
                    builder.addPart("VALUE", new StringBody(Value));
                }

                builder.addPart("MODE", new StringBody("THYROSOFTLITE APP"));
                builder.addBinaryBody("SLL", sll_file);


                Log.e(TAG, "Post params:- " + "KEY" + ":" + apikey + "\n" + "SOURCECODE" + ":" + usercode + "\n" + "PATIENTID" + ":" + patientid + "\n" + "TYPE:SII" + "\n" + "MODE:THYROSOFTLITE APP" + "\n"
                        + "\"TRF_UPLOAD_SIZE\"" + ":\"" + sll_file + "\"");

                httpPost.setEntity(builder.build());
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                Log.e(TAG, "Status Line: " + httpResponse.getStatusLine());

                status_code = httpResponse.getStatusLine().getStatusCode();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.v(TAG, "Response : " + result);

                }
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
                result = "Something went wrong";

            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            GlobalClass.hideProgress(getContext(), barProgressDialog);
            if (status_code == 200) {
                if (response != null && !response.isEmpty()) {
                    Log.e(TAG, "ON Response: " + response);
                    response = response.replaceAll("^\"|\"$", "");


                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!GlobalClass.isNull(obj.getString("ResId")) && obj.getString("ResId").equalsIgnoreCase("RES0000")) {
                            GlobalClass.showTastyToast(mActivity, "" + obj.getString("Response"), 1);
                            CallAPI("1");
                            obj.getString("currentTIME");

                        } else {
                            GlobalClass.showTastyToast(mActivity, "" + obj.getString("Response"), 2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalClass.showTastyToast(mActivity, MessageConstants.RESULTNULL, 2);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, MessageConstants.SOMETHING_WENT_WRONG, 2);
            }
        }

        public String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }

    }
}