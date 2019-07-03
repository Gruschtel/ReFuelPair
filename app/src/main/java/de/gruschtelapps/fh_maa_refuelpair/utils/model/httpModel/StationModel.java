package de.gruschtelapps.fh_maa_refuelpair.utils.model.httpModel;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;

import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import timber.log.Timber;

/*
 * Create by Eric Werner
 */
@SuppressWarnings("FieldCanBeLocal")
public class StationModel extends JsonModel implements JsonModel.ListModelInterface {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String JSON_OK = "ok";
    private final String JSON_LICENSE = "license";
    private final String JSON_DATA = "data";
    private final String JSON_STATUS = "status";
    private final String JSON_STATIONS = "stations";
    private final String JSON_STATION = "station";

    private final String JSON_STATION_ID = "id";
    private final String JSON_STATION_NAME = "name";
    private final String JSON_STATION_BRAND = "brand";
    private final String JSON_STATION_STREET = "street";
    private final String JSON_STATION_PLACE = "place";
    private final String JSON_STATION_LAT = "lat";
    private final String JSON_STATION_LNG = "lng";
    private final String JSON_STATION_DIST = "dist";
    private final String JSON_STATION_DIESEL = "diesel";
    private final String JSON_STATION_E5 = "e5";
    private final String JSON_STATION_E10 = "e10";
    private final String JSON_STATION_OPEN = "isOpen";
    private final String JSON_STATION_HOUSENUMBER = "houseNumber";
    private final String JSON_STATION_POSTCODE = "postCode";
    private final String JSON_STATION_STATE = "state";
    private final String JSON_STATION_WHOLE_DAY = "wholeDay";
    private final String JSON_STATION_PRICE = "price";

    private final String JSON_TIMES = "openingTimes";
    private final String JSON_TIMES_DAY = "text";
    private final String JSON_TIMES_START = "start";
    private final String JSON_TIMES_END = "end";



    // ===========================================================
    // Fields
    // ===========================================================
    private boolean mOk;
    private String mLicence;
    private String mData;
    private String mStatus;
    private ArrayList<JsonModel> mStations;

    // ===========================================================
    // Constructors
    // ===========================================================

    public StationModel() {
        this.mStations = new ArrayList<>();
    }

    @SuppressWarnings("WeakerAccess")
    public StationModel(String mOk, String mLicence, String mData, String mStatus) {
        try{
            this.mOk = Boolean.parseBoolean(mOk);
        }catch (Exception e){
            Timber.e(e);
            this.mOk = false;
        }
        this.mLicence = mLicence;
        this.mData = mData;
        this.mStatus = mStatus;
        this.mStations = new ArrayList<>();
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
        return null;
    }

    @Override
    public JsonModel loadModelByJson(Context context, String information) {
        return null;
    }

    public static boolean compare(double a, double b, double epsilon){
        return Math.abs(a - b) < epsilon;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public JsonModel loadDetailSearch(String information) {
        StationModel model = new StationModel();
        try {
            JSONObject jsonObjectType = new JSONObject(information);

            model = new StationModel(
                    (URLDecoder.decode(jsonObjectType.getString(JSON_OK), MODEL_CHARSET.name())),
                    URLDecoder.decode(jsonObjectType.getString(JSON_LICENSE), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(JSON_DATA), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(JSON_STATUS), MODEL_CHARSET.name()));

            Timber.d(
                    (URLDecoder.decode(jsonObjectType.getString(JSON_OK), MODEL_CHARSET.name()))
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_LICENSE), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_DATA), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_STATUS), MODEL_CHARSET.name()));

            JSONObject jsonArray = new JSONObject(URLDecoder.decode(jsonObjectType.getString(JSON_STATION), MODEL_CHARSET.name()));
            Timber.d(model.toString());

            model.getStations().add(new PetrolStationsModel(
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_ID), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_NAME), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_BRAND), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_STREET), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_PLACE), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_LAT), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_LNG), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_DIESEL), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_E5), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_E10), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_OPEN), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_HOUSENUMBER), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_POSTCODE), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_WHOLE_DAY), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonArray.getString(JSON_STATION_STATE), MODEL_CHARSET.name()))
            );

            JSONArray jsonArrayStationTime = new JSONArray(URLDecoder.decode(jsonArray.getString(JSON_TIMES), MODEL_CHARSET.name()));
            ArrayList<StationOpeningTimes> openingTimes = new ArrayList<>();

            for (int y = 0; y < jsonArrayStationTime.length(); y++) {
                JSONObject jsonObjectStationTime = (JSONObject) jsonArrayStationTime.get(y);
                openingTimes.add(new StationOpeningTimes(
                        URLDecoder.decode(jsonObjectStationTime.getString(JSON_TIMES_DAY), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStationTime.getString(JSON_TIMES_START), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStationTime.getString(JSON_TIMES_END), MODEL_CHARSET.name())
                ));
            }
            ((PetrolStationsModel)model.getStations().get(0)).setStationOpeningTimes(openingTimes);
            Timber.tag("adsgfadsgasdg").d(model.getStations().get(0).toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }


    public JsonModel loadRadiusSearch(String information) {
        StationModel model = new StationModel();
        try {
            JSONObject jsonObjectType = new JSONObject(information);

            model = new StationModel(
                    (URLDecoder.decode(jsonObjectType.getString(JSON_OK), MODEL_CHARSET.name())),
                    URLDecoder.decode(jsonObjectType.getString(JSON_LICENSE), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(JSON_DATA), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(JSON_STATUS), MODEL_CHARSET.name()));

            Timber.d(
                    (URLDecoder.decode(jsonObjectType.getString(JSON_OK), MODEL_CHARSET.name()))
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_LICENSE), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_DATA), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_STATUS), MODEL_CHARSET.name()));

            JSONArray jsonArray = new JSONArray(URLDecoder.decode(jsonObjectType.getString(JSON_STATIONS), MODEL_CHARSET.name()));
            Timber.d(model.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectStation = (JSONObject) jsonArray.get(i);
                model.getStations().add(new PetrolStationsModel(
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_ID), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_NAME), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_BRAND), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_STREET), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_PLACE), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_LAT), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_LNG), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_DIST), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_DIESEL), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_E5), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_E10), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_OPEN), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_HOUSENUMBER), MODEL_CHARSET.name()),
                        URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_POSTCODE), MODEL_CHARSET.name())));

                Timber.d(model.getStations().get(i).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    public JsonModel loadPriceSearch(String request) {
        StationModel model = new StationModel();
        try {
            JSONObject jsonObjectType = new JSONObject(request);

            model = new StationModel(
                    (URLDecoder.decode(jsonObjectType.getString(JSON_OK), MODEL_CHARSET.name())),
                    URLDecoder.decode(jsonObjectType.getString(JSON_LICENSE), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(JSON_DATA), MODEL_CHARSET.name()),
                    URLDecoder.decode(jsonObjectType.getString(JSON_STATUS), MODEL_CHARSET.name()));

            Timber.d(
                    (URLDecoder.decode(jsonObjectType.getString(JSON_OK), MODEL_CHARSET.name()))
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_LICENSE), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_DATA), MODEL_CHARSET.name())
                            + " " + URLDecoder.decode(jsonObjectType.getString(JSON_STATUS), MODEL_CHARSET.name()));

            JSONArray jsonArray = new JSONArray(URLDecoder.decode(jsonObjectType.getString(JSON_STATIONS), MODEL_CHARSET.name()));
            Timber.d(model.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectStation = (JSONObject) jsonArray.get(i);
                   if(!URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_PRICE), MODEL_CHARSET.name()).equals("null"))
                        model.getStations().add(new PetrolStationsModel(
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_ID), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_NAME), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_BRAND), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_STREET), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_PLACE), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_LAT), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_LNG), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_DIST), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_PRICE), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_OPEN), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_HOUSENUMBER), MODEL_CHARSET.name()),
                                URLDecoder.decode(jsonObjectStation.getString(JSON_STATION_POSTCODE), MODEL_CHARSET.name())));
                    Timber.d(model.getStations().get(i).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return model;
    }

    public boolean isOk() {
        return mOk;
    }

    public void setOk(boolean mOk) {
        this.mOk = mOk;
    }

    public String getLicence() {
        return mLicence;
    }

    public void setLicence(String mLicence) {
        this.mLicence = mLicence;
    }

    public String getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public ArrayList<JsonModel> getStations() {
        return mStations;
    }

    public void setStations(ArrayList<JsonModel> mStations) {
        this.mStations = mStations;
    }

    @NotNull
    @Override
    public String toString() {
        return "StationModel{" +
                "mOk=" + mOk +
                ", mLicence='" + mLicence + '\'' +
                ", mData='" + mData + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mStations=" + mStations +
                '}';
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
