package com.example.manager_food.Adapter;

import android.content.Context;
import android.util.Log;
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
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ItemsAdapter(List<Item> itemList, Context context) {
        this.itemList = new ArrayList<>(itemList);
        this.context = context;
    }

    public void setItems(List<Item> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
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

        Log.d("Adapter", "Binding category: " + item.getName());
        Log.d("Adapter", "Binding category: " + item.getDescription());
        Log.d("Adapter", "Binding category: " + item.getCategory());
        Log.d("Adapter", "Binding category: " + item.getPrice());
        Log.d("Adapter", "Binding category: " + item.getId());

        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(String.format("السعر : %s دج", item.getPrice()));
        holder.descriptionTextView.setText(item.getDescription());


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
        return itemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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
        private Button editButton;
        private Button removeButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.productNameTextView_frag);
            priceTextView = itemView.findViewById(R.id.productPriceTextView_frag);
            descriptionTextView = itemView.findViewById(R.id.item_description_popup);
            editButton = itemView.findViewById(R.id.editproduct);
            removeButton = itemView.findViewById(R.id.removeproduct);
        }
    }
}
