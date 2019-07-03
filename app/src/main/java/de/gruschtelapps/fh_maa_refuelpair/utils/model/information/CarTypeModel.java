package de.gruschtelapps.fh_maa_refuelpair.utils.model.information;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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
public class CarTypeModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private String mImageName;

    // ===========================================================
    // Constructors
    // ===========================================================
    public CarTypeModel() {
        super(-1, "");
    }

    private CarTypeModel(int id, String name, String imageName) {
        super(id, name);
        this.mImageName = imageName;
    }

    // Parcelable
    private CarTypeModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mName = in.readString();
        mImageName = in.readString();
    }
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CarTypeModel) {
            if (((CarTypeModel) obj).getId() == mId &&
                    ((CarTypeModel) obj).getName().equals(mName) &&
                    ((CarTypeModel) obj).getImageName().equals(mImageName))
                return true;
        } else
            return false;
        return super.equals(obj);
    }

    @Override
    public ArrayList<JsonModel> loadModels(Context context) {
        ArrayList<JsonModel> list = new ArrayList<>();
        Locale searchlanguage = new Locale("en");
        // Language test
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_CAR_TYP, context));
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

        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_CAR_TYP, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                Timber.d(language);
                // Nicht gesuchte Sprache --> überspringen
                if (searchlanguage.getLanguage().equals(new Locale(language).getLanguage())) {
                    for (int j = 0; j < jsonObject.getJSONArray(MODEL_TYPE).length(); j++) {
                        JSONObject jsonObjectType = (JSONObject) jsonObject.getJSONArray(MODEL_TYPE).get(j);
                        list.add(new CarTypeModel(
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
    public JsonModel loadModelById(Context context, long id) {
        JsonModel object = null;
        Locale searchlanguage = new Locale("en");
        Timber.tag("asdfasdgasdg").d(searchlanguage.toString());

        // Language test
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_CAR_TYP, context));
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
        Timber.tag("asdfasdgasdg").d(searchlanguage.toString());
        // Get information from JSON File
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_CAR_TYP, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                Timber.d(language);
                // Nicht gesuchte Sprache --> überspringen
                if (searchlanguage.getLanguage().equals(new Locale(language).getLanguage())) {
                    for (int j = 0; j < jsonObject.getJSONArray(MODEL_TYPE).length(); j++) {
                        JSONObject jsonObjectType = (JSONObject) jsonObject.getJSONArray(MODEL_TYPE).get(j);
                        if (Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())) == id) {
                            object = new CarTypeModel(
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mImageName);
    }

    @Override
    public String createJson() {
        String jsonString = "{" +
                "\"" + MODEL_ID + "\":\"" + mId + "\"," +
                "\"" + MODEL_NAME + "\":\"" + mName + "\"," +
                "\"" + MODEL_IMAGE + "\":\"" + mImageName + "\"" +
                "}";
        return jsonString;
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        CarTypeModel model = new CarTypeModel();
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            model = new CarTypeModel(
                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_IMAGE), MODEL_CHARSET.name()));
            Timber.d(
                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name()))
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name())
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
    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setImageName(String mImageName) {
        this.mImageName = mImageName;
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    // Parcelable
    public static final Creator<CarTypeModel> CREATOR = new Creator<CarTypeModel>() {
        @Override
        public CarTypeModel createFromParcel(Parcel in) {
            return new CarTypeModel(in);
        }

        @Override
        public CarTypeModel[] newArray(int size) {
            return new CarTypeModel[size];
        }
    };
}
