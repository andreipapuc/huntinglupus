package com.andreicarlopapuc.huntinglupus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainScreenActivity extends Activity{

    Button btnUserMode;
    Button btnAdminMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Buttons
        btnUserMode = (Button) findViewById(R.id.btnUserMode);
        btnAdminMode = (Button) findViewById(R.id.btnAdminMode);

        // view products click event
        btnUserMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnAdminMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), AdminMainActivity.class);
                startActivity(i);

            }
        });
    }
}
