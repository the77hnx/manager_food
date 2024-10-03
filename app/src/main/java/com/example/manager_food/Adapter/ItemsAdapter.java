package com.example.manager_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.manager_food.R;
import com.example.manager_food.model.Category;
import com.example.manager_food.model.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private List<Item> filteredItemList;
    private List<Item> originalItemList; // Added original item list
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ItemsAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.filteredItemList = new ArrayList<>(itemList);
        this.originalItemList = new ArrayList<>(itemList); // Initialize original list
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_popup_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("$%.2f", item.getPrice()));
        holder.descriptionTextView.setText(item.getDescription());

        // Load image using an image loading library (like Glide or Picasso)
        // Example using Glide:
        // Glide.with(context).load(item.getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });

        holder.editButton.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEditClick(item);
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onRemoveClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredItemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void addItem(Item item) {
        itemList.add(item);
        filteredItemList.add(item); // Add to filtered list too
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(Item item);
        void onEditClick(Item item);
        void onRemoveClick(Item item);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView descriptionTextView;
        private ImageView imageView;
        private Button editButton;
        private Button removeButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.productNameTextView_frag);
            priceTextView = itemView.findViewById(R.id.productPriceTextView_frag);
            descriptionTextView = itemView.findViewById(R.id.item_description_popup);
            imageView = itemView.findViewById(R.id.product_image);
            editButton = itemView.findViewById(R.id.editproduct);
            removeButton = itemView.findViewById(R.id.removeproduct);
        }
    }
}
