package com.example.home_restaurant.Dashborad.Buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_restaurant.Adapters.UserOrder;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderCart extends Fragment {

    private RecyclerView setUserOrder;
    private ArrayList<AddDishModel> orderData = new ArrayList<>();
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("UserData")
            .child(FirebaseAuth.getInstance().getUid()).child("Orders");

    public OrderCart() {
        // Required empty public constructor
    }


    public static OrderCart newInstance(String param1, String param2) {
        OrderCart fragment = new OrderCart();
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
        return inflater.inflate(R.layout.fragment_order_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order card");


        setUserOrder = view.findViewById(R.id.userOrder);


        setUserOrder.setHasFixedSize(true);
        UserOrder userOrder = new UserOrder(getContext(), orderData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        setUserOrder.setLayoutManager(linearLayoutManager);
        setUserOrder.setAdapter(userOrder);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        AddDishModel addDishModel = ds.getValue(AddDishModel.class);
                        orderData.add(addDishModel);
                    }
                    userOrder.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}