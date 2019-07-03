package de.gruschtelapps.fh_maa_refuelpair.utils.model.add;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import timber.log.Timber;


/**
 * Create by Eric Werner
 */
public class ServiceModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private Double mTotalCost;
    private long mDate;
    private long mOdometer;
    private String mLocal;
    private long mId;
    private long mIdCar;
    private int mIdTyp;
    private String mService;


    // ===========================================================
    // Constructors
    // ===========================================================

    public ServiceModel() {

    }

    public ServiceModel(int idTyp, long carId, long odometer, String local, double valueTotalCost, long timeInMillis, String s) {
        super();
        this.mId = -1;
        this.mIdTyp = idTyp;
        this.mIdCar = carId;
        this.mOdometer = odometer;
        this.mLocal = local;
        this.mTotalCost = valueTotalCost;
        this.mDate = timeInMillis;
        this.mService = s;
    }

    public ServiceModel(long id, int idTyp, long carId, long odometer, String local, double valueTotalCost, long timeInMillis, String s) {
        super();
        this.mId = id;
        this.mIdTyp = idTyp;
        this.mIdCar = carId;
        this.mOdometer = odometer;
        this.mLocal = local;
        this.mTotalCost = valueTotalCost;
        this.mDate = timeInMillis;
        this.mService = s;
    }

    // Parcelable
    private ServiceModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mIdTyp = in.readInt();
        mIdCar = in.readLong();
        mOdometer = in.readLong();
        mTotalCost = in.readDouble();
        mDate = in.readLong();
        mLocal = in.readString();
        mService = in.readString();
    }



    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mIdTyp);
        dest.writeLong(mIdCar);
        dest.writeLong(mOdometer);
        dest.writeDouble(mTotalCost);
        dest.writeLong(mDate);
        dest.writeString(mLocal);
        dest.writeString(mService);
    }

    @Override
    public String createJson() {
        String jsonString = "{\"" + MODEL_SERVICE + "\":{" +
                "\"" + MODEL_ID + "\":" + mId + "," +
                "\"" + MODEL_ID_TYPE + "\":" + mIdTyp + "," +
                "\"" + MODEL_ID_CAR + "\":" + mIdCar + "," +
                "\"" + MODEL_ODOMETER + "\":" + mOdometer + "," +
                "\"" + MODEL_LOCAL + "\":\"" + mLocal + "\"," +
                "\"" + MODEL_TOTAL_COST + "\":" + mTotalCost + "," +
                "\"" + MODEL_DATE + "\":" + mDate + "," +
                "\"" + MODEL_SERVICE_INFO + "\":\"" + mService + "\""
                + "}}";
        Timber.d(jsonString);
        return jsonString;
    }

    @Override
    public ArrayList<JsonModel> loadModels(Context context) {
        return null;
    }

    @Override
    public JsonModel loadModelById(Context context, long id) {
        return null;
    }


    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        ServiceModel model = new ServiceModel();
        Timber.d(information);
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            //jsonObjectType = URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()));
            JSONObject jsonObject = jsonObjectType.getJSONObject(MODEL_SERVICE);

            model.setId(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID)), MODEL_CHARSET.name())));
            model.setIdType(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID_TYPE)), MODEL_CHARSET.name())));
            model.setCarId(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID_CAR)), MODEL_CHARSET.name())));

            model.setOdometer(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getLong(MODEL_ODOMETER)), MODEL_CHARSET.name())));
            model.setTotalCost(Double.parseDouble(URLDecoder.decode(String.valueOf(jsonObject.getDouble(MODEL_TOTAL_COST)), MODEL_CHARSET.name())));

            model.setDate(Long.parseLong((URLDecoder.decode(String.valueOf(jsonObject.getLong(MODEL_DATE)), MODEL_CHARSET.name()))));
            model.setLocal(URLDecoder.decode(jsonObject.getString(MODEL_LOCAL), MODEL_CHARSET.name()));
            model.setService(URLDecoder.decode(jsonObject.getString(MODEL_SERVICE_INFO), MODEL_CHARSET.name()));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return model;
    }

    // ===========================================================
    // Methods
    // ===========================================================


    @NotNull
    @Override
    public String toString() {
        return "RefuelModel{" +
                ", mTotalCost=" + mTotalCost +
                ", mDate=" + mDate +
                ", mOdometer=" + mOdometer +
                ", mLocal='" + mLocal + '\'' +
                ", mId=" + mId +
                ", mIdCar=" + mIdCar +
                '}';
    }


    public Double getTotalCost() {
        return mTotalCost;
    }

    public void setTotalCost(Double mTotalCost) {
        this.mTotalCost = mTotalCost;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long mDate) {
        this.mDate = mDate;
    }

    public long getOdometer() {
        return mOdometer;
    }

    public void setOdometer(long mOdometer) {
        this.mOdometer = mOdometer;
    }

    public String getLocal() {
        return mLocal;
    }

    public void setLocal(String mLocal) {
        this.mLocal = mLocal;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getCarId() {
        return mIdCar;
    }

    public void setCarId(long mCarId) {
        this.mIdCar = mCarId;
    }

    public int getIdType() {
        return mIdTyp;
    }

    public void setIdType(int mIdType) {
        this.mIdTyp = mIdType;
    }

    public String getService() {
        return mService;
    }

    public void setService(String mService) {
        this.mService = mService;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    // Parcelable
    public static final Creator<ServiceModel> CREATOR = new Creator<ServiceModel>() {
        @Override
        public ServiceModel createFromParcel(Parcel in) {
            return new ServiceModel(in);
        }

        @Override
        public ServiceModel[] newArray(int size) {
            return new ServiceModel[size];
        }
    };
}

