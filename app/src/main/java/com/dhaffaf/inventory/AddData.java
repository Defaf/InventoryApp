package com.dhaffaf.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dhaffaf.inventory.data.ToryContract.ToryEntry;

public class AddData extends AppCompatActivity {

    private EditText pNameEdit;
    private EditText pPriceEdit;
    private EditText pQuantityEdit;
    private EditText sNameEdit;
    private EditText sEmailEdit;
    private EditText sPhonelEdit;
    private Button xSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        pNameEdit = (EditText) findViewById(R.id.add_product_name_edit_text);
        pPriceEdit = (EditText) findViewById(R.id.add_product_price_edit_text);
        pQuantityEdit = (EditText) findViewById(R.id.add_product_quantity_edit_text);
        sNameEdit = (EditText) findViewById(R.id.add_product_sub_name);
        sEmailEdit = (EditText) findViewById(R.id.add_product_sub_mail);
        sPhonelEdit = (EditText) findViewById(R.id.add_product_sub_phone);
        xSaveButton = (Button) findViewById(R.id.btn_save_product);
        xSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save product method
                saveNewProduct();
            }
        });
    }

    private void saveNewProduct() {

        String name = pNameEdit.getText().toString().trim();
        String price = pPriceEdit.getText().toString().trim();
        String quan = pQuantityEdit.getText().toString().trim();
        String sName = sNameEdit.getText().toString().trim();
        String sEmail = sEmailEdit.getText().toString().trim();
        String sPhone = sPhonelEdit.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || quan.isEmpty() || sName.isEmpty() ||
                sEmail.isEmpty() || sPhone.isEmpty()) {
            Toast.makeText(this, "All Fields are required to fill it", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ToryEntry.COLUMN_PRODUCT_NAME, name);
            contentValues.put(ToryEntry.COLUMN_PRODUCT_PRICE, price);
            contentValues.put(ToryEntry.COLUMN_PRODUCT_QUANTITY, quan);
            contentValues.put(ToryEntry.COLUMN_SUPPLIER_NAME, sName);
            contentValues.put(ToryEntry.COLUMN_SUPPLIER_EMAIL, sEmail);
            contentValues.put(ToryEntry.COLUMN_SUPPLIER_PHONE, sPhone);

            Uri mUri =this.getContentResolver().insert(ToryEntry.CONTENT_URI, contentValues);

            if (mUri == null) {
                // Error inserting new row
                Toast.makeText(this, "Error in saving product !", Toast.LENGTH_SHORT).show();
            } else {
                // Success
                Toast.makeText(this, "Product has been saved succesfuly", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
            }
            finish();
        }
    }

}
