package com.example.android.inventoryapp_stage1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.example.android.inventoryapp_stage1.data.StoreContract;
import com.example.android.inventoryapp_stage1.data.StoreDbHelper;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_PRODUCT_NAME;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_PRICE;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_QUANTITY;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_SUPPLIER;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.COLUMN_SUPPLIER_PHONE;
import static com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the pet data loader
     */
    private static final int STORE_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    StoreCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        Button addBook = (Button) findViewById(R.id.button_add_a_book);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView storeListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.text_empty_view);
        storeListView.setEmptyView(emptyView);

        mCursorAdapter = new StoreCursorAdapter(this, null);
        storeListView.setAdapter(mCursorAdapter);

        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(StoreContract.StoreEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentBookUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(STORE_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {StoreContract.StoreEntry._ID, StoreContract.StoreEntry.COLUMN_PRODUCT_NAME, StoreContract.StoreEntry.COLUMN_PRICE, StoreContract.StoreEntry.COLUMN_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                StoreContract.StoreEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link StoreCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}

