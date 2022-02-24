package com.example.home_restaurant.Dashborad.Seller.HotelOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_restaurant.Adapters.ManageHotelOrder;
import com.example.home_restaurant.Dashborad.Seller.hotelProducts;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderProducts extends Fragment {
    private RecyclerView setHotelOrders;
    private ArrayList<AddDishModel> orderContent = new ArrayList<>();
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller")
            .child(FirebaseAuth.getInstance().getUid()).child("Orders");
    private Button goBack;
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
        setHotelOrders = view.findViewById(R.id.setHotelOrders);
        goBack = view.findViewById(R.id.goBack);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        setHotelOrders.setHasFixedSize(true);
        ManageHotelOrder manageHotelOrder = new ManageHotelOrder(getContext() , orderContent);
        setHotelOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        setHotelOrders.setAdapter(manageHotelOrder);

        Animation move = AnimationUtils.loadAnimation(getContext(), R.anim.animation_of_btn);
        goBack.startAnimation(move);

        goBack.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            hotelProducts hotelProducts = new hotelProducts();
            ft.replace(R.id.fragmentContainerView2, hotelProducts).commit();
        });

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                AddDishModel addDishModel = snapshot.getValue(AddDishModel.class);
                                orderContent.add(addDishModel);
                            }
                            manageHotelOrder.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}