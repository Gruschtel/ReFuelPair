package de.gruschtelapps.fh_maa_refuelpair.utils.adapter.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;


/**
 * Create by Eric Werner
 * Loads a list of news stories by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class HistoryItemLoader extends AsyncTaskLoader<List<JsonModel>> {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Constructs a new {@link HistoryItemLoader}.
     *
     * @param context of the activity
     */
    public HistoryItemLoader(Context context) {
        super(context);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<JsonModel> loadInBackground() {
        SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
        DBHelper dbHelper = new DBHelper(getContext());
        long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);
        List<JsonModel> addModels = dbHelper.getGet().selectAllAdds(id);
        return addModels;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
