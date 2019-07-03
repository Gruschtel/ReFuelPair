package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.base.BaseFragment;
import timber.log.Timber;
/*
 * Create by Alea Sauer
 */
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivacyStatementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyStatementFragment extends BaseFragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public PrivacyStatementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PrivacyStatementFragment.
     */
    public static PrivacyStatementFragment newInstance() {
        return new PrivacyStatementFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ...
        Timber.tag(LOG_TAG);
        Timber.d("%s created", LOG_TAG);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_privacy_statement, container, false);
        // set Title Actionbar
        return v;
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
