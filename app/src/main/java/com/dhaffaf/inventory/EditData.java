package com.dhaffaf.inventory;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhaffaf.inventory.data.ToryContract.ToryEntry;

public class EditData extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri currentUri;
    private EditText showName;
    private EditText showPrice;
    private EditText showQuantity;
    private EditText showSupName;
    private EditText showSupEmail;
    private EditText showSupPhone;
    private static final int BELOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        showName = (EditText) findViewById(R.id.name);
        showPrice = (EditText) findViewById(R.id.price);
        showQuantity = (EditText) findViewById(R.id.quantity);
        showSupName = (EditText) findViewById(R.id.sub_name);
        showSupEmail = (EditText) findViewById(R.id.sub_mail);
        showSupPhone = (EditText) findViewById(R.id.sub_phone);

        Button saveEdit = (Button) findViewById(R.id.save_update_product);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditButton();
            }
        });
        Intent myIntent = getIntent();
        //this will be used ini the whole activity to use with content resolver
        currentUri = myIntent.getData();
        getLoaderManager().initLoader(BELOADER, null, this).forceLoad();
    }

    private void EditButton() {
        //start read from input fields
        String nameString = showName.getText().toString().trim();
        String priceString = showPrice.getText().toString().trim();
        String quantityString = showQuantity.getText().toString().trim();
        String supNameString = showSupName.getText().toString().trim();
        String supMailString = showSupEmail.getText().toString().trim();
        String supPhoneString = showSupPhone.getText().toString().trim();

        if (nameString.isEmpty() || priceString.isEmpty() || quantityString.isEmpty() || supNameString.isEmpty() ||
                supMailString.isEmpty() || supPhoneString.isEmpty()) {
            Toast.makeText(this, "All Fields are required to fill it", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ToryEntry.COLUMN_PRODUCT_NAME, nameString);
            contentValues.put(ToryEntry.COLUMN_PRODUCT_PRICE, priceString);
            contentValues.put(ToryEntry.COLUMN_PRODUCT_QUANTITY, quantityString);

            contentValues.put(ToryEntry.COLUMN_SUPPLIER_NAME, supNameString);
            contentValues.put(ToryEntry.COLUMN_SUPPLIER_EMAIL, supMailString);
            contentValues.put(ToryEntry.COLUMN_SUPPLIER_PHONE, supPhoneString);

            if (currentUri == null) {

                Uri newUri = this.getContentResolver().insert(ToryEntry.CONTENT_URI, contentValues);

                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, getString(R.string.editor_insert_product_failed), Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.editor_insert_product_successful), Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = this.getContentResolver().update(currentUri, contentValues, "_ID=?", new String[]{String.valueOf(ContentUris.parseId(currentUri))});

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, getString(R.string.update_product_failed), Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, getString(R.string.update_product_successful), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {ToryEntry._ID,
                ToryEntry.COLUMN_PRODUCT_NAME,
                ToryEntry.COLUMN_PRODUCT_PRICE,
                ToryEntry.COLUMN_PRODUCT_QUANTITY,
                ToryEntry.COLUMN_SUPPLIER_NAME,
                ToryEntry.COLUMN_SUPPLIER_EMAIL,
                ToryEntry.COLUMN_SUPPLIER_PHONE
        };

        return new CursorLoader(this, currentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToNext()) {
            showName.setText(cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_PRODUCT_NAME)));
            showPrice.setText(cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_PRODUCT_PRICE)));
            showQuantity.setText(cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_PRODUCT_QUANTITY)));
            showSupName.setText(cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_SUPPLIER_NAME)));
            showSupEmail.setText(cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_SUPPLIER_EMAIL)));
            showSupPhone.setText(cursor.getString(cursor.getColumnIndex(ToryEntry.COLUMN_SUPPLIER_PHONE)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //nothing at all
    }

}
