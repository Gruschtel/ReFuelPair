package de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Objects;

/**
 * Create by Eric Werner
 */
public class TimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private TimeListener listener;

    // ===========================================================
    // Constructors
    // ===========================================================
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

    // Override the Fragment.onAttach() method to instantiate the DialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (TimeListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Objects.requireNonNull(listener).onTimeDialogClick(view, hourOfDay, minute);
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
