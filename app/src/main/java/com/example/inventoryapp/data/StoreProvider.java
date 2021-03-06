package com.example.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class StoreProvider extends ContentProvider {

    /** Codes for URI Matcher */
    private static final int IPHONES = 100;
    private static final int IPHONE_ID = 101;

    /** Setup the URI Matcher */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /** Add patterns to the URI Matcher */
    static {
        sUriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_IPHONES, IPHONES);
        sUriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_IPHONES + "/#", IPHONE_ID);
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = StoreProvider.class.getSimpleName();

    /** Database helper object */
    private StoreDbHelper mDbHelper;

    /** Initialize the provider and the database helper object */
    @Override
    public boolean onCreate() {
        mDbHelper = new StoreDbHelper(getContext());
        return true;
    }

    /**
     * Perform query for given URI. Use given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case IPHONES:
                cursor = database.query(StoreContract.StoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case IPHONE_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(StoreContract.StoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Oh no! Cannot query unknown URI: " + uri);
        }

        // Set notification URI on cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /** Insert new data into the provider with the given ContentValues */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // Figure out which pattern matches using the sUriMatcher:
        final int match = sUriMatcher.match(uri);

        // Then take the match and execute the proper case:
        switch (match) {
            case IPHONES:
                return insertIPhone(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for: " + uri);
        }
    }

    /**
     * Helper method to insert iPhone into database using URI
     * @param uri
     * @param values
     * @return
     */
    private Uri insertIPhone(Uri uri, ContentValues values) {
        // Get writable database:
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Insert the new iPhone with the given values:
        long id = database.insert(StoreContract.StoreEntry.TABLE_NAME, null, values);
        // If ID = -1, then insertion failed. Log an error:
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for: " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for iPhone uri
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table, return the new URI
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case IPHONES:
                return updateIPhone(uri, values, selection, selectionArgs);
            case IPHONE_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateIPhone(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for: " + uri);
        }
    }

    private int updateIPhone(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Sanity Checks for the Keys:
        if (values.containsKey(StoreContract.StoreEntry.COLUMN_IPHONE_NAME)) {
            String name = values.getAsString(StoreContract.StoreEntry.COLUMN_IPHONE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("iPhone requires a valid name.");
            }
        }
        if (values.containsKey(StoreContract.StoreEntry.COLUMN_PRICE)) {
            Double price = values.getAsDouble(StoreContract.StoreEntry.COLUMN_PRICE);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Price requires a valid price.");
            }
        }
        if (values.containsKey(StoreContract.StoreEntry.COLUMN_QUANTITY)) {
            Long quantity = values.getAsLong(StoreContract.StoreEntry.COLUMN_QUANTITY);
            if (quantity == null && quantity < 0) {
                throw new IllegalArgumentException("iPhone requires a proper quantity.");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(StoreContract.StoreEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }

    /**
     * Delete data at given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case IPHONES:
                rowsDeleted = database.delete(StoreContract.StoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case IPHONE_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(StoreContract.StoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * Returns MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case IPHONES:
                return StoreContract.StoreEntry.CONTENT_LIST_TYPE;
            case IPHONE_ID:
                return StoreContract.StoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
