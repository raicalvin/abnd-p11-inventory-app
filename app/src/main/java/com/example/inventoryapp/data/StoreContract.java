package com.example.inventoryapp.data;

import android.provider.BaseColumns;

public final class StoreContract {

    /**
     * Declare empty constructor to ensure the class cannot be instantiated
     */
    private StoreContract() {}

    public final static String TABLE_NAME = "store";

    public final static String _ID = BaseColumns._ID;
    public final static String COL_PRODUCT_NAME = "product";
    public final static String COL_PRICE = "price";
    public final static String COL_QUANTITY = "quantity";
    public final static String COL_SUPPLIER_NAME = "supplier";
    public final static String COL_SUPPLIER_PHONE = "phone";

    public final static int PRODUCT_IN_STOCK = 1;
    public final static int PRODUCT_OUT_OF_STOCK = 0;

}
