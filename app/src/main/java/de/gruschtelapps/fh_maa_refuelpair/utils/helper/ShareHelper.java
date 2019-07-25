package de.gruschtelapps.fh_maa_refuelpair.utils.helper;

import android.content.Context;
import android.content.Intent;

import de.gruschtelapps.fh_maa_refuelpair.R;


/**
 * Create by Eric Werner
 */
public class ShareHelper {

    public static void shareText(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.action_share)));
    }
}
