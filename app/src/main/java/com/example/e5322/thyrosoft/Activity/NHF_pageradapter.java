package com.example.e5322.thyrosoft.Activity;

import android.content.res.Resources;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.e5322.thyrosoft.Fragment.LedgerFragment;
import com.example.e5322.thyrosoft.Fragment.NHFFragment;
import com.example.e5322.thyrosoft.R;

public class NHF_pageradapter extends FragmentPagerAdapter {

    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public NHF_pageradapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {

            case 0:
                // First Fragment of First Tab
                result = new NHFFragment();
                break;
            case 1:
                // First Fragment of Second Tab
                result = new LedgerFragment();
                break;

            default:
                result = null;
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {

            case 0:
                return resources.getString(R.string.petct);
            case 1:
                return resources.getString(R.string.page_5);

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


