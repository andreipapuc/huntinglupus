package com.andreicarlopapuc.huntinglupus;

/**
 * Created by Alex on 7/25/2015.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.andreicarlopapuc.huntinglupus.adapters.ExpandableListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UserAllProductsExpandableList extends Activity {

    List<String> groupList;
    List<String> productPids;
    List<String> productIdnums;
    List<String> childList;
    HashMap<String, List<String>> productCollection;
    ExpandableListView expListView;
    Button  btnMapSearch;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private static String url_all_products = "http://www.huntinglupus.esy.es/get_all_products.php";
    private static String url_pid_upload = "http://www.huntinglupus.esy.es/findbypidcommit.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_IDNUM = "idnum";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_DESCR = "description";

    // products JSONArray
    JSONArray products = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_expandable);

        //fix networkosonmainthread error
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        createGroupList();
        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.product_list);
        //btnMapSearch = (Button) findViewById(R.id.btnMapSearch);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, productCollection);
        expListView.setAdapter(expListAdapter);

        // long press on group item and pop up dialog box
        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    // fetch name of the group from the group position
                    String groupName = groupList.get(groupPosition);
                    // fetch pid of the selected product
                   final String productPid = productPids.get(groupPosition);
                   final String productIdnum = productIdnums.get(groupPosition);
                    // int childPosition = ExpandableListView.getPackedPositionChild(id);

                    final AlertDialog show = new AlertDialog.Builder(UserAllProductsExpandableList.this)
                            .setTitle("Map Request")
                            .setMessage("Are you sure you want to find this item '" + groupName + "' on map?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // stream data to php to let udoo take it
                                    uploadPid(productPid);
                                    Intent intent = new Intent(getApplicationContext(), MapTileOverlay.class);
                                    intent.putExtra("key",productIdnum);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.drawable.huskybitslogonull)
                            .show();

                    // Return true as we are handling the event.
                    return true;
                }

                return false;
            }
        });



    }



    public void createGroupList() {
        groupList = new ArrayList<String>();
        productPids = new ArrayList<String>();
        productIdnums = new ArrayList<String>();

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
                    String pid = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME);

                    groupList.add(name);
                    productPids.add(pid);
                    productIdnums.add(id);



                   /* // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_IDNUM, id);
                    map.put(TAG_NAME, name); */

                    // adding HashList to ArrayList
                    //productsList.add(map);
                }
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

    }

    public void createCollection() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        productCollection = new LinkedHashMap<String, List<String>>();

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
                    String category = c.getString(TAG_CATEGORY);
                    String description = c.getString(TAG_DESCR);

                    String[] productChildDetails = {category ,description};



                    loadChild(productChildDetails);
                    productCollection.put(name, childList);

                }
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
        /*
        // preparing laptops collection(child)
        String[] hpModels = { "HP Pavilion G6-2014TX", "ProBook HP 4540",
                "HP Envy 4-1025TX" };
        String[] hclModels = { "HCL S2101", "HCL L2102", "HCL V2002" };
        String[] lenovoModels = { "IdeaPad Z Series", "Essential G Series",
                "ThinkPad X Series", "Ideapad Z Series" };
        String[] sonyModels = { "VAIO E Series", "VAIO Z Series",
                "VAIO S Series", "VAIO YB Series" };
        String[] dellModels = { "Inspiron", "Vostro", "XPS" };
        String[] samsungModels = { "NP Series", "Series 5", "SF Series" };

        productCollection = new LinkedHashMap<String, List<String>>();

        for (String laptop : groupList) {
            if (laptop.equals("HP")) {
                loadChild(hpModels);
            } else if (laptop.equals("Dell"))
                loadChild(dellModels);
            else if (laptop.equals("Sony"))
                loadChild(sonyModels);
            else if (laptop .equals("HCL"))
                loadChild(hclModels);
            else if (laptop.equals("Samsung"))
                loadChild(samsungModels);
            else
                loadChild(lenovoModels);

            productCollection.put(laptop, childList);
        } */
    }

    private void uploadPid(final String productPid) {
        runOnUiThread(new Runnable() {
            public void run() {
                // Check for success tag
                int success;
                try {
                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("pid", productPid));

                    // getting product details by making HTTP request
                    // Note that product details url will use GET request
                    JSONObject json = jParser.makeHttpRequest(
                            url_pid_upload, "GET", params);

                    // check your log for json response
                    Log.d("Single Product Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // successfully received product details

                    }else{
                        // product with idnum not found
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadChild(String[] productChildDetails) {
        childList = new ArrayList<String>();
        for (String details : productChildDetails)
            childList.add(details);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } */
}
