package de.gruschtelapps.fh_maa_refuelpair.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;

import static de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase._id;

/**
 * Create by Eric Werner
 */

public class DbGets {
    // ===========================================================
    // Fields
    // ===========================================================
    private static final Object syncRoot = new Object();
    // ===========================================================
    // Constants
    // ===========================================================
    public final String LOG_TAG = getClass().getSimpleName();
    private DBHelper dbHelper;
    private Context mContext;

    // ===========================================================
    // Constructors
    // ===========================================================

    DbGets(DBHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.mContext = context;
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

    /**
     * Gibt eine bestimmtes PointOfInterest mit allen Zusatzinformationen aus der Datenbank zurück
     *
     * @param id aa
     * @return aa
     */
    public VehicleModel selectCarById(long id) {
        VehicleModel vehicleModel = new VehicleModel();
        // http://www.mastertheboss.com/jboss-server/jboss-datasource/using-try-with-resources-to-close-database-connections
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            Cursor c = db.rawQuery(
                    "SELECT * FROM " + ConstDatabase.TABLE_CAR
                            + " WHERE " + _id + "=?",
                    new String[]{Long.toString(id)});
            c.moveToFirst();

            vehicleModel = curserToVehicleModel(c);
            Timber.tag("sadfadsf").d(vehicleModel.createJson());

            c.close();

            Objects.requireNonNull(vehicleModel).setId(id);


            Timber.d("selectStation ausgeführt");
        } catch (Exception e) {
            Timber.e("Fehler");
            e.getMessage();
        }
        return vehicleModel;
    }


    //
    // **********************
    // Add
    // **********************
    //

    /**
     * Gibt alle Adds zu einem bestimmten Auto zurück
     *
     * @param id
     * @return
     */
    public List<JsonModel> selectAllAdds(long id) {
        List<JsonModel> addModel = new ArrayList<>();
        // http://www.mastertheboss.com/jboss-server/jboss-datasource/using-try-with-resources-to-close-database-connections
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            Cursor c = db.rawQuery(
                    "SELECT * FROM " + ConstDatabase.TABLE_ADD
                            + " WHERE " + ConstDatabase.COLUMN_ADD_CAR_ID + "=?" +
                            " ORDER BY " + ConstDatabase.COLUMN_ADD_DATE + " DESC",
                    new String[]{Long.toString(id)});
            c.moveToFirst();


            while (!c.isAfterLast()) {
                int addType = c.getInt(c.getColumnIndex(ConstDatabase.COLUMN_ADD_TYPE));
                if (addType == JsonModel.ADD_TYPE_REFUEL)
                    addModel.add(curserToRefuelModel(c));
                if (addType == JsonModel.ADD_TYPE_SERVICE)
                    addModel.add(curserToServiceModel(c));
                if (addType == JsonModel.ADD_TYPE_CRASH)
                    addModel.add(curserToCrashModel(c));
                c.moveToNext();
            }
            c.close();

            Timber.d(addModel.toString());
            Timber.d("selectStation ausgeführt");
        } catch (Exception e) {
            Timber.e("Fehler");
            e.getMessage();
        }
        return addModel;
    }


    // ===========================================================
    // Curser
    // ===========================================================

    /**
     * Wandelt einen Curser in ein Vehicle Modell um
     *
     * @param cursor
     * @return
     */
    private VehicleModel curserToVehicleModel(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(_id));
        String name = cursor.getString(cursor.getColumnIndex(ConstDatabase.COLUMN_CAR_NAME));
        String information = cursor.getString(cursor.getColumnIndex(ConstDatabase.COLUMN_CAR_INFORMATION));
        return (VehicleModel) new VehicleModel().loadModelByJson(mContext, information);
    }

    /**
     * Wandelt einen Curser in ein Service Modell um
     *
     * @param cursor
     * @return
     */
    private ServiceModel curserToServiceModel(Cursor cursor) {
        String information = cursor.getString(cursor.getColumnIndex(ConstDatabase.COLUMN_ADD_INFORMATION));
        return (ServiceModel) new ServiceModel().loadModelByJson(mContext, information);
    }

    /**
     * Wandelt einen Curser in ein Refuel Modell um
     *
     * @param cursor
     * @return
     */
    private RefuelModel curserToRefuelModel(Cursor cursor) {
        String information = cursor.getString(cursor.getColumnIndex(ConstDatabase.COLUMN_ADD_INFORMATION));
        return (RefuelModel) new RefuelModel().loadModelByJson(mContext, information);
    }

    /**
     * Wandelt einen Curser in ein Crash Modell um
     *
     * @param cursor
     * @return
     */
    private CrashModel curserToCrashModel(Cursor cursor) {
        String information = cursor.getString(cursor.getColumnIndex(ConstDatabase.COLUMN_ADD_INFORMATION));
        return (CrashModel) new CrashModel().loadModelByJson(mContext, information);
    }
}
