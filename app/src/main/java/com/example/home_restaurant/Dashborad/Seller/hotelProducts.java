package com.example.home_restaurant.Dashborad.Seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_restaurant.Adapters.Your_Product;
import com.example.home_restaurant.Dashborad.Seller.HotelOrder.OrderProducts;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class hotelProducts extends Fragment {

    private FloatingActionButton AddYourProduct;
    private RecyclerView ShowProducts;
    private ArrayList<AddDishModel> storeProducts = new ArrayList<AddDishModel>();
    private static final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller")
            .child(FirebaseAuth.getInstance().getUid()).child("Food Product");
    private static Button hotelOrderBtn;

    public hotelProducts() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static hotelProducts newInstance(String param1, String param2) {
        hotelProducts fragment = new hotelProducts();
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
        return inflater.inflate(R.layout.fragment_hotel_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Product");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        AddYourProduct = view.findViewById(R.id.AddYourProduct);
        ShowProducts = view.findViewById(R.id.ShowProducts);
        hotelOrderBtn = view.findViewById(R.id.hotelOrderBtn);

        AddYourProduct.setOnClickListener(v -> {
            addYourProduct addYourProduct = new addYourProduct();
            addYourProduct.show(getActivity().getSupportFragmentManager(), addYourProduct.getTag());
        });

        ShowProducts.setHasFixedSize(true);
        Your_Product your_product = new Your_Product(getContext(), storeProducts);
        ShowProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        ShowProducts.setAdapter(your_product);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    storeProducts.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        AddDishModel addDishModel = ds.getValue(AddDishModel.class);
                        storeProducts.add(addDishModel);
                    }
                    your_product.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        hotelOrderBtn.setOnClickListener(v -> {
            OrderProducts orderProducts = new OrderProducts();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragmentContainerView2 , orderProducts).commit();

        });

    }


}