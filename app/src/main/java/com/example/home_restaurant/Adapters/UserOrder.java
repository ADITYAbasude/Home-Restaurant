package com.example.home_restaurant.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.Models.hotelRegistrationModel;
import com.example.home_restaurant.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserOrder extends RecyclerView.Adapter<UserOrder.MyViewadapter> {
    private Context context;
    private ArrayList<AddDishModel> orderData;
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller");

    public UserOrder(Context context , ArrayList<AddDishModel> orderData) {
        this.context = context;
        this.orderData = orderData;
    }


    @NonNull
    @Override
    public MyViewadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_order , parent , false);
        return new MyViewadapter(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewadapter holder, int position) {
        AddDishModel addDishModel = orderData.get(position);

        holder.setHotelNameInOrderCard.setText(addDishModel.HotelId);
        holder.setDishValue.setText("Amount: " + addDishModel.DishPrice);
        holder.setDishName.setText("Dish Name: " + addDishModel.DishTitle);


        dataRef.child(addDishModel.HotelId).child("Hotel Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    hotelRegistrationModel registrationModel = snapshot.getValue(hotelRegistrationModel.class);
                    holder.setHotelNameInOrderCard.setText(registrationModel.hotelName);
                    holder.setHotelContactNumber.setText("Contact number: " + registrationModel.hotelContact);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        dataRef.child(addDishModel.HotelId).child("OnDelivery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(addDishModel.ProductId)){
                    } else {
                        holder.orderStatus.setText("Pending");
                        holder.orderStatus.setTextColor(Color.parseColor("#FF5F68"));
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class MyViewadapter extends RecyclerView.ViewHolder {
        TextView setHotelNameInOrderCard , setDishValue , setDishName , setHotelContactNumber , orderStatus;
        public MyViewadapter(@NonNull View itemView) {
            super(itemView);
            setHotelNameInOrderCard = itemView.findViewById(R.id.setHotelNameInOrderCard);
            setDishName = itemView.findViewById(R.id.setDishName);
            setDishValue = itemView.findViewById(R.id.setDishValue);
            setHotelContactNumber = itemView.findViewById(R.id.setHotelContactNumber);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }
}
