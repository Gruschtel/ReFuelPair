package de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Eric Werner
 * Eigener PageAdapter zum anzeigen von mehrere Fragmenten
 */
public class DataPagerAdapter extends FragmentPagerAdapter {
    // ===========================================================
    // Constants
    // ===========================================================
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public DataPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
