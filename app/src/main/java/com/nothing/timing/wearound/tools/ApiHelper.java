package com.nothing.timing.wearound.tools;

import android.text.StaticLayout;
import android.util.Log;

import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;

public class ApiHelper {

    private static final String NEAR_BY_URL_1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String NEAR_BY_URL_2 = "&type=clothing_store&key=";

    private static final String SHOP_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";

    private static final String IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    private static final String SEARCH_URL_1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String SEARCHED_URL_2 = "&type=clothing_store&keyword=";

    private static final String AND_KEY = "&key=";

    private static final String AND_RADIUS = "&radius=";


    public static String getNearByUrl(double lat, double lng, String key) {

        return NEAR_BY_URL_1
                + lat + ","
                + lng
                + AND_RADIUS + StaticData.getRadius()
                + NEAR_BY_URL_2
                + key;
    }

    public static String getShopDetailsUrl(String id, String key) {

        return SHOP_DETAILS_URL
                + id
                + AND_KEY
                + key;
    }

    public static String getImageUrl(String imageId, String key) {

        return IMAGE_URL
                + imageId
                + AND_KEY
                + key;
    }

    public static String getSearchedUrl(double lat, double lng, String search, String key) {

        return SEARCH_URL_1
                + lat + ","
                + lng
                + AND_RADIUS + StaticData.getRadius()
                + SEARCHED_URL_2
                + search.replaceAll(" ", "%20")
                + AND_KEY
                + key;
    }
}
