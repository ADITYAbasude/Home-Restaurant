package com.example.home_restaurant.Dashborad.Buyer.HomeContant;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.home_restaurant.R;

public class setHotelProductsInSeparate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hotel_products_in_separate);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));


        ActionBar bar = getSupportActionBar();
        bar.setTitle("");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        bar.setElevation(0);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        bar.setHomeAsUpIndicator(upArrow);
        bar.setElevation(0);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SeparateHotelProducts_fragment separateHotelProducts_fragment = new SeparateHotelProducts_fragment();
        transaction.replace(R.id.fragmentContainerView3 , separateHotelProducts_fragment).commit();

    }
}