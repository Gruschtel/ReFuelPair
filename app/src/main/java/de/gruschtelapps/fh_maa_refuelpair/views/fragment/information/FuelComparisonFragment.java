package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.BuildConfig;
import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.base.BaseFragment;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter.AddItemAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstUrl;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.ItemListDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.HttpHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.LocationPermissionHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.httpModel.PetrolStationsModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.httpModel.StationModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.FuelSettingActivity;
import timber.log.Timber;
/*
 * Create by Eric Werner
 * Edit by Alea Sauer; Bug Fix: The default value when starting the app for the first time & If list is empty a message is displayed now
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FuelComparisonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FuelComparisonFragment extends BaseFragment implements View.OnClickListener, ItemListDialog.DialogListener {
    // https://developer.android.com/guide/topics/location/strategies#java
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    // UI
    private LinearLayout mContainerEnableGPS, mContainerPermissionGPS, mErrorLoading, mErrorEmpty;
    private Button mButtonEnableGPS, mButtonPermissionGPS, mButtonGetPrice;
    private ProgressBar mProgressBar;

    // GPS
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;

    private Boolean permissionGranted = false;
    private Boolean gpsEnabled = false;
    private LocationPermissionHelper permHelper;

    // Model
    private StationModel stationModel;
    private PetrolStationsModel mClickModel;
    private String mTypeText;
    private String mSort;
    private String mType;
    private float mDistance;

    // Adapter, Internet
    private HttpAsyncTask httpDataAsyncTask;
    private RecyclerView mRecyclerView;
    private AddItemAdapter mAdapter;

    private boolean isExit = false;

    // ===========================================================
    // Constructors
    // ===========================================================
    public FuelComparisonFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment FuelComparisonFragment.
     */
    public static FuelComparisonFragment newInstance() {
        return new FuelComparisonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ...
        Timber.tag(LOG_TAG);
        Timber.d("%s created", LOG_TAG);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_fuel_comparison);

        // Get UI
        mContainerEnableGPS = v.findViewById(R.id.container_fuelComparison_enableGPS);
        mContainerPermissionGPS = v.findViewById(R.id.container_fuelComparison_permissionGPS);
        mErrorLoading = v.findViewById(R.id.container_fuelComparison_error);
        mButtonEnableGPS = v.findViewById(R.id.button_fuelComparison_enableGPS);
        mButtonPermissionGPS = v.findViewById(R.id.button_fuelComparison_permissionGPS);
        mErrorEmpty = v.findViewById(R.id.container_fuelComparison_empty);
        mButtonGetPrice = v.findViewById(R.id.button_fuelComparison_getStations);
        mRecyclerView = v.findViewById(R.id.rv_fuelComparision);
        mProgressBar = v.findViewById(R.id.progress_fuelComparision);

        // ClickListener
        mButtonGetPrice.setOnClickListener(this);
        mButtonPermissionGPS.setOnClickListener(this);
        mButtonEnableGPS.setOnClickListener(this);

        // Set UI
        mProgressBar.setVisibility(View.GONE);
        loadPrefs();

        // Check Permission
        if (permissionGranted) {
            // preprocessing gps
            initLocationManager();
            checkGPSenabled();
            mContainerPermissionGPS.setVisibility(View.GONE);
        } else {
            mContainerPermissionGPS.setVisibility(View.VISIBLE);
            mErrorLoading.setVisibility(View.GONE);
        }

        if (permissionGranted) {
            initLocationListener();
        }

        // get location
        getLastPosition();
        return v;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onAttach(Context context) {
        // when onAttach preprocessing gps again
        super.onAttach(context);
        checkPermission_onStart();
        isExit = false;
        if (permissionGranted) {
            //initLocationManager();
            checkGPSenabled();
            if (mContainerPermissionGPS != null)
                mContainerPermissionGPS.setVisibility(View.GONE);
        } else {
            if (mContainerPermissionGPS != null) {
                mContainerPermissionGPS.setVisibility(View.VISIBLE);
                mErrorLoading.setVisibility(View.GONE);
            }
        }
        if (permissionGranted && gpsEnabled) {
            initLocationListener();
        }
    }

    @Override
    public void onResume() {
        // when onResume preprocessing gps again
        super.onResume();
        isExit = false;
        checkPermission();
        if (permissionGranted) {
            //initLocationManager();
            checkGPSenabled();
            if (mContainerPermissionGPS != null)
                mContainerPermissionGPS.setVisibility(View.GONE);
        } else {
            if (mContainerPermissionGPS != null) {
                mContainerPermissionGPS.setVisibility(View.VISIBLE);
                mErrorLoading.setVisibility(View.GONE);
            }
        }
        if (permissionGranted && gpsEnabled) {
            initLocationListener();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Remove the listener you previously added
        if (locationListener != null)
            locationManager.removeUpdates(locationListener);
        isExit = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove the listener you previously added
        if (locationListener != null)
            locationManager.removeUpdates(locationListener);
        isExit = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menue_full, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menue_fuel_settings) {
            Intent intent = new Intent(getActivity(), FuelSettingActivity.class);
            intent.putExtra(ConstExtras.EXTRA_KEY_SETTING, ConstExtras.EXTRA_FUEL);
            int myflag = ConstRequest.REQUEST_FUEL_COMPARISON_SETTINGS;
            startActivityForResult(intent, myflag);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Timber.d("data\t" + data.toString());
        if (data != null) {
            switch (requestCode) {

                // from fuel comparison settings -> reload data (start searching)
                case ConstRequest.REQUEST_FUEL_COMPARISON_SETTINGS:
                    loadPrefs();
                    View v = new View(getContext());
                    v.setId(R.id.button_fuelComparison_getStations);
                    onClick(v);
                    break;

                // when enable gps -> start searching/hide gps_error_hint
                case ConstRequest.REQUEST_FUEL_COMPARISON_ENABLE_GPS:
                    checkPermission();
                    if (permissionGranted) {
                        initLocationManager();
                        checkGPSenabled();
                        if (mContainerPermissionGPS != null)
                            mContainerPermissionGPS.setVisibility(View.GONE);
                    } else {
                        if (mContainerPermissionGPS != null) {
                            mContainerPermissionGPS.setVisibility(View.VISIBLE);
                            mErrorLoading.setVisibility(View.GONE);
                        }
                    }
                    break;

                //
                case ConstRequest.REQUEST_FUEL_COMPARISON_PERMISSION_GPS:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case ConstRequest.REQUEST_PERMISSION_GPS:
                // If request is cancelled, the result arrays are empty.
                //noinspection StatementWithEmptyBody
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    setPermissionGranted();
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                }
                break;
            case ConstRequest.REQUEST_PERMISSION_NETWORK:
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // start gps settings
            case R.id.button_fuelComparison_enableGPS:
                if (locationListener == null)
                    if (permissionGranted && gpsEnabled) {
                        initLocationListener();
                    }
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ConstRequest.REQUEST_FUEL_COMPARISON_ENABLE_GPS);
                break;

            // start refuelpair-app settings
            case R.id.button_fuelComparison_permissionGPS:
                // disable permissions -> open settings
                // https://stackoverflow.com/questions/32822101/how-to-programmatically-open-the-permission-screen-for-a-specific-app-on-android
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivityForResult(i, ConstRequest.REQUEST_FUEL_COMPARISON_PERMISSION_GPS);
                break;

            // start searching for gas-stations
            case R.id.button_fuelComparison_getStations:
                if (lastKnownLocation == null) {
                    getLastPosition();
                }
                mRecyclerView.setAdapter(null);
                // https://creativecommons.tankerkoenig.de/json/list.php?lat=&lng=&rad=&sort=&type=&apikey=00000000-0000-0000-0000-000000000002
                @SuppressLint("DefaultLocale")
                String url;

                // Loading Prefs
                // Get URL
                try {
                    if (mSort.equals("dist")) {
                        url = ConstUrl.URL_TANKERKOENIG +
                                ConstUrl.URL_TANKERKOENIG_ACTION_RADIUS_SEARCH +
                                String.format(ConstUrl.URL_TANKERKOENIG_SEARCH_RADIUS, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), mDistance, "dist", "all", BuildConfig.TANKERKOENIG_API);
                    } else {
                        url = ConstUrl.URL_TANKERKOENIG +
                                ConstUrl.URL_TANKERKOENIG_ACTION_RADIUS_SEARCH +
                                String.format(ConstUrl.URL_TANKERKOENIG_SEARCH_PRICE, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), mDistance, "price", mType, BuildConfig.TANKERKOENIG_API);
                    }
                    httpDataAsyncTask = new HttpAsyncTask(url, "");
                    httpDataAsyncTask.execute((Void) null);

                } catch (Exception e) {
                    //Timber.d(e);
                    mErrorLoading.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
                break;
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Load needed data from preferences for search
     */
    private void loadPrefs() {
        // init
        SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getActivity()));
        mDistance = pref.getPrefFloat(ConstPreferences.PREF_COMPARISON_DISTANC);

        // get distance
        if (Float.compare(mDistance, ConstError.ERROR_FLOAT) == 0) {
            mDistance = 5f;
            pref.setPrefFloat(ConstPreferences.PREF_COMPARISON_DISTANC, mDistance);
        }

        // get ordering
        mSort = pref.getPrefString(ConstPreferences.PREF_COMPARISON_SORT);
        if (mSort.equals(ConstError.ERROR_STRING)) {
            mSort = "dist";
            pref.setPrefString(ConstPreferences.PREF_COMPARISON_SORT, mSort);
        }

        // get fuel type
        mType = pref.getPrefString(ConstPreferences.PREF_COMPARISON_TYPE);
        if (mType.equals(ConstError.ERROR_STRING)) {
            switch ((int) mVehicleModel.getTankOne().getId()) {
                case 1:
                    // mTypeText = mVehicleModel.getTankOne().getName();
                    mTypeText = mType = "diesel";
                    break;
                case 4:
                    // mTypeText = mVehicleModel.getTankOne().getName();
                    mTypeText = mType = "e5";
                    break;
                case 5:
                default:
                    //mTypeText = mVehicleModel.getTankOne().getName();
                    mTypeText = mType = "e10";
                    break;
            }
            pref.setPrefString(ConstPreferences.PREF_COMPARISON_TYPE, mTypeText);
        } else {
            mTypeText = mType;
        }

        // set ordering
        String sortierung;
        if (mSort.equals("dist")) {
            sortierung = getResources().getString(R.string.action_sortDist);
        } else {
            sortierung = getResources().getString(R.string.action_sortPrice);
        }

        // set UI
        mButtonGetPrice.setText(String.format(Objects.requireNonNull(getContext()).getResources().getString(R.string.button_get_price), mTypeText, sortierung));
    }


    /**
     * Switches the visibility between list and search progress
     * When searching, no list should be displayed, instead a search progress should be displayed.
     */
    private void setVisibilitygetStation() {
        mProgressBar.setVisibility(mProgressBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        mRecyclerView.setVisibility(mRecyclerView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    /**
     * Check permission on start
     */
    private void checkPermission_onStart() {
        permHelper = new LocationPermissionHelper(getActivity(), getContext());
        permissionGranted = permHelper.checkLocationPermission_onStart();
    }

    /**
     * Checks each time the gps coordinates are queried if the permission is still present
     */
    public void checkPermission() {
        if (permHelper != null)
            permissionGranted = permHelper.checkLocationPermission();

        if (permissionGranted) {
            if (mContainerPermissionGPS != null)
                mContainerPermissionGPS.setVisibility(View.GONE);
        } else {
            if (mContainerPermissionGPS != null) {
                mContainerPermissionGPS.setVisibility(View.VISIBLE);
                mErrorLoading.setVisibility(View.GONE);
            }
        }
        // Log.d(LOGTAG, "permissionGranted " + permissionGranted);
    }


    public void setPermissionGranted() {
        permissionGranted = true;
    }

    /**
     * Check if gps is enable
     */
    public void checkGPSenabled() {
        if (locationManager == null)
            return;
        if (isExit)
            return;
        //noinspection deprecation
        String provider = Settings.Secure.getString(Objects.requireNonNull(getActivity()).getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        // locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) is the standard way to check for GPS provider.
        // However, on some phones this always returns false.
        // !provider.contains("gps")) is an alternative way to check if GPS is enabled that might help on some phones:
        // https://stackoverflow.com/questions/21600049/gps-isproviderenabled-always-return-false
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || provider.contains("gps")) {
            gpsEnabled = true;
            if (mContainerEnableGPS != null)
                mContainerEnableGPS.setVisibility(View.GONE);
            if (mButtonGetPrice != null)
                mButtonGetPrice.setVisibility(View.VISIBLE);
            if (mRecyclerView != null)
                mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            gpsEnabled = false;
            if (mContainerEnableGPS != null) {
                mContainerEnableGPS.setVisibility(View.VISIBLE);
                mErrorLoading.setVisibility(View.GONE);
            }
            if (mButtonGetPrice != null)
                mButtonGetPrice.setVisibility(View.GONE);
            if (mRecyclerView != null)
                mRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * Init Location Manager
     */
    public void initLocationManager() {
        try {
            // Acquire a reference to the system Location Manager
            locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
            checkGPSenabled();
        } catch (Exception e) {
            Timber.e("LocationManager can't be initialized");
        }
    }

    /**
     * Init Location Listener
     */
    public void initLocationListener() {
        // Configure update interval for GPS provider
        locationListener = new LocationListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onLocationChanged(Location location) {
                if (permissionGranted && gpsEnabled) {
                    lastKnownLocation = location;
                    checkGPSenabled();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                checkPermission();
                checkGPSenabled();
            }

            @Override
            public void onProviderEnabled(String provider) {
                checkPermission();
                checkGPSenabled();
            }

            @Override
            public void onProviderDisabled(String provider) {
                checkPermission();
                checkGPSenabled();
            }
        };
        // Register the listener with the Location Manager to receive location updates
        try {
            if (permissionGranted) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
                if (mContainerPermissionGPS != null)
                    mContainerPermissionGPS.setVisibility(View.GONE);
            } else {
                if (mContainerPermissionGPS != null) {
                    mContainerPermissionGPS.setVisibility(View.VISIBLE);
                    mErrorLoading.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            //Timber.e(e);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastPosition() {
        if (permissionGranted && gpsEnabled) {
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        }
    }


    /**
     * Check permission for internet & connection
     */
    private void getNetworkAccess() {
        //noinspection StatementWithEmptyBody
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //noinspection StatementWithEmptyBody
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                // User may have declined earlier, ask Android if we should show him a reason
                //noinspection StatementWithEmptyBody
                if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)) {
                    // show an explanation to the user
                    // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
                } else {
                    // request the permission.
                    // CALLBACK_NUMBER is a integer constants
                    requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, ConstRequest.REQUEST_PERMISSION_NETWORK);
                    // The callback method gets the result of the request.
                }
            } else {
                // got permission use it


            }
        } else {
            // lower than VERSION_CODES.M

        }
    }

    @Override
    public void onItemDialogClick(DialogInterface dialog, int pos, int item) {
        switch (item) {
            case R.array.comparison_array:
                if (pos == 0) {
                    Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + mClickModel.getLat() + "," + mClickModel.getLng());
                    //Timber.d(gmmIntentUri.toString());
                    Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
                break;
        }
    }

    @Override
    public void onButtonClick(int action, int flag) {

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Starts a background task, which makes a simple http request to a URL address and processes the received message
     */
    @SuppressLint("StaticFieldLeak")
    public class HttpAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private String mUrl;
        private String mToken;

        public HttpAsyncTask(String url, String token) {
            this.mUrl = url;
            this.mToken = token;
            setVisibilitygetStation();
            mErrorLoading.setVisibility(View.GONE);
            mErrorEmpty.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // init + load data
            getNetworkAccess();
            HttpHelper httpHelper = new HttpHelper();
            String request = httpHelper.httpGet(mUrl, mToken);

            // Get Data
            try {
                JSONObject jsonObjectType = new JSONObject(request);
                if (!Boolean.parseBoolean(URLDecoder.decode(jsonObjectType.getString(StationModel.JSON_OK), JsonModel.MODEL_CHARSET.name()))) {
                    // Todo: error
                    return false;
                }


                if (mSort.equals("dist")) {
                    stationModel = (StationModel) new StationModel().loadRadiusSearch(request);
                } else {
                    stationModel = (StationModel) new StationModel().loadPriceSearch(request);
                }

                // set data to adapter
                mAdapter = new AddItemAdapter(getActivity(), stationModel.getStations(), new AddItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Object object) {
                        mClickModel = (PetrolStationsModel) object;
                        FragmentManager fm = getFragmentManager();
                        ItemListDialog dialogFragment = ItemListDialog.newInstance(R.string.action_emptyString, R.array.comparison_array);
                        dialogFragment.setTargetFragment(FuelComparisonFragment.this, ConstRequest.REQUEST_DIALOG_COMPARISON);
                        dialogFragment.show(Objects.requireNonNull(fm), "ItemListDialog");
                    }
                });

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // show result
            if (result) {
                if (stationModel.getStations().size() == 0) {
                    mErrorEmpty.setVisibility(View.VISIBLE);
                } else {
                    mErrorEmpty.setVisibility(View.GONE);
                }
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                setVisibilitygetStation();
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mErrorLoading.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
