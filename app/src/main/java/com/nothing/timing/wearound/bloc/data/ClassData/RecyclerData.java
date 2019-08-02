package com.nothing.timing.wearound.bloc.data.ClassData;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nothing.timing.wearound.bloc.data.extras.DBController;
import com.nothing.timing.wearound.bloc.data.extras.RecyclerUrlManager;
import com.nothing.timing.wearound.bloc.data.extras.RecyclerDbInfo;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerData {

    private Context context;

    private ArrayList<String> shopNames;
    private ArrayList<String> addresses;
    private ArrayList<String> distances;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<Bitmap> images;
    private ArrayList<String> opens;
    private ArrayList<String> shopIdUrl;
    private ArrayList<String> imgUrls;

    private ArrayList<Boolean> recursionSwitch;

    private RecyclerUrlManager urlManager;
    private RecyclerDbInfo dbInfo;

    private LatLng deviceLocation;

    private boolean firstTimeInit = true;

    public RecyclerData(DBController fetcher) {

        urlManager = new RecyclerUrlManager(fetcher);
        dbInfo = new RecyclerDbInfo(fetcher);

        opens = new ArrayList<>();
        addresses = new ArrayList<>();
        images = new ArrayList<>();
        distances = new ArrayList<>();
    }

    public LatLng getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(LatLng deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public ArrayList<Bitmap> getBitmapImagesFromUrl(ArrayList<String> imgUrls) {

        return urlManager.getImageUrl(imgUrls);
    }

    public ArrayList<String> getShopNames() {

        if (shopNames == null) {

            shopNames = dbInfo.getShopNames();
            Collections.reverse(shopNames);
        }

        if (firstTimeInit) {

            recursionSwitch = new ArrayList<>();

            for (int i = 0; i < shopNames.size(); i++) {

                recursionSwitch.add(false);
            }

            firstTimeInit = false;
        }

        return shopNames;
    }

    public void setShopNames(ArrayList<String> shopNames) {
        this.shopNames = shopNames;
    }

    public ArrayList<String> getAddresses(boolean isAround) {

        if (addresses == null || addresses.isEmpty() && !isAround) {

            addresses = dbInfo.getAddresses();
            Collections.reverse(addresses);
        }

        return addresses;
    }

    public void setAddresses(ArrayList<String> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<String> getDistances() {

        return distances;
    }

    public void addAddress(int position,String address) {

        if (addresses == null ) {

            addresses = new ArrayList<>();
        }

        addresses.add(position, address);
    }

    public void setDistances(ArrayList<String> distances) {
        this.distances = distances;
    }

    public void addDistance(int position, String distance) {

        if (distances == null) {

            distances = new ArrayList<>();
        }

        distances.add(position, distance);
    }

    public ArrayList<Double> getLatitudes() {

        if (latitudes == null) {

            latitudes = dbInfo.getLatitudes();
            Collections.reverse(latitudes);
        }

        return latitudes;
    }

    public void setLatitudes(ArrayList<Double> latitudes) {
        this.latitudes = latitudes;
    }

    public ArrayList<Double> getLongitudes() {

        if (longitudes == null) {

            longitudes = dbInfo.getLongitudes();
            Collections.reverse(longitudes);
        }

        return longitudes;
    }

    public void setLongitudes(ArrayList<Double> longitudes) {
        this.longitudes = longitudes;
    }

    public ArrayList<Bitmap> getImages() {

        if (images == null || images.isEmpty()) {

            images = dbInfo.getImages();
            Collections.reverse(images);
        }

        return images;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    public ArrayList<String> getImgUrls(String key) {

        if (imgUrls == null) {

            imgUrls = urlManager.getImgUrls(key);
        }
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<String> getOpens() {
        return opens;
    }

    public void setOpens(ArrayList<String> opens) {
        this.opens = opens;
    }

    public void addOpen(int position, String open) {

        if (opens == null ) {

            opens = new ArrayList<>();
        }

        opens.add(position, open);
    }

    public ArrayList<String> getShopIdUrls(String key) {

        if ( shopIdUrl == null ) {

            shopIdUrl = urlManager.getShopUrls("");
            Collections.reverse(shopIdUrl);
        }
        return shopIdUrl;
    }

    public void setShopIdUrls(ArrayList<String> shopIdUrl) {
        this.shopIdUrl = shopIdUrl;
    }

    public void setRecursionSwitch(int position) {

        recursionSwitch.set(position, true);
    }

    public boolean getRecursionSwitch(int position) {
        return recursionSwitch.get(position);
    }

    public void removeListsPosition(int position) {

        shopNames.remove(position);
        addresses.remove(position);
        longitudes.remove(position);
        latitudes.remove(position);
        images.remove(position);

        if (shopIdUrl != null && !shopIdUrl.isEmpty()) {

            shopIdUrl.remove(position);
        }

        if (imgUrls != null && !imgUrls.isEmpty()) {

            imgUrls.remove(position);
        }

        if (opens != null && !opens.isEmpty()) {

            opens.remove(position);
        }

        if (distances != null && distances.isEmpty()) {

            distances.remove(position);
        }

    }
}
