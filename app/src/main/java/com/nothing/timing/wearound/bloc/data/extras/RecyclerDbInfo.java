package com.nothing.timing.wearound.bloc.data.extras;

import android.graphics.Bitmap;
import android.util.Log;

import com.nothing.timing.wearound.bloc.data.sql.SqlNames;
import com.nothing.timing.wearound.tools.ImageConverter;
import com.nothing.timing.wearound.ui.fragments.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecyclerDbInfo {

    private final static String TAG = "DB Info";

    private DBController controller;

    public RecyclerDbInfo (DBController fetcher) {

        this.controller = fetcher;
    }

    public ArrayList<String> getShopNames() {

        return controller.getStringColumn(SqlNames.SHOP_NAME);
    }

    public ArrayList<String> getAddresses() {

        return controller.getStringColumn(SqlNames.ADDRESS);
    }

    public ArrayList<Double> getLatitudes() {

        ArrayList<Double> lats = new ArrayList<>();

        for (String stringLat : controller.getStringColumn(SqlNames.LATITUDE) ) {

            lats.add(Double.parseDouble(stringLat));
        }
        return lats;
    }

    public ArrayList<Double> getLongitudes() {

        ArrayList<Double> lngs = new ArrayList<>();

        for (String stringLat : controller.getStringColumn(SqlNames.LONGITUDE)) {

            lngs.add(Double.parseDouble(stringLat));
        }
        return lngs;
    }

    public ArrayList<Bitmap> getImages() {

        ArrayList<Bitmap> images = new ArrayList<>();

        for (String blob: controller.getStringColumn(SqlNames.IMAGES)) {

            images.add(ImageConverter.getBitmapFromByte(blob));
        }

        if (images.get(0) == null ) {

            Log.e(TAG, "images are empty");
        }
        return images;
    }

}
