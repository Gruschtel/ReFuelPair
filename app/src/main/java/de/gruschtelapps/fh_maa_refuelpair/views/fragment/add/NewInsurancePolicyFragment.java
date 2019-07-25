package de.gruschtelapps.fh_maa_refuelpair.views.fragment.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstAction;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstPreferences;
import de.gruschtelapps.fh_maa_refuelpair.utils.helper.SharedPreferencesManager;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.InsurancePolicyModel;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.information.VehicleModel;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewInsurancePolicyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewInsurancePolicyFragment extends Fragment {
    // ===========================================================
    // Constants
    // ===========================================================
    private final String pattern_email = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";

    // ===========================================================
    // Fields
    // ===========================================================
    MaterialEditText mTextName, mTextSurename, mTextPhone, mTextEmail, mTextAdress, mTextPolicyName, mTextPolicyNumber;
    private int flag;
    private VehicleModel mVehicleModel;
    private InsurancePolicyModel insurancePolicyModel;

    // ===========================================================
    // Constructors
    // ===========================================================
    public NewInsurancePolicyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehicleFragment.
     */
    public static NewInsurancePolicyFragment newInstance() {
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return new NewInsurancePolicyFragment();
    }

    public static NewInsurancePolicyFragment newInstance(int i) {
        NewInsurancePolicyFragment fragment = new NewInsurancePolicyFragment();
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
        Timber.d("%s created", getClass().getSimpleName());
        setHasOptionsMenu(true);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menue_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_insurance_policy, container, false);

        mTextName = v.findViewById(R.id.text_addInsurance_name);
        mTextSurename = v.findViewById(R.id.text_addInsurance_surename);
        mTextPhone = v.findViewById(R.id.text_addInsurance_phone);
        mTextEmail = v.findViewById(R.id.text_addInsurance_email);
        mTextAdress = v.findViewById(R.id.text_addInsurance_adress);
        mTextPolicyName = v.findViewById(R.id.text_addInsurance_policyName);
        mTextPolicyNumber = v.findViewById(R.id.text_addInsurance_policyNumber);

        // set Title Actionbar
        if (flag == 1) {
            SharedPreferencesManager pref = new SharedPreferencesManager(Objects.requireNonNull(getContext()));
            long id = pref.getPrefLong(ConstPreferences.PREF_CURRENT_CAR);

            Timber.d("load Car Data from id: %s", id);
            DBHelper dbHelper = new DBHelper(getContext());
            mVehicleModel = dbHelper.getGet().selectCarById(id);
            insurancePolicyModel = mVehicleModel.getInsurancePolicyModel();

            mTextName.setText(insurancePolicyModel.getName().equals("") ? "" : insurancePolicyModel.getName());
            mTextSurename.setText(insurancePolicyModel.getSureName().equals("") ? "" : insurancePolicyModel.getSureName());
            mTextPhone.setText(insurancePolicyModel.getTelephone().equals("") ? "" : insurancePolicyModel.getTelephone());
            mTextEmail.setText(insurancePolicyModel.getEmail().equals("") ? "" : insurancePolicyModel.getEmail());
            mTextAdress.setText(insurancePolicyModel.getAdress().equals("") ? "" : insurancePolicyModel.getAdress());
            mTextPolicyName.setText(insurancePolicyModel.getInsurancePolicyName().equals("") ? "" : insurancePolicyModel.getInsurancePolicyName());
            mTextPolicyNumber.setText(insurancePolicyModel.getInsurancePolicyNumber().equals("") ? "" : insurancePolicyModel.getInsurancePolicyNumber());
        }
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected %s", item.getItemId());
        Timber.d("onOptionsItemSelected %s", R.id.menue_add_finish);
        //noinspection SwitchStatementWithTooFewBranches
        switch (item.getItemId()) {
            case R.id.menue_add_finish:
                //noinspection EqualsBetweenInconvertibleTypes
                if (!Objects.equals(mTextEmail.getText(), "") || pattern(String.valueOf(mTextEmail.getText()), pattern_email)) {
                    InsurancePolicyModel insurancePolicyModel = new InsurancePolicyModel();

                    String name = mTextName.getText() + "";
                    insurancePolicyModel.setName(name);
                    insurancePolicyModel.setSureName(Objects.requireNonNull(mTextSurename.getText()).toString());
                    insurancePolicyModel.setTelephone(Objects.requireNonNull(mTextPhone.getText()).toString());
                    insurancePolicyModel.setEmail(Objects.requireNonNull(mTextEmail.getText()).toString());
                    insurancePolicyModel.setAdress(Objects.requireNonNull(mTextAdress.getText()).toString());
                    insurancePolicyModel.setInsurancePolicyName(Objects.requireNonNull(mTextPolicyName.getText()).toString());
                    insurancePolicyModel.setInsurancePolicyNumber(Objects.requireNonNull(mTextPolicyNumber.getText()).toString());

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ConstBundle.BUNDLE_INSURANCE_SHARE, insurancePolicyModel);
                    intent.putExtras(bundle);
                    intent.setAction(ConstAction.ACTION_ADD_FINISH);
                    Timber.d("onOptionsItemSelected " + R.id.menue_add_finish);
                    Objects.requireNonNull(getContext()).sendBroadcast(intent);
                } else {
                    mTextEmail.setError(getResources().getString(R.string.error_email));
                }
                return true;
            default:
                Timber.d(item.toString());
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Context context) {
        setHasOptionsMenu(true);
        super.onAttach(context);
    }


    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Pattern to check if E-Mail is correct
     * @param value
     * @param expression
     * @return
     */
    public static boolean pattern(String value, String expression) {
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}