package fr.saveyourdreams.app.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import fr.saveyourdreams.app.R;
import fr.saveyourdreams.app.utils.AsyncCallback;

public class LocationService {

    public interface CoordinateCallback extends AsyncCallback<LatLng> {
    }

    public interface AddressCallback extends AsyncCallback<Address> {
    }

    @SuppressLint("MissingPermission")
    public static void getCurrentCoordinate(Context context, CoordinateCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (PermissionService.has(context, Manifest.permission.ACCESS_FINE_LOCATION) && PermissionService.has(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.d("SAVE_YOUR_DREAMS", "Na pas les permissions pour ça");
            errorCallback.get(context.getString(R.string.LOCATION_MISSING_PERMISSION));
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000,
                100, location -> {
                    callback.get(new LatLng(location.getLatitude(), location.getLongitude()));
                });
    }

    public static void getCurrentAddress(Context context, AddressCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        getCurrentCoordinate(context, (coords) -> {
            getCoordsAddress(context, coords, callback, errorCallback);
        }, errorCallback);
    }

    public static void getCoordsAddress(Context context, LatLng coords, AddressCallback callback, AsyncCallback.ErrorCallback errorCallback) {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gc.getFromLocation(coords.latitude, coords.longitude, 1);
            if (addresses.isEmpty()) {
                errorCallback.get(context.getString(R.string.LOCATION_ADDRESS_NOT_FOUND));
            } else {
                callback.get(addresses.get(0));
            }
        } catch (IOException e) {
            errorCallback.get(context.getString(R.string.LOCATION_ERROR_GEOCODER));
            e.printStackTrace();
        }
    }
}
