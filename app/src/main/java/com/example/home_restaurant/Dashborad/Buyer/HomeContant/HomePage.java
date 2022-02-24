package com.example.home_restaurant.Dashborad.Buyer.HomeContant;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.home_restaurant.Adapters.showNearestHotel;
import com.example.home_restaurant.Models.UserAuthenticationModel;
import com.example.home_restaurant.Models.hotelRegistrationModel;
import com.example.home_restaurant.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomePage extends Fragment {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("UserData")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info").child("LatLag");
    private static final DatabaseReference hotelDataRef = FirebaseDatabase.getInstance().getReference("Seller");
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView setHotels;
    private ArrayList<hotelRegistrationModel> filterData = new ArrayList<>();
    private boolean update = true;
    private ScrollView scrolling;
    private int count = 0;
    private TextView countHotel;

    public HomePage() {
        // Required empty public constructor
    }


    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        refreshLayout = view.findViewById(R.id.refreshLayout);
        setHotels = view.findViewById(R.id.setHotels);
        scrolling = view.findViewById(R.id.scrolling);
        countHotel = view.findViewById(R.id.countHotel);

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.navColor));

        setHotels.setHasFixedSize(true);
        showNearestHotel nearestHotel = new showNearestHotel(getContext(), filterData);
        setHotels.setLayoutManager(new LinearLayoutManager(getContext()));
        setHotels.setAdapter(nearestHotel);
        refreshLayout.setEnabled(false);

        collectData(nearestHotel);


        scrolling.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollX == 0 && scrollY == 0) {
                refreshLayout.setEnabled(true);
            } else {
                refreshLayout.setEnabled(false);
            }
        });


        refreshLayout.setOnRefreshListener(() -> {
            filterData.clear();
            collectData(nearestHotel);
            refreshLayout.setRefreshing(false);
        });

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                , location.getLongitude(), 1);
                        UserAuthenticationModel authenticationModel = new UserAuthenticationModel(String.valueOf(addresses.get(0).getLatitude()),
                                String.valueOf(addresses.get(0).getLongitude()), "nulls");
                        dataRef.setValue(authenticationModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }

    }

    private void collectData(showNearestHotel nearestHotel) {

        filterData.clear();
        hotelDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.getRef().child("Hotel Data").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                hotelRegistrationModel hotelRegistrationModel = snapshot.getValue(hotelRegistrationModel.class);
                                dataRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            UserAuthenticationModel authenticationModel = snapshot.getValue(UserAuthenticationModel.class);
                                            try {
                                                float[] result = new float[1];
                                                Location.distanceBetween(Double.valueOf(authenticationModel.lat)
                                                        , Double.valueOf(authenticationModel.lag)
                                                        , Double.valueOf(hotelRegistrationModel.lat)
                                                        , Double.valueOf(hotelRegistrationModel.lag)
                                                        , result);

                                                if (result[0] / 1000 <= 4) {
                                                    filterData.add(hotelRegistrationModel);
                                                    countHotel.setText(String.valueOf(filterData.size()) + " restaurants around you");

                                                }
                                                if (update == true) {
                                                    nearestHotel.notifyDataSetChanged();
                                                    update = false;
                                                }
                                            } catch (Exception e) {
                                                Log.e("error", e.getMessage());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}