package de.gruschtelapps.fh_maa_refuelpair.utils.model.add;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.ManufactureModel;
import timber.log.Timber;

/**
 * Create by Alea Sauer
 */
public class CrashModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private KontaktModel mOwner, mDriver;
    private long mId, mDate;
    private ManufactureModel mManufacture;
    private String mInsurancePoliceName, mInsurancePoliceNumber, mModel, mLocal, mDescription, mOtherDamage, mOwnDamage;
    private List<String> mPhotos;
    private long mIdCar;
    private long mIdTyp;


    // ===========================================================
    // Constructors
    // ===========================================================
    public CrashModel() {
        super(-1, "");
        mPhotos = new ArrayList();
        mManufacture = new ManufactureModel();
        mOwner = new KontaktModel();
        mDriver = new KontaktModel();

        mInsurancePoliceNumber = "";
        mInsurancePoliceName = "";
        mModel = "";
        mLocal = "";
        mDescription = "";
        mOtherDamage = "";
        mOwnDamage = "";
        mIdTyp = ADD_TYPE_CRASH;
        mIdCar = ConstError.ERROR_INT;
        mDate = ConstError.ERROR_LONG;
    }


    // Parcelable
    public CrashModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mIdCar = in.readLong();
        mIdTyp = in.readLong();
        mDate = in.readLong();
        mInsurancePoliceName = in.readString();
        mInsurancePoliceNumber = in.readString();
        mModel = in.readString();
        mLocal = in.readString();
        mDescription = in.readString();
        mOtherDamage = in.readString();
        mOwnDamage = in.readString();
        mOwner = in.readParcelable(KontaktModel.class.getClassLoader());
        mDriver = in.readParcelable(KontaktModel.class.getClassLoader());
        mManufacture = in.readParcelable(ManufactureModel.class.getClassLoader());
        mPhotos = in.readArrayList(String.class.getClassLoader());
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mIdCar);
        dest.writeLong(mIdTyp);
        dest.writeLong(mDate);
        dest.writeString(mInsurancePoliceName);
        dest.writeString(mInsurancePoliceNumber);
        dest.writeString(mModel);
        dest.writeString(mLocal);
        dest.writeString(mDescription);
        dest.writeString(mOtherDamage);
        dest.writeString(mOwnDamage);
        dest.writeParcelable(mOwner, flags);
        dest.writeParcelable(mDriver, flags);
        dest.writeParcelable(mManufacture, flags);
        dest.writeList(mPhotos);
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
    public String createJson() {
        StringBuilder jsonString = new StringBuilder("{\"" + MODEL_CRASH + "\":{" +
                "\"" + MODEL_ID + "\":" + mId + "," +
                "\"" + MODEL_ID_TYPE + "\":" + mIdTyp + "," +
                "\"" + MODEL_ID_CAR + "\":" + mIdCar + "," +
                "\"" + MODEL_DATE + "\":" + mDate + "," +

                "\"" + MODEL_INSURANCE_POLICY_NAME + "\":\"" + mInsurancePoliceName + "\"," +
                "\"" + MODEL_INSURANCE_POLICY_NUMBER + "\":\"" + mInsurancePoliceNumber + "\"," +
                "\"" + MODEL_MODEL + "\":\"" + mModel + "\"," +
                "\"" + MODEL_LOCAL + "\":\"" + mLocal + "\"," +
                "\"" + MODEL_CRASH_DESCRIPTION + "\":\"" + mDescription + "\"," +
                "\"" + MODEL_CRASH_OTHER_DAMAGE + "\":\"" + mOtherDamage + "\"," +
                "\"" + MODEL_CRASH_OWN_DAMAGE + "\":\"" + mOwnDamage + "\"," +

                "\"" + MODEL_KONTAKTE_DRIVBER + "\":" + mDriver.createJson() + "," +
                "\"" + MODEL_KONTAKTE_OWNER + "\":" + mOwner.createJson() + "," +
                "\"" + MODEL_MANUFACTURE + "\":" + mManufacture.createJson() + "," +
                "\"" + MODEL_PHOTO + "\":[");
        for (int i = 0; i < mPhotos.size(); i++) {
            if (i == mPhotos.size() - 1) {
                jsonString.append("{\"").append(MODEL_PHOTO_ID).append("\":\"").append(mPhotos.get(i)).append("\"}");
            } else {
                jsonString.append("{\"").append(MODEL_PHOTO_ID).append("\":\"").append(mPhotos.get(i)).append("\"},");
            }
        }
        jsonString.append("]}}");
        Timber.d(jsonString.toString());
        return jsonString.toString();
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        CrashModel model = new CrashModel();
        Timber.d(information);
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            //jsonObjectType = URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()));
            JSONObject jsonObject = jsonObjectType.getJSONObject(MODEL_CRASH);

            model.setId(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID)), MODEL_CHARSET.name())));
            model.setIdTyp(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID_TYPE)), MODEL_CHARSET.name())));
            model.setIdCar(Integer.parseInt(URLDecoder.decode(String.valueOf(jsonObject.getInt(MODEL_ID_CAR)), MODEL_CHARSET.name())));
            model.setDate(Long.parseLong(URLDecoder.decode(String.valueOf(jsonObject.getLong(MODEL_DATE)), MODEL_CHARSET.name())));

            model.setInsurancePoliceName(URLDecoder.decode(jsonObject.getString(MODEL_INSURANCE_POLICY_NAME), MODEL_CHARSET.name()));
            model.setInsurancePoliceNumber(URLDecoder.decode(jsonObject.getString(MODEL_INSURANCE_POLICY_NUMBER), MODEL_CHARSET.name()));
            model.setModel(URLDecoder.decode(jsonObject.getString(MODEL_MODEL), MODEL_CHARSET.name()));
            model.setLocal(URLDecoder.decode(jsonObject.getString(MODEL_LOCAL), MODEL_CHARSET.name()));
            model.setDescription(URLDecoder.decode(jsonObject.getString(MODEL_CRASH_DESCRIPTION), MODEL_CHARSET.name()));
            model.setOtherDamage(URLDecoder.decode(jsonObject.getString(MODEL_CRASH_OTHER_DAMAGE), MODEL_CHARSET.name()));
            model.setOwnDamage(URLDecoder.decode(jsonObject.getString(MODEL_CRASH_OWN_DAMAGE), MODEL_CHARSET.name()));

            model.setOwner((KontaktModel) new KontaktModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_KONTAKTE_OWNER), MODEL_CHARSET.name())));
            model.setDriver((KontaktModel) new KontaktModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_KONTAKTE_DRIVBER), MODEL_CHARSET.name())));
            model.setManufacture((ManufactureModel) new ManufactureModel().loadModelByJson(context, URLDecoder.decode(jsonObject.getString(MODEL_MANUFACTURE), MODEL_CHARSET.name())));

            JSONArray jsonArrayPhoto = new JSONArray(URLDecoder.decode(jsonObject.getString(MODEL_PHOTO), MODEL_CHARSET.name()));
            ArrayList<String> photos = new ArrayList<>();

            for (int y = 0; y < jsonArrayPhoto.length(); y++) {
                JSONObject jsonObjectPhoto = (JSONObject) jsonArrayPhoto.get(y);
                photos.add(URLDecoder.decode(jsonObjectPhoto.getString(MODEL_PHOTO_ID), MODEL_CHARSET.name()));
            }
            model.setPhotos(photos);

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
    public void safeCrashOtherParty(CrashModel crashModel) {
        this.mOwner = crashModel.getOwner();
        this.mDriver = crashModel.getDriver();
        this.mManufacture = crashModel.getManufacture();
        this.mModel = crashModel.getModel();
        this.mInsurancePoliceName = crashModel.getInsurancePoliceName();
        this.mInsurancePoliceNumber = crashModel.getInsurancePoliceNumber();
    }

    public void safeCrashDetails(CrashModel crashModel) {
        this.mDate = crashModel.getDate();
        this.mDescription = crashModel.getDescription();
        this.mOwnDamage = crashModel.getOwnDamage();
        this.mOtherDamage = crashModel.getOtherDamage();
        this.mLocal = crashModel.getLocal();
    }

    public void safeCrashPhotos(CrashModel crashModel) {
        this.mPhotos = crashModel.getPhotos();
    }

    public KontaktModel getOwner() {
        return mOwner;
    }

    public void setOwner(KontaktModel mOwner) {
        this.mOwner = mOwner;
    }

    public KontaktModel getDriver() {
        return mDriver;
    }

    public void setDriver(KontaktModel mDriver) {
        this.mDriver = mDriver;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public String getInsurancePoliceName() {
        return mInsurancePoliceName;
    }

    public void setInsurancePoliceName(String mInsurancePoliceName) {
        this.mInsurancePoliceName = mInsurancePoliceName;
    }

    public String getInsurancePoliceNumber() {
        return mInsurancePoliceNumber;
    }

    public void setInsurancePoliceNumber(String mInsurancePoliceNumber) {
        this.mInsurancePoliceNumber = mInsurancePoliceNumber;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String mModel) {
        this.mModel = mModel;
    }

    public ManufactureModel getManufacture() {
        return mManufacture;
    }

    public void setManufacture(ManufactureModel mManufacture) {
        this.mManufacture = mManufacture;
    }

    public String getLocal() {
        return mLocal;
    }

    public void setLocal(String mLocal) {
        this.mLocal = mLocal;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getOtherDamage() {
        return mOtherDamage;
    }

    public void setOtherDamage(String mOtherDamage) {
        this.mOtherDamage = mOtherDamage;
    }

    public String getOwnDamage() {
        return mOwnDamage;
    }

    public void setOwnDamage(String mOwnDamage) {
        this.mOwnDamage = mOwnDamage;
    }

    public List<String> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<String> mPhotos) {
        this.mPhotos = mPhotos;
    }

    public long getCarID() {
        return mIdCar;
    }

    public void setIdCar(long mIdCar) {
        this.mIdCar = mIdCar;
    }

    public long getIdTyp() {
        return mIdTyp;
    }

    public void setIdTyp(long mIdTyp) {
        this.mIdTyp = mIdTyp;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    // Parcelable
    public static final Creator<CrashModel> CREATOR = new Creator<CrashModel>() {
        @Override
        public CrashModel createFromParcel(Parcel in) {
            return new CrashModel(in);
        }

        @Override
        public CrashModel[] newArray(int size) {
            return new CrashModel[size];
        }
    };
}
