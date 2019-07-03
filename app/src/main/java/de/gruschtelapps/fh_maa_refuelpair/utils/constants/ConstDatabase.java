package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

import android.provider.BaseColumns;

/**
 * Create by Eric Werner
 * Edit by Alea Sauer: Values added and adjusted
 */
public interface ConstDatabase extends BaseColumns {
    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // DATABASE
    // -----------------------------------------------------------
    // -----------------------------------------------------------
    String _id = "_id";

    // TABLE CAR
    String TABLE_CAR = "TABLE_CAR";
    String COLUMN_CAR_NAME = "COLUMN_CAR_NAME";
    String COLUMN_CAR_INFORMATION = "COLUMN_CAR_INFORMATION";

    // TABLE ADD
    String TABLE_ADD = "TABLE_ADD";
    String COLUMN_ADD_TYPE = "COLUMN_ADD_TYPE";
    String COLUMN_ADD_DATE = "COLUMN_ADD_DATE";
    String COLUMN_ADD_INFORMATION = "COLUMN_ADD_INFORMATION";
    String COLUMN_ADD_CAR_ID = "COLUMN_ADD_CAR_ID";


    // -----------------------------------------------------------
    // -----------------------------------------------------------
    // SQL STATEMENT
    // -----------------------------------------------------------
    // -----------------------------------------------------------

    String createCarTable =
            "CREATE TABLE " + TABLE_CAR + " ("
                    + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CAR_NAME + " TEXT,"
                    + COLUMN_CAR_INFORMATION + " TEXT"
                    + ")";

    String createAddTable =
            "CREATE TABLE " + TABLE_ADD + " ("
                    + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ADD_TYPE + " INTEGER,"
                    + COLUMN_ADD_DATE + " INTEGER,"
                    + COLUMN_ADD_INFORMATION + " TEXT,"
                    + COLUMN_ADD_CAR_ID + " INTEGER"
                    + ")";

}
