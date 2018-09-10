package com.example.inventoryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.inventoryapp.data.StoreContract;
import com.example.inventoryapp.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("StoreDbHelper", "Current Database: " + StoreDbHelper.SQL_CREATE_ENTRIES);

        displayDatabaseInfo();

    }

    /**
     * TEMPORARY HELPER METHOD
     */
    private void displayDatabaseInfo() {
        StoreDbHelper mDbHelper = new StoreDbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + StoreContract.TABLE_NAME, null);

        try {
            TextView displayView = (TextView) findViewById(R.id.text_view_store);
            displayView.setText("Number of rows in store database table: " + cursor.getCount());
        } finally {
            cursor.close();
        }
    }

}
