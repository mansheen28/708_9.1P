package com.example.lostfound;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_gallery);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        itemList = new ArrayList<>();
        loadItems();

        imageAdapter = new ImageAdapter(this, itemList);
        recyclerView.setAdapter(imageAdapter);
    }

    private void loadItems() {
        // Simulating loading items from a data source
        itemList.add(new Item("Wallet", "Leather wallet", R.drawable.wallet));
        itemList.add(new Item("Phone", "Smartphone", R.drawable.phone));
        itemList.add(new Item("Keys", "House keys", R.drawable.keys));
        itemList.add(new Item("Backpack", "School backpack", R.drawable.backpack));
        itemList.add(new Item("Watch", "Wrist watch", R.drawable.watch));
    }
}

class Item {
    private String title;
    private String description;
    private int imageResource;

    public Item(String title, String description, int imageResource) {
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }
}

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Item> itemList;

    public ImageAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.imageView.setImageResource(item.getImageResource());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }
}
