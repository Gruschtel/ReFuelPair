package de.gruschtelapps.fh_maa_refuelpair.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import timber.log.Timber;
/*
 * Create by Alea Sauer
 */
public class FuelSettingActivity extends AppCompatActivity {
    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Fields
    // ===========================================================
    private Spinner mSpinnerSprit, mSpinnerSort;
    private SeekBar mSeekBar;
    private TextView mTextSeekBarValue;

    private float mDistance;
    private String mSort, mType;

    // ===========================================================
    // Constructors
    // ===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.title_fuel_setting));
            actionBar.show();
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Load Prefs
        final SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getApplicationContext()));
        mDistance = pref.getPrefFloat(ConstPreferences.PREF_COMPARISON_DISTANC);
        if (Float.compare(mDistance, ConstError.ERROR_FLOAT) == 0)
            mDistance = 2.5f;
        mSort = pref.getPrefString(ConstPreferences.PREF_COMPARISON_SORT);
        if (mSort.equals(ConstError.ERROR_STRING))
            mSort = "dist";
        mType = pref.getPrefString(ConstPreferences.PREF_COMPARISON_TYPE);

        // Get UI
        mSpinnerSprit = findViewById(R.id.spinner_fuellSetting_spritsorte);
        mSpinnerSort = findViewById(R.id.spinner_fuellSetting_sortierung);
        mSeekBar = findViewById(R.id.seekBar_fuellSetting);
        mTextSeekBarValue = findViewById(R.id.text_seekerbar_value);

        // Set UI
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fuel_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSprit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.d(String.valueOf(position));
                switch (position) {
                    case 0:
                        pref.setPrefString(ConstPreferences.PREF_COMPARISON_TYPE, "diesel");
                        break;
                    case 1:
                        pref.setPrefString(ConstPreferences.PREF_COMPARISON_TYPE, "e5");
                        break;
                    case 2:
                        pref.setPrefString(ConstPreferences.PREF_COMPARISON_TYPE, "e10");
                        break;
                }
                // Toast.makeText(getApplicationContext(), mSortierung[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerSprit.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_sort = ArrayAdapter.createFromResource(this, R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter_sort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        pref.setPrefString(ConstPreferences.PREF_COMPARISON_SORT, "dist");

                        break;
                    case 1:
                        pref.setPrefString(ConstPreferences.PREF_COMPARISON_SORT, "price");

                        break;
                }
                //Toast.makeText(getApplicationContext(), mSortierung[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerSort.setAdapter(adapter_sort);


        mTextSeekBarValue.setText(String.valueOf((int)mDistance));
        mSeekBar.setMax(24);

        mSeekBar.setProgress((int)mDistance);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue+1;
                mTextSeekBarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mTextSeekBarValue.setText(String.valueOf(progress));
                pref.setPrefFloat(ConstPreferences.PREF_COMPARISON_DISTANC, getConvertedValue(progress));
            }
        });

        mSpinnerSprit.post(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case "diesel":
                        mSpinnerSprit.setSelection(0);
                        break;
                    case "e5":
                        mSpinnerSprit.setSelection(1);
                        break;
                    case "e10":
                        mSpinnerSprit.setSelection(2);
                        break;
                }
            }
        });

        mSpinnerSort.post(new Runnable() {
            @Override
            public void run() {
                switch (mSort) {
                    case "dist":
                        mSpinnerSort.setSelection(0);
                        break;
                    case "price":
                        mSpinnerSort.setSelection(1);
                        break;
                }
            }
        });


    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
                Intent finishIntent = new Intent();
                setResult(Activity.RESULT_OK, finishIntent);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    // ===========================================================
    // Methods
    // ===========================================================
    public float getConvertedValue(int intVal){
        float floatVal = 0.0f;
        floatVal = 1f * intVal;
        return floatVal;
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
