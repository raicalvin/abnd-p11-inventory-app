package com.example.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class StoreContract {

    /**
     * Declare empty constructor to ensure the class cannot be instantiated
     */
    private StoreContract() {}

    /**  Content Authority Creation */
    public static final String CONTENT_AUTHORITY = "com.example.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_IPHONES = "iphones";


    public static final class StoreEntry implements BaseColumns {

        /** MIME Types */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IPHONES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_IPHONES;


        public final static String TABLE_NAME = "iphones";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_IPHONE_NAME = "product";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";

        public final static int PRODUCT_IN_STOCK = 1;
        public final static int PRODUCT_OUT_OF_STOCK = 0;

        /** Content URI to access the iPhone data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_IPHONES);

    }

}

/**
* Define the Schema below to be used for the Contract above:
 *
 * TABLE_NAME = "iphones"
 *
 * _id      --- INTEGER
 * product  --- TEXT
 * price    --- DECIMAL
 * quantity --- INTEGER
 * supplier --- TEXT
 * phone    --- TEXT
 *
* */