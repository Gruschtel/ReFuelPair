package de.gruschtelapps.fh_maa_refuelpair.utils.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import timber.log.Timber;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Create by Eric Werner
 */
public class JsonModel implements Parcelable {
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    public static final Creator<JsonModel> CREATOR = new Creator<JsonModel>() {
        @Override
        public JsonModel createFromParcel(Parcel in) {
            return new JsonModel(in);
        }

        @Override
        public JsonModel[] newArray(int size) {
            return new JsonModel[size];
        }
    };
    // ===========================================================
    // Constants
    // ===========================================================
    // init
    public static final Charset MODEL_CHARSET = UTF_8;
    protected final String MODEL_LANGUAGE = "language";
    protected final String MODEL_VEHICLE = "vehicle";
    protected final String MODEL_SERVICE= "MODEL_SERVICE";
    protected final String MODEL_REFUEL = "MODEL_REFUEL";
    protected final String MODEL_CRASH = "MODEL_CRASH";
    protected final String MODEL_KONTAKTE_OWNER = "MODEL_KONTAKTE_OWNER";
    protected final String MODEL_KONTAKTE_DRIVBER = "MODEL_KONTAKTE_DRIVER";
    protected final String MODEL_KONTAKT = "MODEL_KONTAKT";
    protected final String MODEL_SERVICE_INFO = "MODEL_SERVICE_INFO";

    //
    protected final String MODEL_TYPE = "type";
    protected final String MODEL_MANUFACTURE = "manufacture";
    protected final String MODEL_MODEL = "model";
    protected final String MODEL_CHASSIS_NUMBER = "chassisNumber";
    protected final String MODEL_LICENSE_PLATE = "licensePlate";
    protected final String MODEL_PHOTO = "photo";
    protected final String MODEL_PHOTO_ID = "MODEL_PHOTO_ID";
    protected final String MODEL_INSURANCE_POLICE = "insurancePolicy";
    protected final String MODEL_TANKS = "tanks";
    protected final String MODEL_TANK_ONE = "tankOne";
    protected final String MODEL_TANK_TWO = "tankTwo";
    protected final String MODEL_ODOMETER = "odometer";

    protected final String MODEL_ID = "id";
    protected final String MODEL_NAME = "name";
    protected final String MODEL_IMAGE = "image";

    // Fuel Typ
    protected final String MODEL_CAPACITY = "capacity";

    // Insurance Policy & Kontakt
    protected final String MODEL_SURENAME = "surename";
    protected final String MODEL_PHONE = "telephone";
    protected final String MODEL_EMAIL = "email";
    protected final String MODEL_ADRESS = "adress";
    protected final String MODEL_INSURANCE_POLICY_NAME = "insurancePolicyName";
    protected final String MODEL_INSURANCE_POLICY_NUMBER = "insurancePolicyNumber";
    // Kontak
    protected final String MODEL_CRASH_DESCRIPTION = "MODEL_CRASH_DESCRIPTION";
    protected final String MODEL_CRASH_OTHER_DAMAGE = "MODEL_CRASH_OTHER_DAMAGE";
    protected final String MODEL_CRASH_OWN_DAMAGE = "MODEL_CRASH_OWN_DAMAGE";

    // ADD
    protected final String MODEL_ID_TYPE = "MODEL_ID_TYPE";
    protected final String MODEL_ID_CAR = "MODEL_ID_CAR";
    protected final String MODEL_FUEL_TYPE = "MODEL_FUEL_TYPE";
    protected final String MODEL_LOCAL = "MODEL_LOCAL";
    protected final String MODEL_LITER = "MODEL_LITER";
    protected final String MODEL_TOTAL_COST = "MODEL_TOTAL_COST";
    protected final String MODEL_LITER_TOTAL_COST = "MODEL_LITER_TOTAL_COST";
    protected final String MODEL_DATE = "MODEL_DATE";


    public static final int ADD_TYPE_REFUEL = 1;
    public static final int ADD_TYPE_SERVICE = 2;
    public static final int ADD_TYPE_CRASH = 3;

    // Crash


    // Files
    protected final String FILE_CAR_TYP = "car_type.json";
    protected final String FILE_MANUFACTURE = "manufacture.json";
    protected final String FILE_FUEL_TYPE = "fuel_type.json";

    // ===========================================================
    // Fields
    // ===========================================================
    protected long mId;
    protected String mName;
    // ===========================================================
    // Constructors
    // ===========================================================
    public JsonModel() {
    }

    protected JsonModel(long id, String name) {
        this.mId = id;
        this.mName = name;
    }
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    // https://stackoverflow.com/questions/5905105/how-to-serialize-null-value-when-using-parcelable-interface


    protected JsonModel(Parcel in) {}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    // ===========================================================
    // Methods
    // ===========================================================
    protected String loadJSONFromAsset(String path, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            json = new String(buffer, MODEL_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Gibt eine MODEL_ID einer Resource zurück, welche an einer bestimmten Position der eingelesenen Referenzen befindet
     *
     * @param drawableName; Drawable nach der gesucht werden soll
     * @return id einer Ressource der Klasse R
     */
    public int getReferenceId(Context context, String drawableName) {
        try{
            return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        } catch (Exception e){
            Timber.d(e);
        }
        return 0;
    }
    /**
     * Gibt eine MODEL_ID einer Resource zurück, welche an einer bestimmten Position der eingelesenen Referenzen befindet
     *
     * @param drawableName; Drawable nach der gesucht werden soll
     * @return id einer Ressource der Klasse R
     */
    public Drawable getDrawable(Context context, String drawableName) {
        return (context.getResources().getDrawable(context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName()), null));
    }

    // ===========================================================
    // Interfaces
    // ===========================================================
    public interface ListModelInterface {
        ArrayList<JsonModel> loadModels(Context context);

        JsonModel loadModelById(Context context, long id);

        String createJson();

        JsonModel loadModelByJson(Context context, String information);
    }
}

