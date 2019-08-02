package com.nothing.timing.wearound.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class CheckServices {

    private static final String TAG = "CheckServices";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    public static boolean isServiceOK(Context context) {

        Log.e(TAG, "isServiceOK: checking google play services");

        int available = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(context);

        if ( available == ConnectionResult.SUCCESS ) {
            Log.e(TAG, "isServiceOK: Google Play Services is working");

            return true;

        } else if ( GoogleApiAvailability.getInstance().isUserResolvableError(available) ) {
            Log.e(TAG, "isServiceOK: an error occurred but it's fixable");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(
                    (Activity) context, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        } else {
            String string = "Cannot make request, connection failed";
            Log.e(TAG,"isServiceOK: " + string);

            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
