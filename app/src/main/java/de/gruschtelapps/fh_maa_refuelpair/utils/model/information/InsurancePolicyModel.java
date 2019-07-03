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

/*
 * Create by Eric Werner
 */
public class InsurancePolicyModel extends JsonModel implements JsonModel.ListModelInterface, Parcelable {
    // ===========================================================
    // Constants
    // ===========================================================

    // Parcelable
    public static final Creator<InsurancePolicyModel> CREATOR = new Creator<InsurancePolicyModel>() {
        @Override
        public InsurancePolicyModel createFromParcel(Parcel in) {
            return new InsurancePolicyModel(in);
        }

        @Override
        public InsurancePolicyModel[] newArray(int size) {
            return new InsurancePolicyModel[size];
        }
    };
    // ===========================================================
    // Fields
    // ===========================================================
    private String mSureName, mTelephone, mEmail, mAdress, mInsurancePolicyName, mInsurancePolicyNumber;


    // ===========================================================
    // Constructors
    // ===========================================================
    public InsurancePolicyModel() {
        super(-1, "");
    }

    private InsurancePolicyModel(int id, String name, String surename, String phone, String email, String adress, String insurancepolicyname, String insurancepolicynumber) {
        super(id, name);
        mSureName = surename;
        mTelephone = phone;
        mEmail = email;
        mAdress = adress;
        mInsurancePolicyName = insurancepolicyname;
        mInsurancePolicyNumber = insurancepolicynumber;

    }

    // Parcelable
    private InsurancePolicyModel(Parcel in) {
        super(in);
        mId = in.readLong();
        mName = (String) in.readValue(String.class.getClassLoader());
        mSureName = in.readString();
        mTelephone = in.readString();
        mEmail = in.readString();
        mAdress = in.readString();
        mInsurancePolicyName = in.readString();
        mInsurancePolicyNumber = in.readString();
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
        String jsonString = "{" +
                "\"" + MODEL_ID + "\":\"" + mId + "\"," +
                "\"" + MODEL_NAME + "\":\"" + mName + "\"," +
                "\"" + MODEL_SURENAME + "\":\"" + mSureName + "\"," +
                "\"" + MODEL_PHONE + "\":\"" + mTelephone + "\"," +
                "\"" + MODEL_EMAIL + "\":\"" + mEmail + "\"," +
                "\"" + MODEL_ADRESS + "\":\"" + mAdress + "\"," +
                "\"" + MODEL_INSURANCE_POLICY_NAME + "\":\"" + mInsurancePolicyName + "\"," +
                "\"" + MODEL_INSURANCE_POLICY_NUMBER + "\":\"" + mInsurancePolicyNumber + "\"" +
                "}";
        return jsonString;
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        InsurancePolicyModel model = new InsurancePolicyModel();
        try {
            // Nicht gesuchte Sprache --> überspringen
            JSONObject jsonObjectType = new JSONObject(information);
            model = new InsurancePolicyModel(
                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name())),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_SURENAME), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_PHONE), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_EMAIL), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_ADRESS), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_INSURANCE_POLICY_NAME), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(MODEL_INSURANCE_POLICY_NUMBER), MODEL_CHARSET.name()));
            Timber.d(
                    Integer.parseInt(URLDecoder.decode(jsonObjectType.getString(MODEL_ID), MODEL_CHARSET.name()))
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_NAME), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_SURENAME), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_PHONE), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_EMAIL), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_ADRESS), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_INSURANCE_POLICY_NAME), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(MODEL_INSURANCE_POLICY_NUMBER), MODEL_CHARSET.name()));


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
        dest.writeString(mSureName);
        dest.writeString(mTelephone);
        dest.writeString(mEmail);
        dest.writeString(mAdress);
        dest.writeString(mInsurancePolicyName);
        dest.writeString(mInsurancePolicyNumber);
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

    public String getSureName() {
        return mSureName;
    }

    public void setSureName(String mSureName) {
        this.mSureName = mSureName;
    }

    public String getTelephone() {
        return mTelephone;
    }

    public void setTelephone(String mTelephone) {
        this.mTelephone = mTelephone;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getAdress() {
        return mAdress;
    }

    public void setAdress(String mAdress) {
        this.mAdress = mAdress;
    }

    public String getInsurancePolicyName() {
        return mInsurancePolicyName;
    }

    public void setInsurancePolicyName(String mInsurancePolicyName) {
        this.mInsurancePolicyName = mInsurancePolicyName;
    }

    public String getInsurancePolicyNumber() {
        return mInsurancePolicyNumber;
    }

    public void setInsurancePolicyNumber(String mInsurancePolicyNumber) {
        this.mInsurancePolicyNumber = mInsurancePolicyNumber;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
