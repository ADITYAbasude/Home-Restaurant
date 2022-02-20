package com.example.home_restaurant.loginSystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.home_restaurant.R;

public class simplayout extends AppCompatActivity {
    private String userNumber, verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplayout);
        getSupportActionBar().setTitle("Home Restaurant");
        verificationId = getIntent().getStringExtra("verificationId");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("vid" , verificationId);
        mobile_number_verification number_verification = new mobile_number_verification();
        number_verification.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentContainerView, number_verification).commit();
    }
}