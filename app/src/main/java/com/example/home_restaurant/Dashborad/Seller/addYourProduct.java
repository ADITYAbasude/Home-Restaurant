package com.example.home_restaurant.Dashborad.Seller;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

public class addYourProduct extends BottomSheetDialogFragment {

    private Button addDish;
    private TextInputEditText addDishTitle, addDishDescription, addDishPrice;
    private ImageView addDishImage;
    private static Uri ImageData, imageuri;
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Seller")
            .child(FirebaseAuth.getInstance().getUid()).child("Food Product");
    private final StorageReference storageRoot = FirebaseStorage.getInstance()
            .getReference("Food Product Image/" + UUID.randomUUID());
    private ProgressBar progressBar;

    public addYourProduct() {
        // Required empty public constructor
    }


    public static addYourProduct newInstance(String param1, String param2) {
        addYourProduct fragment = new addYourProduct();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_your_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addDish = view.findViewById(R.id.addDish);
        addDishImage = view.findViewById(R.id.addDishImage);
        addDishTitle = view.findViewById(R.id.addDishTitle);
        addDishDescription = view.findViewById(R.id.addDishDescription);
        addDishPrice = view.findViewById(R.id.addDishPrice);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayout linear = view.findViewById(R.id.linear);
//        View v2 = view.findViewById(R.id.)

        addDishImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent.createChooser(intent, "pick image "), 1);
        });

        addDish.setOnClickListener(v -> {
            String title = addDishTitle.getText().toString();
            String description = addDishDescription.getText().toString();
            String dishImageUrl = String.valueOf(ImageData);
            String Price = addDishPrice.getText().toString();
            if (title.length() == 0) {
                addDishTitle.requestFocus();
                addDishTitle.setError("Required");
                progressBar.setVisibility(View.GONE);
            } else if (description.length() == 0) {
                addDishDescription.requestFocus();
                addDishDescription.setError("Required");
                progressBar.setVisibility(View.GONE);
            } else if (dishImageUrl.length() == 0) {
                addDishImage.requestFocus();
                progressBar.setVisibility(View.GONE);
            } else if (Price.length() == 0) {
                addDishPrice.requestFocus();
                addDishPrice.setError("Required");
                progressBar.setVisibility(View.GONE);
            } else {
                if (ImageData != null && !addDishImage.equals(R.drawable.ic_menu_camera)) {
                    String ID = FirebaseDatabase.getInstance().getReference().push().getKey();
                    try {
                        Snackbar.make(linear, "Wait", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Wait it's take a time", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        storageRoot.putFile(ImageData).addOnSuccessListener(taskSnapshot -> {
                            storageRoot.getDownloadUrl().addOnSuccessListener(uri -> {
                                imageuri = uri;
                                AddDishModel addDishModel = new AddDishModel(FirebaseAuth.getInstance().getUid(),
                                        title, description , Price, String.valueOf(imageuri), ID);
                                dataRef.child(ID).setValue(addDishModel);
                                addDishPrice.setText("");
                                addDishTitle.setText("");
                                addDishDescription.setText("");
                                addDishImage.setImageResource(R.drawable.ic_menu_camera);
                            });
                        });
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("error", e.getMessage());
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ImageData = data.getData();
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), ImageData);
                addDishImage.setImageBitmap(bitmapImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}