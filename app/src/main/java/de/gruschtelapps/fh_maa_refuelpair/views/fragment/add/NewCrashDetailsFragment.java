package de.gruschtelapps.fh_maa_refuelpair.views.fragment.add;

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

import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.db.DBHelper;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstAction;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.DatePicker;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity.TimePicker;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment.DatePickerFragment;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment.MessageFragmentDialog;
import de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment.TimePickerFragment;
import de.gruschtelapps.fh_maa_refuelpair.utils.model.add.CrashModel;
import timber.log.Timber;
/*
 * Create by Eric Werner
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NewCrashDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCrashDetailsFragment extends Fragment implements
        View.OnClickListener, DatePicker.DateListener, TimePicker.TimeListener, MessageFragmentDialog.DialogListener {
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

    // ===========================================================
    // Fields
    // ===========================================================
    private int flag;
    private CrashModel mCrashModel;

    private MaterialEditText mEditDate, mEditTime, mEditDescription, mEditOwnDamage, mEditOtherDamage, mEditLocal;

    private boolean isEdit = false;
    private Calendar c;

    private int mTimeYear, mTimeMonth, mTimeDay, mTimeHour, mTimeMinute;

    // ===========================================================
    // Constructors
    // ===========================================================
    public NewCrashDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VehicleFragment.
     */
    public static NewCrashDetailsFragment newInstance() {
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return new NewCrashDetailsFragment();
    }

    public static NewCrashDetailsFragment newInstance(CrashModel i) {
        NewCrashDetailsFragment fragment = new NewCrashDetailsFragment();
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

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_crash_details, container, false);

        // Get UI
        mEditDate = v.findViewById(R.id.text_addCrash_Date);
        mEditTime = v.findViewById(R.id.text_addCrash_Date2);
        mEditDescription = v.findViewById(R.id.text_addCrash_description);
        mEditOwnDamage = v.findViewById(R.id.text_addCrash_damageOwn);
        mEditOtherDamage = v.findViewById(R.id.text_addCrash_damageOther);
        mEditLocal = v.findViewById(R.id.text_addCrash_local);
        // set OnnCLickListener

        mEditDate.setOnClickListener(this);
        mEditTime.setOnClickListener(this);

        // Standard Objects
        c = Calendar.getInstance();
        if (mCrashModel == null)
            mCrashModel = new CrashModel();

        // load data
        if (flag == 1) {
            isEdit = true;
            c.setTimeInMillis(mCrashModel.getDate());

            mEditLocal.setText(mCrashModel.getLocal());
            mEditDescription.setText(mCrashModel.getDescription());
            mEditOwnDamage.setText(mCrashModel.getOwnDamage());
            mEditOtherDamage.setText(mCrashModel.getOtherDamage());
        }

        // Get Date & Time

        // date
        String year = setNullVorne(c.get(Calendar.YEAR), 4);
        String month = setNullVorne(c.get(Calendar.MONTH), 2);
        String day = setNullVorne(c.get(Calendar.DAY_OF_MONTH), 2);

        // time
        String hour = setNullVorne(c.get(Calendar.HOUR), 2);
        String minute = setNullVorne(c.get(Calendar.MINUTE), 2);

        mEditDate.setText(String.format(getResources().getString(R.string.value_date),
                year,
                month,
                day));
        Timber.d(String.valueOf(c.getTimeInMillis()));


        mEditTime.setText(String.format(getResources().getString(R.string.value_time),
                hour,
                minute));


        // save time
        mTimeYear = c.get(Calendar.YEAR);
        mTimeMonth = c.get(Calendar.MONTH);
        mTimeDay = c.get(Calendar.DAY_OF_MONTH);
        mTimeHour = c.get(Calendar.HOUR_OF_DAY);
        mTimeMinute = c.get(Calendar.MINUTE);

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

            // open date picker
            case R.id.text_addCrash_Date:
                DatePickerFragment datePicker = DatePickerFragment.newInstance();
                datePicker.setListener(new DatePickerFragment.DateListener() {
                    @Override
                    public void onDateDialogClick(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        mEditDate.setText(String.format(getResources().getString(R.string.value_date),
                                setNullVorne(year, 2),
                                setNullVorne(month, 2),
                                setNullVorne(dayOfMonth, 2)));

                        // save time
                        mTimeYear = year;
                        mTimeMonth = month;
                        mTimeDay = dayOfMonth;
                    }
                });
                //datePicker.setTargetFragment(this, ConstRequest.REQUEST_DIALOG_DATE);
                datePicker.show(getFragmentManager(), "datePicker");
                break;

            // open time picker
            case R.id.text_addCrash_Date2:
                TimePickerFragment timePicker = TimePickerFragment.newInstance();
                timePicker.setListener(new TimePickerFragment.TimeListener() {
                    @Override
                    public void onTimeDialogClick(android.widget.TimePicker view, int hourOfDay, int minute) {
                        mEditTime.setText(String.format(getResources().getString(R.string.value_time),
                                setNullVorne(hourOfDay, 2),
                                setNullVorne(minute, 2)));

                        mTimeHour = hourOfDay;
                        mTimeMinute = minute;
                    }
                });
                timePicker.show(getFragmentManager(), "timePicker");
                break;
        }

        // start activity if intent != null
        if (intent != null) {
            startActivityForResult(intent, flag);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            // save data and go back
            case R.id.menue_add_finish:
                if (checkUI()) {
                    Calendar dateTime = Calendar.getInstance();
                    dateTime.set(mTimeYear, mTimeMonth, mTimeDay, mTimeHour, mTimeMinute);

                    // First Crash
                    CrashModel mCrashShare = new CrashModel();
                    if (flag == 1) {
                        // wenn bearbeiten änderungen hier
                        intent.setAction(ConstAction.ACTION_ADD_UPDATE_CRASH_DETAILS);
                    } else {
                        // wenn new dann code here
                        intent.setAction(ConstAction.ACTION_ADD_NEXT_PAGE);
                    }

                    // save car data
                    mCrashShare.setDate(dateTime.getTimeInMillis());
                    mCrashShare.setLocal(String.valueOf(mEditLocal.getText()));
                    mCrashShare.setDescription(String.valueOf(mEditDescription.getText()));
                    mCrashShare.setOwnDamage(String.valueOf(mEditOwnDamage.getText()));
                    mCrashShare.setOtherDamage(String.valueOf(mEditOtherDamage.getText()));

                    // send broadcast to parent activity with car data
                    bundle.putParcelable(ConstBundle.BUNDLE_CRASH_DETAILS_SHARE, mCrashShare);
                    intent.putExtras(bundle);
                    Objects.requireNonNull(getContext()).sendBroadcast(intent);
                }
                return true;

            // delete data and go back
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

    @Override
    public void onTimeDialogClick(android.widget.TimePicker view, int hourOfDay, int minute) {

    }

    @Override
    public void onDateDialogClick(android.widget.DatePicker view, int year, int month, int dayOfMonth) {

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

    /**
     * Fills a value that is less than length with 0 to
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
