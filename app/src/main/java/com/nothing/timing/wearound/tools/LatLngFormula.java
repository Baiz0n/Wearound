package com.nothing.timing.wearound.tools;

import android.location.Location;

import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;

import java.text.DecimalFormat;

public class LatLngFormula {

    public static String getDistanceText(
            double latA,
            double lngA,
            double latB,
            double lngB
    ) {
        Location locationA = new Location("point A");
        Location locationB = new Location("point B");

        locationA.setLatitude(latA);
        locationA.setLongitude(lngA);

        locationB.setLatitude(latB);
        locationB.setLongitude(lngB);

        double pureDistance = locationA.distanceTo(locationB);

        if ( pureDistance < 1000 ) {

            int meters = (int) pureDistance;

            if (StaticData.isIsKm()) {

                return meters + " meters";

            }

            int yards = (int) (pureDistance*1.09361);

            return yards + " yards";

        } else {

            DecimalFormat formatter = new DecimalFormat("#.#");
            String km = formatter.format(pureDistance / 1000);

            if (StaticData.isIsKm()) {

                return km + " km";

            }
            return formatter.format( (Double.valueOf(km)*0.621371) ) + " miles";
        }
    }

}
