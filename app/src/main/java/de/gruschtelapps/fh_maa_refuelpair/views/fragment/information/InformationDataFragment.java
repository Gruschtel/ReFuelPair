package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.DataPagerAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.ZoomOutPageTransformer;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformationDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformationDataFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    private ViewPager mViewPager;
    private DataPagerAdapter mSectionsPagerAdapter;

    // ===========================================================
    // Constructors
    // ===========================================================
    public InformationDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FuelSettingsFragment.
     */
    public static InformationDataFragment newInstance() {
        InformationDataFragment informationDataFragment = new InformationDataFragment();
        return informationDataFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(LOG_TAG);
        Timber.d("%s created", LOG_TAG);
    }

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        // Get UI
        mSectionsPagerAdapter = new DataPagerAdapter(getChildFragmentManager());

        mViewPager = view.findViewById(R.id.frame_vehicleData_viewPager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        TabLayout tabLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.tabs);

        // set UI
        mSectionsPagerAdapter.addFragment(VehicleFragment.newInstance(), getResources().getString(R.string.title_vehicle));
        mSectionsPagerAdapter.addFragment(InsurancePolicyFragment.newInstance(), getResources().getString(R.string.title_insurancePolicy));
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
