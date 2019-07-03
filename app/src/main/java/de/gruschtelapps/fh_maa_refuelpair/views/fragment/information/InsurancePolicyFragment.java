package de.gruschtelapps.fh_maa_refuelpair.views.fragment.information;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.InsurancePolicyModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import de.gruschtelapps.fh_maa_refuelpair.views.activities.add.NewCarActivity;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link InsurancePolicyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsurancePolicyFragment extends Fragment implements View.OnClickListener {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String LOG_TAG = getClass().getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================
    // Model
    protected VehicleModel mVehicleModel;
    protected LoadDataAsyncTask loadDataAsyncTask;
    protected InsurancePolicyModel insurancePolicyModel;
    // UI
    protected MaterialEditText mTextName, mTextSurename, mTextPhone, mTextEmail, mTextAdress, mTextPolicyName, mTextPolicyNumber;
    private FloatingActionButton mFab;
    // ===========================================================
    // Constructors
    // ===========================================================
    public InsurancePolicyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FuelSettingsFragment.
     */
    public static InsurancePolicyFragment newInstance() {
        InsurancePolicyFragment insurancePolicyFragment = new InsurancePolicyFragment();
        return insurancePolicyFragment;
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_insurance_policy, container, false);

        // SET UI
        mTextName = v.findViewById(R.id.text_insuranceData_name);
        mTextSurename = v.findViewById(R.id.text_insuranceData_surename);
        mTextPhone = v.findViewById(R.id.text_insuranceData_phone);
        mTextEmail = v.findViewById(R.id.text_insuranceData_email);
        mTextAdress = v.findViewById(R.id.text_insuranceData_adress);
        mTextPolicyName = v.findViewById(R.id.text_insuranceData_policyName);
        mTextPolicyNumber = v.findViewById(R.id.text_insuranceData_policyNumber);

        mFab = v.findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);

        return v;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute((Void) null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop(){
        super.onStop();
        loadDataAsyncTask.cancel(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int myflag = ConstRequest.REQUEST_MAIN_EDIT_CAR;
        switch (v.getId()){
            case R.id.fab:
                intent = new Intent(getActivity(), NewCarActivity.class);
                intent.putExtra(ConstExtras.EXTRA_KEY_EDIT, ConstExtras.EXTRA_EDIT_INSURANCE);
                myflag = ConstRequest.REQUEST_MAIN_NEW_CAR;
                break;
        }
        if (intent != null) {
            startActivityForResult(intent, myflag);
        } else {
            Timber.e("onItemClick");
            Toast.makeText(getActivity(), getResources().getString(R.string.error_startActivit, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
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
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Load Data
                SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
                long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);

                if (id != ConstError.ERROR_LONG && pref.getPrefBool(ConstPreferences.PREF_FIRST_START)) {
                    Timber.d("load Car Data from id: %s", id);
                    DBHelper dbHelper = new DBHelper(getContext());
                    mVehicleModel = dbHelper.getGet().selectCarById(id);
                    insurancePolicyModel = mVehicleModel.getInsurancePolicyModel();
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                Timber.e(e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                //Todo: ERROR MESSAGE
                return;
            }
            mTextName.setText(insurancePolicyModel.getName());
            mTextSurename.setText(insurancePolicyModel.getSureName());
            mTextPhone.setText(insurancePolicyModel.getTelephone());
            mTextEmail.setText(insurancePolicyModel.getEmail());
            mTextAdress.setText(insurancePolicyModel.getAdress());
            mTextPolicyName.setText(insurancePolicyModel.getInsurancePolicyName());
            mTextPolicyNumber.setText(insurancePolicyModel.getInsurancePolicyNumber());
        }

        @Override
        protected void onCancelled() {

        }
    }
}
