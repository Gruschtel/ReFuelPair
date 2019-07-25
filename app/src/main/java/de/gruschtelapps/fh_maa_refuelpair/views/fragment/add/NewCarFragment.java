package de.gruschtelapps.fh_maa_refuelpair.views.fragment.add;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstAction;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.ItemListDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.StorageImageManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.CarTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.FuelTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.ManufactureModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.SelectListActivity;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NewCrashOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCarFragment extends Fragment implements View.OnClickListener, ItemListDialog.DialogListener {
    // Fragments/ViewPager
    // https://stackoverflow.com/questions/30721664/android-toolbar-adding-menu-items-for-different-fragments
    // https://stackoverflow.com/questions/42302656/communication-objects-between-multiple-fragments-in-viewpager
    // Camera
    // https://developer.android.com/training/camera/photobasics#java
    // Dialoge
    // https://developer.android.com/guide/topics/ui/dialogs

    // ===========================================================
    // Constants
    // ===========================================================
    final String pattern_TankCapacity = "^([0-9]+(.[0-9]+)?)";

    private CarTypeModel mCarTypeModel;
    private FuelTypeModel mFuelTypeModelOne;
    private FuelTypeModel mFuelTypeModelTwo;
    private ManufactureModel mManufactureModel;
    private String mSaveImage;
    // UI
    private MaterialEditText mTextType, mTextManufacture, mTextModel, mTextName,
            mTextChassisNumber, mTextLicensePlate, mTextTankTypeOne, mTextTankTypeTwo,
            mTextOdometer, mTextTankCapacityOne, mTextTankCapacityTwo;
    private Button mButtonTankOne, mButtonTankTwo;
    private ImageView mImageType, mImageTankOne, mImageTankTwo, mImageManufacture, mImagePhoto;
    private ImageButton mButtonPhoto, mButtonPhotoHaveImage;
    private LinearLayout mLinearTankTwo;
    private ScrollView mScrollView;
    //
    private boolean isTakePhoto;

    // ===========================================================
    // Fields
    // ===========================================================
    private int flag;
    private VehicleModel mVehicleModel;

    public NewCarFragment() {
        // Required empty public constructor
    }

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehicleFragment.
     */
    public static NewCarFragment newInstance() {
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return new NewCarFragment();
    }

    public static NewCarFragment newInstance(int i) {
        NewCarFragment fragment = new NewCarFragment();
        Bundle args = new Bundle();
        args.putInt("flag", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flag = getArguments().getInt("flag");
        }
        // ...
        Timber.d("%s created", getClass().getSimpleName());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_vehicle, container, false);
        // Get UI
        mTextType = v.findViewById(R.id.text_addCar_type);
        mImageType = v.findViewById(R.id.image_addCar_type);
        mTextName = v.findViewById(R.id.text_addCar_name);
        mTextModel = v.findViewById(R.id.text_addCar_vehicleModel);
        mTextManufacture = v.findViewById(R.id.text_addCar_vehicleManufacture);
        mImageManufacture = v.findViewById(R.id.image_addCar_vehicleManufacutre);
        mTextLicensePlate = v.findViewById(R.id.text_addCar_vehicleLicensePlate);
        mTextChassisNumber = v.findViewById(R.id.text_addCar_vehicleChassisNumber);
        mTextTankTypeOne = v.findViewById(R.id.text_addCar_vehicleTankOne);
        mTextTankCapacityOne = v.findViewById(R.id.text_addCar_vehicleTankOneCapacity);
        mImageTankOne = v.findViewById(R.id.image_addCar_vehicleTankOne);
        mButtonTankOne = v.findViewById(R.id.button_addCar_oneTank);
        mLinearTankTwo = v.findViewById(R.id.container_addCar_tankTwo);
        mTextTankTypeTwo = v.findViewById(R.id.text_addCar_vehicleTankTwo);
        mTextTankCapacityTwo = v.findViewById(R.id.text_addCar_vehicleTankTwoCapacity);
        mImageTankTwo = v.findViewById(R.id.image_addCar_vehicleTankTwo);
        mButtonTankTwo = v.findViewById(R.id.button_addCar_twoTank);
        mScrollView = v.findViewById(R.id.scrollbar_addCar);
        mButtonPhoto = v.findViewById(R.id.button_addCar_addPhoto);
        mButtonPhotoHaveImage = v.findViewById(R.id.image_addCar_addPhotoHaveImage);
        mImagePhoto = v.findViewById(R.id.image_addCar_image);
        mTextOdometer = v.findViewById(R.id.text_addCar_odometer);

        // set OnnCLickListener
        mTextType.setOnClickListener(this);
        mTextManufacture.setOnClickListener(this);
        mTextTankTypeOne.setOnClickListener(this);
        mTextTankTypeTwo.setOnClickListener(this);
        mButtonTankOne.setOnClickListener(this);
        mButtonTankTwo.setOnClickListener(this);
        mButtonPhoto.setOnClickListener(this);
        mButtonPhotoHaveImage.setOnClickListener(this);

        // Standard Objects
        mCarTypeModel = (CarTypeModel) new CarTypeModel().loadModelById(getContext(), 0);
        mFuelTypeModelOne = (FuelTypeModel) new FuelTypeModel().loadModelById(getContext(), 5);
        mFuelTypeModelTwo = (FuelTypeModel) new FuelTypeModel().loadModelById(getContext(), 5);
        mManufactureModel = (ManufactureModel) new ManufactureModel().loadModelById(getContext(), 0);
        // Standard vehicle

        // set UI
        mImageType.setImageDrawable(new CarTypeModel().getDrawable(getContext(), mCarTypeModel.getImageName()));
        mTextType.setText(mCarTypeModel.getName());

        mImageTankOne.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), mFuelTypeModelOne.getImageName()));
        mTextTankTypeOne.setText(mFuelTypeModelOne.getName());

        mImageTankTwo.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), mFuelTypeModelTwo.getImageName()));

        mTextTankTypeTwo.setText(mFuelTypeModelTwo.getName());

        // init variables
        isTakePhoto = false;


        // load Data if necessary
        if (flag == 1) {
            Bitmap mIconPhot = null;
            Drawable mIconType = null;
            Drawable mIconManufacture = null;
            Drawable mIconTankOne = null;
            Drawable mIconTankTwo = null;

            // preparation preference manager
            SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
            long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);

            // Check car id and load data
            if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                Timber.d("load Car Data from id: %s", id);
                DBHelper dbHelper = new DBHelper(getContext());
                mVehicleModel = dbHelper.getGet().selectCarById(id);
            }

            // get photo
            try {
                if (!mVehicleModel.getPhoto().equals("")) {
                    StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(getContext()));
                    mIconPhot = storageImageManager.getImage(mVehicleModel.getPhoto());
                }
            } catch (Exception e) {
                Timber.e(e);
            }

            //  get Vehicle icon
            try {
                mIconType = new VehicleModel().getDrawable(getContext(), mVehicleModel.getType().getImageName());
            } catch (Exception e) {
                Timber.e(e);
            }

            //  get vehicle data
            mIconManufacture = new VehicleModel().getDrawable(getContext(), mVehicleModel.getManfacture().getImageName());
            mIconTankOne = new VehicleModel().getDrawable(getContext(), mVehicleModel.getTankOne().getImageName());
            if (mVehicleModel.isTanks()) {
                mIconTankTwo = new VehicleModel().getDrawable(getContext(), mVehicleModel.getTankTwo().getImageName());
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
            mTextTankTypeOne.setText(mVehicleModel.getTankOne().getName());
            mTextTankCapacityOne.setText(String.valueOf(mVehicleModel.getTankCapacityOne()));
            mTextOdometer.setText(String.valueOf(mVehicleModel.getOdometer()));
            // set second tank
            if (mVehicleModel.isTanks()) {
                mImageTankTwo.setImageDrawable(mIconTankTwo);
                mImageTankTwo.setVisibility(View.VISIBLE);
                mTextTankTypeTwo.setText(mVehicleModel.getTankTwo().getName());
                mTextTankCapacityTwo.setText(String.valueOf(mVehicleModel.getTankCapacityTwo()));
                mLinearTankTwo.setVisibility(View.VISIBLE);
            }
            // set photo
            if (!mVehicleModel.getPhoto().equals("") && mIconPhot != null) {
                try {
                    mImagePhoto.setImageBitmap(mIconPhot);
                    mImagePhoto.setVisibility(View.VISIBLE);
                    mButtonPhotoHaveImage.setVisibility(View.VISIBLE);
                    mButtonPhoto.setVisibility(View.GONE);
                    isTakePhoto = true;
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
        return v;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        // if edit than delete icon
        // if new than finish icon
        if (flag == 1) {
            inflater.inflate(R.menu.menue_add, menu);
        } else {
            inflater.inflate(R.menu.menue_add2, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int flag = -1;
        switch (v.getId()) {

            // start selectedList activity (car type)
            case R.id.text_addCar_type:
                intent = new Intent(getActivity(), SelectListActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_CAR);
                flag = ConstRequest.REQUEST_SELECT_CAR_TYP;
                break;

            // start selectedList activity (car manufacture)
            case R.id.text_addCar_vehicleManufacture:
                intent = new Intent(getActivity(), SelectListActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_MANUFACTURE);
                flag = ConstRequest.REQUEST_SELECT_MANUFACTURE;
                break;

            // start selectedList activity (tanke one type)
            case R.id.text_addCar_vehicleTankOne:
                intent = new Intent(getActivity(), SelectListActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_REFUEL);
                flag = ConstRequest.REQUEST_SELECT_TANK_TYP_ONE;
                break;

            // start selectedList activity (tank two type)
            case R.id.text_addCar_vehicleTankTwo:
                intent = new Intent(getActivity(), SelectListActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_REFUEL);
                flag = ConstRequest.REQUEST_SELECT_TANK_TYP_TWO;
                break;

            // toogle between one and two tanks
            // show one tank
            case R.id.button_addCar_oneTank:
                mLinearTankTwo.setVisibility(View.GONE);
                mImageTankTwo.setVisibility(View.GONE);
                mButtonTankOne.setBackgroundTintList(ContextCompat.getColorStateList(Objects.requireNonNull(getActivity()), R.color.colorSecondary));
                mButtonTankTwo.setBackgroundTintList(ContextCompat.getColorStateList(Objects.requireNonNull(getActivity()), R.color.colorGrayE6));
                break;

            // toogle between one and two tanks
            // show two tanks
            case R.id.button_addCar_twoTank:
                mLinearTankTwo.setVisibility(View.VISIBLE);
                mImageTankTwo.setVisibility(View.VISIBLE);
                mButtonTankOne.setBackgroundTintList(ContextCompat.getColorStateList(Objects.requireNonNull(getActivity()), R.color.colorGrayE6));
                mButtonTankTwo.setBackgroundTintList(ContextCompat.getColorStateList(Objects.requireNonNull(getActivity()), R.color.colorSecondary));
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                break;

            // add car photo
            case R.id.image_addCar_addPhotoHaveImage:
            case R.id.button_addCar_addPhoto:
                confirmPhotoDialog();
                break;
        }

        // if intent != null start new activity
        if (intent != null) {
            startActivityForResult(intent, flag);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (item.getItemId()) {
            //
            case R.id.menue_add_finish:
                if (checkUI()) {
                    // Car
                    VehicleModel mVehicleShare = new VehicleModel();
                    if (flag == 1) {
                        mVehicleShare.setId(mVehicleModel.getId());
                    }
                    // safe data
                    String name = mTextName.getText() + "";
                    mVehicleShare.setName(name);
                    Timber.d("mVehicleShare.getName(): %s", mVehicleShare.getName());
                    Timber.d("name: %s", name);
                    Timber.d("mTextName.getText(): %s", mTextName.getText());
                    mVehicleShare.setType(mCarTypeModel);
                    mVehicleShare.setManfacture(mManufactureModel);
                    mVehicleShare.setModel(String.valueOf(mTextModel.getText()));
                    mVehicleShare.setChassingNumber(String.valueOf(mTextChassisNumber.getText()));
                    mVehicleShare.setLicensePlate(String.valueOf(mTextLicensePlate.getText()));
                    mVehicleShare.setTankOne(mFuelTypeModelOne);
                    mVehicleShare.setCapacityOne(Double.parseDouble(String.valueOf(mTextTankCapacityOne.getText())));
                    NumberFormat nf = new DecimalFormat("######");
                    mVehicleShare.setOdometer(Integer.parseInt(nf.format(Double.parseDouble(String.valueOf(mTextOdometer.getText())))));
                    if (isTakePhoto) {
                        mVehicleShare.setPhoto(mSaveImage);
                    } else {
                        mVehicleShare.setPhoto("");
                    }
                    if (mLinearTankTwo.getVisibility() == View.VISIBLE) {
                        Timber.d("Two Tanks true");
                        mVehicleShare.setTankTwo(mFuelTypeModelTwo);
                        mVehicleShare.setCapacityTwo(Double.parseDouble(String.valueOf(mTextTankCapacityTwo.getText())));
                        mVehicleShare.setTanks(true);
                    }

                    // send broadcast to parent activity with car data
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ConstBundle.BUNDLE_VEHICLE_SHARE, mVehicleShare);
                    intent.putExtras(bundle);
                    intent.setAction(ConstAction.ACTION_ADD_NEXT_PAGE);
                    Objects.requireNonNull(getContext()).sendBroadcast(intent);

                }
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data)
            switch (requestCode) {

                // get car type data
                case ConstRequest.REQUEST_SELECT_CAR_TYP:
                    CarTypeModel carTypeModel = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (carTypeModel != null && !carTypeModel.equals(mCarTypeModel)) {
                        mCarTypeModel = carTypeModel;
                        mImageType.setImageDrawable(new CarTypeModel().getDrawable(getContext(), carTypeModel.getImageName()));
                        mTextType.setText(carTypeModel.getName());
                        Timber.d(carTypeModel.getId() + " - " + carTypeModel.getName() + " - " + carTypeModel.getImageName());
                    }
                    break;

                // get manufacture data
                case ConstRequest.REQUEST_SELECT_MANUFACTURE:
                    ManufactureModel manufactureModel = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (manufactureModel != null && !manufactureModel.equals(mManufactureModel)) {
                        mManufactureModel = manufactureModel;
                        mImageManufacture.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), mManufactureModel.getImageName()));
                        mTextManufacture.setText(mManufactureModel.getName());
                        Timber.d(manufactureModel.getId() + " - " + manufactureModel.getName() + " - " + manufactureModel.getImageName());
                    }
                    break;

                // get tank one type
                case ConstRequest.REQUEST_SELECT_TANK_TYP_ONE:
                    FuelTypeModel fuelTypeModelOne = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (fuelTypeModelOne != null && !fuelTypeModelOne.equals(mFuelTypeModelOne)) {
                        mFuelTypeModelOne = fuelTypeModelOne;
                        mImageTankOne.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), fuelTypeModelOne.getImageName()));
                        mTextTankTypeOne.setText(fuelTypeModelOne.getName());
                        Timber.d(fuelTypeModelOne.getId() + " - " + fuelTypeModelOne.getName() + " - " + fuelTypeModelOne.getImageName());
                    }
                    break;

                // get tank two type
                case ConstRequest.REQUEST_SELECT_TANK_TYP_TWO:
                    FuelTypeModel fuelTypeModelTwo = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (fuelTypeModelTwo != null && !fuelTypeModelTwo.equals(mFuelTypeModelTwo)) {
                        mFuelTypeModelTwo = fuelTypeModelTwo;
                        mImageTankTwo.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), fuelTypeModelTwo.getImageName()));
                        mTextTankTypeTwo.setText(fuelTypeModelTwo.getName());
                        Timber.d(fuelTypeModelTwo.getId() + " - " + fuelTypeModelTwo.getName() + " - " + fuelTypeModelTwo.getImageName());
                    }
                    break;

                // get gallery photo
                case ConstRequest.REQUEST_IMAGE_GALLERY:
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), data.getData());
                        getImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                // get camera photo
                case ConstRequest.REQUEST_IMAGE_CAMERA:
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    getImage(bitmap);
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemDialogClick(DialogInterface dialog, int pos, int item) {
        switch (item) {
            case R.array.takephoto:
                /*
                <string-array name="takephoto" >
                    <item>@string/action_takePhotoNew</item>    (pos == 0)
                    <item>@string/action_choosePhotoNew</item>  (pos == 1)
                </string-array>
                 */
                if (pos == 0) {
                    openCamera();
                }
                if (pos == 1) {
                    openGallery();
                }
                break;
            case R.array.changephoto:
                 /*
                 <string-array name="changephoto" >
                    <item>@string/action_removePhoto</item>
                    <item>@string/action_takePhoto</item>
                    <item>@string/action_choosePhoto</item>
                </string-array>
                 */
                if (pos == 0) {
                    isTakePhoto = false;
                    changeImageVisibility();

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    VehicleModel mVehicleShare = new VehicleModel();
                    mVehicleShare.setPhoto(null);
                    bundle.putParcelable(ConstBundle.BUNDLE_VEHICLE_SHARE, mVehicleShare);
                    intent.putExtras(bundle);
                    intent.setAction(ConstAction.ACTION_ADD_UPDATE_VEHICLE);
                    Objects.requireNonNull(getContext()).sendBroadcast(intent);
                }
                if (pos == 1) {
                    openCamera();
                }
                if (pos == 2) {
                    openGallery();
                }
                break;
        }
    }

    @Override
    public void onButtonClick(int action, int flag) {
    }


    // ===========================================================
    // Methods
    // ===========================================================
    private boolean checkUI() {
        boolean allCorrekt = true;

        // check if exist car name is correct
        if (String.valueOf(mTextName.getText()).equals("") || String.valueOf(mTextName.getText()).length() >= 40) {
            allCorrekt = false;
            mTextName.setError(getResources().getString(R.string.error_carName));
        }

        // check if tank one capacity is correct
        if (String.valueOf(mTextTankCapacityOne.getText()).equals("") || !pattern(String.valueOf(mTextTankCapacityOne.getText()), pattern_TankCapacity)) {
            allCorrekt = false;
            mTextTankCapacityOne.setError(getResources().getString(R.string.error_carTankCapacity));
        }

        // check if tank two capacity is correct
        if (String.valueOf(mTextTankCapacityOne.getText()).equals("") || !pattern(String.valueOf(mTextTankCapacityOne.getText()), pattern_TankCapacity)) {
            allCorrekt = false;
            mTextTankCapacityOne.setError(getResources().getString(R.string.error_carTankCapacity));
        }

        // check if odometer is correct
        if (String.valueOf(mTextOdometer.getText()).equals("") || Integer.parseInt(String.valueOf(mTextOdometer.getText())) <= 0)
            if (mLinearTankTwo.getVisibility() == View.VISIBLE) {
                allCorrekt = false;
                mTextOdometer.setError(getResources().getString(R.string.error_carOdometer));
            }
        return allCorrekt;
    }

    /**
     * pattern to check if email is correct
     *
     * @param value
     * @param expression
     * @return
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean pattern(String value, String expression) {
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * Saves a captured bitmap and assigns it to an object
     *
     * @param bitmap
     */
    private void getImage(Bitmap bitmap) {
        // safe image
        StorageImageManager storageImageManager = new StorageImageManager(Objects.requireNonNull(getContext()));
        mSaveImage = storageImageManager.saveImage(bitmap);

        // set Image to vehicle
        VehicleModel mVehicleShare = new VehicleModel();
        mVehicleShare.setPhoto(mSaveImage);

        // send brooadcast to parent activity
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstBundle.BUNDLE_VEHICLE_SHARE, mVehicleShare);
        intent.putExtras(bundle);
        intent.setAction(ConstAction.ACTION_ADD_UPDATE_VEHICLE);

        Objects.requireNonNull(getContext()).sendBroadcast(intent);

        // set photo to imageView
        Timber.d("Objects.requireNonNull(getContext()).sendBroadcast(intent)");
        mImagePhoto.setImageBitmap(bitmap);
        changeImageVisibility();
        isTakePhoto = true;
    }

    /**
     * If no photo is available, only the photo shooting icon should be displayed.
     * Toogble betewwen photo and photo shooting icon
     */
    private void changeImageVisibility() {
        if (isTakePhoto)
            return;
        mImagePhoto.setVisibility(mImagePhoto.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        mButtonPhoto.setVisibility(mButtonPhoto.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        mButtonPhotoHaveImage.setVisibility(mButtonPhotoHaveImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    /**
     * Start photo dialog
     */
    public void confirmPhotoDialog() {
        if (!isTakePhoto) {

            // Open Camera
            // take new photo
            FragmentManager fm = getFragmentManager();
            ItemListDialog dialogFragment = ItemListDialog.newInstance(R.string.action_choosePhotoNew, R.array.takephoto);
            dialogFragment.setTargetFragment(this, ConstRequest.REQUEST_DIALOG_PHOTO);
            dialogFragment.show(Objects.requireNonNull(fm), "ItemListDialog");

            // change taken photo
        } else {
            FragmentManager fm = getFragmentManager();
            ItemListDialog dialogFragment = ItemListDialog.newInstance(R.string.action_choosePhotoNew, R.array.changephoto);
            dialogFragment.setTargetFragment(this, ConstRequest.REQUEST_DIALOG_PHOTO);
            dialogFragment.show(Objects.requireNonNull(fm), "ItemListDialog");
        }
    }

    /**
     * Start device camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, ConstRequest.REQUEST_IMAGE_CAMERA);
        }
    }

    /**
     * Permission checks to use camera
     */
    private void openCamera() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // User may have declined earlier, ask Android if we should show him a reason
                //noinspection StatementWithEmptyBody
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // show an explanation to the user
                    // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
                } else {
                    // request the permission.
                    // CALLBACK_NUMBER is a integer constants
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            ConstRequest.REQUEST_PERMISSION_OPEN_CAMERA);
                    // The callback method gets the result of the request.
                }
            } else {
                // got permission use it
                dispatchTakePictureIntent();
            }
        } else {
            // lower than VERSION_CODES.M
            dispatchTakePictureIntent();
        }
    }

    /**
     * Permission checks to use gallery
     */
    private void openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // User may have declined earlier, ask Android if we should show him a reason
                //noinspection StatementWithEmptyBody
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // show an explanation to the user
                    // Good practise: don't block thread after the user sees the explanation, try again to request the permission.
                } else {
                    // request the permission.
                    // CALLBACK_NUMBER is a integer constants
                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            ConstRequest.REQUEST_PERMISSION_OPEN_GALLERY);
                    // The callback method gets the result of the request.
                }
            } else {
                // got permission use it
                // Open Gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_openGallery)), ConstRequest.REQUEST_IMAGE_GALLERY);
            }
        } else {
            // lower than VERSION_CODES.M
            // Open Gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_openGallery)), ConstRequest.REQUEST_IMAGE_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case ConstRequest.REQUEST_PERMISSION_OPEN_CAMERA:
                // If request is cancelled, the result arrays are empty.
                //noinspection StatementWithEmptyBody
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    dispatchTakePictureIntent();
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                }
                break;
            case ConstRequest.REQUEST_PERMISSION_OPEN_GALLERY:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.title_openGallery)), ConstRequest.REQUEST_IMAGE_GALLERY);
                // other 'case' statements for other permssions
                break;
        }
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}