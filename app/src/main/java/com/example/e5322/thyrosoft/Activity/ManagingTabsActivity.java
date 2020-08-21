package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.frags.RapidAntibodyFrag;
import com.example.e5322.thyrosoft.BottomNavigationViewHelper;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.CheckVideoDataController;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.Myprofile_Controller;
import com.example.e5322.thyrosoft.Controller.Videopoppost_Controller;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Kotlin.KTActivity.AccreditationActivity;
import com.example.e5322.thyrosoft.Kotlin.KTActivity.FAQ_activity;
import com.example.e5322.thyrosoft.Models.GetVideoResponse_Model;
import com.example.e5322.thyrosoft.Models.ResponseModels.ProfileDetailsResponseModel;
import com.example.e5322.thyrosoft.Models.Videopoppost_response;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.UploadDocument;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.BuildConfig;

import static com.example.e5322.thyrosoft.API.Constants.NHF;

public class ManagingTabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String getdate;
    public SharedPreferences sharedpreferences;
    String restoredText;
    String passwrd, access, api_key, USER_CODE;
    SharedPreferences prefs;
    TextView navigationDrawerNameTSP, ecode;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    boolean IsFromNotification;
    boolean covidacc = false;
    int SCRID;
    boolean iscomfrom;
    String VideoID;
    ImageView ic_close;
    AlertDialog dialog;
    SeekBar videoseekbar;
    TextView txt_toltime, txt_ctime;
    RelativeLayout rel_time;
    long milliseconds, minutes, seconds;
    ImageView ic_play, ic_pause;
    boolean iscomplete = false;
    boolean isVideosee = false;
    Activity activity;
    SharedPreferences covid_pref;
    SharedPreferences pref_versioncheck;
    private String TAG = getClass().getSimpleName();
    private CarouselFragment carouselFragment;
    private String user, CLIENT_TYPE;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseHelper db;
    private VideoView video_view;
    private int offline_draft_counts;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("WrongConstant")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_nav:
                    Intent i = new Intent(ManagingTabsActivity.this, ManagingTabsActivity.class);
                    startActivity(i);
                    return true;
                case R.id.commu:
                    Intent j = new Intent(ManagingTabsActivity.this, Communication_Activity.class);
                    startActivity(j);
                    return true;

                case R.id.loud:
                    Intent k = new Intent(ManagingTabsActivity.this, Noticeboard_activity.class);
                    startActivity(k);
                    return true;

                case R.id.bell_ic:
                    Intent l = new Intent(ManagingTabsActivity.this, Notification_activity.class);
                    startActivity(l);
                    return true;

                case R.id.hamb_ic:
                    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawerLayout.openDrawer(Gravity.END);
                    GlobalClass.Hidekeyboard(drawerLayout);
                    return true;
            }
            return false;
        }
    };
    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            try {
                videoseekbar.setProgress(video_view.getCurrentPosition());
                mSeekbarUpdateHandler.postDelayed(this, 50);
                GlobalClass.SetText(txt_toltime, "" + video_view.getDuration());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private ImageView imageViewprofile;

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.activity_main_ll);
        activity = this;

        initViews(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(Bundle savedInstanceState) {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);


        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        pref_versioncheck = getSharedPreferences("pref_versioncheck", MODE_PRIVATE);

        if (pref_versioncheck != null) {
            int versionCode = BuildConfig.VERSION_CODE;
            int prefversioncode = pref_versioncheck.getInt("versioncode", 0);

            Log.e(TAG, "prefversioncode --->" + prefversioncode + "  versionCode-->" + versionCode);

            if (prefversioncode != versionCode) {
                startActivity(new Intent(ManagingTabsActivity.this, Login.class));
                finish();
            }

        }

        covid_pref = getSharedPreferences("COVIDETAIL", MODE_PRIVATE);
        covidacc = covid_pref.getBoolean("covidacc", false);

        if (savedInstanceState == null) {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserving the fragment stack inside each tab
            initScreen();
        } else {
            // restoring the previously created fragment
            // and getting the reference
            carouselFragment = (CarouselFragment) getSupportFragmentManager().getFragments().get(0);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);

        navigationView.addHeaderView(headerView);

        navigationDrawerNameTSP = (TextView) headerView.findViewById(R.id.navigationDrawerNameTSP);
        ecode = (TextView) headerView.findViewById(R.id.ecode);
        imageViewprofile = (ImageView) headerView.findViewById(R.id.imageViewprofile);

        covid_pref = getSharedPreferences("COVIDETAIL", MODE_PRIVATE);
        covidacc = covid_pref.getBoolean("covidacc", false);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        USER_CODE = prefs.getString("USER_CODE", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");


        try {
            db = new DatabaseHelper(ManagingTabsActivity.this);
            db.open();
            offline_draft_counts = db.getProfilesCount();
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (!GlobalClass.isNull(CLIENT_TYPE) && CLIENT_TYPE.equalsIgnoreCase(NHF)) {

            navigationView.getMenu().findItem(R.id.payment).setVisible(false);
            navigationView.getMenu().findItem(R.id.otp_credit).setVisible(false);
            navigationView.getMenu().findItem(R.id.feedback).setVisible(true);
            navigationView.getMenu().findItem(R.id.profile).setVisible(true);
            navigationView.getMenu().findItem(R.id.notification).setVisible(true);
            navigationView.getMenu().findItem(R.id.phone).setVisible(true);
            navigationView.getMenu().findItem(R.id.whatsapp).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);


            navigationView.getMenu().findItem(R.id.upload_document_navigation).setVisible(false);
            navigationView.getMenu().findItem(R.id.sgc_pgc_entry_data).setVisible(false);
            navigationView.getMenu().findItem(R.id.communication).setVisible(false);
            navigationView.getMenu().findItem(R.id.vid_leggy).setVisible(false);
            navigationView.getMenu().findItem(R.id.notice).setVisible(false);
            navigationView.getMenu().findItem(R.id.notification).setVisible(false);
            navigationView.getMenu().findItem(R.id.synchronization).setVisible(false);
            navigationView.getMenu().findItem(R.id.faq_data).setVisible(false);
            navigationView.getMenu().findItem(R.id.accr_data).setVisible(false);
            navigationView.getMenu().findItem(R.id.offer_data).setVisible(false);
            navigationView.getMenu().findItem(R.id.articles_data).setVisible(false);
            navigationView.getMenu().findItem(R.id.company_contcat).setVisible(false);
            navigationView.getMenu().findItem(R.id.thyroshop).setVisible(false);
            navigationView.getMenu().findItem(R.id.bs_entry).setVisible(false);
            navigationView.getMenu().findItem(R.id.rbarcode).setVisible(false);

        } else {
            if (!GlobalClass.isNull(access) && access.equals("STAFF")) {
                if (covidacc) {
                    navigationView.getMenu().findItem(R.id.covid_reg).setVisible(true);
                    navigationView.getMenu().findItem(R.id.crab_camp).setVisible(true);
                } else {
                    navigationView.getMenu().findItem(R.id.covid_reg).setVisible(false);
                    navigationView.getMenu().findItem(R.id.crab_camp).setVisible(false);
                }

                navigationView.getMenu().findItem(R.id.communication).setVisible(true);
                navigationView.getMenu().findItem(R.id.notification).setVisible(true);
                navigationView.getMenu().findItem(R.id.notice).setVisible(true);
                navigationView.getMenu().findItem(R.id.feedback).setVisible(true);
                navigationView.getMenu().findItem(R.id.logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.phone).setVisible(true);
                navigationView.getMenu().findItem(R.id.whatsapp).setVisible(true);
                navigationView.getMenu().findItem(R.id.profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.synchronization).setVisible(true);
            } else if (!GlobalClass.isNull(access) && access.equals("ADMIN")) {
                if (covidacc) {
                    navigationView.getMenu().findItem(R.id.covid_reg).setVisible(true);
                    navigationView.getMenu().findItem(R.id.crab_camp).setVisible(true);
                } else {
                    navigationView.getMenu().findItem(R.id.covid_reg).setVisible(false);
                    navigationView.getMenu().findItem(R.id.crab_camp).setVisible(true);
                }
                navigationView.getMenu().findItem(R.id.communication).setVisible(true);
                navigationView.getMenu().findItem(R.id.notification).setVisible(true);
                navigationView.getMenu().findItem(R.id.notice).setVisible(true);
                navigationView.getMenu().findItem(R.id.feedback).setVisible(true);
                navigationView.getMenu().findItem(R.id.logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.phone).setVisible(true);
                navigationView.getMenu().findItem(R.id.whatsapp).setVisible(true);
                navigationView.getMenu().findItem(R.id.profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.synchronization).setVisible(true);
            }
        }

        SharedPreferences getProfileName = getSharedPreferences("profilename", MODE_PRIVATE);
        String name = getProfileName.getString("name", "");
        String usercode = getProfileName.getString("usercode", "");
        String profile_image = getProfileName.getString("image", "");

        if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(NHF) && !CLIENT_TYPE.equalsIgnoreCase(NHF)) {
            if (getIntent().hasExtra(Constants.COMEFROM)) {
                iscomfrom = getIntent().getBooleanExtra(Constants.COMEFROM, false);
                if (iscomfrom) {
                    Log.e(TAG, " C O M E F R O M -------->" + iscomfrom);
                    if (GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                        CheckVideoData();
                    }
                }
            }
        }


        if (getIntent().hasExtra(Constants.IsFromNotification)) {
            IsFromNotification = getIntent().getBooleanExtra(Constants.IsFromNotification, false);
            if (IsFromNotification) {
                if (getIntent().hasExtra("Screen_category")) {
                    SCRID = getIntent().getIntExtra("Screen_category", 0);
                    Log.e(TAG, "Screen ID ---->" + SCRID);
                    callnotifiedScreen(SCRID);
                }
            }
        }

        if (!GlobalClass.isNull(name)) {
            GlobalClass.SetText(navigationDrawerNameTSP, "HI " + name);
            GlobalClass.SetText(ecode, "(" + usercode + ")");
        }

        if (GlobalClass.isNetworkAvailable(this)) {
            getProfileDetails(ManagingTabsActivity.this);
        } else {

            GlobalClass.showTastyToast(activity, MessageConstants.CHECK_INTERNET_CONN, 2);
        }


        Glide.with(ManagingTabsActivity.this)
                .load(profile_image)
                .placeholder(ManagingTabsActivity.this.getResources().getDrawable(R.drawable.userprofile))
                .into(imageViewprofile);

    }


    private void CheckVideoData() {
        try {
            if (ControllersGlobalInitialiser.checkVideoDataController != null) {
                ControllersGlobalInitialiser.checkVideoDataController = null;
            }
            ControllersGlobalInitialiser.checkVideoDataController = new CheckVideoDataController(activity, ManagingTabsActivity.this);
            ControllersGlobalInitialiser.checkVideoDataController.checkvideodata_controller(user);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void closeVideo(String id, final AlertDialog dialog) {
        try {
            if (ControllersGlobalInitialiser.videopoppost_controller != null) {
                ControllersGlobalInitialiser.videopoppost_controller = null;
            }
            ControllersGlobalInitialiser.videopoppost_controller = new Videopoppost_Controller(activity, ManagingTabsActivity.this);
            ControllersGlobalInitialiser.videopoppost_controller.VideopostController(id, user, dialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callnotifiedScreen(int SCRID) {
        if (SCRID == Constants.SCR_1) {
            if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(NHF) && !CLIENT_TYPE.equalsIgnoreCase(NHF)) {
                Intent startIntent = new Intent(ManagingTabsActivity.this, Payment_Activity.class);
                startIntent.putExtra("COMEFROM", "ManagingTabsActivity");
                startActivity(startIntent);
            }

        } else if (SCRID == Constants.SCR_2) {
            if (!CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                Intent i = new Intent(ManagingTabsActivity.this, FAQ_activity.class);
                startActivity(i);
            }
        } else if (SCRID == Constants.SCR_3) {
            if (!CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                Intent i = new Intent(ManagingTabsActivity.this, VideoActivity.class);
                startActivity(i);
            }
        } else if (SCRID == Constants.SCR_4) {
            Bundle bundle = new Bundle();
            if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(NHF) && CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                bundle.putInt("position", 0);
            } else {
                if (!GlobalClass.isNull(access) && !GlobalClass.isNull(Constants.ADMIN) && access.equalsIgnoreCase(Constants.ADMIN)) {
                    if (covidacc) {
                        bundle.putInt("position", 8);
                    } else {
                        bundle.putInt("position", 7);
                    }

                } else if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(Constants.STAFF) && access.equalsIgnoreCase(Constants.STAFF)) {
                    if (covidacc) {
                        bundle.putInt("position", 7);
                    } else {
                        bundle.putInt("position", 6);
                    }
                }
            }
            carouselFragment = new CarouselFragment();
            carouselFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, carouselFragment).commit();

        } else if (SCRID == Constants.SCR_5) {
            if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(NHF) && !CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 3);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, carouselFragment).commit();
            }
        } else if (SCRID == Constants.SCR_6) {
            Constants.ratfrag_flag = "1";
            Constants.pushrat_flag = 1;
            Constants.universal = 1;
            Bundle bundle = new Bundle();
            if (!GlobalClass.isNull(CLIENT_TYPE) && !GlobalClass.isNull(NHF) && CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                bundle.putInt("position", 0);
            } else {
                if (access.equalsIgnoreCase(Constants.ADMIN)) {
                    if (covidacc) {
                        bundle.putInt("position", 1);
                    }

                } else if (!GlobalClass.isNull(access) && !GlobalClass.isNull(Constants.STAFF) && access.equalsIgnoreCase(Constants.STAFF)) {
                    if (covidacc) {
                        bundle.putInt("position", 1);
                    }
                }
            }

            carouselFragment = new CarouselFragment();
            carouselFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, carouselFragment).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottomNavigationView view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_navigation) {
            try {
                getIntent();
                if (!popFragment()) {

                } else {
                }
                Intent intentstart = new Intent(ManagingTabsActivity.this, ManagingTabsActivity.class);
                startActivity(intentstart);

            } catch (Exception e) {
                e.printStackTrace();
            }
            // Handle the camera action
        } else if (id == R.id.guide_me) {
            Intent i = new Intent(ManagingTabsActivity.this, VidoePlayerActivity.class);
            startActivity(i);
        } else if (id == R.id.woe) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                GlobalClass.flagToSendfromnavigation = true;
                GlobalClass.flagtoMove = true;
                GlobalClass.flagToSend = false;

                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();


            }
        } else if (id == R.id.offlinewoe) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", 1);
            carouselFragment = new CarouselFragment();
            carouselFragment.setArguments(bundle);
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, carouselFragment)
                    .commit();


        } else if (id == R.id.chn) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 6);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();

            }

        } else if (id == R.id.result) {

            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 3);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();

            }

        } else if (id == R.id.trackdetails) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 2);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();
            }
        } else if (id == R.id.ratecal) {

            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 8);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();


            }


        } else if (id == R.id.payment) {

            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent startIntent = new Intent(ManagingTabsActivity.this, Payment_Activity.class);
                startIntent.putExtra("COMEFROM", "ManagingTabsActivity");
                startActivity(startIntent);
            }


        } else if (id == R.id.communication) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Communication_Activity.class);
                startActivity(i);
            }

        } else if (id == R.id.vid_leggy) {

            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, VideoActivity.class);
                startActivity(i);
            }

        } else if (id == R.id.thyroshop) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("http:\\/\\/www.charbi.com\\/cdn\\/applications\\/android\\/thyromart.apk"));
                startActivity(httpIntent);
            }


        } else if (id == R.id.billing) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {

                Bundle bundle = new Bundle();
                bundle.putInt("position", 7);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();

            }


        } else if ((id == R.id.crab_camp)) {


            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, RapidAntibodyFrag.class);
                startActivity(i);
            }


        }
        if (id == R.id.upload_document_navigation) {

            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, UploadDocument.class);
                startActivity(i);

            }


        } else if (id == R.id.sgc_pgc_entry_data) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Sgc_Pgc_Entry_Activity.class);
                startActivity(i);

            }


        } else if (id == R.id.leadgenrate) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, LeadGenerationActivity.class);
                startActivity(i);

            }

        } else if (id == R.id.ledger) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 4);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();
            }
        } else if (id == R.id.notice) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Noticeboard_activity.class);
                startActivity(i);
            }

        } else if (id == R.id.notification) {

            Intent i = new Intent(ManagingTabsActivity.this, Notification_activity.class);
            startActivity(i);

        } else if (id == R.id.feedback) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Feedback_activity.class);
                startActivity(i);

            }


        } else if (id == R.id.profile) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, MyProfile_activity.class);
                startActivity(i);
            }

        } else if (id == R.id.broadCast) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, BroadcastActivity.class);
                startActivity(i);

            }

        } else if (id == R.id.campIntimation) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, CampIntimation.class);
                startActivity(i);
            }

        } else if (id == R.id.stock_availability) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                // Intent i = new Intent(ManagingTabsActivity.this, BMC_StockAvailabilityActivity.class);
                //startActivity(i);
            }
        } else if (id == R.id.bs_entry) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Blood_sugar_entry_activity.class);
                startActivity(i);
            }

        } else if (id == R.id.covid_reg) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, carouselFragment)
                        .commit();

            }

        } else if (id == R.id.otp_credit) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, OTPCreditMISActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setMessage(ToastFile.surelogout)
                    .setCancelable(false)
                    .setPositiveButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GlobalClass.showTastyToast(activity, getResources().getString(R.string.Success), 1);
                            distoye();
                            new LogUserActivityTagging(activity, Constants.LOGOUT);
                            logout();
                        }
                    })
                    .setNegativeButton(MessageConstants.NO, null)
                    .show();
        } else if (id == R.id.phone) {
            new AlertDialog.Builder(this)
                    .setMessage("Would you like to proceed with call?")
                    .setPositiveButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = getSharedPreferences("TspNumber", MODE_PRIVATE);
                            restoredText = prefs.getString("TSPMobileNumber", null);

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            if (CLIENT_TYPE.equalsIgnoreCase(NHF)) {
                                intent.setData(Uri.parse("tel:" + Constants.NHF_Whatsapp));
                            } else {
                                intent.setData(Uri.parse("tel:" + restoredText));
                            }

                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(MessageConstants.NO, null)
                    .show();

        } else if (id == R.id.whatsapp) {
            new AlertDialog.Builder(this)
                    .setMessage("Would you like to proceed with whatsapp?")
                    .setPositiveButton(MessageConstants.YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs1 = getSharedPreferences("TspNumber", MODE_PRIVATE);
                            restoredText = prefs1.getString("TSPMobileNumber", null);
                            Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                            if (CLIENT_TYPE.equalsIgnoreCase(NHF)) {
                                httpIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+91" + Constants.NHF_Whatsapp + "#"));
                            } else {
                                httpIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+91" + restoredText + "#"));
                            }

                            startActivity(httpIntent);

                        }
                    })
                    .setNegativeButton(MessageConstants.NO, null)
                    .show();
        } else if (id == R.id.offer_data) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, SpecialOffer_Activity.class);
                startActivity(i);
            }
        } else if (id == R.id.synchronization) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
                SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
                prefsEditor1.remove("MyObject");
                prefsEditor1.commit();

                SharedPreferences appSharedPrefsdata = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
                SharedPreferences.Editor prefsEditor2 = appSharedPrefsdata.edit();
                prefsEditor2.remove("savelabnames");
                prefsEditor2.commit();

                SharedPreferences saveAlldata = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
                SharedPreferences.Editor deletepredf = saveAlldata.edit();
                deletepredf.remove("saveAlldata");
                deletepredf.commit();

                SharedPreferences myData = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
                SharedPreferences.Editor prefsEditordata = myData.edit();
                prefsEditordata.remove("getBtechnames");
                prefsEditordata.commit();

                Intent i = new Intent(ManagingTabsActivity.this, ManagingTabsActivity.class);
                startActivity(i);

            }
        } else if (id == R.id.faq_data) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, FAQ_activity.class);
                startActivity(i);

            }

        } else if (id == R.id.accr_data) {
            Intent i = new Intent(ManagingTabsActivity.this, AccreditationActivity.class);
            startActivity(i);

        } else if (id == R.id.articles_data) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, HealthArticle_Activity.class);
                startActivity(i);
            }


        } else if (id == R.id.company_contcat) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, CompanyContact_activity.class);
                startActivity(i);
            }

        } else if (id == R.id.rbarcode) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, ReportBarcode_activity.class);
                startActivity(i);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void distoye() {
        Intent intent = new Intent(ManagingTabsActivity.this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();

        SharedPreferences.Editor prefs = getSharedPreferences("Userdetails", MODE_PRIVATE).edit();
        prefs.remove("Username");
        prefs.remove("password");
        prefs.remove("ACCESS_TYPE");
        prefs.remove("API_KEY");

    }

    public boolean popFragment() {
        boolean isPop = false;

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.viewpager);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getSupportFragmentManager().popBackStackImmediate();

        }
        return isPop;
    }

    public void getProfileDetails(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String strurl = Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile";
        try {
            if (ControllersGlobalInitialiser.myprofile_controller != null) {
                ControllersGlobalInitialiser.myprofile_controller = null;
            }
            ControllersGlobalInitialiser.myprofile_controller = new Myprofile_Controller(activity, ManagingTabsActivity.this);
            ControllersGlobalInitialiser.myprofile_controller.getmyprofilecontroller(strurl, queue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        SharedPreferences.Editor covidprefeditor = getSharedPreferences("COVIDETAIL", MODE_PRIVATE).edit();
        covidprefeditor.clear();
        covidprefeditor.apply();

        SharedPreferences.Editor getProfileName = getSharedPreferences("profilename", MODE_PRIVATE).edit();
        getProfileName.clear();
        getProfileName.commit();

        SharedPreferences.Editor profile = getSharedPreferences("profile", MODE_PRIVATE).edit();
        profile.clear();
        profile.commit();

        SharedPreferences.Editor editor1 = getSharedPreferences("profilename", 0).edit();
        editor1.clear();
        editor1.commit();

        SharedPreferences.Editor editor = getSharedPreferences("Userdetails", 0).edit();
        editor.clear();
        editor.commit();

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
        SharedPreferences.Editor prefsEditor1 = appSharedPrefs.edit();
        prefsEditor1.clear();
        prefsEditor1.commit();

        SharedPreferences profilenamesharedpref = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
        SharedPreferences.Editor profile_shared = profilenamesharedpref.edit();
        profile_shared.clear();
        profile_shared.commit();

        SharedPreferences prifleDataShared = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
        SharedPreferences.Editor profile_shared_pref = prifleDataShared.edit();
        profile_shared_pref.clear();
        profile_shared_pref.commit();


        SharedPreferences saveAlldata = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
        SharedPreferences.Editor deletepredf = saveAlldata.edit();
        deletepredf.clear();
        deletepredf.commit();

        SharedPreferences myData = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
        SharedPreferences.Editor prefsEditordata = myData.edit();
        prefsEditordata.clear();
        prefsEditordata.commit();

        SharedPreferences appSharedPrefsDtaa = PreferenceManager.getDefaultSharedPreferences(ManagingTabsActivity.this);
        SharedPreferences.Editor mydataModel = appSharedPrefsDtaa.edit();
        mydataModel.clear();
        mydataModel.commit();

        clearApplicationData();

        Constants.covidwoe_flag = "0";
        Constants.covidfrag_flag = "0";
        Constants.ratfrag_flag = "0";
        Constants.pushrat_flag = 0;
        Constants.universal = 0;
        Constants.PUSHNOT_FLAG = false;


        Intent f = new Intent(getApplicationContext(), Login.class);
        startActivity(f);
        finish();
    }

    public void clearApplicationData() {
//        Log.e(TAG, "<< Clear App Data >>");
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    private void initScreen() {
        // Creating the ViewPager container fragment once
        carouselFragment = new CarouselFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, carouselFragment)
                .commit();
    }


    @Override
    public void onBackPressed() {

        if (!carouselFragment.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.close_app));
            builder.setCancelable(false);
            builder.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // moveTaskToBack(true);
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    finishAffinity();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    private void postdata(VideoView videoView) {

        milliseconds = videoView.getCurrentPosition();
        minutes = (milliseconds / 1000) / 60;
        seconds = (milliseconds / 1000) % 60;
        VideoDataposter videoDataposter = new VideoDataposter(ManagingTabsActivity.this, minutes, seconds);
        videoDataposter.videoDatapost(VideoID, minutes, seconds);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getCheckdataResponse(final retrofit2.Response<GetVideoResponse_Model> response) {
        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase("RSS0000")) {
                if (GlobalClass.CheckArrayList(response.body().getOutput())) {
                    /*TODO Launching New video Pop up fragment */

                    if (!GlobalClass.isNull(response.body().getOutput().get(0).getPath())) {

                        LayoutInflater inflater = getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.fragment_newvideo, null);
                        ic_close = alertLayout.findViewById(R.id.img_close);

                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        TextView tv_title = alertLayout.findViewById(R.id.title);
                        TextView tv_desc = alertLayout.findViewById(R.id.txt_desc);

                        txt_ctime = alertLayout.findViewById(R.id.txt_ctime);
                        txt_toltime = alertLayout.findViewById(R.id.txt_toltime);
                        rel_time = alertLayout.findViewById(R.id.rel_time);

                        ic_play = alertLayout.findViewById(R.id.img_start);
                        ic_pause = alertLayout.findViewById(R.id.img_pause);

                        final LinearLayout linvid = alertLayout.findViewById(R.id.linvid);

                        final LinearLayout layoutVideo = alertLayout.findViewById(R.id.layoutVideo);

                        video_view = alertLayout.findViewById(R.id.video_view);
                        videoseekbar = alertLayout.findViewById(R.id.seekBar);

                        if (!GlobalClass.isNull(response.body().getOutput().get(0).getTitle())) {
                            GlobalClass.SetText(tv_title, response.body().getOutput().get(0).getTitle());
                        }

                        if (!GlobalClass.isNull(response.body().getOutput().get(0).getDescription())) {
                            GlobalClass.SetText(tv_desc, response.body().getOutput().get(0).getDescription());
                        }


                        layoutVideo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {

                                    if (response.body() != null && !GlobalClass.isNull(response.body().getOutput().get(0).getPath())) {

                                        linvid.setVisibility(View.VISIBLE);
                                        layoutVideo.setVisibility(View.GONE);
                                        video_view.setVisibility(View.VISIBLE);


                                        final MediaController mediaController = new MediaController(ManagingTabsActivity.this);
                                        mediaController.setAnchorView(video_view);
                                        video_view.setMediaController(mediaController);
                                        Uri uri = Uri.parse(response.body().getOutput().get(0).getPath());
                                        video_view.setVideoURI(uri);
                                        video_view.requestFocus();
                                        video_view.start();
                                        isVideosee = true;

                                        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);

                                        rel_time.setVisibility(View.VISIBLE);
                                        ic_pause.setVisibility(View.VISIBLE);

                                        VideoID = response.body().getOutput().get(0).getId();

                                        ic_pause.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mediaController != null) {
                                                    video_view.pause();
                                                    mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                                                    ic_pause.setVisibility(View.INVISIBLE);
                                                    ic_play.setVisibility(View.VISIBLE);
                                                    postdata(video_view);
                                                }
                                            }
                                        });

                                        ic_play.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mediaController != null) {
                                                    video_view.start();

                                                    if (iscomplete) {
                                                        videoseekbar.setProgress(0);
                                                        video_view.requestFocus();
                                                    }
                                                    ic_play.setVisibility(View.INVISIBLE);
                                                    ic_pause.setVisibility(View.VISIBLE);
                                                    postdata(video_view);
                                                }
                                            }
                                        });

                                        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                if (mp != null) {
                                                    iscomplete = true;
                                                    ic_play.setVisibility(View.VISIBLE);
                                                    ic_pause.setVisibility(View.INVISIBLE);
                                                    postdata(video_view);
                                                    mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                                                    dialog.dismiss();
                                                }
                                            }
                                        });

                                        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                videoseekbar.setMax(video_view.getDuration());
                                                milliseconds = video_view.getDuration();
                                                minutes = (milliseconds / 1000) / 60;
                                                seconds = (milliseconds / 1000) % 60;
                                            }
                                        });


                                        videoseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                            @Override
                                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                                if (fromUser) {
                                                    video_view.seekTo(progress);
                                                    milliseconds = video_view.getCurrentPosition();
                                                    minutes = (milliseconds / 1000) / 60;
                                                    seconds = (milliseconds / 1000) % 60;

                                                }
                                            }

                                            @Override
                                            public void onStartTrackingTouch(SeekBar seekBar) {
                                            }

                                            @Override
                                            public void onStopTrackingTouch(SeekBar seekBar) {
                                            }

                                        });


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagingTabsActivity.this);
                        alert.setView(alertLayout);

                        alert.setCancelable(false);
                        dialog = alert.create();
                        dialog.show();

                        ic_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!GlobalClass.isNull(response.body().getOutput().get(0).getId())) {
                                    if (GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                                        ic_close.setClickable(false);
                                        ic_close.setEnabled(false);
                                        if (isVideosee) {
                                            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
                                            postdata(video_view);
                                        }
                                        if (GlobalClass.isNetworkAvailable(activity)) {
                                            closeVideo(response.body().getOutput().get(0).getId(), dialog);
                                        } else {
                                            GlobalClass.showTastyToast(activity, MessageConstants.CHECK_INTERNET_CONN, 2);
                                        }

                                    }
                                }

                            }
                        });
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVideopostResponse(retrofit2.Response<Videopoppost_response> response, AlertDialog dialog) {

        try {
            if (response.body() != null && !GlobalClass.isNull(response.body().getResId()) && response.body().getResId().equalsIgnoreCase(Constants.RES0000)) {
                dialog.dismiss();
                ic_close.setClickable(true);
                ic_close.setEnabled(true);
                iscomplete = false;
                isVideosee = false;
            } else {
                GlobalClass.showTastyToast(ManagingTabsActivity.this, MessageConstants.SOMETHING_WENT_WRONG, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getMyprofileRespponse(JSONObject response) {
        try {
            if (response != null) {
                Log.e(TAG, "onResponse: " + response);

                Gson gson = new Gson();
                ProfileDetailsResponseModel responseModel = gson.fromJson(String.valueOf(response), ProfileDetailsResponseModel.class);

                if (responseModel != null) {
                    Constants.preotp = responseModel.getPriOTP();

                    if (GlobalClass.isNull(Constants.preotp)) {
                        Constants.preotp = "";
                    }

                    Log.e(TAG, "balance ---->" + responseModel.getBalance());

                    SharedPreferences.Editor saveProfileDetails = getSharedPreferences("profile", 0).edit();
                    saveProfileDetails.putString("prof", responseModel.getTsp_image());
                    saveProfileDetails.putString("ac_code", responseModel.getAc_code());
                    saveProfileDetails.putString("address", responseModel.getAddress());
                    saveProfileDetails.putString("email", responseModel.getEmail());
                    saveProfileDetails.putString("mobile", responseModel.getMobile());
                    saveProfileDetails.putString("name", responseModel.getName());
                    saveProfileDetails.putString("pincode", responseModel.getPincode());
                    saveProfileDetails.putString("user_code", responseModel.getUser_code());
                    saveProfileDetails.putString("closing_balance", responseModel.getClosing_balance());
                    saveProfileDetails.putString("balance", responseModel.getBalance());
                    saveProfileDetails.putString(Constants.credit_limit, responseModel.getCredit_limit());
                    saveProfileDetails.putString("doj", responseModel.getDoj());
                    saveProfileDetails.putString("source_code", responseModel.getSource_code());
                    saveProfileDetails.putString("tsp_image", responseModel.getTsp_image());
                    saveProfileDetails.putString(Constants.unbilledWOE, responseModel.getUnbilledWOE());
                    saveProfileDetails.putString(Constants.unbilledMaterial, responseModel.getUnbilledmaterial());
                    saveProfileDetails.putInt(Constants.rate_percent, responseModel.getRatePercent());
                    saveProfileDetails.putInt(Constants.max_amt, responseModel.getMaxAmount());
                    saveProfileDetails.apply();


                    SharedPreferences.Editor saveProfileData = getSharedPreferences("profilename", 0).edit();
                    saveProfileData.putString("name", responseModel.getName());
                    saveProfileData.putString("usercode", responseModel.getUser_code());
                    saveProfileData.putString("mobile", responseModel.getMobile());
                    saveProfileData.putString("image", responseModel.getTsp_image());
                    saveProfileData.putString("email", responseModel.getEmail());
                    saveProfileData.apply();

                    Log.e(TAG, "onResponse: tsp name and code" + responseModel.getName() + " " + responseModel.getUser_code());

                    if (!GlobalClass.isNull(responseModel.getName()) && !GlobalClass.isNull(responseModel.getUser_code())) {
                        GlobalClass.SetText(navigationDrawerNameTSP, "HI " + responseModel.getName());
                        GlobalClass.SetText(ecode, "(" + responseModel.getUser_code() + ")");

                    } else {
                        GlobalClass.SetText(navigationDrawerNameTSP, "HI");

                    }

                    Glide.with(activity)
                            .load(responseModel.getTsp_image())
                            .placeholder(activity.getResources().getDrawable(R.drawable.userprofile))
                            .into(imageViewprofile);
                } else {
                    GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
                }
            } else {
                GlobalClass.showTastyToast(activity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
