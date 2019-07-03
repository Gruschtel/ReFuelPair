package de.gruschtelapps.fh_maa_refuelpair.utils.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */
public class SharedPreferencesManager {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();



    // ===========================================================
    // Fields
    // ===========================================================
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    // ===========================================================
    // Constructors
    // ===========================================================
    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(ConstPreferences.PREFS, 0);
        editor = prefs.edit();
    }

    // ===========================================================
    // Getter
    // ===========================================================

    public int getPrefInt(String st_search) {
        int intValue;
        intValue = prefs.getInt(st_search.replaceAll(" ", "").toLowerCase(), ConstError.ERROR_INT);
        Timber.d("getPrefInt:\t%1$s", intValue);
        return intValue;
    }

    public String getPrefString(String st_search) {
        String name;
        name = prefs.getString(String.valueOf(st_search).replaceAll(" ", "").toLowerCase(), ConstError.ERROR_STRING);
        Timber.d("getPrefString:	\t%1$s", name);
        return name;
    }

    public long getPrefLong(String st_search) {
        long longValue;
        longValue = prefs.getLong(st_search.replaceAll(" ", "").toLowerCase(), ConstError.ERROR_LONG);
        Timber.d("getPrefLong:\t%1$s", longValue);
        return longValue;
    }

    public Boolean getPrefBool(String st_search) {
        boolean boolValue;
        boolValue = prefs.getBoolean(st_search.replaceAll(" ", "").toLowerCase(), ConstError.ERROR_BOOLEAN);
        Timber.d("getPrefBool:\t%1$s", boolValue);
        return boolValue;
    }

    public float getPrefFloat(String st_search) {
        float floatValue;
        floatValue = prefs.getFloat(st_search.replaceAll(" ", "").toLowerCase(), ConstError.ERROR_FLOAT);
        Timber.d("getPrefBool:\t%1$f", floatValue);
        return floatValue;

    }

    // ===========================================================
    // Setter
    // ===========================================================

    public void setPrefInt(String st_search, int mValue) {
        Timber.d("setPrefInt: st_search:\t%1$s mValue:\t%2$s", st_search ,mValue);
        editor.putInt(st_search.replaceAll(" ", "").toLowerCase(), mValue);
        editor.commit();
    }

    public void setPreLong(String st_search, long mValue) {
        Timber.d("setPreLong: st_search:\t%1$s mValue:\t%2$s",st_search ,mValue);
        editor.putLong(st_search.replaceAll(" ", "").toLowerCase(), mValue);
        editor.commit();
    }

    public void setPrefString(String st_search, String mValue) {
        Timber.d("setPrefString: st_search:\t%1$s mValue:\t%2$s",st_search ,mValue);
        editor.putString(st_search.replaceAll(" ", "").toLowerCase(), mValue);
        editor.commit();
    }

    public void setPrefBool(String st_search, boolean mValue) {
        Timber.d("setPrefBool: st_search:\t%1$s mValue:\t%2$s",st_search ,mValue);
        editor.putBoolean(st_search.replaceAll(" ", "").toLowerCase(), mValue);
        editor.commit();
    }

    public void setPrefFloat(String st_search, float mValue) {
        Timber.d("setPrefBool: st_search:\t%1$s mValue:\t%2$s",st_search ,mValue);
        editor.putFloat(st_search.replaceAll(" ", "").toLowerCase(), mValue);
        editor.commit();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
