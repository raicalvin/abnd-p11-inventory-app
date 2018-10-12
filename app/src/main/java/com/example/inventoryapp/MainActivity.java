package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inventoryapp.data.StoreContract.StoreEntry;
import com.example.inventoryapp.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity {

    Button addItemButton;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertProduct();

        displayDatabaseInfo();

        // Link button to open EditorActivity
        addItemButton = (Button) this.findViewById(R.id.open_editor_activity);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    // Override the onStart() method to refresh the database
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
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

    private void displayDatabaseInfo() {

        String[] project = {
            StoreEntry._ID,
            StoreEntry.COLUMN_IPHONE_NAME,
            StoreEntry.COLUMN_PRICE,
            StoreEntry.COLUMN_QUANTITY,
            StoreEntry.COLUMN_SUPPLIER_NAME,
            StoreEntry.COLUMN_SUPPLIER_PHONE
        };

        Cursor cursor = getContentResolver().query(
                StoreEntry.CONTENT_URI,
                project,
                null,
                null,
                null);

        // Find the ListView that will have the iPhone data
        ListView iPhoneListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of iPhone data
        StoreCursorAdapter adapter = new StoreCursorAdapter(this, cursor);

        // Attach adapter to ListView
        iPhoneListView.setAdapter(adapter);
    }

}
