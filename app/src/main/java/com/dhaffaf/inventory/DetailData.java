package com.dhaffaf.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dhaffaf.inventory.data.ToryContract;
import com.dhaffaf.inventory.data.ToryContract.ToryEntry;

public class DetailData extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri currentUri;
    private TextView showName;
    private TextView showPrice;
    private TextView showQuantity;
    private TextView showSupName;
    private TextView showSupEmail;
    private TextView showSupPhone;
    private static final int BELOADER = 1;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ToryCursorAdapter mCursorAdapter;
    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        showName = (TextView) findViewById(R.id.name);
        showPrice = (TextView) findViewById(R.id.price);
        showQuantity = (TextView) findViewById(R.id.quantity);
        showSupName = (TextView) findViewById(R.id.sub_name);
        showSupEmail = (TextView) findViewById(R.id.sub_mail);
        showSupPhone = (TextView) findViewById(R.id.sub_phone);

        Button deleteItem = (Button) findViewById(R.id.Delete_Product);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
        Button orderSupplier = (Button) findViewById(R.id.order_from_sub);
        orderSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderThroughEmail();
                dialPhoneNumber();
            }
        });
        Button editProduct = (Button) findViewById(R.id.edit_product);
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailData.this, EditData.class);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });

        Button updateQuantity = (Button) findViewById(R.id.save_update_quantity);
        updateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSaveButton();
            }
        });
        Intent myIntent = getIntent();
        //this will be used ini the whole activity to use with content resolver
        currentUri = myIntent.getData();
        if (currentUri != null) {
            setTitle(R.string.product_detail);
            getLoaderManager().initLoader(BELOADER, null, this).forceLoad();
        }
    }

    private void updateSaveButton() {
        //start read from input fields
        String quantityString = showQuantity.getText().toString().trim();
        if (currentUri == null &&
                TextUtils.isEmpty(quantityString)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToryEntry.COLUMN_PRODUCT_QUANTITY, quantityString);

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
        }
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        // Only perform the delete if this is an existing product.
        if (currentUri != null) {
            int rowsDeleted = this.getContentResolver().delete(currentUri, "_ID=?", new String[]{String.valueOf(ContentUris.parseId(currentUri))});

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.delete_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.delete_product_successful), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    private void displayingQuantity(int numberOfProduct) {
        showQuantity.setText("" + numberOfProduct);
    }

    public void decreaseQuantity(View view) {
        String quantityString = showQuantity.getText().toString().trim();
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
            if (quantity == 0)
                Toast.makeText(this, "You cannot have less than 1 Quantity of product !", Toast.LENGTH_SHORT).show();
            else {
                quantity = quantity - 1;
                displayingQuantity(quantity);
            }
        }
    }

    public void increaseQuantity(View view) {
        String quantityString = showQuantity.getText().toString().trim();
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
            if (quantity == 100)
                Toast.makeText(this, "You cannot have more than 100 Quantity of product !", Toast.LENGTH_SHORT).show();
            else {
                quantity = quantity + 1;
                displayingQuantity(quantity);
            }
        }
    }

    private void orderThroughEmail() {
        try {
            Intent composeEmailIntent = new Intent(Intent.ACTION_SENDTO);
            composeEmailIntent.setType("text/plain");
            composeEmailIntent.setData(Uri.parse("mailto:"));
            composeEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{showSupEmail.getText().toString()});
            composeEmailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_from_sub));
            startActivity(composeEmailIntent);
        } catch (ActivityNotFoundException anfEx) {
            Log.e(DetailData.class.getSimpleName(), anfEx.getMessage());
        }
    }

    public void dialPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + showSupPhone.getText().toString()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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