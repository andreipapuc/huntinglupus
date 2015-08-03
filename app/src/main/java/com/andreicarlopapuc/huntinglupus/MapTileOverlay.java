package com.andreicarlopapuc.huntinglupus;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This demonstrates how to add a tile overlay to a map.
 */
public class MapTileOverlay extends FragmentActivity implements OnMapReadyCallback {

    /** This returns moon tiles. */
    private static final String NOD_MAP_URL_FORMAT =
            //"http://www.huntinglupus.esy.es/tilesHighRes/%d/%d/%d.jpg";    //Higher res
            //"http://www.huntinglupus.esy.es/tilesHighRes2/%d/%d/%d.jpg";
            "http://www.huntinglupus.esy.es/tilesLowRes/%d/%d/%d.jpg";

    private static String url_product_details = "http://www.huntinglupus.esy.es/get_product_details.php";

    private static final String TAG_NAME = "name";
    private static final String TAG_IDNUM = "idnum";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    private static final String TAG_Z = "z";

    //String idnum;
    String name;
    float fx;
    float fy;

    private TileOverlay mCustomTiles;
    private GoogleMap map;
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tile_overlay);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        Intent intent = getIntent();
        String valueIdnum = intent.getStringExtra("key");
        getXyz(valueIdnum);
        map.addMarker(new MarkerOptions().position(new LatLng(fx,fy)).title(name));

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                // The moon tile coordinate system is reversed.  This is not normal.
                //int reversedY = (1 << zoom) - y - 1;
                String s = String.format(Locale.US, NOD_MAP_URL_FORMAT, zoom, x, y);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };

        mCustomTiles = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));






    }

    private void getXyz(final String productIdnum) {
        runOnUiThread(new Runnable() {
            public void run() {
                // Check for success tag
                int success;
                try {
                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("idnum", productIdnum));

                    // getting product details by making HTTP request
                    // Note that product details url will use GET request
                    JSONObject json = jParser.makeHttpRequest(
                            url_product_details, "GET", params);

                    // check your log for json response
                    Log.d("Single Product Details", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // successfully received product details
                        JSONArray productObj = json
                                .getJSONArray(TAG_PRODUCT); // JSON Array

                        // get first product object from JSON Array
                        JSONObject c = productObj.getJSONObject(0);
                        name = c.getString(TAG_NAME);
                        String x = c.getString(TAG_X);
                        String y = c.getString(TAG_Y);
                        String z = c.getString(TAG_Z);

                        fx = Float.parseFloat(x);
                        fy = Float.parseFloat(y);




                    }else{
                        // product with idnum not found
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setUpMap(float x, float y , String name) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(name));
    }

    public void setFadeIn(View v) {
        if (mCustomTiles == null) {
            return;
        }
    }
}
