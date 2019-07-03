package de.gruschtelapps.fh_maa_refuelpair.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import de.gruschtelapps.fh_maa_refuelpair.BuildConfig;
import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase;
import timber.log.Timber;

import static de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase.TABLE_ADD;
import static de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstDatabase.TABLE_CAR;

/**
 * Create by Eric Werner
 */
public class DBHelper extends SQLiteOpenHelper {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();
    private static final int DATABASE_VERSION = BuildConfig.DB_VERSION;
    private static final String DATABASE_NAME = BuildConfig.DB_PATH;

    // ===========================================================
    // Fields
    // ===========================================================
    private Context mContext;

    // Querys für die Datenbank
    private DbAdds mDBAdds;
    private DbGets mDBGets;
    private DbDelete mDBDelete;
    private DbUpdates mDBUpdates;

    // ===========================================================
    // Constructors
    // ===========================================================

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

        mDBAdds = new DbAdds(this, context);
        mDBUpdates = new DbUpdates(this, context);
        mDBGets = new DbGets(this, context);
        mDBDelete = new DbDelete(this, context);
    }
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstDatabase.createCarTable);
        Timber.d(mContext.getResources().getString(R.string.db_tabele_create), TABLE_CAR);

        db.execSQL(ConstDatabase.createAddTable);
        Timber.d(mContext.getResources().getString(R.string.db_tabele_create), TABLE_ADD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // ===========================================================
    // Methods
    // ===========================================================
    public DbAdds getAdd() {
        return mDBAdds;
    }

    public DbUpdates getUpdates() {
        return mDBUpdates;
    }

    public DbGets getGet() {
        return mDBGets;
    }

    public DbDelete getDelete() {
        return mDBDelete;
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
