package com.example.agrishop;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Farmers.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "farmers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_UPI_ID = "upi_id";
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME + " TEXT PRIMARY KEY, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_UPI_ID + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFarmer(String name, String phone, String address, String upiId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_UPI_ID, upiId);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateFarmer(String name, String phone, String address, String upiId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_UPI_ID, upiId);
        String[] whereArgs = {name};
        int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_NAME + " = ?", whereArgs);
        return rowsAffected > 0;
    }

    public boolean deleteFarmer(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = {name};
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_NAME + " = ?", whereArgs);
        return rowsDeleted > 0;
    }
}
