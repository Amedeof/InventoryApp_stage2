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

    // Show CatalogActivity with solution code:
    @Override
    protected void onStart() {
        super.onStart();
        insertPet();
        displayDatabaseInfo();
    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        StoreDbHelper mDbHelper = new StoreDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String [] tableColumns = new String[] {_ID, COLUMN_PRODUCT_NAME,COLUMN_PRICE};
        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.query(TABLE_NAME, tableColumns, null, null, null, null, null);

        TextView displayView = (TextView) findViewById(R.id.text_cursor);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The catalogue table contains " + cursor.getCount() + " books.\n\n");
            displayView.append(_ID + " - " +
                    COLUMN_PRODUCT_NAME + " - " + COLUMN_PRICE +"\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(_ID);
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(COLUMN_PRICE);


            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);


                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertPet() {

        StoreDbHelper mDbHelper = new StoreDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, "Book1");
        values.put(COLUMN_PRICE, "5");
        values.put(COLUMN_QUANTITY, "10");
        values.put(COLUMN_SUPPLIER, "Supplier1");
        values.put(COLUMN_SUPPLIER_PHONE, "P:0123456789");

        // Create and/or open a database to read from it

        long newRowId = db.insert(TABLE_NAME, null, values);

        Log.v("Catalog Activity", "new row editor" + newRowId);
    }
}
