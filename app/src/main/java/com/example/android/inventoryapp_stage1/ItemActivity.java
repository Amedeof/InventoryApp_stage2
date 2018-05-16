package com.example.android.inventoryapp_stage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.inventoryapp_stage1.data.StoreContract;

public class ItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri mCurrentBookUri;

    private TextView mName;
    private TextView mPrice;
    private TextView mQuantity;
    private TextView mSupplier;
    private TextView mPhone;
    private Button mIncreaseQuantity;
    private Button mDecreaseQuantity;
    private String bookQuantityString;
    private Button mCall;
    private Button mSave;
    private Button mDelete;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_item);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // Initialize a loader to read the pet data from the database
        // and display the current values in the editor
        getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);

        // Find all relevant views that we will need to read user input from
        mName = (TextView) findViewById(R.id.text_product_detail_name);
        mPrice = (TextView) findViewById(R.id.text_product_detail_price);
        mQuantity = (TextView) findViewById(R.id.text_product_detail_quantity);
        mSupplier = (TextView) findViewById(R.id.text_product_detail_supplier);
        mPhone = (TextView) findViewById(R.id.text_product_supplier_phone);

        mDecreaseQuantity = (Button) findViewById(R.id.button_decrease_quantity);
        mDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }

        });

        mIncreaseQuantity = (Button) findViewById(R.id.button_increase_quantity);
        mIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        mCall = (Button) findViewById(R.id.button_call);
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        mSave = findViewById(R.id.button_save_product);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });

        mDelete = findViewById(R.id.button_delete_product);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMessage();
            }
        });
    }

    public void increaseQuantity() {
        quantity = Integer.valueOf(mQuantity.getText().toString().trim());
        quantity = quantity + 1;
        mQuantity.setText(String.valueOf(quantity));
    }

    public void decreaseQuantity() {
        quantity = Integer.valueOf(mQuantity.getText().toString().trim());
        if (quantity > 0) {
            quantity = quantity - 1;
            mQuantity.setText(String.valueOf(quantity));
        } else {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have less than 0 book in stock";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void call() {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + mPhone.getText()));
        startActivity(call);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the catalogue table
        String[] projection = new String[]{StoreContract.StoreEntry._ID, StoreContract.StoreEntry.COLUMN_PRODUCT_NAME, StoreContract.StoreEntry.COLUMN_PRICE, StoreContract.StoreEntry.COLUMN_QUANTITY, StoreContract.StoreEntry.COLUMN_SUPPLIER, StoreContract.StoreEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_SUPPLIER);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Update the views on the screen with the values from the database
            mName.setText(name);
            mPrice.setText(Integer.toString(price));
            mQuantity.setText(Integer.toString(quantity));
            mSupplier.setText(supplier);
            mPhone.setText(supplierPhone);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mName.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSupplier.setText("");
        mPhone.setText("");
    }

    private void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deleteBook();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String bookNameString = mName.getText().toString().trim();
        String bookPriceString = mPrice.getText().toString().trim();
        bookQuantityString = mQuantity.getText().toString().trim();
        String supplierString = mSupplier.getText().toString().trim();
        String supplierPhoneString = mPhone.getText().toString().trim();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentBookUri == null && TextUtils.isEmpty(bookNameString) && TextUtils.isEmpty(bookPriceString) && TextUtils.isEmpty(bookQuantityString) && TextUtils.isEmpty(supplierString) && TextUtils.isEmpty(supplierPhoneString)) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

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

        // Determine if this is a new or existing pet by checking if mCurrentBookUri is null or not
        if (mCurrentBookUri == null) {
            // This is a NEW book, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(StoreContract.StoreEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Book unsaved.", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Book saved successfully.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING book, so update the pet with content URI: mCurrentBookUri
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Unsuccesful", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Succesful", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void deleteBook() {
        // Only perform the delete if this is an existing pet.

        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Book wasn't deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Book was deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }

        mName.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSupplier.setText("");
        mPhone.setText("");

        mSave.setEnabled(false);
        mDelete.setEnabled(false);
    }
}