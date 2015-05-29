package com.relianceit.relianceorder.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by sura on 5/29/15.
 */
public class ROSLocationService {

    public static final String TAG = ROSLocationService.class.getSimpleName();

    private LocationManager locationManager = null;
    LocationListener locationListener = null;
    private int checkCount = 0;

    public static interface ROSLocationServiceListener {
        public abstract void onLocationFound(Location location);
        public abstract void onLocationFailed();
    }

    public void getCurrentLocation(Context context, final ROSLocationServiceListener listener) {

        listener.onLocationFound(null);

//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//        String locationProvider = LocationManager.NETWORK_PROVIDER;
//
//        locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                locationFound(location, listener);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                Log.i(TAG, "Location onStatusChanged");
//            }
//
//            public void onProviderEnabled(String provider) {
//                Log.i(TAG, "Location onProviderEnabled: " + provider);
//            }
//
//            public void onProviderDisabled(String provider) {
//                Log.i(TAG, "Location onProviderDisabled: " + provider);
//            }
//        };
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    private void locationFound(Location location, final ROSLocationServiceListener listener) {

        if (checkCount > 3 || location != null) {
            checkCount = 0;
            locationManager.removeUpdates(locationListener);
            listener.onLocationFound(location);
        }else {
            checkCount++;
        }
    }
}
