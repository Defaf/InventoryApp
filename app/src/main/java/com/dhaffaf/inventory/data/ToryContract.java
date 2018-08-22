package com.dhaffaf.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by WIN8 on 22/01/18.
 */

public final class ToryContract {

    public ToryContract() {
    }
    //package name  com.dhaffaf.inventory
    public static final String CONTENT_AUTHORITY = "com.dhaffaf.inventory";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class ToryEntry implements BaseColumns {
        //path name
        public static final String PATH_INVENTORY = "inventory";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        /**
         * The MIME type of the CONTENT_LIST_TYPE for a list of product
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

         // The MIME type of the CONTENT_ITEM_TYPE for a single product.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        /** Name of database table for inventory products */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the inventory product
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="name";

        /**
         * price of the product.
         * Type: NUMERIC
         */
        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * quantity of the product.
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * image of the product.
         * Type: BLOB
         */
        public final static String COLUMN_PRODUCT_IMAGE = "image";

        /**
         * Supplier name.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier";

        /**
         * Supplier email.
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_EMAIL  = "email";

        /**
         * Supplier phone number.
         * Type: LONG
         */
        public final static String COLUMN_SUPPLIER_PHONE  = "phone";
    }
}
