package com.nothing.timing.wearound.bloc.data.ClassData;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.nothing.timing.wearound.tools.CameraLocation;
import com.nothing.timing.wearound.tools.PermissionProvider;

public class GoogleMapsData {

    private boolean locationPermission;
    private LatLng myLatlng;

    private Location currentLocation;

    public float getDefaultZoom() {

        return 15f;
    }

    public void setCurrentLocation(Location currentLocation) {

        if (currentLocation == null) {

            Log.e("Location", "device location is null");
        }

        this.currentLocation = currentLocation;
    }

    public Location getCurrentLocation() {

        return currentLocation;
    }

    public FusedLocationProviderClient getClient(Context context) {

        return LocationServices.getFusedLocationProviderClient(context);
    }


    public void setLocationPermission(boolean onORoff) {

        locationPermission = onORoff;
    }

    public boolean getLocationPermission() {

        return locationPermission;
    }

    public boolean getPermissionsResult(int requestCode, int[] grantResults) {

        return PermissionProvider.permissionResult(requestCode,grantResults);
    }

    public LatLng getLatLng() {

        if (CameraLocation.isFromLists()) {

            return CameraLocation.getShopLatLng();

        } else {

            return getMyLatLng();
        }
    }

    private LatLng getMyLatLng() {

        if (myLatlng == null) {

            myLatlng = new LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
            );
        }

        return myLatlng;
    }


}
