package de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

/**
 * Create by Eric Werner
 * DatePickerFragment  for Fragments
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private DatePickerFragment.DateListener listener;

    // ===========================================================
    // Constructors
    // ===========================================================
    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Objects.requireNonNull(listener).onDateDialogClick(view, year, month, dayOfMonth);
    }


    public void setListener(DateListener listener) {
        this.listener = listener;
    }


    // ===========================================================
    // Methods
    // ===========================================================
    public interface DateListener {
        void onDateDialogClick(android.widget.DatePicker view, int year, int month, int dayOfMonth);
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
