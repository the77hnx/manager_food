package com.example.manager_food;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShopMainActivity extends AppCompatActivity {

    private TextView basket;
    private Switch offerSwitch;
    private Button addProductButton;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String OFFER_STATUS_KEY = "offer_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopmain);

        // Initialize UI components
        basket = findViewById(R.id.basketmain);
        offerSwitch = findViewById(R.id.offer_switch_main);

        // Load the saved offer switch state
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean offerStatus = preferences.getBoolean(OFFER_STATUS_KEY, false);
        offerSwitch.setChecked(offerStatus);

        // Set initial state of the basket TextView
        updateBasketText(offerSwitch.isChecked());

        // Set up listener for the Switch
        offerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateBasketText(isChecked);
            saveOfferSwitchState(isChecked); // Save state when it changes
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_sm);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(ShopMainActivity.this, ShopMainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Navigate to ShowShopDetailsActivity
                    Intent intent = new Intent(ShopMainActivity.this, ShowShopDetailsActivity.class);
                    intent.putExtra("OFFER_STATUS", offerSwitch.isChecked());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(ShopMainActivity.this, OurOrdersActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(ShopMainActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    // Method to save the state of the offerSwitch
    private void saveOfferSwitchState(boolean isChecked) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OFFER_STATUS_KEY, isChecked);
        editor.apply();
    }

    // Method to update the basket TextView based on the Switch state
    private void updateBasketText(boolean isChecked) {
        if (isChecked) {
            basket.setText("مفتوح");
            basket.setTextColor(getResources().getColor(R.color.green)); // Use your green color resource
        } else {
            basket.setText("مغلق");
            basket.setTextColor(getResources().getColor(R.color.red)); // Use your red color resource
        }
    }
}
