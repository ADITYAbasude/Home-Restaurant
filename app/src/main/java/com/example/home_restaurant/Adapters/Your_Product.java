package com.example.home_restaurant.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Your_Product extends RecyclerView.Adapter<Your_Product.myViewHolder> {
    private Context context;
    private ArrayList<AddDishModel> storeProducts;
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller")
            .child(FirebaseAuth.getInstance().getUid()).child("Food Product");
    public Your_Product(Context context , ArrayList<AddDishModel> storeProducts){
        this.context = context;
        this.storeProducts = storeProducts;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.your_product ,parent , false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        AddDishModel addDishModel = storeProducts.get(position);

        Glide.with(context).load(Uri.parse(addDishModel.DishImage)).into(holder.ProductImage);
        holder.FoodDescription.setText(addDishModel.DishDescription);
        holder.FoodPrice.setText(addDishModel.DishPrice);
        holder.FoodTitle.setText(addDishModel.DishTitle);

        holder.deleteProduct.setOnClickListener(v -> {
            dataRef.child(addDishModel.ProductId).setValue(null);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return storeProducts.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView ProductImage;
        TextView FoodTitle , FoodDescription , FoodPrice , deleteProduct;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductImage = itemView.findViewById(R.id.ProductImage);
            FoodTitle = itemView.findViewById(R.id.FoodTitle);
            FoodDescription = itemView.findViewById(R.id.FoodDescription);
            FoodPrice = itemView.findViewById(R.id.FoodPrice);
            deleteProduct = itemView.findViewById(R.id.delectProduct);
        }
    }
}
