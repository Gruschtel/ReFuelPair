package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

import android.provider.BaseColumns;

/**
 * Create by Eric Werner
 * Edit by Alea Sauer: Values added and adjusted
 * <p>
 * Klasse zum verwalten von Bundles Keys
 */
public interface ConstBundle extends BaseColumns {
    //
    // 0x05XX --> x can be used variably
    // If new grouping, try then round up to tenth place
    //
    String BUNDLE_DIALOG_ITEM = "BUNDLE_DIALOG_ITEM";
    String BUNDLE_DIALOG_TITEL = "BUNDLE_DIALOG_TITEL";
    String BUNDLE_DIALOG_MESSAGE = "BUNDLE_DIALOG_MESSAGE";
    String BUNDLE_DIALOG_ACTION = "BUNDLE_DIALOG_ACTION";

    String BUNDLE_VEHICLE_SHARE = "BUNDLE_VEHICLE_SHARE";
    String BUNDLE_INSURANCE_SHARE = "BUNDLE_INSURANCE_SHARE";
    String BUNDLE_EXIT_INFORMATION = "BUNDLE_EXIT_INFORMATION";
    String Bundle_CAR_DATA = "Bundle_CAR_DATA";

    String BUNDLE_CRASH_OTHER_SHARE = "BUNDLE_CRASH_OTHER_SHARE";
    String BUNDLE_CRASH_DETAILS_SHARE = "BUNDLE_CRASH_DETAILS_SHARE";
    String BUNDLE_CRASH_PHOTO_SHARE = "BUNDLE_CRASH_PHOTO_SHARE";
}
