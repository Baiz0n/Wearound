package com.nothing.timing.wearound.bloc.data.sql;

public class SqlNames {

    public static final String DATABASE_NAME = "wearound_db";

    public static String getHistoryTableName() {

        return "searched";
    }

    public static String getFavouritesTableName() {

        return "FavouritesFrag";
    }

    public static final String SHOP_NAME = "shop";
    public static final String ADDRESS = "address";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SHOP_ID_URL = "shop_id";
    public static final String IMG_URL = "img_url";
    public static final String IMAGES = "images";
}
