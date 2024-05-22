package com.example.agrishop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FertilizerDetailsActivity extends AppCompatActivity {

    private ArrayList<String> fertilizerDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizers_details);

        // Retrieve fertilizer details from the intent
        fertilizerDetails = getIntent().getStringArrayListExtra("fertilizerDetails");

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fertilizerDetails);

        // Set up the ListView to display fertilizer details
        ListView fertilizerDetailsListView = findViewById(R.id.fertilizerDetailsListView);
        fertilizerDetailsListView.setAdapter(adapter);
        registerForContextMenu(fertilizerDetailsListView);

        // Set up item click listener
        fertilizerDetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected fertilizer name
                String selectedFertilizer = fertilizerDetails.get(position);

                // Create and show an AlertDialog
                new AlertDialog.Builder(FertilizerDetailsActivity.this)
                        .setTitle("Add to Cart")
                        .setMessage("Do you want to add " + selectedFertilizer + " to your cart?")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform add to cart action
                                showToast(selectedFertilizer + " added to cart");
                                // Start FarmerInfoActivity
                                Intent intent = new Intent(FertilizerDetailsActivity.this, FarmerInfoActivity.class);
                                intent.putExtra("selectedFertilizer", selectedFertilizer);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel the dialog
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        // Add button for asking fertilizer recommendation
        Button askForRecommendationButton = findViewById(R.id.askForRecommendationButton);
        askForRecommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the group chat activity
                Intent intent = new Intent(FertilizerDetailsActivity.this, GroupChatActivity.class);
                startActivity(intent);
            }
        });
    }

    // Override the onCreateContextMenu method to create the context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu); // Assuming you have a context_menu.xml file in your 'res/menu' folder
    }

    // Override the onContextItemSelected method to handle context menu item clicks
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        // Get the selected fertilizer name
        String selectedFertilizer = ((ArrayAdapter<String>) ((ListView) info.targetView.getParent()).getAdapter()).getItem(position);

        // Handle the context menu item clicks
        switch (item.getItemId()) {
            case R.id.menu_option1:
                showToast("Share selected for " + selectedFertilizer);
                return true;
            case R.id.menu_option2:
                showToast("Like selected for " + selectedFertilizer);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agri_shop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                showToast("Search item clicked");
                return true;
            case R.id.action_cart:
                showToast("Cart item clicked");
                return true;
            case R.id.action_settings:
                showToast("Settings item clicked");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
