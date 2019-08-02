package com.nothing.timing.wearound.bloc.data.ClassData;

public class StaticData {

    public static final String NETWORK_CONNECTION = "network_connection";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String OPEN = "Open";
    public static final String CLOSED = "Closed";

    private static final String PREF_TITLE = "pref_title";

    public static String getPrefTitle() {

        return PREF_TITLE;
    }

    private static final String PREF_RADIUS_KEY = "pref_radius";

    public static String getPrefRadiusKey() {

        return PREF_RADIUS_KEY;
    }

    private static final String PREF_DISTANCE_TYPE_KEY = "distance_type";

    public static String getPrefDistanceTypeKey() {

        return PREF_DISTANCE_TYPE_KEY;
    }

    private static final String KM = "km";

    public static String getKmKey() {

        return KM;
    }

    private static final String MILES = "miles";

    public static String getMilesKey() {

        return MILES;
    }

    private static boolean isKm;

    public static boolean isIsKm() {
        return isKm;
    }

    public static void setIsKm(boolean isKm) {
        StaticData.isKm = isKm;
    }

    public static int radius;

    public static int getRadius() {
        return radius;
    }

    public static void setRadius(int radius) {
        StaticData.radius = radius;
    }

    private static boolean isFunctional = true;

    public static void setIsFunctional(boolean b) {

        isFunctional = b;
    }

    public static boolean getIsFunctional() {

        return isFunctional;
    }

    private static int displayWidth = 0;

    public static void setDisplayWidth(int width) {

        displayWidth = width;
    }

    public static int getDisplayWidth() {

        return displayWidth;
    }
}
