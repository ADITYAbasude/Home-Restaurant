package com.example.home_restaurant.Dashborad.Buyer.HomeContant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.home_restaurant.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.List;

public class User_to_hotel_map extends BottomSheetDialogFragment implements OnMapReadyCallback {
    private List<Address> location = null;
    LatLng hotelLocation;
//    private boolean locationPermission = false;
    MarkerOptions markerOptions;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_to_hotel_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


    }


//    private void getCurrentlocation() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(location -> {
//            if (location != null) {
//                Toast.makeText(getContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
//                SupportMapFragment mapFragment =
//                        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//                userlocation = new LatLng(location.getLatitude(), location.getLongitude());
//                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                    @Override
//                    public void onMapReady(@NonNull GoogleMap googleMap) {
//                        MarkerOptions marker = new MarkerOptions().position(userlocation).title("user");
//                        googleMap.addMarker(marker);
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 15));
//
//                        String url = getUrl(userlocation, markerOptions.getPosition(), "driving");
//                        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }


    //
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 44) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentlocation();
//            }
//        }
//
//    }
//
//
//    private String getUrl(LatLng user, LatLng hotel, String directionMode) {
//
//        String mode = "mode=" + directionMode;
//        String parameters = "origin=" + user.latitude + "," + user.longitude + "&" + "destination=" + hotel.latitude + "," + hotel.longitude + "&" + mode;
//        String url = "https://maps.googleapis.com/maps/api/directions/" + "json" + "?" + parameters + "&key=" + getString(R.string.YOUR_API_KEY);
//        return url;
//    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Geocoder geocoder = new Geocoder(getContext());
        try {
            location = geocoder.getFromLocationName(getActivity().getIntent().getStringExtra("hotelAddress"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address1 = location.get(0);
        hotelLocation = new LatLng(address1.getLatitude(), address1.getLongitude());
        markerOptions = new MarkerOptions().position(hotelLocation).title(getActivity().getIntent().getStringExtra("hotelAddress"));
        googleMap.addMarker(markerOptions.icon(BitmapFromVector(getContext(), R.drawable.ic_baseline_business_24)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hotelLocation, 15));
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
