package com.example.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.inventoryapp.data.StoreContract;
import com.example.inventoryapp.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity {

    StoreDbHelper mDbHelper = new StoreDbHelper(this);
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertProduct();

        displayDatabaseInfo();

    }

    /**
     * Insert a new product with the appropriate values into the database
     */
    private void insertProduct() {
        db = mDbHelper.getReadableDatabase();
        // EXERCISE AREA
        ContentValues values = new ContentValues();
        values.put(StoreContract.COL_PRODUCT_NAME, "Some headphones");
        values.put(StoreContract.COL_PRICE, 4.50);
        values.put(StoreContract.COL_QUANTITY, 4);
        values.put(StoreContract.COL_SUPPLIER_NAME, "Richa and famous");
        values.put(StoreContract.COL_SUPPLIER_PHONE, 9876543);
        long newRowID = db.insert(StoreContract.TABLE_NAME, null, values);
        Log.i("MainActivity", "New ROW ID: " + newRowID);
        // END EXERCISE AREA
    }

    /**
     * TEMPORARY HELPER METHOD
     */
    private void displayDatabaseInfo() {

        String[] proj = {
          StoreContract._ID,
          StoreContract.COL_PRODUCT_NAME,
          StoreContract.COL_PRICE
        };

        Cursor cursor = db.query(
                StoreContract.TABLE_NAME,
                proj,
                null,
                null,
                null,
                null,
                null
        );


        try {
            TextView displayView = (TextView) findViewById(R.id.text_view_store);
            displayView.setText("Number of rows in store database table: " + cursor.getCount());
        } finally {
            cursor.close();
        }
    }

}
