package com.andreicarlopapuc.huntinglupus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends ActionBarActivity {

    Button btnAdminViewProducts;
    Button btnAdminCreateProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_activity);

        // Buttons
        btnAdminViewProducts = (Button) findViewById(R.id.btnAdminViewProducts);
        btnAdminCreateProduct = (Button) findViewById(R.id.btnAdminCreateProduct);

        // view products click event
        btnAdminViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AdminAllProductsActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnAdminCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), NewProductActivity.class);
                startActivity(i);

            }
        });
    }
}