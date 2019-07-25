package de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Objects;

/**
 * Create by Eric Werner
 * TimePicker for Fragments
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private TimePickerFragment.TimeListener listener;

    // ===========================================================
    // Constructors
    // ===========================================================
    public static TimePickerFragment newInstance() {
        return new TimePickerFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Objects.requireNonNull(listener).onTimeDialogClick(view, hourOfDay, minute);

    }

    public void setListener(TimeListener timeListener) {
        this.listener = timeListener;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public interface TimeListener {
        void onTimeDialogClick(android.widget.TimePicker view, int hourOfDay, int minute);
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
