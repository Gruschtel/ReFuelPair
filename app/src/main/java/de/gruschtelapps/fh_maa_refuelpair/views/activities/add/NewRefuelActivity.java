package de.gruschtelapps.fh_maa_refuelpair.views.activities.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.FuelTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.SelectListActivity;
import timber.log.Timber;

/*
 * Create by Alea Sauer
 * Edit by Eric Werner: Date and time adjusted (leading zeros)
 */
public class NewRefuelActivity extends AppCompatActivity implements
        View.OnClickListener, DatePicker.DateListener, TimePicker.TimeListener, MessageDialog.DialogListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    // Add new Car
    private VehicleModel mVehicleModel;
    //
    private MaterialEditText mTextDate, mTextTime, mTextOdometer, mTextFuel, mTextLiter, mTextLiterCost, mTextLocal;
    private TextView mTextTotalCost;
    private FuelTypeModel mFuelType;
    private ImageView mImageTankOne;

    private boolean isEdit = false;
    private RefuelModel mRefuelModel;
    private Calendar c;

    private String mLiter, mLiterCost;
    private double mValueLiter, mValueLiterCost, mValueTotalCost;
    private int mTimeYear, mTimeMonth, mTimeDay, mTimeHour, mTimeMinute;
    private long oldOdometer;
    private Button mButtonDelete;

    // ===========================================================
    // Constructors
    // ===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_refuel);
        // ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        setActionBarTitle(getResources().getString(R.string.title_new_vehicle));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ((getIntent().getExtras() == null)) {
            Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
            finish();
        }

        // Get UI
        mTextDate = findViewById(R.id.text_newRefuel_Date);
        mTextTime = findViewById(R.id.text_newRefuel_Date2);
        mTextOdometer = findViewById(R.id.text_newRefuel_odometer);
        mTextLocal = findViewById(R.id.text_newRefuel_local);
        mTextFuel = findViewById(R.id.text_newRefuel_fuel);
        mImageTankOne = findViewById(R.id.image_newRefuel_fuel);
        mTextLiter = findViewById(R.id.text_newRefuel_costLiter);
        mTextLiterCost = findViewById(R.id.text_newRefuel_costCost);
        mTextTotalCost = findViewById(R.id.text_newRefuel_costTotalCost);
        mButtonDelete = findViewById(R.id.text_newRefuel_delete);

        mTextDate.setOnClickListener(this);
        mTextTime.setOnClickListener(this);
        mTextFuel.setOnClickListener(this);
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


        c = Calendar.getInstance();
        // Load Data or New Instance
        if (getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT) != null) {
            // LOAD
            isEdit = true;
            //  mButtonDelete.setVisibility(View.VISIBLE);
            mRefuelModel = getIntent().getParcelableExtra(ConstExtras.EXTRA_OBJECT_EDIT);
            oldOdometer = mRefuelModel.getOdometer();

            c.setTimeInMillis(mRefuelModel.getDate());

            Timber.d(String.valueOf(mRefuelModel.getDate()));
            Timber.d(String.valueOf(c.getTimeInMillis()));

            // fuelType
            mFuelType = mRefuelModel.getFuelTypeModel();


            mTextOdometer.setText(String.valueOf(mRefuelModel.getOdometer()));
            mTextLocal.setText(mRefuelModel.getLocal());
            mTextFuel.setText(String.valueOf(mFuelType.getName()));
            mImageTankOne.setImageDrawable(new JsonModel().getDrawable(getApplication(), mFuelType.getImageName()));


            mTextLiter.setText(String.valueOf((mRefuelModel.getLiter())));
            mTextLiterCost.setText(String.valueOf((mRefuelModel.getLiterCost())));
            mTextTotalCost.setText(String.valueOf((mRefuelModel.getTotalCost())));

            mValueLiter = mRefuelModel.getLiter();
            mValueLiterCost = mRefuelModel.getLiterCost();
            mValueTotalCost = mRefuelModel.getTotalCost();


        } else {

            // Toast.makeText(this, "!11111111111111", Toast.LENGTH_SHORT).show();
            // NEW
            // set Data
            mTextOdometer.setHelperText(getResources().getString(R.string.edit_helper_required) +
                    "\t\t\t\t: " +
                    String.format(getResources().getString(R.string.msg_lastOdometer), String.valueOf(mVehicleModel.getOdometer())));

            // fuel
            mImageTankOne.setImageDrawable(new JsonModel().getDrawable(getApplication(), mVehicleModel.getTankOne().getImageName()));
            mTextFuel.setText(mVehicleModel.getTankOne().getName());

            // fuelType
            mFuelType = mVehicleModel.getTankOne();

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


        mTextLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLiter = String.valueOf(s);
                calculateTotalCosts();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTextLiterCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLiterCost = String.valueOf(s);
                calculateTotalCosts();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menue_add_delete, menu);
        } else {
            getMenuInflater().inflate(R.menu.menue_add, menu);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        MessageDialog messageDialog;
        if (isEdit) {
            messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitEditRefule);
        } else {
            messageDialog = MessageDialog.newInstance(ConstRequest.REQUEST_DIALOG_BACK, R.string.title_backDialog, R.string.msg_exitNewRefuel);
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
        Intent intent = null;
        int flag = -1;
        switch (v.getId()) {
            case R.id.text_newRefuel_fuel:
                intent = new Intent(getApplication(), SelectListActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_REFUEL);
                flag = ConstRequest.REQUEST_SELECT_TANK_TYP_ONE;
                break;

            case R.id.text_newRefuel_Date:
                DialogFragment datePicker = new DatePicker();
                datePicker.show(getSupportFragmentManager(), "datePicker");
                break;

            case R.id.text_newRefuel_Date2:
                DialogFragment timePicker = new TimePicker();
                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;
        }
        if (intent != null) {
            startActivityForResult(intent, flag);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data)
            //noinspection SwitchStatementWithTooFewBranches
            switch (requestCode) {
                case ConstRequest.REQUEST_SELECT_TANK_TYP_ONE:
                    FuelTypeModel fuelTypeModelOne = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (fuelTypeModelOne != null && !fuelTypeModelOne.equals(mFuelType)) {
                        mFuelType = fuelTypeModelOne;
                        mImageTankOne.setImageDrawable(new FuelTypeModel().getDrawable(getApplicationContext(), fuelTypeModelOne.getImageName()));
                        mTextFuel.setText(fuelTypeModelOne.getName());
                        Timber.d(fuelTypeModelOne.getId() + " - " + fuelTypeModelOne.getName() + " - " + fuelTypeModelOne.getImageName());
                    }
                    break;
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Toolbar Button
            case android.R.id.home:
                // Aktuller Service
                onBackPressed();
                return true;
            case R.id.menue_add_finish:
                if (checkUI()) {
                    Calendar dateTime = Calendar.getInstance();
                    dateTime.set(mTimeYear, mTimeMonth, mTimeDay, mTimeHour, mTimeMinute);

                    final DBHelper mDbHelper = new DBHelper(getApplicationContext());
                    if (isEdit) {
                        RefuelModel shareModel = new RefuelModel(
                                mRefuelModel.getId(),
                                JsonModel.ADD_TYPE_REFUEL,
                                mVehicleModel.getId(),
                                mFuelType,
                                Long.parseLong(String.valueOf(mTextOdometer.getText())),
                                String.valueOf(mTextLocal.getText()),
                                mValueLiter,
                                mValueLiterCost,
                                mValueTotalCost,
                                dateTime.getTimeInMillis()
                        );
                        mDbHelper.getUpdates().updateAdd(mRefuelModel.getId(), shareModel.createJson(), shareModel.getDate());


                        long newOdometer = Long.parseLong(String.valueOf(mTextOdometer.getText())) - mRefuelModel.getOdometer();
                        mVehicleModel.setOdometer(mVehicleModel.getOdometer() + newOdometer);
                        mDbHelper.getUpdates().updateCarInformation(mVehicleModel.getId(), mVehicleModel.createJson());
                    } else {
                        RefuelModel shareModel = new RefuelModel(
                                JsonModel.ADD_TYPE_REFUEL,
                                mVehicleModel.getId(),
                                mFuelType,
                                Long.parseLong(String.valueOf(mTextOdometer.getText())),
                                String.valueOf(mTextLocal.getText()),
                                mValueLiter,
                                mValueLiterCost,
                                mValueTotalCost,
                                dateTime.getTimeInMillis()
                        );

                        mDbHelper.getAdd().insertAddModel(shareModel);
                        mVehicleModel.setOdometer(mVehicleModel.getOdometer() + shareModel.getOdometer());
                        mDbHelper.getUpdates().updateCarInformation(mVehicleModel.getId(), mVehicleModel.createJson());

                        Intent finishIntent = new Intent();
                        this.setResult(Activity.RESULT_OK, finishIntent);
                    }

                    finish();
                }
                return true;
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
        if (action == ConstRequest.REQUEST_DIALOG_BACK) {
            if (flag == ConstRequest.REQUEST_DIALOG_POSITIV) {
                Intent finishIntent = new Intent();
                this.setResult(Activity.RESULT_CANCELED, finishIntent);
                finish();
            }
        } else if (action == ConstRequest.REQUEST_DIALOG_DELETE) {
            if (flag == ConstRequest.REQUEST_DIALOG_POSITIV) {
                final DBHelper mDbHelper = new DBHelper(getApplicationContext());
                mDbHelper.getDelete().deleteItem(mRefuelModel.getId());
                mVehicleModel.setOdometer(mVehicleModel.getOdometer() - mRefuelModel.getOdometer());
                mDbHelper.getUpdates().updateCarInformation(mVehicleModel.getId(), mVehicleModel.createJson());
                Intent finishIntent = new Intent();
                setResult(Activity.RESULT_OK, finishIntent);
                finish();
            }
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void setActionBarTitle(String heading) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(heading);
            actionBar.show();
        }
    }

    private boolean checkUI() {
        boolean allCorrekt = true;
        //

        if (String.valueOf(mTextOdometer.getText()).isEmpty()) {
            allCorrekt = false;
            mTextOdometer.setError(getResources().getString(R.string.error_refuelOdometer));
        }
        if (!String.valueOf(mTextOdometer.getText()).isEmpty()) {
            if (Long.parseLong(String.valueOf(mTextOdometer.getText())) < 0) {
                allCorrekt = false;
                mTextOdometer.setError(getResources().getString(R.string.error_refuelOdometer2));
            }
        }
        if (String.valueOf(mTextLiter.getText()).isEmpty() || Double.parseDouble(String.valueOf(mTextLiter.getText())) <= 0) {
            allCorrekt = false;
            mTextLiter.setError(getResources().getString(R.string.error_liter));
        }
        if (String.valueOf(mTextLiterCost.getText()).isEmpty() || Double.parseDouble(String.valueOf(mTextLiterCost.getText())) <= 0) {
            allCorrekt = false;
            mTextLiterCost.setError(getResources().getString(R.string.error_liter));
        }
        return allCorrekt;
    }

    @SuppressLint("SetTextI18n")
    private void calculateTotalCosts() {
        double liter, costLiter;
        if (mLiter == null || mLiter.equals("")) {
            mValueLiter = liter = 1;
        } else {
            mValueLiter = liter = Double.parseDouble(mLiter);
        }

        if (mLiterCost == null || mLiterCost.equals("")) {
            mValueLiterCost = costLiter = 1;
        } else {
            mValueLiterCost = costLiter = Double.parseDouble(mLiterCost);
        }

        if ((mLiter == null || mLiter.equals("")) && (mLiterCost == null || mLiterCost.equals(""))) {
            mTextTotalCost.setText("");
        } else {
            mValueTotalCost = mValueLiterCost * mValueLiter;
            NumberFormat nf = new DecimalFormat("##.##");
            if (costLiter > 0 && liter > 0)
                mTextTotalCost.setText(String.format(getResources().getString(R.string.value_money),
                        String.valueOf(nf.format(mValueTotalCost))));
        }
    }

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
