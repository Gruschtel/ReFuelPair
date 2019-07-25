package de.gruschtelapps.fh_maa_refuelpair.utils.dialog.activity;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstBundle;


/**
 * Create by Eric Werner
 * ItemListDialog for Activities
 */
public class ItemListDialog extends DialogFragment {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public ItemListDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ItemListDialog newInstance(int title, int item) {
        ItemListDialog frag = new ItemListDialog();
        Bundle args = new Bundle();
        args.putInt(ConstBundle.BUNDLE_DIALOG_TITEL, title);
        args.putInt(ConstBundle.BUNDLE_DIALOG_ITEM, item);
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
        final int item = getArguments().getInt(ConstBundle.BUNDLE_DIALOG_ITEM, R.string.fuell_dialog);
        builder.setTitle(title);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                DialogListener listener2 = (DialogListener) getTargetFragment();
                Objects.requireNonNull(listener2).onItemDialogClick(dialog, which, item);
                dismiss();
            }
        });
        // Button
        builder.setPositiveButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }

    public interface DialogListener {
        void onItemDialogClick(DialogInterface dialog, int pos, int item);

        void onButtonClick(int action, int flag);
    }
    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
