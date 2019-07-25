package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.DataPagerAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.LockableViewPager;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.ZoomOutPageTransformer;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;

/*
 * Create by Alea Sauer
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstAidTipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAidTipsFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    // Model
    protected VehicleModel mVehicleModel;

    private LoadDataAsyncTask loadDataAsyncTask;
    private LockableViewPager mViewPager;
    private DataPagerAdapter mSectionsPagerAdapter;

    private LinearLayout vProgressContainer, vDotContainer;
    private int vMaxProgress;
    private TextView[] dots;

    int colorsActive;
    int colorsInactive;

    // ===========================================================
    // Constructors
    // ===========================================================
    public FirstAidTipsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    public static FirstAidTipsFragment newInstance() {
        FirstAidTipsFragment historyFragment = new FirstAidTipsFragment();
        return historyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.d("%s created", LOG_TAG);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_aid_tips, container, false);
        // get UI
        colorsActive = getResources().getColor(R.color.colorSecondary);
        colorsInactive = getResources().getColor(R.color.colorGrayC);
        vProgressContainer = view.findViewById(R.id.ll_dialog_ProgressContainer);
        vDotContainer = view.findViewById(R.id.ll_dialog_DotContainer);
        mViewPager = view.findViewById(R.id.frame_firstAidTips_viewPager);

        // set UI
        vMaxProgress = 7;
        initBottomDots();

        mSectionsPagerAdapter = new DataPagerAdapter(getChildFragmentManager());

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeBottomDots(position);
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });

        // load data
        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);


        return view;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
        loadDataAsyncTask.cancel(true);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Displays the current progress or changes the points according to the progress.
     *
     * @param currentPage
     */
    private void changeBottomDots(int currentPage) {
        if (vDotContainer == null || vProgressContainer == null) {
            return;
        }

        for (int i = 0; i < dots.length; i++) {
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setPaddingRelative(0, 0, 0, 0);
            dots[i].setTextSize(35);
            if (i == currentPage) {
                dots[i].setTextColor(colorsActive);
            } else {
                dots[i].setTextColor(colorsInactive);
            }
        }
    }

    /**
     * init Bottom Dots
     */
    private void initBottomDots() {
        if (vDotContainer == null || vProgressContainer == null) {
            return;
        }
        dots = new TextView[vMaxProgress];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setPaddingRelative(0, 0, 0, 0);
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            vDotContainer.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[0].setTextColor(colorsActive);
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Starts a background task, which all needed Data for Fragment
     */
    @SuppressLint("StaticFieldLeak")
    public class LoadDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            // Load and set Data
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_stayCalm, R.drawable.aid_tips_clear_road), "aid_tips_clear_road");
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_warning, R.drawable.aid_tips_warning), "aid_tips_warning");
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_help, R.drawable.aid_tips_help, true), "aid_tips_help");
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_call, R.string.msg_firstAidTips_help_2, R.drawable.aid_tips_call), "aid_tips_call");
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_dontLeave, R.drawable.aid_tips_stay), "aid_tips_sty");
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_note, R.drawable.aid_tips_note), "aid_tips_note");
            mSectionsPagerAdapter.addFragment(TipsFragment.newInstance(R.string.msg_firstAidTips_photo, R.drawable.aid_tips_photo), "aid_tips_photo");

            // Set up the ViewPager with the sections adapter.
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // set UI
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        @Override
        protected void onCancelled() {

        }
    }
}
