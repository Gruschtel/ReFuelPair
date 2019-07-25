package de.gruschtelapps.fh_maa_refuelpair.views.activities.add;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.DataPagerAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.LockableViewPager;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.viewPager.ZoomOutPageTransformer;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstAction;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.MessageDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.StorageImageManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.InsurancePolicyModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.add.NewCarFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.add.NewInsurancePolicyFragment;
import timber.log.Timber;

/*
 * Create by Eric Werner
 */
public class NewCarActivity extends AppCompatActivity implements MessageDialog.DialogListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private DataPagerAdapter mSectionsPagerAdapter;
    private LockableViewPager mViewPager;

    // Add new Car
    private VehicleModel mVehicleModel;
    private BroadcastReceiver mReceiver;

    //
    private Intent mCurrentService;
    private static boolean isEdit;

    // ===========================================================
    // Constructors
    // ===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ActionBar
        setActionBarTitle(getResources().getString(R.string.title_new_vehicle));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new DataPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter. PRE INIT
        mViewPager = findViewById(R.id.frame_add_viewPager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPagingEnabled(false);

        // load data
        mCurrentService = getIntent();
        if (getIntent().getExtras() != null) {

            // Start New Car (new Car Information and new Insurance)
            if (getIntent().getExtras().getInt(ConstExtras.EXTRA_KEY_ADD) == ConstExtras.EXTRA_NEW_CAR) {
                isEdit = false;
                newCar();

                // Edit Car Information
            } else if (getIntent().getExtras().getInt(ConstExtras.EXTRA_KEY_EDIT) == ConstExtras.EXTRA_EDIT_CAR) {
                isEdit = true;
                editCar();

                // Edit Insurance
            } else if (getIntent().getExtras().getInt(ConstExtras.EXTRA_KEY_EDIT) == ConstExtras.EXTRA_EDIT_INSURANCE) {
                isEdit = true;
                editInsuracne();

                // error meldung
            } else {
                Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set Adapter to ViewPager
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onResume() {
        if (mReceiver == null) {
            mReceiver = new AddReceiver();
            // set Brodcast-Filter
            IntentFilter filterRefreshUpdate = new IntentFilter();
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_NEXT_PAGE);
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_UPDATE_VEHICLE);
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_FINISH);

            registerReceiver(mReceiver, filterRefreshUpdate);
        }
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        // unregister Broadcastreceiver
        if (mReceiver != null)
            this.unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (item.getItemId()) {
            // Toolbar Button
            case android.R.id.home:
                // Aktuller Service
                ClickHandler();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ClickHandler();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDialogItemClick(DialogInterface dialog, int pos, int item) {

    }

    @Override
    public void onDialogButtonClick(int action, int flag) {
        // handle back dialog
        if (action == ConstRequest.REQUEST_DIALOG_BACK) {
            switch (flag) {
                // if positiv save date
                case ConstRequest.REQUEST_DIALOG_POSITIV:
                    Intent intent = new Intent();
                    SharedPreferencesManager pref = new SharedPreferencesManager(this);
                    if (!pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                        setResult(ConstRequest.REQUEST_EXIT_APP, intent);
                    } else {

                        // CANCEL ADD NEW CAR
                        Timber.d(mVehicleModel.getPhoto());
                        // if(!mVehicleModel.getPhoto().equals(ConstError.ERROR_STRING) ){
                        if (mVehicleModel.getPhoto() != null) {
                            StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(getApplicationContext()));
                            storageImageManager.deleteImage(mVehicleModel.getPhoto());
                        }
                        setResult(Activity.RESULT_CANCELED, intent);
                    }
                    finish();
                    break;
                case ConstRequest.REQUEST_DIALOG_NEGATIV:
                    break;
            }
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * init new car fragments
     */
    private void newCar() {
        mSectionsPagerAdapter.addFragment(NewCarFragment.newInstance(), getResources().getString(R.string.title_vehicle));
        mSectionsPagerAdapter.addFragment(NewInsurancePolicyFragment.newInstance(), getResources().getString(R.string.title_insurancePolicy));
    }

    /**
     * init edit insurance fragments
     */
    private void editInsuracne() {
        mSectionsPagerAdapter.addFragment(NewInsurancePolicyFragment.newInstance(1), getResources().getString(R.string.title_insurancePolicy));
    }

    /**
     * init edit car fragment
     */
    private void editCar() {
        mSectionsPagerAdapter.addFragment(NewCarFragment.newInstance(1), getResources().getString(R.string.title_vehicle));
    }

    /**
     * Set ActionBar Title
     *
     * @param heading
     */
    private void setActionBarTitle(String heading) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(heading);
            actionBar.show();
        }
    }

    /*
     * Wenn New Car dann werden zwei fragmente angezeigt
     * nextPage switch vom ersten Fragment zum zweiten
     */
    private void nextPage(Intent intent) {
        if (isEdit) {
            mVehicleModel = new VehicleModel();
            Bundle bundle = intent.getExtras();
            mVehicleModel = bundle.getParcelable(ConstBundle.BUNDLE_VEHICLE_SHARE);

            final DBHelper mDbHelper = new DBHelper(getApplicationContext());
            mDbHelper.getUpdates().updateCarInformation(mVehicleModel.getId(), mVehicleModel.createJson());

            Intent finishIntent = new Intent();
            finishIntent.putExtra(String.valueOf(ConstExtras.EXTRA_NEW_CAR), mVehicleModel.getId());
            this.setResult(Activity.RESULT_OK, finishIntent);
            finish();
        } else {
            mVehicleModel = new VehicleModel();
            if (null != intent.getExtras()) {
                Bundle bundle = intent.getExtras();
                mVehicleModel = bundle.getParcelable(ConstBundle.BUNDLE_VEHICLE_SHARE);
                // Toast.makeText(context, Objects.requireNonNull(mVehicleModel).getName(), Toast.LENGTH_SHORT).show();
                Timber.d("mVehicleModel.getName(): %s", Objects.requireNonNull(mVehicleModel).getName());
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
        }
    }

    /*
     * Wenn New Car dann werden zwei fragmente angezeigt
     * nextPage switch vom zweiten Fragment zurück zum ersten
     */
    private void previousPage() {
        if (isEdit) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();

        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    private void ClickHandler() {
        if (mCurrentService.getExtras() != null) {
            // Wenn Edit dann müssen manche Buttons anderster verwaltet werden
            // Wenn NICHT EDIT
            if (!isEdit)

                // Nur wenn new car modus
                if (Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_ADD) == ConstExtras.EXTRA_NEW_CAR) {

                    // Wenn pager zeigt zweites fragment --> dann previousPage
                    if (mViewPager.getCurrentItem() > 0) {
                        previousPage();

                        // sonst show back dialog
                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        SharedPreferencesManager pref = new SharedPreferencesManager(this);
                        MessageDialog messageDialog;
                        if (!pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                            messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitApp);
                        } else {
                            messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_backDialog);
                        }
                        messageDialog.show(fm, "MessageDialog");
                    }
                }

            // Wenn Edit --> Dann gehe zur main activity zurück (abbruch)
            if (isEdit) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * BrodcastReciver: Wird benötigt um auf Events/Clicks der ChildFragmenten reagieren zu können
     */
    public class AddReceiver extends BroadcastReceiver {
        // https://stackoverflow.com/questions/11770794/how-to-set-permissions-in-broadcast-sender-and-receiver-in-android
        // https://stackoverflow.com/questions/49197282/how-to-send-a-custom-broadcast-action-to-receivers-in-manifest
        // https://stackoverflow.com/questions/38346567/android-whats-the-meaning-of-exported-receivers-attribute
        // https://developer.android.com/reference/android/content/BroadcastReceiver.html
        @Override
        public void onReceive(Context context, Intent intent) {
            // Aktueller Intent
            if (mCurrentService.getExtras() != null) {
                if (Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_ADD) == ConstExtras.EXTRA_NEW_CAR ||
                        Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_EDIT) == ConstExtras.EXTRA_EDIT_CAR ||
                        Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_EDIT) == ConstExtras.EXTRA_EDIT_INSURANCE) {
                    // nextPage
                    if (Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_NEXT_PAGE)) {
                        nextPage(intent);
                        Timber.d("ACTION_ADD_NEXT_PAGE");
                    }

                    // UPDATE VEHICLE CAR
                    if (Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_UPDATE_VEHICLE)) {
                        Bundle bundle = intent.getExtras();
                        mVehicleModel = Objects.requireNonNull(bundle).getParcelable(ConstBundle.BUNDLE_VEHICLE_SHARE);
                        Timber.d("ACTION_ADD_UPDATE_VEHICLE");
                    }

                    // FINISH
                    if (intent.getAction().equals(ConstAction.ACTION_ADD_FINISH)) {
                        Intent finishIntent = new Intent();
                        Bundle bundle = intent.getExtras();
                        SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getApplicationContext()));

                        long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);

                        // edit modus
                        if (isEdit) {
                            if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                                Timber.d("load Car Data from id: %s", id);
                                DBHelper dbHelper = new DBHelper(getApplicationContext());
                                mVehicleModel = dbHelper.getGet().selectCarById(id);
                                mVehicleModel.setInsurancePolicyModel((InsurancePolicyModel) Objects.requireNonNull(bundle).getParcelable(ConstBundle.BUNDLE_INSURANCE_SHARE));
                                dbHelper.getUpdates().updateCarInformation(mVehicleModel.getId(), mVehicleModel.createJson());
                            }
                            // no edit modus
                        } else {
                            mVehicleModel.setInsurancePolicyModel((InsurancePolicyModel) Objects.requireNonNull(bundle).getParcelable(ConstBundle.BUNDLE_INSURANCE_SHARE));
                            Timber.d("ACTION_ADD_FINISH");
                            Timber.d(mVehicleModel.createJson());
                            final DBHelper mDbHelper = new DBHelper(getApplicationContext());
                            id = mDbHelper.getAdd().insertCar(mVehicleModel.getName(), mVehicleModel);
                            mVehicleModel.setInsurancePolicyModel((InsurancePolicyModel) Objects.requireNonNull(bundle).getParcelable(ConstBundle.BUNDLE_INSURANCE_SHARE));
                        }

                        // finish activity
                        finishIntent.putExtra(String.valueOf(ConstExtras.EXTRA_NEW_CAR), id);
                        NewCarActivity.this.setResult(Activity.RESULT_OK, finishIntent);
                        finish();
                    }
                }
            }
        }
    }
}
