package com.example.manager_food;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignNewShopActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText etNumber, etName, etDescriptionRes, etPlacesRes;
    private Button selectImageResButton, resIDButton, cardIDButton, gpsButton, btnPhoneLogin;
    private ImageView selectedImageRes, selectedResID, selectedCardID;
    private FusedLocationProviderClient fusedLocationClient;
    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_new_shop);

        initializeViews();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        selectImageResButton.setOnClickListener(v -> showImagePickerDialog(selectImageResLauncher));
        resIDButton.setOnClickListener(v -> showImagePickerDialog(resIDLauncher));
        cardIDButton.setOnClickListener(v -> showImagePickerDialog(cardIDLauncher));
        gpsButton.setOnClickListener(v -> checkLocationSettings());
        btnPhoneLogin.setOnClickListener(v -> validateAndSubmit());
    }

    private void initializeViews() {
        etNumber = findViewById(R.id.etnumber);
        etName = findViewById(R.id.etName);
        etDescriptionRes = findViewById(R.id.etDescriptionres);
        etPlacesRes = findViewById(R.id.etplacesres);

        selectImageResButton = findViewById(R.id.selectImageresButton);
        resIDButton = findViewById(R.id.ResIDButton);
        cardIDButton = findViewById(R.id.CardIDButton);
        gpsButton = findViewById(R.id.GPSButton);
        btnPhoneLogin = findViewById(R.id.btnsendinfores);

        selectedImageRes = findViewById(R.id.selectedImageRes);
        selectedResID = findViewById(R.id.selectedResID);
        selectedCardID = findViewById(R.id.selectedCardID);

        // Set initial visibility to gone
        selectedImageRes.setVisibility(View.GONE);
        selectedResID.setVisibility(View.GONE);
        selectedCardID.setVisibility(View.GONE);
    }

    private void showImagePickerDialog(ActivityResultLauncher<String> launcher) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_image_picker, null);
        bottomSheetDialog.setContentView(dialogView);

        Button chooseFromGallery = dialogView.findViewById(R.id.btnPickGallery);
        Button capturePhoto = dialogView.findViewById(R.id.btnCaptureCamera);

        chooseFromGallery.setOnClickListener(v -> {
            launcher.launch("image/*");
            bottomSheetDialog.dismiss();
        });

        capturePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    ActivityResultLauncher<String> selectImageResLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        displayImage(uri, selectedImageRes, selectImageResButton);
                    }
                }
            });

    ActivityResultLauncher<String> resIDLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        displayImage(uri, selectedResID, resIDButton);
                    }
                }
            });

    ActivityResultLauncher<String> cardIDLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        displayImage(uri, selectedCardID, cardIDButton);
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    selectedImageRes.setImageBitmap(imageBitmap);
                    selectImageResButton.setVisibility(View.GONE);
                    selectedImageRes.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void displayImage(Uri uri, ImageView imageView, Button button) {
        if (uri != null) {
            imageView.setImageURI(uri);
            button.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
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
                    resolvable.startResolutionForResult(SignNewShopActivity.this, 1);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(SignNewShopActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses != null && !addresses.isEmpty()) {
                                        Address address = addresses.get(0);
                                        selectedLocation = address.getAddressLine(0);
                                        etPlacesRes.setText(selectedLocation);
                                    } else {
                                        Toast.makeText(SignNewShopActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(SignNewShopActivity.this, "Location is null", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private void validateAndSubmit() {
        String number = etNumber.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String description = etDescriptionRes.getText().toString().trim();
        String places = etPlacesRes.getText().toString().trim();

        if (number.isEmpty() || name.isEmpty() || description.isEmpty() || places.isEmpty() ||
                selectedImageRes.getDrawable() == null || selectedResID.getDrawable() == null ||
                selectedCardID.getDrawable() == null || selectedLocation == null) {
            Toast.makeText(this, "All fields and images must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert images to Bitmap for further processing or upload
        BitmapDrawable resDrawable = (BitmapDrawable) selectedImageRes.getDrawable();
        BitmapDrawable resIdDrawable = (BitmapDrawable) selectedResID.getDrawable();
        BitmapDrawable cardIdDrawable = (BitmapDrawable) selectedCardID.getDrawable();

        // Here, you can convert them to Bitmaps or any further processing you need.
        // Bitmap resBitmap = resDrawable.getBitmap();
        // Bitmap resIdBitmap = resIdDrawable.getBitmap();
        // Bitmap cardIdBitmap = cardIdDrawable.getBitmap();

        // Further processing or submission of data...
        Toast.makeText(this, "All fields and images are filled, ready to submit", Toast.LENGTH_SHORT).show();
    }
}
