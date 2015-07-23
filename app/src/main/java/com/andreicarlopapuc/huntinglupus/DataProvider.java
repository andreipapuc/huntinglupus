package com.andreicarlopapuc.huntinglupus;

/**
 * Created by Alex on 7/20/2015.
 */


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



    public class DataProvider extends ListActivity {

        // Creating JSON Parser object
        JSONParser jParser = new JSONParser();

        ArrayList<HashMap<String, String>> productsList;
        // url set to current wamp server, replace when you get to host server on another platform
        // url to get all products list
        private static String url_all_products = "http://192.168.1.11/hl_androidcon/get_all_products.php";

        // JSON Node names
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_PRODUCTS = "products";
        private static final String TAG_IDNUM = "idnum";
        private static final String TAG_NAME = "name";

        // products JSONArray
        JSONArray products = null;

        protected ArrayList<HashMap<String, String>> doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_IDNUM);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_IDNUM, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                    return productsList;
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return productsList;
        }









    }

/*

        OLD CODE:


        public static HashMap<String, List<String>> getInfo()
        {

            HashMap<String, List<String>> ThingDetails = new HashMap<String, List<String>>();

            List<String> Tools = new ArrayList<String>();
            Tools.add("Ciocan");
            Tools.add("Surubelnita");


            List<String> Furniture = new ArrayList<String>();
            Furniture.add("Scaune");
            Furniture.add("Tabureti");


            List<String> Keys= new ArrayList<String>();
            Keys.add("Intrare");
            Keys.add("Baie");

            ThingDetails.put("Tools", Tools);
            ThingDetails.put("Furniture", Furniture);
            ThingDetails.put("Keys", Keys);

            return ThingDetails;

        }
*/





