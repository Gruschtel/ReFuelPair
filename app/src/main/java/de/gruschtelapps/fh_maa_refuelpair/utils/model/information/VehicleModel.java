package de.gruschtelapps.fh_maa_refuelpair.utils.model.information;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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
public class VehicleModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private CarTypeModel mType;
    private ManufactureModel mManfacture;
    private InsurancePolicyModel mInsurancePolicyModel;
    private FuelTypeModel mTankOne;
    private FuelTypeModel mTankTwo;

    private String mName = null;
    private String mModel = null;
    private String mChassingNumber = null;
    private String mLicensePlate = null;
    private String mPhoto = null;
    private boolean mTwoTanks = false;
    private long mOdometer;

    // ===========================================================
    // Constructors
    // ===========================================================
    public VehicleModel() {
        super(-1, "");
        mType = new CarTypeModel();
        mManfacture = new ManufactureModel();
        mTankOne = new FuelTypeModel();
        mTankTwo = new FuelTypeModel();
        mInsurancePolicyModel = new InsurancePolicyModel();
        mOdometer = 0;
        mName = "";
    }

    public VehicleModel(int id, String name) {
        super(id, name);
    }

    // Parcelable
    private VehicleModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mName = (String) in.readValue(String.class.getClassLoader());
        mType = in.readParcelable(CarTypeModel.class.getClassLoader());
        mManfacture = in.readParcelable(ManufactureModel.class.getClassLoader());
        mModel = in.readString();
        mChassingNumber = in.readString();
        mLicensePlate = in.readString();
        mTwoTanks = in.readByte() != 0;
        mTankOne = in.readParcelable(FuelTypeModel.class.getClassLoader());
        mTankTwo = in.readParcelable(FuelTypeModel.class.getClassLoader());
        mPhoto = in.readString();
        mInsurancePolicyModel = in.readParcelable(InsurancePolicyModel.class.getClassLoader());
        mOdometer = in.readLong();
        Timber.tag("VehicleModel").d("Parcel in\t\tid: %1$d\t\tmName: %2$s\t\tmType: %3$s\t\tmModel: %4$s\t\tLicense: %5$s", mId, mName, mType.getName(), mModel, mLicensePlate);
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public ArrayList<JsonModel> loadModels(Context context) {
        return null;
    }

    @Override
    public JsonModel loadModelById(Context context, long id) {
        return null;
    }

    @Override
    public String createJson() {
        String jsonString = "{\"" + MODEL_VEHICLE + "\":{" +
                "\"" + MODEL_ID + "\":\"" + mId + "\"," +
                "\"" + MODEL_NAME + "\":\"" + mName + "\"," +
                "\"" + MODEL_TYPE + "\":" + mType.createJson() + "," +
                "\"" + MODEL_MANUFACTURE + "\":" + mManfacture.createJson() + "," +
                "\"" + MODEL_MODEL + "\":\"" + mModel + "\"," +
                "\"" + MODEL_CHASSIS_NUMBER + "\":\"" + mChassingNumber + "\"," +
                "\"" + MODEL_LICENSE_PLATE + "\":\"" + mLicensePlate + "\"," +
                "\"" + MODEL_PHOTO + "\":\"" + mPhoto + "\"," +
                "\"" + MODEL_INSURANCE_POLICE + "\":" + mInsurancePolicyModel.createJson() + "," +
                "\"" + MODEL_TANKS + "\":\"" + (mTwoTanks ? 1 : 0) + "\"," +
                "\"" + MODEL_TANK_ONE + "\":" + mTankOne.createJson() + "," +
                "\"" + MODEL_ODOMETER + "\":" + mOdometer + "";
        if (mTwoTanks)
            jsonString += ",\"" + MODEL_TANK_TWO + "\":" + mTankTwo.createJson() + "";
        jsonString += "}}";
        return jsonString;
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        VehicleModel model = new VehicleModel();
        Timber.d(information);
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            //jsonObjectType = URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()));
            JSONObject jsonObject = jsonObjectType.getJSONObject("vehicle");
            model.setId(Integer.parseInt(URLDecoder.decode(jsonObject.getString(MODEL_ID), MODEL_CHARSET.name())));
            model.setName(URLDecoder.decode(jsonObject.getString(MODEL_NAME), MODEL_CHARSET.name()));

            model.setModel(URLDecoder.decode(jsonObject.getString(MODEL_MODEL), MODEL_CHARSET.name()));
            model.setChassingNumber(URLDecoder.decode(jsonObject.getString(MODEL_CHASSIS_NUMBER), MODEL_CHARSET.name()));
            model.setLicensePlate(URLDecoder.decode(jsonObject.getString(MODEL_LICENSE_PLATE), MODEL_CHARSET.name()));
            model.setPhoto(URLDecoder.decode(jsonObject.getString(MODEL_PHOTO), MODEL_CHARSET.name()));
            model.setTanks(URLDecoder.decode(jsonObject.getString(MODEL_TANKS), MODEL_CHARSET.name()).equals("1"));
            model.setOdometer(Integer.parseInt(URLDecoder.decode(jsonObject.getString(MODEL_ODOMETER), MODEL_CHARSET.name())));
            // Type
            CarTypeModel carTypeModel = (CarTypeModel) new CarTypeModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_TYPE), MODEL_CHARSET.name()));
            model.setType((CarTypeModel) carTypeModel.loadModelById(context, carTypeModel.getId()));
            model.setManfacture((ManufactureModel) new ManufactureModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_MANUFACTURE), MODEL_CHARSET.name())));
            model.setInsurancePolicyModel((InsurancePolicyModel) new InsurancePolicyModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_INSURANCE_POLICE), MODEL_CHARSET.name())));


            FuelTypeModel fuelTypeModel = (FuelTypeModel) new FuelTypeModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_TANK_ONE), MODEL_CHARSET.name()));
            model.setTankOne((FuelTypeModel) fuelTypeModel.loadModelById(context, fuelTypeModel.getId()));
            model.getTankOne().setCapacity(fuelTypeModel.getCapacity());

            if (model.isTanks()) {
                fuelTypeModel = (FuelTypeModel) new FuelTypeModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_TANK_TWO), MODEL_CHARSET.name()));
                model.setTankTwo((FuelTypeModel) fuelTypeModel.loadModelById(context, fuelTypeModel.getId()));
                model.getTankTwo().setCapacity(fuelTypeModel.getCapacity());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return model;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeValue(mName);
        dest.writeParcelable(mType, flags);
        dest.writeParcelable(mManfacture, flags);
        dest.writeString(mModel);
        dest.writeString(mChassingNumber);
        dest.writeString(mLicensePlate);
        dest.writeByte((byte) (mTwoTanks ? 1 : 0));
        dest.writeParcelable(mTankOne, flags);
        dest.writeParcelable(mTankTwo, flags);
        dest.writeString(mPhoto);
        dest.writeParcelable(mInsurancePolicyModel, flags);
        dest.writeLong(mOdometer);
        Timber.d("writeToParcel\tid: %1$d\t\tmName: %2$s\t\tmType: %3$s\t\tmModel: %4$s", mId, mName, mType.getName(), mModel);
    }

    public static final Creator<VehicleModel> CREATOR = new Creator<VehicleModel>() {
        @Override
        public VehicleModel createFromParcel(Parcel in) {
            return new VehicleModel(in);
        }

        @Override
        public VehicleModel[] newArray(int size) {
            return new VehicleModel[size];
        }
    };

    // ===========================================================
    // Methods
    // ===========================================================
    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public CarTypeModel getType() {
        return mType;
    }

    public void setType(CarTypeModel type) {
        this.mType = type;
    }

    public ManufactureModel getManfacture() {
        return mManfacture;
    }

    public void setManfacture(ManufactureModel manfacture) {
        this.mManfacture = manfacture;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getChassingNumber() {
        return mChassingNumber;
    }

    public void setChassingNumber(String chassingNumber) {
        this.mChassingNumber = chassingNumber;
    }

    public String getLicensePlate() {
        return mLicensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.mLicensePlate = licensePlate;
    }

    public boolean isTanks() {
        return mTwoTanks;
    }

    public void setTanks(boolean tanks) {
        this.mTwoTanks = tanks;
    }

    public FuelTypeModel getTankOne() {
        return mTankOne;
    }

    public void setTankOne(FuelTypeModel fuelTypeOne) {
        this.mTankOne = fuelTypeOne;
    }

    public double getTankCapacityOne() {
        return mTankOne.getCapacity();
    }

    public void setCapacityOne(double fuelCapacityOne) {
        this.mTankOne.setCapacity(fuelCapacityOne);
    }

    public FuelTypeModel getTankTwo() {
        return mTankTwo;
    }

    public void setTankTwo(FuelTypeModel fuelTypeTwo) {
        this.mTankTwo = fuelTypeTwo;
    }

    public double getTankCapacityTwo() {
        return mTankTwo.getCapacity();
    }

    public void setCapacityTwo(double fuelCapacityTwo) {
        this.mTankTwo.setCapacity(fuelCapacityTwo);
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        this.mPhoto = photo;
    }

    public InsurancePolicyModel getInsurancePolicyModel() {
        return mInsurancePolicyModel;
    }

    public void setInsurancePolicyModel(InsurancePolicyModel mInsurancePolicyModel) {
        this.mInsurancePolicyModel = mInsurancePolicyModel;
    }

    public long getOdometer() {
        return mOdometer;
    }

    public void setOdometer(long mOdometer) {
        this.mOdometer = mOdometer;
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
