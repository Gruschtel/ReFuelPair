package de.gruschtelapps.fh_maa_refuelpair.utils.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import de.gruschtelapps.fh_maa_refuelpair.R;
import de.gruschtelapps.fh_maa_refuelpair.utils.constants.ConstRequest;
import timber.log.Timber;

/**
 * Create by Eric Werner
 */

public class LocationPermissionHelper {


    private Activity activity;
    private Context context;


    public LocationPermissionHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    // react to permission requests
    public static void onRequestPermissionsResult(Context context, int requestCode,
                                                  String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ConstRequest.REQUEST_PERMISSIN_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Timber.d("GPS PERMISSION ALLOWD");
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(context, context.getResources().getString(R.string.error_gpsPermission), Toast.LENGTH_LONG).show();
                  /*  // Send intent
                    Intent locationIntent = new Intent();
                    locationIntent.putExtra("locationpermission", false);
                    context.sendBroadcast(locationIntent);*/
                        Timber.d("GPS PERMISSION NOT ALLOWD");

                    }
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkLocationPermission_onStart() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ConstRequest.REQUEST_PERMISSIN_FINE_LOCATION);
            return false;
        } else {
            return true;
        }
    }

}
