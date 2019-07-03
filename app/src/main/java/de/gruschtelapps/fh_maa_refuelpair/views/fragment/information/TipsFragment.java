package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;

/*
 * Create by Alea Sauer
 */
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TipsFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();
    private static final String ARG_IMAGE = "ARG_IMAGE";
    private static final String ARG_TEXT = "ARG_TEXT";
    private static final String ARG_TEXT_2 = "ARG_TEXT_2";
    private static final String ARG_TEXT_3 = "ARG_TEXT_3";

    // ===========================================================
    // Fields
    // ===========================================================
    // Model
    private VehicleModel mVehicleModel;
    private int mImage, mText, mTextHelper;
    // ===========================================================
    // Constructors
    // ===========================================================
    public TipsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    public static TipsFragment newInstance(int text, int image) {
        TipsFragment historyFragment = new TipsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putInt(ARG_TEXT, text);
        args.putInt(ARG_TEXT_2, ConstError.ERROR_INT);
        args.putInt(ARG_TEXT_3, ConstError.ERROR_INT);
        historyFragment.setArguments(args);
        return historyFragment;
    }

    public static TipsFragment newInstance(int text, int text2, int image) {
        TipsFragment historyFragment = new TipsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putInt(ARG_TEXT, text);
        args.putInt(ARG_TEXT_2, text2);
        args.putInt(ARG_TEXT_3, ConstError.ERROR_INT);
        historyFragment.setArguments(args);
        return historyFragment;
    }

    public static TipsFragment newInstance(int text, int image, boolean fistaidstips) {
        TipsFragment historyFragment = new TipsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putInt(ARG_TEXT, text);
        args.putInt(ARG_TEXT_2, ConstError.ERROR_INT);
        args.putInt(ARG_TEXT_3, fistaidstips?1:ConstError.ERROR_INT);
        historyFragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        if (getArguments() != null) {
            mImage = getArguments().getInt(ARG_IMAGE);
            mText = getArguments().getInt(ARG_TEXT);
            if (getArguments().getInt(ARG_TEXT_2) != ConstError.ERROR_INT){
                mTextHelper = getArguments().getInt(ARG_TEXT_2);
                view = inflater.inflate(R.layout.fragment_tips_2, container, false);
            }
            if (getArguments().getInt(ARG_TEXT_3) != ConstError.ERROR_INT){
                view = inflater.inflate(R.layout.fragment_tips_3, container, false);
            }
        }

        ((ImageView)view.findViewById(R.id.image_firstAidTips_icon)).setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(mImage));
        ((TextView) view.findViewById(R.id.text_firstAidTips_info)).setText(getContext().getResources().getString(mText));
        if (getArguments().getInt(ARG_TEXT_2) != ConstError.ERROR_INT){
            ((TextView) view.findViewById(R.id.text_firstAidTips_infohelper)).setText(getContext().getResources().getString(mTextHelper));
        }
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
