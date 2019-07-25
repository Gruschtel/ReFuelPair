package de.gruschtelapps.fh_maa_refuelpair.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter.AddItemAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.CarTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.FuelTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.ManufactureModel;

/*
 * Create by Eric Werner
 */
@SuppressWarnings("deprecation")
public class SelectListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<JsonModel>> {
    // https://developer.android.com/guide/components/loaders
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    // Model
    private RecyclerView recyclerView;
    private Activity mContext;

    // ===========================================================
    // Constructors
    // ===========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ActionBar
        setContentView(R.layout.activity_select_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ActionBar settings
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Floating action bar
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Lookup the recyclerview in activity layout

        mContext = this;

        if (getIntent().getExtras() != null) {
            switch (getIntent().getExtras().getInt(ConstExtras.EXTRA_KEY_SELECT)) {
                case ConstExtras.EXTRA_SELECT_CAR:

                    // adapter = new AddItemAdapter(new CarTypeModel().loadModels(this), this);
                    // recyclerView.setHasFixedSize(true);
                    break;
                case ConstExtras.EXTRA_SELECT_REFUEL:

                    // adapter = new AddItemAdapter(new FuelTypeModel().loadModels(this), this);
                    // recyclerView.setHasFixedSize(true);
                    break;
                case ConstExtras.EXTRA_SELECT_SERVICE:
                    break;
                case ConstExtras.EXTRA_SELECT_MANUFACTURE:

                    // adapter = new AddItemAdapter(new ManufactureModel().loadModels(this), this);
                    // recyclerView.setHasFixedSize(true);
                    break;
                default:
                    Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
            finish();
        }
        getLoaderManager().restartLoader(0, null, this);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SwitchStatementWithTooFewBranches
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Loads items into a ListView and then displays them
     *
     * @param mItems
     */
    @SuppressWarnings("JavaDoc")
    private void updateListView(List<JsonModel> mItems) {
        AddItemAdapter adapter = new AddItemAdapter(mItems, this);
        // Set layout manager to position the items
        ((RecyclerView) findViewById(R.id.rv_selectList)).setAdapter(adapter);
        ((RecyclerView) findViewById(R.id.rv_selectList)).setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.ll_RecyclerViewContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.progress_selectItem_list).setVisibility(View.GONE);
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Loader der das ListView Element Aktualliseren soll, sobald sich etwas in der DB bzgl. der Testbogen verändert hat
     * z.B. kann dies das erstellen oder Löschen eines Testbogens sein
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<JsonModel>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<JsonModel>>(this) {
            {
                onContentChanged();
            }

            @Override
            protected void onStartLoading() {
                if (takeContentChanged()) {
                    forceLoad();
                    findViewById(R.id.ll_RecyclerViewContainer).setVisibility(View.GONE);
                    findViewById(R.id.progress_selectItem_list).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public List<JsonModel> loadInBackground() {

                // load needed data from json file
                switch (Objects.requireNonNull(getIntent().getExtras()).getInt(ConstExtras.EXTRA_KEY_SELECT)) {

                    // load car type
                    case ConstExtras.EXTRA_SELECT_CAR:
                        return new CarTypeModel().loadModels(mContext);

                    // load refuel type
                    case ConstExtras.EXTRA_SELECT_REFUEL:
                        return new FuelTypeModel().loadModels(mContext);

                    // load manufacture data
                    case ConstExtras.EXTRA_SELECT_MANUFACTURE:
                        return new ManufactureModel().loadModels(mContext);

                    case ConstExtras.EXTRA_SELECT_SERVICE:
                        return new ManufactureModel().loadModels(mContext);
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<JsonModel>> loader, List<JsonModel> data) {
        updateListView(data);
    }

    @Override
    public void onLoaderReset(Loader<List<JsonModel>> loader) {
        ((RecyclerView) findViewById(R.id.rv_selectList)).setAdapter(null);
    }
}
