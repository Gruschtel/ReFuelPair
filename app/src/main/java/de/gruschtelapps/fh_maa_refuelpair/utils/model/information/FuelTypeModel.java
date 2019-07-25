package de.gruschtelapps.fh_maa_refuelpair.utils.model.information;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;

import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import timber.log.Timber;

/*
 * Create by Eric Werner
 */
public class FuelTypeModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private String mImageName;
    private double mCapacity;

    // ===========================================================
    // Constructors
    // ===========================================================
    public FuelTypeModel() {
        super(-1, "");
        mCapacity = 0;
    }

    private FuelTypeModel(long id, String name, String imageName) {
        super(id, name);
        this.mImageName = imageName;
        this.mCapacity = 0;
    }

    // Parcelable
    private FuelTypeModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mName = in.readString();
        mImageName = in.readString();
        mCapacity = in.readDouble();
    }

    private FuelTypeModel(long id, String name, String capacity, String imageName) {
        super(id, name);
        this.mImageName = imageName;
        this.mCapacity = Double.parseDouble(capacity);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FuelTypeModel) {
            if (((FuelTypeModel) obj).getId() == mId &&
                    ((FuelTypeModel) obj).getName().equals(mName) &&
                    ((FuelTypeModel) obj).getImageName().equals(mImageName) &&
                    ((FuelTypeModel) obj).getCapacity() == (mCapacity))
                return true;
        } else
            return false;
        return super.equals(obj);
    }

    @Override
    public ArrayList<JsonModel> loadModels(Context context) {
        ArrayList<JsonModel> list = new ArrayList<>();
        Locale searchlanguage = new Locale("en");
        Timber.tag("asdfasdgasdg").d(searchlanguage.toString());
        // Language test
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_FUEL_TYPE, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                if (Locale.getDefault().getLanguage().equals(new Locale(language).getLanguage())) {
                    searchlanguage = new Locale(language);
                    Timber.tag("asdfasdgasdg").d(searchlanguage.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_FUEL_TYPE, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                Timber.d(language);
                // Nicht gesuchte Sprache --> überspringen
                if (searchlanguage.getLanguage().equals(new Locale(language).getLanguage())) {
                    for (int j = 0; j < jsonObject.getJSONArray(MODEL_TYPE).length(); j++) {
                        JSONObject jsonObjectType = (JSONObject) jsonObject.getJSONArray(MODEL_TYPE).get(j);
                        list.add(new FuelTypeModel(
                                Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())),
                                URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name())));
                        Timber.d(
                                Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name()))
                                        + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name())
                                        + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name()));
                    }
                    // Gesuchte Sprache == Geräte Sprache --> Durchlauf beenden
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mImageName);
        dest.writeDouble(mCapacity);
    }

    @Override
    public String createJson() {
        String jsonString = "{" +
                "\"" + MODEL_ID + "\":\"" + mId + "\"," +
                "\"" + MODEL_NAME + "\":\"" + mName + "\"," +
                "\"" + MODEL_CAPACITY + "\":\"" + mCapacity + "\"," +
                "\"" + MODEL_IMAGE + "\":\"" + mImageName + "\"" +
                "}";
        return jsonString;
    }

    @Override
    public JsonModel loadModelById(Context context, long id) {
        JsonModel object = null;
        Locale searchlanguage = new Locale("en");
        // Language test
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_FUEL_TYPE, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                if (Locale.getDefault().getLanguage().equals(new Locale(language).getLanguage()))
                    searchlanguage = new Locale(language);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Get information from JSON File
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_FUEL_TYPE, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                Timber.d(language);
                // Nicht gesuchte Sprache --> überspringen
                if (searchlanguage.getLanguage().equals(new Locale(language).getLanguage())) {
                    for (int j = 0; j < jsonObject.getJSONArray(MODEL_TYPE).length(); j++) {
                        JSONObject jsonObjectType = (JSONObject) jsonObject.getJSONArray(MODEL_TYPE).get(j);
                        if (Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())) == id) {
                            object = new FuelTypeModel(
                                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())),
                                    URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()),
                                    URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name()));
                            Timber.d(
                                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name()))
                                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name())
                                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name()));
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        FuelTypeModel model = new FuelTypeModel();
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            model = new FuelTypeModel(
                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_CAPACITY), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name()));
            Timber.d(
                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name()))
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_CAPACITY), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name()));


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
        return "FuelTypeModel{" +
                "mImageName='" + mImageName + '\'' +
                ", mCapacity=" + mCapacity +
                '}';
    }

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

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    @SuppressWarnings("WeakerAccess")
    public double getCapacity() {
        return mCapacity;
    }

    @SuppressWarnings("WeakerAccess")
    public void setCapacity(double capacity) {
        mCapacity = capacity;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    // Parcelable
    public static final Creator<FuelTypeModel> CREATOR = new Creator<FuelTypeModel>() {
        @Override
        public FuelTypeModel createFromParcel(Parcel in) {
            return new FuelTypeModel(in);
        }

        @Override
        public FuelTypeModel[] newArray(int size) {
            return new FuelTypeModel[size];
        }
    };

}

