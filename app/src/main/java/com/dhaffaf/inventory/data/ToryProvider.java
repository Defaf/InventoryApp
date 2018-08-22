package com.dhaffaf.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.dhaffaf.inventory.data.ToryContract.ToryEntry;

/**
 * Created by WIN8 on 27/01/18.
 */

public class ToryProvider extends ContentProvider {

    private ToryDbHelper mHelper;
    private static final int PRODUCT = 100;
    private static final int PRODUCT_ID = 101;
    public static final String LOG_TAG = ToryProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //call one time to store the both uri in contract
    static {
        sUriMatcher.addURI(ToryContract.CONTENT_AUTHORITY, ToryEntry.PATH_INVENTORY, PRODUCT);
        sUriMatcher.addURI(ToryContract.CONTENT_AUTHORITY, ToryContract.ToryEntry.PATH_INVENTORY + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new ToryDbHelper(getContext());
        return true;
    }

    @Override //select
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;
        switch (getType(uri)) {
            case ToryEntry.CONTENT_LIST_TYPE:
                //send hole table
                cursor = database.query(ToryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                Log.v(LOG_TAG, "processing to select the table");
                break;
            case ToryEntry.CONTENT_ITEM_TYPE:
                selection = ToryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ToryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                Log.v(LOG_TAG, "processing to select item ");
                break;
        }
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) { //send my uri and then return content type 100 or 101
        final int uriCode = sUriMatcher.match(uri);
        switch (uriCode) {
            case PRODUCT:
                return ToryEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ToryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + uriCode);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if (sUriMatcher.match(uri) != PRODUCT) {
            throw new IllegalArgumentException("Insertion is not supported for " + sUriMatcher.match(uri));
        } else {
            long id = db.insert(ToryEntry.TABLE_NAME, null, contentValues);
            if (id != -1) {
                return ContentUris.withAppendedId(ToryEntry.CONTENT_URI, id);
            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted = 0;
        switch (getType(uri)) {
            case ToryEntry.CONTENT_LIST_TYPE:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ToryEntry.TABLE_NAME, null, null);
                break;
            case ToryEntry.CONTENT_ITEM_TYPE:
                rowsDeleted = database.delete(ToryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = 0;

        switch (getType(uri)) {
            case ToryEntry.CONTENT_LIST_TYPE:
                count = db.update(ToryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
            case ToryEntry.CONTENT_ITEM_TYPE:
                count = db.update(ToryEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        }
        return count;
    }
}
