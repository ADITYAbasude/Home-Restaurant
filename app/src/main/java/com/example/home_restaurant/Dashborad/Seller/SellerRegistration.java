package com.example.home_restaurant.Dashborad.Seller;

import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.home_restaurant.Models.hotelRegistrationModel;
import com.example.home_restaurant.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SellerRegistration extends Fragment {

    private int Hour, Minute;
    private TextInputEditText chooseTime, hotelAddress, hotelDescription, hotelName, hotelContact;
    private Button ContinueHotelRegistration;
    private final static DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller")
            .child(FirebaseAuth.getInstance().getUid()).child("Hotel Data");
    private GoogleMap map;
    private List<Address> location = null;

    public SellerRegistration() {
        // Required empty public constructor
    }

    public static SellerRegistration newInstance(String param1, String param2) {
        SellerRegistration fragment = new SellerRegistration();
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
        return inflater.inflate(R.layout.fragment_seller_registration, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chooseTime = view.findViewById(R.id.chooseTime);
        hotelAddress = view.findViewById(R.id.hotelAddress);
        hotelName = view.findViewById(R.id.hotelName);
        hotelDescription = view.findViewById(R.id.hotelDescription);
        ContinueHotelRegistration = view.findViewById(R.id.ContinueHotelRegistration);
        hotelContact = view.findViewById(R.id.hotelContact);

        chooseTime.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener pickTime = (view1, hourOfDay, minute) -> {
                this.Hour = hourOfDay;
                this.Minute = minute;
                Toast.makeText(getContext(), String.format(Locale.getDefault(), "%02d:%02d", this.Hour, this.Minute), Toast.LENGTH_SHORT).show();
                chooseTime.setText(String.format(Locale.getDefault(), "%02d:%02d", Hour, Minute));
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), pickTime, this.Hour, this.Minute, false);
            timePickerDialog.setTitle("Choose time");
            timePickerDialog.show();
        });


        ContinueHotelRegistration.setOnClickListener(v -> {
            String name = hotelName.getText().toString();
            String address = hotelAddress.getText().toString();
            String description = hotelDescription.getText().toString();
            String timing = chooseTime.getText().toString();
            String contactNumber = hotelContact.getText().toString();

            if (name.length() == 0) {
                hotelName.requestFocus();
                hotelName.setError("Required");
            } else if (address.length() == 0) {
                hotelAddress.requestFocus();
                hotelAddress.setError("Required");
            } else if (description.length() == 0) {
                hotelDescription.requestFocus();
                hotelDescription.setError("Required");
            } else if (timing.length() == 0) {
                chooseTime.requestFocus();
                chooseTime.setError("Required");
            } else if (contactNumber.length() == 0 || contactNumber.matches("[0-9] {10}")) {
                hotelContact.requestFocus();
                hotelContact.setError("Required");
            } else {

                Geocoder geocoder = new Geocoder(getContext());

                try {
                    location = geocoder.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address1 = location.get(0);

                hotelRegistrationModel hotelRegistrationModel = new hotelRegistrationModel(FirebaseDatabase.getInstance().getReference().push().getKey(),
                        FirebaseAuth.getInstance().getUid(), name, description, address, timing, contactNumber
                        , String.valueOf(address1.getLatitude()), String.valueOf(address1.getLongitude()));
                dataRef.setValue(hotelRegistrationModel);

                hotelRegistrationModel hotelModel = new hotelRegistrationModel("0.0");

                dataRef.child("Hotel Rating").child("rating").setValue(hotelModel);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hotelProducts hotelProducts = new hotelProducts();
                fragmentTransaction.replace(R.id.fragmentContainerView2, hotelProducts).commit();
            }

        });


    }
}