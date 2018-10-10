package com.example.inventoryapp.data;

import android.provider.BaseColumns;

public final class StoreContract {

    /**
     * Declare empty constructor to ensure the class cannot be instantiated
     */
    private StoreContract() {}


    public static final class StoreEntry implements BaseColumns {

        public final static String TABLE_NAME = "iphones";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_IPHONE_NAME = "product";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";

        public final static int PRODUCT_IN_STOCK = 1;
        public final static int PRODUCT_OUT_OF_STOCK = 0;

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