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
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.FuelTypeModel;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */
@SuppressWarnings("WeakerAccess")
public class RefuelModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private FuelTypeModel mFuelTypeModel;
    private Double mLiter, mLiterCost, mTotalCost;
    private long mDate;
    private long mOdometer;
    private int mIdType;
    private String mLocal;
    private long mId;
    private long mIdCar;


    // ===========================================================
    // Constructors
    // ===========================================================

    public RefuelModel() {

    }

    public RefuelModel(int idTyp, long carId, FuelTypeModel fuelType, long odometer, String local, double valueLiter, double valueLiterCost, double valueTotalCost, long timeInMillis) {
        super();
        this.mId = -1;
        this.mIdType = idTyp;
        this.mIdCar = carId;
        this.mFuelTypeModel = fuelType;
        this.mOdometer = odometer;
        this.mLocal = local;
        this.mLiter = valueLiter;
        this.mLiterCost = valueLiterCost;
        this.mTotalCost = valueTotalCost;
        this.mDate = timeInMillis;
    }

    public RefuelModel(long id, int idTyp, long carId, FuelTypeModel fuelType, long odometer, String local, double valueLiter, double valueLiterCost, double valueTotalCost, long timeInMillis) {
        super();
        this.mId = id;
        this.mIdType = idTyp;
        this.mIdCar = carId;
        this.mFuelTypeModel = fuelType;
        this.mOdometer = odometer;
        this.mLocal = local;
        this.mLiter = valueLiter;
        this.mLiterCost = valueLiterCost;
        this.mTotalCost = valueTotalCost;
        this.mDate = timeInMillis;
    }

    // Parcelable
    private RefuelModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mIdCar = in.readLong();
        mIdType = in.readInt();
        mOdometer = in.readLong();
        mLiter = in.readDouble();
        mLiterCost = in.readDouble();
        mTotalCost = in.readDouble();
        mDate = in.readLong();
        mLocal = in.readString();
        mFuelTypeModel = in.readParcelable(FuelTypeModel.class.getClassLoader());

    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mIdCar);
        dest.writeInt(mIdType);
        dest.writeLong(mOdometer);
        dest.writeDouble(mLiter);
        dest.writeDouble(mLiterCost);
        dest.writeDouble(mTotalCost);
        dest.writeLong(mDate);
        dest.writeString(mLocal);
        dest.writeParcelable(mFuelTypeModel, flags);
    }

    @Override
    public String createJson() {
        String jsonString = "{\"" + MODEL_REFUEL + "\":{" +
                "\"" + MODEL_ID + "\":" + mId + "," +
                "\"" + MODEL_ID_TYPE + "\":" + mIdType + "," +
                "\"" + MODEL_ID_CAR + "\":" + mIdCar + "," +
                "\"" + MODEL_FUEL_TYPE + "\":" + mFuelTypeModel.createJson() + "," +
                "\"" + MODEL_ODOMETER + "\":" + mOdometer + "," +
                "\"" + MODEL_LOCAL + "\":\"" + mLocal + "\"," +
                "\"" + MODEL_LITER + "\":" + mLiter + "," +
                "\"" + MODEL_TOTAL_COST + "\":" + mLiterCost + "," +
                "\"" + MODEL_LITER_TOTAL_COST + "\":" + mTotalCost + "," +
                "\"" + MODEL_DATE + "\":" + mDate + ""
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
        RefuelModel model = new RefuelModel();
        Timber.d(information);
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            //jsonObjectType = URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()));
            JSONObject jsonObject = jsonObjectType.getJSONObject(MODEL_REFUEL);

            model.setId(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID)), MODEL_CHARSET.name())));
            model.setIdType(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID_TYPE)), MODEL_CHARSET.name())));
            model.setCarId(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID_CAR)), MODEL_CHARSET.name())));

            model.setOdometer(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getLong(MODEL_ODOMETER)), MODEL_CHARSET.name())));
            model.setLiter(Double.parseDouble(URLDecoder.decode(String.valueOf(jsonObject.getDouble(MODEL_LITER)), MODEL_CHARSET.name())));
            model.setLiterCost(Double.parseDouble(URLDecoder.decode(String.valueOf(jsonObject.getDouble(MODEL_TOTAL_COST)), MODEL_CHARSET.name())));
            model.setTotalCost(Double.parseDouble(URLDecoder.decode(String.valueOf(jsonObject.getDouble(MODEL_LITER_TOTAL_COST)), MODEL_CHARSET.name())));

            model.setDate(Long.parseLong((URLDecoder.decode(String.valueOf(jsonObject.getLong(MODEL_DATE)), MODEL_CHARSET.name()))));
            model.setLocal(URLDecoder.decode(jsonObject.getString(MODEL_LOCAL), MODEL_CHARSET.name()));

            FuelTypeModel fuelTypeModel = (FuelTypeModel) new FuelTypeModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_FUEL_TYPE), MODEL_CHARSET.name()));
            model.setFuelTypeModel((FuelTypeModel) fuelTypeModel.loadModelById(context, fuelTypeModel.getId()));

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
                "mFuelTypeModel=" + mFuelTypeModel +
                ", mLiter=" + mLiter +
                ", mLiterCost=" + mLiterCost +
                ", mTotalCost=" + mTotalCost +
                ", mDate=" + mDate +
                ", mOdometer=" + mOdometer +
                ", mIdType=" + mIdType +
                ", mLocal='" + mLocal + '\'' +
                ", mId=" + mId +
                ", mIdCar=" + mIdCar +
                '}';
    }

    public FuelTypeModel getFuelTypeModel() {
        return mFuelTypeModel;
    }

    public void setFuelTypeModel(FuelTypeModel mFuelTypeModel) {
        this.mFuelTypeModel = mFuelTypeModel;
    }

    public Double getLiter() {
        return mLiter;
    }

    public void setLiter(Double mLiter) {
        this.mLiter = mLiter;
    }

    public Double getLiterCost() {
        return mLiterCost;
    }

    public void setLiterCost(Double mLiterCost) {
        this.mLiterCost = mLiterCost;
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

    public int getIdType() {
        return mIdType;
    }

    public void setIdType(int mIdType) {
        this.mIdType = mIdType;
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    // Parcelable
    public static final Creator<RefuelModel> CREATOR = new Creator<RefuelModel>() {
        @Override
        public RefuelModel createFromParcel(Parcel in) {
            return new RefuelModel(in);
        }

        @Override
        public RefuelModel[] newArray(int size) {
            return new RefuelModel[size];
        }
    };
}
