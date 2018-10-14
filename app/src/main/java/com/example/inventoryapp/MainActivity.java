package com.example.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.inventoryapp.data.StoreContract.StoreEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Button addItemButton;

    RelativeLayout emptyView;

    ListView iPhoneListView;

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
        iPhoneListView = (ListView) findViewById(R.id.list);

        // Setup Adapter and attach to list view
        mCursorAdapter = new StoreCursorAdapter(this, null);

        // Attach adapter to ListView
        iPhoneListView.setAdapter(mCursorAdapter);

        // Setup item click listener to go to EditorActivity
        iPhoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentIPhoneUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, id);

                intent.setData(currentIPhoneUri);

                startActivity(intent);
            }
        });

        // Start loader
        getLoaderManager().initLoader(STORE_LOADER, null, this);

    }

    // Override the onStart() method to refresh the database
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Insert new product with appropriate values into the database
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

        // Toggle the empty view if no data
        if (mCursorAdapter.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            iPhoneListView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            iPhoneListView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback for when data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    public void sellItem(long quantity, String ID) {

        ContentValues values = new ContentValues();
        values.put(StoreEntry._ID, ID);
        values.put(StoreEntry.COLUMN_QUANTITY, quantity);

        Uri iPhoneSellUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, Long.parseLong(ID));

        // Update the quantity in database
        int rowsAffected = getContentResolver().update(iPhoneSellUri, values, null, null);

    }
}
