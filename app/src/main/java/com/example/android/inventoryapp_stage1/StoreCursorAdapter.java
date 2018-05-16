package com.example.android.inventoryapp_stage1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp_stage1.data.StoreContract.StoreEntry;

import static com.example.android.inventoryapp_stage1.data.StoreProvider.LOG_TAG;

public class StoreCursorAdapter extends CursorAdapter {

    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

    }

    private Button mSale;

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the store data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find textview(s) in list_item.xml
        TextView nameTextView = (TextView) view.findViewById(R.id.text_product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.text_product_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.text_product_quantity);
        // Find constant values in columns
        int nameIndexColumn = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME);
        int priceIndexColumn = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
        int quantityIndexColumn = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
        //Get string from column
        String name = cursor.getString(nameIndexColumn);
        String price = cursor.getString(priceIndexColumn);
        String quantity = cursor.getString(quantityIndexColumn);


        final int quantityTextView_int = cursor.getInt(quantityIndexColumn);
        final Uri uri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, cursor.getInt(cursor.getColumnIndexOrThrow(StoreEntry._ID)));
        //Display strings in list_item.xml
        nameTextView.setText(name);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);

        mSale = view.findViewById(R.id.button_sale);
        mSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityTextView_int > 0) {
                    int newQuantity = quantityTextView_int - 1;

                    ContentValues values = new ContentValues();
                    values.put(StoreEntry.COLUMN_QUANTITY, newQuantity);
                    context.getContentResolver().update(uri, values, null, null);
                    Log.d(LOG_TAG, "URI for update: " + uri);
                } else {
                    Toast.makeText(context, "Sold out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


