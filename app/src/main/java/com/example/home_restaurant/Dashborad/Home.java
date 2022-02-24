package com.example.home_restaurant.Dashborad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.home_restaurant.Models.UserAuthenticationModel;
import com.example.home_restaurant.R;
import com.example.home_restaurant.databinding.ActivityHomeBinding;
import com.example.home_restaurant.loginSystem.user_verification;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity  {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private ImageView setUserImage;
    private final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("UserData")
            .child(FirebaseAuth.getInstance().getUid());
    private TextView setUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_sell, R.id.nav_order)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setUserImage = navigationView.getHeaderView(0).findViewById(R.id.setUserImage);
        setUserName = navigationView.getHeaderView(0).findViewById(R.id.setUserName);


        dataRef.child("userImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserAuthenticationModel photoUrl = snapshot.getValue(UserAuthenticationModel.class);
                    Picasso.get().load(photoUrl.UserImage).into(setUserImage);
                } else {
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Home.this);
                    if (acct != null) {
                        Picasso.get().load(acct.getPhotoUrl()).into(setUserImage);
                    } else {
                        Picasso.get().load(R.drawable.defultuser).into(setUserImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        dataRef.child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserAuthenticationModel storeUserName = snapshot.getValue(UserAuthenticationModel.class);
                    setUserName.setText(storeUserName.UserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(item -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this , user_verification.class));
            finish();
            return false;
        });
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}