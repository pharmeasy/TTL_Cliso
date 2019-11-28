package com.example.e5322.thyrosoft.Activity;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.e5322.thyrosoft.Fragment.BillingSummary;
import com.example.e5322.thyrosoft.Fragment.CHNfragment;
import com.example.e5322.thyrosoft.Fragment.FilterReport;
import com.example.e5322.thyrosoft.Fragment.LedgerFragment;
import com.example.e5322.thyrosoft.Fragment.NHFFragment;
import com.example.e5322.thyrosoft.Fragment.Offline_woe;
import com.example.e5322.thyrosoft.Fragment.PETCT_Frag;
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.Fragment.TrackDetails;
import com.example.e5322.thyrosoft.Fragment.Wind_up_fragment;
import com.example.e5322.thyrosoft.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {

            case 0:
                // First Fragment of First Tab
                result = new Start_New_Woe();
                break;
            case 1:
                // First Fragment of Second Tab
                result = new Offline_woe();
                break;
            case 2:
                // First Fragment of Third Tab
                result = new TrackDetails();
                break;
            case 3:
                // First Fragment of Third Tab
                result = new FilterReport();
                break;

            case 4:
                // First Fragment of Third Tab
                result = new LedgerFragment();
                break;
            case 5:
                // First Fragment of Third Tab
                result = new Wind_up_fragment();
                break;
            case 6:
                // First Fragment of Third Tab
                result = new CHNfragment();
                break;
            case 7:
                // First Fragment of Third Tab
                result = new BillingSummary();
                break;
            case 8:
                // First Fragment of Third Tab
                result = new RateCalculatorFragment();
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {

            case 0:
                return resources.getString(R.string.page_1);
            case 1:
                return resources.getString(R.string.page_2);
            case 2:
                return resources.getString(R.string.page_3);
            case 3:
                return resources.getString(R.string.page_4);
            case 4:
                return resources.getString(R.string.page_5);
            case 5:
                return resources.getString(R.string.page_6);
            case 6:
                return resources.getString(R.string.page_7);
            case 7:
                return resources.getString(R.string.page_8);
            case 8:
                return resources.getString(R.string.page_9);

            default:
                return null;
        }
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
