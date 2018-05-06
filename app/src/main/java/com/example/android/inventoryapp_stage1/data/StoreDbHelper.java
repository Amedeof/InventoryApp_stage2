package com.example.android.inventoryapp_stage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class StoreDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file and his version
     */
    private static final String DATABASE_NAME = "catalogue.db";
    private static final int DATABASE_VERSION = 1;


    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the catalogue table
        String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + StoreContract.StoreEntry.TABLE_NAME + "(" + StoreContract.StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + StoreContract.StoreEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " + StoreContract.StoreEntry.COLUMN_PRICE + " INTEGER NOT NULL, " + StoreContract.StoreEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " + StoreContract.StoreEntry.COLUMN_SUPPLIER + " TEXT, " + StoreContract.StoreEntry.COLUMN_SUPPLIER_PHONE + " TEXT); ";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_STORE_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}