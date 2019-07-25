package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.StorageImageManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.MainActivity;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewCarActivity;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link VehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleFragment extends Fragment implements View.OnClickListener {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    //
    private VehicleModel mVehicleModel;
    private LoadDataAsyncTask loadDataAsyncTask;
    // UI
    protected MaterialEditText mTextType, mTextManufacture, mTextModel,
            mTextName, mTextChassisNumber, mTextLicensePlate, mTextTankOne,
            mTextTankTwo, mTextTankCapacityOne, mTextTankCapacityTwo, mTextOdometer;
    protected Button mButtonTankOne, mButtonTankTwo;
    protected ImageView mImageType, mImageTankOne, mImageTankTwo, mImageManufacture, mImagePhoto;
    protected ImageButton mButtonPhoto, mButtonPhotoHaveImage;
    protected LinearLayout mLinearTankTwo;
    protected ScrollView mScrollView;
    private FloatingActionButton mFab;
    //
    protected boolean isTakePhoto;

    // ===========================================================
    // Constructors
    // ===========================================================
    public VehicleFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @return A new instance of fragment FuelSettingsFragment.
     */
    public static VehicleFragment newInstance() {
        VehicleFragment vehicleFragment = new VehicleFragment();
        return vehicleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(LOG_TAG);
        Timber.d("%s created", LOG_TAG);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View v = super.onCreateView(inflater,container,savedInstanceState, R.layout.fragment_vehicle);
        View v = inflater.inflate(R.layout.fragment_vehicle, container, false);

        // set UI
        mTextType = v.findViewById(R.id.text_vehicleData_type);
        mImageType = v.findViewById(R.id.image_vehicleData_type);
        mTextName = v.findViewById(R.id.text_vehicleData_name);
        mTextModel = v.findViewById(R.id.text_vehicleData_vehicleModel);
        mTextManufacture = v.findViewById(R.id.text_vehicleData_vehicleManufacture);
        mImageManufacture = v.findViewById(R.id.image_vehicleData_vehicleManufacutre);
        mTextLicensePlate = v.findViewById(R.id.text_vehicleData_vehicleLicensePlate);
        mTextChassisNumber = v.findViewById(R.id.text_vehicleData_vehicleChassisNumber);
        mTextTankOne = v.findViewById(R.id.text_vehicleData_vehicleTankOne);
        mTextTankCapacityOne = v.findViewById(R.id.text_vehicleData_vehicleTankOneCapacity);
        mImageTankOne = v.findViewById(R.id.image_vehicleData_vehicleTankOne);
        mButtonTankOne = v.findViewById(R.id.button_vehicleData_oneTank);
        mLinearTankTwo = v.findViewById(R.id.container_vehicleData_tankTwo);
        mTextTankTwo = v.findViewById(R.id.text_vehicleData_vehicleTankTwo);
        mTextTankCapacityTwo = v.findViewById(R.id.text_vehicleData_vehicleTankTwoCapacity);
        mImageTankTwo = v.findViewById(R.id.image_vehicleData_vehicleTankTwo);
        mButtonTankTwo = v.findViewById(R.id.button_vehicleData_twoTank);
        mButtonPhotoHaveImage = v.findViewById(R.id.image_vehicleData_addPhotoHaveImage);
        mImagePhoto = v.findViewById(R.id.image_vehicleData_image);
        mTextOdometer = v.findViewById(R.id.text_vehicleData_odometer);
        mFab = v.findViewById(R.id.fab);

        // Set OnClickListener
        mFab.setOnClickListener(this);

        // load Data
        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);
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
    public void onStop() {
        super.onStop();
        loadDataAsyncTask.cancel(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int myflag = ConstRequest.REQUEST_MAIN_EDIT_CAR;
        switch (v.getId()) {
            case R.id.fab:

                // start edit Car Information
                intent = new Intent(getActivity(), NewCarActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_EDIT, ConstExtras.EXTRA_EDIT_CAR);
                myflag = ConstRequest.REQUEST_MAIN_EDIT_CAR;
                break;
        }
        if (intent != null) {

            // start activity
            startActivityForResult(intent, myflag);
        } else {
            Timber.e("onItemClick");
            Toast.makeText(getActivity(), getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
            //noinspection SwitchStatementWithTooFewBranches
            switch (requestCode) {

            // reload Data
                case ConstRequest.REQUEST_MAIN_EDIT_CAR:
                    loadDataAsyncTask = new LoadDataAsyncTask();
                    loadDataAsyncTask.execute((Void) null);
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }

    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * Starts a background task, which all needed Data for Fragment
     */
    @SuppressLint("StaticFieldLeak")
    public class LoadDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Bitmap mIconPhot;
        Drawable mIconType;
        Drawable mIconManufacture;
        Drawable mIconTankOne;
        Drawable mIconTankTwo;

        @Override
        protected Boolean doInBackground(Void... voids) {
            // Load Data
            SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
            long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);

            // Load Car Information
            if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                Timber.d("load Car Data from id: %s", id);
                DBHelper dbHelper = new DBHelper(getContext());
                mVehicleModel = dbHelper.getGet().selectCarById(id);
            } else {
                return false;
            }
            // Load Photo
            try {
                if (!mVehicleModel.getPhoto().equals("")) {
                    StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(getContext()));
                    mIconPhot = storageImageManager.getImage(mVehicleModel.getPhoto());
                }
            } catch (Exception e) {
                Timber.e(e);
            }
            // Load vehicle type icon
            try {
                mIconType = new VehicleModel().getDrawable(getContext(), mVehicleModel.getType().getImageName());
            } catch (Exception e) {
                Timber.e(e);
            }

            // load other data
            mIconManufacture = new VehicleModel().getDrawable(getContext(), mVehicleModel.getManfacture().getImageName());
            mIconTankOne = new VehicleModel().getDrawable(getContext(), mVehicleModel.getTankOne().getImageName());
            if (mVehicleModel.isTanks()) {
                mIconTankTwo = new VehicleModel().getDrawable(getContext(), mVehicleModel.getTankTwo().getImageName());
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                //Todo: ERROR MESSAGE
                return;
            }
            // set UI
            mTextType.setText(mVehicleModel.getType().getName());
            mImageType.setImageDrawable(mIconType);
            mTextName.setAnimation(null);
            mTextName.setText(mVehicleModel.getName());
            mTextManufacture.setText(mVehicleModel.getManfacture().getName());
            mImageManufacture.setImageDrawable(mIconManufacture);
            mTextLicensePlate.setText(mVehicleModel.getLicensePlate());
            mTextChassisNumber.setText(mVehicleModel.getChassingNumber());
            mImageTankOne.setImageDrawable(mIconTankOne);
            mTextTankOne.setText(mVehicleModel.getTankOne().getName());
            mTextTankCapacityOne.setText(String.valueOf(mVehicleModel.getTankCapacityOne()));
            mTextOdometer.setText(String.valueOf(mVehicleModel.getOdometer()));
            if (mVehicleModel.isTanks()) {
                mImageTankTwo.setImageDrawable(mIconTankTwo);
                mImageTankTwo.setVisibility(View.VISIBLE);
                mTextTankTwo.setText(mVehicleModel.getTankTwo().getName());
                mTextTankCapacityTwo.setText(String.valueOf(mVehicleModel.getTankCapacityTwo()));
                mLinearTankTwo.setVisibility(View.VISIBLE);
            }
            if (!mVehicleModel.getPhoto().equals("") && mIconPhot != null) {
                try {
                    mImagePhoto.setImageBitmap(mIconPhot);
                    mImagePhoto.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Timber.e(e);
                }
            }
            MainActivity.setHeaderName(mVehicleModel.getName());
        }

        @Override
        protected void onCancelled() {

        }
    }
}
