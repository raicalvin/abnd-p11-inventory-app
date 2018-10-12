package com.example.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.inventoryapp.data.StoreContract.StoreEntry;
import com.example.inventoryapp.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Button addItemButton;

    SQLiteDatabase db;

    RelativeLayout emptyView;

    private static final int STORE_LOADER = 0; // Arbitrary integer value for loader

    StoreCursorAdapter mCursorAdapter; // Adapter for our List View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyView = (RelativeLayout) findViewById(R.id.empty_view);

        // Link button to open EditorActivity
        addItemButton = (Button) this.findViewById(R.id.open_editor_activity);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView that will have the iPhone data
        ListView iPhoneListView = (ListView) findViewById(R.id.list);

        // Setup Adapter and attach to list view
        mCursorAdapter = new StoreCursorAdapter(this, null);

        // Attach adapter to ListView
        iPhoneListView.setAdapter(mCursorAdapter);

        if (mCursorAdapter.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            iPhoneListView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            iPhoneListView.setVisibility(View.VISIBLE);
        }

        // Start loader
        getLoaderManager().initLoader(STORE_LOADER, null, this);

    }

    // Override the onStart() method to refresh the database
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Insert a new product with the appropriate values into the database.
     */
    private void insertProduct() {

        ContentValues values = new ContentValues();
        values.put(StoreEntry.COLUMN_IPHONE_NAME, "iPhone Xs Max");
        values.put(StoreEntry.COLUMN_PRICE, 999.99);
        values.put(StoreEntry.COLUMN_QUANTITY, 4);
        values.put(StoreEntry.COLUMN_SUPPLIER_NAME, "Apple");
        values.put(StoreEntry.COLUMN_SUPPLIER_PHONE, "9167652212");

        // Insert the values into the database:
        Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define projection specifying columns from table
        String[] project = {
                StoreEntry._ID,
                StoreEntry.COLUMN_IPHONE_NAME,
                StoreEntry.COLUMN_PRICE,
                StoreEntry.COLUMN_QUANTITY,
                StoreEntry.COLUMN_SUPPLIER_NAME,
                StoreEntry.COLUMN_SUPPLIER_PHONE
        };

        // Perform query method on background thread:
        return new CursorLoader(this,
                StoreEntry.CONTENT_URI,
                project,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback for when data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
