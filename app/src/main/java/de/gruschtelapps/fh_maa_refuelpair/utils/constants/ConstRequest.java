package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

import android.provider.BaseColumns;

/**
 * Create by Eric Werner
 * Edit by Alea Sauer: Values added and adjusted
 * <p>
 * Klasse zum verwalten von Request Keys
 */
public interface ConstRequest extends BaseColumns {
    //
    // 0x01XX --> x can be used variably
    // If new grouping, try then round up to tenth place
    //

    // SelectActivity
    int REQUEST_SELECT_CAR_TYP = 0x0100;
    int REQUEST_SELECT_MANUFACTURE = 0x0101;
    int REQUEST_SELECT_TANK_TYP_ONE = 0x0102;
    int REQUEST_SELECT_TANK_TYP_TWO = 0x0103;
    int REQUEST_SELECT_OTHER_OWNER = 0x0104;
    int REQUEST_SELECT_OTHER_DRIVER = 0x0105;

    // Image
    int REQUEST_IMAGE_CAMERA = 0x0110;
    int REQUEST_IMAGE_GALLERY = 0x0111;

    // Permission
    int REQUEST_PERMISSION_OPEN_CAMERA = 0x0120;
    int REQUEST_PERMISSION_OPEN_GALLERY = 0x0121;
    int REQUEST_PERMISSION_GPS = 0x0122;
    int REQUEST_PERMISSION_NETWORK = 0x0123;
    int REQUEST_PERMISSION_ACCESS_LOCATION = 0x0124;
    int REQUEST_PERMISSIN_FINE_LOCATION = 0x0125;


    // Dialoge
    int REQUEST_DIALOG_PHOTO = 0x0130;
    int REQUEST_DIALOG_BACK = 0x0131;
    int REQUEST_DIALOG_POSITIV = 0x0132;
    int REQUEST_DIALOG_NEGATIV = 0x0133;
    int REQUEST_DIALOG_GPS_OFF = 0x0134;
    int REQUEST_DIALOG_COMPARISON = 0x0135;
    int REQUEST_DIALOG_DELETE = 0x0136;
    int REQUEST_DIALOG_DATE = 0x0137;
    int REQUEST_DIALOG_TIME = 0x0138;


    int REQUEST_MAIN_NEW_CAR = 0x0140;
    int REQUEST_MAIN_NEW_HISTORY = 0x0141;
    int REQUEST_MAIN_EDIT_HISTORY = 0x0142;
    int REQUEST_MAIN_EDIT_CAR = 0x0143;


    int REQUEST_FUEL_COMPARISON_SETTINGS = 0x0150;
    int REQUEST_FUEL_COMPARISON_ENABLE_GPS = 0x0151;
    int REQUEST_FUEL_COMPARISON_PERMISSION_GPS = 0x0152;


    //
    int REQUEST_EXIT_APP = 0x0150;


}
