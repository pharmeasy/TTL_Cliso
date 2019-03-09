package com.example.e5322.thyrosoft.Activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.e5322.thyrosoft.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselFragment extends Fragment {

    /**
     * TabPagerIndicator
     * <p>
     * Please refer to ViewPagerIndicator library
     */
    protected TabLayout tabLayout;
    private static ManagingTabsActivity mContext;
    protected ViewPager pager;
    public static Object currentFragment;
    private ViewPagerAdapter adapter;
    String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private int positionInt = 0;

    public CarouselFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);
        mContext = (ManagingTabsActivity) getActivity();
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        pager = (ViewPager) rootView.findViewById(R.id.vp_pages);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
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

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());


        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), mContext.getResources().getColor(R.color.tabindicatorColor));

        try {
            if (getArguments() != null) {
                positionInt = getArguments().getInt("position");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pager != null && pager.getCurrentItem() != positionInt) {
            pager.setCurrentItem(positionInt);
        } else {
            pager.setCurrentItem(0);
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
            currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());
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

        // this Fragment couldn't handle the onBackPressed call
    }
}
