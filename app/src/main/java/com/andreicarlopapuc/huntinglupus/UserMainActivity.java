package com.andreicarlopapuc.huntinglupus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class UserMainActivity extends ActionBarActivity {

    Button btnViewProducts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);

        // view products click event
        btnViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), UserAllProductsExpandableList.class);
                startActivity(i);

            }
        });


    }
}
