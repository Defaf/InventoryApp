package com.dhaffaf.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dhaffaf.inventory.data.ToryContract.ToryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ToryCursorAdapter mCursorAdapter;
    private static final int PRODUCTLOADER = 0;
    private ListView listViewPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open AddActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(MainActivity.this, AddData.class);
                startActivity(addProductIntent);
            }
        });

        // Get list view to populate
        listViewPro = (ListView) findViewById(R.id.List_view_product);

        // Setup the empty view, if there is no data to be displayed
        View emptyView = findViewById(R.id.empty_view);
        listViewPro.setEmptyView(emptyView);

        mCursorAdapter = new ToryCursorAdapter(this, null);

        listViewPro.setAdapter(mCursorAdapter);
        listViewPro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DetailData.class);
                Uri currentProUri = ContentUris.withAppendedId(ToryEntry.CONTENT_URI, id);
                intent.setData(currentProUri);
                startActivity(intent);
            }
        });


        getLoaderManager().initLoader(PRODUCTLOADER, null, this);
    }

    private void deleteAllProduct() {
        int rowsDeleted = getContentResolver().delete(ToryEntry.CONTENT_URI, null, null);
        Log.v(LOG_TAG, rowsDeleted + " rows deleted from inventory database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                deleteAllProduct();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ToryEntry._ID,
                ToryEntry.COLUMN_PRODUCT_NAME,
                ToryEntry.COLUMN_PRODUCT_PRICE,
                ToryEntry.COLUMN_PRODUCT_QUANTITY,
                ToryEntry.COLUMN_PRODUCT_IMAGE,
                ToryEntry.COLUMN_SUPPLIER_NAME,
                ToryEntry.COLUMN_SUPPLIER_EMAIL,
                ToryEntry.COLUMN_SUPPLIER_PHONE};
        return new CursorLoader(this, ToryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            if (data.getCount() > 0) {
                mCursorAdapter.swapCursor(data);
            } else {
                Toast.makeText(this, "Sorry the DataBase is empty" , Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.v(LOG_TAG, " error ");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
