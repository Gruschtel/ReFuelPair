package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

import android.provider.BaseColumns;

/**
 * Create by Eric Werner
 * <p>
 * Klasse zum verwalten von Preferences Keys
 */
public interface ConstPreferences extends BaseColumns {
    //
    // 0x02XX --> x can be used variably
    // If new grouping, try then round up to tenth place
    //
    // PREFS
    String PREFS = "PREFS";
    String PREF_FIRST_START = "PREF_FIRST_START";
    String PREF_CURRENT_CAR = "PREF_CURRENT_CARs";


    // Fuel Comparison
    String PREF_COMPARISON_DISTANC = "PREF_COMPARISON_DISTANC";
    String PREF_COMPARISON_SORT = "PREF_COMPARISON_SORT";
    String PREF_COMPARISON_TYPE = "PREF_COMPARISON_TYPE";
}
