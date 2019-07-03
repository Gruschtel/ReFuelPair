package de.gruschtelapps.fh_maa_refuelpair.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */
public class BaseFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    protected VehicleModel mVehicleModel;


    // ===========================================================
    // Constructors
    // ===========================================================
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState, int layout) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container,savedInstanceState);
        View v = inflater.inflate(layout, container, false);

        // Load Data
        SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
        long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);

        //noinspection StatementWithEmptyBody
        if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
            Timber.d("load Car Data from id: %s", id);
            mVehicleModel = loadCarData(id);
        } else {
            // TODO: ERROR MESSAGE
        }
        return v;
    }
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    protected VehicleModel loadCarData(long id) {
        DBHelper dbHelper = new DBHelper(getContext());
        return dbHelper.getGet().selectCarById(id);
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
