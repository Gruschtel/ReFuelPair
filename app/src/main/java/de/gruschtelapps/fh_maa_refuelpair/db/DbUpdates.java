package de.gruschtelapps.fh_maa_refuelpair.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */
public class DbUpdates {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final Object syncRoot = new Object();
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    private DBHelper dbHelper;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DbUpdates(DBHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    // Update New Car
    public void updateCarInformation(long mId, String json) {
        synchronized (syncRoot) {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues content = new ContentValues();
                content.put(ConstDatabase.COLUMN_CAR_INFORMATION, json);

                String where = ConstDatabase._id + "=?";
                String[] whereArg = new String[]{Long.toString(mId)};

                db.update(ConstDatabase.TABLE_CAR, content, where, whereArg);
                db.close();

                Timber.d(ConstDatabase.TABLE_CAR + " update, bei: " + mId + ": " + json);
            }
        }
    }


    public void updateAdd(long id, String json, long date) {
        synchronized (syncRoot) {
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues content = new ContentValues();
                content.put(ConstDatabase.COLUMN_ADD_INFORMATION, json);
                content.put(ConstDatabase.COLUMN_ADD_DATE, date);

                String where = ConstDatabase._id + "=?";
                String[] whereArg = new String[]{Long.toString(id)};

                db.update(ConstDatabase.TABLE_ADD, content, where, whereArg);
                db.close();

                Timber.d(ConstDatabase.TABLE_ADD + " update, bei: " + id + ": " + json);
            }
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
