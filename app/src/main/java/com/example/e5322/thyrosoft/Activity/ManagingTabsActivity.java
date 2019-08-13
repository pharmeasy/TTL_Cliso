package com.example.e5322.thyrosoft.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.android.volley.DefaultRetryPolicy;
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
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Payment_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Sgc_Pgc_Entry_Activity;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.UploadDocument;
import com.example.e5322.thyrosoft.SpecialOffer.SpecialOffer_Activity;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagingTabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CarouselFragment carouselFragment;
    String restoredText;
    private String user;
    public static String getdate;
    private static String TAG = ManagingTabsActivity.class.getSimpleName();
    private GlobalClass globalClass;
    String prof, address, ac_code, email, mobile, pincode, user_code, name, dojresponse;
    String passwrd, access, api_key;
    SharedPreferences prefs;
    TextView navigationDrawerNameTSP, ecode;
    private static android.support.v4.app.FragmentManager fragmentManager;
    public SharedPreferences sharedpreferences;
    ImageView imageViewprofile, home;
    private String closing_bal;
    private String credit_lim;
    private String source_code;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseHelper db;
    private int offline_draft_counts;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ll);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setItemIconTintList(null);

        if (globalClass.checkForApi21()) {
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
// Add Shadow with default param
// or with custom param
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String passToAPI = sdf.format(d);
//new JSONTask().execute(Api.count + api_key + "" + "/" + user + "" + "/" + "" + passToAPI + "/getdashboard");
        //Change the color of navigation drawer

        db = new DatabaseHelper(ManagingTabsActivity.this);
        db.open();
        offline_draft_counts = db.getProfilesCount();
        db.close();


        if (access.equals("STAFF")) {
            //navigationView.getMenu().findItem(R.id.home_navigation).setVisible(true);
            //navigationView.getMenu().findItem(R.id.offlinewoe).setVisible(true);
//            navigationView.getMenu().findItem(R.id.woe).setVisible(true);
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
            navigationView.getMenu().findItem(R.id.accr_data);
            navigation.getMenu().findItem(R.id.articles_data);

        } else if (access.equals("ADMIN")) {
            //navigationView.getMenu().findItem(R.id.home_navigation).setVisible(true);
            //navigationView.getMenu().findItem(R.id.offlinewoe).setVisible(true);
//            navigationView.getMenu().findItem(R.id.woe).setVisible(true);
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
            navigationView.getMenu().findItem(R.id.accr_data);
            navigation.getMenu().findItem(R.id.articles_data);
        }

       /* navigationView.getMenu().findItem(R.id.home_navigation).setVisible(false);
        navigationView.getMenu().findItem(R.id.offlinewoe).setVisible(false);
        navigationView.getMenu().findItem(R.id.result).setVisible(false);
        navigationView.getMenu().findItem(R.id.trackdetails).setVisible(false);
        navigationView.getMenu().findItem(R.id.ratecal).setVisible(false);
        navigationView.getMenu().findItem(R.id.ledger).setVisible(false);
        navigationView.getMenu().findItem(R.id.billing).setVisible(false);
        */
        SharedPreferences getProfileName = getSharedPreferences("profilename", MODE_PRIVATE);
        String name = getProfileName.getString("name", null);
        String usercode = getProfileName.getString("usercode", null);
        String profile_image = getProfileName.getString("image", null);

        if (name != null) {
            navigationDrawerNameTSP.setText("HI " + name);
            ecode.setText("(" + usercode + ")");
        } else {
            getProfileDetails();
        }


        Glide.with(ManagingTabsActivity.this)
                .load(profile_image)
                .placeholder(ManagingTabsActivity.this.getResources().getDrawable(R.drawable.userprofile))
                .into(imageViewprofile);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        fab.setVisibility(View.VISIBLE);

        if (id == R.id.home_navigation) {
            try {

                getIntent();
                if (!popFragment()) {

                } else {
                }
                Intent intentstart = new Intent(ManagingTabsActivity.this,
                        ManagingTabsActivity.class);

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
               /* Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("http://www.charbi.com/dsa/mobile_online_payment.asp?usercode=" + "" + user));
                startActivity(httpIntent);*/
            }


        } else if (id == R.id.communication) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Communication_Activity.class);
                i.putExtra("comefrom", "TSP");
                startActivity(i);
            }

        } else
        /*if (id == R.id.vid_leggy) {

            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, LeggyVideo_Activity.class);
                startActivity(i);
            }

        } else*/
            if (id == R.id.thyroshop) {
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
                    i.putExtra("comefrom", "TSP");
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

            }

        /*else if (id == R.id.blood_s_entry) {
            if (!GlobalClass.isNetworkAvailable(ManagingTabsActivity.this)) {
                GlobalClass.showAlertDialog(ManagingTabsActivity.this);
            } else {
                Intent i = new Intent(ManagingTabsActivity.this, Blood_sugar_entry_activity.class);
                startActivity(i);
            }

        }*/

            else if (id == R.id.logout) {
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
                                intent.setData(Uri.parse("tel:" + restoredText));
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
                                httpIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+91" + restoredText + "#"));
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

        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.viewpager);


        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getSupportFragmentManager().popBackStackImmediate();

        }
        return isPop;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageViewprofile;

        public DownloadImageTask(ImageView bmImage) {
            this.imageViewprofile = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error image display", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            imageViewprofile.setImageBitmap(result);
        }
    }


    public void getProfileDetails() {
        RequestQueue queue = Volley.newRequestQueue(ManagingTabsActivity.this);

        Log.e(TAG, "Get my Profile ---->" + Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Api.SOURCEils + api_key + "/" + user + "/" + "getmyprofile",
                new Response.Listener<JSONObject>() {
                    public String tsp_img;

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(TAG, "onResponse: " + response);
                            prof = response.getString(Constants.tsp_image);
                            ac_code = response.getString(Constants.ac_code);
                            address = response.getString(Constants.address);
                            email = response.getString(Constants.email);
                            mobile = response.getString(Constants.mobile);
                            name = response.getString("name");
                            pincode = response.getString(Constants.pincode);
                            user_code = response.getString(Constants.user_code);
                            closing_bal = response.getString(Constants.closing_balance);
                            credit_lim = response.getString(Constants.credit_limit);
                            dojresponse = response.getString(Constants.doj);
                            source_code = response.getString(Constants.source_code);
                            tsp_img = response.getString(Constants.tsp_image);

                            SharedPreferences.Editor saveProfileDetails = getSharedPreferences("profile", 0).edit();
                            saveProfileDetails.putString("prof", prof);
                            saveProfileDetails.putString("ac_code", ac_code);
                            saveProfileDetails.putString("address", address);
                            saveProfileDetails.putString("email", email);
                            saveProfileDetails.putString("mobile", mobile);
                            saveProfileDetails.putString("name", name);
                            saveProfileDetails.putString("pincode", pincode);
                            saveProfileDetails.putString("user_code", user_code);
                            saveProfileDetails.putString("closing_balance", closing_bal);
                            saveProfileDetails.putString("credit_limit", credit_lim);
                            saveProfileDetails.putString("doj", dojresponse);
                            saveProfileDetails.putString("source_code", source_code);
                            saveProfileDetails.putString("tsp_image", tsp_img);
                            saveProfileDetails.putString("address", address);
                            saveProfileDetails.commit();

                            SharedPreferences.Editor saveProfileData = getSharedPreferences("profilename", 0).edit();
                            saveProfileData.putString("name", name);
                            saveProfileData.putString("usercode", user_code);
                            saveProfileData.putString("mobile", mobile);
                            saveProfileData.putString("image", prof);
                            saveProfileData.putString("email", email);

                            saveProfileData.commit();

                            Log.e(TAG, "onResponse: tsp name and code" + name + " " + user_code);
                            if (name != null && user_code != null) {
                                navigationDrawerNameTSP.setText("HI " + name);
                                ecode.setText("(" + user_code + ")");
//                                getTspNumber();
                            } else {
                                navigationDrawerNameTSP.setText("HI");
//                                getTspNumber();
                            }
                            if (tsp_img != null) {
                                new DownloadImageTask(imageViewprofile).execute(tsp_img);
                            } else {
                                Glide.with(ManagingTabsActivity.this)
                                        .load("")
                                        .placeholder(ManagingTabsActivity.this.getResources().getDrawable(R.drawable.userprofile))
                                        .into(imageViewprofile);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        queue.add(jsonObjectRequest);
        Log.e(TAG, "getProfileDetails: url" + jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /*public class JSONTask extends AsyncTask<String, String, String> {
        String RES_ID, URL, VERSION_NO, cancelled_count, chn_count, confirmed_count, draft_count, maxRRT, patient_count, pending_count, processdate, reported_count, response, sample_count, woedate;

        @Override
        protected void onPreExecute() {
            barProgressDialog = new ProgressDialog(ManagingTabsActivity.this);
            barProgressDialog.setTitle("Kindly wait ...");
            barProgressDialog.setMessage(ToastFile.processing_request);
            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            barProgressDialog.setProgress(0);
            barProgressDialog.setMax(20);
            barProgressDialog.show();
            barProgressDialog.setCanceledOnTouchOutside(false);
            barProgressDialog.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {



                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = null;
                date = new Date();
                String outputDateStr = outputFormat.format(date);

                java.net.URL url = new URL(Api.count + api_key + "" + "/" + user + "" + "/" + outputDateStr + "" + "/getdashboard");
                Log.e(TAG, "doInBackground: URL" + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                RES_ID = parentObject.getString("RES_ID");
                URL = parentObject.getString("URL");
                VERSION_NO = parentObject.getString("VERSION_NO");
                cancelled_count = parentObject.getString("cancelled_count");
                chn_count = parentObject.getString("chn_count");
                confirmed_count = parentObject.getString("confirmed_count");
                draft_count = parentObject.getString("draft_count");
                maxRRT = parentObject.getString("maxRRT");
                patient_count = parentObject.getString("patient_count");
                pending_count = parentObject.getString("pending_count");
                processdate = parentObject.getString("processdate");
                reported_count = parentObject.getString("reported_count");
                response = parentObject.getString("response");
                sample_count = parentObject.getString("sample_count");
                woedate = parentObject.getString("woedate");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(final String result) {
            super.onPostExecute(result);
            Log.e(TAG, "doInBackground: RESPONSE" + result);
            if (woe != null) {
                for (int i = 0; i < woe.size(); i++) {
                    if (woe.get(i).equals("null")) {
                        TastyToast.makeText(ManagingTabsActivity.this, "Unauthorized user", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        logout();
                    }
                }
            }


            if (barProgressDialog.isShowing()) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
            }

        }
    }
*/

    public void logout() {

        SharedPreferences.Editor getProfileName = getSharedPreferences("profilename", MODE_PRIVATE).edit();
        getProfileName.clear();
        getProfileName.commit();

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
                    j.putExtra("comefrom", "TSP");
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


        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }

}
