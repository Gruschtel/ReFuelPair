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
public class ManufactureModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
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
    public ManufactureModel() {
        super(-1, "");
        mImageName = "ic_car_black_24dp";
    }

    private ManufactureModel(long id, String name, String imageName) {
        super(id, name);
        this.mImageName = imageName;
    }

    // Parcelable
    private ManufactureModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mName = in.readString();
        mImageName = in.readString();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    // Parcelable
    public static final Creator<ManufactureModel> CREATOR = new Creator<ManufactureModel>() {
        @Override
        public ManufactureModel createFromParcel(Parcel in) {
            return new ManufactureModel(in);
        }

        @Override
        public ManufactureModel[] newArray(int size) {
            return new ManufactureModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mImageName);
    }


    @Override
    public ArrayList<JsonModel> loadModels(Context context) {
        ArrayList<JsonModel> list = new ArrayList<>();
        Locale searchlanguage = new Locale("en");
        // Language test
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_MANUFACTURE, context));
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
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_MANUFACTURE, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                Timber.d(language);
                // Nicht gesuchte Sprache --> überspringen
                if (searchlanguage.getLanguage().equals(new Locale(language).getLanguage())) {
                    for (int j = 0; j < jsonObject.getJSONArray(MODEL_TYPE).length(); j++) {
                        JSONObject jsonObjectType = (JSONObject) jsonObject.getJSONArray(MODEL_TYPE).get(j);
                        list.add(new ManufactureModel(
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
        // Language test
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_MANUFACTURE, context));
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
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(FILE_MANUFACTURE, context));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String language = URLDecoder.decode(jsonObject.getString(MODEL_LANGUAGE), MODEL_CHARSET.name());
                Timber.d(language);
                // Nicht gesuchte Sprache --> überspringen
                if (searchlanguage.getLanguage().equals(new Locale(language).getLanguage())) {
                    for (int j = 0; j < jsonObject.getJSONArray(MODEL_TYPE).length(); j++) {
                        JSONObject jsonObjectType = (JSONObject) jsonObject.getJSONArray(MODEL_TYPE).get(j);
                        if (Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())) == id) {
                            object = new ManufactureModel(
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
        ManufactureModel model = new ManufactureModel();
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            model = new ManufactureModel(
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

    @Override
    public String createJson() {
        String jsonString = "{" +
                "\"" + MODEL_ID + "\":\"" + mId + "\"," +
                "\"" + MODEL_NAME + "\":\"" + mName + "\"," +
                "\"" + MODEL_IMAGE + "\":\"" + mImageName + "\"" +
                "}";
        return jsonString;
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

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ManufactureModel) {
            if (((ManufactureModel) obj).getId() == mId &&
                    ((ManufactureModel) obj).getName().equals(mName) &&
                    ((ManufactureModel) obj).getImageName().equals(mImageName))
                return true;
        } else
            return false;
        return super.equals(obj);
    }

}
