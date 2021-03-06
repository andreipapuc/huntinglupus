package com.andreicarlopapuc.huntinglupus;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;


public class NewProductActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputPid;
    EditText inputName;
    CheckBox checkBoxCatTool;
    CheckBox checkBoxCatFurniture;
    CheckBox checkBoxCatKey;
    EditText inputDesc;
    Button btnCreateProduct;

    // url set to current wamp server, replace when you get to host server on another platform
    // url to create new product
    private static String url_create_product = "http://46.101.186.73/create_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        // Edit Text
        inputPid = (EditText) findViewById(R.id.inputPid);
        inputName = (EditText) findViewById(R.id.inputName);
        checkBoxCatTool = (CheckBox) findViewById(R.id.inputCategoryTool);
        checkBoxCatFurniture = (CheckBox) findViewById(R.id.inputCategoryFurniture);
        checkBoxCatKey = (CheckBox) findViewById((R.id.inputCategoryKey));
        inputDesc = (EditText) findViewById(R.id.inputDesc);

        // Create button
        btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);


        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // creating new product in background thread
                new CreateNewProduct().execute();
            }
        });
        // checkbox tool click event
        checkBoxCatTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(checkBoxCatTool.isChecked()) {
                    checkBoxCatFurniture.setChecked(false);
                    checkBoxCatKey.setChecked(false);
                }
            }
        });
        // checkbox furniture click event
        checkBoxCatFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(checkBoxCatFurniture.isChecked()) {
                    checkBoxCatTool.setChecked(false);
                    checkBoxCatKey.setChecked(false);
                }
            }
        });
        // checkbox key click event
        checkBoxCatKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(checkBoxCatKey.isChecked()) {
                    checkBoxCatFurniture.setChecked(false);
                    checkBoxCatTool.setChecked(false);
                }
            }
        });

    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String pid = inputPid.getText().toString();
            String name = inputName.getText().toString();
            String category;
            if(checkBoxCatTool.isChecked()) {
                category = "Category: Tool";
            } else if(checkBoxCatFurniture.isChecked()){
                category = "Category: Furniture";
            } else if(checkBoxCatKey.isChecked()){
                category = "Category: Key";
            } else {
                category = " ";
            }
            String description = inputDesc.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", pid));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("category", category));
            params.add(new BasicNameValuePair("description", description));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), AdminAllProductsActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}