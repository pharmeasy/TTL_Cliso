package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import static com.example.e5322.thyrosoft.API.Constants.small_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.ent_pin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Controller.CategoryListController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.EmailValidationController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CategoryResModel;
import com.example.e5322.thyrosoft.Models.RequestModels.ClientRegisterRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.ClientRegisterResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sgc_Pgc_Entry_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int PLACE_PICKER_REQUEST = 1;
    Activity activity;

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
    ImageView correct_img, cross_img;
    Spinner sgc_pgc_spinner, brand_spinner, qualification_spinner, spn_sgc_category;
    LinearLayout dr_name_layout, brand_name, visiting_card_layout;
    EditText pincode_edt, mobile_edt, email_edt, client_name_edt, dr_name, incharge_name, phone_number, website_edt, dr_name_edt, addressEdt;
    Button next_btn_sgc_pgc, visiting_card_btn;
    TextView lattitude_txt, longitude_txt;
    String blockCharacterSetdata = "~#^|$%&*!+:`-/|><";
    ImageView back, home;
    ImageView latlong;
    ProgressDialog barProgressDialog;
    CharSequence address;

    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSetdata.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private String get_Registration_type;
    private String getpincode, getmobile_number, email_address, getclient_name, get_brand_name, get_incharge_name, get_Address, get_phone_number, get_website, get_visiting_card, get_dr_name, get_qualification, selSGCCategory = "";
    private String imageName;
    private String image;
    private RequestQueue PostQuePGC;
    private SharedPreferences prefs;
    private String user;
    private String api_key;
    private String TAG = Sgc_Pgc_Entry_Activity.class.getSimpleName().toString();
    private String entered_mobile_String;
    private String entered_Type_String;
    private String pincode;
    private String getLatitude;
    private String getLongitude;
    private double lat, log;
    ArrayList<String> SGCCategoryList;
    ConnectionDetector cd;

    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgc__pgc__entry_);

        init();
        initlistner();
        initSpinners();
        GlobalClass.setStatusBarcolor(activity);
        callSGCCategoryAPI();

    }

    private void initSpinners() {
        SGCCategoryList = new ArrayList<>();
        SGCCategoryList.add("SELECT CATEGORY*");
        setSGCCategoryListAdapter(SGCCategoryList);
    }

    private void callSGCCategoryAPI() {
        if (cd.isConnectingToInternet()) {
            CategoryListController categoryListController = new CategoryListController(Sgc_Pgc_Entry_Activity.this, activity);
            categoryListController.fetchSGCcategoryList();
        } else {
            Toast.makeText(this, MessageConstants.CHECK_INTERNET_CONN, Toast.LENGTH_SHORT).show();
        }

    }

    private void initlistner() {


        next_btn_sgc_pgc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                get_Registration_type = sgc_pgc_spinner.getSelectedItem().toString();
                if (get_Registration_type.equals("SGC")) {
                    getpincode = pincode_edt.getText().toString();
                    getmobile_number = mobile_edt.getText().toString();
                    email_address = email_edt.getText().toString();
                    getclient_name = client_name_edt.getText().toString();
//                    get_brand_name = brand_spinner.getSelectedItem().toString();
                    get_brand_name = "TTL";
                    get_incharge_name = incharge_name.getText().toString();
                    get_Address = addressEdt.getText().toString();
                    get_phone_number = phone_number.getText().toString();
                    get_website = website_edt.getText().toString();
                    getLatitude = lattitude_txt.getText().toString();
                    getLongitude = longitude_txt.getText().toString();
                    getclient_name = getclient_name.replaceAll("\\s+", " ");
                    get_incharge_name = get_incharge_name.replaceAll("\\s+", " ");
                    get_Address = get_Address.replaceAll("\\s+", " ");

                    getclient_name = getclient_name.replaceAll("\\.", "");
                    get_incharge_name = get_incharge_name.replaceAll("\\.", "");

                    if (!GlobalClass.isNull(get_website) && !GlobalClass.isValidURL(get_website)) {
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "URL is invalid!", Toast.LENGTH_SHORT, TastyToast.ERROR);
                    }/*else if (getLatitude.equalsIgnoreCase("")) {
                        lattitude_txt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter latitude", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getLongitude.equalsIgnoreCase("")) {
                        longitude_txt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct longitude", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/ else if (getclient_name.equals("")) {
                        client_name_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct client name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getclient_name.length() < 2) {
                        client_name_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct client name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getmobile_number.equals("")) {
                        mobile_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct mobile number", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getmobile_number.length() < 10) {
                        mobile_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter 10 digits mobile number", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (email_address.equals("")) {
                        email_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter email address", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_Address.equals("")) {
                        addressEdt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter address", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_Address.length() < 25) {
                        addressEdt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.addre25long, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getpincode.equals("")) {
                        pincode_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct pincode", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getpincode.length() < 6) {
                        pincode_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter 6 digits pincode", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_incharge_name.equals("")) {
                        incharge_name.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct incharge name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_incharge_name.length() < 2) {
                        incharge_name.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct incharge name", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (spn_sgc_category.getSelectedItemPosition() == 0) {
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Kindly select category", Toast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!GlobalClass.isNull(get_phone_number) && GlobalClass.LongestStringSequence(get_phone_number) > 8) {
                        phone_number.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, MessageConstants.VALID_LANDLINE_NUMBER, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (image == null) {
                        cross_img.setVisibility(View.VISIBLE);
                        correct_img.setVisibility(View.GONE);
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please upload visiting card", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!email_address.equals("")) {
                        boolean check;
                        Pattern p;
                        Matcher m;
                        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                        p = Pattern.compile(EMAIL_STRING);
                        m = p.matcher(email_address);
                        check = m.matches();
                        if (!check) {
                            email_edt.requestFocus();
                            TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Not valid email", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            try {
                                if (ControllersGlobalInitialiser.emailValidationController != null) {
                                    ControllersGlobalInitialiser.emailValidationController = null;
                                }
                                ControllersGlobalInitialiser.emailValidationController = new EmailValidationController(Sgc_Pgc_Entry_Activity.this, activity, "SGC");
                                ControllersGlobalInitialiser.emailValidationController.emailvalidationapi(email_edt.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }
                } else if (get_Registration_type.equals("PGC")) {
                    getpincode = pincode_edt.getText().toString();//
                    getmobile_number = mobile_edt.getText().toString();//
                    email_address = email_edt.getText().toString();//
                    get_dr_name = dr_name_edt.getText().toString();//
                    get_qualification = qualification_spinner.getSelectedItem().toString();
                    get_Address = addressEdt.getText().toString();
                    get_phone_number = phone_number.getText().toString();
                    get_website = website_edt.getText().toString();
                    getLatitude = lattitude_txt.getText().toString();
                    getLongitude = lattitude_txt.getText().toString();
                    get_dr_name = get_dr_name.replaceAll("\\s+", " ");
                    get_Address = get_Address.replaceAll("\\s+", " ");
                    get_dr_name = get_dr_name.replaceAll("\\.", "");

                    if (!GlobalClass.isNull(get_website) && !GlobalClass.isValidURL(get_website)) {
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "URL is invalid!", Toast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                    //Sushil
                    /*else if (getLatitude.equalsIgnoreCase("")) {
                        lattitude_txt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter latitude", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getLongitude.equalsIgnoreCase("")) {
                        longitude_txt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct longitude", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }*/
                    else if (getpincode.equals("")) {
                        pincode_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct pincode", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getpincode.length() < 6) {
                        pincode_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter 6 digits pincode", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getmobile_number.equals("")) {
                        mobile_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct mobile number", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (getmobile_number.length() < 10) {
                        mobile_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter 10 digits mobile number", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (email_address.equals("")) {
                        email_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter email address", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_dr_name.equals("")) {
                        dr_name_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.crt_name_woe, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_dr_name.length() < 2) {
                        dr_name_edt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.crt_name_woe, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_Address.equals("")) {
                        addressEdt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter address", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (get_Address.length() < 25) {
                        addressEdt.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.addre25long, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!GlobalClass.isNull(get_phone_number) && GlobalClass.LongestStringSequence(get_phone_number) > 8) {
                        phone_number.requestFocus();
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, MessageConstants.VALID_LANDLINE_NUMBER, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (image == null) {
                        cross_img.setVisibility(View.VISIBLE);
                        correct_img.setVisibility(View.GONE);
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please upload visiting card", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!email_address.equals("")) {
                        boolean check;
                        Pattern p;
                        Matcher m;

                        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                        p = Pattern.compile(EMAIL_STRING);

                        m = p.matcher(email_address);
                        check = m.matches();
                        if (!check) {
                            email_edt.requestFocus();
                            TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Not valid email", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            try {
                                if (ControllersGlobalInitialiser.emailValidationController != null) {
                                    ControllersGlobalInitialiser.emailValidationController = null;
                                }
                                ControllersGlobalInitialiser.emailValidationController = new EmailValidationController(Sgc_Pgc_Entry_Activity.this, activity, "PGC");
                                ControllersGlobalInitialiser.emailValidationController.emailvalidationapi(email_edt.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        latlong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePickerMethod();
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
                GlobalClass.goToHome(Sgc_Pgc_Entry_Activity.this);
            }
        });

        pincode_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("9")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            ent_pin,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        pincode_edt.setText(enteredString.substring(1));
                    } else {
                        pincode_edt.setText("");
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

        mobile_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            ToastFile.crt_mob_num,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        mobile_edt.setText(enteredString.substring(1));
                    } else {
                        mobile_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                entered_Type_String = sgc_pgc_spinner.getSelectedItem().toString();
                entered_mobile_String = s.toString();
                if (entered_mobile_String.length() == 10) {
                    barProgressDialog.show();
                    try {
                        RequestQueue requestQueue = GlobalClass.setVolleyReq(getApplicationContext());
                        String URL = Api.checkValidEmail;
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("type", entered_Type_String);
                        jsonBody.put("mobile", entered_mobile_String);
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "onResponse: " + response);
                                GlobalClass.hideProgress(activity, barProgressDialog);
                                if (response.contains("\"TRUE\"")) {

                                } else {
                                    mobile_edt.setError(response);
                                    mobile_edt.setText("");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                GlobalClass.hideProgress(activity, barProgressDialog);
                                Log.e("VOLLEY", error.toString());
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                        };
                        requestQueue.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        spn_sgc_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0) {
                    selSGCCategory = spn_sgc_category.getSelectedItem().toString();
                } else {
                    selSGCCategory = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sgc_pgc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sgc_pgc_spinner.getSelectedItem().equals("SGC")) {
//                    brand_spinner.setVisibility(View.VISIBLE);
                    qualification_spinner.setVisibility(View.GONE);
                    pincode_edt.setVisibility(View.VISIBLE);
                    mobile_edt.setVisibility(View.VISIBLE);
                    email_edt.setVisibility(View.VISIBLE);
                    client_name_edt.setVisibility(View.VISIBLE);
                    incharge_name.setVisibility(View.VISIBLE);
                    addressEdt.setVisibility(View.VISIBLE);
                    phone_number.setVisibility(View.VISIBLE);
                    website_edt.setVisibility(View.VISIBLE);
                    visiting_card_layout.setVisibility(View.VISIBLE);
                    dr_name_layout.setVisibility(View.GONE);
                    spn_sgc_category.setVisibility(View.VISIBLE);
                    pincode_edt.setText("");
                    mobile_edt.setText("");
                    email_edt.setText("");
                    dr_name_edt.setText("");
                    client_name_edt.setText("");
                    incharge_name.setText("");
                    addressEdt.setText("");
                    phone_number.setText("");
                    website_edt.setText("");
                    lattitude_txt.setText("");
                    longitude_txt.setText("");

                } else if (sgc_pgc_spinner.getSelectedItem().equals("PGC")) {
                    brand_spinner.setVisibility(View.GONE);
                    qualification_spinner.setVisibility(View.VISIBLE);
                    pincode_edt.setVisibility(View.VISIBLE);
                    mobile_edt.setVisibility(View.VISIBLE);
                    email_edt.setVisibility(View.VISIBLE);
                    dr_name_layout.setVisibility(View.VISIBLE);
                    addressEdt.setVisibility(View.VISIBLE);
                    phone_number.setVisibility(View.VISIBLE);
                    website_edt.setVisibility(View.VISIBLE);
                    visiting_card_layout.setVisibility(View.VISIBLE);
                    client_name_edt.setVisibility(View.GONE);
                    incharge_name.setVisibility(View.GONE);
                    spn_sgc_category.setVisibility(View.GONE);
                    pincode_edt.setText("");
                    mobile_edt.setText("");
                    email_edt.setText("");
                    dr_name_edt.setText("");
                    email_edt.setText("");
                    client_name_edt.setText("");
                    incharge_name.setText("");
                    lattitude_txt.setText("");
                    longitude_txt.setText("");
                    addressEdt.setText("");
                    phone_number.setText("");
                    website_edt.setText("");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        visiting_card_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 0);
                GlobalClass.cropImageFullScreenActivity(Sgc_Pgc_Entry_Activity.this, 2);

            }
        });


        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8")
                        || enteredString.startsWith("9")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            ToastFile.crt_phone_num,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        phone_number.setText(enteredString.substring(1));
                    } else {
                        phone_number.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteerdstring = s.toString();
            }
        });


        email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter email id",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        email_edt.setText(enteredString.substring(1));
                    } else {
                        email_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 50) {
                    email_edt.setText(enteredString.substring(1));
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter maximum 50 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        client_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter client name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        client_name_edt.setText(enteredString.substring(1));
                    } else {
                        client_name_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 30) {
                    client_name_edt.setText(enteredString.substring(1));
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter maximum 30 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dr_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter client name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        dr_name_edt.setText(enteredString.substring(1));
                    } else {
                        dr_name_edt.setText("");
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


        incharge_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter incharge name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        incharge_name.setText(enteredString.substring(1));
                    } else {
                        incharge_name.setText("");
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


        website_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter website",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        website_edt.setText(enteredString.substring(1));
                    } else {
                        website_edt.setText("");
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
    }

    private void init() {
        activity = Sgc_Pgc_Entry_Activity.this;
        cd = new ConnectionDetector(activity);
        correct_img = (ImageView) findViewById(R.id.correct_img);
        cross_img = (ImageView) findViewById(R.id.cross_img);
        next_btn_sgc_pgc = (Button) findViewById(R.id.next_btn_sgc_pgc);
        visiting_card_btn = (Button) findViewById(R.id.visiting_card_btn);
        pincode_edt = (EditText) findViewById(R.id.pincode_edt);
        mobile_edt = (EditText) findViewById(R.id.mobile_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        dr_name_edt = (EditText) findViewById(R.id.dr_name_edt);
        client_name_edt = (EditText) findViewById(R.id.client_name_edt);
        incharge_name = (EditText) findViewById(R.id.incharge_name);
        lattitude_txt = (TextView) findViewById(R.id.lattitude_txt);
        longitude_txt = (TextView) findViewById(R.id.longitude_txt);
        phone_number = (EditText) findViewById(R.id.phone_number);
        website_edt = (EditText) findViewById(R.id.website_edt);
        addressEdt = (EditText) findViewById(R.id.address);
        sgc_pgc_spinner = (Spinner) findViewById(R.id.sgc_pgc_spinner);
        brand_spinner = (Spinner) findViewById(R.id.brand_spinner);
        qualification_spinner = (Spinner) findViewById(R.id.qualification_spinner);
        dr_name_layout = (LinearLayout) findViewById(R.id.dr_name_layout);
        brand_name = (LinearLayout) findViewById(R.id.brand_name);
        visiting_card_layout = (LinearLayout) findViewById(R.id.visiting_card_layout);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        latlong = findViewById(R.id.latlong);
        spn_sgc_category = findViewById(R.id.spn_sgc_category);

        correct_img.setVisibility(View.GONE);
        cross_img.setVisibility(View.GONE);
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        api_key = prefs.getString("API_KEY", null);


        ArrayAdapter sgc_pgc_spinner_adapter = ArrayAdapter.createFromResource(Sgc_Pgc_Entry_Activity.this, R.array.sgc_pgc_entry, R.layout.whitetext_spin);
        sgc_pgc_spinner_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        sgc_pgc_spinner.setAdapter(sgc_pgc_spinner_adapter);

        ArrayAdapter qualification_adapter = ArrayAdapter.createFromResource(Sgc_Pgc_Entry_Activity.this, R.array.dr_qualification, R.layout.whitetext_spin);
        qualification_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        qualification_spinner.setAdapter(qualification_adapter);

        ArrayAdapter sgc_brand_adapter = ArrayAdapter.createFromResource(Sgc_Pgc_Entry_Activity.this, R.array.sgc_brand, R.layout.whitetext_spin);
        sgc_brand_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        brand_spinner.setAdapter(sgc_brand_adapter);

        int maxLength = 30;
        InputFilter[] FilterArray = new InputFilter[3];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        FilterArray[1] = filter1;
        FilterArray[2] = EMOJI_FILTER;
        client_name_edt.setFilters(FilterArray);

        email_edt.setFilters(new InputFilter[]{filter1});
//        client_name_edt.setFilters(new InputFilter[]{filter1});
        incharge_name.setFilters(new InputFilter[]{filter1});
        dr_name_edt.setFilters(new InputFilter[]{filter1});
        website_edt.setFilters(new InputFilter[]{filter1});
        email_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
//        client_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        incharge_name.setFilters(new InputFilter[]{EMOJI_FILTER});
        barProgressDialog = GlobalClass.progress(activity, false);
        dr_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        website_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
    }

    private void uploadClientEntry(final String type, String brandName, String name, String qualification, String inchargeName, String SGCCategory) {
        barProgressDialog.show();

        PostQuePGC = GlobalClass.setVolleyReq(Sgc_Pgc_Entry_Activity.this);
        JSONObject jsonObject = null;
        try {
            ClientRegisterRequestModel requestModel = new ClientRegisterRequestModel();
            requestModel.setAddress(get_Address.replace("+", ""));
            requestModel.setBrand(brandName);
            requestModel.setChannel("MOBILE APPLICATION");
            requestModel.setCountry("India");
            requestModel.setEmail(email_address);
            requestModel.setInchargeName(inchargeName);
            requestModel.setName(name);
            requestModel.setTspOLc(user);
            requestModel.setVerificationSource("");
            requestModel.setQualification(qualification);
            requestModel.setSpeciality("");
            requestModel.setApikey(api_key);
            requestModel.setMobile(getmobile_number);
            requestModel.setPhone_no(get_phone_number);
            requestModel.setEntryType(get_Registration_type);
            requestModel.setPincode(getpincode);
            requestModel.setWebsite(get_website);
            requestModel.setAPP("B2B");
            requestModel.setFile(imageName);
            requestModel.setLatitute(String.valueOf(lat));
            requestModel.setLongitude(String.valueOf(log));
            requestModel.setOpType("entry");
            requestModel.setCategory(SGCCategory);
            requestModel.setVisiting_Card(image);


            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);
            Log.e("Req body--->", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("SGC API--->", Api.SGCRegistration);

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.SGCRegistration, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Sushil
                    Log.e(TAG, "onResponse: " + response);
                    GlobalClass.hideProgress(activity, barProgressDialog);
                    ClientRegisterResponseModel responseModel = new Gson().fromJson(String.valueOf(response), ClientRegisterResponseModel.class);
                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {

                            if (type.equalsIgnoreCase("PGCEntry")) {
                                new LogUserActivityTagging(activity, "PGC-REGISTRATION", getmobile_number);

                                TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "PGC registered successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            } else {
                                if (responseModel.getResponse().equalsIgnoreCase("Invalid Address")) {
                                    TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Invalid Address", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                                } else {
                                    if (responseModel.getMessage().equalsIgnoreCase("ClientId: Mobile Number Already Exist")) {
                                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Mobile Number Already Exist", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                                    } else {
                                        new LogUserActivityTagging(activity, "SGC-REGISTRATION", getmobile_number);
                                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "SGC registered successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    }
                                }
                            }

                            Intent i = new Intent(Sgc_Pgc_Entry_Activity.this, Sgc_Pgc_Entry_Activity.class);
                            startActivity(i);
                            finish();
                        } else if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(small_invalidApikey)) {
                            GlobalClass.redirectToLogin(Sgc_Pgc_Entry_Activity.this);
                        } else {
                            if (responseModel.getResponse().equalsIgnoreCase("Invalid Address")) {
                                TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Invalid address", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            } else {
                                TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "" + responseModel.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            }
                        }
                    } else {
                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.hideProgress(activity, barProgressDialog);
                if (error != null) {
                } else {
                    System.out.println(error);
                }
            }
        });
        GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
        PostQuePGC.add(jsonObjectRequest1);
        Log.e(TAG, "uploadClientEntry: " + jsonObject);
        Log.e(TAG, "uploadClientEntry: " + jsonObjectRequest1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            try {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place.getLatLng() != null) {
                    Log.e(TAG, "Places---->" + place.getName() + ", " + place.getId());

                    lat = place.getLatLng().latitude;
                    log = place.getLatLng().longitude;

                    CharSequence name = place.getName();
                    address = place.getAddress();

                    if (!TextUtils.isEmpty(address)) {
                        addressEdt.setEnabled(true);
                    }

                    pincode = getPinCodeFromAddress(address);
                    pincode_edt.setText(pincode);
                    addressEdt.setText(address);
                    lattitude_txt.setText("Lat :" + String.valueOf(lat));
                    lattitude_txt.setError(null);

                    longitude_txt.setText("Long :" + String.valueOf(log));
                    longitude_txt.setError(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


        if (data != null && requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();

                String picturePath = null;
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(targetUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Uptill Here

                try {
                    imageName = picturePath.substring(picturePath.lastIndexOf("/") + 1, picturePath.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    image = ConvertBitmapToString(resizedBitmap);
                    correct_img.setVisibility(View.VISIBLE);
                    cross_img.setVisibility(View.GONE);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                image = ImagePicker.Companion.getFile(data).toString();
                File file = new File(image);
                imageName = file.getName();
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                image = ConvertBitmapToString(myBitmap);
                correct_img.setVisibility(View.VISIBLE);
                cross_img.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void getemailresponse(String type) {
        if (type.equalsIgnoreCase("SGC")) {
            uploadClientEntry("SGCEntry", get_brand_name, getclient_name, "", get_incharge_name, selSGCCategory);
        } else {
            uploadClientEntry("PGCEntry", "", "DR " + get_dr_name, get_qualification, "", "");
        }
    }

    private void PlacePickerMethod() {
        if (!Places.isInitialized()) {
            try {
                Places.initialize(activity, "" + getResources().getString(R.string.googlemapkey));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }


    public static String getPinCodeFromAddress(CharSequence address) {

        String pincodeStr = String.valueOf(address);
        Pattern zipPattern = Pattern.compile("(\\d{6})");
        Matcher zipMatcher = zipPattern.matcher(pincodeStr);

        String zip = "";
        if (zipMatcher.find()) {
            zip = zipMatcher.group(1);
        }

        return zip;
    }


    public void getSGCCategoryListResponse(CategoryResModel categoryResModel) {
        if (GlobalClass.checkEqualIgnoreCase(categoryResModel.getRespID(), Constants.RES0000)) {
            if (GlobalClass.isArraylistNotNull(categoryResModel.getCategoryList())) {
                for (int i = 0; i < categoryResModel.getCategoryList().size(); i++) {
                    SGCCategoryList.add((categoryResModel.getCategoryList().get(i).getCategory()));
                }
                setSGCCategoryListAdapter(SGCCategoryList);
            }
        } else {
            TastyToast.makeText(activity, categoryResModel.getResp(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        }
    }

    private void setSGCCategoryListAdapter(ArrayList<String> categoryList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.whitetext_spin, categoryList);
        adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        spn_sgc_category.setAdapter(adapter);
    }
}
