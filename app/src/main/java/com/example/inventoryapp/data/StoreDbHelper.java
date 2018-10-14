package com.example.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.inventoryapp.data.StoreContract.StoreEntry;

// This class helps create, manage, and open the database needed for inventory
public class StoreDbHelper extends SQLiteOpenHelper {

    /**
     * Constants for the database name and database version.
     * If the schema is changed, the version below must be updated.
     */
    public static final String DATABASE_NAME = "store.db";
    public static final int DATABASE_VERSION = 1;

    private static final String INTEGER_TYPE = " INTEGER, ";
    private static final String TEXT_TYPE = " TEXT, ";
    private static final String DECIMAL_TYPE = " DECIMAL, ";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StoreEntry.TABLE_NAME;

    /**
     * CONSTRUCTOR
     *
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
        /**
         * If there is no database, this method will create a database and then make
         * and instance of an SQLiteDatabase object and then return it to the UI activity
         * that asked for the database.
         */
        String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StoreEntry.TABLE_NAME + "(" +
                    StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    StoreEntry.COLUMN_IPHONE_NAME + TEXT_TYPE +
                    StoreEntry.COLUMN_PRICE + DECIMAL_TYPE +
                    StoreEntry.COLUMN_QUANTITY + INTEGER_TYPE +
                    StoreEntry.COLUMN_SUPPLIER_NAME + TEXT_TYPE +
                    StoreEntry.COLUMN_SUPPLIER_PHONE + " TEXT);";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     *
     * This will drop the old database and then create a new one.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES); // Delete the old database
        onCreate(db);                   // Create new database
    }
}
