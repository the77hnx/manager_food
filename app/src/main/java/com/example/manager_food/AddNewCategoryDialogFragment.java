package com.example.manager_food;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddNewCategoryDialogFragment extends DialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etName;
    private ImageView imageView;
    private Button btnSave;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Inflate the custom view
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_category_dialog_fragement, null);

        // Initialize UI elements
        imageView = view.findViewById(R.id.imageViewup);
        etName = view.findViewById(R.id.etfullnamecat);
        btnSave = view.findViewById(R.id.btnSavecat);

        // Set up click listeners
        imageView.setOnClickListener(v -> {
            // Open image picker
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            // Handle save action
            String name = etName.getText().toString().trim();

            if (validateInput(name)) {
                // Save the data (e.g., add to database)
                dismiss(); // Close the dialog
            } else {
                // Show validation errors
                etName.setError("Name cannot be empty");
            }
        });

        builder.setView(view);

        return builder.create();
    }

    private boolean validateInput(String name) {
        // Validate that name is not empty
        return !name.isEmpty();
    }
}
