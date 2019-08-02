package com.nothing.timing.wearound.bloc.data.ClassData;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nothing.timing.wearound.bloc.data.extras.AroundChangingListContent;
import com.nothing.timing.wearound.tools.ApiHelper;
import com.nothing.timing.wearound.bloc.data.extras.results.NearbyResult;
import com.nothing.timing.wearound.bloc.data.extras.results.PlaceIdResult;
import com.nothing.timing.wearound.bloc.data.extras.results.ResultCursor;

import java.util.ArrayList;

public class AroundFragData {

    private static final String TAG = "AroundFragData";

    private String url;
    private String idUrl;
    private String key;
    private double deviceLat;
    private double deviceLng;

    private ArrayList<String> shopNames;
    private ArrayList<String> distances;
    private ArrayList<String> opens;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> ids;
    private ArrayList<String> imgUrls;

    private ArrayList<String> addresses = new ArrayList<>();

    private ArrayList<Bitmap> images;

    private ArrayList<String> shopUrls;
    private LatLng deviceLocation;

    public AroundFragData(String key, double lat, double lng) {

        this.key = key;
        deviceLat = lat;
        deviceLng = lng;

        deviceLocation = new LatLng(lat,lng);

        this.url = ApiHelper.getNearByUrl(lat,lng,key);
    }

    public void setSearchUrl(String query) {

        this.url = ApiHelper.getSearchedUrl(deviceLat,deviceLng,query,key);

        Log.e(TAG, "query is " + query);
    }

    public String getUrl() {
        return url;
    }

    public String getShopIdUrl() {
        return idUrl;
    }

    public void setShopId(String id) {
        this.idUrl = ApiHelper.getShopDetailsUrl(id,key);
    }

    public void setLists(NearbyResult nearbyResult) {

        ResultCursor resultCursor = new ResultCursor(nearbyResult,
                deviceLat, deviceLng, key);

        this.shopNames = resultCursor.getShopNames();
        this.distances = resultCursor.getDistances();
        this.latitudes = resultCursor.getLatitudes();
        this.longitudes = resultCursor.getLongitudes();
        this.ids = resultCursor.getIds();
        this.imgUrls = resultCursor.getImgUrls();
        this.opens = resultCursor.getOpens();
        this.shopUrls = resultCursor.getShopUrls();

        AroundChangingListContent changer = new AroundChangingListContent(
                resultCursor.getImgUrls());

        this.images = changer.getImages();
    }

    public ArrayList<String> getShopNames() {
        return shopNames;
    }

    public ArrayList<String> getDistances() {
        return distances;
    }

    public ArrayList<String> getOpens() {

        return opens;
    }

    public ArrayList<Double> getLatitudes() {
        return latitudes;
    }

    public ArrayList<Double> getLongitudes() {
        return longitudes;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public void addAddress(PlaceIdResult placeIdResult) {

        ResultCursor resultCursor = new ResultCursor(placeIdResult, deviceLocation);

        this.addresses.add(resultCursor.getAddress());
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public ArrayList<String> getShopUrls() { return shopUrls; }

    public LatLng getDeviceLocation() {
        return deviceLocation;
    }
}
