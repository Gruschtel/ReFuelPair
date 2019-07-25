package de.gruschtelapps.fh_maa_refuelpair.utils.model.add;

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
 * Create by Alea Sauer
 */
public class KontaktModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private String mName, mSurename, mPhonenumber, mMail, mAdress;

    // ===========================================================
    // Constructors
    // ===========================================================
    public KontaktModel() {
        mName = "";
        mSurename = "";
        mPhonenumber = "";
        mMail = "";
        mAdress = "";
    }


    public KontaktModel(String name, String surename, String phonenumber, String mail, String adress) {
        this.mName = name;
        this.mSurename = surename;
        this.mPhonenumber = phonenumber;
        this.mMail = mail;
        this.mAdress = adress;
    }


    // Parcelable
    private KontaktModel(Parcel in) {
        super(in);
        this.mName = in.readString();
        this.mSurename = in.readString();
        this.mPhonenumber = in.readString();
        this.mMail = in.readString();
        this.mAdress = in.readString();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mSurename);
        dest.writeString(mPhonenumber);
        dest.writeString(mMail);
        dest.writeString(mAdress);
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
        String jsonString = "{\"" + MODEL_KONTAKT + "\":{" +
                "\"" + MODEL_NAME + "\":\"" + mName + "\"," +
                "\"" + MODEL_SURENAME + "\":\"" + mSurename + "\"," +
                "\"" + MODEL_PHONE + "\":\"" + mPhonenumber + "\"," +
                "\"" + MODEL_EMAIL + "\":\"" + mMail + "\"," +
                "\"" + MODEL_ADRESS + "\":\"" + mAdress + "\"" +
                "}}";
        Timber.d(jsonString);
        return jsonString;
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        KontaktModel model = new KontaktModel();
        Timber.d(information);
        try {
            JSONObject jsonObjectType = new JSONObject(information);
            JSONObject jsonObject = jsonObjectType.getJSONObject(MODEL_KONTAKT);

            model.setName(URLDecoder.decode(jsonObject.getString(MODEL_NAME), MODEL_CHARSET.name()));
            model.setSurename(URLDecoder.decode(jsonObject.getString(MODEL_SURENAME), MODEL_CHARSET.name()));

            model.setPhonenumber(URLDecoder.decode(jsonObject.getString(MODEL_PHONE), MODEL_CHARSET.name()));
            model.setMail(URLDecoder.decode(jsonObject.getString(MODEL_EMAIL), MODEL_CHARSET.name()));
            model.setAdress(URLDecoder.decode(jsonObject.getString(MODEL_ADRESS), MODEL_CHARSET.name()));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public String toString() {
        return "KontaktModel{" +
                "mName='" + mName + '\'' +
                ", mSurename='" + mSurename + '\'' +
                ", mPhonenumber='" + mPhonenumber + '\'' +
                ", mMail='" + mMail + '\'' +
                ", mAdress='" + mAdress + '\'' +
                '}';
    }

    // ===========================================================
    // Methods
    // ===========================================================


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSurename() {
        return mSurename;
    }

    public void setSurename(String mSurename) {
        this.mSurename = mSurename;
    }

    public String getPhonenumber() {
        return mPhonenumber;
    }

    public void setPhonenumber(String mPhonenumber) {
        this.mPhonenumber = mPhonenumber;
    }

    public String getMail() {
        return mMail;
    }

    public void setMail(String mMail) {
        this.mMail = mMail;
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(String mAdress) {
        this.mAdress = mAdress;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
    // Parcelable
    public static final Parcelable.Creator<KontaktModel> CREATOR = new Parcelable.Creator<KontaktModel>() {
        @Override
        public KontaktModel createFromParcel(Parcel in) {
            return new KontaktModel(in);
        }

        @Override
        public KontaktModel[] newArray(int size) {
            return new KontaktModel[size];
        }
    };
}
