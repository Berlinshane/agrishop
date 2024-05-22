package com.example.agrishop;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FarmerDetailsActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextAddress, editTextUpiId;
    private Button buttonAdd, buttonShow, buttonUpdate, buttonDelete;
    private DatabaseHelper databaseHelper;
    private ListView listViewFarmers;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> farmerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_details);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextUpiId = findViewById(R.id.editTextUpiId);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonShow = findViewById(R.id.buttonShow);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewFarmers = findViewById(R.id.listViewFarmers);

        databaseHelper = new DatabaseHelper(this);
        farmerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, farmerList);
        listViewFarmers.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String address = editTextAddress.getText().toString();
                String upiId = editTextUpiId.getText().toString();
                boolean inserted = insertFarmer(name, phone, address, upiId);
                if (inserted) {
                    Toast.makeText(FarmerDetailsActivity.this, "Farmer details saved", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextPhone.setText("");
                    editTextAddress.setText("");
                    editTextUpiId.setText("");
                } else {
                    Toast.makeText(FarmerDetailsActivity.this, "Failed to save farmer details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFarmers();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String address = editTextAddress.getText().toString();
                String upiId = editTextUpiId.getText().toString();
                boolean updated = updateFarmer(name, phone, address, upiId);
                if (updated) {
                    Toast.makeText(FarmerDetailsActivity.this, "Farmer details updated", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextPhone.setText("");
                    editTextAddress.setText("");
                    editTextUpiId.setText("");
                } else {
                    Toast.makeText(FarmerDetailsActivity.this, "Failed to update farmer details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                boolean deleted = deleteFarmer(name);
                if (deleted) {
                    Toast.makeText(FarmerDetailsActivity.this, "Farmer details deleted", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextPhone.setText("");
                    editTextAddress.setText("");
                    editTextUpiId.setText("");
                } else {
                    Toast.makeText(FarmerDetailsActivity.this, "Failed to delete farmer details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean insertFarmer(String name, String phone, String address, String upiId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PHONE, phone);
        contentValues.put(DatabaseHelper.COLUMN_ADDRESS, address);
        contentValues.put(DatabaseHelper.COLUMN_UPI_ID, upiId);
        long result = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    private void showFarmers() {
        farmerList.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);
            int addressIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS);
            int upiIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_UPI_ID);
            do {
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);
                String address = cursor.getString(addressIndex);
                String upiId = cursor.getString(upiIdIndex);
                String farmerDetails = "Name: " + name + "\nPhone: " + phone + "\nAddress: " + address + "\nUPI ID: " + upiId;
                farmerList.add(farmerDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private boolean updateFarmer(String name, String phone, String address, String upiId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PHONE, phone);
        contentValues.put(DatabaseHelper.COLUMN_ADDRESS, address);
        contentValues.put(DatabaseHelper.COLUMN_UPI_ID, upiId);
        String[] whereArgs = {name}; // Assuming name is the primary key
        int rowsAffected = db.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.COLUMN_NAME + " = ?", whereArgs);
        return rowsAffected > 0;
    }

    private boolean deleteFarmer(String name) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] whereArgs = {name}; // Assuming name is the primary key
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_NAME + " = ?", whereArgs);
        return rowsDeleted > 0;
    }
}
