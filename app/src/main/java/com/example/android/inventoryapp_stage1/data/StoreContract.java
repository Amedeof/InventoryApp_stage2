package com.example.android.inventoryapp_stage1.data;

import android.provider.BaseColumns;

public final class StoreContract {

    public class StoreEntry implements BaseColumns {

        // List of name of table and constants
        public static final String TABLE_NAME = "stock";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "ProductName";
        public static final String COLUMN_PRICE = "Price";
        public static final String COLUMN_QUANTITY = "Quantity";
        public static final String COLUMN_SUPPLIER = "SupplierName";
        public static final String COLUMN_SUPPLIER_PHONE = "SupplierPhoneNumber";

    }
}
