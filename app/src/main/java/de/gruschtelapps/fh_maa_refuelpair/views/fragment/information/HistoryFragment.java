package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.adapter.HistoryAdapter;
import de.gruschtelapps.fh_maa_refuelpair.utils.adapter.loader.HistoryItemLoader;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.JsonModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.RefuelModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.ServiceModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewCrashActivity;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewRefuelActivity;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewServiceActivity;
import timber.log.Timber;

/*
 * Create by Alea Sauer,
 * Edit by Eric Werner
 * Chance AsyncronTask to Loader
 * BugFix Loader for Historyadapter --> Possibility to display different elements
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("deprecation")
public class HistoryFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<List<JsonModel>> {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    protected VehicleModel mVehicleModel;

    private FloatingActionMenu mFabMenue;
    private FloatingActionButton mFabRefule, mFabService, mFabCrash;
    private RelativeLayout mContainerBackground;

    private RecyclerView mRecyclerView;
    private TextView mTextEmpty;
    private ProgressBar mProgressBar;
    private LoaderManager loaderManager;

    // ===========================================================
    // Constructors
    // ===========================================================
    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment HistoryFragment.
     */
    public static HistoryFragment newInstance() {
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.d("%s created", LOG_TAG);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        // Get UI
        mContainerBackground = v.findViewById(R.id.container_history_background);
        mContainerBackground.setOnClickListener(this);

        mTextEmpty = v.findViewById(R.id.text_history_empty);
        mProgressBar = v.findViewById(R.id.progress_history_list);
        mRecyclerView = v.findViewById(R.id.rv_history_recycler);

        mFabMenue = v.findViewById(R.id.fab_menue);
        mFabRefule = v.findViewById(R.id.fab_refuel);
        mFabService = v.findViewById(R.id.fab_service);
        mFabCrash = v.findViewById(R.id.fab_crash);

        // set Listener
        mFabMenue.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    mContainerBackground.setVisibility(View.VISIBLE);
                } else {
                    mContainerBackground.setVisibility(View.GONE);
                }
            }
        });

        mFabRefule.setOnClickListener(this);
        mFabService.setOnClickListener(this);
        mFabCrash.setOnClickListener(this);

        // load data
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
        loaderManager.restartLoader(0, null, this);

        return v;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
            if (resultCode == Activity.RESULT_CANCELED)
                return;
        // reload Data when return
        switch (requestCode) {
            case ConstRequest.REQUEST_MAIN_NEW_HISTORY:
                loaderManager.restartLoader(0, null, this);
                break;
            case ConstRequest.REQUEST_MAIN_EDIT_HISTORY:
                loaderManager.restartLoader(0, null, this);
                break;
        }
    }

    @NonNull
    @Override
    public Loader<List<JsonModel>> onCreateLoader(int i, @Nullable Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        return new HistoryItemLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<JsonModel>> loader, List<JsonModel> addModels) {
        updateListView(addModels);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<JsonModel>> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int myflag = ConstRequest.REQUEST_MAIN_NEW_HISTORY;
        switch (v.getId()) {

            // start add new refuel
            case R.id.fab_refuel:
                intent = new Intent(getActivity(), NewRefuelActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_ADD, ConstExtras.EXTRA_NEW_REFUEL);
                break;

            // start add new service
            case R.id.fab_service:
                intent = new Intent(getActivity(), NewServiceActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_ADD, ConstExtras.EXTRA_NEW_SERVICE);
                break;

            // start add new crash
            case R.id.fab_crash:
                intent = new Intent(getActivity(), NewCrashActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_ADD, ConstExtras.EXTRA_NEW_CRASH);
                break;

            // close FloatingActionButton menü
            case R.id.container_history_background:
                mContainerBackground.setVisibility(View.GONE);
                mFabMenue.close(true);
                break;
        }

        // start activity or show error
        if (intent != null) {
            startActivityForResult(intent, myflag);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
        }

        // close FloatingActionButton menü
        mFabMenue.close(true);
        mContainerBackground.setVisibility(View.GONE);
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
        // Wenn Liste == 0 show hint
        if (mItems.size() == 0) {
            mTextEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);

            // Wenn Liste > 0 show items
        } else {
            // Set adapter + OnItemClickListener for the individual elements
            HistoryAdapter adapter = new HistoryAdapter(getActivity(), mItems, new HistoryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, JsonModel object) {
                    Intent intent = null;
                    int myflag = ConstRequest.REQUEST_MAIN_EDIT_HISTORY;
                    //
                    // RefuelModel
                    //
                    if (object instanceof RefuelModel) {
                        RefuelModel shared = (RefuelModel) object;
                        intent = new Intent(getActivity(), NewRefuelActivity.class);
                        intent.putExtra(ConstExtras.EXTRA_KEY_EDIT, ConstExtras.EXTRA_EDIT_REFUEL);
                        intent.putExtra(ConstExtras.EXTRA_OBJECT_EDIT, shared);
                    }
                    //
                    // ServiceModel
                    //
                    if (object instanceof ServiceModel) {
                        ServiceModel shared = (ServiceModel) object;
                        intent = new Intent(getActivity(), NewServiceActivity.class);
                        intent.putExtra(ConstExtras.EXTRA_KEY_EDIT, ConstExtras.EXTRA_EDIT_SERVICE);
                        intent.putExtra(ConstExtras.EXTRA_OBJECT_EDIT, shared);
                    }
                    //
                    // CrashModel
                    //
                    if (object instanceof CrashModel) {
                        CrashModel shared = (CrashModel) object;
                        intent = new Intent(getActivity(), NewCrashActivity.class);
                        intent.putExtra(ConstExtras.EXTRA_KEY_EDIT, ConstExtras.EXTRA_EDIT_CRASH);
                        intent.putExtra(ConstExtras.EXTRA_OBJECT_EDIT, shared);
                    }

                    // start activity
                    if (intent != null) {
                        startActivityForResult(intent, myflag);
                    } else {
                        Timber.e("onItemClick");
                        Toast.makeText(getActivity(), getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Set layout manager to position the items
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // set UI --> show list, enable hint
            mTextEmpty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
