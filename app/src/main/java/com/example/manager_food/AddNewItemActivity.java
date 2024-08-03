package com.example.manager_food;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class AddNewItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText etName, etPrice, etDescription;
    private Spinner spinner;
    private ImageView imageView;
    private Button btnSave;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        // Initialize UI elements
        imageView = findViewById(R.id.imageViewupitemadd);
        etName = findViewById(R.id.etfullnameprod);
        etPrice = findViewById(R.id.etpriceprod);
        etDescription = findViewById(R.id.etdescriptionprod);
        spinner = findViewById(R.id.itemCategorySpinner);
        btnSave = findViewById(R.id.btnsaveitem);

        // Intent data handling
        Intent intent = getIntent();
        if (intent != null) {
            String itemName = intent.getStringExtra("ITEM_NAME");
            String itemPrice = intent.getStringExtra("ITEM_PRICE");
            String itemDescription = intent.getStringExtra("ITEM_DESCRIPTION");
            String itemType = intent.getStringExtra("ITEM_TYPE");
            int imageResId = intent.getIntExtra("ITEM_IMAGE_RES_ID", 0);

            // Set received data to views
            etName.setText(itemName);
            etPrice.setText(itemPrice);
            etDescription.setText(itemDescription);

            // Set image if it was passed via resource ID
            if (imageResId != 0) {
                imageView.setImageResource(imageResId);
            }

            // Get the categories list from intent
            ArrayList<String> categories = intent.getStringArrayListExtra("CATEGORIES_LIST");

            if (categories != null && !categories.isEmpty()) { // Ensure list is not null and not empty
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Set spinner selection if type is available
                if (itemType != null) {
                    int spinnerPosition = adapter.getPosition(itemType);
                    if (spinnerPosition >= 0) { // Check if position is valid
                        spinner.setSelection(spinnerPosition);
                    }
                }
            } else {
                // Handle the case where categories list is null or empty
                Log.e("AddNewItemActivity", "Categories list is null or empty.");
                Toast.makeText(this, "Categories data is not available.", Toast.LENGTH_SHORT).show();
                // You can initialize the spinner with default data or disable it
                spinner.setEnabled(false);
            }
        }

        // Set up click listeners
        imageView.setOnClickListener(v -> {
            if (checkAndRequestPermissions()) {
                openImageChooser();
            }
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String type = spinner.getSelectedItem().toString();

            if (validateInput(name, price, description, type)) {
                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                Intent Intent = new Intent(AddNewItemActivity.this, ShowShopDetailsActivity.class);
                startActivity(Intent);
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String name, String price, String description, String type) {
        // Basic validation example
        if (name.isEmpty() || price.isEmpty() || description.isEmpty() || type.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(price); // Check if price is a valid number
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }


}
