package com.example.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.inventoryapp.data.StoreContract;

public class StoreDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StoreListings.dp";

    private static final String INTEGER_TYPE = " INTEGER, ";
    private static final String TEXT_TYPE = " TEXT, ";
    private static final String DECIMAL_TYPE = " DECIMAL, ";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StoreContract.TABLE_NAME + "(" + StoreContract._ID + INTEGER_TYPE +
                    StoreContract.COL_PRODUCT_NAME + TEXT_TYPE + StoreContract.COL_PRICE + DECIMAL_TYPE +
                    StoreContract.COL_QUANTITY + INTEGER_TYPE + StoreContract.COL_SUPPLIER_NAME + TEXT_TYPE +
                    StoreContract.COL_SUPPLIER_PHONE + " INTEGER);";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StoreContract.TABLE_NAME;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
