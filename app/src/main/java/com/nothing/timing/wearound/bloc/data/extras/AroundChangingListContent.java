package com.nothing.timing.wearound.bloc.data.extras;

import android.graphics.Bitmap;

import com.nothing.timing.wearound.tools.ImageConverter;

import java.util.ArrayList;

public class AroundChangingListContent {

    private ArrayList<String> imgUrls;

    public AroundChangingListContent(ArrayList<String> imgUrls) {

        this.imgUrls = imgUrls;
    }

    public ArrayList<Bitmap> getImages() {

        ArrayList<Bitmap> images = new ArrayList<>();

        for (String url: imgUrls) {

            images.add(ImageConverter.getBitmapFromURL(url));
        }

        return images;
    }
}
