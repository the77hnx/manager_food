package com.example.manager_food;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager_food.Adapter.CategoryAdapter;
import com.example.manager_food.Adapter.ItemsAdapter;
import com.example.manager_food.model.Category;
import com.example.manager_food.model.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowShopDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCat;
    private RecyclerView recyclerViewItem;
    private CategoryAdapter categoryAdapter;
    private ItemsAdapter itemsAdapter;
    private Switch offerSwitch;
    private TextView shopNameTextView, valCompletedTextView, valRatingTextView, offerStatusTextView;
    private Button addCategoryButton, addProductButton;

    private List<Item> itemList;
    List<Category> categoryList;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String OFFER_STATUS_KEY = "offer_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_details);



        // Initialize UI elements
        recyclerViewCat = findViewById(R.id.recycler_view_cat);
        recyclerViewItem = findViewById(R.id.recycler_view_item);
        offerSwitch = findViewById(R.id.offer_switch_details);
        shopNameTextView = findViewById(R.id.shopname_det);
        valCompletedTextView = findViewById(R.id.valcompleted);
        valRatingTextView = findViewById(R.id.valrating);
        offerStatusTextView = findViewById(R.id.status_det);
        addCategoryButton = findViewById(R.id.addcategory);
        addProductButton = findViewById(R.id.addproduct);

        // Load the saved offer switch state
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean offerStatus = preferences.getBoolean(OFFER_STATUS_KEY, false);

        // Retrieve the offer switch status from the Intent (if passed)
        Intent intent = getIntent();
        if (intent.hasExtra("OFFER_STATUS")) {
            offerStatus = intent.getBooleanExtra("OFFER_STATUS", false);
        }

        // Set the offerSwitch state and update the offerStatusTextView
        offerSwitch.setChecked(offerStatus);
        updateOfferStatusText(offerStatus);

        // Set up listener for the offerSwitch to sync with offerStatusTextView
        offerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateOfferStatusText(isChecked);

            // Save the offer switch state in SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(OFFER_STATUS_KEY, isChecked);
            editor.apply();
        });

        // Create a list of categories
        // Create a list of Category objects
        categoryList = new ArrayList<>();
        categoryList.add(new Category("All", R.drawable.image_for_card));  // Replace with actual image resource
        categoryList.add(new Category("Meat", R.drawable.meat)); // Replace with actual image resource
        categoryList.add(new Category("pastries", R.drawable.pastries)); // Replace with actual image resource
        categoryList.add(new Category("vegetables", R.drawable.vegetables)); // Replace with actual image resource
        categoryList.add(new Category("Drinks", R.drawable.drinks)); // Replace with actual image resource

// Initialize category adapter with Category objects
        categoryAdapter = new CategoryAdapter(categoryList, this);


        // Initialize the item list
        itemList = new ArrayList<>();
        itemList.add(new Item("pizza", "Fresh and juicy oranges", 150, R.drawable.pizza, "pastries"));
        itemList.add(new Item("pasta", "Crisp and sweet apples", 200, R.drawable.pasta, "pastries"));
        itemList.add(new Item("burger", "Crunchy carrots", 100, R.drawable.burger, "pastries"));
        itemList.add(new Item("Potato Chips", "Crispy potato chips", 50, R.drawable.potato, "vegetables"));
        itemList.add(new Item("chicken", "Refreshing cola drink", 80, R.drawable.chicken, "Meat"));

        // Initialize category adapter


        // Set category click listener
        categoryAdapter.setOnCategoryClickListener(selectedCategory -> {
            itemsAdapter.filterItemsByCategory(selectedCategory);
        });

        // Set up the category RecyclerView
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCat.setAdapter(categoryAdapter);

        // Initialize items adapter
        itemsAdapter = new ItemsAdapter(itemList, this);

        // Set up the items RecyclerView
        recyclerViewItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewItem.setAdapter(itemsAdapter);

        // Add category button click listener
        addCategoryButton.setOnClickListener(v -> {
            showAddCategoryDialog();
        });

        // Add product button click listener
        addProductButton.setOnClickListener(v -> {
            showAddItemDialog();
        });

        // Set up item click listener for ItemsAdapter
        itemsAdapter.setOnItemClickListener(new ItemsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(Item item) {
                // Handle item click action here
            }
            @Override
            public void onEditClick(Item item) {
                showEditItemDialog(item);
            }

            @Override
            public void onRemoveClick(Item item) {
                // Handle remove action
                int position = itemList.indexOf(item);
                if (position != -1) {
                    itemList.remove(position);
                    itemsAdapter.notifyItemRemoved(position);
                }
            }
        });

        // Setup bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView_ssd);
        bottomNavigationView.setSelectedItemId(R.id.navigation_following); // Change this based on the activity

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to ShopsActivity
                    Intent intent = new Intent(ShowShopDetailsActivity.this, ShopMainActivity.class);
                    intent.putExtra("OFFER_STATUS", offerSwitch.isChecked());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_following) {
                    // Show toast indicating following action
                    startActivity(new Intent(ShowShopDetailsActivity.this, ShowShopDetailsActivity.class));

                    return true;
                } else if (itemId == R.id.navigation_basket) {
                    // Navigate to OrderSummaryActivity
                    startActivity(new Intent(ShowShopDetailsActivity.this, OurOrdersActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to ProfileActivity
                    startActivity(new Intent(ShowShopDetailsActivity.this, EditProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        // Initialize item list
        // itemList = new ArrayList<>(); // This line is redundant

        // Setup RecyclerViews
        setupRecyclerViews();

        // Load initial data (dummy data)
    }

    // Method to update the offerStatusTextView based on the Switch state
    private void updateOfferStatusText(boolean isChecked) {
        if (isChecked) {
            offerStatusTextView.setText("مفتوح");
            offerStatusTextView.setTextColor(getResources().getColor(R.color.green)); // Use your green color resource
        } else {
            offerStatusTextView.setText("مغلق");
            offerStatusTextView.setTextColor(getResources().getColor(R.color.red)); // Use your red color resource
        }
    }

    // Method to save the state of the offerSwitch
    private void saveOfferSwitchState(boolean isChecked) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(OFFER_STATUS_KEY, isChecked);
        editor.apply();
    }

    // Method to setup RecyclerViews
    private void setupRecyclerViews() {
        // Setup category RecyclerView
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCat.setAdapter(categoryAdapter);

        // Setup items RecyclerView
        recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItem.setAdapter(itemsAdapter);
    }

    // Method to show the Add Category Dialog
    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_new_category_dialog_fragement, null);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Initialize dialog views and set click listeners
        TextView saveButton = dialogView.findViewById(R.id.btnSavecat);
        TextView categoryNameTextView = dialogView.findViewById(R.id.etfullnamecat);
        // Assuming you have an ImageView to pick or set the image resource
        ImageView categoryImageView = dialogView.findViewById(R.id.imageViewup); // Replace with actual image picking logic

        saveButton.setOnClickListener(v -> {
            String newCategoryName = categoryNameTextView.getText().toString().trim();
            // Get selected image resource id (example placeholder here)
            int selectedImageResId = R.drawable.image_for_card; // Replace with actual image selection logic

            if (!newCategoryName.isEmpty()) {
                Category newCategory = new Category(newCategoryName, selectedImageResId);
                categoryAdapter.addCategory(newCategory);
                dialog.dismiss();
            } else {
                // Show an error message or handle the case when fields are empty
            }
        });

        dialog.show();


    }


    // Method to show the Add Product Dialog
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_new_item_dialog_fragment, null);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText itemNameEditText = dialogView.findViewById(R.id.etfullnameprod);
        EditText itemPriceEditText = dialogView.findViewById(R.id.etpriceprod);
        EditText itemDescriptionEditText = dialogView.findViewById(R.id.etdescriptionprod);
        Spinner itemCategorySpinner = dialogView.findViewById(R.id.itemCategorySpinner);
        ImageView itemImageView = dialogView.findViewById(R.id.imageViewupitemadd); // Placeholder for image selection
        Button saveButton = dialogView.findViewById(R.id.btnsaveitem);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategorySpinner.setAdapter(categoryAdapter);

        saveButton.setOnClickListener(v -> {
            String itemName = itemNameEditText.getText().toString().trim();
            String itemPriceStr = itemPriceEditText.getText().toString().trim();
            String itemDescription = itemDescriptionEditText.getText().toString().trim();
            String selectedCategory = itemCategorySpinner.getSelectedItem().toString();

            if (itemName.isEmpty() || itemPriceStr.isEmpty() || itemDescription.isEmpty()) {
                Toast.makeText(ShowShopDetailsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double itemPrice = Double.parseDouble(itemPriceStr);
                Uri itemImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.resrurant_image); // Placeholder

                Item newItem = new Item(itemName, itemDescription, itemPrice, R.drawable.pizza, selectedCategory); // Replace R.drawable.pizza with actual image URI
                itemsAdapter.addItem(newItem);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(ShowShopDetailsActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }



    private void showEditItemDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_new_item_dialog_fragment, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Initialize dialog views
        EditText itemNameEditText = dialogView.findViewById(R.id.etfullnameprod);
        EditText itemPriceEditText = dialogView.findViewById(R.id.etpriceprod);
        EditText itemDescriptionEditText = dialogView.findViewById(R.id.etdescriptionprod);
        Spinner itemCategorySpinner = dialogView.findViewById(R.id.itemCategorySpinner);
        ImageView itemImageView = dialogView.findViewById(R.id.imageViewupitemadd);
        Button saveButton = dialogView.findViewById(R.id.btnsaveitem);

        // Set current item data to dialog views
        itemNameEditText.setText(item.getName());
        itemPriceEditText.setText(String.valueOf(item.getPrice()));
        itemDescriptionEditText.setText(item.getDescription());
        itemImageView.setImageResource(item.getImageResId());

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategorySpinner.setAdapter(categoryAdapter);
        itemCategorySpinner.setSelection(categoryAdapter.getPosition(item.getCategory()));

        // Set click listener for the save button
        saveButton.setOnClickListener(v -> {
            String itemName = itemNameEditText.getText().toString().trim();
            String itemPriceStr = itemPriceEditText.getText().toString().trim();
            String itemDescription = itemDescriptionEditText.getText().toString().trim();
            String selectedCategory = itemCategorySpinner.getSelectedItem().toString();

            if (!itemName.isEmpty() && !itemPriceStr.isEmpty() && !itemDescription.isEmpty()) {
                double itemPrice;
                try {
                    itemPrice = Double.parseDouble(itemPriceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ShowShopDetailsActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update item details
                item.setName(itemName);
                item.setPrice(itemPrice);
                item.setDescription(itemDescription);
                item.setCategory(selectedCategory);

                // Notify adapter about item update
                itemsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                Toast.makeText(ShowShopDetailsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    // Helper method to get category names for spinner
    private List<String> getCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

}