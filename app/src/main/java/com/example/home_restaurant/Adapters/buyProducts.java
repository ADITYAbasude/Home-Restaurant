package com.example.home_restaurant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.home_restaurant.Dashborad.Buyer.HomeContant.PaymentMethod.PayWithGoogle;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;

import java.util.ArrayList;

public class buyProducts extends RecyclerView.Adapter<buyProducts.myViewAdapter> {

    private Context context;
    private ArrayList<AddDishModel> products;

    public buyProducts(Context context , ArrayList<AddDishModel> products){
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public myViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.buy_products , parent , false);
        return new myViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewAdapter holder, int position) {
        AddDishModel addDishModel = products.get(position);
        holder.productName.setText(addDishModel.DishTitle);
        holder.productDescription.setText(addDishModel.DishDescription);
        holder.productPrice.setText(addDishModel.DishPrice);
        Glide.with(context).load(addDishModel.DishImage).into(holder.productImage);



        holder.buyNow.setOnClickListener(v -> {
            Intent intent = new Intent(context , PayWithGoogle.class);
            intent.putExtra("ProductID" , addDishModel.ProductId);
            intent.putExtra("foodPrice" , addDishModel.DishPrice);
            intent.putExtra("hotelId" , addDishModel.HotelId);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class myViewAdapter extends RecyclerView.ViewHolder {
        TextView productName , productDescription , productPrice , buyNow;
        ImageView productImage;
        public myViewAdapter(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            buyNow = itemView.findViewById(R.id.buyNow);
            productImage = itemView.findViewById(R.id.productPhoto);
        }
    }
}
