package com.example.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.data.StoreContract;

/**
 * This will populate list items in the list view for all iPhones
 */
public class StoreCursorAdapter extends CursorAdapter {

    private MainActivity activity;
    public StoreCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    /** Construct a new StoreCursorAdapter */
    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /** Creates a new list item */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate and return the list item layout
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /** Binds iPhone data to given list item layout */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        // Find the columns of data
        int nameColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_IPHONE_NAME);
        int priceColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_QUANTITY);

        // Read iPhone data from Cursor
        String iPhoneName = cursor.getString(nameColumnIndex);
        String iPhonePrice = cursor.getString(priceColumnIndex);
        final String iPhoneQuantity = cursor.getString(quantityColumnIndex);

        // Update the product details
        nameTextView.setText(iPhoneName);
        priceTextView.setText("$" + iPhonePrice);
        quantityTextView.setText("Stock: " + iPhoneQuantity);

        /** Update quantity using the sale button */
        ImageButton saleButton = (ImageButton) view.findViewById(R.id.sale_button);

        int idColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry._ID);
        final String iPhoneId = cursor.getString(idColumnIndex);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long soldQuantity = Long.parseLong(iPhoneQuantity) - 1;
                if (soldQuantity < 0) {
                    Toast.makeText(v.getContext(), "Hey! You have no iPhones left!", Toast.LENGTH_SHORT).show();
                } else {
                    activity.sellItem(soldQuantity, iPhoneId);
                }
            }
        });

    }
}
