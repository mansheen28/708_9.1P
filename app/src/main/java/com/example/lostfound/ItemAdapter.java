package com.example.lostfound;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ItemViewHolder> {
    private Context mContext;
    private List<String[]> mDataList;

    public LostFoundAdapter(Context context, List<String[]> dataList) {
        this.mContext = context;
        this.mDataList = dataList != null ? dataList : new ArrayList<String[]>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String[] currentItem = mDataList.get(position);
        holder.bindData(currentItem);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.item_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String[] selectedItem = mDataList.get(position);
                        Intent intent = new Intent(mContext, ViewItemActivity.class);
                        intent.putExtra("item_data", selectedItem);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        void bindData(String[] data) {
            if (data != null && data.length > 1) {
                itemTextView.setText(data[6].toUpperCase() + ": " + data[1]);
            }
        }
    }
}
