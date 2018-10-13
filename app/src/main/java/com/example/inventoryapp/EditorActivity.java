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
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventoryapp.data.StoreContract.StoreEntry;
import com.example.inventoryapp.data.StoreDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** iPhone Data Loader Identifier */
    private static final int EXISTING_IPHONE_LOADER = 0;

    private Uri mCurrentIPhoneUri;

    EditText iPhoneEdtTxt, priceEdtTxt, quantityEdtTxt, supplierEdtTxt, phoneEdtTxt;
    Button incrementBtn, decrementBtn, addiPhoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentIPhoneUri = intent.getData();

        if (mCurrentIPhoneUri == null) {
            setTitle("Add New iPhone");
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_iphone));
        }

        iPhoneEdtTxt = (EditText) findViewById(R.id.iphone_name_edit);
        priceEdtTxt = (EditText) findViewById(R.id.price_edit_text);
        quantityEdtTxt = (EditText) findViewById(R.id.quantity_edit_text);
        supplierEdtTxt = (EditText) findViewById(R.id.supplier_edit_text);
        phoneEdtTxt = (EditText) findViewById(R.id.phone_edit_text);

        incrementBtn = (Button) findViewById(R.id.increment_quantity_button);
        decrementBtn = (Button) findViewById(R.id.decrement_quantity_button);

        addiPhoneBtn = (Button) findViewById(R.id.add_iphone_button);

        addiPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveiPhone();
            }
        });

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

        getLoaderManager().initLoader(EXISTING_IPHONE_LOADER, null, this);

    }

    public void decrementQuantity() {
        String quantityText = quantityEdtTxt.getText().toString().trim();
        int quantity = 0;
        if (quantityText.isEmpty() || quantityText.equalsIgnoreCase("0")) {
            // Do nothing
        } else {
            quantity = Integer.parseInt(quantityText) - 1;
        }
        quantityEdtTxt.setText(Integer.toString(quantity));
    }

    public void incrementQuantity() {
        String quantityText = quantityEdtTxt.getText().toString().trim();
        int quantity = 0;
        if (quantityText.isEmpty()) {
            quantity = 1;
        } else {
            quantity = Integer.parseInt(quantityText) + 1;
        }
        quantityEdtTxt.setText(Integer.toString(quantity));
    }

    public void saveiPhone() {

        // Obtain input from fields in UI
        String iPhoneName = iPhoneEdtTxt.getText().toString().trim();
        String iPhonePrice = priceEdtTxt.getText().toString().trim();
        String iPhoneQuantity = quantityEdtTxt.getText().toString().trim();
        String iPhoneSupplier = supplierEdtTxt.getText().toString().trim();
        String iPhoneNumber = phoneEdtTxt.getText().toString().trim();

        // Perform data validation for input
        boolean inputIsValid = isInputValid(iPhoneName, iPhonePrice, iPhoneQuantity, iPhoneSupplier, iPhoneNumber);

        if (inputIsValid) {

            ContentValues values = new ContentValues();
            values.put(StoreEntry.COLUMN_IPHONE_NAME, iPhoneName);
            values.put(StoreEntry.COLUMN_PRICE, Double.valueOf(iPhonePrice));
            values.put(StoreEntry.COLUMN_QUANTITY, Integer.parseInt(iPhoneQuantity));
            values.put(StoreEntry.COLUMN_SUPPLIER_NAME, iPhoneSupplier);
            values.put(StoreEntry.COLUMN_SUPPLIER_PHONE, iPhoneNumber);

            // Show toast if insertion was successful or failed
            if (mCurrentIPhoneUri == null) {
                Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_iphone_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_iphone_successful), Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentIPhoneUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_iphone_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_iphone_successful), Toast.LENGTH_SHORT).show();
                }
            }

            // Close and go back to previous activity
            finish();

        } else {
            // Do nothing
        }

    }

    public boolean isInputValid(String name, String price, String quantity, String supplier, String number) {
        if (name.length() == 0) {
            Toast newToast = Toast.makeText(getApplicationContext(),"Please enter a valid product name.", Toast.LENGTH_SHORT);
            newToast.show();
            return false;
        }
        if (price.length() == 0) {
            Toast newToast = Toast.makeText(getApplicationContext(),"Please enter a valid price.", Toast.LENGTH_SHORT);
            newToast.show();
            return false;
        }
        if (quantity.length() == 0) {
            Toast newToast = Toast.makeText(getApplicationContext(),"Please enter a valid quantity.", Toast.LENGTH_SHORT);
            newToast.show();
            return false;
        }
        if (supplier.isEmpty()) {
            Toast newToast = Toast.makeText(getApplicationContext(),"Please enter a supplier name.", Toast.LENGTH_SHORT);
            newToast.show();
            return false;
        }
        if (number.length() != 10) {
            Toast newToast = Toast.makeText(getApplicationContext(),"Please enter a valid 10-digit phone number.", Toast.LENGTH_SHORT);
            newToast.show();
            return false;
        }
        return true;

    }

    /** CURSORLOADER METHODS IMPLEMENTED BELOW */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mCurrentIPhoneUri == null) {
            return null;
        }

        String[] project = {
                StoreEntry._ID,
                StoreEntry.COLUMN_IPHONE_NAME,
                StoreEntry.COLUMN_PRICE,
                StoreEntry.COLUMN_QUANTITY,
                StoreEntry.COLUMN_SUPPLIER_NAME,
                StoreEntry.COLUMN_SUPPLIER_PHONE
        };

        return new CursorLoader(
                this,
                mCurrentIPhoneUri,
                project,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_IPHONE_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_PHONE);


            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            Double price = cursor.getDouble(priceColumnIndex);
            Long quantity = cursor.getLong(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);

            // Update the views on the screen with the values from the database
            iPhoneEdtTxt.setText(name);
            priceEdtTxt.setText(price.toString());
            quantityEdtTxt.setText(quantity.toString());
            supplierEdtTxt.setText(supplier);
            phoneEdtTxt.setText(phone);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        iPhoneEdtTxt.setText("");
        priceEdtTxt.setText("");
        quantityEdtTxt.setText("");
        supplierEdtTxt.setText("");
        phoneEdtTxt.setText("");
    }
}
