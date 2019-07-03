package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

import android.provider.BaseColumns;

/**
 * Create by Eric Werner,
 * Edit by Alea Sauer: Values added and adjusted
 */
public interface ConstAction extends BaseColumns {
    //
    // 0x03XX --> x can be used variably
    // If new grouping, try then round up to tenth place
    //

    // ADD
    String ACTION_ADD_NEXT_PAGE = "de.gruschtelapps.fh_maa_refuelpair.ACTION_ADD_NEXT_PAGE";
    String ACTION_ADD_UPDATE_VEHICLE = "de.gruschtelapps.fh_maa_refuelpair.ACTION_ADD_UPDATE_VEHICLE";
    String ACTION_ADD_FINISH = "de.gruschtelapps.fh_maa_refuelpair.ACTION_ADD_FINISH";


    String ACTION_ADD_UPDATE_CRASH_OTHER = "de.gruschtelapps.fh_maa_refuelpair.ACTION_ADD_UPDATE_CRASH_OTHER";
    String ACTION_ADD_UPDATE_CRASH_DETAILS = "de.gruschtelapps.fh_maa_refuelpair.ACTION_ADD_UPDATE_CRASH_DETAILS";
    String ACTION_ADD_UPDATE_CRASH_PHOTOS = "de.gruschtelapps.fh_maa_refuelpair.ACTION_ADD_UPDATE_CRASH_PHOTOS";


    // EDIT
    String ACTION_EDIT_FINISH = "de.gruschtelapps.fh_maa_refuelpair.ACTION_EDIT_FINISH";
}
