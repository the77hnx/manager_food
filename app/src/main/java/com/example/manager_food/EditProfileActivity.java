package com.example.manager_food;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private TextView displayTextView, descriptionTextView, addressTextView, gpsTextView;
    private EditText etNumber, etName, addressEditText;
    private Button editBtn1, editBtn2, editBtn3, btnPhoneLogin;
    private boolean isEditingName = false, isEditingDescription = false, isEditingAddress = false;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        displayTextView = findViewById(R.id.displayTextView);
        descriptionTextView = findViewById(R.id.descriptiontv);
        addressTextView = findViewById(R.id.addresstvwritten);
        etNumber = findViewById(R.id.etnumberedit);
        etName = findViewById(R.id.etNameedit);
        addressEditText = findViewById(R.id.addresset);
        editBtn1 = findViewById(R.id.editbtn1);
        editBtn2 = findViewById(R.id.editbtn2);
        editBtn3 = findViewById(R.id.editbtn3);
        btnPhoneLogin = findViewById(R.id.saveeditbtn);
        gpsTextView = findViewById(R.id.editGPStext);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_ep);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    startActivity(new Intent(EditProfileActivity.this, ShopMainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(EditProfileActivity.this, ShowShopDetailsActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(EditProfileActivity.this, OurOrdersActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(EditProfileActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set click listeners for the buttons
        editBtn1.setOnClickListener(v -> {
            toggleEditText(etNumber, displayTextView, editBtn1, isEditingName);
            isEditingName = !isEditingName;
        });

        editBtn2.setOnClickListener(v -> {
            toggleEditText(etName, descriptionTextView, editBtn2, isEditingDescription);
            isEditingDescription = !isEditingDescription;
        });

        editBtn3.setOnClickListener(v -> {
            toggleEditText(addressEditText, addressTextView, editBtn3, isEditingAddress);
            isEditingAddress = !isEditingAddress;
        });

        // Set click listener for the save button
        btnPhoneLogin.setOnClickListener(v -> {
            saveAllEdits();
            // Navigate to the menu page
            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        gpsTextView.setOnClickListener(v -> checkLocationSettings());
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> requestLocationPermission());

        task.addOnFailureListener(this, e -> {
            if (e instanceof com.google.android.gms.common.api.ResolvableApiException) {
                try {
                    com.google.android.gms.common.api.ResolvableApiException resolvable = (com.google.android.gms.common.api.ResolvableApiException) e;
                    resolvable.startResolutionForResult(EditProfileActivity.this, LOCATION_PERMISSION_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            } else {
                Snackbar.make(findViewById(R.id.main), "Location settings are not satisfied.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", v -> {
                            // Open settings
                        }).show();
            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(EditProfileActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    currentLocation = address.getAddressLine(0);
                                    gpsTextView.setText(currentLocation);
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(EditProfileActivity.this, "Unable to get address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toggleEditText(EditText editText, TextView textView, Button button, boolean isEditing) {
        if (isEditing) {
            // Save changes and switch to TextView
            String newText = editText.getText().toString();
            if (!TextUtils.isEmpty(newText)) {
                textView.setText(newText);
            }
            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_edit));
        } else {
            // Switch to EditText
            editText.setText(textView.getText());
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_done));
        }
    }

    private void saveAllEdits() {
        if (etNumber.getVisibility() == View.VISIBLE) {
            String newNumber = etNumber.getText().toString();
            if (!TextUtils.isEmpty(newNumber)) {
                displayTextView.setText(newNumber);
            }
            etNumber.setVisibility(View.GONE);
            displayTextView.setVisibility(View.VISIBLE);
        }

        if (etName.getVisibility() == View.VISIBLE) {
            String newName = etName.getText().toString();
            if (!TextUtils.isEmpty(newName)) {
                descriptionTextView.setText(newName);
            }
            etName.setVisibility(View.GONE);
            descriptionTextView.setVisibility(View.VISIBLE);
        }

        if (addressEditText.getVisibility() == View.VISIBLE) {
            String newAddress = addressEditText.getText().toString();
            if (!TextUtils.isEmpty(newAddress)) {
                addressTextView.setText(newAddress);
            }
            addressEditText.setVisibility(View.GONE);
            addressTextView.setVisibility(View.VISIBLE);
        }
    }
}
