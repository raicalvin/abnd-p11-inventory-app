package com.example.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.inventoryapp.data.StoreContract;

/**
 * This will populate list items in the list view for all iPhones
 */
public class StoreCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link StoreCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Create new list item
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the list item layout
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the iPhone data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.summary);

        // Find the columns of data
        int nameColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_IPHONE_NAME);
        int breedColumnIndex = cursor.getColumnIndex(StoreContract.StoreEntry.COLUMN_PRICE);

        // Read iPhone data from Cursor
        String iPhoneName = cursor.getString(nameColumnIndex);
        String iPhonePrice = cursor.getString(breedColumnIndex);

        // Update the product details
        nameTextView.setText(iPhoneName);
        priceTextView.setText(iPhonePrice);
    }
}
