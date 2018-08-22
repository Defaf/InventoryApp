package com.dhaffaf.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dhaffaf.inventory.data.ToryContract.ToryEntry;
/**
 * Created by WIN8 on 22/01/18.
 */

public class ToryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ToryDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "product.db";//Name of the database file
    private static final int DATABASE_VERSION = 1;//Database version.

    /**
     * Constructs a new instance of ToryDbHelper
     *
     * @param context of the app
     */
    public ToryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_TORY_TABLE =  "CREATE TABLE " + ToryEntry.TABLE_NAME + " ("
                + ToryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ToryEntry.COLUMN_PRODUCT_PRICE + " NUMERIC NOT NULL, "
                + ToryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + ToryEntry.COLUMN_PRODUCT_IMAGE + " BLOB,"
                + ToryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL,"
                + ToryEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL,"
                + ToryEntry.COLUMN_SUPPLIER_PHONE + " LONG NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TORY_TABLE);
        Log.v(LOG_TAG, "Inventory DataBase has been create it succesfully !");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // The database is still at version 1
    }
}
