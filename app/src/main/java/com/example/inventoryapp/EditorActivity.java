package com.example.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventoryapp.data.StoreContract.StoreEntry;
import com.example.inventoryapp.data.StoreDbHelper;

public class EditorActivity extends AppCompatActivity {

    EditText iPhoneEdtTxt, priceEdtTxt, quantityEdtTxt, supplierEdtTxt, phoneEdtTxt;
    Button incrementBtn, decrementBtn, addiPhoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

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

                insertPet();

                // TODO: Setup increment and decrement buttons
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

    public void insertPet() {

        // Create instance of the StoreDbHelper class to manage connection between UI and database
        StoreDbHelper mDbHelper = new StoreDbHelper(this);

        // Obtain the database in writable mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

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

            // Insert values into database
            long newRowId = db.insert(StoreEntry.TABLE_NAME, null, values);

            Toast newToast = Toast.makeText(getApplicationContext(),"iPhone added!", Toast.LENGTH_SHORT);
            newToast.show();

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

}
