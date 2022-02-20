package com.example.home_restaurant.loginSystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class mobile_number_verification extends Fragment {

    private EditText Vcode1, Vcode2, Vcode3, Vcode4, Vcode5, Vcode6;
    private Button VerifyNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_number_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Vcode1 = view.findViewById(R.id.Vcode);
        Vcode2 = view.findViewById(R.id.Vcode2);
        Vcode3 = view.findViewById(R.id.Vcode3);
        Vcode4 = view.findViewById(R.id.Vcode4);
        Vcode5 = view.findViewById(R.id.Vcode5);
        Vcode6 = view.findViewById(R.id.Vcode6);
        VerifyNumber = view.findViewById(R.id.VerifyNumber);


        editTextSetting();

        VerifyNumber.setOnClickListener(v -> {
            if (Vcode1.getText().toString().trim().isEmpty() ||
                    Vcode2.getText().toString().trim().isEmpty() ||
                    Vcode3.getText().toString().trim().isEmpty() ||
                    Vcode4.getText().toString().trim().isEmpty() ||
                    Vcode5.getText().toString().trim().isEmpty() ||
                    Vcode6.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }
            String VerificationCode = Vcode1.getText().toString() +
                    Vcode2.getText().toString() +
                    Vcode3.getText().toString() +
                    Vcode4.getText().toString() +
                    Vcode5.getText().toString() +
                    Vcode6.getText().toString();

            String VerificationId = getActivity().getIntent().getStringExtra("verificationId");


            if (VerificationId != null) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, VerificationCode);

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        takeUsername takeUsername = new takeUsername();
                        fragmentTransaction.replace(R.id.fragmentContainerView, takeUsername).commit();
                    }
                });

            }


        });

    }

    private void editTextSetting() {
        Vcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Vcode1.getText().toString().isEmpty()) {
                    Vcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Vcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Vcode2.getText().toString().isEmpty()) {
                    Vcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Vcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Vcode3.getText().toString().isEmpty()) {
                    Vcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Vcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Vcode4.getText().toString().isEmpty()) {
                    Vcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Vcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Vcode5.getText().toString().isEmpty()) {
                    Vcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
