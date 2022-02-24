package com.example.home_restaurant.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;
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

public class ManageHotelOrder extends RecyclerView.Adapter<ManageHotelOrder.MyViewAdapter> {
    private Context context;
    private ArrayList<AddDishModel> orderContent;
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller")
            .child(FirebaseAuth.getInstance().getUid()).child("OnDelivery");

    public ManageHotelOrder(Context context, ArrayList<AddDishModel> orderContent) {
        this.context = context;
        this.orderContent = orderContent;
    }


    @NonNull
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.manage_hotel_orders, parent, false);
        return new MyViewAdapter(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter holder, int position) {
        AddDishModel addDishModel = orderContent.get(position);

        holder.dishTitle.setText(addDishModel.DishTitle);
        holder.dishPrice.setText(addDishModel.DishPrice);
        holder.customerName.setText("Customer Name: " + addDishModel.userName);

        holder.deliveryOption.setOnClickListener(v -> {
            holder.deliveryOption.setVisibility(View.VISIBLE);
            dataRef.child(addDishModel.ProductId).setValue("true");
        });
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(addDishModel.ProductId)) {
                    holder.deliveryOption.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(addDishModel.lat)
                    , Double.parseDouble(addDishModel.lng), 1);
            holder.customerAddress.setText("Customer address: " + addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return orderContent.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView dishTitle, dishPrice, customerName, customerAddress;
        Button deliveryOption;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);
            dishTitle = itemView.findViewById(R.id.dishTitle);
            dishPrice = itemView.findViewById(R.id.dishPrice);
            customerName = itemView.findViewById(R.id.customerName);
            deliveryOption = itemView.findViewById(R.id.deliveryOption);
            customerAddress = itemView.findViewById(R.id.customerAddress);
        }
    }
}
