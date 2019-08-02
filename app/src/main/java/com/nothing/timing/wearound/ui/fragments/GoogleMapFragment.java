package com.nothing.timing.wearound.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nothing.timing.wearound.bloc.data.ClassData.GoogleMapsData;
import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.tools.DialogAndToast;
import com.nothing.timing.wearound.tools.PermissionProvider;
import com.nothing.timing.wearound.ui.MainActivity;

public class GoogleMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleMapsData data;
    private GoogleMap mMap;

    private Location mLocation;
    private Marker marker;
    private OnLatLngFinnitoListener latlngListener;

    public interface OnLatLngFinnitoListener {

       void setOnLatLngFinnitoListener(double lat, double lng);
    }

    public void setLists(Lists lists) {

        latlngListener = lists;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        data = new GoogleMapsData();

        getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady: Running");

        mMap = googleMap;

            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(10, 10))
                    .title("Hello world"));
    }

    private void moveCamera(LatLng latLng) {
        Log.e(TAG, "initMap: moving camera to => lat: " + latLng.latitude
                + ", long: " + latLng.longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, data.getDefaultZoom()));

    }

    public void initShopLocation(LatLng latLng, String shopName) {

        if ( marker != null ) {

            marker.remove();
        }

        moveCamera(latLng);

        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(shopName));
    }

    private void getDeviceLocation() {
        Log.e(TAG,"getDeviceLocation: getting the device location");

        try {
            Task location = data.getClient(getContext()).getLastLocation();
            location.addOnCompleteListener(locationListener);

        } catch (SecurityException e) {

            Log.e(TAG, "getDeviceLocation, SecurityException caught: "+e );
        }
    }

    private OnCompleteListener locationListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {

            if (task.isSuccessful() && task.getResult() != null) {

                Log.e(TAG, "locationListener: found location");

                mLocation = (Location) task.getResult();

                data.setCurrentLocation(mLocation);

                moveCamera(data.getLatLng());

                if (PermissionProvider.isNetworkAvailable(getContext())) {

                    if (latlngListener != null ) {

                        latlngListener.setOnLatLngFinnitoListener(
                                data.getLatLng().latitude,
                                data.getLatLng().longitude);
                    }

                } else {

                    DialogAndToast.shortToast(getContext(), "No internet connection");
                }

            } else {

                DialogAndToast.shortToast(getContext(), "Unable to get current location");

                StaticData.setIsFunctional(false);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        data.setLocationPermission(data.getPermissionsResult(requestCode, grantResults));
    }

}