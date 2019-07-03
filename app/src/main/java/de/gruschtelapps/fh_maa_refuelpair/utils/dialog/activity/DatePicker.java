package de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

/**
 * Create by Eric Werner
 */
public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private DateListener listener;

    // ===========================================================
    // Constructors
    // ===========================================================
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
    // Override the Fragment.onAttach() method to instantiate the DialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DateListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Objects.requireNonNull(listener).onDateDialogClick(view,year,month,dayOfMonth);
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
