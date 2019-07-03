package de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Create by Eric Werner
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    // ===========================================================
    // Constants
    // ===========================================================
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private final int mAnzahl = 4;
    private Context mContext;
    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods

    public MainPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

        }
        return null;
    }

    @Override
    public int getCount() {
        return mAnzahl;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

        }
        return null;
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
