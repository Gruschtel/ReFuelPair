package de.gruschtelapps.fh_maa_refuelpair.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */

public class DbDelete {
    private static Object syncRoot = new Object();
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();
    // ===========================================================
    // Fields
    // ===========================================================
    private DBHelper dbHelper;

    // ===========================================================
    // Constructors
    // ===========================================================

    DbDelete(DBHelper dbHelper, Context context) {
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
    public void deleteItem(long mID) {
        synchronized (syncRoot) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                // "=?" verhindert SQL-Injection
                String where = ConstDatabase._id + "=?";
                String[] whereArg = new String[]{Long.toString(mID)};
                db.delete(ConstDatabase.TABLE_ADD, where, whereArg);
                Timber.d("Item mit der ID: " + mID + " wurde gelöscht");
            } catch (Exception e) {
                e.getMessage();
            } finally {
                db.close();
            }
        }
    }

    public void deleteItem(long mID, long vehicleID, String json) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // "=?" verhindert SQL-Injection
            String where = ConstDatabase._id + "=?";
            String[] whereArg = new String[]{Long.toString(mID)};
            db.delete(ConstDatabase.TABLE_ADD, where, whereArg);
            Timber.d("Item mit der ID: " + mID + " wurde gelöscht");


            dbHelper.getUpdates().updateCarInformation(vehicleID, json);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            db.close();
        }
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
