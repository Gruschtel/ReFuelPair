package de.gruschtelapps.fh_maa_refuelpair.utils.dialog.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstError;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;

/**
 * Create by Eric Werner
 */
public class MessageFragmentDialog extends DialogFragment {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private DialogListener listener;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MessageFragmentDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static MessageFragmentDialog newInstance(int dialogRequest, int title, int message) {
        MessageFragmentDialog frag = new MessageFragmentDialog();
        Bundle args = new Bundle();
        args.putInt(ConstBundle.BUNDLE_DIALOG_TITEL, title);
        args.putInt(ConstBundle.BUNDLE_DIALOG_MESSAGE, message);
        args.putInt(ConstBundle.BUNDLE_DIALOG_ACTION, dialogRequest);
        frag.setArguments(args);
        return frag;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        final int title = Objects.requireNonNull(getArguments()).getInt(ConstBundle.BUNDLE_DIALOG_TITEL, R.string.fuell_dialog);
        final int message = getArguments().getInt(ConstBundle.BUNDLE_DIALOG_MESSAGE, R.string.fuell_dialog);
        final int dialogRequest = getArguments().getInt(ConstBundle.BUNDLE_DIALOG_ACTION, ConstError.ERROR_INT);
        builder.setTitle(title);
        builder.setMessage(message);
        // Positiv Button
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogListener listener2 = (DialogListener) getTargetFragment();
                Objects.requireNonNull(listener2).onDialogButtonClick(dialogRequest, ConstRequest.REQUEST_DIALOG_POSITIV);
                dismiss();
            }
        });
        // Negativ Button
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogListener listener2 = (DialogListener) getTargetFragment();
                Objects.requireNonNull(listener2).onDialogButtonClick(dialogRequest, ConstRequest.REQUEST_DIALOG_NEGATIV);
                dismiss();
            }
        });
        return builder.create();
    }

    public interface DialogListener {
        void onDialogItemClick(DialogInterface dialog, int pos, int item);
        void onDialogButtonClick(int action, int flag);
    }
    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
