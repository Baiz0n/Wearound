package com.nothing.timing.wearound.bloc.data.extras.results;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.nothing.timing.wearound.tools.ApiHelper;
import com.nothing.timing.wearound.tools.LatLngFormula;

import java.util.ArrayList;

public class ResultCursor {

    private static final String TAG = "Result Cursor";

   /*
    * add blobs to sql
    */

    private ArrayList<String> shopNames;
    private ArrayList<String> distances;
    private ArrayList<String> opens;
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;
    private ArrayList<String> ids;
    private ArrayList<String> imgUrls;
    private ArrayList<String> shopUrls;


    public ResultCursor(NearbyResult results, double deviceLat,
                        double deviceLng, String key) {

        shopNames = new ArrayList<>();
        distances = new ArrayList<>();
        opens = new ArrayList<>();
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        ids = new ArrayList<>();
        imgUrls = new ArrayList<>();
        shopUrls = new ArrayList<>();

        double shopLat;
        double shopLng;

        ResultChecker checker = new ResultChecker();

        for(NearbyResult.ResultsBean result : results.getResults()) {

            shopLat = result.getGeometry().getLocation().getLat();
            shopLng = result.getGeometry().getLocation().getLng();

            String distance = LatLngFormula.getDistanceText(deviceLat,
                    deviceLng,
                    shopLat,
                    shopLng);

            shopNames.add(result.getName());
            distances.add(distance);
            latitudes.add(shopLat);
            longitudes.add(shopLng);
            ids.add(result.getPlace_id());

            shopUrls.add( ApiHelper.getShopDetailsUrl(result.getPlace_id(),key) );

            opens.add(checker.getIsOpenResult(result.getOpening_hours(),null));
            imgUrls.add(checker.getImgUrlResult(result, key));
        }
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

    public ArrayList<String> getShopUrls() { return shopUrls; }


    private ResultChecker checker;
    private PlaceIdResult placeIdResult;
    private LatLng deviceLocation;

    public ResultCursor(PlaceIdResult placeIdResult, LatLng deviceLocation) {

        this.placeIdResult = placeIdResult;
        this.deviceLocation = deviceLocation;
        checker = new ResultChecker();
    }

    public String getAddress() {

        return placeIdResult.getResult().getFormatted_address();
    }

    public String isOpen() {

        return checker.getIsOpenResult(null,placeIdResult.getResult().getOpening_hours());
    }

    public String getDistance() {

        double shopLat = placeIdResult.getResult().getGeometry().getLocation().getLat();
        double shopLng = placeIdResult.getResult().getGeometry().getLocation().getLng();

        return LatLngFormula.getDistanceText(
                deviceLocation.latitude,
                deviceLocation.longitude,
                shopLat,
                shopLng);
    }
}
