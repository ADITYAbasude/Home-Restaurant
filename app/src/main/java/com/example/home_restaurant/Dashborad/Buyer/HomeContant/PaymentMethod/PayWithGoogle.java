package com.example.home_restaurant.Dashborad.Buyer.HomeContant.PaymentMethod;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.home_restaurant.Dashborad.Buyer.HomeContant.PaymentMethod.viewmodel.CheckoutViewModel;
import com.example.home_restaurant.Models.AddDishModel;
import com.example.home_restaurant.Models.UserAuthenticationModel;
import com.example.home_restaurant.R;
import com.example.home_restaurant.databinding.ActivityPayWithGoogleBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Checkout implementation for the app
 */
public class PayWithGoogle extends AppCompatActivity {

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private CheckoutViewModel model;

    private ActivityPayWithGoogleBinding layoutBinding;
    private View googlePayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Pay");
//        ActionBar bar = getSupportActionBar();
//        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
//        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
//        bar.setHomeAsUpIndicator(upArrow);
//        bar.setElevation(0);

        initializeUi();
        model = new ViewModelProvider(this).get(CheckoutViewModel.class);
        model.canUseGooglePay.observe(this, this::setGooglePayAvailable);
    }


    private void initializeUi() {

        // Use view binding to access the UI elements
        layoutBinding = ActivityPayWithGoogleBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        // The Google Pay button is a layout file – take the root view
        googlePayButton = layoutBinding.googlePayButton.getRoot();
        googlePayButton.setOnClickListener(this::requestPayment);


        FirebaseDatabase.getInstance().getReference("Seller").child(getIntent().getStringExtra("hotelId"))
                .child("Food Product").child(getIntent().getStringExtra("ProductID"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase.getInstance().getReference("UserData").child(FirebaseAuth.getInstance().getUid())
                                .child("Info").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserAuthenticationModel authenticationModel = snapshot.getValue(UserAuthenticationModel.class);

                                String orderId = FirebaseDatabase.getInstance().getReference().push().getKey();
                                AddDishModel addDishModel = snapshot.getValue(AddDishModel.class);
                                AddDishModel addDishModel1 = new AddDishModel(addDishModel.HotelId
                                        , addDishModel.DishTitle, addDishModel.DishDescription, addDishModel.DishPrice
                                        , addDishModel.DishImage, orderId , authenticationModel.UserName);
                                FirebaseDatabase.getInstance().getReference("UserData").child(FirebaseAuth.getInstance().getUid())
                                        .child("Orders").child(orderId).setValue(addDishModel1);
                                FirebaseDatabase.getInstance().getReference("Seller").child(getIntent().getStringExtra("hotelId"))
                                        .child("Orders").child(orderId).setValue(addDishModel1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        long dummyPriceCents = Long.parseLong(getIntent().getStringExtra("foodPrice"));
        long shippingCostCents = 0;
        long totalPriceCents = dummyPriceCents + shippingCostCents;
        final Task<PaymentData> task = model.getLoadPaymentDataTask(totalPriceCents);

        // Shows the payment sheet and forwards the result to the onActivityResult method.
        AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case AppCompatActivity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case AppCompatActivity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status);
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }

    private void handlePaymentSuccess(@Nullable PaymentData paymentData) {
        final String paymentInfo = paymentData.toJson();

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(@Nullable Status status) {
        String errorString = "Unknown error.";
        if (status != null) {
            int statusCode = status.getStatusCode();
            errorString = String.format(Locale.getDefault(), "Error code: %d", statusCode);
        }

        Log.e("loadPaymentData failed", errorString);
    }
}