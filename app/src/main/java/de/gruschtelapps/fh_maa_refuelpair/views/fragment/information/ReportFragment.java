package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.DataPagerAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.LockableViewPager;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.ZoomOutPageTransformer;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.charts.BarChartFrag;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.charts.PieChartFrag;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    protected VehicleModel mVehicleModel;
    private LockableViewPager mViewPager;
    private DataPagerAdapter mSectionsPagerAdapter;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * @return A new instance of fragment ReportFragment.
     */
    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("%s created", LOG_TAG);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        // Get UI
        mSectionsPagerAdapter = new DataPagerAdapter(getChildFragmentManager());

        mViewPager = view.findViewById(R.id.frame_report_viewPager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPagingEnabled(false);

        TabLayout tabLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.tabs);

        // set UI
        mSectionsPagerAdapter.addFragment(BarChartFrag.newInstance(), getResources().getString(R.string.title_Bar));
        mSectionsPagerAdapter.addFragment(PieChartFrag.newInstance(), getResources().getString(R.string.title_pie));

        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
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

    // ===========================================================
    // Methods
    // ===========================================================


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
