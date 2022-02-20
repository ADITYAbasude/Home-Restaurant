package com.example.home_restaurant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.home_restaurant.Dashborad.Buyer.HomeContant.setHotelProductsInSeparate;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.Models.UserAuthenticationModel;
import com.example.home_restaurant.Models.hotelRegistrationModel;
import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showNearestHotel extends RecyclerView.Adapter<showNearestHotel.myViewAdapter> {

    private Context context;
    private ArrayList<hotelRegistrationModel> storeHotelData;
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("UserData")
            .child(FirebaseAuth.getInstance().getUid()).child("Info").child("LatLag");
    private ArrayList<String> storeImageUri = new ArrayList<>();
    private int count = 0;

    public showNearestHotel(Context context, ArrayList<hotelRegistrationModel> storeHotelData) {
        this.context = context;
        this.storeHotelData = storeHotelData;
    }

    @NonNull
    @Override
    public myViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.show_nearest_hotel, parent, false);
        return new myViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewAdapter holder, int position) {
        hotelRegistrationModel hotelRegistrationModel = storeHotelData.get(position);
        holder.HotelName.setText(hotelRegistrationModel.hotelName);
        holder.HotelDescription.setText(hotelRegistrationModel.hotelDescription);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    float[] result = new float[1];
                    UserAuthenticationModel authenticationModel = snapshot.getValue(UserAuthenticationModel.class);
                    Location.distanceBetween(Double.valueOf(authenticationModel.lat)
                            , Double.valueOf(authenticationModel.lag)
                            , Double.valueOf(hotelRegistrationModel.lat)
                            , Double.valueOf(hotelRegistrationModel.lag)
                            , result);
                    holder.distanceFromUser.setText((String.format("%.2f", result[0] / 1000)) + "km");

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


//        ----------------- set hotel rating -----------------

        FirebaseDatabase.getInstance().getReference("Seller").child(hotelRegistrationModel.SellerId)
                .child("Hotel Data").child("Hotel Rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("rating").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                        hotelRegistrationModel hotelRegistrationModel1 = snapshot2.getValue(hotelRegistrationModel.class);
                        int totalUser = (int) snapshot.getChildrenCount() - 1;
                        float totalRating = Float.parseFloat(hotelRegistrationModel1.rating);
                        float calculateRating = totalRating / totalUser;
                        holder.ratingOfHotel.setText(String.format("%.1f", calculateRating));
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


//        --------------------set food images -------------------
        FirebaseDatabase.getInstance().getReference("Seller").child(hotelRegistrationModel.SellerId)
                .child("Food Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AddDishModel dishModel = snapshot.getValue(AddDishModel.class);
                            storeImageUri.add(dishModel.DishImage);
                            Glide.with(context).load(storeImageUri.get(0)).into(holder.setFoodImage);
                            holder.backward.setOnClickListener(v -> {
                                if (count < 0) {
                                    count = storeImageUri.size() - 1;
                                    Glide.with(context).load(storeImageUri.get(count)).into(holder.setFoodImage);
                                } else {
                                    Glide.with(context).load(storeImageUri.get(count)).into(holder.setFoodImage);
                                    count--;
                                }
                            });
                            holder.forward.setOnClickListener(v -> {
                                if (count > storeImageUri.size() - 1) {
                                    count = 0;
                                    Glide.with(context).load(storeImageUri.get(count)).into(holder.setFoodImage);
                                } else {
                                    Glide.with(context).load(storeImageUri.get(count)).into(holder.setFoodImage);
                                    count++;
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        -------------- item click ----------------

        holder.itemClick.setOnClickListener(v -> {
            Intent intent = new Intent(context, setHotelProductsInSeparate.class);
            intent.putExtra("SellerKey", hotelRegistrationModel.SellerId);
            intent.putExtra("hotelName", hotelRegistrationModel.hotelName);
            intent.putExtra("hotelDescription", hotelRegistrationModel.hotelDescription);
            intent.putExtra("hotelAddress", hotelRegistrationModel.hotelAddress);
            intent.putExtra("km", holder.distanceFromUser.getText());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return storeHotelData.size();
    }

    public class myViewAdapter extends RecyclerView.ViewHolder {
        TextView HotelName, HotelDescription, distanceFromUser, backward, forward, ratingOfHotel;
        ImageView setFoodImage;
        ConstraintLayout itemClick;

        public myViewAdapter(@NonNull View itemView) {
            super(itemView);
            HotelName = itemView.findViewById(R.id.HotelName);
            HotelDescription = itemView.findViewById(R.id.HotelDescription);
            distanceFromUser = itemView.findViewById(R.id.DistanceFromUser);
            setFoodImage = itemView.findViewById(R.id.setFoodImage);
            backward = itemView.findViewById(R.id.backward);
            forward = itemView.findViewById(R.id.forward);
            itemClick = itemView.findViewById(R.id.itemClick);
            ratingOfHotel = itemView.findViewById(R.id.ratingOfHotel);
        }
    }
}
