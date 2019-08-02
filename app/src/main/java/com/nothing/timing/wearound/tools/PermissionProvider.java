package com.nothing.timing.wearound.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionProvider {

    public final static int CODE = 1234;

    public final static String PERMISSION[] = {

            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static boolean permissionResult(int requestCode, @NonNull int[] grantResults) {

//        switch (requestCode) {
//
//            case CODE:
//
//                if (grantResults.length > 0) {
//
//                    for (int grantResult : grantResults) {
//
//                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
//
//                            return false;
//                        }
//                    }
//
//                return true;
//            }
//        }
//
     return false;
    }
    public static boolean isNetworkAvailable(Context context) {

        if (context == null ) {

            Log.e("isNetWorkAvailable",   "context is NULL");
        }

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
