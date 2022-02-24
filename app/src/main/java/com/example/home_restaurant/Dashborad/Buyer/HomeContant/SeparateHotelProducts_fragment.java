package com.example.home_restaurant.Dashborad.Buyer.HomeContant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_restaurant.Adapters.buyProducts;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.Models.hotelRegistrationModel;
import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeparateHotelProducts_fragment extends Fragment {

    private TextView hotelName, hotelDescription, hotelAddress, post, heading, setRating, numberOfFeedback;
    private RecyclerView productItem;
    private ArrayList<AddDishModel> products = new ArrayList<>();
    private ScrollView scroll;
    private RatingBar ratingBar;
    private ConstraintLayout ratingLayout;


    public SeparateHotelProducts_fragment() {
        // Required empty public constructor
    }


    public static SeparateHotelProducts_fragment newInstance(String param1, String param2) {
        SeparateHotelProducts_fragment fragment = new SeparateHotelProducts_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_separate_hotel_products, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hotelName = view.findViewById(R.id.setHotelName);
        hotelDescription = view.findViewById(R.id.setHotelDescription);
        hotelAddress = view.findViewById(R.id.setHotelAddress);
        productItem = view.findViewById(R.id.productItem);
        scroll = view.findViewById(R.id.scroll);
        ratingBar = view.findViewById(R.id.ratingBar);
        post = view.findViewById(R.id.post);
        heading = view.findViewById(R.id.heading);
        setRating = view.findViewById(R.id.setRating);
        ratingLayout = view.findViewById(R.id.ratingLayout);
        numberOfFeedback = view.findViewById(R.id.numberOfFeedback);


        hotelName.setText(getActivity().getIntent().getStringExtra("hotelName"));
        hotelDescription.setText(getActivity().getIntent().getStringExtra("hotelDescription"));
        hotelAddress.setText(getActivity().getIntent().getStringExtra("hotelAddress") + ", " +
                getActivity().getIntent().getStringExtra("km"));


        if (hotelAddress.getText().toString() != null) {
            hotelAddress.setOnClickListener(v -> {
                User_to_hotel_map userToHotelMap = new User_to_hotel_map();
                userToHotelMap.show(getActivity().getSupportFragmentManager(), userToHotelMap.getTag());
            });
        }


        productItem.setHasFixedSize(true);
        buyProducts buyProducts = new buyProducts(getContext(), products);
        productItem.setLayoutManager(new LinearLayoutManager(getContext()));
        productItem.setAdapter(buyProducts);


        FirebaseDatabase.getInstance().getReference("Seller").child(getActivity().getIntent().getStringExtra("SellerKey"))
                .child("Food Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AddDishModel dishModel = snapshot.getValue(AddDishModel.class);
                            products.add(dishModel);
                            buyProducts.notifyDataSetChanged();
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


//    --------------------rating bar concept------------------------


        FirebaseDatabase.getInstance().getReference("Seller").child(getActivity().getIntent().getStringExtra("SellerKey"))
                .child("Hotel Data").child("Hotel Rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.getRef().child("rating").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot2) {

                        hotelRegistrationModel hotelModel = snapshot2.getValue(hotelRegistrationModel.class);
                        float totalRating2 = Float.parseFloat(hotelModel.rating);
                        int totalUser2 = (int) snapshot.getChildrenCount() - 1;
                        float calculateRating2 = totalRating2 / totalUser2;
                        numberOfFeedback.setText(String.valueOf(totalUser2));
                        setRating.setText(String.format("%.1f", calculateRating2));

                        if (!snapshot.hasChild(FirebaseAuth.getInstance().getUid())) {
                            setRating.setText("0.0");
                            post.setOnClickListener(v -> {
                                ratingLayout.setVisibility(View.VISIBLE);
                                float ratingCount = ratingBar.getRating();
                                int totalUser = (int) snapshot.getChildrenCount();
                                float totalRating = Float.parseFloat(hotelModel.rating) + ratingCount;
                                float calculateRating = totalRating / totalUser;

                                numberOfFeedback.setText(String.valueOf(totalUser));
                                setRating.setText(String.format("%.1f", calculateRating));
                                hotelRegistrationModel hotelRegistrationModel = new hotelRegistrationModel(String.valueOf(totalRating));
                                FirebaseDatabase.getInstance().getReference("Seller").child(getActivity().getIntent().getStringExtra("SellerKey"))
                                        .child("Hotel Data").child("Hotel Rating").child(FirebaseAuth.getInstance().getUid())
                                        .setValue("true");
                                snapshot2.getRef().setValue(hotelRegistrationModel);
                            });
                        } else {
                            ratingLayout.setVisibility(View.GONE);
                            float totalRating = Float.parseFloat(hotelModel.rating);
                            int totalUser = (int) snapshot.getChildrenCount() - 1;
                            float calculateRating = totalRating / totalUser;
                            numberOfFeedback.setText(String.valueOf(totalUser));
                            setRating.setText(String.format("%.1f", calculateRating));
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
