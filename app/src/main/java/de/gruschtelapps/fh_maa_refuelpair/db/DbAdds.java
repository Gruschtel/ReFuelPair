package de.gruschtelapps.fh_maa_refuelpair.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */
public class DbAdds {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();
    private static final Object syncRoot = new Object();

    // ===========================================================
    // Fields
    // ===========================================================
    private DBHelper dbHelper;
    private Context mContext;

    // ===========================================================
    // Constructors
    // ===========================================================
    DbAdds(DBHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.mContext = context;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * New Object for Car Table
     * Car Information
     *
     * @param name
     * @param vehicleModel
     * @return
     */
    public long insertCar(String name, VehicleModel vehicleModel) {
        synchronized (syncRoot) {
            long id;
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues content = new ContentValues();

                content.put(ConstDatabase.COLUMN_CAR_NAME, name);
                content.put(ConstDatabase.COLUMN_CAR_INFORMATION, "");
                // erst wird ein dummy erzeugt um die Id zu erhalten
                id = db.insert(ConstDatabase.TABLE_CAR, null, content);
                // id wird dem modell hinzugefügt
                vehicleModel.setId(id);
                // Fahrzeug wird abgespeichert
                dbHelper.getUpdates().updateCarInformation(id, vehicleModel.createJson());


                Timber.d(mContext.getResources().getString(R.string.db_insert) + "\t\tid: " + id);
            } catch (Exception e) {
                e.getMessage();
                return ConstError.ERROR_LONG;
            }
            return id;
        }
    }

    /**
     * New Object for Add Table
     * Safe ne Add*
     *
     * @param addModel
     * @return
     */
    public long insertAddModel(JsonModel addModel) {
        synchronized (syncRoot) {
            long id = 0;
            try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues content = new ContentValues();
                if (addModel instanceof RefuelModel) {
                    content.put(ConstDatabase.COLUMN_ADD_TYPE, JsonModel.ADD_TYPE_REFUEL);
                    content.put(ConstDatabase.COLUMN_ADD_DATE, ((RefuelModel) addModel).getDate());
                    content.put(ConstDatabase.COLUMN_ADD_CAR_ID, ((RefuelModel) addModel).getCarId());
                    // erst wird ein dummy erzeugt um die Id zu erhalten
                    id = db.insert(ConstDatabase.TABLE_ADD, null, content);
                    // id wird dem modell hinzugefügt
                    ((RefuelModel) addModel).setId(id);
                    // Model wird abgespeichert
                    dbHelper.getUpdates().updateAdd(id, ((RefuelModel) addModel).createJson(), ((RefuelModel) addModel).getDate());
                    // Update Car
                    VehicleModel vehicleModel = dbHelper.getGet().selectCarById(((RefuelModel) addModel).getCarId());
                    if (vehicleModel.getOdometer() < ((RefuelModel) addModel).getOdometer())
                        vehicleModel.setOdometer(((RefuelModel) addModel).getOdometer());
                    dbHelper.getUpdates().updateCarInformation(((RefuelModel) addModel).getCarId(), vehicleModel.createJson());
                    return id;
                }
                if (addModel instanceof ServiceModel) {
                    content.put(ConstDatabase.COLUMN_ADD_TYPE, JsonModel.ADD_TYPE_SERVICE);
                    content.put(ConstDatabase.COLUMN_ADD_DATE, ((ServiceModel) addModel).getDate());
                    content.put(ConstDatabase.COLUMN_ADD_CAR_ID, ((ServiceModel) addModel).getCarId());
                    id = db.insert(ConstDatabase.TABLE_ADD, null, content);
                    ((ServiceModel) addModel).setId(id);
                    dbHelper.getUpdates().updateAdd(id, ((ServiceModel) addModel).createJson(), ((ServiceModel) addModel).getDate());
                    return id;
                }
                if (addModel instanceof CrashModel) {
                    content.put(ConstDatabase.COLUMN_ADD_TYPE, JsonModel.ADD_TYPE_CRASH);
                    content.put(ConstDatabase.COLUMN_ADD_DATE, ((CrashModel) addModel).getDate());
                    content.put(ConstDatabase.COLUMN_ADD_CAR_ID, ((CrashModel) addModel).getCarID());
                    id = db.insert(ConstDatabase.TABLE_ADD, null, content);
                    ((CrashModel) addModel).setId(id);
                    dbHelper.getUpdates().updateAdd(id, ((CrashModel) addModel).createJson(), ((CrashModel) addModel).getDate());
                    return id;
                }
                Timber.d(mContext.getResources().getString(R.string.db_insert));
            } catch (Exception e) {
                e.getMessage();
                return ConstError.ERROR_LONG;
            }
            return id;
        }
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
