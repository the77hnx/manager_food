package com.example.manager_food;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.pm.PackageManager;
import android.Manifest;

public class AddNewItemDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText etName, etPrice, etDescription;
    private Spinner spinner;
    private ImageView imageView;
    private Button btnSave;
    private Uri imageUri;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Inflate the custom view
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_item_dialog_fragment, null);

        // Initialize UI elements
        imageView = view.findViewById(R.id.imageViewupitemadd);
        etName = view.findViewById(R.id.etfullnameprod);
        etPrice = view.findViewById(R.id.etpriceprod);
        etDescription = view.findViewById(R.id.etdescriptionprod);
        spinner = view.findViewById(R.id.itemCategorySpinner);
        btnSave = view.findViewById(R.id.btnsaveitem);

        // Set up the spinner with an adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.states_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
                // Save the data
                // Example: saveData(name, price, description, type, imageUri);
                Toast.makeText(requireContext(), "Item saved", Toast.LENGTH_SHORT).show();
                dismiss(); // Close the dialog
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);

        return builder.create();
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
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
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
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
