package de.gruschtelapps.fh_maa_refuelpair.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.BuildConfig;
import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewCarActivity;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.information.FirstAidTipsFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.information.FuelComparisonFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.information.HistoryFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.information.ImprintFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.information.InformationDataFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.information.ReportFragment;
import timber.log.Timber;

/**
 * Create by Alea Sauer
 * Edit by Eric Werner: IllegalStateException: Can not perform this action after onSaveInstanceState with ViewPager
 * https://stackoverflow.com/questions/7575921/illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    private static final int TIME_INTERVAL = 2500; // # milliseconds, desired time passed between two back presses.

    // ===========================================================
    // Fields
    // ===========================================================
    private boolean pressedOnce;
    private VehicleModel mVehicleModel;

    private static TextView mHeaderCarName;
    private View header;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Fragment[] fragments = new Fragment[]{
            InformationDataFragment.newInstance(),
            HistoryFragment.newInstance(),
            ReportFragment.newInstance(),
            FuelComparisonFragment.newInstance(),
            FirstAidTipsFragment.newInstance(),
            ImprintFragment.newInstance(),
    };

    private String[] fragmentTAGS = new String[]{
            "0",
            "1",
            "2",
            "3",
            "4",
            "5"};

    private String[] fragmentTitle;

    private boolean addCarIsShow = false;
    private NavigationView navigationView;
    private int startID = BuildConfig.START_VIEW;
    private int latestFragmentTag = 0;

    // ===========================================================
    // Constructors
    // ===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Timber.tag(LOG_TAG);
        Timber.d("%s created", LOG_TAG);


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        fragmentTitle = new String[]{
                getResources().getString(R.string.title_Information),
                getResources().getString(R.string.title_history),
                getResources().getString(R.string.title_reports),
                getResources().getString(R.string.title_comparison),
                getResources().getString(R.string.title_firstaidTips),
                getResources().getString(R.string.title_imprint)};


        fragmentManager = getSupportFragmentManager();
        changeFragment(startID);


        setActionBarTitle(Objects.requireNonNull(fragmentTitle[startID]));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);
        mHeaderCarName = header.findViewById(R.id.text_navHeaderMain_title);

        SharedPreferencesManager pref = new SharedPreferencesManager(this);
        long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);
        if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
            Timber.d("load Car Data from id: %s", id);
            mVehicleModel = loadCarData(id);
            mHeaderCarName.setText(mVehicleModel.getName());
        } else {
            // if (false) {
            Timber.d(getResources().getString(R.string.error_loadCarData));
            startNewActivity(ConstExtras.EXTRA_NEW_CAR);
        }
    }


    private void init_page(int id) {
        latestFragmentTag = 5;
        changeFragment(id);
    }

    // https://stackoverflow.com/questions/7575921/illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!pressedOnce) {
                pressedOnce = true;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pressedOnce), Toast.LENGTH_SHORT).show();
                // Handler nach 3000ms ausführen!
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pressedOnce = false;
                    }
                }, TIME_INTERVAL);
            } else if (pressedOnce) {
                pressedOnce = false;
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_navHeaderMain_editCar:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.image_navHeaderMain_dropDown:
                if (addCarIsShow) {
                    v.animate().rotation(0f);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer);
                    addCarIsShow = false;
                } else {
                    v.animate().rotation(180f);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer_car);
                    addCarIsShow = true;
                }
                break;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        TabLayout tabLayout = findViewById(R.id.tabs);
        int position;
        // NAVIGATION DRAWER
        if (id == R.id.menue_navDrawerMain_vehicleData) {
            position = 0;
            changeFragment(position);
            setActionBarTitle(Objects.requireNonNull(fragmentTitle[position]));
            tabLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menue_navDrawerMain_history) {
            position = 1;
            changeFragment(position);
            setActionBarTitle(Objects.requireNonNull(fragmentTitle[position]));
            tabLayout.setVisibility(View.GONE);
        } else if (id == R.id.menue_navDrawerMain_reports) {
            position = 2;
            changeFragment(position);
            setActionBarTitle(Objects.requireNonNull(fragmentTitle[position]));
            tabLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menue_navDrawerMain_fuelComparison) {
            position = 3;
            changeFragment(position);
            setActionBarTitle(Objects.requireNonNull(fragmentTitle[position]));
            tabLayout.setVisibility(View.GONE);
        } else if (id == R.id.menue_navDrawerMain_firstAidTips) {
            position = 4;
            changeFragment(position);
            setActionBarTitle(Objects.requireNonNull(fragmentTitle[position]));
            tabLayout.setVisibility(View.GONE);
        } else if (id == R.id.menue_navDrawerMain_legalDetails) {
            position = 5;
            changeFragment(position);
            setActionBarTitle(Objects.requireNonNull(fragmentTitle[position]));
            tabLayout.setVisibility(View.GONE);
        } else if (id == R.id.menue_navDrawerMain_addCar) {
            startNewActivity(ConstExtras.EXTRA_NEW_CAR);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
            //noinspection SwitchStatementWithTooFewBranches
            switch (requestCode) {
                case ConstRequest.REQUEST_MAIN_NEW_CAR:
                case ConstRequest.REQUEST_MAIN_EDIT_CAR:
                    Timber.d("REQUEST_MAIN_NEW_CAR");
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            Timber.d("RESULT_OK");

                            SharedPreferencesManager pref = new SharedPreferencesManager(this);
                            long id = data.getLongExtra(String.valueOf(ConstExtras.EXTRA_NEW_CAR), ConstError.ERROR_LONG);

                            if (id != ConstError.ERROR_LONG) {
                                pref.setPreLong(ConstPreferences.PREF_CURRENT_CAR, id);
                                pref.setPrefBool(ConstPreferences.PREF_FIRST_START, true);
                                // check if first start!
                                // Load Data
                                Timber.d("load Car Data from id: %s", id);
                                mVehicleModel = loadCarData(id);

                                Timber.d(String.valueOf(mVehicleModel.getId()));
                                Timber.d(String.valueOf(mVehicleModel.getName()));
                                Timber.d(mVehicleModel.createJson());
                                fragments = null;
                                fragments = new Fragment[]{
                                        InformationDataFragment.newInstance(),
                                        HistoryFragment.newInstance(),
                                        ReportFragment.newInstance(),
                                        FuelComparisonFragment.newInstance(),
                                        FirstAidTipsFragment.newInstance(),
                                        ImprintFragment.newInstance()};

                                startID = 1;
                                latestFragmentTag = 5;
                                fragmentManager = null;
                                fragmentManager = getSupportFragmentManager();
                                changeFragment(startID);
                                setHeaderName(mVehicleModel.getName());
                            } else {
                                Toast.makeText(this, getResources().getString(R.string.error_loadCarData), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case ConstRequest.REQUEST_EXIT_APP:
                            finish();
                            Timber.d("REQUEST_EXIT_APP");
                            break;
                    }
                    break;
                default:
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public static void setHeaderName(String name) {
        Timber.d("setHeaderName");
        mHeaderCarName.setText(name);
    }

    private void setActionBarTitle(String heading) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(heading);
        }
        Objects.requireNonNull(actionBar).show();

    }

    private VehicleModel loadCarData(long id) {
        DBHelper dbHelper = new DBHelper(this);
        return dbHelper.getGet().selectCarById(id);
    }

    private void startNewActivity(@SuppressWarnings("SameParameterValue") int id) {
        Intent intent = null;
        int myflag = 0;
        //noinspection SwitchStatementWithTooFewBranches
        switch (id) {
            case ConstExtras.EXTRA_NEW_CAR:
                intent = new Intent(MainActivity.this, NewCarActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_ADD, ConstExtras.EXTRA_NEW_CAR);
                myflag = ConstRequest.REQUEST_MAIN_NEW_CAR;
                break;
        }
        if (intent != null) {
            startActivityForResult(intent, myflag);
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
//
    // fragmentTransaction.add = adds a fragment to the current fragmentManager --> it only has to be created once. This way you can save some time and resources
    // in which the already created fragment is called instead of creating a fragment each time with fragmentTransaction.replace.
    //
    // fragmentTransaction.replace = creates each time a new fragment and replaces it with the current
    //
    // https://stackoverflow.com/questions/27427543/android-save-fragments-into-navigation-drawer
    //
    public void changeFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Add the fragments only once if array haven't fragment
        if (fragmentManager.findFragmentByTag(fragmentTAGS[position]) == null) {
            fragmentTransaction.add(R.id.container_main_framelayout, fragments[position], fragmentTAGS[position]);
        }
        // Hiding & Showing fragments
        for (int catx = 0; catx < fragments.length; catx++) {
            if (catx == position) {
                fragmentTransaction.show(fragments[catx]);
            } else {
                // Check if the fragment is added and then hide it
                if (fragmentManager.findFragmentByTag(fragmentTAGS[catx]) != null) {
                    fragmentTransaction.hide(fragments[catx]);
                }

            }
        }
        if (!fragmentTAGS[latestFragmentTag].equals(fragmentTAGS[position])) {
            fragmentTransaction.replace(R.id.container_main_framelayout, fragments[position], fragmentTAGS[position]);
            latestFragmentTag = position;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}

