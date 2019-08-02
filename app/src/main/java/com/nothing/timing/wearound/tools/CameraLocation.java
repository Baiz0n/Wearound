package com.nothing.timing.wearound.tools;

import com.google.android.gms.maps.model.LatLng;

public class CameraLocation {

    public static boolean fromList = false;
    private static LatLng latLng;

    public static boolean isFromLists() {

        return fromList;
    }

    public static void setShopLatLng(LatLng latLng) {

        CameraLocation.latLng = latLng;
    }

    public static LatLng getShopLatLng() {

        return latLng;
    }
}
