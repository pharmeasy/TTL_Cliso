package com.example.e5322.thyrosoft.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Adapter.BMCViewPagerAdapter;
import com.example.e5322.thyrosoft.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselFragment extends Fragment {

    public static Object currentFragment;
    private static ManagingTabsActivity mContext;
    /**
     * TabPagerIndicator
     * <p>
     * Please refer to ViewPagerIndicator library
     */
    protected TabLayout tabLayout;
    protected ViewPager pager;
    String TAG = ManagingTabsActivity.class.getSimpleName();
    String user, passwrd, access, api_key, user_code, CLIENT_TYPE;
    private ViewPagerAdapter adapter;
    private StaffViewPagerAdapter adapterStafff;
    private BMCViewPagerAdapter bmcViewPagerAdapter;
    NHF_pageradapter nhf_pageradapter;
    private int positionInt = 0;
    private SharedPreferences prefs;

    public CarouselFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);
        mContext = (ManagingTabsActivity) getActivity();
        tabLayout = rootView.findViewById(R.id.tabs);
        pager = rootView.findViewById(R.id.vp_pages);

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        user_code = prefs.getString("USER_CODE", null);

        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            nhf_pageradapter = new NHF_pageradapter(getResources(), getChildFragmentManager());
            pager.setAdapter(nhf_pageradapter);
        } else {
            if (access.equalsIgnoreCase(Constants.STAFF)) {
                adapterStafff = new StaffViewPagerAdapter(getResources(), getChildFragmentManager());
                pager.setAdapter(adapterStafff);
            } else {
                adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
                pager.setAdapter(adapter);
            }
        }


        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), mContext.getResources().getColor(R.color.tabindicatorColor));

        //TODO if tab menu are present in navigatoion drawer then get Intent from managingtab activity and set the fragment
        try {
            if (getArguments() != null) {
                positionInt = getArguments().getInt("position");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Constants.tab_flag.equalsIgnoreCase("1")) {
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                pager.setCurrentItem(0);
            }
            Constants.tab_flag = "0";
        } else {
            Constants.tab_flag = "0";
            if (pager != null && pager.getCurrentItem() != positionInt) {
                pager.setCurrentItem(positionInt);
            } else {
                pager.setCurrentItem(0);
            }
        }
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = null;
        try {
            {
                if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                    currentFragment = (OnBackPressListener) nhf_pageradapter.getRegisteredFragment(pager.getCurrentItem());
                } else {
                    if (access.equalsIgnoreCase(Constants.STAFF)) {
                        currentFragment = (OnBackPressListener) adapterStafff.getRegisteredFragment(pager.getCurrentItem());
                    } else {
                        currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            boolean a = currentFragment.onBackPressed();
            if (!a) {
                if (pager != null && pager.getCurrentItem() != 0) {
                    pager.setCurrentItem(0);
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}