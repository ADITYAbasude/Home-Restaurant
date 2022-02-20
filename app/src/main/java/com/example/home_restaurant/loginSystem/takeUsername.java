package com.example.home_restaurant.loginSystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.home_restaurant.Dashborad.Home;
import com.example.home_restaurant.Models.UserAuthenticationModel;
import com.example.home_restaurant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class takeUsername extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private Button SubmitUserName;
    private static final DatabaseReference references = FirebaseDatabase.getInstance().getReference("UserData")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Info");
    private EditText setUserName;

    public takeUsername() {
        // Required empty public constructor
    }

    public static takeUsername newInstance(String param1, String param2) {
        takeUsername fragment = new takeUsername();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_username, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SubmitUserName = view.findViewById(R.id.SubmitUserName);
        setUserName = view.findViewById(R.id.setUserName);

        SubmitUserName.setOnClickListener(v -> {
            UserAuthenticationModel authentication = new UserAuthenticationModel(setUserName.getText().toString());
            references.setValue(authentication);
            Toast.makeText(getContext(), FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext() , Home.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}