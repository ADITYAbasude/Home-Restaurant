package com.example.home_restaurant;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.home_restaurant.Dashborad.Home;
import com.example.home_restaurant.loginSystem.user_verification;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (FirebaseAuth.getInstance().getCurrentUser()!= null) {
                        Intent gotoDashboard = new Intent(splash_screen.this, Home.class);
                        startActivity(gotoDashboard);
                        finish();
                    } else {
                        Intent gotoVerification = new Intent(splash_screen.this, user_verification.class);
                        startActivity(gotoVerification);
                        finish();
                    }
                    Dexter.withContext(splash_screen.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse )
                                {
                                    finishAffinity();
                                    System.exit(0);
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            })
                            .check();
                }
            }
        };
        thread.start();

    }
}