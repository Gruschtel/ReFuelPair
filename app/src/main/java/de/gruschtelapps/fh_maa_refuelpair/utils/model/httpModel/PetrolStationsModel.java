package de.gruschtelapps.fh_maa_refuelpair.utils.model.httpModel;

import android.location.Location;

import java.util.ArrayList;

import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import timber.log.Timber;


/*
 * Create by Eric Werner
 */
public class PetrolStationsModel extends JsonModel {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private String mId;
    private String mName;
    private String mBrand;
    private String mStreet;
    private String mPlace;
    private float mLat;
    private float mLng;
    private float mDistance;
    private float mDiesel;
    private float mE5;
    private float mE10;
    private boolean mOpen;
    private String mHousenumber;
    private int mPostCode;
    private float mPrice;

    private boolean mWholeDay;
    private String mState;

    private Location mLocation;

    private ArrayList<StationOpeningTimes> mStationOpeningTimes;

    public PetrolStationsModel(String uuid, String name, String brand, String street, String place, String lat, String lng, String dist, String diesel, String e5, String e10, String open, String housenumber, String postCoade) {
        mId = uuid;
        mName = name;
        mBrand = brand;
        mStreet = street;
        mPlace = place;
        try {
            this.mLat = Float.parseFloat(lat);
        } catch (Exception e) {
            //Timber.e(e);
            this.mLat = 0.0f;
        }
        try {
            this.mLng = Float.parseFloat(lng);
        } catch (Exception e) {
            //Timber.e(e);
            this.mLng = 0.0f;
        }
        try {
            this.mDistance = Float.parseFloat(dist);
        } catch (Exception e) {
            //Timber.e(e);
            this.mDistance = 0.0f;
        }
        try {
            this.mDiesel = Float.parseFloat(diesel);
        } catch (Exception e) {
            //Timber.e(e);
            this.mDiesel = 0.0f;
        }
        try {
            this.mE5 = Float.parseFloat(e5);
        } catch (Exception e) {
            //Timber.e(e);
            this.mE5 = 0.0f;
        }
        try {
            this.mE10 = Float.parseFloat(e10);
        } catch (Exception e) {
            //Timber.e(e);
            this.mE10 = 0.0f;
        }
        try {
            this.mOpen = Boolean.parseBoolean(e5);
        } catch (Exception e) {
            //Timber.e(e);
            this.mOpen = false;
        }
        this.mHousenumber = housenumber;
        try {
            this.mPostCode = Integer.parseInt(postCoade);
        } catch (Exception e) {
            //Timber.e(e);
            this.mPostCode = 0;
        }

        mLocation = new Location("");
        mLocation.setLatitude(mLat);
        mLocation.setLongitude(mLng);
        mPrice = 0.0f;
    }


    public PetrolStationsModel(String uuid, String name, String brand, String street, String place,
                               String lat, String lng,
                               String diesel, String e5, String e10,
                               String open, String housenumber, String postCoade, String wholeDay, String state) {
        mId = uuid;
        mName = name;
        mBrand = brand;
        mStreet = street;
        mPlace = place;
        try {
            this.mLat = Float.parseFloat(lat);
        } catch (Exception e) {
            Timber.e(e);
            this.mLat = 0.0f;
        }
        try {
            this.mLng = Float.parseFloat(lng);
        } catch (Exception e) {
            Timber.e(e);
            this.mLng = 0.0f;
        }
        try {
            this.mDiesel = Float.parseFloat(diesel);
        } catch (Exception e) {
            Timber.e(e);
            this.mDiesel = 0.0f;
        }
        try {
            this.mE5 = Float.parseFloat(e5);
        } catch (Exception e) {
            Timber.e(e);
            this.mE5 = 0.0f;
        }
        try {
            this.mE10 = Float.parseFloat(e10);
        } catch (Exception e) {
            Timber.e(e);
            this.mE10 = 0.0f;
        }
        try {
            this.mOpen = Boolean.parseBoolean(open);
        } catch (Exception e) {
            Timber.e(e);
            this.mOpen = false;
        }
        this.mHousenumber = housenumber;
        try {
            this.mPostCode = Integer.parseInt(postCoade);
        } catch (Exception e) {
            Timber.e(e);
            this.mPostCode = 0;
        }
        try {
            this.mWholeDay = Boolean.parseBoolean(wholeDay);
        } catch (Exception e) {
            Timber.e(e);
            this.mWholeDay = false;
        }

        this.mState = state;

        mLocation = new Location("");
        mLocation.setLatitude(mLat);
        mLocation.setLongitude(mLng);
        mPrice = 0.0f;
    }

    public PetrolStationsModel(String uuid, String name, String brand, String street, String place,
                               String lat, String lng, String dist, String price,
                               String open, String housenumber, String postcode) {
        mId = uuid;
        mName = name;
        mBrand = brand;
        mStreet = street;
        mPlace = place;
        try {
            this.mLat = Float.parseFloat(lat);
        } catch (Exception e) {
            Timber.e(e);
            this.mLat = 0.0f;
        }
        try {
            this.mLng = Float.parseFloat(lng);
        } catch (Exception e) {
            Timber.e(e);
            this.mLng = 0.0f;
        }
        try {
            this.mOpen = Boolean.parseBoolean(open);
        } catch (Exception e) {
            Timber.e(e);
            this.mOpen = false;
        }
        this.mHousenumber = housenumber;

        try {
            this.mDistance = Float.parseFloat(dist);
        } catch (Exception e) {
            Timber.e(e);
            this.mDistance = 0.0f;
        }

        try {
            this.mPrice = Float.parseFloat(price);
        } catch (Exception e) {
            Timber.e(e);
            this.mPrice = 0.0f;
        }

        try {
            this.mPostCode = Integer.parseInt(postcode);
        } catch (Exception e) {
            Timber.e(e);
            this.mPostCode = 1;
        }

        mDiesel = -1.0f;
        mE5 = -1.0f;
        mE10 = -1.0f;


        mLocation = new Location("");
        mLocation.setLatitude(mLat);
        mLocation.setLongitude(mLng);

    }

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public String toString() {
        return "PetrolStationsModel{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mBrand='" + mBrand + '\'' +
                ", mStreet='" + mStreet + '\'' +
                ", mPlace='" + mPlace + '\'' +
                ", mLat=" + mLat +
                ", mLng=" + mLng +
                ", mDistance=" + mDistance +
                ", mDiesel=" + mDiesel +
                ", mE5=" + mE5 +
                ", mE10=" + mE10 +
                ", mOpen=" + mOpen +
                ", mHousenumber='" + mHousenumber + '\'' +
                ", mPostCode=" + mPostCode +
                ", mPrice=" + mPrice +
                ", mWholeDay=" + mWholeDay +
                ", mState='" + mState + '\'' +
                ", mLocation=" + mLocation +
                ", mStationOpeningTimes=" + mStationOpeningTimes +
                '}';
    }


    // ===========================================================
    // Methods
    // ===========================================================

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String mBrand) {
        this.mBrand = mBrand;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public float getLat() {
        return mLat;
    }

    public void setLat(float mLat) {
        this.mLat = mLat;
    }

    public float getLng() {
        return mLng;
    }

    public void setLng(float mLng) {
        this.mLng = mLng;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public float getDiesel() {
        return mDiesel;
    }

    public void setDiesel(float mDiesel) {
        this.mDiesel = mDiesel;
    }

    public float getE5() {
        return mE5;
    }

    public void setE5(float mE5) {
        this.mE5 = mE5;
    }

    public float getE10() {
        return mE10;
    }

    public void setE10(float mE10) {
        this.mE10 = mE10;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean mOpen) {
        this.mOpen = mOpen;
    }

    public String getHousenumber() {
        return mHousenumber;
    }

    public void setHousenumber(String mHousenumber) {
        this.mHousenumber = mHousenumber;
    }

    public int getPostCode() {
        return mPostCode;
    }

    public void setPostCode(int mPostCode) {
        this.mPostCode = mPostCode;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public ArrayList<StationOpeningTimes> getStationOpeningTimes() {
        return mStationOpeningTimes;
    }

    public void setStationOpeningTimes(ArrayList<StationOpeningTimes> mStationOpeningTimes) {
        this.mStationOpeningTimes = mStationOpeningTimes;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
