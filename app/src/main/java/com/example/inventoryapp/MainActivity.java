package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventoryapp.data.StoreContract.StoreEntry;
import com.example.inventoryapp.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity {

    Button addItemButton;

    // This is used to help us create, manage, and open database connections
    StoreDbHelper mDbHelper = new StoreDbHelper(this);

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
     * TODO: Comment this entire method out once the EditorActivity is done
     */
    private void insertProduct() {
        db = mDbHelper.getReadableDatabase();

        // EXERCISE AREA
        ContentValues values = new ContentValues();
        values.put(StoreEntry.COLUMN_IPHONE_NAME, "iPhone Xs Max");
        values.put(StoreEntry.COLUMN_PRICE, 999.99);
        values.put(StoreEntry.COLUMN_QUANTITY, 4);
        values.put(StoreEntry.COLUMN_SUPPLIER_NAME, "Apple");
        values.put(StoreEntry.COLUMN_SUPPLIER_PHONE, "9167652212");
        long newRowID = db.insert(StoreEntry.TABLE_NAME, null, values);
        Log.i("MainActivity", "New ROW ID: " + newRowID);
        // END EXERCISE AREA

    }

    /**
     * TEMPORARY HELPER METHOD
     */
    private void displayDatabaseInfo() {

        String[] project = {
            StoreEntry._ID,
            StoreEntry.COLUMN_IPHONE_NAME,
            StoreEntry.COLUMN_PRICE,
            StoreEntry.COLUMN_QUANTITY,
            StoreEntry.COLUMN_SUPPLIER_NAME,
            StoreEntry.COLUMN_SUPPLIER_PHONE
        };

        Cursor cursor = db.query(
                StoreEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = (TextView) findViewById(R.id.text_view_store);

        try {
            displayView.setText("The iphones table contains " + cursor.getCount() + " iPhones.\n\n");
            displayView.append(StoreEntry._ID + " - " +
            StoreEntry.COLUMN_IPHONE_NAME + " - " +
            StoreEntry.COLUMN_PRICE + " - " +
            StoreEntry.COLUMN_QUANTITY + " - " +
            StoreEntry.COLUMN_SUPPLIER_NAME + " - " +
            StoreEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Obtain column index of each column
            int idColumnIndex = cursor.getColumnIndex(StoreEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_IPHONE_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {
                // Use index to extract value for each column
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                double currentPrice = cursor.getDouble(priceColumnIndex);
                long currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);

                // Display value from each column inside TextView
                displayView.append(("\n" +
                        currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentPhone));
            }

        } finally {
            cursor.close();
        }
    } // END TEMPORARY HELPER METHOD

}
