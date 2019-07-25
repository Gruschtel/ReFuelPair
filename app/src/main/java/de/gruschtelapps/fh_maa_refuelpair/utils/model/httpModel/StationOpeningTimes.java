package de.gruschtelapps.fh_maa_refuelpair.utils.model.httpModel;

/*
 * Create by Eric Werner
 */
public class StationOpeningTimes {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    String mDate;
    String mOpenTime;
    String mCloseTime;


    // ===========================================================
    // Constructors
    // ===========================================================
    public StationOpeningTimes(String date, String openTime, String closeTime) {
        this.mDate = date;
        this.mOpenTime = openTime;
        this.mCloseTime = closeTime;
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public String toString() {
        return "StationOpeningTimes{" +
                "mDate='" + mDate + '\'' +
                ", mOpenTime='" + mOpenTime + '\'' +
                ", mCloseTime='" + mCloseTime + '\'' +
                '}';
    }


    // ===========================================================
    // Methods
    // ===========================================================

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getOpenTime() {
        return mOpenTime;
    }

    public void setOpenTime(String mOpenTime) {
        this.mOpenTime = mOpenTime;
    }

    public String getCloseTime() {
        return mCloseTime;
    }

    public void setCloseTime(String mCloseTime) {
        this.mCloseTime = mCloseTime;
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
