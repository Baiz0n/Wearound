package com.nothing.timing.wearound.bloc.data.extras;

import android.graphics.Bitmap;

import com.nothing.timing.wearound.bloc.data.sql.SqlNames;
import com.nothing.timing.wearound.tools.ImageConverter;

import java.util.ArrayList;

public class RecyclerUrlManager {

    private DBController controller;

    public RecyclerUrlManager(DBController fetcher) {

        this.controller = fetcher;
    }

    public ArrayList<Bitmap> getImageUrl(ArrayList<String> imgUrls) {

        ArrayList<Bitmap> images = new ArrayList<>();

        if (imgUrls == null && controller != null) {

            imgUrls = controller.getStringColumn(SqlNames.IMG_URL);
        }

        if ( imgUrls != null ) {

            for(String url: imgUrls) {

                images.add( ImageConverter.getBitmapFromURL(url) );
            }
        }

        return images;
    }

    public ArrayList<String> getImgUrls(String key) {

        ArrayList<String> imgUrls = controller.getStringColumn(SqlNames.IMG_URL);

        for( int i = 0; i < imgUrls.size(); i++ ) {

            imgUrls.set(i, imgUrls.get(i)+key);
        }

        return imgUrls;
    }

    public ArrayList<String> getShopUrls(String key) {

        ArrayList<String> shopIdUrl = controller.getStringColumn(SqlNames.SHOP_ID_URL);

        for ( int i = 0; i < shopIdUrl.size(); i++ ) {

            shopIdUrl.set(i, shopIdUrl.get(i) + key);
        }

        return shopIdUrl;
    }
}
