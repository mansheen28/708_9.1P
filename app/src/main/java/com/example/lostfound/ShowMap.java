package com.example.inventorymanagement;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class InventorySummaryActivity extends AppCompatActivity {

    private TextView summaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_summary);
        
        summaryTextView = findViewById(R.id.summaryTextView);
        
        // Retrieve inventory data and display summary
        displayInventorySummary();
    }

    private void displayInventorySummary() {
        // In a real scenario, this data would come from a database or API
        Map<String, Integer> inventoryData = getInventoryData();
        
        StringBuilder summaryBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : inventoryData.entrySet()) {
            summaryBuilder.append(entry.getKey())
                          .append(": ")
                          .append(entry.getValue())
                          .append("\n");
        }

        summaryTextView.setText(summaryBuilder.toString());
    }

    private Map<String, Integer> getInventoryData() {
        // Simulated inventory data
        Map<String, Integer> inventoryData = new HashMap<>();
        inventoryData.put("Item A", 50);
        inventoryData.put("Item B", 30);
        inventoryData.put("Item C", 20);
        inventoryData.put("Item D", 10);
        return inventoryData;
    }
}
