package de.gruschtelapps.fh_maa_refuelpair.views.activities.add;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.MessageDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.add.NewCrashDetailsFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.add.NewCrashOtherFragment;
import de.gruschtelapps.fh_maa_refuelpair.views.fragment.add.NewCrashPhotosFragment;
import timber.log.Timber;

/*
 * Create by Eric Werner
 */
public class NewCrashActivity extends AppCompatActivity implements MessageDialog.DialogListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private DataPagerAdapter mSectionsPagerAdapter;
    private LockableViewPager mViewPager;

    // Add new Car
    private CrashModel mCrashModel;
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
        setContentView(R.layout.activity_new_crash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ActionBar
        setActionBarTitle(getResources().getString(R.string.title_new_crash));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init bzw. load data
        SharedPreferencesManager pref = new SharedPreferencesManager(this);
        mCrashModel = new CrashModel();
        mCrashModel.setIdCar(pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new DataPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter. PRE INIT
        mViewPager = findViewById(R.id.frame_add_viewPager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPagingEnabled(true);

        TabLayout tabLayout = Objects.requireNonNull(this).findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // load data
        mCurrentService = getIntent();
        if (getIntent().getExtras() != null) {

            // start new crash modus
            if (getIntent().getExtras().getInt(ConstExtras.EXTRA_KEY_ADD) == ConstExtras.EXTRA_NEW_CRASH) {
                isEdit = false;
                newCrash();

                // start edit crash modus
            } else if (getIntent().getExtras().getInt(ConstExtras.EXTRA_KEY_EDIT) == ConstExtras.EXTRA_EDIT_CRASH) {
                if (getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT) != null) {
                    isEdit = true;
                    mCrashModel = getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT);
                    mViewPager.setPagingEnabled(true);
                    editCrash();
                }
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

            // Set Broadcast filter
            IntentFilter filterRefreshUpdate = new IntentFilter();
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_NEXT_PAGE);
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_UPDATE_CRASH_OTHER);
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_UPDATE_CRASH_DETAILS);
            filterRefreshUpdate.addAction(ConstAction.ACTION_ADD_UPDATE_CRASH_PHOTOS);
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

        // unregister Broadcastreciver
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
        previousPage();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SwitchStatementWithTooFewBranches
        switch (item.getItemId()) {
            // Toolbar Button
            case android.R.id.home:
                previousPage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                previousPage();
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
            if (flag == ConstRequest.REQUEST_DIALOG_POSITIV) {
                Intent finishIntent = new Intent();
                this.setResult(Activity.RESULT_CANCELED, finishIntent);
                finish();
            }
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Init new crash fragments
     */
    private void newCrash() {
        mSectionsPagerAdapter.addFragment(NewCrashOtherFragment.newInstance(), getResources().getString(R.string.title_crashOther));
        mSectionsPagerAdapter.addFragment(NewCrashDetailsFragment.newInstance(), getResources().getString(R.string.title_crashDetails));
        mSectionsPagerAdapter.addFragment(NewCrashPhotosFragment.newInstance(), getResources().getString(R.string.title_crashPhotos));
    }

    /**
     * load edit crash fragments
     */
    private void editCrash() {
        mSectionsPagerAdapter.addFragment(NewCrashOtherFragment.newInstance(mCrashModel), getResources().getString(R.string.title_crashOther));
        mSectionsPagerAdapter.addFragment(NewCrashDetailsFragment.newInstance(mCrashModel), getResources().getString(R.string.title_crashDetails));
        mSectionsPagerAdapter.addFragment(NewCrashPhotosFragment.newInstance(mCrashModel), getResources().getString(R.string.title_crashPhotos));
    }


    private void setActionBarTitle(String heading) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(heading);
            actionBar.show();
        }
    }

    /**
     * Methode zum wechseln zur nächsten Fragment
     *
     * @param intent
     */
    private void nextPage(Intent intent) {
        // Todo:
        //
        // Crash Other
        //
        if (mViewPager.getCurrentItem() == 0) {
            mCrashModel.safeCrashOtherParty((CrashModel) intent.getParcelableExtra(ConstBundle.BUNDLE_CRASH_OTHER_SHARE));
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            //
            // Crash Details
            //
        } else if (mViewPager.getCurrentItem() == 1) {
            mCrashModel.safeCrashDetails((CrashModel) intent.getParcelableExtra(ConstBundle.BUNDLE_CRASH_DETAILS_SHARE));
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }


    /**
     * Zum wechseln zur letzten angezeigten Fragment
     */
    private void previousPage() {
        if (mViewPager.getCurrentItem() == 0) {
            FragmentManager fm = getSupportFragmentManager();
            MessageDialog messageDialog;
            if (isEdit) {
                messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitEditCrash);
            } else {
                messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitNewCrash);
            }
            messageDialog.show(fm, "MessageDialog");
            //
            //
            //
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
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
                if (Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_ADD) == ConstExtras.EXTRA_NEW_CRASH ||
                        Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_EDIT) == ConstExtras.EXTRA_EDIT_CRASH) {
                    // nextPage
                    if (Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_NEXT_PAGE)) {
                        nextPage(intent);
                        Timber.d("ACTION_ADD_NEXT_PAGE");
                    }

                    // UPDATE VEHICLE CAR
                    if (Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_UPDATE_CRASH_OTHER) ||
                            Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_UPDATE_CRASH_DETAILS) ||
                            Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_UPDATE_CRASH_PHOTOS)) {
                        nextPage(intent);
                        Timber.d("ACTION_ADD_UPDATE_VEHICLE");
                    }

                    // UPDATE VEHICLE CAR
                    if (Objects.equals(intent.getAction(), ConstAction.ACTION_ADD_FINISH)) {
                        Timber.d("BUNDLE_VEHICLE_SHARE");
                        if (mViewPager.getCurrentItem() == 2) {
                            DBHelper dbHelper = new DBHelper(getApplicationContext());
                            mCrashModel.safeCrashPhotos((CrashModel) intent.getParcelableExtra(ConstBundle.BUNDLE_CRASH_PHOTO_SHARE));
                            if (!isEdit) {
                                dbHelper.getAdd().insertAddModel(mCrashModel);
                            } else {
                                dbHelper.getUpdates().updateAdd(mCrashModel.getId(), mCrashModel.createJson(), mCrashModel.getDate());
                            }
                            finish();
                        }
                    }
                }
            }
        }
    }
}
