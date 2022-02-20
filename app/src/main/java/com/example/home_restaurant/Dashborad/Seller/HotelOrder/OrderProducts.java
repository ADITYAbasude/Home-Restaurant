package com.example.home_restaurant.Dashborad.Seller.HotelOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.home_restaurant.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OrderProducts extends BottomSheetDialogFragment {

    public OrderProducts() {
        // Required empty public constructor
    }

    public static OrderProducts newInstance(String param1, String param2) {
        OrderProducts fragment = new OrderProducts();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_products, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}