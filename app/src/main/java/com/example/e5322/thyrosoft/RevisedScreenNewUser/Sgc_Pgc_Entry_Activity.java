package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.CommonItils.Validator;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.EmailValidationController;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.SGCController;
import com.example.e5322.thyrosoft.Controller.ValidateEmail_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.ClientRegisterRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.ClientRegisterResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.e5322.thyrosoft.API.Constants.small_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.ent_pin;

public class Sgc_Pgc_Entry_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int PLACE_PICKER_REQUEST = 1;
    Activity activity;
    String strtype;

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
    Spinner sgc_pgc_spinner, brand_spinner, qualification_spinner;
    LinearLayout dr_name_layout, brand_name, visiting_card_layout;
    EditText pincode_edt, mobile_edt, email_edt, client_name_edt, incharge_name, phone_number, website_edt, dr_name_edt, addressEdt;
    Button next_btn_sgc_pgc, visiting_card_btn;
    TextView lattitude_txt, longitude_txt;

    String blockCharacterSetdata = "~#^|$%&*!+:`-/|><";
    ImageView back, home;
    LinearLayout latlong;
    private String encodedImage;

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
    private String getpincode, getmobile_number, email_address, getclient_name, get_brand_name, get_incharge_name, get_Address, get_phone_number, get_website, get_visiting_card, get_dr_name, get_qualification;
    private String imageName;
    private String image;
    private RequestQueue PostQuePGC;
    private SharedPreferences prefs;
    private String user;
    private String passwrd;
    private String access;
    private String api_key;
    private String TAG = Sgc_Pgc_Entry_Activity.class.getSimpleName().toString();
    private String finalJson;
    private String entered_mobile_String;
    private String entered_Type_String;
    private String pincode;
    private String getLatitude;
    private String getLongitude;
    private double lat, log;

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

        activity = Sgc_Pgc_Entry_Activity.this;

        initViews();
        initListner();


        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        correct_img.setVisibility(View.GONE);
        cross_img.setVisibility(View.GONE);
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
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

        email_edt.setFilters(new InputFilter[]{filter1});
        client_name_edt.setFilters(new InputFilter[]{filter1});
        incharge_name.setFilters(new InputFilter[]{filter1});

        dr_name_edt.setFilters(new InputFilter[]{filter1});

        website_edt.setFilters(new InputFilter[]{filter1});

        email_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        client_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        incharge_name.setFilters(new InputFilter[]{EMOJI_FILTER});

        dr_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});

        website_edt.setFilters(new InputFilter[]{EMOJI_FILTER});

    }

    private void initListner() {

        next_btn_sgc_pgc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                get_Registration_type = sgc_pgc_spinner.getSelectedItem().toString();

                if (!GlobalClass.isNull(get_Registration_type) && get_Registration_type.equals("SGC")) {
                    getpincode = pincode_edt.getText().toString();
                    getmobile_number = mobile_edt.getText().toString();
                    email_address = email_edt.getText().toString();
                    getclient_name = client_name_edt.getText().toString();
                    get_brand_name = brand_spinner.getSelectedItem().toString();
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

                    String enteredUrl = website_edt.getText().toString();

                    if (!enteredUrl.equals("")) {
                        if (Patterns.WEB_URL.matcher(enteredUrl).matches()) {

                        } else {
                            GlobalClass.showTastyToast(activity, MessageConstants.URL_is_invalid, 2);
                        }
                    }
                    if (getLatitude.equalsIgnoreCase("")) {
                        lattitude_txt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_latitude, 2);
                    } else if (getLongitude.equalsIgnoreCase("")) {
                        longitude_txt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_LONG, 2);
                    } else if (getpincode.equals("")) {
                        pincode_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_Correct_pincode, 2);
                    } else if (getpincode.length() < 6) {
                        pincode_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_6_digits_Pincode, 2);
                    } else if (getmobile_number.equals("")) {
                        mobile_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.MOBNO, 2);
                    } else if (getmobile_number.length() < 10) {
                        mobile_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.MOBILE_10_DIGITS, 2);
                    } else if (!Validator.emailValidation(activity, email_edt)) {
                    } else if (getclient_name.equals("")) {
                        client_name_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Please_client_name, 2);
                    } else if (getclient_name.length() < 2) {
                        client_name_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Please_client_name, 2);
                    } else if (get_incharge_name.equals("")) {
                        incharge_name.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Please_incharge_name, 2);
                    } else if (get_incharge_name.length() < 2) {
                        incharge_name.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Please_incharge_name, 2);
                    } else if (get_Address.equals("")) {
                        addressEdt.requestFocus();
                        GlobalClass.showTastyToast(activity, ToastFile.ent_addr, 2);
                    } else if (get_Address.length() < 25) {
                        addressEdt.requestFocus();
                        GlobalClass.showTastyToast(activity, ToastFile.addre25long, 2);
                    } else if (image == null) {
                        cross_img.setVisibility(View.VISIBLE);
                        correct_img.setVisibility(View.GONE);
                        GlobalClass.showTastyToast(activity, MessageConstants.Upload_visit_card, 2);
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
                            GlobalClass.showTastyToast(activity, MessageConstants.INVALID_EMAIL, 2);
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

                } else if (!GlobalClass.isNull(get_Registration_type) && get_Registration_type.equals("PGC")) {
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


                    if (getLatitude.equalsIgnoreCase("")) {
                        lattitude_txt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_latitude, 2);
                    } else if (getLongitude.equalsIgnoreCase("")) {
                        longitude_txt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_LONG, 2);
                    } else if (getpincode.equalsIgnoreCase("")) {
                        pincode_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.ENTER_PINCODE, 2);
                    } else if (getpincode.length() < 6) {
                        pincode_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.Enter_6_digits_Pincode, 2);
                    } else if (getmobile_number.equalsIgnoreCase("")) {
                        mobile_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.MOBNO, 2);
                    } else if (getmobile_number.length() < 10) {
                        mobile_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.MOBILE_10_DIGITS, 2);
                    } else if (email_address.equals("")) {
                        email_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, MessageConstants.ENTER_EMAIL, 2);
                    } else if (get_dr_name.equalsIgnoreCase("")) {
                        dr_name_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, ToastFile.crt_name_woe, 2);
                    } else if (get_dr_name.length() < 2) {
                        dr_name_edt.requestFocus();
                        GlobalClass.showTastyToast(activity, ToastFile.crt_name_woe, 2);
                    } else if (get_Address.equals("")) {
                        addressEdt.requestFocus();
                        GlobalClass.showTastyToast(activity, ToastFile.ent_addr, 2);
                    } else if (get_Address.length() < 25) {
                        addressEdt.requestFocus();
                        GlobalClass.showTastyToast(activity, ToastFile.addre25long, 2);
                    } else if (image == null) {
                        cross_img.setVisibility(View.VISIBLE);
                        correct_img.setVisibility(View.GONE);
                        GlobalClass.showTastyToast(activity, MessageConstants.Upload_visit_card, 2);
                    } else if (!GlobalClass.isNull(email_address)) {

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
                            GlobalClass.showTastyToast(activity, MessageConstants.INVALID_EMAIL, 2);
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
                    GlobalClass.showTastyToast(activity, ent_pin, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(pincode_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(pincode_edt, "");
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
                    GlobalClass.showTastyToast(activity, ToastFile.crt_mob_num, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(mobile_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(mobile_edt, "");

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
                    try {
                        RequestQueue requestQueue = GlobalClass.setVolleyReq(getApplicationContext());
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("type", entered_Type_String);
                        jsonBody.put("mobile", entered_mobile_String);
                        final String requestBody = jsonBody.toString();

                        try {
                            if (ControllersGlobalInitialiser.validateEmail_controller != null) {
                                ControllersGlobalInitialiser.validateEmail_controller = null;
                            }
                            ControllersGlobalInitialiser.validateEmail_controller = new ValidateEmail_Controller(activity,Sgc_Pgc_Entry_Activity.this);
                            ControllersGlobalInitialiser.validateEmail_controller.checkemailvalidate(requestBody,requestQueue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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

                    GlobalClass.showTastyToast(activity, ToastFile.crt_mob_num, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(phone_number, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(phone_number, "");
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

                    GlobalClass.showTastyToast(activity, MessageConstants.INVALID_EMAIL, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(email_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(email_edt, "");
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
                    GlobalClass.EditSetText(email_edt, enteredString.substring(1));
                    GlobalClass.showTastyToast(activity, MessageConstants.Enter_max_50_char, 2);
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
                    GlobalClass.showTastyToast(activity, MessageConstants.Please_client_name, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(client_name_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(client_name_edt, "");
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
                    GlobalClass.EditSetText(client_name_edt, enteredString.substring(1));
                    GlobalClass.showTastyToast(activity, MessageConstants.Enter_max_30_char, 2);
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
                    GlobalClass.showTastyToast(activity, MessageConstants.Please_client_name, 2);
                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(dr_name_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(dr_name_edt, "");
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

                    GlobalClass.showTastyToast(activity, MessageConstants.Please_incharge_name, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(incharge_name, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(incharge_name, "");
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

                    GlobalClass.showTastyToast(activity, MessageConstants.Enter_website, 2);

                    if (enteredString.length() > 0) {
                        GlobalClass.EditSetText(website_edt, enteredString.substring(1));
                    } else {
                        GlobalClass.EditSetText(website_edt, "");
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

        sgc_pgc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sgc_pgc_spinner.getSelectedItem().equals("SGC")) {
                    brand_spinner.setVisibility(View.VISIBLE);
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


                    GlobalClass.EditSetText(pincode_edt, "");
                    GlobalClass.EditSetText(mobile_edt, "");
                    GlobalClass.EditSetText(email_edt, "");
                    GlobalClass.EditSetText(dr_name_edt, "");
                    GlobalClass.EditSetText(client_name_edt, "");
                    GlobalClass.EditSetText(incharge_name, "");
                    GlobalClass.EditSetText(addressEdt, "");
                    GlobalClass.EditSetText(phone_number, "");
                    GlobalClass.EditSetText(website_edt, "");
                    GlobalClass.SetText(lattitude_txt, "");
                    GlobalClass.SetText(longitude_txt, "");

                } else if (!GlobalClass.isNull(sgc_pgc_spinner.getSelectedItem().toString()) && sgc_pgc_spinner.getSelectedItem().toString().equalsIgnoreCase("PGC")) {
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

                    GlobalClass.EditSetText(pincode_edt, "");
                    GlobalClass.EditSetText(mobile_edt, "");
                    GlobalClass.EditSetText(email_edt, "");
                    GlobalClass.EditSetText(dr_name_edt, "");
                    GlobalClass.EditSetText(email_edt, "");
                    GlobalClass.EditSetText(client_name_edt, "");
                    GlobalClass.EditSetText(incharge_name, "");
                    GlobalClass.SetText(lattitude_txt, "");
                    GlobalClass.SetText(longitude_txt, "");
                    GlobalClass.EditSetText(addressEdt, "");
                    GlobalClass.EditSetText(phone_number, "");
                    GlobalClass.EditSetText(website_edt, "");

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
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, 0);
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

    }

    private void initViews() {
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
        latlong = (LinearLayout) findViewById(R.id.latlong);
    }

    private void uploadClientEntry(final String type, String brandName, String name, String qualification, String inchargeName) {
        strtype = type;


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
            requestModel.setVisiting_Card(image);

            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);
            Log.e("Req body--->", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue= GlobalClass.setVolleyReq(Sgc_Pgc_Entry_Activity.this);

        try {
            if (ControllersGlobalInitialiser.sgcController != null) {
                ControllersGlobalInitialiser.sgcController = null;
            }
            ControllersGlobalInitialiser.sgcController = new SGCController(activity,Sgc_Pgc_Entry_Activity.this);
            ControllersGlobalInitialiser.sgcController.sgccontroller(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            try {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place.getLatLng() != null) {
                    Log.e(TAG, "Places---->" + place.getName() + ", " + place.getId());

                    final double lat = place.getLatLng().latitude;
                    final double log = place.getLatLng().longitude;
                    CharSequence name = place.getName();
                    CharSequence address = place.getAddress();

                    pincode = getPinCodeFromAddress(address);
                    GlobalClass.EditSetText(pincode_edt, pincode);
                    GlobalClass.EditSetText(addressEdt, address.toString());
                    GlobalClass.SetText(lattitude_txt, "Lat :" + String.valueOf(lat));
                    lattitude_txt.setError(null);

                    GlobalClass.SetText(longitude_txt, "Long :" + String.valueOf(log));
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

                //ImageName Procedure
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void getemailresponse(String type) {
        if (type.equalsIgnoreCase("SGC")) {
            uploadClientEntry("SGCEntry", get_brand_name, getclient_name, "", get_incharge_name);
        } else {
            uploadClientEntry("PGCEntry", "", "DR " + get_dr_name, get_qualification, "");
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

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }


    public static String getPinCodeFromAddress(CharSequence address) {

        String pincodeStr = String.valueOf(address);

        //Pincode Extracter
        Pattern zipPattern = Pattern.compile("(\\d{6})");
        Matcher zipMatcher = zipPattern.matcher(pincodeStr);

        String zip = "";
        if (zipMatcher.find()) {
            zip = zipMatcher.group(1);
        }

        return zip;
    }


    public void getSGCResponse(JSONObject response) {

        try {
            Log.e(TAG, "onResponse: " + response);


            ClientRegisterResponseModel responseModel = new Gson().fromJson(String.valueOf(response), ClientRegisterResponseModel.class);

            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {

                    if (!GlobalClass.isNull(strtype) && strtype.equalsIgnoreCase("PGCEntry")) {
                        GlobalClass.showTastyToast(activity, MessageConstants.PGC_SUCC, 1);
                    } else {
                        GlobalClass.showTastyToast(activity, MessageConstants.SGC_SUCC, 1);
                    }

                    Intent i = new Intent(Sgc_Pgc_Entry_Activity.this, Sgc_Pgc_Entry_Activity.class);
                    startActivity(i);
                    finish();
                } else if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(small_invalidApikey)) {
                    GlobalClass.redirectToLogin(Sgc_Pgc_Entry_Activity.this);
                } else {
                    GlobalClass.showTastyToast(activity, responseModel.getMessage(), 2);
                }
            } else {
                GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getvalidateemailResponse(String response) {
        try {
            if (!GlobalClass.isNull(response) && response.contains("\"TRUE\"")) {
                Log.e(TAG,"SUCCESS");
            } else {
                mobile_edt.setError(response);
                GlobalClass.EditSetText(mobile_edt, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
