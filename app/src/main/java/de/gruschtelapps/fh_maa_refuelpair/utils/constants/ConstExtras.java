package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

import android.provider.BaseColumns;

/**
 * Create by Eric Werner
 * Edit by Alea Sauer: Values added and adjusted
 */
public interface ConstExtras extends BaseColumns {
    //
    // 0x00XX --> x can be used variably
    // If new grouping, try then round up to tenth place
    //

    // CarInformation or AddCar
    String EXTRA_KEY_ADD = "EXTRA_KEY_ADD";
    String EXTRA_OBJECT_ADD = "EXTRA_OBJECT_ADD";
    int EXTRA_NEW_CAR = 0x0000;
    int EXTRA_NEW_REFUEL = 0x0001;
    int EXTRA_NEW_SERVICE = 0x0002;
    int EXTRA_NEW_REMINDER = 0x0003;
    int EXTRA_NEW_CRASH = 0x0004;

    // SelectList
    String EXTRA_KEY_SELECT = "EXTRA_KEY_SELECT_LIST";
    int EXTRA_SELECT_CAR = 0x0010;
    int EXTRA_SELECT_SERVICE = 0x0011;
    int EXTRA_SELECT_MODEL = 0x0012;
    int EXTRA_SELECT_REFUEL = 0x0013;
    int EXTRA_SELECT_MANUFACTURE = 0x0014;
    int EXTRA_SELECT_OTHER_OWNER = 0x0015;
    int EXTRA_SELECT_OTHER_DRIVER = 0x0016;


    // CarInformation or AddCar
    String EXTRA_KEY_EDIT = "EXTRA_KEY_ADD";
    String EXTRA_OBJECT_EDIT = "EXTRA_OBJECT_ADD";
    int EXTRA_EDIT_CAR = 0x0020;
    int EXTRA_EDIT_REFUEL = 0x0021;
    int EXTRA_EDIT_SERVICE = 0x0022;
    int EXTRA_EDIT_REMINDER = 0x0023;
    int EXTRA_EDIT_CRASH = 0x0024;
    int EXTRA_EDIT_INSURANCE = 0x0025;


    //
    String EXTRA_KEY_SETTING = "EXTRA_KEY_SETTING";
    int EXTRA_FUEL = 0x0030;

}
