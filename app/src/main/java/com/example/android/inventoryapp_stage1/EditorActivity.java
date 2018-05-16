package com.example.android.inventoryapp_stage1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp_stage1.data.StoreContract;

public class EditorActivity extends AppCompatActivity {

    private Uri mCurrentBookUri;
    /**
     * EditText field to enter book's data
     */
    private TextView mNameEditText;
    private TextView mPriceEditText;
    private TextView mQuantityEditText;
    private TextView mSupplierEditText;
    private TextView mSupplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (TextView) findViewById(R.id.text_edit_name);
        mPriceEditText = (TextView) findViewById(R.id.text_edit_price);
        mQuantityEditText = (TextView) findViewById(R.id.text_edit_quantity);
        mSupplierEditText = (TextView) findViewById(R.id.text_edit_supplier_name);
        mSupplierPhoneEditText = (TextView) findViewById(R.id.text_edit_supplier_phone);

        // Setup FAB to open EditorActivity
        final Button saveBook = (Button) findViewById(R.id.button_save_product);
        saveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBook();
            }
        });
    }

    /**
     * Get user input from editor and save pet into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String bookNameString = mNameEditText.getText().toString().trim();
        String bookPriceString = mPriceEditText.getText().toString().trim();
        String bookQuantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentBookUri == null && TextUtils.isEmpty(bookNameString) && TextUtils.isEmpty(bookPriceString) && TextUtils.isEmpty(bookQuantityString) && TextUtils.isEmpty(supplierString) && TextUtils.isEmpty(supplierPhoneString)) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }


        if (TextUtils.isEmpty(bookNameString)) {
            Toast.makeText(this, "Name missing", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookPriceString)) {
            Toast.makeText(this, "price missing", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(supplierString)) {
            Toast.makeText(this, "supplier's name missing", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(supplierPhoneString)) {
            Toast.makeText(this, "supplier's phone missing", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bookQuantityString)) {
            Toast.makeText(this, "quantity missing", Toast.LENGTH_LONG).show();
            return;
        }
        // Create a ContentValues object where column names are the keys,
        // and book attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(StoreContract.StoreEntry.COLUMN_PRODUCT_NAME, bookNameString);
        values.put(StoreContract.StoreEntry.COLUMN_PRICE, bookPriceString);
        values.put(StoreContract.StoreEntry.COLUMN_QUANTITY, bookQuantityString);
        values.put(StoreContract.StoreEntry.COLUMN_SUPPLIER, supplierString);
        values.put(StoreContract.StoreEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);
        int price = 0;
        if (!TextUtils.isEmpty(bookPriceString)) {
            price = Integer.parseInt(bookPriceString);
        }
        values.put(StoreContract.StoreEntry.COLUMN_PRICE, price);

        // Determine if this is a new or existing book by checking if mCurrentBookUri is null or not
        if (mCurrentBookUri == null) {
            // NEW book > so insert a new book into the provider,
            Uri newUri = getContentResolver().insert(StoreContract.StoreEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Book unsaved", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Book saved successfully", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the book hasn't changed, continue with handling back button press
            super.onBackPressed();
    }
}

