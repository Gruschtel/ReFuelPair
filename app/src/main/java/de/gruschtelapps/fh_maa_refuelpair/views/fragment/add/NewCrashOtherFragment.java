package de.gruschtelapps.fh_maa_refuelpair.views.fragment.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstAction;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstExtras;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment.MessageFragmentDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.KontaktModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.FuelTypeModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.ManufactureModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.SelectListActivity;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewKontaktActivity;
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
public class NewCrashOtherFragment extends Fragment implements
        View.OnClickListener, MessageFragmentDialog.DialogListener {
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

    // ===========================================================
    // Fields
    // ===========================================================
    private int flag = 0;
    private MaterialEditText mEditOwner, mEditDriver, mEditManufacture, mEditModel, mEditInsurancePoliceName, mEditInsurancePoliceNumber;
    private ImageView mImageManufacture;

    private KontaktModel mKontaktOwner, mKontaktDriver;
    private CrashModel mCrashModel;
    private ManufactureModel mManufacture;

    // ===========================================================
    // Constructors
    // ===========================================================
    public NewCrashOtherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehicleFragment.
     */
    public static NewCrashOtherFragment newInstance() {
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return new NewCrashOtherFragment();
    }

    public static NewCrashOtherFragment newInstance(CrashModel i) {
        NewCrashOtherFragment fragment = new NewCrashOtherFragment();
        Bundle args = new Bundle();
        args.putParcelable("flag", i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCrashModel = getArguments().getParcelable("flag");
            if (mCrashModel != null)
                flag = 1;
        }
        // ...
        Timber.d("%s created", getClass().getSimpleName());
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_crash_other, container, false);
        // Get UI
        mEditOwner = v.findViewById(R.id.text_addCrash_owner);
        mEditDriver = v.findViewById(R.id.text_addCrash_carDriver);
        mEditManufacture = v.findViewById(R.id.text_addCrash_vehicleManufacture);
        mImageManufacture = v.findViewById(R.id.image_addCrash_vehicleManufacture);
        mEditModel = v.findViewById(R.id.text_addCrash_vehicleModel);
        mEditInsurancePoliceName = v.findViewById(R.id.text_addCrash_policyName);
        mEditInsurancePoliceNumber = v.findViewById(R.id.text_addCrash_policyNumber);

        // set OnCLickListener
        mEditOwner.setOnClickListener(this);
        mEditDriver.setOnClickListener(this);
        mEditManufacture.setOnClickListener(this);

        // Standard Objects
        if (mCrashModel == null)
            mCrashModel = new CrashModel();
        mKontaktOwner = new KontaktModel();
        mKontaktDriver = new KontaktModel();
        mManufacture = new ManufactureModel();

        // load data
        if (flag == 1) {

            //  load car owner name
            if (!mCrashModel.getOwner().getName().equals(""))
                mEditOwner.setText(mCrashModel.getOwner().getName() + " " + mCrashModel.getOwner().getSurename());

            // load driver name
            if (!mCrashModel.getDriver().getName().equals(""))
                mEditDriver.setText(mCrashModel.getDriver().getName() + " " + mCrashModel.getDriver().getSurename());

            // get data
            mKontaktOwner = mCrashModel.getOwner();
            mKontaktDriver = mCrashModel.getDriver();

            // set data
            mEditManufacture.setText(mCrashModel.getManufacture().getName());
            mImageManufacture.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), mManufacture.getImageName()));
            mEditModel.setText(mCrashModel.getModel());
            mEditInsurancePoliceName.setText(mCrashModel.getInsurancePoliceName());
            mEditInsurancePoliceNumber.setText(mCrashModel.getInsurancePoliceNumber());
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
            inflater.inflate(R.menu.menue_add_delete2, menu);
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

            // start new Kontakt activity
            case R.id.text_addCrash_owner:
                intent = new Intent(getActivity(), NewKontaktActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_OTHER_OWNER);
                if (mKontaktOwner != null)
                    intent.putExtra(ConstExtras.EXTRA_OBJECT_EDIT, mKontaktOwner);
                flag = ConstRequest.REQUEST_SELECT_OTHER_OWNER;
                break;

            // start new Kontakt activity
            case R.id.text_addCrash_carDriver:
                intent = new Intent(getActivity(), NewKontaktActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_OTHER_DRIVER);
                if (mKontaktDriver != null)
                    intent.putExtra(ConstExtras.EXTRA_OBJECT_EDIT, mKontaktDriver);
                flag = ConstRequest.REQUEST_SELECT_OTHER_DRIVER;
                break;

            // start new selectedList activity (manufacture)
            case R.id.text_addCrash_vehicleManufacture:
                intent = new Intent(getActivity(), SelectListActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_SELECT, ConstExtras.EXTRA_SELECT_MANUFACTURE);
                flag = ConstRequest.REQUEST_SELECT_MANUFACTURE;
                break;

        }
        if (intent != null) {
            startActivityForResult(intent, flag);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            //
            case R.id.menue_add_finish:
                if (checkUI()) {
                    // First Crash
                    CrashModel mCrashShare = new CrashModel();
                    if (flag == 1) {
                        // wenn bearbeiten änderungen hier
                        intent.setAction(ConstAction.ACTION_ADD_UPDATE_CRASH_OTHER);
                    } else {
                        // wenn new dann code here
                        intent.setAction(ConstAction.ACTION_ADD_NEXT_PAGE);
                    }

                    // safe data
                    mCrashShare.setOwner(mKontaktOwner);
                    mCrashShare.setDriver(mKontaktDriver);
                    mCrashShare.setManufacture(mManufacture);
                    mCrashShare.setModel(String.valueOf(mEditModel.getText()));
                    mCrashShare.setInsurancePoliceName(String.valueOf(mEditInsurancePoliceName.getText()));
                    mCrashShare.setInsurancePoliceNumber(String.valueOf(mEditInsurancePoliceNumber.getText()));

                    // send broadcast to parent activity with car data
                    bundle.putParcelable(ConstBundle.BUNDLE_CRASH_OTHER_SHARE, mCrashShare);
                    intent.putExtras(bundle);
                    Objects.requireNonNull(getContext()).sendBroadcast(intent);
                }
                return true;

            // delete data
            case R.id.menue_add_delete:
                FragmentManager fm = getFragmentManager();
                MessageFragmentDialog messageDialog = MessageFragmentDialog.newInstance(ConstRequest.REQUEST_DIALOG_DELETE, R.string.title_button_delete, R.string.msg_button_delete);
                messageDialog.setTargetFragment(this, ConstRequest.REQUEST_DIALOG_POSITIV);
                messageDialog.show(Objects.requireNonNull(Objects.requireNonNull(fm)), "messageDialog");
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data)
            switch (requestCode) {

                // get other owner data
                case ConstRequest.REQUEST_SELECT_OTHER_OWNER:
                    KontaktModel kontaktModel = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (kontaktModel != null) {
                        mKontaktOwner = kontaktModel;
                        mEditOwner.setText((mKontaktOwner.getName().equals("") ? "no name" : mKontaktOwner.getName()) + " " + (mKontaktOwner.getSurename().equals("") ? ", no Surename" : mKontaktOwner.getName()));
                        mCrashModel.setOwner(mKontaktOwner);
                    }
                    break;

                // get other driver data
                case ConstRequest.REQUEST_SELECT_OTHER_DRIVER:
                    KontaktModel kontaktModel2 = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (kontaktModel2 != null) {
                        mKontaktDriver = kontaktModel2;
                        mEditDriver.setText((mKontaktDriver.getName().equals("") ? "no name" : mKontaktDriver.getName()) + " " + (mKontaktDriver.getSurename().equals("") ? ", no Surename" : mKontaktDriver.getName()));
                        mCrashModel.setDriver(mKontaktDriver);
                    }
                    break;

                // get manufacture data
                case ConstRequest.REQUEST_SELECT_MANUFACTURE:
                    ManufactureModel manufactureModel = Objects.requireNonNull(data.getExtras()).getParcelable(ConstExtras.EXTRA_OBJECT_ADD);
                    if (manufactureModel != null && !manufactureModel.equals(mEditManufacture)) {
                        mManufacture = manufactureModel;
                        mImageManufacture.setImageDrawable(new FuelTypeModel().getDrawable(getContext(), mManufacture.getImageName()));
                        mEditManufacture.setText(mManufacture.getName());
                        Timber.d(manufactureModel.getId() + " - " + manufactureModel.getName() + " - " + manufactureModel.getImageName());
                    }
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDialogItemClick(DialogInterface dialog, int pos, int item) {

    }

    @Override
    public void onDialogButtonClick(int action, int flag) {
        // If the user responds positively to the deletion request, the object is to be deleted.
        if (action == ConstRequest.REQUEST_DIALOG_DELETE)
            if (flag == ConstRequest.REQUEST_DIALOG_POSITIV) {
                final DBHelper mDbHelper = new DBHelper(getContext());
                mDbHelper.getDelete().deleteItem(mCrashModel.getId());
                Intent finishIntent = new Intent();
                Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, finishIntent);
                getActivity().finish();
            }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private boolean checkUI() {
        boolean allCorrekt = true;
        //
        return allCorrekt;
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}