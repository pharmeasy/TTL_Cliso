package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.BottomNavigationViewHelper;
import com.example.e5322.thyrosoft.Cliso_BMC.BMC_StockAvailabilityActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.ProfileDetailsResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.UploadDocument;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.io.File;

import static com.example.e5322.thyrosoft.API.Constants.NHF;

public class ManagingTabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String getdate;
    private static android.support.v4.app.FragmentManager fragmentManager;
    public SharedPreferences sharedpreferences;
    String restoredText;
    String passwrd, access, api_key, USER_CODE;
    SharedPreferences prefs;
    TextView navigationDrawerNameTSP, ecode;
    ImageView imageViewprofile, home;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    boolean IsFromNotification;
    int SCRID;
    private String TAG = getClass().getSimpleName();
    private CarouselFragment carouselFragment;
    private String user, CLIENT_TYPE;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseHelper db;
    private int offline_draft_counts;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_nav:
                    Intent i = new Intent(ManagingTabsActivity.this, ManagingTabsActivity.class);
                    startActivity(i);
                    return true;
                case R.id.commu:
                    Intent j = new Intent(ManagingTabsActivity.this, Communication_Activity.class);
//                    j.putExtra("comefrom", "TSP");
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
                    return true;
            }
            return false;
        }
    };

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
        setContentView(R.layout.activity_main_ll);

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
//        View headerView =(View) LayoutInflater.inflate(R.layout.nav_header_main, navigationView, false);

        navigationView.addHeaderView(headerView);
        navigationDrawerNameTSP = (TextView) headerView.findViewById(R.id.navigationDrawerNameTSP);
        ecode = (TextView) headerView.findViewById(R.id.ecode);
        imageViewprofile = (ImageView) headerView.findViewById(R.id.imageViewprofile);

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        USER_CODE = prefs.getString("USER_CODE", null);
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);

        db = new DatabaseHelper(ManagingTabsActivity.this);
        db.open();
        offline_draft_counts = db.getProfilesCount();
        db.close();

        if (CLIENT_TYPE.equalsIgnoreCase(NHF)) {
            navigationView.getMenu().findItem(R.id.payment).setVisible(true);
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

        } else {
            if (access.equals("STAFF")) {
                //navigationView.getMenu().findItem(R.id.home_navigation).setVisible(true);
                //navigationView.getMenu().findItem(R.id.offlinewoe).setVisible(true);
                // navigationView.getMenu().findItem(R.id.woe).setVisible(true);
                //navigationView.getMenu().findItem(R.id.result).setVisible(true);
                //navigationView.getMenu().findItem(R.id.trackdetails).setVisible(true);
                //navigationView.getMenu().findItem(R.id.ratecal).setVisible(true);
                //navigationView.getMenu().findItem(R.id.payment).setVisible(true);
                // navigationView.getMenu().findItem(R.id.ledger).setVisible(false);
                //navigationView.getMenu().findItem(R.id.billing).setVisible(false);
                navigationView.getMenu().findItem(R.id.communication).setVisible(true);
                navigationView.getMenu().findItem(R.id.notification).setVisible(true);
                navigationView.getMenu().findItem(R.id.notice).setVisible(true);
                navigationView.getMenu().findItem(R.id.feedback).setVisible(true);
                navigationView.getMenu().findItem(R.id.logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.phone).setVisible(true);
                navigationView.getMenu().findItem(R.id.whatsapp).setVisible(true);
                navigationView.getMenu().findItem(R.id.profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.synchronization).setVisible(true);
            } else if (access.equals("ADMIN")) {
                //navigationView.getMenu().findItem(R.id.home_navigation).setVisible(true);
                //navigationView.getMenu().findItem(R.id.offlinewoe).setVisible(true);
                //  navigationView.getMenu().findItem(R.id.woe).setVisible(true);
                //navigationView.getMenu().findItem(R.id.result).setVisible(true);
                //navigationView.getMenu().findItem(R.id.trackdetails).setVisible(true);
                //navigationView.getMenu().findItem(R.id.ratecal).setVisible(true);
                //navigationView.getMenu().findItem(R.id.payment).setVisible(true);
                //navigationView.getMenu().findItem(R.id.ledger).setVisible(true);
                //navigationView.getMenu().findItem(R.id.billing).setVisible(true);
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
        String name = getProfileName.getString("name", null);
        String usercode = getProfileName.getString("usercode", null);
        String profile_image = getProfileName.getString("image", null);

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

        if (name != null) {
            navigationDrawerNameTSP.setText("HI " + name);
            ecode.setText("(" + usercode + ")");
        } else {
            getProfileDetails(ManagingTabsActivity.this);
        }


        Glide.with(ManagingTabsActivity.this)
                .load(profile_image)
                .placeholder(ManagingTabsActivity.this.getResources().getDrawable(R.drawable.userprofile))
                .into(imageViewprofile);
    }

    private void callnotifiedScreen(int SCRID) {
        if (SCRID == Constants.SCR_1) {
            Intent startIntent = new Intent(ManagingTabsActivity.this, Payment_Activity.class);
            startIntent.putExtra("COMEFROM", "ManagingTabsActivity");
            startActivity(startIntent);
        } else if (SCRID == Constants.SCR_2) {
            if (!CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                Intent i = new Intent(ManagingTabsActivity.this, Faq_activity.class);
                startActivity(i);
            }
        } else if (SCRID == Constants.SCR_3) {
            if (!CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                Intent i = new Intent(ManagingTabsActivity.this, VideoActivity.class);
                startActivity(i);
            }
        } else if (SCRID == Constants.SCR_4) {
            Bundle bundle = new Bundle();
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                bundle.putInt("position", 0);
            } else {
                if (access.equalsIgnoreCase(Constants.ADMIN)) {
                    bundle.putInt("position", 7);
                } else if (access.equalsIgnoreCase(Constants.STAFF)) {
                    bundle.putInt("position", 6);
                }
            }
            carouselFragment = new CarouselFragment();
            carouselFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, carouselFragment).commit();
        } else if (SCRID == Constants.SCR_5) {
            if (!CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 3);
                carouselFragment = new CarouselFragment();
                carouselFragment.setArguments(bundle);
                final FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, carouselFragment).commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottomNavigationView view item clicks here.
        int id = item.getItemId();
//        fab.setVisibility(View.VISIBLE);

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
//                i.putExtra("comefrom", "TSP");
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
                httpIntent.setData(Uri.parse("http://www.charbi.com/CDN/Applications/Android/Thyroshop.apk"));
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


        } else if (id == R.id.upload_document_navigation) {

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
                /*Noticeboard_Fragment noticeboard_fragment = new Noticeboard_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, noticeboard_fragment, noticeboard_fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/

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
//                    i.putExtra("comefrom", "TSP");
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
                Intent i = new Intent(ManagingTabsActivity.this, BMC_StockAvailabilityActivity.class);
                startActivity(i);
            }
        } else if (id == R.id.bs_entry) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Blood_sugar_entry_activity.class);
                startActivity(i);
            }

        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setMessage(ToastFile.surelogout)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TastyToast.makeText(getApplicationContext(), getResources().getString(R.string.Success), TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            // Toast.makeText(getApplicationContext(), "You have successfully logout", Toast.LENGTH_LONG).show();
                            distoye();
                            logout();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if (id == R.id.phone) {
            new AlertDialog.Builder(this)
                    .setMessage("Would you like to proceed with call?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                    .setNegativeButton("No", null)
                    .show();

        } else if (id == R.id.whatsapp) {
            new AlertDialog.Builder(this)
                    .setMessage("Would you like to proceed with whatsapp?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                    .setNegativeButton("No", null)
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
                Intent i = new Intent(ManagingTabsActivity.this, Faq_activity.class);
                startActivity(i);

                /*FAQ_Fragment faq_fragment = new FAQ_Fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, faq_fragment, faq_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
*/
            }

        } else if (id == R.id.accr_data) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, AccreditationActivity.class);
                startActivity(i);
            }

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

               /* Contact_list_fragment contact_list_fragment = new Contact_list_fragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_mainLayout, contact_list_fragment, contact_list_fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/

                Intent i = new Intent(ManagingTabsActivity.this, CompanyContact_activity.class);
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

        final ProgressDialog progressDialog = GlobalClass.ShowprogressDialog(context);

        Log.e(TAG, "Get my Profile ---->" + Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile",
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                GlobalClass.hideProgress(context, progressDialog);
                                Log.e(TAG, "onResponse: " + response);

                                Gson gson = new Gson();
                                ProfileDetailsResponseModel responseModel = gson.fromJson(String.valueOf(response), ProfileDetailsResponseModel.class);

                                if (responseModel != null) {
                                    Constants.preotp = responseModel.getPriOTP();
                                    //  Constants.preotp = "NO";
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
                                    saveProfileDetails.apply();

                                    SharedPreferences.Editor saveProfileData = getSharedPreferences("profilename", 0).edit();
                                    saveProfileData.putString("name", responseModel.getName());
                                    saveProfileData.putString("usercode", responseModel.getUser_code());
                                    saveProfileData.putString("mobile", responseModel.getMobile());
                                    saveProfileData.putString("image", responseModel.getTsp_image());
                                    saveProfileData.putString("email", responseModel.getEmail());
                                    saveProfileData.apply();

                                    Log.e(TAG, "onResponse: tsp name and code" + responseModel.getName() + " " + responseModel.getUser_code());
                                    if (responseModel.getName() != null && responseModel.getUser_code() != null) {
                                        navigationDrawerNameTSP.setText("HI " + responseModel.getName());
                                        ecode.setText("(" + responseModel.getUser_code() + ")");
//                                getTspNumber();
                                    } else {
                                        navigationDrawerNameTSP.setText("HI");
//                                getTspNumber();
                                    }

                                    Glide.with(context)
                                            .load(responseModel.getTsp_image())
                                            .placeholder(context.getResources().getDrawable(R.drawable.userprofile))
                                            .into(imageViewprofile);
                                } else {
                                    Toast.makeText(ManagingTabsActivity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ManagingTabsActivity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.hideProgress(context, progressDialog);
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "getProfileDetails: url" + jsonObjectRequest);
        GlobalClass.volleyRetryPolicy(jsonObjectRequest);
    }

    public void logout() {

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

//        prefsEditor.putString("myData", jsondata);

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

    /**
     * Only Activity has this special callback method
     * Fragment doesn't have any onBackPressed callback
     * <p>
     * Logic:
     * Each time when the back button is pressed, this Activity will propagate the call to the
     * container Fragment and that Fragment will propagate the call to its each tab Fragment
     * those Fragments will propagate this method call to their child Fragments and
     * eventually all the propagated calls will get back to this initial method
     * <p>
     * If the container Fragment or any of its Tab Fragments and/or Tab child Fragments couldn't
     * handle the onBackPressed propagated call then this Activity will handle the callback itself
     */

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
}
