package com.example.android.inventoryapp_stage1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.inventoryapp_stage1.data.StoreDbHelper;

import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_PRODUCT_NAME;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_PRICE;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_QUANTITY;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_SUPPLIER;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_SUPPLIER_PHONE;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.TABLE_NAME;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry._ID;

public class MainActivity extends AppCompatActivity {

    private StoreDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new StoreDbHelper(this);
    }

    /*
Start activity and call insertData to populate database with one result
     */
    @Override
    protected void onStart() {
        super.onStart();
        insertData();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the catalogue database.
     */
    private void displayDatabaseInfo() {
        StoreDbHelper mDbHelper = new StoreDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] tableColumns = new String[]{_ID, COLUMN_PRODUCT_NAME, COLUMN_PRICE};
        Cursor cursor = db.query(TABLE_NAME, tableColumns, null, null, null, null, null);
        TextView displayView = findViewById(R.id.text_cursor);

        try {
            displayView.setText("The catalogue table contains " + cursor.getCount() + " books.\n\n");
            displayView.append(_ID + " - " + COLUMN_PRODUCT_NAME + " - " + COLUMN_PRICE + "\n");
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(_ID);
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(COLUMN_PRICE);
            //Position cursor
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                displayView.append(("\n" + currentID + " - " + currentName + " - " + currentPrice));
            }
        } finally {
            cursor.close();
        }
    }

    /*
    This method starts to populate the database
     */
    private void insertData() {

        StoreDbHelper mDbHelper = new StoreDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //Values for db
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, "Book1");
        values.put(COLUMN_PRICE, "5");
        values.put(COLUMN_QUANTITY, "10");
        values.put(COLUMN_SUPPLIER, "Supplier1");
        values.put(COLUMN_SUPPLIER_PHONE, "P:0123456789");
        //Db insert
        long newRowId = db.insert(TABLE_NAME, null, values);
        Log.v("Catalog Activity", "new row editor" + newRowId);
    }
}
