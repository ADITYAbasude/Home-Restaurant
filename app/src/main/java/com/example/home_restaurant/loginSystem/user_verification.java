package com.example.home_restaurant.loginSystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.home_restaurant.Dashborad.Home;
import com.example.home_restaurant.Models.UserAuthenticationModel;
import com.example.home_restaurant.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class user_verification extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
    private UserAuthenticationModel userAuthenticationModel;
    private Button continueToVerification;
    private EditText UserMobileNumber;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("UserprofilePhoto/" + UUID.randomUUID());



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_varification);
        getSupportActionBar().hide();
        SignInButton SignInWithGoogleBtn = findViewById(R.id.SignInWithGoogleBtn);
        continueToVerification = findViewById(R.id.continueToVerification);
        UserMobileNumber = findViewById(R.id.UserMobileNumber);

        //TODO: say user to allow the permission

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        SignInWithGoogleBtn.setSize(SignInButton.SIZE_STANDARD);

        SignInWithGoogleBtn.setOnClickListener(v -> {
            signIn();

        });


//      ---------------------mobile number verification------------------------------------
        continueToVerification.setOnClickListener(v -> {
            if (UserMobileNumber.getText().toString().matches("[0-9]{10}")) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + UserMobileNumber.getText().toString(),
                        120,
                        TimeUnit.SECONDS,
                        user_verification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                UserMobileNumber.setError("Enter a valid number");
                                UserMobileNumber.requestFocus();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Intent intent = new Intent(user_verification.this, simplayout.class);
                                intent.putExtra("verificationId", s);
                                startActivity(intent);
                            }
                        }
                );
            } else {
                UserMobileNumber.setError("Enter a valid mobile number");
                UserMobileNumber.requestFocus();
            }
        });



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("Error", e.getMessage());
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        } catch (ApiException e) {
            Log.d("signInResult:failed code=", String.valueOf(e.getStatusCode()));
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("result", "signInWithCredential:success");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        updateUi(user);
                        Intent intent = new Intent(user_verification.this, Home.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                        Log.w("result", "signInWithCredential:failure", task.getException());
                    }
                });

    }

    private void updateUi(FirebaseUser user){
        GoogleSignInAccount userSignIn = GoogleSignIn.getLastSignedInAccount(this);
        userAuthenticationModel = new UserAuthenticationModel(userSignIn.getDisplayName());
        UserAuthenticationModel userAuthenticationModel2 = new UserAuthenticationModel(userSignIn.getPhotoUrl().toString() , null);
        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("Info").setValue(userAuthenticationModel);
        databaseReference.child(FirebaseAuth.getInstance().getUid()).child("userImage").setValue(userAuthenticationModel2);
    }


}