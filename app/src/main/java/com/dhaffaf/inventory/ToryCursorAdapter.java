package com.dhaffaf.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dhaffaf.inventory.data.ToryContract.ToryEntry;

import java.text.BreakIterator;

import static android.content.ContentValues.TAG;

/**
 * Created by WIN8 on 27/01/18.
 */

public class ToryCursorAdapter extends CursorAdapter {

    public ToryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name_item);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_item);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_item);

        final int productId = cursor.getInt(cursor.getColumnIndex(ToryEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_PRODUCT_NAME));
        Double price = cursor.getDouble(cursor.getColumnIndex(ToryEntry.COLUMN_PRODUCT_PRICE));
        final int count = cursor.getInt(cursor.getColumnIndex(ToryEntry.COLUMN_PRODUCT_QUANTITY));

        nameTextView.setText(name);
        priceTextView.setText("Cost: " + String.format("%.2f", price) + " SR");
        quantityTextView.setText(String.valueOf(count));

        Button saleBtn = (Button) view.findViewById(R.id.sale_btn);

        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentCount = 0;
                String howCount = quantityTextView.getText().toString();
                if (!TextUtils.isEmpty(howCount)) {
                    currentCount = Integer.parseInt(howCount);
                    if (currentCount == 0) {
                        Log.i(TAG, "buy product has reach to limit ");
                    } else {
                        if (currentCount > 0) {
                            int newCount = (currentCount >= 1) ? currentCount - 1 : 0;
                            ContentValues values = new ContentValues();
                            values.put(ToryEntry.COLUMN_PRODUCT_QUANTITY, newCount);
                            Uri itemUri = ContentUris.withAppendedId(ToryEntry.CONTENT_URI, productId);
                            int numRowsUpdated = context.getContentResolver().update(itemUri, values, null, null);

                            if (numRowsUpdated > 0) {
                                Log.i(TAG, "Buy product successfully");
                                quantityTextView.setText(String.valueOf(newCount));
                            } else {
                                Log.i(TAG, "Could not update buy product!");
                            }
                        }
                    }
                }
            }
        });
    }
}
