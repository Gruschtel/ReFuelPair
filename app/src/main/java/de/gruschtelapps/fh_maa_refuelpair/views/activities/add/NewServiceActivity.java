package de.gruschtelapps.fh_maa_refuelpair.views.activities.add;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.DatePicker;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.MessageDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.TimePicker;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;

/*
 * Create by Alea Sauer
 * Edit by Eric Werner: Date and time adjusted (leading zeros)
 */
public class NewServiceActivity extends AppCompatActivity implements DatePicker.DateListener, TimePicker.TimeListener, View.OnClickListener, MessageDialog.DialogListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    // Add new Car
    private VehicleModel mVehicleModel;
    //
    private MaterialEditText mTextDate, mTextTime, mTextOdometer, mTestService, mTextLocal, mTextTotalCost;

    private boolean isEdit = false;
    private ServiceModel mServiceModel;
    Calendar c;
    private double mValueTotalCost;

    private int mTimeYear, mTimeMonth, mTimeDay, mTimeHour, mTimeMinute;
    private Button mButtonDelete;

    // ===========================================================
    // Constructors
    // ===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service);
        // ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        setActionBarTitle(getResources().getString(R.string.title_new_service));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ((getIntent().getExtras() == null)) {
            Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
            finish();
        }

        // Get UI
        mTextDate = findViewById(R.id.text_newService_Date);
        mTextTime = findViewById(R.id.text_newService_Date2);
        mTextOdometer = findViewById(R.id.text_newService_odometer);
        mTextLocal = findViewById(R.id.text_newService_local);
        mTextTotalCost = findViewById(R.id.text_newService_serviceCost);
        mTestService = findViewById(R.id.text_newService_service);
        mButtonDelete = findViewById(R.id.text_newService_delete);

        // set onClickListener
        mTextDate.setOnClickListener(this);
        mTextTime.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);

        // Get Model
        SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getApplication()));
        long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);
        if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
            DBHelper dbHelper = new DBHelper(getApplication());
            mVehicleModel = dbHelper.getGet().selectCarById(id);
        } else {
            Timber.e("NewRefuelActivity");
            Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
            finish();
        }

        //
        c = Calendar.getInstance();
        // Load Data or New Instance
        if (getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT) != null) {

            isEdit = true;

            // mButtonDelete.setVisibility(View.VISIBLE);
            mServiceModel = getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT);

            // Get Time
            c.setTimeInMillis(mServiceModel.getDate());

            Timber.d(String.valueOf(mServiceModel.getDate()));
            Timber.d(String.valueOf(c.getTimeInMillis()));

            // set loaded data
            mTextOdometer.setText(mServiceModel.getOdometer() != ConstError.ERROR_LONG ? String.valueOf(mServiceModel.getOdometer()) : "");
            mTextLocal.setText(mServiceModel.getLocal());

            mTextTotalCost.setText(String.valueOf(mServiceModel.getTotalCost()));
            mValueTotalCost = mServiceModel.getTotalCost();

            mTestService.setText(mServiceModel.getService());
        }

        // date
        String year = setNullVorne(c.get(Calendar.YEAR), 4);
        String month = setNullVorne(c.get(Calendar.MONTH), 2);
        String day = setNullVorne(c.get(Calendar.DAY_OF_MONTH), 2);

        // time
        String hour = setNullVorne(c.get(Calendar.HOUR), 2);
        String minute = setNullVorne(c.get(Calendar.MINUTE), 2);

        mTextDate.setText(String.format(getResources().getString(R.string.value_date),
                year,
                month,
                day));
        Timber.d(String.valueOf(c.getTimeInMillis()));

        mTextTime.setText(String.format(getResources().getString(R.string.value_time),
                hour,
                minute));

        // save time
        mTimeYear = c.get(Calendar.YEAR);
        mTimeMonth = c.get(Calendar.MONTH);
        mTimeDay = c.get(Calendar.DAY_OF_MONTH);
        mTimeHour = c.get(Calendar.HOUR_OF_DAY);
        mTimeMinute = c.get(Calendar.MINUTE);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // if edit than delete icon
        // if new than finish icon
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menue_add_delete, menu);
        } else {
            getMenuInflater().inflate(R.menu.menue_add, menu);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        // Show dialog when press back
        FragmentManager fm = getSupportFragmentManager();
        MessageDialog messageDialog;
        if (isEdit) {
            messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitEditService);
        } else {
            messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitNewService);
        }
        messageDialog.show(fm, "MessageDialog");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // show date picker
            case R.id.text_newService_Date:
                DialogFragment datePicker = new DatePicker();
                datePicker.show(getSupportFragmentManager(), "datePicker");
                break;

            // show time picker
            case R.id.text_newService_Date2:
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;

            // delete service
            case R.id.text_newService_delete:
                MessageDialog messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_DELETE, R.string.title_button_delete, R.string.msg_button_delete);
                messageDialog.show(getSupportFragmentManager(), "messageDialog");
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Toolbar Button
            case android.R.id.home:

                // Aktueller Service
                onBackPressed();
                return true;
            case R.id.menue_add_finish:

                // Check zunächst ob alle benötigten Felder ausgefüllt sind
                if (checkUI()) {
                    double totalcost;
                    if (!String.valueOf(mTextTotalCost.getText()).equals("")) {
                        totalcost = Double.parseDouble(String.valueOf(mTextTotalCost.getText()));
                    } else {
                        totalcost = 0;
                    }
                    String message = String.valueOf(mTestService.getText()).replaceAll("\\\\n", "\n");

                    // get time
                    Calendar dateTime = Calendar.getInstance();
                    dateTime.set(mTimeYear, mTimeMonth, mTimeDay, mTimeHour, mTimeMinute);

                    // get & set odometer
                    long mValueOdometer = 0;
                    if (String.valueOf(mTextOdometer.getText()).isEmpty()) {
                        mValueOdometer = ConstError.ERROR_LONG;
                    } else {
                        mValueOdometer = Long.parseLong(String.valueOf(mTextOdometer.getText()));
                    }

                    // Get Database
                    final DBHelper mDbHelper = new DBHelper(getApplicationContext());

                    // Wenn Edit Modus dann objekt updaten
                    if (isEdit) {
                        ServiceModel shareModel = new ServiceModel(
                                mServiceModel.getId(),
                                JsonModel.ADD_TYPE_SERVICE,
                                mVehicleModel.getId(),
                                mValueOdometer,
                                String.valueOf(mTextLocal.getText()),
                                totalcost,
                                dateTime.getTimeInMillis(),
                                message
                        );

                        // load data
                        mDbHelper.getUpdates().updateAdd(mServiceModel.getId(), shareModel.createJson(), shareModel.getDate());
                        Intent finishIntent = new Intent();
                        this.setResult(Activity.RESULT_OK, finishIntent);

                        // Sonst Objekt neu erstellen
                    } else {
                        ServiceModel shareModel = new ServiceModel(
                                JsonModel.ADD_TYPE_SERVICE,
                                mVehicleModel.getId(),
                                mValueOdometer,
                                String.valueOf(mTextLocal.getText()),
                                totalcost,
                                dateTime.getTimeInMillis(),
                                message
                        );
                        mDbHelper.getAdd().insertAddModel(shareModel);
                        Intent finishIntent = new Intent();
                        this.setResult(Activity.RESULT_OK, finishIntent);
                    }

                    finish();
                }
                return true;

            // delete Item
            case R.id.menue_add_delete:
                MessageDialog messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_DELETE, R.string.title_button_delete, R.string.msg_button_delete);
                messageDialog.show(getSupportFragmentManager(), "messageDialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateDialogClick(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        mTextDate.setText(String.format(getResources().getString(R.string.value_date),
                setNullVorne(year, 2),
                setNullVorne(month, 2),
                setNullVorne(dayOfMonth, 2)));
        // save time
        mTimeYear = year;
        mTimeMonth = month;
        mTimeDay = dayOfMonth;
    }

    @Override
    public void onTimeDialogClick(android.widget.TimePicker view, int hourOfDay, int minute) {
        mTextTime.setText(String.format(getResources().getString(R.string.value_time),
                setNullVorne(hourOfDay, 2),
                setNullVorne(minute, 2)));
        mTimeHour = hourOfDay;
        mTimeMinute = minute;
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


            // handle delete dialog
        } else if (action == ConstRequest.REQUEST_DIALOG_DELETE) {
            if (flag == ConstRequest.REQUEST_DIALOG_POSITIV) {
                final DBHelper mDbHelper = new DBHelper(getApplicationContext());
                mDbHelper.getDelete().deleteItem(mServiceModel.getId());
                Intent finishIntent = new Intent();
                setResult(Activity.RESULT_OK, finishIntent);
                finish();
            }
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * set actionbar title
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

    /**
     * Check ob alle TextViews korrekt ausgefüllt wurden
     *
     * @return
     */
    private boolean checkUI() {
        boolean allCorrekt = true;
        if (!String.valueOf(mTextOdometer.getText()).isEmpty()) {
            if (Long.parseLong(String.valueOf(mTextOdometer.getText())) < 0) {
                allCorrekt = false;
                mTextOdometer.setError(getResources().getString(R.string.error_refuelOdometer2));
            }
        }
        return allCorrekt;
    }


    /**
     * Füllt einem Wert mit 0 der länge "leange" auf
     *
     * @param zahl
     * @param laenge
     * @return
     */
    public static String setNullVorne(int zahl, int laenge) {
        StringBuilder tmp = new StringBuilder(Integer.toString(zahl));
        int leng = tmp.length();
        if (leng < laenge) {
            for (; leng < laenge; leng++) {
                tmp.insert(0, "0");
            }
        }
        return tmp.toString();
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
